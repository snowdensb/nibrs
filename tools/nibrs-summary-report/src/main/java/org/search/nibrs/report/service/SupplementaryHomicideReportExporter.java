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
import java.util.List;

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
import org.search.nibrs.model.reports.supplementaryhomicide.Person;
import org.search.nibrs.model.reports.supplementaryhomicide.SupplementaryHomicideReport;
import org.search.nibrs.model.reports.supplementaryhomicide.SupplementaryHomicideReportRow;
import org.search.nibrs.report.SummaryReportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplementaryHomicideReportExporter {
	private static final Log log = LogFactory.getLog(SupplementaryHomicideReportExporter.class);
	
	@Autowired
	private SummaryReportProperties appProperties;
	
	XSSFFont boldFont; 
	XSSFFont italicFont; 
	XSSFFont normalWeightFont; 
	XSSFFont underlineFont;
	CellStyle wrappedStyle;
	CellStyle vTopWrappedStyle;
	CellStyle wrappedBorderedStyle;
	CellStyle centeredWrappedBorderedStyle;
	CellStyle centeredStyle;
	CellStyle greyForeGround;
	CellStyle rotateStyle; 

	public void exportSupplementaryHomicideReport(SupplementaryHomicideReport supplementaryHomicideReport){
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
        
        rotateStyle = workbook.createCellStyle();
        rotateStyle.setRotation((short)90);
        rotateStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        rotateStyle.setAlignment(HorizontalAlignment.CENTER);
        rotateStyle.setBorderBottom(BorderStyle.THIN);
        rotateStyle.setBorderTop(BorderStyle.THIN);
        rotateStyle.setBorderLeft(BorderStyle.THIN);
        rotateStyle.setBorderRight(BorderStyle.THIN);

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
        
        createNonNegligentSheet(supplementaryHomicideReport, workbook);
        createNegligentSheet(supplementaryHomicideReport, workbook);
        
        try {
        	String fileName = appProperties.getSummaryReportOutputPath() + "/SupplementaryHomicideReport-" + supplementaryHomicideReport.getOri() + "-" + supplementaryHomicideReport.getYear() + "-" + StringUtils.leftPad(String.valueOf(supplementaryHomicideReport.getMonth()), 2, '0') + ".xlsx"; 
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

	private void createNonNegligentSheet(SupplementaryHomicideReport supplementaryHomicideReport, XSSFWorkbook workbook) {
		XSSFSheet sheet = workbook.createSheet("SHR-non-negligent");
		sheet.setFitToPage(true);
		PrintSetup ps = sheet.getPrintSetup();
		ps.setLandscape(true);
		ps.setFitWidth( (short) 1);
		ps.setFitHeight( (short) 1);

        int rowNum = 0;
        log.info("Write to the excel file");

    	rowNum = createMurderAndNonNegligentTitleRow(sheet, rowNum);
    	
		rowNum = createSupplementaryHomicideReportHeaderRow(sheet, rowNum, true);
		
        for (SupplementaryHomicideReportRow supplementaryHomicideReportRow: supplementaryHomicideReport.getMurderAndNonNegligenceManslaughter()){
        	writeSupplementaryHomicideReportRow(sheet, supplementaryHomicideReportRow, rowNum++);
        }
        
		if (supplementaryHomicideReport.getMurderAndNonNegligenceManslaughter().isEmpty()) {
	    	rowNum = createEmptyRow(sheet, rowNum++);
		}

		setBordersToMergedCells(sheet, 0, rowNum);
		addAdministrativeInformation(sheet, rowNum, supplementaryHomicideReport);
		setColumnsWidth(sheet);
	}

	private void createNegligentSheet(SupplementaryHomicideReport supplementaryHomicideReport, XSSFWorkbook workbook) {
		XSSFSheet sheet = workbook.createSheet("SHR-negligent");
		sheet.setFitToPage(true);
		PrintSetup ps = sheet.getPrintSetup();
		ps.setLandscape(true);
		ps.setFitWidth( (short) 1);
		ps.setFitHeight( (short) 1);
		
		int rowNum = 0;
		log.info("Write to the excel file");
		
		rowNum = createNegligentTitleRow(sheet, rowNum);
		
		rowNum = createSupplementaryHomicideReportHeaderRow(sheet, rowNum, false);
		
		for (SupplementaryHomicideReportRow supplementaryHomicideReportRow: supplementaryHomicideReport.getManslaughterByNegligence()){
			writeSupplementaryHomicideReportRow(sheet, supplementaryHomicideReportRow, rowNum++);
		}
		
		if (supplementaryHomicideReport.getManslaughterByNegligence().size() == 0) {
	    	rowNum = createEmptyRow(sheet, rowNum);
		}

		setBordersToMergedCells(sheet, 1, rowNum+1);
		
		rowNum = addAsteriskInformation(sheet, rowNum);
		
		setColumnsWidth(sheet);
	}

	private int addAsteriskInformation(XSSFSheet sheet, int rowNum) {
		rowNum +=2;
		Row row = sheet.createRow(rowNum);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
        Cell cell = row.createCell(0);
        cell.setCellValue("* - Situations");
        
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 13));
        cell = row.createCell(2);
        cell.setCellValue("A - SingleVictim/SingleOffender");
        
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 14, 16));
        cell = row.createCell(14);
        cell.setCellValue("D - MultipleVictims/SingleOffender");
        
        rowNum++; 
		row = sheet.createRow(rowNum);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 13));
        cell = row.createCell(2);
        cell.setCellValue("B - SingleVictim/UnknownOffenderorOffenders");
        
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 14, 16));
        cell = row.createCell(14);
        cell.setCellValue("E - MultipleVictims/MultipleOffenders");
        
        rowNum++;
		row = sheet.createRow(rowNum);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 13));
        cell = row.createCell(2);
        cell.setCellValue("C - SingleVictim/MultipleOffenders");
        
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 14, 16));
        cell = row.createCell(14);
        cell.setCellValue("F - MultipleVictims/UnknownOffenderorOffenders");

        rowNum += 2;
		row = sheet.createRow(rowNum);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 16));
        cell = row.createCell(0);
        cell.setCellValue("Use only one victim/offender situation code per set of information. "
        		+ "The utilization of a new code will signify the beginning of a new murder situation.");
        
		rowNum += 2;
		row = sheet.createRow(rowNum);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
        cell = row.createCell(0);
        cell.setCellValue("** - Age");
        
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 16));
        cell = row.createCell(2);
        cell.setCellValue("- 01 to 99. If 100 or older use 99. New born up to one week old use NB. If over one week, but less than one year old use BB. Use two characters only in age column.");
        
        rowNum ++;
        row = sheet.createRow(rowNum);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
        cell = row.createCell(0);
        cell.setCellValue("     Sex");
        
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 16));
        cell = row.createCell(2);
        cell.setCellValue("- M for Male and F for Female. Use one character only.");
        
        rowNum ++;
        row = sheet.createRow(rowNum);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
        cell = row.createCell(0);
        cell.setCellValue("     Race");
        
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 16));
        cell = row.createCell(2);
        cell.setCellValue("- White - W, Black - B, American Indian or Alaskan Native - I, Asian - A, Pacific Islander - P, Unknown - U. Use only these as race designations.");
        
        rowNum ++;
        row = sheet.createRow(rowNum);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
        cell = row.createCell(0);
        cell.setCellValue("     Ethnicity");
        
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 2, 16));
        cell = row.createCell(2);
        cell.setCellValue("- Hispanic Origin - H, Not of Hispanic Origin - N, Unknown - U.");
		return rowNum;
	}

	private int createEmptyRow(XSSFSheet sheet, int rowNum) {
		Row row = sheet.createRow(rowNum);
		for (int colNum = 0; colNum < 15; colNum++) {
			Cell cell = row.createCell(colNum);
			cell.setCellStyle(wrappedBorderedStyle);
		}
		Cell cell = row.createCell(15);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 15, 16));
		cell.setCellStyle(wrappedBorderedStyle);
		return rowNum;
	}
	
	private void setColumnsWidth(XSSFSheet sheet) {
		sheet.autoSizeColumn(0);
		
		for (int i = 1; i < 10; i++) {
			sheet.setColumnWidth(i, 100 * sheet.getDefaultColumnWidth());
		}
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(10);
		sheet.autoSizeColumn(11);
		sheet.autoSizeColumn(12);
		for (int i = 13; i < 15; i++) {
			sheet.setColumnWidth(i, 850 * sheet.getDefaultColumnWidth());
		}
		sheet.setColumnWidth(15, 475 * sheet.getDefaultColumnWidth());
		sheet.setColumnWidth(16, 475 * sheet.getDefaultColumnWidth());
	}

	private int addAdministrativeInformation(XSSFSheet sheet, int rowNum, SupplementaryHomicideReport supplementaryHomicideReport) {
		
		rowNum ++;

		Row row = sheet.createRow(rowNum);
    	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 8));
    	Cell cell =row.createCell(0);
    	cell.setCellValue("*\n** - see the SHR-negligent tab for explanation");
    	cell.setCellStyle(wrappedStyle);
		
    	rowNum ++; 
		row = sheet.createRow(rowNum);
		cell= row.createCell(15);
		cell.setCellStyle(centeredWrappedBorderedStyle);
		cell.setCellValue("DO NOT WRITE HERE");
		CellRangeAddress mergedRegions = new CellRangeAddress(rowNum, rowNum, 15, 16); 
		sheet.addMergedRegion(mergedRegions);
        RegionUtil.setBorderLeft(BorderStyle.THIN.getCode(), mergedRegions, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), mergedRegions, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN.getCode(), mergedRegions, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);

		rowNum ++; 
		row = sheet.createRow(rowNum);
		cell= row.createCell(15);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue("Recorded");
		cell= row.createCell(16);
		cell.setCellStyle(wrappedBorderedStyle);
		
		rowNum ++; 
		row = sheet.createRow(rowNum);
		
		cell = row.createCell(0); 
		cell.setCellStyle(centeredStyle); 
		cell.setCellValue(supplementaryHomicideReport.getMonthString() + "/" + supplementaryHomicideReport.getYear());
		mergedRegions = new CellRangeAddress(rowNum, rowNum, 0, 1);
		sheet.addMergedRegion(mergedRegions);
        RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);
		
        cell = row.createCell(3); 
        cell.setCellStyle(centeredStyle); 
        cell.setCellValue(supplementaryHomicideReport.getOri());
        mergedRegions = new CellRangeAddress(rowNum, rowNum, 3, 8);
		sheet.addMergedRegion(mergedRegions);
        RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);
        
        cell = row.createCell(10); 
        cell.setCellStyle(centeredStyle); 
        mergedRegions = new CellRangeAddress(rowNum, rowNum, 10, 11);
		sheet.addMergedRegion(mergedRegions);
        RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);
        
        cell = row.createCell(13); 
        cell.setCellStyle(centeredStyle); 
        mergedRegions = new CellRangeAddress(rowNum, rowNum, 13, 13);
        RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);
        
		cell= row.createCell(15);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue("Edited");
		cell= row.createCell(16);
		cell.setCellStyle(wrappedBorderedStyle);
		
		rowNum ++; 
		row = sheet.createRow(rowNum);
		cell = row.createCell(0); 
		cell.setCellStyle(centeredStyle); 
		cell.setCellValue("Month and Year");
		mergedRegions = new CellRangeAddress(rowNum, rowNum, 0, 1);
		sheet.addMergedRegion(mergedRegions);
		
        cell = row.createCell(3); 
        cell.setCellStyle(centeredStyle); 
        cell.setCellValue("Agency Identifier");
        mergedRegions = new CellRangeAddress(rowNum, rowNum, 3, 8);
		sheet.addMergedRegion(mergedRegions);
        
        cell = row.createCell(10); 
        cell.setCellStyle(centeredStyle); 
        cell.setCellValue("Prepared By");
        mergedRegions = new CellRangeAddress(rowNum, rowNum, 10, 11);
		sheet.addMergedRegion(mergedRegions);
        
        cell = row.createCell(13); 
        cell.setCellStyle(centeredStyle);
        cell.setCellValue("Title");
        
		cell= row.createCell(15);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue("Entered");
		cell= row.createCell(16);
		cell.setCellStyle(wrappedBorderedStyle);
		
		rowNum ++; 
		row = sheet.createRow(rowNum);
		cell= row.createCell(15);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue("Verified");
		cell= row.createCell(16);
		cell.setCellStyle(wrappedBorderedStyle);
		
		rowNum ++; 
		row = sheet.createRow(rowNum);
		cell= row.createCell(15);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue("Adjusted");
		cell= row.createCell(16);
		cell.setCellStyle(wrappedBorderedStyle);
		
		rowNum ++; 
		row = sheet.createRow(rowNum);
		
		cell = row.createCell(0); 
		cell.setCellStyle(centeredStyle); 
		cell.setCellValue(supplementaryHomicideReport.getAgencyName());
		mergedRegions = new CellRangeAddress(rowNum, rowNum, 0, 1);
		sheet.addMergedRegion(mergedRegions);
        RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);
		
        cell = row.createCell(3); 
        cell.setCellStyle(centeredStyle); 
        cell.setCellValue(supplementaryHomicideReport.getStateName());
        mergedRegions = new CellRangeAddress(rowNum, rowNum, 3, 8);
		sheet.addMergedRegion(mergedRegions);
        RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);
        
        cell = row.createCell(10); 
        cell.setCellStyle(centeredStyle); 
        mergedRegions = new CellRangeAddress(rowNum, rowNum, 10, 13);
		sheet.addMergedRegion(mergedRegions);
        RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);
        
        rowNum ++; 
        row = sheet.createRow(rowNum);
        
        cell = row.createCell(0); 
        cell.setCellStyle(centeredStyle); 
        cell.setCellValue("Agency");
        mergedRegions = new CellRangeAddress(rowNum, rowNum, 0, 1);
        sheet.addMergedRegion(mergedRegions);
        
        cell = row.createCell(3); 
        cell.setCellStyle(centeredStyle); 
        cell.setCellValue("State");
        mergedRegions = new CellRangeAddress(rowNum, rowNum, 3, 8);
        sheet.addMergedRegion(mergedRegions);
        
        cell = row.createCell(10); 
        cell.setCellStyle(centeredStyle); 
        cell.setCellValue("Sheriff, Chief, Superintendent, Commanding Officer");
        mergedRegions = new CellRangeAddress(rowNum, rowNum, 10, 13);
        sheet.addMergedRegion(mergedRegions);
        
		return rowNum;
	}

	private int createNegligentTitleRow(XSSFSheet sheet, int rowNum) {
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		cell.setCellStyle(centeredStyle);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("SUPPLEMENTARY HOMICIDE REPORT");
		s1.applyFont(boldFont);
		cell.setCellValue(s1);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
		
		row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((4*sheet.getDefaultRowHeightInPoints()));
		cell = row.createCell(0);
		cell.setCellStyle(vTopWrappedStyle);
		
		XSSFRichTextString s2 = new XSSFRichTextString("1b. Manslaughter by Negligence \n");
		s2.applyFont(normalWeightFont);
		s2.append("      Do not list traffic fatalities, accidental deaths, or death due to the negligence of the victim. "
				+ "List below all other negligent manslaughters, regardless of prosecutive action taken.", normalWeightFont);
		cell.setCellValue(s2);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 16));
		
		return rowNum;
	}
    
	private int createMurderAndNonNegligentTitleRow(XSSFSheet sheet, int rowNum) {
		Row row = sheet.createRow(rowNum++);
		row.setHeightInPoints((10*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		cell.setCellStyle(vTopWrappedStyle);

		XSSFRichTextString s2 = new XSSFRichTextString("This report is authorized by law Title 28, Section 534, U.S. Code. "
				+ "Your cooperation in using this form to list data pertaining to all homicides reported on your " 
				+ "Return A will assist the FBI in compiling comprehensive, accurate data regarding this important classification "
				+ "on a timely basis. Any questions regarding this report may be addressed to the FBI, Criminal Justice Information "
				+ "Services Division, Attention: Uniform Crime Reports/Module E-3, 1000 Custer Hollow Road, Clarksburg, West "
				+ "Virginia 26306; telephone 304-625-4830, facsimile 304-625-3566. Under the Paperwork Reduction Act, you are not "
				+ "required to complete this form unless it contains a valid OMB control number. "
				+ "The form takes approximately 9 minutes to complete. ");
		s2.applyFont(normalWeightFont);
		s2.append("\n\n1a. Murder and Nonnegligent Manslaughter\n", normalWeightFont);
		s2.append("               List below for each category specific information for each murder and nonnegligent homicide and/or "
				+ "justifiable homicide shown in item 1a of the monthly Return A. In\n" + 
				"addition, for justifiable homicide list all justifiable killings of felons by a citizen or by a peace officer in the "
				+ "line of duty. A brief explanation in the circumstances column regarding unfounded homicide offenses will aid the "
				+ "national Uniform Crime Reporting Program in editing the reports.", normalWeightFont);
		cell.setCellValue(s2);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
		
		return rowNum;
	}
	
	private int createSupplementaryHomicideReportHeaderRow(XSSFSheet sheet, int rowNum, boolean nonNegligent) {
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 5, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 5, 1, 1));

        Row row = sheet.createRow(rowNum++);
        Cell cell = row.createCell(0);
		cell.setCellStyle(centeredWrappedBorderedStyle);
		cell.setCellValue("Incident");

		cell=row.createCell(1);
		cell.setCellStyle(rotateStyle);
		cell.setCellValue("Situation*");
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,2,5));
		Cell cell2 = row.createCell(2);
		cell2.setCellStyle(centeredWrappedBorderedStyle);
		cell2.setCellValue("Victim**");

		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,6,9));
		Cell cell3 = row.createCell(6);
		cell3.setCellStyle(centeredWrappedBorderedStyle);
		cell3.setCellValue("Offender**");
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1,10,12));
		Cell cell4 = row.createCell(10);
		cell4.setCellStyle(centeredWrappedBorderedStyle);
		cell4.setCellValue("Data Code");
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1 + 5,13,13));
		Cell cell5 = row.createCell(13);
		cell5.setCellStyle(centeredWrappedBorderedStyle);
		if (nonNegligent) {
			cell5.setCellValue("Weapon Used\n (Handgun, Rifle, Shotgun,\n Club, Poison, etc.)");
		}
		else {
			cell5.setCellValue("Weapon Used\n (Handgun, Rifle, Shotgun,\n Knife, etc.)");
		}
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1 + 5,14,14));
		Cell cell6 = row.createCell(14);
		cell6.setCellStyle(centeredWrappedBorderedStyle);
		cell6.setCellValue("Relationship of Victim\n to Offender\n (Husband, Wife, Son,\n Father, Acquaintance,\n Neighbor, Stranger, etc.)");
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1 + 5,15,16));
		Cell cell7 = row.createCell(15);
		cell7.setCellStyle(centeredWrappedBorderedStyle);
		if (nonNegligent) {
			cell7.setCellValue("Circumstances\n(Victim shot by robber, robbery victim\n shot robber, killed by patron during\n barroom brawl, etc.)");
		}
		else {
			cell7.setCellValue("Circumstances\n(Victim shot in hunting accident, gun-\ncleaning, children playing with gun, etc.)");
		}
		
		row = sheet.createRow(rowNum++);
		List<String> demographics = Arrays.asList("Age", "Sex", "Race", "Ethnicity"); 
		for (int i=2, j=0;  j <demographics.size(); i++, j++) {
			sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum + 3,i,i));
			cell2 = row.createCell(i);
			cell2.setCellStyle(rotateStyle);
			cell2.setCellValue(demographics.get(j));
		}
		for (int i=6, j=0;  j <demographics.size(); i++, j++) {
			sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum + 3,i,i));
			cell2 = row.createCell(i);
			cell2.setCellStyle(rotateStyle);
			cell2.setCellValue(demographics.get(j));
		}

		cell = row.createCell(10);
		cell.setCellValue("Do Not Write\n In These Spaces");
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum + 3,10,12));
		
		rowNum += 4; 
		return rowNum;
	}

    private void writeSupplementaryHomicideReportRow(XSSFSheet sheet, SupplementaryHomicideReportRow supplementaryHomicideReportRow, int rowNum) {
    	Row row = sheet.createRow(rowNum);
    	int colNum = 0;
    	Cell cell = row.createCell(colNum++);
    	cell.setCellStyle(wrappedBorderedStyle);
    	cell.setCellValue(supplementaryHomicideReportRow.getIncidentNumber());
    	
		cell = row.createCell(colNum++);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue(supplementaryHomicideReportRow.getHomicideSituation().code);

		addPersonCells(row, colNum, supplementaryHomicideReportRow.getVictim());
		colNum += 4; 
		addPersonCells(row, colNum, supplementaryHomicideReportRow.getOffender());
		
		colNum += 4; 
		
		for ( int  j = 0; j< 3; j++) {
			cell = row.createCell(colNum++);
			cell.setCellStyle(wrappedBorderedStyle);
		}
		
		cell = row.createCell(colNum++);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue(StringUtils.join(supplementaryHomicideReportRow.getWeaponUsed(), ','));

		cell = row.createCell(colNum++);
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue(supplementaryHomicideReportRow.getRelationshipOfVictimToOffender());
		
		cell = row.createCell(colNum++);
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum,15,16));
		cell.setCellStyle(wrappedBorderedStyle);
		cell.setCellValue(StringUtils.join(supplementaryHomicideReportRow.getCircumstances(), ','));
		
	}

	private void addPersonCells(Row row, int colNum, Person person) {
		Cell cell;
		cell = row.createCell(colNum++);
		cell.setCellValue(person.getAge());
		cell.setCellStyle(wrappedBorderedStyle);
		
		cell = row.createCell(colNum++);
		cell.setCellValue(person.getSex());
		cell.setCellStyle(wrappedBorderedStyle);
		
		cell = row.createCell(colNum++);
		cell.setCellValue(person.getRace());
		cell.setCellStyle(wrappedBorderedStyle);
		
		cell = row.createCell(colNum++);
		cell.setCellValue(person.getEthnicity());
		cell.setCellStyle(wrappedBorderedStyle);
	}
    
    private void setBordersToMergedCells(XSSFSheet sheet, int minRowNum, int maxRowNum) {
        int numMerged = sheet.getNumMergedRegions();
        for (int i = 0; i < numMerged; i++) {
            CellRangeAddress mergedRegions = sheet.getMergedRegion(i);

            System.out.println("CellRangeAddress:" + mergedRegions);
            if (mergedRegions.getFirstRow() > minRowNum && mergedRegions.getFirstRow() < maxRowNum) {
            	System.out.println("Add borders");
	            RegionUtil.setBorderLeft(BorderStyle.THIN.getCode(), mergedRegions, sheet);
	            RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), mergedRegions, sheet);
	            RegionUtil.setBorderTop(BorderStyle.THIN.getCode(), mergedRegions, sheet);
	            RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);
            }

        }
    }
	
}