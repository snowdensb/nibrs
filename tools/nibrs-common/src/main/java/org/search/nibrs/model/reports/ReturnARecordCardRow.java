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
import java.util.Arrays;

public class ReturnARecordCardRow implements Serializable{
	private static final long serialVersionUID = 966471035821662433L;
	private int firstHalfSubtotal; 
	private int secondHalfSubtotal;
	private int total;
	private int[] months = new int[12];
	private int clearedOffenses;
	private int clearanceInvolvingOnlyJuvenile;
	
	@Override
	public String toString() {
		return "ReturnARecordCardRow [firstHalfSubtotal=" + firstHalfSubtotal + ", secondHalfSubtotal="
				+ secondHalfSubtotal + ", total=" + total + ", months=" + Arrays.toString(months) + ", clearedOffenses="
				+ clearedOffenses + ", clearanceInvolvingOnlyJuvenile=" + clearanceInvolvingOnlyJuvenile + "]";
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public void increaseTotal(int increment){
		total += increment;
	}
	public void increaseFirstHalfSubtotal(int increment){
		firstHalfSubtotal += increment;
	}
	public void increaseSecondHalfSubtotal(int increment){
		secondHalfSubtotal += increment;
	}
	public void increaseMonthNumber(int increment, int index){
		months[index] += increment;
	}
	public int getFirstHalfSubtotal() {
		return firstHalfSubtotal;
	}
	public void setFirstHalfSubtotal(int firstHalfSubtotal) {
		this.firstHalfSubtotal = firstHalfSubtotal;
	}
	public int getSecondHalfSubtotal() {
		return secondHalfSubtotal;
	}
	public void setSecondHalfSubtotal(int secondHalfSubtotal) {
		this.secondHalfSubtotal = secondHalfSubtotal;
	}
	public int[] getMonths() {
		return months;
	}
	public void setMonths(int[] months) {
		this.months = months;
	}
	public int getClearedOffenses() {
		return clearedOffenses;
	}
	public void setClearedOffenses(int clearedOffenses) {
		this.clearedOffenses = clearedOffenses;
	}
	public void increaseClearedOffenses(int increment){
		clearedOffenses += increment;
	}
	public int getClearanceInvolvingOnlyJuvenile() {
		return clearanceInvolvingOnlyJuvenile;
	}
	public void setClearanceInvolvingOnlyJuvenile(int clearanceInvolvingOnlyJuvenile) {
		this.clearanceInvolvingOnlyJuvenile = clearanceInvolvingOnlyJuvenile;
	}
	public void increaseClearanceInvolvingOnlyJuvenile(int increment){
		clearanceInvolvingOnlyJuvenile += increment;
	}
	
}
