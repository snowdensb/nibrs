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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.importer.DefaultReportListener;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.ZeroReport;
import org.search.nibrs.xmlfile.NibrsXmlFileProperties;

public class XmlIncidentBuilderZeroReportTest {
	private DefaultReportListener incidentListener;
	
	@Before
	public void setUp() throws Exception {
		InputStream inputStream = new FileInputStream(new File("src/test/resources/iep-sample/nibrs_ZeroReport_Sample.xml"));
		incidentListener = new DefaultReportListener();
		
		XmlIncidentBuilder incidentBuilder = new XmlIncidentBuilder(new NibrsXmlFileProperties());
		incidentBuilder.addIncidentListener(incidentListener);
		incidentBuilder.buildIncidents(inputStream, getClass().getName());
		List<NIBRSError> errorList = incidentListener.getErrorList();
		assertEquals(0, errorList.size());
	}

	@Test
	@Ignore
	//TODO work on the zero report builder 
	public void test() {
		List<GroupAIncidentReport> groupAIncidentList = incidentListener.getGroupAIncidentList();
		assertEquals(0, groupAIncidentList.size());
		List<ZeroReport> zeroIncidentList = incidentListener.getZeroReportList();
		assertEquals(1, zeroIncidentList.size());
		
		ZeroReport zeroReport = zeroIncidentList.get(0); 
		assertNotNull(zeroReport);
		assertEquals("000000000000", zeroReport.getIncidentNumber());
		assertEquals('0', zeroReport.getAdminSegmentLevel());
		assertEquals('A', zeroReport.getReportActionType());
		assertEquals(Integer.valueOf(2), zeroReport.getMonthOfTape());
		assertEquals(Integer.valueOf(2016), zeroReport.getYearOfTape());
	}

}
