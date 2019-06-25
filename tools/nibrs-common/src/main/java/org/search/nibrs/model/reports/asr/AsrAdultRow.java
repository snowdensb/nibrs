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

public class AsrAdultRow extends AsrRow{
	private static final long serialVersionUID = -2900703485491703677L;
	public enum AdultAgeGroup{
		_18, 
		_19,
		_20, 
		_21, 
		_22, 
		_23, 
		_24, 
		_25To29, 
		_30To34, 
		_35To39, 
		_40To44, 
		_45To49, 
		_50To54, 
		_55To59, 
		_60To64, 
		_65AndOver, 
		TOTAL
	}; 
	public AsrAdultRow() {
		super();
		setMaleAgeGroups(new int[AdultAgeGroup.values().length]);
		setFemaleAgeGroups(new int[AdultAgeGroup.values().length]);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
