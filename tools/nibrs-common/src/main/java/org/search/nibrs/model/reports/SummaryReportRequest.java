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

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SummaryReportRequest implements Serializable {
	
	private static final long serialVersionUID = 6719873817881739578L;

	private String stateCode;
	
	private String ori; 
	
	private Integer agencyId;

	private Integer incidentMonth;
	private Integer incidentYear; 
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@JsonIgnore
	public boolean isEmpty() {
		return StringUtils.isBlank(stateCode)
				&& agencyId == null
				&& StringUtils.isBlank(getOri())
				&& getIncidentMonth() == null 
				&& getIncidentYear() == null ;
	}

	public String getOri() {
		return ori;
	}

	public void setOri(String ori) {
		this.ori = ori;
	}

	public Integer getIncidentMonth() {
		return incidentMonth;
	}

	public String getIncidentMonthString() {
		
		if (incidentMonth == null) return StringUtils.EMPTY;
		return incidentMonth.toString();
	}
	
	public void setIncidentMonth(Integer incidentMonth) {
		this.incidentMonth = incidentMonth;
	}

	public Integer getIncidentYear() {
		return incidentYear;
	}

	public String getIncidentYearString() {
		return String.valueOf(incidentYear);
	}
	
	public void setIncidentYear(Integer incidentYear) {
		this.incidentYear = incidentYear;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}
}
