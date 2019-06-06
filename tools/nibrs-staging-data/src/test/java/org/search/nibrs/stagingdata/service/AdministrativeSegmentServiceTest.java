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
package org.search.nibrs.stagingdata.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.common.ParsedObject;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.stagingdata.model.search.IncidentSearchRequest;
import org.search.nibrs.stagingdata.model.search.IncidentSearchResult;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.util.BaselineIncidentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdministrativeSegmentServiceTest {

	@Autowired
	public AdministrativeSegmentService administrativeSegmentService; 
	@Autowired
	public GroupAIncidentService groupAIncidentService; 
	
	@Test
	@DirtiesContext
	public void testFindByOriAndIncidentDate() {
		GroupAIncidentReport groupAIncidentReport = BaselineIncidentFactory.getBaselineIncident();
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		groupAIncidentReport.setIncidentNumber("12345678");
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentService
				.findByOriAndIncidentDate("WA1234567", 2016, 5);
		assertThat(administrativeSegments.size(), equalTo(2));
		
		List<String> incidentNumbers = administrativeSegments.stream()
				.map(AdministrativeSegment::getIncidentNumber)
				.collect(Collectors.toList()); 
		assertTrue(incidentNumbers.containsAll(Arrays.asList("12345678", "54236732")));
		
		
		administrativeSegments = administrativeSegmentService
				.findByOriAndIncidentDate("WA1234567", 2017, 5);
		
		
		assertThat(administrativeSegments.size(), equalTo(0));
		
		administrativeSegments = administrativeSegmentService
				.findByOriAndIncidentDate("WA1234568", 2017, 5);
		
		assertThat(administrativeSegments.size(), equalTo(0));

	}

	@Test
	@DirtiesContext
	public void testFindByOriAndClearanceDate() {
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
		
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentService
				.findByOriAndClearanceDate("WA1234567", 2016, 5);
		assertThat(administrativeSegments.size(), equalTo(2));
		
		List<String> incidentNumbers = administrativeSegments.stream()
				.map(AdministrativeSegment::getIncidentNumber)
				.collect(Collectors.toList()); 
		assertTrue(incidentNumbers.containsAll(Arrays.asList("12345678", "54236732")));
		
		administrativeSegments = administrativeSegmentService
				.findByOriAndClearanceDate("WA1234567", 2015, 5);
		assertThat(administrativeSegments.size(), equalTo(3));
		
		incidentNumbers = administrativeSegments.stream()
				.map(AdministrativeSegment::getIncidentNumber)
				.collect(Collectors.toList()); 
		assertTrue(incidentNumbers.containsAll(Arrays.asList("12345678", "54236732", "12345679")));
		
		administrativeSegments = administrativeSegmentService
				.findByOriAndIncidentDate("WA1234567", 2017, 5);
		
		
		assertThat(administrativeSegments.size(), equalTo(0));
		
		administrativeSegments = administrativeSegmentService
				.findByOriAndIncidentDate("WA1234568", 2017, 5);
		
		assertThat(administrativeSegments.size(), equalTo(0));
		
	}
	@Test
	@DirtiesContext
	public void testFindByCriteria() {
		GroupAIncidentReport groupAIncidentReport = BaselineIncidentFactory.getBaselineIncident();
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		groupAIncidentReport.setIncidentNumber("12345678");
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		groupAIncidentReport.setIncidentNumber("12345679");
		groupAIncidentReport.setExceptionalClearanceDate(new ParsedObject<>(LocalDate.of(2016, 6, 12)));
		groupAIncidentReport.setMonthOfTape(6);
		groupAIncidentReport.setYearOfTape(2017);
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		List<AdministrativeSegment> administrativeSegments = administrativeSegmentService
				.findByOriAndClearanceDate("WA1234567", 2016, 5);
		assertThat(administrativeSegments.size(), equalTo(2));
		
		IncidentSearchRequest incidentSearchRequest = new IncidentSearchRequest();
		long count = administrativeSegmentService.countByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(3L));
		
		incidentSearchRequest.setIncidentIdentifier("12345678");
		count = administrativeSegmentService.countByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(1L));
		
		incidentSearchRequest.setIncidentDate(LocalDate.of(2016, 5, 12));
		count = administrativeSegmentService.countByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(1L));
		
		incidentSearchRequest.setIncidentIdentifier(null);
		count = administrativeSegmentService.countByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(3L));
		
		incidentSearchRequest.setSubmissionMonth(6);
		count = administrativeSegmentService.countByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(1L));
		
		incidentSearchRequest.setSubmissionYear(2016);
		count = administrativeSegmentService.countByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(0L));
		
		incidentSearchRequest.setSubmissionYear(2017);
		administrativeSegments = 
				administrativeSegmentService.findByCriteria(incidentSearchRequest);
		assertThat(administrativeSegments.size(), equalTo(1));
		
		AdministrativeSegment administrativeSegment = administrativeSegments.get(0);
		assertThat(administrativeSegment.getIncidentNumber(), equalTo("12345679"));
		
		incidentSearchRequest.setSubmissionYear(null);
		incidentSearchRequest.setSubmissionMonth(null);
		
		incidentSearchRequest.setUcrOffenseCodeTypeId(131);
		count = administrativeSegmentService.countByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(3L));
	}
	
	@Test
	@DirtiesContext
	public void testFindAllByCriteria() {
		GroupAIncidentReport groupAIncidentReport = BaselineIncidentFactory.getBaselineIncident();
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		groupAIncidentReport.setIncidentNumber("12345678");
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		groupAIncidentReport.setIncidentDate(new ParsedObject<>(LocalDate.of(2016, 6, 12)));
		groupAIncidentReport.setMonthOfTape(6);
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		groupAIncidentReport.setIncidentNumber("12345679");
		groupAIncidentReport.setExceptionalClearanceDate(new ParsedObject<>(LocalDate.of(2016, 6, 12)));
		groupAIncidentReport.setMonthOfTape(6);
		groupAIncidentReport.setYearOfTape(2017);
		OffenseSegment offenseSegment = new OffenseSegment(groupAIncidentReport.getOffenses().get(0));
		offenseSegment.setUcrOffenseCode("09A");
		groupAIncidentReport.addOffense(offenseSegment);
		groupAIncidentService.saveGroupAIncidentReports(groupAIncidentReport);
		
		IncidentSearchRequest incidentSearchRequest = new IncidentSearchRequest();
		List<IncidentSearchResult> incidentSearchResults = administrativeSegmentService
				.findAllByCriteria(incidentSearchRequest);
		assertThat(incidentSearchResults.size(), equalTo(3));
		
		long count = administrativeSegmentService.countAllByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(3L));
		
		incidentSearchRequest.setIncidentIdentifier("12345678");
		count = administrativeSegmentService.countAllByCriteria(incidentSearchRequest);
		incidentSearchResults = 
				administrativeSegmentService.findAllByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(1L));
		assertThat(incidentSearchResults.get(0).getIncidentDate(), equalTo(LocalDate.of(2016, 6, 12)));
		assertThat(incidentSearchResults.get(0).getSubmissionMonth(), equalTo("06"));
		
		incidentSearchRequest.setIncidentDate(LocalDate.of(2016, 5, 12));
		count = administrativeSegmentService.countAllByCriteria(incidentSearchRequest);
		incidentSearchResults = 
				administrativeSegmentService.findAllByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(1L));
		
		incidentSearchRequest.setIncidentIdentifier(null);
		count = administrativeSegmentService.countAllByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(2L));
		
		incidentSearchRequest = new IncidentSearchRequest();
		incidentSearchRequest.setSubmissionMonth(6);
		count = administrativeSegmentService.countAllByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(2L));
		
		incidentSearchRequest.setSubmissionYear(2016);
		count = administrativeSegmentService.countAllByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(1L));
		
		incidentSearchRequest.setSubmissionYear(2017);
		incidentSearchResults = 
				administrativeSegmentService.findAllByCriteria(incidentSearchRequest);
		assertThat(incidentSearchResults.size(), equalTo(1));
		assertThat(incidentSearchResults.get(0).getUcrOffenseCodeTypeId(), equalTo(91));
		assertThat(incidentSearchResults.get(0).getOffenseCode(), equalTo("09A"));
		
		IncidentSearchResult result = incidentSearchResults.get(0);
		assertThat(result.getIncidentIdentifier(), equalTo("12345679"));
		
		incidentSearchRequest.setSubmissionYear(null);
		incidentSearchRequest.setSubmissionMonth(null);
		
		incidentSearchRequest.setUcrOffenseCodeTypeId(131);
		count = administrativeSegmentService.countAllByCriteria(incidentSearchRequest);
		assertThat(count, equalTo(3L));
	}
	
}
