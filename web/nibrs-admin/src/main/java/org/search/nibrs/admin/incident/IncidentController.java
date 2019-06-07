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
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.admin.AppProperties;
import org.search.nibrs.stagingdata.model.search.IncidentSearchRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"incidentSearchRequest"})
@RequestMapping("/incidents")
public class IncidentController {
	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(this.getClass());
	@Resource
	AppProperties appProperties;

	@GetMapping("/searchForm")
	public String getSearchForm(Map<String, Object> model) throws IOException {
		IncidentSearchRequest incidentSearchRequest = (IncidentSearchRequest) model.get("incidentSearchRequest");
		
		if (incidentSearchRequest == null) {
			incidentSearchRequest = new IncidentSearchRequest();
		}
		
		model.put("incidentSearchRequest", incidentSearchRequest);
	    return "/incident/incidents::resultsPage";
	}
	
	
}

