
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
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.reports.asr.AsrAdultRow;
import org.search.nibrs.model.reports.asr.AsrAdultRow.AdultAgeGroup;
import org.search.nibrs.model.reports.asr.AsrAdultRowName;
import org.search.nibrs.model.reports.asr.AsrJuvenileRow;
import org.search.nibrs.model.reports.asr.AsrJuvenileRow.JuvenileAgeGroup;
import org.search.nibrs.model.reports.asr.AsrJuvenileRowName;
import org.search.nibrs.model.reports.asr.AsrReports;
import org.search.nibrs.model.reports.asr.AsrRow;
import org.search.nibrs.model.reports.asr.AsrRow.Ethnicity;
import org.search.nibrs.model.reports.asr.AsrRow.Race;
import org.search.nibrs.stagingdata.AppProperties;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.search.nibrs.stagingdata.model.segment.PropertySegment;
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

	private Map<String, AsrAdultRowName> offenseCodeAdultRowNameMap; 
	private Map<String, AsrJuvenileRowName> offenseCodeJuvenileRowNameMap; 
	
	public AsrFormService() {
		offenseCodeAdultRowNameMap = new HashMap<>();
		offenseCodeAdultRowNameMap.put("09A", AsrAdultRowName.MURDER_NONNEGLIGENT_MANSLAUGHTER); 
		offenseCodeAdultRowNameMap.put("09B", AsrAdultRowName.MANSLAUGHTER_BY_NEGLIGENCE); 
		offenseCodeAdultRowNameMap.put("11AF", AsrAdultRowName.RAPE); 
		offenseCodeAdultRowNameMap.put("120", AsrAdultRowName.ROBBERY); 
		offenseCodeAdultRowNameMap.put("13A", AsrAdultRowName.AGGRAVATED_ASSAULT); 
		offenseCodeAdultRowNameMap.put("13B", AsrAdultRowName.OTHER_ASSAULTS); 
		offenseCodeAdultRowNameMap.put("13C", AsrAdultRowName.OTHER_ASSAULTS); 
		offenseCodeAdultRowNameMap.put("220", AsrAdultRowName.BURGLARY); 
		offenseCodeAdultRowNameMap.put("23A", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeAdultRowNameMap.put("23B", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeAdultRowNameMap.put("23C", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeAdultRowNameMap.put("23D", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeAdultRowNameMap.put("23E", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeAdultRowNameMap.put("23F", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeAdultRowNameMap.put("23G", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeAdultRowNameMap.put("23H", AsrAdultRowName.LARCENY_THEFT); 
		offenseCodeAdultRowNameMap.put("240", AsrAdultRowName.MOTOR_VEHICLE_THEFT); 
		offenseCodeAdultRowNameMap.put("200", AsrAdultRowName.ARSON); 
		offenseCodeAdultRowNameMap.put("250", AsrAdultRowName.FORGERY_AND_COUNTERFEITING); 
		offenseCodeAdultRowNameMap.put("26A", AsrAdultRowName.FRAUD); 
		offenseCodeAdultRowNameMap.put("26B", AsrAdultRowName.FRAUD); 
		offenseCodeAdultRowNameMap.put("26C", AsrAdultRowName.FRAUD); 
		offenseCodeAdultRowNameMap.put("26D", AsrAdultRowName.FRAUD); 
		offenseCodeAdultRowNameMap.put("26E", AsrAdultRowName.FRAUD); 
		offenseCodeAdultRowNameMap.put("90A", AsrAdultRowName.FRAUD); 
		offenseCodeAdultRowNameMap.put("270", AsrAdultRowName.EMBEZZLEMENT); 
		offenseCodeAdultRowNameMap.put("280", AsrAdultRowName.STOLEN_PROPERTY_BUYING_RECEIVING_POSSESSING); 
		offenseCodeAdultRowNameMap.put("290", AsrAdultRowName.VANDALISM); 
		offenseCodeAdultRowNameMap.put("520", AsrAdultRowName.WEAPONS_CARRYING_POSSESSING_ETC); 
		offenseCodeAdultRowNameMap.put("40A", AsrAdultRowName.PROSTITUTION); 
		offenseCodeAdultRowNameMap.put("40B", AsrAdultRowName.ASSISTING_PROMOTING_PROSTITUTION); 
		offenseCodeAdultRowNameMap.put("40C", AsrAdultRowName.PURCHASING_PROSTITUTION); 
		offenseCodeAdultRowNameMap.put("11AM", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeAdultRowNameMap.put("11B", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeAdultRowNameMap.put("11C", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeAdultRowNameMap.put("11D", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeAdultRowNameMap.put("36A", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeAdultRowNameMap.put("36B", AsrAdultRowName.SEX_OFFENSES); 
		offenseCodeAdultRowNameMap.put("39A", AsrAdultRowName.GAMBLING_TOTAL); 
		offenseCodeAdultRowNameMap.put("39B", AsrAdultRowName.GAMBLING_TOTAL); 
		offenseCodeAdultRowNameMap.put("39C", AsrAdultRowName.GAMBLING_TOTAL); 
		offenseCodeAdultRowNameMap.put("39D", AsrAdultRowName.GAMBLING_TOTAL); 
		offenseCodeAdultRowNameMap.put("90F", AsrAdultRowName.OFFENSES_AGAINST_FAMILY_AND_CHILDREN); 
		offenseCodeAdultRowNameMap.put("90D", AsrAdultRowName.DRIVING_UNDER_THE_INFLUENCE); 
		offenseCodeAdultRowNameMap.put("90G", AsrAdultRowName.LIQUOR_LAWS); 
		offenseCodeAdultRowNameMap.put("90E", AsrAdultRowName.DRUNKENNESS); 
		offenseCodeAdultRowNameMap.put("90C", AsrAdultRowName.DISORDERLY_CONDUCT); 
		offenseCodeAdultRowNameMap.put("90B", AsrAdultRowName.VAGRANCY); 
		offenseCodeAdultRowNameMap.put("35B", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeAdultRowNameMap.put("100", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeAdultRowNameMap.put("210", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeAdultRowNameMap.put("370", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeAdultRowNameMap.put("510", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeAdultRowNameMap.put("90H", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeAdultRowNameMap.put("90J", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeAdultRowNameMap.put("90Z", AsrAdultRowName.ALL_OTHER_OFFENSES); 
		offenseCodeAdultRowNameMap.put("64A", AsrAdultRowName.HUMAN_TRAFFICKING_COMMERCIAL_SEX_ACTS); 
		offenseCodeAdultRowNameMap.put("64B", AsrAdultRowName.HUMAN_TRAFFICKING_INVOLUNTARY_SERVITUDE); 
		
		offenseCodeJuvenileRowNameMap = new HashMap<>();
		offenseCodeJuvenileRowNameMap.put("09A", AsrJuvenileRowName.MURDER_NONNEGLIGENT_MANSLAUGHTER); 
		offenseCodeJuvenileRowNameMap.put("09B", AsrJuvenileRowName.MANSLAUGHTER_BY_NEGLIGENCE); 
		offenseCodeJuvenileRowNameMap.put("11AF", AsrJuvenileRowName.RAPE); 
		offenseCodeJuvenileRowNameMap.put("120", AsrJuvenileRowName.ROBBERY); 
		offenseCodeJuvenileRowNameMap.put("13A", AsrJuvenileRowName.AGGRAVATED_ASSAULT); 
		offenseCodeJuvenileRowNameMap.put("13B", AsrJuvenileRowName.OTHER_ASSAULTS); 
		offenseCodeJuvenileRowNameMap.put("13C", AsrJuvenileRowName.OTHER_ASSAULTS); 
		offenseCodeJuvenileRowNameMap.put("220", AsrJuvenileRowName.BURGLARY); 
		offenseCodeJuvenileRowNameMap.put("23A", AsrJuvenileRowName.LARCENY_THEFT); 
		offenseCodeJuvenileRowNameMap.put("23B", AsrJuvenileRowName.LARCENY_THEFT); 
		offenseCodeJuvenileRowNameMap.put("23C", AsrJuvenileRowName.LARCENY_THEFT); 
		offenseCodeJuvenileRowNameMap.put("23D", AsrJuvenileRowName.LARCENY_THEFT); 
		offenseCodeJuvenileRowNameMap.put("23E", AsrJuvenileRowName.LARCENY_THEFT); 
		offenseCodeJuvenileRowNameMap.put("23F", AsrJuvenileRowName.LARCENY_THEFT); 
		offenseCodeJuvenileRowNameMap.put("23G", AsrJuvenileRowName.LARCENY_THEFT); 
		offenseCodeJuvenileRowNameMap.put("23H", AsrJuvenileRowName.LARCENY_THEFT); 
		offenseCodeJuvenileRowNameMap.put("240", AsrJuvenileRowName.MOTOR_VEHICLE_THEFT); 
		offenseCodeJuvenileRowNameMap.put("200", AsrJuvenileRowName.ARSON); 
		offenseCodeJuvenileRowNameMap.put("250", AsrJuvenileRowName.FORGERY_AND_COUNTERFEITING); 
		offenseCodeJuvenileRowNameMap.put("26A", AsrJuvenileRowName.FRAUD); 
		offenseCodeJuvenileRowNameMap.put("26B", AsrJuvenileRowName.FRAUD); 
		offenseCodeJuvenileRowNameMap.put("26C", AsrJuvenileRowName.FRAUD); 
		offenseCodeJuvenileRowNameMap.put("26D", AsrJuvenileRowName.FRAUD); 
		offenseCodeJuvenileRowNameMap.put("26E", AsrJuvenileRowName.FRAUD); 
		offenseCodeJuvenileRowNameMap.put("90A", AsrJuvenileRowName.FRAUD); 
		offenseCodeJuvenileRowNameMap.put("270", AsrJuvenileRowName.EMBEZZLEMENT); 
		offenseCodeJuvenileRowNameMap.put("280", AsrJuvenileRowName.STOLEN_PROPERTY_BUYING_RECEIVING_POSSESSING); 
		offenseCodeJuvenileRowNameMap.put("290", AsrJuvenileRowName.VANDALISM); 
		offenseCodeJuvenileRowNameMap.put("520", AsrJuvenileRowName.WEAPONS_CARRYING_POSSESSING_ETC); 
		offenseCodeJuvenileRowNameMap.put("40A", AsrJuvenileRowName.PROSTITUTION); 
		offenseCodeJuvenileRowNameMap.put("40B", AsrJuvenileRowName.ASSISTING_PROMOTING_PROSTITUTION); 
		offenseCodeJuvenileRowNameMap.put("40C", AsrJuvenileRowName.PURCHASING_PROSTITUTION); 
		offenseCodeJuvenileRowNameMap.put("11AM", AsrJuvenileRowName.SEX_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("11B", AsrJuvenileRowName.SEX_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("11C", AsrJuvenileRowName.SEX_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("11D", AsrJuvenileRowName.SEX_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("36A", AsrJuvenileRowName.SEX_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("36B", AsrJuvenileRowName.SEX_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("39A", AsrJuvenileRowName.GAMBLING_TOTAL); 
		offenseCodeJuvenileRowNameMap.put("39B", AsrJuvenileRowName.GAMBLING_TOTAL); 
		offenseCodeJuvenileRowNameMap.put("39C", AsrJuvenileRowName.GAMBLING_TOTAL); 
		offenseCodeJuvenileRowNameMap.put("39D", AsrJuvenileRowName.GAMBLING_TOTAL); 
		offenseCodeJuvenileRowNameMap.put("90F", AsrJuvenileRowName.OFFENSES_AGAINST_FAMILY_AND_CHILDREN); 
		offenseCodeJuvenileRowNameMap.put("90D", AsrJuvenileRowName.DRIVING_UNDER_THE_INFLUENCE); 
		offenseCodeJuvenileRowNameMap.put("90G", AsrJuvenileRowName.LIQUOR_LAWS); 
		offenseCodeJuvenileRowNameMap.put("90E", AsrJuvenileRowName.DRUNKENNESS); 
		offenseCodeJuvenileRowNameMap.put("90C", AsrJuvenileRowName.DISORDERLY_CONDUCT); 
		offenseCodeJuvenileRowNameMap.put("35B", AsrJuvenileRowName.ALL_OTHER_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("100", AsrJuvenileRowName.ALL_OTHER_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("210", AsrJuvenileRowName.ALL_OTHER_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("370", AsrJuvenileRowName.ALL_OTHER_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("510", AsrJuvenileRowName.ALL_OTHER_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("90H", AsrJuvenileRowName.ALL_OTHER_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("90J", AsrJuvenileRowName.ALL_OTHER_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("90Z", AsrJuvenileRowName.ALL_OTHER_OFFENSES); 
		offenseCodeJuvenileRowNameMap.put("64A", AsrJuvenileRowName.HUMAN_TRAFFICKING_COMMERCIAL_SEX_ACTS); 
		offenseCodeJuvenileRowNameMap.put("64B", AsrJuvenileRowName.HUMAN_TRAFFICKING_INVOLUNTARY_SERVITUDE); 
		offenseCodeJuvenileRowNameMap.put("90B", AsrJuvenileRowName.CURFEW_AND_LOITERING_LAW_VIOLATIONS); 
		offenseCodeJuvenileRowNameMap.put("90I", AsrJuvenileRowName.RUNAWAYS); 
		
	}
	
	public AsrReports createAsrSummaryReports(String ownerId, String ori, Integer arrestYear,  Integer arrestMonth ) {
		
		AsrReports asrReports = new AsrReports(ori, arrestYear, arrestMonth); 
		
		if (!"StateWide".equalsIgnoreCase(ori)){
			Agency agency = agencyRepository.findFirstByAgencyOri(ori); 
			if (agency!= null){
				asrReports.setAgencyName(agency.getAgencyName());
				asrReports.setStateName(agency.getStateName());
				asrReports.setStateCode(agency.getStateCode());
				asrReports.setPopulation(agency.getPopulation());
			}
			else{
				return asrReports; 
			}
		}
		else{
			asrReports.setAgencyName(ori);
			asrReports.setStateName("");
			asrReports.setStateCode("");
			asrReports.setPopulation(null);
		}

		processGroupAArrests(ori, arrestYear, arrestMonth, asrReports, ownerId);
		processGroupBArrests(ori, arrestYear, arrestMonth, asrReports, ownerId);
		
		log.debug("asrAdult: " + asrReports);
		log.debug("asrAdult rows drug grand total "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()]);
		log.debug("asrAdult rows drug sale total "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()]);
		log.debug("asrAdult rows drug possession total "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()]);
		log.debug("asrAdult rows drug sale opium "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES.ordinal()]);
		log.debug("asrAdult rows drug sale weed "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_SALE_MANUFACTURING_MARIJUANA.ordinal()]);
		log.debug("asrAdult rows DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS.ordinal()]);
		log.debug("asrAdult rows DRUG_SALE_MANUFACTURING_OTHER "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_SALE_MANUFACTURING_OTHER.ordinal()]);
		log.debug("asrAdult rows drug poSSession opium "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES.ordinal()]);
		log.debug("asrAdult rows drug DRUG_POSSESSION_MARIJUANA "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_POSSESSION_MARIJUANA.ordinal()]);
		log.debug("asrAdult rows DRUG_POSSESSION_SYNTHETIC_NARCOTICS "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_POSSESSION_SYNTHETIC_NARCOTICS.ordinal()]);
		log.debug("asrAdult rows DRUG_POSSESSION_OTHER "  + asrReports.getAdultRows()[AsrAdultRowName.DRUG_POSSESSION_OTHER.ordinal()]);
		return asrReports;
	}

	private void processGroupBArrests(String ori, Integer arrestYear, Integer arrestMonth, AsrReports asrReports, String ownerId) {
		AsrAdultRow[] asrAdultRows = asrReports.getAdultRows(); 
		List<ArrestReportSegment> arrestReportSegments = arrestReportService.findArrestReportSegmentByOriAndArrestDate(ori, arrestYear, arrestMonth, ownerId);
		
		List<ArrestReportSegment> adultArrestReportSegments = arrestReportSegments.stream()
				.filter( i-> i.isAgeUnknown() || i.getAverageAge() >= 18)
				.collect(Collectors.toList());
		
		for (ArrestReportSegment arrestReportSegment: adultArrestReportSegments) {
			String offenseCode = arrestReportSegment.getUcrOffenseCodeType().getNibrsCode(); 
			AsrAdultRowName asrAdultRowName = offenseCodeAdultRowNameMap.get(offenseCode);
			
			if (asrAdultRowName != null) {
				countToAdultAgeGroups(asrAdultRows, arrestReportSegment.getAverageAge(), arrestReportSegment.getSexOfPersonType().getNibrsCode(), asrAdultRowName);
				countToRaceGroups(asrAdultRows, arrestReportSegment.getRaceOfPersonType().getNibrsCode(), asrAdultRowName.name(), AsrAdultRowName.class);
				countToEthnicityGroups(asrAdultRows, arrestReportSegment.getEthnicityOfPersonType().getNibrsCode(), 
						asrAdultRowName.name(), AsrAdultRowName.class);
			}
		}
		
		List<ArrestReportSegment> juvenileArrestReportSegments = arrestReportSegments.stream()
				.filter(ArrestReportSegment::isJuvenile)
				.collect(Collectors.toList());
		AsrJuvenileRow[] asrJuvenileRows = asrReports.getJuvenileRows(); 
		
		for (ArrestReportSegment arrestReportSegment: juvenileArrestReportSegments) {
			String offenseCode = arrestReportSegment.getUcrOffenseCodeType().getNibrsCode(); 
			AsrJuvenileRowName asrJuvenileRowName = offenseCodeJuvenileRowNameMap.get(offenseCode);
			
			if (asrJuvenileRowName != null) {
				countToJuvenileAgeGroups(asrJuvenileRows, arrestReportSegment.getAverageAge(), arrestReportSegment.getSexOfPersonType().getNibrsCode(), asrJuvenileRowName);
				countToRaceGroups(asrJuvenileRows, arrestReportSegment.getRaceOfPersonType().getNibrsCode(), asrJuvenileRowName.name(), AsrJuvenileRowName.class);
				countToEthnicityGroups(asrJuvenileRows, arrestReportSegment.getEthnicityOfPersonType().getNibrsCode(), 
						asrJuvenileRowName.name(), AsrJuvenileRowName.class);
			}
		}
		
	}

	private void processGroupAArrests(String ori, Integer arrestYear, Integer arrestMonth, AsrReports asrReports, String ownerId) {
		List<ArresteeSegment> arresteeSegments = administrativeSegmentService.findArresteeSegmentByOriAndArrestDate(ori, arrestYear, arrestMonth, ownerId); 
		countAdultFormGroupAArrestees(ori, arrestYear, arrestMonth, asrReports, arresteeSegments);
		countJuvenileFormGroupAArrestees(ori, arrestYear, arrestMonth, asrReports, arresteeSegments);
	}

	private void countJuvenileFormGroupAArrestees(String ori, Integer arrestYear, Integer arrestMonth,
			AsrReports asrReports, List<ArresteeSegment> arresteeSegments) {
		AsrJuvenileRow[] asrJuvenileRows = asrReports.getJuvenileRows(); 
		
		List<ArresteeSegment> juvenileArresteeSegments = arresteeSegments
				.stream()
				.filter(i->
						Arrays.asList("C", "N").contains(i.getMultipleArresteeSegmentsIndicatorType().getNibrsCode()) &&  
						i.isJuvenile())
				.collect(Collectors.toList());

		for (ArresteeSegment arresteeSegment: juvenileArresteeSegments){
			
			AsrJuvenileRowName asrJuvenileRowName = null;
			
			String offenseCode = arresteeSegment.getUcrOffenseCodeType().getNibrsCode();
			if ("35A".equals(offenseCode)){
				asrJuvenileRowName= get35AAsrRowName(arresteeSegment).map(AsrJuvenileRowName::valueOf).orElse(null);
			}
			else if ("11A".equals(offenseCode)){
				asrJuvenileRowName = offenseCodeJuvenileRowNameMap.get(offenseCode + getVictimGender(arresteeSegment));
			}
			else{
				asrJuvenileRowName = offenseCodeJuvenileRowNameMap.get(offenseCode); 
			}
			
			if (asrJuvenileRowName != null){
				countToJuvenileAgeGroups(asrJuvenileRows, arresteeSegment.getAverageAge(), arresteeSegment.getSexOfPersonType().getNibrsCode(), asrJuvenileRowName);
				countToRaceGroups(asrJuvenileRows, arresteeSegment.getRaceOfPersonType().getNibrsCode(), asrJuvenileRowName.name(), AsrJuvenileRowName.class);
				countToEthnicityGroups(asrJuvenileRows, arresteeSegment.getEthnicityOfPersonType().getNibrsCode(), asrJuvenileRowName.name(), AsrJuvenileRowName.class);
			}
			
		}
	}
	private void countAdultFormGroupAArrestees(String ori, Integer arrestYear, Integer arrestMonth,
			AsrReports asrReports, List<ArresteeSegment> arresteeSegments) {
		AsrAdultRow[] asrAdultRows = asrReports.getAdultRows(); 
		
		List<ArresteeSegment> adultArresteeSegments = arresteeSegments
				.stream()
				.filter(i->
				Arrays.asList("C", "N").contains(i.getMultipleArresteeSegmentsIndicatorType().getNibrsCode()) &&  
				(i.isAgeUnknown() || i.getAverageAge() >= 18))
				.collect(Collectors.toList());
		
		for (ArresteeSegment arresteeSegment: adultArresteeSegments){
			
			AsrAdultRowName asrAdultRowName = null;
			
			String offenseCode = arresteeSegment.getUcrOffenseCodeType().getNibrsCode();
			if ("35A".equals(offenseCode)){
				asrAdultRowName= get35AAsrRowName(arresteeSegment).map(AsrAdultRowName::valueOf).orElse(null);
			}
			else if ("11A".equals(offenseCode)){
				asrAdultRowName = offenseCodeAdultRowNameMap.get(offenseCode + getVictimGender(arresteeSegment));
			}
			else{
				asrAdultRowName = offenseCodeAdultRowNameMap.get(offenseCode); 
			}
			
			if (asrAdultRowName != null){
				countToAdultAgeGroups(asrAdultRows, arresteeSegment.getAverageAge(), arresteeSegment.getSexOfPersonType().getNibrsCode(), asrAdultRowName);
				countToRaceGroups(asrAdultRows, arresteeSegment.getRaceOfPersonType().getNibrsCode(), asrAdultRowName.name(), AsrAdultRowName.class);
				countToEthnicityGroups(asrAdultRows, arresteeSegment.getEthnicityOfPersonType().getNibrsCode(), asrAdultRowName.name(), AsrAdultRowName.class);
			}
			
		}
	}

	private Optional<String> get35AAsrRowName(ArresteeSegment arresteeSegment) {
		OffenseSegment offenseSegment = arresteeSegment.getAdministrativeSegment()
				.getOffenseSegments()
				.stream()
				.filter(i-> Objects.equals("35A", i.getUcrOffenseCodeType().getNibrsCode()))
				.findFirst().orElse(null);
		
		Optional<String> asrAdultRowName = Optional.empty();
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
			
			List<PropertySegment> propertySegments = arresteeSegment.getAdministrativeSegment()
					.getPropertySegments()
					.stream()
					.filter(i->(i.getPropertyTypes().stream().anyMatch(pt -> "10".equals(pt.getPropertyDescriptionType().getNibrsCode()))))
					.collect(Collectors.toList());
			if (StringUtils.isNotBlank(rowNamePrefix)){
				List<String> suspectedDrugCode = propertySegments.stream()
						.flatMap(i->i.getSuspectedDrugTypes().stream())
						.map(i-> i.getSuspectedDrugTypeType().getNibrsCode())
						.collect(Collectors.toList());
				
				if (CollectionUtils.containsAny(OPIUM_COCAINE_AND_DERIVATIVES_CODES, suspectedDrugCode)){
					asrAdultRowName = Optional.of(rowNamePrefix + "_OPIUM_COCAINE_DERIVATIVES");
				}
				else if (CollectionUtils.containsAny(Other_Dangerous_Nonnarcotic_Drugs_CODES, suspectedDrugCode)){
					if ("DRUG_SALE_MANUFACTURING".contentEquals(rowNamePrefix) && (arresteeSegment.getAverageAge() >= 18 || arresteeSegment.isAgeUnknown())) {
						log.info("adminSegmentId" +  arresteeSegment.getAdministrativeSegment().getAdministrativeSegmentId());
						log.info("suspectedDrugCode: " + suspectedDrugCode);
					}
					asrAdultRowName = Optional.of(rowNamePrefix + "_OTHER");
				}
				else if (CollectionUtils.containsAny(SYNTHETIC_NARCOTICS_CODES, suspectedDrugCode)){
					if ("DRUG_SALE_MANUFACTURING".contentEquals(rowNamePrefix) && (arresteeSegment.getAverageAge() >= 18 || arresteeSegment.isAgeUnknown())) {
						log.info("adminSegmentId" +  arresteeSegment.getAdministrativeSegment().getAdministrativeSegmentId());
						log.info("suspectedDrugCode: " + suspectedDrugCode);
					}
					asrAdultRowName = Optional.of(rowNamePrefix + "_SYNTHETIC_NARCOTICS");
				}
				else if (CollectionUtils.containsAny(MARIJUANA_CODES, suspectedDrugCode)){
					asrAdultRowName =Optional.of(rowNamePrefix + "_MARIJUANA");
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

	private void countToEthnicityGroups(AsrRow[] asrRows, String ethnicityCode,
			String asrRowName, Class asrRowEnum) {
		if (StringUtils.isNotBlank(ethnicityCode) && !"U".equals(ethnicityCode)){
			Ethnicity ethnicity = Ethnicity.valueOf(ethnicityCode); 
			
			asrRows[Enum.valueOf( asrRowEnum, asrRowName).ordinal()].getEthnicityGroups()[ethnicity.ordinal()]++;
			asrRows[Enum.valueOf(asrRowEnum, "TOTAL").ordinal()].getEthnicityGroups()[ethnicity.ordinal()]++;
			
			switch (asrRowName) {
			case "DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES":
			case "DRUG_SALE_MANUFACTURING_MARIJUANA":
			case "DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS":
			case "DRUG_SALE_MANUFACTURING_OTHER":
				asrRows[Enum.valueOf(asrRowEnum, "DRUG_SALE_MANUFACTURING_SUBTOTAL").ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;
				asrRows[Enum.valueOf(asrRowEnum, "DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL").ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;
				break;
			case "DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES":
			case "DRUG_POSSESSION_MARIJUANA":
			case "DRUG_POSSESSION_SYNTHETIC_NARCOTICS":
			case "DRUG_POSSESSION_OTHER":
				asrRows[Enum.valueOf(asrRowEnum, "DRUG_POSSESSION_SUBTOTAL").ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;
				asrRows[Enum.valueOf(asrRowEnum, "DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL").ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;;
				break;
				
			case "PROSTITUTION":
			case "ASSISTING_PROMOTING_PROSTITUTION": 
				asrRows[Enum.valueOf(asrRowEnum, "PROSTITUTION_AND_COMMERCIALIZED_VICE").ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;
				break;
			case "GAMBLING_TOTAL": 
				asrRows[Enum.valueOf(asrRowEnum, "GAMBLING_ALL_OTHER").ordinal()].getEthnicityGroups()[ethnicity.ordinal()] ++;
				break; 
			default:
				break;
			}
		}
	}

	private void countToRaceGroups(AsrRow[] asrRows, String raceCode,
			String asrRowName, Class asrRowEnumClass) {
		if (StringUtils.isNotBlank(raceCode) && !"U".equals(raceCode)){
			Race race = Race.valueOf(raceCode); 
			asrRows[Enum.valueOf(asrRowEnumClass, asrRowName).ordinal()].getRaceGroups()[race.ordinal()]++;
			asrRows[Enum.valueOf(asrRowEnumClass, "TOTAL").ordinal()].getRaceGroups()[race.ordinal()]++;
			
			switch (asrRowName) {
			case "DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES":
			case "DRUG_SALE_MANUFACTURING_MARIJUANA":
			case "DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS":
			case "DRUG_SALE_MANUFACTURING_OTHER":
				asrRows[Enum.valueOf(asrRowEnumClass, "DRUG_SALE_MANUFACTURING_SUBTOTAL").ordinal()].getRaceGroups()[race.ordinal()]++;
				asrRows[Enum.valueOf(asrRowEnumClass, "DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL").ordinal()].getRaceGroups()[race.ordinal()]++;
				break;
			case "DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES":
			case "DRUG_POSSESSION_MARIJUANA":
			case "DRUG_POSSESSION_SYNTHETIC_NARCOTICS":
			case "DRUG_POSSESSION_OTHER":
				asrRows[Enum.valueOf(asrRowEnumClass, "DRUG_POSSESSION_SUBTOTAL").ordinal()].getRaceGroups()[race.ordinal()]++;
				asrRows[Enum.valueOf(asrRowEnumClass, "DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL").ordinal()].getRaceGroups()[race.ordinal()]++;
				break;
			case "PROSTITUTION":
			case "ASSISTING_PROMOTING_PROSTITUTION": 
				asrRows[Enum.valueOf(asrRowEnumClass, "PROSTITUTION_AND_COMMERCIALIZED_VICE").ordinal()].getRaceGroups()[race.ordinal()]++;
				break;
			case "GAMBLING_TOTAL": 
				asrRows[Enum.valueOf(asrRowEnumClass, "GAMBLING_ALL_OTHER").ordinal()].getRaceGroups()[race.ordinal()] ++;
				break; 
			default:
				break;
			}

		}
	}
	
	private void countToAdultAgeGroups(AsrAdultRow[] asrAdultRows, Integer averageAge, String sexCode,
			AsrAdultRowName asrAdultRowName) {
		AdultAgeGroup ageGroup = getAdultAgeGroup(averageAge);
		if (ageGroup != null){
			switch( sexCode ){
			case "M":
				asrAdultRows[asrAdultRowName.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrAdultRows[asrAdultRowName.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrAdultRows[AsrAdultRowName.TOTAL.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
				
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
				case GAMBLING_TOTAL: 
					asrAdultRows[AsrAdultRowName.GAMBLING_ALL_OTHER.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.GAMBLING_ALL_OTHER.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
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
				case GAMBLING_TOTAL: 
					asrAdultRows[AsrAdultRowName.GAMBLING_ALL_OTHER.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					asrAdultRows[AsrAdultRowName.GAMBLING_ALL_OTHER.ordinal()].getFemaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
					break; 
				default:
					break;
				}
				break; 
			}
		}
	}

	private void countToJuvenileAgeGroups(AsrJuvenileRow[] asrJuvenilRows, Integer averageAge, String sexCode,
			AsrJuvenileRowName asrJuvenileRowName) {
		JuvenileAgeGroup ageGroup = getJuvenileAgeGroup(averageAge);
		if (ageGroup != null){
			switch( sexCode ){
			case "M":
				asrJuvenilRows[asrJuvenileRowName.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrJuvenilRows[asrJuvenileRowName.ordinal()].getMaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
				asrJuvenilRows[AsrJuvenileRowName.TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrJuvenilRows[AsrJuvenileRowName.TOTAL.ordinal()].getMaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
				
				switch (asrJuvenileRowName) {
				case DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES:
				case DRUG_SALE_MANUFACTURING_MARIJUANA:
				case DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS:
				case DRUG_SALE_MANUFACTURING_OTHER:
					asrJuvenilRows[AsrJuvenileRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()].getMaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getMaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
					
					break;
				case DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES:
				case DRUG_POSSESSION_MARIJUANA:
				case DRUG_POSSESSION_SYNTHETIC_NARCOTICS:
				case DRUG_POSSESSION_OTHER:
					asrJuvenilRows[AsrJuvenileRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()].getMaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getMaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
					break;
				case PROSTITUTION:
				case ASSISTING_PROMOTING_PROSTITUTION: 
					asrJuvenilRows[AsrJuvenileRowName.PROSTITUTION_AND_COMMERCIALIZED_VICE.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					break;
				case GAMBLING_TOTAL: 
					asrJuvenilRows[AsrJuvenileRowName.GAMBLING_ALL_OTHER.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.GAMBLING_ALL_OTHER.ordinal()].getMaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
					break; 
				default:
					break;
				}
				
				break; 
			case "F":
				asrJuvenilRows[asrJuvenileRowName.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
				asrJuvenilRows[asrJuvenileRowName.ordinal()].getFemaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
				asrJuvenilRows[AsrJuvenileRowName.TOTAL.ordinal()].getMaleAgeGroups()[ageGroup.ordinal()] ++;
				asrJuvenilRows[AsrJuvenileRowName.TOTAL.ordinal()].getMaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
				
				switch (asrJuvenileRowName) {
				case DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES:
				case DRUG_SALE_MANUFACTURING_MARIJUANA:
				case DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS:
				case DRUG_SALE_MANUFACTURING_OTHER:
					asrJuvenilRows[AsrJuvenileRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_SALE_MANUFACTURING_SUBTOTAL.ordinal()].getFemaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getFemaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
					
					break;
				case DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES:
				case DRUG_POSSESSION_MARIJUANA:
				case DRUG_POSSESSION_SYNTHETIC_NARCOTICS:
				case DRUG_POSSESSION_OTHER:
					asrJuvenilRows[AsrJuvenileRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_POSSESSION_SUBTOTAL.ordinal()].getFemaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL.ordinal()].getFemaleAgeGroups()[JuvenileAgeGroup.TOTAL.ordinal()] ++;
					break;
				case PROSTITUTION:
				case ASSISTING_PROMOTING_PROSTITUTION: 
					asrJuvenilRows[AsrJuvenileRowName.PROSTITUTION_AND_COMMERCIALIZED_VICE.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					break;
				case GAMBLING_TOTAL: 
					asrJuvenilRows[AsrJuvenileRowName.GAMBLING_ALL_OTHER.ordinal()].getFemaleAgeGroups()[ageGroup.ordinal()] ++;
					asrJuvenilRows[AsrJuvenileRowName.GAMBLING_ALL_OTHER.ordinal()].getFemaleAgeGroups()[AdultAgeGroup.TOTAL.ordinal()] ++;
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

	private JuvenileAgeGroup getJuvenileAgeGroup(Integer averageAge) {
		
		if (averageAge == null) return null; 
		
		JuvenileAgeGroup ageGroup = null; 
		
		if (averageAge < 10){
			ageGroup = JuvenileAgeGroup.Under10;
		}
		else if (averageAge <= 12){
			ageGroup = JuvenileAgeGroup._10To12;
		}
		else if (averageAge <=14) {
			ageGroup = JuvenileAgeGroup._13To14; 
		}
		else if (averageAge <= 17){
			ageGroup = JuvenileAgeGroup.valueOf("_" + averageAge); 
		}
		return ageGroup;
	}
	
}
