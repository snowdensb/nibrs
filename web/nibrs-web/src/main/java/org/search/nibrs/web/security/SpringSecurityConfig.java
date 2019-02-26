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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${rest.user.name}")
	private String userName;
	
	@Value("${rest.user.password}")
	private String userPassword;
	
    // Authentication, user, password and role
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
            .withUser(userName).password(userPassword).roles("JSON_USER");
    }

    // Authorization : configure which URLs are secured by what roles
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests()
        	.antMatchers("/about/**").permitAll()
        	.antMatchers("/toolLimitations/**").permitAll()
        	.antMatchers("/resources/**").permitAll()
        	.antMatchers("/testFiles/**").permitAll()
        	.antMatchers("/").permitAll()
            .antMatchers("/json").hasRole("JSON_USER")
            .and()
            .csrf().disable().headers().frameOptions().disable();
    }
}