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
import org.search.nibrs.model.reports.asr.AsrAdult;
import org.search.nibrs.model.reports.asr.AsrAdultRow;
import org.search.nibrs.model.reports.asr.AsrAdultRowName;
import org.search.nibrs.report.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsrAdultExcelExporter {
	private static final Log log = LogFactory.getLog(AsrAdultExcelExporter.class);
	
	@Autowired
	private AppProperties appProperties;

    public void exportAsrAdultForm(AsrAdult asrAdult){
        XSSFWorkbook workbook = new XSSFWorkbook();
        
        createSheet(asrAdult, workbook);
		
        try {
        	String fileName = appProperties.getReturnAFormOutputPath() + "/ASR-ADULT-" + asrAdult.getOri() + "-" + asrAdult.getYear() + "-" + StringUtils.leftPad(String.valueOf(asrAdult.getMonth()), 2, '0') + ".xlsx"; 
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
	private void createSheet(AsrAdult asrAdult, XSSFWorkbook workbook ) {
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

        XSSFSheet sheet = workbook.createSheet("ASR-ADULT");
		sheet.setFitToPage(true);
		PrintSetup ps = sheet.getPrintSetup();
		ps.setFitWidth( (short) 1);
		ps.setFitHeight( (short) 0);

        rowNum = createAsrAdultTitleRow(sheet, rowNum, wrappedStyle, boldFont, normalWeightFont);
		createAsrAdultHeaderRow(sheet, rowNum, boldFont, normalWeightFont);
		
		rowNum = 7;
        for (AsrAdultRowName rowName: AsrAdultRowName.values()){
        	writeAsrAdultRow(sheet, rowName, asrAdult.getRows()[rowName.ordinal()], rowNum, boldFont);
        	rowNum += 2;
        }
        
		sheet.setColumnWidth(0, 700 * sheet.getDefaultColumnWidth());
		
		for (int i = 19; i < 25; i++) {
			RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(1, 99, i, i), sheet);
		}
		
		RegionUtil.setBorderRight(BorderStyle.THICK.getCode(), new CellRangeAddress(1, 99, 25, 25), sheet);
		RegionUtil.setBorderLeft(BorderStyle.THICK.getCode(), new CellRangeAddress(1, 99, 0, 0), sheet);
		RegionUtil.setBorderBottom(BorderStyle.THICK.getCode(), new CellRangeAddress(99, 99, 0, 25), sheet);
		

	}
    private void writeAsrAdultRow(XSSFSheet sheet, AsrAdultRowName rowName,
    		AsrAdultRow asrAdultRow, int rowNum, Font boldFont) {
    	Row row = sheet.createRow(rowNum);
    	int colNum = 0;
    	Cell cell = row.createCell(colNum++);
        
    	CellStyle defaultStyle = sheet.getWorkbook().createCellStyle();
    	defaultStyle.setWrapText(true);
    	defaultStyle.setBorderBottom(BorderStyle.THIN);
    	defaultStyle.setBorderTop(BorderStyle.THIN);
    	defaultStyle.setBorderRight(BorderStyle.THIN);
    	defaultStyle.setBorderLeft(BorderStyle.THIN);
    	
        CellStyle centeredStyle = sheet.getWorkbook().createCellStyle(); 
        centeredStyle.cloneStyleFrom(defaultStyle);
        centeredStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle greyForeGround = sheet.getWorkbook().createCellStyle();
        greyForeGround.cloneStyleFrom(defaultStyle);
        greyForeGround.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        greyForeGround.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle yellowForeGround = sheet.getWorkbook().createCellStyle();
        yellowForeGround.cloneStyleFrom(greyForeGround);
        yellowForeGround.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        
        CellStyle lightYellowForeGround = sheet.getWorkbook().createCellStyle();
        lightYellowForeGround.cloneStyleFrom(greyForeGround);
        lightYellowForeGround.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        
        switch(rowName){
    	case TOTAL: 
            XSSFRichTextString allBoldString = new XSSFRichTextString(rowName.getLabel());
            allBoldString.applyFont(boldFont);
            cell.setCellValue(allBoldString);
            cell.setCellStyle(centeredStyle);
            
    		cell = row.createCell(colNum++);
    		cell.setCellStyle(defaultStyle);
    		cell.setCellValue("");
    		
    		for (int i = 0 ; i < asrAdultRow.getMaleAgeGroups().length; i++) {
        		cell = row.createCell(colNum++);
        		cell.setCellStyle(defaultStyle);
        		cell.setCellValue(asrAdultRow.getMaleAgeGroups()[i]);
    		}
    		
    		for (int i = 0;  i < asrAdultRow.getRaceGroups().length; i++) {
        		cell = row.createCell(colNum++);
        		cell.setCellStyle(defaultStyle);
        		cell.setCellValue(asrAdultRow.getRaceGroups()[i]);
    		}
    		for (int i = 0;  i < asrAdultRow.getEthnicityGroups().length; i++) {
    			cell = row.createCell(colNum++);
        		cell.setCellStyle(defaultStyle);
    			cell.setCellValue(asrAdultRow.getEthnicityGroups()[i]);
    		}
    		break; 
    	default: 
    		cell.setCellValue(rowName.getLabel());
    		cell.setCellStyle(defaultStyle);
    		sheet.addMergedRegionUnsafe(new CellRangeAddress(rowNum,  rowNum+1, colNum-1, colNum-1 ));
    		
    		cell= row.createCell(colNum); 
    		cell.setCellValue("Male");
    		cell.setCellStyle(greyForeGround);
    		
        	Row rowFemale = sheet.createRow(++rowNum);
        	cell=rowFemale.createCell(colNum++);
        	cell.setCellValue("Female");
        	cell.setCellStyle(defaultStyle);

    		if (Arrays.asList(AsrAdultRowName.PROSTITUTION_AND_COMMERCIALIZED_VICE, AsrAdultRowName.SEX_OFFENSES).contains(rowName)){
    			row.setHeightInPoints((float) (1.5*sheet.getDefaultRowHeightInPoints()));
    			rowFemale.setHeightInPoints((float) (1.5*sheet.getDefaultRowHeightInPoints()));
    		}
    		if (Arrays.asList(AsrAdultRowName.DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES, AsrAdultRowName.DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS, 
    				AsrAdultRowName.DRUG_SALE_MANUFACTURING_OTHER, AsrAdultRowName.DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES, 
    				AsrAdultRowName.DRUG_POSSESSION_SYNTHETIC_NARCOTICS, AsrAdultRowName.DRUG_POSSESSION_OTHER).contains(rowName)){
    			row.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
    			rowFemale.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
    		}
        	int ageGroupSize = asrAdultRow.getMaleAgeGroups().length; 
    		for (int i = 0 ; i < ageGroupSize - 1; i++) {
        		Cell maleCell = row.createCell(colNum);
        		maleCell.setCellValue(asrAdultRow.getMaleAgeGroups()[i]);
        		
        		Cell femaleCell = rowFemale.createCell(colNum++);
        		femaleCell.setCellValue(asrAdultRow.getFemaleAgeGroups()[i]);
        		
        		if (Arrays.asList(AsrAdultRowName.PROSTITUTION_AND_COMMERCIALIZED_VICE, AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL, 
        				AsrAdultRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL, AsrAdultRowName.DRUG_POSSESSION_SUBTOTAL, 
        				AsrAdultRowName.GAMBLING_TOTAL).contains(rowName)){
	        		maleCell.setCellStyle(defaultStyle);
	        		femaleCell.setCellStyle(defaultStyle);
        		}
        		else {
	        		maleCell.setCellStyle(yellowForeGround);
	        		femaleCell.setCellStyle(lightYellowForeGround);
        		}
    		}
    		
    		Cell maleCell = row.createCell(colNum);
    		maleCell.setCellValue(asrAdultRow.getMaleAgeGroups()[ageGroupSize -1 ]);
    		maleCell.setCellStyle(greyForeGround);
    		
    		Cell femaleCell = rowFemale.createCell(colNum++);
    		femaleCell.setCellValue(asrAdultRow.getFemaleAgeGroups()[ageGroupSize -1]);
    		femaleCell.setCellStyle(defaultStyle);
    		
    		for (int i = 0;  i < asrAdultRow.getRaceGroups().length - 2; i++) {
    			sheet.addMergedRegionUnsafe(new CellRangeAddress(rowNum-1, rowNum, colNum, colNum));
        		cell = row.createCell(colNum++);
        		cell.setCellStyle(lightYellowForeGround);
        		cell.setCellValue(asrAdultRow.getRaceGroups()[i]);
    		}
    		for (int i = 3;  i < asrAdultRow.getRaceGroups().length; i++) {
    			sheet.addMergedRegionUnsafe(new CellRangeAddress(rowNum-1, rowNum, colNum, colNum));
    			cell = row.createCell(colNum++);
    			cell.setCellStyle(defaultStyle);
    			cell.setCellValue(asrAdultRow.getRaceGroups()[i]);
    		}
    		for (int i = 0;  i < asrAdultRow.getEthnicityGroups().length; i++) {
    			sheet.addMergedRegionUnsafe(new CellRangeAddress(rowNum-1, rowNum, colNum, colNum));
    			cell = row.createCell(colNum++);
        		cell.setCellStyle(defaultStyle);
    			cell.setCellValue(asrAdultRow.getEthnicityGroups()[i]);
    		}
    	}
	}
	private int createAsrAdultHeaderRow(XSSFSheet sheet, int rowNum, Font boldFont,
			XSSFFont normalWeightFont) {
		sheet.addMergedRegion(new CellRangeAddress(1, 6, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(1, 6, 1, 1));
		
        CellStyle normalStyle = sheet.getWorkbook().createCellStyle();
        normalStyle.setWrapText(true);
        normalStyle.setAlignment(HorizontalAlignment.CENTER);
        normalStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        normalStyle.setBorderBottom(BorderStyle.THIN);
        normalStyle.setBorderTop(BorderStyle.THIN);
        normalStyle.setBorderRight(BorderStyle.THIN);
        normalStyle.setBorderLeft(BorderStyle.THIN);

        CellStyle boldStyle = sheet.getWorkbook().createCellStyle();
        boldStyle.cloneStyleFrom(normalStyle);
        boldStyle.setFont(boldFont);


        Row row = sheet.createRow(rowNum++);
        Cell cell = row.createCell(0);
		cell.setCellStyle(boldStyle);
		cell.setCellValue("CLASSIFICATION\nOF OFFENSES");

		
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("SEX");
		cell1.setCellStyle(boldStyle);
		
		sheet.addMergedRegion(new CellRangeAddress(1,1,2,17));
		Cell cell2 = row.createCell(2);
		cell2.setCellStyle(boldStyle);
		cell2.setCellValue("AGE");
		
		sheet.addMergedRegion(new CellRangeAddress(1,6,18,18));
		Cell cell3 = row.createCell(18);
		cell3.setCellStyle(normalStyle);
		cell3.setCellValue("TOTAL");
		
		sheet.addMergedRegion(new CellRangeAddress(1,1,19,23));
		Cell cell4 = row.createCell(4);
		cell4.setCellStyle(boldStyle);
		cell4.setCellValue("RACE");
		
		sheet.addMergedRegion(new CellRangeAddress(1,1,24,25));
		Cell cell5 = row.createCell(5);
		cell5.setCellStyle(boldStyle);
		cell5.setCellValue("ETHNICITY");
		
		row = sheet.createRow(rowNum++);
		List<String> ageTitles = Arrays.asList("18", "19", "20", "21", "22", "23", "24", 
				"25-29", "30-34", "35-39", "40-44", "45-49", "50-54", "55-59", "60-64", "65\nand\nover"    ); 
		for (int i=2, j=0;  j <ageTitles.size(); i++, j++) {
			sheet.addMergedRegion(new CellRangeAddress(2,6,i,i));
			cell2 = row.createCell(i);
			cell2.setCellStyle(normalStyle);
			cell2.setCellValue(ageTitles.get(j));
		}
		
		List<String> raceAndEthnicityTitles = Arrays.asList("White", "Black", "American\nIndian or\nAlaskan\nNative", 
				"Asian", "Native\nHawaiian\nor Other\nPacific\nIslander", "Hispanic\nor Latino", "Not\nHispanic\nor Latino"); 
		for (int i=19, j=0; j < raceAndEthnicityTitles.size(); i++, j++) {
			sheet.addMergedRegion(new CellRangeAddress(2,6,i,i));
			cell2 = row.createCell(i);
			cell2.setCellStyle(normalStyle);
			cell2.setCellValue(raceAndEthnicityTitles.get(j));
		}
		
		RegionUtil.setBorderTop(BorderStyle.THICK.getCode(), new CellRangeAddress(1, 1, 0, 25), sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN.getCode(), new CellRangeAddress(1, 6, 1, 1), sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(1, 6, 1, 1), sheet);
		for (int i = 2; i < 17; i++) {
			RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(1, 6, i, i), sheet);
		}
		RegionUtil.setBorderLeft(BorderStyle.THIN.getCode(), new CellRangeAddress(0, 6, 18, 18), sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN.getCode(), new CellRangeAddress(0, 6, 18, 18), sheet);
		
	
		return rowNum;
	}
    
	private int createAsrAdultTitleRow(XSSFSheet sheet, int rowNum, CellStyle cs, Font boldFont, XSSFFont normalWeightFont) {
		Row row = sheet.createRow(rowNum++);
    	row.setHeightInPoints((3*sheet.getDefaultRowHeightInPoints()));
    	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 25));
		Cell cell = row.createCell(0);
		
        CellStyle centered = sheet.getWorkbook().createCellStyle();
        centered.setWrapText(true);
        centered.setAlignment(HorizontalAlignment.CENTER);
        centered.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(centered);
		 
		XSSFRichTextString s1 = new XSSFRichTextString("AGE, SEX, RACE, AND ETHNICITIES OF PERSONS ARRESTED, 18 years of age and over");
		s1.applyFont(boldFont);
		s1.append("\n(Include those released without having been formally charged)");
		cell.setCellValue(s1);
		

		return rowNum;
	}
    
	
}