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
package org.search.nibrs.admin;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.search.nibrs.admin.security.AuthUser;
import org.search.nibrs.admin.services.rest.RestService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@ControllerAdvice
@SessionAttributes({"authUser"})
public class GlobalControllerAdvice {
	
	@Resource
    AppProperties appProperties;
    
	@Resource
	RestService restService;
	
    @ModelAttribute
    public void setupModelAttributes(HttpServletRequest request, Model model, Authentication authentication) {
        model.addAttribute("inactivityTimeout", appProperties.getInactivityTimeout());
        model.addAttribute("inactivityTimeoutInSeconds", appProperties.getInactivityTimeoutInSeconds());
        model.addAttribute("showUserInfoDropdown", appProperties.getShowUserInfoDropdown());
        model.addAttribute("securityEnabled", appProperties.getSecurityEnabled());
        model.addAttribute("allowSubmitToFbi", appProperties.getAllowSubmitToFbi());
        model.addAttribute("privateSummaryReportSite", appProperties.getPrivateSummaryReportSite());
        
        AuthUser authUser = null;
        if (authentication != null) {
	        authUser = (AuthUser) authentication.getPrincipal();
	        model.addAttribute("authUser", authUser);
        }
        
		if (appProperties.getPrivateSummaryReportSite()) {
			model.addAttribute("agencyMapping", restService.getAgencies(StringUtils.EMPTY));
		}
		else if (authUser != null){
			model.addAttribute("agencyMapping", restService.getAgencies(Objects.toString(authUser.getUserId())));
		}
		else {
			model.addAttribute("agencyMapping", new HashMap<Integer, String>());
		}
			
    }
    
}
