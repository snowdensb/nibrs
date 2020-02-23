#' @import dplyr
#' @importFrom purrr imap
#' @import rlang
#' @export
detectMissingUnknown <- function(extractDfList, variableValueList) {

  flist <- variableValueList %>%
    .[!(names(.) %in% c('distincts','stateRegex','yearRegex'))] %>%
    imap(function(item, nm) {
      parse_expr(paste0(nm, ' %in% c(', paste0(gsub(x=item$values, pattern='(.+)', replacement='"\\1"'), collapse=','), ')'))
    })

  distincts <- variableValueList$distincts

  states <- extractDfList$State %>%
    semi_join(extractDfList$FullIncidentView, by='StateID') %>%
    filter(grepl(x=StateCode, pattern=variableValueList$stateRegex))

  results <- extractDfList$FullIncidentView %>%
    semi_join(states, by='StateID') %>%
    filter(grepl(x=as.character(CDEYear), pattern=variableValueList$yearRegex)) %>%
    select(!!!syms(c(names(flist), distincts,'AgencyID'))) %>%
    group_by(!!!syms(distincts)) %>%
    filter(row_number()==1) %>%
    mutate(!!!flist) %>%
    group_by(AgencyID)

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
                mutate(AgencyName='Total'))

  results

}
