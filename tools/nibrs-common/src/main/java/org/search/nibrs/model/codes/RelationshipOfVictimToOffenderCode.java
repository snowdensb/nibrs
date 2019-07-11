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
package org.search.nibrs.model.codes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum RelationshipOfVictimToOffenderCode {

	SE("SE","Victim Was Spouse", "Family Member_Spouse"),
	CS("CS","Victim Was Common-Law Spouse", "Family Member_Spouse_Common Law"),
	PA("PA","Victim Was Parent", "Family Member_Parent"),
	SB("SB","Victim Was Sibling", "Family Member_Sibling"),
	CH("CH","Victim Was Child", "Family Member_Child"),
	GP("GP","Victim Was Grandparent", "Family Member_Grandparent"),
	GC("GC","Victim Was Grandchild", "Family Member_Grandchild"),
	IL("IL","Victim Was In-law", "Family Member_In-Law"),
	SP("SP","Victim Was Step-parent", "Family Member_Stepparent"),
	SC("SC","Victim Was Step-child", "Family Member_Stepchild"),
	SS("SS","Victim Was Step-sibling", "Family Member_Stepsibling"),
	OF("OF","Victim Was Other Family Member", "Family Member"),
	AQ("AQ","Victim Was Acquaintance", "Acquaintance"),
	FR("FR","Victim Was Friend", "Friend"),
	NE("NE","Victim Was Neighbor", "Neighbor"),
	BE("BE","Victim Was Babysittee", "Babysittee"),
	BG("BG","Victim Was Boyfriend/Girlfriend", "Boyfriend_Girlfriend"),
	CF("CF","Victim Was Child of Boyfriend/Girlfriend", "Child of Boyfriend_Girlfriend"),
	XS("XS","Victim Was Ex-Spouse", "Ex_Spouse"),
	XR("XR","Victim Was Ex-Relationship", "Ex_Relationship"),
	EE("EE","Victim Was Employee", "Employee"),
	ER("ER","Victim Was Employer", "Employer"),
	OK("OK","Victim Was Otherwise Known", "NonFamily_Otherwise Known"),
	RU("RU","Relationship Unknown", "Relationship Unknown"),
	ST("ST","Victim Was Stranger", "Stranger"),
	VO("VO","Victim Was Offender", "Victim Was Offender");
	
	private RelationshipOfVictimToOffenderCode(String code, String description, String iepdCode){
		
		this.code = code;
		this.description = description;
		this.iepdCode = iepdCode; 
	}
	
	public String code;
	public String description;
	public String iepdCode;
		
	public static Set<String> codeSet(){
		
		Set<String> rCodeSet = new HashSet<>();
		
		for(RelationshipOfVictimToOffenderCode relationshipCode : values()){
			
			String sCode = relationshipCode.code;
			
			rCodeSet.add(sCode);
		}		
		return rCodeSet;
	}		

	public static final RelationshipOfVictimToOffenderCode valueOfIepdCode(String iepdCode){
		return	Arrays.stream(RelationshipOfVictimToOffenderCode.values())
					.filter(i->i.iepdCode.equals(iepdCode)).findFirst().orElse(null); 
	}


}

