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

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for cargo theft indicator (Data Element 2A)
 */
public enum CargoTheftIndicatorCode {
	
	Y("Y" , "Yes"),
	N("N" , "No"),
	_blank(" ", "Not a Cargo Theft offense");
	
	public String code;
	public String description;
	
	private CargoTheftIndicatorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public static final Set<CargoTheftIndicatorCode> asSet() {
		return EnumSet.allOf(CargoTheftIndicatorCode.class);
	}
	
	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (CargoTheftIndicatorCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}
