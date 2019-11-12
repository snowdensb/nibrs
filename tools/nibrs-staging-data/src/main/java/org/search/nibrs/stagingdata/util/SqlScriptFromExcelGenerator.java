/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.stagingdata.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
/**
 * A dirty simple program that reads an Excel file.
 *
 */
public class SqlScriptFromExcelGenerator {
     
    public static void main(String[] args) throws IOException {
//        generatePolulateCodeTableScript("src/test/resources/db/data.sql", 
//        		"src/test/resources/codeSpreadSheets/NIBRSCodeTablesGeneral.xlsx", false);
        generatePolulateCodeTableScript("src/test/resources/db/dataArkansas.sql", 
        		"src/test/resources/codeSpreadSheets/NIBRSCodeTablesArkansas.xlsx", false);
//        generatePolulateCodeTableScript("src/test/resources/db/dataHawaii.sql", 
//        		"src/test/resources/codeSpreadSheets/NIBRSCodeTablesHawaii.xlsx", false);
    }

	private static void generatePolulateCodeTableScript(String sqlScriptPath, String excelFilePath, boolean isSqlServerInsert) 
				throws FileNotFoundException, IOException {
		Path adamsSqlPath = Paths.get(sqlScriptPath);

        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
         
        Workbook workbook = new XSSFWorkbook(inputStream);
        StringBuilder sb = new StringBuilder(); 
        sb.append("/*\n "
        		+ "* Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics\n "
        		+ "*\n "
        		+ "* Licensed under the Apache License, Version 2.0 (the \"License\");\n "
        		+ "* you may not use this file except in compliance with the License.\n "
        		+ "* You may obtain a copy of the License at\n "
        		+ "*\n "
        		+ "*    http://www.apache.org/licenses/LICENSE-2.0\n "
        		+ "*\n "
        		+ "* Unless required by applicable law or agreed to in writing, software\n "
        		+ "* distributed under the License is distributed on an \"AS IS\" BASIS,\n "
        		+ "* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n "
        		+ "* See the License for the specific language governing permissions and\n "
        		+ "* limitations under the License.\n "
        		+ "*/\n");
        
        Set<String> sheetNames = new HashSet<String>();
        for (int i=0; i<workbook.getNumberOfSheets(); i++){
        	Sheet sheet = workbook.getSheetAt(i); 
        
        	sheetNames.add(sheet.getSheetName());
        	
        	if (sheet.getSheetName().equals("TOC")){
        		continue;
        	}
        	
        	if (isSqlServerInsert){
        		sb.append("SET IDENTITY_INSERT dbo." + getTableName(sheet.getSheetName()) + " ON;\n");
        	}

        	System.out.println("sheetName: " + sheet.getSheetName());
        	String baseString = "insert into " + getTableName(sheet.getSheetName()) + 
        			"  values (";
            for (int j = 1; j<=sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                StringBuilder insertString = new StringBuilder();
                insertString.append(baseString);
                
                Integer pkId = Double.valueOf(row.getCell(0).getNumericCellValue()).intValue();
                insertString.append("'" + pkId + "'" );
                
                for ( int z = 1; z < row.getLastCellNum(); z++){
                	
                	String value = null; 
                	
                	if (row.getCell(z).getCellTypeEnum() == CellType.NUMERIC){
                		value = String.valueOf((int)row.getCell(z).getNumericCellValue());
                	}
                	else{
                		value = row.getCell(z).getStringCellValue();
                	}
                	
                	if (!"null".equals(value)){
                		insertString.append(", '" + value.replace("'", "''") + "'");
                	}
                	else{
                		insertString.append(", null"); 
                	}
                }
                
                insertString.append( ");\n");
                sb.append(insertString);
            }
            
        	if (isSqlServerInsert){
        		sb.append("SET IDENTITY_INSERT dbo." + getTableName(sheet.getSheetName()) + " OFF;\n");
        	}
        }
         
        workbook.close();
        inputStream.close();
        
    	if (isSqlServerInsert){
    		sb.append("SET IDENTITY_INSERT dbo.DateType ON;\n");
    	}
    	
    	LocalDate localDate = LocalDate.of(2010, 1, 1);
    	LocalDate endDate = LocalDate.of(2100, 12, 31);
    	String baseString = "insert into DateType " + 
    			" values (";
    	int i = 1; 
    	while (!localDate.isAfter(endDate)){
            StringBuilder insertString = new StringBuilder();
            insertString.append(baseString);
            
            insertString.append("'"+ i + "'"); 
            i ++; 
            
        	appendDateTypeFieldValues(localDate, insertString);
        	
        	localDate = localDate.plusDays(1);
        	sb.append(insertString);
    	}
    	
        sb.append("insert into DateType  values ('99999', '1889-01-01' , 0 , 'UNK', 0 , 0 , 'Unknown', 'Unknown' , 0 , 'Unknown', 0, 'Unknown');\n");
        sb.append("insert into DateType  values ('99998', '1890-01-01' , 0 , 'BLK', 0 , 0 , 'Blank', 'Blank' , 0 , 'Blank', 0, 'Blank');\n");
        
        if ( !sheetNames.contains("Agency") ){
	        sb.append("insert into Agency  values ('1', 'agencyORI', 'Agency Name', 2, 'WI', 'Wisconsin', 12345678);\n");
	        sb.append("insert into Agency  values ('99998', '', 'Blank', 99998, 'NA', 'Blank', 0);");
        }

        if (isSqlServerInsert){
    		sb.append("SET IDENTITY_INSERT dbo.DateType OFF;\n");
    	}
        
        try (BufferedWriter writer = Files.newBufferedWriter(adamsSqlPath)) {
            writer.write(sb.toString());
        }
        
        System.out.println("Sql script " + sqlScriptPath + " generated. ");
	}

