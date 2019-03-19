# todo: remove once done with iterative development
# but update createKeyFromDate with new code to handle nulls
# directory <- '/tmp/nibrs-cde/WA'
#
# createKeyFromDate <- function(d) {
#   ret <- as.integer(format(d, "%Y%m%d"))
#   case_when(is.na(ret) ~ 99998L, TRUE ~ ret)
# }
#
# spreadsheetFile <- 'inst/raw/NIBRSCodeTables.xlsx'
# conn <- NULL
# end todo

# and uncomment the following
#spreadsheetFile <- system.file("raw", "NIBRSCodeTables.xlsx", package=getPackageName())
# end uncomment

#' @import stringr
#' @import dplyr
#' @import readr
#' @importFrom lubridate dmy
#' @export
loadCDEDataToStaging <- function(directory, codeTableList=NULL) {

  if (is.null(codeTableList)) {
    writeLines('Loading code tables')
    ret <- loadCodeTables(quiet=TRUE)
  } else {
    ret <- c(list(), codeTableList)
  }

  writeLines('Loading agencies')
  # we spell out the cols for agencies.csv because there are columns with illegal names ("0")
  agencyDf <- read_csv(file.path(directory, 'agencies.csv'),
                       skip=1,
                       col_types='-iicc-----cccc--cc------ci---------------------------------',
                       col_names=c('AGENCY_ID','DATA_YEAR','ORI','LEGACY_ORI','UCR_AGENCY_NAME','NCIC_AGENCY_NAME','PUB_AGENCY_NAME','PUB_AGENCY_UNIT','STATE_NAME','STATE_ABBR','AGENCY_TYPE_NAME','POPULATION'))

  writeLines(paste0('Loaded ', nrow(agencyDf), ' Agency records'))

  ret$AdministrativeSegment <- loadCDEIncidentData(ret, agencyDf, directory)
  ret <- c(ret, loadCDEOffenseData(ret, directory))
  ret <- c(ret, loadCDEArresteeData(ret, agencyDf, directory))
  ret$AdministrativeSegment <- ret$AdministrativeSegment %>% anti_join(ret$ArrestReportSegment, by=c('AdministrativeSegmentID'='ArrestReportSegmentID'))
  ret$OffenderSegment <- loadCDEOffenderData(ret, directory)

  ret$AgencyType <- bind_rows(ret$AgencyType,
                              tibble(AgencyTypeID=100, StateCode='100', StateDescription='Other') %>% mutate(NIBRSCode=StateCode, NIBRSDescription=StateDescription))

  ret$Agency <- agencyDf %>%
    rename(AgencyID=AGENCY_ID,
           AgencyORI=ORI,
           AgencyName=NCIC_AGENCY_NAME,
           Population=POPULATION,
           StateCode=STATE_ABBR,
           StateName=STATE_NAME) %>%
    mutate(AGENCY_TYPE_NAME=case_when(
      AGENCY_TYPE_NAME=='Tribal' ~ 'Tribal agencies',
      AGENCY_TYPE_NAME=='Other State Agency' ~ 'Other state agencies',
      AGENCY_TYPE_NAME=='University or College' ~ 'University or college',
      TRUE ~ AGENCY_TYPE_NAME
    )) %>%
    left_join(ret$AgencyType %>% select(NIBRSDescription, AgencyTypeID), by=c('AGENCY_TYPE_NAME'='NIBRSDescription')) %>%
    select(AgencyID, AgencyORI, AgencyName, AgencyTypeID, StateCode, StateName, Population)

  ret

}

