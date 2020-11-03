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
package org.search.nibrs.stagingdata.controller;

import org.search.nibrs.model.reports.ReturnAForm;
import org.search.nibrs.model.reports.ReturnARecordCardReport;
import org.search.nibrs.model.reports.SummaryReportRequest;
import org.search.nibrs.model.reports.arson.ArsonReport;
import org.search.nibrs.model.reports.asr.AsrReports;
import org.search.nibrs.model.reports.cargotheft.CargoTheftReport;
import org.search.nibrs.model.reports.humantrafficking.HumanTraffickingForm;
import org.search.nibrs.model.reports.supplementaryhomicide.SupplementaryHomicideReport;
import org.search.nibrs.stagingdata.service.summary.ArsonFormService;
import org.search.nibrs.stagingdata.service.summary.AsrFormService;
import org.search.nibrs.stagingdata.service.summary.CargoTheftReportService;
import org.search.nibrs.stagingdata.service.summary.HumanTraffickingFormService;
import org.search.nibrs.stagingdata.service.summary.ReturnAFormService;
import org.search.nibrs.stagingdata.service.summary.SupplementaryHomicideReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SummaryFormController {

	@Autowired
	private ReturnAFormService returnAFormService;
	
	@Autowired
	private AsrFormService asrFormService;
	
	@Autowired
	private ArsonFormService arsonFormService;
	
	@Autowired
	private HumanTraffickingFormService humanTraffickingFormService;
	
	@Autowired
	private SupplementaryHomicideReportService supplementaryHomicideReportService;
	
	@Autowired
	private CargoTheftReportService cargoTheftReportService;
	
	@PostMapping("/returnAForm")
	public ReturnAForm getReturnAFormByRequest(@RequestBody SummaryReportRequest summaryReportRequest){
		return returnAFormService.createReturnASummaryReportByRequest(summaryReportRequest);
	}
	
	@PostMapping("/returnARecordCard")
	public ReturnARecordCardReport getReturnARecordCardByRequest(@RequestBody SummaryReportRequest summaryReportRequest){
		return returnAFormService.createReturnARecordCardReportByRequest(summaryReportRequest);
	}
	
	@PostMapping("/arsonReport")
	public ArsonReport getArsonReportByRequest(@RequestBody SummaryReportRequest summaryReportRequest){
		return arsonFormService.createArsonSummaryReportsByRequest(summaryReportRequest);
	}
	
	@PostMapping("/humanTraffickingReport")
	public HumanTraffickingForm getHumanTraffickingReportByRequest(@RequestBody SummaryReportRequest summaryReportRequest){
		return humanTraffickingFormService.createHumanTraffickingReportByRequest(summaryReportRequest);
	}
	
	@RequestMapping("/asrReports")
	public AsrReports getAsrReportsByRequest(@RequestBody SummaryReportRequest summaryReportRequest){
		return asrFormService.createAsrSummaryReportsByRequest(summaryReportRequest);
	}
	
	@PostMapping("/shrReports")
	public SupplementaryHomicideReport getSupplementaryHomicideReports(@RequestBody SummaryReportRequest summaryReportRequest){
		return supplementaryHomicideReportService.createSupplementaryHomicideReportByRequest(summaryReportRequest);
	}
	
	@PostMapping("/cargoTheftReport")
	public CargoTheftReport getCargoTheftReport(@RequestBody SummaryReportRequest summaryReportRequest){
		return cargoTheftReportService.createCargoTheftReportByRequest(summaryReportRequest);
	}
	
}
