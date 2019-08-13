# assumes drill is running in docker via:
# docker-compose -f nibrs-analytics-drill-compose.yaml up -d

# note: for debugging/troubleshooting, you can add the following two arguments to the end of the R function call in the container CMD:
# reusePriorCsv=TRUE
# cleanUpCsv=FALSE

docker run -it --rm --network ubuntu_nibrs_analytics_nw \
  --mount type=bind,source=/opt/data/nibrs/cde/csv,target=/opt/data/nibrs/cde/csv \
  --mount type=bind,source=/opt/data/nibrs/cde/parquet,target=/opt/data/nibrs/cde/parquet \
  --mount type=bind,source=/opt/data/nibrs/cde-run-persist,target=/tmp/accum \
  --mount type=bind,source=/opt/data/nibrs/cde-zip,target=/opt/data/nibrs/cde-zip searchncjis/nibrs-load \
  R -e "nibrs::loadMultiStateYearDataToParquetDimensional('/opt/data/nibrs/cde-zip', yearRegex='201[0-9]', drillHost='nibrs-analytics-drill', parallel=TRUE, writeProgressDetail=FALSE, cleanUpCsv=FALSE, accumListStorageDir='/tmp/accum', reusePriorCsv=TRUE)"
