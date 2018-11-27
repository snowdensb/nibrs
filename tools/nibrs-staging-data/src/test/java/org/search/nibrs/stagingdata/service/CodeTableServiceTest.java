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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeTableServiceTest {

	@Autowired
	public CodeTableService codeTableService; 
	@Autowired
	public SegmentActionTypeRepository segmentActionTypeRepository; 
	
	@Test
	public void getCodeTableTypeTest() {
		SegmentActionTypeType segmentActionTypeType = 
				codeTableService.getCodeTableType("I", segmentActionTypeRepository::findFirstByStateCode, SegmentActionTypeType::new);
		assertThat(segmentActionTypeType.getSegmentActionTypeTypeId(), equalTo(3));
		
		SegmentActionTypeType segmentActionTypeTypeBlank = 
				codeTableService.getCodeTableType(null, segmentActionTypeRepository::findFirstByStateCode, SegmentActionTypeType::new);
		assertThat(segmentActionTypeTypeBlank.getSegmentActionTypeTypeId(), equalTo(99998));
		
		Map<String, SegmentActionTypeType> map = new HashMap<>();
		segmentActionTypeRepository.findAll().forEach((i) -> map.put(i.getStateCode(), i) );
		
		assertThat(map.size(), equalTo(6));
	}


}
