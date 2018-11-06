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

import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface ArrestReportSegmentRepository extends JpaRepository<ArrestReportSegment, Integer>{
	long deleteByArrestTransactionNumber(String arrestTransactionNumber);
	
	@EntityGraph(value="allArrestReportSegmentJoins", type=EntityGraphType.LOAD)
	ArrestReportSegment findByArrestTransactionNumber(String arrestTransactionNumber);
	
	boolean existsByArrestTransactionNumber(String arrestTransactionNumber);
	
	@EntityGraph(value="allArrestReportSegmentJoins", type=EntityGraphType.LOAD)
	ArrestReportSegment findByArrestReportSegmentId(Integer arrestReportSegmentId);

	@Query("SELECT DISTINCT a.arrestReportSegmentId from ArrestReportSegment a "
			+ "WHERE (?1 = null OR a.ori = ?1) AND "
			+ "		(year(a.arrestDate) = ?2 AND ( ?3 = 0 OR month(a.arrestDate) = ?3)) ")
	List<Integer> findIdsByOriAndArrestDate(String ori, Integer year, Integer month);

	@Query("SELECT DISTINCT a.arrestReportSegmentId from ArrestReportSegment a "
			+ "WHERE (?1 = null OR a.ori = ?1) AND "
			+ "		(?2 = null OR a.arrestDate >= ?2) AND "
			+ "		(?3 = null OR a.arrestDate <= ?3) ")
	List<Integer> findIdsByOriListAndArrestDateRange(List<String> ori, Date startDate, 
			Date endDate);
	
	@Query("SELECT count(DISTINCT a.arrestReportSegmentId) from ArrestReportSegment a "
			+ "WHERE (?1 = null OR a.ori = ?1) AND "
			+ "		(?2 = null OR a.arrestDate >= ?2) AND "
			+ "		(?3 = null OR a.arrestDate <= ?3) ")
	long countByOriListAndArrestDateRange(List<String> oris, Date startDate, 
			Date endDate);
	
	@EntityGraph(value="allArrestReportSegmentJoins", type=EntityGraphType.LOAD)
	List<ArrestReportSegment> findAll(Iterable<Integer> ids);
}
