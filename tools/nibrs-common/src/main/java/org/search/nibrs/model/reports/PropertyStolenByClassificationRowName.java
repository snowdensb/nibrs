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
package org.search.nibrs.model.reports;
public enum PropertyStolenByClassificationRowName{
	MURDER_AND_NONNEGLIGENT_MANSLAUGHTER("1. MURDER AND NONNEGLIGENT MANSLAUGHTER", "12"),  
	RAPE("2. RAPE","20"), 
	ROBBERY_HIGHWAY("3. ROBBERY \n\t\t\t\t(a) HIGHWAY (streets, alleys, etc.)", "31"), 
	ROBBERY_COMMERCIAL_HOUSE ("\t\t\t\t(b) COMMERCIAL HOUSE (except c, d, and f)", "32"), 
	ROBBERY_GAS_OR_SERVICE_STATION("\t\t\t\t(c) GAS OR SERVICE STATION", "33"), 
	ROBBERY_CONVENIENCE_STORE("\t\t\t\t(d) CONVENIENCE STORE", "34"), 
	ROBBERY_RESIDENCE("\t\t\t\t(e) RESIDENCE (anywhere on premises)", "35"), 
	ROBBERY_BANK("\t\t\t\t(f) BANK", "36"), 
	ROBBERY_MISCELLANEOUS("\t\t\t\t(g) MISCELLANEOUS", "37"), 
	ROBBERY_TOTAL("\t\t\t\t\t\t\t\t\t\tTOTAL ROBBERY", "30"), 
	BURGLARY_RESIDENCE_NIGHT("5. BURGLARY - BREAKING OR ENTERING\n\t\t\t\t(a) RESIDENCE (dwelling)\n\t\t\t\t\t\t\t\t(1) NIGHT (6 p.m. - 6 a.m.)", "51" ), 
	BURGLARY_RESIDENCE_DAY("\t\t\t\t\t\t\t\t(2) DAY (6 a.m. - 6 p.m.)", "52" ), 
	BURGLARY_RESIDENCE_UNKNOWN("\t\t\t\t\t\t\t\t(3) UNKNOWN", "53" ), 
	BURGLARY_NON_RESIDENCE_NIGHT("\t\t\t\t(b) NON-RESIDENCE (store, office, etc.)\n\t\t\t\t\t\t\t\t(1) NIGHT (6 p.m. - 6 a.m.)", "54" ), 
	BURGLARY_NON_RESIDENCE_DAY("\t\t\t\t\t\t\t\t(2) DAY (6 a.m. - 6 p.m.)", "55" ), 
	BURGLARY_NON_RESIDENCE_UNKNOWN("\t\t\t\t\t\t\t\t(3) UNKNOWN", "56" ), 
	BURGLARY_TOTAL("\t\t\t\t\t\t\t\t\t\t\t\tTOTAL BURGLARY", "50"),
	LARCENY_200_PLUS("6. LARCENY - THEFT (Except Motor Vehicle Theft)\n\t\t\t\t(a) $200 AND OVER", "61"),
	LARCENY_50_199("\t\t\t\t(b) $50 TO $199", "62"),
	LARCENY_UNDER_50("\t\t\t\t(c) UNDER $50", "63"),
	LARCENY_TOTAL("\t\t\t\t\t\t\t\t\t\t\t\tTOTAL LARCENY (Same as Item 6x)", "60"),
	MOTOR_VEHICLE_THEFT("7. MOTOR VEHICLE THEFT (Include Alleged Joy Ride)", "70"),
	GRAND_TOTAL("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tGRAND TOTAL - ALL ITEMS", "77"),
	LARCENY_POCKET_PICKING("ADDITIONAL ANALYSIS OF LARCENY AND MOTOR VEHICLE THEFT\n6x. NATURE OF LARCENIES UNDER ITEM 6"
			+ "\n\t\t\t\t(a) POCKET-PICKING", "81"),
	LARCENY_PURSE_SNATCHING("\t\t\t\t(b) PURSE-SNATCHING", "82"),
	LARCENY_SHOPLIFTING("\t\t\t\t(c) SHOPLIFTING", "83"),
	LARCENY_FROM_MOTOR_VEHICLES("\t\t\t\t(d) FROM MOTOR VEHICLES (except e)", "84"),
	LARCENY_MOTOR_VEHICLE_PARTS_AND_ACCESSORIES("\t\t\t\t(e) MOTOR VEHICLE PARTS AND ACCESSORIES", "85"),
	LARCENY_BICYCLES("\t\t\t\t(f) BICYCLES", "86"),
	LARCENY_FROM_BUILDING("\t\t\t\t(g) FROM BUILDING (except c and h)", "87"),
	LARCENY_FROM_COIN_OPERATED_MACHINES("\t\t\t\t(h) FROM ANY COIN-OPERATED MACHINES (parking meters,etc.)", "88"),
	LARCENY_ALL_OTHER("\t\t\t\t(i) ALL OTHER", "89"),
	LARCENIES_TOTAL_BY_NATURE("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTOTAL LARCENIES (Same as Item 6)", "80"),
	MOTOR_VEHICLES_STOLEN_AND_RECOVERED_LOCALLY("7x. MOTOR VEHICLES RECOVERED\n\t\t\t\t(a) STOLEN LOCALLY AND RECOVERED LOCALLY", "91"),
	MOTOR_VEHICLES_STOLEN_LOCALLY_AND_RECOVERED_BY_OTHER_JURISDICTIONS("\t\t\t\t(b) STOLEN LOCALLY AND RECOVERED BY OTHER JURISDICTIONS", "92"),
	MOTOR_VEHICLES_TOTAL_LOCALLY_STOLEN_MOTOR_VEHICLES_RECOVERED("\t\t\t\t(c) TOTAL LOCALLY STOLEN MOTOR VEHICLES RECOVERED (a & b)", "90"),
	MOTOR_VEHICLES_STOLEN_IN_OTHER_JURISDICTIONS_AND_RECOVERED_LOCALLY("\t\t\t\t(d) STOLEN IN OTHER JURISDICTIONS AND RECOVERED LOCALLY", "93");
	
	private String label;
	
	private String dataEntry;
	
	private PropertyStolenByClassificationRowName(String label, String dataEntry){
		
		this.setLabel(label);
		
		this.setDataEntry(dataEntry);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDataEntry() {
		return dataEntry;
	}

	public void setDataEntry(String dataEntry) {
		this.dataEntry = dataEntry;
	}
}