	private static void appendDateTypeFieldValues(LocalDate localDate, StringBuilder insertString) {
		insertString.append(", '" + java.sql.Date.valueOf(localDate) + "' ");
		insertString.append(", " + localDate.getYear() + " ");
		insertString.append(", '" + String.valueOf(localDate.getYear()) + "'");
		insertString.append(", " + localDate.get(IsoFields.QUARTER_OF_YEAR) + " ");
		insertString.append(", " + localDate.getMonthValue() + " ");
		insertString.append(", '" + capitalize(Month.of(localDate.getMonthValue()).toString()) + "'");
		insertString.append(", '" + localDate.toString().substring(0, 7) + "' ");
		insertString.append(", " +  localDate.getDayOfYear() + " ");
		insertString.append(", '" + capitalize(localDate.getDayOfWeek().toString()) + "'");
		insertString.append(", " + getDayOfWeekSort(localDate)  + "");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
		insertString.append(", '" + localDate.format( formatter) + "'");
		insertString.append( ");\n");
	}

	private static String capitalize(String string){
		if (StringUtils.isBlank(string)) return string; 
		
		return StringUtils.capitalize(string.toLowerCase());
	}
	private static String getTableName(String sheetName) {
		switch (sheetName) {
		case "AggravatedAssaultHomicideCircum":
			return "AggravatedAssaultHomicideCircumstancesType";
		case "AdditionalJustifiableHomicideCi":
			return "AdditionalJustifiableHomicideCircumstancesType";
		case "RelationshipsVictimToOffendersT":
			return "VictimOffenderRelationshipType";
		case "MultipleArresteeSegmentsIndicat":
			return "MultipleArresteeSegmentsIndicatorType";
		case "DispositionOfArresteeUnder18Typ":
			return "DispositionOfArresteeUnder18Type";
		default:
			return sheetName;
		}
	}

	private static int getDayOfWeekSort(LocalDate localDate) {
		localDate.getDayOfWeek().getValue();  
		
		switch (localDate.getDayOfWeek()){
		case SUNDAY: 
			return 1; 
		default: 
			return localDate.getDayOfWeek().getValue() + 1; 
		}
	}

}