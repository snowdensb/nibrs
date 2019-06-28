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
package org.search.nibrs.admin.incident;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.admin.AppProperties;
import org.search.nibrs.admin.services.rest.RestService;
import org.search.nibrs.stagingdata.model.search.IncidentSearchRequest;
import org.search.nibrs.stagingdata.model.search.IncidentSearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"incidentSearchRequest", "agencyMapping", "offenseCodeMapping"})
@RequestMapping("/incidents")
public class IncidentController {
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
		if (!model.containsAttribute("offenseCodeMapping")) {
			model.addAttribute("offenseCodeMapping", restService.getOffenseCodes());
		}
		
    	log.info("Added ModelAtrributes");
    	log.info("Model: " + model);
		
    }
    
	@GetMapping("/searchForm")
	public String getSearchForm(Map<String, Object> model) throws IOException {
		IncidentSearchRequest incidentSearchRequest = (IncidentSearchRequest) model.get("incidentSearchRequest");
		
		if (incidentSearchRequest == null) {
			incidentSearchRequest = new IncidentSearchRequest();
		}
		
		model.put("incidentSearchRequest", incidentSearchRequest);
	    return "/incident/incidents::resultsPage";
	}
	
	@PostMapping("/search")
	public String advancedSearch(HttpServletRequest request, @Valid @ModelAttribute IncidentSearchRequest incidentSearchRequest, BindingResult bindingResult, 
			Map<String, Object> model) throws Throwable {
		
		log.info("incidentSearchRequest:" + incidentSearchRequest );
		if (bindingResult.hasErrors()) {
			log.info("has binding errors");
			log.info(bindingResult.getAllErrors());
			return "/incident/incidents::resultsPage";
		}
		getIncidentSearchResults(request, incidentSearchRequest, model);
		return "/incident/incidents::resultsPage";
	}

	private void getIncidentSearchResults(HttpServletRequest request, IncidentSearchRequest incidentSearchRequest,
			Map<String, Object> model) throws Throwable {
		List<IncidentSearchResult> incidentSearchResults = restService.getIncidents(incidentSearchRequest);
		model.put("incidentSearchResults", incidentSearchResults); 
	}	

	
}

