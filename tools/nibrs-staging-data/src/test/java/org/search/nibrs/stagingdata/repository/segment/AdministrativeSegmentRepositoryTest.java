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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.common.ParsedObject;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.service.GroupAIncidentService;
import org.search.nibrs.stagingdata.util.BaselineIncidentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdministrativeSegmentRepositoryTest {
	private static final Log log = LogFactory.getLog(AdministrativeSegmentRepositoryTest.class);

	@Autowired
	public AdministrativeSegmentRepository administrativeSegmentRepository; 
	@Autowired
	public GroupAIncidentService groupAIncidentService;
	
	@Before
	public void setup() {
		GroupAIncidentReport groupAIncidentReport = BaselineIncidentFactory.getBaselineIncident();
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		groupAIncidentReport.setIncidentNumber("12345678");
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		groupAIncidentReport.setIncidentNumber("12345679");
		groupAIncidentReport.setExceptionalClearanceDate(new ParsedObject<>(LocalDate.of(2016, 6, 12)));
		ArresteeSegment arrestee = new ArresteeSegment(groupAIncidentReport.getArrestees().get(0));
		arrestee.setArrestDate(new ParsedObject<>(LocalDate.of(2016, 5, 12)));
		groupAIncidentReport.addArrestee(arrestee);
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
	}
	
	@Test
	public void testFindDistinctByOriAndIncidentDateTypeYearAndIncidentDateTypeMonth() {
		
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentRepository
				.findDistinctByOriAndIncidentDateTypeYearAndIncidentDateTypeMonth("WA1234567", 2016, 5);
		assertThat(administrativeSegments.size(), equalTo(3));
		List<String> incidentNumbers = administrativeSegments.stream()
				.map(AdministrativeSegment::getIncidentNumber)
				.collect(Collectors.toList()); 
		assertTrue(incidentNumbers.containsAll(Arrays.asList("12345678", "54236732", "12345679")));

	}
		
	@Test
	public void testFindDistinctByOriListAndIncidentDateRange() {

		administrativeSegmentRepository.findAll().forEach(i->log.info(i.getIncidentNumber()));
		List<Integer> administrativeSegmentIds = administrativeSegmentRepository
				.findIdsByOriListAndIncidentDateRange(Arrays.asList("WA1234567"),Date.valueOf(LocalDate.of(2016, 5, 1)), Date.valueOf(LocalDate.of(2016, 5, 31)));
		assertThat(administrativeSegmentIds.size(), equalTo(3));
		
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentRepository.findAll(administrativeSegmentIds);
		List<String> incidentNumbers = administrativeSegments.stream()
				.map(AdministrativeSegment::getIncidentNumber)
				.collect(Collectors.toList()); 
		assertTrue(incidentNumbers.containsAll(Arrays.asList("12345678", "54236732", "12345679")));
		
	}
	
	@Test
	public void testFindIdsByOriAndClearanceDateAndFindAll() {
		List<Integer> administrativeSegmentIds = administrativeSegmentRepository
				.findIdsByOriAndClearanceDate("WA1234567", 2016, 5);
		
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentRepository
				.findAll(administrativeSegmentIds).stream().distinct().collect(Collectors.toList());
		
		assertThat(administrativeSegments.size(), equalTo(3));
		List<String> incidentNumbers = administrativeSegments.stream()
				.map(AdministrativeSegment::getIncidentNumber)
				.collect(Collectors.toList()); 
		assertTrue(incidentNumbers.containsAll(Arrays.asList("12345678", "54236732", "12345679")));

	}

}
