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
package org.search.nibrs.admin.services.rest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.search.nibrs.admin.AppProperties;
import org.search.nibrs.stagingdata.model.search.IncidentSearchRequest;
import org.search.nibrs.stagingdata.model.search.IncidentSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Profile({"incident-search"})
public class RestService{
	private final WebClient webClient;

	@Autowired
	public RestService(WebClient.Builder webClientBuilder, AppProperties appProperties) {
		this.webClient = webClientBuilder.baseUrl(appProperties.getRestServiceBaseUrl()).build();
	}
	
	public Map<Integer, String> getAgencies() {
		return this.webClient.get().uri("/codeTables/agencies")
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<LinkedHashMap<Integer, String>>() {})
				.block();
	}
	
	public Map<Integer, String> getOffenseCodes() {
		return this.webClient.get().uri("/codeTables/offenseCodes")
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<LinkedHashMap<Integer, String>>() {})
				.block();
	}
	
	
	public List<IncidentSearchResult> getIncidents(IncidentSearchRequest incidentSearchRequest){
		return this.webClient.post().uri("/reports/search")
				.body(BodyInserters.fromObject(incidentSearchRequest))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<IncidentSearchResult>>() {})
				.defaultIfEmpty(new ArrayList<IncidentSearchResult>())
				.block();
	}
	
}