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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.common.ParsedObject;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.ArresteeSegmentWasArmedWith;
import org.search.nibrs.stagingdata.model.OffenderSuspectedOfUsingType;
import org.search.nibrs.stagingdata.model.PropertyType;
import org.search.nibrs.stagingdata.model.SuspectedDrugType;
import org.search.nibrs.stagingdata.model.TypeOfCriminalActivityType;
import org.search.nibrs.stagingdata.model.TypeOfWeaponForceInvolved;
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
import org.search.nibrs.stagingdata.util.BaselineIncidentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(methodMode=MethodMode.BEFORE_METHOD)
public class GroupAIncidentServiceTest {
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
	@Autowired
	public GroupAIncidentService groupAIncidentService;
	
	@Autowired
	public AdministrativeSegmentFactory administrativeSegmentFactory; 

	@Test
	public void test() {
		AdministrativeSegment administrativeSegment = administrativeSegmentFactory.getBasicAdministrativeSegment();
		groupAIncidentService.saveAdministrativeSegment(administrativeSegment); 
		
		AdministrativeSegment persisted = 
				groupAIncidentService.findAdministrativeSegment(administrativeSegment.getAdministrativeSegmentId());
		assertThat(persisted.getSegmentActionType().getStateCode(), equalTo("I"));
		assertThat(persisted.getMonthOfTape(), equalTo("12"));
		assertThat(persisted.getYearOfTape(), equalTo("2016"));
		assertThat(persisted.getCityIndicator(), equalTo("Y"));
		assertThat(persisted.getOri(), equalTo("ori"));
		assertThat(persisted.getStateCode(), equalTo("NA"));
		assertThat(persisted.getAgency().getAgencyOri(), equalTo("agencyORI"));
		assertThat(persisted.getIncidentNumber(), equalTo("1234568910"));
		assertThat(persisted.getIncidentDate(), equalTo(LocalDate.of(2016, 6, 12)));
		assertThat(persisted.getIncidentDateType().getDateTypeId(), equalTo(2355));
		assertTrue(DateUtils.isSameDay(persisted.getExceptionalClearanceDate(), Date.from(LocalDateTime.of(2016, 7, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
		assertThat(persisted.getExceptionalClearanceDateType().getDateMMDDYYYY(), equalTo("07122016"));
		assertNull(persisted.getReportDateIndicator());
		assertThat(persisted.getIncidentHour(), equalTo("13"));
		assertThat(persisted.getClearedExceptionallyType().getStateCode(), equalTo("B"));
		
		Set<OffenseSegment> offenseSegments = persisted.getOffenseSegments();
		assertThat(offenseSegments.size(), equalTo(2));
		
		for (OffenseSegment offenseSegment: offenseSegments){
			if (offenseSegment.getBiasMotivationTypes().contains(biasMotivationTypeRepository.findFirstByStateCode("11"))){
				assertThat(offenseSegment.getSegmentActionType().getStateCode(), equalTo("I"));
				assertThat(offenseSegment.getUcrOffenseCodeType().getStateCode(), equalTo("23D"));
				assertThat(offenseSegment.getOffenseAttemptedCompleted(), equalTo("C"));
				assertThat(offenseSegment.getLocationType().getStateCode(), equalTo("04"));
				assertThat(offenseSegment.getNumberOfPremisesEntered(), equalTo(2));
				assertThat(offenseSegment.getMethodOfEntryType().getStateCode(), equalTo("F"));
				
				Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes = 
						offenseSegment.getOffenderSuspectedOfUsingTypes();  
				assertThat(offenderSuspectedOfUsingTypes.size(), equalTo(2));
				assertTrue(offenderSuspectedOfUsingTypes.contains(offenderSuspectedOfUsingTypeRepository.findById(1).get()));
				assertTrue(offenderSuspectedOfUsingTypes.contains(offenderSuspectedOfUsingTypeRepository.findById(2).get()));
				
				Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes = 
						offenseSegment.getTypeOfCriminalActivityTypes(); 
				assertThat(typeOfCriminalActivityTypes.size(), equalTo(0));

				Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds = 
						offenseSegment.getTypeOfWeaponForceInvolveds();
				assertThat(typeOfWeaponForceInvolveds.size(), equalTo(2));
				
				for (TypeOfWeaponForceInvolved typeOfWeaponForceInvolved: typeOfWeaponForceInvolveds){
					assertThat(typeOfWeaponForceInvolved.getOffenseSegment().getOffenseSegmentId(), equalTo(offenseSegment.getOffenseSegmentId()));
					if (typeOfWeaponForceInvolved.getAutomaticWeaponIndicator().equals("A")){
						assertThat(typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getTypeOfWeaponForceInvolvedTypeId(), equalTo(120));
					}
					else{
						assertThat(typeOfWeaponForceInvolved.getAutomaticWeaponIndicator(), equalTo(""));
						assertThat(typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getTypeOfWeaponForceInvolvedTypeId(), equalTo(110));
					}
				}
				
			}
			else {
				assertThat(offenseSegment.getBiasMotivationTypes().stream().findFirst().get().getStateCode(), equalTo("12"));
				assertThat(offenseSegment.getSegmentActionType().getStateCode(), equalTo("I"));
				assertThat(offenseSegment.getUcrOffenseCodeType().getStateCode(), equalTo("26B"));
				assertThat(offenseSegment.getOffenseAttemptedCompleted(), equalTo("A"));
				assertThat(offenseSegment.getLocationType().getStateCode(), equalTo("02"));
				assertThat(offenseSegment.getNumberOfPremisesEntered(), equalTo(1));
				assertThat(offenseSegment.getMethodOfEntryType().getStateCode(), equalTo("F"));
				Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes = 
						offenseSegment.getOffenderSuspectedOfUsingTypes();  
				assertThat(offenderSuspectedOfUsingTypes.size(), equalTo(0));
				
				Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes = 
						offenseSegment.getTypeOfCriminalActivityTypes(); 
				assertThat(typeOfCriminalActivityTypes.size(), equalTo(2));
				assertTrue(typeOfCriminalActivityTypes.contains(typeOfCriminalActivityTypeRepository.findById(3).orElse(null)));
				assertTrue(typeOfCriminalActivityTypes.contains(typeOfCriminalActivityTypeRepository.findById(4).orElse(null)));
				
				Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds = 
						offenseSegment.getTypeOfWeaponForceInvolveds();
				assertThat(typeOfWeaponForceInvolveds.size(), equalTo(0));
			}
		}
		
		Set<PropertySegment> propertySegments = persisted.getPropertySegments();
		assertThat(propertySegments.size(), equalTo(2));
		
		List<String> typeOfPropertyLossCodes = propertySegments.stream()
				.map(i->i.getTypePropertyLossEtcType().getStateCode())
				.collect(Collectors.toList()); 
		Collections.sort(typeOfPropertyLossCodes);
		assertThat(typeOfPropertyLossCodes, equalTo(Arrays.asList("4", "7")));
		
		for (PropertySegment propertySegment: propertySegments){
			Set<PropertyType> propertyTypes = propertySegment.getPropertyTypes();
			Set<SuspectedDrugType> suspectedDrugTypes = propertySegment.getSuspectedDrugTypes();
			
			switch (propertySegment.getTypePropertyLossEtcType().getStateCode()){
			case "7":
				assertThat(propertySegment.getAdministrativeSegment(), equalTo(persisted));
				assertThat(propertySegment.getSegmentActionType().getStateCode(), equalTo("I"));
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(0));
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(1));
				
				assertThat(propertyTypes.size(), equalTo(1));
				PropertyType propertyType = propertyTypes.toArray(new PropertyType[]{})[0];
				assertNotNull(propertyType);
				assertThat(propertyType.getPropertyDescriptionType().getStateCode(), equalTo("03")); 
				assertNull(propertyType.getRecoveredDate()); 
				assertThat(propertyType.getRecoveredDateType().getDateTypeId(), equalTo(99998));
				assertThat(propertyType.getValueOfProperty(), equalTo(10000.0));
				
				assertThat(suspectedDrugTypes.size(), equalTo(0));
				break; 
			case "4": 
				assertThat(propertySegment.getAdministrativeSegment(), equalTo(persisted));
				assertThat(propertySegment.getSegmentActionType().getStateCode(), equalTo("I"));
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(0));
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(0));
				
				assertThat(propertyTypes.size(), equalTo(0));
				assertThat(suspectedDrugTypes.size(), equalTo(2));
				
				for (SuspectedDrugType suspectedDrugType: suspectedDrugTypes){
					if (suspectedDrugType.getSuspectedDrugTypeType().getStateCode().equals("A")){
						assertThat(suspectedDrugType.getPropertySegment(), equalTo(propertySegment));
						assertThat(suspectedDrugType.getEstimatedDrugQuantity(), equalTo(1.0));
						assertThat(suspectedDrugType.getTypeDrugMeasurementType().getTypeDrugMeasurementTypeId(), equalTo(1)); 
					}
					else{
						assertThat(suspectedDrugType.getPropertySegment(), equalTo(propertySegment));
						assertThat(suspectedDrugType.getSuspectedDrugTypeType().getStateCode(), equalTo("D"));
						assertThat(suspectedDrugType.getEstimatedDrugQuantity(), equalTo(1.0));
						assertThat(suspectedDrugType.getTypeDrugMeasurementType().getStateCode(), equalTo("OZ")); 
					}
				}
				break; 
			default: 
				fail("Unexpected property loss type"); 
			}
		}
		
		Set<ArresteeSegment> arresteeSegments = persisted.getArresteeSegments();
		assertThat(arresteeSegments.size(), equalTo(1));
		
		for (ArresteeSegment arresteeSegment: arresteeSegments){
			if (arresteeSegment.getArrestTransactionNumber().equals("201040889")){
//				ArresteeSegment[arresteeSequenceNumber=01,arrestTransactionNumber=201040889,arrestDate=Tue Aug 23 00:00:00 CDT 2016,
				//typeOfArrest=S,multipleArresteeSegmentsIndicator=N,ucrArrestOffenseCode=23D,arresteeArmedWith={01,<null>},
				//automaticWeaponIndicator={<null>,<null>},residentStatus=U,dispositionOfArresteeUnder18=<null>,
				//age=33,sex=M,race=B,ethnicity=U
				assertThat(arresteeSegment.getArrestTransactionNumber(), equalTo("201040889"));
				assertThat(arresteeSegment.getArresteeSequenceNumber(), equalTo(1));
				assertThat(arresteeSegment.getArrestDate(), equalTo(LocalDate.of(2016, 6, 12)));
				
				assertThat(arresteeSegment.getArrestDateType().getDateTypeId(), equalTo(2355));
				assertThat(arresteeSegment.getArrestDateType().getDateMMDDYYYY(), equalTo("06122016"));
				assertThat(arresteeSegment.getTypeOfArrestType().getStateCode(), equalTo("S"));
				assertThat(arresteeSegment.getMultipleArresteeSegmentsIndicatorType().getStateCode(), 
						equalTo("N"));
				assertThat(arresteeSegment.getUcrOffenseCodeType().getStateCode(), equalTo("23D"));
				assertThat(arresteeSegment.getResidentStatusOfPersonType().getStateCode(), equalTo("U"));
				assertThat(arresteeSegment.getDispositionOfArresteeUnder18Type().getStateCode(), equalTo("9"));
				assertThat(arresteeSegment.getAgeOfArresteeMax(), equalTo(33));
				assertThat(arresteeSegment.getAgeOfArresteeMin(), equalTo(33));
				assertThat(arresteeSegment.getSexOfPersonType().getStateCode(), equalTo("M"));
				assertThat(arresteeSegment.getRaceOfPersonType().getStateCode(), equalTo("B"));
				assertThat(arresteeSegment.getEthnicityOfPersonType().getStateCode(), equalTo("U"));
				
				Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWiths = 
						arresteeSegment.getArresteeSegmentWasArmedWiths();
				assertThat(arresteeSegmentWasArmedWiths.size(), equalTo(2));
				
				for (ArresteeSegmentWasArmedWith arresteeSegmentWasArmedWith: arresteeSegmentWasArmedWiths){
					if (arresteeSegmentWasArmedWith.getAutomaticWeaponIndicator().equals("A")){
						assertThat(arresteeSegmentWasArmedWith.getArresteeWasArmedWithType().getStateCode(), equalTo("12"));
					}
					else if (arresteeSegmentWasArmedWith.getAutomaticWeaponIndicator().equals("")){
						assertThat(arresteeSegmentWasArmedWith.getArresteeWasArmedWithType().getStateCode(), equalTo("11"));
					}
					else {
						fail("Unexpected reportSegmentWasArmedWith.getAutomaticWeaponIndicator() value");
					}
				}

			}
			else {
				fail("Unexpected arrestee arrest transaction number");
			}

		}
		
//		2 OffenderSegment Segments:
//		OffenderSegment [age=00, sex=M, race=B, ethnicity=null, offenderSequenceNumber=01]
//		OffenderSegment [age=33, sex=M, race=B, ethnicity=null, offenderSequenceNumber=02]
		Set<OffenderSegment> offenderSegments = persisted.getOffenderSegments();
		assertThat(offenderSegments.size(), equalTo(2));
		
		for (OffenderSegment offenderSegment: offenderSegments){
			if (offenderSegment.getOffenderSequenceNumber().equals(1)){
				assertThat(offenderSegment.getSegmentActionType().getStateCode(), equalTo("I"));
				assertThat(offenderSegment.getAgeOfOffenderMax(), equalTo(0));
				assertThat(offenderSegment.getAgeOfOffenderMin(), equalTo(0));
				assertThat(offenderSegment.getSexOfPersonType().getStateCode(), equalTo("M"));
				assertThat(offenderSegment.getRaceOfPersonType().getStateCode(), equalTo("B"));
				assertThat(offenderSegment.getEthnicityOfPersonType().getStateCode(), equalTo("U"));
			}
			else if (offenderSegment.getOffenderSequenceNumber().equals(2)){
				assertThat(offenderSegment.getSegmentActionType().getStateCode(), equalTo("I"));
				assertThat(offenderSegment.getAgeOfOffenderMax(), equalTo(33));
				assertThat(offenderSegment.getAgeOfOffenderMin(), equalTo(33));
				assertThat(offenderSegment.getSexOfPersonType().getStateCode(), equalTo("M"));
				assertThat(offenderSegment.getRaceOfPersonType().getStateCode(), equalTo("B"));
				assertThat(offenderSegment.getEthnicityOfPersonType().getStateCode(), equalTo("U"));
			}
			else {
				fail("Unexpected offender sequence number"); 
			}
		}
		
		Set<VictimSegment> victimSegments = persisted.getVictimSegments();
		assertThat(victimSegments.size(), equalTo(1));
		
		for (VictimSegment victimSegment: victimSegments){
			if (victimSegment.getVictimSequenceNumber().equals(1)){
				assertThat(victimSegment.getAdministrativeSegment().getAdministrativeSegmentId(), equalTo(persisted.getAdministrativeSegmentId())); 
				assertThat(victimSegment.getSegmentActionType().getStateCode(), equalTo("I")); 
				assertThat(victimSegment.getTypeOfVictimType().getStateCode(), equalTo("L")); 
				assertThat(victimSegment.getOfficerActivityCircumstanceType().getStateCode(), equalTo("01")); 
				assertThat(victimSegment.getOfficerAssignmentTypeType().getStateCode(), equalTo("F")); 
				assertThat(victimSegment.getOfficerOtherJurisdictionOri(), equalTo("other1234")); 
				
				assertNull(victimSegment.getAgeOfVictimMax());
				assertNull(victimSegment.getAgeOfVictimMin());
				assertThat(victimSegment.getAgeNeonateIndicator(), equalTo(0));
				assertThat(victimSegment.getAgeFirstWeekIndicator(), equalTo(0));
				assertThat(victimSegment.getAgeFirstYearIndicator(), equalTo(0));
				
				assertThat(victimSegment.getSexOfPersonType().getStateCode(), equalTo("U")); 
				assertThat(victimSegment.getRaceOfPersonType().getStateCode(), equalTo("U")); 
				assertThat(victimSegment.getEthnicityOfPersonType().getStateCode(), equalTo("U")); 
				assertThat(victimSegment.getResidentStatusOfPersonType().getStateCode(), equalTo("U")); 
				assertThat(victimSegment.getAdditionalJustifiableHomicideCircumstancesType().getStateCode(), equalTo("9"));

				assertThat(victimSegment.getAggravatedAssaultHomicideCircumstancesTypes().size(), equalTo(2)); 
				assertTrue(victimSegment.getAggravatedAssaultHomicideCircumstancesTypes().containsAll(
						Arrays.asList(
								aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByStateCode("01"), 
								aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByStateCode("02") ))); 
				assertThat(victimSegment.getTypeInjuryTypes().size(), equalTo(1)); 
				assertTrue(victimSegment.getTypeInjuryTypes().contains(typeInjuryTypeRepository.findFirstByStateCode("O"))); 
				
				assertTrue(victimSegment.getOffenseSegments().containsAll(persisted.getOffenseSegments()));
				
				Set<VictimOffenderAssociation> victimOffenderAssociations = victimSegment.getVictimOffenderAssociations();
				assertThat(victimOffenderAssociations.size(), equalTo(1));
				
				VictimOffenderAssociation victimOffenderAssociation = new ArrayList<>(victimOffenderAssociations).get(0);
				
				assertThat(victimOffenderAssociation.getVictimSegment().getVictimSegmentId(), equalTo(victimSegment.getVictimSegmentId()));
				assertThat(victimOffenderAssociation.getOffenderSegment().getOffenderSequenceNumber(), equalTo(1));
				assertThat(victimOffenderAssociation.getVictimOffenderRelationshipType().getStateCode(), equalTo("AQ"));

			}
			else {
				fail("Unexpected victim sequence number"); 
			}
		}
		
		testUpdate(persisted);
		testDelete(persisted);
	}
	
