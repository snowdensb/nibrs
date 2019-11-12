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
package org.search.nibrs.model.reports.supplementaryhomicide;

/**
 * Code ENUM for HomicideSituation
 */
public enum HomicideSituation {
	
	A("A","Single Victim/Single Offender"),
	B("B","Single Victim/Unknown Offender or Offenders"),
	C("C","Single Victim/Multiple Offenders"),
	D("D","Multiple Victims/Single Offenders"),
	E("E","Multiple Victims/Multiple Offenders"),
	F("F","Multiple Victims/Unknown Offender or Offenders");
	
	private HomicideSituation(String code, String description){
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;

}
