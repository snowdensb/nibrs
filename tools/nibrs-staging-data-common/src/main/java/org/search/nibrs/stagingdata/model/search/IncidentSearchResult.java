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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IncidentSearchResult implements Serializable {
	private static final long serialVersionUID = 7916910066665545067L;

	private Integer totalCount; 
	private Integer returnedCount; 
	private long eligibleFbiSubmissionCount; 
	private List<IncidentPointer> incidentPointers; 
	public IncidentSearchResult() {
		super();
	}
	
	public IncidentSearchResult(List<IncidentPointer> incidentPointers, Integer sizeLimit) {
		this();
		this.setTotalCount(incidentPointers.size());
		
		this.setEligibleFbiSubmissionCount(incidentPointers.stream()
				.filter(i-> i.getOffenseCode() == null || !i.getOffenseCode().equalsIgnoreCase("90I"))
				.filter(i->BooleanUtils.isNotTrue(i.getFbiSubmission()) )
				.count());
		
		if (getTotalCount() > sizeLimit) {
		    Comparator<IncidentPointer> incidentSearchResultComparator
		      = Comparator.comparing(IncidentPointer::getReportTimestamp);
		    
		    incidentPointers.sort(incidentSearchResultComparator.reversed());
	
			this.setIncidentPointers(incidentPointers.stream()
					.limit(sizeLimit)
					.collect(Collectors.toList()));
			this.setReturnedCount(sizeLimit); 
		}
		else {
			this.setReturnedCount(this.getTotalCount()); 
			this.setIncidentPointers(incidentPointers); 
		}
		
		
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public Integer getReturnedCount() {
		return returnedCount;
	}

	private void setReturnedCount(Integer returnedCount) {
		this.returnedCount = returnedCount;
	}

	public List<IncidentPointer> getIncidentPointers() {
		return incidentPointers;
	}

	private void setIncidentPointers(List<IncidentPointer> incidentPointers) {
		this.incidentPointers = incidentPointers;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	private void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public long getEligibleFbiSubmissionCount() {
		return eligibleFbiSubmissionCount;
	}

	public void setEligibleFbiSubmissionCount(long eligibleFbiSubmissionCount) {
		this.eligibleFbiSubmissionCount = eligibleFbiSubmissionCount;
	}
}
