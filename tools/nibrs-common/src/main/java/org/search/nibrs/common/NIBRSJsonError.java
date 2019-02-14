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
package org.search.nibrs.common;

public class NIBRSJsonError {

	/**
	 * See NIBRSError, dateOfTape
	 */
	private String submissionDate;
	
	private String sourceLocation;
	
	private char actionType;
	
	private String ori;
	
	private String incidentNumber;
	
	private String segment;
	
	private String withinSegmentIdentifier;
	
	private String withinOffenderArrestVictim;
	
	private String withinProperty;
	
	private String dataElement;
	
	private String errorCode;
	
	private String rejectedValue;
	
	public String getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(String sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public char getActionType() {
		return actionType;
	}

	public void setActionType(char actionType) {
		this.actionType = actionType;
	}

	public String getOri() {
		return ori;
	}

	public void setOri(String ori) {
		this.ori = ori;
	}

	public String getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}

	public String getWithinSegmentIdentifier() {
		return withinSegmentIdentifier;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public void setWithinSegmentIdentifier(String withinSegmentIdentifier) {
		this.withinSegmentIdentifier = withinSegmentIdentifier;
	}

	public String getWithinOffenderArrestVictim() {
		return withinOffenderArrestVictim;
	}

	public void setWithinOffenderArrestVictim(String withinOffenderArrestVictim) {
		this.withinOffenderArrestVictim = withinOffenderArrestVictim;
	}

	public String getWithinProperty() {
		return withinProperty;
	}

	public void setWithinProperty(String withinProperty) {
		this.withinProperty = withinProperty;
	}

	public String getDataElement() {
		return dataElement;
	}

	public void setDataElement(String dataElement) {
		this.dataElement = dataElement;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getRejectedValue() {
		return rejectedValue;
	}

	public void setRejectedValue(String rejectedValue) {
		this.rejectedValue = rejectedValue;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private String errorMessage;

	
}
