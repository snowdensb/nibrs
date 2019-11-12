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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.admin.AppProperties;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class PortalAuthenticationDetailsSource implements
        AuthenticationDetailsSource<HttpServletRequest, PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails> {
    private final Log log = LogFactory.getLog(this.getClass());
    
	@Resource
	AppProperties appProperties;

    @Override
    public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(
            HttpServletRequest context) {
        
    	log.info("Enter portal authentication details source");
    	log.info("context:" + context.getRequestURL());
    	
        List<SimpleGrantedAuthority> grantedAuthorities = 
                new ArrayList<SimpleGrantedAuthority>(); 
 
        Element samlAssertion = (Element)context.getAttribute("samlAssertion");
        
        if (samlAssertion == null){
        	log.info("samlAssertion is null ");
        }
        else {
        	SimpleGrantedAuthority rolePortalUser = new SimpleGrantedAuthority(Authorities.AUTHZ_PORTAL.name()); 
        	grantedAuthorities.add(rolePortalUser);
        }
        
        return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, 
                grantedAuthorities);
    }

}