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

import static org.search.nibrs.stagingdata.util.ObjectUtils.getInteger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.reports.SummaryReportRequest;
import org.search.nibrs.model.reports.cargotheft.CargoTheftFormRow;
import org.search.nibrs.stagingdata.model.search.IncidentPointer;
import org.search.nibrs.stagingdata.model.search.IncidentSearchRequest;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.search.nibrs.stagingdata.repository.segment.AdministrativeSegmentRepository;
import org.search.nibrs.stagingdata.repository.segment.AdministrativeSegmentRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Service to process Group B Arrest Report.  
 *
 */
@Service
public class AdministrativeSegmentService {
	private static final Log log = LogFactory.getLog(AdministrativeSegmentService.class);
	
	@Autowired
	AdministrativeSegmentRepository administrativeSegmentRepository;
	@Autowired
	AdministrativeSegmentRepositoryCustom administrativeSegmentRepositoryCustom;
	
	public AdministrativeSegment find(Integer id){
		return administrativeSegmentRepository.findByAdministrativeSegmentId(id);
	}
	
	public List<AdministrativeSegment> findAllAdministrativeSegments(){
		List<AdministrativeSegment> administrativeSegments = new ArrayList<>();
		administrativeSegmentRepository.findAll().forEach(administrativeSegments::add);
		return administrativeSegments;
	}
	
	public List<AdministrativeSegment> findByCriteria(IncidentSearchRequest incidentSearchRequest){
		return administrativeSegmentRepository.findAll(new Specification<AdministrativeSegment>() {
			private static final long serialVersionUID = 2264585355475434091L;

			@Override
            public Predicate toPredicate(Root<AdministrativeSegment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				query.distinct(true);
//				query.groupBy(root.get("incidentNumber"));
//				query.multiselect(criteriaBuilder.max(root.get("administrativeSegmentId")));
                List<Predicate> predicates = getAdministrativeSegmentPredicates(incidentSearchRequest, root, criteriaBuilder);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }

        });
	}

	public List<IncidentPointer> findAllByCriteria(IncidentSearchRequest incidentSearchRequest){
		return administrativeSegmentRepositoryCustom.findAllByCriteria(incidentSearchRequest);
	}
	
	public long countAllByCriteria(IncidentSearchRequest incidentSearchRequest){
		return administrativeSegmentRepositoryCustom.countAllByCriteria(incidentSearchRequest);
	}
	
	public long countByCriteria(IncidentSearchRequest incidentSearchRequest){
		return administrativeSegmentRepository.count(new Specification<AdministrativeSegment>() {
			private static final long serialVersionUID = 2264585355475434091L;
			
			@Override
			public Predicate toPredicate(Root<AdministrativeSegment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//				query.groupBy(root.get("incidentNumber"));
//				query.select(criteriaBuilder.max(root.get("administrativeSegmentId")));
				query.distinct(true);
				List<Predicate> predicates = getAdministrativeSegmentPredicates(incidentSearchRequest, root, criteriaBuilder);
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			
		});
	}
	
	private List<Predicate> getAdministrativeSegmentPredicates(IncidentSearchRequest incidentSearchRequest,
			Root<AdministrativeSegment> root, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
        if(incidentSearchRequest != null) {
        	if (incidentSearchRequest.getAgencyIds() != null && incidentSearchRequest.getAgencyIds().size() > 0) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.in(root.get("agencyId").in(incidentSearchRequest.getAgencyIds()))));
        	}
        	
