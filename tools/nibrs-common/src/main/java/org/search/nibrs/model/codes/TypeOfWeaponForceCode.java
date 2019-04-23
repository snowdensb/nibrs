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

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for type of weapon/force (data element 13)
 */
public enum TypeOfWeaponForceCode {
	
	_11("11", "Firearm"),
	_12("12", "Handgun"),
	_13("13", "Rifle"),
	_14("14", "Shotgun"),
	_15("15", "Other Firearm"),
	_20("20", "Knife/Cutting Instrument"),
	_30("30", "Blunt Object"),
	_35("35", "Motor Vehicle/Vessel"),
	_40("40", "Personal Weapons"),
	_50("50", "Poison"),
	_60("60", "Explosives"),
	_65("65", "Fire/Incendiary Device"),
	_70("70", "Drugs/Narcotics/Sleeping Pills"),
	_85("85", "Asphyxiation"),
	_90("90", "Other"),
	_95("95", "Unknown"),
	_99("99", "None");
		
	private TypeOfWeaponForceCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<TypeOfWeaponForceCode> asSet() {
		return EnumSet.allOf(TypeOfWeaponForceCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (TypeOfWeaponForceCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

	public static final Set<String> noneValueCodeSet() {
		Set<String> ret = new HashSet<>();
		ret.add(_99.code);
		return ret;
	}

}
