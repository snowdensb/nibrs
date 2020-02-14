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
package org.search.nibrs.model.reports.cargotheft;

import java.io.Serializable;
import java.time.LocalDate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CargoTheftFormRow implements Serializable{
	private static final long serialVersionUID = -8439219265321448834L;
	private String incidentNumber; 
	private LocalDate incidentDate;
	private String segmentActionType; 
	
	public CargoTheftFormRow() {
		super();
	}
	
	public CargoTheftFormRow(String incidentNumber, LocalDate incidentDate, String segmentActionType) {
		this.incidentNumber = incidentNumber; 
		this.incidentDate = incidentDate; 
		this.segmentActionType = segmentActionType;
	}

	public String getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}

	public LocalDate getIncidentDate() {
		return incidentDate;
	}

	public String getIncidentDateString() {
		String incidentDateString = StringUtils.EMPTY; 
		
		if (incidentDate != null) {
			incidentDateString = 
					incidentDate.toString().replace("-", StringUtils.EMPTY);
		}
		return incidentDateString;
	}
	
	public void setIncidentDate(LocalDate incidentDate) {
		this.incidentDate = incidentDate;
	}

	public String getSegmentActionType() {
		return segmentActionType;
	}

	public void setSegmentActionType(String segmentActionType) {
		this.segmentActionType = segmentActionType;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
