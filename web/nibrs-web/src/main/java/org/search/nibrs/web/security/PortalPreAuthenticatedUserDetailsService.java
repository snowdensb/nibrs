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
package org.search.nibrs.web.security;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.xml.XmlUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

@Component
public class PortalPreAuthenticatedUserDetailsService implements
		AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final Log log = LogFactory.getLog(this.getClass());
	/**
	 * Get a UserDetails object based on the user name contained in the given token, and
	 * the GrantedAuthorities as returned by the GrantedAuthoritiesContainer
	 * implementation as returned by the token.getDetails() method.
	 */
	public final UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token)
			throws AuthenticationException {
		Assert.notNull(token.getDetails(), "token.getDetails() cannot be null");
		Assert.isInstanceOf(GrantedAuthoritiesContainer.class, token.getDetails());
		Collection<? extends GrantedAuthority> authorities = ((GrantedAuthoritiesContainer) token
				.getDetails()).getGrantedAuthorities();
		return createUserDetails(token, authorities);
	}

	/**
	 * Creates the final <tt>UserDetails</tt> object. Can be overridden to customize the
	 * contents.
	 *
	 * @param token the authentication request token
	 * @param authorities the pre-authenticated authorities.
	 */
	protected UserDetails createUserDetails(Authentication token,
			Collection<? extends GrantedAuthority> authorities) {
		String userName = "anonymous"; 
		Element samlAssertion = (Element)token.getPrincipal();
		try {
			String givenName = XmlUtils.xPathStringSearch(samlAssertion,
			        "/saml2:Assertion/saml2:AttributeStatement[1]/"
			        + "saml2:Attribute[@Name='gfipm:2.0:user:GivenName']/saml2:AttributeValue");
			String surName = XmlUtils.xPathStringSearch(samlAssertion,
					"/saml2:Assertion/saml2:AttributeStatement[1]/"
							+ "saml2:Attribute[@Name='gfipm:2.0:user:SurName']/saml2:AttributeValue");
			String fullName = StringUtils.join(Arrays.asList(givenName, surName), ' ');
			if (StringUtils.isNotBlank(fullName)) {
				userName = fullName; 
			}
		} catch (Exception e) {
			log.error("Failed to retrieve the user name from the SAML token.", e);
		}
		return new User(userName, "N/A", true, true, true, true, authorities);
	}
}