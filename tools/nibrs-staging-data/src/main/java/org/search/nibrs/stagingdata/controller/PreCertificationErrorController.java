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

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.stagingdata.AppProperties;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.AgencyType;
import org.search.nibrs.stagingdata.model.NibrsErrorCodeType;
import org.search.nibrs.stagingdata.model.Owner;
import org.search.nibrs.stagingdata.model.PreCertificationError;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.search.PrecertErrorSearchRequest;
import org.search.nibrs.stagingdata.model.search.SearchResult;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.NibrsErrorCodeTypeRepository;
import org.search.nibrs.stagingdata.repository.PreCertificationErrorRepository;
import org.search.nibrs.stagingdata.repository.PreCertificationErrorRepositoryCustom;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/preCertificationErrors")
public class PreCertificationErrorController {
	private final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private PreCertificationErrorRepository preCertificationErrorRepository;
	@Autowired
	private PreCertificationErrorRepositoryCustom preCertificationErrorRepositoryCustom;
	@Autowired
	private AgencyRepository agencyRepository;
	@Autowired
	private SegmentActionTypeRepository segmentActionTypeRepository;
	@Autowired
	private NibrsErrorCodeTypeRepository nibrsErrorCodeTypeRepository;
	@Autowired
	public AppProperties appProperties;

	@PostMapping("")
	public Integer savePreCertificationErrors(@RequestBody List<PreCertificationError> preCertificationErrors){
		
		log.info("About to save " + preCertificationErrors.size() + " Pre Certification Errors...");
		List<PreCertificationError> preCertificationErrorsConverted = preCertificationErrors.stream()
				.map(this::convertPreCertificationError)
				.collect(Collectors.toList());
		return preCertificationErrorRepository.saveAll(preCertificationErrorsConverted).size();
	}

	private PreCertificationError convertPreCertificationError(PreCertificationError preCertificationError) {
		Agency agency = agencyRepository.findFirstByAgencyOri(preCertificationError.getOri());
		
		if (agency == null && StringUtils.isNotBlank(preCertificationError.getOri())){
			agency = new Agency(); 
			agency.setAgencyId(null);
			agency.setAgencyOri(preCertificationError.getOri());
			agency.setAgencyName(preCertificationError.getOri());
			agency.setAgencyType(new AgencyType(99999));
			agency.setStateCode(StringUtils.substring(preCertificationError.getOri(), 0, 2));
			agency.setStateName(StringUtils.substring(preCertificationError.getOri(), 0, 2));
			agency = agencyRepository.save(agency);
		}
		else if (agency == null){
			agency = agencyRepository.findById(99999).get();
		}
		preCertificationError.setAgency(agency);
		
		preCertificationError.setOwner(new Owner(preCertificationError.getOwnerId()));
		SegmentActionTypeType segmentActionType = 
				segmentActionTypeRepository.findFirstByStateCode(preCertificationError.getSegmentActionTypeCode());
		preCertificationError.setSegmentActionType(segmentActionType);
		
		NibrsErrorCodeType nibrsErrorCodeType = nibrsErrorCodeTypeRepository.findFirstByCode(preCertificationError.getNibrsErrorCode());
		preCertificationError.setNibrsErrorCodeType(nibrsErrorCodeType);
		
		return preCertificationError; 
	}
	
	@PostMapping("/search")
	public @ResponseBody SearchResult<PreCertificationError> search(@RequestBody PrecertErrorSearchRequest precertErrorSearchRequest){
		log.info("precertErrorSearchRequest:" + precertErrorSearchRequest);
		List<PreCertificationError> preCertificationErrors = preCertificationErrorRepositoryCustom.findAllByCriteria(precertErrorSearchRequest);
		
		SearchResult<PreCertificationError> searchResult = new SearchResult<>(preCertificationErrors, appProperties.getReportSearchResultsLimit());
		
		return searchResult;
	}
	

}
