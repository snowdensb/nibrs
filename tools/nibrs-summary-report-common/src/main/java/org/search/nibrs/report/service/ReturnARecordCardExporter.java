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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.search.nibrs.model.reports.ReturnARecordCard;
import org.search.nibrs.model.reports.ReturnARecordCardRow;
import org.search.nibrs.model.reports.ReturnARecordCardRowName;
import org.search.nibrs.report.SummaryReportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnARecordCardExporter {
	private static final Log log = LogFactory.getLog(ReturnARecordCardExporter.class);
	
	@Autowired
	private SummaryReportProperties appProperties;
	private CellStyle centeredStyle; 
	private CellStyle rightAlignedStyle;
	private CellStyle wrappedStyle;
	private CellStyle bottomBorderStyle;
	private XSSFCellStyle defaultStyle;
	private XSSFCellStyle grayForeGround;
	private XSSFCellStyle blueForeGround;
	private XSSFCellStyle blueBoldForeGround;
	private XSSFCellStyle blueLeftStyle;
	private XSSFCellStyle blueBoldCenteredStyle;
	private XSSFCellStyle blueBoldTopCenteredStyle;
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, YYYY"); 
    public void exportReturnARecordCard(ReturnARecordCard returnARecordCard){
        XSSFWorkbook workbook = createReturnARecordCardWorkbook(returnARecordCard);
		
        try {
        	String fileName = appProperties.getSummaryReportOutputPath() + "/ReturnARecordCard-" + returnARecordCard.getOri() + "-" + returnARecordCard.getYear()  + ".xlsx"; 
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
            log.info("The return A record card is writen to fileName: " + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
	public XSSFWorkbook createReturnARecordCardWorkbook(ReturnARecordCard returnARecordCard) {
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFFont normalWeightFont = workbook.createFont();
        normalWeightFont.setBold(false);
        normalWeightFont.setFontName("Tahoma");
        normalWeightFont.setFontHeightInPoints(Short.valueOf("8"));
        
        XSSFFont normalCalibriFont = workbook.createFont();
        normalCalibriFont.setBold(false);
        normalCalibriFont.setFontName("Calibri");
        normalCalibriFont.setFontHeightInPoints(Short.valueOf("8"));

        int rowNum = 0;
        log.info("Write to the excel file");
        log.debug("Return A record card " + returnARecordCard);
        defaultStyle = workbook.createCellStyle();
        defaultStyle.setWrapText(true);
        defaultStyle.setBorderBottom(BorderStyle.THIN);
        defaultStyle.setBorderTop(BorderStyle.THIN);
        defaultStyle.setBorderRight(BorderStyle.THIN);
        defaultStyle.setBorderLeft(BorderStyle.THIN);
        defaultStyle.setFont(normalWeightFont);
        
        bottomBorderStyle = workbook.createCellStyle(); 
        bottomBorderStyle.setAlignment(HorizontalAlignment.CENTER);
        bottomBorderStyle.setBorderBottom(BorderStyle.THIN);
        centeredStyle = workbook.createCellStyle();
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);
        rightAlignedStyle = workbook.createCellStyle();
        rightAlignedStyle.setAlignment(HorizontalAlignment.RIGHT);
        
        Font boldFont = workbook.createFont();
        boldFont.setFontName("Tahoma");
        boldFont.setFontHeightInPoints(Short.parseShort("8"));
        boldFont.setBold(true);
        
        grayForeGround = workbook.createCellStyle();
        grayForeGround.cloneStyleFrom(defaultStyle);
        XSSFColor greyColor = new XSSFColor(new java.awt.Color(220,220,220));
        grayForeGround.setFillForegroundColor(greyColor);
        grayForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        grayForeGround.setFont(boldFont);
        
        blueForeGround = workbook.createCellStyle(); 
        blueForeGround.cloneStyleFrom(defaultStyle);
        XSSFColor blueColor = new XSSFColor(new java.awt.Color(207,224,241));
        blueForeGround.setFillForegroundColor(blueColor);
        blueForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        blueBoldForeGround = workbook.createCellStyle(); 
        blueBoldForeGround.cloneStyleFrom(blueForeGround);
        blueBoldForeGround.setFont(boldFont);
        
		blueBoldCenteredStyle = workbook.createCellStyle();
		blueBoldCenteredStyle.cloneStyleFrom(blueBoldForeGround);
		blueBoldCenteredStyle.setAlignment(HorizontalAlignment.CENTER);
		
		blueLeftStyle = workbook.createCellStyle();
		blueLeftStyle.cloneStyleFrom(blueForeGround);
		blueLeftStyle.setAlignment(HorizontalAlignment.LEFT);
		
		blueBoldTopCenteredStyle = workbook.createCellStyle();
		blueBoldTopCenteredStyle.cloneStyleFrom(blueBoldCenteredStyle);
		blueBoldTopCenteredStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
        Font underlineFont = workbook.createFont();
        underlineFont.setUnderline(Font.U_SINGLE);
        
    	createReturnARecordCardSheet(workbook, rowNum, boldFont, normalWeightFont, returnARecordCard);
		return workbook;
	}

	private void createReturnARecordCardSheet(XSSFWorkbook workbook, int rowNum, Font boldFont, 
			XSSFFont normalWeightFont, ReturnARecordCard returnARecordCard) {
        XSSFSheet sheet = workbook.createSheet();
        CellStyle wrappedStyle = workbook.createCellStyle();
        wrappedStyle.setWrapText(true);
        
    	rowNum = createReturnARecordTitleRow(sheet, rowNum, wrappedStyle, boldFont, normalWeightFont);
    	rowNum = createMetaDataRows(sheet, rowNum, returnARecordCard);
    	rowNum = createHeaderRow(sheet, rowNum, boldFont, normalWeightFont);
    	
    	int grandTotalRowNum = rowNum; 
    	
    	creatRowHeaders(sheet, rowNum, boldFont, normalWeightFont);

    	rowNum = grandTotalRowNum; 
    	
        for (ReturnARecordCardRowName rowName: ReturnARecordCardRowName.values()){
        	writeReportedOffensesRow(sheet, rowName, returnARecordCard.getRows()[rowName.ordinal()], rowNum, boldFont);
        	rowNum ++;
        }

	}
	
	private void writeReportedOffensesRow(XSSFSheet sheet, ReturnARecordCardRowName rowName,
			ReturnARecordCardRow returnARecordCardRow, int rowNum, Font boldFont) {
        CellStyle rightGrayStyle = sheet.getWorkbook().createCellStyle(); 
        rightGrayStyle.cloneStyleFrom(grayForeGround);
        rightGrayStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle rightDefaultStyle = sheet.getWorkbook().createCellStyle(); 
        rightDefaultStyle.cloneStyleFrom(defaultStyle);
        rightDefaultStyle.setAlignment(HorizontalAlignment.RIGHT);
        
    	Row row = sheet.getRow(rowNum);
        

		switch (rowName) {
		case GRAND_TOTAL:
		case VIOLENT_TOTAL: 
		case MURDER_SUBTOTAL: 
		case RAPE_SUBTOTAL: 
		case ROBBERY_SUBTOTAL: 
		case ASSAULT_SUBTOTAL: 
		case PROPERTY_TOTAL: 
		case BURGLARY_SUBTOTAL: 
		case LARCENY_THEFT_SUBTOTAL: 
		case MOTOR_VEHICLE_THEFT_SUBTOTAL:
			for (int i = 0; i < 6; i++) {
				Cell cell = row.createCell(i+3);
				cell.setCellValue(returnARecordCardRow.getMonths()[i]);
				cell.setCellStyle(rightGrayStyle);
			}
			
			for (int i = 6; i < 12; i++) {
				Cell cell = row.createCell(i+4);
				cell.setCellValue(returnARecordCardRow.getMonths()[i]);
				cell.setCellStyle(rightGrayStyle);
			}
			
			break; 
		default: 
			for (int i = 0; i < 6; i++) {
				Cell cell = row.createCell(i+3);
				cell.setCellValue(returnARecordCardRow.getMonths()[i]);
				cell.setCellStyle(rightDefaultStyle);
			}
			for (int i = 6; i < 12; i++) {
				Cell cell = row.createCell(i+4);
				cell.setCellValue(returnARecordCardRow.getMonths()[i]);
				cell.setCellStyle(rightDefaultStyle);
			}
		}
		
		Cell cellFirstHalfTotal = row.createCell(9); 
		cellFirstHalfTotal.setCellValue(returnARecordCardRow.getFirstHalfSubtotal());
		cellFirstHalfTotal.setCellStyle(rightGrayStyle);
		Cell cellSecondHalfTotal = row.createCell(16); 
		cellSecondHalfTotal.setCellValue(returnARecordCardRow.getSecondHalfSubtotal());
		cellSecondHalfTotal.setCellStyle(rightGrayStyle);
		Cell cellTotal = row.createCell(17); 
		cellTotal.setCellValue(returnARecordCardRow.getTotal());
		cellTotal.setCellStyle(rightGrayStyle);
	}

	private void creatRowHeaders(XSSFSheet sheet, int rowNum, Font boldFont, XSSFFont normalWeightFont) {
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));
		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(0); 
		cell.setCellValue("Grand Total");
		cell.setCellStyle(grayForeGround);
		
		CellStyle blueBoldCenteredCenteredStyle = sheet.getWorkbook().createCellStyle();
		blueBoldCenteredCenteredStyle.cloneStyleFrom(blueBoldCenteredStyle);
		blueBoldCenteredCenteredStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+15, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 2));
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(0); 
		cell.setCellValue("Violent");
		cell.setCellStyle(blueBoldTopCenteredStyle);
		cell = row.createCell(1); 
		cell.setCellValue("Total");
		cell.setCellStyle(grayForeGround);
		
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, 1, 1));
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(1);
		cell.setCellValue("Murder");
		cell.setCellStyle(blueBoldCenteredCenteredStyle);
		
		cell = row.createCell(2);
		cell.setCellValue("Subtotal");
		cell.setCellStyle(grayForeGround);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue("Murder");
		cell.setCellStyle(blueLeftStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+2, 1, 1));
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(1);
		cell.setCellValue("Rape");
		cell.setCellStyle(blueBoldCenteredCenteredStyle);
		
		cell = row.createCell(2);
		cell.setCellValue("Subtotal");
		cell.setCellStyle(grayForeGround);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue("Rape");
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue("Attempted Rape");
		cell.setCellStyle(blueLeftStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+4, 1, 1));
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(1);
		cell.setCellValue("Robbery");
		cell.setCellStyle(blueBoldCenteredCenteredStyle);
		
		cell = row.createCell(2);
		cell.setCellValue("Subtotal");
		cell.setCellStyle(grayForeGround);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.FIREARM_ROBBERY.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.KNIFE_CUTTING_INSTRUMENT_ROBBERY.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.OTHER_DANGEROUS_WEAPON_ROBBERY.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.STRONG_ARM_ROBBERY.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+4, 1, 1));
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(1);
		cell.setCellValue("Assault");
		cell.setCellStyle(blueBoldCenteredCenteredStyle);
		
		cell = row.createCell(2);
		cell.setCellValue("Subtotal");
		cell.setCellStyle(grayForeGround);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.FIREARM_ASSAULT.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.KNIFE_CUTTING_INSTRUMENT_ASSAULT.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.OTHER_DANGEROUS_WEAPON_ASSAULT.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.HANDS_FISTS_FEET_AGGRAVATED_INJURY_ASSAULT.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+10, 0, 0));
		row = sheet.createRow(rowNum); 
		cell = row.createCell(0); 
		cell.setCellValue("Property");
		cell.setCellStyle(blueBoldTopCenteredStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 2));
		cell = row.createCell(1); 
		cell.setCellValue("Total");
		cell.setCellStyle(grayForeGround);

		rowNum++;
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+3, 1, 1));
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(1);
		cell.setCellValue("Burglary");
		cell.setCellStyle(blueBoldCenteredCenteredStyle);
		
		cell = row.createCell(2);
		cell.setCellValue("Subtotal");
		cell.setCellStyle(grayForeGround);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.FORCIBLE_ENTRY_BURGLARY.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.UNLAWFUL_ENTRY_NO_FORCE_BURGLARY.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.ATTEMPTED_FORCIBLE_ENTRY_BURGLARY.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, 1, 1));
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(1);
		cell.setCellValue("Larceny");
		cell.setCellStyle(blueBoldCenteredCenteredStyle);
		
		cell = row.createCell(2);
		cell.setCellValue("Subtotal");
		cell.setCellStyle(grayForeGround);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.LARCENY_THEFT.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+3, 1, 1));
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(1);
		cell.setCellValue("Motor Vehicle\n Theft");
		cell.setCellStyle(blueBoldCenteredCenteredStyle);
		
		cell = row.createCell(2);
		cell.setCellValue("Subtotal");
		cell.setCellStyle(grayForeGround);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.AUTOS_THEFT.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.TRUCKS_BUSES_THEFT.getLabel());
		cell.setCellStyle(blueLeftStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.OTHER_VEHICLES_THEFT.getLabel());
		cell.setCellStyle(blueLeftStyle);

	}

	private int createHeaderRow(XSSFSheet sheet, int rowNum, Font boldFont, XSSFFont normalWeightFont) {
		CellStyle normalStyle = sheet.getWorkbook().createCellStyle();
		normalStyle.setWrapText(true);
		normalStyle.setAlignment(HorizontalAlignment.CENTER);
		normalStyle.setBorderBottom(BorderStyle.THIN);
		normalStyle.setBorderTop(BorderStyle.THIN);
		normalStyle.setBorderRight(BorderStyle.THIN);
		normalStyle.setBorderLeft(BorderStyle.THIN);
		
		CellStyle boldStyle = sheet.getWorkbook().createCellStyle();
		boldStyle.cloneStyleFrom(normalStyle);
		boldStyle.setFont(boldFont);

		CellStyle boldLeftStyle = sheet.getWorkbook().createCellStyle();
		boldLeftStyle.cloneStyleFrom(boldStyle);
		boldLeftStyle.setAlignment(HorizontalAlignment.LEFT);
		boldLeftStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		CellStyle boldTopStyle = sheet.getWorkbook().createCellStyle();
		boldTopStyle.cloneStyleFrom(boldStyle);
		boldTopStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
		XSSFCellStyle blueBoldLeftStyle = sheet.getWorkbook().createCellStyle();
		blueBoldLeftStyle.cloneStyleFrom(blueLeftStyle);
		blueBoldLeftStyle.setFont(boldFont);
		
		XSSFCellStyle greyBoldCenteredStyle = sheet.getWorkbook().createCellStyle();
		greyBoldCenteredStyle.cloneStyleFrom(grayForeGround);
		greyBoldCenteredStyle.setAlignment(HorizontalAlignment.CENTER);;
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, 0, 2));
		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(0);
		cell.setCellStyle(normalStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,3,9));
		Cell cell1 = row.createCell(3);
		cell1.setCellStyle(blueBoldLeftStyle);
		cell1.setCellValue("First Half");
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,10,16));
		Cell cell2 = row.createCell(10);
		cell2.setCellStyle(blueBoldLeftStyle);
		cell2.setCellValue("Second Half");
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, 17, 17));
		Cell cell3 = row.createCell(17);
		cell3.setCellStyle(blueBoldTopCenteredStyle);
		cell3.setCellValue("Total");
		
		row = sheet.createRow(++rowNum);
		List<String> headers = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Subtotal", 
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Subtotal"); 

		for(int i=3, j=0; j < headers.size(); i++, j++) {
			Cell cell5 = row.createCell(i);
			cell5.setCellValue(headers.get(j));
			
			if (!headers.get(j).contentEquals("Subtotal")) {
				cell5.setCellStyle(blueForeGround);
			}
			else {
				cell5.setCellStyle(greyBoldCenteredStyle);
			}
		}

		return ++rowNum;
	}

	private int createMetaDataRows(XSSFSheet sheet, int rowNum, ReturnARecordCard returnARecordCard) {
		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(0);
		cell.setCellValue(returnARecordCard.getYear());
		cell.setCellStyle(bottomBorderStyle);
		
		cell=row.createCell(1); 
		cell.setCellValue(returnARecordCard.getStateName());
		cell.setCellStyle(bottomBorderStyle);
		
		cell=row.createCell(2); 
		cell.setCellValue(returnARecordCard.getAgencyName());
		cell.setCellStyle(bottomBorderStyle);
		
		cell=row.createCell(5); 
		cell.setCellValue("");
		cell.setCellStyle(bottomBorderStyle);
		
		cell=row.createCell(6); 
		cell.setCellValue(returnARecordCard.getOri());
		cell.setCellStyle(bottomBorderStyle);
		
		cell=row.createCell(8); 
		cell.setCellValue("");
		cell.setCellStyle(bottomBorderStyle);
		
		cell=row.createCell(10); 
		cell.setCellValue("");
		cell.setCellStyle(bottomBorderStyle);
		
		cell=row.createCell(12); 
		cell.setCellValue(returnARecordCard.getPopulationString());
		cell.setCellStyle(bottomBorderStyle);
		
		cell=row.createCell(14); 
		cell.setCellValue("");
		cell.setCellStyle(bottomBorderStyle);
		
		cell=row.createCell(16); 
		cell.setCellValue("Revised");
		cell.setCellStyle(bottomBorderStyle);
		
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		cell.setCellValue("Year");
		cell.setCellStyle(centeredStyle);
		
		cell=row.createCell(1); 
		cell.setCellValue("State");
		cell.setCellStyle(centeredStyle);
		
		cell=row.createCell(2); 
		cell.setCellValue("Agency");
		cell.setCellStyle(centeredStyle);
		
		cell=row.createCell(5); 
		cell.setCellValue("MSA");
		cell.setCellStyle(centeredStyle);
		
		cell=row.createCell(6); 
		cell.setCellValue("ORI");
		cell.setCellStyle(centeredStyle);
		
		cell=row.createCell(8); 
		cell.setCellValue("Group");
		cell.setCellStyle(centeredStyle);
		
		cell=row.createCell(10); 
		cell.setCellValue("Division");
		cell.setCellStyle(centeredStyle);
		
		cell=row.createCell(12); 
		cell.setCellValue("Population");
		cell.setCellStyle(centeredStyle);
		
		cell=row.createCell(14); 
		cell.setCellValue("Months Submitted");
		cell.setCellStyle(centeredStyle);
		
		cell=row.createCell(16); 
		cell.setCellValue("Rape Definition");
		cell.setCellStyle(centeredStyle);
		
		return rowNum;
	}

	private int createReturnARecordTitleRow(XSSFSheet sheet, int rowNum, CellStyle cs, 
			Font boldFont, XSSFFont normalWeightFont) {
		rowNum++; 
		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(0);
		cell.setCellStyle(centeredStyle);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("Return A Record Card");
		s1.applyFont(boldFont);
		cell.setCellValue(s1);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 17));
		
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		cell.setCellStyle(rightAlignedStyle);
		
		cell.setCellValue(LocalDate.now().format(dateTimeFormatter));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 17));
		
		return rowNum;
	}
    

}