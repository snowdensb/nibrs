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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;
import org.search.nibrs.stagingdata.repository.segment.AdministrativeSegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to process Group B Arrest Report.  
 *
 */
@Service
public class AdministrativeSegmentService {
	private static final Log log = LogFactory.getLog(AdministrativeSegmentService.class);
	
	@Autowired
	AdministrativeSegmentRepository administrativeSegmentRepository;
	
	public AdministrativeSegment find(Integer id){
		return administrativeSegmentRepository.findByAdministrativeSegmentId(id);
	}
	
	public List<AdministrativeSegment> findAllAdministrativeSegments(){
		List<AdministrativeSegment> administrativeSegments = new ArrayList<>();
		administrativeSegmentRepository.findAll().forEach(administrativeSegments::add);
		return administrativeSegments;
	}
	
	public List<AdministrativeSegment> findByOriAndClearanceDate(String ori, Integer year, Integer month){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndClearanceDate(ori, year, month);
			
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		return administrativeSegments; 
	}

	public List<AdministrativeSegment> findByOriAndIncidentDate(String ori, Integer year, Integer month){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndIncidentDate(ori, year, month);
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<AdministrativeSegment> findArsonIncidentByOriAndIncidentDate(String ori, Integer year, Integer month){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findArsonIdsByOriAndIncidentDate(ori, year, month);
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<AdministrativeSegment> findHumanTraffickingIncidentByOriAndIncidentDate(String ori, Integer year, Integer month){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndIncidentDateAndOffenses(ori, year, month, Arrays.asList("64A", "64B"));
//		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndIncidentDateAndOffenses(ori, year, month, Arrays.asList("64A", "64B"));
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<AdministrativeSegment> findArsonIncidentByOriAndAClearanceDate(String ori, Integer year, Integer month){
		
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findArsonIdsByOriAndClearanceDate(ori, year, month);
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		return administrativeSegments; 
	}
	
	public List<ArresteeSegment> findArresteeSegmentByOriAndArrestDate(String ori, Integer arrestYear, Integer arrestMonth){
		if ("StateWide".equalsIgnoreCase(ori)){
			ori = null;
		}
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndArrestDate(ori, arrestYear, arrestMonth);
		
		List<AdministrativeSegment> administrativeSegments = 
				administrativeSegmentRepository.findAllById(ids)
				.stream().distinct().collect(Collectors.toList());
		
		log.info("ids size" + ids.size());
		log.info("administrativeSegments size" + administrativeSegments.size());
		
		List<ArresteeSegment> arresteeSegments = administrativeSegments.stream()
				.flatMap(i->i.getArresteeSegments().stream())
				.filter(i->Objects.equals(i.getArrestDateType().getYearNum(), arrestYear) &&  Objects.equals(i.getArrestDateType().getMonthNum(),arrestMonth))
				.collect(Collectors.toList());
		return arresteeSegments; 
		
	}
	
}
