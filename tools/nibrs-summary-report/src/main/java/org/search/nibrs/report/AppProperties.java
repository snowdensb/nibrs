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
package org.search.nibrs.report;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
public class AppProperties {

	private String stagingDataRestServiceBaseUrl = "http://localhost:9080";
	private String returnAFormOutputPath = ".";
	
	public String getStagingDataRestServiceBaseUrl() {
		return stagingDataRestServiceBaseUrl;
	}

	public void setStagingDataRestServiceBaseUrl(String stagingDataRestServiceBaseUrl) {
		this.stagingDataRestServiceBaseUrl = stagingDataRestServiceBaseUrl;
	}

	public String getReturnAFormOutputPath() {
		return returnAFormOutputPath;
	}

	public void setReturnAFormOutputPath(String returnAFormOutputPath) {
		this.returnAFormOutputPath = returnAFormOutputPath;
	}

	@Override
	public String toString() {
		return "AppProperties [stagingDataRestServiceBaseUrl=" + stagingDataRestServiceBaseUrl
				+ ", returnAFormOutputPath=" + returnAFormOutputPath + "]";
	}

}