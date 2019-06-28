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
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IncidentSearchRequest implements Serializable {
	private static final long serialVersionUID = 7916910066665545067L;
	private String incidentIdentifier; 
	private List<Integer> agencyIds; 
	private List<String> agenycyNames; 
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate incidentDateRangeStartDate; 
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate incidentDateRangeEndDate; 
	private Integer ucrOffenseCodeTypeId; 
	private String offenseCode; 

	@Max(12)
	@Min(1)
	private Integer submissionMonth;
	private Integer submissionYear; 
	private Boolean fbiSubmission;
	
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
	public Integer getUcrOffenseCodeTypeId() {
		return ucrOffenseCodeTypeId;
	}
	public void setUcrOffenseCodeTypeId(Integer ucrOffenseCodeTypeId) {
		this.ucrOffenseCodeTypeId = ucrOffenseCodeTypeId;
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
	public Boolean getFbiSubmission() {
		return fbiSubmission;
	}
	public void setFbiSubmission(Boolean fbiSubmission) {
		this.fbiSubmission = fbiSubmission;
	}
	public String getOffenseCode() {
		return offenseCode;
	}
	public void setOffenseCode(String offenseCode) {
		this.offenseCode = offenseCode;
	} 

	@JsonIgnore
	public boolean isEmpty() {
		return StringUtils.isBlank(incidentIdentifier)
				&& (agencyIds == null || agencyIds.isEmpty())  
				&& incidentDateRangeStartDate == null
				&& incidentDateRangeEndDate == null
				&& (ucrOffenseCodeTypeId == null || ucrOffenseCodeTypeId == 0) 
				&& submissionMonth == null 
				&& submissionYear == null ;
	}
	public List<Integer> getAgencyIds() {
		return agencyIds;
	}
	public void setAgencyIds(List<Integer> agencyIds) {
		this.agencyIds = agencyIds;
	}
	public List<String> getAgenycyNames() {
		return agenycyNames;
	}
	public void setAgenycyNames(List<String> agenycyNames) {
		this.agenycyNames = agenycyNames;
	}
	public LocalDate getIncidentDateRangeStartDate() {
		return incidentDateRangeStartDate;
	}
	public void setIncidentDateRangeStartDate(LocalDate incidentDateRangeStartDate) {
		this.incidentDateRangeStartDate = incidentDateRangeStartDate;
	}
	public LocalDate getIncidentDateRangeEndDate() {
		return incidentDateRangeEndDate;
	}
	public void setIncidentDateRangeEndDate(LocalDate incidentDateRangeEndDate) {
		this.incidentDateRangeEndDate = incidentDateRangeEndDate;
	}
}
