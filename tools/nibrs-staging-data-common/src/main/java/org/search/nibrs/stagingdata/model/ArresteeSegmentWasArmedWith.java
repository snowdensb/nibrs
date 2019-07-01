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

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;


@Entity
public class ArresteeSegmentWasArmedWith implements Serializable{

	private static final long serialVersionUID = -869451478846730203L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer arresteeSegmentWasArmedWithId; 
	
    @ManyToOne
    @JoinColumn(name = "arresteeSegmentId")
	private ArresteeSegment arresteeSegment; 
    
    @ManyToOne
    @JoinColumn(name = "arresteeWasArmedWithTypeId")
	private ArresteeWasArmedWithType arresteeWasArmedWithType;
    
	private String automaticWeaponIndicator; 
	
	public ArresteeSegmentWasArmedWith() {
		super();
	}

	public ArresteeSegmentWasArmedWith(Integer arresteeSegmentWasArmedWithId, ArresteeSegment arresteeSegment,
			ArresteeWasArmedWithType arresteeWasArmedWithType, String automaticWeaponIndicator) {
		super();
		this.arresteeSegmentWasArmedWithId = arresteeSegmentWasArmedWithId;
		this.arresteeSegment = arresteeSegment;
		this.arresteeWasArmedWithType = arresteeWasArmedWithType;
		this.automaticWeaponIndicator = automaticWeaponIndicator;
	}

	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		String resultWithoutParentSegment = ReflectionToStringBuilder.toStringExclude(this, "arresteeSegment");
		int index = StringUtils.indexOf(resultWithoutParentSegment, ",");
		
		StringBuilder sb = new StringBuilder(resultWithoutParentSegment);
		sb.insert(index + 1, "arresteeSegmentId=" + getArresteeSegment().getArresteeSegmentId() + ",");
		
        return sb.toString();
	}
	public String getAutomaticWeaponIndicator() {
		return automaticWeaponIndicator;
	}

	public void setAutomaticWeaponIndicator(String automaticWeaponIndicator) {
		this.automaticWeaponIndicator = automaticWeaponIndicator;
	}

	public Integer getArresteeSegmentWasArmedWithId() {
		return arresteeSegmentWasArmedWithId;
	}

	public void setArresteeSegmentWasArmedWithId(Integer arresteeSegmentWasArmedWithId) {
		this.arresteeSegmentWasArmedWithId = arresteeSegmentWasArmedWithId;
	}

	public ArresteeSegment getArresteeSegment() {
		return arresteeSegment;
	}

	public void setArresteeSegment(ArresteeSegment arresteeSegment) {
		this.arresteeSegment = arresteeSegment;
	}

	public ArresteeWasArmedWithType getArresteeWasArmedWithType() {
		return arresteeWasArmedWithType;
	}

	public void setArresteeWasArmedWithType(ArresteeWasArmedWithType arresteeWasArmedWithType) {
		this.arresteeWasArmedWithType = arresteeWasArmedWithType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arresteeSegmentWasArmedWithId == null) ? 0 : arresteeSegmentWasArmedWithId.hashCode());
		result = prime * result + ((arresteeWasArmedWithType == null) ? 0 : arresteeWasArmedWithType.hashCode());
		result = prime * result + ((automaticWeaponIndicator == null) ? 0 : automaticWeaponIndicator.hashCode());
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
		ArresteeSegmentWasArmedWith other = (ArresteeSegmentWasArmedWith) obj;
		if (arresteeSegment == null) {
			if (other.arresteeSegment != null)
				return false;
		} else if (!arresteeSegment.equals(other.arresteeSegment))
			return false;
		if (arresteeSegmentWasArmedWithId == null) {
			if (other.arresteeSegmentWasArmedWithId != null)
				return false;
		} else if (!arresteeSegmentWasArmedWithId.equals(other.arresteeSegmentWasArmedWithId))
			return false;
		if (arresteeWasArmedWithType == null) {
			if (other.arresteeWasArmedWithType != null)
				return false;
		} else if (!arresteeWasArmedWithType.equals(other.arresteeWasArmedWithType))
			return false;
		if (automaticWeaponIndicator == null) {
			if (other.automaticWeaponIndicator != null)
				return false;
		} else if (!automaticWeaponIndicator.equals(other.automaticWeaponIndicator))
			return false;
		return true;
	}

}
