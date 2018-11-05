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

import org.joda.time.LocalDateTime;
import org.search.nibrs.stagingdata.model.Submission;
import org.search.nibrs.stagingdata.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubmissionController {
	@Autowired
	private SubmissionRepository submissionRepository;
	
	@PostMapping("/submissions")
	public Submission saveSubmission(@RequestBody Submission submission){
		/*
		 * To fulfill the relationship mapping. 
		 */
		if (submission.getViolations() != null) {
			submission.getViolations()
				.forEach(violation->violation.setSubmission(submission));
		}
		return submissionRepository.save(submission);
	}
	
	@GetMapping(value="/submissions")
	public List<Submission> getAllSubmissions(){
		return submissionRepository.findAll();
	}
	
	@GetMapping(value="/localDateTime")
	public String getLocalDateTime(){
		return LocalDateTime.now().toString();
	}
	
}
