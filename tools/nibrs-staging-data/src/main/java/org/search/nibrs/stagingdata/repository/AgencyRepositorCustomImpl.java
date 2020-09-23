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

import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AgencyRepositorCustomImpl implements AgencyRepositoryCustom{
	private static final Log log = LogFactory.getLog(AgencyRepositorCustomImpl.class);
	@PersistenceContext
    private EntityManager entityManager;

	@Override
	public Map<String, String> findAllStatesByOwnerId(Integer ownerId) {
		
		Map<String, String> stateCodeMapping = entityManager
				.createQuery(
				    "select " +
				    "   distinct (stateCode) as stateCode , " +
				    "   stateName as stateName " +
				    "from Agency a " +
				    "where exists (select adminSegment from AdministrativeSegment adminSegment " + 
				    "				where adminSegment.agency.agencyId = a.agencyId " + 
				    "				AND (?1 = null OR adminSegment.owner.ownerId = ?1 )) " + 
				    "	OR exists (select arrestReportSegment from ArrestReportSegment arrestReportSegment " + 
				    "				where arrestReportSegment.agency.agencyId = a.agencyId " + 
				    "				AND (?1 = null OR arrestReportSegment.owner.ownerId = ?1)) " + 
				    "order by stateCode ", Tuple.class)
				.setParameter( 1, ownerId)
				.getResultList()
				.stream()
				.collect(
				    Collectors.toMap(
				        tuple -> (String)tuple.get("stateCode"),
				        entry -> (String)entry.get("stateName"), 
			             (stateCode1, stateCode2) -> {
			                 log.warn("duplicate stateCode found!");
			                 return stateCode1;
			             }
				 ));
		
		return stateCodeMapping;
	}

	@Override
	public Map<Integer, String> findAllAgenciesByStateAndOwnerId(Integer ownerId, String stateCode) {
		Map<Integer, String> agencyIdMapping = entityManager
				.createQuery(
				    "select " +
				    "   agencyId as agencyId , " +
				    "   agencyName as agencyName " +
				    "from Agency a " +
				    "where stateCode = ?2 AND (exists (select adminSegment from AdministrativeSegment adminSegment " + 
				    "				where adminSegment.agency.agencyId = a.agencyId " + 
				    "				AND (?1 = null OR adminSegment.owner.ownerId = ?1 )) " + 
				    "	OR exists (select arrestReportSegment from ArrestReportSegment arrestReportSegment " + 
				    "				where arrestReportSegment.agency.agencyId = a.agencyId " + 
				    "				AND (?1 = null OR arrestReportSegment.owner.ownerId = ?1))) " + 
				    "order by agencyName ", Tuple.class)
				.setParameter( 1, ownerId)
				.setParameter( 2, stateCode)
				.getResultList()
				.stream()
				.collect(
				    Collectors.toMap(
				        tuple -> (Integer)tuple.get("agencyId"),
				        entry -> (String)entry.get("agencyName"), 
			             (stateCode1, stateCode2) -> {
			                 log.warn("duplicate stateCode found!");
			                 return stateCode1;
			             }
				 ));
		
		return agencyIdMapping;
	}
	


}
