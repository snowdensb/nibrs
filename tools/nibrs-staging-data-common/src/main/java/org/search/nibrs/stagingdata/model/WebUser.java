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

import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Cacheable
@Table(name = "user")
public class WebUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId; 
	
	private String federationId;
	private String firstName;
	private String lastName;
	private String email;
	
	public WebUser() {
		super();
	}
	public WebUser(Integer userId) {
		super();
		this.setUserId(userId);
	}
	

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, federationId, firstName, lastName, getUserId());
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof WebUser)) {
			return false;
		}
		WebUser other = (WebUser) obj;
		return Objects.equals(email, other.email) && Objects.equals(federationId, other.federationId)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(getUserId(), other.getUserId());
	}
	public String getFederationId() {
		return federationId;
	}
	public void setFederationId(String federationId) {
		this.federationId = federationId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
