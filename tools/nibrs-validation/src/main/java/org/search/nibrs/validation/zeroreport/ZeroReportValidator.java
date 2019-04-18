/*
 * Copyright 2016 Research Triangle Institute
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
package org.search.nibrs.validation.zeroreport;

import java.util.ArrayList;
import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.ZeroReport;


public class ZeroReportValidator {

	
	public List<NIBRSError> validate(ZeroReport zeroReport){
				
		List<NIBRSError> errorsList = new ArrayList<NIBRSError>();
		
		return errorsList;
	}
	
	
//	NIBRSError _015_validate12Zeros(ZeroReport zeroReport, List<NIBRSError> nibrsErrorList){
//				
//		NIBRSError rNibrsError = null;
//		
//		String incidentNumber = zeroReport.getIncidentNumber();
//		
//		if(!"000000000000".equals(incidentNumber)){
//			
//			rNibrsError = new NIBRSError();
//			
//			rNibrsError.setNIBRSErrorCode(NIBRSErrorCode._015);
//			
//			nibrsErrorList.add(rNibrsError);
//		}
//		
//		return rNibrsError;
//	}	
	
	
}
