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
public enum PropertyTypeValueRowName{
	CURRENCY_NOTES_ETC("(A) Currency, Notes, Etc.", "01"),  
	JEWELRY_AND_PRECIOUS_METALS("(B) Jewelry and Precious Metals","02"), 
	CLOTHING_FURS("(C) Clothing and Furs", "03"), 
	LOCALLY_STOLEN_MOTOR_VEHICLES("(D) Locally Stolen Motor Vehicles", "04"), 
	OFFICE_EQUIPMENT("(E) Office Equipment", "05"), 
	TELEVISIONS_RADIOS_STEREOS_ETC("(F) Televisions, Radios, Stereos, Etc.", "06"), 
	FIREARMS("(G) Firearms", "07"), 
	HOUSEHOLD_GOODS("(H) Household Goods", "08"), 
	CONSUMABLE_GOODS("(I) Consumable Goods", "09"), 
	LIVESTOCK("(J) Livestock", "10"), 
	MISCELLANEOUS("(K) Miscellaneous", "11" ), 
	TOTAL("TOTAL", "00");
	
	private String label;
	
	private String dataEntry;
	
	private PropertyTypeValueRowName(String label, String dataEntry){
		
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