        	if (incidentSearchRequest.getIncidentIdentifier() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("incidentNumber"), incidentSearchRequest.getIncidentIdentifier())));
        	}
        	
        	if (incidentSearchRequest.getIncidentDateRangeStartDate() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("incidentDate"), incidentSearchRequest.getIncidentDateRangeStartDate())));
        	}
        	
        	if (incidentSearchRequest.getIncidentDateRangeEndDate() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("incidentDate"), incidentSearchRequest.getIncidentDateRangeEndDate())));
        	}
        	
        	if (incidentSearchRequest.getUcrOffenseCodeTypeId() != null) {
        		Join<AdministrativeSegment, OffenseSegment> joinOptions = root.join("offenseSegments", JoinType.LEFT);
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(joinOptions.get("ucrOffenseCodeType").get("ucrOffenseCodeTypeId"), 
        				incidentSearchRequest.getUcrOffenseCodeTypeId())));
        	}
        	
        	if (incidentSearchRequest.getSubmissionMonth() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("monthOfTape"), incidentSearchRequest.getSubmissionMonth())));
        	}
        	
        	if (incidentSearchRequest.getSubmissionYear() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("yearOfTape"), incidentSearchRequest.getSubmissionYear())));
        	}
        }
		return predicates;
	}

	public List<AdministrativeSegment> findByOriAndClearanceDate(String ori, Integer year, Integer month){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndClearanceDate(ori, year, month);
			
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		return administrativeSegments; 
	}

	public List<AdministrativeSegment> findByOriAndIncidentDate(String ori, Integer year, Integer month){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndIncidentDate(ori, year, month);
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<AdministrativeSegment> findByOriAndIncidentDateAndOffenses(Integer ownerId, String ori, Integer year, Integer month, List<String> offenseCodes){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndIncidentDateAndOffenses(ori, year, month, offenseCodes, ownerId);
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<AdministrativeSegment> findBySummaryReportRequestAndOffenses(SummaryReportRequest summaryReportRequest, List<String> offenseCodes){
		
		List<Integer> ids = 
				administrativeSegmentRepository.findIdsBySummaryReportRequestAndOffenses(
						summaryReportRequest.getStateCode(), summaryReportRequest.getAgencyId(), 
						summaryReportRequest.getIncidentYear(), summaryReportRequest.getIncidentMonth(), 
						summaryReportRequest.getOwnerId(), offenseCodes);
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<AdministrativeSegment> findBySummaryReportRequestClearanceDateAndOffenses(SummaryReportRequest summaryReportRequest,
			List<String> offenseCodes) {
		log.info("summaryReportRequest:" + summaryReportRequest);
		List<Integer> ids = administrativeSegmentRepository.findIdsByStateCodeAndOriAndClearanceDateAndOffenses(
				summaryReportRequest.getStateCode(), summaryReportRequest.getAgencyId(), 
				summaryReportRequest.getIncidentYear(), summaryReportRequest.getIncidentMonth(), 
				summaryReportRequest.getOwnerId(), offenseCodes);
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}

	public List<AdministrativeSegment> findArsonIncidentByOriAndIncidentDate(String ori, Integer year, Integer month, String ownerId){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findArsonIdsByOriAndIncidentDate(ori, year, month);
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<AdministrativeSegment> findHumanTraffickingIncidentByOriAndIncidentDate(String ori, Integer year, Integer month, String ownerId){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndIncidentDateAndOffenses(ori, year, month, Arrays.asList("64A", "64B"), getInteger(ownerId));
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<AdministrativeSegment> findIncidentByOriAndIncidentDateAndOffenses(String ori, Integer year, Integer month, String ownerId, String... offenseCodes){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndIncidentDateAndOffenses(ori, year, month, Arrays.asList(offenseCodes), getInteger(ownerId));
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<AdministrativeSegment> findArsonIncidentByOriAndAClearanceDate(String ori, Integer year, Integer month, String ownerId){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findArsonIdsByOriAndClearanceDate(ori, year, month, getInteger(ownerId));
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<ArresteeSegment> findArresteeSegmentByOriAndArrestDate(String ori, Integer arrestYear, Integer arrestMonth, String ownerId){
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndArrestDate(ori, arrestYear, arrestMonth, getInteger(ownerId));
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		log.info("ids size" + ids.size());
		log.info("administrativeSegments size" + administrativeSegments.size());
		
		List<ArresteeSegment> arresteeSegments = administrativeSegments.stream()
				.flatMap(i->i.getArresteeSegments().stream())
				.filter(i->Objects.equals(i.getArrestDateType().getYearNum(), arrestYear) &&
						( arrestMonth == 0 ||Objects.equals(i.getArrestDateType().getMonthNum(),arrestMonth)))
				.collect(Collectors.toList());
		return arresteeSegments; 
		
	}

	public List<AdministrativeSegment> findHumanTraffickingIncidentByOriAndClearanceDate(String ori, Integer year,
			Integer month, String ownerId) {
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndClearanceDateAndOffenses(ori, year, month, Arrays.asList("64A", "64B"), getInteger(ownerId));
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}

	public List<CargoTheftFormRow> findCargoTheftRowsByOriAndIncidentDate(String ori, Integer year,
			Integer month, String ownerId) {
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findCargoTheftIdsByOriAndIncidentDate(ori, year, month, getInteger(ownerId));
		
		log.info("ids:" + ids);
		
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentRepository.findAllById(ids);
		List<CargoTheftFormRow> cargoTheftFormRows = administrativeSegments.stream()
				.map(item-> new CargoTheftFormRow(item.getIncidentNumber(), item.getIncidentDate(), item.getSegmentActionType().getNibrsDescription()))
				.collect(Collectors.toList());
		return cargoTheftFormRows;
	}
	public List<AdministrativeSegment> findByOriAndClearanceDateAndOffenses(Integer ownerId, String ori, Integer year, Integer month,
			ArrayList<String> offenseCodes) {
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndClearanceDateAndOffenses(ori, year, month, offenseCodes, ownerId);
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}

	
}
