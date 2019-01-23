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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
        
    	rowNum = createTheTitleRow(sheet, rowNum, wrappedStyle, boldFont, normalWeightFont);
		rowNum = createTheTableHeaderRow(sheet, rowNum, boldFont, normalWeightFont);

        for (ReturnARowName rowName: ReturnARowName.values()){
        	writeRow(sheet, rowName, returnAForm.getRows()[rowName.ordinal()], rowNum++, boldFont);
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
        	String fileName = appProperties.getReturnAFormOutputPath() + "/AsrAdult" + returnAForm.getOri() + "-" + returnAForm.getYear() + "-" + StringUtils.leftPad(String.valueOf(returnAForm.getMonth()), 2, '0') + ".xlsx"; 
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

	private int createTheTitleRow(XSSFSheet sheet, int rowNum, CellStyle cs, Font boldFont, XSSFFont normalWeightFont) {
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
    
	private int createTheTableHeaderRow(XSSFSheet sheet, int rowNum, Font boldFont, XSSFFont normalWeightFont) {
        
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
	
    private void writeRow(XSSFSheet sheet, ReturnARowName rowName, ReturnAFormRow returnAFormRow, int rowNum, Font boldFont) {
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