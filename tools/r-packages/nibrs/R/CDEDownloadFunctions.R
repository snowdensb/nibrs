#' Download individual state+year extracts from the CDE site
#' @import dplyr
#' @import purrr
#' @importFrom jsonlite fromJSON
#' @import httr
#' @export
downloadCDEData <- function(outputDir, constrainYears=NULL, constrainStates=NULL, download=TRUE) {

  states <- c(datasets::state.abb, 'DC') %>%
    set_names(c(tolower(datasets::state.name), 'washington dc'))

  if (!is.null(constrainStates)) {
    states <- states[states %in% constrainStates]
  }

  participationList <- fromJSON('https://crime-data-explorer.fr.cloud.gov/public/data/participation.json') %>% tail(-1) %>%
    set_names(gsub(x=names(.), pattern='-', replacement=' ')) %>%
    .[names(states)]

  map2_dfr(participationList, names(participationList), function(state, name) {

    ret <- NULL

    if (is.list(state$nibrs))  {

      years <- seq(state$nibrs$`initial-year`, 2018)
      years <- setdiff(years, state$nibrs$`no-data-years`)

      if (!is.null(constrainYears)) {
        years <- base::intersect(years, constrainYears)
      }

      ret <- map_dfr(years, function(yr) {
        if (yr <= 2015) {
          bucket <- 'cg-d3f0433b-a53e-4934-8b94-c678aa2cbaf3'
        } else {
          bucket <- 'cg-d4b776d0-d898-4153-90c8-8336f86bdfec'
        }
        url <- paste0('http://s3-us-gov-west-1.amazonaws.com/', bucket, '/', yr, '/', states[name], '-', yr, '.zip')
        if (download) {
          writeLines(paste0('Downloading ', url))
          GET(url, write_disk(file.path(outputDir, paste0(states[name], '-', yr, '.zip')), overwrite=TRUE))
        }
        tibble(State=name, StateAbbr=states[name], Year=yr, URL=url)
      })

    }

    ret

  })

}
