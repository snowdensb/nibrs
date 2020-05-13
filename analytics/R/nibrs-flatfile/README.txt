Converts a NIBRS Flatfile to an R Data Frame


FOLDERS

data
	nibrs - The location of the nibrs flatfile to be processed [THIS FILE MUST BE NAMED "nibrs.txt"]
	temp - The location Segment files used for extracting segment specifc information [IGNORE.  USED BY THE R SCRIPT]

documents - The location of the nibrs spec and other informative files

examples - A folder to store nibrs flatfiles

TO USE THE NIBRS R SCRIPT

1. Copy the nibrs flatfile to be processed to the data/nibrs folder and rename to "nibrs.txt"

2. Run the "FlatfileToNIBRS" Function

3. Run the following to generate the NIBRS R Dataframe:

	nibrsFlat <- FlatfileToNIBRS (segments, NIBRS)

4. To write the dataframe to a csv, run the following:

	write.csv(nibrsFlat, "nibrsFlat.csv", row.names=FALSE, quote = FALSE)