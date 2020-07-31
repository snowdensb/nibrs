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
package org.search.nibrs.admin.summaryreport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.search.nibrs.admin.AppProperties;
import org.search.nibrs.admin.security.AuthUser;
import org.search.nibrs.admin.services.rest.RestService;
import org.search.nibrs.model.reports.ReturnAForm;
import org.search.nibrs.model.reports.SummaryReportRequest;
import org.search.nibrs.model.reports.arson.ArsonReport;
import org.search.nibrs.model.reports.asr.AsrReports;
import org.search.nibrs.model.reports.cargotheft.CargoTheftReport;
import org.search.nibrs.model.reports.humantrafficking.HumanTraffickingForm;
import org.search.nibrs.model.reports.supplementaryhomicide.SupplementaryHomicideReport;
import org.search.nibrs.report.service.ArsonExcelExporter;
import org.search.nibrs.report.service.AsrExcelExporter;
import org.search.nibrs.report.service.CargoTheftReportExporter;
import org.search.nibrs.report.service.HumanTraffickingExporter;
import org.search.nibrs.report.service.ReturnAFormExporter;
import org.search.nibrs.report.service.StagingDataRestClient;
import org.search.nibrs.report.service.SupplementaryHomicideReportExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"summaryReportRequest", "oriMapping", "authUser", "ownerId", "agencyMapping", "stateCodeMappingByOwner"})
public class SummaryReportController {
	private static final Log log = LogFactory.getLog(SummaryReportController.class);

	@Resource
	RestService restService;
	@Resource
    AppProperties appProperties;
	@Autowired 
	public StagingDataRestClient restClient; 
	@Autowired 
	public ReturnAFormExporter returnAFormExporter;
	@Autowired 
	public AsrExcelExporter asrExcelExporter;
	@Autowired 
	public ArsonExcelExporter arsonExcelExporter;
	@Autowired 
	public HumanTraffickingExporter humanTraffickingExporter;
	@Autowired 
	public SupplementaryHomicideReportExporter supplementaryHomicideReportExporter;
	@Autowired 
	public CargoTheftReportExporter cargoTheftReportExporter;
	
    @ModelAttribute
    public void addModelAttributes(Map<String, Object> model) {
    	
    	log.info("Add ModelAtrributes");
		
		if (appProperties.getPrivateSummaryReportSite()) {
			model.put("oriMapping", restService.getOris(StringUtils.EMPTY));
			model.put("agencyMapping", restService.getAgencies(null));
			model.put("stateCodeMappingByOwner", restService.getStatesNoChache(null));
		}
		else {
			AuthUser authUser =(AuthUser) model.get("authUser");  
			String ownerId = Objects.toString(authUser.getUserId(), "");
			model.put("oriMapping", restService.getOris(ownerId));
			model.put("agencyMapping", restService.getAgencies(authUser.getUserId()));
			model.put("stateCodeMappingByOwner", restService.getStatesNoChache(ownerId));
		}
    	log.debug("Model: " + model);
    }

	@GetMapping("/summaryReports/searchForm")
	public String getSummaryReportSearchForm(Map<String, Object> model) throws IOException{
		SummaryReportRequest summaryReportRequest = (SummaryReportRequest) model.get("summaryReportRequest");
		
		if (summaryReportRequest == null) {
			summaryReportRequest = new SummaryReportRequest();
			
			if (!appProperties.getPrivateSummaryReportSite()) {
				Integer ownerId = (Integer) model.get("ownerId");
				summaryReportRequest.setOwnerId(ownerId);
			}
		}
		
		model.put("summaryReportRequest", summaryReportRequest);
	    return "/summaryReports/searchForm::summaryReportForm";
	}
	
	@GetMapping("/summaryReports/searchForm/reset")
	public String resetSearchForm(Map<String, Object> model) throws IOException {
		SummaryReportRequest summaryReportRequest = new SummaryReportRequest();;
		model.put("summaryReportRequest", summaryReportRequest);
	    return "/summaryReports/searchForm::summaryReportForm";
	}
	
