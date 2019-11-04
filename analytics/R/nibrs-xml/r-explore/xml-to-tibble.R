library(tidyverse)
library(xml2)
library(purrr)

nibrsFiles <- list.files('data/', full.names=TRUE)
nibrsDF <- map_df(nibrsFiles, function(f) {
  x <- read_xml(f)
  
  incidentNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report')
  i <- map2_df(incidentNodes, seq(incidentNodes), function(incidentNode, withinFileIndex) {
    
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
  o <- map2_df(offenseNodes, seq(offenseNodes), function(offenseNode, withinFileIndex) {
    
    tibble(
      IncidentNumber=offenseNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      UCROffenseCode=offenseNode %>% xml_find_first('moibrs:OffenseUCRCode') %>% xml_text(),
      OffenseAttemptedIndicator=offenseNode %>% xml_find_first('j:OffenseAttemptedIndicator') %>% xml_text(),
      #Offender Suspect of Using?
      BiasMotivation=offenseNode %>% xml_find_first('j:OffenseFactorBiasMotivationCode') %>% xml_text(),
      #LocationType?
      NumberOfPremisesEntered=offenseNode %>% xml_find_first('j:OffenseStructuresEnteredQuantity') %>% xml_text(),
      MethodOfEntry=offenseNode %>% xml_find_first('j:OffenseEntryPoint/j:PassagePointMethodCode') %>% xml_text(),
      TypeCriminalActivityGangInformation=offenseNode %>% xml_find_first('nibrs:CriminalActivityCategoryCode') %>% xml_text(),
      OffenseFactorCode=offenseNode %>% xml_find_first('j:OffenseFactor/j:OffenseFactorCode') %>% xml_text(),
      OffenseForceCategoryCode=offenseNode %>% xml_find_first('j:OffenseForce/j:ForceCategoryCode') %>% xml_text()
    )
    
  })
  
  #join offenses to incident
  i %>% 
    left_join(o, by="IncidentNumber")
})
