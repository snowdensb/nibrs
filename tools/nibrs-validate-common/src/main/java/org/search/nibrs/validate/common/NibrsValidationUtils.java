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
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.flatfile.importer.IncidentBuilder;
import org.search.nibrs.importer.ReportListener;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.util.NibrsFileUtils;
import org.search.nibrs.xmlfile.importer.XmlIncidentBuilder;
import org.xml.sax.SAXException;

/**
 * Utilities class for getting file content type
 *
 */
public class NibrsValidationUtils {
	private static final Log log = LogFactory.getLog(NibrsValidationUtils.class);
	
	public static void validateFile(ReportListener validatorListener, 
			File file) throws ParserConfigurationException, IOException, TikaException, SAXException {
		String fileType = NibrsFileUtils.getMediaType(file);
		FileInputStream inputStream = new FileInputStream(file);
		validateInputStream(validatorListener, fileType, inputStream, file.getAbsolutePath());
	}

	public static final void validateInputStream(ReportListener validatorListener, String fileContentType,
			InputStream stream, String readerLocationName) throws ParserConfigurationException, IOException {

		switch (fileContentType){
		case "text/xml":
		case "application/xml":
			XmlIncidentBuilder xmlIncidentBuilder = new XmlIncidentBuilder();
			xmlIncidentBuilder.addIncidentListener(validatorListener);
			xmlIncidentBuilder.buildIncidents(stream, readerLocationName);

			break; 
		case "text/plain": 
		case "application/octet-stream": 
			Reader inputReader = new BufferedReader(new InputStreamReader(stream));
			IncidentBuilder incidentBuilder = new IncidentBuilder();

			incidentBuilder.addIncidentListener(validatorListener);

			incidentBuilder.buildIncidents(inputReader, readerLocationName);
			inputReader.close();
			break;
		default:
			log.warn("The file type " + fileContentType + " is not supported"); 
		}
		
		stream.close();
	}

	public static void addReportWithoutErrors(ValidationResults validationResults, AbstractReport report) {
		validationResults.increaseTotalReportCount();
		if (validationResults.getErrorList().isEmpty()){
			validationResults.getReportsWithoutErrors().add(report);
		}
		else{
			NIBRSError nibrsError = validationResults.getErrorList().get(validationResults.getErrorList().size()-1);
			if (report.getIdentifier() != null && 
					!report.getIdentifier().equals(nibrsError.getReportUniqueIdentifier())) {
				validationResults.getReportsWithoutErrors().add(report);
			}
		}
	}
	

}