	private void testUpdate(AdministrativeSegment persisted) {
		persisted.setYearOfTape("2017");
		persisted.setAgency(new Agency(99998));
		List<OffenseSegment> offenseSegmentsList = 
				new ArrayList<>(persisted.getOffenseSegments());
		offenseSegmentsList.removeIf(item -> item.getUcrOffenseCodeType().getStateCode().equals("23D"));
		persisted.getOffenseSegments().clear();
		persisted.getOffenseSegments().addAll(offenseSegmentsList);
		VictimSegment victimSegment = new ArrayList<>(persisted.getVictimSegments()).get(0); 
		victimSegment.getOffenseSegments().clear(); 
		victimSegment.getOffenseSegments().addAll(offenseSegmentsList);
		
		groupAIncidentService.saveAdministrativeSegment(persisted);
		AdministrativeSegment updated = 
				groupAIncidentService.findAdministrativeSegment(persisted.getAdministrativeSegmentId());

		assertThat(updated.getSegmentActionType().getStateCode(), equalTo("I"));
		assertThat(updated.getMonthOfTape(), equalTo("12"));
		assertThat(updated.getYearOfTape(), equalTo("2017"));
		assertThat(updated.getCityIndicator(), equalTo("Y"));
		assertThat(updated.getOri(), equalTo("ori"));
		assertThat(updated.getAgency().getAgencyId(), equalTo(99998));
		assertThat(updated.getIncidentNumber(), equalTo("1234568910"));
		assertThat(persisted.getIncidentDate(), equalTo(LocalDate.of(2016, 6, 12)));
		assertThat(updated.getIncidentDateType().getDateTypeId(), equalTo(2355));
		assertNull(updated.getReportDateIndicator());
		assertThat(updated.getIncidentHour(), equalTo("13"));
		assertThat(updated.getClearedExceptionallyType().getStateCode(), equalTo("B"));
		
		Set<OffenseSegment> offenseSegments = updated.getOffenseSegments();
		assertThat(offenseSegments.size(), equalTo(1));
		Set<ArresteeSegment> arresteeSegments = updated.getArresteeSegments();
		assertThat(arresteeSegments.size(), equalTo(1));
		Set<OffenderSegment> offenderSegments = updated.getOffenderSegments();
		assertThat(offenderSegments.size(), equalTo(2));
		Set<VictimSegment> victimSegments = updated.getVictimSegments();
		assertThat(victimSegments.size(), equalTo(1));
		Set<PropertySegment> propertySegments = updated.getPropertySegments();
		assertThat(propertySegments.size(), equalTo(2));

	}

