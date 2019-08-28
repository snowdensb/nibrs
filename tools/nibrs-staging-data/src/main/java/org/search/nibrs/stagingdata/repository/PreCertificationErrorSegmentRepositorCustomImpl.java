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

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
        
        query.select(root).where(criteriaBuilder.and(
        		queryPredicates.toArray( new Predicate[queryPredicates.size()])))
        	 .orderBy(criteriaBuilder.desc(root.get("preCertificationErrorTimestamp")));
        
        EntityGraph<PreCertificationError> fetchGraph = entityManager.createEntityGraph(PreCertificationError.class);
//        fetchGraph.addAttributeNodes("segmentActionType", "agency", "nibrsErrorCodeType");
        fetchGraph.addSubgraph("segmentActionType");
        fetchGraph.addSubgraph("agency");
        fetchGraph.addSubgraph("nibrsErrorCodeType");
		return entityManager.createQuery(query).setHint("javax.persistence.loadgraph", fetchGraph)
	            .getResultList();
	}

	private List<Predicate> getPreCertificationErrorPredicates(PrecertErrorSearchRequest precertErrorSearchRequest,
			Root<PreCertificationError> root, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		return predicates;
	}

	@Override
	public long countAllByCriteria(PrecertErrorSearchRequest precertErrorSearchRequest) {
		// TODO Auto-generated method stub
		return 0;
	}

}
