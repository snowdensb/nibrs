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
package org.search.nibrs.report.service;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.search.nibrs.model.reports.PropertyStolenByClassification;
import org.search.nibrs.model.reports.PropertyStolenByClassificationRowName;
import org.search.nibrs.model.reports.PropertyTypeValue;
import org.search.nibrs.model.reports.PropertyTypeValueRowName;
import org.search.nibrs.model.reports.ReturnAForm;
import org.search.nibrs.model.reports.ReturnAFormRow;
import org.search.nibrs.model.reports.ReturnARowName;
import org.search.nibrs.report.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelExporter {
	private static final String CRIMINAL_HOMICIDE = "CRIMINAL HOMICIDE";
	private static final Log log = LogFactory.getLog(ExcelExporter.class);
	
	@Autowired
	private AppProperties appProperties;

    public void exportReturnASupplement(ReturnAForm returnAForm){
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        XSSFSheet sheet = workbook.createSheet("Return A Supplement");
    	
        int rowNum = 0;
        log.info("Write to the excel file");
        CellStyle wrappedStyle = workbook.createCellStyle();
        wrappedStyle.setWrapText(true);
        wrappedStyle.setBorderBottom(BorderStyle.THIN);
        wrappedStyle.setBorderTop(BorderStyle.THIN);
        wrappedStyle.setBorderRight(BorderStyle.THIN);
        wrappedStyle.setBorderLeft(BorderStyle.THIN);
        CellStyle centeredStyle = workbook.createCellStyle();
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);;
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        
        XSSFFont normalWeightFont = workbook.createFont();
        normalWeightFont.setBold(false);
        
        Font underlineFont = workbook.createFont();
        underlineFont.setUnderline(Font.U_SINGLE);
        
    	createReturnASupplementTextSheet(sheet, rowNum, boldFont, normalWeightFont);
        createPropertyByTypeAndValueSheet(returnAForm, workbook, wrappedStyle, centeredStyle, boldFont, normalWeightFont);
        createPropertyStolenByClassificationSheet(returnAForm, workbook, wrappedStyle, centeredStyle, boldFont, normalWeightFont);
		
        try {
        	String fileName = appProperties.getReturnAFormOutputPath() + "/ReturnASupplement-" + returnAForm.getOri() + "-" + returnAForm.getYear() + "-" + StringUtils.leftPad(String.valueOf(returnAForm.getMonth()), 2, '0') + ".xlsx"; 
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
            System.out.println("The return A form is writen to fileName: " + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
	private void createPropertyStolenByClassificationSheet(ReturnAForm returnAForm, XSSFWorkbook workbook,
			CellStyle wrappedStyle, CellStyle centeredStyle, Font boldFont, XSSFFont normalWeightFont) {
		int rowNum = 0;
		
		XSSFSheet propertyStolenSheet = workbook.createSheet("PROPERTY STOLEN BY CLASSIFICATION");
        rowNum = createPropertyStolenTitleRow(propertyStolenSheet, rowNum, boldFont, normalWeightFont);
		rowNum = createPropertyStolenHeaderRow(propertyStolenSheet, rowNum, boldFont, normalWeightFont);
		
        for (PropertyStolenByClassificationRowName rowName: PropertyStolenByClassificationRowName.values()){
        	writePropertyStolenRow(propertyStolenSheet, rowName, returnAForm.getPropertyStolenByClassifications()[rowName.ordinal()], rowNum++, boldFont);
        }

		propertyStolenSheet.autoSizeColumn(0);
		propertyStolenSheet.autoSizeColumn(1);
		propertyStolenSheet.setColumnWidth(2, 600 * propertyStolenSheet.getDefaultColumnWidth());
		propertyStolenSheet.setColumnWidth(3, 900 * propertyStolenSheet.getDefaultColumnWidth());

		
	}
	private void writePropertyStolenRow(XSSFSheet propertyStolenSheet, PropertyStolenByClassificationRowName rowName,
			PropertyStolenByClassification propertyStolenByClassification, int rowNum, Font boldFont) {
    	Row row = propertyStolenSheet.createRow(rowNum);
    	int colNum = 0;
    	Cell cell = row.createCell(colNum++);
    	CellStyle wrapStyle = propertyStolenSheet.getWorkbook().createCellStyle();
    	wrapStyle.setBorderBottom(BorderStyle.THIN);
    	wrapStyle.setBorderTop(BorderStyle.THIN);
    	wrapStyle.setBorderLeft(BorderStyle.THIN);
    	wrapStyle.setBorderRight(BorderStyle.THIN);
    	wrapStyle.setWrapText(true);
        
    	CellStyle centeredStyle = propertyStolenSheet.getWorkbook().createCellStyle();
    	centeredStyle.cloneStyleFrom(wrapStyle);
    	centeredStyle.setAlignment(HorizontalAlignment.CENTER);
    	
        CellStyle greyForeGround = propertyStolenSheet.getWorkbook().createCellStyle();
        greyForeGround.cloneStyleFrom(wrapStyle);
        greyForeGround.setAlignment(HorizontalAlignment.RIGHT);
        greyForeGround.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        greyForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        CellStyle yellowForeGround = propertyStolenSheet.getWorkbook().createCellStyle();
        yellowForeGround.cloneStyleFrom(greyForeGround);
        yellowForeGround.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        
        
        Font smallerUnderlineFont = propertyStolenSheet.getWorkbook().createFont();
        smallerUnderlineFont.setUnderline(Font.U_SINGLE);
        smallerUnderlineFont.setFontHeightInPoints(Short.parseShort("7"));
        
        switch(rowName){
    	case ROBBERY_TOTAL: 
    	case BURGLARY_TOTAL:
    	case LARCENY_TOTAL:
    	case LARCENIES_TOTAL_BY_NATURE:
    	case GRAND_TOTAL:
            XSSFRichTextString allBoldString = new XSSFRichTextString(rowName.getLabel());
            allBoldString.applyFont(boldFont);
            cell.setCellValue(allBoldString);
            cell = row.createCell(colNum++);
            cell.setCellValue(rowName.getDataEntry());
            cell.setCellStyle(greyForeGround);
            cell = row.createCell(colNum++);
            cell.setCellStyle(centeredStyle);
            cell.setCellValue( propertyStolenByClassification.getNumberOfOffenses());
            cell = row.createCell(colNum++);
            cell.setCellType(CellType.STRING);
            cell.setCellStyle(centeredStyle);
            cell.setCellValue("$" +  propertyStolenByClassification.getMonetaryValue());
    		break; 
    		
    	default: 
    		switch (rowName) {
        	case MURDER_AND_NONNEGLIGENT_MANSLAUGHTER:
        	case RAPE:
        	case MOTOR_VEHICLE_THEFT:
                allBoldString = new XSSFRichTextString(rowName.getLabel());
                allBoldString.applyFont(boldFont);
                cell.setCellValue(allBoldString);
        		break;
        	case ROBBERY_HIGHWAY: 
        	case BURGLARY_RESIDENCE_NIGHT: 
        	case LARCENY_200_PLUS: 
        	case MOTOR_VEHICLES_STOLEN_AND_RECOVERED_LOCALLY: 
                XSSFRichTextString s1 = new XSSFRichTextString(rowName.getLabel());
                s1.applyFont(0, rowName.getLabel().indexOf('\n'), boldFont);
                cell.setCellValue(s1);
                break;
        	case LARCENY_POCKET_PICKING: 
                s1 = new XSSFRichTextString(rowName.getLabel());
                s1.applyFont(0, rowName.getLabel().indexOf('\n'), smallerUnderlineFont);
                s1.applyFont(rowName.getLabel().indexOf("6x"), rowName.getLabel().lastIndexOf('\n'), boldFont);
                cell.setCellValue(s1);
                break;
    		default: 
    			cell.setCellValue(rowName.getLabel());
    		}
    		cell.setCellStyle(wrapStyle);
    		cell = row.createCell(colNum++);
    		cell.setCellValue(rowName.getDataEntry());
    		cell.setCellStyle(greyForeGround);
    		cell = row.createCell(colNum++);
    		cell.setCellValue((Integer) propertyStolenByClassification.getNumberOfOffenses());
    		cell.setCellStyle(yellowForeGround);
    		
    		List<PropertyStolenByClassificationRowName> lastFourRows = 
    				Arrays.asList(PropertyStolenByClassificationRowName.MOTOR_VEHICLES_STOLEN_AND_RECOVERED_LOCALLY, 
    						PropertyStolenByClassificationRowName.MOTOR_VEHICLES_STOLEN_LOCALLY_AND_RECOVERED_BY_OTHER_JURISDICTIONS, 
    						PropertyStolenByClassificationRowName.MOTOR_VEHICLES_TOTAL_LOCALLY_STOLEN_MOTOR_VEHICLES_RECOVERED, 
    						PropertyStolenByClassificationRowName.MOTOR_VEHICLES_STOLEN_IN_OTHER_JURISDICTIONS_AND_RECOVERED_LOCALLY);
    		if (!lastFourRows.contains(rowName)) {
    			cell = row.createCell(colNum++);
    			cell.setCellValue(propertyStolenByClassification.getMonetaryValue());
    			cell.setCellStyle(yellowForeGround);
    		}
    	}
		
	}
	private int  createPropertyStolenHeaderRow(XSSFSheet sheet, int rowNum, Font boldFont,
			XSSFFont normalWeightFont) {
        XSSFFont underlineFont = sheet.getWorkbook().createFont();
		underlineFont.setUnderline(Font.U_SINGLE);
		
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((4*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		
        CellStyle column0Style = sheet.getWorkbook().createCellStyle();
        column0Style.setWrapText(true);
        column0Style.setAlignment(HorizontalAlignment.CENTER);
        column0Style.setVerticalAlignment(VerticalAlignment.CENTER);
        column0Style.setBorderTop(BorderStyle.THIN);
        column0Style.setBorderLeft(BorderStyle.THIN);
        column0Style.setBorderRight(BorderStyle.THIN);

		cell.setCellStyle(column0Style);
		XSSFRichTextString s1 = new XSSFRichTextString(); 
		s1.append("CLASSIFICATION", underlineFont);
		cell.setCellValue(s1);
		
		Cell cell1 = row.createCell(1);
		XSSFCellStyle column1Style = sheet.getWorkbook().createCellStyle(); 
		column1Style.setRotation((short)90);
		column1Style.setVerticalAlignment(VerticalAlignment.CENTER);
		column1Style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		column1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		column1Style.setBorderTop(BorderStyle.THIN);
		column1Style.setBorderLeft(BorderStyle.THIN);
		column1Style.setBorderRight(BorderStyle.THIN);
		cell1.setCellValue("Data Entry");
		cell1.setCellStyle(column1Style);
		

		Cell cell2 = row.createCell(2);
		cell2.setCellStyle(column0Style);
		s1 = new XSSFRichTextString("Number of Actual \n Offenses (Column 4\n Return A)");
		cell2.setCellValue(s1);
		
		Cell cell3 = row.createCell(3);
		cell3.setCellStyle(column0Style);
		s1 = new XSSFRichTextString("Monetary \n Value of Property Stolen");
		cell3.setCellValue(s1);
		
		return rowNum;
	}
	private void createPropertyByTypeAndValueSheet(ReturnAForm returnAForm, XSSFWorkbook workbook, CellStyle wrappedStyle,
			CellStyle centeredStyle, Font boldFont, XSSFFont normalWeightFont) {
		
		int rowNum = 0;
		XSSFSheet propertyByTypeAndValueSheet = workbook.createSheet("Property By Type and Value");
        rowNum = createPropertyByTypeAndValueTitleRow(propertyByTypeAndValueSheet, rowNum, wrappedStyle, boldFont, normalWeightFont);
		createPropertyByTypeAndValueHeaderRow(propertyByTypeAndValueSheet, rowNum, boldFont, normalWeightFont);
		
		rowNum = 5;
        for (PropertyTypeValueRowName rowName: PropertyTypeValueRowName.values()){
        	writePropertyTypeValueRow(propertyByTypeAndValueSheet, rowName, returnAForm.getPropertyTypeValues()[rowName.ordinal()], rowNum++, boldFont);
        }


        propertyByTypeAndValueSheet.autoSizeColumn(0);
        propertyByTypeAndValueSheet.autoSizeColumn(1);
        propertyByTypeAndValueSheet.setColumnWidth(2, 350*propertyByTypeAndValueSheet.getDefaultColumnWidth());
        propertyByTypeAndValueSheet.setColumnWidth(3, 350*propertyByTypeAndValueSheet.getDefaultColumnWidth());
        propertyByTypeAndValueSheet.setColumnWidth(4, 350*propertyByTypeAndValueSheet.getDefaultColumnWidth());
        propertyByTypeAndValueSheet.setColumnWidth(5, 350*propertyByTypeAndValueSheet.getDefaultColumnWidth());
        
		rowNum = 20; 
		Row row = propertyByTypeAndValueSheet.createRow(rowNum);
		
		CellStyle thinBorderBottom = workbook.createCellStyle();
		thinBorderBottom.setBorderBottom(BorderStyle.THIN);
		thinBorderBottom.setAlignment(HorizontalAlignment.CENTER);

		Cell cell = row.createCell(0);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(1);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(2);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(3);
		cell.setCellStyle(thinBorderBottom);
		
		row = propertyByTypeAndValueSheet.createRow(rowNum+1); 
		cell = row.createCell(0); 
		cell.setCellValue("Prepared by");
		cell = row.createCell(2); 
		propertyByTypeAndValueSheet.addMergedRegion(new CellRangeAddress(rowNum+1, rowNum+1, 1, 2));
		cell.setCellValue("Title");
		
		rowNum = 23; 
		row = propertyByTypeAndValueSheet.createRow(rowNum);
		
		cell = row.createCell(0);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(1);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(2);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(3);
		cell.setCellStyle(thinBorderBottom);
		
		row = propertyByTypeAndValueSheet.createRow(rowNum+1); 
		cell = row.createCell(0); 
		cell.setCellValue("Telephone Number");
		cell = row.createCell(2); 
		propertyByTypeAndValueSheet.addMergedRegion(new CellRangeAddress(rowNum+1, rowNum+1, 1, 2));
		cell.setCellValue("Date");

		rowNum = 26; 
		row = propertyByTypeAndValueSheet.createRow(rowNum);
		
		cell = row.createCell(0);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(1);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(2);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(3);
		cell.setCellStyle(thinBorderBottom);

		row = propertyByTypeAndValueSheet.createRow(rowNum+1); 
		propertyByTypeAndValueSheet.addMergedRegion(new CellRangeAddress(rowNum+1, rowNum+1, 0, 3));
		cell = row.createCell(0); 
		cell.setCellValue("Chief, Sheriff, Superintendent, or Commanding Officer");
		cell.setCellStyle(centeredStyle);
		

		rowNum = 29; 
		row = propertyByTypeAndValueSheet.createRow(rowNum);
		
		cell = row.createCell(0);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(2);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(3);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(5);
		cell.setCellStyle(thinBorderBottom);
		
		row = propertyByTypeAndValueSheet.createRow(rowNum+1);
		propertyByTypeAndValueSheet.addMergedRegion(new CellRangeAddress(rowNum+1, rowNum+1, 2, 3));
		cell = row.createCell(0); 
		cell.setCellValue("Month and Year of Report");
		cell.setCellStyle(centeredStyle);
		cell = row.createCell(2); 
		cell.setCellValue("Agency Identifier");
		cell.setCellStyle(centeredStyle);
		cell = row.createCell(5); 
		cell.setCellValue("Population");
		cell.setCellStyle(centeredStyle);
		
		rowNum = 32; 
		row = propertyByTypeAndValueSheet.createRow(rowNum);
		
		cell = row.createCell(0);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(1);
		cell.setCellStyle(thinBorderBottom);
		
		row = propertyByTypeAndValueSheet.createRow(rowNum+1); 
		propertyByTypeAndValueSheet.addMergedRegion(new CellRangeAddress(rowNum+1, rowNum+1, 0, 1));
		cell = row.createCell(0); 
		cell.setCellValue("Agency and State");
		cell.setCellStyle(centeredStyle);

		CellStyle centeredBordered = workbook.createCellStyle();
		centeredBordered.cloneStyleFrom(centeredStyle);
		centeredBordered.setBorderBottom(BorderStyle.THIN);
		centeredBordered.setBorderTop(BorderStyle.THIN);
		centeredBordered.setBorderLeft(BorderStyle.THIN);
		centeredBordered.setBorderRight(BorderStyle.THIN);
		
		cell=row.createCell(4); 
		XSSFRichTextString s1 = new XSSFRichTextString("DO NOT USE THIS SPACE");
		s1.applyFont(boldFont);
		cell.setCellValue(s1);
		cell.setCellStyle(centeredBordered);
		cell=row.createCell(5); 
		cell.setCellStyle(centeredBordered);
		propertyByTypeAndValueSheet.addMergedRegionUnsafe(new CellRangeAddress(rowNum+1, rowNum+1, 4, 5));
		
		rowNum += 2; 
		row = propertyByTypeAndValueSheet.createRow(rowNum++); 
		cell=row.createCell(4); 
		cell.setCellStyle(wrappedStyle);
		cell=row.createCell(5); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("INITIALs");
		row = propertyByTypeAndValueSheet.createRow(rowNum++); 
		cell=row.createCell(4); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("RECORDED");
		cell=row.createCell(5); 
		cell.setCellStyle(wrappedStyle);
		row = propertyByTypeAndValueSheet.createRow(rowNum++); 
		cell=row.createCell(4); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("EDITED");
		cell=row.createCell(5); 
		cell.setCellStyle(wrappedStyle);
		row = propertyByTypeAndValueSheet.createRow(rowNum++); 
		cell=row.createCell(4); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("ENTERED");
		cell=row.createCell(5); 
		cell.setCellStyle(wrappedStyle);
		row = propertyByTypeAndValueSheet.createRow(rowNum++); 
		cell=row.createCell(4); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("ADJUSTED");
		cell=row.createCell(5); 
		cell.setCellStyle(wrappedStyle);
		row = propertyByTypeAndValueSheet.createRow(rowNum++); 
		cell=row.createCell(4); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("CORRES.");
		cell=row.createCell(5); 
		cell.setCellStyle(wrappedStyle);
	}
    private void writePropertyTypeValueRow(XSSFSheet sheet, PropertyTypeValueRowName rowName,
			PropertyTypeValue propertyTypeValue, int rowNum, Font boldFont) {
    	Row row = sheet.createRow(rowNum);
    	int colNum = 0;
    	Cell cell = row.createCell(colNum++);
        
    	CellStyle defaultStyle = sheet.getWorkbook().createCellStyle();
    	defaultStyle.setBorderBottom(BorderStyle.THIN);
    	defaultStyle.setBorderTop(BorderStyle.THIN);
    	defaultStyle.setBorderRight(BorderStyle.THIN);
    	defaultStyle.setBorderLeft(BorderStyle.THIN);
    	
        CellStyle centeredStyle = sheet.getWorkbook().createCellStyle(); 
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);
        centeredStyle.setBorderBottom(BorderStyle.THIN);
        centeredStyle.setBorderTop(BorderStyle.THIN);
        centeredStyle.setBorderRight(BorderStyle.THIN);
        centeredStyle.setBorderLeft(BorderStyle.THIN);

        CellStyle greyForeGround = sheet.getWorkbook().createCellStyle();
        greyForeGround.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        greyForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        greyForeGround.setBorderBottom(BorderStyle.THIN);
        greyForeGround.setBorderTop(BorderStyle.THIN);
        greyForeGround.setBorderRight(BorderStyle.THIN);
        greyForeGround.setBorderLeft(BorderStyle.THIN);

        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 4, 5));
        switch(rowName){
    	case TOTAL: 
        	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
            XSSFRichTextString allBoldString = new XSSFRichTextString(rowName.getLabel());
            allBoldString.applyFont(boldFont);
            cell.setCellValue(allBoldString);
            cell.setCellStyle(centeredStyle);
            
    		cell = row.createCell(colNum++);
    		cell.setCellValue(rowName.getDataEntry());
    		cell.setCellStyle(greyForeGround);
    		cell.setCellType(CellType.STRING);
    		cell = row.createCell(colNum++);
    		cell.setCellType(CellType.STRING);
    		cell.setCellValue("$" + propertyTypeValue.getStolen());
    		cell.setCellStyle(centeredStyle);
    		cell = row.createCell(colNum++);
    		cell.setCellStyle(centeredStyle);
    		cell = row.createCell(colNum++);
    		cell.setCellType(CellType.STRING);
    		cell.setCellValue("$" + propertyTypeValue.getRecovered());
    		cell.setCellStyle(centeredStyle);
    		cell = row.createCell(colNum++);
    		cell.setCellStyle(centeredStyle);
    		break; 
    	default: 
    		cell.setCellValue(rowName.getLabel());
    		cell.setCellStyle(defaultStyle);
            CellStyle yellowForeGround = sheet.getWorkbook().createCellStyle();
            yellowForeGround.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            yellowForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            yellowForeGround.setBorderBottom(BorderStyle.THIN);
            yellowForeGround.setBorderTop(BorderStyle.THIN);
            yellowForeGround.setBorderRight(BorderStyle.THIN);
            yellowForeGround.setBorderLeft(BorderStyle.THIN);
            
    		cell = row.createCell(colNum++);
    		cell.setCellType(CellType.STRING);
    		cell.setCellStyle(greyForeGround);
    		cell.setCellValue(rowName.getDataEntry());
    		cell = row.createCell(colNum++);
    		cell.setCellStyle(yellowForeGround);
    		cell.setCellType(CellType.NUMERIC);
    		cell.setCellValue(propertyTypeValue.getStolen());
    		cell = row.createCell(colNum++);
    		cell.setCellStyle(yellowForeGround);
    		cell = row.createCell(colNum++);
    		cell.setCellStyle(yellowForeGround);
    		cell.setCellType(CellType.NUMERIC);
    		cell.setCellValue(propertyTypeValue.getRecovered());
    		cell = row.createCell(colNum++);
    		cell.setCellStyle(yellowForeGround);
    	}
	}
	private int createPropertyByTypeAndValueHeaderRow(XSSFSheet sheet, int rowNum, Font boldFont,
			XSSFFont normalWeightFont) {
		sheet.addMergedRegion(new CellRangeAddress(1, 4, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(1, 4, 1, 1));
		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(0);
		
        CellStyle column0Style = sheet.getWorkbook().createCellStyle();
        column0Style.setWrapText(true);
        column0Style.setAlignment(HorizontalAlignment.CENTER);
        column0Style.setVerticalAlignment(VerticalAlignment.CENTER);
        column0Style.setBorderBottom(BorderStyle.THIN);
        column0Style.setBorderTop(BorderStyle.THIN);
        column0Style.setBorderRight(BorderStyle.THIN);
        column0Style.setBorderLeft(BorderStyle.THIN);
        

		cell.setCellStyle(column0Style);
		cell.setCellValue("Type of Property\n (1)");

		
		Cell cell1 = row.createCell(1);
		XSSFCellStyle column1Style = sheet.getWorkbook().createCellStyle(); 
		column1Style.setRotation((short)90);
		column1Style.setVerticalAlignment(VerticalAlignment.CENTER);
		column1Style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		column1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		column1Style.setBorderBottom(BorderStyle.THIN);
		column1Style.setBorderTop(BorderStyle.THIN);
		column1Style.setBorderRight(BorderStyle.THIN);
		column1Style.setBorderLeft(BorderStyle.THIN);
		cell1.setCellValue("Data Entry");
		cell1.setCellStyle(column1Style);
		
		sheet.addMergedRegion(new CellRangeAddress(1,1,2,5));
		Cell cell2 = row.createCell(2);
		cell2.setCellStyle(column0Style);
		cell2.setCellValue("Monetary Value of Property Stolen in Your Jurisdiction");
		
		row = sheet.createRow(rowNum++);
		sheet.addMergedRegion(new CellRangeAddress(2,4,2,3));
		cell2 = row.createCell(2);
		cell2.setCellStyle(column0Style);
		XSSFRichTextString s1 = new XSSFRichTextString("Stolen \n (2)");
		cell2.setCellValue(s1);
		sheet.addMergedRegion(new CellRangeAddress(2,4,4,5));
		
		Cell cell4 = row.createCell(4);
		cell4.setCellStyle(column0Style);
		s1 = new XSSFRichTextString("Recovered \n (3)");
		cell4.setCellValue(s1);
		Cell cell5 = row.createCell(5);
		cell5.setCellStyle(column0Style);
		
		RegionUtil.setBorderLeft(BorderStyle.THIN.getCode(), new CellRangeAddress(1, 4, 0, 0), sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(1, 4, 0, 0), sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(1,1,2,5), sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), new CellRangeAddress(1,1,2,5), sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN.getCode(), new CellRangeAddress(2,4,4,5), sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN.getCode(), new CellRangeAddress(2,4,2,3), sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(2,4,4,5), sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), new CellRangeAddress(2,4,4,5), sheet);
				
		return rowNum;
	}
    
	private int createPropertyByTypeAndValueTitleRow(XSSFSheet sheet, int rowNum, CellStyle cs, Font boldFont, XSSFFont normalWeightFont) {
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		Cell cell = row.createCell(0);
		
        CellStyle centered = sheet.getWorkbook().createCellStyle();
        centered.setAlignment(HorizontalAlignment.CENTER);
        centered.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(centered);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("PROPERTY BY TYPE AND VALUE");
		s1.applyFont(boldFont);
		cell.setCellValue(s1);
		
		RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), new CellRangeAddress(0, 0, 0, 5), sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN.getCode(), new CellRangeAddress(0, 0, 0, 5), sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN.getCode(), new CellRangeAddress(0, 0, 0, 5), sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(0, 0, 0, 5), sheet);

		return rowNum;
	}
    

	private int createPropertyStolenTitleRow(XSSFSheet sheet, int rowNum, Font boldFont, XSSFFont normalWeightFont) {
		Row row = sheet.createRow(rowNum++);
		row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
		Cell cell = row.createCell(0);
		
		CellStyle centered = sheet.getWorkbook().createCellStyle();
		centered.setAlignment(HorizontalAlignment.CENTER);
		centered.setVerticalAlignment(VerticalAlignment.BOTTOM);
		cell.setCellStyle(centered);
		
		XSSFRichTextString s1 = new XSSFRichTextString("PROPERTY STOLEN BY CLASSIFICATION");
		s1.applyFont(boldFont);
		cell.setCellValue(s1);
		
		return rowNum;
	}
	
	
	private void createReturnASupplementTextSheet(XSSFSheet sheet, int rowNum, Font boldFont,
			XSSFFont normalWeightFont) {
		
        CellStyle centered = sheet.getWorkbook().createCellStyle();
        centered.setAlignment(HorizontalAlignment.CENTER);
        centered.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle wrapped = sheet.getWorkbook().createCellStyle();
        wrapped.setWrapText(true);;
        
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		cell.setCellStyle(centered);
		 
        XSSFFont normalWeightItalicFont = sheet.getWorkbook().createFont();
        normalWeightItalicFont.setBold(false);
        normalWeightItalicFont.setItalic(true);
        
		XSSFRichTextString s1 = new XSSFRichTextString("SUPPLEMENT TO RETURN A");
		s1.applyFont(boldFont);
		cell.setCellValue(s1);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
		
		row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
		cell = row.createCell(0);
		 
		s1 = new XSSFRichTextString("MONTHLY RETURN OF OFFENSES KNOWN TO THE POLICE");
		s1.applyFont(boldFont);
		cell.setCellValue(s1);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));
		cell.setCellStyle(centered);
		
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		cell.setCellStyle(wrapped);
		
		XSSFRichTextString s2 = new XSSFRichTextString();
		s2.append("This report is authorized by law Title 28, Section 534, U.S. Code. Your cooperation in completing this form with the ", normalWeightFont); 
		s2.append("Return A ", normalWeightItalicFont);
		s2.append("will assist the FBI in compiling timely, comprehensive, and accurate data. Please submit this form monthly,"
				+ "by the seventh day after the close of the month, and any questions to the FBI, Criminal Justice Information Services Division, "
				+ "Attention: Uniform Crime Reports/Module E-3, 1000 Custer Hollow Road, Clarksburg, West Virginia 26306; telephone "
				+ "304-625-4830, facsimile 304-625-3566. Under the Paperwork Reduction Act, you are not required to complete the form unless"
				+ "it contains a valid OMB control number. The form takes approximately 11 minutes to complete. \n\n"
				+ "This form deals with the nature of crime and the monetary value of property stolen and recovered. The total offenses recorded"
				+ "on this form, page 2, should be the same as the number of actual offenses listed in Column 4 of the ", normalWeightFont);
		s2.append("Return A ", normalWeightItalicFont);
		s2.append("for each crime class\n"
				+ "Include attempted crimes on this form, but do not include unfounded offenses. If you cannot complete the report in all areas,"
				+ "please record as much information as is available. Tally sheets will be sent upon request. ", normalWeightFont); 
		
		cell.setCellValue(s2);
		sheet.addMergedRegion(new CellRangeAddress(2, 12, 0, 8));
		
		sheet.setFitToPage(true);
		PrintSetup ps = sheet.getPrintSetup();
		ps.setFitWidth( (short) 1);
		ps.setFitHeight( (short) 0);
	}
	
	public void exportReturnAForm(ReturnAForm returnAForm){
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        XSSFSheet sheet = workbook.createSheet("Return A Form");
    	
        int rowNum = 0;
        log.info("Write to the excel file");
        CellStyle wrappedStyle = workbook.createCellStyle();
        wrappedStyle.setWrapText(true);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        XSSFFont normalWeightFont = workbook.createFont();
        normalWeightFont.setBold(false);
        
        Font underlineFont = workbook.createFont();
        underlineFont.setUnderline(Font.U_SINGLE);
        
    	rowNum = createReturnATitleRow(sheet, rowNum, wrappedStyle, boldFont, normalWeightFont);
		rowNum = createReturnATableHeaderRow(sheet, rowNum, boldFont, normalWeightFont);

        for (ReturnARowName rowName: ReturnARowName.values()){
        	writeReturnARow(sheet, rowName, returnAForm.getRows()[rowName.ordinal()], rowNum++, boldFont);
        }

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		
		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(0);
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("CHECKING ANY OF THE APPROPRIATE BLOCKS BELOW WILL ELIMINATE YOUR NEED OF SUBMIT REPORTS WHEN \n"
				+ "THE VALUES ARE ZERO. THIS WILL ALSO AID THE NATIONAL PROGRAM IN ITS QUALITY CONTROL EFFORTS  ");
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1 , 0, 4));
		cell = row.createCell(5); 
        CellStyle centered = workbook.createCellStyle();
        centered.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(centered);
        cell.setCellValue("DO NOT USE THIS SPACE");
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+2, 5, 6));
		
		rowNum = 30; 
		row = sheet.createRow(rowNum++);
		cell=row.createCell(0);
		String text = "\t\tNO SUPPLEMENTARY HOMICIDE REPORT SUBMITTED\n"
				+ "\t\t\t\t\t SINCE NO MURDERS, JUSTIFIEABLE HOMICIDE, \n"
				+ "\t\t\t\t\t OR MANSLAUGHTERS BY NEGLIGENCE OCCURRED IN\n"
				+ "\t\t\t\t\t THIS JURISDICTION DURING THE MONTH";
		addCheckBoxAndWrappedText(workbook, wrappedStyle, cell, text);
		sheet.addMergedRegion(new CellRangeAddress(30, 34, 0, 1));
		cell=row.createCell(2);
		addCheckBoxAndWrappedText(workbook, wrappedStyle, cell, 
				"\t\tNO AGE, SEX, AND RACE OF PERSONS ARRESTED UNDER\n"
			  + "\t\t\t\t\t 18 YEARS OF AGE REPORT SINCE NO ARRESTS \n"
			  + "\t\t\t\t\t OF PERSONS WITHIN THIS AGE GROUP.");
		sheet.addMergedRegion(new CellRangeAddress(30, 34, 2, 4));
		
		row = sheet.createRow(rowNum);
		cell = row.createCell(5); 
		cell.setCellValue("");
		sheet.addMergedRegion(new CellRangeAddress(31, 32, 5, 5));
		cell = row.createCell(6); 
		
		cell.setCellValue("INITIALS");
		cell.setCellStyle(centered);
		sheet.addMergedRegion(new CellRangeAddress(31, 32, 6, 6));

		int initialTableRowNum = 33; 
		addInitialTableRow(sheet,initialTableRowNum, "RECORDED", null );
		initialTableRowNum += 2; 
		initialTableRowNum += 2; 
		addInitialTableRow(sheet, initialTableRowNum, "ENTERED", null);
		initialTableRowNum += 2; 
		initialTableRowNum += 2; 
		addInitialTableRow(sheet, initialTableRowNum, "CORRES", null);
		
		rowNum = 35; 
		row = sheet.createRow(rowNum);
		cell=row.createCell(0);
		addCheckBoxAndWrappedText(workbook, wrappedStyle, cell, 
				"\t\tNO SUPPLEMENT TO RETURN A REPORT SINCE NO	\n"
			  + "\t\t\t\t\t CRIME OFFENSES OR RECOVERY OF PROPERTY \n"
			  + "\t\t\t\t\t REPORTED DURING THE MONTH.");
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+3, 0, 1));
		cell=row.createCell(2);
		addCheckBoxAndWrappedText(workbook, wrappedStyle, cell, 
				"\t\tNO AGE, SEX, AND RACE OF PERSONS ARRESTED UNDER\n"
			  + "\t\t\t\t\t 18 YEARS OF AGE AND OVER REPORT SINCE NO ARRESTS \n"
			  + "\t\t\t\t\t OF PERSONS WITHIN THIS AGE GROUP.");
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+3, 2, 4));
		addInitialTableRow(sheet, rowNum, "EDITED", row);
		
		rowNum = 39;
		row = sheet.createRow(rowNum);
		cell=row.createCell(0);
		addCheckBoxAndWrappedText(workbook, wrappedStyle, cell, 
				"\t\tNO LAW ENFORCEMENT OFFICERS KILLED OR \n"
			  + "\t\t\t\t\t ASSAULTED OR KILLED REPORT SINCE NONE OF THE\n"
			  + "\t\t\t\t\t OFFICERS WERE ASSAULTED DURING THE MONTH.");
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+3, 0, 1));
		cell=row.createCell(2);
		addCheckBoxAndWrappedText(workbook, wrappedStyle, cell, 
				"\t\tNO MONTHLY RETURN OF ARSON OFFENSES KNOWN \n"
			  + "\t\t\t\t\t TO LAW ENFORCEMENT REPORT SINCE NO ARSONS  \n"
			  + "\t\t\t\t\t OCCURRED.");
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+3, 2, 4));
		addInitialTableRow(sheet, rowNum, "ADJUSTED", row);
		
		rowNum = 44; 
		row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellValue(returnAForm.getMonthString() + "/" + returnAForm.getYear());
		CellStyle thinBorderBottom = workbook.createCellStyle();
		thinBorderBottom.setBorderBottom(BorderStyle.THIN);
		thinBorderBottom.setAlignment(HorizontalAlignment.CENTER);
		cell.setCellStyle(thinBorderBottom);
		
		cell=row.createCell(3); 
		cell.setCellStyle(thinBorderBottom);
		cell.setCellValue(returnAForm.getOri());
		cell=row.createCell(4); 
		cell.setCellStyle(thinBorderBottom);
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 3, 4));
		cell=row.createCell(6); 
		cell.setCellStyle(thinBorderBottom);
		cell.setCellValue(returnAForm.getPopulationString());
		
		CellStyle topCentered = workbook.createCellStyle();
		topCentered.setVerticalAlignment(VerticalAlignment.TOP);
		topCentered.setAlignment(HorizontalAlignment.CENTER);
		rowNum = 45; 
		row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellStyle(topCentered);
		cell.setCellValue("Month and Year");
		
		cell = row.createCell(3);
		cell.setCellStyle(topCentered);
		cell.setCellValue("Agency Identifier");
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 3, 4));
		
		cell = row.createCell(6);
		cell.setCellStyle(topCentered);
		cell.setCellValue("Population");
		
		rowNum = 47; 
		row = sheet.createRow(rowNum);
		cell = row.createCell(4);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(5);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(6);
		cell.setCellStyle(thinBorderBottom);
		cell.setCellValue(LocalDate.now().toString());
		
		rowNum = 48; 
		row = sheet.createRow(rowNum);
		cell = row.createCell(6);
		cell.setCellStyle(topCentered);
		cell.setCellValue("Date");
		rowNum = 50; 
		row = sheet.createRow(rowNum);
		cell = row.createCell(4);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(5);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(6);
		cell.setCellStyle(thinBorderBottom);
		rowNum = 51; 
		row = sheet.createRow(rowNum);
		cell = row.createCell(4);
		cell.setCellStyle(topCentered);
		cell.setCellValue("Prepared By");
		cell = row.createCell(6);
		cell.setCellStyle(topCentered);
		cell.setCellValue("Title");
		
		rowNum = 53; 
		row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellStyle(thinBorderBottom);
		cell.setCellValue(returnAForm.getAgencyName() + ", " + returnAForm.getStateCode());
		cell = row.createCell(4);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(5);
		cell.setCellStyle(thinBorderBottom);
		cell = row.createCell(6);
		cell.setCellStyle(thinBorderBottom);
		
		rowNum = 54;
		row = sheet.createRow(rowNum);
		cell = row.createCell(0);
		cell.setCellStyle(topCentered);
		cell.setCellValue("Agency and State");
		cell = row.createCell(4);
		cell.setCellStyle(topCentered);
		cell.setCellValue("Chief, Commisioner, Sheriff, or Superintendent");
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 4, 6));
		
		RegionUtil.setBorderBottom(BorderStyle.THICK.getCode(), new CellRangeAddress(1, 1, 0, 6), sheet);
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(1, 1, 0, 6), sheet);
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(4, 4, 0, 6), sheet);
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(7, 7, 0, 6), sheet);
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(12, 12, 0, 6), sheet);
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(18, 18, 0, 6), sheet);
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(22, 22, 0, 6), sheet);
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(23, 23, 0, 6), sheet);
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(27, 27, 0, 6), sheet);
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(28, 28, 0, 6), sheet);
		RegionUtil.setBorderLeft(BorderStyle.THICK.getCode(), new CellRangeAddress(28, 42, 5, 5), sheet);
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(43, 43, 0, 6), sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM.getCode(), new CellRangeAddress(55, 55, 0, 6), sheet);
		RegionUtil.setBorderTop(BorderStyle.MEDIUM.getCode(), new CellRangeAddress(0, 0, 0, 6), sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM.getCode(), new CellRangeAddress(0, 55, 0, 0), sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM.getCode(), new CellRangeAddress(0, 55, 6, 6), sheet);
		
		Arrays.asList(3, 5, 6, 8, 9, 10, 11, 13, 14, 15, 16, 17, 19, 20, 21, 24, 25, 26)
			.forEach(item-> RegionUtil.setBorderTop(BorderStyle.THIN.getCode(), new CellRangeAddress(item, item, 0, 6), sheet));
		
		for (int column = 0;  column < 6; column ++){
			RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(1, 27, column, column), sheet);
		}

		for (int i = 31; i < 42; i+=2){
			RegionUtil.setBorderTop(BorderStyle.THIN.getCode(), new CellRangeAddress(i, i, 5, 6), sheet);
		}
		
		RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(31, 42, 5, 5), sheet);
		
        try {
        	String fileName = appProperties.getReturnAFormOutputPath() + "/ReturnA-" + returnAForm.getOri() + "-" + returnAForm.getYear() + "-" + StringUtils.leftPad(String.valueOf(returnAForm.getMonth()), 2, '0') + ".xlsx"; 
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
            System.out.println("The return A form is writen to fileName: " + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

	private void addInitialTableRow(XSSFSheet sheet, int rowNum, String label, Row row) {
		XSSFCellStyle rightAligned = sheet.getWorkbook().createCellStyle(); 
		rightAligned.setAlignment(HorizontalAlignment.RIGHT);
		
		if (row == null){
			row = sheet.createRow(rowNum);
		}
		Cell cell = row.createCell(5); 
		cell.setCellValue(label);
		cell.setCellStyle(rightAligned);
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, 5, 5));
		cell = row.createCell(6); 
		cell.setCellValue("");
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, 6, 6));
	}

	private void addCheckBoxAndWrappedText(XSSFWorkbook workbook, CellStyle wrappedStyle, Cell cell, String text) {
		wrappedStyle.setVerticalAlignment(VerticalAlignment.TOP);
		cell.setCellStyle(wrappedStyle);
		XSSFRichTextString s1 = new XSSFRichTextString("\u25A1");
        Font bigFont = workbook.createFont();
        bigFont.setFontHeightInPoints((short)16);
        s1.applyFont(bigFont);
        s1.append(text);;
		cell.setCellValue(s1);
	}

	private int createReturnATitleRow(XSSFSheet sheet, int rowNum, CellStyle cs, Font boldFont, XSSFFont normalWeightFont) {
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((4*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		cell.setCellStyle(cs);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("RETURN A - MONTHLY RETURN OF OFFENSES KNOWN TO THE POLICE");
		s1.applyFont(boldFont);
		s1.append("\nThis report is authorized by law Title 28, Section 534, U.S.Code. While you are not required to respond, your "
				+ "\ncooperation in forwarding this report by the seventh day after the close of the month to Uniform Crime Reports, Federal Bureau of Investigation, "
				+ "\nClarksburg, WV, 26306, will assist in compiling comprehensive, accurate national crime figures on a timely basis. ", normalWeightFont);
		cell.setCellValue(s1);
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
		return rowNum;
	}
    
	private int createReturnATableHeaderRow(XSSFSheet sheet, int rowNum, Font boldFont, XSSFFont normalWeightFont) {
        
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((5*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		
        CellStyle column0Style = sheet.getWorkbook().createCellStyle();
        column0Style.setWrapText(true);
        column0Style.setAlignment(HorizontalAlignment.CENTER);
        column0Style.setVerticalAlignment(VerticalAlignment.TOP);

		cell.setCellStyle(column0Style);
		cell.setCellValue("1\n\n CLASSIFICATION OF OFFENSES");
		
		Cell cell1 = row.createCell(1);
		XSSFCellStyle column1Style = sheet.getWorkbook().createCellStyle(); 
		column1Style.setRotation((short)90);
		column1Style.setVerticalAlignment(VerticalAlignment.CENTER);
		column1Style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		column1Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell1.setCellValue("Data Entry");
		cell1.setCellStyle(column1Style);
		
        Font underlineFont = sheet.getWorkbook().createFont();
        underlineFont.setUnderline(Font.U_SINGLE);

		Cell cell2 = row.createCell(2);
		cell2.setCellStyle(column0Style);
		XSSFRichTextString s1 = returnStringWithSpecialFontSubString(
				"2 \n Offenses reported\n or know to police \n (include \"unfounded\" \n and attempts)", 
				"Offenses",
				underlineFont);
		cell2.setCellValue(s1);
		
		Cell cell3 = row.createCell(3);
		cell3.setCellStyle(column0Style);
		s1 = returnStringWithSpecialFontSubString(
				"3 \n Unfounded, i.e.\n false or baseless \n complaints", 
				"false or baseless",
				underlineFont);
		cell3.setCellValue(s1);
		Cell cell4 = row.createCell(4);
		cell4.setCellStyle(column0Style);
		s1 = returnStringWithSpecialFontSubString(
				"4 \n Number of actual \n offenses ( column 2 \n minus column 3) \n (include attempts)", 
				"offenses",
				underlineFont);
		cell4.setCellValue(s1);
		
		Cell cell5 = row.createCell(5);
		cell5.setCellStyle(column0Style);
		cell5.setCellValue("5 \n Total Offenses\n cleared by offenses or \n exceptional means \n (include column 6)");
		
		Cell cell6 = row.createCell(6);
		cell6.setCellStyle(column0Style);
		cell6.setCellValue("6 \n Number of clearances\n involving only \n persons under 18 \n years of age)");
		
		return rowNum;
	}

	private XSSFRichTextString returnStringWithSpecialFontSubString(
			String string,  String subString, Font underlineFont) {
        XSSFRichTextString s1 = new XSSFRichTextString(string);
        
        int startIndex = string.indexOf(subString); 
        int endIndex = startIndex + subString.length(); 
        s1.applyFont(startIndex, endIndex, underlineFont);
		return s1;
	}
	
    private void writeReturnARow(XSSFSheet sheet, ReturnARowName rowName, ReturnAFormRow returnAFormRow, int rowNum, Font boldFont) {
    	Row row = sheet.createRow(rowNum);
    	int colNum = 0;
    	Cell cell = row.createCell(colNum++);
    	CellStyle wrapStyle = sheet.getWorkbook().createCellStyle();
    	wrapStyle.setWrapText(true);
        
        switch(rowName){
    	case MURDER_NONNEGLIGENT_HOMICIDE: 
    	case LARCENY_THEFT_TOTAL:
        	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
            cell.setCellStyle(wrapStyle);
            XSSFRichTextString s1 = new XSSFRichTextString(rowName.getLabel());
            s1.applyFont(0, rowName.getLabel().indexOf('\n'), boldFont);
            
            int criminalHomicideIndex = rowName.getLabel().indexOf(CRIMINAL_HOMICIDE);
            if (criminalHomicideIndex > 0){
                Font underlineFont = sheet.getWorkbook().createFont();
                underlineFont.setUnderline(Font.U_SINGLE);
                underlineFont.setBold(true);
                int endIndex = criminalHomicideIndex + CRIMINAL_HOMICIDE.length();
                s1.applyFont(criminalHomicideIndex, endIndex, underlineFont);
            }
            cell.setCellValue(s1);
    		break; 
            
    	case FORCIBLE_RAPE_TOTAL: 
    	case ROBBERY_TOTAL: 
    	case ASSAULT_TOTAL: 
    	case BURGLARY_TOTAL: 
    	case MOTOR_VEHICLE_THEFT_TOTAL: 
    	case GRAND_TOTAL: 
        	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
            XSSFRichTextString allBoldString = new XSSFRichTextString(rowName.getLabel());
            allBoldString.applyFont(boldFont);
            cell.setCellValue(allBoldString);
    		break; 
    	default: 
    		cell.setCellValue(rowName.getLabel());
    	}
        CellStyle greyForeGround = sheet.getWorkbook().createCellStyle();
        greyForeGround.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        greyForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cell = row.createCell(colNum++);
		cell.setCellValue(rowName.getDataEntry());
		cell.setCellStyle(greyForeGround);
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) returnAFormRow.getReportedOffenses());
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) returnAFormRow.getUnfoundedOffenses());
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) returnAFormRow.getActualOffenses());
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) returnAFormRow.getClearedOffenses());
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) returnAFormRow.getClearanceInvolvingOnlyJuvenile());

	}

	public static void main(String[] args) {

//        AsrAdult [rows=
//        	[Summary [reportedOffenses=4, unfoundedOffenses=0, actualOffenses=4, clearedOffense=3, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=0, unfoundedOffenses=0, actualOffenses=0, clearedOffense=0, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=24, unfoundedOffenses=0, actualOffenses=24, clearedOffense=7, clearanceInvolvingJuvenile=0], 
//          Summary [reportedOffenses=24, unfoundedOffenses=0, actualOffenses=24, clearedOffense=7, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=0, unfoundedOffenses=0, actualOffenses=0, clearedOffense=0, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=74, unfoundedOffenses=0, actualOffenses=74, clearedOffense=11, clearanceInvolvingJuvenile=1], 
//        	Summary [reportedOffenses=15, unfoundedOffenses=0, actualOffenses=15, clearedOffense=1, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=6, unfoundedOffenses=0, actualOffenses=6, clearedOffense=1, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=12, unfoundedOffenses=0, actualOffenses=12, clearedOffense=2, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=41, unfoundedOffenses=0, actualOffenses=41, clearedOffense=7, clearanceInvolvingJuvenile=1], 
//        	Summary [reportedOffenses=119, unfoundedOffenses=0, actualOffenses=119, clearedOffense=39, clearanceInvolvingJuvenile=2], 
//        	Summary [reportedOffenses=12, unfoundedOffenses=0, actualOffenses=12, clearedOffense=4, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=22, unfoundedOffenses=0, actualOffenses=22, clearedOffense=6, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=49, unfoundedOffenses=0, actualOffenses=49, clearedOffense=20, clearanceInvolvingJuvenile=1], 
//        	Summary [reportedOffenses=36, unfoundedOffenses=0, actualOffenses=36, clearedOffense=9, clearanceInvolvingJuvenile=1], 
//        	Summary [reportedOffenses=0, unfoundedOffenses=0, actualOffenses=0, clearedOffense=0, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=323, unfoundedOffenses=0, actualOffenses=323, clearedOffense=30, clearanceInvolvingJuvenile=4], 
//        	Summary [reportedOffenses=111, unfoundedOffenses=0, actualOffenses=111, clearedOffense=5, clearanceInvolvingJuvenile=2], 
//        	Summary [reportedOffenses=119, unfoundedOffenses=0, actualOffenses=119, clearedOffense=2, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=44, unfoundedOffenses=0, actualOffenses=44, clearedOffense=3, clearanceInvolvingJuvenile=1], 
//        	Summary [reportedOffenses=1751, unfoundedOffenses=0, actualOffenses=1751, clearedOffense=117, clearanceInvolvingJuvenile=21], 
//        	Summary [reportedOffenses=0, unfoundedOffenses=0, actualOffenses=0, clearedOffense=0, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=0, unfoundedOffenses=0, actualOffenses=0, clearedOffense=0, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=0, unfoundedOffenses=0, actualOffenses=0, clearedOffense=0, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=0, unfoundedOffenses=0, actualOffenses=0, clearedOffense=0, clearanceInvolvingJuvenile=0], 
//        	Summary [reportedOffenses=33, unfoundedOffenses=0, actualOffenses=33, clearedOffense=1, clearanceInvolvingJuvenile=1], 
//        	Summary [reportedOffenses=2328, unfoundedOffenses=0, actualOffenses=2328, clearedOffense=208, clearanceInvolvingJuvenile=29]], 
//        	ori=HI0020000, agencyName=Honolulu Police Department, stateName=Hawaii, month=3, year=2017]
        ReturnAForm returnAForm = new ReturnAForm("HI0020000", 2017, 3);
        ReturnAFormRow[] rows = new ReturnAFormRow[]{
        		new ReturnAFormRow(4, 0, 3, 0), 
        		new ReturnAFormRow(0, 0, 0, 0), 
        		new ReturnAFormRow(24, 0, 7, 0), 
        		new ReturnAFormRow(24, 0, 7, 0), 
        		new ReturnAFormRow(0, 0, 0, 0), 
        		new ReturnAFormRow(74, 0, 11, 1), 
        		new ReturnAFormRow(15, 0, 1, 0), 
        		new ReturnAFormRow(6, 0, 1, 0), 
        		new ReturnAFormRow(12, 0, 2, 0), 
        		new ReturnAFormRow(41, 0, 7, 1), 
        		new ReturnAFormRow(119, 0, 39, 2), 
        		new ReturnAFormRow(12, 0, 4, 0), 
        		new ReturnAFormRow(22, 0, 6, 0), 
        		new ReturnAFormRow(49, 0, 20, 1), 
        		new ReturnAFormRow(36, 0, 9, 1), 
        		new ReturnAFormRow(0, 0, 0, 0), 
        		new ReturnAFormRow(323, 0, 30, 4), 
        		new ReturnAFormRow(111, 0, 5, 2), 
        		new ReturnAFormRow(119, 0, 2, 0), 
        		new ReturnAFormRow(44, 0, 3, 1), 
        		new ReturnAFormRow(1751, 0, 117, 21), 
        		new ReturnAFormRow(0, 0, 0, 0), 
        		new ReturnAFormRow(0, 0, 0, 0), 
        		new ReturnAFormRow(0, 0, 0, 0), 
        		new ReturnAFormRow(0, 0, 0, 0), 
        		new ReturnAFormRow(2295, 0, 207, 28) };
        returnAForm.setRows(rows);
        returnAForm.setAgencyName("Honolulu Police Department");
        returnAForm.setStateName("Hawaii");
        returnAForm.setStateCode("HI");
        
        ExcelExporter exporter = new ExcelExporter(); 
        exporter.exportReturnAForm(returnAForm);
        
    }
	
}