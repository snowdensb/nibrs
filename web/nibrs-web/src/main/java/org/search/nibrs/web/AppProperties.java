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
package org.search.nibrs.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pct")
public class AppProperties {

	private Boolean allowQueriesWithoutSAMLToken = true; 
	private Boolean demoLawEnforcementEmployerIndicator = true;
	private Integer ajpPort = 9090; 
	private Boolean ajpEnabled = true; 
	
	public AppProperties() {
		super();
	}

	public Boolean getAllowQueriesWithoutSAMLToken() {
		return allowQueriesWithoutSAMLToken;
	}

	public void setAllowQueriesWithoutSAMLToken(Boolean allowQueriesWithoutSAMLToken) {
		this.allowQueriesWithoutSAMLToken = allowQueriesWithoutSAMLToken;
	}

	public Boolean getAjpEnabled() {
		return ajpEnabled;
	}

	public void setAjpEnabled(Boolean ajpEnabled) {
		this.ajpEnabled = ajpEnabled;
	}

	public Integer getAjpPort() {
		return ajpPort;
	}

	public void setAjpPort(Integer ajpPort) {
		this.ajpPort = ajpPort;
	}

	public Boolean getDemoLawEnforcementEmployerIndicator() {
		return demoLawEnforcementEmployerIndicator;
	}

	public void setDemoLawEnforcementEmployerIndicator(Boolean demoLawEnforcementEmployerIndicator) {
		this.demoLawEnforcementEmployerIndicator = demoLawEnforcementEmployerIndicator;
	}

}