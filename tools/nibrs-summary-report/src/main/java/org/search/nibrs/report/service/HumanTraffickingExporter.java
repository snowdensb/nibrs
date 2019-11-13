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
import org.apache.poi.ss.usermodel.Font;
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
import org.search.nibrs.model.reports.humantrafficking.HumanTraffickingForm;
import org.search.nibrs.model.reports.humantrafficking.HumanTraffickingFormRow;
import org.search.nibrs.model.reports.humantrafficking.HumanTraffickingRowName;
import org.search.nibrs.report.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanTraffickingExporter {
	private static final Log log = LogFactory.getLog(HumanTraffickingExporter.class);
	
	@Autowired
	private AppProperties appProperties;
	
	XSSFFont boldFont; 
	XSSFFont italicFont; 
	XSSFFont normalWeightFont; 
	XSSFFont underlineFont;
	CellStyle wrappedStyle;
	CellStyle wrappedBorderedStyle;
	CellStyle centeredStyle;
	CellStyle greyForeGround;

	public void exportHumanTraffickingReport(HumanTraffickingForm humanTraffickingForm){
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        XSSFSheet sheet = workbook.createSheet("HumanTraffickingOffenses");
		sheet.setFitToPage(true);
		PrintSetup ps = sheet.getPrintSetup();
		ps.setFitWidth( (short) 1);
		ps.setFitHeight( (short) 1);

        int rowNum = 0;
        log.info("Write to the excel file");
        wrappedStyle = workbook.createCellStyle();
        wrappedStyle.setWrapText(true);
        
        wrappedBorderedStyle = workbook.createCellStyle(); 
        wrappedBorderedStyle.cloneStyleFrom(wrappedStyle);
        wrappedBorderedStyle.setBorderBottom(BorderStyle.THIN);
        wrappedBorderedStyle.setBorderTop(BorderStyle.THIN);
        wrappedBorderedStyle.setBorderRight(BorderStyle.THIN);
        wrappedBorderedStyle.setBorderLeft(BorderStyle.THIN);

        centeredStyle = workbook.createCellStyle(); 
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);
        
        boldFont = workbook.createFont();
        boldFont.setBold(true);
        normalWeightFont = workbook.createFont();
        normalWeightFont.setBold(false);
        
        underlineFont = workbook.createFont();
        underlineFont.setUnderline(Font.U_SINGLE);
        
        italicFont=workbook.createFont(); 
        italicFont.setItalic(true);
        
        greyForeGround = workbook.createCellStyle();
        greyForeGround.cloneStyleFrom(wrappedStyle);
        greyForeGround.setAlignment(HorizontalAlignment.LEFT);
        greyForeGround.setVerticalAlignment(VerticalAlignment.CENTER);
        greyForeGround.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        greyForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    	rowNum = createHumanTraffickingTitleRow(sheet, rowNum);
    	
    	rowNum = addAdministrativeInformation(sheet, rowNum); 

    	
		rowNum = createHumanTraffickingHeaderRow(sheet, rowNum);

        for (HumanTraffickingRowName rowName: HumanTraffickingRowName.values()){
        	writeHumanTraffickingRow(sheet, rowName, humanTraffickingForm.getRows()[rowName.ordinal()], rowNum++);
        }

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		
        try {
        	String fileName = appProperties.getSummaryReportOutputPath() + "/HumanTrafficking-" + humanTraffickingForm.getOri() + "-" + humanTraffickingForm.getYear() + "-" + StringUtils.leftPad(String.valueOf(humanTraffickingForm.getMonth()), 2, '0') + ".xlsx"; 
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
            System.out.println("The Human Trafficking form is writen to fileName: " + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

	private int addAdministrativeInformation(XSSFSheet sheet, int rowNum) {
		
		rowNum ++;

		createGreayBarInfo(sheet, rowNum, "ADMINISTRATIVE INFORMATION");
		rowNum +=2;
		rowNum = addLabel(sheet, rowNum, "ORI Number:", "Enter the nine-character Originating Agency Identifier assigned to your agency."); 
		rowNum = addLabel(sheet, rowNum, "Month and Year:", "Enter the month and year of data being submitted.");
		rowNum = addLabel(sheet, rowNum, "Name of Agency:", "Enter the name of your agency.");
		rowNum = addLabel(sheet, rowNum, "Name and Title of Preparer:", "Enter the preparer's name and job title.");
		rowNum = addLabel(sheet, rowNum, "Telephone Number and E-mail address of Preparer:", "Enter the preparer's telephone number and e-mail address.");
		
		Row row = sheet.createRow(rowNum);
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 5));
		Cell cell= row.createCell(0);
        XSSFRichTextString label = new XSSFRichTextString("If there were no human trafficking offenses to report for the month, check this box.  ");
        label.applyFont(normalWeightFont);
        XSSFFont bigFont = sheet.getWorkbook().createFont();
        bigFont.setFontHeightInPoints((short)25);
        bigFont.setBold(true);

        label.append("\u25A1", bigFont);
		cell.setCellValue(label);
		
		rowNum +=2;
		createGreayBarInfo(sheet, rowNum, "HUMAN TRAFFICKING OFFENSES");
		
		rowNum ++;
		row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((Float.valueOf("0.5")*sheet.getDefaultRowHeightInPoints()));
		return rowNum;
	}

	private int addLabel(XSSFSheet sheet, int rowNum, String boldLabel, String normalLabel) {
		RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), new CellRangeAddress(rowNum, rowNum, 0, 4), sheet);
		rowNum ++;
		Row row = sheet.createRow(rowNum);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 5));
        Cell cell = row.createCell(0);
        XSSFRichTextString label = new XSSFRichTextString(boldLabel);
        label.applyFont(boldFont);
        label.append(normalLabel, normalWeightFont);
        cell.setCellValue(label);
        rowNum += 2;
		return rowNum;
	}

	private void createGreayBarInfo(XSSFSheet sheet, int rowNum, String message) {
        Font bigFont = sheet.getWorkbook().createFont();
        bigFont.setFontHeightInPoints((short)16);
        bigFont.setBold(true);
		Row row = sheet.createRow(rowNum);
    	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));

    	Cell cell =row.createCell(0);
    	cell.setCellStyle(greyForeGround);
    	
        XSSFRichTextString allBoldString = new XSSFRichTextString(message);
        
        allBoldString.applyFont(bigFont);
        cell.setCellValue(allBoldString);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 5));
		RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), new CellRangeAddress(rowNum, rowNum, 0, 5), sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN.getCode(), new CellRangeAddress(rowNum, rowNum, 0, 5), sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(rowNum, rowNum, 5, 5), sheet);
	}


	private int createHumanTraffickingTitleRow(XSSFSheet sheet, int rowNum) {
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		cell.setCellStyle(centeredStyle);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("MONTHLY RETURN OF HUMAN TRAFFICKING OFFENSES KNOWN TO LAW ENFORCEMENT");
		s1.applyFont(boldFont);
		cell.setCellValue(s1);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		
		row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((6*sheet.getDefaultRowHeightInPoints()));
		cell = row.createCell(0);
		cell.setCellStyle(wrappedStyle);
		
		XSSFRichTextString s2 = new XSSFRichTextString("This form is authorized by PL 110-457 (HR 7311), the ");
		s2.applyFont(normalWeightFont);
		s2.append("William Wilberforce Trafficking Victims Protection Reauthorization Act of 2008. ", italicFont);
		s2.append("Even though you are not required to respond, your cooperation in completing this form to report all monthly "
				+ "incidents of human trafficking will assist the FBI in compiling timely, comprehensive, and accurate data. "
				+ "Please submit this form to UCRstat@leo.gov and any questions to the FBI, Criminal Justice Information Services "
				+ "Division, Attention: Uniform Crime Reports/Module E-3, 1000 Custer Hollow Road, Clarksburg, West Virginia 26306; "
				+ "telephone 304-625-4830, facsimile 304-625-3566. Under the Paperwork Reduction Act, you are not required to "
				+ "complete this form unless it contains a valid OMB control number. "
				+ "This form takes approximately 5 minutes to complete.", normalWeightFont);
		cell.setCellValue(s2);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
		
		return rowNum;
	}
    
	private int createHumanTraffickingHeaderRow(XSSFSheet sheet, int rowNum) {
        
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((5*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		
        CellStyle column0Style = sheet.getWorkbook().createCellStyle();
        column0Style.setWrapText(true);
        column0Style.setAlignment(HorizontalAlignment.CENTER);
        column0Style.setVerticalAlignment(VerticalAlignment.TOP);
        column0Style.setBorderBottom(BorderStyle.THIN);
        column0Style.setBorderTop(BorderStyle.THIN);
        column0Style.setBorderLeft(BorderStyle.THIN);
        column0Style.setBorderRight(BorderStyle.THIN);

		cell.setCellStyle(column0Style);
		cell.setCellValue("1\n\n Human Trafficking CLASSIFICATION");
		
		Cell cell1 = row.createCell(1);
		cell1.setCellStyle(column0Style);
		cell1.setCellValue("2 \n\n\n Offenses reported");
		
		Cell cell2 = row.createCell(2);
		cell2.setCellStyle(column0Style);
		cell2.setCellValue("3 \n Unfounded, i.e.\n false or baseless \n complaints");
		
		Cell cell3 = row.createCell(3);
		cell3.setCellStyle(column0Style);
		cell3.setCellValue("4 \n Number of actual \n offenses ( column 2 \n minus column 3) \n (include attempts)");
		
		Cell cell4 = row.createCell(4);
		cell4.setCellStyle(column0Style);
		cell4.setCellValue("5 \n Total Offenses\n cleared by offenses or \n exceptional means");
		
		Cell cell5 = row.createCell(5);
		cell5.setCellStyle(column0Style);
		cell5.setCellValue("6 \n Number of clearances\n involving only \n persons under 18 \n years of age)");
		
		return rowNum;
	}

    private void writeHumanTraffickingRow(XSSFSheet sheet, HumanTraffickingRowName rowName, HumanTraffickingFormRow returnAFormRow, int rowNum) {
    	Row row = sheet.createRow(rowNum);
    	int colNum = 0;
    	Cell cell = row.createCell(colNum++);
    	cell.setCellStyle(wrappedBorderedStyle);
        
        if (rowName == HumanTraffickingRowName.GRAND_TOTAL) {
            XSSFRichTextString allBoldString = new XSSFRichTextString(rowName.getLabel());
            allBoldString.applyFont(boldFont);
            cell.setCellValue(allBoldString);
        }
        else {
    		cell.setCellValue(rowName.getLabel());
    	}
        CellStyle yellowForeGround = sheet.getWorkbook().createCellStyle();
        yellowForeGround.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        yellowForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        yellowForeGround.setBorderBottom(BorderStyle.THIN);
        yellowForeGround.setBorderTop(BorderStyle.THIN);
        yellowForeGround.setBorderLeft(BorderStyle.THIN);
        yellowForeGround.setBorderRight(BorderStyle.THIN);

		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) returnAFormRow.getReportedOffenses());
		if (rowName != HumanTraffickingRowName.GRAND_TOTAL) {
			cell.setCellStyle(yellowForeGround);
		}
		else {
			cell.setCellStyle(wrappedBorderedStyle);
		}
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) returnAFormRow.getUnfoundedOffenses());
		if (rowName != HumanTraffickingRowName.GRAND_TOTAL) {
			cell.setCellStyle(yellowForeGround);
		}
		else {
			cell.setCellStyle(wrappedBorderedStyle);
		}
		cell = row.createCell(colNum++);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue((Integer) returnAFormRow.getActualOffenses());
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) returnAFormRow.getClearedOffenses());
		if (rowName != HumanTraffickingRowName.GRAND_TOTAL) {
			cell.setCellStyle(yellowForeGround);
		}
		else {
			cell.setCellStyle(wrappedBorderedStyle);
		}
		cell = row.createCell(colNum++);
		cell.setCellValue((Integer) returnAFormRow.getClearanceInvolvingOnlyJuvenile());
		if (rowName != HumanTraffickingRowName.GRAND_TOTAL) {
			cell.setCellStyle(yellowForeGround);
		}
		else {
			cell.setCellStyle(wrappedBorderedStyle);

		}

	}

	
}