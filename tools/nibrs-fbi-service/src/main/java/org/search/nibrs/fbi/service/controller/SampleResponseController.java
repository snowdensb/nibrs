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
package org.search.nibrs.fbi.service.controller;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.xml.XmlUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

@RestController
public class SampleResponseController {
	private final Log log = LogFactory.getLog(SampleResponseController.class);
	
	private Integer count = 0; 

	@RequestMapping(value = "/response/accepted")
	@ResponseBody
	public String getSampleAcceptedResponse() throws Exception{
		log.info("in getSampleAcceptedResponse()");
		Document document = XmlUtils.parseFileToDocument(new File("src/main/resources/xmlInstances/NIBRS-Accepted-Response.xml"));
		return XmlUtils.nodeToString(document);
	}
	
	@RequestMapping(value="/response/error")
	@ResponseBody
	public String getSampleErrorResponse() throws Exception{
		Document document = XmlUtils.parseFileToDocument(new File("src/main/resources/xmlInstances/NIBRS-Error-Response.xml"));
		return XmlUtils.nodeToString(document);
	}
	
	@RequestMapping(value="/response/warning" )
	@ResponseBody
	public String getSampleWarningResponse() throws Exception{
		Document document = XmlUtils.parseFileToDocument(new File("src/main/resources/xmlInstances/NIBRS-Warnings-Response.xml"));
		return XmlUtils.nodeToString(document);
	}
	
	@RequestMapping(value="/response/fault" )
	@ResponseBody
	public String getSampleFaultResponse() throws Exception{
		Document document = XmlUtils.parseFileToDocument(new File("src/main/resources/xmlInstances/NIBRS-Fault-Response.xml"));
		return XmlUtils.nodeToString(document);
	}
	
	@RequestMapping(value="/response" )
	@ResponseBody
	public String getSampleResponse() throws Exception{
		String document = null;
		if (count % 40 == 0 ) {
			document = getSampleFaultResponse();
		}
		else if (count % 30 == 0) {
			document = getSampleWarningResponse();
		}
		else if (count % 20 == 0) {
			document = getSampleErrorResponse();
		}
		else {
			document = getSampleAcceptedResponse();
		}
		
		count ++; 
		return document;
	}
	
}
