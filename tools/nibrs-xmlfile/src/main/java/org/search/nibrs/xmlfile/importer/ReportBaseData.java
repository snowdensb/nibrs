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
package org.search.nibrs.xmlfile.importer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.xml.XmlUtils;
import org.w3c.dom.Element;

/**
 * Representation of a single line in a NIBRS report file.
 *
 */
public class ReportBaseData
{
	private static final Log log = LogFactory.getLog(ReportBaseData.class);;

    private String incidentNumber;
    private String ori;
    private char segmentType;
    private char actionType;
    private char segmentLevel;
    private String nibrsReportCategoryCode ; 

    private Element reportElement;
    private ReportSource reportSource;
    
    public char getSegmentLevel() {
		return segmentLevel;
	}

	public List<NIBRSError> setData(ReportSource reportSource, Element reportElement) 
    {
    	List<NIBRSError> ret = new ArrayList<NIBRSError>();
        this.setReportElement(reportElement);
        this.reportSource = reportSource;
        NIBRSError e = null;
        
		try {
			String ori = XmlUtils.xPathStringSearch(reportElement, "nibrs:ReportHeader/nibrs:ReportingAgency/j:OrganizationAugmentation/j:OrganizationORIIdentification/nc:IdentificationID");
			String reportActionType = XmlUtils.xPathStringSearch(reportElement, "nibrs:ReportHeader/nibrs:ReportActionCategoryCode");
			
			this.nibrsReportCategoryCode = XmlUtils.xPathStringSearch(reportElement, "nibrs:ReportHeader/nibrs:NIBRSReportCategoryCode");
			String incidentNumber = null;
			
			switch (this.nibrsReportCategoryCode){
			case "GROUP A INCIDENT REPORT":
				this.segmentLevel= '1'; 
				incidentNumber = XmlUtils.xPathStringSearch(reportElement, "nc:Incident/nc:ActivityIdentification/nc:IdentificationID");
				break; 
			case "GROUP B ARREST REPORT": 
				this.segmentLevel = '7'; 
				incidentNumber = XmlUtils.xPathStringSearch(reportElement, "j:Arrest/nc:ActivityIdentification/nc:IdentificationID");
				break; 
			case "ZERO REPORT": 
				this.segmentLevel = '0'; 
				incidentNumber = XmlUtils.xPathStringSearch(reportElement, "nc:Incident/nc:ActivityIdentification/nc:IdentificationID");
				break;
			}
			
			// TODO should have reject x01 errors based on segment level. 
			if (StringUtils.isBlank(ori) 
					|| StringUtils.isBlank(reportActionType)
					|| StringUtils.isBlank(incidentNumber)){
				throw new IllegalArgumentException("Ori, reportActionType and incidentNumber may not be empty."); 
			}
	
			this.ori = ori;
			this.actionType = reportActionType.charAt(0);
			this.incidentNumber = incidentNumber;
	
			
		} catch (Exception e1) {
			log.error(e1);
        	e = new NIBRSError();
        	e.setContext(reportSource);
        	e.setNIBRSErrorCode(NIBRSErrorCode._001);
        	e.setDataElementIdentifier("SGL");
        	ret.add(e);

		}

    	
        return ret;
    }

	public ReportSource getReportSource() {
		return reportSource;
	}
    public String getOri()
    {
        return ori;
    }
    public char getSegmentType()
    {
        return segmentType;
    }
    public char getActionType()
    {
        return actionType;
    }

	public String getIncidentNumber() {
		return incidentNumber;
	}

	public Element getReportElement() {
		return reportElement;
	}

	public void setReportElement(Element reportElement) {
		this.reportElement = reportElement;
	}

}
