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
package org.search.nibrs.stagingdata.model.segment;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.search.nibrs.stagingdata.model.ArresteeSegmentWasArmedWith;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.model.DispositionOfArresteeUnder18Type;
import org.search.nibrs.stagingdata.model.EthnicityOfPersonType;
import org.search.nibrs.stagingdata.model.MultipleArresteeSegmentsIndicatorType;
import org.search.nibrs.stagingdata.model.RaceOfPersonType;
import org.search.nibrs.stagingdata.model.ResidentStatusOfPersonType;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.SexOfPersonType;
import org.search.nibrs.stagingdata.model.TypeOfArrestType;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(
	generator = ObjectIdGenerators.PropertyGenerator.class, 
	property = "arresteeSegmentId")
public class ArresteeSegment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer arresteeSegmentId;
	
	@ManyToOne
	@JoinColumn(name="segmentActionTypeTypeID") 
	private SegmentActionTypeType segmentActionType; 
	@ManyToOne
	@JoinColumn(name="administrativeSegmentId") 
	private AdministrativeSegment administrativeSegment; 
	private Integer arresteeSequenceNumber; 
	private String arrestTransactionNumber; 
	private LocalDate arrestDate;
	
	@ManyToOne
	@JoinColumn(name="arrestDateId")
	private DateType arrestDateType; 
	
	@ManyToOne
	@JoinColumn(name="typeOfArrestTypeId") 
	private TypeOfArrestType typeOfArrestType;
	
	@ManyToOne
	@JoinColumn(name="multipleArresteeSegmentsIndicatorTypeId") 
	private MultipleArresteeSegmentsIndicatorType multipleArresteeSegmentsIndicatorType;
	
	private Integer ageOfArresteeMin; 
	private Integer ageOfArresteeMax;
	private String nonNumericAge;

	@ManyToOne
	@JoinColumn(name="sexOfPersonTypeId") 
	private SexOfPersonType sexOfPersonType; 
	@ManyToOne
	@JoinColumn(name="raceOfPersonTypeId") 
	private RaceOfPersonType raceOfPersonType;
	@ManyToOne
	@JoinColumn(name="ethnicityOfPersonTypeId") 
	private EthnicityOfPersonType ethnicityOfPersonType;
	@ManyToOne
	@JoinColumn(name="residentStatusOfPersonTypeId") 
	private ResidentStatusOfPersonType residentStatusOfPersonType;
	@ManyToOne
	@JoinColumn(name="dispositionOfArresteeUnder18TypeId") 
	private DispositionOfArresteeUnder18Type dispositionOfArresteeUnder18Type;
	
	@ManyToOne
	@JoinColumn(name="ucrOffenseCodeTypeId")
	private UcrOffenseCodeType ucrOffenseCodeType;
	
	@OneToMany(mappedBy = "arresteeSegment", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, orphanRemoval = true)
	private Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWiths;
	
	public String getArrestTransactionNumber() {
		return arrestTransactionNumber;
	}
	public void setArrestTransactionNumber(String arrestTransactionNumber) {
		this.arrestTransactionNumber = arrestTransactionNumber;
	}
	public Integer getArresteeSequenceNumber() {
		return arresteeSequenceNumber;
	}
	public void setArresteeSequenceNumber(Integer arresteeSequenceNumber) {
		this.arresteeSequenceNumber = arresteeSequenceNumber;
	}
	public LocalDate getArrestDate() {
		return arrestDate;
	}
	public void setArrestDate(LocalDate arrestDate) {
		this.arrestDate = arrestDate;
	}
	public Integer getAgeOfArresteeMin() {
		return ageOfArresteeMin;
	}
	public void setAgeOfArresteeMin(Integer ageOfArresteeMin) {
		this.ageOfArresteeMin = ageOfArresteeMin;
	}
	public Integer getAgeOfArresteeMax() {
		return ageOfArresteeMax;
	}
	public void setAgeOfArresteeMax(Integer ageOfArresteeMax) {
		this.ageOfArresteeMax = ageOfArresteeMax;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	public UcrOffenseCodeType getUcrOffenseCodeType() {
		return ucrOffenseCodeType;
	}
	public void setUcrOffenseCodeType(UcrOffenseCodeType ucrOffenseCodeType) {
		this.ucrOffenseCodeType = ucrOffenseCodeType;
	}
	public DateType getArrestDateType() {
		return arrestDateType;
	}
	public void setArrestDateType(DateType arrestDateType) {
		this.arrestDateType = arrestDateType;
	}
	public DispositionOfArresteeUnder18Type getDispositionOfArresteeUnder18Type() {
		return dispositionOfArresteeUnder18Type;
	}
	public void setDispositionOfArresteeUnder18Type(DispositionOfArresteeUnder18Type dispositionOfArresteeUnder18Type) {
		this.dispositionOfArresteeUnder18Type = dispositionOfArresteeUnder18Type;
	}
	public EthnicityOfPersonType getEthnicityOfPersonType() {
		return ethnicityOfPersonType;
	}
	public void setEthnicityOfPersonType(EthnicityOfPersonType ethnicityOfPersonType) {
		this.ethnicityOfPersonType = ethnicityOfPersonType;
	}
	public RaceOfPersonType getRaceOfPersonType() {
		return raceOfPersonType;
	}
	public void setRaceOfPersonType(RaceOfPersonType raceOfPersonType) {
		this.raceOfPersonType = raceOfPersonType;
	}
	public SexOfPersonType getSexOfPersonType() {
		return sexOfPersonType;
	}
	public void setSexOfPersonType(SexOfPersonType sexOfPersonType) {
		this.sexOfPersonType = sexOfPersonType;
	}
	public TypeOfArrestType getTypeOfArrestType() {
		return typeOfArrestType;
	}
	public void setTypeOfArrestType(TypeOfArrestType typeOfArrestType) {
		this.typeOfArrestType = typeOfArrestType;
	}
	public SegmentActionTypeType getSegmentActionType() {
		return segmentActionType;
	}
	public void setSegmentActionType(SegmentActionTypeType segmentActionType) {
		this.segmentActionType = segmentActionType;
	}
	public ResidentStatusOfPersonType getResidentStatusOfPersonType() {
		return residentStatusOfPersonType;
	}
	public void setResidentStatusOfPersonType(ResidentStatusOfPersonType residentStatusOfPersonType) {
		this.residentStatusOfPersonType = residentStatusOfPersonType;
	}
	public AdministrativeSegment getAdministrativeSegment() {
		return administrativeSegment;
	}
	public void setAdministrativeSegment(AdministrativeSegment administrativeSegment) {
		this.administrativeSegment = administrativeSegment;
	}
	public MultipleArresteeSegmentsIndicatorType getMultipleArresteeSegmentsIndicatorType() {
		return multipleArresteeSegmentsIndicatorType;
	}
	public void setMultipleArresteeSegmentsIndicatorType(MultipleArresteeSegmentsIndicatorType multipleArresteeSegmentsIndicatorType) {
		this.multipleArresteeSegmentsIndicatorType = multipleArresteeSegmentsIndicatorType;
	}
	public Integer getArresteeSegmentId() {
		return arresteeSegmentId;
	}
	public void setArresteeSegmentId(Integer arresteeSegmentId) {
		this.arresteeSegmentId = arresteeSegmentId;
	}
	public Set<ArresteeSegmentWasArmedWith> getArresteeSegmentWasArmedWiths() {
		return arresteeSegmentWasArmedWiths;
	}
	public void setArresteeSegmentWasArmedWiths(Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWiths) {
		this.arresteeSegmentWasArmedWiths = arresteeSegmentWasArmedWiths;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((administrativeSegment == null) ? 0 : administrativeSegment.hashCode());
		result = prime * result + ((ageOfArresteeMax == null) ? 0 : ageOfArresteeMax.hashCode());
		result = prime * result + ((ageOfArresteeMin == null) ? 0 : ageOfArresteeMin.hashCode());
		result = prime * result + Objects.hashCode(nonNumericAge);
		result = prime * result + ((arrestDate == null) ? 0 : arrestDate.hashCode());
		result = prime * result + ((arrestDateType == null) ? 0 : arrestDateType.hashCode());
		result = prime * result + ((arrestTransactionNumber == null) ? 0 : arrestTransactionNumber.hashCode());
		result = prime * result + ((arresteeSegmentId == null) ? 0 : arresteeSegmentId.hashCode());
		result = prime * result
				+ ((arresteeSegmentWasArmedWiths == null) ? 0 : arresteeSegmentWasArmedWiths.hashCode());
		result = prime * result + ((arresteeSequenceNumber == null) ? 0 : arresteeSequenceNumber.hashCode());
		result = prime * result
				+ ((dispositionOfArresteeUnder18Type == null) ? 0 : dispositionOfArresteeUnder18Type.hashCode());
		result = prime * result + ((ethnicityOfPersonType == null) ? 0 : ethnicityOfPersonType.hashCode());
		result = prime * result + ((multipleArresteeSegmentsIndicatorType == null) ? 0
				: multipleArresteeSegmentsIndicatorType.hashCode());
		result = prime * result + ((raceOfPersonType == null) ? 0 : raceOfPersonType.hashCode());
		result = prime * result + ((residentStatusOfPersonType == null) ? 0 : residentStatusOfPersonType.hashCode());
		result = prime * result + ((segmentActionType == null) ? 0 : segmentActionType.hashCode());
		result = prime * result + ((sexOfPersonType == null) ? 0 : sexOfPersonType.hashCode());
		result = prime * result + ((typeOfArrestType == null) ? 0 : typeOfArrestType.hashCode());
		result = prime * result + ((ucrOffenseCodeType == null) ? 0 : ucrOffenseCodeType.hashCode());
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
		ArresteeSegment other = (ArresteeSegment) obj;
		if (administrativeSegment == null) {
			if (other.administrativeSegment != null)
				return false;
		} else if (!administrativeSegment.equals(other.administrativeSegment))
			return false;
		if (ageOfArresteeMax == null) {
			if (other.ageOfArresteeMax != null)
				return false;
		} else if (!ageOfArresteeMax.equals(other.ageOfArresteeMax))
			return false;
		if (ageOfArresteeMin == null) {
			if (other.ageOfArresteeMin != null)
				return false;
		} else if (!ageOfArresteeMin.equals(other.ageOfArresteeMin))
			return false;
		if (arrestDate == null) {
			if (other.arrestDate != null)
				return false;
		} else if (!arrestDate.equals(other.arrestDate))
			return false;
		if (arrestDateType == null) {
			if (other.arrestDateType != null)
				return false;
		} else if (!arrestDateType.equals(other.arrestDateType))
			return false;
		if (arrestTransactionNumber == null) {
			if (other.arrestTransactionNumber != null)
				return false;
		} else if (!arrestTransactionNumber.equals(other.arrestTransactionNumber))
			return false;
		if (arresteeSegmentId == null) {
			if (other.arresteeSegmentId != null)
				return false;
		} else if (!arresteeSegmentId.equals(other.arresteeSegmentId))
			return false;
		if (arresteeSegmentWasArmedWiths == null) {
			if (other.arresteeSegmentWasArmedWiths != null)
				return false;
		} else if (!arresteeSegmentWasArmedWiths.equals(other.arresteeSegmentWasArmedWiths))
			return false;
		if (arresteeSequenceNumber == null) {
			if (other.arresteeSequenceNumber != null)
				return false;
		} else if (!arresteeSequenceNumber.equals(other.arresteeSequenceNumber))
			return false;
		if (dispositionOfArresteeUnder18Type == null) {
			if (other.dispositionOfArresteeUnder18Type != null)
				return false;
		} else if (!dispositionOfArresteeUnder18Type.equals(other.dispositionOfArresteeUnder18Type))
			return false;
		if (ethnicityOfPersonType == null) {
			if (other.ethnicityOfPersonType != null)
				return false;
		} else if (!ethnicityOfPersonType.equals(other.ethnicityOfPersonType))
			return false;
		if (multipleArresteeSegmentsIndicatorType == null) {
			if (other.multipleArresteeSegmentsIndicatorType != null)
				return false;
		} else if (!multipleArresteeSegmentsIndicatorType.equals(other.multipleArresteeSegmentsIndicatorType))
			return false;
		if (raceOfPersonType == null) {
			if (other.raceOfPersonType != null)
				return false;
		} else if (!raceOfPersonType.equals(other.raceOfPersonType))
			return false;
		if (residentStatusOfPersonType == null) {
			if (other.residentStatusOfPersonType != null)
				return false;
		} else if (!residentStatusOfPersonType.equals(other.residentStatusOfPersonType))
			return false;
		if (segmentActionType == null) {
			if (other.segmentActionType != null)
				return false;
		} else if (!segmentActionType.equals(other.segmentActionType))
			return false;
		if (sexOfPersonType == null) {
			if (other.sexOfPersonType != null)
				return false;
		} else if (!sexOfPersonType.equals(other.sexOfPersonType))
			return false;
		if (typeOfArrestType == null) {
			if (other.typeOfArrestType != null)
				return false;
		} else if (!typeOfArrestType.equals(other.typeOfArrestType))
			return false;
		if (ucrOffenseCodeType == null) {
			if (other.ucrOffenseCodeType != null)
				return false;
		} else if (!ucrOffenseCodeType.equals(other.ucrOffenseCodeType))
			return false;
		
		if (!Objects.equals(nonNumericAge, other.getNonNumericAge())){
			return false; 
		}
		return true;
	}
	
    public boolean isJuvenile() {
    	boolean ret = false; 
    	if ( !isAgeUnknown() ) {
    		ret = ageOfArresteeMax < 18 || (ageOfArresteeMin < 18 && getAverageAge() < 18);
    	}
    	return ret;
    }
    
    public boolean isAdult() {
    	boolean ret = false; 
    	if ( !isAgeUnknown() ) {
    		ret = ageOfArresteeMin >= 18 || (ageOfArresteeMax >= 18 && getAverageAge() >= 18);
    	}
    	return ret;
    }
    
	public Integer getAverageAge() {
		Integer ret = null;
		if (!isAgeUnknown()) {
			double min = ageOfArresteeMin.doubleValue();
			double max = ageOfArresteeMax.doubleValue();
			double average = (min + max) / 2.0;
			ret = new Integer((int) average);
		}
		return ret;
	}

    public boolean isAgeUnknown() {
    	// set forth in rule for data element 52
    	return ageOfArresteeMax == null && ageOfArresteeMin == null && Objects.equals(nonNumericAge, "00");
    }
	public String getNonNumericAge() {
		return nonNumericAge;
	}
	public void setNonNumericAge(String nonNumericAge) {
		this.nonNumericAge = nonNumericAge;
	}

}
