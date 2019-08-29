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

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PrecertErrorSearchRequest implements Serializable {
	private static final long serialVersionUID = 7916910066665545067L;
	private String incidentIdentifier; 
	private List<Integer> agencyIds; 
	private Integer nibrsErrorCodeTypeId; 

	@Range(min=1, max=12)
	private Integer submissionMonth;
	private Integer submissionYear; 
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getIncidentIdentifier() {
		return incidentIdentifier;
	}
	public void setIncidentIdentifier(String incidentIdentifier) {
		this.incidentIdentifier = StringUtils.trimToNull(incidentIdentifier);
	}
	public Integer getSubmissionMonth() {
		return submissionMonth;
	}
	public void setSubmissionMonth(Integer submissionMonth) {
		this.submissionMonth = submissionMonth;
	}
	public Integer getSubmissionYear() {
		return submissionYear;
	}
	public void setSubmissionYear(Integer submissionYear) {
		this.submissionYear = submissionYear;
	}

	@JsonIgnore
	public boolean isEmpty() {
		return StringUtils.isBlank(incidentIdentifier)
				&& (agencyIds == null || agencyIds.isEmpty())  
				&& (nibrsErrorCodeTypeId == null || nibrsErrorCodeTypeId == 0) 
				&& submissionMonth == null 
				&& submissionYear == null ;
	}
	public List<Integer> getAgencyIds() {
		return agencyIds;
	}
	public void setAgencyIds(List<Integer> agencyIds) {
		this.agencyIds = agencyIds;
	}
	public Integer getNibrsErrorCodeTypeId() {
		return nibrsErrorCodeTypeId;
	}
	public void setNibrsErrorCodeTypeId(Integer nibrsErrorCodeTypeId) {
		this.nibrsErrorCodeTypeId = nibrsErrorCodeTypeId;
	}
	
}
