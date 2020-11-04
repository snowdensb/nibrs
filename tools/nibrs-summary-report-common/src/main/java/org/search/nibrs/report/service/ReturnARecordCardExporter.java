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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.search.nibrs.model.reports.ReturnARecordCard;
import org.search.nibrs.model.reports.ReturnARecordCardReport;
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
	private XSSFCellStyle centeredStyle; 
	private XSSFCellStyle rightAlignedStyle;
	private XSSFCellStyle bottomBorderStyle;
	private XSSFCellStyle defaultStyle;
	private XSSFCellStyle grayForeGround;
	private XSSFCellStyle blueForeGround;
	private XSSFCellStyle blueBoldForeGround;
	private XSSFCellStyle blueLeftStyle;
	private XSSFCellStyle blueBoldCenteredStyle;
	private XSSFCellStyle blueBoldTopCenteredStyle;
	private XSSFCellStyle blueLeftNoWrapStyle;
	private XSSFCellStyle blueLeftFont8NoWrapStyle;
	private XSSFCellStyle rightDefaultStyle;
	private XSSFCellStyle rightGrayStyle;
	private XSSFFont boldSmallerFont;
	private XSSFFont normalWeightFont;
	private XSSFFont normalCalibriFont;
	private XSSFColor borderColor = 
			new XSSFColor(IndexedColors.GREY_50_PERCENT, new DefaultIndexedColorMap());
	
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, YYYY"); 
    public void exportReturnARecordCardReport(ReturnARecordCardReport returnARecordCardReport){
        XSSFWorkbook workbook = createReturnARecordCardWorkbook(returnARecordCardReport);
		
        try {
        	String fileName = appProperties.getSummaryReportOutputPath() + "/ReturnARecordCard-" + 
        			returnARecordCardReport.getStateName() + "-" + returnARecordCardReport.getYear()  + ".xlsx"; 
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
    
	public XSSFWorkbook createReturnARecordCardWorkbook(ReturnARecordCardReport returnARecordCardReport) {
		XSSFWorkbook workbook = new XSSFWorkbook();
        normalWeightFont = workbook.createFont();
        normalWeightFont.setBold(false);
        normalWeightFont.setFontName("Tahoma");
        normalWeightFont.setFontHeightInPoints(Short.valueOf("8"));
        
        XSSFFont normalWeightSmallerFont = workbook.createFont();
        normalWeightSmallerFont.setBold(false);
        normalWeightSmallerFont.setFontName("Tahoma");
        normalWeightSmallerFont.setFontHeightInPoints(Short.valueOf("7"));
        
        normalCalibriFont = workbook.createFont();
        normalCalibriFont.setBold(false);
        normalCalibriFont.setFontName("Calibri");
        normalCalibriFont.setFontHeightInPoints(Short.valueOf("8"));

        log.info("Write to the excel file");
        log.debug("Return A record card " + returnARecordCardReport);
        defaultStyle = workbook.createCellStyle();
        defaultStyle.setWrapText(true);
        defaultStyle.setBorderBottom(BorderStyle.THIN);
        defaultStyle.setBorderTop(BorderStyle.THIN);
        defaultStyle.setBorderRight(BorderStyle.THIN);
        defaultStyle.setBorderLeft(BorderStyle.THIN);
        XSSFColor borderColor = new XSSFColor(IndexedColors.GREY_50_PERCENT, new DefaultIndexedColorMap());
        defaultStyle.setBorderColor(BorderSide.BOTTOM, borderColor);
        defaultStyle.setBorderColor(BorderSide.RIGHT, borderColor);
        defaultStyle.setBorderColor(BorderSide.LEFT, borderColor);
        defaultStyle.setBorderColor(BorderSide.TOP, borderColor);
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
        
        boldSmallerFont = workbook.createFont();
        boldSmallerFont.setFontName("Tahoma");
        boldSmallerFont.setFontHeightInPoints(Short.parseShort("7"));
        boldSmallerFont.setBold(true);
        
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
		blueBoldCenteredStyle.setFont(boldSmallerFont);
		
		blueLeftStyle = workbook.createCellStyle();
		blueLeftStyle.cloneStyleFrom(blueForeGround);
		blueLeftStyle.setAlignment(HorizontalAlignment.LEFT);
		blueLeftStyle.setFont(normalWeightSmallerFont);
		
		blueLeftNoWrapStyle = workbook.createCellStyle();
		blueLeftNoWrapStyle.cloneStyleFrom(blueLeftStyle);
		blueLeftNoWrapStyle.setWrapText(false);;
		
		blueLeftFont8NoWrapStyle = workbook.createCellStyle(); 
		blueLeftFont8NoWrapStyle.cloneStyleFrom(blueLeftNoWrapStyle);
		blueLeftFont8NoWrapStyle.setFont(normalWeightFont);
		
		blueBoldTopCenteredStyle = workbook.createCellStyle();
		blueBoldTopCenteredStyle.cloneStyleFrom(blueBoldCenteredStyle);
		blueBoldTopCenteredStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
	    rightGrayStyle = workbook.createCellStyle(); 
	    rightGrayStyle.cloneStyleFrom(grayForeGround);
	    rightGrayStyle.setAlignment(HorizontalAlignment.RIGHT);

	    rightDefaultStyle = workbook.createCellStyle(); 
	    rightDefaultStyle.cloneStyleFrom(defaultStyle);
	    rightDefaultStyle.setAlignment(HorizontalAlignment.RIGHT);


        Font underlineFont = workbook.createFont();
        underlineFont.setUnderline(Font.U_SINGLE);
        
    	createReturnARecordCardSheet(workbook, boldFont, normalWeightFont, returnARecordCardReport);
		return workbook;
	}

	private void createReturnARecordCardSheet(XSSFWorkbook workbook, Font boldFont, 
			XSSFFont normalWeightFont, ReturnARecordCardReport returnARecordCardReport) {
		
		for (ReturnARecordCard returnARecordCard: returnARecordCardReport.getReturnARecordCards().values()) {
			int rowNum = 0;
	        XSSFSheet sheet = workbook.createSheet(returnARecordCard.getOri());
			sheet.setFitToPage(true);
			PrintSetup ps = sheet.getPrintSetup();
			ps.setLandscape(true);
			ps.setFitWidth( (short) 1);
			ps.setFitHeight( (short) 0);
	
	        sheet.setColumnWidth(2, 750 * sheet.getDefaultColumnWidth());
	        CellStyle wrappedStyle = workbook.createCellStyle();
	        wrappedStyle.setWrapText(true);
	        
	    	rowNum = createReturnARecordTitleRow(sheet, rowNum);
	    	rowNum = createMetaDataRows(sheet, rowNum, returnARecordCard);
	    	
	    	createReportedOffenseTable(boldFont, normalWeightFont, returnARecordCard, rowNum, sheet);
	    	createSimpleAssaultTable(boldFont, normalWeightFont, returnARecordCard, sheet);
		}
        
	}

	private void createSimpleAssaultTable(Font boldFont, XSSFFont normalWeightFont2,
			ReturnARecordCard returnARecordCard, XSSFSheet sheet) {
		int rowNum = 46; 
		rowNum = createSimpleAssaultTableTitle(sheet, rowNum);
		rowNum = createHeaderRow(sheet, rowNum, boldFont, normalWeightFont);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));
		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(0); 
		cell.setCellValue("Simple Assault");
		cell.setCellStyle(blueLeftFont8NoWrapStyle);
		
		ReturnARecordCardRow returnARecordCardRow = 
				returnARecordCard.getRows()[ReturnARecordCardRowName.OTHER_ASSAULT_NOT_AGGRAVATED.ordinal()]; 
		writeMonthlyCounts(returnARecordCardRow, rightDefaultStyle, row);
		writeSubtotalAndTotalColumns(returnARecordCardRow, rightGrayStyle, row);
		
		CellRangeAddress bottomRow = new CellRangeAddress(rowNum, rowNum, 1, 2);
		RegionUtil.setBorderTop(BorderStyle.THIN, bottomRow, sheet);
		RegionUtil.setTopBorderColor(borderColor.getIndex(), bottomRow, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, bottomRow, sheet);
		RegionUtil.setBottomBorderColor(borderColor.getIndex(), bottomRow, sheet);
		
		CellRangeAddress firstRow = new CellRangeAddress(rowNum - 2, rowNum - 2, 0, 17);
		RegionUtil.setBorderTop(BorderStyle.THIN, firstRow, sheet);
		RegionUtil.setTopBorderColor(borderColor.getIndex(), firstRow, sheet);
		
		CellRangeAddress tableUpperRight = new CellRangeAddress(rowNum - 2, rowNum - 1, 17, 17);
		RegionUtil.setBorderRight(BorderStyle.THIN, tableUpperRight, sheet);
		RegionUtil.setRightBorderColor(borderColor.getIndex(), tableUpperRight, sheet);

	}

	private int createSimpleAssaultTableTitle(XSSFSheet sheet, int rowNum) {
        XSSFFont boldWeightFont = sheet.getWorkbook().createFont();
        boldWeightFont.setBold(true);
        boldWeightFont.setFontName("Calibri");
        boldWeightFont.setFontHeightInPoints(Short.valueOf("10"));

		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(0);
		cell.setCellStyle(centeredStyle);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("Simple Assault");
		s1.applyFont(boldWeightFont);
		cell.setCellValue(s1);
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 17));
		return ++rowNum;
	}

	private void createReportedOffenseTable(Font boldFont, XSSFFont normalWeightFont, ReturnARecordCard returnARecordCard, int rowNum,
			XSSFSheet sheet) {
		rowNum = createHeaderRow(sheet, rowNum, boldFont, normalWeightFont);
		
		int grandTotalRowNum = rowNum; 
		
		creatRowHeaders(sheet, rowNum, boldFont, normalWeightFont);

		rowNum = grandTotalRowNum; 
		
		for (ReturnARecordCardRowName rowName: ReturnARecordCardRowName.values()){
			if (rowName != ReturnARecordCardRowName.OTHER_ASSAULT_NOT_AGGRAVATED) {
				writeReportedOffensesRow(sheet, rowName, returnARecordCard.getRows()[rowName.ordinal()], rowNum, boldFont);
				rowNum ++;
			}
		}

		RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(4, 4, 0, 17), sheet);
		
		CellRangeAddress tableTop = new CellRangeAddress(5, 5, 0, 17);
		RegionUtil.setBorderTop(BorderStyle.THIN, tableTop, sheet);
		RegionUtil.setTopBorderColor(borderColor.getIndex(), tableTop, sheet);
		
		CellRangeAddress grandTotalCell = new CellRangeAddress(7, 7, 0, 3);
		RegionUtil.setBorderTop(BorderStyle.THIN, grandTotalCell, sheet);
		RegionUtil.setTopBorderColor(borderColor.getIndex(), grandTotalCell, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN, grandTotalCell, sheet);
		RegionUtil.setBottomBorderColor(borderColor.getIndex(), grandTotalCell, sheet);
		
		CellRangeAddress tableFirstLeft = new CellRangeAddress(5, 34, 0, 0);
		RegionUtil.setBorderLeft(BorderStyle.THIN, tableFirstLeft, sheet);
		RegionUtil.setLeftBorderColor(borderColor.getIndex(), tableFirstLeft, sheet);
		
		CellRangeAddress tableViolentAndProperty = new CellRangeAddress(8, 34, 0, 0);
		RegionUtil.setBorderRight(BorderStyle.THIN, tableViolentAndProperty, sheet);
		RegionUtil.setRightBorderColor(borderColor.getIndex(), tableViolentAndProperty, sheet);
		
		CellRangeAddress tableBottom = new CellRangeAddress(34, 34, 0, 2);
		RegionUtil.setBorderBottom(BorderStyle.THIN, tableBottom, sheet);
		RegionUtil.setBottomBorderColor(borderColor.getIndex(), tableBottom, sheet);
		
		CellRangeAddress tableUpperRight = new CellRangeAddress(5, 6, 17, 17);
		RegionUtil.setBorderRight(BorderStyle.THIN, tableUpperRight, sheet);
		RegionUtil.setRightBorderColor(borderColor.getIndex(), tableUpperRight, sheet);
	}
	
	private void writeReportedOffensesRow(XSSFSheet sheet, ReturnARecordCardRowName rowName,
			ReturnARecordCardRow returnARecordCardRow, int rowNum, Font boldFont) {
        
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
			writeMonthlyCounts(returnARecordCardRow, rightGrayStyle, row);
			
			break; 
		default: 
			writeMonthlyCounts(returnARecordCardRow, rightDefaultStyle, row);
		}
		
		writeSubtotalAndTotalColumns(returnARecordCardRow, rightGrayStyle, row);
	}

	private void writeSubtotalAndTotalColumns(ReturnARecordCardRow returnARecordCardRow, CellStyle rightGrayStyle, Row row) {
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

	private void writeMonthlyCounts(ReturnARecordCardRow returnARecordCardRow, CellStyle rightDefaultStyle, Row row) {
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
		cell.setCellStyle(blueLeftNoWrapStyle);
		
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
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue("Attempted Rape");
		cell.setCellStyle(blueLeftNoWrapStyle);
		
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
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.KNIFE_CUTTING_INSTRUMENT_ROBBERY.getLabel());
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.OTHER_DANGEROUS_WEAPON_ROBBERY.getLabel());
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.STRONG_ARM_ROBBERY.getLabel());
		cell.setCellStyle(blueLeftNoWrapStyle);
		
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
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.KNIFE_CUTTING_INSTRUMENT_ASSAULT.getLabel());
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.OTHER_DANGEROUS_WEAPON_ASSAULT.getLabel());
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.HANDS_FISTS_FEET_AGGRAVATED_INJURY_ASSAULT.getLabel());
		cell.setCellStyle(blueLeftNoWrapStyle);
		
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
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.UNLAWFUL_ENTRY_NO_FORCE_BURGLARY.getLabel());
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.ATTEMPTED_FORCIBLE_ENTRY_BURGLARY.getLabel());
		cell.setCellStyle(blueLeftNoWrapStyle);
		
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
		cell.setCellStyle(blueLeftNoWrapStyle);
		
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
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.TRUCKS_BUSES_THEFT.getLabel());
		cell.setCellStyle(blueLeftNoWrapStyle);
		
		row = sheet.createRow(rowNum ++); 
		cell = row.createCell(2);
		cell.setCellValue(ReturnARecordCardRowName.OTHER_VEHICLES_THEFT.getLabel());
		cell.setCellStyle(blueLeftNoWrapStyle);

	}

	private int createHeaderRow(XSSFSheet sheet, int rowNum, Font boldFont, XSSFFont normalWeightFont) {
		XSSFCellStyle blueBoldLeftStyle = sheet.getWorkbook().createCellStyle();
		blueBoldLeftStyle.cloneStyleFrom(blueForeGround);
		blueBoldLeftStyle.setFont(boldSmallerFont);
		
		XSSFCellStyle blueCenteredStyle = sheet.getWorkbook().createCellStyle();
		blueCenteredStyle.cloneStyleFrom(blueForeGround);
		blueCenteredStyle.setAlignment(HorizontalAlignment.CENTER);
		blueCenteredStyle.setFont(normalWeightFont);
		
		XSSFCellStyle blueBold8TopCenteredStyle = sheet.getWorkbook().createCellStyle();
		blueBold8TopCenteredStyle.cloneStyleFrom(blueBoldForeGround);
		blueBold8TopCenteredStyle.setVerticalAlignment(VerticalAlignment.TOP);
		blueBold8TopCenteredStyle.setAlignment(HorizontalAlignment.CENTER);
		
		XSSFCellStyle greyBoldCenteredStyle = sheet.getWorkbook().createCellStyle();
		greyBoldCenteredStyle.cloneStyleFrom(grayForeGround);
		greyBoldCenteredStyle.setAlignment(HorizontalAlignment.CENTER);
		greyBoldCenteredStyle.setFont(boldSmallerFont);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum+1, 0, 2));
		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(0);
		cell.setCellStyle(defaultStyle);
		
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
		cell3.setCellStyle(blueBold8TopCenteredStyle);
		cell3.setCellValue("Total");
		
		row = sheet.createRow(++rowNum);
		List<String> headers = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Subtotal", 
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Subtotal"); 

		for(int i=3, j=0; j < headers.size(); i++, j++) {
			Cell cell5 = row.createCell(i);
			cell5.setCellValue(headers.get(j));
			
			if (!headers.get(j).contentEquals("Subtotal")) {
				cell5.setCellStyle(blueCenteredStyle);
			}
			else {
				cell5.setCellStyle(greyBoldCenteredStyle);
			}
		}

		return ++rowNum;
	}

	private int createMetaDataRows(XSSFSheet sheet, int rowNum, ReturnARecordCard returnARecordCard) {
		XSSFCellStyle centeredNormalTahoma = sheet.getWorkbook().createCellStyle(); 
		centeredNormalTahoma.cloneStyleFrom(centeredStyle);
		centeredNormalTahoma.setFont(normalWeightFont);
		
		Row row = sheet.createRow(rowNum);
		Cell cell = row.createCell(0);
		cell.setCellValue(returnARecordCard.getYear());
		cell.setCellStyle(centeredNormalTahoma);
		
		cell=row.createCell(1); 
		cell.setCellValue(returnARecordCard.getStateName());
		cell.setCellStyle(centeredNormalTahoma);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 4));
		cell=row.createCell(2); 
		cell.setCellValue(returnARecordCard.getAgencyName());
		cell.setCellStyle(centeredNormalTahoma);
		
		cell=row.createCell(5); 
		cell.setCellValue("");
		cell.setCellStyle(centeredNormalTahoma);
		
		cell=row.createCell(6); 
		cell.setCellValue(returnARecordCard.getOri());
		cell.setCellStyle(centeredNormalTahoma);
		
		cell=row.createCell(8); 
		cell.setCellValue("");
		cell.setCellStyle(centeredNormalTahoma);
		
		cell=row.createCell(10); 
		cell.setCellValue("");
		cell.setCellStyle(centeredNormalTahoma);
		
		cell=row.createCell(12); 
		cell.setCellValue(returnARecordCard.getPopulationString());
		cell.setCellStyle(centeredNormalTahoma);
		
		cell=row.createCell(14); 
		cell.setCellValue("");
		cell.setCellStyle(centeredNormalTahoma);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 16, 17));
		cell=row.createCell(16); 
		cell.setCellValue("Revised");
		cell.setCellStyle(centeredNormalTahoma);
		
		XSSFCellStyle centeredNormalCalibri = sheet.getWorkbook().createCellStyle(); 
		centeredNormalCalibri.cloneStyleFrom(centeredStyle);
		centeredNormalCalibri.setVerticalAlignment(VerticalAlignment.TOP);
		centeredNormalCalibri.setFont(normalCalibriFont);
		
		row = sheet.createRow(++rowNum);
		cell = row.createCell(0);
		cell.setCellValue("Year");
		cell.setCellStyle(centeredNormalCalibri);
		
		cell=row.createCell(1); 
		cell.setCellValue("State");
		cell.setCellStyle(centeredNormalCalibri);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 4));
		cell=row.createCell(2); 
		cell.setCellValue("Agency");
		cell.setCellStyle(centeredNormalCalibri);
		
		cell=row.createCell(5); 
		cell.setCellValue("MSA");
		cell.setCellStyle(centeredNormalCalibri);
		
		cell=row.createCell(6); 
		cell.setCellValue("ORI");
		cell.setCellStyle(centeredNormalCalibri);
		
		cell=row.createCell(8); 
		cell.setCellValue("Group");
		cell.setCellStyle(centeredNormalCalibri);
		
		cell=row.createCell(10); 
		cell.setCellValue("Division");
		cell.setCellStyle(centeredNormalCalibri);
		
		cell=row.createCell(12); 
		cell.setCellValue("Population");
		cell.setCellStyle(centeredNormalCalibri);
		
		cell=row.createCell(14); 
		cell.setCellValue("Months Submitted");
		cell.setCellStyle(centeredNormalCalibri);
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 16, 17));
		cell=row.createCell(16); 
		cell.setCellValue("Rape Definition");
		cell.setCellStyle(centeredNormalCalibri);
		
		return ++rowNum;
	}

	private int createReturnARecordTitleRow(XSSFSheet sheet, int rowNum) {
		
        XSSFFont normalWeightFont = sheet.getWorkbook().createFont();
        normalWeightFont.setBold(false);
        normalWeightFont.setFontName("Tahoma");
        normalWeightFont.setFontHeightInPoints(Short.valueOf("10"));
        
        XSSFFont boldWeightFont = sheet.getWorkbook().createFont();
        boldWeightFont.setBold(true);
        boldWeightFont.setFontName("Calibri");
        boldWeightFont.setFontHeightInPoints(Short.valueOf("12"));

		rowNum++; 
		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(0);
		cell.setCellStyle(centeredStyle);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("Return A Record Card");
		s1.applyFont(boldWeightFont);
		cell.setCellValue(s1);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 17));
		
		rightAlignedStyle.setFont(normalWeightFont);
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		cell.setCellStyle(rightAlignedStyle);
		
		cell.setCellValue(LocalDate.now().format(dateTimeFormatter));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 17));
		
		return rowNum;
	}
    

}