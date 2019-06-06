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

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AsrRow implements Serializable{
	private static final long serialVersionUID = -2900703485491703677L;
	private int[] maleAgeGroups; 
	private int[] femaleAgeGroups;
	private int[] raceGroups;
	private int[] ethnicityGroups;
	
	public enum Race{W, B, I, A, P}
	public enum Ethnicity{H, N};
	
	public AsrRow() {
		super();
		setRaceGroups(new int[Race.values().length]);
		setEthnicityGroups(new int[Ethnicity.values().length]);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public int[] getMaleAgeGroups() {
		return maleAgeGroups;
	}

	public void setMaleAgeGroups(int[] maleAgeGroups) {
		this.maleAgeGroups = maleAgeGroups;
	}

	public int[] getFemaleAgeGroups() {
		return femaleAgeGroups;
	}

	public void setFemaleAgeGroups(int[] femaleAgeGroups) {
		this.femaleAgeGroups = femaleAgeGroups;
	}

	public int[] getRaceGroups() {
		return raceGroups;
	}

	public void setRaceGroups(int[] raceGroups) {
		this.raceGroups = raceGroups;
	}

	public int[] getEthnicityGroups() {
		return ethnicityGroups;
	}

	public void setEthnicityGroups(int[] ethnicityGroups) {
		this.ethnicityGroups = ethnicityGroups;
	}
}
