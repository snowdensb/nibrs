library(tidyverse)
library(dplyr)
library(lubridate)
library(tibble)
library(data.table)

#Merge all "txt" nibrs files in the /data directory into file nibrs.txt used by this script 
#system("./flatfile.sh")

# Vector of segment number for each row in the flatfile
segments <- read_fwf("./data/nibrs/nibrs.txt", 
           fwf_positions( c(5),c(5), ))

# NIBRS flatfile
NIBRS <- read_fwf("./data/nibrs/nibrs.txt", 
                  fwf_positions( 
                    c(1),
                    c(300),
                    col_names = c("NIBRS") ))

### BUILD THE FUNCTION BEFORE RUNNING THE FOLLOWING COMMANDS
# Create an R dataframe (nibrsFlat)
nibrsFlat <- FlatfileToNIBRS (segments, NIBRS)
# Remove the first 6 columns 
nibrsFlat <- nibrsFlat[-c(1:6)]
# Write dataframe as a .csv
write.csv(nibrsFlat, "nibrsFlat.csv", row.names=FALSE, quote = FALSE)
###


# Convert NIBRS Flatfile to R DF FUNCTION
FlatfileToNIBRS <- function(segments, NIBRS) {

# Define variable for the number of rows in the nibrs flatfile
rows <- nrow(NIBRS)

# Initialize dataframe
emptydf <- data.frame()

#### NIBRS Segment 1 - ADMINISTRATIVE
segment_1 <- emptydf
for(i in 1:rows) {
  ifelse(segments[i,1] == 1, segment_1[i,1] <- NIBRS[i,1], "")
}
segment_1 <- na.omit(segment_1) 
write.table(segment_1, file = "data/temp/segment_1.txt", quote = FALSE,
            row.names = FALSE)
original_nibrs_segment_1 <- read_fwf("data/temp/segment_1.txt", skip = 1, 
                                     fwf_positions( 
                                       c(1,5,6,7,9,13,17,26,38,46,47,49,50,88),
                                       c(4,5,6,8,12,16,25,37,45,46,48,49,57,88),
                                       col_names = c("SegmentLength","SegmentLevel","SegmentActionType","MonthOfSubmission",
                                                     "YearOfSubmission","CityIndicator","ORI","IncidentNumber","IncidentDate",
                                                     "ReportDateIndicator","IncidentHour","ExceptionalClearanceCode",
                                                     "ExceptionalClearanceDate", "CargoTheftIndicator") ))

####
#### NIBRS Segment 2 - OFFENSE
segment_2 <- emptydf
for(i in 1:rows) {
  ifelse(segments[i,1] == 2, segment_2[i,1] <- NIBRS[i,1], "")
}
segment_2 <- na.omit(segment_2) 
write.table(segment_2, file = "data/temp/segment_2.txt", quote = FALSE,
            row.names = FALSE)
original_nibrs_segment_2 <- read_fwf("data/temp/segment_2.txt", skip = 1, 
                                     fwf_positions( 
                                       c(1,5,6,7,9,13,17,26,38,41,42,43,44,45,47,49,50,51,52,53,55,56,58,59,61,62,64,66,68,70),
                                       c(4,5,6,8,12,16,25,37,40,41,42,43,44,46,48,49,50,51,52,54,55,57,58,60,61,63,65,67,69,71),
                                       col_names = c("SegmentLength","SegmentLevel","SegmentActionType","MonthOfSubmission",
                                                     "YearOfSubmission","CityIndicator","ORI","IncidentNumber","UCROffenseCode",
                                                     "OffenseAttemptedIndicator","OffenderSuspectedOfUsingCode #1","OffenderSuspectedOfUsingCode #2",
                                                     "OffenderSuspectedOfUsingCode #3","LocationType",
                                                     "NumberOfPremisesEntered", "MethodOfEntry", 
                                                     "TypeCriminalActivityGangInformation #1", 
                                                     "TypeCriminalActivityGangInformation #2",
                                                     "TypeCriminalActivityGangInformation #3", 
                                                     "TypeOfWeaponForceInvolved #1", 
                                                     "AutomaticWeaponIndicator #1",
                                                     "TypeOfWeaponForceInvolved #2", 
                                                     "AutomaticWeaponIndicator #2", 
                                                     "TypeOfWeaponForceInvolved #3",
                                                     "AutomaticWeaponIndicator #3", 
                                                     "BiasMotivation #1", 
                                                     "BiasMotivation #2", 
                                                     "BiasMotivation #3",
                                                     "BiasMotivation #4", 
                                                     "BiasMotivation #5") ))

## MODIFIED - REMOVE MULTIPLES
nibrs <- original_nibrs_segment_2
offenderSuspecting2 <-  nibrs %>% 
  filter(`OffenderSuspectedOfUsingCode #2` != "NA") %>%
  mutate(`OffenderSuspectedOfUsingCode #1` = `OffenderSuspectedOfUsingCode #2`)

offenderSuspecting3 <-  nibrs %>% 
  filter(`OffenderSuspectedOfUsingCode #3` != "NA") %>%
  mutate(`OffenderSuspectedOfUsingCode #1` = `OffenderSuspectedOfUsingCode #3`)

typeCriminalActivity2 <-  nibrs %>% 
  filter(`TypeCriminalActivityGangInformation #2` != "NA") %>%
  mutate(`TypeCriminalActivityGangInformation #1` = `TypeCriminalActivityGangInformation #2`)

typeCriminalActivity3 <-  nibrs %>% 
  filter(`TypeCriminalActivityGangInformation #3` != "NA") %>%
  mutate(`TypeCriminalActivityGangInformation #1` = `TypeCriminalActivityGangInformation #3`)

typeWeaponForce2 <-  nibrs %>% 
  filter(`TypeOfWeaponForceInvolved #2` != "NA") %>%
  mutate(`TypeOfWeaponForceInvolved #1` = `TypeOfWeaponForceInvolved #2`) %>%
  mutate(`AutomaticWeaponIndicator #1` = `AutomaticWeaponIndicator #2`)

typeWeaponForce3 <-  nibrs %>% 
  filter(`TypeOfWeaponForceInvolved #3` != "NA") %>%
  mutate(`TypeOfWeaponForceInvolved #1` = `TypeOfWeaponForceInvolved #3`) %>%
  mutate(`AutomaticWeaponIndicator #1` = `AutomaticWeaponIndicator #3`)

biasMotivation2 <-  nibrs %>% 
  filter(`BiasMotivation #2` != "NA") %>%
  mutate(`BiasMotivation #1` = `BiasMotivation #2`)

biasMotivation3 <-  nibrs %>% 
  filter(`BiasMotivation #3` != "NA") %>%
  mutate(`BiasMotivation #1` = `BiasMotivation #3`)

biasMotivation4 <-  nibrs %>% 
  filter(`BiasMotivation #4` != "NA") %>%
  mutate(`BiasMotivation #1` = `BiasMotivation #4`)

biasMotivation5 <-  nibrs %>% 
  filter(`BiasMotivation #5` != "NA") %>%
  mutate(`BiasMotivation #1` = `BiasMotivation #5`)

nibrs_segment_2_modified <- rbind(nibrs,
                                  offenderSuspecting2,
                                  offenderSuspecting3,
                                  typeCriminalActivity2,
                                  typeCriminalActivity3,
                                  typeWeaponForce2,
                                  typeWeaponForce3,
                                  biasMotivation2,
                                  biasMotivation3,
                                  biasMotivation4,
                                  biasMotivation5)

nibrs_segment_2_modified <- nibrs_segment_2_modified[-c(12,13,18,19,22,23,24,25,27,28,29,30)] %>%
  
  rename(
    OffenderSuspectedOfUsingCode = "OffenderSuspectedOfUsingCode #1",
    TypeCriminalActivityGangInformation = "TypeCriminalActivityGangInformation #1",
    TypeOfWeaponForceInvolved = "TypeOfWeaponForceInvolved #1",
    AutomaticWeaponIndicator = "AutomaticWeaponIndicator #1",
    BiasMotivation = "BiasMotivation #1",
  )  

####
#### NIBRS Segment 3 - PROPERTY
segment_3 <- emptydf
for(i in 1:rows) {
  ifelse(segments[i,1] == 3, segment_3[i,1] <- NIBRS[i,1], "")
}
segment_3 <- na.omit(segment_3) 
write.table(segment_3, file = "data/temp/segment_3.txt", quote = FALSE,
            row.names = FALSE)
original_nibrs_segment_3 <- read_fwf("data/temp/segment_3.txt", skip = 1, 
                                     fwf_positions( 
                                       c(1,5,6,7,9,13,17,26,38,39,41,50,58,60,69,77,79,88,96,98,107,115,117,126,134,136,145,153,155,164,172,174,183,191,193,202,210,212,221,229,231,233,234,246,248,249,261,263,264,276),
                                       c(4,5,6,8,12,16,25,37,38,40,49,57,59,68,76,78,87,95,97,106,114,116,125,133,135,144,152,154,163,171,173,182,190,192,201,209,211,220,228,230,232,233,245,247,248,260,262,263,275,277),
                                       col_names = c("SegmentLength","SegmentLevel","SegmentActionType","MonthOfSubmission",
                                                     "YearOfSubmission","CityIndicator","ORI","IncidentNumber", "TypePropertyLoss",
                                                     
                                                     "PropertyDescription #1", 
                                                     "ValueOfProperty #1", 
                                                     "DateRecovered #1",
                                                     
                                                     "PropertyDescription #2", 
                                                     "ValueOfProperty #2", 
                                                     "DateRecovered #2",
                                                     
                                                     "PropertyDescription #3", 
                                                     "ValueOfProperty #3", 
                                                     "DateRecovered #3",
                                                     
                                                     "PropertyDescription #4", 
                                                     "ValueOfProperty #4", 
                                                     "DateRecovered #4",
                                                     
                                                     "PropertyDescription #5", 
                                                     "ValueOfProperty #5", 
                                                     "DateRecovered #5",
                                                     
                                                     "PropertyDescription #6", 
                                                     "ValueOfProperty #6", 
                                                     "DateRecovered #6", 
                                                     
                                                     "PropertyDescription #7", 
                                                     "ValueOfProperty #7", 
                                                     "DateRecovered #7",   
                                                     
                                                     "PropertyDescription #8", 
                                                     "ValueOfProperty #8", 
                                                     "DateRecovered #8",  
                                                     
                                                     "PropertyDescription #9", 
                                                     "ValueOfProperty #9", 
                                                     "DateRecovered #9",
                                                     
                                                     "PropertyDescription #10", 
                                                     "ValueOfProperty #10", 
                                                     "DateRecovered #10",
                                                     
                                                     "NumberOfStolenMotorVehicles",
                                                     "NumberOfRecoveredMotorVehicles",
                                                     
                                                     "SuspectedDrugType #1",     
                                                     "EstimatedDrugQuantity #1", 
                                                     "TypeDrugMeasurement #1",                                                      
                                                     
                                                     "SuspectedDrugType #2",     
                                                     "EstimatedDrugQuantity #2", 
                                                     "TypeDrugMeasurement #2", 
                                                     
                                                     "SuspectedDrugType #3",     
                                                     "EstimatedDrugQuantity #3", 
                                                     "TypeDrugMeasurement #3") ))

## MODIFIED - REMOVE MULTIPLES
nibrs <- original_nibrs_segment_3

property2 <-  nibrs %>% 
  filter(`PropertyDescription #2` != "NA") %>%
  mutate(`PropertyDescription #1` = `PropertyDescription #2`) %>%
  mutate(`ValueOfProperty #1` = `ValueOfProperty #2`) %>%
  mutate(`DateRecovered #1` = `DateRecovered #2`)

property3 <-  nibrs %>% 
  filter(`PropertyDescription #3` != "NA") %>%
  mutate(`PropertyDescription #1` = `PropertyDescription #3`) %>%
  mutate(`ValueOfProperty #1` = `ValueOfProperty #3`) %>%
  mutate(`DateRecovered #1` = `DateRecovered #3`)

property4 <-  nibrs %>% 
  filter(`PropertyDescription #4` != "NA") %>%
  mutate(`PropertyDescription #1` = `PropertyDescription #4`) %>%
  mutate(`ValueOfProperty #1` = `ValueOfProperty #4`) %>%
  mutate(`DateRecovered #1` = `DateRecovered #4`)

property5 <-  nibrs %>% 
  filter(`PropertyDescription #5` != "NA") %>%
  mutate(`PropertyDescription #1` = `PropertyDescription #5`) %>%
  mutate(`ValueOfProperty #1` = `ValueOfProperty #5`) %>%
  mutate(`DateRecovered #1` = `DateRecovered #5`)

property6 <-  nibrs %>% 
  filter(`PropertyDescription #6` != "NA") %>%
  mutate(`PropertyDescription #1` = `PropertyDescription #6`) %>%
  mutate(`ValueOfProperty #1` = `ValueOfProperty #6`) %>%
  mutate(`DateRecovered #1` = `DateRecovered #6`)

property7 <-  nibrs %>% 
  filter(`PropertyDescription #7` != "NA") %>%
  mutate(`PropertyDescription #1` = `PropertyDescription #7`) %>%
  mutate(`ValueOfProperty #1` = `ValueOfProperty #7`) %>%
  mutate(`DateRecovered #1` = `DateRecovered #7`)

property8 <-  nibrs %>% 
  filter(`PropertyDescription #8` != "NA") %>%
  mutate(`PropertyDescription #1` = `PropertyDescription #8`) %>%
  mutate(`ValueOfProperty #1` = `ValueOfProperty #8`) %>%
  mutate(`DateRecovered #1` = `DateRecovered #8`)

property9 <-  nibrs %>% 
  filter(`PropertyDescription #9` != "NA") %>%
  mutate(`PropertyDescription #1` = `PropertyDescription #9`) %>%
  mutate(`ValueOfProperty #1` = `ValueOfProperty #9`) %>%
  mutate(`DateRecovered #1` = `DateRecovered #9`)

property10 <-  nibrs %>% 
  filter(`PropertyDescription #10` != "NA") %>%
  mutate(`PropertyDescription #1` = `PropertyDescription #10`) %>%
  mutate(`ValueOfProperty #1` = `ValueOfProperty #10`) %>%
  mutate(`DateRecovered #1` = `DateRecovered #10`)

drug2 <-  nibrs %>% 
  filter(`SuspectedDrugType #2` != "NA") %>%
  mutate(`SuspectedDrugType #1` = `SuspectedDrugType #2`) %>%
  mutate(`EstimatedDrugQuantity #1` = `EstimatedDrugQuantity #2`) %>%
  mutate(`TypeDrugMeasurement #1` = `TypeDrugMeasurement #2`)

drug3 <-  nibrs %>% 
  filter(`SuspectedDrugType #3` != "NA") %>%
  mutate(`SuspectedDrugType #1` = `SuspectedDrugType #3`) %>%
  mutate(`EstimatedDrugQuantity #1` = `EstimatedDrugQuantity #3`) %>%
  mutate(`TypeDrugMeasurement #1` = `TypeDrugMeasurement #3`)

nibrs_segment_3_modified <- rbind(nibrs,
                                  property2,
                                  property3,                
                                  property4,
                                  property5,
                                  property6,
                                  property7,
                                  property8,                
                                  property9,
                                  property10,
                                  drug2,
                                  drug3)

nibrs_segment_3_modified <- nibrs_segment_3_modified[-c(13:39,45:50)] %>%
  
  rename(
    PropertyDescription = "PropertyDescription #1",
    ValueOfProperty = "ValueOfProperty #1",
    DateRecovered = "DateRecovered #1",
    SuspectedDrugType = "SuspectedDrugType #1",
    EstimatedDrugQuantity = "EstimatedDrugQuantity #1",
    TypeDrugMeasurement = "TypeDrugMeasurement #1",
  )   

####
#### NIBRS Segment 4 - VICTIM
segment_4 <- emptydf
for(i in 1:rows) {
  ifelse(segments[i,1] == 4, segment_4[i,1] <- NIBRS[i,1], "")
}
segment_4 <- na.omit(segment_4) 
write.table(segment_4, file = "data/temp/segment_4.txt", quote = FALSE,
            row.names = FALSE)
original_nibrs_segment_4 <- read_fwf("data/temp/segment_4.txt", skip = 1, 
                                     fwf_positions( 
                                       c(1,5,6,7,9,13,17,26,38,41,44,47,50,53,56,59,62,65,68,71,72,76,77,78,79,80,82,84,85,86,87,88,89,90,92,94,96,98,100,102,104,106,108,110,112,114,116,118,120,122,124,126,128,130,132,133),
                                       c(4,5,6,8,12,16,25,37,40,43,46,49,52,55,58,61,64,67,70,71,75,76,77,78,79,81,83,84,85,86,87,88,89,91,93,95,97,99,101,103,105,107,109,111,113,115,117,119,121,123,125,127,129,131,132,141),
                                       col_names = c("SegmentLength","SegmentLevel","SegmentActionType","MonthOfSubmission",
                                                     "YearOfSubmission","CityIndicator","ORI","IncidentNumber","VictimSequenceNumber",
                                                     
                                                     "VictimConnectedToUCROffense #1",
                                                     "VictimConnectedToUCROffense #2",
                                                     "VictimConnectedToUCROffense #3",
                                                     "VictimConnectedToUCROffense #4",
                                                     "VictimConnectedToUCROffense #5",
                                                     "VictimConnectedToUCROffense #6",
                                                     "VictimConnectedToUCROffense #7",
                                                     "VictimConnectedToUCROffense #8",
                                                     "VictimConnectedToUCROffense #9",
                                                     "VictimConnectedToUCROffense #10",
                                                     "TypeOfVictim",                                                     
                                                     "AgeOfVictim",  
                                                     "SexOfVictim",
                                                     "RaceOfVictim",  
                                                     "EthnicityOfVictim",     
                                                     "ResidentStatusOfVictim", 
                                                     
                                                     "VictimAggravatedAssaultHomicideCircumstance #1",                                                      
                                                     "VictimAggravatedAssaultHomicideCircumstance #2",                                                     
                                                     
                                                     "VictimJustifiableHomicideCircumstance",                                                        
                                                     
                                                     "VictimInjuryType #1",
                                                     "VictimInjuryType #2",
                                                     "VictimInjuryType #3",
                                                     "VictimInjuryType #4",
                                                     "VictimInjuryType #5",
                                                     
                                                     "OffenderNumberToBeRelated #1",                       
                                                     "RelationshipOfVictimToOffender #1",
                                                     
                                                     "OffenderNumberToBeRelated #2",
                                                     "RelationshipOfVictimToOffender #2",
                                                     
                                                     "OffenderNumberToBeRelated #3",
                                                     "RelationshipOfVictimToOffender #3",                                                     
                                                     
                                                     "OffenderNumberToBeRelated #4",
                                                     "RelationshipOfVictimToOffender #4",  
                                                     
                                                     "OffenderNumberToBeRelated #5",
                                                     "RelationshipOfVictimToOffender #5",      
                                                     
                                                     "OffenderNumberToBeRelated #6",
                                                     "RelationshipOfVictimToOffender #6",                                                          
                                                     
                                                     "OffenderNumberToBeRelated #7",
                                                     "RelationshipOfVictimToOffender #7",
                                                     
                                                     "OffenderNumberToBeRelated #8",
                                                     "RelationshipOfVictimToOffender #8",                                                     
                                                     
                                                     "OffenderNumberToBeRelated #9",
                                                     "RelationshipOfVictimToOffender #9",     
                                                     
                                                     "OffenderNumberToBeRelated #10",
                                                     "RelationshipOfVictimToOffender #10",
                                                     
                                                     "TypeOfOfficerActivity",                                                     
                                                     "OfficerAssignmentType",
   
                                                     "OfficerORIOtherJurisdiction") ))

## MODIFIED - REMOVE MULTIPLES
nibrs <- original_nibrs_segment_4

victimConnected2 <-  nibrs %>% 
  filter(`VictimConnectedToUCROffense #2` != "NA") %>%
  mutate(`VictimConnectedToUCROffense #1` = `VictimConnectedToUCROffense #2`)

victimConnected3 <-  nibrs %>% 
  filter(`VictimConnectedToUCROffense #3` != "NA") %>%
  mutate(`VictimConnectedToUCROffense #1` = `VictimConnectedToUCROffense #3`)  
  
victimConnected4 <-  nibrs %>% 
  filter(`VictimConnectedToUCROffense #4` != "NA") %>%
  mutate(`VictimConnectedToUCROffense #1` = `VictimConnectedToUCROffense #4`)   

victimConnected5 <-  nibrs %>% 
  filter(`VictimConnectedToUCROffense #5` != "NA") %>%
  mutate(`VictimConnectedToUCROffense #1` = `VictimConnectedToUCROffense #5`)   

victimConnected6 <-  nibrs %>% 
  filter(`VictimConnectedToUCROffense #6` != "NA") %>%
  mutate(`VictimConnectedToUCROffense #1` = `VictimConnectedToUCROffense #6`)

victimConnected7 <-  nibrs %>% 
  filter(`VictimConnectedToUCROffense #7` != "NA") %>%
  mutate(`VictimConnectedToUCROffense #1` = `VictimConnectedToUCROffense #7`)  

victimConnected8 <-  nibrs %>% 
  filter(`VictimConnectedToUCROffense #8` != "NA") %>%
  mutate(`VictimConnectedToUCROffense #1` = `VictimConnectedToUCROffense #8`)   

victimConnected9 <-  nibrs %>% 
  filter(`VictimConnectedToUCROffense #9` != "NA") %>%
  mutate(`VictimConnectedToUCROffense #1` = `VictimConnectedToUCROffense #9`)   

victimConnected10 <-  nibrs %>% 
  filter(`VictimConnectedToUCROffense #10` != "NA") %>%
  mutate(`VictimConnectedToUCROffense #1` = `VictimConnectedToUCROffense #10`)   

##

aggravated2 <-  nibrs %>% 
  filter(`VictimAggravatedAssaultHomicideCircumstance #2` != "NA") %>%
  mutate(`VictimAggravatedAssaultHomicideCircumstance #1` = `VictimAggravatedAssaultHomicideCircumstance #2`)   

##

typeInjury2 <-  nibrs %>% 
  filter(`VictimInjuryType #2` != "NA") %>%
  mutate(`VictimInjuryType #1` = `VictimInjuryType #2`)

typeInjury3 <-  nibrs %>% 
  filter(`VictimInjuryType #3` != "NA") %>%
  mutate(`VictimInjuryType #1` = `VictimInjuryType #3`)

typeInjury4 <-  nibrs %>% 
  filter(`VictimInjuryType #4` != "NA") %>%
  mutate(`VictimInjuryType #1` = `VictimInjuryType #4`)

typeInjury5 <-  nibrs %>% 
  filter(`VictimInjuryType #5` != "NA") %>%
  mutate(`VictimInjuryType #1` = `VictimInjuryType #5`)

##

relationship2 <-  nibrs %>% 
  filter(`OffenderNumberToBeRelated #2` != "NA") %>%
  mutate(`OffenderNumberToBeRelated #1` = `OffenderNumberToBeRelated #2`) %>%
  mutate(`RelationshipOfVictimToOffender #1` = `RelationshipOfVictimToOffender #2`)

relationship3 <-  nibrs %>% 
  filter(`OffenderNumberToBeRelated #3` != "NA") %>%
  mutate(`OffenderNumberToBeRelated #1` = `OffenderNumberToBeRelated #3`) %>%
  mutate(`RelationshipOfVictimToOffender #1` = `RelationshipOfVictimToOffender #3`)

relationship4 <-  nibrs %>% 
  filter(`OffenderNumberToBeRelated #4` != "NA") %>%
  mutate(`OffenderNumberToBeRelated #1` = `OffenderNumberToBeRelated #4`) %>%
  mutate(`RelationshipOfVictimToOffender #1` = `RelationshipOfVictimToOffender #4`)

relationship5 <-  nibrs %>% 
  filter(`OffenderNumberToBeRelated #5` != "NA") %>%
  mutate(`OffenderNumberToBeRelated #1` = `OffenderNumberToBeRelated #5`) %>%
  mutate(`RelationshipOfVictimToOffender #1` = `RelationshipOfVictimToOffender #5`)

relationship6 <-  nibrs %>% 
  filter(`OffenderNumberToBeRelated #6` != "NA") %>%
  mutate(`OffenderNumberToBeRelated #1` = `OffenderNumberToBeRelated #6`) %>%
  mutate(`RelationshipOfVictimToOffender #1` = `RelationshipOfVictimToOffender #6`)

relationship7 <-  nibrs %>% 
  filter(`OffenderNumberToBeRelated #7` != "NA") %>%
  mutate(`OffenderNumberToBeRelated #1` = `OffenderNumberToBeRelated #7`) %>%
  mutate(`RelationshipOfVictimToOffender #1` = `RelationshipOfVictimToOffender #7`)

relationship8 <-  nibrs %>% 
  filter(`OffenderNumberToBeRelated #8` != "NA") %>%
  mutate(`OffenderNumberToBeRelated #1` = `OffenderNumberToBeRelated #8`) %>%
  mutate(`RelationshipOfVictimToOffender #1` = `RelationshipOfVictimToOffender #8`)

relationship9 <-  nibrs %>% 
  filter(`OffenderNumberToBeRelated #9` != "NA") %>%
  mutate(`OffenderNumberToBeRelated #1` = `OffenderNumberToBeRelated #9`) %>%
  mutate(`RelationshipOfVictimToOffender #1` = `RelationshipOfVictimToOffender #9`)

relationship10 <-  nibrs %>% 
  filter(`OffenderNumberToBeRelated #10` != "NA") %>%
  mutate(`OffenderNumberToBeRelated #1` = `OffenderNumberToBeRelated #10`) %>%
  mutate(`RelationshipOfVictimToOffender #1` = `RelationshipOfVictimToOffender #10`)

nibrs_segment_4_modified <- rbind(nibrs,
                                  victimConnected2,
                                  victimConnected3,
                                  victimConnected4,
                                  victimConnected5,
                                  victimConnected6,
                                  victimConnected7,
                                  victimConnected8,
                                  victimConnected9,
                                  victimConnected10,
                                  aggravated2,
                                  typeInjury2,
                                  typeInjury3,
                                  typeInjury4,
                                  typeInjury5,
                                  relationship2,
                                  relationship3,
                                  relationship4,
                                  relationship5,
                                  relationship6,
                                  relationship7,
                                  relationship8,
                                  relationship9,
                                  relationship10)

nibrs_segment_4_modified <- nibrs_segment_4_modified[-c(11:19,27,30:33,36:53)] %>%

  rename(
    VictimConnectedToUCROffense = "VictimConnectedToUCROffense #1",
    VictimInjuryType = "VictimInjuryType #1",
    VictimAggravatedAssaultHomicideCircumstance = "VictimAggravatedAssaultHomicideCircumstance #1",
    OffenderNumberToBeRelated = "OffenderNumberToBeRelated #1",
    RelationshipOfVictimToOffender = "RelationshipOfVictimToOffender #1"
  )   
  

####
#### NIBRS Segment 5 - OFFENDER
segment_5 <- emptydf
for(i in 1:rows) {
  ifelse(segments[i,1] == 5, segment_5[i,1] <- NIBRS[i,1], "")
}
segment_5 <- na.omit(segment_5) 
write.table(segment_5, file = "data/temp/segment_5.txt", quote = FALSE,
            row.names = FALSE)
original_nibrs_segment_5 <- read_fwf("data/temp/segment_5.txt", skip = 1, 
                                     fwf_positions( 
                                       c(1,5,6,7,9,13,17,26,38,40,44,45,46),
                                       c(4,5,6,8,12,16,25,37,39,43,44,45,46),
                                       col_names = c("SegmentLength","SegmentLevel","SegmentActionType","MonthOfSubmission",
                                                     "YearOfSubmission","CityIndicator","ORI","IncidentNumber","OffenderSequenceNumber",
                                                     "AgeOfOffender",
                                                     "SexOfOffender", 
                                                     "RaceOfOffender",    
                                                     "EthnicityOfOffender") ))
####
#### NIBRS Segment 6 - ARREST
segment_6 <- emptydf
for(i in 1:rows) {
  ifelse(segments[i,1] == 6, segment_6[i,1] <- NIBRS[i,1], "")
}
segment_6 <- na.omit(segment_6) 
write.table(segment_6, file = "data/temp/segment_6.txt", quote = FALSE, na = "NA",
            row.names = FALSE)

original_nibrs_segment_6 <- read_fwf("data/temp/segment_6.txt", skip = 1, 
                                     fwf_positions( 
                                       c(1,5,6,7,9,13,17,26,38,40,52,60,61,62,65,67,68,70,71,75,76,77,78,79),
                                       c(4,5,6,8,12,16,25,37,39,51,59,60,61,64,66,67,69,70,74,75,76,77,78,79),
                                       col_names = c("SegmentLength","SegmentLevel","SegmentActionType","MonthOfSubmission",
                                                     "YearOfSubmission","CityIndicator","ORI","IncidentNumber","ArresteeSequenceNumber",
                                                     "ArrestTransactionNumber","ArrestDate","TypeOfArrest","MultipleArresteeSegmentsIndicator",
                                                     "UCRArrestOffenseCode",
                                                     
                                                     "ArresteeArmedWithCode #1",
                                                     "AutomaticWeaponIndicator #1",
                                                     
                                                     "ArresteeArmedWithCode #2",                                                     
                                                     "AutomaticWeaponIndicator #2",
                                                     
                                                     "AgeOfArrestee",
                                                     "SexOfArrestee",
                                                     "RaceOfArrestee",
                                                     "EthnicityOfArrestee",
                                                     "ResidentStatusOfArrestee",   
                                                     "DispositionOfArresteeUnder18") ))

## MODIFIED - REMOVE MULTIPLES
nibrs <- original_nibrs_segment_6

armed2 <-  nibrs %>% 
  filter(`ArresteeArmedWithCode #2` != "NA") %>%
  mutate(`ArresteeArmedWithCode #1` = `ArresteeArmedWithCode #2`) %>%
  mutate(`AutomaticWeaponIndicator #1` = `AutomaticWeaponIndicator #2`)

nibrs_segment_6_modified <- rbind(nibrs,
                                  armed2)

nibrs_segment_6_modified <- nibrs_segment_6_modified[-c(17:18)] %>%
  
  rename(
    ArresteeArmedWithCode = "ArresteeArmedWithCode #1",
    AutomaticWeaponIndicator6 = "AutomaticWeaponIndicator #1"
  )  %>%
  
mutate(`TypeOfArrest`=case_when(
  `TypeOfArrest` == "FALSE" ~ 'F',
  `TypeOfArrest` == "TRUE"  ~ 'T'))


##
#Identify Null Segments
null_segment <- data.frame("SegmentLength"= as.character())

if (dim(nibrs_segment_3_modified)[1] == 0) {nibrs_segment_3_modified <- null_segment}
if (dim(nibrs_segment_4_modified)[1] == 0) {nibrs_segment_4_modified <- null_segment}
if (dim(original_nibrs_segment_5)[1] == 0) {original_nibrs_segment_5 <- null_segment}
if (dim(nibrs_segment_6_modified)[1] == 0) {nibrs_segment_6_modified <- null_segment}


## Create R NIBRS DF
nibrsFlat <- list(
  original_nibrs_segment_1,
  nibrs_segment_2_modified,
  nibrs_segment_3_modified,
  nibrs_segment_4_modified,
  original_nibrs_segment_5,
  nibrs_segment_6_modified) %>% 
  reduce(full_join)

return(nibrsFlat)

}





