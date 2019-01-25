
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
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.service.AdministrativeSegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsrFormService {

	private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	AdministrativeSegmentService administrativeSegmentService;
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
		offenseCodeRowNameMap.put("40A", AsrAdultRowName.PROSTITUTION_AND_COMMERCIALIZED_VICE); 
		offenseCodeRowNameMap.put("40B", AsrAdultRowName.PROSTITUTION_AND_COMMERCIALIZED_VICE); 
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
//TODO 35A is complicated. need to be handled elsewhere.
		
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
//		processGroupBArrests(ori, arrestYear, arrestMonth, asrAdult);
		
		log.info("asrAdult: " + asrAdult);
		return asrAdult;
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
				//TODO resolve the row name. 
			}
			if ("11A".equals(offenseCode)){
				asrAdultRowName = offenseCodeRowNameMap.get(offenseCode + getVictimGender(arresteeSegment));
			}
			else{
				asrAdultRowName = offenseCodeRowNameMap.get(offenseCode); 
			}
			
			log.info("arrestee offenseCode: " + offenseCode);
			log.info("asrAdultRowName: " + asrAdultRowName);
			if (asrAdultRowName != null){
				countToAgeGroups(asrAdultRows, arresteeSegment, asrAdultRowName);
				
				countToRaceGroups(asrAdultRows, arresteeSegment, asrAdultRowName);
				countToEthnicityGroups(asrAdultRows, arresteeSegment, asrAdultRowName);
				
				
			}
			
		}
		
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

	private void countToEthnicityGroups(AsrAdultRow[] asrAdultRows, ArresteeSegment arresteeSegment,
			AsrAdultRowName asrAdultRowName) {
		String ethnicityString = arresteeSegment.getEthnicityOfPersonType().getNibrsCode();
		if (StringUtils.isNotBlank(ethnicityString) && !"U".equals(ethnicityString)){
			Ethnicity ethnicity = Ethnicity.valueOf(ethnicityString); 
			asrAdultRows[asrAdultRowName.ordinal()].getEthnicityGroups()[ethnicity.ordinal()]++;
			asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getEthnicityGroups()[ethnicity.ordinal()]++;
		}
	}

	private void countToRaceGroups(AsrAdultRow[] asrAdultRows, ArresteeSegment arresteeSegment,
			AsrAdultRowName asrAdultRowName) {
		String raceString = arresteeSegment.getRaceOfPersonType().getNibrsCode();
		if (StringUtils.isNotBlank(raceString) && !"U".equals(raceString)){
			Race race = Race.valueOf(raceString); 
			asrAdultRows[asrAdultRowName.ordinal()].getRaceGroups()[race.ordinal()]++;
			asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getRaceGroups()[race.ordinal()]++;
		}
	}
	
	private void countToAgeGroups(AsrAdultRow[] asrAdultRows, ArresteeSegment arresteeSegment,
			AsrAdultRowName asrAdultRowName) {
		AdultAgeGroup ageGroup = getAgeGroup(arresteeSegment);
		if (ageGroup != null){
			switch( arresteeSegment.getSexOfPersonType().getNibrsCode() ){
			case "M":
				asrAdultRows[asrAdultRowName.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrAdultRows[asrAdultRowName.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
				break; 
			case "F":
				asrAdultRows[asrAdultRowName.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
				asrAdultRows[asrAdultRowName.ordinal()].getFemaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
				break; 
			}
		}
	}

	private AdultAgeGroup getAgeGroup(ArresteeSegment arresteeSegment) {
		
		if (arresteeSegment.isAgeUnknown()) return null; 

		AdultAgeGroup ageGroup = null; 
		
		Integer averageAge = arresteeSegment.getAverageAge();
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
