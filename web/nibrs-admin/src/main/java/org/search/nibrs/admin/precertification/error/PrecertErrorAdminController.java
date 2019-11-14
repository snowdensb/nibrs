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
package org.search.nibrs.admin.precertification.error;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.admin.AppProperties;
import org.search.nibrs.admin.incident.Data;
import org.search.nibrs.admin.services.rest.RestService;
import org.search.nibrs.stagingdata.model.PreCertificationError;
import org.search.nibrs.stagingdata.model.search.PrecertErrorSearchRequest;
import org.search.nibrs.stagingdata.model.search.SearchResult;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"precertErrorSearchRequest", "agencyMapping", "nibrsErrorCodeMapping", "precertErrorSearchResult"})
@RequestMapping("/precertErrors")
public class PrecertErrorAdminController {
	private final Log log = LogFactory.getLog(this.getClass());
	@Resource
	AppProperties appProperties;

	@Resource
	RestService restService;
	
    @ModelAttribute
    public void addModelAttributes(Model model) {
    	
    	log.info("Add ModelAtrributes");
		
		if (!model.containsAttribute("agencyMapping")) {
			model.addAttribute("agencyMapping", restService.getAgencies());
		}
		if (!model.containsAttribute("nibrsErrorCodeMapping")) {
			model.addAttribute("nibrsErrorCodeMapping", restService.getNibrsErrorCodes());
		}
    	log.info("Added ModelAtrributes");
    	log.info("Model: " + model);
    }
    
	@GetMapping("/searchForm")
	public String getSearchForm(Map<String, Object> model) throws IOException {
		PrecertErrorSearchRequest precertErrorSearchRequest = (PrecertErrorSearchRequest) model.get("precertErrorSearchRequest");
		
		if (precertErrorSearchRequest == null) {
			precertErrorSearchRequest = new PrecertErrorSearchRequest();
		}
		
		model.put("precertErrorSearchRequest", precertErrorSearchRequest);
	    return "/precertErrors/precertErrors::resultsPage";
	}
	
	@GetMapping("/searchForm/reset")
	public String resetSearchForm(Map<String, Object> model) throws IOException {
		PrecertErrorSearchRequest precertErrorSearchRequest = new PrecertErrorSearchRequest();
		model.put("precertErrorSearchRequest", precertErrorSearchRequest);
		return "/precertErrors/precertErrorSearchForm::precertErrorSearchFormContent";
	}
	
	@PostMapping("/search")
	public String advancedSearch(HttpServletRequest request, @Valid @ModelAttribute PrecertErrorSearchRequest 
			precertErrorSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		
		log.info("precertErrorSearchRequest:" + precertErrorSearchRequest );
		if (bindingResult.hasErrors()) {
			log.info("has binding errors");
			log.info(bindingResult.getAllErrors());
		    return "/precertErrors/precertErrors::resultsPage";
		}
		
		getPrecertErrorSearchResults(request, precertErrorSearchRequest, model);
	    return "/precertErrors/precertErrors::resultsPage";
	}

	private void getPrecertErrorSearchResults(HttpServletRequest request, PrecertErrorSearchRequest precertErrorSearchRequest,
			Map<String, Object> model) throws Throwable {
		model.put("precertErrorSearchRequest: ", precertErrorSearchRequest); 
		SearchResult<PreCertificationError> searchResult = restService.getPrecertErrors(precertErrorSearchRequest);
		log.debug("precertErrorSearchResult" + searchResult);
		model.put("precertErrorSearchResult", searchResult); 
	}	

	@RequestMapping(value="/searchResults", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Data<PreCertificationError> getPrecertErrors(
			Map<String, Object> model) throws Throwable {
		@SuppressWarnings("unchecked")
		SearchResult<PreCertificationError> searchResult = (SearchResult<PreCertificationError>) model.get("precertErrorSearchResult");
		log.debug("precertErrorSearchResult:" + searchResult);
		
		return new Data<PreCertificationError>(searchResult.getReturnedHits());
	}	
	
	@GetMapping("/{reportType}/{id}")
	public String getReportDetail(HttpServletRequest request, @PathVariable String id, 
			@PathVariable String reportType, Map<String, Object> model) throws Throwable {
		
		if ("GroupA".equals(reportType)) { 
			AdministrativeSegment administrativeSegment = restService.getAdministrativeSegment(id);
			model.put("administrativeSegment", administrativeSegment);
			log.debug("administrativeSegment: " + administrativeSegment);
			return "incident/groupAIncidentDetail::detail";
		}
		else {
			ArrestReportSegment arrestReportSegment = restService.getArrestReportSegment(id);
			model.put("arrestReportSegment", arrestReportSegment);
			log.debug("ArrestReportSegment: " + arrestReportSegment);
			return "incident/groupBArrestDetail::detail";
		}
	}

}

