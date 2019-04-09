# Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#' @importFrom DBI dbClearResult dbSendQuery
truncateIncidents <- function(conn) {
  dbClearResult(dbSendQuery(conn, "truncate AdministrativeSegment"))
}

#' @import dplyr
#' @importFrom DBI dbWriteTable
#' @importFrom lubridate month year ymd
writeRawAdministrativeSegmentTables <- function(conn, inputDfList, state, tableList, records=-1) {

  dfName <- load(inputDfList[2])
  adminSegmentDf <- get(dfName) %>%  mutate_if(is.factor, as.character)
  rm(list=dfName)

  currentMonth <- formatC(month(Sys.Date()), width=2, flag="0")
  currentYear <- year(Sys.Date()) %>% as.integer()

  adminSegmentDf <- adminSegmentDf  %>%
    inner_join(tableList$Agency %>% select(AgencyORI), by=c('V1003'='AgencyORI'))

  if (records != -1) {
    adminSegmentDf <- adminSegmentDf %>% head(records)
  }

  if (nrow(adminSegmentDf) == 0) {
    stop('No Administrative Segment records available.')
  }

  AdministrativeSegment <- adminSegmentDf %>%
    select(ORI=V1003, IncidentNumber=V1004, INCDATE=V1005, IncidentHour=V1007,
           V1013, V1016, V1014,
           ReportDateIndicator=V1006) %>%
    mutate(IncidentHour=gsub(x=IncidentHour, pattern='\\(([0-9]+)\\).+', replacement='\\1')) %>%
    mutate(INCDATE=ifelse(trimws(INCDATE)=='' | is.na(INCDATE), NA, INCDATE)) %>%
    mutate(V1014=ifelse(trimws(V1014)=='' | is.na(V1014), NA, V1014)) %>%
    mutate(IncidentDate=ymd(INCDATE), ExceptionalClearanceDate=ymd(V1014),
           IncidentDateID=createKeyFromDate(IncidentDate),
           ExceptionalClearanceDateID=createKeyFromDate(ExceptionalClearanceDate),
           ExceptionalClearanceDateID=case_when(is.na(ExceptionalClearanceDateID) ~ 99998L, TRUE ~ ExceptionalClearanceDateID),
           MonthOfTape=currentMonth, YearOfTape=currentYear, CityIndicator=NA_character_, SegmentActionTypeTypeID=99998L,
           IncidentHour=ifelse(is.na(IncidentHour), NA_integer_, as.integer(IncidentHour))) %>%
    select(-INCDATE) %>%
    mutate(AdministrativeSegmentID=row_number()) %>%
    left_join(tableList$Agency %>% select(AgencyID, ORI=AgencyORI), by='ORI') %>%
    left_join(tableList$ClearedExceptionallyType %>% select(ClearedExceptionallyTypeID, StateCode), by=c('V1013'='StateCode')) %>%
    left_join(tableList$CargoTheftIndicatorType %>% select(CargoTheftIndicatorTypeID, StateCode), by=c('V1016'='StateCode')) %>%
    mutate(ClearedExceptionallyTypeID=ifelse(is.na(ClearedExceptionallyTypeID), 99998L, ClearedExceptionallyTypeID)) %>%
    mutate(CargoTheftIndicatorTypeID=ifelse(is.na(CargoTheftIndicatorTypeID), 99998L, CargoTheftIndicatorTypeID)) %>%
    select(-V1013, -V1016, -V1014) %>%
    mutate(StateCode=state) %>%
    as_tibble()

  rm(adminSegmentDf)

  writeLines(paste0("Writing ", nrow(AdministrativeSegment), " administrative segments to database"))
  writeDataFrameToDatabase(conn, AdministrativeSegment, 'AdministrativeSegment', viaBulk=TRUE, localBulk=FALSE, append=FALSE)

  attr(AdministrativeSegment, 'type') <- 'FT'

  tableList$AdministrativeSegment <- AdministrativeSegment

  tableList

}
