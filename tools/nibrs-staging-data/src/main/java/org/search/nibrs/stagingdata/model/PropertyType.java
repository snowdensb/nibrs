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
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.search.nibrs.stagingdata.model.segment.PropertySegment;


@Entity
@Cacheable
public class PropertyType implements Serializable{

	private static final long serialVersionUID = -2370698406499145556L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer propertyTypeId; 
	
    @ManyToOne
    @JoinColumn(name = "propertySegmentId")
	private PropertySegment propertySegment; 
    
    @ManyToOne
    @JoinColumn(name = "PropertyDescriptionTypeId")
	private PropertyDescriptionType propertyDescriptionType;
    
    @ManyToOne
    @JoinColumn(name = "recoveredDateId")
    private DateType recoveredDateType;
    
	private Double valueOfProperty; 
	private Date recoveredDate;
	
	public PropertyType() {
		super();
	}

	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		String resultWithoutParentSegment = ReflectionToStringBuilder.toStringExclude(this, "propertySegment");
		int index = StringUtils.indexOf(resultWithoutParentSegment, ",");
		
		StringBuilder sb = new StringBuilder(resultWithoutParentSegment);
		sb.insert(index + 1, "propertySegmentId=" + getPropertySegment().getPropertySegmentId() + ",");
		
        return sb.toString();
	}

	public Integer getPropertyTypeId() {
		return propertyTypeId;
	}

	public void setPropertyTypeId(Integer propertyTypeId) {
		this.propertyTypeId = propertyTypeId;
	}

	public PropertySegment getPropertySegment() {
		return propertySegment;
	}

	public void setPropertySegment(PropertySegment propertySegment) {
		this.propertySegment = propertySegment;
	}

	public PropertyDescriptionType getPropertyDescriptionType() {
		return propertyDescriptionType;
	}

	public void setPropertyDescriptionType(PropertyDescriptionType propertyDescriptionType) {
		this.propertyDescriptionType = propertyDescriptionType;
	}

	public DateType getRecoveredDateType() {
		return recoveredDateType;
	}

	public void setRecoveredDateType(DateType recoveredDateType) {
		this.recoveredDateType = recoveredDateType;
	}

	public Double getValueOfProperty() {
		return valueOfProperty;
	}

	public void setValueOfProperty(Double valueOfProperty) {
		this.valueOfProperty = valueOfProperty;
	}

	public Date getRecoveredDate() {
		return recoveredDate;
	}

	public void setRecoveredDate(Date recoveredDate) {
		this.recoveredDate = recoveredDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyType other = (PropertyType) obj;
		if (propertyDescriptionType == null) {
			if (other.propertyDescriptionType != null)
				return false;
		} else if (!propertyDescriptionType.equals(other.propertyDescriptionType))
			return false;
		if (propertySegment == null) {
			if (other.propertySegment != null)
				return false;
		} else if (!propertySegment.equals(other.propertySegment))
			return false;
		if (propertyTypeId == null) {
			if (other.propertyTypeId != null)
				return false;
		} else if (!propertyTypeId.equals(other.propertyTypeId))
			return false;
		if (recoveredDate == null) {
			if (other.recoveredDate != null)
				return false;
		} else if (!recoveredDate.equals(other.recoveredDate))
			return false;
		if (recoveredDateType == null) {
			if (other.recoveredDateType != null)
				return false;
		} else if (!recoveredDateType.equals(other.recoveredDateType))
			return false;
		if (valueOfProperty == null) {
			if (other.valueOfProperty != null)
				return false;
		} else if (!valueOfProperty.equals(other.valueOfProperty))
			return false;
		return true;
	}

}
