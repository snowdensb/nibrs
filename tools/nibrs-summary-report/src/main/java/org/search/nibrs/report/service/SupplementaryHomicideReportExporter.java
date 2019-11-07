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
import org.search.nibrs.model.reports.humantrafficking.HumanTraffickingFormRow;
import org.search.nibrs.model.reports.humantrafficking.HumanTraffickingRowName;
import org.search.nibrs.model.reports.supplementaryhomicide.SupplementaryHomicideReport;
import org.search.nibrs.report.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplementaryHomicideReportExporter {
	private static final Log log = LogFactory.getLog(SupplementaryHomicideReportExporter.class);
	
	@Autowired
	private AppProperties appProperties;
	
	XSSFFont boldFont; 
	XSSFFont italicFont; 
	XSSFFont normalWeightFont; 
	XSSFFont underlineFont;
	CellStyle wrappedStyle;
	CellStyle wrappedBorderedStyle;
	CellStyle centeredWrappedBorderedStyle;
	CellStyle centeredStyle;
	CellStyle greyForeGround;
	CellStyle rotateStyle; 

	public void exportSupplementaryHomicideReport(SupplementaryHomicideReport supplementaryHomicideReport){
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        wrappedStyle = workbook.createCellStyle();
        wrappedStyle.setWrapText(true);
        
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
		
        try {
        	String fileName = appProperties.getReturnAFormOutputPath() + "/SupplementaryHomicideReport-" + supplementaryHomicideReport.getOri() + "-" + supplementaryHomicideReport.getYear() + "-" + StringUtils.leftPad(String.valueOf(supplementaryHomicideReport.getMonth()), 2, '0') + ".xlsx"; 
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
		rowNum = createSupplementaryHomicideReportHeaderRow(sheet, rowNum);
		
		setBordersToMergedCells(sheet);
//		rowNum = addAdministrativeInformation(sheet, rowNum); 

//        for (HumanTraffickingRowName rowName: HumanTraffickingRowName.values()){
//        	writeHumanTraffickingRow(sheet, rowName, supplementaryHomicideReport.getRows()[rowName.ordinal()], rowNum++);
//        }

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
		sheet.setColumnWidth(15, 950 * sheet.getDefaultColumnWidth());
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


	private int createNegligenceTitleRow(XSSFSheet sheet, int rowNum) {
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		cell.setCellStyle(centeredStyle);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("SUPPLEMENTARY HOMICIDE REPORT");
		s1.applyFont(boldFont);
		cell.setCellValue(s1);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		
		row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((6*sheet.getDefaultRowHeightInPoints()));
		cell = row.createCell(0);
		cell.setCellStyle(wrappedStyle);
		
		XSSFRichTextString s2 = new XSSFRichTextString("1b. Manslaughter by Negligence \n");
		s2.applyFont(normalWeightFont);
		s2.append("      Do not list traffic fatalities, accidental deaths, or death due to the negligence of the victim. "
				+ "List below all other negligent manslaughters, regardless of prosecutive action taken.", normalWeightFont);
		cell.setCellValue(s2);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
		
		return rowNum;
	}
    
	private int createMurderAndNonNegligentTitleRow(XSSFSheet sheet, int rowNum) {
		Row row = sheet.createRow(rowNum++);
		row.setHeightInPoints((9*sheet.getDefaultRowHeightInPoints()));
		Cell cell = row.createCell(0);
		cell.setCellStyle(wrappedStyle);
		
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
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
		
		return rowNum;
	}
	
	private int createSupplementaryHomicideReportHeaderRow(XSSFSheet sheet, int rowNum) {
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 6, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 6, 1, 1));

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
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1 + 6,13,13));
		Cell cell5 = row.createCell(13);
		cell5.setCellStyle(centeredWrappedBorderedStyle);
		cell5.setCellValue("Weapon Used\n (Handgun, Rifle, Shotgun,\n Club, Poison, etc.)");
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1 + 6,14,14));
		Cell cell6 = row.createCell(14);
		cell6.setCellStyle(centeredWrappedBorderedStyle);
		cell6.setCellValue("Relationship of Victim\n to Offender\n (Husband, Wife, Son,\n Father, Acquaintance,\n Neighbor, Stranger, etc.)");
		
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum-1 + 6,15,15));
		Cell cell7 = row.createCell(15);
		cell7.setCellStyle(centeredWrappedBorderedStyle);
		cell7.setCellValue("Circumstances\n(Victim shot by robber, robbery victim\n shot robber, killed by patron during\n barroom brawl, etc.)");
		
		row = sheet.createRow(rowNum++);
		List<String> demographics = Arrays.asList("Age", "Sex", "Race", "Ethnicity"); 
		for (int i=2, j=0;  j <demographics.size(); i++, j++) {
			sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum + 4,i,i));
			cell2 = row.createCell(i);
			cell2.setCellStyle(rotateStyle);
			cell2.setCellValue(demographics.get(j));
		}
		for (int i=6, j=0;  j <demographics.size(); i++, j++) {
			sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum + 4,i,i));
			cell2 = row.createCell(i);
			cell2.setCellStyle(rotateStyle);
			cell2.setCellValue(demographics.get(j));
		}

		cell = row.createCell(10);
		cell.setCellValue("Do Not Write\n In These Spaces");
		sheet.addMergedRegion(new CellRangeAddress(rowNum-1,rowNum + 4,10,12));
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
    
    private void setBordersToMergedCells(XSSFSheet sheet) {
        int numMerged = sheet.getNumMergedRegions();
        for (int i = 0; i < numMerged; i++) {
            CellRangeAddress mergedRegions = sheet.getMergedRegion(i);

            System.out.println("CellRangeAddress:" + mergedRegions);
            if (mergedRegions.getFirstRow() != 0) {
            	System.out.println("Add borders");
	            RegionUtil.setBorderLeft(BorderStyle.THIN.getCode(), mergedRegions, sheet);
	            RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), mergedRegions, sheet);
	            RegionUtil.setBorderTop(BorderStyle.THIN.getCode(), mergedRegions, sheet);
	            RegionUtil.setBorderBottom(BorderStyle.THIN.getCode(), mergedRegions, sheet);
            }

        }
    }
	
}