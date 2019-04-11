### SEARCH NIBRS Docker Images

This directory contains image contexts and Dockerfiles for the following images:

* `nibrs-analytics-db`: MariaDB image suitable for providing the SEARCH NIBRS dimensional model; by default, it
contains pre-packaged data for one state/year, harvested from ICPSR data
* `nibrs-analytics-saiku`: A [Saiku 3.17](https://github.com/OSBI/saiku) image set up to serve cubes based on the
data in `nibrs-analytics-db`
* `nibrs-analytics-saiku-drill`: A [Saiku 3.17](https://github.com/OSBI/saiku) image set up to serve cubes based on the
data in `nibrs-drill`
* `nibrs-drill`: An [Apache Drill](https://drill.apache.org/) image set up to serve NIBRS data from a mount point at `/nibrs`
* `nibrs-load`: An image based on `rocker/tidyverse` with the R packages needed to load data from the [ICPSR NIBRS series](https://www.icpsr.umich.edu/icpsrweb/ICPSR/series/128)
or the FBI's [Crime Data Explorer extracts](https://crime-data-explorer.fr.cloud.gov/downloads-and-docs) into the SEARCH NIBRS dimensional model
* `nibrs-staging-db`: MariaDB image suitable for providing the SEARCH NIBRS staging model (e.g., that would be used by a state
or agency to host a repository of NIBRS data)

### Running the NIBRS loading process

See the documentation for the SEARCH `nibrs` R package in its [README](https://github.com/SEARCH-NCJIS/nibrs/tree/master/tools/r-packages/nibrs).

### Running Saiku and companion dimensional model with Docker Compose

The best way to run the containers necessary to query NIBRS data with Saiku is to use the provided [Docker Compose](https://docs.docker.com/compose/) service
definitions.

#### Running Saiku in front of MariaDB (for small-ish datasets, such as one state/year loaded from ICPSR)

To run Saiku in front of MariaDB, use `nibrs-analytics-compose.yaml`:

```
docker-compose -f nibrs-analytics-compose.yaml up -d
```

Saiku will then be available at http://[host]:8080 (i.e., http://localhost:8080).

Note that once the `nibrs-analytics-db` container is up and running, you can use the `nibrs-load` image to replace the pre-packaged data.  See the R
package documentation linked above for detailed instructions.

#### Running Saiku in front of Drill

For larger datasets (e.g., multiple states and years from ICPSR or CDE), use Apache Drill for enhanced performance.  The R package loads dimensional data into
[parquet files](https://parquet.apache.org/).  Drill makes these files available for query via SQL and JDBC.  The loading process sets up views that correspond
to the tables (i.e., tables with the same names) that are loaded into the MariaDB version, allowing us to use (pretty much) the same Mondrian schema to query
both.  (The Mondrian schema for the drill version, used in the `nibrs-analytics-saiku-drill` image, needs to have the Drill schema specified; this minor edit
is handled with a `sed` command in the Dockerfile.)

The R package [README](https://github.com/SEARCH-NCJIS/nibrs/tree/master/tools/r-packages/nibrs) contains detailed instructions on loading NIBRS data into the
Drill image filesystem.  Note that the `nibrs-drill` image does not come pre-packaged with any data (the parquet files are generally too large and cumbersome to
commit to git effectively). So there is an extra initial step to bring up Saiku backed by Drill:

1. Run `docker-compose -f nibrs-analytics-drill-compose.yaml up -d` to bring up the bare instance of Drill (with the necessary companion zookeeper cluster container)
1. Run the loading process to populate the Drill filesystem in the container (mounted to host directory `/opt/data/nibrs/cde/`) with whatever data you desire
1. Run `docker-compose -f nibrs-analytics-drill-compose.yaml -f nibrs-analytics-saiku-drill-compose.yaml up -d` to add Saiku to the mix.  Note that because we
mounted host directory `/opt/data/nibrs/cde/` into the container, the loaded parquet data will persist there across container (or host) restarts.
