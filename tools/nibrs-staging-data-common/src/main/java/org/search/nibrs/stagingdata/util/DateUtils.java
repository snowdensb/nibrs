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
package org.search.nibrs.stagingdata.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * A set of utility methods that deal with Dates.
 */
public class DateUtils
{
    public static Date asDate(LocalDate localDate) {
        return localDate == null ? null : Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    
	public static java.sql.Date getStartDate(Integer year, Integer month) {
		java.sql.Date date = null;
		if (year != null && year > 0) {
			LocalDate localDate = LocalDate.of(year, 1, 1);
			
			if (isValidMonth(month)) {
					localDate = LocalDate.of(year, month, 1);
			}
			date = java.sql.Date.valueOf(localDate);
		}
		
		return date;
	}
	
	public static boolean isValidMonth(Integer month) {
		return month != null && month > 0 && month <=12;
	}

}