library(tidyverse)
library(dplyr)
library(lubridate)
library(tibble)

# Vector of segment number for each row in the flatfile
segments <- read_fwf("data/HIUCR001_20190912_NIBRS_023.txt", 
           fwf_positions( c(5),c(5), ))

# NIBRS flatfile
NIBRS <- read_fwf("data/HIUCR001_20190912_NIBRS_023.txt", 
                  fwf_positions( 
                    c(1),
                    c(300),
                    col_names = c("NIBRS") ))

# Define variable for the number of rows in the nibrs flatfile
rows <- nrow(NIBRS)

# Initialize dataframe
emptydf <- data.frame()

# NIBRS Segment 1 - ADMINISTRATIVE
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
                                       col_names = c("SEGMENT LENGTH","SEGMENT LEVEL","SEGMENT ACTION TYPE","MONTH OF SUBMISSION",
                                                     "YEAR OF SUBMISSION","CITY INDICATOR","ORI","INCIDENT NUMBER","INCIDENT DATE",
                                                     "REPORT DATE INDICATOR","INCIDENT HOUR","CLEARED EXCEPTIONALLY",
                                                     "EXCEPTIONAL CLEARANCE DATE", "CARGO THEFT") ))

# NIBRS Segment 2 - OFFENSE
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
                                       col_names = c("SEGMENT LENGTH","SEGMENT LEVEL","SEGMENT ACTION TYPE","MONTH OF SUBMISSION",
                                                     "YEAR OF SUBMISSION","CITY INDICATOR","ORI","INCIDENT NUMBER","UCR OFFENSE CODE",
                                                     "OFFENSE ATTEMPTED/COMPLETED","OFFENDER SUSPECTED OF USING #1","OFFENDER SUSPECTED OF USING #2",
                                                     "OFFENDER SUSPECTED OF USING #3","LOCATION TYPE",
                                                     "NUMBER OF PREMISES ENTERED", "METHOD OF ENTRY", 
                                                     "TYPE CRIMINAL ACTIVITY #1", 
                                                     "TYPE CRIMINAL ACTIVITY #2",
                                                     "TYPE CRIMINAL ACTIVITY #3", 
                                                     "TYPE WEAPON FORCE INVOLVED #1", 
                                                     "AUTOMATIC WEAPON INDICATOR ##1",
                                                     "TYPE WEAPON FORCE INVOLVED #2", 
                                                     "AUTOMATIC WEAPON INDICATOR ##2", 
                                                     "TYPE WEAPON FORCE INVOLVED #3",
                                                     "AUTOMATIC WEAPON INDICATOR ##3", 
                                                     "BIAS MOTIVATION #1", 
                                                     "BIAS MOTIVATION #2", 
                                                     "BIAS MOTIVATION #3",
                                                     "BIAS MOTIVATION #4", 
                                                     "BIAS MOTIVATION #5") ))

# NIBRS Segment 3 - PROPERTY
segment_3 <- emptydf
for(i in 1:rows) {
  ifelse(segments[i,1] == 3, segment_3[i,1] <- NIBRS[i,1], "")
}
segment_3 <- na.omit(segment_3) 
write.table(segment_3, file = "data/temp/segment_3.txt", quote = FALSE,
            row.names = FALSE)
