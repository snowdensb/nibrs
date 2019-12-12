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
package org.search.nibrs.validate.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.exception.TikaException;
import org.search.nibrs.flatfile.importer.IncidentBuilder;
import org.search.nibrs.importer.ReportListener;
import org.search.nibrs.util.NibrsFileUtils;
import org.search.nibrs.xmlfile.importer.XmlIncidentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class SubmissionFileValidator {
	private static final Log log = LogFactory.getLog(SubmissionFileValidator.class);
	@Autowired
	XmlIncidentBuilder xmlIncidentBuilder;

	@Autowired
	IncidentBuilder incidentBuilder;
	
	
	public void validateFile(ReportListener validatorListener, 
			File file) throws ParserConfigurationException, IOException, TikaException, SAXException {
		String fileType = NibrsFileUtils.getMediaType(file);
		FileInputStream inputStream = new FileInputStream(file);
		validateInputStream(validatorListener, fileType, inputStream, file.getAbsolutePath());
	}

	public final void validateInputStream(ReportListener validatorListener, String fileContentType,
			InputStream stream, String readerLocationName) throws ParserConfigurationException, IOException {

		switch (fileContentType){
		case "text/xml":
		case "application/xml":
			xmlIncidentBuilder.addIncidentListener(validatorListener);
			xmlIncidentBuilder.buildIncidents(stream, readerLocationName);

			break; 
		case "text/plain": 
		case "application/octet-stream": 
			Reader inputReader = new BufferedReader(new InputStreamReader(stream));

			incidentBuilder.addIncidentListener(validatorListener);

			incidentBuilder.buildIncidents(inputReader, readerLocationName);
			inputReader.close();
			break;
		default:
			log.warn("The file type " + fileContentType + " is not supported"); 
		}
		
		stream.close();
	}

	
}

