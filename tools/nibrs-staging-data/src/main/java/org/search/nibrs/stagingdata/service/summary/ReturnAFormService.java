
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

import static org.search.nibrs.stagingdata.util.ObjectUtils.getInteger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	private Map<String, Integer> larcenyOffenseImportanceMap; 
	private Map<String, PropertyStolenByClassificationRowName> larcenyOffenseByNatureMap; 

	private List<Integer> propertyTypeIds; 
	private List<String> incidentNumbers; 
	public ReturnAFormService() {
		partIOffensesMap = new HashMap<>();
		partIOffensesMap.put("09A", 1); 
		partIOffensesMap.put("09B", 2); 
		partIOffensesMap.put("11A", 3); 
		partIOffensesMap.put("11B", 4); 
		partIOffensesMap.put("11C", 5); 
		partIOffensesMap.put("120", 6); 
		partIOffensesMap.put("13A", 7); 
		partIOffensesMap.put("13B", 8); 
		partIOffensesMap.put("13C", 8); 
		partIOffensesMap.put("220", 9); 
		partIOffensesMap.put("23B", 10); 
		partIOffensesMap.put("23A", 10); 
		partIOffensesMap.put("23C", 10); 
		partIOffensesMap.put("23D", 10); 
		partIOffensesMap.put("23E", 10); 
		partIOffensesMap.put("23F", 10); 
		partIOffensesMap.put("23G", 10); 
		partIOffensesMap.put("23H", 10); 
		partIOffensesMap.put("240", 11); 
				
		larcenyOffenseByNatureMap = new HashMap<>();
		larcenyOffenseByNatureMap.put("23B", PropertyStolenByClassificationRowName.LARCENY_PURSE_SNATCHING);  // Purse-snatching
		larcenyOffenseByNatureMap.put("23A", PropertyStolenByClassificationRowName.LARCENY_POCKET_PICKING);  // Pocket-picking
		larcenyOffenseByNatureMap.put("23C", PropertyStolenByClassificationRowName.LARCENY_SHOPLIFTING); // Shoplifting
		larcenyOffenseByNatureMap.put("23D", PropertyStolenByClassificationRowName.LARCENY_FROM_BUILDING); // Theft from building
		larcenyOffenseByNatureMap.put("23G", PropertyStolenByClassificationRowName.LARCENY_MOTOR_VEHICLE_PARTS_AND_ACCESSORIES);  // Theft of Motor Vehicle Parts or Accessories
		larcenyOffenseByNatureMap.put("23H38", PropertyStolenByClassificationRowName.LARCENY_MOTOR_VEHICLE_PARTS_AND_ACCESSORIES); // Theft of Motor Vehicle Parts or Accessories
		larcenyOffenseByNatureMap.put("23F", PropertyStolenByClassificationRowName.LARCENY_FROM_MOTOR_VEHICLES);  // Theft from motor Vehicles
		larcenyOffenseByNatureMap.put("23E", PropertyStolenByClassificationRowName.LARCENY_FROM_COIN_OPERATED_MACHINES);  // Theft from Coin Operated machines and device
		larcenyOffenseByNatureMap.put("23H04", PropertyStolenByClassificationRowName.LARCENY_BICYCLES); // Bicycles 
		larcenyOffenseByNatureMap.put("23H", PropertyStolenByClassificationRowName.LARCENY_ALL_OTHER);  // All Other
		
		larcenyOffenseImportanceMap = new HashMap<>();
		larcenyOffenseImportanceMap.put("23B", 1);
		larcenyOffenseImportanceMap.put("23A", 2);
		larcenyOffenseImportanceMap.put("23C", 3);
		larcenyOffenseImportanceMap.put("23D", 4);
		larcenyOffenseImportanceMap.put("23G", 5);
		larcenyOffenseImportanceMap.put("23H38", 5);
		larcenyOffenseImportanceMap.put("23F", 6);
		larcenyOffenseImportanceMap.put("23E", 7);
		larcenyOffenseImportanceMap.put("23H04", 8);
		larcenyOffenseImportanceMap.put("23H", 9);
	}
	
	public ReturnAForm createReturnASummaryReport(String ownerId, String ori, Integer year,  Integer month ) {
		
		ReturnAForm returnAForm = new ReturnAForm(ori, year, month); 
		
		propertyTypeIds = new ArrayList<>();
		incidentNumbers = new ArrayList<>();
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

		processReportedOffenses(ownerId, ori, year, month, returnAForm);
		processOffenseClearances(ownerId, ori, year, month, returnAForm);
		
		fillTheForcibleRapeTotalRow(returnAForm);
		fillTheRobberyTotalRow(returnAForm);
		fillTheAssaultTotalRow(returnAForm);
		fillTheBurglaryTotalRow(returnAForm);
		fillTheMotorVehicleTheftTotalRow(returnAForm);
		fillTheGrandTotalRow(returnAForm);

		log.info("returnAForm: " + returnAForm);
		return returnAForm;
	}

	private void processOffenseClearances(String ownerId, String ori, Integer year, Integer month, ReturnAForm returnAForm) {
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentService.findByOriAndClearanceDateAndOffenses(getInteger(ownerId), ori, year, month, new ArrayList<>(partIOffensesMap.keySet()));

		for (AdministrativeSegment administrativeSegment: administrativeSegments){
			if (administrativeSegment.getOffenseSegments().size() == 0) continue;
			
			boolean isClearanceInvolvingOnlyJuvenile = administrativeSegment.isClearanceInvolvingOnlyJuvenile();
			
			List<OffenseSegment> offenses = getClearedOffenses(administrativeSegment);
			for (OffenseSegment offense: offenses){
				ReturnARowName returnARowName = null; 
				int offenseCount = 1; 
				switch (OffenseCode.forCode(offense.getUcrOffenseCodeType().getNibrsCode())){
				case _09A:
					returnARowName = ReturnARowName.MURDER_NONNEGLIGENT_HOMICIDE;
					offenseCount = getOffenseCountByConnectedVictim(administrativeSegment, offense);
					break; 
				case _09B: 
					returnARowName = ReturnARowName.MANSLAUGHTER_BY_NEGLIGENCE; 
					offenseCount = getOffenseCountByConnectedVictim(administrativeSegment, offense);
					break; 
				case _11A: 
				case _11B:
				case _11C:
					returnARowName = getRowFor11AOffense(administrativeSegment, offense);
					offenseCount = getOffenseCountByConnectedVictim(administrativeSegment, "11A", "11B", "11C");
					break;
				case _120:
					returnARowName = getReturnARowForRobbery(offense);
					break; 
				case _13A:
					returnARowName = getReturnARowForAssault(offense);
					offenseCount = getOffenseCountByConnectedVictim(administrativeSegment, offense);
					break;
				case _13B: 
				case _13C: 
					returnARowName = getReturnARowFor13B13COffense(offense);
					offenseCount = getOffenseCountByConnectedVictim(administrativeSegment, offense);
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
					returnARowName = ReturnARowName.LARCENY_THEFT_TOTAL; 
					break; 
				case _240: 
					countClearedMotorVehicleTheftOffense(returnAForm, offense, isClearanceInvolvingOnlyJuvenile );
					break; 
				default: 
				}
				
				if (returnARowName != null){
					returnAForm.getRows()[returnARowName.ordinal()].increaseClearedOffenses(offenseCount);
					
					if (isClearanceInvolvingOnlyJuvenile){
						returnAForm.getRows()[returnARowName.ordinal()].increaseClearanceInvolvingOnlyJuvenile(offenseCount);
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

	private void processReportedOffenses(String ownerId, String ori, Integer year, Integer month, ReturnAForm returnAForm) {
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentService.findByOriAndIncidentDateAndOffenses(getInteger(ownerId), ori, year, month, new ArrayList(partIOffensesMap.keySet()));
		
		PropertyStolenByClassification[] stolenProperties = returnAForm.getPropertyStolenByClassifications();
		for (AdministrativeSegment administrativeSegment: administrativeSegments){
			if (administrativeSegment.getOffenseSegments().size() == 0) continue; 
			
			List<OffenseSegment> offensesToReport = getReturnAOffenses(administrativeSegment); 
			for (OffenseSegment offense: offensesToReport){
				
				ReturnARowName returnARowName = null; 
				int burglaryOffenseCount = 0; 
				int offenseCount = 1; 
				boolean hasMotorVehicleTheftOffense = false; 
				double stolenPropertyValue = 0.0;
				OffenseCode offenseCode = OffenseCode.forCode(offense.getUcrOffenseCodeType().getNibrsCode()); 
				switch (offenseCode){
				case _09A:
					returnARowName = ReturnARowName.MURDER_NONNEGLIGENT_HOMICIDE;
					offenseCount = getOffenseCountByConnectedVictim(administrativeSegment, offense);
					processStolenProperties(stolenProperties, administrativeSegment, PropertyStolenByClassificationRowName.MURDER_AND_NONNEGLIGENT_MANSLAUGHTER, offenseCount);	
					sumPropertyValuesByType(returnAForm, administrativeSegment);
					break; 
				case _09B: 
					returnARowName = ReturnARowName.MANSLAUGHTER_BY_NEGLIGENCE; 
					offenseCount = getOffenseCountByConnectedVictim(administrativeSegment, offense);
					stolenPropertyValue = getStolenPropertyValue(administrativeSegment, 0);
					//log.info("09B offense stolen property value: " + stolenPropertyValue); 
					break; 
				//case _09C: // TODO  Not finding anything about 09C in the "Conversion of NIBRS Data to Summary Data" document. comment out this block -hw 20190110
				//	returnARowName = ReturnARowName.MURDER_NONNEGLIGENT_HOMICIDE; 
				//	returnAForm.getRows()[returnARowName.ordinal()].increaseUnfoundedOffenses(1); ///?why
				//	break; 
				case _11A:
				case _11B:
				case _11C:
					returnARowName = getRowFor11AOffense(administrativeSegment, offense);
					offenseCount = getOffenseCountByConnectedVictim(administrativeSegment, "11A", "11B", "11C");
					if (returnARowName != null){
						processStolenProperties(stolenProperties, administrativeSegment, PropertyStolenByClassificationRowName.RAPE, offenseCount);
						sumPropertyValuesByType(returnAForm, administrativeSegment);
					}
					break;
				case _120:
					returnARowName = getReturnARowForRobbery(offense);
					if (returnARowName != null){
						processRobberyStolenPropertyByLocation(stolenProperties, offense);
						sumPropertyValuesByType(returnAForm, administrativeSegment);
					}
					break; 
				case _13A:
					returnARowName = getReturnARowForAssault(offense);
					offenseCount = getOffenseCountByConnectedVictim(administrativeSegment, offense);
					break;
				case _13B: 
				case _13C: 
					returnARowName = getReturnARowFor13B13COffense(offense);
					offenseCount = getOffenseCountByConnectedVictim(administrativeSegment, "13B", "13C");
//					log.debug("return A row name is 13B or 13C: " + returnARowName != null?returnARowName:"null");
//					if (returnARowName != null) {
//						log.debug("returnAForm.getRows()[returnARowName.ordinal()]: " + returnAForm.getRows()[returnARowName.ordinal()].getReportedOffenses());
//						log.debug("13B13C count increase:" + offenseCount);
//					}
					break;
				case _220: 
					burglaryOffenseCount = countBurglaryOffense(returnAForm, offense);
					break;
				case _23A: 
				case _23B:
				case _23C: 
				case _23D: 
				case _23E: 
				case _23F: 
				case _23G: 
				case _23H: 
					returnARowName = ReturnARowName.LARCENY_THEFT_TOTAL; 
					processLarcenyStolenPropertyByValue(stolenProperties, administrativeSegment);
					processLarcenyStolenPropertyByNature(stolenProperties, offenseCode, administrativeSegment);
					sumPropertyValuesByType(returnAForm, administrativeSegment);
					break; 
				case _240: 
					hasMotorVehicleTheftOffense = countMotorVehicleTheftOffense(returnAForm, offense);
					break; 
				default: 
				}
				
				if (returnARowName != null){
					returnAForm.getRows()[returnARowName.ordinal()].increaseReportedOffenses(offenseCount);
				}
				
				if ( burglaryOffenseCount > 0 || hasMotorVehicleTheftOffense){
					sumPropertyValuesByType(returnAForm, administrativeSegment);
				}
				
//				log.info("ReturnA property by type stolen total: " + returnAForm.getPropertyTypeValues()[PropertyTypeValueRowName.TOTAL.ordinal()].getStolen());
//				log.info("ReturnA property by classification stolen total: " + stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].getMonetaryValue());
//				log.info("debug"); 
			}
			
		}
		
	}

	private int getOffenseCountByConnectedVictim(AdministrativeSegment administrativeSegment, OffenseSegment offense) {
		long offenseCount = administrativeSegment.getVictimSegments()
				.stream()
				.filter(victim->victim.getOffenseSegments().contains(offense))
				.count();
		return Long.valueOf(offenseCount).intValue();
	}

	private int getOffenseCountByConnectedVictim(AdministrativeSegment administrativeSegment, String... offenseCodes) {
		int offenseCount = 0; 
		
		
		for (VictimSegment victimSegment: administrativeSegment.getVictimSegments()) {
			List<String> victimConnectedOffenseCodes = victimSegment.getConnectedOffenseCodes();
			
			boolean connectedToRape = CollectionUtils.containsAny(victimConnectedOffenseCodes, Arrays.asList(offenseCodes));
			if (connectedToRape) {
				offenseCount += 1; 
			}
			
		}
		
		return offenseCount;
	}
	
	private void processLarcenyStolenPropertyByNature(PropertyStolenByClassification[] stolenProperties, OffenseCode offenseCode, 
			AdministrativeSegment administrativeSegment) {
		
		List<String> offenseCodes =  administrativeSegment.getOffenseSegments()
				.stream()
				.map(i -> i.getUcrOffenseCodeType().getNibrsCode())
				.collect(Collectors.toList()); 
		List<String> larcenyOffenseCodes = administrativeSegment.getOffenseSegments()
				.stream()
				.map(i -> i.getUcrOffenseCodeType().getNibrsCode())
				.filter(OffenseCode::isLarcenyOffenseCode)
				.collect(Collectors.toList());
		
		List<String> convertedLarcenyOffenseCodes = 
				larcenyOffenseCodes.stream().map(i-> convert23H(i, administrativeSegment)).collect(Collectors.toList());
		
//		log.info("convertedLarcenyOffenseCodes:" + convertedLarcenyOffenseCodes);
		String offenseCodeString = StringUtils.EMPTY; 
		Integer larcenyOffenseImportance = 99; 
		for (String code: convertedLarcenyOffenseCodes) {
			Integer importanceOfCode = larcenyOffenseImportanceMap.get(code); 
			if (importanceOfCode != null && importanceOfCode < larcenyOffenseImportance) {
				larcenyOffenseImportance = importanceOfCode; 
				offenseCodeString = code; 
			}
		}
		
		
		PropertyStolenByClassificationRowName propertyStolenByClassificationRowName = larcenyOffenseByNatureMap.get(offenseCodeString);
//		if ("23D".equals(offenseCodeString)) {
		if (propertyStolenByClassificationRowName == PropertyStolenByClassificationRowName.LARCENY_MOTOR_VEHICLE_PARTS_AND_ACCESSORIES) {
			log.info("offenseCodes: " + offenseCodes);
		}
		
		stolenProperties[propertyStolenByClassificationRowName.ordinal()].increaseNumberOfOffenses(1);
		stolenProperties[PropertyStolenByClassificationRowName.LARCENIES_TOTAL_BY_NATURE.ordinal()].increaseNumberOfOffenses(1);

		double stolenPropertyValue = getStolenPropertyValue(administrativeSegment, 0);
		stolenProperties[propertyStolenByClassificationRowName.ordinal()].increaseMonetaryValue(stolenPropertyValue);
//		if ("23D".equals(offenseCodeString)) {
//			log.info("propertyTypes:" + administrativeSegment.getPropertySegments()
//						.stream()
//						.filter(propertySegment -> propertySegment.getTypePropertyLossEtcType().getNibrsCode().equals("7"))
//						.flatMap(i->i.getPropertyTypes().stream())
//						.map(i->i.getValueOfProperty())
//						.collect(Collectors.toList()));
//			log.info("stolenPropertyValue: " + stolenPropertyValue);
//			log.info("stolenProperties[LARCENY_FROM_BUILDING]: " + stolenProperties[propertyStolenByClassificationRowName.ordinal()].getMonetaryValue());
//			incidentNumbers.add(administrativeSegment.getIncidentNumber());
//			log.info("23D incidentNumbers: " + StringUtils.join(incidentNumbers, ","));
//			propertyTypeIds.add(administrativeSegment.getAdministrativeSegmentId()); 
//			log.info("23D administrativeSegmentIds: " + StringUtils.join(propertyTypeIds, ","));
//		}
		if (propertyStolenByClassificationRowName == PropertyStolenByClassificationRowName.LARCENY_MOTOR_VEHICLE_PARTS_AND_ACCESSORIES) {
			log.info("propertyTypes:" + administrativeSegment.getPropertySegments()
						.stream()
						.filter(propertySegment -> propertySegment.getTypePropertyLossEtcType().getNibrsCode().equals("7"))
						.flatMap(i->i.getPropertyTypes().stream())
						.map(i->i.getValueOfProperty())
						.collect(Collectors.toList()));
			log.info("stolenPropertyValue: " + stolenPropertyValue);
			log.info("stolenProperties[LARCENY_MOTOR_VEHICLE_PARTS_AND_ACCESSORIES]: " + stolenProperties[propertyStolenByClassificationRowName.ordinal()].getMonetaryValue());
			incidentNumbers.add(administrativeSegment.getIncidentNumber());
			log.info("23H38 23G incidentNumbers: " + StringUtils.join(incidentNumbers, ","));
			propertyTypeIds.add(administrativeSegment.getAdministrativeSegmentId()); 
			log.info("23H38 23G administrativeSegmentIds: " + StringUtils.join(propertyTypeIds, ","));
		}
		stolenProperties[PropertyStolenByClassificationRowName.LARCENIES_TOTAL_BY_NATURE.ordinal()].increaseMonetaryValue(stolenPropertyValue);
	}

	private String convert23H(String offenseCodeString, AdministrativeSegment administrativeSegment) {
		if ("23H".equals(offenseCodeString)){
			List<PropertyType> stolenPropertyTypes =  administrativeSegment.getPropertySegments()
					.stream()
					.filter(propertySegment -> propertySegment.getTypePropertyLossEtcType().getNibrsCode().equals("7"))
					.flatMap(i->i.getPropertyTypes().stream())
					.filter(i->i.getValueOfProperty() > 0)
					.collect(Collectors.toList());
			
			if (stolenPropertyTypes.size() > 0){
				PropertyType propertyTypeWithMaxValue = Collections.max(stolenPropertyTypes, Comparator.comparing(PropertyType::getValueOfProperty));
				if ("38".equals(propertyTypeWithMaxValue.getPropertyDescriptionType().getNibrsCode())
						|| "04".equals(propertyTypeWithMaxValue.getPropertyDescriptionType().getNibrsCode())){
					offenseCodeString += propertyTypeWithMaxValue.getPropertyDescriptionType().getNibrsCode(); 
				}
			}
		}
		return offenseCodeString;
	}

	private void processLarcenyStolenPropertyByValue(PropertyStolenByClassification[] stolenProperties, AdministrativeSegment administrativeSegment) {
		double stolenPropertyValue = getStolenPropertyValue(administrativeSegment, 0);
		PropertyStolenByClassificationRowName propertyStolenByClassificationRowName = null;
		
		if (stolenPropertyValue >= 200.0){
			propertyStolenByClassificationRowName = PropertyStolenByClassificationRowName.LARCENY_200_PLUS;
		}
		else if (stolenPropertyValue >= 50 ){
			propertyStolenByClassificationRowName = PropertyStolenByClassificationRowName.LARCENY_50_199;
		}
		else{
			propertyStolenByClassificationRowName = PropertyStolenByClassificationRowName.LARCENY_UNDER_50;
		}
		
		stolenProperties[propertyStolenByClassificationRowName.ordinal()].increaseNumberOfOffenses(1);
		stolenProperties[propertyStolenByClassificationRowName.ordinal()].increaseMonetaryValue(stolenPropertyValue);
		
//		if (stolenPropertyValue >= 200.0){
//			log.info("administrativeSegment ID: " + administrativeSegment.getAdministrativeSegmentId());
//			log.info("offenses: " + administrativeSegment.getOffenseSegments().stream().map(i->i.getUcrOffenseCodeType().getNibrsCode()).collect(Collectors.toList()));
//			log.info("$200 AND OVER total: " + stolenProperties[propertyStolenByClassificationRowName.ordinal()].getMonetaryValue());
//		}
		
		stolenProperties[PropertyStolenByClassificationRowName.LARCENY_TOTAL.ordinal()].increaseNumberOfOffenses(1);
		stolenProperties[PropertyStolenByClassificationRowName.LARCENY_TOTAL.ordinal()].increaseMonetaryValue(stolenPropertyValue);
		stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].increaseNumberOfOffenses(1);
		stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].increaseMonetaryValue(stolenPropertyValue);
	}

	private void processRobberyStolenPropertyByLocation(PropertyStolenByClassification[] stolenProperties,
			OffenseSegment offenseSegment) {
		String locationType = appProperties.getLocationCodeMapping().get(offenseSegment.getLocationType().getNibrsCode());
		if ( StringUtils.isNotBlank(locationType)){
			PropertyStolenByClassificationRowName rowName = PropertyStolenByClassificationRowName.valueOf("ROBBERY_" + locationType);
			stolenProperties[rowName.ordinal()].increaseNumberOfOffenses(1);
			stolenProperties[PropertyStolenByClassificationRowName.ROBBERY_TOTAL.ordinal()].increaseNumberOfOffenses(1);
			stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].increaseNumberOfOffenses(1);
			
			Double stolenPropertyValue = getStolenPropertyValue(offenseSegment.getAdministrativeSegment(), 0);
			stolenProperties[rowName.ordinal()].increaseMonetaryValue(stolenPropertyValue);
			stolenProperties[PropertyStolenByClassificationRowName.ROBBERY_TOTAL.ordinal()].increaseMonetaryValue(stolenPropertyValue);
			stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].increaseMonetaryValue(stolenPropertyValue);
		}
	}

	private void processStolenProperties(PropertyStolenByClassification[] stolenProperties,
			AdministrativeSegment administrativeSegment, PropertyStolenByClassificationRowName propertyStolenByClassificationRowName, int offenseCount) {
		double stolenPropertyValue;
		stolenProperties[propertyStolenByClassificationRowName.ordinal()].increaseNumberOfOffenses(offenseCount);
		stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].increaseNumberOfOffenses(offenseCount);
		stolenPropertyValue = getStolenPropertyValue(administrativeSegment, 0);
		stolenProperties[propertyStolenByClassificationRowName.ordinal()].increaseMonetaryValue(stolenPropertyValue);
		stolenProperties[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()].increaseMonetaryValue(stolenPropertyValue);
	}

	private Double getStolenPropertyValue(AdministrativeSegment administrativeSegment, int lowerLimit) {
		return administrativeSegment.getPropertySegments()
				.stream()
				.filter(propertySegment -> propertySegment.getTypePropertyLossEtcType().getNibrsCode().equals("7"))
				.flatMap(i->i.getPropertyTypes().stream())
				.filter(i->i.getValueOfProperty() > lowerLimit)
				.map(PropertyType::getValueOfProperty)
				.reduce(Double::sum).orElse(0.0);
	}

	private void sumPropertyValuesByType(ReturnAForm returnAForm, AdministrativeSegment administrativeSegment) {
		for (PropertySegment propertySegment: administrativeSegment.getPropertySegments()){
			List<PropertyType> propertyTypes = propertySegment.getPropertyTypes()
					.stream()
					.filter(propertyType -> propertyType.getValueOfProperty() != null)
					.collect(Collectors.toList()); 
			
			if (propertyTypes.size() > 0){
				for (PropertyType propertyType: propertyTypes){
					String propertyDescription = appProperties.getPropertyCodeMapping().get(propertyType.getPropertyDescriptionType().getNibrsCode());
					if (StringUtils.isNotBlank(propertyDescription)) {
						PropertyTypeValueRowName rowName = PropertyTypeValueRowName.valueOf(propertyDescription); 
						switch (propertySegment.getTypePropertyLossEtcType().getNibrsCode()){
						case "7":
							returnAForm.getPropertyTypeValues()[rowName.ordinal()].increaseStolen(propertyType.getValueOfProperty());
							returnAForm.getPropertyTypeValues()[PropertyTypeValueRowName.TOTAL.ordinal()].increaseStolen(propertyType.getValueOfProperty());
							break; 
						case "5":
							returnAForm.getPropertyTypeValues()[rowName.ordinal()].increaseRecovered(propertyType.getValueOfProperty());
//							if (rowName == PropertyTypeValueRowName.CURRENCY_NOTES_ETC) {
//								log.info("***********************************************************");
//								log.info("TypePropertyLossEtcType: " + propertySegment.getTypePropertyLossEtcType().getNibrsCode());
//								log.info("administrativeSegmentId: " + administrativeSegment.getAdministrativeSegmentId());
//								log.info("incidentNumber: " + administrativeSegment.getIncidentNumber());
//								log.info("incidentDate: " + administrativeSegment.getIncidentDate());
//								log.info("propertySegmentId: " + propertySegment.getPropertySegmentId());
//								log.info("propertyTypeId: " + propertyType.getPropertyTypeId());
//								log.info("property description: " + propertyType.getPropertyDescriptionType().getNibrsCode());
//								log.info("valueOfProperty(): " + propertyType.getValueOfProperty());
//								log.info("recovered Date: " + propertyType.getRecoveredDate());
//								log.info("recovered currency amount total: " + returnAForm.getPropertyTypeValues()[rowName.ordinal()].getRecovered());
//							}
//							if (rowName == PropertyTypeValueRowName.CONSUMABLE_GOODS) {
//								propertyTypeIds.add(propertyType.getPropertyTypeId());
//								log.info("***********************************************************");
//								log.info("incidentNumber: " + administrativeSegment.getIncidentNumber());
//								log.info("property description: " + propertyType.getPropertyDescriptionType().getNibrsCode());
//								log.info("valueOfProperty(): " + propertyType.getValueOfProperty());
//								log.info("incidentDate: " + administrativeSegment.getIncidentDate());
//								log.info("TypePropertyLossEtcType: " + propertySegment.getTypePropertyLossEtcType().getNibrsCode());
//								log.info("administrativeSegmentId: " + administrativeSegment.getAdministrativeSegmentId());
//								log.info("propertySegmentId: " + propertySegment.getPropertySegmentId());
//								log.info("propertyTypeId: " + propertyType.getPropertyTypeId());
//								log.info("recovered Date: " + propertyType.getRecoveredDate());
//								log.info("recovered currency amount total: " + returnAForm.getPropertyTypeValues()[rowName.ordinal()].getRecovered());
//								log.info("propertyIds so far: " + StringUtils.join(propertyTypeIds, ",")); 
//							}
							returnAForm.getPropertyTypeValues()[PropertyTypeValueRowName.TOTAL.ordinal()].increaseRecovered(propertyType.getValueOfProperty());
							break; 
						default:
						}
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
				ReturnARowName.LARCENY_THEFT_TOTAL, 
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
		
		int totalOffenseCount = 0;
		if ("A".equals(offense.getOffenseAttemptedCompleted())){
			returnAForm.getRows()[ReturnARowName.AUTOS_THEFT.ordinal()].increaseReportedOffenses(1);
			totalOffenseCount = 1;
		}
		else {
			List<PropertySegment> properties =  offense.getAdministrativeSegment().getPropertySegments()
					.stream().filter(property->TypeOfPropertyLossCode._7.code.equals(property.getTypePropertyLossEtcType().getNibrsCode()))
					.collect(Collectors.toList());
			
			for (PropertySegment property: properties){
				int offenseCountInThisProperty = 0;
				List<String> motorVehicleCodes = property.getPropertyTypes().stream()
						.map(propertyType -> propertyType.getPropertyDescriptionType().getNibrsCode())
						.filter(code -> PropertyDescriptionCode.isMotorVehicleCode(code))
						.collect(Collectors.toList()); 
				
				int numberOfStolenMotorVehicles = Optional.ofNullable(property.getNumberOfStolenMotorVehicles()).orElse(0);
				
//				log.info("offense.getOffenseAttemptedCompleted():" + offense.getOffenseAttemptedCompleted()); 
				if ( numberOfStolenMotorVehicles > 0){
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
					double valueOfStolenProperty = getStolenPropertyValue(offense.getAdministrativeSegment(), 0);
					returnAForm.getPropertyStolenByClassifications()
						[PropertyStolenByClassificationRowName.MOTOR_VEHICLE_THEFT.ordinal()]
							.increaseMonetaryValue(valueOfStolenProperty);
					returnAForm.getPropertyStolenByClassifications()
						[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()]
						.increaseMonetaryValue(valueOfStolenProperty);
				}
	
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

	private int countBurglaryOffense(ReturnAForm returnAForm, OffenseSegment offense) {
		ReturnARowName returnARowName = getBurglaryRow(offense);
		
		int burglaryOffenseCount = 0; 
//		If there is an entry in Data Element 10 (Number of Premises Entered) and an entry of 19 
//		(Rental Storage Facility) in Data Element 9 (Location Type), use the number of premises 
//		listed in Data Element 10 as the number of burglaries to be counted.
		if (returnARowName != null){
			int numberOfPremisesEntered = Optional.ofNullable(offense.getNumberOfPremisesEntered()).orElse(0);
//			log.info("numberOfPremisesEntered:" + numberOfPremisesEntered);
//			log.info("LocationTypeCode._19.code.equals(offense.getLocationType().getNibrsCode()):" + LocationTypeCode._19.code.equals(offense.getLocationType().getNibrsCode()));
			if ( numberOfPremisesEntered > 0 
					&& LocationTypeCode._19.code.equals(offense.getLocationType().getNibrsCode())){
				burglaryOffenseCount = offense.getNumberOfPremisesEntered();
			}
			else {
				burglaryOffenseCount = 1; 
			}
			
			returnAForm.getRows()[returnARowName.ordinal()].increaseReportedOffenses(burglaryOffenseCount);
		}
		
		if (burglaryOffenseCount > 0){
			PropertyStolenByClassificationRowName propertyStolenByClassificationRowName = 
					getPropertyStolenByClassificationBurglaryRowName(offense.getLocationType().getNibrsCode(), offense.getAdministrativeSegment().getIncidentHour());
			returnAForm.getPropertyStolenByClassifications()[propertyStolenByClassificationRowName.ordinal()]
					.increaseNumberOfOffenses(burglaryOffenseCount);
			returnAForm.getPropertyStolenByClassifications()[PropertyStolenByClassificationRowName.BURGLARY_TOTAL.ordinal()]
					.increaseNumberOfOffenses(burglaryOffenseCount);
			returnAForm.getPropertyStolenByClassifications()[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()]
					.increaseNumberOfOffenses(burglaryOffenseCount);
			
			double stolenPropertyValue = getStolenPropertyValue(offense.getAdministrativeSegment(), 0);
			returnAForm.getPropertyStolenByClassifications()[propertyStolenByClassificationRowName.ordinal()]
					.increaseMonetaryValue(stolenPropertyValue);
			returnAForm.getPropertyStolenByClassifications()[PropertyStolenByClassificationRowName.BURGLARY_TOTAL.ordinal()]
					.increaseMonetaryValue(stolenPropertyValue);
			returnAForm.getPropertyStolenByClassifications()[PropertyStolenByClassificationRowName.GRAND_TOTAL.ordinal()]
					.increaseMonetaryValue(stolenPropertyValue);
		}
		return burglaryOffenseCount; 
	}

	private PropertyStolenByClassificationRowName getPropertyStolenByClassificationBurglaryRowName(String locationCode,
			String incidentHour) {
		PropertyStolenByClassificationRowName propertyStolenByClassificationRowName = null;
		if (LocationTypeCode._20.code.equals(locationCode)){
			if (StringUtils.isBlank(incidentHour)){
				propertyStolenByClassificationRowName = PropertyStolenByClassificationRowName.BURGLARY_RESIDENCE_UNKNOWN;
			}
			else if (Integer.valueOf(incidentHour) >= 6 && Integer.valueOf(incidentHour) < 18){
				propertyStolenByClassificationRowName = PropertyStolenByClassificationRowName.BURGLARY_RESIDENCE_DAY; 
			}
			else{
				propertyStolenByClassificationRowName = PropertyStolenByClassificationRowName.BURGLARY_RESIDENCE_NIGHT; 
			}
		}
		else{
			if (StringUtils.isBlank(incidentHour)){
				propertyStolenByClassificationRowName = PropertyStolenByClassificationRowName.BURGLARY_NON_RESIDENCE_UNKNOWN;
			}
			else if (Integer.valueOf(incidentHour) >= 6 && Integer.valueOf(incidentHour) < 18){
				propertyStolenByClassificationRowName = PropertyStolenByClassificationRowName.BURGLARY_NON_RESIDENCE_DAY; 
			}
			else{
				propertyStolenByClassificationRowName = PropertyStolenByClassificationRowName.BURGLARY_NON_RESIDENCE_NIGHT; 
			}
		}
		return propertyStolenByClassificationRowName;
	}

	private ReturnARowName getReturnARowFor13B13COffense(OffenseSegment offense) {
		
		List<String> typeOfWeaponForceInvolved = offense.getTypeOfWeaponForceInvolveds()
				.stream().map(TypeOfWeaponForceInvolved::getTypeOfWeaponForceInvolvedType)
				.map(TypeOfWeaponForceInvolvedType::getNibrsCode)
				.collect(Collectors.toList());
//		log.debug("TypeOfWeaponForceInvolveds:" + typeOfWeaponForceInvolved);
		ReturnARowName returnARowName = null; 
		boolean containsValidWeaponForceType = 
				offense.getTypeOfWeaponForceInvolveds()
				.stream()
				.filter(type -> Arrays.asList("40", "90", "95", "99", " ").contains(type.getTypeOfWeaponForceInvolvedType().getNibrsCode()))
				.count() > 0 || typeOfWeaponForceInvolved.isEmpty();
				
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
		boolean containsCompletedRapeOffense = administrativeSegment.getOffenseSegments()
				.stream()
				.filter(item -> OffenseCode.isReturnARapeCode(item.getUcrOffenseCodeType().getNibrsCode()))
				.anyMatch(item->"C".equals(item.getOffenseAttemptedCompleted()));
		boolean containsAttemptedRapeOffense = administrativeSegment.getOffenseSegments()
				.stream()
				.filter(item -> OffenseCode.isReturnARapeCode(item.getUcrOffenseCodeType().getNibrsCode()))
				.anyMatch(item->"A".equals(item.getOffenseAttemptedCompleted()));
		List<VictimSegment> victimSegments = administrativeSegment.getVictimSegments()
			.stream().filter(victim->CollectionUtils.containsAny(victim.getConnectedOffenseCodes(), Arrays.asList("11A", "11B", "11C")))
			.filter(victim->Arrays.asList("F", "M").contains(victim.getSexOfPersonType().getNibrsCode()))
			.collect(Collectors.toList());
		if (victimSegments.size() > 0){
			if (containsCompletedRapeOffense){
				returnARowName = ReturnARowName.RAPE_BY_FORCE;
			}
			else if (containsAttemptedRapeOffense){
				returnARowName = ReturnARowName.ATTEMPTS_TO_COMMIT_FORCIBLE_RAPE;
			} 
		}
		
		return returnARowName;
	}

	private List<OffenseSegment> getReturnAOffenses(AdministrativeSegment administrativeSegment) {
		List<OffenseSegment> offenses = new ArrayList<>(); 
		
		OffenseSegment reportingOffense = null; 
		Integer reportingOffenseValue = 99; 
		
//		List<String> offenseCodes = administrativeSegment.getOffenseSegments()
//				.stream().map(offense->offense.getUcrOffenseCodeType().getNibrsCode())
//				.collect(Collectors.toList());
		for (OffenseSegment offense: administrativeSegment.getOffenseSegments()){
//			if (offense.getUcrOffenseCodeType().getNibrsCode().startsWith("23")
//					&& CollectionUtils.containsAny(offenseCodes, Arrays.asList("09A", "09B", "11A", "11B", "11C", "120", "13A", "13B", "13C", "220" ))) {
//				log.info("Larcency Offense Not Added");
//				log.info("OffenseCodes: " + offenseCodes);
//				log.info("offense.getOffenseAttemptedCompleted():" + offense.getOffenseAttemptedCompleted());
//			}
			if (!Arrays.asList("A", "C").contains(offense.getOffenseAttemptedCompleted())){
				continue;
			}
			
			if (offense.getUcrOffenseCodeType().getNibrsCode().equals(OffenseCode._09C.code)){
				offenses.add(offense);
				continue;
			}
			Integer offenseValue = Optional.ofNullable(partIOffensesMap.get(offense.getUcrOffenseCodeType().getNibrsCode())).orElse(99); 
			
			if (offenseValue < reportingOffenseValue){
//				if (reportingOffense!= null && reportingOffense.getUcrOffenseCodeType().getNibrsCode().equals("220")) {
//					log.info("220 added against the rule");
//					int numberOfPremisesEntered = Optional.ofNullable(reportingOffense.getNumberOfPremisesEntered()).orElse(0);
//					log.info("numberOfPremisesEntered: " + numberOfPremisesEntered);
//					log.info("LocationTypeCode._19.code.equals(offense.getLocationType().getNibrsCode()): " + LocationTypeCode._19.code.equals(reportingOffense.getLocationType().getNibrsCode()));
//					offenses.add(reportingOffense);
//					log.info("reportingOffense: " + offense.getUcrOffenseCodeType().getNibrsCode());
//				}
				reportingOffense = offense; 
				reportingOffenseValue = offenseValue; 
			}
//			else if (offense.getUcrOffenseCodeType().getNibrsCode().equals("220")) {
//				offenses.add(offense);
////				log.info("administrativeSegmentID: " + offense.getAdministrativeSegment().getAdministrativeSegmentId());
//				int numberOfPremisesEntered = Optional.ofNullable(offense.getNumberOfPremisesEntered()).orElse(0);
//				log.info("220 added against the rule");
//				log.info("numberOfPremisesEntered: " + numberOfPremisesEntered);
//				log.info("LocationTypeCode._19.code.equals(offense.getLocationType().getNibrsCode()): " + LocationTypeCode._19.code.equals(offense.getLocationType().getNibrsCode()));
//				log.info("reportingOffense: " + reportingOffense.getUcrOffenseCodeType().getNibrsCode());
//			}
		}
		
		if (reportingOffense != null){
			offenses.add(reportingOffense);
		}
		return offenses;
	}
	
	
}
