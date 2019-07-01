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
package org.search.nibrs.stagingdata.repository.segment;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.search.nibrs.stagingdata.model.search.IncidentSearchRequest;
import org.search.nibrs.stagingdata.model.search.IncidentSearchResult;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.springframework.stereotype.Repository;

@Repository
public class AdministrativeSegmentRepositorCustomImpl implements AdministrativeSegmentRepositoryCustom{
	@PersistenceContext
    private EntityManager entityManager;

	@Override
	public List<IncidentSearchResult> findAllByCriteria(IncidentSearchRequest incidentSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<IncidentSearchResult> query = criteriaBuilder.createQuery(IncidentSearchResult.class);
        Root<AdministrativeSegment> root = query.from(AdministrativeSegment.class);
		Join<AdministrativeSegment, OffenseSegment> joinOptions = root.join("offenseSegments", JoinType.LEFT);
		query.multiselect(root.get("administrativeSegmentId"),
				criteriaBuilder.literal("A"),root.get("incidentNumber"), 
				root.get("agency").get("agencyId"), root.get("agency").get("agencyName"), root.get("incidentDate"), 
				joinOptions.get("ucrOffenseCodeType").get("ucrOffenseCodeTypeId"),
				joinOptions.get("ucrOffenseCodeType").get("nibrsCode"),
				root.get("monthOfTape"), root.get("yearOfTape"), 
				root.get("reportTimestamp"));

        Subquery<Integer> subQuery = query.subquery(Integer.class);
        Root<AdministrativeSegment> subRoot = subQuery.from(AdministrativeSegment.class);
        subQuery.distinct(true);
        subQuery.groupBy(subRoot.get("incidentNumber"));
        subQuery.select(criteriaBuilder.max(subRoot.get("administrativeSegmentId")));
        List<Predicate> predicates = getAdministrativeSegmentPredicates(incidentSearchRequest, subRoot, criteriaBuilder);

        subQuery
        	.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
        
        Subquery<Integer> offenseSubQuery = query.subquery(Integer.class);
        Root<OffenseSegment> offenseSubRoot = offenseSubQuery.from(OffenseSegment.class);
        offenseSubQuery.select(criteriaBuilder.min(offenseSubRoot.get("ucrOffenseCodeType").get("ucrOffenseCodeTypeId")));
        offenseSubQuery.where(criteriaBuilder.equal(
        		offenseSubRoot.get("administrativeSegment").get("administrativeSegmentId"), 
        		root.get("administrativeSegmentId")));

        query.where(criteriaBuilder.and(
        		root.get("administrativeSegmentId").in(subQuery), 
        		criteriaBuilder.equal(joinOptions.get("ucrOffenseCodeType").get("ucrOffenseCodeTypeId"), offenseSubQuery)));
        
		return entityManager.createQuery(query)
	            .getResultList();
	}

	@Override
	public long countAllByCriteria(IncidentSearchRequest incidentSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<AdministrativeSegment> root = query.from(AdministrativeSegment.class);
        List<Predicate> predicates = getAdministrativeSegmentPredicates(incidentSearchRequest, root, criteriaBuilder);
        
		query.select(criteriaBuilder.countDistinct(root.get("incidentNumber")))
			.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		return entityManager.createQuery(query).getSingleResult();
	}

	private List<Predicate> getAdministrativeSegmentPredicates(IncidentSearchRequest incidentSearchRequest,
			Root<AdministrativeSegment> root, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
        if(incidentSearchRequest != null) {
        	if (incidentSearchRequest.getAgencyIds() != null && incidentSearchRequest.getAgencyIds().size() > 0) {
        		predicates.add(criteriaBuilder.and(root.get("agency").get("agencyId").in(incidentSearchRequest.getAgencyIds())));
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


}
