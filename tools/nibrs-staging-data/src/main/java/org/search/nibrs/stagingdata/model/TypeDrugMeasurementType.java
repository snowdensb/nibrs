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

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Cacheable
public class TypeDrugMeasurementType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer typeDrugMeasurementTypeId; 
	
	private String stateCode; 
	private String stateDescription; 
	private String nibrsCode; 
	private String nibrsDescription; 
	
	public TypeDrugMeasurementType() {
		super();
	}

	public TypeDrugMeasurementType(Integer typeDrugMeasurementTypeId) {
		super();
		this.typeDrugMeasurementTypeId = typeDrugMeasurementTypeId;
	}

	public TypeDrugMeasurementType(Integer typeDrugMeasurementTypeId, String stateCode, String stateDescription,
			String nibrsCode, String nibrsDescription) {
		super();
		this.typeDrugMeasurementTypeId = typeDrugMeasurementTypeId;
		this.stateCode = stateCode;
		this.stateDescription = stateDescription;
		this.nibrsCode = nibrsCode;
		this.nibrsDescription = nibrsDescription;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getTypeDrugMeasurementTypeId() {
		return typeDrugMeasurementTypeId;
	}

	public void setTypeDrugMeasurementTypeId(Integer typeDrugMeasurementTypeId) {
		this.typeDrugMeasurementTypeId = typeDrugMeasurementTypeId;
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
		result = prime * result + ((typeDrugMeasurementTypeId == null) ? 0 : typeDrugMeasurementTypeId.hashCode());
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
		TypeDrugMeasurementType other = (TypeDrugMeasurementType) obj;
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
		if (typeDrugMeasurementTypeId == null) {
			if (other.typeDrugMeasurementTypeId != null)
				return false;
		} else if (!typeDrugMeasurementTypeId.equals(other.typeDrugMeasurementTypeId))
			return false;
		return true;
	}

}