loadCDEOffenderData <- function(tableList, directory) {

  writeLines('Loading Offender Segment data')

  raceCt <- buildRaceLookup(tableList, directory)
  ethnicityCt <- buildEthnicityLookup(tableList, directory)
  ageCt <- buildAgeCodeLookup(tableList, directory)

  tdf <- read_csv(file.path(directory, 'NIBRS_OFFENDER.csv'), col_types=cols(.default=col_character()), progress=FALSE) %>%
    mutate_at(vars(matches('_ID$|_NUM$')), as.integer) %>%
    rename(
      AgeOfOffenderMin=AGE_RANGE_LOW_NUM,
      AgeOfOffenderMax=AGE_RANGE_HIGH_NUM) %>%
    mutate(
      SegmentActionTypeTypeID=99998L,
      SEX_CODE=case_when(is.na(SEX_CODE) ~ ' ', TRUE ~ SEX_CODE),
    ) %>%
    left_join(raceCt, by='RACE_ID') %>%
    left_join(ethnicityCt, by='ETHNICITY_ID') %>%
    left_join(tableList$SexOfPersonType %>% select(NIBRSCode, SexOfPersonTypeID), by=c('SEX_CODE'='NIBRSCode')) %>%
    left_join(ageCt, by='AGE_ID') %>%
    mutate(
      EthnicityOfPersonTypeID=case_when(is.na(EthnicityOfPersonTypeID) ~ 99998L, TRUE ~ EthnicityOfPersonTypeID),
      RaceOfPersonTypeID=case_when(is.na(RaceOfPersonTypeID) ~ 99998L, TRUE ~ RaceOfPersonTypeID),
    ) %>%
    select(OffenderSegmentID=OFFENDER_ID,
           AdministrativeSegmentID=INCIDENT_ID,
           OffenderSequenceNumber=OFFENDER_SEQ_NUM,
           starts_with('AgeOfOffender'),
           NonNumericAge,
           ends_with('OfPersonTypeID'))

  writeLines(paste0('Loaded ', nrow(tdf), ' Offender Segment records'))

  tdf

}

