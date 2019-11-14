library(tidyverse)
library(xml2)
library(purrr)
library(openssl)

#load xml files in this directory
nibrsFiles <- list.files('data/', pattern = "\\.xml$", full.names=TRUE)


nibrsDF <- map_df(nibrsFiles, function(f) {
  x <- read_xml(f)
  
  #incidentNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report[normalize-space(nibrs:ReportHeader/nibrs:NIBRSReportCategoryCode)="GROUP A INCIDENT REPORT"]')
  incidentNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report')
  incident <- map2_df(incidentNodes, seq(incidentNodes), function(incidentNode, reportNodeNumber) {
    
    tibble(
      IncidentNumber=incidentNode %>% xml_find_first('nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      ReportCategory=incidentNode %>% xml_find_first('nibrs:ReportHeader/nibrs:NIBRSReportCategoryCode') %>% xml_text(),
      SegmentActionType=incidentNode %>% xml_find_first('nibrs:ReportHeader/nibrs:ReportActionCategoryCode') %>% xml_text(),
      ReportDate=incidentNode %>% xml_find_first('nibrs:ReportHeader/nibrs:ReportDate/nc:YearMonthDate') %>% xml_text(),
      ReportDateIndicator=incidentNode %>% xml_find_first('nc:Incident/cjis:IncidentAugmentation/cjis:IncidentReportDateIndicator') %>% xml_text(),
      ReportingORI=incidentNode %>% xml_find_first('nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification') %>% xml_text(),
      IncidentDate=incidentNode %>% xml_find_first('nc:Incident/nc:ActivityDate/nc:DateTime') %>% xml_text() %>% substr(1,10),
      IncidentHour=incidentNode %>% xml_find_first('nc:Incident/nc:ActivityDate/nc:DateTime') %>% xml_text() %>% substr(12,13),
      CargoTheftIndicator=incidentNode %>% xml_find_first('nc:Incident/cjis:IncidentAugmentation/j:OffenseCargoTheftIndicator') %>% xml_text(),
      ExceptionalClearanceCode=incidentNode %>% xml_find_first('nc:Incident/j:IncidentAugmentation/j:IncidentExceptionalClearanceCode') %>% xml_text(),
      ExceptionalClearanceDate=incidentNode %>% xml_find_first('nc:Incident/j:IncidentAugmenatation/j:IncidentExceptionalClearanceDate/nc:Date') %>% xml_text()
      
    )
    
  })
  
  offenseNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Offense')
  offense <- map2_df(offenseNodes, seq(offenseNodes), function(offenseNode, withinFileIndex) {
    tibble(
      OffenseID=offenseNode %>% xml_find_first('@s:id') %>% xml_text() %>% paste(f),
      IncidentNumber=offenseNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      UCROffenseCode=offenseNode %>% xml_find_first('moibrs:OffenseUCRCode | nibrs:OffenseUCRCode') %>% xml_text(),
      OffenseAttemptedIndicator=offenseNode %>% xml_find_first('j:OffenseAttemptedIndicator') %>% xml_text(),
      OffenderSuspectedOfUsingCode=offenseNode %>% xml_find_all('j:OffenseFactor/j:OffenseFactorCode') %>% xml_text(),
      BiasMotivation=offenseNode %>% xml_find_all('j:OffenseFactorBiasMotivationCode') %>% xml_text(),
      NumberOfPremisesEntered=offenseNode %>% xml_find_first('j:OffenseStructuresEnteredQuantity') %>% xml_text(),
      MethodOfEntry=offenseNode %>% xml_find_first('j:OffenseEntryPoint/j:PassagePointMethodCode') %>% xml_text(),
      TypeCriminalActivityGangInformation=offenseNode %>% xml_find_first('nibrs:CriminalActivityCategoryCode') %>% xml_text(),
      TypeOfWeaponForceInvolved=offenseNode %>% xml_find_all('j:OffenseForce/j:ForceCategoryCode') %>% xml_text()
      
    )
  })
  
  locationNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/nc:Location')
  location <- map2_df(locationNodes, seq(locationNodes), function(locationNode, withinFileIndex) {
    tibble(
      LocationID=locationNode %>% xml_find_first('@s:id') %>% xml_text() %>% paste(f),
      LocationType=locationNode %>% xml_find_first('nibrs:LocationCategoryCode') %>% xml_text()
    )
  })
  
  offenseLocationNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:OffenseLocationAssociation')
  offenseLocation <- map2_df(offenseLocationNodes, seq(offenseLocationNodes), function(offenseLocationNode, withinFileIndex) {
    tibble(
      LocationID=offenseLocationNode %>% xml_find_first('nc:Location/@s:ref') %>% xml_text() %>% paste(f),
      OffenseID=offenseLocationNode %>% xml_find_first('j:Offense/@s:ref') %>% xml_text() %>% paste(f)
    )
  })
  
  propertyNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/nc:Item')
  property <- map2_df(propertyNodes, seq(propertyNodes), function(propertyNode, withinFileIndex) {
    tibble(
      IncidentNumber=propertyNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      TypePropertyLoss=propertyNode %>% xml_find_first('nc:ItemStatus/cjis:ItemStatusCode') %>% xml_text(),
      PropertyDescription=propertyNode %>% xml_find_first('j:ItemCategoryNIBRSPropertyCategoryCode') %>% xml_text(),
      ValueOfProperty=propertyNode %>% xml_find_first('nc:ItemValue/nc:ItemValueAmount/nc:Amount') %>% xml_text(),       
      DateRecovered=propertyNode %>% xml_find_first('nc:Item/nc:ItemValue/nc:ItemValueDate/nc:Date') %>% xml_text()            
    )
  })
 
  victimNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Victim')
  victim <- map2_df(victimNodes, seq(victimNodes), function(victimNode, withinFileIndex) {
    tibble(
      VictimID=victimNode %>% xml_find_first('@s:id') %>% xml_text() %>% paste(f),
      IncidentNumber=victimNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      VictimSequenceNumber=victimNode %>% xml_find_first('j:VictimSequenceNumberText') %>% xml_text(),
      VictimInjury=victimNode %>% xml_find_first('j:VictimInjury/j:InjuryCategoryCode') %>% xml_text(),
      TypeOfVictim=victimNode %>% xml_find_first('j:VictimCategoryCode') %>% xml_text(),     
      VictimAggravatedAssaultHomicideFactor=victimNode %>% xml_find_first('j:VictimAggravatedAssaultHomicideFactorCode') %>% xml_text(),    
      VictimJustifiableHomicideFactor=victimNode %>% xml_find_first('j:VictimJustifiableHomicideFactorCode') %>% xml_text()
    )
  })
  
  victimOffenseNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:OffenseVictimAssociation')
  victimOffense <- map2_df(victimOffenseNodes, seq(victimOffenseNodes), function(victimOffenseNode, withinFileIndex) {
    tibble(
      VictimID=victimOffenseNode %>% xml_find_first('j:Victim/@s:ref') %>% xml_text() %>% paste(f),
      OffenseID=victimOffenseNode %>% xml_find_first('j:Offense/@s:ref') %>% xml_text() %>% paste(f)
    )
  })
  
  incident %>% 
    left_join(offense, by="IncidentNumber") %>% 
    left_join(offenseLocation, by="OffenseID") %>% 
    left_join(victim, by="OffenseID") %>% 
    left_join(location, by="LocationID") %>% 
    left_join(property, by="IncidentNumber")
#    select (-c(LocationID, OffenseID))
})