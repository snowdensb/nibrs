library(tidyverse)
library(xml2)
library(purrr)
library(openssl)
library(lubridate)

### TEST FOR xml2 xml_find_all

##TEST 1 - All "xml_find_all" nodes exist
nibrsFiles <- list.files('data/', pattern = "Test_1.xml", full.names=TRUE)
x <-read_xml(nibrsFiles)

  offenseNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Offense')
  offense <- map2_df(offenseNodes, seq(offenseNodes), function(offenseNode, withinFileIndex) {
    tibble(
      SegmentLevel="2",
      SegmentActionType=offenseNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportActionCategoryCode') %>% xml_text(),
      ORI=offenseNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID') %>% xml_text(),
      OffenseID=offenseNode %>% xml_find_first('@s:id') %>% xml_text(),
      UCROffenseCode=offenseNode %>% xml_find_first('nibrs:OffenseUCRCode') %>% xml_text(),
      OffenderSuspectedOfUsingCode=offenseNode %>% xml_find_all('j:OffenseFactor/j:OffenseFactorCode') %>% xml_text(),
      NumberOfPremisesEntered=offenseNode %>% xml_find_first('j:OffenseStructuresEnteredQuantity') %>% xml_text(),
      MethodOfEntry=offenseNode %>% xml_find_first('j:OffenseEntryPoint/j:PassagePointMethodCode') %>% xml_text()
    )
  })

  ##TEST 2 - "xml_find_all" node OffenderSuspectedOfUsingCode missing (j:OffenseFactor/j:OffenseFactorCode' NOT in XML file)
  nibrsFiles <- list.files('data/', pattern = "Test_2.xml", full.names=TRUE)
  x <-read_xml(nibrsFiles)
  
  offenseNodes <- x %>% xml_find_all('/nibrs:Submission/nibrs:Report/j:Offense')
  offense <- map2_df(offenseNodes, seq(offenseNodes), function(offenseNode, withinFileIndex) {
    tibble(
      SegmentLevel="2",
      SegmentActionType=offenseNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportActionCategoryCode') %>% xml_text(),
      ORI=offenseNode %>% xml_find_first('../nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID') %>% xml_text(),
      OffenseID=offenseNode %>% xml_find_first('@s:id') %>% xml_text(),
      UCROffenseCode=offenseNode %>% xml_find_first('nibrs:OffenseUCRCode') %>% xml_text(),
      OffenderSuspectedOfUsingCode=offenseNode %>% xml_find_all('j:OffenseFactor/j:OffenseFactorCode') %>% xml_text(),
      NumberOfPremisesEntered=offenseNode %>% xml_find_first('j:OffenseStructuresEnteredQuantity') %>% xml_text(),
      MethodOfEntry=offenseNode %>% xml_find_first('j:OffenseEntryPoint/j:PassagePointMethodCode') %>% xml_text()
    )
  })
  
  
  
  
  