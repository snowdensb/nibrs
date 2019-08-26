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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.stagingdata.model.NibrsError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NibrsErrroRepositoryTest {
	
	@Autowired
	public NibrsErrorRepository nibrsErrorRepository; 
	@Autowired
	public AgencyRepository agencyRepository; 
	@Autowired
	public SegmentActionTypeRepository segmentActionTypeRepository; 
	@Autowired
	public NibrsErrorCodeTypeRepository nibrsErrorCodeTypeRepository; 
	@Autowired

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		List<NibrsError> nibrsErrorBefore = nibrsErrorRepository.findAll(); 
		assertThat(nibrsErrorBefore.size(), is(0));
		
		NibrsError nibrsError1 = new NibrsError(); 
		nibrsError1.setMonthOfTape("01");
		nibrsError1.setYearOfTape("2016");
		nibrsError1.setIncidentIdentifier("15019757");
		nibrsError1.setSourceLocation("29");
		nibrsError1.setAgency(agencyRepository.getOne(1));
		nibrsError1.setSegmentActionType(segmentActionTypeRepository.findFirstByStateCode("I"));
		nibrsError1.setDataElement("34");		
		nibrsError1.setNibrsErrorCodeType(nibrsErrorCodeTypeRepository.findFirstByCode("085"));	
		nibrsError1.setNibrsErrorTimestamp(LocalDateTime.now());
		
		nibrsErrorRepository.save(nibrsError1);
		List<NibrsError> nibrsErrorAfterInsert1 = nibrsErrorRepository.findAll(); 
		
		assertThat(nibrsErrorAfterInsert1.size(), is(1)); 
		
		NibrsError nibrsError1Saved = nibrsErrorRepository.findByNibrsErrorId(nibrsError1.getNibrsErrorId());
		
		assertThat(nibrsError1Saved.getAgency().getAgencyOri(), equalTo("agencyORI"));
		assertThat(nibrsError1Saved.getSegmentActionType().getNibrsDescription(), equalTo("Incident Report"));
		assertThat(nibrsError1Saved.getNibrsErrorCodeType().getCode(), equalTo("085"));

		NibrsError nibrsError2 = new NibrsError(); 
		nibrsError2.setMonthOfTape("01");
		nibrsError2.setYearOfTape("2016");
		nibrsError2.setIncidentIdentifier("15037043");
		nibrsError2.setSourceLocation("101");
		nibrsError2.setAgency(agencyRepository.getOne(1));
		nibrsError2.setSegmentActionType(segmentActionTypeRepository.findFirstByStateCode("I"));
		nibrsError2.setDataElement("24");		
		nibrsError2.setNibrsErrorCodeType(nibrsErrorCodeTypeRepository.findFirstByCode("466"));	
		nibrsError2.setRejectedValue("220");;	
		nibrsError2.setNibrsErrorTimestamp(LocalDateTime.now());
		
		nibrsErrorRepository.save(nibrsError2);
		List<NibrsError> nibrsErrorAfterInsert2 = nibrsErrorRepository.findAll(); 
		
		assertThat(nibrsErrorAfterInsert2.size(), is(2)); 
		
		NibrsError nibrsError2Saved = nibrsErrorRepository.findByNibrsErrorId(nibrsError2.getNibrsErrorId());
		assertThat(nibrsError2Saved.getRejectedValue(), equalTo("220"));
		assertThat(nibrsError2Saved.getNibrsErrorCodeType().getMessage(), equalTo("OFFENSE MUST BE SUBMITTED AS LEVEL 2 RECORD IF VICTIM IS CONNECTED"));

	}

}
