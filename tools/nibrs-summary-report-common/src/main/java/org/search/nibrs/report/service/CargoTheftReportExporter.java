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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.search.nibrs.model.reports.cargotheft.CargoTheftFormRow;
import org.search.nibrs.model.reports.cargotheft.CargoTheftReport;
import org.search.nibrs.report.SummaryReportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CargoTheftReportExporter {
	private static final Log log = LogFactory.getLog(CargoTheftReportExporter.class);
	
	@Autowired
	private SummaryReportProperties appProperties;
	
	XSSFFont boldFont; 
	XSSFFont normalWeightFont; 
	CellStyle wrappedStyle;
	CellStyle vTopWrappedStyle;
	CellStyle wrappedBorderedStyle;
	CellStyle centeredWrappedBorderedStyle;
	CellStyle hCenteredStyle;
	CellStyle centeredStyle;
	CellStyle yellowForeGround;

	public void exportCargoTheftReport(CargoTheftReport cargoTheftReport){
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        wrappedStyle = workbook.createCellStyle();
        wrappedStyle.setWrapText(true);
        
        vTopWrappedStyle = workbook.createCellStyle();
        vTopWrappedStyle.cloneStyleFrom(wrappedStyle);
        vTopWrappedStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
        wrappedBorderedStyle = workbook.createCellStyle(); 
        wrappedBorderedStyle.cloneStyleFrom(wrappedStyle);
        wrappedBorderedStyle.setBorderBottom(BorderStyle.THIN);
        wrappedBorderedStyle.setBorderTop(BorderStyle.THIN);
        wrappedBorderedStyle.setBorderRight(BorderStyle.THIN);
        wrappedBorderedStyle.setBorderLeft(BorderStyle.THIN);
        wrappedBorderedStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        
        centeredWrappedBorderedStyle = workbook.createCellStyle(); 
        centeredWrappedBorderedStyle.cloneStyleFrom(wrappedBorderedStyle);
        centeredWrappedBorderedStyle.setAlignment(HorizontalAlignment.CENTER);
        
        centeredStyle = workbook.createCellStyle(); 
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);
        centeredStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        
        boldFont = workbook.createFont();
        boldFont.setBold(true);
        normalWeightFont = workbook.createFont();
        normalWeightFont.setBold(false);
        
        yellowForeGround = workbook.createCellStyle();
        yellowForeGround.cloneStyleFrom(centeredWrappedBorderedStyle);
        yellowForeGround.setFillForegroundColor(IndexedColors.YELLOW.index);
        yellowForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        createWorkSheet(cargoTheftReport, workbook);
        
        try {
        	String fileName = appProperties.getSummaryReportOutputPath() + "/CargoTheftReport-" + cargoTheftReport.getOri() + "-" + cargoTheftReport.getYear() + "-" + StringUtils.leftPad(String.valueOf(cargoTheftReport.getMonth()), 2, '0') + ".xlsx"; 
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
            System.out.println("The Supplementary Homicide Report is writen to fileName: " + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

	private void createWorkSheet(CargoTheftReport cargoTheftReport, XSSFWorkbook workbook) {
		XSSFSheet sheet = workbook.createSheet("Cargo Theft Incident Reports");
		sheet.setFitToPage(true);
		PrintSetup ps = sheet.getPrintSetup();
		ps.setFitWidth( (short) 1);
		ps.setFitHeight( (short) 1);

        int rowNum = 0;
        log.info("Write to the excel file");

    	rowNum = createTitleRow(sheet, rowNum);
    	
		rowNum = createHeaderRow(sheet, rowNum, true);
		
        for (CargoTheftFormRow cargoTheftFormRow: cargoTheftReport.getCargoTheftRows()){
        	writeCargoTheftFormRow(sheet, cargoTheftFormRow, rowNum++);
        }
        
		if (cargoTheftReport.getCargoTheftRows().isEmpty()) {
	    	rowNum = createEmptyRow(sheet, rowNum++);
		}

		setColumnsWidth(sheet);
	}


	private int createEmptyRow(XSSFSheet sheet, int rowNum) {
		Row row = sheet.createRow(rowNum);
		for (int colNum = 0; colNum < 3; colNum++) {
			Cell cell = row.createCell(colNum);
			cell.setCellStyle(wrappedBorderedStyle);
		}
		return rowNum;
	}
	
	private void setColumnsWidth(XSSFSheet sheet) {
		sheet.setColumnWidth(0, 900 * sheet.getDefaultColumnWidth());
		for (int i = 1; i < 4; i++) {
			sheet.setColumnWidth(i, 475 * sheet.getDefaultColumnWidth());
		}
	}


	private int createTitleRow(XSSFSheet sheet, int rowNum) {
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((3*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		cell.setCellStyle(centeredStyle);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("Cargo Theft Incident Reports");
		s1.applyFont(boldFont);
		cell.setCellValue(s1);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		return rowNum;
	}
    
	private int createHeaderRow(XSSFSheet sheet, int rowNum, boolean nonNegligent) {
		
		CellRangeAddress mergedRegions = new CellRangeAddress(rowNum, rowNum, 0, 2);
		sheet.addMergedRegion(mergedRegions);


        Row row = sheet.createRow(rowNum++);
        Cell cell = row.createCell(0);
		cell.setCellStyle(yellowForeGround);
		cell.setCellValue("INCIDENT INFORMATION");

		row = sheet.createRow(rowNum++);
		row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
		cell=row.createCell(0);
		cell.setCellStyle(yellowForeGround);
		cell.setCellValue("INCIDENT NUMBER");
		
		cell=row.createCell(1);
		cell.setCellStyle(yellowForeGround);
		cell.setCellValue("INCIDENT DATE\n(YYYYMMDD)");
		
		cell=row.createCell(2);
		cell.setCellStyle(yellowForeGround);
		cell.setCellValue("SUBMISSION TYPE");
		
		RegionUtil.setBorderLeft(BorderStyle.THIN.getCode(), mergedRegions, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), mergedRegions, sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN.getCode(), mergedRegions, sheet);
		RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);
		return rowNum;
	}

    private void writeCargoTheftFormRow(XSSFSheet sheet, CargoTheftFormRow cargoTheftFormRow, int rowNum) {
    	Row row = sheet.createRow(rowNum);
    	int colNum = 0;
    	Cell cell = row.createCell(colNum++);
    	cell.setCellStyle(wrappedBorderedStyle);
    	cell.setCellValue(cargoTheftFormRow.getIncidentNumber());
    	
		cell = row.createCell(colNum++);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue(cargoTheftFormRow.getIncidentDateString());

		cell = row.createCell(colNum++);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue(cargoTheftFormRow.getSegmentActionType());
		
	}

}