 
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.reports.supplementaryhomicide.HomicideSituation;
import org.search.nibrs.model.reports.supplementaryhomicide.Person;
import org.search.nibrs.model.reports.supplementaryhomicide.SupplementaryHomicideReport;
import org.search.nibrs.model.reports.supplementaryhomicide.SupplementaryHomicideReportRow;
import org.search.nibrs.stagingdata.AppProperties;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.VictimOffenderAssociation;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenderSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.search.nibrs.stagingdata.model.segment.VictimSegment;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.service.AdministrativeSegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplementaryHomicideReportService {

	private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	AdministrativeSegmentService administrativeSegmentService;
	@Autowired
	public AgencyRepository agencyRepository; 
	@Autowired
	public AppProperties appProperties; 
	
	public SupplementaryHomicideReportService() {
		super();
	}
	
	public SupplementaryHomicideReport createSupplementaryHomicideReport(String ownerId, String ori, Integer year,  Integer month ) {
		
		SupplementaryHomicideReport supplementaryHomicideReport = new SupplementaryHomicideReport(ori, year, month); 
		
		if (!"StateWide".equalsIgnoreCase(ori)){
			Agency agency = agencyRepository.findFirstByAgencyOri(ori); 
			if (agency!= null){
				supplementaryHomicideReport.setAgencyName(agency.getAgencyName());
				supplementaryHomicideReport.setStateName(agency.getStateName());
				supplementaryHomicideReport.setStateCode(agency.getStateCode());
				supplementaryHomicideReport.setPopulation(agency.getPopulation());
			}
			else{
				return supplementaryHomicideReport; 
			}
		}
		else{
			supplementaryHomicideReport.setAgencyName(ori);
			supplementaryHomicideReport.setStateName("");
			supplementaryHomicideReport.setStateCode("");
			supplementaryHomicideReport.setPopulation(null);
		}

		processMurderAndNonnegligentManslaughter(ori, year, month, supplementaryHomicideReport, ownerId);
		processNegligentManslaughter(ori, year, month, supplementaryHomicideReport, ownerId);
		

		log.info("supplementaryHomicideReport: " + supplementaryHomicideReport);
		return supplementaryHomicideReport;
	}

	private void processNegligentManslaughter(String ori, Integer year, Integer month, SupplementaryHomicideReport supplementaryHomicideReport, String ownerId) {
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentService.findIncidentByOriAndIncidentDateAndOffenses(ori, year, month, ownerId, "09B");
		
		for (AdministrativeSegment administrativeSegment: administrativeSegments){
			
			OffenseSegment offenseSegment = administrativeSegment.getOffenseSegments()
					.stream()
					.filter(offense->offense.getUcrOffenseCodeType().getNibrsCode().equals("09B"))
					.findAny().orElse(null); 
			
			List<String> weaponUsed = offenseSegment.getTypeOfWeaponForceInvolveds().stream()
					.map(typeOfWeaponForceInvolved -> typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getNibrsDescription())
					.collect(Collectors.toList());
			
			Set<VictimSegment> victimSegments = administrativeSegment.getVictimSegments()
					.stream()
					.filter(victimSegment -> victimSegment.getConnectedOffenseCodes().contains("09B"))
					.collect(Collectors.toSet());
			HomicideSituation homicideSituation = getHomicideSituation(administrativeSegment.getVictimSegments()); 
			
			for (VictimSegment victimSegment : victimSegments) {
				List<String> circumstances = victimSegment.getAggravatedAssaultHomicideCircumstancesTypes()
						.stream()
						.map(circumstance->circumstance.getNibrsDescription())
						.filter(i -> !"Blank".contentEquals(i))
						.collect(Collectors.toList());
				
				for (VictimOffenderAssociation victimOffenderAssociation: victimSegment.getVictimOffenderAssociations()) {
					SupplementaryHomicideReportRow homicideReportRow = createHomicideReportRow(administrativeSegment, weaponUsed,
							homicideSituation, circumstances, victimOffenderAssociation);
					
					supplementaryHomicideReport.getManslaughterByNegligence().add(homicideReportRow);
				}
			}
			

		}
	}

	private HomicideSituation getHomicideSituation(Set<VictimSegment> victimSegments) {
		Set<VictimSegment> victimSegmentsInRelationship = new HashSet<>();
		Set<OffenderSegment> offenderSegment = new HashSet<>();
		
		for (VictimSegment victimSegment: victimSegments) {
			victimSegment.getVictimOffenderAssociations().forEach(association-> victimSegmentsInRelationship.add(association.getVictimSegment()));
			victimSegment.getVictimOffenderAssociations().forEach(association-> offenderSegment.add(association.getOffenderSegment()));
		}
		
		long victimCount = victimSegmentsInRelationship.size(); 
		long unknownOffenderCount = offenderSegment.stream()
				.filter(offender -> offender.getOffenderSequenceNumber() == 0).count();
		long knownOffenderCount = offenderSegment.stream()
				.filter(offender -> offender.getOffenderSequenceNumber() > 0).count();
		HomicideSituation homicideSituation = null;  
		if (victimCount == 1) {
			if (knownOffenderCount == 1) {
				homicideSituation = HomicideSituation.A;
			}
			else if (knownOffenderCount > 1) {
				homicideSituation = HomicideSituation.C; 
			}
			else if (unknownOffenderCount > 0) {
				homicideSituation = HomicideSituation.B; 
			}
		}
		else if (victimCount > 1) {
			if (knownOffenderCount == 1) {
				homicideSituation = HomicideSituation.D;
			}
			else if (knownOffenderCount > 1) {
				homicideSituation = HomicideSituation.E; 
			}
			else if (unknownOffenderCount > 0) {
				homicideSituation = HomicideSituation.F; 
			}
			
		}
		return homicideSituation;
	}

	private OffenseSegment getMuderOrNonNegligentManslaughter(AdministrativeSegment administrativeSegment) {

		OffenseSegment offense = administrativeSegment.getOffenseSegments()
				.stream()
				.filter(offenseSegment->offenseSegment.getUcrOffenseCodeType().getNibrsCode().contentEquals("09A"))
				.findFirst().orElse(null);
		
		if (offense == null) {
			offense = administrativeSegment.getOffenseSegments()
					.stream()
					.filter(offenseSegment->offenseSegment.getUcrOffenseCodeType().getNibrsCode().contentEquals("09C"))
					.findFirst().orElse(null);
		}
		return offense;
	}

	private void processMurderAndNonnegligentManslaughter(String ori, Integer year, Integer month, SupplementaryHomicideReport supplementaryHomicideReport, String ownerId) {
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentService.findIncidentByOriAndIncidentDateAndOffenses(ori, year, month, ownerId, "09A", "09C");
		
		for (AdministrativeSegment administrativeSegment: administrativeSegments){
			
			OffenseSegment offenseSegment = getMuderOrNonNegligentManslaughter(administrativeSegment); 
			
			List<String> weaponUsed = offenseSegment.getTypeOfWeaponForceInvolveds().stream()
					.map(typeOfWeaponForceInvolved -> typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getNibrsDescription())
					.collect(Collectors.toList());
			
			Set<VictimSegment> victimSegments = administrativeSegment.getVictimSegments()
					.stream()
					.filter(victimSegment -> victimSegment.getConnectedOffenseCodes().contains(offenseSegment.getUcrOffenseCodeType().getNibrsCode()))
					.collect(Collectors.toSet());
			HomicideSituation homicideSituation = getHomicideSituation(administrativeSegment.getVictimSegments()); 
			
			for (VictimSegment victimSegment : victimSegments) {
				List<String> circumstances = victimSegment.getAggravatedAssaultHomicideCircumstancesTypes()
						.stream()
						.map(circumstance->circumstance.getNibrsDescription())
						.filter(i -> !"Blank".contentEquals(i))
						.collect(Collectors.toList());
				
				if (victimSegment.getAdditionalJustifiableHomicideCircumstancesType() != null 
						&& victimSegment.getAdditionalJustifiableHomicideCircumstancesType().getAdditionalJustifiableHomicideCircumstancesTypeId() != 99998) {
					String additionalCircumstance = victimSegment.getAdditionalJustifiableHomicideCircumstancesType().getNibrsDescription();
					circumstances.add(additionalCircumstance);
				}
				
				for (VictimOffenderAssociation victimOffenderAssociation: victimSegment.getVictimOffenderAssociations()) {
					SupplementaryHomicideReportRow homicideReportRow = createHomicideReportRow(administrativeSegment, weaponUsed,
							homicideSituation, circumstances, victimOffenderAssociation);
					
					supplementaryHomicideReport.getMurderAndNonNegligenceManslaughter().add(homicideReportRow);
				}
			}
		}
	}

	private SupplementaryHomicideReportRow createHomicideReportRow(AdministrativeSegment administrativeSegment,
			List<String> weaponUsed, HomicideSituation homicideSituation, List<String> circumstances,
			VictimOffenderAssociation victimOffenderAssociation) {
		Person victim= new Person(victimOffenderAssociation.getVictimSegment().getAgeString(), 
				victimOffenderAssociation.getVictimSegment().getSexOfPersonType().getNibrsCode(),
				victimOffenderAssociation.getVictimSegment().getRaceOfPersonType().getNibrsCode(), 
				victimOffenderAssociation.getVictimSegment().getEthnicityOfPersonType().getNibrsCode());
		Person offender = new Person(victimOffenderAssociation.getOffenderSegment().getAgeString(), 
				victimOffenderAssociation.getOffenderSegment().getSexOfPersonType().getNibrsCode(),
				victimOffenderAssociation.getOffenderSegment().getRaceOfPersonType().getNibrsCode(), 
				victimOffenderAssociation.getOffenderSegment().getEthnicityOfPersonType().getNibrsCode());;
		
		SupplementaryHomicideReportRow homicideReportRow = new SupplementaryHomicideReportRow();
		homicideReportRow.setIncidentNumber(administrativeSegment.getIncidentNumber());
		homicideReportRow.setHomicideSituation(homicideSituation);
		homicideReportRow.setVictim(victim);
		homicideReportRow.setOffender(offender);
		homicideReportRow.setWeaponUsed(weaponUsed);
		homicideReportRow.setRelationshipOfVictimToOffender(victimOffenderAssociation.getVictimOffenderRelationshipType().getNibrsDescription());
		homicideReportRow.setCircumstances(circumstances);
		return homicideReportRow;
	}

}