loadCDEArresteeData <- function(tableList, agencyDf, directory) {

  writeLines('Loading Arrestee Segment data')

  arrestTypeCt <- read_csv(file.path(directory, 'NIBRS_ARREST_TYPE.csv'), col_types='icc', progress=FALSE) %>%
    select(ARREST_TYPE_ID, ARREST_TYPE_CODE) %>%
    inner_join(tableList$TypeOfArrestType, by=c('ARREST_TYPE_CODE'='NIBRSCode')) %>%
    select(ARREST_TYPE_ID, TypeOfArrestTypeID)

  raceCt <- buildRaceLookup(tableList, directory)
  ethnicityCt <- buildEthnicityLookup(tableList, directory)
  ageCt <- buildAgeCodeLookup(tableList, directory)
  offenseTypeCt <- buildOffenseTypeLookup(tableList, directory, TRUE)

  tdf <- read_csv(file.path(directory, 'NIBRS_ARRESTEE.csv'), col_types=cols(.default=col_character()), progress=FALSE) %>%
    mutate_at(vars(matches('_ID$|_NUM$')), as.integer) %>%
    rename(
      ArresteeSequenceNumber=ARRESTEE_SEQ_NUM,
      AgeOfArresteeMin=AGE_RANGE_LOW_NUM,
      AgeOfArresteeMax=AGE_RANGE_HIGH_NUM) %>%
    mutate(
      SegmentActionTypeTypeID=99998L,
      ArrestTransactionNumber=str_pad(ARRESTEE_ID, 12, 'left', '0'),
      SEX_CODE=case_when(is.na(SEX_CODE) ~ ' ', TRUE ~ SEX_CODE),
      RESIDENT_CODE=case_when(is.na(RESIDENT_CODE) ~ ' ', TRUE ~ RESIDENT_CODE),
      UNDER_18_DISPOSITION_CODE=case_when(is.na(UNDER_18_DISPOSITION_CODE) ~ ' ', TRUE ~ UNDER_18_DISPOSITION_CODE),
      MULTIPLE_INDICATOR=case_when(is.na(MULTIPLE_INDICATOR) ~ ' ', TRUE ~ MULTIPLE_INDICATOR),
      ArrestDate=dmy(ARREST_DATE),
      ArrestDateID=createKeyFromDate(ArrestDate)
    ) %>%
    left_join(offenseTypeCt, by='OFFENSE_TYPE_ID') %>%
    left_join(raceCt, by='RACE_ID') %>%
    left_join(ethnicityCt, by='ETHNICITY_ID') %>%
    left_join(arrestTypeCt, by='ARREST_TYPE_ID') %>%
    left_join(tableList$SexOfPersonType %>% select(NIBRSCode, SexOfPersonTypeID), by=c('SEX_CODE'='NIBRSCode')) %>%
    left_join(tableList$ResidentStatusOfPersonType %>% select(NIBRSCode, ResidentStatusOfPersonTypeID), by=c('RESIDENT_CODE'='NIBRSCode')) %>%
    left_join(tableList$DispositionOfArresteeUnder18Type %>% select(NIBRSCode, DispositionOfArresteeUnder18TypeID), by=c('UNDER_18_DISPOSITION_CODE'='NIBRSCode')) %>%
    left_join(tableList$MultipleArresteeSegmentsIndicatorType %>% select(NIBRSCode, MultipleArresteeSegmentsIndicatorTypeID), by=c('MULTIPLE_INDICATOR'='NIBRSCode')) %>%
    left_join(ageCt, by='AGE_ID') %>%
    mutate(
      TypeOfArrestTypeID=case_when(is.na(TypeOfArrestTypeID) ~ 99998L, TRUE ~ TypeOfArrestTypeID),
      EthnicityOfPersonTypeID=case_when(is.na(EthnicityOfPersonTypeID) ~ 99998L, TRUE ~ EthnicityOfPersonTypeID),
      RaceOfPersonTypeID=case_when(is.na(RaceOfPersonTypeID) ~ 99998L, TRUE ~ RaceOfPersonTypeID),
      UCROffenseCodeTypeID=case_when(is.na(UCROffenseCodeTypeID) ~ 99998L, TRUE ~ UCROffenseCodeTypeID),
    )

  weaponCt <- read_csv(file.path(directory, 'NIBRS_WEAPON_TYPE.csv'), col_types='ic--', progress=FALSE) %>%
    mutate(WEAPON_ID=as.integer(WEAPON_ID))

  weaponDf <- read_csv(file.path(directory, 'NIBRS_ARRESTEE_WEAPON.csv'), col_types=cols(.default=col_character()), progress=FALSE) %>%
    mutate_at(vars(matches('_ID$')), as.integer) %>%
    select(WEAPON_ID, ARRESTEE_ID) %>%
    left_join(weaponCt, by='WEAPON_ID') %>%
    mutate(WEAPON_CODE=case_when(is.na(WEAPON_CODE) ~ ' ', WEAPON_CODE=='95' ~ '99', TRUE ~ WEAPON_CODE),
           AutomaticWeaponIndicator=str_sub(WEAPON_CODE, 3, 3), WEAPON_CODE=str_sub(WEAPON_CODE, 1, 2)) %>%
    left_join(tableList$ArresteeWasArmedWithType %>% select(ArresteeWasArmedWithTypeID, NIBRSCode), by=c('WEAPON_CODE'='NIBRSCode')) %>%
    select(ARRESTEE_ID, ArresteeWasArmedWithTypeID, AutomaticWeaponIndicator)

  ret <- list()

  writeLines('Separating Group A and Group B segment data')

  ret$ArresteeSegment <- tdf %>%
    filter(OffenseCategory1=='Group A') %>% select(-OffenseCategory1) %>%
    select(ArresteeSegmentID=ARRESTEE_ID,
           SegmentActionTypeTypeID,
           AdministrativeSegmentID=INCIDENT_ID,
           ArresteeSequenceNumber,
           ArrestTransactionNumber,
           starts_with('ArrestDate'),
           TypeOfArrestTypeID,
           MultipleArresteeSegmentsIndicatorTypeID,
           starts_with('AgeOfArrestee'),
           NonNumericAge,
           ends_with('OfPersonTypeID'),
           DispositionOfArresteeUnder18TypeID,
           UCROffenseCodeTypeID)

  ret$ArresteeSegmentWasArmedWith <- weaponDf %>%
    semi_join(ret$ArresteeSegment, by=c('ARRESTEE_ID'='ArresteeSegmentID')) %>%
    rename(ArresteeSegmentID=ARRESTEE_ID)

  ret$ArrestReportSegment <- tdf %>%
    filter(OffenseCategory1=='Group B') %>% select(-OffenseCategory1) %>%
    inner_join(tableList$AdministrativeSegment %>% select(AdministrativeSegmentID, AgencyID, ORI), by=c('INCIDENT_ID'='AdministrativeSegmentID')) %>%
    select(ArrestReportSegmentID=ARRESTEE_ID,
           SegmentActionTypeTypeID,
           AgencyID, ORI,
           ArrestTransactionNumber,
           ArresteeSequenceNumber,
           starts_with('ArrestDate'),
           TypeOfArrestTypeID,
           MultipleArresteeSegmentsIndicatorTypeID,
           starts_with('AgeOfArrestee'),
           NonNumericAge,
           ends_with('OfPersonTypeID'),
           DispositionOfArresteeUnder18TypeID,
           UCROffenseCodeTypeID)

  ret$ArrestReportSegmentWasArmedWith <- weaponDf %>%
    semi_join(ret$ArrestReportSegment, by=c('ARRESTEE_ID'='ArrestReportSegmentID')) %>%
    rename(ArrestReportSegmentID=ARRESTEE_ID)

  writeLines(paste0('Loaded ', nrow(ret$ArresteeSegment), ' Group A Arrestee Segment records'))
  writeLines(paste0('Loaded ', nrow(ret$ArrestReportSegment), ' Group B Arrest Report Segment records'))

  ret

}

