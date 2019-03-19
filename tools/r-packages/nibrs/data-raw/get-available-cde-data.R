library(jsonlite)
library(sf)
library(tidyverse)
library(httr)

stateShp <- read_sf('/opt/data/Shapefiles/cb_2013_us_state_500k/cb_2013_us_state_500k.shp') %>% st_set_geometry(NULL) %>%
  mutate(name=tolower(NAME), abbr=STUSPS) %>%
  mutate(name=case_when(abbr=='DC' ~ 'washington dc', TRUE ~ name))

states <- stateShp$abbr %>% set_names(stateShp$name)

participationList <- fromJSON('https://crime-data-explorer.fr.cloud.gov/public/data/participation.json') %>% tail(-1)

availableData <- map2_dfr(participationList, names(participationList), function(state, name) {
  name <- gsub(x=name, pattern='-', replacement=' ')
  ret <- NULL
  if (is.list(state$nibrs))  {
    years <- seq(state$nibrs$`initial-year`, 2017)
    years <- setdiff(years, state$nibrs$`no-data-years`)
    #years <- paste0(years, collapse=',')
    #writeLines(paste0(name, ': ', years))
    #writeLines(paste0('Downloading ', name))
    ret <- map_dfr(years, function(yr) {
      url <- paste0('http://s3-us-gov-west-1.amazonaws.com/cg-d4b776d0-d898-4153-90c8-8336f86bdfec/', yr, '/', states[name], '-', yr, '.zip')
      tibble(State=name, StateAbbr=states[name], Year=yr, URL=url)
    })
    #GET(url, write_disk(paste0('/tmp/nibrs-cde/', name, '.zip'), overwrite=TRUE))
  }
  ret
})

saveRDS(availableData, 'inst/raw/available-cde-data.rds')
