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
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for bias motivation (Data element 8A)
 */
public enum BiasMotivationCode {
	
	_11("11" , "Anti-White", "ANTIWHITE"),
	_12("12" , "Anti-Black or African American", "ANTIBLACK_AFRICAN AMERICAN"),
	_13("13" , "Anti-American Indian or Alaska Native", "ANTIAMERICAN INDIAN_ ALASKAN NATIVE"),
	_14("14" , "Anti-Asian", "ANTIASIAN"),
	_15("15" , "Anti-Multiple Races, Group", "ANTIMULTIRACIAL GROUP"),
	_16("16" , "Anti-Native Hawaiian or Other Pacific Islander", "ANTINATIVEHAWAIIAN_OTHERPACIFICISLANDER"),
	_31("31" , "Anti-Arab", "ANTIARAB"),
	_32("32" , "Anti-Hispanic or Latino", "ANTIHISPANIC_LATINO"),
	_33("33" , "Anti-Other Race/Ethnicity/Ancestry", "ANTIOTHER ETHNICITY_NATIONAL ORIGIN"),
	_21("21" , "Anti-Jewish", "ANTIJEWISH"),
	_22("22" , "Anti-Catholic", "ANTICATHOLIC"),
	_23("23" , "Anti-Protestant", "ANTIPROTESTANT"),
	_24("24" , "Anti-Islamic (Muslim)", "ANTIISLAMIC"),
	_25("25" , "Anti-Other Religion", "ANTIOTHER RELIGION"),
	_26("26" , "Anti-Multiple Religions, Group", "ANTIMULTIRELIGIOUS GROUP"),
	_27("27" , "Anti-Atheism/Agnosticism", "ANTIATHEIST_AGNOSTIC"),
	_28("28" , "Anti-Mormon", "ANTIMORMON"),
	_29("29" , "Anti-Jehovah’s Witness", "ANTIJEHOVAHWITNESS"),
	_81("81" , "Anti-Eastern Orthodox (Russian, Greek, Other)", "ANTIEASTERNORTHODOX"),
	_82("82" , "Anti-Other Christian", "ANTIOTHER CHRISTIAN"),
	_83("83" , "Anti-Buddhist", "ANTIBUDDHIST"),
	_84("84" , "Anti-Hindu", "ANTIHINDU"),
	_85("85" , "Anti-Sikh", "ANTISIKH"),
	_41("41" , "Anti-Gay", "ANTIMALE HOMOSEXUAL"),
	_42("42" , "Anti-Lesbian", "ANTIFEMALE HOMOSEXUAL"),
	_43("43" , "Anti-Lesbian, Gay, Bisexual, or Transgender (Mixed Group)", "NA"),	
	_44("44","Anti-Heterosexual", "ANTIHETEROSEXUAL"),
	_45("45","Anti-Bisexual", "ANTIBISEXUAL"),
	_51("51","Anti-Physical Disability", "ANTIPHYSICAL DISABILITY"),
	_52("52","Anti-Mental Disability", "ANTIMENTAL DISABILITY"),
	_61("61","Anti-Male", "ANTIMALE"),
	_62("62","Anti-Female", "ANTIFEMALE"),
	_71("71","Anti-Transgender", "ANTITRANSGENDER"),
	_72("72","Anti-Gender Non-Conforming", "ANTIGENDER_NONCONFORMING"),
	_88("88","None (no bias)(mutually exclusive)", "NONE"),
	_99("99","Unknown (offender’s motivation not known)(mutually exclusive)", "UNKNOWN");
		
	private BiasMotivationCode(String code, String description, String iepdCode){
		this.code = code;
		this.description = description;
		this.iepdCode = iepdCode;
	}
	
	public String code;
	public String description;
	public String iepdCode;

	public static final Set<BiasMotivationCode> asSet() {
		return EnumSet.allOf(BiasMotivationCode.class);
	}
	
	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (BiasMotivationCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}
	
	public static final Set<String> noneOrUnknownValueCodeSet() {
		Set<String> ret = new HashSet<>();
		ret.add(_88.code);
		ret.add(_99.code);
		return ret;
	}
	
	public static final BiasMotivationCode valueOfIepdCode(String iepdCode){
		return	Arrays.stream(BiasMotivationCode.values()).filter(i->i.iepdCode.equals(iepdCode)).findFirst().orElse(null); 
	}

	public static final BiasMotivationCode valueOfCode(String code){
		return	Arrays.stream(BiasMotivationCode.values()).filter(i->i.code.equals(code)).findFirst().orElse(null); 
	}
	
}