buildRaceLookup <- function(tableList, directory) {
  read_csv(file.path(directory, 'REF_RACE.csv'), col_types='ic-----') %>%
    select(RACE_ID, RACE_CODE) %>%
    inner_join(tableList$RaceOfPersonType, by=c('RACE_CODE'='NIBRSCode')) %>%
    select(RACE_ID, RaceOfPersonTypeID)
}

buildEthnicityLookup <- function(tableList, directory) {
  read_csv(file.path(directory, 'NIBRS_ETHNICITY.csv'), col_types='icc') %>%
    select(ETHNICITY_ID, ETHNICITY_CODE) %>%
    inner_join(tableList$EthnicityOfPersonType, by=c('ETHNICITY_CODE'='NIBRSCode')) %>%
    select(ETHNICITY_ID, EthnicityOfPersonTypeID)
}

buildAgeCodeLookup <- function(tableList, directory) {
  read_csv(file.path(directory, 'NIBRS_AGE.csv'), col_types='icc') %>%
    select(AGE_ID, AGE_CODE) %>% filter(AGE_CODE %in% c('NN','NB','BB')) %>%
    rename(NonNumericAge=AGE_CODE)
}

buildOffenseTypeLookup <- function(tableList, directory, includeGroupIndicatorField=FALSE) {
  ret <- read_csv(file.path(directory, 'NIBRS_OFFENSE_TYPE.csv'), col_types='ic-------') %>%
    select(OFFENSE_TYPE_ID, OFFENSE_CODE) %>%
    inner_join(tableList$UCROffenseCodeType, by=c('OFFENSE_CODE'='NIBRSCode')) %>%
    select(OFFENSE_TYPE_ID, UCROffenseCodeTypeID, OffenseCategory1)
  if (!includeGroupIndicatorField) {
    ret <- ret %>% select(-OffenseCategory1)
  }
  ret
}

