package org.search.nibrs.model.reports.supplementaryhomicide;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Person {
	private String age; 
	private String sex; 
	private String race; 
	private String ethnicity;
	public Person() {
		super();
	}
	public Person(String age, String sex, String race, String ethnicity) {
		this();
		this.age = age;
		this.sex = sex;
		this.race = race;
		this.ethnicity = ethnicity;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getRace() {
		return race;
	}
	public void setRace(String race) {
		this.race = race;
	}
	public String getEthnicity() {
		return ethnicity;
	}
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
