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

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractSegment;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.codes.NIBRSErrorCode;

// This class replicates code from the corresponding class in the flatfile module, except for variations around non-numeric ages
// Violation of OAOO is justified to avoid a coupling between these format-oriented packages

final class NIBRSAgeBuilder {

	private static final NIBRSError buildNonNumericAgeError(char segmentContext, String ageString) {
		NIBRSError error = new NIBRSError();
		error.setValue(ageString);
		switch (segmentContext) {
		case '4':
			error.setNIBRSErrorCode(NIBRSErrorCode.valueOf("_" + segmentContext + "04"));
			break;
		case '5':
			error.setNIBRSErrorCode(NIBRSErrorCode.valueOf("_" + segmentContext + "56"));
			break;
		case '6':
			error.setNIBRSErrorCode(NIBRSErrorCode.valueOf("_" + segmentContext + "64"));
			break;
		case '7':
			error.setNIBRSErrorCode(NIBRSErrorCode.valueOf("_" + segmentContext + "57"));
			break;
		}
		return error;
	}

	static final NIBRSAge buildAgeFromRawString(String ageString, AbstractSegment segmentContext) {
		
		if (ageString == null || ageString.trim().length() == 0) {
			return null;
		}
		
		char segmentTypeCode = segmentContext.getSegmentType();
		
		String nonNumericAge = null;
		Integer ageMin = null;
		Integer ageMax = null;
		NIBRSError error = null;
		
		if (ageString != null) {
			String ageStringTrim = ageString.trim();
			if (ageStringTrim.length() == 4) {
				try {
					ageMin = Integer.parseInt(ageStringTrim.substring(0, 2));
				} catch (NumberFormatException nfe) {
					error = buildNonNumericAgeError(segmentTypeCode, ageString);
				}
				try {
					ageMax = Integer.parseInt(ageStringTrim.substring(2, 4));
				} catch (NumberFormatException nfe) {
					error = new NIBRSError();
					error.setValue(ageString);
					error.setNIBRSErrorCode(NIBRSErrorCode.valueOf("_" + segmentTypeCode + "09"));
				}
				if (ageMin == 0) {
					error = new NIBRSError();
					error.setValue(ageString);
					error.setNIBRSErrorCode(NIBRSErrorCode.valueOf("_" + segmentTypeCode + "22"));
				}
			} else if (ageStringTrim.length() <= 2) {
				try {
					ageMin = Integer.parseInt(ageStringTrim);
					ageMax = ageMin;
				} catch (NumberFormatException nfe) {
					nonNumericAge = ageStringTrim;
					error = buildNonNumericAgeError(segmentTypeCode, ageString);
				}
			} else {
				nonNumericAge = ageStringTrim; 
				if (ageStringTrim.length() == 3){
					error = new NIBRSError();
					error.setValue(ageString);
					error.setNIBRSErrorCode(NIBRSErrorCode.valueOf("_" + segmentTypeCode + "09"));
				}
				else{
					error = buildNonNumericAgeError(segmentTypeCode, ageString);
				}
			}
		}
		
		if (error != null) {
			error.setReport(segmentContext.getParentReport());
		}
		
		NIBRSAge ret = new NIBRSAge();
		ret.setAgeMin(ageMin);
		ret.setAgeMax(ageMax);
		ret.setNonNumericAge(nonNumericAge);
		ret.setError(error);
		
		return ret;
		
	}

}
