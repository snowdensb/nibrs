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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.search.nibrs.stagingdata.model.search.IncidentSearchRequest;
import org.search.nibrs.stagingdata.model.search.IncidentSearchResult;
import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.springframework.stereotype.Repository;

@Repository
public class ArrestReportSegmentRepositorCustomImpl implements ArrestReportSegmentRepositoryCustom{
	@PersistenceContext
    private EntityManager entityManager;

	@Override
	public List<IncidentSearchResult> findAllByCriteria(IncidentSearchRequest incidentSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<IncidentSearchResult> query = criteriaBuilder.createQuery(IncidentSearchResult.class);
        Root<ArrestReportSegment> root = query.from(ArrestReportSegment.class);
        query.multiselect(root.get("arrestReportSegmentId"),
        		criteriaBuilder.literal("GroupB"), root.get("arrestTransactionNumber"), 
        		root.get("agency").get("agencyId"), root.get("agency").get("agencyName"), root.get("arrestDate"), 
        		root.get("ucrOffenseCodeType").get("ucrOffenseCodeTypeId"),
        		root.get("ucrOffenseCodeType").get("nibrsCode"),
        		root.get("monthOfTape"), root.get("yearOfTape"), 
        		root.get("reportTimestamp"));

        Subquery<Integer> subQuery = query.subquery(Integer.class);
        Root<ArrestReportSegment> subRoot = subQuery.from(ArrestReportSegment.class);
        subQuery.distinct(true);
        subQuery.groupBy(subRoot.get("arrestTransactionNumber"));
        subQuery.select(criteriaBuilder.max(subRoot.get("arrestReportSegmentId")));
        List<Predicate> predicates = getArrestReportSegmentPredicates(incidentSearchRequest, subRoot, criteriaBuilder);

        subQuery
        	.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

        query.where(root.get("arrestReportSegmentId").in(subQuery));
		return entityManager.createQuery(query)
	            .getResultList();
	}

	@Override
	public long countAllByCriteria(IncidentSearchRequest incidentSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<ArrestReportSegment> root = query.from(ArrestReportSegment.class);
        List<Predicate> predicates = getArrestReportSegmentPredicates(incidentSearchRequest, root, criteriaBuilder);
        
		query.select(criteriaBuilder.countDistinct(root.get("arrestTransactionNumber")))
			.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		return entityManager.createQuery(query).getSingleResult();
	}

	private List<Predicate> getArrestReportSegmentPredicates(IncidentSearchRequest incidentSearchRequest,
			Root<ArrestReportSegment> root, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
        if(incidentSearchRequest != null) {
        	if (incidentSearchRequest.getAgencyIds() != null && incidentSearchRequest.getAgencyIds().size() > 0) {
        		predicates.add(criteriaBuilder.and(root.get("agency").get("agencyId").in(incidentSearchRequest.getAgencyIds())));
        	}
        	
        	if (incidentSearchRequest.getIncidentIdentifier() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("arrestTransactionNumber"), incidentSearchRequest.getIncidentIdentifier())));
        	}
        	
        	if (incidentSearchRequest.getIncidentDate() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("arrestDate"), incidentSearchRequest.getIncidentDate())));
        	}
        	
        	if (incidentSearchRequest.getUcrOffenseCodeTypeId() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("ucrOffenseCodeType").get("ucrOffenseCodeTypeId"), 
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
