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
loadCDEDataToStaging <- function(directory) {

  ret <- loadCodeTables()

  # we spell out the cols for agencies.csv because there are columns with illegal names ("0")
  agencyDf <- read_csv(file.path(directory, 'agencies.csv'),
                       skip=1,
                       col_types='-iicc-----cccc--cc------ci---------------------------------',
                       col_names=c('AGENCY_ID','DATA_YEAR','ORI','LEGACY_ORI','UCR_AGENCY_NAME','NCIC_AGENCY_NAME','PUB_AGENCY_NAME','PUB_AGENCY_UNIT','STATE_NAME','STATE_ABBR','AGENCY_TYPE_NAME','POPULATION'))


  ret$AdministrativeSegment <- loadCDEIncidentData(ret, agencyDf, directory)
  ret$OffenseSegment <- loadCDEOffenseData(ret, directory)

  ret <- c(ret, loadCDEArresteeData(ret, agencyDf, directory))
  ret$AdministrativeSegment <- ret$AdministrativeSegment %>% anti_join(ret$ArrestReportSegment, by=c('AdministrativeSegmentID'='ArrestReportSegmentID'))

  ret

}

loadCDEArresteeData <- function(tableList, agencyDf, directory) {

  arrestTypeCt <- read_csv(file.path(directory, 'NIBRS_ARREST_TYPE.csv'), col_types='icc') %>%
    select(ARREST_TYPE_ID, ARREST_TYPE_CODE) %>%
    inner_join(tableList$TypeOfArrestType, by=c('ARREST_TYPE_CODE'='NIBRSCode')) %>%
    select(ARREST_TYPE_ID, TypeOfArrestTypeID)

  raceCt <- read_csv(file.path(directory, 'REF_RACE.csv'), col_types='ic-----') %>%
    select(RACE_ID, RACE_CODE) %>%
    inner_join(tableList$RaceOfPersonType, by=c('RACE_CODE'='NIBRSCode')) %>%
    select(RACE_ID, RaceOfPersonTypeID)

  ethnicityCt <- read_csv(file.path(directory, 'NIBRS_ETHNICITY.csv'), col_types='icc') %>%
    select(ETHNICITY_ID, ETHNICITY_CODE) %>%
    inner_join(tableList$EthnicityOfPersonType, by=c('ETHNICITY_CODE'='NIBRSCode')) %>%
    select(ETHNICITY_ID, EthnicityOfPersonTypeID)

  ageCt <- read_csv(file.path(directory, 'NIBRS_AGE.csv'), col_types='icc') %>%
    select(AGE_ID, AGE_CODE) %>% filter(AGE_CODE %in% c('NN','NB','BB')) %>%
    rename(NonNumericAge=AGE_CODE)

  offenseTypeCt <- buildOffenseTypeLookup(tableList, directory, TRUE)

  tdf <- read_csv(file.path(directory, 'NIBRS_ARRESTEE.csv'), col_types=cols(.default=col_character())) %>%
    mutate_at(vars(matches('_ID$|_NUM$')), as.integer) %>%
    rename(
      ArresteeSequenceNumber=ARRESTEE_SEQ_NUM,
      AgeOfArresteeMin=AGE_RANGE_LOW_NUM,
      AgeOfArresteeMax=AGE_RANGE_HIGH_NUM) %>%
    left_join(offenseTypeCt, by='OFFENSE_TYPE_ID') %>%
    left_join(raceCt, by='RACE_ID') %>%
    left_join(ethnicityCt, by='ETHNICITY_ID') %>%
    left_join(arrestTypeCt, by='ARREST_TYPE_ID') %>%
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
    left_join(tableList$SexOfPersonType %>% select(NIBRSCode, SexOfPersonTypeID), by=c('SEX_CODE'='NIBRSCode')) %>%
    left_join(tableList$ResidentStatusOfPersonType %>% select(NIBRSCode, ResidentStatusOfPersonTypeID), by=c('RESIDENT_CODE'='NIBRSCode')) %>%
    left_join(tableList$DispositionOfArresteeUnder18Type %>% select(NIBRSCode, DispositionOfArresteeUnder18TypeID), by=c('UNDER_18_DISPOSITION_CODE'='NIBRSCode')) %>%
    left_join(tableList$MultipleArresteeSegmentsIndicatorType %>% select(NIBRSCode, MultipleArresteeSegmentsIndicatorTypeID), by=c('MULTIPLE_INDICATOR'='NIBRSCode')) %>%
    left_join(ageCt, by='AGE_ID')

  ret <- list()

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

  ret

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

  locationTypeCt <- read_csv(file.path(directory, 'NIBRS_LOCATION_TYPE.csv'), col_types='icc') %>%
    select(LOCATION_ID, LOCATION_CODE) %>%
    inner_join(tableList$LocationTypeType, by=c('LOCATION_CODE'='NIBRSCode')) %>%
    select(LOCATION_ID, LocationTypeTypeID)

  offenseTypeCt <- buildOffenseTypeLookup(tableList, directory)

  ret <- read_csv(file.path(directory, 'NIBRS_OFFENSE.csv'), col_types=cols(.default=col_character())) %>%
    mutate_at(vars(ends_with('_ID')), as.integer) %>%
    mutate(NumberOfPremisesEntered=as.integer(NUM_PREMISES_ENTERED),
           SegmentActionTypeTypeID=99998L,
           OffenseSegmentID=OFFENSE_ID, AdministrativeSegmentID=INCIDENT_ID,
           OffenseAttemptedCompleted=ATTEMPT_COMPLETE_FLAG,
           METHOD_ENTRY_CODE=case_when(is.na(METHOD_ENTRY_CODE) ~ ' ', TRUE ~ METHOD_ENTRY_CODE)) %>%
    left_join(locationTypeCt, by='LOCATION_ID') %>%
    left_join(offenseTypeCt, by='OFFENSE_TYPE_ID') %>%
    left_join(tableList$MethodOfEntryType %>% select(NIBRSCode, MethodOfEntryTypeID), by=c('METHOD_ENTRY_CODE'='NIBRSCode')) %>%
    select(OffenseSegmentID, SegmentActionTypeTypeID, AdministrativeSegmentID, UCROffenseCodeTypeID, OffenseAttemptedCompleted, LocationTypeTypeID,
           NumberOfPremisesEntered, MethodOfEntryTypeID)

  ret

}

loadCDEIncidentData <- function(tableList, agencyDf, directory) {

  exceptionalClearanceCt <- read_csv(file.path(directory, 'NIBRS_CLEARED_EXCEPT.csv'), col_types='iccc') %>%
    select(CLEARED_EXCEPT_ID, CLEARED_EXCEPT_CODE) %>%
    inner_join(tableList$ClearedExceptionallyType, by=c('CLEARED_EXCEPT_CODE'='NIBRSCode')) %>%
    select(CLEARED_EXCEPT_ID, ClearedExceptionallyTypeID)

  ret <- read_csv(file.path(directory, 'NIBRS_incident.csv'), col_types=cols(.default=col_character())) %>%
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

  ret

}
