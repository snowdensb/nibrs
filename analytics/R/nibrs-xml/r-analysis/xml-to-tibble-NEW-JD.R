library(tidyverse)
library(xml2)
library(purrr)
library(openssl)
library(lubridate)

#load xml files in this directory
nibrsFiles <- list.files('data/', pattern = "\\.xml$", full.names=TRUE)

x <-read_xml(nibrsFiles)

## LEVEL 1 - ADMINISTRATIVE SEGMENT
incidentNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report')
  
  incidentNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report')
  incident <- map2_df(incidentNodes, seq(incidentNodes), function(incidentNode, reportNodeNumber) {
    
    tibble(
      SegmentLevel="1",
      SegmentActionType=incidentNode %>% xml_find_first('nibrs:ReportHeader/nibrs:ReportActionCategoryCode') %>% xml_text(),
      ORI=incidentNode %>% xml_find_first('nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID') %>% xml_text(),
      IncidentNumber=incidentNode %>% xml_find_first('nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      # Check with Diane on how to handle
      ReportDateIndicator=incidentNode %>% xml_find_first('nc:Incident/cjis:IncidentAugmentation/cjis:IncidentReportDateIndicator') %>% xml_text(),
      IncidentDate=incidentNode %>% xml_find_first('nc:Incident/nc:ActivityDate/nc:DateTime') %>% xml_text() %>% substr(1,10),
      IncidentHour=incidentNode %>% xml_find_first('nc:Incident/nc:ActivityDate/nc:DateTime') %>% xml_text() %>% substr(12,13),
      CargoTheftIndicator=incidentNode %>% xml_find_first('nc:Incident/cjis:IncidentAugmentation/j:OffenseCargoTheftIndicator') %>% xml_text(),
      ExceptionalClearanceCode=incidentNode %>% xml_find_first('nc:Incident/j:IncidentAugmentation/j:IncidentExceptionalClearanceCode') %>% xml_text(),
      ExceptionalClearanceDate=incidentNode %>% xml_find_first('nc:Incident/j:IncidentAugmentation/j:IncidentExceptionalClearanceDate/nc:Date') %>% xml_text()

    )
  })

## LEVEL 2 - OFFENSE SEGMENT   
  offenseNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Offense')
  offense <- map2_df(offenseNodes, seq(offenseNodes), function(offenseNode, withinFileIndex) {
    tibble(
      SegmentLevel="2",
      SegmentActionType=offenseNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportActionCategoryCode') %>% xml_text(),
      ORI=offenseNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID') %>% xml_text(),
      OffenseID=offenseNode %>% xml_find_first('@s:id') %>% xml_text(),
      IncidentNumber=offenseNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      UCROffenseCode=offenseNode %>% xml_find_first('nibrs:OffenseUCRCode') %>% xml_text(),
#      UCROffenseCode=offenseNode %>% xml_find_first('moibrs:OffenseUCRCode | nibrs:OffenseUCRCode') %>% xml_text(),
      OffenseAttemptedIndicator=offenseNode %>% xml_find_first('j:OffenseAttemptedIndicator') %>% xml_text(),
      OffenderSuspectedOfUsingCode=offenseNode %>% xml_find_all('j:OffenseFactor/j:OffenseFactorCode') %>% xml_text(),
      NumberOfPremisesEntered=offenseNode %>% xml_find_first('j:OffenseStructuresEnteredQuantity') %>% xml_text(),
      MethodOfEntry=offenseNode %>% xml_find_first('j:OffenseEntryPoint/j:PassagePointMethodCode') %>% xml_text(),
      TypeCriminalActivityGangInformation=offenseNode %>% xml_find_all('nibrs:CriminalActivityCategoryCode') %>% xml_text(),
      TypeOfWeaponForceInvolved=offenseNode %>% xml_find_all('j:OffenseForce/j:ForceCategoryCode') %>% xml_text(),
      AutomaticWeaponIndicator="",
      BiasMotivation=offenseNode %>% xml_find_all('j:OffenseFactorBiasMotivationCode') %>% xml_text()
    )
  })
  
  locationNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/nc:Location')
  location <- map2_df(locationNodes, seq(locationNodes), function(locationNode, withinFileIndex) {
    tibble(
      LocationID=locationNode %>% xml_find_first('@s:id') %>% xml_text(),
      LocationType=locationNode %>% xml_find_first('nibrs:LocationCategoryCode') %>% xml_text()
    )
  })
  
  ## LEVEL 3 - PROPERTY SEGMENT   
  propertyNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/nc:Item | /nibrs:Submission/nibrs:Report/nc:Substance')
  property <- map2_df(propertyNodes, seq(propertyNodes), function(propertyNode, withinFileIndex) {
    tibble(
      SegmentLevel="3",
      SegmentActionType=propertyNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportActionCategoryCode') %>% xml_text(),
      ORI=propertyNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID') %>% xml_text(),
      IncidentNumber=propertyNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      TypePropertyLoss=propertyNode %>% xml_find_first('nc:ItemStatus/cjis:ItemStatusCode') %>% xml_text(),
      PropertyDescription=propertyNode %>% xml_find_first('j:ItemCategoryNIBRSPropertyCategoryCode') %>% xml_text(),
      ValueOfProperty=propertyNode %>% xml_find_first('nc:ItemValue/nc:ItemValueAmount/nc:Amount') %>% xml_text(),       
      DateRecovered=propertyNode %>% xml_find_first('nc:ItemValue/nc:ItemValueDate/nc:Date') %>% xml_text(),
      NumberOfStolenMotorVehicles="",
      NumberOfRecoveredMotorVehicles="",
      ItemQuantity=propertyNode %>% xml_find_first('nc:ItemQuantity') %>% xml_text(),
      SuspectedDrugType=propertyNode %>% xml_find_first('j:DrugCategoryCode') %>% xml_text(),
      EstimatedDrugQuantity=propertyNode %>% xml_find_first('nc:SubstanceQuantityMeasure/nc:MeasureDecimalValue') %>% xml_text(),
      TypeDrugMeasurement=propertyNode %>% xml_find_first('nc:SubstanceQuantityMeasure/j:SubstanceUnitCode') %>% xml_text(),
      
    )
  })
  
  
## LEVEL 4 - VICTIM SEGMENT  
  victimNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Victim')
  victim <- map2_df(victimNodes, seq(victimNodes), function(victimNode, withinFileIndex) {
    tibble(
      SegmentLevel="4",
      SegmentActionType=victimNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportActionCategoryCode') %>% xml_text(),
      ORI=victimNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID') %>% xml_text(),
      VictimID=victimNode %>% xml_find_first('@s:id') %>% xml_text(),
      VictimSequenceNumber=victimNode %>% xml_find_first('j:VictimSequenceNumberText') %>% xml_text(),
      TypeOfVictim=victimNode %>% xml_find_first('j:VictimCategoryCode') %>% xml_text(),     
      VictimAggravatedAssaultHomicideCircumstance=victimNode %>% xml_find_all('j:VictimAggravatedAssaultHomicideFactorCode') %>% xml_text(),      
      VictimJustifiableHomicideCircumstance=victimNode %>% xml_find_first('j:VictimJustifiableHomicideFactorCode') %>% xml_text(),  
      VictimInjuryType=victimNode %>% xml_find_all('j:VictimInjury/j:InjuryCategoryCode') %>% xml_text()
     
  
    )
  })

  victimOffenseNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:OffenseVictimAssociation')
  victimOffense <- map2_df(victimOffenseNodes, seq(victimOffenseNodes), function(victimOffenseNode, withinFileIndex) {
    tibble(
      IncidentNumber=victimOffenseNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      VictimID=victimOffenseNode %>% xml_find_first('j:Victim/@s:ref') %>% xml_text(),
      OffenseID=victimOffenseNode %>% xml_find_first('j:Offense/@s:ref') %>% xml_text()
    )
  }) 
  
  
  
  
  
    
  victimPersonNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/nc:Person')
  victimPerson <- map2_df(victimPersonNodes, seq(victimPersonNodes), function(victimPersonNode, withinFileIndex) {
    tibble(
      RoleOfVictimID=victimPersonNode  %>% xml_find_first('@s:id') %>% xml_text(),
      AgeOfVictim=victimPersonNode %>% xml_find_first('nc:PersonAgeMeasure/nc:MeasureIntegerValue') %>% xml_text(),
      SexOfVictim=victimPersonNode %>% xml_find_first('j:PersonSexCode') %>% xml_text(),
      RaceOfVictim=victimPersonNode %>% xml_find_first('j:PersonRaceNDExCode') %>% xml_text(),
      EthnicityOfVictim=victimPersonNode %>% xml_find_first('j:PersonEthnicityCode') %>% xml_text(),
      ResidentStatusOfVictim=victimPersonNode %>% xml_find_first('j:PersonResidentCode') %>% xml_text() 
    )
  })

  victimLEONodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:EnforcementOfficial')
  victimLEO <- map2_df(victimLEONodes, seq(victimLEONodes), function(victimLEONode, withinFileIndex) {
    tibble(
    
    RoleOfVictimID=victimLEONode %>% xml_find_first('nc:RoleOfPerson/@s:ref') %>% xml_text(),
    TypeOfOfficerActivity=victimLEONode %>% xml_find_first('j:EnforcementOfficialActivityCategoryCode') %>% xml_text(),
    OfficerAssignmentType=victimLEONode %>% xml_find_first('j:EnforcementOfficialAssignmentCategoryCode') %>% xml_text(),
    OfficerORIOtherJurisdiction=victimLEONode %>% xml_find_first('j:EnforcementOfficialUnit/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID') %>% xml_text()      
      
    )
  }) 
  
  roleOfVictimNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Victim')
  roleOfVictim <- map2_df(roleOfVictimNodes, seq(roleOfVictimNodes), function(roleOfVictimNode, withinFileIndex) {
    tibble(
      VictimID=roleOfVictimNode %>% xml_find_first('@s:id') %>% xml_text(),
      RoleOfVictimID=roleOfVictimNode %>% xml_find_first('nc:RoleOfPerson/@s:ref') %>% xml_text()
    )
  })    

  ## LEVEL 5 - OFFENDER SEGMENT 
  roleOfOffenderNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Subject')
  roleOfOffender <- map2_df(roleOfOffenderNodes, seq(roleOfOffenderNodes), function(roleOfOffenderNode, withinFileIndex) {
    tibble(
      SegmentLevel="5",
      SegmentActionType=roleOfOffenderNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportActionCategoryCode') %>% xml_text(),
      ORI=roleOfOffenderNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID') %>% xml_text(),
      IncidentNumber=roleOfOffenderNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      OffenderID=roleOfOffenderNode %>% xml_find_first('@s:id') %>% xml_text(),
      RoleOfOffenderID=roleOfOffenderNode %>% xml_find_first('nc:RoleOfPerson/@s:ref') %>% xml_text(),
      OffenderSequenceNumber=roleOfOffenderNode %>% xml_find_first('j:SubjectSequenceNumberText') %>% xml_text()
    )
  }) 
 
  offenderPersonNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/nc:Person')
  offenderPerson <- map2_df(offenderPersonNodes, seq(offenderPersonNodes), function(offenderPersonNode, withinFileIndex) {
    tibble(
      RoleOfOffenderID=offenderPersonNode  %>% xml_find_first('@s:id') %>% xml_text(),
      AgeOfOffender=offenderPersonNode %>% xml_find_first('nc:PersonAgeMeasure/nc:MeasureIntegerRange/nc:RangeMaximumIntegerValue') %>% xml_text(),
      SexOfOffender=offenderPersonNode %>% xml_find_first('j:PersonSexCode') %>% xml_text(),
      RaceOfOffender=offenderPersonNode %>% xml_find_first('j:PersonRaceNDExCode') %>% xml_text(),
      EthnicityOfOffender=offenderPersonNode %>% xml_find_first('j:PersonEthnicityCode') %>% xml_text(),
    )
  })  
  
## LEVEL 6 - ARREST SEGMENT   
  arrestNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Arrest')
  arrest <- map2_df(arrestNodes, seq(arrestNodes), function(arrestNode, withinFileIndex) {
    tibble(
      SegmentLevel="6",
      SegmentActionType=arrestNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportActionCategoryCode') %>% xml_text(),
      ORI=arrestNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID') %>% xml_text(),
      IncidentNumber=arrestNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      ArrestID=arrestNode %>% xml_find_first('@s:id') %>% xml_text(),
      ArrestTransactionNumber=arrestNode %>% xml_find_all('nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      ArrestDate=arrestNode %>% xml_find_all('nc:ActivityDate/nc:Date') %>% xml_text(),
      TypeOfArrest=arrestNode %>% xml_find_all('j:ArrestCategoryCode') %>% xml_text(),
      UCRArrestOffenseCode=arrestNode %>% xml_find_all('j:ArrestCharge/nibrs:ChargeUCRCode') %>% xml_text()
      
    )
  })
  
  arresteeNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Arrestee')
  arrestee <- map2_df(arresteeNodes, seq(arresteeNodes), function(arresteeNode, withinFileIndex) {
    tibble(
      ArresteeID=arresteeNode %>% xml_find_first('@s:id') %>% xml_text(),
      RoleOfArresteeID=arresteeNode %>% xml_find_first('nc:RoleOfPerson/@s:ref') %>% xml_text(),
      ArresteeSequenceNumber=arresteeNode %>% xml_find_first('j:ArrestSequenceID') %>% xml_text(),
      DispositionOfArresteeUnder18=arresteeNode %>% xml_find_first('j:ArresteeJuvenileDispositionCode') %>% xml_text(),     
      MultipleArresteeSegmentsIndicator=arresteeNode %>% xml_find_first('j:ArrestSubjectCountCode') %>% xml_text(), 
      ArresteeArmedWithCode=arresteeNode %>% xml_find_all('j:ArresteeArmedWithCode') %>% xml_text()
      
    )
  })  

  arresteePersonNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/nc:Person')
  arresteePerson <- map2_df(arresteePersonNodes, seq(arresteePersonNodes), function(arresteePersonNode, withinFileIndex) {
    tibble(
      RoleOfArresteeID=arresteePersonNode  %>% xml_find_first('@s:id') %>% xml_text(),
      AgeOfArrestee=arresteePersonNode %>% xml_find_first('nc:PersonAgeMeasure/nc:MeasureIntegerValue') %>% xml_text(),
      SexOfArrestee=arresteePersonNode %>% xml_find_first('j:PersonSexCode') %>% xml_text(),
      RaceOfArrestee=arresteePersonNode %>% xml_find_first('j:PersonRaceNDExCode') %>% xml_text(),
      EthnicityOfArrestee=arresteePersonNode %>% xml_find_first('j:PersonEthnicityCode') %>% xml_text(),
      ResidentStatusOfArrestee=arresteePersonNode %>% xml_find_first('j:PersonResidentCode') %>% xml_text(),
    )
  }) 
  
  ## ASSOCIATIONS  
  offenseLocationNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:OffenseLocationAssociation')
  offenseLocation <- map2_df(offenseLocationNodes, seq(offenseLocationNodes), function(offenseLocationNode, withinFileIndex) {
    tibble(
      LocationID=offenseLocationNode %>% xml_find_first('nc:Location/@s:ref') %>% xml_text(),
      OffenseID=offenseLocationNode %>% xml_find_first('j:Offense/@s:ref') %>% xml_text()
    )
  })  
  
  victimOffenseNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:OffenseVictimAssociation')
  victimOffense <- map2_df(victimOffenseNodes, seq(victimOffenseNodes), function(victimOffenseNode, withinFileIndex) {
    tibble(
      IncidentNumber=victimOffenseNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      VictimID=victimOffenseNode %>% xml_find_first('j:Victim/@s:ref') %>% xml_text(),
      OffenseID=victimOffenseNode %>% xml_find_first('j:Offense/@s:ref') %>% xml_text()
    )
  }) 

  arrestSubjectNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:ArrestSubjectAssociation')
  arrestSubject <- map2_df(arrestSubjectNodes, seq(arrestSubjectNodes), function(arrestSubjectNode, withinFileIndex) {
    tibble(
      ArrestID=arrestSubjectNode %>% xml_find_first('nc:Activity/@s:ref') %>% xml_text(),
      ArresteeID=arrestSubjectNode %>% xml_find_first('j:Subject/@s:ref') %>% xml_text()
    )
  }) 
  
  subjectVictimNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:SubjectVictimAssociation')
  subjectVictim <- map2_df(subjectVictimNodes, seq(subjectVictimNodes), function(subjectVictimNode, withinFileIndex) {
    tibble(
      VictimID=subjectVictimNode %>% xml_find_first('j:Victim/@s:ref') %>% xml_text(),
      OffenderID=subjectVictimNode %>% xml_find_first('j:Subject/@s:ref') %>% xml_text(),
      VictimConnectedToUCROffense=subjectVictimNode %>% xml_find_all('../j:Offense[@s:id=//j:OffenseVictimAssociation[j:Victim/@s:ref= //j:Victim/@s:id]//j:Offense/@s:ref]/nibrs:OffenseUCRCode') %>% xml_text(),
      OffenderNumberToBeRelated=subjectVictimNode %>% xml_find_first('../j:Subject/j:SubjectSequenceNumberText') %>% xml_text(),	  
      RelationshipOfVictimToOffender=subjectVictimNode %>% xml_find_first('nibrs:VictimToSubjectRelationshipCode') %>% xml_text() 
      
    )
  })    
  

NIBRS <- setNames(data.frame(matrix(ncol =68, nrow = 0)),
 c("Segment Length",
            "SegmentLevel",
            "SegmentActionType",
            "MonthOfSubmission",
            "YearOfSubmission",
            "CityIndicator",
            "ORI",
            "IncidentNumber",
            "IncidentDate",
            "ReportDateIndicator",
            "IncidentHour",
            "ExceptionalClearanceCode",
            "ExceptionalClearanceDate",
            "CargoTheftIndicator",
            "UCROffenseCode",
            "OffenseAttemptedIndicator",
            "OffenderSuspectedOfUsingCode",
            "LocationType",
            "NumberOfPremisesEntered",
            "MethodOfEntry",
            "TypeCriminalActivityGangInformation", 
            "TypeOfWeaponForceInvolved",
            "AutomaticWeaponIndicator",
            "BiasMotivation",
            "TypePropertyLoss",
            "PropertyDescription",
            "ValueOfProperty",
            "DateRecovered",
            "NumberOfStolenMotorVehicles",
            "NumberOfRecoveredMotorVehicles",                                               
            "SuspectedDrugType",
            "EstimatedDrugQuantity",
            "TypeDrugMeasurement",                                       
            "VictimSequenceNumber",
            "VictimConnectedToUCROffense",
            "TypeOfVictim",                                              
            "AgeOfVictim",
            "SexOfVictim",
            "RaceOfVictim",
            "EthnicityOfVictim", 
            "ResidentStatusOfVictim",
            "VictimAggravatedAssaultHomicideCircumstance",
            "VictimJustifiableHomicideCircumstance",                                                   
            "VictimInjuryType",
            "OffenderNumberToBeRelated",                  
            "RelationshipOfVictimToOffender",
            "TypeOfOfficerActivity",
            "OfficerAssignmentType",
            "OfficerORIOtherJurisdiction",
            "OffenderSequenceNumber",
            "AgeOfOffender",
            "SexOfOffender",
            "RaceOfOffender", 
            "EthnicityOfOffender",
            "ArresteeSequenceNumber",
            "ArrestTransactionNumber",
            "ArrestDate",
            "TypeOfArrest",
            "MultipleArresteeSegmentsIndicator",
            "UCRArrestOffenseCode",
            "ArresteeArmedWithCode",
            "AutomaticWeaponIndicator",
            "AgeOfArrestee",
            "SexOfArrestee",
            "RaceOfArrestee",
            "EthnicityOfArrestee",
            "ResidentStatusOfArrestee",  
            "DispositionOfArresteeUnder18"
   ))

INCIDENT  <- incident %>%
  mutate(`CargoTheftIndicator`=case_when(
    `CargoTheftIndicator` == "false" ~ 'NA',
    `CargoTheftIndicator` == "true"  ~ 'Y'))

OFFENSE  <- offense %>% 
  left_join(offenseLocation, by="OffenseID") %>% 
  left_join(location, by="LocationID") %>%
mutate(`OffenseAttemptedIndicator`=case_when(
  `OffenseAttemptedIndicator` == "false" ~ 'C',
  `OffenseAttemptedIndicator` == "true"  ~ 'A')) %>%
mutate(.,AutomaticWeaponIndicator = with(.,case_when(
    (str_detect(TypeOfWeaponForceInvolved, "A")) ~ "A"
  )))

PROPERTY  <- property %>%
  mutate(.,NumberOfStolenMotorVehicles = with(.,case_when(
    (str_detect(TypePropertyLoss, "STOLEN")) ~ ItemQuantity
  ))) %>%

  mutate(.,NumberOfRecoveredMotorVehicles = with(.,case_when(
  (str_detect(TypePropertyLoss, "RECOVERED")) ~ ItemQuantity
  ))) %>%

  select (-c(ItemQuantity))


VICTIM  <- victimOffense %>% 
  left_join(victim, by="VictimID") %>%
  left_join(roleOfVictim, by="VictimID") %>% 
  left_join(victimPerson, by="RoleOfVictimID") %>% 
  left_join(subjectVictim, by="VictimID", "OffenderID")
# How to handle when df is empty (e.g., no "enforcement", no "arrest", etc)
#  left_join(victimLEO, by="RoleOfVictimID")


OFFENDER <- roleOfOffender %>% 
  left_join(offenderPerson, by="RoleOfOffenderID")
  
ARREST <- arrest %>% 
  left_join(arrestSubject, by="ArrestID") %>% 
  left_join(arrestee, by="ArresteeID") %>%
  left_join(arresteePerson, by="RoleOfArresteeID")

INCIDENT$IncidentDate <- str_remove_all(INCIDENT$IncidentDate, "-")
INCIDENT$ExceptionalClearanceDate <- str_remove_all(INCIDENT$ExceptionalClearanceDate, "-")
PROPERTY$DateRecovered <- str_remove_all(PROPERTY$DateRecovered, "-")
ARREST$ArrestDate <- str_remove_all(ARREST$ArrestDate, "-")

NIBRS <-bind_rows (NIBRS, INCIDENT, OFFENSE, PROPERTY, VICTIM, OFFENDER, ARREST) %>%
  select (-c(LocationID, OffenseID, OffenderID, VictimID, ArresteeID, ArrestID, RoleOfVictimID, RoleOfOffenderID, RoleOfArresteeID)) %>%
  mutate_all(na_if,"")

write_csv(NIBRS, "NIBRS.csv")

