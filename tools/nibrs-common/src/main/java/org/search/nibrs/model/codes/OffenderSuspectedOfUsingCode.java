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
package org.search.nibrs.model.codes;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for suspected-of-using (data element 8)
 */
public enum OffenderSuspectedOfUsingCode {
	
	A("A", "Alcohol"),
	C("C", "Computer Equipment (Handheld Devices)"),
	D("D", "Drugs/Narcotics"),
	N("N", "Not Applicable (Mutually Exclusive)");
	
	private OffenderSuspectedOfUsingCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<OffenderSuspectedOfUsingCode> asSet() {
		return EnumSet.allOf(OffenderSuspectedOfUsingCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (OffenderSuspectedOfUsingCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

	public static final Set<String> noneOrUnknownValueCodeSet() {
		return Collections.singleton(N.code);
	}

}
