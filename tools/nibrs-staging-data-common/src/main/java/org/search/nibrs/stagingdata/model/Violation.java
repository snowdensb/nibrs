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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class, 
	property = "violationId")
public class Violation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer violationId;
	
	@ManyToOne
	@JoinColumn(name="submissionId") 
	private Submission submission; 
	
	private String violationCode; 
	private String violationLevel; 
	private LocalDateTime violationTimestamp;
	
	public Integer getViolationId() {
		return violationId;
	}
	public void setViolationId(Integer violationId) {
		this.violationId = violationId;
	}
	public Submission getSubmission() {
		return submission;
	}
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}
	public String getViolationCode() {
		return violationCode;
	}
	public void setViolationCode(String violationCode) {
		this.violationCode = violationCode;
	}
	public String getViolationLevel() {
		return violationLevel;
	}
	public void setViolationLevel(String violationLevel) {
		this.violationLevel = violationLevel;
	}
	public LocalDateTime getViolationTimestamp() {
		return violationTimestamp;
	}
	public void setViolationTimestamp(LocalDateTime violationTimestamp) {
		this.violationTimestamp = violationTimestamp;
	}
	@Override
	public String toString() {
		return "Violation [violationId=" + violationId + ", submission=" + submission + ", violationCode="
				+ violationCode + ", violationLevel=" + violationLevel + ", violationTimestamp=" + violationTimestamp
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((submission == null) ? 0 : submission.hashCode());
		result = prime * result + ((violationCode == null) ? 0 : violationCode.hashCode());
		result = prime * result + ((violationId == null) ? 0 : violationId.hashCode());
		result = prime * result + ((violationLevel == null) ? 0 : violationLevel.hashCode());
		result = prime * result + ((violationTimestamp == null) ? 0 : violationTimestamp.hashCode());
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
		Violation other = (Violation) obj;
		if (submission == null) {
			if (other.submission != null)
				return false;
		} else if (!submission.getSubmissionId().equals(other.submission.getSubmissionId()))
			return false;
		if (violationCode == null) {
			if (other.violationCode != null)
				return false;
		} else if (!violationCode.equals(other.violationCode))
			return false;
		if (violationId == null) {
			if (other.violationId != null)
				return false;
		} else if (!violationId.equals(other.violationId))
			return false;
		if (violationLevel == null) {
			if (other.violationLevel != null)
				return false;
		} else if (!violationLevel.equals(other.violationLevel))
			return false;
		if (violationTimestamp == null) {
			if (other.violationTimestamp != null)
				return false;
		} else if (!violationTimestamp.equals(other.violationTimestamp))
			return false;
		return true;
	}

}
