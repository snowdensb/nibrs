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
package org.search.nibrs.model.reports.arson;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

public class ArsonReport implements Serializable{
	private static final long serialVersionUID = 5814935054920484969L;
	private ArsonRow[] arsonRows = new ArsonRow[ArsonRowName.values().length]; 
	private String ori; 
	private String agencyName; 
	private String stateName;
	private String stateCode;
	private Integer population;
	private int month; 
	private int year; 
	
	public ArsonReport() {
		super();
		for (int i = 0; i < getArsonRows().length; i++){
			getArsonRows()[i] = new ArsonRow(); 
		}
	}
	
	public ArsonReport(String ori, int year, int month) {
		this();
		this.ori = ori; 
		this.year = year; 
		this.month = month; 
	}
	
	public ArsonReport(int year, int month) {
		this();
		this.year = year; 
		this.month = month; 
	}
	

	public String getOri() {
		return ori;
	}

	public void setOri(String ori) {
		this.ori = ori;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public int getMonth() {
		return month;
	}
	
	public String getMonthString() {
		return StringUtils.leftPad(String.valueOf(month), 2, '0');
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public Integer getPopulation() {
		return population;
	}

	public String getPopulationString() {
		return population == null? "" : String.valueOf(population);
	}
	
	public void setPopulation(Integer population) {
		this.population = population;
	}

	public ArsonRow[] getArsonRows() {
		return arsonRows;
	}

	public void setArsonRows(ArsonRow[] arsonRows) {
		this.arsonRows = arsonRows;
	}

}