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

package org.search.nibrs.admin.security;

import java.util.Collection;
import java.util.List;

import org.search.nibrs.stagingdata.model.Owner;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
public class AuthUser extends User implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = 1606369551188528192L;
	private List<String> oris;
	private Integer userId; 
	private String federationId;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String organizationName;
	private String agencyOri;

	// ~ Constructors
	// ===================================================================================================

	/**
	 * Calls the more complex constructor with all boolean arguments set to {@code true}.
	 */
	public AuthUser(String username, String password,
			Collection<? extends GrantedAuthority> authorities,  Owner owner) {
		super(username, password, true, true, true, true, authorities);
		this.setOris(oris);
		this.setEmailAddress(owner.getEmail());
		this.setFederationId(owner.getFederationId());
		this.setFirstName(owner.getFirstName());
		this.setLastName(owner.getLastName());
		this.setUserId(owner.getOwnerId());
	}

	// ~ Methods
	// ========================================================================================================

	/**
	 * Returns {@code true} if the supplied object is a {@code User} instance with the
	 * same {@code username} value.
	 * <p>
	 * In other words, the objects are equal if they have the same username, representing
	 * the same principal.
	 */
	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof AuthUser) {
			return super.equals(rhs);
		}
		return false;
	}

	/**
	 * Returns the hashcode of the {@code username}.
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(" ");
		sb.append("oris: ").append(oris).append("; ");
		sb.append("email: ").append(emailAddress).append("; ");
		sb.append("agencyOri: ").append(agencyOri).append("; ");
		sb.append("federationId: ").append(federationId).append("; ");
		sb.append("firstName: ").append(firstName).append("; ");
		sb.append("lastName: ").append(lastName).append("; ");
		sb.append("organizationName: ").append(organizationName).append("; ");
		return sb.toString();
	}


	public List<String> getOris() {
		return oris;
	}

	public void setOris(List<String> oris) {
		this.oris = oris;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getAgencyOri() {
		return agencyOri;
	}

	public void setAgencyOri(String agencyOri) {
		this.agencyOri = agencyOri;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
