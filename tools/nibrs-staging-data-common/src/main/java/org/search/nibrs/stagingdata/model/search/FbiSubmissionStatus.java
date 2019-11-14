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
package org.search.nibrs.stagingdata.model.search;

public enum FbiSubmissionStatus {
	REJECTED("Rejected"), ACCEPTED("Accepted"), NOT_SUBMITTED("Not submitted"); 
	
	private String description; 
	
	FbiSubmissionStatus(String description) {
		this.setDescription(description);
	}
	
	public String toString() {
		return getDescription(); 
	}

	public String getName() {
		return name(); 
	}
	
	public String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}
}
