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
import java.util.List;

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
	
//	public Map<String, String> getMuniDispositionCodeMap(){
//		return getCodeDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-disposition-codes");
//	}
//
//	public Map<String, String> getMuniFiledChargeCodeMap(){
//		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-filed-charge-codes");
//	}
//	
//	public Map<String, String> getMuniAmendedChargeCodeMap(){
//		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-amended-charge-codes");
//	}
//	
//	public Map<String, String> getMuniAlternateSentenceMap(){
//		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-alternate-sentences");
//	}
//	
//	public Map<String, String> getMuniReasonsForDismissalMap(){
//		return getIdDescriptionMap(this::getCodeTableEntries, "/criminalhistory/municipal-reasons-for-dismissal");
//	}
//	
//	
//	public Map<String, String> getIdDescriptionMap(Function<String, List<CodeTableEntry>> function, String uri){
//		return function.apply(uri)
//				.stream()
//				.filter(i->!"no description".equalsIgnoreCase(StringUtils.lowerCase(i.getDescription().trim())))
//				.collect(Collectors.toMap(CodeTableEntry::getId, CodeTableEntry::getDescription, 
//					(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
//	}
//	
//	public Map<String, String> getCodeDescriptionMap(Function<String, List<CodeTableEntry>> function, String uri){
//		return function.apply(uri)
//				.stream()
//				.collect(Collectors.toMap(CodeTableEntry::getCode, CodeTableEntry::getDescription, 
//						(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
//	}
//	
//	public Map<String, String> getIdCitationDescriptionMap(Function<String, List<CodeTableEntry>> function, String uri){
//		return function.apply(uri)
//				.stream()
//				.collect(Collectors.toMap(CodeTableEntry::getId, CodeTableEntry::getCitationDescription, 
//						(oldValue, newValue) -> oldValue, LinkedHashMap::new)); 
//	}
//	
//	public List<CodeTableEntry> getCodeTableEntries(String uri) {
//		return this.webClient.get().uri(uri)
//				.retrieve()
//				.bodyToMono( new ParameterizedTypeReference<List<CodeTableEntry>>() {})
//				.defaultIfEmpty(new ArrayList<CodeTableEntry>())
//				.block();
//	}
//	
	public List<IncidentSearchResult> getIncidents(IncidentSearchRequest incidentSearchRequest){
		return this.webClient.post().uri("/reports/search")
				.body(BodyInserters.fromObject(incidentSearchRequest))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<IncidentSearchResult>>() {})
				.defaultIfEmpty(new ArrayList<IncidentSearchResult>())
				.block();
	}
	
}