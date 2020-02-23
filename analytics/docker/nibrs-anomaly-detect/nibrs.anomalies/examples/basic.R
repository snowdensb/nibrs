library(tidyverse)
library(nibrs.anomalies)

extractDfList <- readRDS('/opt/data/nibrs/cde/cde-extract.rds')

variableValueList <- list(
  'distincts'='VictimSegmentID',
  'stateRegex'='AR',
  'yearRegex'='2018',
  'VictimAgeDim'=list(
    'values'='Blank',
    'label'='Victim Age'
  ),
  'SexOfPersonTypeID'=list(
    'values'=99998:99999,
    'label'='Victim Sex'
  )
)

detectMissingUnknown(extractDfList, variableValueList) %>%
  write_csv('/tmp/anomaly.csv')
