library(tidyverse)
library(DBI)

con <- dbConnect(
  RPostgres::Postgres(),
  dbname = "NIBRS_2017",
  host = "ncsx-dev.cr3raossyqye.us-east-1.rds.amazonaws.com",
  port = 5432,
  user = 'analyst',
  password = 'nibrs_analyst'
)

query1 <- "
SELECT ni.incident_id, ni.incident_date, ni.cargo_theft_flag, ni.report_date_flag, ni.incident_hour, ni.cleared_except_date,
o.offense_id, nv.victim_id,
a.ori, ot.offense_code, vt.victim_type_code, ce.cleared_except_code, ce.cleared_except_id
FROM nibrs_incident ni
FULL JOIN agencies a ON a.agency_id = ni.agency_id AND a.data_year = ni.data_year
FULL JOIN nibrs_offense o ON ni.incident_id= o.incident_id 
FULL JOIN nibrs_offense_type ot ON ot.offense_type_id = o.offense_type_id
FULL JOIN nibrs_victim nv ON nv.incident_id = ni.incident_id
FULL JOIN nibrs_victim_type vt ON vt.victim_type_id = nv.victim_type_id 
FULL JOIN nibrs_cleared_except ce ON ce.cleared_except_id = ni.cleared_except_id 
INNER JOIN nibrs_month nm ON nm.nibrs_month_id = ni.nibrs_month_id
WHERE nm.data_year = 2017
"

Admin <- dbGetQuery(con, query1) %>% as_tibble()

library(sergeant)
db <- src_drill('localhost')

drillAdmin <- dbGetQuery(db$con, "select distinct AdministrativeSegmentID, OffenseSegmentID, VictimSegmentID from dfs.nibrs.FullIncidentView where ORI='VT0040100'")

Admin <- readRDS('/opt/data/nibrs/rti-tabulation-code/Admin.rds') %>% as_tibble()

Admin %>% filter(offense_code %in% c('09A','09B','100','11A','11B','11C','11D','36A','36B','13A','13B','13C','64A','64B')) %>%
  group_by(offense_code) %>%
  summarize(n=n())

Admin %>%
  filter(offense_code %in% c('23A','23B','23C','23D','23E','23F','23G','23H','240','200','510','220','250',
                             '290','270','210','26A','26B','26C','26D','26E','26F','26G','120','280')) %>%
  group_by(offense_code) %>%
  summarize(n=n()) %>% arrange(n) %>% print(n=30)

Offense <- readRDS('/opt/data/nibrs/rti-tabulation-code/Offense.rds') %>% as_tibble()

Offense %>% filter(offense_code %in% c('09A','09B','100','11A','11B','11C','11D','36A','36B','13A','13B','13C','64A','64B')) %>%
  group_by(offense_code) %>%
  summarize(n=n()) %>% arrange(desc(n))

Victim <- readRDS('/opt/data/nibrs/rti-tabulation-code/Victim.rds') %>% as_tibble()

Victim %>% filter(offense_code %in% c('09A','09B','100','11A','11B','11C','11D','36A','36B','13A','13B','13C','64A','64B')) %>%
  filter(victim_type_id %in% 4:5) %>%
  select(victim_id) %>% distinct() %>% nrow()

drillVictim <- readRDS('/tmp/victim.rds')

  