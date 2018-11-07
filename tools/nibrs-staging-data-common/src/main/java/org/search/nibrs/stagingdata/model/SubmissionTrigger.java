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
package org.search.nibrs.stagingdata.model;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SubmissionTrigger {
	private List<String> oris; 
	private Integer startYear; 
	private Integer startMonth; 
	private Integer endYear; 
	private Integer endMonth; 
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public List<String> getOris() {
		return oris;
	}

	public void setOris(List<String> oris) {
		this.oris = oris;
	}

	public Integer getStartYear() {
		return startYear;
	}

	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	public Integer getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}

	public Integer getEndYear() {
		return endYear;
	}

	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}

	public Integer getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}

	public java.sql.Date getStartDate() {
		java.sql.Date date = null;
		if (startYear != null && startYear > 0) {
			LocalDate localDate = LocalDate.of(startYear, 1, 1);
			
			if (isValidMonth(startMonth)) {
					localDate = LocalDate.of(startYear, startMonth, 1);
			}
			date = java.sql.Date.valueOf(localDate);
		}
		
		return date;
	}
	
	public java.sql.Date getEndDate() {
		
		java.sql.Date date = null;
		if (endYear != null && endYear > 0) {
			LocalDate localDate = LocalDate.of(endYear, 12, 31);
			
			if (isValidMonth(endMonth)) {
					if (endMonth < 12) {
						localDate = LocalDate.of(endYear, endMonth+1, 1).minusDays(1);
					}
					else {
						localDate = LocalDate.of(endYear, 12, 31);
					}
			}
			
			date = java.sql.Date.valueOf(localDate);
		}
		
		return date;
	}
	
	private boolean isValidMonth(Integer month) {
		return month != null && month > 0 && month <=12;
	}

}
