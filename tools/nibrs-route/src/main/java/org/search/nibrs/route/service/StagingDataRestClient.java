
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

package org.search.nibrs.route.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.route.AppProperties;
import org.search.nibrs.validate.common.ValidationResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class StagingDataRestClient {

	private final Log log = LogFactory.getLog(this.getClass());

	private RestTemplate restTemplate;
	@Autowired
	private AppProperties appProperties;

	public StagingDataRestClient() {
		super();
		restTemplate = new RestTemplate(); 
		restTemplate.setMessageConverters(getMessageConverters());
	}
	
	public void persistIncidentReports(@Body ValidationResults validationResults, @Header("CamelFileName") String fileName) {
		List<AbstractReport> abstractReports = validationResults.getReportsWithoutErrors(); 
		
		logCountsOfReports(abstractReports);
		int count = 0; 
		for(AbstractReport abstractReport: abstractReports){
			try{
				persistAbstractReport(abstractReport);
				log.info("Progress: " + (++count) + "/" + abstractReports.size());
			}
			catch(ResourceAccessException rae){
				log.error("Failed to connect to the rest service to process the reports in " + fileName);
				throw rae;
			}
			catch(Exception e){
				log.warn("Failed to persist incident " + abstractReport.getIdentifier());
				log.error(e);
				log.info("Progress: " + (++count) + "/" + abstractReports.size());
			}
		}
		log.info("All reports from the file " + fileName + " are procesed.");
	}

	private void logCountsOfReports(List<AbstractReport> abstractReports) {
		Set<String> incidentNumbers = new HashSet<>();
		abstractReports.forEach(item->incidentNumbers.add(item.getIdentifier()));
		log.info("about to process " + abstractReports.size() + " reports with " + incidentNumbers.size() + " distinct identifiers. ");
	}

	public void persistAbstractReport(AbstractReport abstractReport) {
		if (abstractReport instanceof GroupAIncidentReport){
			GroupAIncidentReport groupAIncidentReport = (GroupAIncidentReport) abstractReport; 
			log.info("About to post for group A incident report " + groupAIncidentReport.getIncidentNumber());
			log.info("Action category " + groupAIncidentReport.getReportActionType());
			restTemplate.postForLocation(appProperties.getStagingDataRestServiceBaseUrl() + "groupAIncidentReports", groupAIncidentReport);
		}
		else if (abstractReport instanceof GroupBArrestReport){
			GroupBArrestReport groupBArrestReport = (GroupBArrestReport) abstractReport; 
			log.info("About to post for group B Arrest Report" + groupBArrestReport.getIdentifier());
			log.info("Action category " + groupBArrestReport.getReportActionType());
			restTemplate.postForLocation(appProperties.getStagingDataRestServiceBaseUrl() + "arrestReports", groupBArrestReport);
		}
		else {
			log.warn("The report type " +  abstractReport.getClass().getName() + "is not supported");
		}
		
	}
	
	private List<HttpMessageConverter<?>> getMessageConverters() {
	    List<HttpMessageConverter<?>> converters = 
	      new ArrayList<HttpMessageConverter<?>>();
	    converters.add(new MappingJackson2HttpMessageConverter());
	    return converters;
	}
}
