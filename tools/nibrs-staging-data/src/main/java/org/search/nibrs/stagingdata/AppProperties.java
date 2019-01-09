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
	private Map<String, String> propertyCodeMapping = new HashMap<>();
	private Map<String, String> locationCodeMapping = new HashMap<>();

	public AppProperties() {
		super();
		getNonNumericAgeCodeMapping().put("NN", "NEONATAL");
		getNonNumericAgeCodeMapping().put("NB", "NEWBORN");
		getNonNumericAgeCodeMapping().put("BB", "BABY");
		getNonNumericAgeCodeMapping().put("00", "UNKNOWN");
		
		propertyCodeMapping.put("01", "Miscellaneous");
		propertyCodeMapping.put("02", "Consumable Goods");
		propertyCodeMapping.put("03", "Locally Stolen Motor Vehicles");
		propertyCodeMapping.put("04", "Miscellaneous");
		propertyCodeMapping.put("05", "Locally Stolen Motor Vehicles");
		propertyCodeMapping.put("06", "Clothing and Furs");
		propertyCodeMapping.put("07", "Office Equipment");
		propertyCodeMapping.put("08", "Consumable Goods");
		propertyCodeMapping.put("09", "Miscellaneous");
		propertyCodeMapping.put("10", "Consumable Goods");
		propertyCodeMapping.put("11", "Miscellaneous");
		propertyCodeMapping.put("12", "Miscellaneous");
		propertyCodeMapping.put("13", "Firearms");
		propertyCodeMapping.put("14", "Miscellaneous");
		propertyCodeMapping.put("15", "Miscellaneous");
		propertyCodeMapping.put("16", "Household Goods");
		propertyCodeMapping.put("17", "Jewelry and Precious Metals");
		propertyCodeMapping.put("18", "Livestock");
		propertyCodeMapping.put("19", "Miscellaneous");
		propertyCodeMapping.put("20", "Currency, Notes, Etc.");
		propertyCodeMapping.put("21", "Currency, Notes, Etc.");
		propertyCodeMapping.put("22", "Miscellaneous");
		propertyCodeMapping.put("23", "Office Equipment");
		propertyCodeMapping.put("24", "Locally Stolen Motor Vehicles");
		propertyCodeMapping.put("25", "Clothing and Furs");
		propertyCodeMapping.put("26", "Televisions, Radios, Stereos, Etc");
		propertyCodeMapping.put("27", "Televisions, Radios, Stereos, Etc");
		propertyCodeMapping.put("28", "Locally Stolen Motor Vehicles");
		propertyCodeMapping.put("29", "Miscellaneous");
		propertyCodeMapping.put("30", "Miscellaneous");
		propertyCodeMapping.put("31", "Miscellaneous");
		propertyCodeMapping.put("32", "Miscellaneous");
		propertyCodeMapping.put("33", "Miscellaneous");
		propertyCodeMapping.put("34", "Miscellaneous");
		propertyCodeMapping.put("35", "Miscellaneous");
		propertyCodeMapping.put("36", "Miscellaneous");
		propertyCodeMapping.put("37", "Locally Stolen Motor Vehicles");
		propertyCodeMapping.put("38", "Miscellaneous");
		propertyCodeMapping.put("39", "Miscellaneous");
		propertyCodeMapping.put("40", "Miscellaneous");
		propertyCodeMapping.put("41", "Miscellaneous");
		propertyCodeMapping.put("42", "Consumable Goods");
		propertyCodeMapping.put("43", "Miscellaneous");
		propertyCodeMapping.put("44", "Miscellaneous");
		propertyCodeMapping.put("45", "Miscellaneous");
		propertyCodeMapping.put("46", "Miscellaneous");
		propertyCodeMapping.put("47", "Miscellaneous");
		propertyCodeMapping.put("48", "Miscellaneous");
		propertyCodeMapping.put("49", "Miscellaneous");
		propertyCodeMapping.put("50", "Miscellaneous");
		propertyCodeMapping.put("51", "Miscellaneous");
		propertyCodeMapping.put("52", "Miscellaneous");
		propertyCodeMapping.put("53", "Miscellaneous");
		propertyCodeMapping.put("54", "Miscellaneous");
		propertyCodeMapping.put("55", "Miscellaneous");
		propertyCodeMapping.put("56", "Miscellaneous");
		propertyCodeMapping.put("57", "Miscellaneous");
		propertyCodeMapping.put("58", "Miscellaneous");
		propertyCodeMapping.put("59", "Miscellaneous");
		propertyCodeMapping.put("64", "Consumable Goods");
		propertyCodeMapping.put("65", "Miscellaneous");
		propertyCodeMapping.put("66", "Miscellaneous");
		propertyCodeMapping.put("67", "Miscellaneous");
		propertyCodeMapping.put("68", "Miscellaneous");
		propertyCodeMapping.put("69", "Miscellaneous");
		propertyCodeMapping.put("70", "Miscellaneous");
		propertyCodeMapping.put("71", "Miscellaneous");
		propertyCodeMapping.put("72", "Miscellaneous");
		propertyCodeMapping.put("73", "Miscellaneous");
		propertyCodeMapping.put("74", "Televisions, Radios, Stereos, Etc");
		propertyCodeMapping.put("75", "Televisions, Radios, Stereos, Etc");
		propertyCodeMapping.put("76", "Miscellaneous");
		propertyCodeMapping.put("77", "Miscellaneous");
		propertyCodeMapping.put("78", "Miscellaneous");
		propertyCodeMapping.put("79", "Miscellaneous");
		propertyCodeMapping.put("80", "Miscellaneous");
		propertyCodeMapping.put("88", "Miscellaneous");
		
		locationCodeMapping.put("01", "Miscellaneous");
		locationCodeMapping.put("02", "Bank");
		locationCodeMapping.put("03", "Commercial House");
		locationCodeMapping.put("04", "Miscellaneous");
		locationCodeMapping.put("05", "Commercial House");
		locationCodeMapping.put("06", "Miscellaneous");
		locationCodeMapping.put("07", "Convenience Store");
		locationCodeMapping.put("08", "Commercial House");
		locationCodeMapping.put("09", "Miscellaneous");
		locationCodeMapping.put("10", "Miscellaneous");
		locationCodeMapping.put("11", "Miscellaneous");
		locationCodeMapping.put("12", "Commercial House");
		locationCodeMapping.put("13", "Highway");
		locationCodeMapping.put("14", "Commercial House");
		locationCodeMapping.put("15", "Miscellaneous");
		locationCodeMapping.put("16", "Miscellaneous");
		locationCodeMapping.put("17", "Commercial House");
		locationCodeMapping.put("18", "Miscellaneous");
		locationCodeMapping.put("19", "Miscellaneous");
		locationCodeMapping.put("20", "Residence");
		locationCodeMapping.put("21", "Commercial House");
		locationCodeMapping.put("22", "Miscellaneous");
		locationCodeMapping.put("23", "Gas or Service Station");
		locationCodeMapping.put("24", "Commercial House");
		locationCodeMapping.put("25", "Miscellaneous");
		locationCodeMapping.put("37", "Miscellaneous");
		locationCodeMapping.put("38", "Miscellaneous");
		locationCodeMapping.put("39", "Miscellaneous");
		locationCodeMapping.put("40", "Miscellaneous");
		locationCodeMapping.put("41", "Commercial House");
		locationCodeMapping.put("42", "Miscellaneous");
		locationCodeMapping.put("43", "Miscellaneous");
		locationCodeMapping.put("44", "Miscellaneous");
		locationCodeMapping.put("45", "Miscellaneous");
		locationCodeMapping.put("46", "Miscellaneous");
		locationCodeMapping.put("47", "Commercial House");
		locationCodeMapping.put("48", "Miscellaneous");
		locationCodeMapping.put("49", "Miscellaneous");
		locationCodeMapping.put("50", "Miscellaneous");
		locationCodeMapping.put("51", "Miscellaneous");
		locationCodeMapping.put("52", "Miscellaneous");
		locationCodeMapping.put("53", "Miscellaneous");
		locationCodeMapping.put("54", "Commercial House");
		locationCodeMapping.put("55", "Commercial House");
		locationCodeMapping.put("56", "Miscellaneous");
		locationCodeMapping.put("57", "Miscellaneous");
		locationCodeMapping.put("58", "Miscellaneous");
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