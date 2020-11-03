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
package org.search.nibrs.model.reports;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReturnARecordCardReport implements Serializable{
	private static final long serialVersionUID = -2340194364781985383L;
	private Map<Integer, ReturnARecordCard> returnARecordCards; 
	private String stateName;
	private int year; 

	public ReturnARecordCardReport() {
		super();
		setReturnARecordCards(new HashMap<>());
	}

	public ReturnARecordCardReport(int year) {
		this();
		this.setYear(year);
	}
	
	public Map<Integer, ReturnARecordCard> getReturnARecordCards() {
		return returnARecordCards;
	}

	public void setReturnARecordCards(Map<Integer, ReturnARecordCard> returnARecordCards) {
		this.returnARecordCards = returnARecordCards;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	

}