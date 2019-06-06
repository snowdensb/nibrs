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
package org.search.nibrs.model.reports.asr;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AsrJuvenileRow extends AsrRow{
	private static final long serialVersionUID = -2900703485491703677L;
	public enum JuvenileAgeGroup{
		Under10, 
		_10To12,
		_13To14, 
		_15, 
		_16, 
		_17, 
		TOTAL
	}; 
	public AsrJuvenileRow() {
		super();
		setMaleAgeGroups(new int[JuvenileAgeGroup.values().length]);
		setFemaleAgeGroups(new int[JuvenileAgeGroup.values().length]);
		setRaceGroups(new int[Race.values().length]);
		setEthnicityGroups(new int[Ethnicity.values().length]);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
