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

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Cacheable
public class TypeOfCriminalActivityType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer typeOfCriminalActivityTypeId; 
	
	private String stateCode; 
	private String stateDescription; 
	private String nibrsCode; 
	private String nibrsDescription; 
	
	public TypeOfCriminalActivityType(Integer typeOfCriminalActivityTypeId, String stateCode, String stateDescription,
			String nibrsCode, String nibrsDescription) {
		super();
		this.typeOfCriminalActivityTypeId = typeOfCriminalActivityTypeId;
		this.stateCode = stateCode;
		this.stateDescription = stateDescription;
		this.nibrsCode = nibrsCode;
		this.nibrsDescription = nibrsDescription;
	}

	@ManyToMany(mappedBy = "typeOfCriminalActivityTypes", fetch=FetchType.LAZY)
	@JsonIgnore
	private Set<OffenseSegment> offenseSegments;
	
	public TypeOfCriminalActivityType() {
		super();
	}

	public TypeOfCriminalActivityType(Integer typeOfCriminalActivityTypeId) {
		super();
		this.typeOfCriminalActivityTypeId = typeOfCriminalActivityTypeId;
	}
	
	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toStringExclude(this, "offenseSegments");		
	}

	public Integer getTypeOfCriminalActivityTypeId() {
		return typeOfCriminalActivityTypeId;
	}

	public void setTypeOfCriminalActivityTypeId(Integer typeOfCriminalActivityTypeId) {
		this.typeOfCriminalActivityTypeId = typeOfCriminalActivityTypeId;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateDescription() {
		return stateDescription;
	}

	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}

	public String getNibrsCode() {
		return nibrsCode;
	}

	public void setNibrsCode(String nibrsCode) {
		this.nibrsCode = nibrsCode;
	}

	public String getNibrsDescription() {
		return nibrsDescription;
	}

	public void setNibrsDescription(String nibrsDescription) {
		this.nibrsDescription = nibrsDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nibrsCode == null) ? 0 : nibrsCode.hashCode());
		result = prime * result + ((nibrsDescription == null) ? 0 : nibrsDescription.hashCode());
		result = prime * result + ((stateCode == null) ? 0 : stateCode.hashCode());
		result = prime * result + ((stateDescription == null) ? 0 : stateDescription.hashCode());
		result = prime * result
				+ ((typeOfCriminalActivityTypeId == null) ? 0 : typeOfCriminalActivityTypeId.hashCode());
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
		TypeOfCriminalActivityType other = (TypeOfCriminalActivityType) obj;
		if (nibrsCode == null) {
			if (other.nibrsCode != null)
				return false;
		} else if (!nibrsCode.equals(other.nibrsCode))
			return false;
		if (nibrsDescription == null) {
			if (other.nibrsDescription != null)
				return false;
		} else if (!nibrsDescription.equals(other.nibrsDescription))
			return false;
		if (stateCode == null) {
			if (other.stateCode != null)
				return false;
		} else if (!stateCode.equals(other.stateCode))
			return false;
		if (stateDescription == null) {
			if (other.stateDescription != null)
				return false;
		} else if (!stateDescription.equals(other.stateDescription))
			return false;
		if (typeOfCriminalActivityTypeId == null) {
			if (other.typeOfCriminalActivityTypeId != null)
				return false;
		} else if (!typeOfCriminalActivityTypeId.equals(other.typeOfCriminalActivityTypeId))
			return false;
		return true;
	}

}
