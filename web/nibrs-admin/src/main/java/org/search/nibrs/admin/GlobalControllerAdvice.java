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

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource
    AppProperties appProperties;
    
    @ModelAttribute
    public void setupModelAttributes(Model model) {
        model.addAttribute("inactivityTimeout", appProperties.getInactivityTimeout());
        model.addAttribute("inactivityTimeoutInSeconds", appProperties.getInactivityTimeoutInSeconds());
        model.addAttribute("showUserInfoDropdown", appProperties.getShowUserInfoDropdown());
        model.addAttribute("securityEnabled", appProperties.getSecurityEnabled());
    }
    
}