	private void testDelete(AdministrativeSegment persisted) {
		long countOfAdministrativeSegmentsBeforeDelete = administrativeSegmentRepository.count(); 
		
		administrativeSegmentRepository.deleteByIncidentNumber(persisted.getIncidentNumber());  
		
		AdministrativeSegment afterDelete = administrativeSegmentRepository.findById(persisted.getAdministrativeSegmentId()).orElse(null);
		assertThat(afterDelete,  equalTo(null));
		
		long countOfAdministrativeSegmentsAfterDelete = administrativeSegmentRepository.count(); 
		assertThat(countOfAdministrativeSegmentsAfterDelete, equalTo(countOfAdministrativeSegmentsBeforeDelete - 1));
		
	}


	@Test
	public void testSaveGroupAIncidentReport(){
		GroupAIncidentReport groupAIncidentReport = BaselineIncidentFactory.getBaselineIncident();
		AdministrativeSegment administrativeSegment = StreamSupport.stream(
				groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport).spliterator(), false)
				.findFirst().orElse(null);
		AdministrativeSegment persisted = 
				groupAIncidentService.findAdministrativeSegment(administrativeSegment.getAdministrativeSegmentId());

		assertNotNull(persisted);
		assertThat(persisted.getSegmentActionType().getStateCode(), equalTo("I"));
		assertThat(persisted.getMonthOfTape(), equalTo("05"));
		assertThat(persisted.getYearOfTape(), equalTo("2016"));
		assertThat(persisted.getCityIndicator(), equalTo("Y"));
		assertThat(persisted.getOri(), equalTo("WA1234567"));
		assertThat(persisted.getAgency().getAgencyId(), equalTo(100000));
		assertThat(persisted.getIncidentNumber(), equalTo("54236732"));
		assertThat(persisted.getIncidentDate(), equalTo(LocalDate.of(2016, 5, 12)));
		assertThat(persisted.getIncidentDateType().getDateTypeId(), equalTo(2324));
		assertTrue(DateUtils.isSameDay(persisted.getExceptionalClearanceDate(), Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
		assertThat(persisted.getExceptionalClearanceDateType().getDateTypeId(), equalTo(2324));
		assertNull(persisted.getReportDateIndicator());
		assertThat(persisted.getIncidentHour(), equalTo(""));
		assertThat(persisted.getClearedExceptionallyType().getStateCode(), equalTo("A"));
		assertThat(persisted.getCargoTheftIndicatorType().getCargoTheftIndicatorTypeId(), equalTo(99998));
		
//		1 OffenseSegment Segments:
//		OffenseSegment [ucrOffenseCode=13A, offenseAttemptedCompleted=C, 
	//offendersSuspectedOfUsing=[N, null, null], locationType=15, numberOfPremisesEntered=null, 
	//methodOfEntry=null, 
	//typeOfCriminalActivity=[J, null, null], 
	//typeOfWeaponForceInvolved=[14, null, null], 
	//automaticWeaponIndicator=[ , null, null], 
	//biasMotivation=[15, null, null, null, null], 
	//populatedBiasMotivationCount=1, 
	//populatedTypeOfWeaponForceInvolvedCount=1, 
	//populatedTypeOfCriminalActivityCount=1, 
	//populatedOffendersSuspectedOfUsingCount=1]

		Set<OffenseSegment> offenseSegments = persisted.getOffenseSegments();
		assertThat(offenseSegments.size(), equalTo(1));
		
		OffenseSegment offenseSegment = offenseSegments.stream().findFirst().get();
		assertThat(offenseSegment.getBiasMotivationTypes().stream().findFirst().get().getStateCode(), equalTo("15"));
		assertThat(offenseSegment.getSegmentActionType().getStateCode(), equalTo("I"));
		assertThat(offenseSegment.getUcrOffenseCodeType().getStateCode(), equalTo("13A"));
		assertThat(offenseSegment.getOffenseAttemptedCompleted(), equalTo("C"));
		assertThat(offenseSegment.getLocationType().getStateCode(), equalTo("15"));
		assertThat(offenseSegment.getNumberOfPremisesEntered(), equalTo(null));
		assertThat(offenseSegment.getMethodOfEntryType().getMethodOfEntryTypeId(), equalTo(99998));
		Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes = 
				offenseSegment.getOffenderSuspectedOfUsingTypes();  
		assertThat(offenderSuspectedOfUsingTypes.size(), equalTo(1));
		assertThat(offenderSuspectedOfUsingTypes.contains(offenderSuspectedOfUsingTypeRepository.findFirstByStateCode("N")), equalTo(true));
		
		Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes = 
				offenseSegment.getTypeOfCriminalActivityTypes(); 
		assertThat(typeOfCriminalActivityTypes.size(), equalTo(1));
		assertTrue(typeOfCriminalActivityTypes.contains(typeOfCriminalActivityTypeRepository.findFirstByStateCode("J")));
		
		Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds = 
				offenseSegment.getTypeOfWeaponForceInvolveds();
		assertThat(typeOfWeaponForceInvolveds.size(), equalTo(1));
		TypeOfWeaponForceInvolved typeOfWeaponForceInvolved = typeOfWeaponForceInvolveds.stream().findFirst().get();
		assertThat(typeOfWeaponForceInvolved.getOffenseSegment().getOffenseSegmentId(), equalTo(offenseSegment.getOffenseSegmentId()));
		assertThat(typeOfWeaponForceInvolved.getAutomaticWeaponIndicator(), equalTo(""));
		assertThat(typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getStateCode(), equalTo("14"));
//	    "offenders": [
//      {
//          "segmentType": "5",
//          "age": {
//              "ageMin": 22,
//              "ageMax": 22,
//          },
//          "sex": "M",
//          "race": "W",
//          "ethnicity": "H",
//          "offenderSequenceNumber": {
//              "value": 1,
//          },
//      }
//  ],
		Set<OffenderSegment> offenderSegments = persisted.getOffenderSegments();
		assertThat(offenderSegments.size(), equalTo(1));
		OffenderSegment offenderSegment = offenderSegments.stream().findFirst().get();
		
		assertThat(offenderSegment.getOffenderSequenceNumber(), equalTo(1)); 
		assertThat(offenderSegment.getSegmentActionType().getStateCode(), equalTo("I"));
		assertThat(offenderSegment.getAgeOfOffenderMax(), equalTo(22));
		assertThat(offenderSegment.getAgeOfOffenderMin(), equalTo(22));
		assertThat(offenderSegment.getSexOfPersonType().getStateCode(), equalTo("M"));
		assertThat(offenderSegment.getRaceOfPersonType().getStateCode(), equalTo("W"));
		assertThat(offenderSegment.getEthnicityOfPersonType().getStateCode(), equalTo("H"));
//	    "arrestees": [
//      {
//          "segmentType": "6",
//          "age": {
//              "ageMin": 22,
//              "ageMax": 22,
//          },
//          "sex": "M",
//          "race": "W",
//          "ethnicity": "U",
//          "arresteeSequenceNumber": {
//              "value": 1,
//          },
//          "arrestTransactionNumber": "12345",
//          "arrestDate": { 05162015
//              "value": 1431752400000,
//          },
//          "typeOfArrest": "O",
//          "multipleArresteeSegmentsIndicator": "N",
//          "ucrArrestOffenseCode": "13A",
//          "arresteeArmedWith": [
//              "01",
//              null
//          ],
//          "automaticWeaponIndicator": [
//              null,
//              null
//          ],
//          "residentStatus": "R",
//          "dispositionOfArresteeUnder18": null,
//          "reportActionType": "I",
//          "unknown": false,
//          "identifier": "12345"
//      }
//  ]
		Set<ArresteeSegment> arresteeSegments = persisted.getArresteeSegments();
		assertThat(arresteeSegments.size(), equalTo(1));
		
		ArresteeSegment arresteeSegment = arresteeSegments.stream().findFirst().get();
		
		assertThat(arresteeSegment.getArrestTransactionNumber(), equalTo("12345"));
		assertThat(arresteeSegment.getArresteeSequenceNumber(), equalTo(1));
		assertThat(arresteeSegment.getArrestDate(), equalTo(LocalDate.of(2015, 5, 16)));
		
		assertThat(arresteeSegment.getArrestDateType().getDateTypeId(), equalTo(1962));
		assertThat(arresteeSegment.getArrestDateType().getDateMMDDYYYY(), equalTo("05162015"));
		assertThat(arresteeSegment.getTypeOfArrestType().getStateCode(), equalTo("O"));
		assertThat(arresteeSegment.getMultipleArresteeSegmentsIndicatorType().getStateCode(), 
				equalTo("N"));
		assertThat(arresteeSegment.getUcrOffenseCodeType().getStateCode(), equalTo("13A"));
		assertThat(arresteeSegment.getResidentStatusOfPersonType().getStateCode(), equalTo("R"));
		assertThat(arresteeSegment.getDispositionOfArresteeUnder18Type().getStateCode(), equalTo(" "));
		assertThat(arresteeSegment.getAgeOfArresteeMax(), equalTo(22));
		assertThat(arresteeSegment.getAgeOfArresteeMin(), equalTo(22));
		assertThat(arresteeSegment.getSexOfPersonType().getStateCode(), equalTo("M"));
		assertThat(arresteeSegment.getRaceOfPersonType().getStateCode(), equalTo("W"));
		assertThat(arresteeSegment.getEthnicityOfPersonType().getStateCode(), equalTo("U"));
		
		Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWiths = 
				arresteeSegment.getArresteeSegmentWasArmedWiths();
		assertThat(arresteeSegmentWasArmedWiths.size(), equalTo(1));
		
		ArresteeSegmentWasArmedWith arresteeSegmentWasArmedWith = arresteeSegmentWasArmedWiths.stream().findFirst().get();
		assertThat(arresteeSegmentWasArmedWith.getArresteeWasArmedWithType().getStateCode(), equalTo("01"));
		assertThat(arresteeSegmentWasArmedWith.getAutomaticWeaponIndicator(), equalTo(""));
		
//		1 VictimSegment Segments:
//		VictimSegment [age=20-22, sex=F, race=B, ethnicity=N, victimSequenceNumber=01, 
//	ucrOffenseCodeConnection=[13A, null, null, null, null, null, null, null, null, null], 
//	typeOfVictim=I, residentStatus=R, aggravatedAssaultHomicideCircumstances=[null, null], 
//	additionalJustifiableHomicideCircumstances=null, typeOfInjury=[N, null, null, null, null], 
//	offenderNumberRelated=[01, null, null, null, null, null, null, null, null, null], 
//	victimOffenderRelationship=[SE, null, null, null, null, null, null, null, null, null], 
//	typeOfOfficerActivityCircumstance=null, officerAssignmentType=null, officerOtherJurisdictionORI=null, 
//	populatedAggravatedAssaultHomicideCircumstancesCount=0, 
//	populatedTypeOfInjuryCount=1, populatedUcrOffenseCodeConnectionCount=1, populatedOffenderNumberRelatedCount=1]
//	,segmentType=6]
		Set<VictimSegment> victimSegments = persisted.getVictimSegments();
		assertThat(victimSegments.size(), equalTo(1));
		VictimSegment victimSegment = victimSegments.stream().findFirst().get();
		
		assertThat(victimSegment.getVictimSequenceNumber(), equalTo(1));
		assertThat(victimSegment.getAdministrativeSegment().getAdministrativeSegmentId(), equalTo(persisted.getAdministrativeSegmentId())); 
		assertThat(victimSegment.getSegmentActionType().getStateCode(), equalTo("I")); 
		assertThat(victimSegment.getTypeOfVictimType().getStateCode(), equalTo("I")); 
		assertThat(victimSegment.getOfficerActivityCircumstanceType().getOfficerActivityCircumstanceTypeId(), equalTo(99998)); 
		assertThat(victimSegment.getOfficerAssignmentTypeType().getOfficerAssignmentTypeTypeId(), equalTo(99998)); 
		
		assertThat(victimSegment.getAgeOfVictimMax(), equalTo(22));
		assertThat(victimSegment.getAgeOfVictimMin(), equalTo(20));
		assertThat(victimSegment.getAgeNeonateIndicator(), equalTo(0));
		assertThat(victimSegment.getAgeFirstWeekIndicator(), equalTo(0));
		assertThat(victimSegment.getAgeFirstYearIndicator(), equalTo(0));
		
		assertThat(victimSegment.getSexOfPersonType().getStateCode(), equalTo("F")); 
		assertThat(victimSegment.getRaceOfPersonType().getStateCode(), equalTo("B")); 
		assertThat(victimSegment.getEthnicityOfPersonType().getStateCode(), equalTo("N")); 
		assertThat(victimSegment.getResidentStatusOfPersonType().getStateCode(), equalTo("R")); 
		assertThat(victimSegment.getAdditionalJustifiableHomicideCircumstancesType()
				.getAdditionalJustifiableHomicideCircumstancesTypeId(), equalTo(99998));
		assertThat(victimSegment.getAggravatedAssaultHomicideCircumstancesTypes().size(), equalTo(1)); 
		assertTrue(victimSegment.getAggravatedAssaultHomicideCircumstancesTypes().contains(
						aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByStateCode("01"))); 
		assertThat(victimSegment.getTypeInjuryTypes().size(), equalTo(1)); 
		assertTrue(victimSegment.getTypeInjuryTypes().contains(typeInjuryTypeRepository.findFirstByStateCode("N"))); 
		
		assertTrue(victimSegment.getOffenseSegments().containsAll(persisted.getOffenseSegments()));
		
		Set<VictimOffenderAssociation> victimOffenderAssociations = victimSegment.getVictimOffenderAssociations();
		assertThat(victimOffenderAssociations.size(), equalTo(1));
		
		VictimOffenderAssociation victimOffenderAssociation = new ArrayList<>(victimOffenderAssociations).get(0);
		assertThat(victimOffenderAssociation.getVictimSegment().getVictimSegmentId(), equalTo(victimSegment.getVictimSegmentId()));
		assertThat(victimOffenderAssociation.getOffenderSegment().getOffenderSequenceNumber(), equalTo(1));
		assertThat(victimOffenderAssociation.getVictimOffenderRelationshipType().getStateCode(), equalTo("SE"));
		
//		3 PropertySegment Segments:
//			PropertySegment [typeOfPropertyLoss=7, propertyDescription=[20, null, null, null, null, null, null, null, null, null], valueOfProperty=[5000, null, null, null, null, null, null, null, null, null], dateRecovered=[null, null, null, null, null, null, null, null, null, null], numberOfStolenMotorVehicles=null, numberOfRecoveredMotorVehicles=null, suspectedDrugType=[null, null, null], estimatedDrugQuantity=[null, null, null], typeDrugMeasurement=[null, null, null], populatedPropertyDescriptionCount=1, populatedSuspectedDrugTypeCount=0]
//			PropertySegment [typeOfPropertyLoss=5, propertyDescription=[20, null, null, null, null, null, null, null, null, null], valueOfProperty=[5000, null, null, null, null, null, null, null, null, null], dateRecovered=[Thu Jan 08 00:00:00 CST 2015, null, null, null, null, null, null, null, null, null], numberOfStolenMotorVehicles=null, numberOfRecoveredMotorVehicles=null, suspectedDrugType=[null, null, null], estimatedDrugQuantity=[null, null, null], typeDrugMeasurement=[null, null, null], populatedPropertyDescriptionCount=1, populatedSuspectedDrugTypeCount=0]
//			PropertySegment [typeOfPropertyLoss=6, propertyDescription=[10, 11, null, null, null, null, null, null, null, null], valueOfProperty=[null, 100, null, null, null, null, null, null, null, null], dateRecovered=[null, null, null, null, null, null, null, null, null, null], numberOfStolenMotorVehicles=null, numberOfRecoveredMotorVehicles=null, suspectedDrugType=[E, E, X], estimatedDrugQuantity=[0.001, 0.001, null], typeDrugMeasurement=[LB, OZ, null], populatedPropertyDescriptionCount=2, populatedSuspectedDrugTypeCount=3]
		
		Set<PropertySegment> propertySegments = persisted.getPropertySegments();
		assertThat(propertySegments.size(), equalTo(3));
		
		for (PropertySegment propertySegment: propertySegments){
			Set<PropertyType> propertyTypes = propertySegment.getPropertyTypes();
			Set<SuspectedDrugType> suspectedDrugTypes = propertySegment.getSuspectedDrugTypes();
			switch (propertySegment.getTypePropertyLossEtcType().getStateCode()){
			case "5": 
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(null)); 
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(null)); 
				assertThat(propertyTypes.size(), equalTo(1));
				for (PropertyType propertyType: propertyTypes){
					assertThat(propertyType.getPropertyDescriptionType().getStateCode(), equalTo("20"));
					assertThat(propertyType.getValueOfProperty(), equalTo(5000.0));
					assertThat(propertyType.getRecoveredDateType().getDateMMDDYYYY(), equalTo("01082015"));
				}
				
				assertThat(suspectedDrugTypes.size(), equalTo(0));
				break; 
			case "6": 
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(null)); 
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(null)); 
				assertThat(propertyTypes.size(), equalTo(2));
				for (PropertyType propertyType: propertyTypes){
					switch(propertyType.getPropertyDescriptionType().getStateCode()){
					case "10":
						assertThat(propertyType.getValueOfProperty(), equalTo(null));
						assertThat(propertyType.getRecoveredDateType().getDateTypeId(), equalTo(99998));
						break; 
					case "11": 
						assertThat(propertyType.getValueOfProperty(), equalTo(100.0));
						assertThat(propertyType.getRecoveredDateType().getDateTypeId(), equalTo(99998));
						break; 
					default:
						fail("Unexpected property description in the type of loss 6 property segment.");
					}
				}
				assertThat(suspectedDrugTypes.size(), equalTo(3));
//				suspectedDrugType=[E, E, X], estimatedDrugQuantity=[0.001, 0.001, null], typeDrugMeasurement=[LB, OZ, null], populatedPropertyDescriptionCount=2, populatedSuspectedDrugTypeCount=3]
				for (SuspectedDrugType suspectedDrugType: suspectedDrugTypes){
					assertThat(suspectedDrugType.getPropertySegment().getPropertySegmentId(), equalTo(propertySegment.getPropertySegmentId()));
					switch(suspectedDrugType.getTypeDrugMeasurementType().getStateCode()){
					case " ": 
						assertThat(suspectedDrugType.getSuspectedDrugTypeType().getStateCode(), equalTo("X"));
						assertThat(suspectedDrugType.getEstimatedDrugQuantity(), equalTo(null));
						break; 
					case "LB": 
						assertThat(suspectedDrugType.getSuspectedDrugTypeType().getStateCode(), equalTo("E"));
						assertThat(suspectedDrugType.getEstimatedDrugQuantity(), equalTo(0.001));
						break; 
					case "OZ": 
						assertThat(suspectedDrugType.getSuspectedDrugTypeType().getStateCode(), equalTo("E"));
						assertThat(suspectedDrugType.getEstimatedDrugQuantity(), equalTo(0.001));
						break; 
					default:
						fail("Unexpected type drug measurement type."); 
					}
				}
				break; 
			case "7": 
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(null)); 
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(null)); 
				assertThat(propertyTypes.size(), equalTo(1));
				for (PropertyType propertyType: propertyTypes){
					assertThat(propertyType.getPropertyDescriptionType().getStateCode(), equalTo("20"));
					assertThat(propertyType.getValueOfProperty(), equalTo(5000.0));
					assertThat(propertyType.getRecoveredDateType().getDateTypeId(), equalTo(99998));
				}
				assertThat(suspectedDrugTypes.size(), equalTo(0));
				break; 
			default: 
				fail("Unexpected type of property loss code. ");	
			}
		}
		
		testUpdateGroupAIncidentReport(groupAIncidentReport);
		testDeleteGroupAIncidentReport(groupAIncidentReport);
	}

	@Test
	public void testSaveGroupAIncidentReportWithMultipleOffenders(){
		GroupAIncidentReport groupAIncidentReport = BaselineIncidentFactory.getBaselineIncidentWithMultipleOffenders();
		AdministrativeSegment administrativeSegment = StreamSupport.stream(
				groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport).spliterator(), false)
				.findFirst().orElse(null);
		AdministrativeSegment persisted = 
				groupAIncidentService.findAdministrativeSegment(administrativeSegment.getAdministrativeSegmentId());

		assertNotNull(persisted);
		assertThat(persisted.getSegmentActionType().getStateCode(), equalTo("I"));
		assertThat(persisted.getMonthOfTape(), equalTo("05"));
		assertThat(persisted.getYearOfTape(), equalTo("2016"));
		assertThat(persisted.getCityIndicator(), equalTo("Y"));
		assertThat(persisted.getOri(), equalTo("WA1234567"));
		assertThat(persisted.getAgency().getAgencyId(), equalTo(100000));
		assertThat(persisted.getIncidentNumber(), equalTo("9876543"));
		assertThat(persisted.getIncidentDate(), equalTo(LocalDate.of(2016, 5, 12)));
		assertThat(persisted.getIncidentDateType().getDateTypeId(), equalTo(2324));
		assertTrue(DateUtils.isSameDay(persisted.getExceptionalClearanceDate(), Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
		assertThat(persisted.getExceptionalClearanceDateType().getDateTypeId(), equalTo(2324));
		assertNull(persisted.getReportDateIndicator());
		assertThat(persisted.getIncidentHour(), equalTo(""));
		assertThat(persisted.getClearedExceptionallyType().getStateCode(), equalTo("A"));
		assertThat(persisted.getCargoTheftIndicatorType().getCargoTheftIndicatorTypeId(), equalTo(99998));
		
//		1 OffenseSegment Segments:
//		OffenseSegment [ucrOffenseCode=13A, offenseAttemptedCompleted=C, 
	//offendersSuspectedOfUsing=[N, null, null], locationType=15, numberOfPremisesEntered=null, 
	//methodOfEntry=null, 
	//typeOfCriminalActivity=[J, null, null], 
	//typeOfWeaponForceInvolved=[14, null, null], 
	//automaticWeaponIndicator=[ , null, null], 
	//biasMotivation=[15, null, null, null, null], 
	//populatedBiasMotivationCount=1, 
	//populatedTypeOfWeaponForceInvolvedCount=1, 
	//populatedTypeOfCriminalActivityCount=1, 
	//populatedOffendersSuspectedOfUsingCount=1]

		Set<OffenseSegment> offenseSegments = persisted.getOffenseSegments();
		assertThat(offenseSegments.size(), equalTo(1));
		
		OffenseSegment offenseSegment = offenseSegments.stream().findFirst().get();
		assertThat(offenseSegment.getBiasMotivationTypes().stream().findFirst().get().getStateCode(), equalTo("15"));
		assertThat(offenseSegment.getSegmentActionType().getStateCode(), equalTo("I"));
		assertThat(offenseSegment.getUcrOffenseCodeType().getStateCode(), equalTo("13A"));
		assertThat(offenseSegment.getOffenseAttemptedCompleted(), equalTo("C"));
		assertThat(offenseSegment.getLocationType().getStateCode(), equalTo("15"));
		assertThat(offenseSegment.getNumberOfPremisesEntered(), equalTo(null));
		assertThat(offenseSegment.getMethodOfEntryType().getMethodOfEntryTypeId(), equalTo(99998));
		Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes = 
				offenseSegment.getOffenderSuspectedOfUsingTypes();  
		assertThat(offenderSuspectedOfUsingTypes.size(), equalTo(1));
		assertThat(offenderSuspectedOfUsingTypes.contains(offenderSuspectedOfUsingTypeRepository.findFirstByStateCode("N")), equalTo(true));
		
		Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes = 
				offenseSegment.getTypeOfCriminalActivityTypes(); 
		assertThat(typeOfCriminalActivityTypes.size(), equalTo(1));
		assertTrue(typeOfCriminalActivityTypes.contains(typeOfCriminalActivityTypeRepository.findFirstByStateCode("J")));
		
		Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds = 
				offenseSegment.getTypeOfWeaponForceInvolveds();
		assertThat(typeOfWeaponForceInvolveds.size(), equalTo(1));
		TypeOfWeaponForceInvolved typeOfWeaponForceInvolved = typeOfWeaponForceInvolveds.stream().findFirst().get();
		assertThat(typeOfWeaponForceInvolved.getOffenseSegment().getOffenseSegmentId(), equalTo(offenseSegment.getOffenseSegmentId()));
		assertThat(typeOfWeaponForceInvolved.getAutomaticWeaponIndicator(), equalTo(""));
		assertThat(typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getStateCode(), equalTo("14"));
//	    "offenders": [
//      {
//          "segmentType": "5",
//          "age": {
//              "ageMin": 22,
//              "ageMax": 22,
//          },
//          "sex": "M",
//          "race": "W",
//          "ethnicity": "H",
//          "offenderSequenceNumber": {
//              "value": 1,
//          },
//      }
//  ],
		Set<OffenderSegment> offenderSegments = persisted.getOffenderSegments();
		assertThat(offenderSegments.size(), equalTo(2));

		for (OffenderSegment offenderSegment: offenderSegments){

			if (offenderSegment.getOffenderSequenceNumber().equals(1)){
				assertThat(offenderSegment.getSegmentActionType().getStateCode(), equalTo("I"));
				assertThat(offenderSegment.getAgeOfOffenderMax(), equalTo(22));
				assertThat(offenderSegment.getAgeOfOffenderMin(), equalTo(22));
				assertThat(offenderSegment.getSexOfPersonType().getStateCode(), equalTo("M"));
				assertThat(offenderSegment.getRaceOfPersonType().getStateCode(), equalTo("W"));
				assertThat(offenderSegment.getEthnicityOfPersonType().getStateCode(), equalTo("H"));
			}
			else{
				assertThat(offenderSegment.getSegmentActionType().getStateCode(), equalTo("I"));
				assertThat(offenderSegment.getAgeOfOffenderMax(), equalTo(23));
				assertThat(offenderSegment.getAgeOfOffenderMin(), equalTo(23));
				assertThat(offenderSegment.getSexOfPersonType().getStateCode(), equalTo("F"));
				assertThat(offenderSegment.getRaceOfPersonType().getStateCode(), equalTo("W"));
				assertThat(offenderSegment.getEthnicityOfPersonType().getStateCode(), equalTo("H"));
			}
		}
//	    "arrestees": [
//      {
//          "segmentType": "6",
//          "age": {
//              "ageMin": 22,
//              "ageMax": 22,
//          },
//          "sex": "M",
//          "race": "W",
//          "ethnicity": "U",
//          "arresteeSequenceNumber": {
//              "value": 1,
//          },
//          "arrestTransactionNumber": "12345",
//          "arrestDate": { 05162015
//              "value": 1431752400000,
//          },
//          "typeOfArrest": "O",
//          "multipleArresteeSegmentsIndicator": "N",
//          "ucrArrestOffenseCode": "13A",
//          "arresteeArmedWith": [
//              "01",
//              null
//          ],
//          "automaticWeaponIndicator": [
//              null,
//              null
//          ],
//          "residentStatus": "R",
//          "dispositionOfArresteeUnder18": null,
//          "reportActionType": "I",
//          "unknown": false,
//          "identifier": "12345"
//      }
//  ]
		Set<ArresteeSegment> arresteeSegments = persisted.getArresteeSegments();
		assertThat(arresteeSegments.size(), equalTo(1));
		
		ArresteeSegment arresteeSegment = arresteeSegments.stream().findFirst().get();
		
		assertThat(arresteeSegment.getArrestTransactionNumber(), equalTo("12345"));
		assertThat(arresteeSegment.getArresteeSequenceNumber(), equalTo(1));
		assertThat(arresteeSegment.getArrestDate(), equalTo(LocalDate.of(2015, 5, 16)));
		
		assertThat(arresteeSegment.getArrestDateType().getDateTypeId(), equalTo(1962));
		assertThat(arresteeSegment.getArrestDateType().getDateMMDDYYYY(), equalTo("05162015"));
		assertThat(arresteeSegment.getTypeOfArrestType().getStateCode(), equalTo("O"));
		assertThat(arresteeSegment.getMultipleArresteeSegmentsIndicatorType().getStateCode(), 
				equalTo("N"));
		assertThat(arresteeSegment.getUcrOffenseCodeType().getStateCode(), equalTo("13A"));
		assertThat(arresteeSegment.getResidentStatusOfPersonType().getStateCode(), equalTo("R"));
		assertThat(arresteeSegment.getDispositionOfArresteeUnder18Type().getStateCode(), equalTo(" "));
		assertThat(arresteeSegment.getAgeOfArresteeMax(), equalTo(22));
		assertThat(arresteeSegment.getAgeOfArresteeMin(), equalTo(22));
		assertThat(arresteeSegment.getSexOfPersonType().getStateCode(), equalTo("M"));
		assertThat(arresteeSegment.getRaceOfPersonType().getStateCode(), equalTo("W"));
		assertThat(arresteeSegment.getEthnicityOfPersonType().getStateCode(), equalTo("U"));
		
		Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWiths = 
				arresteeSegment.getArresteeSegmentWasArmedWiths();
		assertThat(arresteeSegmentWasArmedWiths.size(), equalTo(1));
		
		ArresteeSegmentWasArmedWith arresteeSegmentWasArmedWith = arresteeSegmentWasArmedWiths.stream().findFirst().get();
		assertThat(arresteeSegmentWasArmedWith.getArresteeWasArmedWithType().getStateCode(), equalTo("01"));
		assertThat(arresteeSegmentWasArmedWith.getAutomaticWeaponIndicator(), equalTo(""));
		
//		1 VictimSegment Segments:
//		VictimSegment [age=20-22, sex=F, race=B, ethnicity=N, victimSequenceNumber=01, 
//	ucrOffenseCodeConnection=[13A, null, null, null, null, null, null, null, null, null], 
//	typeOfVictim=I, residentStatus=R, aggravatedAssaultHomicideCircumstances=[null, null], 
//	additionalJustifiableHomicideCircumstances=null, typeOfInjury=[N, null, null, null, null], 
//	offenderNumberRelated=[01, null, null, null, null, null, null, null, null, null], 
//	victimOffenderRelationship=[SE, null, null, null, null, null, null, null, null, null], 
//	typeOfOfficerActivityCircumstance=null, officerAssignmentType=null, officerOtherJurisdictionORI=null, 
//	populatedAggravatedAssaultHomicideCircumstancesCount=0, 
//	populatedTypeOfInjuryCount=1, populatedUcrOffenseCodeConnectionCount=1, populatedOffenderNumberRelatedCount=1]
//	,segmentType=6]
		Set<VictimSegment> victimSegments = persisted.getVictimSegments();
		assertThat(victimSegments.size(), equalTo(1));
		VictimSegment victimSegment = victimSegments.stream().findFirst().get();
		
		assertThat(victimSegment.getVictimSequenceNumber(), equalTo(1));
		assertThat(victimSegment.getAdministrativeSegment().getAdministrativeSegmentId(), equalTo(persisted.getAdministrativeSegmentId())); 
		assertThat(victimSegment.getSegmentActionType().getStateCode(), equalTo("I")); 
		assertThat(victimSegment.getTypeOfVictimType().getStateCode(), equalTo("I")); 
		assertThat(victimSegment.getOfficerActivityCircumstanceType().getOfficerActivityCircumstanceTypeId(), equalTo(99998)); 
		assertThat(victimSegment.getOfficerAssignmentTypeType().getOfficerAssignmentTypeTypeId(), equalTo(99998)); 
		
		assertThat(victimSegment.getAgeOfVictimMax(), equalTo(22));
		assertThat(victimSegment.getAgeOfVictimMin(), equalTo(20));
		assertThat(victimSegment.getAgeNeonateIndicator(), equalTo(0));
		assertThat(victimSegment.getAgeFirstWeekIndicator(), equalTo(0));
		assertThat(victimSegment.getAgeFirstYearIndicator(), equalTo(0));
		
		assertThat(victimSegment.getSexOfPersonType().getStateCode(), equalTo("F")); 
		assertThat(victimSegment.getRaceOfPersonType().getStateCode(), equalTo("B")); 
		assertThat(victimSegment.getEthnicityOfPersonType().getStateCode(), equalTo("N")); 
		assertThat(victimSegment.getResidentStatusOfPersonType().getStateCode(), equalTo("R")); 
		assertThat(victimSegment.getAdditionalJustifiableHomicideCircumstancesType()
				.getAdditionalJustifiableHomicideCircumstancesTypeId(), equalTo(99998));
		assertThat(victimSegment.getAggravatedAssaultHomicideCircumstancesTypes().size(), equalTo(1)); 
		assertTrue(victimSegment.getAggravatedAssaultHomicideCircumstancesTypes().contains(
						aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByStateCode("01"))); 
		assertThat(victimSegment.getTypeInjuryTypes().size(), equalTo(1)); 
		assertTrue(victimSegment.getTypeInjuryTypes().contains(typeInjuryTypeRepository.findFirstByStateCode("N"))); 
		
		assertTrue(victimSegment.getOffenseSegments().containsAll(persisted.getOffenseSegments()));
		
		Set<VictimOffenderAssociation> victimOffenderAssociations = victimSegment.getVictimOffenderAssociations();
		assertThat(victimOffenderAssociations.size(), equalTo(2));
		
		VictimOffenderAssociation victimOffenderAssociation1 = new ArrayList<>(victimOffenderAssociations).get(0);
		assertThat(victimOffenderAssociation1.getVictimSegment().getVictimSegmentId(), equalTo(victimSegment.getVictimSegmentId()));
		assertThat(victimOffenderAssociation1.getOffenderSegment().getOffenderSequenceNumber(), equalTo(1));
		assertThat(victimOffenderAssociation1.getVictimOffenderRelationshipType().getStateCode(), equalTo("IL"));
		
		VictimOffenderAssociation victimOffenderAssociation2 = new ArrayList<>(victimOffenderAssociations).get(1);
		assertThat(victimOffenderAssociation2.getVictimSegment().getVictimSegmentId(), equalTo(victimSegment.getVictimSegmentId()));
		assertThat(victimOffenderAssociation2.getOffenderSegment().getOffenderSequenceNumber(), equalTo(2));
		assertThat(victimOffenderAssociation2.getVictimOffenderRelationshipType().getStateCode(), equalTo("IL"));
		
//		3 PropertySegment Segments:
//			PropertySegment [typeOfPropertyLoss=7, propertyDescription=[20, null, null, null, null, null, null, null, null, null], valueOfProperty=[5000, null, null, null, null, null, null, null, null, null], dateRecovered=[null, null, null, null, null, null, null, null, null, null], numberOfStolenMotorVehicles=null, numberOfRecoveredMotorVehicles=null, suspectedDrugType=[null, null, null], estimatedDrugQuantity=[null, null, null], typeDrugMeasurement=[null, null, null], populatedPropertyDescriptionCount=1, populatedSuspectedDrugTypeCount=0]
//			PropertySegment [typeOfPropertyLoss=5, propertyDescription=[20, null, null, null, null, null, null, null, null, null], valueOfProperty=[5000, null, null, null, null, null, null, null, null, null], dateRecovered=[Thu Jan 08 00:00:00 CST 2015, null, null, null, null, null, null, null, null, null], numberOfStolenMotorVehicles=null, numberOfRecoveredMotorVehicles=null, suspectedDrugType=[null, null, null], estimatedDrugQuantity=[null, null, null], typeDrugMeasurement=[null, null, null], populatedPropertyDescriptionCount=1, populatedSuspectedDrugTypeCount=0]
//			PropertySegment [typeOfPropertyLoss=6, propertyDescription=[10, 11, null, null, null, null, null, null, null, null], valueOfProperty=[null, 100, null, null, null, null, null, null, null, null], dateRecovered=[null, null, null, null, null, null, null, null, null, null], numberOfStolenMotorVehicles=null, numberOfRecoveredMotorVehicles=null, suspectedDrugType=[E, E, X], estimatedDrugQuantity=[0.001, 0.001, null], typeDrugMeasurement=[LB, OZ, null], populatedPropertyDescriptionCount=2, populatedSuspectedDrugTypeCount=3]
		
		Set<PropertySegment> propertySegments = persisted.getPropertySegments();
		assertThat(propertySegments.size(), equalTo(3));
		
		for (PropertySegment propertySegment: propertySegments){
			Set<PropertyType> propertyTypes = propertySegment.getPropertyTypes();
			Set<SuspectedDrugType> suspectedDrugTypes = propertySegment.getSuspectedDrugTypes();
			switch (propertySegment.getTypePropertyLossEtcType().getStateCode()){
			case "5": 
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(null)); 
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(null)); 
				assertThat(propertyTypes.size(), equalTo(1));
				for (PropertyType propertyType: propertyTypes){
					assertThat(propertyType.getPropertyDescriptionType().getStateCode(), equalTo("20"));
					assertThat(propertyType.getValueOfProperty(), equalTo(5000.0));
					assertThat(propertyType.getRecoveredDateType().getDateMMDDYYYY(), equalTo("01082015"));
				}
				
				assertThat(suspectedDrugTypes.size(), equalTo(0));
				break; 
			case "6": 
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(null)); 
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(null)); 
				assertThat(propertyTypes.size(), equalTo(2));
				for (PropertyType propertyType: propertyTypes){
					switch(propertyType.getPropertyDescriptionType().getStateCode()){
					case "10":
						assertThat(propertyType.getValueOfProperty(), equalTo(null));
						assertThat(propertyType.getRecoveredDateType().getDateTypeId(), equalTo(99998));
						break; 
					case "11": 
						assertThat(propertyType.getValueOfProperty(), equalTo(100.0));
						assertThat(propertyType.getRecoveredDateType().getDateTypeId(), equalTo(99998));
						break; 
					default:
						fail("Unexpected property description in the type of loss 6 property segment.");
					}
				}
				assertThat(suspectedDrugTypes.size(), equalTo(3));
