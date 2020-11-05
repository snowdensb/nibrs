
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
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.codes.TypeOfPropertyLossCode;
import org.search.nibrs.model.reports.SummaryReportRequest;
import org.search.nibrs.model.reports.arson.ArsonReport;
import org.search.nibrs.model.reports.arson.ArsonRow;
import org.search.nibrs.model.reports.arson.ArsonRowName;
import org.search.nibrs.stagingdata.AppProperties;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.PropertyType;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.PropertySegment;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.service.AdministrativeSegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArsonFormService {

	private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	AdministrativeSegmentService administrativeSegmentService;
	@Autowired
	public AgencyRepository agencyRepository; 
	@Autowired
	public AppProperties appProperties; 
	private Map<String, Integer> propertyDescriptionHierarchyMap; 
	private Map<String, ArsonRowName> propertyDescriptionArsonRowNameMap; 

	public ArsonFormService() {
		propertyDescriptionHierarchyMap = new HashMap<>();
		propertyDescriptionHierarchyMap.put("29", 1);
		propertyDescriptionHierarchyMap.put("30", 1);
		propertyDescriptionHierarchyMap.put("34", 1);
		propertyDescriptionHierarchyMap.put("32", 1);
		propertyDescriptionHierarchyMap.put("31", 1);
		propertyDescriptionHierarchyMap.put("33", 1);
		propertyDescriptionHierarchyMap.put("35", 1);
		propertyDescriptionHierarchyMap.put("03", 2);
		propertyDescriptionHierarchyMap.put("05", 2);
		propertyDescriptionHierarchyMap.put("24", 2);
		propertyDescriptionHierarchyMap.put("37", 2);
		propertyDescriptionHierarchyMap.put("01", 2);
		propertyDescriptionHierarchyMap.put("12", 2);
		propertyDescriptionHierarchyMap.put("15", 2);
		propertyDescriptionHierarchyMap.put("28", 2);
		propertyDescriptionHierarchyMap.put("39", 2);
		propertyDescriptionHierarchyMap.put("78", 2);
		propertyDescriptionHierarchyMap.put("02", 3);
		propertyDescriptionHierarchyMap.put("04", 3);
		propertyDescriptionHierarchyMap.put("06", 3);
		propertyDescriptionHierarchyMap.put("07", 3);
		propertyDescriptionHierarchyMap.put("08", 3);
		propertyDescriptionHierarchyMap.put("09", 3);
		propertyDescriptionHierarchyMap.put("10", 3);
		propertyDescriptionHierarchyMap.put("11", 3);
		propertyDescriptionHierarchyMap.put("13", 3);
		propertyDescriptionHierarchyMap.put("14", 3);
		propertyDescriptionHierarchyMap.put("16", 3);
		propertyDescriptionHierarchyMap.put("17", 3);
		propertyDescriptionHierarchyMap.put("18", 3);
		propertyDescriptionHierarchyMap.put("19", 3);
		propertyDescriptionHierarchyMap.put("20", 3);
		propertyDescriptionHierarchyMap.put("21", 3);
		propertyDescriptionHierarchyMap.put("22", 3);
		propertyDescriptionHierarchyMap.put("23", 3);
		propertyDescriptionHierarchyMap.put("25", 3);
		propertyDescriptionHierarchyMap.put("26", 3);
		propertyDescriptionHierarchyMap.put("27", 3);
		propertyDescriptionHierarchyMap.put("36", 3);
		propertyDescriptionHierarchyMap.put("38", 3);
		propertyDescriptionHierarchyMap.put("41", 3);
		propertyDescriptionHierarchyMap.put("42", 3);
		propertyDescriptionHierarchyMap.put("43", 3);
		propertyDescriptionHierarchyMap.put("44", 3);
		propertyDescriptionHierarchyMap.put("45", 3);
		propertyDescriptionHierarchyMap.put("46", 3);
		propertyDescriptionHierarchyMap.put("47", 3);
		propertyDescriptionHierarchyMap.put("48", 3);
		propertyDescriptionHierarchyMap.put("49", 3);
		propertyDescriptionHierarchyMap.put("59", 3);
		propertyDescriptionHierarchyMap.put("64", 3);
		propertyDescriptionHierarchyMap.put("65", 3);
		propertyDescriptionHierarchyMap.put("66", 3);
		propertyDescriptionHierarchyMap.put("67", 3);
		propertyDescriptionHierarchyMap.put("68", 3);
		propertyDescriptionHierarchyMap.put("69", 3);
		propertyDescriptionHierarchyMap.put("70", 3);
		propertyDescriptionHierarchyMap.put("71", 3);
		propertyDescriptionHierarchyMap.put("72", 3);
		propertyDescriptionHierarchyMap.put("73", 3);
		propertyDescriptionHierarchyMap.put("74", 3);
		propertyDescriptionHierarchyMap.put("75", 3);
		propertyDescriptionHierarchyMap.put("76", 3);
		propertyDescriptionHierarchyMap.put("77", 3);
		propertyDescriptionHierarchyMap.put("79", 3);
		propertyDescriptionHierarchyMap.put("80", 3);
		
		
		propertyDescriptionArsonRowNameMap = new HashMap<>();
		propertyDescriptionArsonRowNameMap.put("29", ArsonRowName.SINGLE_OCCUPANCY_RESIDENTIAL);
		propertyDescriptionArsonRowNameMap.put("30", ArsonRowName.OTHER_RESIDENTIAL);
		propertyDescriptionArsonRowNameMap.put("34", ArsonRowName.STORAGE);
		propertyDescriptionArsonRowNameMap.put("32", ArsonRowName.INDUSTRIAL_MANUFACTURING);
		propertyDescriptionArsonRowNameMap.put("31", ArsonRowName.OTHER_COMMERCIAL);
		propertyDescriptionArsonRowNameMap.put("33", ArsonRowName.COMMUNITY_PUBLIC);
		propertyDescriptionArsonRowNameMap.put("35", ArsonRowName.ALL_OTHER_STRUCTURE);
		propertyDescriptionArsonRowNameMap.put("03", ArsonRowName.MOTOR_VEHICLES);
		propertyDescriptionArsonRowNameMap.put("05", ArsonRowName.MOTOR_VEHICLES);
		propertyDescriptionArsonRowNameMap.put("24", ArsonRowName.MOTOR_VEHICLES);
		propertyDescriptionArsonRowNameMap.put("37", ArsonRowName.MOTOR_VEHICLES);
		propertyDescriptionArsonRowNameMap.put("01", ArsonRowName.OTHER_MOBILE_PROPERTY);
		propertyDescriptionArsonRowNameMap.put("12", ArsonRowName.OTHER_MOBILE_PROPERTY);
		propertyDescriptionArsonRowNameMap.put("15", ArsonRowName.OTHER_MOBILE_PROPERTY);
		propertyDescriptionArsonRowNameMap.put("28", ArsonRowName.OTHER_MOBILE_PROPERTY);
		propertyDescriptionArsonRowNameMap.put("39", ArsonRowName.OTHER_MOBILE_PROPERTY);
		propertyDescriptionArsonRowNameMap.put("78", ArsonRowName.OTHER_MOBILE_PROPERTY);
		propertyDescriptionArsonRowNameMap.put("02", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("04", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("06", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("07", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("08", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("09", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("10", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("11", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("13", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("14", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("16", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("17", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("18", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("19", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("20", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("21", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("22", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("23", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("25", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("26", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("27", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("36", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("38", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("41", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("42", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("43", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("44", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("45", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("46", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("47", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("48", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("49", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("59", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("64", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("65", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("66", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("67", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("68", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("69", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("70", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("71", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("72", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("73", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("74", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("75", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("76", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("77", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("79", ArsonRowName.TOTAL_OTHER);
		propertyDescriptionArsonRowNameMap.put("80", ArsonRowName.TOTAL_OTHER);
	}
	
	public ArsonReport createArsonSummaryReportsByRequest(SummaryReportRequest summaryReportRequest ) {
		
		ArsonReport arsonReport = new ArsonReport(summaryReportRequest.getIncidentYear(), summaryReportRequest.getIncidentMonth());
		
		if (summaryReportRequest.getAgencyId() != null){
			Optional<Agency> agency = agencyRepository.findById(summaryReportRequest.getAgencyId()); 
			if (agency.isPresent()){
				arsonReport.setAgencyName(agency.get().getAgencyName());
				arsonReport.setStateName(agency.get().getStateName());
				arsonReport.setStateCode(agency.get().getStateCode());
				arsonReport.setPopulation(agency.get().getPopulation());
			}
			else{
				return arsonReport; 
			}
		}
		else{
			Agency agency = agencyRepository.findFirstByStateCode(summaryReportRequest.getStateCode());
			arsonReport.setAgencyName("");
			arsonReport.setStateName(agency.getStateName());
			arsonReport.setStateCode(summaryReportRequest.getStateCode());
			arsonReport.setPopulation(null);
		}
		processReportedOffenses(summaryReportRequest, arsonReport);
		processClearedOffenses(summaryReportRequest, arsonReport);
		log.debug("arsonReport: " + arsonReport);
		log.debug("summaryReportRequest: " + summaryReportRequest);
		return arsonReport;
	}
	
	private void processClearedOffenses(SummaryReportRequest summaryReportRequest, ArsonReport arsonReport) {
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentService.findBySummaryReportRequestClearanceDateAndOffenses(summaryReportRequest, Arrays.asList("200"));
		getClearanceRows(arsonReport, administrativeSegments);
	}

	private void getClearanceRows(ArsonReport arsonReport, List<AdministrativeSegment> administrativeSegments) {
		ArsonRow[] arsonRows = arsonReport.getArsonRows();
		for (AdministrativeSegment administrativeSegment: administrativeSegments){
			String offenseAttemptedIndicator = administrativeSegment.getOffenseSegments()
					.stream()
					.filter(offense->"200".equals(offense.getUcrOffenseCodeType().getNibrsCode()))
					.map(offense->offense.getOffenseAttemptedCompleted())
					.findFirst().orElse("");  
			PropertySegment burnedProperty = administrativeSegment.getPropertySegments()
					.stream()
					.filter(property -> TypeOfPropertyLossCode._2.code.equals(property.getTypePropertyLossEtcType().getNibrsCode()))
					.findFirst().orElse(null);
			if (Arrays.asList("A", "C").contains(offenseAttemptedIndicator) && burnedProperty != null) {
				if (administrativeSegment.isClearanceInvolvingOnlyJuvenile()) {
					arsonRows[ArsonRowName.GRAND_TOTAL.ordinal()].increaseClearanceInvolvingOnlyJuvenile(1);
				}
				arsonRows[ArsonRowName.GRAND_TOTAL.ordinal()].increaseClearedOffenses(1);
				
				if ("C".equalsIgnoreCase(offenseAttemptedIndicator)) {
					ArsonRowName arsonRowName = getArsonRowName(burnedProperty);
					if (arsonRowName != null) {
						arsonRows[arsonRowName.ordinal()].increaseClearedOffenses(1);;
						if (administrativeSegment.isClearanceInvolvingOnlyJuvenile()) {
							arsonRows[arsonRowName.ordinal()].increaseClearanceInvolvingOnlyJuvenile(1);
						}
							
						switch (arsonRowName) {
						case SINGLE_OCCUPANCY_RESIDENTIAL: 
						case OTHER_RESIDENTIAL:
						case STORAGE:
						case INDUSTRIAL_MANUFACTURING: 
						case OTHER_COMMERCIAL: 
						case COMMUNITY_PUBLIC: 
						case ALL_OTHER_STRUCTURE:
							arsonRows[ArsonRowName.TOTAL_STRUCTURE.ordinal()].increaseClearedOffenses(1);;
							if (administrativeSegment.isClearanceInvolvingOnlyJuvenile()) {
								arsonRows[ArsonRowName.TOTAL_STRUCTURE.ordinal()].increaseClearanceInvolvingOnlyJuvenile(1);
							}
							break;
						case MOTOR_VEHICLES: 
						case OTHER_MOBILE_PROPERTY:
							arsonRows[ArsonRowName.TOTAL_MOBILE.ordinal()].increaseClearedOffenses(1);
							if (administrativeSegment.isClearanceInvolvingOnlyJuvenile()) {
								arsonRows[ArsonRowName.TOTAL_MOBILE.ordinal()].increaseClearanceInvolvingOnlyJuvenile(1);
							}
							break;
						default: 
						}
					}
				}
			}
		}
	}

	private void processReportedOffenses(SummaryReportRequest summaryReportRequest, ArsonReport arsonReport) {
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentService.findBySummaryReportRequestAndOffenses(summaryReportRequest, Arrays.asList("200"));
		
		getArsonReportRows(arsonReport, administrativeSegments);
	}

	private void getArsonReportRows(ArsonReport arsonReport, List<AdministrativeSegment> administrativeSegments) {
		ArsonRow[] arsonRows = arsonReport.getArsonRows();
		for (AdministrativeSegment administrativeSegment: administrativeSegments){
			String offenseAttemptedIndicator = administrativeSegment.getOffenseSegments()
					.stream()
					.filter(offense->"200".equals(offense.getUcrOffenseCodeType().getNibrsCode()))
					.map(offense->offense.getOffenseAttemptedCompleted())
					.findFirst().orElse("");  
			PropertySegment burnedProperty = administrativeSegment.getPropertySegments()
					.stream()
					.filter(property -> TypeOfPropertyLossCode._2.code.equals(property.getTypePropertyLossEtcType().getNibrsCode()))
					.findFirst().orElse(null);
			if (Arrays.asList("A", "C").contains(offenseAttemptedIndicator) && burnedProperty != null) {
				Double totalValue = burnedProperty.getPropertyTypes()
						.stream()
						.mapToDouble(propertyType -> propertyType.getValueOfProperty())
						.sum(); 
				arsonRows[ArsonRowName.GRAND_TOTAL.ordinal()].increaseActualOffenses(1);
				arsonRows[ArsonRowName.GRAND_TOTAL.ordinal()].increaseEstimatedPropertyDamage(totalValue);
				
				if ("C".equalsIgnoreCase(offenseAttemptedIndicator)) {
					ArsonRowName arsonRowName = getArsonRowName(burnedProperty);
					if (arsonRowName != null) {
						arsonRows[arsonRowName.ordinal()].increaseActualOffenses(1);
						arsonRows[arsonRowName.ordinal()].increaseEstimatedPropertyDamage(totalValue);
						
						switch (arsonRowName) {
						case SINGLE_OCCUPANCY_RESIDENTIAL: 
						case OTHER_RESIDENTIAL:
						case STORAGE:
						case INDUSTRIAL_MANUFACTURING: 
						case OTHER_COMMERCIAL: 
						case COMMUNITY_PUBLIC: 
						case ALL_OTHER_STRUCTURE: 
							arsonRows[ArsonRowName.TOTAL_STRUCTURE.ordinal()].increaseActualOffenses(1);
							arsonRows[ArsonRowName.TOTAL_STRUCTURE.ordinal()].increaseEstimatedPropertyDamage(totalValue);
							break;
						case MOTOR_VEHICLES: 
						case OTHER_MOBILE_PROPERTY:
							arsonRows[ArsonRowName.TOTAL_MOBILE.ordinal()].increaseActualOffenses(1);
							arsonRows[ArsonRowName.TOTAL_MOBILE.ordinal()].increaseEstimatedPropertyDamage(totalValue);
							break;
						default: 
						}
					}
				}
			}
		}
	}
	
	private ArsonRowName getArsonRowName(PropertySegment propertySegment) {
		Double currentValue = 0.0; 
		Integer currentLevel = 4;
		String currentPropertyDescription = "";
		
		for (PropertyType propertyType: propertySegment.getPropertyTypes()) {
			String propertyDescription = propertyType.getPropertyDescriptionType().getNibrsCode();
			Integer level = propertyDescriptionHierarchyMap.get(propertyDescription);
			if (level <= 0) continue;
			
			if (level < currentLevel || 
				(level == currentLevel && propertyType.getValueOfProperty() > currentValue)) {
				currentPropertyDescription = propertyDescription;
				currentValue = propertyType.getValueOfProperty();
			}
		}
		
		ArsonRowName arsonRowName = propertyDescriptionArsonRowNameMap.get(currentPropertyDescription); 
		return arsonRowName;
	}

}
