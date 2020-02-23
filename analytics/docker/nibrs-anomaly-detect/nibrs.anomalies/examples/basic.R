extractDfList <- readRDS('~/git-repos/SEARCH-NCJIS/nibrs/analytics/docker/nibrs-cde-extract/cde-extract.rds')

variableValueList <- list(
  'distincts'='VictimSegmentID',
  'stateRegex'='WA',
  'yearRegex'='201[67]',
  'VictimAgeDim'=list(
    'values'='Blank'
  ),
  'SexOfPersonTypeID'=list(
    'values'=99998:99999
  )
)

detectMissingUnknown(extractDfList, variableValueList)
