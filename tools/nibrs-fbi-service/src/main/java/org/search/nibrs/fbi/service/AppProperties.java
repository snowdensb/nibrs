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
package org.search.nibrs.fbi.service;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("nibrs.fbi.services")
public class AppProperties {

    private String nibrsNiemDocumentFolder = "/tmp/nibrs/niemSubmission";
	private String stagingDataRestServiceBaseUrl = "http://localhost:8080/";
	private String nibrsNiemServiceEndpointUrl = "https4://localhost:443/UCR/NOE/NIBRSServices";
	private String truststoreLocation = "";
	private String truststorePassword = "";
	private String fbiKeyPassword="";
	private String fbiKeystoreLocation="";
	private String fbiKeystorePassword="";

	public String getStagingDataRestServiceBaseUrl() {
		return stagingDataRestServiceBaseUrl;
	}

	public void setStagingDataRestServiceBaseUrl(String stagingDataRestServiceBaseUrl) {
		this.stagingDataRestServiceBaseUrl = stagingDataRestServiceBaseUrl;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getNibrsNiemDocumentFolder() {
		return nibrsNiemDocumentFolder;
	}

	public void setNibrsNiemDocumentFolder(String nibrsNiemDocumentFolder) {
		this.nibrsNiemDocumentFolder = nibrsNiemDocumentFolder;
	}

	public String getNibrsNiemServiceEndpointUrl() {
		return nibrsNiemServiceEndpointUrl;
	}

	public void setNibrsNiemServiceEndpointUrl(String nibrsNiemServiceEndpointUrl) {
		this.nibrsNiemServiceEndpointUrl = nibrsNiemServiceEndpointUrl;
	}

	public String getTruststoreLocation() {
		return truststoreLocation;
	}

	public void setTruststoreLocation(String truststoreLocation) {
		this.truststoreLocation = truststoreLocation;
	}

	public String getTruststorePassword() {
		return truststorePassword;
	}

	public void setTruststorePassword(String truststorePassword) {
		this.truststorePassword = truststorePassword;
	}

	public String getFbiKeyPassword() {
		return fbiKeyPassword;
	}

	public void setFbiKeyPassword(String fbiKeyPassword) {
		this.fbiKeyPassword = fbiKeyPassword;
	}

	public String getFbiKeystoreLocation() {
		return fbiKeystoreLocation;
	}

	public void setFbiKeystoreLocation(String fbiKeystoreLocation) {
		this.fbiKeystoreLocation = fbiKeystoreLocation;
	}

	public String getFbiKeystorePassword() {
		return fbiKeystorePassword;
	}

	public void setFbiKeystorePassword(String fbiKeystorePassword) {
		this.fbiKeystorePassword = fbiKeystorePassword;
	}

}