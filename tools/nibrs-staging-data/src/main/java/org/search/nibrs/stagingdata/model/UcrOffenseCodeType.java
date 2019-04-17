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
public class UcrOffenseCodeType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ucrOffenseCodeTypeId; 
	private String stateCode; 
	private String stateDescription; 
	private String nibrsCode; 
	private String nibrsDescription; 
	private String offenseCategory1; 
	private String offenseCategory2; 
	private String offenseCategory3; 
	private String offenseCategory4; 
	
	public UcrOffenseCodeType() {
		super();
	}
	
	public UcrOffenseCodeType(Integer ucrOffenseCodeTypeId) {
		super();
		this.ucrOffenseCodeTypeId = ucrOffenseCodeTypeId;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getOffenseCategory1() {
		return offenseCategory1;
	}

	public void setOffenseCategory1(String offenseCategory1) {
		this.offenseCategory1 = offenseCategory1;
	}

	public String getOffenseCategory2() {
		return offenseCategory2;
	}

	public void setOffenseCategory2(String offenseCategory2) {
		this.offenseCategory2 = offenseCategory2;
	}

	public String getOffenseCategory3() {
		return offenseCategory3;
	}

	public void setOffenseCategory3(String offenseCategory3) {
		this.offenseCategory3 = offenseCategory3;
	}

	public String getOffenseCategory4() {
		return offenseCategory4;
	}

	public void setOffenseCategory4(String offenseCategory4) {
		this.offenseCategory4 = offenseCategory4;
	}

	public Integer getUcrOffenseCodeTypeId() {
		return ucrOffenseCodeTypeId;
	}

	public void setUcrOffenseCodeTypeId(Integer ucrOffenseCodeTypeId) {
		this.ucrOffenseCodeTypeId = ucrOffenseCodeTypeId;
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
		result = prime * result + ((offenseCategory1 == null) ? 0 : offenseCategory1.hashCode());
		result = prime * result + ((offenseCategory2 == null) ? 0 : offenseCategory2.hashCode());
		result = prime * result + ((offenseCategory3 == null) ? 0 : offenseCategory3.hashCode());
		result = prime * result + ((offenseCategory4 == null) ? 0 : offenseCategory4.hashCode());
		result = prime * result + ((stateCode == null) ? 0 : stateCode.hashCode());
		result = prime * result + ((stateDescription == null) ? 0 : stateDescription.hashCode());
		result = prime * result + ((ucrOffenseCodeTypeId == null) ? 0 : ucrOffenseCodeTypeId.hashCode());
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
		UcrOffenseCodeType other = (UcrOffenseCodeType) obj;
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
		if (offenseCategory1 == null) {
			if (other.offenseCategory1 != null)
				return false;
		} else if (!offenseCategory1.equals(other.offenseCategory1))
			return false;
		if (offenseCategory2 == null) {
			if (other.offenseCategory2 != null)
				return false;
		} else if (!offenseCategory2.equals(other.offenseCategory2))
			return false;
		if (offenseCategory3 == null) {
			if (other.offenseCategory3 != null)
				return false;
		} else if (!offenseCategory3.equals(other.offenseCategory3))
			return false;
		if (offenseCategory4 == null) {
			if (other.offenseCategory4 != null)
				return false;
		} else if (!offenseCategory4.equals(other.offenseCategory4))
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
		if (ucrOffenseCodeTypeId == null) {
			if (other.ucrOffenseCodeTypeId != null)
				return false;
		} else if (!ucrOffenseCodeTypeId.equals(other.ucrOffenseCodeTypeId))
			return false;
		return true;
	}

}
