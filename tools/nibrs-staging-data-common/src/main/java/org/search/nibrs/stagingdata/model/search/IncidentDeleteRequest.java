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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IncidentDeleteRequest implements Serializable {
	private static final long serialVersionUID = 7916910066665545067L;
	private Integer ownerId; 
	private Integer agencyId; 
	private String stateCode;

	private String submissionMonth;
	private String submissionYear; 
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getSubmissionMonth() {
		return submissionMonth;
	}
	public void setSubmissionMonth(String submissionMonth) {
		this.submissionMonth = submissionMonth;
	}
	public String getSubmissionYear() {
		return submissionYear;
	}
	public void setSubmissionYear(String submissionYear) {
		this.submissionYear = submissionYear;
	}

	@JsonIgnore
	public boolean isEmpty() {
		return getAgencyId() == null 
				&& StringUtils.isBlank(stateCode)
				&& StringUtils.isBlank(submissionMonth) 
				&& StringUtils.isBlank(submissionYear) ;
	}
	public Integer getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
	public Integer getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
}
