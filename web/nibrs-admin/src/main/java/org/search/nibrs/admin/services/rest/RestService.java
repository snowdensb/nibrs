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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.admin.AppProperties;
import org.search.nibrs.admin.security.AuthUser;
import org.search.nibrs.admin.uploadfile.PersistReportTask;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.stagingdata.model.Owner;
import org.search.nibrs.stagingdata.model.PreCertificationError;
import org.search.nibrs.stagingdata.model.SubmissionTrigger;
import org.search.nibrs.stagingdata.model.search.IncidentSearchRequest;
import org.search.nibrs.stagingdata.model.search.IncidentSearchResult;
import org.search.nibrs.stagingdata.model.search.PrecertErrorSearchRequest;
import org.search.nibrs.stagingdata.model.search.SearchResult;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Profile({"incident-search"})
public class RestService{
	private final Log log = LogFactory.getLog(this.getClass());

	private final WebClient webClient;

	@Autowired
	public RestService(WebClient.Builder webClientBuilder, AppProperties appProperties) {
		this.webClient = webClientBuilder.baseUrl(appProperties.getRestServiceBaseUrl()).build();
	}
	
	public Map<Integer, String> getAgencies(String ownerId) {
		return this.webClient.get().uri("/codeTables/agencies/"+ownerId)
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<LinkedHashMap<Integer, String>>() {})
				.block();
	}
	
	public Map<String, String> getOris(String ownerId) {
		return this.webClient.get().uri("/codeTables/agenciesHavingData/" + ownerId)
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<LinkedHashMap<String, String>>() {})
				.block();
	}
	
	public List<Integer> getYears(String ori, String ownerId) {
		return this.webClient.get().uri("/codeTables/years/"+ ownerId + "/" + ori)
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<List<Integer>>() {})
				.block();
	}
	
	public List<Integer> getMonths(String ori, Integer year, String ownerId) {
		return this.webClient.get().uri("/codeTables/months/" + year + "/" + ownerId + "/"+ ori)
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<List<Integer>>() {})
				.block();
	}
	
	public Map<Integer, String> getOffenseCodes() {
		return this.webClient.get().uri("/codeTables/offenseCodes")
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<LinkedHashMap<Integer, String>>() {})
				.block();
	}
	
	public Map<Integer, String> getNibrsErrorCodes() {
		return this.webClient.get().uri("/codeTables/nibrsErrorCodes")
				.retrieve()
				.bodyToMono( new ParameterizedTypeReference<LinkedHashMap<Integer, String>>() {})
				.block();
	}
	
	public IncidentSearchResult getIncidents(IncidentSearchRequest incidentSearchRequest){
		return this.webClient.post().uri("/reports/search")
				.body(BodyInserters.fromObject(incidentSearchRequest))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<IncidentSearchResult>() {})
				.block();
	}
	
	public Owner getSavedUser(Owner webUser){
		return this.webClient.post().uri("/codeTables/user")
				.body(BodyInserters.fromObject(webUser))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Owner>() {})
				.block();
	}
	
	public AdministrativeSegment getAdministrativeSegment(String id){
		return this.webClient.get().uri("/reports/A/" + id)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<AdministrativeSegment>() {})
				.block();
	}
	
	public ArrestReportSegment getArrestReportSegment(String id){
		return this.webClient.get().uri("/reports/B/" + id)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<ArrestReportSegment>() {})
				.block();
	}
	
	public void persistAbstractReport(AbstractReport abstractReport) {
		if (abstractReport instanceof GroupAIncidentReport){
			GroupAIncidentReport groupAIncidentReport = (GroupAIncidentReport) abstractReport; 
			log.info("About to post for group A incident report " + groupAIncidentReport.getIncidentNumber());
			log.info("Action category " + groupAIncidentReport.getReportActionType());
			webClient.post().uri("/groupAIncidentReports")
				.body(BodyInserters.fromObject(groupAIncidentReport))
				.retrieve()
				.bodyToMono(String.class)
				.block();
		}
		else if (abstractReport instanceof GroupBArrestReport){
			GroupBArrestReport groupBArrestReport = (GroupBArrestReport) abstractReport; 
			log.info("About to post for group B Arrest Report" + groupBArrestReport.getIdentifier());
			log.info("Action category " + groupBArrestReport.getReportActionType());
			webClient.post().uri("/arrestReports")
				.body(BodyInserters.fromObject(groupBArrestReport))
				.retrieve()
				.bodyToMono(String.class)
				.block();
		}
		else {
			log.warn("The report type " +  abstractReport.getClass().getName() + "is not supported");
		}
		
	}
	
	public String generateSubmissionFiles(IncidentSearchRequest incidentSearchRequest) {
		SubmissionTrigger submissionTrigger = new SubmissionTrigger(incidentSearchRequest);
		log.info("submissionTrigger: " + submissionTrigger);
		log.info("submissionIncidentSearchRequest: " + incidentSearchRequest);
		
		String response = ""; 
		try { 
			response = webClient.post().uri("/submissions/trigger")
				.body(BodyInserters.fromObject(submissionTrigger))
				.retrieve()
				.bodyToMono(String.class)
				.block();
		}
		catch(Throwable e) {
			log.error("Got error when calling the service /submissions/trigger", e);
			response = "Failed to process the request, please report the error or check back later."; 
		}
		
		return response; 
	}
	
	@Async
	public void persistValidReportsAsync(PersistReportTask persistReportTask, List<AbstractReport> validReports, AuthUser authUser) {
		log.info("Execute method asynchronously. "
			      + Thread.currentThread().getName());
		int count = 0; 
		persistReportTask.setStarted(true);
		for(AbstractReport abstractReport: validReports){
			if (authUser != null) {
				abstractReport.setOwnerId(authUser.getUserId());
			}
			try{
				this.persistAbstractReport(abstractReport);
				persistReportTask.increaseProcessedCount();
				log.info("Progress: " + (++count) + "/" + persistReportTask.getTotalCount());
			}
			catch(ResourceAccessException rae){
				log.error("Failed to connect to the rest service to process the " + abstractReport.getAdminSegmentLevel() + 
						"level report with Identifier " + abstractReport.getIdentifier());
				persistReportTask.setAborted(true);
				throw rae;
			}
			catch(Exception e){
				persistReportTask.increaseProcessedCount();
				persistReportTask.addFailedToProcess(abstractReport.getUniqueReportDescription());
				log.warn("Failed to persist incident " + abstractReport.getIdentifier());
				log.error(e);
				log.info("Progress: " + (++count) + "/" + persistReportTask.getTotalCount());
			}
		}
		
	}
	
	public String persistPreCertificationErrors(List<NIBRSError> nibrsErrors, AuthUser authUser) {
		log.info("Execute method asynchronously. "
				+ Thread.currentThread().getName());
		
		List<PreCertificationError> preCertificationErrors = nibrsErrors.stream()
				.map(nibrsError -> new PreCertificationError(nibrsError))
				.collect(Collectors.toList());
		
		if (authUser != null) {
			preCertificationErrors.forEach(error -> error.setOwnerId(authUser.getUserId()));
		}
		
		Integer savedCount = webClient.post().uri("/preCertificationErrors")
			.body(BodyInserters.fromObject(preCertificationErrors))
			.retrieve()
			.bodyToMono(Integer.class)
			.block();
		
		return savedCount + " errors are persisted."; 
		
	}

	public SearchResult<PreCertificationError> getPrecertErrors(PrecertErrorSearchRequest precertErrorSearchRequest) {
		return this.webClient.post().uri("/preCertificationErrors/search")
				.body(BodyInserters.fromObject(precertErrorSearchRequest))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<SearchResult<PreCertificationError>>() {})
				.block();
	}
	
	
}