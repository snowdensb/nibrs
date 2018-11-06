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
package org.search.nibrs.stagingdata;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("staging.data")
public class AppProperties {
	
	private String nibrsNiemDocumentFolder = "/tmp/nibrs/niemSubmission/input";
    private String submittingAgencyOri = "SUBORI123";
	private Map<String, String> nonNumericAgeCodeMapping = new HashMap<>();

	public AppProperties() {
		super();
		getNonNumericAgeCodeMapping().put("NN", "NEONATAL");
		getNonNumericAgeCodeMapping().put("NB", "NEWBORN");
		getNonNumericAgeCodeMapping().put("BB", "BABY");
		getNonNumericAgeCodeMapping().put("00", "UNKNOWN");
	}

	public String getSubmittingAgencyOri() {
		return submittingAgencyOri;
	}

	public void setSubmittingAgencyOri(String submittingAgencyOri) {
		this.submittingAgencyOri = submittingAgencyOri;
	}

	public Map<String, String> getNonNumericAgeCodeMapping() {
		return nonNumericAgeCodeMapping;
	}

	public String getNibrsNiemDocumentFolder() {
		return nibrsNiemDocumentFolder;
	}

	public void setNibrsNiemDocumentFolder(String nibrsNiemDocumentFolder) {
		this.nibrsNiemDocumentFolder = nibrsNiemDocumentFolder;
	}

}