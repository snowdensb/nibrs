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

import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface AdministrativeSegmentRepository 
	extends JpaRepository<AdministrativeSegment, Integer>{
	
	long deleteByIncidentNumber(String incidentNumber);
	
	@EntityGraph(value="allAdministrativeSegmentJoins", type=EntityGraphType.LOAD)
	AdministrativeSegment findByIncidentNumber(String incidentNumber);
	
	@EntityGraph(value="allAdministrativeSegmentJoins", type=EntityGraphType.LOAD)
	AdministrativeSegment findByAdministrativeSegmentId(Integer administrativeSegmentId);
	
	boolean existsByIncidentNumber(String incidentNumber);
	
	@EntityGraph(value="allAdministrativeSegmentJoins", type=EntityGraphType.LOAD)
	List<AdministrativeSegment> findDistinctByOriAndIncidentDateTypeYearAndIncidentDateTypeMonth(String ori, Integer year,  Integer month);
		
	@EntityGraph(value="allAdministrativeSegmentJoins", type=EntityGraphType.LOAD)
	List<AdministrativeSegment> findDistinctByIncidentDateTypeYearAndIncidentDateTypeMonth(Integer year,  Integer month);
	
	@EntityGraph(value="allAdministrativeSegmentJoins", type=EntityGraphType.LOAD)
	List<AdministrativeSegment> findAll(Iterable<Integer> ids);
	
	@Query("SELECT DISTINCT a.administrativeSegmentId from AdministrativeSegment a "
			+ "LEFT JOIN a.exceptionalClearanceDateType ae "
			+ "LEFT JOIN a.arresteeSegments aa "
			+ "LEFT JOIN aa.arrestDateType aaa "
			+ "WHERE (?1 = null OR a.ori = ?1) AND "
			+ "		((ae.year = ?2 AND ( ?3 = 0 OR ae.month = ?3)) "
			+ "			OR ( aaa.year = ?2 AND ( ?3 = 0 OR aaa.month = ?3 ))) ")
	List<Integer> findIdsByOriAndClearanceDate(String ori, Integer year, Integer month);
	
	@Query("SELECT DISTINCT a.administrativeSegmentId from AdministrativeSegment a "
			+ "LEFT JOIN a.exceptionalClearanceDateType ae "
			+ "LEFT JOIN a.arresteeSegments aa "
			+ "LEFT JOIN aa.arrestDateType aaa "
			+ "WHERE (?1 = null OR a.ori in (?1)) AND "
			+ "		(?2 = null OR a.incidentDate >= ?2 ) AND "
			+ "		(?3 = null OR a.incidentDate <= ?3) ")
	List<Integer> findIdsByOriListAndIncidentDateRange(List<String> oris, Date startDate, Date endDate);
	
	@Query("SELECT DISTINCT a.administrativeSegmentId from AdministrativeSegment a "
			+ "WHERE (?1 = null OR a.ori = ?1) AND "
			+ "		(year(a.incidentDate) = ?2 AND ( ?3 = 0 OR month(a.incidentDate) = ?3)) ")
	List<Integer> findIdsByOriAndIncidentDate(String ori, Integer year, Integer month);
}