//				suspectedDrugType=[E, E, X], estimatedDrugQuantity=[0.001, 0.001, null], typeDrugMeasurement=[LB, OZ, null], populatedPropertyDescriptionCount=2, populatedSuspectedDrugTypeCount=3]
				for (SuspectedDrugType suspectedDrugType: suspectedDrugTypes){
					assertThat(suspectedDrugType.getPropertySegment().getPropertySegmentId(), equalTo(propertySegment.getPropertySegmentId()));
					switch(suspectedDrugType.getTypeDrugMeasurementType().getStateCode()){
					case " ": 
						assertThat(suspectedDrugType.getSuspectedDrugTypeType().getStateCode(), equalTo("X"));
						assertThat(suspectedDrugType.getEstimatedDrugQuantity(), equalTo(null));
						break; 
					case "LB": 
						assertThat(suspectedDrugType.getSuspectedDrugTypeType().getStateCode(), equalTo("E"));
						assertThat(suspectedDrugType.getEstimatedDrugQuantity(), equalTo(0.001));
						break; 
					case "OZ": 
						assertThat(suspectedDrugType.getSuspectedDrugTypeType().getStateCode(), equalTo("E"));
						assertThat(suspectedDrugType.getEstimatedDrugQuantity(), equalTo(0.001));
						break; 
					default:
						fail("Unexpected type drug measurement type."); 
					}
				}
				break; 
			case "7": 
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(null)); 
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(null)); 
				assertThat(propertyTypes.size(), equalTo(1));
				for (PropertyType propertyType: propertyTypes){
					assertThat(propertyType.getPropertyDescriptionType().getStateCode(), equalTo("20"));
					assertThat(propertyType.getValueOfProperty(), equalTo(5000.0));
					assertThat(propertyType.getRecoveredDateType().getDateTypeId(), equalTo(99998));
				}
				assertThat(suspectedDrugTypes.size(), equalTo(0));
				break; 
			default: 
				fail("Unexpected type of property loss code. ");	
			}
		}
		
	}
	private void testDeleteGroupAIncidentReport(GroupAIncidentReport groupAIncidentReport) {
		List<AdministrativeSegment> beforeDeletion = 
				administrativeSegmentRepository.findByIncidentNumber(groupAIncidentReport.getIncidentNumber());
		assertThat(beforeDeletion.size(), not(equalTo(0)));
		groupAIncidentService.deleteGroupAIncidentReport(groupAIncidentReport.getIncidentNumber());
		List<AdministrativeSegment> deleted = 
				administrativeSegmentRepository.findByIncidentNumber(groupAIncidentReport.getIncidentNumber());
		assertThat(deleted.size(), equalTo(0));
	}

	private void testUpdateGroupAIncidentReport(GroupAIncidentReport groupAIncidentReport) {
		groupAIncidentReport.setOri("WA1234567");
		groupAIncidentReport.setCargoTheftIndicator("Y");
		
		org.search.nibrs.model.OffenseSegment o = new org.search.nibrs.model.OffenseSegment();
		groupAIncidentReport.addOffense(o);
		o.setUcrOffenseCode("13B");
		o.setTypeOfCriminalActivity(0, "J");
		o.setOffenseAttemptedCompleted("C");
		o.setTypeOfWeaponForceInvolved(0, "14");
		o.setOffendersSuspectedOfUsing(0, "N");
		o.setBiasMotivation(0, "15");
		o.setLocationType("15");
		o.setNumberOfPremisesEntered(ParsedObject.getMissingParsedObject());
		o.setAutomaticWeaponIndicator(0, " ");
		org.search.nibrs.model.VictimSegment v = new org.search.nibrs.model.VictimSegment();
		groupAIncidentReport.addVictim(v);
		v.setTypeOfVictim("I");
		v.setTypeOfInjury(0, "N");
		v.setAggravatedAssaultHomicideCircumstances(0, "01");
		v.setVictimSequenceNumber(new ParsedObject<>(2));
		v.setAge(NIBRSAge.getAge(25, 30));
		v.setEthnicity("N");
		v.setResidentStatus("R");
		v.setSex("M");
		v.setRace("B");
		v.setOffenderNumberRelated(0, new ParsedObject<>(1));
		v.setVictimOffenderRelationship(0, "SE");
		v.setUcrOffenseCodeConnection(0, "13B");

		groupAIncidentReport.removeProperty(2);
		
		groupAIncidentReport.setMonthOfTape(4);
		groupAIncidentReport.setYearOfTape(2016);
		
		Iterable<AdministrativeSegment> saved = groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		assertThat(saved.iterator().hasNext(), equalTo(false));
		
		groupAIncidentReport.setMonthOfTape(5);
		groupAIncidentReport.setYearOfTape(2016);
		
		saved = groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		assertThat(saved.iterator().hasNext(), equalTo(true));
		
		AdministrativeSegment updated = 
				administrativeSegmentRepository.findByAdministrativeSegmentId(saved.iterator().next().getAdministrativeSegmentId());

		assertNotNull(updated);
		assertThat(updated.getSegmentActionType().getStateCode(), equalTo("R"));
		assertThat(updated.getMonthOfTape(), equalTo("05"));
		assertThat(updated.getYearOfTape(), equalTo("2016"));
		assertThat(updated.getCityIndicator(), equalTo("Y"));
		assertThat(updated.getOri(), equalTo("WA1234567"));
		assertThat(updated.getAgency().getAgencyId(), equalTo(100000));
		assertThat(updated.getIncidentNumber(), equalTo("54236732"));
		assertThat(updated.getIncidentDate(), equalTo(LocalDate.of(2016, 5, 12)));
		assertThat(updated.getIncidentDateType().getDateTypeId(), equalTo(2324));
		assertNull(updated.getReportDateIndicator());
		assertThat(updated.getIncidentHour(), equalTo(""));
		assertThat(updated.getClearedExceptionallyType().getStateCode(), equalTo("A"));
		assertThat(updated.getCargoTheftIndicatorType().getStateCode(), equalTo("Y"));
		
//		2 OffenseSegment Segments:
//		OffenseSegment [ucrOffenseCode=13A, offenseAttemptedCompleted=C, 
	//offendersSuspectedOfUsing=[N, null, null], locationType=15, numberOfPremisesEntered=null, 
	//methodOfEntry=null, 
	//typeOfCriminalActivity=[J, null, null], 
	//typeOfWeaponForceInvolved=[14, null, null], 
	//automaticWeaponIndicator=[ , null, null], 
	//biasMotivation=[15, null, null, null, null], 
	//populatedBiasMotivationCount=1, 
	//populatedTypeOfWeaponForceInvolvedCount=1, 
	//populatedTypeOfCriminalActivityCount=1, 
	//populatedOffendersSuspectedOfUsingCount=1]

		Set<OffenseSegment> offenseSegments = updated.getOffenseSegments();
		assertThat(offenseSegments.size(), equalTo(2));
		
		List<String> offenseCodes = offenseSegments.stream().map(i->i.getUcrOffenseCodeType().getStateCode()).collect(Collectors.toList()); 
		assertTrue(offenseCodes.containsAll(Arrays.asList("13A", "13B")));
		
		for (OffenseSegment offenseSegment: offenseSegments){
			assertThat(offenseSegment.getBiasMotivationTypes().stream().findFirst().get().getStateCode(), equalTo("15"));
			assertThat(offenseSegment.getSegmentActionType().getStateCode(), equalTo("R"));
			assertThat(offenseSegment.getOffenseAttemptedCompleted(), equalTo("C"));
			assertThat(offenseSegment.getLocationType().getStateCode(), equalTo("15"));
			assertThat(offenseSegment.getNumberOfPremisesEntered(), equalTo(null));
			assertThat(offenseSegment.getMethodOfEntryType().getMethodOfEntryTypeId(), equalTo(99998));
			Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes = 
					offenseSegment.getOffenderSuspectedOfUsingTypes();  
			assertThat(offenderSuspectedOfUsingTypes.size(), equalTo(1));
			assertThat(offenderSuspectedOfUsingTypes.contains(offenderSuspectedOfUsingTypeRepository.findFirstByStateCode("N")), equalTo(true));
			Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes = 
					offenseSegment.getTypeOfCriminalActivityTypes(); 
			assertThat(typeOfCriminalActivityTypes.size(), equalTo(1));
			assertTrue(typeOfCriminalActivityTypes.contains(typeOfCriminalActivityTypeRepository.findFirstByStateCode("J")));
			
			Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds = 
					offenseSegment.getTypeOfWeaponForceInvolveds();
			assertThat(typeOfWeaponForceInvolveds.size(), equalTo(1));
			TypeOfWeaponForceInvolved typeOfWeaponForceInvolved = typeOfWeaponForceInvolveds.stream().findFirst().get();
			assertThat(typeOfWeaponForceInvolved.getOffenseSegment().getOffenseSegmentId(), equalTo(offenseSegment.getOffenseSegmentId()));
			assertThat(typeOfWeaponForceInvolved.getAutomaticWeaponIndicator(), equalTo(""));
			assertThat(typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getStateCode(), equalTo("14"));
		}
		
		Set<OffenderSegment> offenderSegments = updated.getOffenderSegments();
		assertThat(offenderSegments.size(), equalTo(1));
		OffenderSegment offenderSegment = offenderSegments.stream().findFirst().get();
		
		assertThat(offenderSegment.getOffenderSequenceNumber(), equalTo(1)); 
		assertThat(offenderSegment.getSegmentActionType().getStateCode(), equalTo("R"));
		assertThat(offenderSegment.getAgeOfOffenderMax(), equalTo(22));
		assertThat(offenderSegment.getAgeOfOffenderMin(), equalTo(22));
		assertThat(offenderSegment.getSexOfPersonType().getStateCode(), equalTo("M"));
		assertThat(offenderSegment.getRaceOfPersonType().getStateCode(), equalTo("W"));
		assertThat(offenderSegment.getEthnicityOfPersonType().getStateCode(), equalTo("H"));
		
		Set<ArresteeSegment> arresteeSegments = updated.getArresteeSegments();
		assertThat(arresteeSegments.size(), equalTo(1));
		
		ArresteeSegment arresteeSegment = arresteeSegments.stream().findFirst().get();
		
		assertThat(arresteeSegment.getArrestTransactionNumber(), equalTo("12345"));
		assertThat(arresteeSegment.getArresteeSequenceNumber(), equalTo(1));
		assertThat(arresteeSegment.getArrestDate(), equalTo(LocalDate.of(2015, 5, 16)));
		
		assertThat(arresteeSegment.getArrestDateType().getDateTypeId(), equalTo(1962));
		assertThat(arresteeSegment.getArrestDateType().getDateMMDDYYYY(), equalTo("05162015"));
		assertThat(arresteeSegment.getTypeOfArrestType().getStateCode(), equalTo("O"));
		assertThat(arresteeSegment.getMultipleArresteeSegmentsIndicatorType().getStateCode(), 
				equalTo("N"));
		assertThat(arresteeSegment.getUcrOffenseCodeType().getStateCode(), equalTo("13A"));
		assertThat(arresteeSegment.getResidentStatusOfPersonType().getStateCode(), equalTo("R"));
		assertThat(arresteeSegment.getDispositionOfArresteeUnder18Type().getStateCode(), equalTo(" "));
		assertThat(arresteeSegment.getAgeOfArresteeMax(), equalTo(22));
		assertThat(arresteeSegment.getAgeOfArresteeMin(), equalTo(22));
		assertThat(arresteeSegment.getSexOfPersonType().getStateCode(), equalTo("M"));
		assertThat(arresteeSegment.getRaceOfPersonType().getStateCode(), equalTo("W"));
		assertThat(arresteeSegment.getEthnicityOfPersonType().getStateCode(), equalTo("U"));
		
		Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWiths = 
				arresteeSegment.getArresteeSegmentWasArmedWiths();
		assertThat(arresteeSegmentWasArmedWiths.size(), equalTo(1));
		
		ArresteeSegmentWasArmedWith arresteeSegmentWasArmedWith = arresteeSegmentWasArmedWiths.stream().findFirst().get();
		assertThat(arresteeSegmentWasArmedWith.getArresteeWasArmedWithType().getStateCode(), equalTo("01"));
		assertThat(arresteeSegmentWasArmedWith.getAutomaticWeaponIndicator(), equalTo(""));
		
//		2 VictimSegment Segments:
		Set<VictimSegment> victimSegments = updated.getVictimSegments();
		assertThat(victimSegments.size(), equalTo(2));
//		v.setTypeOfVictim("I");
//		v.setTypeOfInjury(0, "N");
//		v.setAggravatedAssaultHomicideCircumstances(0, "01");
//		v.setVictimSequenceNumber(new ParsedObject<>(2));
//		v.setAgeString("2530");
//		v.setEthnicity("N");
//		v.setResidentStatus("R");
//		v.setSex("M");
//		v.setRace("B");
//		v.setOffenderNumberRelated(0, new ParsedObject<>(1));
//		v.setVictimOffenderRelationship(0, "SE");
//		v.setUcrOffenseCodeConnection(0, "13B");
		for (VictimSegment victimSegment: victimSegments){
			assertThat(victimSegment.getAdministrativeSegment().getAdministrativeSegmentId(), equalTo(updated.getAdministrativeSegmentId())); 
			assertThat(victimSegment.getSegmentActionType().getStateCode(), equalTo("R")); 
			assertThat(victimSegment.getTypeOfVictimType().getStateCode(), equalTo("I")); 
			assertThat(victimSegment.getOfficerActivityCircumstanceType().getOfficerActivityCircumstanceTypeId(), equalTo(99998)); 
			assertThat(victimSegment.getOfficerAssignmentTypeType().getOfficerAssignmentTypeTypeId(), equalTo(99998)); 
			assertThat(victimSegment.getAgeNeonateIndicator(), equalTo(0));
			assertThat(victimSegment.getAgeFirstWeekIndicator(), equalTo(0));
			assertThat(victimSegment.getAgeFirstYearIndicator(), equalTo(0));
			assertThat(victimSegment.getRaceOfPersonType().getStateCode(), equalTo("B")); 
			assertThat(victimSegment.getEthnicityOfPersonType().getStateCode(), equalTo("N")); 
			assertThat(victimSegment.getResidentStatusOfPersonType().getStateCode(), equalTo("R")); 
			assertThat(victimSegment.getAdditionalJustifiableHomicideCircumstancesType()
					.getAdditionalJustifiableHomicideCircumstancesTypeId(), equalTo(99998));
			assertTrue(victimSegment.getAggravatedAssaultHomicideCircumstancesTypes().contains(
					aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByStateCode("01"))); 
			assertTrue(victimSegment.getTypeInjuryTypes().contains(typeInjuryTypeRepository.findFirstByStateCode("N"))); 
			assertThat(victimSegment.getAggravatedAssaultHomicideCircumstancesTypes().size(), equalTo(1)); 
			
			Set<VictimOffenderAssociation> victimOffenderAssociations = victimSegment.getVictimOffenderAssociations();
			assertThat(victimOffenderAssociations.size(), equalTo(1));
			VictimOffenderAssociation victimOffenderAssociation = new ArrayList<>(victimOffenderAssociations).get(0);
			assertThat(victimOffenderAssociation.getVictimSegment().getVictimSegmentId(), equalTo(victimSegment.getVictimSegmentId()));
			assertThat(victimOffenderAssociation.getOffenderSegment().getOffenderSequenceNumber(), equalTo(1));
			assertThat(victimOffenderAssociation.getVictimOffenderRelationshipType().getStateCode(), equalTo("SE"));
			assertThat(victimSegment.getTypeInjuryTypes().size(), equalTo(1)); 
			
			if(victimSegment.getVictimSequenceNumber().equals(1)){
				assertThat(victimSegment.getAgeOfVictimMax(), equalTo(22));
				assertThat(victimSegment.getAgeOfVictimMin(), equalTo(20));
				
				assertThat(victimSegment.getSexOfPersonType().getStateCode(), equalTo("F")); 
				
				assertThat(victimSegment.getOffenseSegments().size(), equalTo(1));
				assertThat( new ArrayList<>(victimSegment.getOffenseSegments()).get(0).getUcrOffenseCodeType().getStateCode(), equalTo("13A"));
				
			}
			else{
				assertThat(victimSegment.getAgeOfVictimMax(), equalTo(30));
				assertThat(victimSegment.getAgeOfVictimMin(), equalTo(25));
				
				assertThat(victimSegment.getSexOfPersonType().getStateCode(), equalTo("M")); 
				
				assertThat(victimSegment.getOffenseSegments().size(), equalTo(1));
				assertThat( new ArrayList<>(victimSegment.getOffenseSegments()).get(0).getUcrOffenseCodeType().getStateCode(), equalTo("13B"));
			}
		}
//		2 PropertySegment Segments:
//			PropertySegment [typeOfPropertyLoss=7, propertyDescription=[20, null, null, null, null, null, null, null, null, null], valueOfProperty=[5000, null, null, null, null, null, null, null, null, null], dateRecovered=[null, null, null, null, null, null, null, null, null, null], numberOfStolenMotorVehicles=null, numberOfRecoveredMotorVehicles=null, suspectedDrugType=[null, null, null], estimatedDrugQuantity=[null, null, null], typeDrugMeasurement=[null, null, null], populatedPropertyDescriptionCount=1, populatedSuspectedDrugTypeCount=0]
//			PropertySegment [typeOfPropertyLoss=5, propertyDescription=[20, null, null, null, null, null, null, null, null, null], valueOfProperty=[5000, null, null, null, null, null, null, null, null, null], dateRecovered=[Thu Jan 08 00:00:00 CST 2015, null, null, null, null, null, null, null, null, null], numberOfStolenMotorVehicles=null, numberOfRecoveredMotorVehicles=null, suspectedDrugType=[null, null, null], estimatedDrugQuantity=[null, null, null], typeDrugMeasurement=[null, null, null], populatedPropertyDescriptionCount=1, populatedSuspectedDrugTypeCount=0]
		
		Set<PropertySegment> propertySegments = updated.getPropertySegments();
		assertThat(propertySegments.size(), equalTo(2));
		
		for (PropertySegment propertySegment: propertySegments){
			Set<PropertyType> propertyTypes = propertySegment.getPropertyTypes();
			Set<SuspectedDrugType> suspectedDrugTypes = propertySegment.getSuspectedDrugTypes();
			switch (propertySegment.getTypePropertyLossEtcType().getStateCode()){
			case "5": 
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(null)); 
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(null)); 
				assertThat(propertyTypes.size(), equalTo(1));
				for (PropertyType propertyType: propertyTypes){
					assertThat(propertyType.getPropertyDescriptionType().getStateCode(), equalTo("20"));
					assertThat(propertyType.getValueOfProperty(), equalTo(5000.0));
					assertThat(propertyType.getRecoveredDateType().getDateMMDDYYYY(), equalTo("01082015"));
				}
				
				assertThat(suspectedDrugTypes.size(), equalTo(0));
				break; 
			case "7": 
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(null)); 
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(null)); 
				assertThat(propertyTypes.size(), equalTo(1));
				for (PropertyType propertyType: propertyTypes){
					assertThat(propertyType.getPropertyDescriptionType().getStateCode(), equalTo("20"));
					assertThat(propertyType.getValueOfProperty(), equalTo(5000.0));
					assertThat(propertyType.getRecoveredDateType().getDateTypeId(), equalTo(99998));
				}
				assertThat(suspectedDrugTypes.size(), equalTo(0));
				break; 
			default: 
				fail("Unexpected type of property loss code. ");	
			}
		}
		
	}
}