	private void downloadReport(HttpServletResponse response, XSSFWorkbook workbook, String fileName) throws IOException {
		String mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		// set content attributes for the response
		response.setContentType(mimeType);
		
//		response.setContentLength();
		
		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", fileName);
		response.setHeader(headerKey, headerValue);
		
		// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(8192)) { 
            try { 
                workbook.write(baos); 
            } finally { 
                workbook.close(); 
            }
            
            baos.writeTo(outStream);
            baos.close(); 
        } finally{
        	outStream.close();
        }
		log.info("The report is writen to fileName: " + fileName);
	}
	
	@PostMapping("/summaryReports/returnAForm")
	public void getReturnAFormByRequest(@ModelAttribute SummaryReportRequest summaryReportRequest,
			HttpServletResponse response, Map<String, Object> model) throws IOException{
		ReturnAForm returnAForm = restClient.getReturnAFormByRequest(summaryReportRequest);
		XSSFWorkbook workbook = returnAFormExporter.createReturnAWorkbook(returnAForm);
		String fileName = getFileName("ReturnA", returnAForm.getStateName(), returnAForm.getOri(), returnAForm.getYear(), returnAForm.getMonth());

		downloadReport(response, workbook, fileName);
	}

	private String getFileName(String reportType, String stateName,  String ori, Integer year, Integer month) {

		List<String> nameParts = new ArrayList<>();
		nameParts.add(reportType);
		
		if (StringUtils.isNotBlank(ori)){
			nameParts.add(ori); 
		}
		else {
			nameParts.add(String.valueOf(stateName));
		}
		nameParts.add(String.valueOf(year)); 
		if ( month!=null && month!=0) {
			nameParts.add(StringUtils.leftPad(String.valueOf(month), 2, '0')); 
		}
		String fileName = StringUtils.join(nameParts, "-") + ".xlsx";
		return fileName;
	}
	
	@PostMapping("/summaryReports/returnASupplement")
	public void getReturnASupplementByRequest(@ModelAttribute SummaryReportRequest summaryReportRequest,
			HttpServletResponse response, Map<String, Object> model) throws IOException{
		ReturnAForm returnAForm = restClient.getReturnAFormByRequest(summaryReportRequest);
		XSSFWorkbook workbook = returnAFormExporter.createReturnASupplementWorkBook(returnAForm);
		String fileName = getFileName("ReturnASupplement", returnAForm.getStateName(), returnAForm.getOri(), returnAForm.getYear(), returnAForm.getMonth()); 
		
		downloadReport(response, workbook, fileName);
	}
	
	@PostMapping("/summaryReports/arsonReport")
	public void getArsonReportByRequest(@ModelAttribute SummaryReportRequest summaryReportRequest,
			HttpServletResponse response, Map<String, Object> model) throws IOException{
		log.info("get arson report");
		ArsonReport arsonReport = restClient.getArsonReportByRequest(summaryReportRequest);
		XSSFWorkbook workbook = arsonExcelExporter.createWorkBook(arsonReport);
		String fileName = getFileName("ARSON-Report", arsonReport.getStateName(), arsonReport.getOri(), arsonReport.getYear(), arsonReport.getMonth());
		
		downloadReport(response, workbook, fileName);
	}
	
	@PostMapping("/summaryReports/humanTraffickingReport")
	public void getHumanTraffickingReportByRequest(@ModelAttribute SummaryReportRequest summaryReportRequest,
			HttpServletResponse response, Map<String, Object> model) throws IOException{
		log.info("get humanTraffickingReport");
		HumanTraffickingForm humanTraffickingForm = 
				restClient.getHumanTraffickingFormByRequest(summaryReportRequest);
		XSSFWorkbook workbook = humanTraffickingExporter.createWorkbook(humanTraffickingForm);
		String fileName = getFileName("HumanTrafficking", humanTraffickingForm.getStateName(), humanTraffickingForm.getOri(), humanTraffickingForm.getYear(), humanTraffickingForm.getMonth());
		downloadReport(response, workbook, fileName);
	}
	
	@PostMapping("/summaryReports/asrReports")
	public void getAsrReportsByRequest(@ModelAttribute SummaryReportRequest summaryReportRequest,
			HttpServletResponse response, Map<String, Object> model) throws IOException{
		log.info("get asrReports");
		AsrReports asrReports = restClient.getAsrReportsByRequest(summaryReportRequest);
		XSSFWorkbook workbook = asrExcelExporter.createWorkbook(asrReports);
		String fileName = getFileName("ASR-REPORTS", asrReports.getStateName(), asrReports.getOri(), asrReports.getYear(), asrReports.getMonth());
		downloadReport(response, workbook, fileName);
	}
	
	@PostMapping("/summaryReports/shrReports")
	public void getSupplementaryHomicideReportsByRequest(@ModelAttribute SummaryReportRequest summaryReportRequest,
			HttpServletResponse response, Map<String, Object> model) throws IOException{
		log.info("get shrReports");
		SupplementaryHomicideReport supplementaryHomicideReport = restClient.getSupplementaryHomicideReportByRequest(summaryReportRequest);
		XSSFWorkbook workbook = supplementaryHomicideReportExporter.createWorkbook(supplementaryHomicideReport);
		String fileName = getFileName("SupplementaryHomicideReport", supplementaryHomicideReport.getStateName(), supplementaryHomicideReport.getOri(), 
				supplementaryHomicideReport.getYear(), supplementaryHomicideReport.getMonth());
		downloadReport(response, workbook, fileName);
	}
	
	@PostMapping("/summaryReports/cargoTheftReport")
	public void getCargoTheftReportByRequest(@ModelAttribute SummaryReportRequest summaryReportRequest,
			HttpServletResponse response, Map<String, Object> model) throws IOException{
		log.info("get cargo theft report");
        CargoTheftReport cargoTheftReport = restClient.getCargoTheftReportByRequest(summaryReportRequest);
		XSSFWorkbook workbook = cargoTheftReportExporter.createWorkbook(cargoTheftReport);
		String fileName = getFileName("CargoTheftReport", cargoTheftReport.getStateName(), cargoTheftReport.getOri(), 
				cargoTheftReport.getYear(), cargoTheftReport.getMonth());
		downloadReport(response, workbook, fileName);
	}
	
	@GetMapping("/years/{ori}")
	public @ResponseBody List<Integer> getDistinctYears(@PathVariable String ori, Map<String, Object> model) throws IOException{
		String ownerId = getOwnerId(model);
		return restService.getYears(ori, ownerId);
	}
	
	@GetMapping("/state/years/{stateCode}")
	public @ResponseBody List<Integer> getDistinctYearsByStateCode(@PathVariable String stateCode, Map<String, Object> model) throws IOException{
		log.info("stateCode:" + stateCode); 
		String ownerId = getOwnerId(model);
		log.info("ownerId:" + ownerId); 
		List<Integer> years = restService.getYearsByStateCode(stateCode, ownerId);
		log.info("years:" + years);
		return years;
	}

	private String getOwnerId(Map<String, Object> model) {
		String ownerId = "";
		if (!appProperties.getPrivateSummaryReportSite()) {
			AuthUser authUser =(AuthUser) model.get("authUser");  
			ownerId = Objects.toString(authUser.getUserId());
		}
		
		return ownerId;
	}
	
	@GetMapping("/months/{year}/{ori}")
	public @ResponseBody List<Integer> getDistinctMonths(@PathVariable String ori, @PathVariable Integer year, Map<String, Object> model) throws IOException{
		String ownerId = getOwnerId(model);
		return restService.getMonths(ori, year, ownerId);
	}
	
	@GetMapping("state/months/{year}/{stateCode}")
	public @ResponseBody List<Integer> getDistinctMonthsByStateCode(@PathVariable String stateCode, @PathVariable Integer year, Map<String, Object> model) throws IOException{
		String ownerId = getOwnerId(model);
		return restService.getMonthsByStateCode(stateCode, year, ownerId);
	}
	
}
