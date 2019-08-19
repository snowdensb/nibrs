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
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.BooleanUtils;
import org.search.nibrs.stagingdata.model.Submission;
import org.search.nibrs.stagingdata.model.search.IncidentPointer;
import org.search.nibrs.stagingdata.model.search.IncidentSearchRequest;
import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.springframework.stereotype.Repository;

@Repository
public class ArrestReportSegmentRepositorCustomImpl implements ArrestReportSegmentRepositoryCustom{
	@PersistenceContext
    private EntityManager entityManager;

	@Override
	public List<IncidentPointer> findAllByCriteria(IncidentSearchRequest incidentSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<IncidentPointer> query = criteriaBuilder.createQuery(IncidentPointer.class);
        Root<ArrestReportSegment> root = query.from(ArrestReportSegment.class);
        
        Join<ArrestReportSegment, Submission> submissionJoin = root.join("submission", JoinType.LEFT);

        query.multiselect(root.get("arrestReportSegmentId"),
        		criteriaBuilder.literal("B"), root.get("arrestTransactionNumber"), 
        		root.get("agency").get("agencyId"), root.get("agency").get("agencyName"), root.get("arrestDate"), 
        		root.get("ucrOffenseCodeType").get("ucrOffenseCodeTypeId"),
        		root.get("ucrOffenseCodeType").get("nibrsCode"),
        		root.get("monthOfTape"), root.get("yearOfTape"), 
        		root.get("reportTimestamp"), 
        		submissionJoin.get("acceptedIndicator"));

        List<Predicate> predicates = getArrestReportSegmentPredicates(incidentSearchRequest, root, criteriaBuilder);

        query.where(criteriaBuilder.and(
        		predicates.toArray( new Predicate[predicates.size()])));
		return entityManager.createQuery(query)
	            .getResultList();
	}

	@Override
	public long countAllByCriteria(IncidentSearchRequest incidentSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<ArrestReportSegment> root = query.from(ArrestReportSegment.class);
        
        List<Predicate> predicates = getArrestReportSegmentPredicates(incidentSearchRequest, root, criteriaBuilder);
        
		query.select(criteriaBuilder.count(root.get("arrestReportSegmentId")))
			.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		return entityManager.createQuery(query).getSingleResult();
	}

	private List<Predicate> getArrestReportSegmentPredicates(IncidentSearchRequest incidentSearchRequest,
			Root<ArrestReportSegment> root, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
        if(incidentSearchRequest != null) {
            if (BooleanUtils.isTrue(incidentSearchRequest.getFbiSubmission())) {
            	predicates.add(criteriaBuilder.and(criteriaBuilder.isNotNull(root.get("submission")))); 
            }
            
        	if (incidentSearchRequest.getAgencyIds() != null && incidentSearchRequest.getAgencyIds().size() > 0) {
        		predicates.add(criteriaBuilder.and(root.get("agency").get("agencyId").in(incidentSearchRequest.getAgencyIds())));
        	}
        	
        	if (incidentSearchRequest.getIncidentIdentifier() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("arrestTransactionNumber"), incidentSearchRequest.getIncidentIdentifier())));
        	}
        	
        	if (incidentSearchRequest.getIncidentDateRangeStartDate() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("arrestDate"), incidentSearchRequest.getIncidentDateRangeStartDate())));
        	}
        	
        	if (incidentSearchRequest.getIncidentDateRangeEndDate() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("arrestDate"), incidentSearchRequest.getIncidentDateRangeEndDate())));
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
         	if (incidentSearchRequest.getSubmissionStartDate() != null ) {
         		predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(
         				criteriaBuilder.function("concat", String.class, root.get("yearOfTape"), criteriaBuilder.literal("-"), root.get("monthOfTape"),  criteriaBuilder.literal("-01")), 
         				incidentSearchRequest.getSubmissionStartDate().toString())));
         	}
         	if (incidentSearchRequest.getSubmissionEndDate() != null ) {
         		predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(
         				criteriaBuilder.function("concat", String.class, root.get("yearOfTape"), criteriaBuilder.literal("-"), root.get("monthOfTape"),  criteriaBuilder.literal("-01")), 
         				incidentSearchRequest.getSubmissionEndDate().toString())));
         	}
        }
		return predicates;
	}

	@Override
	public int updateSubmissionId(Integer arrestReportSegmentId, Integer submissionId) {
	      Query query = entityManager.createQuery("UPDATE ArrestReportSegment a SET a.submission.submissionId = :submissionId "
	              + "WHERE a.arrestReportSegmentId = :arrestReportSegmentId");
	      query.setParameter("arrestReportSegmentId", arrestReportSegmentId);
	      query.setParameter("submissionId", submissionId);
	      int rowsUpdated = query.executeUpdate();
	      return rowsUpdated;
	}

}
