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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.stagingdata.AppProperties;
import org.search.nibrs.stagingdata.model.search.IncidentSearchRequest;
import org.search.nibrs.stagingdata.model.search.IncidentSearchResult;
import org.search.nibrs.stagingdata.service.AdministrativeSegmentService;
import org.search.nibrs.stagingdata.service.ArrestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportSearchController {
	private static final Log log = LogFactory.getLog(ReportSearchController.class);

	@Autowired
	public AdministrativeSegmentService administrativeSegmentService; 
	@Autowired
	public ArrestReportService arrestReportService; 

	@Autowired
	public AppProperties appProperties;

	@PostMapping("/search")
	public @ResponseBody List<IncidentSearchResult> search(@RequestBody IncidentSearchRequest incidentSearchRequest){
		log.info("IncidentSearchRequest:" + incidentSearchRequest);
		List<IncidentSearchResult> incidentSearchResults = administrativeSegmentService.findAllByCriteria(incidentSearchRequest);
		List<IncidentSearchResult> arrestIncidentSearchResults = arrestReportService.findAllByCriteria(incidentSearchRequest);
		incidentSearchResults.addAll(arrestIncidentSearchResults); 
		
	    Comparator<IncidentSearchResult> incidentSearchResultComparator
	      = Comparator.comparing(IncidentSearchResult::getReportTimestamp);
	    
	    incidentSearchResults.sort(incidentSearchResultComparator.reversed());
		
		return incidentSearchResults.stream()
				.limit(appProperties.getReportSearchResultsLimit())
				.collect(Collectors.toList());
	}
	
	@GetMapping(value="/incidentSearchRequest")
	public @ResponseBody IncidentSearchRequest getIncidentSearchRequest(){
		IncidentSearchRequest incidentSearchRequest = new IncidentSearchRequest();
		return incidentSearchRequest;
	}

	
}
