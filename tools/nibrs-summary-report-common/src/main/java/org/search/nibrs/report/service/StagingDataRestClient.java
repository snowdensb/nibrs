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
package org.search.nibrs.report.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.buf.StringUtils;
import org.search.nibrs.model.reports.ReturnAForm;
import org.search.nibrs.model.reports.SummaryReportRequest;
import org.search.nibrs.model.reports.arson.ArsonReport;
import org.search.nibrs.model.reports.asr.AsrReports;
import org.search.nibrs.model.reports.cargotheft.CargoTheftReport;
import org.search.nibrs.model.reports.humantrafficking.HumanTraffickingForm;
import org.search.nibrs.model.reports.supplementaryhomicide.SupplementaryHomicideReport;
import org.search.nibrs.report.SummaryReportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StagingDataRestClient {

	private static final Log log = LogFactory.getLog(StagingDataRestClient.class);

	private RestTemplate restTemplate;
	@Autowired
	private SummaryReportProperties appProperties;

	public StagingDataRestClient() {
		super();
		restTemplate = new RestTemplate(); 
		restTemplate.setMessageConverters(getMessageConverters());
	}
	
	public ReturnAForm getReturnAFormByRequest(SummaryReportRequest summaryReportRequest) {
		
		HttpEntity<SummaryReportRequest> request = new HttpEntity<>(summaryReportRequest);
		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"returnAForm"); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the ReturnAForm from the url " + url);
		
		ReturnAForm returnAForm = restTemplate.postForObject( url, request, ReturnAForm.class);
		log.debug("returnAForm: " + returnAForm);
		return returnAForm;
	}
	
	public AsrReports getAsrReportsByRequest(SummaryReportRequest summaryReportRequest) {
		HttpEntity<SummaryReportRequest> request = new HttpEntity<>(summaryReportRequest);

		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"asrReports"); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the ASR Report object from the url " + url);
		
		AsrReports asrReports = restTemplate.postForObject( url, request, AsrReports.class);
		log.info("asrReports: " + asrReports);
		return asrReports;
	}

	public ArsonReport getArsonReportByRequest(SummaryReportRequest summaryReportRequest) {
		
		HttpEntity<SummaryReportRequest> request = new HttpEntity<>(summaryReportRequest);
		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"arsonReport"); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the Arson Report object from the url " + url);
		
		ArsonReport arsonReport = restTemplate.postForObject( url, request, ArsonReport.class);
		log.info("arsonReport: " + arsonReport);
		return arsonReport;
	}
	
	public HumanTraffickingForm getHumanTraffickingFormByRequest(SummaryReportRequest summaryReportRequest) {
		HttpEntity<SummaryReportRequest> request = new HttpEntity<>(summaryReportRequest);
		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"humanTraffickingReport"); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the humanTraffickingForm object from the url " + url);
		
		HumanTraffickingForm humanTraffickingForm = restTemplate.postForObject( url, request, HumanTraffickingForm.class);
		log.info("humanTraffickingForm: " + humanTraffickingForm);
		return humanTraffickingForm;
	}

	public SupplementaryHomicideReport getSupplementaryHomicideReportByRequest(
			SummaryReportRequest summaryReportRequest) {
		HttpEntity<SummaryReportRequest> request = new HttpEntity<>(summaryReportRequest);
		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"shrReports"); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the SupplementaryHomicideReport object from the url " + url);
		
		SupplementaryHomicideReport supplementaryHomicideReport = 
				restTemplate.postForObject( url, request, SupplementaryHomicideReport.class);
		log.info("supplementaryHomicideReport: " + supplementaryHomicideReport);
		return supplementaryHomicideReport;
	}

	public CargoTheftReport getCargoTheftReportByRequest(SummaryReportRequest summaryReportRequest) {
		HttpEntity<SummaryReportRequest> request = new HttpEntity<>(summaryReportRequest);
		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"cargoTheftReport"); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the CargoTheftReport object from the url " + url);
		
		CargoTheftReport cargoTheftReport = restTemplate.postForObject( url, request, CargoTheftReport.class);
		log.info("cargoTheftReport: " + cargoTheftReport);
		return cargoTheftReport;
	}
	
	private List<HttpMessageConverter<?>> getMessageConverters() {
	    List<HttpMessageConverter<?>> converters = 
	      new ArrayList<HttpMessageConverter<?>>();
	    converters.add(new MappingJackson2HttpMessageConverter());
	    return converters;
	}

}
