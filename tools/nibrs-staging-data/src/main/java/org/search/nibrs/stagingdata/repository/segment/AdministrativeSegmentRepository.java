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
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface AdministrativeSegmentRepository 
	extends JpaRepository<AdministrativeSegment, Integer>, JpaSpecificationExecutor<AdministrativeSegment> {
	
	long deleteByIncidentNumber(String incidentNumber);
	
	@EntityGraph(value="allAdministrativeSegmentJoins", type=EntityGraphType.LOAD)
	List<AdministrativeSegment> findByIncidentNumber(String incidentNumber);
	
	@EntityGraph(value="allAdministrativeSegmentJoins", type=EntityGraphType.LOAD)
	AdministrativeSegment findByAdministrativeSegmentId(Integer administrativeSegmentId);
	
	@Query("SELECT count(*) > 0 from AdministrativeSegment a "
			+ "LEFT JOIN a.segmentActionType s "
			+ "WHERE a.administrativeSegmentId = "
			+ "			(SELECT max(administrativeSegmentId) FROM AdministrativeSegment where incidentNumber = ?1 ) "
			+ "		AND s.nibrsCode != 'D' ")
	boolean existsByIncidentNumber(String incidentNumber);
	
	@EntityGraph(value="allAdministrativeSegmentJoins", type=EntityGraphType.LOAD)
	List<AdministrativeSegment> findDistinctByOriAndIncidentDateTypeYearNumAndIncidentDateTypeMonthNum(String ori, Integer year,  Integer month);
		
	@EntityGraph(value="allAdministrativeSegmentJoins", type=EntityGraphType.LOAD)
	List<AdministrativeSegment> findDistinctByIncidentDateTypeYearNumAndIncidentDateTypeMonthNum(Integer year,  Integer month);
	
	@EntityGraph(value="allAdministrativeSegmentJoins", type=EntityGraphType.LOAD)
	List<AdministrativeSegment> findAllById(Iterable<Integer> ids);
	
	@Query("SELECT max(a.administrativeSegmentId) from AdministrativeSegment a "
			+ "LEFT JOIN a.exceptionalClearanceDateType ae "
			+ "LEFT JOIN a.arresteeSegments aa "
			+ "WHERE (?1 = null OR a.ori = ?1) AND (aa.arrestDate = (select min (arrestDate) from a.arresteeSegments )) AND "
			+ "		((year(a.exceptionalClearanceDate) = ?2 AND ( ?3 = 0 OR month(a.exceptionalClearanceDate) = ?3)) "
			+ "			OR ( year(aa.arrestDate) = ?2 AND ( ?3 = 0 OR month(aa.arrestDate) = ?3 ))) "
			+ "GROUP BY a.incidentNumber ")
	List<Integer> findIdsByOriAndClearanceDate(String ori, Integer year, Integer month);
	
	@Query("SELECT DISTINCT a.administrativeSegmentId from AdministrativeSegment a "
			+ "LEFT JOIN a.exceptionalClearanceDateType ae "
			+ "LEFT JOIN a.arresteeSegments aa "
			+ "LEFT JOIN aa.arrestDateType aaa "
			+ "WHERE (?1 = null OR a.ori in (?1)) AND "
			+ "		(?2 = null OR cast(concat(a.yearOfTape, '-', a.monthOfTape, '-01') as date) >= ?2 ) AND "
			+ "		(?3 = null OR cast(concat(a.yearOfTape, '-', a.monthOfTape, '-01') as date) <= ?3) "
			+ "ORDER BY a.administrativeSegmentId asc ")
	List<Integer> findIdsByOriListAndSubmissionDateRange(List<String> oris, Date startDate, Date endDate);
	
	@Query("SELECT max(a.administrativeSegmentId) from AdministrativeSegment a "
			+ "LEFT JOIN a.arresteeSegments aa "
			+ "WHERE (?1 = null OR a.ori = ?1 ) AND "
			+ "		( year(aa.arrestDate) = ?2 AND ( ?3=0 OR month(aa.arrestDate) = ?3) ) "
			+ "GROUP BY a.incidentNumber ")
	List<Integer> findIdsByOriAndArrestDate(String ori, Integer year, Integer month);
	
	@Query("SELECT count(DISTINCT a.administrativeSegmentId) from AdministrativeSegment a "
			+ "LEFT JOIN a.exceptionalClearanceDateType ae "
			+ "LEFT JOIN a.arresteeSegments aa "
			+ "LEFT JOIN aa.arrestDateType aaa "
			+ "WHERE (?1 = null OR a.ori in (?1)) AND "
			+ "		(?2 = null OR cast(concat(a.yearOfTape, '-', a.monthOfTape, '-01') as date) >= ?2 ) AND "
			+ "		(?3 = null OR cast(concat(a.yearOfTape, '-', a.monthOfTape, '-01') as date) <= ?3) ")
	long countByOriListAndSubmissionDateRange(List<String> oris, Date startDate, Date endDate);
	
	@Query("SELECT max(a.administrativeSegmentId) from AdministrativeSegment a "
			+ "WHERE (?1 = null OR a.ori = ?1) AND "
			+ "		(year(a.incidentDate) = ?2 AND ( ?3 = 0 OR month(a.incidentDate) = ?3)) "
			+ "GROUP BY a.incidentNumber ")
	List<Integer> findIdsByOriAndIncidentDate(String ori, Integer year, Integer month);
	
	@Query("SELECT max(a.administrativeSegmentId) from AdministrativeSegment a "
			+ "LEFT JOIN a.offenseSegments ao "
			+ "WHERE ao.ucrOffenseCodeType.nibrsCode = '200' AND"
			+ "		(?1 = null OR a.ori = ?1) AND "
			+ "		(year(a.incidentDate) = ?2 AND "
			+ "		( ?3 = 0 OR month(a.incidentDate) = ?3)) "
			+ "GROUP BY a.incidentNumber ")
	List<Integer> findArsonIdsByOriAndIncidentDate(String ori, Integer year, Integer month);
	
	@Query("SELECT max(a.administrativeSegmentId) from AdministrativeSegment a "
			+ "LEFT JOIN a.offenseSegments ao "
			+ "LEFT JOIN a.arresteeSegments aa "
			+ "WHERE ao.ucrOffenseCodeType.nibrsCode = '200' AND (aa.arrestDate = (select min (arrestDate) from a.arresteeSegments )) AND "
			+ "		(?1 = null OR a.ori = ?1) AND "
			+ "		((year(a.exceptionalClearanceDate) = ?2 AND ( ?3 = 0 OR month(a.exceptionalClearanceDate) = ?3)) "
			+ "			OR ( year(aa.arrestDate) = ?2 AND ( ?3 = 0 OR month(aa.arrestDate) = ?3 ))) "
			+ "GROUP BY a.incidentNumber ")
	List<Integer> findArsonIdsByOriAndClearanceDate(String ori, Integer year, Integer month);
	
}