original_nibrs_segment_3 <- read_fwf("data/temp/segment_3.txt", skip = 1, 
                                     fwf_positions( 
                                       c(1,5,6,7,9,13,17,26,38,39,41,50,58,60,69,77,79,88,96,98,107,115,117,126,134,136,145,153,155,164,172,174,183,191,193,172,210,212,221,229,231,233,234,246,248,249,261,263,264,276),
                                       c(4,5,6,8,12,16,25,37,38,40,49,57,59,68,76,78,87,95,97,106,114,116,125,133,135,144,152,154,163,171,173,182,190,192,201,209,211,220,228,230,232,233,245,247,248,260,262,263,275,277),
                                       col_names = c("SEGMENT LENGTH","SEGMENT LEVEL","SEGMENT ACTION TYPE","MONTH OF SUBMISSION",
                                                     "YEAR OF SUBMISSION","CITY INDICATOR","ORI","INCIDENT NUMBER", "TYPE PROPERTY LOSS/ETC.",
                                                     
                                                     "PROPERTY DESCRIPTION #1", 
                                                     "VALUE OF PROPERTY #1", 
                                                     "DATE RECOVERED #1",
                                                     
                                                     "PROPERTY DESCRIPTION #2", 
                                                     "VALUE OF PROPERTY #2", 
                                                     "DATE RECOVERED #2",
                                                     
                                                     "PROPERTY DESCRIPTION #3", 
                                                     "VALUE OF PROPERTY #3", 
                                                     "DATE RECOVERED #3",
                                                     
                                                     "PROPERTY DESCRIPTION #4", 
                                                     "VALUE OF PROPERTY #4", 
                                                     "DATE RECOVERED #4",
                                                     
                                                     "PROPERTY DESCRIPTION #5", 
                                                     "VALUE OF PROPERTY #5", 
                                                     "DATE RECOVERED #5",
                                                     
                                                     "PROPERTY DESCRIPTION #6", 
                                                     "VALUE OF PROPERTY #6", 
                                                     "DATE RECOVERED #6", 
                                                     
                                                     "PROPERTY DESCRIPTION #7", 
                                                     "VALUE OF PROPERTY #7", 
                                                     "DATE RECOVERED #7",   
                                                     
                                                     "PROPERTY DESCRIPTION #8", 
                                                     "VALUE OF PROPERTY #8", 
                                                     "DATE RECOVERED #8",  
                                                     
                                                     "PROPERTY DESCRIPTION #9", 
                                                     "VALUE OF PROPERTY #9", 
                                                     "DATE RECOVERED #9",
                                                     
                                                     "PROPERTY DESCRIPTION #10", 
                                                     "VALUE OF PROPERTY #10", 
                                                     "DATE RECOVERED #10",
                                                     
                                                     "NUMBER OF STOLEN MOTOR VEHICLES",
                                                     "NUMBER OF RECOVERED MOTOR VEHICLES",
                                                     
                                                     "SUSPECTED DRUG TYPE #1",     
                                                     "ESTIMATED DRUG QUANTITY #1", 
                                                     "TYPE DRUG MEASUREMENT #1",                                                      
                                                     
                                                     "SUSPECTED DRUG TYPE #2",     
                                                     "ESTIMATED DRUG QUANTITY #2", 
                                                     "TYPE DRUG MEASUREMENT #2", 
                                                     
                                                     "SUSPECTED DRUG TYPE #3",     
                                                     "ESTIMATED DRUG QUANTITY #3", 
                                                     "TYPE DRUG MEASUREMENT #3") ))

# NIBRS Segment 4 - VICTIM
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
                                       col_names = c("SEGMENT LENGTH","SEGMENT LEVEL","SEGMENT ACTION TYPE","MONTH OF SUBMISSION",
                                                     "YEAR OF SUBMISSION","CITY INDICATOR","ORI","INCIDENT NUMBER","VICTIM SEQUENCE NUMBER",
                                                     
                                                     "VICTIM CONNECTED TO UCR OFFENSE #1",
                                                     "VICTIM CONNECTED TO UCR OFFENSE #2",
                                                     "VICTIM CONNECTED TO UCR OFFENSE #3",
                                                     "VICTIM CONNECTED TO UCR OFFENSE #4",
                                                     "VICTIM CONNECTED TO UCR OFFENSE #5",
                                                     "VICTIM CONNECTED TO UCR OFFENSE #6",
                                                     "VICTIM CONNECTED TO UCR OFFENSE #7",
                                                     "VICTIM CONNECTED TO UCR OFFENSE #8",
                                                     "VICTIM CONNECTED TO UCR OFFENSE #9",
                                                     "VICTIM CONNECTED TO UCR OFFENSE #10",
                                                     "TYPE OF VICTIM",                                                     
                                                     "AGE OF VICTIM",  
                                                     "SEX OF VICTIM",
                                                     "RACE OF VICTIM",  
                                                     "ETHNICITY OF VICTIM",     
                                                     "RESIDENT STATUS OF VICTIM", 
                                                     
                                                     "AGGRAVATED ASSAULT/HOMICIDE CIRCUMSTANCES #1",                                                      
                                                     "AGGRAVATED ASSAULT/HOMICIDE CIRCUMSTANCES #1",                                                     
                                                     
                                                     "ADDITIONAL JUSTIFIABLE HOMICIDE CIRCUMSTANCES",                                                        
                                                     
                                                     "TYPE INJURY #1",
                                                     "TYPE INJURY #2",
                                                     "TYPE INJURY #3",
                                                     "TYPE INJURY #4",
                                                     "TYPE INJURY #5",
                                                     
                                                     "OFFENDER NUMBER TO BE RELATED #1",                       
                                                     "RELATIONSHIP OF VICTIM TO OFFENDER #1",
                                                     
                                                     "OFFENDER NUMBER TO BE RELATED #2",
                                                     "RELATIONSHIP OF VICTIM TO OFFENDER #2",
                                                     
                                                     "OFFENDER NUMBER TO BE RELATED #3",
                                                     "RELATIONSHIP OF VICTIM TO OFFENDER #3",                                                     
                                                     
                                                     "OFFENDER NUMBER TO BE RELATED #4",
                                                     "RELATIONSHIP OF VICTIM TO OFFENDER #4",  
                                                     
                                                     "OFFENDER NUMBER TO BE RELATED #5",
                                                     "RELATIONSHIP OF VICTIM TO OFFENDER #5",      
                                                     
                                                     "OFFENDER NUMBER TO BE RELATED #6",
                                                     "RELATIONSHIP OF VICTIM TO OFFENDER #6",                                                          
                                                     
                                                     "OFFENDER NUMBER TO BE RELATED #7",
                                                     "RELATIONSHIP OF VICTIM TO OFFENDER #7",
                                                     
                                                     "OFFENDER NUMBER TO BE RELATED #8",
                                                     "RELATIONSHIP OF VICTIM TO OFFENDER #8",                                                     
                                                     
                                                     "OFFENDER NUMBER TO BE RELATED #9",
                                                     "RELATIONSHIP OF VICTIM TO OFFENDER #9",     
                                                     
                                                     "OFFENDER NUMBER TO BE RELATED #10",
                                                     "RELATIONSHIP OF VICTIM TO OFFENDER #10",
                                                     
                                                     "TYPE OF OFFICER ACTIVITY/CIRCUMSTANCE",                                                     
                                                     "OFFICER ASSIGNMENT TYPE",
                                                     "OFFICER – ORI OTHER JURISDICTION") ))