loadCDEOffenseData <- function(tableList, directory) {

  writeLines('Loading Offense Segment data')

  ret <- list()

  locationTypeCt <- read_csv(file.path(directory, 'NIBRS_LOCATION_TYPE.csv'), col_types='icc', progress=FALSE) %>%
    select(LOCATION_ID, LOCATION_CODE) %>%
    inner_join(tableList$LocationTypeType, by=c('LOCATION_CODE'='NIBRSCode')) %>%
    select(LOCATION_ID, LocationTypeTypeID)

  offenseTypeCt <- buildOffenseTypeLookup(tableList, directory)

  tdf <- read_csv(file.path(directory, 'NIBRS_OFFENSE.csv'), col_types=cols(.default=col_character()), progress=FALSE) %>%
    mutate_at(vars(ends_with('_ID')), as.integer) %>%
    mutate(NumberOfPremisesEntered=as.integer(NUM_PREMISES_ENTERED),
           SegmentActionTypeTypeID=99998L,
           OffenseSegmentID=OFFENSE_ID, AdministrativeSegmentID=INCIDENT_ID,
           OffenseAttemptedCompleted=ATTEMPT_COMPLETE_FLAG,
           METHOD_ENTRY_CODE=case_when(is.na(METHOD_ENTRY_CODE) ~ ' ', TRUE ~ METHOD_ENTRY_CODE)) %>%
    left_join(locationTypeCt, by='LOCATION_ID') %>%
    left_join(offenseTypeCt, by='OFFENSE_TYPE_ID') %>%
    left_join(tableList$MethodOfEntryType %>% select(NIBRSCode, MethodOfEntryTypeID), by=c('METHOD_ENTRY_CODE'='NIBRSCode')) %>%
    mutate(
      LocationTypeTypeID=case_when(is.na(LocationTypeTypeID) ~ 99998L, TRUE ~ LocationTypeTypeID),
      UCROffenseCodeTypeID=case_when(is.na(UCROffenseCodeTypeID) ~ 99998L, TRUE ~ UCROffenseCodeTypeID),
    ) %>%
    select(OffenseSegmentID, SegmentActionTypeTypeID, AdministrativeSegmentID, UCROffenseCodeTypeID, OffenseAttemptedCompleted, LocationTypeTypeID,
           NumberOfPremisesEntered, MethodOfEntryTypeID)

  ret$OffenseSegment <- tdf

  writeLines(paste0('Loaded ', nrow(ret$OffenseSegment), ' Offense Segment records'))

  ct <- read_csv(file.path(directory, 'NIBRS_BIAS_LIST.csv'), col_types='ic--', progress=FALSE) %>%
    select(BIAS_ID, BIAS_CODE) %>%
    inner_join(tableList$BiasMotivationType, by=c('BIAS_CODE'='NIBRSCode')) %>%
    select(BIAS_ID, BiasMotivationTypeID)

  tdf <- read_csv(file.path(directory, 'NIBRS_BIAS_MOTIVATION.csv'), col_types=cols(.default=col_character()), progress=FALSE) %>%
    mutate_at(vars(ends_with('_ID')), as.integer) %>%
    left_join(ct, by='BIAS_ID') %>%
    select(OffenseSegmentID=OFFENSE_ID, BiasMotivationTypeID)

  ret$BiasMotivation <- tdf

  ct <- read_csv(file.path(directory, 'NIBRS_CRIMINAL_ACT_TYPE.csv'), col_types='ic--', progress=FALSE) %>%
    select(CRIMINAL_ACT_ID, CRIMINAL_ACT_CODE) %>%
    inner_join(tableList$TypeOfCriminalActivityType, by=c('CRIMINAL_ACT_CODE'='NIBRSCode')) %>%
    select(CRIMINAL_ACT_ID, TypeOfCriminalActivityTypeID)

  tdf <- read_csv(file.path(directory, 'NIBRS_CRIMINAL_ACT.csv'), col_types=cols(.default=col_character()), progress=FALSE) %>%
    mutate_at(vars(ends_with('_ID')), as.integer) %>%
    left_join(ct, by='CRIMINAL_ACT_ID') %>%
    select(OffenseSegmentID=OFFENSE_ID, TypeOfCriminalActivityTypeID)

  ret$TypeCriminalActivity <- tdf

  ct <- read_csv(file.path(directory, 'NIBRS_USING_LIST.csv'), col_types='ic-', progress=FALSE) %>%
    select(SUSPECT_USING_ID, SUSPECT_USING_CODE) %>%
    inner_join(tableList$OffenderSuspectedOfUsingType, by=c('SUSPECT_USING_CODE'='NIBRSCode')) %>%
    select(SUSPECT_USING_ID, OffenderSuspectedOfUsingTypeID)

  tdf <- read_csv(file.path(directory, 'NIBRS_SUSPECT_USING.csv'), col_types=cols(.default=col_character()), progress=FALSE) %>%
    mutate_at(vars(ends_with('_ID')), as.integer) %>%
    left_join(ct, by='SUSPECT_USING_ID') %>%
    select(OffenseSegmentID=OFFENSE_ID, OffenderSuspectedOfUsingTypeID)

  ret$OffenderSuspectedOfUsing <- tdf

  weaponCt <- read_csv(file.path(directory, 'NIBRS_WEAPON_TYPE.csv'), col_types='ic--', progress=FALSE) %>%
    mutate(WEAPON_ID=as.integer(WEAPON_ID))

  ret$TypeOfWeaponForceInvolved <- read_csv(file.path(directory, 'NIBRS_WEAPON.csv'), col_types=cols(.default=col_character()), progress=FALSE) %>%
    mutate_at(vars(matches('_ID$')), as.integer) %>%
    select(WEAPON_ID, OFFENSE_ID) %>%
    left_join(weaponCt, by='WEAPON_ID') %>%
    mutate(WEAPON_CODE=case_when(is.na(WEAPON_CODE) ~ ' ', TRUE ~ WEAPON_CODE),
           AutomaticWeaponIndicator=str_sub(WEAPON_CODE, 3, 3), WEAPON_CODE=str_sub(WEAPON_CODE, 1, 2)) %>%
    left_join(tableList$TypeOfWeaponForceInvolvedType %>% select(TypeOfWeaponForceInvolvedTypeID, NIBRSCode), by=c('WEAPON_CODE'='NIBRSCode')) %>%
    select(OffenseSegmentID=OFFENSE_ID, TypeOfWeaponForceInvolvedTypeID, AutomaticWeaponIndicator)

  ret

}

