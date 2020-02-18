library(xml2)
library(tidyverse)

schema <- read_xml('~/git-repos/SEARCH-NCJIS/nibrs/analytics/mondrian/NIBRSAnalyticsMondrianSchema.xml') %>% as_list()

getTableLink <- function(tableName) {
  ret <- schema$Schema$PhysicalSchema %>%
    .[names(.)=='Link'] %>%
    keep(function(item) {
      attr(item, 'source') == tableName || attr(item, 'target') == tableName
    })
  if (length(ret) > 0) {
    ret[[1]]
  } else {
    NULL
  }
}

tschema <- list()

processDimension <- function(d) {
  dimName <- attr(d, 'name')
  getAttribute <- function(name) {
    d$Attributes %>%
      keep(function(item) {
        attr(item, 'name') == name
      }) %>% .[[1]]
  }
  ret <- list()
  hierarchies <- d$Hierarchies %>%
    map(function(h) {
      ret <- list()
      firstAttributeTableName <- attr(d$Attributes$Attribute, 'table')
      link <- NULL
      link <- if (!is.null(firstAttributeTableName)) getTableLink(firstAttributeTableName)
      primaryKeyTable <- NULL
      if (is.null(link)) {
        ret$Table <- list()
        if (is.null(attr(d, 'table'))) {
          attr(ret$Table, 'name') <- firstAttributeTableName
        } else {
          attr(ret$Table, 'name') <- attr(d, 'table')
        }
      } else {
        key <- attr(link$ForeignKey$Column, 'name')
        ret$Join <- list()
        ret$Join[[1]] <- list()
        attr(ret$Join[[1]], 'name') <- attr(link, 'source')
        ret$Join[[2]] <- list()
        attr(ret$Join[[2]], 'name') <- attr(link, 'target')
        ret$Join <- set_names(ret$Join, rep('Table', 2))
        attr(ret$Join, 'leftKey') <- key
        attr(ret$Join, 'rightKey') <- key
        primaryKeyTable <- attr(link, 'target')
      }
      hierarchies <- h %>%
        imap(function(level, name) {
          ret <- list()
          levelAttribute <- attr(level, 'attribute')
          aa <- getAttribute(levelAttribute)
          attr(ret, 'name') <- attr(aa, 'name')
          if (!is.null(attr(aa, 'keyColumn'))) {
            attr(ret, 'column') <- attr(aa, 'keyColumn')
          } else {
            attr(ret, 'column') <- attr(aa$Name$Column, 'name')
          }
          if (!is.null(attr(aa, 'orderByColumn'))) {
            attr(ret, 'ordinalColumn') <- attr(aa, 'orderByColumn')
          }
          if (!is.null(attr(aa, 'table'))) {
            attr(ret, 'table') <- attr(aa, 'table')
          }
          attr(ret, 'caption') <- attr(level, 'caption')
          if (!is.null(attr(aa, 'levelType'))) {
            attr(ret, 'levelType') <- attr(aa, 'levelType')
          }
          if (attr(aa, 'name')=='Year') {
            attr(ret, 'levelType') <- 'TimeYears'
          }
          ret
        })
      ret <- c(ret, hierarchies)
      attr(ret, 'primaryKeyTable') <- primaryKeyTable
      attr(ret, 'hasAll') <- "true"
      attr(ret, 'allMemberName') <- attr(h, 'allMemberName')
      attr(ret, 'name') <- attr(h, 'name')
      attr(ret, 'caption') <- attr(h, 'caption')
      if (is.null(attr(d, 'key'))) {
        attr(ret, 'primaryKey') <- attr(d$Attributes$Attribute, 'keyColumn')
      } else {
        attr(ret, 'primaryKey') <- attr(d, 'key')
      }
      ret
    })
  ret <- hierarchies
  attr(ret, 'name') <- dimName
  attr(ret, 'caption') <- attr(d, 'caption')
  if (!is.null(attr(d, 'type'))) {
    attr(ret, 'type') <- "TimeDimension"
  }
  ret
}

dimensions <- schema$Schema %>%
  .[names(.)=='Dimension'] %>%
  map(processDimension)

cubes <- schema$Schema %>%
  .[names(.)=='Cube'] %>%
  map(function(cube) {
    dimensionLinks <- cube$MeasureGroups$MeasureGroup$DimensionLinks
    getLink <- function(name) {
      dimensionLinks %>%
        keep(function(link) {
          attr(link, 'dimension')==name
        }) %>% .[[1]]
    }
    ret <- list()
    ret$Table <- list()
    attr(ret$Table, 'name') <- attr(cube$MeasureGroups$MeasureGroup, 'table')
    dimensions <- cube$Dimensions %>%
      .[names(.)=='Dimension'] %>%
      map(function(d) {
        ret <- NULL
        dimensionName <- NULL
        if (is.null(attr(d, 'source'))) {
          ret <- processDimension(d)
          dimensionName <- attr(ret, 'name')
        } else {
          ret <- list()
          attr(ret, 'source') <- attr(d, 'source')
          if (is.null(attr(d, 'name'))) {
            dimensionName <- attr(d, 'source')
          } else {
            dimensionName <- attr(d, 'name')
          }
          attr(ret, 'name') <- dimensionName
        }
        link <- getLink(dimensionName)
        attr(ret, 'foreignKey') <- attr(link, 'foreignKeyColumn')
        ret
      })
    dNames <- dimensions %>% map_chr(function(d) {
      ret <- 'Dimension'
      if (!is.null(attr(d, 'source'))) {
        ret <- 'DimensionUsage'
      }
      ret
    })
    dimensions <- set_names(dimensions, dNames)
    measures <- cube$MeasureGroups$MeasureGroup$Measures %>%
      .[names(.)=='Measure'] %>%
      map(function(item) {
        # <Measure name="Incident Count" column="AdministrativeSegmentID" aggregator="distinct-count" visible="true" caption="Distinct Incident Count" formatString="#,###"/>
        item
      })
    ret <- c(ret, dimensions, measures)
    attr(ret, 'name') <- attr(cube, 'name')
    attr(ret, 'caption') <- attr(cube, 'caption')
    attr(ret, 'description') <- attr(cube, 'caption')
    ret
  })

tschema$Schema <- c(dimensions, cubes)
attr(tschema$Schema, 'name') <- 'nibrs_analytics'
attr(tschema$Schema, 'caption') <- 'NIBRS Analytics'
attr(tschema$Schema, 'description') <- 'NIBRS Analytics'

write_xml(tschema %>% as_xml_document(), '~/git-repos/SEARCH-NCJIS/nibrs/analytics/docker/nibrs-analytics-piet-drill/files/NIBRSAnalyticsMondrianSchema.8.xml')
