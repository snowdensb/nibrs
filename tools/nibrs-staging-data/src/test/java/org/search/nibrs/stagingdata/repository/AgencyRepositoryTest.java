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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.AgencyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AgencyRepositoryTest {
	private static final Log log = LogFactory.getLog(AgencyRepositoryTest.class);

	@Autowired
	public AgencyRepository agencyRepository; 

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Map<Integer, String> agencyPairs = StreamSupport.stream(agencyRepository.findAll().spliterator(), false)
				.collect(Collectors.toMap(Agency::getAgencyId, Agency::getAgencyName));
		System.out.println(agencyPairs);
	}
				
	@Test
	public void testSaveAll() {
		List<Agency> agencies = new ArrayList<>();
		Agency agency1 = new Agency(); 
		agency1.setAgencyName("Agency 1");
		agency1.setAgencyOri("ori1");
		
		AgencyType agencyType = new AgencyType(1);
		agency1.setAgencyType(agencyType);
		agency1.setPopulation(345);
		agency1.setStateCode("S1");
		agency1.setStateName("State 1");
		agencies.add(agency1);
		
		Agency agency2 = new Agency(); 
		agency2.setAgencyName("Agency 2");
		agency2.setAgencyOri("ori2");
		
		agency2.setAgencyType(agencyType);
		agency2.setPopulation(500);
		agency2.setStateCode("S2");
		agency2.setStateName("State 2");
		agencies.add(agency2); 
		
		List<Agency> agenciesSaved = agencyRepository.saveAll(agencies);
		log.info("agenciesSaved:" + agenciesSaved); 
	}
	
				
}
