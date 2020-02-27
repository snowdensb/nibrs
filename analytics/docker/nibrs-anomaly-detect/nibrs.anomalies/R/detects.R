#' Summarize NIBRS data to tally percent of given values for a variable
#'
#' This function summarizes data in the NIBRS `FullIncidentView` materialized view, by performing a distinct-count tally
#' of records for each agency, and calculating a percentage of the tallied records that match a defined list of values. Primarily
#' this is useful for anomaly detection, but can be used for tallying any range of values.
#' @param extractDfList a list of NIBRS data frames
#' At a minimum, this list must include `FullIncidentView`, `State` and `Agency` data frames.
#' @param variableValueList A list structure configuring the summarization. The structure
#' should contain the following names (keys) and values:
#'
#' * `aggregateBy`: a character vector containing the names of the columns in `FullIncidentView` that should be distinctly counted
#' * `stateRegex`: a regular expression representing the two-letter state codes to include in the analysis
#' * `yearRegex`: a regular expression representing the years to include in the analysis
#' * 1 or more sub-lists, where the name (key) of the sublist in `variableValueList` is the name of the column to summarize, and the
#' elements of the list have the following names (keys) and values:
#'   * `values`: the values of the column to count as "anomalies" (i.e., to count in the numerator of the tally)
#'   * `label`: a label to use as the column name for the resulting tally of this column
#' @return a data frame with the following columns:
#'
#' * `AgencyName`: The agency name
#' * `Records`: The number of distinct-counted values of the `aggregateBy` columns for that agency
#' * A column for each of the variables identified in `variableValueList`, containing the percentage of records for each agency
#' for which the value of the variable matches the `values`
#' @import dplyr
#' @importFrom purrr imap
#' @import rlang
#' @export
summarizeValues <- function(extractDfList, variableValueList) {

  flist <- variableValueList %>%
    .[!(names(.) %in% c('aggregateBy','stateRegex','yearRegex','filter'))] %>%
    imap(function(item, nm) {
      parse_expr(paste0(nm, ' %in% c(', paste0(gsub(x=item$values, pattern='(.+)', replacement='"\\1"'), collapse=','), ')'))
    })

  aggregateBy <- variableValueList$aggregateBy

  states <- extractDfList$State %>%
    semi_join(extractDfList$FullIncidentView, by='StateID') %>%
    filter(grepl(x=StateCode, pattern=variableValueList$stateRegex))

  if (is.null(variableValueList$filter)) {
    filt <- parse_expr('TRUE')
  } else {
    filt <- parse_expr(variableValueList$filter)
  }

  results <- extractDfList$FullIncidentView %>%
    filter(!!filt) %>%
    semi_join(states, by='StateID') %>%
    filter(grepl(x=as.character(CDEYear), pattern=variableValueList$yearRegex)) %>%
    select(!!!syms(c(names(flist), aggregateBy,'AgencyID'))) %>%
    group_by(!!!syms(aggregateBy)) %>%
    filter(row_number()==1) %>%
    mutate(!!!flist) %>%
    group_by(AgencyID)

  outNames <- variableValueList %>%
    .[!(names(.) %in% c('aggregateBy','stateRegex','yearRegex','filter'))] %>%
    map(function(item) {
      item$label
    }) %>%
    imap_chr(function(item, nm) {
      if (is.null(item)) {
        nm
      } else {
        item
      }
    })

  outNames <- names(outNames) %>% set_names(outNames)

  results <- inner_join(
    results %>% summarize_at(names(flist), mean),
    results %>% summarize(Records=n()),
    by='AgencyID'
  ) %>%
    inner_join(extractDfList$Agency %>% select(AgencyID, AgencyName), by='AgencyID') %>%
    select(-AgencyID) %>%
    select(AgencyName, Records, everything()) %>%
    bind_rows(results %>% ungroup() %>% summarize_at(names(flist), mean) %>%
                bind_cols(results %>% ungroup() %>% summarize(Records=n()) %>% select(Records)) %>%
                mutate(AgencyName='Total')) %>%
    rename(!!!outNames)

  results %>% arrange(desc(Records))

}
