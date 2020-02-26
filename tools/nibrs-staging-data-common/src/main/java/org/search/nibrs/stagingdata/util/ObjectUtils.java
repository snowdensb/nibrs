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

import org.apache.commons.lang3.StringUtils;

/**
 *
 * A set of utility methods that deal with Dates.
 */
public class ObjectUtils
{
	public static Integer getInteger(String stringValue) {
		Integer result = null;
		if (StringUtils.isNotBlank(stringValue)) {
			result = Integer.valueOf(stringValue);
		}
		return result;
	}

}