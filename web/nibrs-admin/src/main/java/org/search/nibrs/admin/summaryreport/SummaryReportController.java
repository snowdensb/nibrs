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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.search.nibrs.admin.services.rest.RestService;
import org.search.nibrs.model.reports.ReturnAForm;
import org.search.nibrs.report.service.ArsonExcelExporter;
import org.search.nibrs.report.service.AsrExcelExporter;
import org.search.nibrs.report.service.HumanTraffickingExporter;
import org.search.nibrs.report.service.ReturnAFormExporter;
import org.search.nibrs.report.service.StagingDataRestClient;
import org.search.nibrs.report.service.SupplementaryHomicideReportExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"summaryReportRequest", "oriMapping"})
public class SummaryReportController {
	private static final Log log = LogFactory.getLog(SummaryReportController.class);

	@Resource
	RestService restService;
	
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
	
    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
    	log.info("Add ModelAtrributes");
		
		if (!model.containsAttribute("oriMapping")) {
			model.addAttribute("oriMapping", restService.getOris());
		}
    	log.debug("Model: " + model);
    }

	@GetMapping("/summaryReports/searchForm")
	public String getSummaryReportSearchForm(Map<String, Object> model) throws IOException{
		SummaryReportRequest summaryReportRequest = (SummaryReportRequest) model.get("summaryReportRequest");
		
		if (summaryReportRequest == null) {
			summaryReportRequest = new SummaryReportRequest();
		}
		
		model.put("summaryReportRequest", summaryReportRequest);
	    return "/summaryReports/searchForm::summaryReportForm";
	}
	
	@GetMapping("/returnAForm/{ori}/{year}/{month}")
	public void getReturnAForm(@PathVariable String ori, @PathVariable String year, @PathVariable String month, 
			HttpServletResponse response) throws IOException{
		downloadReturnAForm(ori, year, month, response);
	}

	private void downloadReturnAForm(String ori, String year, String month, HttpServletResponse response) throws IOException {
		ReturnAForm returnAForm = restClient.getReturnAForm(ori, year, month);
		XSSFWorkbook workbook = returnAFormExporter.createReturnAWorkbook(returnAForm);
		String mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		// set content attributes for the response
		response.setContentType(mimeType);
		
		String filename = "ReturnA-" + returnAForm.getOri() + "-" + returnAForm.getYear() + "-" + StringUtils.leftPad(String.valueOf(returnAForm.getMonth()), 2, '0') + ".xlsx";
//		response.setContentLength();
		
		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", filename);
		response.setHeader(headerKey, headerValue);
		
		// get output stream of the response
		OutputStream outStream = response.getOutputStream();
		workbook.write(outStream);
		outStream.close();
	}
	
	@PostMapping("/summaryReports/returnAForm")
	public void getReturnAFormByRequest(SummaryReportRequest summaryReportRequest, 
			HttpServletResponse response) throws IOException{
		downloadReturnAForm(summaryReportRequest.getOri(), summaryReportRequest.getIncidentYearString(), summaryReportRequest.getIncidentMonthString(), response);
	}
	
	@RequestMapping("/arsonReport/{ori}/{year}/{month}")
	public void getArsonReport(@PathVariable String ori, @PathVariable Integer year, @PathVariable Integer month){
	}
	@RequestMapping("/humanTraffickingReport/{ori}/{year}/{month}")
	public void getHumanTraffickingReport(@PathVariable String ori, @PathVariable Integer year, @PathVariable Integer month){
	}
	
	@RequestMapping("/asrReports/{ori}/{arrestYear}/{arrestMonth}")
	public void getAsrReports(@PathVariable String ori, @PathVariable Integer arrestYear, @PathVariable Integer arrestMonth){
	}
	
	@RequestMapping("/shrReports/{ori}/{arrestYear}/{arrestMonth}")
	public void getSupplementaryHomicideReports(@PathVariable String ori, @PathVariable Integer arrestYear, @PathVariable Integer arrestMonth){
	}
	
}
