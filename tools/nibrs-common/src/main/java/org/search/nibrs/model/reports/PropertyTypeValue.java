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

public class PropertyTypeValue implements Serializable{
	private static final long serialVersionUID = -8439219265321448834L;
	private double stolen; 
	private double recovered; 
	
	public PropertyTypeValue(int stolen, int recovered) {
		this();
		this.stolen = stolen;
		this.recovered = recovered;
	}

	public PropertyTypeValue() {
		super();
	}

	public double getStolen() {
		return stolen;
	}

	public void setStolen(double stolen) {
		this.stolen = stolen;
	}

	public double getRecovered() {
		return recovered;
	}
	
	public void increaseStolen(double stolen){
		this.stolen += stolen;
	}

	public void setRecovered(double recovered) {
		this.recovered = recovered;
	}
	
	public void increaseRecovered(double recovered){
		this.recovered += recovered;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
}
