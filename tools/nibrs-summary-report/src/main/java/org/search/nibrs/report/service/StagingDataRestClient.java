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
import org.search.nibrs.model.reports.asr.AsrAdult;
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

	public AsrAdult getAsrAdult(String ori, String year, String month) {
		List<String> urlParts = Arrays.asList(appProperties.getStagingDataRestServiceBaseUrl(), 
				"asrAdult", ori, year, month); 
		String url = StringUtils.join(urlParts, '/');
		log.info("Getting the ReturnAForm object from the url " + url);
		
		AsrAdult asrAdult = restTemplate.getForObject( url, AsrAdult.class);
		log.info("asrAdult: " + asrAdult);
		return asrAdult;
	}
	
	private List<HttpMessageConverter<?>> getMessageConverters() {
	    List<HttpMessageConverter<?>> converters = 
	      new ArrayList<HttpMessageConverter<?>>();
	    converters.add(new MappingJackson2HttpMessageConverter());
	    return converters;
	}
}
