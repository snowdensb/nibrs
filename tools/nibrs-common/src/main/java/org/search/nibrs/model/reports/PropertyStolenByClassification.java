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

import org.apache.commons.lang.builder.ToStringBuilder;

public class PropertyStolenByClassification implements Serializable{
	private static final long serialVersionUID = -8439219265321448834L;
	private double monetaryValue; 
	private int numberOfOffenses; 
	

	public PropertyStolenByClassification() {
		super();
	}

	public void increaseMonetaryValue(double monetaryValue){
		this.setMonetaryValue(this.getMonetaryValue() + monetaryValue);
	}

	public void increaseNumberOfOffenses(int numberOfOffenses){
		this.setNumberOfOffenses(this.getNumberOfOffenses() + numberOfOffenses);
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public double getMonetaryValue() {
		return monetaryValue;
	}

	public void setMonetaryValue(double monetaryValue) {
		this.monetaryValue = monetaryValue;
	}

	public int getNumberOfOffenses() {
		return numberOfOffenses;
	}

	public void setNumberOfOffenses(int numberOfOffenses) {
		this.numberOfOffenses = numberOfOffenses;
	}
}
