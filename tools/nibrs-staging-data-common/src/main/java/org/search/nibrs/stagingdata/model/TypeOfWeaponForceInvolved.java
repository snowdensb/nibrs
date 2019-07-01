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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;


@Entity
public class TypeOfWeaponForceInvolved implements Serializable{
	private static final long serialVersionUID = 8597871552401839697L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer typeOfWeaponForceInvolvedId; 
	
    @ManyToOne
    @JoinColumn(name = "offenseSegmentId")
	private OffenseSegment offenseSegment; 
    
    @ManyToOne
    @JoinColumn(name = "typeOfWeaponForceInvolvedTypeId")
	private TypeOfWeaponForceInvolvedType typeOfWeaponForceInvolvedType;
    
    @Column(name = "automaticWeaponIndicator")
	private String automaticWeaponIndicator; 
	
	public TypeOfWeaponForceInvolved() {
		super();
	}

	public TypeOfWeaponForceInvolved(OffenseSegment offenseSegment,
			TypeOfWeaponForceInvolvedType typeOfWeaponForceInvolvedType, String automaticWeaponIndicator) {
		super();
		this.offenseSegment = offenseSegment;
		this.typeOfWeaponForceInvolvedType = typeOfWeaponForceInvolvedType;
		this.automaticWeaponIndicator = automaticWeaponIndicator;
	}
	
	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		String resultWithoutOffenseSegment = ReflectionToStringBuilder.toStringExclude(this, "offenseSegment");
		int index = StringUtils.indexOf(resultWithoutOffenseSegment, ",");
		
		StringBuilder sb = new StringBuilder(resultWithoutOffenseSegment);
		sb.insert(index + 1, "offenseSegmentId=" + offenseSegment.getOffenseSegmentId() + ",");
		
        return sb.toString();
	}
	public Integer getTypeOfWeaponForceInvolvedId() {
		return typeOfWeaponForceInvolvedId;
	}

	public void setTypeOfWeaponForceInvolvedId(Integer typeOfWeaponForceInvolvedId) {
		this.typeOfWeaponForceInvolvedId = typeOfWeaponForceInvolvedId;
	}

	public OffenseSegment getOffenseSegment() {
		return offenseSegment;
	}

	public void setOffenseSegment(OffenseSegment offenseSegment) {
		this.offenseSegment = offenseSegment;
	}

	public TypeOfWeaponForceInvolvedType getTypeOfWeaponForceInvolvedType() {
		return typeOfWeaponForceInvolvedType;
	}

	public void setTypeOfWeaponForceInvolvedType(TypeOfWeaponForceInvolvedType typeOfWeaponForceInvolvedType) {
		this.typeOfWeaponForceInvolvedType = typeOfWeaponForceInvolvedType;
	}

	public String getAutomaticWeaponIndicator() {
		return automaticWeaponIndicator;
	}

	public void setAutomaticWeaponIndicator(String automaticWeaponIndicator) {
		this.automaticWeaponIndicator = automaticWeaponIndicator;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeOfWeaponForceInvolved other = (TypeOfWeaponForceInvolved) obj;
		if (automaticWeaponIndicator == null) {
			if (other.automaticWeaponIndicator != null)
				return false;
		} else if (!automaticWeaponIndicator.equals(other.automaticWeaponIndicator))
			return false;
		if (offenseSegment == null) {
			if (other.offenseSegment != null)
				return false;
		} else if (!offenseSegment.equals(other.offenseSegment))
			return false;
		if (typeOfWeaponForceInvolvedId == null) {
			if (other.typeOfWeaponForceInvolvedId != null)
				return false;
		} else if (!typeOfWeaponForceInvolvedId.equals(other.typeOfWeaponForceInvolvedId))
			return false;
		if (typeOfWeaponForceInvolvedType == null) {
			if (other.typeOfWeaponForceInvolvedType != null)
				return false;
		} else if (!typeOfWeaponForceInvolvedType.equals(other.typeOfWeaponForceInvolvedType))
			return false;
		return true;
	}

}
