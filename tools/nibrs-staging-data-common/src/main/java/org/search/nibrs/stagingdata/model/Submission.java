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

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;

@Entity
@NamedEntityGraph(name="allSubmissionJoins", attributeNodes = {
        @NamedAttributeNode("violations"),
	})
public class Submission {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer submissionId; 
	
	private String incidentIdentifier; 
	private String requestFilePath;
	private String responseFilePath;
	
	private String faultCode; 
	private String faultDescription; 
	private Boolean acceptedIndicator;
	private LocalDateTime submissionTimestamp;
	private LocalDateTime responseTimestamp;
	
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
	private Set<Violation> violations;
	
	public Submission() {
		super();
	}

	public String getIncidentIdentifier() {
		return incidentIdentifier;
	}

	public void setIncidentIdentifier(String incidentIdentifier) {
		this.incidentIdentifier = incidentIdentifier;
	}

	public String getRequestFilePath() {
		return requestFilePath;
	}

	public void setRequestFilePath(String requestFilePath) {
		this.requestFilePath = requestFilePath;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public String getResponseFilePath() {
		return responseFilePath;
	}

	public void setResponseFilePath(String responseFilePath) {
		this.responseFilePath = responseFilePath;
	}

	public Boolean getAcceptedIndicator() {
		return acceptedIndicator;
	}

	public void setAcceptedIndicator(Boolean acceptedIndicator) {
		this.acceptedIndicator = acceptedIndicator;
	}

	public LocalDateTime getSubmissionTimestamp() {
		return submissionTimestamp;
	}

	public void setSubmissionTimestamp(LocalDateTime submissionTimestamp) {
		this.submissionTimestamp = submissionTimestamp;
	}

	public LocalDateTime getResponseTimestamp() {
		return responseTimestamp;
	}

	public void setResponseTimestamp(LocalDateTime responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}

	public Set<Violation> getViolations() {
		return violations;
	}

	public void setViolations(Set<Violation> violations) {
		this.violations = violations;
	}

	public String getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

	public String getFaultDescription() {
		return faultDescription;
	}

	public void setFaultDescription(String faultDescription) {
		this.faultDescription = faultDescription;
	}

	@Override
	public String toString() {
		return "Submission [submissionId=" + submissionId + ", incidentIdentifier=" + incidentIdentifier
				+ ", requestFilePath=" + requestFilePath + ", responseFilePath=" + responseFilePath + ", faultCode="
				+ faultCode + ", faultDescription=" + faultDescription + ", acceptedIndicator=" + acceptedIndicator
				+ ", submissionTimestamp=" + submissionTimestamp + ", responseTimestamp=" + responseTimestamp
				+ ", violations=" + violations + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acceptedIndicator == null) ? 0 : acceptedIndicator.hashCode());
		result = prime * result + ((faultCode == null) ? 0 : faultCode.hashCode());
		result = prime * result + ((faultDescription == null) ? 0 : faultDescription.hashCode());
		result = prime * result + ((incidentIdentifier == null) ? 0 : incidentIdentifier.hashCode());
		result = prime * result + ((requestFilePath == null) ? 0 : requestFilePath.hashCode());
		result = prime * result + ((responseFilePath == null) ? 0 : responseFilePath.hashCode());
		result = prime * result + ((responseTimestamp == null) ? 0 : responseTimestamp.hashCode());
		result = prime * result + ((submissionId == null) ? 0 : submissionId.hashCode());
		result = prime * result + ((submissionTimestamp == null) ? 0 : submissionTimestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Submission other = (Submission) obj;
		if (acceptedIndicator == null) {
			if (other.acceptedIndicator != null)
				return false;
		} else if (!acceptedIndicator.equals(other.acceptedIndicator))
			return false;
		if (faultCode == null) {
			if (other.faultCode != null)
				return false;
		} else if (!faultCode.equals(other.faultCode))
			return false;
		if (faultDescription == null) {
			if (other.faultDescription != null)
				return false;
		} else if (!faultDescription.equals(other.faultDescription))
			return false;
		if (incidentIdentifier == null) {
			if (other.incidentIdentifier != null)
				return false;
		} else if (!incidentIdentifier.equals(other.incidentIdentifier))
			return false;
		if (requestFilePath == null) {
			if (other.requestFilePath != null)
				return false;
		} else if (!requestFilePath.equals(other.requestFilePath))
			return false;
		if (responseFilePath == null) {
			if (other.responseFilePath != null)
				return false;
		} else if (!responseFilePath.equals(other.responseFilePath))
			return false;
		if (responseTimestamp == null) {
			if (other.responseTimestamp != null)
				return false;
		} else if (!responseTimestamp.equals(other.responseTimestamp))
			return false;
		if (submissionId == null) {
			if (other.submissionId != null)
				return false;
		} else if (!submissionId.equals(other.submissionId))
			return false;
		if (submissionTimestamp == null) {
			if (other.submissionTimestamp != null)
				return false;
		} else if (!submissionTimestamp.equals(other.submissionTimestamp))
			return false;
		if (violations == null) {
			if (other.violations != null)
				return false;
		} else if (!violations.equals(other.violations))
			return false;
		return true;
	}

}
