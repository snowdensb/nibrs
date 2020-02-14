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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.search.nibrs.admin.security.saml.SamlService;
import org.search.nibrs.xml.XmlUtils;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.w3c.dom.Element;

public class SamlAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
	private final String EMPTY_FEDERATION_ID = "EmptyFederationID";
	private SamlService samlService;
    
    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        Element samlAssertion = this.extractSAMLAssertion(request);
        request.setAttribute("samlAssertion", samlAssertion);

        String federationId = null;
        if ( samlAssertion != null) {
           try {
            federationId = XmlUtils.xPathStringSearch(samlAssertion,
                        "/saml2:Assertion/saml2:AttributeStatement[1]/"
                        + "saml2:Attribute[@Name='gfipm:2.0:user:FederationId']/saml2:AttributeValue");
            
            } catch (Exception e) {
                e.printStackTrace();
            } 

        }
        
        String principal = StringUtils.isNotBlank(federationId)? federationId: EMPTY_FEDERATION_ID;
        
        request.setAttribute("principal", principal);
        return samlAssertion; 
    }

    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        
        Element samlAssertion = extractSAMLAssertion(request); 
        
        request.setAttribute("samlAssertion", samlAssertion);
        return samlAssertion;
    }

    private Element extractSAMLAssertion(HttpServletRequest request) {
        return samlService.getSamlAssertion(request); 
    }

    public SamlService getSamlService() {
        return samlService;
    }

    public void setSamlService(SamlService samlService) {
        this.samlService = samlService;
    }

    

}