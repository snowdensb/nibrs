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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.search.nibrs.admin.security.PortalAuthenticationDetailsSource;
import org.search.nibrs.admin.security.PortalPreAuthenticatedUserDetailsService;
import org.search.nibrs.admin.security.SamlAuthenticationFilter;
import org.search.nibrs.admin.security.saml.SamlService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Resource
	AppProperties appProperties;

	@Resource
	SamlService samlService;
	
	@Resource
	PortalAuthenticationDetailsSource portalAuthenticationDetailsSource;
	
	@Resource
	PortalPreAuthenticatedUserDetailsService portalPreAuthenticatedUserDetailsService;
	
	@Override
    public void configure(WebSecurity web) throws Exception {
		if (appProperties.getSecurityEnabled()) {
			web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/testFiles/**", "/logoutSuccess/**", "/webjars/**", "/index");
		}
		else {
			web.ignoring().antMatchers("/**");
		}
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	if (appProperties.getSecurityEnabled()) {
	    	http
			    .authorizeRequests()
			    .anyRequest().authenticated()
			    .and()
			    	.logout().logoutUrl("/logout").deleteCookies("JSESSIONID").clearAuthentication(true).permitAll()
			    .and()
			    	.addFilterBefore(samlAuthenticationFilter(authenticationManager()), LogoutFilter.class);
    	}
    }    
    
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        final List<AuthenticationProvider> providers = new ArrayList<>(1);
        providers.add(preauthAuthProvider());
        return new ProviderManager(providers);
    }
    
    @Bean
    public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
    	PreAuthenticatedAuthenticationProvider preauthAuthProvider =
    		new PreAuthenticatedAuthenticationProvider();
    	preauthAuthProvider.setPreAuthenticatedUserDetailsService(portalPreAuthenticatedUserDetailsService);
    	return preauthAuthProvider;
    }
    
    /**
     * Do not add the @Bean annotation to the customer filter, otherwise the filter will not be ignored by the 
     *  web.ignoring() Urls.
     */
    public SamlAuthenticationFilter samlAuthenticationFilter(
        final AuthenticationManager authenticationManager) {
    	SamlAuthenticationFilter filter = new SamlAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationDetailsSource(portalAuthenticationDetailsSource);
        filter.setSamlService(samlService);
        return filter;
    }   
    
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("AUTHZ_"); 
    }
}