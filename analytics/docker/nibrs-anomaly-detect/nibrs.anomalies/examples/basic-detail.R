library(tidyverse)
library(nibrs.anomalies)
library(openxlsx)

analyzeAnomalies <- function(state, yr) {

  writeLines(paste0('Analyzing anomalies in ', state))

  results <- list()

  results$VictimSegment <- summarizeValues(
    extractDfList,
    list(
      'aggregateBy'='VictimSegmentID',
      'stateRegex'=state,
      'yearRegex'=yr,
      'filter'="TypeOfVictimTypeID %in% c(1,5)",
      'VictimAgeDim'=list(
        'values'=c('Blank', 'Unknown'),
        'label'='Victim Age Blank/Unknown'
      ),
      'SexOfPersonTypeID'=list(
        'values'=99998:99999,
        'label'='Victim Sex missing/unknown'
      ),
      'RaceOfPersonTypeID'=list(
        'values'=99998:99999,
        'label'='Victim Race missing/unknown'
      )
    )
  )

  results$OffenderSegment <- summarizeValues(
    extractDfList,
    list(
      'aggregateBy'='OffenderSegmentID',
      'stateRegex'=state,
      'yearRegex'=yr,
      'OffenderAgeDim'=list(
        'values'=c('Blank', 'Unknown'),
        'label'='Offender Age Blank/Unknown'
      ),
      'OffenderSexOfPersonTypeID'=list(
        'values'=99998:99999,
        'label'='Offender Sex missing/unknown'
      ),
      'OffenderRaceOfPersonTypeID'=list(
        'values'=99998:99999,
        'label'='Offender Race missing/unknown'
      )
    )
  ) %>%
    inner_join(summarizeValues(
      extractDfList,
      list(
        'aggregateBy'='OffenderSegmentID',
        'stateRegex'=state,
        'yearRegex'=yr,
        'OffenderAgeDim'=list(
          'values'=1:9,
          'label'='Offender Age < 10'
        )
      )
    ) %>% select(-Records), by='AgencyName'
    )

  results$VictimOffenderAssn <- summarizeValues(
    extractDfList,
    list(
      'aggregateBy'='VictimOffenderAssociationID',
      'stateRegex'=state,
      'yearRegex'=yr,
      'VictimOffenderRelationshipTypeID'=list(
        'values'=99999,
        'label'='Victim-Offender Relationship Unknown'
      )
    )
  )

  results$OffenseSegment <- summarizeValues(
    extractDfList,
    list(
      'aggregateBy'='OffenseSegmentID',
      'stateRegex'=state,
      'yearRegex'=yr,
      'LocationTypeTypeID'=list(
        'values'=25,
        'label'='Location Type Other/Unknown'
      ),
      'TypeOfWeaponForceInvolvedTypeID'=list(
        'values'=99999,
        'label'='Weapon/Force Involved Unknown'
      )
    )
  )

  results$PropertySegment <- summarizeValues(
    extractDfList,
    list(
      'aggregateBy'='PropertySegmentID',
      'stateRegex'=state,
      'yearRegex'=yr,
      'PropertyDescriptionTypeID'=list(
        'values'=99998:99999,
        'label'='Property Type Blank/Unknown'
      )
    )
  ) %>% inner_join(
    summarizeValues(
      extractDfList,
      list(
        'aggregateBy'='PropertySegmentID',
        'stateRegex'=state,
        'yearRegex'=yr,
        'PropertyDescriptionTypeID'=list(
          'values'=77,
          'label'='Property Type "Other"'
        )
      )
    ) %>% select(-Records), by='AgencyName'
  )

  results$AdminSegment <- summarizeValues(
    extractDfList,
    list(
      'aggregateBy'='AdministrativeSegmentID',
      'stateRegex'=state,
      'yearRegex'=yr,
      'IncidentHour'=list(
        'values'=NA,
        'label'='Incident Hour Missing'
      )
    )
  ) %>% inner_join(
    summarizeValues(
      extractDfList,
      list(
        'aggregateBy'='AdministrativeSegmentID',
        'stateRegex'=state,
        'yearRegex'=yr,
        'IncidentHour'=list(
          'values'=0:1,
          'label'='Incident Hour of 0 or 1'
        )
      )
    ) %>% select(-Records), by='AgencyName'
  )

  wb <- createWorkbook()
  boldStyle <- createStyle(textDecoration = "bold")

  results %>%
    iwalk(function(rdf, nm) {
      sheet <- addWorksheet(wb, nm)
      writeData(wb, nm, rdf, headerStyle=boldStyle)
      setColWidths(wb, nm, widths="auto", cols=1:ncol(rdf))
    })

  saveWorkbook(wb, paste0('/opt/data/nibrs/cde/anomalies-', state, '-', yr, '.xlsx'), overwrite = TRUE)

  invisible(results)

}

extractDfList <- readRDS('/opt/data/nibrs/cde/cde-extract.rds')

c('AR','ID','KY') %>%
  walk(analyzeAnomalies, '2018')
