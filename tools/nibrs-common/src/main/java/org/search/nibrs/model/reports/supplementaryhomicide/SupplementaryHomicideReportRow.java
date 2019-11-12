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
package org.search.nibrs.model.reports.supplementaryhomicide;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SupplementaryHomicideReportRow implements Serializable{
	private static final long serialVersionUID = -8439219265321448834L;
	private String incidentNumber; 
	private HomicideSituation homicideSituation; 
	private Person victim; 
	private Person offender; 
	private List<String> weaponUsed;
	private String relationshipOfVictimToOffender;
	private List<String> circumstances;
	
	public SupplementaryHomicideReportRow() {
		super();
	}

	public String getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}

	public HomicideSituation getHomicideSituation() {
		return homicideSituation;
	}

	public void setHomicideSituation(HomicideSituation homicideSituation) {
		this.homicideSituation = homicideSituation;
	}

	public Person getVictim() {
		return victim;
	}

	public void setVictim(Person victim) {
		this.victim = victim;
	}

	public Person getOffender() {
		return offender;
	}

	public void setOffender(Person offender) {
		this.offender = offender;
	}

	public List<String> getWeaponUsed() {
		return weaponUsed;
	}

	public void setWeaponUsed(List<String> weaponUsed) {
		this.weaponUsed = weaponUsed;
	}

	public String getRelationshipOfVictimToOffender() {
		return relationshipOfVictimToOffender;
	}

	public void setRelationshipOfVictimToOffender(String relationshipOfVictimToOffender) {
		this.relationshipOfVictimToOffender = relationshipOfVictimToOffender;
	}

	public List<String> getCircumstances() {
		return circumstances;
	}

	public void setCircumstances(List<String> circumstances) {
		this.circumstances = circumstances;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
