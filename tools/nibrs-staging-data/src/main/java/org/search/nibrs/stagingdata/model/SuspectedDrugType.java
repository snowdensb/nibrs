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
import org.search.nibrs.stagingdata.model.segment.PropertySegment;

@Entity
public class SuspectedDrugType implements Serializable{

	private static final long serialVersionUID = -2370698406499145556L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer suspectedDrugTypeId; 
	
    @ManyToOne
    @JoinColumn(name = "propertySegmentId")
	private PropertySegment propertySegment; 
    
    @ManyToOne
    @JoinColumn(name = "suspectedDrugTypeTypeId")
	private SuspectedDrugTypeType suspectedDrugTypeType;
    
    @ManyToOne
    @JoinColumn(name = "typeDrugMeasurementTypeId")
    private TypeDrugMeasurementType typeDrugMeasurementType;
    
	private Double estimatedDrugQuantity; 
	
	public String toString(){
		ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		String resultWithoutParentSegment = ReflectionToStringBuilder.toStringExclude(this, "propertySegment");
		int index = StringUtils.indexOf(resultWithoutParentSegment, ",");
		
		StringBuilder sb = new StringBuilder(resultWithoutParentSegment);
		sb.insert(index + 1, "propertySegmentId=" + getPropertySegment().getPropertySegmentId() + ",");
		
        return sb.toString();
	}

	public Integer getSuspectedDrugTypeId() {
		return suspectedDrugTypeId;
	}

	public void setSuspectedDrugTypeId(Integer suspectedDrugTypeId) {
		this.suspectedDrugTypeId = suspectedDrugTypeId;
	}

	public SuspectedDrugTypeType getSuspectedDrugTypeType() {
		return suspectedDrugTypeType;
	}

	public void setSuspectedDrugTypeType(SuspectedDrugTypeType suspectedDrugTypeType) {
		this.suspectedDrugTypeType = suspectedDrugTypeType;
	}

	public TypeDrugMeasurementType getTypeDrugMeasurementType() {
		return typeDrugMeasurementType;
	}

	public void setTypeDrugMeasurementType(TypeDrugMeasurementType typeDrugMeasurementType) {
		this.typeDrugMeasurementType = typeDrugMeasurementType;
	}

	public Double getEstimatedDrugQuantity() {
		return estimatedDrugQuantity;
	}

	public void setEstimatedDrugQuantity(Double estimatedDrugQuantity) {
		this.estimatedDrugQuantity = estimatedDrugQuantity;
	}

	public PropertySegment getPropertySegment() {
		return propertySegment;
	}

	public void setPropertySegment(PropertySegment propertySegment) {
		this.propertySegment = propertySegment;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SuspectedDrugType other = (SuspectedDrugType) obj;
		if (estimatedDrugQuantity == null) {
			if (other.estimatedDrugQuantity != null)
				return false;
		} else if (!estimatedDrugQuantity.equals(other.estimatedDrugQuantity))
			return false;
		if (propertySegment == null) {
			if (other.propertySegment != null)
				return false;
		} else if (!propertySegment.equals(other.propertySegment))
			return false;
		if (suspectedDrugTypeId == null) {
			if (other.suspectedDrugTypeId != null)
				return false;
		} else if (!suspectedDrugTypeId.equals(other.suspectedDrugTypeId))
			return false;
		if (suspectedDrugTypeType == null) {
			if (other.suspectedDrugTypeType != null)
				return false;
		} else if (!suspectedDrugTypeType.equals(other.suspectedDrugTypeType))
			return false;
		if (typeDrugMeasurementType == null) {
			if (other.typeDrugMeasurementType != null)
				return false;
		} else if (!typeDrugMeasurementType.equals(other.typeDrugMeasurementType))
			return false;
		return true;
	}

}
