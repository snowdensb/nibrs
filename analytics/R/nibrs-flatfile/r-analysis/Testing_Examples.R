library(tidyverse)
library(dplyr)
library(lubridate)
library(tibble)
library(data.table)

## TEST EXAMPLES

# A BASIC LOOP
for(i in 1:10) {
  b <- i^2
  test[i,1] <- i
  test[i,2] <- b}


# USING RETURN IN A FUNCTION
test <- data.frame()

new.function <- function(a) {
  for(i in 1:10) {
    b <- i^2
    test[i,1] <- i
    test[i,2] <- b}
  return(test)
}

RESULT <- new.function(6)




