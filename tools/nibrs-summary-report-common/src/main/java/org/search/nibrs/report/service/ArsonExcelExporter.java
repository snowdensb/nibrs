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
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.search.nibrs.model.reports.arson.ArsonReport;
import org.search.nibrs.model.reports.arson.ArsonRow;
import org.search.nibrs.model.reports.arson.ArsonRowName;
import org.search.nibrs.report.SummaryReportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArsonExcelExporter {
	private static final Log log = LogFactory.getLog(ArsonExcelExporter.class);
	
	@Autowired
	private SummaryReportProperties appProperties;

    public void exportArsonReport(ArsonReport arsonReport){
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        createSheet(arsonReport, workbook);
		
        try {
        	String fileName = appProperties.getSummaryReportOutputPath() + "/ARSON-Report-" 
        			+ arsonReport.getOri() + "-" + arsonReport.getYear() + "-" 
        			+ StringUtils.leftPad(String.valueOf(arsonReport.getMonth()), 2, '0') 
        			+ ".xlsx"; 
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
            System.out.println("The Arson Summary Report Form is writen to fileName: " + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	private void createSheet(ArsonReport arsonReport, XSSFWorkbook workbook ) {
        int rowNum = 0;
        log.info("Write to the excel file");
        CellStyle wrappedStyle = workbook.createCellStyle();
        wrappedStyle.setWrapText(true);
        wrappedStyle.setBorderBottom(BorderStyle.THIN);
        wrappedStyle.setBorderTop(BorderStyle.THIN);
        wrappedStyle.setBorderRight(BorderStyle.THIN);
        wrappedStyle.setBorderLeft(BorderStyle.THIN);
        CellStyle centeredStyle = workbook.createCellStyle();
        centeredStyle.cloneStyleFrom(wrappedStyle);
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);;
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        
        XSSFFont normalWeightFont = workbook.createFont();
        normalWeightFont.setBold(false);
        
        Font underlineFont = workbook.createFont();
        underlineFont.setUnderline(Font.U_SINGLE);

        XSSFSheet sheet = workbook.createSheet("ARSON-SUMMARY");
		sheet.setFitToPage(true);
		PrintSetup ps = sheet.getPrintSetup();
		ps.setLandscape(true);
		ps.setFitWidth( (short) 1);
		ps.setFitHeight( (short) 0);
		
		Footer footer = sheet.getFooter();
		footer.setCenter("&B MONTHLY RETURN OF ARSON OFFENSES KNOWN TO LAW ENFORCEMENT");

        rowNum = createArsonTitleRow(sheet, rowNum, wrappedStyle, boldFont, normalWeightFont);
		createArsonHeaderRow(sheet, rowNum++, boldFont, normalWeightFont);
		
        for (ArsonRowName rowName: ArsonRowName.values()){
        	writeArsonFormRow(sheet, rowName, arsonReport.getArsonRows()[rowName.ordinal()], rowNum++, boldFont);
        }
        
		createMetaData(arsonReport, workbook, wrappedStyle, centeredStyle, sheet);
        
		sheet.setColumnWidth(0, 100 * sheet.getDefaultColumnWidth());
		sheet.setColumnWidth(1, 1400 * sheet.getDefaultColumnWidth());
		
		for ( int i=2; i<9; i++) {
			sheet.setColumnWidth(i, 580 * sheet.getDefaultColumnWidth());
		}

		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(1, 1, 0, 8), sheet);
		RegionUtil.setBorderLeft(BorderStyle.THICK.getCode(), new CellRangeAddress(1, 14, 0, 0), sheet);
		RegionUtil.setBorderBottom(BorderStyle.THICK.getCode(), new CellRangeAddress(9, 9, 0, 8), sheet);
		RegionUtil.setBorderBottom(BorderStyle.THICK.getCode(), new CellRangeAddress(12, 12, 0, 8), sheet);
		RegionUtil.setBorderBottom(BorderStyle.THICK.getCode(), new CellRangeAddress(13, 13, 0, 8), sheet);
		RegionUtil.setBorderBottom(BorderStyle.THICK.getCode(), new CellRangeAddress(14, 14, 0, 8), sheet);
		RegionUtil.setBorderLeft(BorderStyle.THICK.getCode(), new CellRangeAddress(1, 14, 4, 4), sheet);
		RegionUtil.setBorderRight(BorderStyle.THICK.getCode(), new CellRangeAddress(1, 14, 8, 8), sheet);

	}

	private void createMetaData(ArsonReport arsonReport, XSSFWorkbook workbook, CellStyle wrappedStyle,
			CellStyle centeredStyle, XSSFSheet sheet) {
		int rowNum;
		CellStyle thinBorderBottom = workbook.createCellStyle();
		thinBorderBottom.setBorderBottom(BorderStyle.THIN);

        rowNum = 16;
        XSSFRow row = sheet.createRow(rowNum);
        XSSFCell cell = row.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 4));
        cell.setCellValue(arsonReport.getOri());
        cell.setCellStyle(thinBorderBottom);
        for (int i = 1; i < 5; i++) {
	        cell = row.createCell(i); 
	        cell.setCellStyle(thinBorderBottom);
        }
        
        cell=row.createCell(7); 
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 7, 8));
        cell.setCellValue("DO NOT WRITE HERE");
        cell.setCellStyle(centeredStyle);
        cell = row.createCell(8);
        CellStyle rightBorderStyle = workbook.createCellStyle(); 
        rightBorderStyle.setBorderTop(BorderStyle.THIN);
        rightBorderStyle.setBorderRight(BorderStyle.THIN);
        cell.setCellStyle(rightBorderStyle);
        
        row = sheet.createRow(++rowNum);  
		CellStyle vTopStyle = workbook.createCellStyle();
		vTopStyle.setVerticalAlignment(VerticalAlignment.TOP);
		cell = row.createCell(0); 
		cell.setCellValue("Agency Identifier");
		cell.setCellStyle(vTopStyle);
		
		cell = row.createCell(7); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("Recorded");
		cell = row.createCell(8); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("");
        
        row = sheet.createRow(++rowNum);
        cell = row.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 4));
        cell.setCellValue(arsonReport.getAgencyName());
        cell.setCellStyle(thinBorderBottom);
        for (int i = 1; i < 5; i++) {
	        cell = row.createCell(i); 
	        cell.setCellStyle(thinBorderBottom);
        }
		cell = row.createCell(7); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("Edited");
		cell = row.createCell(8); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("");
        
        row = sheet.createRow(++rowNum);  
		cell = row.createCell(0); 
		cell.setCellValue("Agency");
		cell.setCellStyle(vTopStyle);
		
		cell = row.createCell(7); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("Entered");
		cell = row.createCell(8); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("");
        
        row = sheet.createRow(++rowNum);
        cell = row.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 5));
        cell.setCellValue("");
        cell.setCellStyle(thinBorderBottom);
        for (int i = 1; i < 6; i++) {
	        cell = row.createCell(i); 
	        cell.setCellStyle(thinBorderBottom);
        }
		cell = row.createCell(7); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("Adjusted");
		cell = row.createCell(8); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("");
        
        row = sheet.createRow(++rowNum);  
		cell = row.createCell(0); 
		cell.setCellValue("Prepared by/Telephone number/Email address");
		cell.setCellStyle(vTopStyle);
		
		cell = row.createCell(3); 
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 3, 5));
		cell.setCellValue("Chief, Commissioner, Sheriff, or Superintendent");
		cell.setCellStyle(vTopStyle);
		
		cell = row.createCell(7); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("Corres.");
		cell = row.createCell(8); 
		cell.setCellStyle(wrappedStyle);
		cell.setCellValue("");
	}
    private void writeArsonFormRow(XSSFSheet sheet, ArsonRowName rowName,
    		ArsonRow arsonRow, int rowNum, Font boldFont) {
    	Row row = sheet.createRow(rowNum);
    	int colNum = 0;
        
    	CellStyle defaultStyle = sheet.getWorkbook().createCellStyle();
    	defaultStyle.setWrapText(true);
    	defaultStyle.setBorderBottom(BorderStyle.THIN);
    	defaultStyle.setBorderTop(BorderStyle.THIN);
    	defaultStyle.setBorderRight(BorderStyle.THIN);
    	defaultStyle.setBorderLeft(BorderStyle.THIN);

    	CellStyle rowLabelStyle = sheet.getWorkbook().createCellStyle(); 
    	rowLabelStyle.cloneStyleFrom(defaultStyle);
    	rowLabelStyle.setVerticalAlignment(VerticalAlignment.TOP);
    	
    	CellStyle totalOtherLabelStyle = sheet.getWorkbook().createCellStyle(); 
    	totalOtherLabelStyle.cloneStyleFrom(defaultStyle);
    	totalOtherLabelStyle.setBorderLeft(BorderStyle.NONE);
    	
    	CellStyle rightStyle = sheet.getWorkbook().createCellStyle(); 
    	rightStyle.cloneStyleFrom(defaultStyle);
    	rightStyle.setAlignment(HorizontalAlignment.RIGHT);
    	
    	CellStyle vCenterStyle = sheet.getWorkbook().createCellStyle(); 
    	vCenterStyle.cloneStyleFrom(defaultStyle);
    	vCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    	vCenterStyle.setAlignment(HorizontalAlignment.CENTER);
    	
    	CellStyle centerStyle = sheet.getWorkbook().createCellStyle(); 
    	centerStyle.cloneStyleFrom(defaultStyle);
    	centerStyle.setAlignment(HorizontalAlignment.CENTER);
    	
    	CellStyle boldCenterStyle = sheet.getWorkbook().createCellStyle(); 
    	boldCenterStyle.cloneStyleFrom(defaultStyle);
    	boldCenterStyle.setAlignment(HorizontalAlignment.CENTER);
    	boldCenterStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
    	boldCenterStyle.setFont(boldFont);
    	
        CellStyle lightYellowForeGround = sheet.getWorkbook().createCellStyle();
        lightYellowForeGround.cloneStyleFrom(centerStyle);
        lightYellowForeGround.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        lightYellowForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        Cell cell = row.createCell(colNum++);
        switch(rowName){
    	case GRAND_TOTAL: 
    		row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
    		sheet.addMergedRegion(new CellRangeAddress(14, 14, 0, 1));
            XSSFRichTextString allBoldString = new XSSFRichTextString(rowName.getLabel());
            allBoldString.applyFont(boldFont);
            cell.setCellValue(allBoldString);
            cell.setCellStyle(rowLabelStyle);

    		writeColumns(arsonRow, row, ++colNum, boldCenterStyle, boldCenterStyle);
    		break; 
    	case SINGLE_OCCUPANCY_RESIDENTIAL: 
    	case MOTOR_VEHICLES:
    		if (rowName == ArsonRowName.SINGLE_OCCUPANCY_RESIDENTIAL) {
    			sheet.addMergedRegion(new CellRangeAddress(2,9,0,0));
    			XSSFRichTextString boldString = new XSSFRichTextString("S\nT\nR\nU\nC\nT\nU\nR\nA\nL");
    			boldString.applyFont(boldFont);
                cell.setCellValue(boldString);
    			cell.setCellStyle(vCenterStyle);
    		}
    		else {
    			sheet.addMergedRegion(new CellRangeAddress(10,12,0,0));
    			XSSFRichTextString boldString = new XSSFRichTextString("M\nO\nB\nI\nL\nE");
    			boldString.applyFont(boldFont);
                cell.setCellValue(boldString);
    			cell.setCellStyle(vCenterStyle);
    		}
    	default: 
    		cell = row.createCell(colNum++);
    		cell.setCellValue(rowName.getLabel());
    		if (rowName == ArsonRowName.TOTAL_OTHER) {
    			cell.setCellStyle(totalOtherLabelStyle);
    		}
    		else {
    			cell.setCellStyle(rowLabelStyle);
    		}
    		if (Arrays.asList(ArsonRowName.TOTAL_STRUCTURE, ArsonRowName.TOTAL_OTHER, ArsonRowName.TOTAL_MOBILE).contains(rowName)) {
    			row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
    			writeColumns(arsonRow, row, colNum, boldCenterStyle, boldCenterStyle);
    		}
    		else {
    			writeColumns(arsonRow, row, colNum, centerStyle, lightYellowForeGround);
    		}
    	}
	}

	private void writeColumns(ArsonRow arsonRow, Row row, int colNum, CellStyle defaultStyle, CellStyle sideStyle) {
		Cell cell = row.createCell(colNum++);
		cell.setCellValue((Integer) arsonRow.getReportedOffenses());
		cell.setCellStyle(sideStyle);
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) arsonRow.getUnfoundedOffenses());
		cell.setCellStyle(sideStyle);
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) arsonRow.getActualOffenses());
		cell.setCellStyle(defaultStyle);
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) arsonRow.getClearedOffenses());
		cell.setCellStyle(sideStyle);
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) arsonRow.getClearanceInvolvingOnlyJuvenile());
		cell.setCellStyle(sideStyle);
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) arsonRow.getUninhabitedStructureOffenses());
		cell.setCellStyle(sideStyle);
		cell = row.createCell(colNum++);
		cell.setCellValue( arsonRow.getEstimatedPropertyDamage());
		cell.setCellStyle(sideStyle);
		cell.setCellType(CellType.NUMERIC);
	}
	private int createArsonHeaderRow(XSSFSheet sheet, int rowNum, Font boldFont,
			XSSFFont normalWeightFont) {
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
		
		CellStyle normalStyle = sheet.getWorkbook().createCellStyle();
		normalStyle.setWrapText(true);
		normalStyle.setAlignment(HorizontalAlignment.CENTER);
		normalStyle.setVerticalAlignment(VerticalAlignment.TOP);
		normalStyle.setBorderBottom(BorderStyle.THIN);
		normalStyle.setBorderTop(BorderStyle.THIN);
		normalStyle.setBorderRight(BorderStyle.THIN);
		normalStyle.setBorderLeft(BorderStyle.THIN);
		
		CellStyle boldStyle = sheet.getWorkbook().createCellStyle();
		boldStyle.cloneStyleFrom(normalStyle);
		boldStyle.setFont(boldFont);
		boldStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		
		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(0);
		cell.setCellStyle(normalStyle);
		cell.setCellValue("1\n\n\nPROPERTY CLASSIFICATION");
		
		
		Cell cell1 = row.createCell(2);
		cell1.setCellValue("2\nOffense Reported\nor Known to Police\n(Include Unfounded)");
		cell1.setCellStyle(normalStyle);
		
		Cell cell2 = row.createCell(3);
		cell2.setCellStyle(normalStyle);
		cell2.setCellValue("3\nUnfounded, i.e.,\nFalse or Baseless\nComplaints");
		
		Cell cell3 = row.createCell(4);
		cell3.setCellStyle(normalStyle);
		cell3.setCellValue("4\nNumber of\nActual Offenses\n(Column 2 Minus\nColumn 3)\n(Include Attempts)");
		
		Cell cell4 = row.createCell(5);
		cell4.setCellStyle(normalStyle);
		cell4.setCellValue("5\nTotal Offenses Cleared\nby Arrest or\nExceptional Means\n(Include Column 6)");
		
		Cell cell5 = row.createCell(6);
		cell5.setCellStyle(normalStyle);
		cell5.setCellValue("6\nNumber of\nClearances\nInvolving Only\nPersons Under\n18 Years of Age");
		
		Cell cell6 = row.createCell(7);
		cell6.setCellStyle(normalStyle);
		cell6.setCellValue("7\nOffenses Where\nStructures\nUninhabited,\nAbandoned, or not\nNormally in Use");
		
		Cell cell7 = row.createCell(8);
		cell7.setCellStyle(normalStyle);
		cell7.setCellValue("8\nEstimated Value of\nProperty Damage");
		
		RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), new CellRangeAddress(1, 1, 1, 1), sheet);
		
		return rowNum;
	}
	
	private int createArsonTitleRow(XSSFSheet sheet, int rowNum, CellStyle cs, Font boldFont, XSSFFont normalWeightFont) {
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((6*sheet.getDefaultRowHeightInPoints()));
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		Cell cell = row.createCell(0);
		
        CellStyle defaultStyle = sheet.getWorkbook().createCellStyle();
        defaultStyle.setWrapText(true);
        defaultStyle.setAlignment(HorizontalAlignment.LEFT);
        defaultStyle.setVerticalAlignment(VerticalAlignment.TOP);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("MONTHLY RETURN OF ARSON OFFENSES KNOWN TO LAW ENFORCEMENT");
		s1.applyFont(boldFont);
		s1.append("\nThis form is authorized by law Title 28, Section 534, U.S. Code, and the enactment of the fiscal year 1979, Department of Justice Authorization Bill S. 3151. Your cooperation");
		s1.append("\nin completing this form to report all monthly incidents of arson, will assist the FBI in compiling timely, comprehensive, and accurate data. Please submit this form and any questions");
		s1.append("\nto the FBI, Criminal Justice Information Services Division, Attention: Uniform Crime Reports/Module E-3, 1000 Custer Hollow Road, Clarksburg, West Virginia 26306; telephone 304-625-4830,");
		s1.append("\nfacsimile 304-625-3566. Under the Paperwork Reduction Act, you are not required to complete this form unless it contains a valid OMB control number. This form takes approximately 9 minutes");
		s1.append("\nto complete. Instructions appear on reverse side.");
		cell.setCellValue(s1);
		cell.setCellStyle(defaultStyle);
		
		cell = row.createCell(8);
		cell.setCellValue("1-725 (Rev. 08-08-11)\nOMB No. 1110-0008\nExpires 06-30-17");
		cell.setCellStyle(defaultStyle);

		return rowNum;
	}
    
}