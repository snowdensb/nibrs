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
package org.search.nibrs.admin.security.saml;

public enum SamlAttribute{
    SurName("gfipm:2.0:user:SurName"),
    FederatedQueryUserIndicator("gfipm:ext:user:FederatedQueryUserIndicator"),
    EmployerName("gfipm:2.0:user:EmployerName"),
    EmployeePositionName("gfipm:2.0:user:EmployeePositionName"),
    GivenName("gfipm:2.0:user:GivenName"),
    CommonName("gfipm:2.0:user:CommonName"),
    CriminalJusticeEmployerIndicator("gfipm:ext:user:CriminalJusticeEmployerIndicator"),
    LawEnforcementEmployerIndicator("gfipm:ext:user:LawEnforcementEmployerIndicator"),
    FederationId("gfipm:2.0:user:FederationId"), 
    TelephoneNumber("gfipm:2.0:user:TelephoneNumber"), 
    EmployerSubUnitName("gfipm:2.0:user:EmployerSubUnitName"), 
    EmailAddressText("gfipm:2.0:user:EmailAddressText"), 
    EmployerORI("gfipm:2.0:user:EmployerORI"),
    IdentityProviderId("gfipm:2.0:user:IdentityProviderId"),
    FirearmsRegistrationRecordsPersonnelIndicator("gfipm:ext:user:FirearmsRegistrationRecordsPersonnelIndicator"),
    SupervisoryRoleIndicator("gfipm:ext:user:SupervisoryRoleIndicator"),
    EmployerOrganizationCategoryText("gfipm:2.0:user:ext:EmployerOrganizationCategoryText")
    ; 
    
    private String attibuteName; 
    
    private SamlAttribute(String attributeName) {
        this.setAttibuteName(attributeName);
    }

    public String getAttibuteName() {
        return attibuteName;
    }

    public void setAttibuteName(String attibuteName) {
        this.attibuteName = attibuteName;
    }
    
}