# NIBRS Segment 5 - OFFENDER
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
                                       col_names = c("SEGMENT LENGTH","SEGMENT LEVEL","SEGMENT ACTION TYPE","MONTH OF SUBMISSION",
                                                     "YEAR OF SUBMISSION","CITY INDICATOR","ORI","INCIDENT NUMBER","OFFENDER SEQUENCE NUMBER",
                                                     "AGE OF OFFENDER",
                                                     "SEX OF OFFENDER", 
                                                     "RACE OF OFFENDER",    
                                                     "ETHNICITY OF OFFENDER") ))

# NIBRS Segment 6 - ARREST
segment_6 <- emptydf
for(i in 1:rows) {
  ifelse(segments[i,1] == 6, segment_6[i,1] <- NIBRS[i,1], "")
}
segment_6 <- na.omit(segment_6) 
write.table(segment_6, file = "data/temp/segment_6.txt", quote = FALSE,
            row.names = FALSE)
original_nibrs_segment_6 <- read_fwf("data/temp/segment_6.txt", skip = 1, 
                                     fwf_positions( 
                                       c(1,5,6,7,9,13,17,26,38,40,52,60,61,62,65,67,68,70,71,75,76,77,78,79),
                                       c(4,5,6,8,12,16,25,37,39,51,59,60,61,64,66,67,69,70,74,75,76,77,78,79),
                                       col_names = c("SEGMENT LENGTH","SEGMENT LEVEL","SEGMENT ACTION TYPE","MONTH OF SUBMISSION",
                                                     "YEAR OF SUBMISSION","CITY INDICATOR","ORI","INCIDENT NUMBER","ARRESTEE SEQUENCE NUMBER",
                                                     "ARRESTEE TRANSACTION NUMBER","ARREST DATE","TYPE OF ARREST","MULTIPLE ARRESTEE SEGMENTS INDICATOR",
                                                     "UCR ARREST OFFENSE CODE",
                                                     
                                                     "ARRESTEE WAS ARMED WITH #1",
                                                     "AUTOMATIC WEAPON INDICATOR #1",
                                                     
                                                     "ARRESTEE WAS ARMED WITH #2",                                                     
                                                     "AUTOMATIC WEAPON INDICATOR #2",
                                                     
                                                     "AGE OF ARRESTEE",
                                                     "SEX OF ARRESTEE",
                                                     "RACE OF ARRESTEE",
                                                     "ETHNICITY OF ARRESTEE",
                                                     "RESIDENT STATUS OF ARRESTEE",   
                                                     "DISPOSITION OF ARRESTEE UNDER 18") ))


#Entire nibrs flatfile df
original_nibrs <- list(
  original_nibrs_segment_1,
  original_nibrs_segment_2,
  original_nibrs_segment_3,
  original_nibrs_segment_4,
  original_nibrs_segment_5,
  original_nibrs_segment_6
  ) %>% reduce(full_join)

#NOTES:

# All segments had to be parsed and written to txt files to be able to do a read_fwf
# Need to decide how the handle multiples









