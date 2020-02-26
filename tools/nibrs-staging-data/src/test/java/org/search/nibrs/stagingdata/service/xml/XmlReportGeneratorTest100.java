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
package org.search.nibrs.stagingdata.service.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.search.nibrs.stagingdata.repository.segment.AdministrativeSegmentRepository;
import org.search.nibrs.stagingdata.repository.segment.ArrestReportSegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class XmlReportGeneratorTest100 {
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(XmlReportGeneratorTest100.class);
    private String nibrsFileFolderPath = "/tmp/nibrs/xml/";

	@Autowired
	public AdministrativeSegmentRepository administrativeSegmentRepository; 
	@Autowired
	public ArrestReportSegmentRepository arrestReportSegmentRepository; 
	@Autowired
	public XmlReportGenerator xmlReportGenerator;

	private Transformer transformer; 
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS");
	
	@Before
	public void setup() throws TransformerConfigurationException, IOException{
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    transformer = transformerFactory.newTransformer();
	}
	
	@Test
	public void createGroupAIncidentReport() throws Exception {
	    File directorty = new File(nibrsFileFolderPath); 
	    if (!directorty.exists()){
	    	directorty.mkdirs(); 
	    }
	    FileUtils.cleanDirectory(directorty);
		
		List<Integer> ids = administrativeSegmentRepository.findIdsByOriAndIncidentDate(null, 2018, 1);
		List<Integer> administrativeSegmentIds = ids.stream().limit(50).collect(Collectors.toList()); 
		
		for (Integer administrativeSegmentId : administrativeSegmentIds) {
			AdministrativeSegment administrativeSegment = administrativeSegmentRepository.findByAdministrativeSegmentId(administrativeSegmentId);
			Document document = xmlReportGenerator.createGroupAIncidentReport(administrativeSegment);
			
			String fileName = nibrsFileFolderPath + "GroupAIncident" + administrativeSegment.getIncidentNumber() + "-" + LocalDateTime.now().format(formatter) + ".xml";
			
			DOMSource source = new DOMSource(document);
		    FileWriter writer = new FileWriter(new File(fileName));
		    StreamResult result = new StreamResult(writer);

		    transformer.transform(source, result);
		    writer.close();
		    
//			XmlUtils.printNode(document);
			
		}
	}
	
	@Test
	public void createGroupBIncidentReport() throws Exception {
		
		List<Integer> ids = arrestReportSegmentRepository.findIdsByOriAndArrestDate(null, 2018, 1, null);
		List<Integer> arrestReportSegmentIds = ids.stream().limit(50).collect(Collectors.toList()); 
		
		for (Integer arrestReportSegmentId : arrestReportSegmentIds) {
			ArrestReportSegment arrestReportSegment = arrestReportSegmentRepository.findByArrestReportSegmentId(arrestReportSegmentId);
			Document document = xmlReportGenerator.createGroupBArrestReport(arrestReportSegment);
			
			String fileName = nibrsFileFolderPath + "GroupBArrestReport" + arrestReportSegment.getArrestTransactionNumber() + "-" + LocalDateTime.now().format(formatter) + ".xml";
			
			DOMSource source = new DOMSource(document);
			FileWriter writer = new FileWriter(new File(fileName));
			StreamResult result = new StreamResult(writer);
			
			transformer.transform(source, result);
			writer.close();
			
//			XmlUtils.printNode(document);
			
		}
	}

}
