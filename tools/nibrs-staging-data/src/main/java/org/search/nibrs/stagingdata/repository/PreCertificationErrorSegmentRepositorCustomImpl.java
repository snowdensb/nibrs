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
package org.search.nibrs.stagingdata.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.search.nibrs.stagingdata.model.PreCertificationError;
import org.search.nibrs.stagingdata.model.search.PrecertErrorSearchRequest;
import org.springframework.stereotype.Repository;

@Repository
public class PreCertificationErrorSegmentRepositorCustomImpl implements PreCertificationErrorRepositoryCustom{
	@PersistenceContext
    private EntityManager entityManager;

	@Override
	public List<PreCertificationError> findAllByCriteria(PrecertErrorSearchRequest precertErrorSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PreCertificationError> query = criteriaBuilder.createQuery(PreCertificationError.class);
        Root<PreCertificationError> root = query.from(PreCertificationError.class);
        
        List<Predicate> queryPredicates = getPreCertificationErrorPredicates(precertErrorSearchRequest, root, criteriaBuilder);
        
        
 		query.multiselect(root.get("preCertificationErrorId"),
 				root.get("nibrsErrorCodeType").get("code"), 
 				root.get("nibrsErrorCodeType").get("message"), 
 				root.get("ori"), root.get("segmentActionType").get("stateCode"), 
 				root.get("monthOfTape"), root.get("yearOfTape"),
 				root.get("sourceLocation"), root.get("incidentIdentifier"), 
 				root.get("dataElement"), root.get("rejectedValue"),
 				root.get("preCertificationErrorTimestamp"));
        query.where(criteriaBuilder.and(
        		queryPredicates.toArray( new Predicate[queryPredicates.size()])))
        	 .orderBy(criteriaBuilder.desc(root.get("preCertificationErrorTimestamp")));
        
		return entityManager.createQuery(query)
	            .getResultList();
	}

	private List<Predicate> getPreCertificationErrorPredicates(PrecertErrorSearchRequest precertErrorSearchRequest,
			Root<PreCertificationError> root, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		
		if (!precertErrorSearchRequest.isEmpty()) {
        	if (precertErrorSearchRequest.getOwnerId() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("owner").get("ownerId"), precertErrorSearchRequest.getOwnerId())));
        	}
        	
			if (StringUtils.isNotBlank(precertErrorSearchRequest.getIncidentIdentifier())) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("incidentIdentifier"), precertErrorSearchRequest.getIncidentIdentifier())));
			}
        	if (precertErrorSearchRequest.getAgencyIds() != null && precertErrorSearchRequest.getAgencyIds().size() > 0) {
        		predicates.add(criteriaBuilder.and(root.get("agency").get("agencyId").in(precertErrorSearchRequest.getAgencyIds())));
        	}
			if (precertErrorSearchRequest.getNibrsErrorCodeTypeId() != null && precertErrorSearchRequest.getNibrsErrorCodeTypeId() > 0) {
				predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("nibrsErrorCodeType").get("nibrsErrorCodeTypeId"),precertErrorSearchRequest.getNibrsErrorCodeTypeId())));
			}
        	if (precertErrorSearchRequest.getSubmissionMonth() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("monthOfTape"), precertErrorSearchRequest.getSubmissionMonth())));
        	}
        	
         	if (precertErrorSearchRequest.getSubmissionYear() != null) {
        		predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("yearOfTape"), precertErrorSearchRequest.getSubmissionYear())));
        	}
		}
		
		return predicates;
	}

	@Override
	public long countAllByCriteria(PrecertErrorSearchRequest precertErrorSearchRequest) {
		// TODO Auto-generated method stub
		return 0;
	}

}
