library(tidyverse)
library(xml2)
library(purrr)


nibrsxml <- read_xml("data/nibrs1.xml")
nibrsns <- xml_ns(nibrsxml)

offenseNodes <- nibrsxml %>% xml_find_first('//j:Offense')

#######
nibrsFiles <- list.files('data/', full.names=TRUE)

nibrsReports <- map_df(nibrsFiles, function(f) {
  
  x <- read_xml(f)
  
#  statusDate <- ymd(gsub(x=basename(f), pattern='CJIS_(.+)\\.xml', replacement='\\1'))
  
  ##create offense data frame
  ## can't just do //j:Offense since it'll pick up the assoc elements
  offenseNodes <- nibrsxml %>% xml_find_all('//j:Offense[../nc:Incident/nc:ActivityIdentification/nc:IdentificationID]')
 
  map2_df(offenseNodes, seq(offenseNodes), function(offenseNode, withinFileIndex) {
    
    tibble(
      IncidentID=offenseNode %>% xml_find_first('../nc:Incident/nc:ActivityIdentification/nc:IdentificationID') %>% xml_text(),
      OffenseUCRCode=offenseNode %>% xml_find_first('moibrs:OffenseUCRCode') %>% xml_text(),
      CriminalActivityCode=offenseNode %>% xml_find_first('nibrs:CriminalActivityCategoryCode') %>% xml_text(),
      BiasMotivation=offenseNode %>% xml_find_first('j:OffenseFactorBiasMotivationCode') %>% xml_text(),
      OffenseFactorCode=offenseNode %>% xml_find_first('j:OffenseFactor/j:OffenseFactorCode') %>% xml_text(),
      OffenseForceCategoryCode=offenseNode %>% xml_find_first('j:OffenseForce/j:ForceCategoryCode') %>% xml_text(),
      OffenseAttemptedIndiator=offenseNode %>% xml_find_first('j:OffenseAttemptedIndicator') %>% xml_text(),
      WithinFileIndex=withinFileIndex
    )
    
  })
  
})

saveRDS(probationCases, 'probationCases.rds')