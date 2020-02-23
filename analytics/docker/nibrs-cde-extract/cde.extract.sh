#!/bin/bash

R -e "saveRDS(cde.extract::extractData(drillHost='$drill_host', yearRegex='$year_regex', stateRegex='$state_regex'), '/output/cde-extract.rds')"
