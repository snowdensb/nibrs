library(tidyverse)
library(xml2)
library(purrr)


nibrsxml <- read_xml("data/nibrs1.xml")
nibrsns <- xml_ns(nibrsxml)


nibrsOffenseDF <- map_df(nibrsFiles, createNIBRSOffenseDF)
nibrsAdministrativeDF <- map_df(nibrsFiles, createNIBRSAdministrativeDF)

#FUNCTIONS

createNIBRSOffenseDF <- function(f) {
  
  x <- read_xml(f)
  offenseNodes <- nibrsxml %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Offense')
  
  map2_df(offenseNodes, seq(offenseNodes), function(offenseNode, withinFileIndex) {
    
    tibble(
      IncidentID=offenseNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      OffenseUCRCode=offenseNode %>% xml_find_first('moibrs:OffenseUCRCode') %>% xml_text(),
      CriminalActivityCode=offenseNode %>% xml_find_first('nibrs:CriminalActivityCategoryCode') %>% xml_text(),
      BiasMotivation=offenseNode %>% xml_find_first('j:OffenseFactorBiasMotivationCode') %>% xml_text(),
      StructuresEnteredQuantity=offenseNode %>% xml_find_first('j:OffenseStructuresEnteredQuantity') %>% xml_text(),
      OffenseFactorCode=offenseNode %>% xml_find_first('j:OffenseFactor/j:OffenseFactorCode') %>% xml_text(),
      OffenseEntryPoint=offenseNode %>% xml_find_first('j:OffenseEntryPoint/j:PassagePointMethodCode') %>% xml_text(),
      OffenseForceCategoryCode=offenseNode %>% xml_find_first('j:OffenseForce/j:ForceCategoryCode') %>% xml_text(),
      OffenseAttemptedIndiator=offenseNode %>% xml_find_first('j:OffenseAttemptedIndicator') %>% xml_text(),
      #WithinFileIndex=withinFileIndex
    )
    
  })
  
}

createNIBRSAdministrativeDF <- function(f) {
  
  x <- read_xml(f)
  incidentNodes <- nibrsxml %>% xml_find_all('/nibrs:Submission/nibrs:Report')
  
  map2_df(incidentNodes, seq(incidentNodes), function(incidentNode, withinFileIndex) {
    
    tibble(
      IncidentID=incidentNode %>% xml_find_first('nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      ReportCategory=incidentNode %>% xml_find_first('nibrs:ReportHeader/nibrs:NIBRSReportCategoryCode') %>% xml_text(),
      Action=incidentNode %>% xml_find_first('nibrs:ReportHeader/nibrs:ReportActionCategoryCode') %>% xml_text(),
      ReportDate=incidentNode %>% xml_find_first('nibrs:ReportHeader/nibrs:ReportDate/nc:YearMonthDate') %>% xml_text(),
      ReportingORI=incidentNode %>% xml_find_first('nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification') %>% xml_text()
    )
    
  })
  
}
