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
import org.search.nibrs.model.reports.arson.ArsonReport;
import org.search.nibrs.model.reports.asr.AsrReports;
import org.search.nibrs.model.reports.humantrafficking.HumanTraffickingForm;
import org.search.nibrs.report.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StagingDataRestClient {

	private static final Log log = LogFactory.getLog(StagingDataRestClient.class);

	private RestTemplate restTemplate;
	@Autowired
	private AppProperties appProperties;

	public StagingDataRestClient() {
		super();
		restTemplate = new RestTemplate(); 
		restTemplate.setMessageConverters(getMessageConverters());
	}
	
	public ReturnAForm getReturnAForm(String ori, String year, String month) {
		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"returnAForm/", ori, year, month); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the ReturnAForm object from the url " + url);

		ReturnAForm returnAForm = restTemplate.getForObject( url, ReturnAForm.class);
		log.info("returnAForm: " + returnAForm);
		return returnAForm;
	}

	public AsrReports getAsrReports(String ori, String year, String month) {
		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"asrReports", ori, year, month); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the ASR Report object from the url " + url);
		
		AsrReports asrAdult = restTemplate.getForObject( url, AsrReports.class);
		log.info("asrAdult: " + asrAdult);
		return asrAdult;
	}
	
	public ArsonReport getArsonReport(String ori, String year, String month) {
		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"arsonReport", ori, year, month); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the Arson Report object from the url " + url);
		
		ArsonReport arsonReport = restTemplate.getForObject( url, ArsonReport.class);
		log.info("arsonReport: " + arsonReport);
		return arsonReport;
	}
	
	public HumanTraffickingForm getHumanTraffickingForm(String ori, String year, String month) {
		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"humanTraffickingReport", ori, year, month); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the humanTraffickingForm object from the url " + url);
		
		HumanTraffickingForm humanTraffickingForm = restTemplate.getForObject( url, HumanTraffickingForm.class);
		log.info("humanTraffickingForm: " + humanTraffickingForm);
		return humanTraffickingForm;
	}
	
	private List<HttpMessageConverter<?>> getMessageConverters() {
	    List<HttpMessageConverter<?>> converters = 
	      new ArrayList<HttpMessageConverter<?>>();
	    converters.add(new MappingJackson2HttpMessageConverter());
	    return converters;
	}
}
