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
package org.search.nibrs.flatfile.importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.flatfile.util.StringUtils;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * Representation of a single line in a NIBRS report file.
 *
 */
public class Segment
{
	
	private static final Logger LOG = LogManager.getLogger(Segment.class);
    
    private String segmentUniqueIdentifier;
    private String ori;
    private char segmentType;
    private char actionType;
    private String data;
    private int segmentLength;
    private char segmentLevel;
    private ReportSource reportSource;
    
    public char getSegmentLevel() {
		return segmentLevel;
	}

	public List<NIBRSError> setData(ReportSource reportSource, String data)
    {
    	List<NIBRSError> ret = new ArrayList<NIBRSError>();
    	
        this.data = data;
        this.reportSource = reportSource;
        NIBRSError e = null;
        if (data == null || data.length() < 37) {
        	e = new NIBRSError();
        	e.setContext(reportSource);
        	e.setNIBRSErrorCode(NIBRSErrorCode._001);
        	e.setDataElementIdentifier("Segment Length");
        	if (data != null) {
        		e.setValue(data.length());
        	}
			ret.add(e);
        }
		if (data.length() >= 5) {
			segmentType = StringUtils.getStringBetween(5, 5, data).charAt(0);
			if (e != null) {
				e.setSegmentType(segmentType);
			}
		}
		if (e == null) {
			String sv = StringUtils.getStringBetween(1, 4, data);
	        Integer i = null;
	        try {
				i = Integer.parseInt(sv);
				this.segmentLength = i;
	        } catch (NumberFormatException nfe) {
	        	e = new NIBRSError();
	        	e.setContext(reportSource);
	        	
	        	NIBRSErrorCode nibrsErrorCode = NIBRSErrorCode._001;
	        	if (Arrays.asList('0', '1').contains(segmentType)) {
	        		String errorCode = "_" + segmentType + "001";
	        		nibrsErrorCode = NIBRSErrorCode.valueOf(errorCode);
	        	}
        		e.setNIBRSErrorCode(nibrsErrorCode);
	        	e.setDataElementIdentifier("Segment Length");
	        	e.setValue(sv);
	        	ret.add(e);
	        }
	        if (i != data.length()) {
	        	LOG.warn("Warning at line " + reportSource.getSourceLocation() + ":  expected segment length of " + i + ", actual length was " + data.length());
	        }
	        if (e == null) {
	        	this.segmentUniqueIdentifier = StringUtils.getStringBetween(26, 37, data);
	        	this.ori = StringUtils.getStringBetween(17, 25, data);
	        	this.actionType =  data.charAt(5);
	        	this.segmentLevel = StringUtils.getStringBetween(5, 5, data).charAt(0);
	        }
		}
        return ret;
    }

	public ReportSource getReportSource() {
		return reportSource;
	}
    public String getData()
    {
        return data;
    }
    public String getSegmentUniqueIdentifier()
    {
        return segmentUniqueIdentifier;
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
	public int getSegmentLength() {
		return segmentLength;
	}

}
