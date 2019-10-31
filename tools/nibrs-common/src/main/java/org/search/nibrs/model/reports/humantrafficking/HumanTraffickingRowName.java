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
package org.search.nibrs.model.reports.humantrafficking;
public enum HumanTraffickingRowName{
	COMMERCIAL_SEX_ACTS("A. Commercial Sex Acts "),  
	INVOLUNTARY_SERVITUDE("B. Involuntary Servitude"), 
	GRAND_TOTAL("GRAND TOTAL");
	
	private String label;
	
	private HumanTraffickingRowName(String label){
		this.setLabel(label);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}

