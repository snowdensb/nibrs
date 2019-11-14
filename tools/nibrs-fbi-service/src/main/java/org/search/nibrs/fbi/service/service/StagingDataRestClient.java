
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

package org.search.nibrs.fbi.service.service;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.fbi.service.AppProperties;
import org.search.nibrs.stagingdata.model.Submission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StagingDataRestClient {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private RestTemplate restTemplate;
	@Autowired
	private AppProperties appProperties;

	public StagingDataRestClient() {
		super();
		restTemplate = new RestTemplate(); 
		restTemplate.setMessageConverters(getMessageConverters());
	}

	public void persistSubmission(Submission submission) {
		logger.debug("about to persist submission " + submission);
		restTemplate.postForLocation(appProperties.getStagingDataRestServiceBaseUrl() + "submissions", submission);
		logger.info("Called the %s%s to persist the  ", appProperties.getStagingDataRestServiceBaseUrl(),  "submission"); 
	}


	
	private List<HttpMessageConverter<?>> getMessageConverters() {
	    List<HttpMessageConverter<?>> converters = 
	      new ArrayList<HttpMessageConverter<?>>();
	    converters.add(new MappingJackson2HttpMessageConverter());
	    return converters;
	}
}
