
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.reports.asr.AsrAdult;
import org.search.nibrs.model.reports.asr.AsrAdultRow;
import org.search.nibrs.model.reports.asr.AsrAdultRow.AdultAgeGroup;
import org.search.nibrs.model.reports.asr.AsrAdultRow.Ethnicity;
import org.search.nibrs.model.reports.asr.AsrAdultRow.Race;
import org.search.nibrs.model.reports.asr.AsrAdultRowName;
import org.search.nibrs.stagingdata.AppProperties;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.service.AdministrativeSegmentService;
import org.search.nibrs.stagingdata.service.ArrestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsrFormService {

	private static final List<String> POSSESSION_CODES = Arrays.asList("B","P","U");
	private static final List<String> SALE_MANUFACTURING_CODES = Arrays.asList("C","D","E","O","T");
	private static final List<String> OPIUM_COCAINE_AND_DERIVATIVES_CODES = Arrays.asList("A","B","D","F","G");
	private static final List<String> MARIJUANA_CODES = Arrays.asList("C","E");
	private static final List<String> SYNTHETIC_NARCOTICS_CODES = Arrays.asList("H");
	private static final List<String> Other_Dangerous_Nonnarcotic_Drugs_CODES = Arrays.asList("I","J","K","L","M","N","O","P","U","X");
	private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	AdministrativeSegmentService administrativeSegmentService;
	@Autowired
	ArrestReportService arrestReportService;
	@Autowired
	public AgencyRepository agencyRepository; 
	@Autowired
	public AppProperties appProperties; 

	private Map<String, AsrAdultRowName> offenseCodeRowNameMap; 
	
	public AsrFormService() {
		offenseCodeRowNameMap = new HashMap<>();
		offenseCodeRowNameMap.put("09A", AsrAdultRowName.MURDER_NONNEGLIGENT_MANSLAUGHTER); 
		offenseCodeRowNameMap.put("09B", AsrAdultRowName.MANSLAUGHTER_BY_NEGLIGENCE); 
		offenseCodeRowNameMap.put("11AF", AsrAdultRowName.RAPE); 
		offenseCodeRowNameMap.put("120", AsrAdultRowName.ROBBERY); 
		offenseCodeRowNameMap.put("13A", AsrAdultRowName.AGGRAVATED_ASSAULT); 
		offenseCodeRowNameMap.put("13B", AsrAdultRowName.OTHER_ASSAULTS); 
		offenseCodeRowNameMap.put("13C", AsrAdultRowName.OTHER_ASSAULTS); 
		offenseCodeRowNameMap.put("220", AsrAdultRowName.BURGLARY); 
		offenseCodeRowNameMap.put("23A", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeRowNameMap.put("23B", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeRowNameMap.put("23C", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeRowNameMap.put("23D", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeRowNameMap.put("23E", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeRowNameMap.put("23F", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeRowNameMap.put("23G", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeRowNameMap.put("23H", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeRowNameMap.put("240", AsrAdultRowName.MOTOR_VEHICLE_THEFT); 
		offenseCodeRowNameMap.put("200", AsrAdultRowName.ARSON); 
		offenseCodeRowNameMap.put("250", AsrAdultRowName.FORGERY_AND_COUNTERFEITING); 
		offenseCodeRowNameMap.put("26A", AsrAdultRowName.FRAUD); 
		offenseCodeRowNameMap.put("26B", AsrAdultRowName.FRAUD); 
		offenseCodeRowNameMap.put("26C", AsrAdultRowName.FRAUD); 
		offenseCodeRowNameMap.put("26D", AsrAdultRowName.FRAUD); 
		offenseCodeRowNameMap.put("26E", AsrAdultRowName.FRAUD); 
		offenseCodeRowNameMap.put("90A", AsrAdultRowName.FRAUD); 
		offenseCodeRowNameMap.put("270", AsrAdultRowName.EMBEZZLEMENT); 
		offenseCodeRowNameMap.put("280", AsrAdultRowName.STOLEN_PROPERTY_BUYING_RECEIVING_POSSESSING); 
		offenseCodeRowNameMap.put("290", AsrAdultRowName.VANDALISM); 
		offenseCodeRowNameMap.put("520", AsrAdultRowName.WEAPONS_CARRYING_POSSESSING_ETC); 
		offenseCodeRowNameMap.put("40A", AsrAdultRowName.PROSTITUTION); 
		offenseCodeRowNameMap.put("40B", AsrAdultRowName.ASSISTING_PROMOTING_PROSTITUTION); 
		offenseCodeRowNameMap.put("40C", AsrAdultRowName.PURCHASING_PROSTITUTION); 
		offenseCodeRowNameMap.put("11AM", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeRowNameMap.put("11B", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeRowNameMap.put("11C", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeRowNameMap.put("11D", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeRowNameMap.put("36A", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeRowNameMap.put("36B", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeRowNameMap.put("39A", AsrAdultRowName.GAMBLING_TOTAL); 
		offenseCodeRowNameMap.put("39B", AsrAdultRowName.GAMBLING_TOTAL); 
		offenseCodeRowNameMap.put("39C", AsrAdultRowName.GAMBLING_TOTAL); 
		offenseCodeRowNameMap.put("39D", AsrAdultRowName.GAMBLING_TOTAL); 
		offenseCodeRowNameMap.put("90F", AsrAdultRowName.OFFENSES_AGAINST_FAMILY_AND_CHILDREN); 
		offenseCodeRowNameMap.put("90D", AsrAdultRowName.DRIVING_UNDER_THE_INFLUENCE); 
		offenseCodeRowNameMap.put("90G", AsrAdultRowName.LIQUOR_LAWS); 
		offenseCodeRowNameMap.put("90E", AsrAdultRowName.DRUNKENNESS); 
		offenseCodeRowNameMap.put("90C", AsrAdultRowName.DISORDERLY_CONDUCT); 
		offenseCodeRowNameMap.put("90B", AsrAdultRowName.VAGRANCY); 
		offenseCodeRowNameMap.put("35B", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeRowNameMap.put("100", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeRowNameMap.put("210", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeRowNameMap.put("370", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeRowNameMap.put("510", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeRowNameMap.put("90H", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeRowNameMap.put("90J", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeRowNameMap.put("90Z", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeRowNameMap.put("64A", AsrAdultRowName.HUMAN_TRAFFICKING_COMMERCIAL_SEX_ACTS); 
		offenseCodeRowNameMap.put("64B", AsrAdultRowName.HUMAN_TRAFFICKING_INVOLUNTARY_SERVITUDE); 
	}
	
	public AsrAdult createAsrAdultSummaryReport(String ori, Integer arrestYear,  Integer arrestMonth ) {
		
		AsrAdult asrAdult = new AsrAdult(ori, arrestYear, arrestMonth); 
		
		if (!"StateWide".equalsIgnoreCase(ori)){
			Agency agency = agencyRepository.findFirstByAgencyOri(ori); 
			if (agency!= null){
				asrAdult.setAgencyName(agency.getAgencyName());
				asrAdult.setStateName(agency.getStateName());
				asrAdult.setStateCode(agency.getStateCode());
				asrAdult.setPopulation(agency.getPopulation());
			}
			else{
				return asrAdult; 
			}
		}
		else{
			asrAdult.setAgencyName(ori);
			asrAdult.setStateName("");
			asrAdult.setStateCode("");
			asrAdult.setPopulation(null);
		}

		processGroupAArrests(ori, arrestYear, arrestMonth, asrAdult);
		processGroupBArrests(ori, arrestYear, arrestMonth, asrAdult);
		
		log.info("asrAdult: " + asrAdult);
		log.info("asrAdult rows drug grand total "  + asrAdult.getRows()[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()]);
		log.info("asrAdult rows drug sale total "  + asrAdult.getRows()[AsrAdultRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()]);
		log.info("asrAdult rows drug possession total "  + asrAdult.getRows()[AsrAdultRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()]);
		log.info("asrAdult rows drug sale opium "  + asrAdult.getRows()[AsrAdultRowName.DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES.ordinal()]);
		log.info("asrAdult rows drug sale weed "  + asrAdult.getRows()[AsrAdultRowName.DRUG_SALE_MANUFACTURING_MARIJUANA.ordinal()]);
		log.info("asrAdult rows DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS "  + asrAdult.getRows()[AsrAdultRowName.DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS.ordinal()]);
		log.info("asrAdult rows DRUG_SALE_MANUFACTURING_OTHER "  + asrAdult.getRows()[AsrAdultRowName.DRUG_SALE_MANUFACTURING_OTHER.ordinal()]);
		log.info("asrAdult rows drug poSSession opium "  + asrAdult.getRows()[AsrAdultRowName.DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES.ordinal()]);
		log.info("asrAdult rows drug DRUG_POSSESSION_MARIJUANA "  + asrAdult.getRows()[AsrAdultRowName.DRUG_POSSESSION_MARIJUANA.ordinal()]);
		log.info("asrAdult rows DRUG_POSSESSION_SYNTHETIC_NARCOTICS "  + asrAdult.getRows()[AsrAdultRowName.DRUG_POSSESSION_SYNTHETIC_NARCOTICS.ordinal()]);
		log.info("asrAdult rows DRUG_POSSESSION_OTHER "  + asrAdult.getRows()[AsrAdultRowName.DRUG_POSSESSION_OTHER.ordinal()]);
		return asrAdult;
	}

	private void processGroupBArrests(String ori, Integer arrestYear, Integer arrestMonth, AsrAdult asrAdult) {
		AsrAdultRow[] asrAdultRows = asrAdult.getRows(); 
		List<ArrestReportSegment> arrestReportSegments = arrestReportService.findArrestReportSegmentByOriAndArrestDate(ori, arrestYear, arrestMonth);
		
		List<ArrestReportSegment> adultArrestReportSegments = arrestReportSegments.stream()
				.filter(i-> i.getAverageAge() >= 18 || i.isAgeUnknown())
				.collect(Collectors.toList());
		
		for (ArrestReportSegment arrestReportSegment: adultArrestReportSegments) {
			String offenseCode = arrestReportSegment.getUcrOffenseCodeType().getNibrsCode(); 
			AsrAdultRowName asrAdultRowName = offenseCodeRowNameMap.get(offenseCode);
			
			if (asrAdultRowName != null) {
				countToAgeGroups(asrAdultRows, arrestReportSegment.getAverageAge(), arrestReportSegment.getSexOfPersonType().getNibrsCode(), asrAdultRowName);
				countToRaceGroups(asrAdultRows, arrestReportSegment.getRaceOfPersonType().getNibrsCode(), asrAdultRowName);
				countToEthnicityGroups(asrAdultRows, arrestReportSegment.getEthnicityOfPersonType().getNibrsCode(), asrAdultRowName);
			}
		}
		
	}

	private void processGroupAArrests(String ori, Integer arrestYear, Integer arrestMonth, AsrAdult asrAdult) {
		AsrAdultRow[] asrAdultRows = asrAdult.getRows(); 
		
		List<ArresteeSegment> arresteeSegments = administrativeSegmentService.findArresteeSegmentByOriAndArrestDate(ori, arrestYear, arrestMonth); 
		
		List<ArresteeSegment> filteredArresteeSegments = arresteeSegments
				.stream()
				.filter(i->
						Arrays.asList("C", "N").contains(i.getMultipleArresteeSegmentsIndicatorType().getNibrsCode()) &&  
						(i.getAverageAge() >= 18 || i.isAgeUnknown()))
				.collect(Collectors.toList());

		for (ArresteeSegment arresteeSegment: filteredArresteeSegments){
			
			AsrAdultRowName asrAdultRowName = null;
			
			String offenseCode = arresteeSegment.getUcrOffenseCodeType().getNibrsCode();
			if ("35A".equals(offenseCode)){
				asrAdultRowName=get35AAsrAdultRowName(arresteeSegment);
			}
			else if ("11A".equals(offenseCode)){
				asrAdultRowName = offenseCodeRowNameMap.get(offenseCode + getVictimGender(arresteeSegment));
			}
			else{
				asrAdultRowName = offenseCodeRowNameMap.get(offenseCode); 
			}
			
			log.info("arrestee offenseCode: " + offenseCode);
			log.info("asrAdultRowName: " + asrAdultRowName);
			if (asrAdultRowName != null){
				countToAgeGroups(asrAdultRows, arresteeSegment.getAverageAge(), arresteeSegment.getSexOfPersonType().getNibrsCode(), asrAdultRowName);
				countToRaceGroups(asrAdultRows, arresteeSegment.getRaceOfPersonType().getNibrsCode(), asrAdultRowName);
				countToEthnicityGroups(asrAdultRows, arresteeSegment.getEthnicityOfPersonType().getNibrsCode(), asrAdultRowName);
			}
			
		}
		
	}

	private AsrAdultRowName get35AAsrAdultRowName(ArresteeSegment arresteeSegment) {
		OffenseSegment offenseSegment = arresteeSegment.getAdministrativeSegment()
				.getOffenseSegments()
				.stream()
				.filter(i-> Objects.equals("35A", i.getUcrOffenseCodeType().getNibrsCode()))
				.findFirst().orElse(null);
		
		AsrAdultRowName asrAdultRowName = null;
		if (offenseSegment != null){
			List<String> criminalActivityTypes = offenseSegment.getTypeOfCriminalActivityTypes()
					.stream()
					.map(i->i.getNibrsCode())
					.collect(Collectors.toList());
					
			String rowNamePrefix = null; 
			
			if (CollectionUtils.containsAny(SALE_MANUFACTURING_CODES, criminalActivityTypes)){
				rowNamePrefix = "DRUG_SALE_MANUFACTURING"; 
			}
			else if (CollectionUtils.containsAny(POSSESSION_CODES, criminalActivityTypes)){
				rowNamePrefix = "DRUG_POSSESSION";
			}
			
			if (StringUtils.isNotBlank(rowNamePrefix)){
				List<String> suspectedDrugCode = offenseSegment.getOffenderSuspectedOfUsingTypes().stream()
						.map(i->i.getNibrsCode())
						.collect(Collectors.toList());
				
				if (CollectionUtils.containsAny(OPIUM_COCAINE_AND_DERIVATIVES_CODES, suspectedDrugCode)){
					asrAdultRowName = AsrAdultRowName.valueOf(rowNamePrefix + "_OPIUM_COCAINE_DERIVATIVES");
				}
				else if (CollectionUtils.containsAny(MARIJUANA_CODES, suspectedDrugCode)){
					asrAdultRowName = AsrAdultRowName.valueOf(rowNamePrefix + "_MARIJUANA");
				}
				else if (CollectionUtils.containsAny(SYNTHETIC_NARCOTICS_CODES, suspectedDrugCode)){
					asrAdultRowName = AsrAdultRowName.valueOf(rowNamePrefix + "_SYNTHETIC_NARCOTICS");
				}
				else if (CollectionUtils.containsAny(Other_Dangerous_Nonnarcotic_Drugs_CODES, suspectedDrugCode)){
					asrAdultRowName = AsrAdultRowName.valueOf(rowNamePrefix + "_OTHER");
				}
			}
					
		}
		return asrAdultRowName;
	}

	private String getVictimGender(ArresteeSegment arresteeSegment) {
		String offenseCode = arresteeSegment.getUcrOffenseCodeType().getNibrsCode(); 
		String victimGender = arresteeSegment.getAdministrativeSegment()
				.getVictimSegments()
				.stream()
				.filter(i->i.getOffenseSegments().stream().anyMatch(o->Objects.equals(offenseCode, o.getUcrOffenseCodeType().getNibrsCode())))
				.map(i->i.getSexOfPersonType().getNibrsCode()).findFirst().orElse("");
		return victimGender;
	}

	private void countToEthnicityGroups(AsrAdultRow[] asrAdultRows, String ethnicityCode,
			AsrAdultRowName asrAdultRowName) {
		if (StringUtils.isNotBlank(ethnicityCode) && !"U".equals(ethnicityCode)){
			Ethnicity ethnicity = Ethnicity.valueOf(ethnicityCode); 
			asrAdultRows[asrAdultRowName.ordinal()].getEthnicityGroups()[ethnicity.ordinal()]++;
			asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getEthnicityGroups()[ethnicity.ordinal()]++;
			
			switch (asrAdultRowName) {
			case DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES:
			case DRUG_SALE_MANUFACTURING_MARIJUANA:
			case DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS:
			case DRUG_SALE_MANUFACTURING_OTHER:
				asrAdultRows[AsrAdultRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;
				
				break;
			case DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES:
			case DRUG_POSSESSION_MARIJUANA:
			case DRUG_POSSESSION_SYNTHETIC_NARCOTICS:
			case DRUG_POSSESSION_OTHER:
				asrAdultRows[AsrAdultRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;;
				break;
				
			case PROSTITUTION:
			case ASSISTING_PROMOTING_PROSTITUTION: 
				asrAdultRows[AsrAdultRowName.PROSTITUTION_AND_COMMERCIALIZED_VICE.ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;
				break;
			default:
				break;
			}

		}
	}

	private void countToRaceGroups(AsrAdultRow[] asrAdultRows, String raceCode,
			AsrAdultRowName asrAdultRowName) {
		if (StringUtils.isNotBlank(raceCode) && !"U".equals(raceCode)){
			Race race = Race.valueOf(raceCode); 
			asrAdultRows[asrAdultRowName.ordinal()].getRaceGroups()[race.ordinal()]++;
			asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getRaceGroups()[race.ordinal()]++;
			
			switch (asrAdultRowName) {
			case DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES:
			case DRUG_SALE_MANUFACTURING_MARIJUANA:
			case DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS:
			case DRUG_SALE_MANUFACTURING_OTHER:
				asrAdultRows[AsrAdultRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()].getRaceGroups()[race.ordinal()]++;
				asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getRaceGroups()[race.ordinal()]++;
				
				break;
			case DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES:
			case DRUG_POSSESSION_MARIJUANA:
			case DRUG_POSSESSION_SYNTHETIC_NARCOTICS:
			case DRUG_POSSESSION_OTHER:
				asrAdultRows[AsrAdultRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()].getRaceGroups()[race.ordinal()]++;
				asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getRaceGroups()[race.ordinal()]++;
				break;
			case PROSTITUTION:
			case ASSISTING_PROMOTING_PROSTITUTION: 
				asrAdultRows[AsrAdultRowName.PROSTITUTION_AND_COMMERCIALIZED_VICE.ordinal()].getRaceGroups()[race.ordinal()]++;
				break;
			default:
				break;
			}

		}
	}
	
	private void countToAgeGroups(AsrAdultRow[] asrAdultRows, Integer averageAge, String sexCode,
			AsrAdultRowName asrAdultRowName) {
		AdultAgeGroup ageGroup = getAdultAgeGroup(averageAge);
		if (ageGroup != null){
			switch( sexCode ){
			case "M":
				asrAdultRows[asrAdultRowName.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrAdultRows[asrAdultRowName.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
				
				//TODO maybe put all the drug total logic into one method. 
				switch (asrAdultRowName) {
				case DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES:
				case DRUG_SALE_MANUFACTURING_MARIJUANA:
				case DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS:
				case DRUG_SALE_MANUFACTURING_OTHER:
					asrAdultRows[AsrAdultRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
					
					break;
				case DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES:
				case DRUG_POSSESSION_MARIJUANA:
				case DRUG_POSSESSION_SYNTHETIC_NARCOTICS:
				case DRUG_POSSESSION_OTHER:
					asrAdultRows[AsrAdultRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
					break;
				case PROSTITUTION:
				case ASSISTING_PROMOTING_PROSTITUTION: 
					asrAdultRows[AsrAdultRowName.PROSTITUTION_AND_COMMERCIALIZED_VICE.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					break;
				default:
					break;
				}
				
				break; 
			case "F":
				asrAdultRows[asrAdultRowName.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
				asrAdultRows[asrAdultRowName.ordinal()].getFemaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
				
				switch (asrAdultRowName) {
				case DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES:
				case DRUG_SALE_MANUFACTURING_MARIJUANA:
				case DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS:
				case DRUG_SALE_MANUFACTURING_OTHER:
					asrAdultRows[AsrAdultRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()].getFemaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getFemaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
					
					break;
				case DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES:
				case DRUG_POSSESSION_MARIJUANA:
				case DRUG_POSSESSION_SYNTHETIC_NARCOTICS:
				case DRUG_POSSESSION_OTHER:
					asrAdultRows[AsrAdultRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()].getFemaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getFemaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
					break;
				case PROSTITUTION:
				case ASSISTING_PROMOTING_PROSTITUTION: 
					asrAdultRows[AsrAdultRowName.PROSTITUTION_AND_COMMERCIALIZED_VICE.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					break;
				default:
					break;
				}
				break; 
			}
		}
	}

	private AdultAgeGroup getAdultAgeGroup(Integer averageAge) {
		
		if (averageAge == null) return null; 

		AdultAgeGroup ageGroup = null; 
		
		if (averageAge < 25){
			ageGroup = AdultAgeGroup.valueOf(StringUtils.join("_", averageAge.toString()));
		}
		else if (averageAge < 30){
			ageGroup = AdultAgeGroup._25To29;
		}
		else if (averageAge < 35) {
			ageGroup = AdultAgeGroup._30To34; 
		}
		else if (averageAge < 40){
			ageGroup = AdultAgeGroup._35To39; 
		}
		else if (averageAge < 45){
			ageGroup = AdultAgeGroup._40To44; 
		}
		else if (averageAge < 50){
			ageGroup = AdultAgeGroup._45To49; 
		}
		else if (averageAge < 55){
			ageGroup = AdultAgeGroup._50To54; 
		}
		else if (averageAge < 60){
			ageGroup = AdultAgeGroup._55To59; 
		}
		else if (averageAge < 65){
			ageGroup = AdultAgeGroup._60To64; 
		}
		else {
			ageGroup = AdultAgeGroup._65AndOver;
		}
				
		return ageGroup;
	}

}
