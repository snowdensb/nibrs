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
package org.search.nibrs.stagingdata.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.joda.time.LocalDateTime;
import org.search.nibrs.stagingdata.AppProperties;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.UcrOffenseCodeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codeTables")
public class CodeTableController {
	@Autowired
	private AgencyRepository agencyRepository;
	@Autowired
	private UcrOffenseCodeTypeRepository ucrOffenseCodeTypeRepository;
	@Autowired
	public AppProperties appProperties;

	@GetMapping("/agencies")
	public Map<Integer, String> agencies(){
		Map<Integer, String> agencyMap = StreamSupport.stream(agencyRepository.findAll(new Sort(Sort.Direction.ASC, "agencyName")).spliterator(), false)
				.collect(Collectors.toMap(Agency::getAgencyId, Agency::getAgencyName, (u, v) -> u,
					      LinkedHashMap::new));
		return agencyMap;
	}
	
	@GetMapping("/offenseCodes")
	public Map<Integer, String> offenseCodes(){
		Map<Integer, String> agencyMap = StreamSupport.stream(ucrOffenseCodeTypeRepository.findAll(new Sort(Sort.Direction.ASC,"stateDescription")).spliterator(), false)
				.collect(Collectors.toMap(UcrOffenseCodeType::getUcrOffenseCodeTypeId, UcrOffenseCodeType::getStateDescription, (u, v) -> u,
					      LinkedHashMap::new));
		return agencyMap;
	}
	
	@GetMapping(value="/localDateTime")
	public String getLocalDateTime(){
		return LocalDateTime.now().toString();
	}
	
}
