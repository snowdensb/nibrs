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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.joda.time.LocalDateTime;
import org.search.nibrs.stagingdata.AppProperties;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.NibrsErrorCodeType;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.NibrsErrorCodeTypeRepository;
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
	private NibrsErrorCodeTypeRepository nibrsErrorCodeTypeRepository;
	@Autowired
	public AppProperties appProperties;
	
	private final List<String> unknownOrBlank = Arrays.asList("UNKOWN", "BLANK");

	@GetMapping("/agencies")
	public Map<Integer, String> agencies(){
		Map<Integer, String> agencyMap = StreamSupport.stream(agencyRepository.findAll(new Sort(Sort.Direction.ASC, "agencyName")).spliterator(), false)
				.filter(agency-> !unknownOrBlank.contains(agency.getAgencyName().toUpperCase()))
				.collect(Collectors.toMap(Agency::getAgencyId, Agency::getAgencyName, (u, v) -> u,
					      LinkedHashMap::new));
		return agencyMap;
	}
	
	@GetMapping("/offenseCodes")
	public Map<Integer, String> offenseCodes(){
		Map<Integer, String> offenseCodeMap = StreamSupport.stream(ucrOffenseCodeTypeRepository.findAll(new Sort(Sort.Direction.ASC,"stateDescription")).spliterator(), false)
				.filter(offenseCode-> !unknownOrBlank.contains(offenseCode.getNibrsDescription().toUpperCase()))
				.collect(Collectors.toMap(UcrOffenseCodeType::getUcrOffenseCodeTypeId, UcrOffenseCodeType::getStateDescription, (u, v) -> u,
					      LinkedHashMap::new));
		return offenseCodeMap;
	}
	
	@GetMapping("/nibrsErrorCodes")
	public Map<Integer, String> nibrsErrorCodes(){
		Map<Integer, String> nibrsErrorCodeMap = StreamSupport.stream(nibrsErrorCodeTypeRepository.findAll(new Sort(Sort.Direction.ASC,"code")).spliterator(), false)
				.filter(nibrsErrorCode-> !unknownOrBlank.contains(nibrsErrorCode.getMessage().toUpperCase()))
				.collect(Collectors.toMap(NibrsErrorCodeType::getNibrsErrorCodeTypeId, NibrsErrorCodeType::getCode, (u, v) -> u,
						LinkedHashMap::new));
		return nibrsErrorCodeMap;
	}
	
	@GetMapping(value="/localDateTime")
	public String getLocalDateTime(){
		return LocalDateTime.now().toString();
	}
	
}
