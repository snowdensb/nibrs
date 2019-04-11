## `nibrs` R package

This directory contains the source code for an R package that supports processing and analysis of
FBI [National Incident Based Reporting System (NIBRS)](https://ucr.fbi.gov/nibrs-overview) data.

The package exposes the following R functions:

* `loadICPSRRaw(...)`: Reads data from the "raw" NIBRS extracts hosted at the
University of Michigan's
[Interuniversity Consortium for Political and Social Research (ICPSR)](http://www.icpsr.umich.edu/icpsrweb/NACJD/series/128), transforms the data, and
writes the records into a "staging" database structured to mirror the native NIBRS data structure
* `loadDimensionalFromStagingDatabase(...)`: reads data from a NIBRS staging database, transforms them, and writes them into a dimensional database consisting of several
materialized views that support OLAP analysis specified in the NIBRS Mondrian schema (hosted elsewhere in this github repo)
* `loadDimensionalFromObjectLists(...)`: lists of R data frames in the SEARCH NIBRS staging format (one list of dimension tables, one list of fact tables), transforms them, and writes them into a dimensional database
* `convertStagingTablesToDimensional(...)` takes lists of R data frames in the SEARCH NIBRS staging format (one list of dimension tables, one list of fact tables) and transforms them to the SEARCH
NIBRS dimensional format (i.e., the materialized views) without writing them to a dimensional database.  This function is useful if you simply want to obtain
the dimensional tables for analysis in R; it is also a shared function reused by both the Drill and relational database loading functions
* `downloadCDEData(...)`: Harvests Crime Data Explorer extracts from the CDE website, and stores them as a local zip file for further processing
* `loadCDEDataToStaging(...)`: Reads data from a single CDE extract (unzipped) and returns a list of R data frames in the SEARCH NIBRS staging format; note that this function does
not write the data to a staging database because typically we just want the data to wind up in the dimensional format for analytics.
* `loadMultiStateYearDataToStaging(...)`: Reads data from a directory of CDE extracts (zip files) into the staging format
* `loadMultiStateYearDataToParquetDimensional(...)`: Reads data from a directory of CDE extracts (zip files) and processes them directly into the dimensional format, stored as parquet files managed by a Drill
instance. A set of csv files for each state+year are written to a directory that is shared between the R loading process and Drill (i.e., mounted into the Drill container). The function can also be (optionally)
parallelized to process each state+year in parallel (if the environment supports).

We have created separate staging and dimensional databases to decouple source NIBRS data from the analytical dataset. For demonstration purposes, we provide
the ability to populate the staging database from ICPSR data, but states or local agencies might populate a staging database by extracting from local submissions
or loading from NIBRS submission files. Either way, the staging data can then be transformed into the dimensional structure for analysis.

Note that because the data are typically so large, we don't currently support the ability to populate the staging database with CDE data. Instead we handle the staging->dimensional
transformation directly in R for each state+year extract and write the data out directly in the dimensional format.  If at some point it is desirable to write the CDE data to a staging
database, it should not be difficult to do so.

The functions that write data into MariaDB truncate the contents of the destination (output) database before loading.

### Installing the package

The package is not available on CRAN. Install instead with `devtools`:

```
devtools::install_github('SEARCH-NCJIS/nibrs/tools/r-packages/nibrs')
```

### Loading data into Drill/Parquet

For large NIBRS datasets like the combination of all Crime Data Explorer extracts for a large number of years, the best option is to load the data into partitioned parquet files and serve them
up with Apache Drill on a multi-CPU high memory server (like an `m5a.12xlarge` EC2 instance at AWS).

#### Drill setup

The easiest way to setup and run Drill is with Docker.

1. On the docker host, ensure that the root of the drill NIBRS workspace is available as a filesystem directory.  To keep things simple, we like to use the same paths on the host as in the container.  That means
creating directories `/opt/data/nibrs/cde/csv` and `/opt/data/nibrs/cde/parquet` on the host.
1. Simply run the [NIBRS analytics drill compose services](https://github.com/SEARCH-NCJIS/nibrs/blob/master/analytics/docker/nibrs-analytics-drill-compose.yaml) in
docker-compose. Note that by default, the compose file mounts the `/opt/data/nibrs` directory on the host into the container at that same path.
  ```
  docker-compose -f nibrs-analytics-drill-compose.yaml up -d
  ```
1. Download the CDE zip files you want (using the R package function `nibrs::downloadCDEData(...)` if desired) and put them in `/opt/data/nibrs/cde-zip`
1. Run R from the `searchncjis/nibrs-load` image, since it has all the necessary packages (including `nibrs`) installed already.  The following will load data from 2010-2019:

  ```
  docker run -it --rm --network ubuntu_nibrs_analytics_nw \
    --mount type=bind,source=/opt/data/nibrs/cde/csv,target=/opt/data/nibrs/cde/csv \
    --mount type=bind,source=/opt/data/nibrs/cde/parquet,target=/opt/data/nibrs/cde/parquet \
    --mount type=bind,source=/opt/data/nibrs/cde-zip,target=/opt/data/nibrs/cde-zip searchncjis/nibrs-load \
    R -e "nibrs::loadMultiStateYearDataToParquetDimensional('/opt/data/nibrs/cde-zip', yearRegex='201[0-9]', drillHost='nibrs-analytics-drill', parallel=TRUE)"
  ```

### Loading data into MySQL

For more modest-sized datasets (such as for a single state) it is perfectly fine to use MySQL (MariaDB) as the dimensional database platform.

#### MySQL setup

The `loadICPSRRaw()` function requires that a staging database be up and running, and the
`loadDimensionalFromStagingDatabase()` function requires both staging and dimensional.

The easiest way to run these databases is via Docker, using the images for each database:

* staging: `searchncjis/nibrs-staging-db` [link](https://hub.docker.com/r/searchncjis/nibrs-staging-db/)
* dimensional: `searchncjis/nibrs-analytics-db` [link](https://hub.docker.com/r/searchncjis/nibrs-analytics-db/)

Just run containers from these images, expose the mysql port (3306), and specify the appropriate connection information in the connection objects passed
to each function.

You can also create the staging database in an existing mysql instance from the [DDL](https://github.com/SEARCH-NCJIS/nibrs/blob/master/analytics/db/schema-mysql.sql).

The dimensional database does not have DDL.  The R package creates the table structure as well as the data.

#### Downloading ICPSR data

The package uses the "raw" NIBRS data at ICPSR, not the "Extract Files".  ICPSR publishes the raw data in a variety of formats; the R package uses the
serialized R data frame format.  From the [ICPSR Series Page](https://www.icpsr.umich.edu/icpsrweb/NACJD/series/128#) just visit the page for the year of interest, choosing
the "study" that does *not* have "Extract Files" in the name.  For instance the appropriate study for the 2015 data is [here](https://www.icpsr.umich.edu/icpsrweb/NACJD/studies/36795), not
[this one](https://www.icpsr.umich.edu/icpsrweb/NACJD/studies/36851).  Select the "R" option from the "Download" button above the tabbed area, which is easier than downloading
all the individual files from the "Data & Documentation" tab.  Unzip the resulting zip file; the root directory of the unzipped structure is the directory to pass as the `dataDir=`
parameter to the `nibrs::loadICPSRRaw()` function.

#### Example Run

The following snippets show the output from running the two functions on the ICPSR data from 2015.  Note that the dimensional load only loads a random 5% sample of the records.

```
R version 3.5.1 (2018-07-02) -- "Feather Spray"
Copyright (C) 2018 The R Foundation for Statistical Computing
Platform: x86_64-pc-linux-gnu (64-bit)

R is free software and comes with ABSOLUTELY NO WARRANTY.
You are welcome to redistribute it under certain conditions.
Type 'license()' or 'licence()' for distribution details.

R is a collaborative project with many contributors.
Type 'contributors()' for more information and
'citation()' on how to cite R or R packages in publications.

Type 'demo()' for some demos, 'help()' for on-line help, or
'help.start()' for an HTML browser interface to help.
Type 'q()' to quit R.

> dfs <- nibrs::loadICPSRRaw(conn=DBI::dbConnect(RMariaDB::MariaDB(), host="nibrs-staging-db", dbname="search_nibrs_staging"), dataDir="/nibrs", state="OH")
Loading code table: AdditionalJustifiableHomicideCircumstancesType
Loading code table: AggravatedAssaultHomicideCircumstancesType
Loading code table: ArresteeWasArmedWithType
Loading code table: BiasMotivationType
Loading code table: ClearedExceptionallyType
Loading code table: DispositionOfArresteeUnder18Type
Loading code table: EthnicityOfPersonType
Loading code table: LocationTypeType
Loading code table: MethodOfEntryType
Loading code table: MultipleArresteeSegmentsIndicatorType
Loading code table: OffenderSuspectedOfUsingType
Loading code table: OfficerActivityCircumstanceType
Loading code table: OfficerAssignmentTypeType
Loading code table: PropertyDescriptionType
Loading code table: RaceOfPersonType
Loading code table: VictimOffenderRelationshipType
Loading code table: ResidentStatusOfPersonType
Loading code table: SegmentActionTypeType
Loading code table: SexOfPersonType
Loading code table: SuspectedDrugTypeType
Loading code table: TypeDrugMeasurementType
Loading code table: TypeInjuryType
Loading code table: TypeOfArrestType
Loading code table: TypeOfCriminalActivityType
Loading code table: TypeOfVictimType
Loading code table: TypeOfWeaponForceInvolvedType
Loading code table: TypePropertyLossEtcType
Loading code table: UCROffenseCodeType
Loading code table: AgencyType
Loading code table: CargoTheftIndicatorType
Writing 909 Agency rows to database
Writing 469560 administrative segments to database
Writing 526397 offense segments to database
Writing 531527 OffenderSuspectedOfUsing records to database
Writing 201044 TypeCriminalActivity records to database
Writing 526397 BiasMotivation records to database
Writing 70333 TypeOfWeaponForceInvolved records to database
Writing 406417 property segments to database
Writing 40141 SuspectedDrugType association rows to database
Writing 546604 PropertyType association rows to database
Writing 531362 offender segments to database
> dfs <- nibrs::loadDimensionalFromStagingDatabase(stagingConn=DBI::dbConnect(RMariaDB::MariaDB(), host="nibrs-staging-db", dbname="search_nibrs_staging"), dimensionalConn=DBI::dbConnect(RMariaDB::MariaDB(), host="nibrs-analytics-db", dbname="search_nibrs_dimensional", user="analytics", sampleFraction=.05))
Reading dimension tables from staging
Reading fact tables from staging
Creating Incident View
Creating Victim-Offense View
Creating Victim-Offender View
Creating Group A Arrest View
Creating Property View
Creating Group B Arrest View
Writing dimensional db table FullIncidentView
Creating indexes for table FullIncidentView
Writing dimensional db table FullVictimOffenseView
Creating indexes for table FullVictimOffenseView
Writing dimensional db table FullVictimOffenderView
Creating indexes for table FullVictimOffenderView
Writing dimensional db table FullGroupAArrestView
Creating indexes for table FullGroupAArrestView
Writing dimensional db table FullPropertyView
Creating indexes for table FullPropertyView
Writing dimensional db table FullGroupBArrestView
Creating indexes for table FullGroupBArrestView
Writing dimensional db table AdditionalJustifiableHomicideCircumstancesType
Creating indexes for table AdditionalJustifiableHomicideCircumstancesType
Writing dimensional db table AggravatedAssaultHomicideCircumstancesType
Creating indexes for table AggravatedAssaultHomicideCircumstancesType
Writing dimensional db table ArresteeWasArmedWithType
Creating indexes for table ArresteeWasArmedWithType
Writing dimensional db table BiasMotivationType
Creating indexes for table BiasMotivationType
Writing dimensional db table ClearedExceptionallyType
Creating indexes for table ClearedExceptionallyType
Writing dimensional db table DispositionOfArresteeUnder18Type
Creating indexes for table DispositionOfArresteeUnder18Type
Writing dimensional db table EthnicityOfPersonType
Creating indexes for table EthnicityOfPersonType
Writing dimensional db table LocationTypeType
Creating indexes for table LocationTypeType
Writing dimensional db table MethodOfEntryType
Creating indexes for table MethodOfEntryType
Writing dimensional db table OffenderSuspectedOfUsingType
Creating indexes for table OffenderSuspectedOfUsingType
Writing dimensional db table OfficerActivityCircumstanceType
Creating indexes for table OfficerActivityCircumstanceType
Writing dimensional db table OfficerAssignmentTypeType
Creating indexes for table OfficerAssignmentTypeType
Writing dimensional db table PropertyDescriptionType
Creating indexes for table PropertyDescriptionType
Writing dimensional db table RaceOfPersonType
Creating indexes for table RaceOfPersonType
Writing dimensional db table VictimOffenderRelationshipType
Creating indexes for table VictimOffenderRelationshipType
Writing dimensional db table ResidentStatusOfPersonType
Creating indexes for table ResidentStatusOfPersonType
Writing dimensional db table SexOfPersonType
Creating indexes for table SexOfPersonType
Writing dimensional db table SuspectedDrugTypeType
Creating indexes for table SuspectedDrugTypeType
Writing dimensional db table TypeDrugMeasurementType
Creating indexes for table TypeDrugMeasurementType
Writing dimensional db table TypeInjuryType
Creating indexes for table TypeInjuryType
Writing dimensional db table TypeOfArrestType
Creating indexes for table TypeOfArrestType
Writing dimensional db table TypeOfCriminalActivityType
Creating indexes for table TypeOfCriminalActivityType
Writing dimensional db table TypeOfVictimType
Creating indexes for table TypeOfVictimType
Writing dimensional db table TypeOfWeaponForceInvolvedType
Creating indexes for table TypeOfWeaponForceInvolvedType
Writing dimensional db table TypePropertyLossEtcType
Creating indexes for table TypePropertyLossEtcType
Writing dimensional db table UCROffenseCodeType
Creating indexes for table UCROffenseCodeType
Writing dimensional db table DateType
Creating indexes for table DateType
Writing dimensional db table Agency
Creating indexes for table Agency
Writing dimensional db table AgencyType
Creating indexes for table AgencyType
Writing dimensional db table CompletionStatusType
Creating indexes for table CompletionStatusType
Writing dimensional db table ClearanceType
Creating indexes for table ClearanceType
Writing dimensional db table HourType
Creating indexes for table HourType
>
```
