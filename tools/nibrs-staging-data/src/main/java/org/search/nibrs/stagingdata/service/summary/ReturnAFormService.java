
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

package org.search.nibrs.stagingdata.service.summary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.codes.ClearedExceptionallyCode;
import org.search.nibrs.model.codes.LocationTypeCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.PropertyDescriptionCode;
import org.search.nibrs.model.codes.TypeOfPropertyLossCode;
import org.search.nibrs.model.reports.PropertyStolenByClassification;
import org.search.nibrs.model.reports.PropertyStolenByClassificationRowName;
import org.search.nibrs.model.reports.PropertyTypeValueRowName;
import org.search.nibrs.model.reports.ReturnAForm;
import org.search.nibrs.model.reports.ReturnARowName;
import org.search.nibrs.stagingdata.AppProperties;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.PropertyType;
import org.search.nibrs.stagingdata.model.TypeOfWeaponForceInvolved;
import org.search.nibrs.stagingdata.model.TypeOfWeaponForceInvolvedType;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenderSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.search.nibrs.stagingdata.model.segment.PropertySegment;
import org.search.nibrs.stagingdata.model.segment.VictimSegment;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.service.AdministrativeSegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnAFormService {

	private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	AdministrativeSegmentService administrativeSegmentService;
	@Autowired
	public AgencyRepository agencyRepository; 
	@Autowired
	public AppProperties appProperties; 

	private Map<String, Integer> partIOffensesMap; 
	
	public ReturnAFormService() {
		partIOffensesMap = new HashMap<>();
		partIOffensesMap.put("09A", 1); 
		partIOffensesMap.put("09B", 2); 
		partIOffensesMap.put("11A", 3); 
		partIOffensesMap.put("120", 4); 
		partIOffensesMap.put("13A", 5); 
		partIOffensesMap.put("13B", 6); 
		partIOffensesMap.put("13C", 6); 
		partIOffensesMap.put("220", 7); 
		partIOffensesMap.put("23A", 8); 
		partIOffensesMap.put("23B", 8); 
		partIOffensesMap.put("23C", 8); 
		partIOffensesMap.put("23D", 8); 
		partIOffensesMap.put("23E", 8); 
		partIOffensesMap.put("23G", 8); 
		partIOffensesMap.put("23H", 8); 
		partIOffensesMap.put("240", 9); 
		partIOffensesMap.put("23F", 10); 
	}
	
	public ReturnAForm createReturnASummaryReport(String ori, Integer year,  Integer month ) {
		
		ReturnAForm returnAForm = new ReturnAForm(ori, year, month); 
		
		if (!"StateWide".equalsIgnoreCase(ori)){
			Agency agency = agencyRepository.findFirstByAgencyOri(ori); 
			if (agency!= null){
				returnAForm.setAgencyName(agency.getAgencyName());
				returnAForm.setStateName(agency.getStateName());
				returnAForm.setStateCode(agency.getStateCode());
				returnAForm.setPopulation(agency.getPopulation());
			}
			else{
				return returnAForm; 
			}
		}
		else{
			returnAForm.setAgencyName(ori);
			returnAForm.setStateName("");
			returnAForm.setStateCode("");
			returnAForm.setPopulation(null);
		}

		processReportedOffenses(ori, year, month, returnAForm);
		processOffenseClearances(ori, year, month, returnAForm);
		
		fillTheForcibleRapeTotalRow(returnAForm);
		fillTheRobberyTotalRow(returnAForm);
		fillTheAssaultTotalRow(returnAForm);
		fillTheBurglaryTotalRow(returnAForm);
		fillTheMotorVehicleTheftTotalRow(returnAForm);
		fillTheGrandTotalRow(returnAForm);

		log.info("returnAForm: " + returnAForm);
		return returnAForm;
	}

	private void processOffenseClearances(String ori, Integer year, Integer month, ReturnAForm returnAForm) {
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentService.findIdsByOriAndClearanceDate(ori, year, month);
		
		for (AdministrativeSegment administrativeSegment: administrativeSegments){
			if (administrativeSegment.getOffenseSegments().size() == 0) continue;
			
			boolean isClearanceInvolvingOnlyJuvenile = isClearanceInvolvingOnlyJuvenile(administrativeSegment);
			
			List<OffenseSegment> offenses = getClearedOffenses(administrativeSegment);
			for (OffenseSegment offense: offenses){
				ReturnARowName returnARowName = null; 
				switch (OffenseCode.forCode(offense.getUcrOffenseCodeType().getNibrsCode())){
				case _09A:
					returnARowName = ReturnARowName.MURDER_NONNEGLIGENT_HOMICIDE;
					break; 
				case _09B: 
					returnARowName = ReturnARowName.MANSLAUGHTER_BY_NEGLIGENCE; 
					break; 
				case _11A: 
					returnARowName = getRowFor11AOffense(administrativeSegment, offense);
					break;
				case _120:
					returnARowName = getReturnARowForRobbery(offense);
					break; 
				case _13A:
					returnARowName = getReturnARowForAssault(offense);
					break;
				case _13B: 
				case _13C: 
					returnARowName = getReturnARowFor13B13COffense(offense);
					break;
				case _220: 
					countClearedBurglaryOffense(returnAForm, offense, isClearanceInvolvingOnlyJuvenile);
					break;
				case _23A: 
				case _23B:
				case _23C: 
				case _23D: 
				case _23E: 
				case _23F: 
				case _23G: 
				case _23H: 
					returnARowName = ReturnARowName.LARCENCY_THEFT_TOTAL; 
					break; 
				case _240: 
					countClearedMotorVehicleTheftOffense(returnAForm, offense, isClearanceInvolvingOnlyJuvenile );
					break; 
				default: 
				}
				
				if (returnARowName != null){
					returnAForm.getRows()[returnARowName.ordinal()].increaseClearedOffenses(1);
					
					if (isClearanceInvolvingOnlyJuvenile){
						returnAForm.getRows()[returnARowName.ordinal()].increaseClearanceInvolvingOnlyJuvenile(1);
					}
				}
			}

		}
	}

	private void countClearedMotorVehicleTheftOffense(ReturnAForm returnAForm, OffenseSegment offense,
			boolean isClearanceInvolvingOnlyJuvenile) {
		List<PropertySegment> properties =  offense.getAdministrativeSegment().getPropertySegments()
				.stream().filter(property->TypeOfPropertyLossCode._7.code.equals(property.getTypePropertyLossEtcType().getNibrsCode()))
				.collect(Collectors.toList());
		
		for (PropertySegment property: properties){
			List<String> motorVehicleCodes = property.getPropertyTypes().stream()
					.map(propertyType -> propertyType.getPropertyDescriptionType().getNibrsCode())
					.filter(code -> PropertyDescriptionCode.isMotorVehicleCode(code))
					.collect(Collectors.toList()); 
			if ("A".equals(offense.getOffenseAttemptedCompleted())){
				returnAForm.getRows()[ReturnARowName.AUTOS_THEFT.ordinal()].increaseReportedOffenses(motorVehicleCodes.size());
			}
			else if (property.getNumberOfStolenMotorVehicles() > 0){
				int numberOfStolenMotorVehicles = Optional.ofNullable(property.getNumberOfStolenMotorVehicles()).orElse(0);
				
				if (motorVehicleCodes.contains(PropertyDescriptionCode._03.code)){
					for (String code: motorVehicleCodes){
						switch (code){
						case "05":
						case "28": 
						case "37": 
							numberOfStolenMotorVehicles --; 
							returnAForm.getRows()[ReturnARowName.TRUCKS_BUSES_THEFT.ordinal()].increaseClearedOffenses(1);
							
							if(isClearanceInvolvingOnlyJuvenile){
								returnAForm.getRows()[ReturnARowName.TRUCKS_BUSES_THEFT.ordinal()].increaseClearanceInvolvingOnlyJuvenile(1);
							}
							break; 
						case "24": 
							numberOfStolenMotorVehicles --; 
							returnAForm.getRows()[ReturnARowName.OTHER_VEHICLES_THEFT.ordinal()].increaseClearedOffenses(1);
							if(isClearanceInvolvingOnlyJuvenile){
								returnAForm.getRows()[ReturnARowName.OTHER_VEHICLES_THEFT.ordinal()].increaseClearanceInvolvingOnlyJuvenile(1);
							}
							break; 
						}
					}
					
					if (numberOfStolenMotorVehicles > 0){
						returnAForm.getRows()[ReturnARowName.AUTOS_THEFT.ordinal()].increaseClearedOffenses(numberOfStolenMotorVehicles);
						if(isClearanceInvolvingOnlyJuvenile){
							returnAForm.getRows()[ReturnARowName.AUTOS_THEFT.ordinal()].increaseClearanceInvolvingOnlyJuvenile(1);
						}
					}
				}
				else if (CollectionUtils.containsAny(motorVehicleCodes, 
						Arrays.asList(PropertyDescriptionCode._05.code, PropertyDescriptionCode._28.code, PropertyDescriptionCode._37.code))){
					int countOfOtherVehicles = Long.valueOf(motorVehicleCodes.stream()
							.filter(code -> code.equals(PropertyDescriptionCode._24.code)).count()).intValue();
					numberOfStolenMotorVehicles -= Long.valueOf(countOfOtherVehicles).intValue();
					
					returnAForm.getRows()[ReturnARowName.OTHER_VEHICLES_THEFT.ordinal()].increaseClearedOffenses(countOfOtherVehicles);
					if(isClearanceInvolvingOnlyJuvenile){
						returnAForm.getRows()[ReturnARowName.OTHER_VEHICLES_THEFT.ordinal()].increaseClearanceInvolvingOnlyJuvenile(countOfOtherVehicles);
					}
					
					if (numberOfStolenMotorVehicles > 0){
						returnAForm.getRows()[ReturnARowName.TRUCKS_BUSES_THEFT.ordinal()].increaseClearedOffenses(numberOfStolenMotorVehicles);
						if(isClearanceInvolvingOnlyJuvenile){
							returnAForm.getRows()[ReturnARowName.TRUCKS_BUSES_THEFT.ordinal()].increaseClearanceInvolvingOnlyJuvenile(numberOfStolenMotorVehicles);
						}
					}
				}
				else if (motorVehicleCodes.contains(PropertyDescriptionCode._24.code)){
					returnAForm.getRows()[ReturnARowName.OTHER_VEHICLES_THEFT.ordinal()].increaseClearedOffenses(numberOfStolenMotorVehicles);
					if(isClearanceInvolvingOnlyJuvenile){
						returnAForm.getRows()[ReturnARowName.OTHER_VEHICLES_THEFT.ordinal()].increaseClearanceInvolvingOnlyJuvenile(numberOfStolenMotorVehicles);
					}
				}
			}
		}
		
	}

	private boolean isClearanceInvolvingOnlyJuvenile(AdministrativeSegment administrativeSegment) {
		boolean isClearanceInvolvingOnlyJuvenile = false; 
		if (ClearedExceptionallyCode.applicableCodeSet().contains(administrativeSegment.getClearedExceptionallyType().getNibrsCode())){
			Set<OffenderSegment> offenders = administrativeSegment.getOffenderSegments();
			isClearanceInvolvingOnlyJuvenile = offenders.stream().allMatch(offender -> offender.isJuvenile() || offender.isAgeUnknown()); 
		}
		else {
			Set<ArresteeSegment> arrestees = administrativeSegment.getArresteeSegments();
			isClearanceInvolvingOnlyJuvenile = arrestees.stream().allMatch(arrestee -> arrestee.isJuvenile() || arrestee.isAgeUnknown()); 
		}
		return isClearanceInvolvingOnlyJuvenile;
	}

	private void countClearedBurglaryOffense(ReturnAForm returnAForm, OffenseSegment offense, boolean isClearanceInvolvingOnlyJuvenile) {
		ReturnARowName returnARowName = getBurglaryRow(offense);
		
//		If there is an entry in Data Element 10 (Number of Premises Entered) and an entry of 19 
//		(Rental Storage Facility) in Data Element 9 (Location Type), use the number of premises 
//		listed in Data Element 10 as the number of burglaries to be counted.
		
		if (returnARowName != null){
			
			int increment = 1;
			int numberOfPremisesEntered = Optional.ofNullable(offense.getNumberOfPremisesEntered()).orElse(0);
			if (numberOfPremisesEntered > 0 && "19".equals(offense.getLocationType().getNibrsCode())){
				increment = offense.getNumberOfPremisesEntered(); 
			}
			
			returnAForm.getRows()[returnARowName.ordinal()].increaseClearedOffenses(increment);
			
			if (isClearanceInvolvingOnlyJuvenile){
				returnAForm.getRows()[returnARowName.ordinal()].increaseClearanceInvolvingOnlyJuvenile(increment);
			}
		}
	}

	private ReturnARowName getBurglaryRow(OffenseSegment offense) {
		ReturnARowName returnARowName = null; 
		if ("C".equals(offense.getOffenseAttemptedCompleted())){
			if (offense.getMethodOfEntryType().getNibrsCode().equals("F")){
				returnARowName = ReturnARowName.FORCIBLE_ENTRY_BURGLARY; 
			}
			else if (offense.getMethodOfEntryType().getNibrsCode().equals("N")){
				returnARowName = ReturnARowName.UNLAWFUL_ENTRY_NO_FORCE_BURGLARY; 
			}
		}
		else if ("A".equals(offense.getOffenseAttemptedCompleted()) && 
				Arrays.asList("N", "F").contains(offense.getMethodOfEntryType().getNibrsCode())){
			returnARowName = ReturnARowName.ATTEMPTED_FORCIBLE_ENTRY_BURGLARY; 
		}
		return returnARowName;
	}

	private List<OffenseSegment> getClearedOffenses(AdministrativeSegment administrativeSegment) {
		//TODO need to handle the Time-Window submission types and Time-Window offenses  
		List<OffenseSegment> offenses = new ArrayList<>(); 
		
		OffenseSegment reportingOffense = null; 
		Integer reportingOffenseValue = 99; 
		for (OffenseSegment offense: administrativeSegment.getOffenseSegments()){
			if (!Arrays.asList("A", "C").contains(offense.getOffenseAttemptedCompleted())){
				continue;
			}
			
			if (offense.getUcrOffenseCodeType().getNibrsCode().equals(OffenseCode._200.code)){
				offenses.add(offense);
				continue;
			}
			Integer offenseValue = Optional.ofNullable(partIOffensesMap.get(offense.getUcrOffenseCodeType().getNibrsCode())).orElse(99); 
			
			if (offenseValue < reportingOffenseValue){
				reportingOffense = offense; 
				reportingOffenseValue = offenseValue; 
			}
		}
		
		if (reportingOffense != null){
			offenses.add(reportingOffense);
		}
		return offenses;
	}

	private void processReportedOffenses(String ori, Integer year, Integer month, ReturnAForm returnAForm) {
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentService.findByOriAndIncidentDate(ori, year, month);

		PropertyStolenByClassification[] stolenProperties = returnAForm.getPropertyStolenByClassifications();
		for (AdministrativeSegment administrativeSegment: administrativeSegments){
			if (administrativeSegment.getOffenseSegments().size() == 0) continue; 
			
			List<OffenseSegment> offensesToReport = getReturnAOffenses(administrativeSegment); 
			for (OffenseSegment offense: offensesToReport){
				
				ReturnARowName returnARowName = null; 
				boolean hasBurglaryOffense = false; 
				boolean hasMotorVehicleTheftOffense = false; 
				double stolenPropertyValue = 0.0;
				switch (OffenseCode.forCode(offense.getUcrOffenseCodeType().getNibrsCode())){
				case _09A:
					returnARowName = ReturnARowName.MURDER_NONNEGLIGENT_HOMICIDE; 
					processStolenProperties(stolenProperties, administrativeSegment, PropertyStolenByClassificationRowName.MURDER_AND_NONNEGLIGENT_MANSLAUGHTER);	
							
					break; 
				case _09B: 
					returnARowName = ReturnARowName.MANSLAUGHTER_BY_NEGLIGENCE; 
					stolenPropertyValue = getStolenPropertyValue(administrativeSegment);
					log.info("09B offense stolen property value: " + stolenPropertyValue); 
					break; 
				//case _09C: // TODO  Not finding anything about 09C in the "Conversion of NIBRS Data to Summary Data" document. comment out this block -hw 20190110
				//	returnARowName = ReturnARowName.MURDER_NONNEGLIGENT_HOMICIDE; 
				//	returnAForm.getRows()[returnARowName.ordinal()].increaseUnfoundedOffenses(1); ///?why
				//	break; 
				case _11A: 
					returnARowName = getRowFor11AOffense(administrativeSegment, offense);
					if (returnARowName != null){
						processStolenProperties(stolenProperties, administrativeSegment, PropertyStolenByClassificationRowName.RAPE);
					}
					break;
				case _120:
					returnARowName = getReturnARowForRobbery(offense);
					if (returnARowName != null){
						processRobberyStolenPropertyByLocation(stolenProperties, offense);
					}
					break; 
				case _13A:
					returnARowName = getReturnARowForAssault(offense);
					break;
				case _13B: 
				case _13C: 
					returnARowName = getReturnARowFor13B13COffense(offense);
					break;
				case _220: 
					hasBurglaryOffense = countBurglaryOffense(returnAForm, offense);
					//Add a method to process stolen properties for burglary TODO
					break;
				case _23A: 
				case _23B:
				case _23C: 
				case _23D: 
				case _23E: 
				case _23F: 
				case _23G: 
				case _23H: 
					returnARowName = ReturnARowName.LARCENCY_THEFT_TOTAL; 
					//Add a method to process stolen properties for larcency TODO
					break; 
				case _240: 
					hasMotorVehicleTheftOffense = countMotorVehicleTheftOffense(returnAForm, offense);
					break; 
				default: 
				}
				
				if (returnARowName != null){
					returnAForm.getRows()[returnARowName.ordinal()].increaseReportedOffenses(1);
				}
				
				if (returnARowName != null || hasBurglaryOffense || hasMotorVehicleTheftOffense){
					sumPropertyValuesByType(returnAForm, administrativeSegment);
				}
			}
			
		}
		
	}

	private void processRobberyStolenPropertyByLocation(PropertyStolenByClassification[] stolenProperties,
			OffenseSegment offenseSegment) {
		log.info("offense segment LocationType: " + offenseSegment.getLocationType().getNibrsCode());
		String locationType = appProperties.getLocationCodeMapping().get(offenseSegment.getLocationType().getNibrsCode());
		if ( StringUtils.isNotBlank(locationType)){
			PropertyStolenByClassificationRowName rowName = PropertyStolenByClassificationRowName.valueOf("ROBBERY_" + locationType);
			stolenProperties[rowName.ordinal()].increaseNumberOfOffenses(1);
			stolenProperties[PropertyStolenByClassificationRowName.ROBBERY_TOTAL.ordinal()].increaseNumberOfOffenses(1);
			stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].increaseNumberOfOffenses(1);
			
			Double stolenPropertyValue = getStolenPropertyValue(offenseSegment.getAdministrativeSegment());
			stolenProperties[rowName.ordinal()].increaseMonetaryValue(stolenPropertyValue);
			stolenProperties[PropertyStolenByClassificationRowName.ROBBERY_TOTAL.ordinal()].increaseMonetaryValue(stolenPropertyValue);
			stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].increaseMonetaryValue(stolenPropertyValue);
		}
	}

	private void processStolenProperties(PropertyStolenByClassification[] stolenProperties,
			AdministrativeSegment administrativeSegment, PropertyStolenByClassificationRowName propertyStolenByClassificationRowName) {
		double stolenPropertyValue;
		stolenProperties[propertyStolenByClassificationRowName.ordinal()].increaseNumberOfOffenses(1);
		stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].increaseNumberOfOffenses(1);
		stolenPropertyValue = getStolenPropertyValue(administrativeSegment);
		stolenProperties[propertyStolenByClassificationRowName.ordinal()].increaseMonetaryValue(stolenPropertyValue);
		stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].increaseMonetaryValue(stolenPropertyValue);
	}

	private Double getStolenPropertyValue(AdministrativeSegment administrativeSegment) {
		return administrativeSegment.getPropertySegments()
				.stream()
				.filter(propertySegment -> propertySegment.getTypePropertyLossEtcType().getNibrsCode().equals("7"))
				.flatMap(i->i.getPropertyTypes().stream())
				.filter(i->i.getValueOfProperty() > 0)
				.map(PropertyType::getValueOfProperty)
				.reduce(Double::sum).orElse(0.0);
	}

	private void sumPropertyValuesByType(ReturnAForm returnAForm, AdministrativeSegment administrativeSegment) {
		for (PropertySegment propertySegment: administrativeSegment.getPropertySegments()){
			List<PropertyType> propertyTypes = propertySegment.getPropertyTypes()
					.stream()
					.filter(propertyType -> propertyType.getValueOfProperty() > 0)
					.collect(Collectors.toList()); 
			
			if (propertyTypes.size() > 0){
				for (PropertyType propertyType: propertyTypes){
					String propertyDescription = appProperties.getPropertyCodeMapping().get(propertyType.getPropertyDescriptionType().getNibrsCode());
					PropertyTypeValueRowName rowName = PropertyTypeValueRowName.valueOf(propertyDescription); 
					switch (propertySegment.getTypePropertyLossEtcType().getNibrsCode()){
					case "7":
						returnAForm.getPropertyTypeValues()[rowName.ordinal()].increaseStolen(propertyType.getValueOfProperty());
						returnAForm.getPropertyTypeValues()[PropertyTypeValueRowName.TOTAL.ordinal()].increaseStolen(propertyType.getValueOfProperty());
						break; 
					case "5":
						returnAForm.getPropertyTypeValues()[rowName.ordinal()].increaseRecovered(propertyType.getValueOfProperty());
						returnAForm.getPropertyTypeValues()[PropertyTypeValueRowName.TOTAL.ordinal()].increaseRecovered(propertyType.getValueOfProperty());
						break; 
					default:
					}
				}
			}
			
		}
	}

	private void fillTheMotorVehicleTheftTotalRow(ReturnAForm returnAForm) {
		ReturnARowName totalRow = ReturnARowName.MOTOR_VEHICLE_THEFT_TOTAL; 
		
		fillTheTotalRow(returnAForm, totalRow, ReturnARowName.AUTOS_THEFT, 
				ReturnARowName.TRUCKS_BUSES_THEFT,
				ReturnARowName.OTHER_VEHICLES_THEFT);
	}

	private void fillTheBurglaryTotalRow(ReturnAForm returnAForm) {
		ReturnARowName totalRow = ReturnARowName.BURGLARY_TOTAL; 
		
		fillTheTotalRow(returnAForm, totalRow, ReturnARowName.FORCIBLE_ENTRY_BURGLARY, 
				ReturnARowName.UNLAWFUL_ENTRY_NO_FORCE_BURGLARY,
				ReturnARowName.ATTEMPTED_FORCIBLE_ENTRY_BURGLARY);
	}

	private void fillTheAssaultTotalRow(ReturnAForm returnAForm) {
		ReturnARowName totalRow = ReturnARowName.ASSAULT_TOTAL; 
		
		fillTheTotalRow(returnAForm, totalRow, ReturnARowName.FIREARM_ASSAULT, 
				ReturnARowName.KNIFE_CUTTING_INSTRUMENT_ASSAULT,
				ReturnARowName.OTHER_DANGEROUS_WEAPON_ASSAULT, 
				ReturnARowName.HANDS_FISTS_FEET_AGGRAVATED_INJURY_ASSAULT, 
				ReturnARowName.OTHER_ASSAULT_NOT_AGGRAVATED);
	}

	private void fillTheGrandTotalRow(ReturnAForm returnAForm) {
		ReturnARowName totalRow = ReturnARowName.GRAND_TOTAL; 
		
		fillTheTotalRow(returnAForm, totalRow, ReturnARowName.MURDER_NONNEGLIGENT_HOMICIDE, 
				ReturnARowName.MANSLAUGHTER_BY_NEGLIGENCE,
				ReturnARowName.FORCIBLE_RAPE_TOTAL, 
				ReturnARowName.ROBBERY_TOTAL, 
				ReturnARowName.ASSAULT_TOTAL, 
				ReturnARowName.BURGLARY_TOTAL, 
				ReturnARowName.LARCENCY_THEFT_TOTAL, 
				ReturnARowName.MOTOR_VEHICLE_THEFT_TOTAL);
		
	}

	private void fillTheTotalRow(ReturnAForm returnAForm, ReturnARowName totalRow, ReturnARowName... rowsArray) {
		List<ReturnARowName> rows = Arrays.asList(rowsArray);
		int totalReportedOffense = 
				rows.stream()
					.mapToInt(row -> returnAForm.getRows()[row.ordinal()].getReportedOffenses())
					.sum(); 
		returnAForm.getRows()[totalRow.ordinal()].setReportedOffenses(totalReportedOffense);
		
		int totalUnfoundedOffense = 
				rows.stream()
				.mapToInt(row -> returnAForm.getRows()[row.ordinal()].getUnfoundedOffenses())
				.sum(); 
		returnAForm.getRows()[totalRow.ordinal()].setUnfoundedOffenses(totalUnfoundedOffense);
		
		int totalClearedOffense = 
				rows.stream()
				.mapToInt(row -> returnAForm.getRows()[row.ordinal()].getClearedOffenses())
				.sum(); 
		returnAForm.getRows()[totalRow.ordinal()].setClearedOffenses(totalClearedOffense);
		
		int totalClearanceInvolvingJuvenile = 
				rows.stream()
				.mapToInt(row -> returnAForm.getRows()[row.ordinal()].getClearanceInvolvingOnlyJuvenile())
				.sum(); 
		returnAForm.getRows()[totalRow.ordinal()].setClearanceInvolvingOnlyJuvenile(totalClearanceInvolvingJuvenile);
	}

	private void fillTheRobberyTotalRow(ReturnAForm returnAForm) {
		ReturnARowName totalRow = ReturnARowName.ROBBERY_TOTAL; 
		
		fillTheTotalRow(returnAForm, totalRow, ReturnARowName.FIREARM_ROBBERY, 
				ReturnARowName.KNIFE_CUTTING_INSTRUMENT_ROBBERY,
				ReturnARowName.OTHER_DANGEROUS_WEAPON_ROBBERY,
				ReturnARowName.STRONG_ARM_ROBBERY);
	}

	private void fillTheForcibleRapeTotalRow(ReturnAForm returnAForm) {
		ReturnARowName totalRow = ReturnARowName.FORCIBLE_RAPE_TOTAL; 
		
		fillTheTotalRow(returnAForm, totalRow, ReturnARowName.RAPE_BY_FORCE, 
				ReturnARowName.ATTEMPTS_TO_COMMIT_FORCIBLE_RAPE);
	}

	private boolean countMotorVehicleTheftOffense(ReturnAForm returnAForm, OffenseSegment offense) {
		
		List<PropertySegment> properties =  offense.getAdministrativeSegment().getPropertySegments()
				.stream().filter(property->TypeOfPropertyLossCode._7.code.equals(property.getTypePropertyLossEtcType().getNibrsCode()))
				.collect(Collectors.toList());
		
		int totalOffenseCount = 0;
		for (PropertySegment property: properties){
			int offenseCountInThisProperty = 0;
			List<String> motorVehicleCodes = property.getPropertyTypes().stream()
					.map(propertyType -> propertyType.getPropertyDescriptionType().getNibrsCode())
					.filter(code -> PropertyDescriptionCode.isMotorVehicleCode(code))
					.collect(Collectors.toList()); 
			
			int numberOfStolenMotorVehicles = Optional.ofNullable(property.getNumberOfStolenMotorVehicles()).orElse(0);
			
			if ("A".equals(offense.getOffenseAttemptedCompleted())){
				returnAForm.getRows()[ReturnARowName.AUTOS_THEFT.ordinal()].increaseReportedOffenses(motorVehicleCodes.size());
				offenseCountInThisProperty += motorVehicleCodes.size();
			}
			else if ( numberOfStolenMotorVehicles > 0){
				offenseCountInThisProperty += numberOfStolenMotorVehicles;
				if (motorVehicleCodes.contains(PropertyDescriptionCode._03.code)){
					for (String code: motorVehicleCodes){
						switch (code){
						case "05":
						case "28": 
						case "37": 
							numberOfStolenMotorVehicles --; 
							returnAForm.getRows()[ReturnARowName.TRUCKS_BUSES_THEFT.ordinal()].increaseReportedOffenses(1);
							break; 
						case "24": 
							numberOfStolenMotorVehicles --; 
							returnAForm.getRows()[ReturnARowName.OTHER_VEHICLES_THEFT.ordinal()].increaseReportedOffenses(1);
							break; 
						}
					}
					
					if (numberOfStolenMotorVehicles > 0){
						returnAForm.getRows()[ReturnARowName.AUTOS_THEFT.ordinal()].increaseReportedOffenses(numberOfStolenMotorVehicles);
					}
				}
				else if (CollectionUtils.containsAny(motorVehicleCodes, 
						Arrays.asList(PropertyDescriptionCode._05.code, PropertyDescriptionCode._28.code, PropertyDescriptionCode._37.code))){
					int countOfOtherVehicles = Long.valueOf(motorVehicleCodes.stream()
							.filter(code -> code.equals(PropertyDescriptionCode._24.code)).count()).intValue();
					numberOfStolenMotorVehicles -= countOfOtherVehicles;
					returnAForm.getRows()[ReturnARowName.OTHER_VEHICLES_THEFT.ordinal()].increaseReportedOffenses(countOfOtherVehicles);
					
					if (numberOfStolenMotorVehicles > 0){
						returnAForm.getRows()[ReturnARowName.TRUCKS_BUSES_THEFT.ordinal()].increaseReportedOffenses(numberOfStolenMotorVehicles);
					}
				}
				else if (motorVehicleCodes.contains(PropertyDescriptionCode._24.code)){
					returnAForm.getRows()[ReturnARowName.OTHER_VEHICLES_THEFT.ordinal()].increaseReportedOffenses(numberOfStolenMotorVehicles);
				}
			}
			
			totalOffenseCount += offenseCountInThisProperty;
			
			if (offenseCountInThisProperty > 0){
				double valueOfStolenVehicles = property.getPropertyTypes().stream()
						.filter(propertyType -> PropertyDescriptionCode.isMotorVehicleCode(propertyType.getPropertyDescriptionType().getNibrsCode()))
						.filter(propertyType->propertyType.getValueOfProperty() > 0)
						.map(PropertyType::getValueOfProperty)
						.reduce((a,b)->a+b).orElse(0.0);
				returnAForm.getPropertyStolenByClassifications()
					[PropertyStolenByClassificationRowName.MOTOR_VEHICLE_THEFT.ordinal()]
						.increaseMonetaryValue(valueOfStolenVehicles);
				returnAForm.getPropertyStolenByClassifications()
					[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()]
					.increaseMonetaryValue(valueOfStolenVehicles);
			}

		}
		
		returnAForm.getPropertyStolenByClassifications()
			[PropertyStolenByClassificationRowName.MOTOR_VEHICLE_THEFT.ordinal()]
					.increaseNumberOfOffenses(totalOffenseCount);
		returnAForm.getPropertyStolenByClassifications()
			[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()]
				.increaseNumberOfOffenses(totalOffenseCount);
		return totalOffenseCount > 0; 
	}

	private boolean countBurglaryOffense(ReturnAForm returnAForm, OffenseSegment offense) {
		ReturnARowName returnARowName = getBurglaryRow(offense);
		
		boolean hasBurglaryOffense = false; 
//		If there is an entry in Data Element 10 (Number of Premises Entered) and an entry of 19 
//		(Rental Storage Facility) in Data Element 9 (Location Type), use the number of premises 
//		listed in Data Element 10 as the number of burglaries to be counted.
		
		if (returnARowName != null){
			int numberOfPremisesEntered = Optional.ofNullable(offense.getNumberOfPremisesEntered()).orElse(0);
			if ( numberOfPremisesEntered > 0 
					&& LocationTypeCode._19.code.equals(offense.getLocationType().getNibrsCode())){
				returnAForm.getRows()[returnARowName.ordinal()].increaseReportedOffenses(offense.getNumberOfPremisesEntered());
			}
			else {
				returnAForm.getRows()[returnARowName.ordinal()].increaseReportedOffenses(1);
			}
			
			hasBurglaryOffense = true; 
		}
		return hasBurglaryOffense; 
	}

	private ReturnARowName getReturnARowFor13B13COffense(OffenseSegment offense) {
		ReturnARowName returnARowName = null; 
		boolean containsValidWeaponForceType = 
				offense.getTypeOfWeaponForceInvolveds()
				.stream()
				.filter(type -> Arrays.asList("40", "90", "95", "99", " ").contains(type.getTypeOfWeaponForceInvolvedType().getNibrsCode()))
				.count() > 0;
				
		if (containsValidWeaponForceType){
			returnARowName = ReturnARowName.OTHER_ASSAULT_NOT_AGGRAVATED;
		}
		return returnARowName;
	}

	private ReturnARowName getReturnARowForRobbery(OffenseSegment offense) {
		List<String> typeOfWeaponInvolvedCodes = offense.getTypeOfWeaponForceInvolveds()
				.stream()
				.map(TypeOfWeaponForceInvolved::getTypeOfWeaponForceInvolvedType)
				.map(TypeOfWeaponForceInvolvedType::getNibrsCode)
				.collect(Collectors.toList()); 

		if (CollectionUtils.containsAny(typeOfWeaponInvolvedCodes, Arrays.asList("11", "12", "13", "14", "15"))){
			return ReturnARowName.FIREARM_ROBBERY; 
		}
		else if (typeOfWeaponInvolvedCodes.contains("20")){
			return ReturnARowName.KNIFE_CUTTING_INSTRUMENT_ROBBERY;
		}
		else if (CollectionUtils.containsAny(typeOfWeaponInvolvedCodes, 
				Arrays.asList("30", "35", "50", "60", "65", "70", "85", "90", "95"))){
			return ReturnARowName.OTHER_DANGEROUS_WEAPON_ROBBERY;
		}
		else if (CollectionUtils.containsAny(typeOfWeaponInvolvedCodes, 
				Arrays.asList("40", "99"))){
			return ReturnARowName.STRONG_ARM_ROBBERY;
		}
			
		return null;
	}

	private ReturnARowName getReturnARowForAssault(OffenseSegment offense) {
		List<String> typeOfWeaponInvolvedCodes = offense.getTypeOfWeaponForceInvolveds()
				.stream()
				.map(TypeOfWeaponForceInvolved::getTypeOfWeaponForceInvolvedType)
				.map(TypeOfWeaponForceInvolvedType::getNibrsCode)
				.collect(Collectors.toList()); 
		
		if (CollectionUtils.containsAny(typeOfWeaponInvolvedCodes, Arrays.asList("11", "12", "13", "14", "15"))){
			return ReturnARowName.FIREARM_ASSAULT; 
		}
		else if (typeOfWeaponInvolvedCodes.contains("20")){
			return ReturnARowName.KNIFE_CUTTING_INSTRUMENT_ASSAULT;
		}
		else if (CollectionUtils.containsAny(typeOfWeaponInvolvedCodes, 
				Arrays.asList("30", "35", "50", "60", "65", "70", "85", "90", "95"))){
			return ReturnARowName.OTHER_DANGEROUS_WEAPON_ASSAULT;
		}
		else if (CollectionUtils.containsAny(typeOfWeaponInvolvedCodes, 
				Arrays.asList("40", "99"))){
			return ReturnARowName.HANDS_FISTS_FEET_AGGRAVATED_INJURY_ASSAULT;
		}
		
		return null;
	}
	
	private ReturnARowName getRowFor11AOffense(AdministrativeSegment administrativeSegment,
			OffenseSegment offense) {
		
		ReturnARowName returnARowName = null;
		List<VictimSegment> victimSegments = administrativeSegment.getVictimSegments()
			.stream().filter(victim->victim.getOffenseSegments().contains(offense))
			.filter(victim->victim.getSexOfPersonType().getNibrsCode().equals("F"))
			.collect(Collectors.toList());
		if (victimSegments.size() > 0){
			switch (offense.getOffenseAttemptedCompleted()){
			case "C": 
				returnARowName = ReturnARowName.RAPE_BY_FORCE;
				break; 
			case "A": 
				returnARowName = ReturnARowName.ATTEMPTS_TO_COMMIT_FORCIBLE_RAPE;
				break; 
			default: 
			}
		}
		
		return returnARowName;
	}

	private List<OffenseSegment> getReturnAOffenses(AdministrativeSegment administrativeSegment) {
		List<OffenseSegment> offenses = new ArrayList<>(); 
		
		OffenseSegment reportingOffense = null; 
		Integer reportingOffenseValue = 99; 
		for (OffenseSegment offense: administrativeSegment.getOffenseSegments()){
			if (!Arrays.asList("A", "C").contains(offense.getOffenseAttemptedCompleted())){
				continue;
			}
			
			if (offense.getUcrOffenseCodeType().getNibrsCode().equals(OffenseCode._09C.code)){
				offenses.add(offense);
				continue;
			}
			Integer offenseValue = Optional.ofNullable(partIOffensesMap.get(offense.getUcrOffenseCodeType().getNibrsCode())).orElse(99); 
			
			if (offenseValue < reportingOffenseValue){
				reportingOffense = offense; 
				reportingOffenseValue = offenseValue; 
			}
		}
		
		if (reportingOffense != null){
			offenses.add(reportingOffense);
		}
		return offenses;
	}
	
	
}
