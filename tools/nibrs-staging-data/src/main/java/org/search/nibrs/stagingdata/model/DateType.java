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
package org.search.nibrs.stagingdata.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Cacheable
public class DateType {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer dateTypeId; 
	
	private Date calendarDate; 
	private Integer yearNum; 
	private String yearLabel; 
	private Integer calendarQuarter; 
	private Integer monthNum; 
	private String monthName; 
	private String fullMonth; 
	private Integer dayNum; 
	private String dayOfWeek; 
	private Integer dayOfWeekSort; 
	private String dateMMDDYYYY; 
	
	public DateType() {
		super();
	}
	
	public DateType(Integer dateTypeId) {
		super();
		this.dateTypeId = dateTypeId;
	}
	
	public DateType(Integer dateTypeId, Date calendarDate, Integer year, String yearLabel, Integer calendarQuarter,
			Integer month, String monthName, String fullMonth, Integer day, String dayOfWeek, Integer dayOfWeekSort,
			String dateMMDDYYYY) {
		this();
		this.dateTypeId = dateTypeId;
		this.calendarDate = calendarDate;
		this.yearNum = year;
		this.yearLabel = yearLabel;
		this.calendarQuarter = calendarQuarter;
		this.monthNum = month;
		this.monthName = monthName;
		this.fullMonth = fullMonth;
		this.dayNum = day;
		this.dayOfWeek = dayOfWeek;
		this.dayOfWeekSort = dayOfWeekSort;
		this.dateMMDDYYYY = dateMMDDYYYY;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getDateTypeId() {
		return dateTypeId;
	}

	public void setDateTypeID(Integer dateTypeId) {
		this.dateTypeId = dateTypeId;
	}

	public Date getCalendarDate() {
		return calendarDate;
	}

	public void setCalendarDate(Date calendarDate) {
		this.calendarDate = calendarDate;
	}

	public Integer getYearNum() {
		return yearNum;
	}

	public void setYearNum(Integer yearNum) {
		this.yearNum = yearNum;
	}

	public String getYearLabel() {
		return yearLabel;
	}

	public void setYearLabel(String yearLabel) {
		this.yearLabel = yearLabel;
	}

	public Integer getCalendarQuarter() {
		return calendarQuarter;
	}

	public void setCalendarQuarter(Integer calendarQuarter) {
		this.calendarQuarter = calendarQuarter;
	}

	public Integer getMonthNum() {
		return monthNum;
	}

	public void setMonthNum(Integer monthNum) {
		this.monthNum = monthNum;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public String getFullMonth() {
		return fullMonth;
	}

	public void setFullMonth(String fullMonth) {
		this.fullMonth = fullMonth;
	}

	public Integer getDayNum() {
		return dayNum;
	}

	public void setDayNum(Integer dayNum) {
		this.dayNum = dayNum;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Integer getDayOfWeekSort() {
		return dayOfWeekSort;
	}

	public void setDayOfWeekSort(Integer dayOfWeekSort) {
		this.dayOfWeekSort = dayOfWeekSort;
	}

	public String getDateMMDDYYYY() {
		return dateMMDDYYYY;
	}

	public void setDateMMDDYYYY(String dateMMDDYYYY) {
		this.dateMMDDYYYY = dateMMDDYYYY;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((calendarDate == null) ? 0 : calendarDate.hashCode());
		result = prime * result + ((calendarQuarter == null) ? 0 : calendarQuarter.hashCode());
		result = prime * result + ((dateMMDDYYYY == null) ? 0 : dateMMDDYYYY.hashCode());
		result = prime * result + ((dateTypeId == null) ? 0 : dateTypeId.hashCode());
		result = prime * result + ((dayNum == null) ? 0 : dayNum.hashCode());
		result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
		result = prime * result + ((dayOfWeekSort == null) ? 0 : dayOfWeekSort.hashCode());
		result = prime * result + ((fullMonth == null) ? 0 : fullMonth.hashCode());
		result = prime * result + ((monthNum == null) ? 0 : monthNum.hashCode());
		result = prime * result + ((monthName == null) ? 0 : monthName.hashCode());
		result = prime * result + ((yearNum == null) ? 0 : yearNum.hashCode());
		result = prime * result + ((yearLabel == null) ? 0 : yearLabel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateType other = (DateType) obj;
		if (calendarDate == null) {
			if (other.calendarDate != null)
				return false;
		} else if (!calendarDate.equals(other.calendarDate))
			return false;
		if (calendarQuarter == null) {
			if (other.calendarQuarter != null)
				return false;
		} else if (!calendarQuarter.equals(other.calendarQuarter))
			return false;
		if (dateMMDDYYYY == null) {
			if (other.dateMMDDYYYY != null)
				return false;
		} else if (!dateMMDDYYYY.equals(other.dateMMDDYYYY))
			return false;
		if (dateTypeId == null) {
			if (other.dateTypeId != null)
				return false;
		} else if (!dateTypeId.equals(other.dateTypeId))
			return false;
		if (dayNum == null) {
			if (other.dayNum != null)
				return false;
		} else if (!dayNum.equals(other.dayNum))
			return false;
		if (dayOfWeek == null) {
			if (other.dayOfWeek != null)
				return false;
		} else if (!dayOfWeek.equals(other.dayOfWeek))
			return false;
		if (dayOfWeekSort == null) {
			if (other.dayOfWeekSort != null)
				return false;
		} else if (!dayOfWeekSort.equals(other.dayOfWeekSort))
			return false;
		if (fullMonth == null) {
			if (other.fullMonth != null)
				return false;
		} else if (!fullMonth.equals(other.fullMonth))
			return false;
		if (monthNum == null) {
			if (other.monthNum != null)
				return false;
		} else if (!monthNum.equals(other.monthNum))
			return false;
		if (monthName == null) {
			if (other.monthName != null)
				return false;
		} else if (!monthName.equals(other.monthName))
			return false;
		if (yearNum == null) {
			if (other.yearNum != null)
				return false;
		} else if (!yearNum.equals(other.yearNum))
			return false;
		if (yearLabel == null) {
			if (other.yearLabel != null)
				return false;
		} else if (!yearLabel.equals(other.yearLabel))
			return false;
		return true;
	}

}