loadCDEIncidentData <- function(tableList, agencyDf, directory) {

  writeLines('Loading Administrative Segment data')

  exceptionalClearanceCt <- read_csv(file.path(directory, 'NIBRS_CLEARED_EXCEPT.csv'), col_types='iccc', progress=FALSE) %>%
    select(CLEARED_EXCEPT_ID, CLEARED_EXCEPT_CODE) %>%
    inner_join(tableList$ClearedExceptionallyType, by=c('CLEARED_EXCEPT_CODE'='NIBRSCode')) %>%
    select(CLEARED_EXCEPT_ID, ClearedExceptionallyTypeID)

  ret <- read_csv(file.path(directory, 'NIBRS_incident.csv'), col_types=cols(.default=col_character()), progress=FALSE) %>%
    mutate_at(vars(ends_with('_ID')), as.integer) %>%
    inner_join(agencyDf %>% select(ORI, AGENCY_ID), by='AGENCY_ID') %>%
    rename(AgencyID=AGENCY_ID,
           IncidentHour=INCIDENT_HOUR,
           ReportDateIndicator=REPORT_DATE_FLAG) %>%
    mutate(IncidentNumber=str_pad(INCIDENT_ID, 12, 'left', '0'),
           SegmentActionTypeTypeID=99998L,
           IncidentDate=dmy(INCIDENT_DATE),
           ExceptionalClearanceDate=dmy(CLEARED_EXCEPT_DATE),
           IncidentDateID=createKeyFromDate(IncidentDate),
           ExceptionalClearanceDateID=createKeyFromDate(ExceptionalClearanceDate),
           CARGO_THEFT_FLAG=case_when(is.na(CARGO_THEFT_FLAG) ~ ' ', TRUE ~ CARGO_THEFT_FLAG)) %>%
    left_join(exceptionalClearanceCt, by='CLEARED_EXCEPT_ID') %>%
    left_join(tableList$CargoTheftIndicatorType %>% select(NIBRSCode, CargoTheftIndicatorTypeID), by=c('CARGO_THEFT_FLAG'='NIBRSCode')) %>%
    select(AdministrativeSegmentID=INCIDENT_ID, SegmentActionTypeTypeID, ORI, AgencyID, IncidentNumber,
           IncidentDate, IncidentDateID, ReportDateIndicator, IncidentHour, ClearedExceptionallyTypeID, ExceptionalClearanceDate, ExceptionalClearanceDateID, CargoTheftIndicatorTypeID)

  writeLines(paste0('Loaded ', nrow(ret), ' Administrative Segment records'))

  ret

}
