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
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@RequestMapping("/returnAForm/{ori}/{year}/{month}")
	public ReturnAForm getReturnAForm(@PathVariable String ori, @PathVariable Integer year, @PathVariable Integer month){
		return returnAFormService.createReturnASummaryReport(ori, year, month);
	}
	
	@RequestMapping("/arsonReport/{ori}/{year}/{month}")
	public ArsonReport getArsonReport(@PathVariable String ori, @PathVariable Integer year, @PathVariable Integer month){
		return arsonFormService.createArsonSummaryReports(ori, year, month);
	}
	@RequestMapping("/humanTraffickingReport/{ori}/{year}/{month}")
	public HumanTraffickingForm getHumanTraffickingReport(@PathVariable String ori, @PathVariable Integer year, @PathVariable Integer month){
		return humanTraffickingFormService.createHumanTraffickingReport(ori, year, month);
	}
	
	@RequestMapping("/asrReports/{ori}/{arrestYear}/{arrestMonth}")
	public AsrReports getAsrReports(@PathVariable String ori, @PathVariable Integer arrestYear, @PathVariable Integer arrestMonth){
		return asrFormService.createAsrSummaryReports(ori, arrestYear, arrestMonth);
	}
	
	@RequestMapping("/shrReports/{ori}/{arrestYear}/{arrestMonth}")
	public SupplementaryHomicideReport getSupplementaryHomicideReports(@PathVariable String ori, @PathVariable Integer arrestYear, @PathVariable Integer arrestMonth){
		return supplementaryHomicideReportService.createSupplementaryHomicideReport(ori, arrestYear, arrestMonth);
	}
	
	@RequestMapping("/cargoTheftReport/{ori}/{incidentYear}/{incidentMonth}")
	public CargoTheftReport getCargoTheftReport(@PathVariable String ori, @PathVariable Integer incidentYear, @PathVariable Integer incidentMonth){
		return cargoTheftReportService.createCargoTheftReport(ori, incidentYear, incidentMonth);
	}
	
}
