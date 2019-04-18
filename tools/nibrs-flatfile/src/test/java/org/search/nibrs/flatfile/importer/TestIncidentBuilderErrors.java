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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.importer.DefaultReportListener;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.NIBRSErrorCode;

public class TestIncidentBuilderErrors {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(TestIncidentBuilderErrors.class);
	
    @Test
    public void testBadSegmentValue() throws IOException {
    	
    	String testData = 
            "00871I022003    TN006000002-000895   20020102 10N                                      \n" +
            "00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
            "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
            "01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
            "00468I022003    TN006000002-000895   0124  MW \n";
    	
    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);

        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(NIBRSErrorCode._051, e.getNIBRSErrorCode());
        assertEquals('8', e.getValue());
        ReportSource reportSource = (ReportSource) e.getContext();
        assertEquals("5", reportSource.getSourceLocation());
        
        GroupAIncidentReport incident = incidentListener.getGroupAIncidentList().get(0);
		assertTrue(incident.getHasUpstreamErrors());
		assertEquals(incident, e.getReport());
    	
    }

    @Test
    public void testInvalidInts() throws IOException {
    	
    	String testData = 
            "00871IABCDEF    TN006000002-000895   20020102 10N                                      \n" +
            "00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
            "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
            "01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
            "00465I022003    TN006000002-000895   0124  MW \n";
    	
    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(2, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals("AB", e.getValue());
    	
        GroupAIncidentReport incident = incidentListener.getGroupAIncidentList().get(0);
		assertTrue(incident.getHasUpstreamErrors());
		assertEquals(incident, e.getReport());

    }
    
    @Test
    public void testInvalidVictimSegmentInts() throws IOException {
    	
    	String testData = 
            "00871I022003    TN006000002-000895   20020102 10N                                      \n" +
            "00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
            "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
            "01414I022003    TN006000002-000895   001220                           I46  FWNR          XXAQ                                                \n" +
            "00465I022003    TN006000002-000895   0124  MW \n";
    	
    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals("XX", e.getValue());
        assertEquals(NIBRSErrorCode._402, e.getNIBRSErrorCode());
        assertEquals("34", e.getDataElementIdentifier());
        assertEquals(1, e.getWithinSegmentIdentifier());
    	
        GroupAIncidentReport incident = incidentListener.getGroupAIncidentList().get(0);
		assertTrue(incident.getHasUpstreamErrors());
		assertEquals(incident, e.getReport());

    }
    
    @Test
    public void testInvalidAdministrativeSegmentLength() throws IOException {
    	
    	String testData = 
                "00801I022003    TN006000002-000895   20020102 10N                               \n" +
                "00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
                "03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
                "01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
                "00465I022003    TN006000002-000895   0124  MW \n";
        	
    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        
        NIBRSError e = errorList.get(0);
        assertEquals(GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        ReportSource reportSource = (ReportSource) e.getContext();
        assertEquals("1", reportSource.getSourceLocation());
        assertEquals(80, e.getValue());
        assertEquals(NIBRSErrorCode._178, e.getNIBRSErrorCode());
    	
        GroupAIncidentReport incident = incidentListener.getGroupAIncidentList().get(0);
		assertTrue(incident.getHasUpstreamErrors());
		assertEquals(incident, e.getReport());

    }
    
    @Test
    public void testInvalidArresteeSegmentLength() throws IOException {
    	
    	String testData = 
    			"00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
    			"00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
    			"03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
    			"01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
    			"00465I022003    TN006000002-000895   0124  MW \n" +
    			"01116I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                 ";

    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        ReportSource reportSource = (ReportSource) e.getContext();
        assertEquals("6", reportSource.getSourceLocation());
        assertEquals(111, e.getValue());
        
        GroupAIncidentReport incident = incidentListener.getGroupAIncidentList().get(0);
		assertTrue(incident.getHasUpstreamErrors());
		assertEquals(incident, e.getReport());
        
    }

    @Test
    public void testInvalidOffenseSegmentLength() throws IOException {
    	
    	String testData = 
    			"00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
    			"00702I022003    TN006000002-000895   220CN  20  N            88       \n" +
    			"03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
    			"01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
    			"00465I022003    TN006000002-000895   0124  MW \n" +
    			"01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                ";

    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(OffenseSegment.OFFENSE_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        ReportSource reportSource = (ReportSource) e.getContext();
        assertEquals("2", reportSource.getSourceLocation());
        assertEquals(70, e.getValue());
        
        GroupAIncidentReport incident = incidentListener.getGroupAIncidentList().get(0);
		assertTrue(incident.getHasUpstreamErrors());
		assertEquals(incident, e.getReport());
        
    }

    @Test
    public void testInvalidPropertySegmentLength() throws IOException {
    	
    	String testData = 
    			"00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
    			"00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
    			"03063I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                 \n" +
    			"01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
    			"00465I022003    TN006000002-000895   0124  MW \n" +
    			"01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                ";

    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(PropertySegment.PROPERTY_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        ReportSource reportSource = (ReportSource) e.getContext();
        assertEquals("3", reportSource.getSourceLocation());
        assertEquals(306, e.getValue());
        
        GroupAIncidentReport incident = incidentListener.getGroupAIncidentList().get(0);
		assertTrue(incident.getHasUpstreamErrors());
		assertEquals(incident, e.getReport());
        
    }

    @Test
    public void testInvalidVictimSegmentLength() throws IOException {
    	
    	String testData = 
    			"00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
    			"00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
    			"03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
    			"01404I022003    TN006000002-000895   001220                           I46  FWNR                                                             \n" +
    			"00465I022003    TN006000002-000895   0124  MW \n" +
    			"01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                ";

    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        ReportSource reportSource = (ReportSource) e.getContext();
        assertEquals("4", reportSource.getSourceLocation());
        assertEquals(140, e.getValue());
        
        GroupAIncidentReport incident = incidentListener.getGroupAIncidentList().get(0);
		assertTrue(incident.getHasUpstreamErrors());
		assertEquals(incident, e.getReport());
        
    }

    @Test
    public void testInvalidOffenderSegmentLength() throws IOException {
    	
    	String testData = 
    			"00881I022003    TN006000002-000895   20020102 10N                                      N\n" +
    			"00712I022003    TN006000002-000895   220CN  20  N            88        \n" +
    			"03073I022003    TN006000002-000895   713000000020                                                                                                                                                                                                                                                                  \n" +
    			"01414I022003    TN006000002-000895   001220                           I46  FWNR                                                              \n" +
    			"00475I022003    TN006000002-000895   0124   MW \n" +
    			"01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                ";

    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
        NIBRSError e = errorList.get(0);
        assertEquals(OffenderSegment.OFFENDER_SEGMENT_TYPE_IDENTIFIER, e.getSegmentType());
        ReportSource reportSource = (ReportSource) e.getContext();
        assertEquals("5", reportSource.getSourceLocation());
        assertEquals(47, e.getValue());
        
        GroupAIncidentReport incident = incidentListener.getGroupAIncidentList().get(0);
		assertTrue(incident.getHasUpstreamErrors());
		assertEquals(incident, e.getReport());
        
    }
    
    @Test
    public void testInvalidRecoveredDate() throws IOException {
    	String testData = 
                "00871I022003    TN006000002-000895   20020102 10N                                      \n" +
                "00632I022003    TN006000002-000895   220CN  20  N            88\n" +
                "03073I022003    TN006000002-000895   713000000020         3800000005020161102                                                                                                                                                                                                                                      \n" +
                "01294I022003    TN006000002-000895   001220                           I46  FWNR                                                  \n" +
                "00455I022003    TN006000002-000895   0124  MW\n" +
                "01106I022003    TN006000002-000895   0102-000895   20021230TM22001    24  MWNR                                \n";

    	DefaultReportListener incidentListener = new DefaultReportListener();
        List<NIBRSError> errorList = getErrorsForTestData(testData, incidentListener);
        assertEquals(1, errorList.size());
    }

	private List<NIBRSError> getErrorsForTestData(String testData, DefaultReportListener incidentListener) throws IOException {
		Reader testdataReader = new BufferedReader(new StringReader(testData));
        IncidentBuilder incidentBuilder = new IncidentBuilder();
        incidentBuilder.addIncidentListener(incidentListener);
        incidentBuilder.buildIncidents(testdataReader, getClass().getName());
        List<NIBRSError> errorList = incidentListener.getErrorList();
		return errorList;
	}
    
}
