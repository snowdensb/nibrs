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
package org.search.nibrs.stagingdata.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.AggravatedAssaultHomicideCircumstancesType;
import org.search.nibrs.stagingdata.model.ArresteeSegmentWasArmedWith;
import org.search.nibrs.stagingdata.model.BiasMotivationType;
import org.search.nibrs.stagingdata.model.ClearedExceptionallyType;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.model.LocationType;
import org.search.nibrs.stagingdata.model.MethodOfEntryType;
import org.search.nibrs.stagingdata.model.OffenderSuspectedOfUsingType;
import org.search.nibrs.stagingdata.model.PropertyType;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.SuspectedDrugType;
import org.search.nibrs.stagingdata.model.TypeInjuryType;
import org.search.nibrs.stagingdata.model.TypeOfCriminalActivityType;
import org.search.nibrs.stagingdata.model.TypeOfWeaponForceInvolved;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;
import org.search.nibrs.stagingdata.model.VictimOffenderAssociation;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenderSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.search.nibrs.stagingdata.model.segment.PropertySegment;
import org.search.nibrs.stagingdata.model.segment.VictimSegment;
import org.search.nibrs.stagingdata.repository.AdditionalJustifiableHomicideCircumstancesTypeRepository;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.AggravatedAssaultHomicideCircumstancesTypeRepository;
import org.search.nibrs.stagingdata.repository.ArresteeWasArmedWithTypeRepository;
import org.search.nibrs.stagingdata.repository.BiasMotivationTypeRepository;
import org.search.nibrs.stagingdata.repository.CargoTheftIndicatorTypeRepository;
import org.search.nibrs.stagingdata.repository.ClearedExceptionallyTypeRepository;
import org.search.nibrs.stagingdata.repository.DateTypeRepository;
import org.search.nibrs.stagingdata.repository.DispositionOfArresteeUnder18TypeRepository;
import org.search.nibrs.stagingdata.repository.EthnicityOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.LocationTypeRepository;
import org.search.nibrs.stagingdata.repository.MethodOfEntryTypeRepository;
import org.search.nibrs.stagingdata.repository.MultipleArresteeSegmentsIndicatorTypeRepository;
import org.search.nibrs.stagingdata.repository.OffenderSuspectedOfUsingTypeRepository;
import org.search.nibrs.stagingdata.repository.OfficerActivityCircumstanceTypeRepository;
import org.search.nibrs.stagingdata.repository.OfficerAssignmentTypeTypeRepository;
import org.search.nibrs.stagingdata.repository.PropertyDescriptionTypeRepository;
import org.search.nibrs.stagingdata.repository.RaceOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.ResidentStatusOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeRepository;
import org.search.nibrs.stagingdata.repository.SexOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.SuspectedDrugTypeTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeDrugMeasurementTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeInjuryTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfArrestTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfCriminalActivityTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfVictimTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfWeaponForceInvolvedTypeRepository;
import org.search.nibrs.stagingdata.repository.TypePropertyLossEtcTypeRepository;
import org.search.nibrs.stagingdata.repository.UcrOffenseCodeTypeRepository;
import org.search.nibrs.stagingdata.repository.VictimOffenderRelationshipTypeRepository;
import org.search.nibrs.stagingdata.repository.segment.AdministrativeSegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdministrativeSegmentFactory {
	@Autowired
	public DateTypeRepository dateTypeRepository; 
	@Autowired
	public AgencyRepository agencyRepository; 
	@Autowired
	public SegmentActionTypeRepository segmentActionTypeRepository; 
	@Autowired
	public ClearedExceptionallyTypeRepository clearedExceptionallyTypeRepository; 
	@Autowired
	public UcrOffenseCodeTypeRepository ucrOffenseCodeTypeRepository; 
	@Autowired
	public LocationTypeRepository locationTypeRepository; 
	@Autowired
	public MethodOfEntryTypeRepository methodOfEntryTypeRepository; 
	@Autowired
	public BiasMotivationTypeRepository biasMotivationTypeRepository; 
	@Autowired
	public CargoTheftIndicatorTypeRepository cargoTheftIndicatorTypeRepository; 
	@Autowired
	public TypeOfWeaponForceInvolvedTypeRepository typeOfWeaponForceInvolvedTypeRepository; 
	@Autowired
	public OffenderSuspectedOfUsingTypeRepository offenderSuspectedOfUsingTypeRepository; 
	@Autowired
	public TypeOfCriminalActivityTypeRepository typeOfCriminalActivityTypeRepository; 
	@Autowired
	public TypePropertyLossEtcTypeRepository typePropertyLossEtcTypeRepository; 
	@Autowired
	public TypeDrugMeasurementTypeRepository typeDrugMeasurementTypeRepository; 
	@Autowired
	public PropertyDescriptionTypeRepository propertyDescriptionTypeRepository; 
	@Autowired
	public SuspectedDrugTypeTypeRepository suspectedDrugTypeTypeRepository; 
	@Autowired
	public DispositionOfArresteeUnder18TypeRepository dispositionOfArresteeUnder18TypeRepository; 
	@Autowired
	public EthnicityOfPersonTypeRepository ethnicityOfPersonTypeRepository; 
	@Autowired
	public RaceOfPersonTypeRepository raceOfPersonTypeRepository; 
	@Autowired
	public SexOfPersonTypeRepository sexOfPersonTypeRepository; 
	@Autowired
	public TypeOfArrestTypeRepository typeOfArrestTypeRepository; 
	@Autowired
	public ResidentStatusOfPersonTypeRepository residentStatusOfPersonTypeRepository; 
	@Autowired
	public MultipleArresteeSegmentsIndicatorTypeRepository multipleArresteeSegmentsIndicatorTypeRepository; 
	@Autowired
	public ArresteeWasArmedWithTypeRepository arresteeWasArmedWithTypeRepository; 
	@Autowired
	public TypeOfVictimTypeRepository typeOfVictimTypeRepository; 
	@Autowired
	public OfficerActivityCircumstanceTypeRepository officerActivityCircumstanceTypeRepository; 
	@Autowired
	public OfficerAssignmentTypeTypeRepository officerAssignmentTypeTypeRepository; 
	@Autowired
	public AdditionalJustifiableHomicideCircumstancesTypeRepository additionalJustifiableHomicideCircumstancesTypeRepository; 
	@Autowired
	public TypeInjuryTypeRepository typeInjuryTypeRepository; 
	@Autowired
	public AggravatedAssaultHomicideCircumstancesTypeRepository aggravatedAssaultHomicideCircumstancesTypeRepository; 
	@Autowired
	public VictimOffenderRelationshipTypeRepository victimOffenderRelationshipTypeRepository;
	
	@Autowired
	public AdministrativeSegmentRepository administrativeSegmentRepository; 
	@SuppressWarnings("serial")
	public AdministrativeSegment getBasicAdministrativeSegment(){
		
		AdministrativeSegment administrativeSegment = new AdministrativeSegment();
		administrativeSegment.setReportTimestamp(LocalDateTime.now());
		SegmentActionTypeType segmentActionTypeType = segmentActionTypeRepository.findFirstByStateCode("I");
		administrativeSegment.setSegmentActionType(segmentActionTypeType);
		administrativeSegment.setMonthOfTape("12");
		administrativeSegment.setYearOfTape("2016");
		administrativeSegment.setCityIndicator("Y");
		administrativeSegment.setOri("ori");;
		
		Agency agency = agencyRepository.findFirstByAgencyOri("agencyORI");
		administrativeSegment.setAgency(agency);
		administrativeSegment.setIncidentNumber("1234568910");
		administrativeSegment.setIncidentDate(Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
		DateType incidentDateType = dateTypeRepository.findFirstByDateMMDDYYYY("06122016");
		administrativeSegment.setIncidentDateType(incidentDateType);
		
		administrativeSegment.setExceptionalClearanceDate(Date.from(LocalDateTime.of(2016, 7, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
		DateType exceptionalClearanceDateType = dateTypeRepository.findFirstByDateMMDDYYYY("07122016");
		administrativeSegment.setExceptionalClearanceDateType(exceptionalClearanceDateType);
		
		administrativeSegment.setReportDateIndicator(null);  //'R' for report. Must be empty when incident is known.    
		administrativeSegment.setIncidentHour("13");  // allowed value 0-23.  
		ClearedExceptionallyType clearedExceptionallyType
			= clearedExceptionallyTypeRepository.findFirstByStateCode("B");
		administrativeSegment.setClearedExceptionallyType(clearedExceptionallyType);
		administrativeSegment.setCargoTheftIndicatorType(cargoTheftIndicatorTypeRepository.findFirstByStateCode("N"));
		
		
//		Offense segment 1 
		Set<OffenseSegment> offenseSegments = new HashSet<>();
		OffenseSegment offenseSegment = new OffenseSegment();
		offenseSegment.setSegmentActionType(segmentActionTypeType);
		
		UcrOffenseCodeType ucrOffenseCode = ucrOffenseCodeTypeRepository.findFirstByStateCode("23D");
		offenseSegment.setUcrOffenseCodeType(ucrOffenseCode);
		
		offenseSegment.setOffenseAttemptedCompleted("C");  //Allowed values C or A 
		
		LocationType locationType = locationTypeRepository.findFirstByStateCode("04");
		offenseSegment.setLocationType(locationType);
		
		offenseSegment.setNumberOfPremisesEntered(2);
		
		MethodOfEntryType methodOfEntryType = methodOfEntryTypeRepository.findFirstByStateCode("F");
		offenseSegment.setMethodOfEntryType(methodOfEntryType);
		BiasMotivationType biasMotivationType = biasMotivationTypeRepository.findFirstByStateCode("11");
		offenseSegment.setBiasMotivationTypes(new HashSet<BiasMotivationType>(){{
			add(biasMotivationType);
		}});;
		offenseSegment.setAdministrativeSegment(administrativeSegment);
		
		offenseSegment.setOffenderSuspectedOfUsingTypes(new HashSet<OffenderSuspectedOfUsingType>(){{
			add(offenderSuspectedOfUsingTypeRepository.findOne(1));
			add(offenderSuspectedOfUsingTypeRepository.findOne(2));
		}});
		
		TypeOfWeaponForceInvolved typeOfWeaponForceInvolved1 = new TypeOfWeaponForceInvolved();
		typeOfWeaponForceInvolved1.setOffenseSegment(offenseSegment);
		typeOfWeaponForceInvolved1.setAutomaticWeaponIndicator("A");
		typeOfWeaponForceInvolved1.setTypeOfWeaponForceInvolvedType(typeOfWeaponForceInvolvedTypeRepository.findOne(120));
		
		TypeOfWeaponForceInvolved typeOfWeaponForceInvolved2 = new TypeOfWeaponForceInvolved();
		typeOfWeaponForceInvolved2.setOffenseSegment(offenseSegment);
		typeOfWeaponForceInvolved2.setAutomaticWeaponIndicator("");
		typeOfWeaponForceInvolved2.setTypeOfWeaponForceInvolvedType(typeOfWeaponForceInvolvedTypeRepository.findOne(110));
		
		offenseSegment.setTypeOfWeaponForceInvolveds(new HashSet<TypeOfWeaponForceInvolved>(){{
			add(typeOfWeaponForceInvolved1);
			add(typeOfWeaponForceInvolved2);
		}});
		
		offenseSegments.add(offenseSegment);
//		Offense segment 2 
		OffenseSegment offenseSegment2 = new OffenseSegment();
		offenseSegment2.setSegmentActionType(segmentActionTypeType);
		
		UcrOffenseCodeType ucrOffenseCode2 = ucrOffenseCodeTypeRepository.findFirstByStateCode("26B");
		offenseSegment2.setUcrOffenseCodeType(ucrOffenseCode2);
		
		offenseSegment2.setOffenseAttemptedCompleted("A");  //Allowed values C or A 
		
		LocationType locationType2 = locationTypeRepository.findFirstByStateCode("02");
		offenseSegment2.setLocationType(locationType2);
		
		offenseSegment2.setNumberOfPremisesEntered(1);
		
		MethodOfEntryType methodOfEntryType2 = methodOfEntryTypeRepository.findFirstByStateCode("F");
		offenseSegment2.setMethodOfEntryType(methodOfEntryType2);
		BiasMotivationType biasMotivationType2 = biasMotivationTypeRepository.findFirstByStateCode("12");
		offenseSegment2.setBiasMotivationTypes(new HashSet<BiasMotivationType>(){{
			add(biasMotivationType2);
		}});;
		offenseSegment2.setAdministrativeSegment(administrativeSegment);

		offenseSegment2.setTypeOfCriminalActivityTypes(new HashSet<TypeOfCriminalActivityType>(){{
			add(typeOfCriminalActivityTypeRepository.findOne(3));
			add(typeOfCriminalActivityTypeRepository.findOne(4));
		}});

		offenseSegments.add(offenseSegment2);
		administrativeSegment.setOffenseSegments(offenseSegments);
		
//		PropertySegment 1
		PropertySegment propertySegment1 = new PropertySegment();
		propertySegment1.setSegmentActionType(segmentActionTypeType);
		propertySegment1.setAdministrativeSegment(administrativeSegment);
		propertySegment1.setTypePropertyLossEtcType(typePropertyLossEtcTypeRepository.findOne(7));
		propertySegment1.setNumberOfRecoveredMotorVehicles(0);
		propertySegment1.setNumberOfStolenMotorVehicles(1);
		
		PropertyType propertyType1 = new PropertyType();
		propertyType1.setPropertySegment(propertySegment1);
		propertyType1.setPropertyDescriptionType(propertyDescriptionTypeRepository.findFirstByStateCode("03"));
		propertyType1.setValueOfProperty(10000.0);
		propertyType1.setRecoveredDateType(dateTypeRepository.findOne(99998));
		
		propertySegment1.setPropertyTypes(new LinkedHashSet<PropertyType>(){{
			add(propertyType1);
		}});
		
		Set<PropertySegment> propertySegments = new LinkedHashSet<>();
		propertySegments.add(propertySegment1);
		
//		PropertySegment 2
		PropertySegment propertySegment2 = new PropertySegment();
		propertySegment2.setSegmentActionType(segmentActionTypeType);
		propertySegment2.setAdministrativeSegment(administrativeSegment);
		propertySegment2.setTypePropertyLossEtcType(typePropertyLossEtcTypeRepository.findOne(4));
		propertySegment2.setNumberOfRecoveredMotorVehicles(0);
		propertySegment2.setNumberOfStolenMotorVehicles(0);
		
		SuspectedDrugType suspectedDrugType1 = new SuspectedDrugType();
		suspectedDrugType1.setPropertySegment(propertySegment2);
		suspectedDrugType1.setSuspectedDrugTypeType(suspectedDrugTypeTypeRepository.findFirstByStateCode("A"));
		suspectedDrugType1.setEstimatedDrugQuantity(1.0);
		suspectedDrugType1.setTypeDrugMeasurementType(typeDrugMeasurementTypeRepository.findOne(1));
		
		SuspectedDrugType suspectedDrugType2 = new SuspectedDrugType();
		suspectedDrugType2.setPropertySegment(propertySegment2);
		suspectedDrugType2.setSuspectedDrugTypeType(suspectedDrugTypeTypeRepository.findFirstByStateCode("D"));
		suspectedDrugType2.setEstimatedDrugQuantity(1.0);
		suspectedDrugType2.setTypeDrugMeasurementType(typeDrugMeasurementTypeRepository.findFirstByStateCode("OZ"));
		
		propertySegment2.setSuspectedDrugTypes(new LinkedHashSet<SuspectedDrugType>(){{
			add(suspectedDrugType1);
			add(suspectedDrugType2);
		}});
		
		propertySegments.add(propertySegment2);
		
		administrativeSegment.setPropertySegments(propertySegments);
		
		
//		1 arrestee segment 
//		ArresteeSegment[arresteeSequenceNumber=01,arrestTransactionNumber=201040889,arrestDate=Tue Aug 23 00:00:00 CDT 2016,
		//typeOfArrest=S,multipleArresteeSegmentsIndicator=N,ucrArrestOffenseCode=23D,arresteeArmedWith={01,<null>},
		//automaticWeaponIndicator={<null>,<null>},residentStatus=U,dispositionOfArresteeUnder18=<null>,
		//age=33,sex=M,race=B,ethnicity=U
		
		ArresteeSegment arresteeSegment = new ArresteeSegment();
		arresteeSegment.setAdministrativeSegment(administrativeSegment);
		arresteeSegment.setAgeOfArresteeMax(33);
		arresteeSegment.setAgeOfArresteeMin(33);
		arresteeSegment.setArrestDate(Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
		
		DateType arrestDateType = dateTypeRepository.findFirstByDateMMDDYYYY("06122016");
		arresteeSegment.setArrestDateType(arrestDateType);
		
		arresteeSegment.setArresteeSequenceNumber(1);
		arresteeSegment.setArrestTransactionNumber("201040889");
		
		arresteeSegment.setDispositionOfArresteeUnder18Type(
				dispositionOfArresteeUnder18TypeRepository.findFirstByStateCode("9"));
		arresteeSegment.setEthnicityOfPersonType(ethnicityOfPersonTypeRepository.findFirstByStateCode("U"));
		arresteeSegment.setRaceOfPersonType(raceOfPersonTypeRepository.findFirstByStateCode("B"));
		arresteeSegment.setResidentStatusOfPersonType(residentStatusOfPersonTypeRepository.findFirstByStateCode("U"));
		arresteeSegment.setSegmentActionType(segmentActionTypeRepository.findFirstByStateCode("I"));
		arresteeSegment.setSexOfPersonType(sexOfPersonTypeRepository.findFirstByStateCode("M"));
		arresteeSegment.setTypeOfArrestType(typeOfArrestTypeRepository.findFirstByStateCode("S"));
		arresteeSegment.setUcrOffenseCodeType(ucrOffenseCodeTypeRepository.findFirstByStateCode("23D"));
		arresteeSegment.setMultipleArresteeSegmentsIndicatorType(
				multipleArresteeSegmentsIndicatorTypeRepository.findFirstByStateCode("N"));
		
		ArresteeSegmentWasArmedWith arresteeSegmentWasArmedWith1 = new ArresteeSegmentWasArmedWith();
		arresteeSegmentWasArmedWith1.setArresteeSegment(arresteeSegment);
		arresteeSegmentWasArmedWith1.setAutomaticWeaponIndicator("A");
		arresteeSegmentWasArmedWith1.setArresteeWasArmedWithType(arresteeWasArmedWithTypeRepository.findOne(120));
		
		ArresteeSegmentWasArmedWith arresteeSegmentWasArmedWith2 = new ArresteeSegmentWasArmedWith();
		arresteeSegmentWasArmedWith2.setArresteeSegment(arresteeSegment);
		arresteeSegmentWasArmedWith2.setAutomaticWeaponIndicator("");
		arresteeSegmentWasArmedWith2.setArresteeWasArmedWithType(arresteeWasArmedWithTypeRepository.findOne(110));
		
		arresteeSegment.setArresteeSegmentWasArmedWiths(new HashSet<ArresteeSegmentWasArmedWith>(){{
			add(arresteeSegmentWasArmedWith1);
			add(arresteeSegmentWasArmedWith2);
		}});
		
		administrativeSegment.setArresteeSegments(new HashSet<ArresteeSegment>(){{
			add(arresteeSegment);
		}});

//		2 OffenderSegment Segments:
//		OffenderSegment [age=00, sex=M, race=B, ethnicity=null, offenderSequenceNumber=01]
//		OffenderSegment [age=33, sex=M, race=B, ethnicity=null, offenderSequenceNumber=02]
		OffenderSegment offenderSegment1 = new OffenderSegment();
		offenderSegment1.setAdministrativeSegment(administrativeSegment);
		offenderSegment1.setSegmentActionType(segmentActionTypeType);
		offenderSegment1.setAgeOfOffenderMax(0);
		offenderSegment1.setAgeOfOffenderMin(0);
		
		offenderSegment1.setSexOfPersonType(sexOfPersonTypeRepository.findFirstByStateCode("M"));
		offenderSegment1.setRaceOfPersonType(raceOfPersonTypeRepository.findFirstByStateCode("B"));
		offenderSegment1.setEthnicityOfPersonType(ethnicityOfPersonTypeRepository.findFirstByStateCode("U"));
		offenderSegment1.setOffenderSequenceNumber(1);

		OffenderSegment offenderSegment2 = new OffenderSegment();
		offenderSegment2.setAdministrativeSegment(administrativeSegment);
		offenderSegment2.setSegmentActionType(segmentActionTypeType);
		offenderSegment2.setAgeOfOffenderMax(33);
		offenderSegment2.setAgeOfOffenderMin(33);
		
		offenderSegment2.setSexOfPersonType(sexOfPersonTypeRepository.findFirstByStateCode("M"));
		offenderSegment2.setRaceOfPersonType(raceOfPersonTypeRepository.findFirstByStateCode("B"));
		offenderSegment2.setEthnicityOfPersonType(ethnicityOfPersonTypeRepository.findFirstByStateCode("U"));
		offenderSegment2.setOffenderSequenceNumber(2);
		
		administrativeSegment.setOffenderSegments(new HashSet<OffenderSegment>(){{
			add(offenderSegment1);
			add(offenderSegment2);
		}});
		
//		1 VictimSegment Segments:
//		VictimSegment [age=null, sex=null, race=null, ethnicity=null, victimSequenceNumber=01, 
//		ucrOffenseCodeConnection=[26B, 23D, null, null, null, null, null, null, null, null], 
//		typeOfVictim=B, residentStatus=null, aggravatedAssaultHomicideCircumstances=[null, null], 
//		additionalJustifiableHomicideCircumstances=null, typeOfInjury=[null, null, null, null, null], 
//		offenderNumberRelated=[null, null, null, null, null, null, null, null, null, null], 
//		victimOffenderRelationship=[null, null, null, null, null, null, null, null, null, null], 
//		typeOfOfficerActivityCircumstance=null, officerAssignmentType=null, officerOtherJurisdictionORI=null, 
//		populatedAggravatedAssaultHomicideCircumstancesCount=2, populatedTypeOfInjuryCount=5, 
//		populatedUcrOffenseCodeConnectionCount=10, populatedOffenderNumberRelatedCount=0]
		VictimSegment victimSegment = new VictimSegment();
		victimSegment.setAdministrativeSegment(administrativeSegment);
		victimSegment.setSegmentActionType(segmentActionTypeType);
		victimSegment.setVictimSequenceNumber(1);
		victimSegment.setTypeOfVictimType(typeOfVictimTypeRepository.findFirstByStateCode("L"));
		victimSegment.setOfficerActivityCircumstanceType(officerActivityCircumstanceTypeRepository.findFirstByStateCode("01"));
		victimSegment.setOfficerAssignmentTypeType(officerAssignmentTypeTypeRepository.findFirstByStateCode("F"));
		victimSegment.setOfficerOtherJurisdictionOri("other1234");;
		
		victimSegment.setAgeNeonateIndicator(0);
		victimSegment.setAgeFirstWeekIndicator(0);
		victimSegment.setAgeFirstYearIndicator(0);
		victimSegment.setNonNumericAge("00");
		
		victimSegment.setSexOfPersonType(sexOfPersonTypeRepository.findFirstByStateCode("U"));
		victimSegment.setRaceOfPersonType(raceOfPersonTypeRepository.findFirstByStateCode("U"));
		victimSegment.setEthnicityOfPersonType(ethnicityOfPersonTypeRepository.findFirstByStateCode("U"));
		victimSegment.setResidentStatusOfPersonType(residentStatusOfPersonTypeRepository.findFirstByStateCode("U"));
		
		victimSegment.setAdditionalJustifiableHomicideCircumstancesType(
				additionalJustifiableHomicideCircumstancesTypeRepository.findFirstByStateCode("9"));
		
		
		victimSegment.setTypeInjuryTypes(new HashSet<TypeInjuryType>(){{
			add(typeInjuryTypeRepository.findFirstByStateCode("O"));
		}});

		victimSegment.setOffenseSegments(new HashSet<OffenseSegment>(){{
			add(offenseSegment);
			add(offenseSegment2);
		}});
		
		victimSegment.setAggravatedAssaultHomicideCircumstancesTypes(new HashSet<AggravatedAssaultHomicideCircumstancesType>(){{
			add(aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByStateCode("01"));
			add(aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByStateCode("02"));
		}});
		
		VictimOffenderAssociation victimOffenderAssociation1 = new VictimOffenderAssociation();
		victimOffenderAssociation1.setVictimSegment(victimSegment);
		victimOffenderAssociation1.setOffenderSegment(offenderSegment1);
		victimOffenderAssociation1.setVictimOffenderRelationshipType(
				victimOffenderRelationshipTypeRepository.findFirstByStateCode("AQ"));;
		
		victimSegment.setVictimOffenderAssociations(new HashSet<VictimOffenderAssociation>(){{
			add(victimOffenderAssociation1);
		}});

		administrativeSegment.setVictimSegments(new HashSet<VictimSegment>(){{
			add(victimSegment);
		}});
		
		return administrativeSegment; 
	}

}
