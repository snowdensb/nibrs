#' @import arrow
#' @import dplyr
#' @import purrr
#' @import sergeant
#' @import DBI
#' @export
extractData <- function(drillHost='localhost', drillPort=8047L, yearRegex='.+', stateRegex='.+', parquetFilePath='/opt/data/nibrs/cde/parquet/') {

  options(sergeant.bigint.warnonce = FALSE)

  db <- src_drill(host=drillHost, port=drillPort)
  dc <- drill_connection(host=drillHost, port=drillPort)

  states <- dbGetQuery(db$con, "select StateID, StateCode from dfs.nibrs.`State`") %>%
    filter(grepl(x=StateCode, pattern=stateRegex))

  yearsDf <- dbGetQuery(db$con, "select distinct YearLabel from dfs.nibrs.`DateType`") %>%
    filter(grepl(x=YearLabel, pattern=yearRegex)) %>%
    filter(grepl(x=YearLabel, pattern='[0-9]{4}'))

  parquetFiles <- list.files(file.path(parquetFilePath, 'FullIncidentView.parquet'), pattern='.+\\.parquet', full.names=TRUE)

  writeLines(paste0("Extracting year/state parquet file mapping from ", length(parquetFiles), " parquet files"))

  stateYearMapDf <- parquetFiles %>%
    map_dfr(function(f) {
      read_parquet(f, col_select=c('StateID', 'CDEYear')) %>% distinct() %>% mutate(file=f)
    })

  if (nrow(states)==0) {
    warning(paste0("No states selected with regex ", stateRegex))
  } else if (nrow(yearsDf)==0) {
    warning(paste0("No years selected with regex ", yearRegex))
  } else {

    stateIDs <- paste(states$StateID, collapse=',')
    years <- paste0(yearsDf$YearLabel, collapse=',')
    writeLines(paste0("Extracting data for ", nrow(states), " states: ", paste(states$StateCode, collapse=',')))
    writeLines(paste0("Extracting data for ", nrow(yearsDf), " years: ", years))

    fact <- states %>%
      pmap_dfr(function(StateID, StateCode) {
        yearsDf$YearLabel %>%
          map_dfr(function(yearLabel) {
            yy <- as.integer(yearLabel)
            ss <- StateID
            f <- stateYearMapDf %>% filter(StateID==ss) %>% filter(CDEYear==yy) %>% pluck('file')
            ret <- NULL
            if (!is.null(f)) {
              writeLines(paste0("Extracting data from file ", f, " containing data for state=", StateCode, " and year=", yy))
              ret <- read_parquet(f) %>% filter(StateID==ss) %>% filter(CDEYear==yy)
              writeLines(paste0("Extracted ", nrow(ret), " records"))
            }
            ret
          })
      })

    writeLines(paste0("Extracted ", nrow(fact), " rows from FullIncidentView fact table"))

  }

  tables <- drill_show_files(dc, 'dfs.nibrs') %>%
    filter(!grepl(x=name, pattern='^Full.+View\\.')) %>%
    filter(!isDirectory) %>%
    pluck('name') %>% gsub(x=., pattern='^(.+)\\.view\\.drill$', replacement='\\1')

  ret <- tables %>%
    map(function(table) {
      ret <- dbGetQuery(db$con, paste0("select * from dfs.nibrs.`", table, "`"))
      writeLines(paste0("Extracted ", nrow(ret), " rows from dimension table ", table))
      ret
    }) %>% set_names(tables)

  ret$FullIncidentView <- fact

  ret$stateRegex <- stateRegex
  ret$yearRegex <- yearRegex

  writeLines("CDE data extraction complete")

  ret

}
