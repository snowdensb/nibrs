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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * Code enum for property description (data element 15)
 */
public enum PropertyDescriptionCode {
	/**
	 * Aircraft
	 */
	_01("01", "Aircraft"),
	/**
	 * Alcohol
	 */
	_02("02", "Alcohol"),
	/**
	 * Automobiles
	 */
	_03("03", "Automobiles"),
	/**
	 * Bicycles
	 */
	_04("04", "Bicycles"),
	/**
	 * Buses
	 */
	_05("05", "Buses"),
	/**
	 * Clothes/Furs
	 */
	_06("06", "Clothes/Furs"),
	/**
	 * Computer Hardware/Software
	 */
	_07("07", "Computer Hardware/Software"),
	/**
	 * Consumable Goods
	 */
	_08("08", "Consumable Goods"),
	/**
	 * Credit/Debit Cards
	 */
	_09("09", "Credit/Debit Cards"),
	/**
	 * Drugs/Narcotics
	 */
	_10("10", "Drugs/Narcotics"),
	/**
	 * Drug/Narcotic Equipment
	 */
	_11("11", "Drug/Narcotic Equipment"),
	/**
	 * Farm Equipment
	 */
	_12("12", "Farm Equipment"),
	/**
	 * Firearms
	 */
	_13("13", "Firearms"),
	/**
	 * Gambling Equipment
	 */
	_14("14", "Gambling Equipment"),
	/**
	 * Heavy Construction/Industrial Equipment
	 */
	_15("15", "Heavy Construction/Industrial Equipment"),
	/**
	 * Household Goods
	 */
	_16("16", "Household Goods"),
	/**
	 * Jewelry/Precious Metals/Gems
	 */
	_17("17", "Jewelry/Precious Metals/Gems"),
	/**
	 * Livestock
	 */
	_18("18", "Livestock"),
	/**
	 * Merchandise
	 */
	_19("19", "Merchandise"),
	/**
	 * Money
	 */
	_20("20", "Money"),
	/**
	 * Negotiable Instruments
	 */
	_21("21", "Negotiable Instruments"),
	/**
	 * Nonnegotiable Instruments
	 */
	_22("22", "Nonnegotiable Instruments"),
	/**
	 * Office-type Equipment
	 */
	_23("23", "Office-type Equipment"),
	/**
	 * Other Motor Vehicles
	 */
	_24("24", "Other Motor Vehicles"),
	/**
	 * Purses/Handbags/Wallets
	 */
	_25("25", "Purses/Handbags/Wallets"),
	/**
	 * Radios/TVs/VCRs/DVD Players
	 */
	_26("26", "Radios/TVs/VCRs/DVD Players"),
	/**
	 * Recordings–Audio/Visual
	 */
	_27("27", "Recordings–Audio/Visual"),
	/**
	 * Recreational Vehicles
	 */
	_28("28", "Recreational Vehicles"),
	/**
	 * Structures–Single Occupancy Dwellings
	 */
	_29("29", "Structures–Single Occupancy Dwellings"),
	/**
	 * Structures–Other Dwellings
	 */
	_30("30", "Structures–Other Dwellings"),
	/**
	 * Structures–Other Commercial/Business
	 */
	_31("31", "Structures–Other Commercial/Business"),
	/**
	 * Structures–Industrial/Manufacturing
	 */
	_32("32", "Structures–Industrial/Manufacturing"),
	/**
	 * Structures–Public/Community
	 */
	_33("33", "Structures–Public/Community"),
	/**
	 * Structures–Storage
	 */
	_34("34", "Structures–Storage"),
	/**
	 * Structures–Other
	 */
	_35("35", "Structures–Other"),
	/**
	 * Tools
	 */
	_36("36", "Tools"),
	/**
	 * Trucks
	 */
	_37("37", "Trucks"),
	/**
	 * Vehicle Parts/Accessories
	 */
	_38("38", "Vehicle Parts/Accessories"),
	/**
	 * Watercraft
	 */
	_39("39", "Watercraft"),
	/**
	 * Aircraft Parts/Accessories
	 */
	_41("41", "Aircraft Parts/Accessories"),
	/**
	 * Artistic Supplies/Accessories
	 */
	_42("42", "Artistic Supplies/Accessories"),
	/**
	 * Building Materials
	 */
	_43("43", "Building Materials"),
	/**
	 * Camping/Hunting/Fishing Equipment/Supplies
	 */
	_44("44", "Camping/Hunting/Fishing Equipment/Supplies"),
	/**
	 * Chemicals
	 */
	_45("45", "Chemicals"),
	/**
	 * Collections/Collectibles
	 */
	_46("46", "Collections/Collectibles"),
	/**
	 * Crops
	 */
	_47("47", "Crops"),
	/**
	 * Documents/Personal or Business
	 */
	_48("48", "Documents/Personal or Business"),
	/**
	 * Explosives
	 */
	_49("49", "Explosives"),
	/**
	 * Firearm Accessories
	 */
	_59("59", "Firearm Accessories"),
	/**
	 * Fuel
	 */
	_64("64", "Fuel"),
	/**
	 * Identity Documents
	 */
	_65("65", "Identity Documents"),
	/**
	 * Identity–Intangible
	 */
	_66("66", "Identity–Intangible"),
	/**
	 * Law Enforcement Equipment
	 */
	_67("67", "Law Enforcement Equipment"),
	/**
	 * Lawn/Yard/Garden Equipment
	 */
	_68("68", "Lawn/Yard/Garden Equipment"),
	/**
	 * Logging Equipment
	 */
	_69("69", "Logging Equipment"),
	/**
	 * Medical/Medical Lab Equipment
	 */
	_70("70", "Medical/Medical Lab Equipment"),
	/**
	 * Metals, Non-Precious
	 */
	_71("71", "Metals, Non-Precious"),
	/**
	 * Musical Instruments
	 */
	_72("72", "Musical Instruments"),
	/**
	 * Pets
	 */
	_73("73", "Pets"),
	/**
	 * Photographic/Optical Equipment
	 */
	_74("74", "Photographic/Optical Equipment"),
	/**
	 * Portable Electronic Communications
	 */
	_75("75", "Portable Electronic Communications"),
	/**
	 * Recreational/Sports Equipment
	 */
	_76("76", "Recreational/Sports Equipment"),
	/**
	 * Other
	 */
	_77("77", "Other"),
	/**
	 * Trailers
	 */
	_78("78", "Trailers"),
	/**
	 * Watercraft Equipment/Parts/Accessories
	 */
	_79("79", "Watercraft Equipment/Parts/Accessories"),
	/**
	 * Weapons–Other
	 */
	_80("80", "Weapons–Other"),
	/**
	 * Pending Inventory
	 */
	_88("88", "Pending Inventory"),
	/**
	 * (blank)–this data value is not currently used by the FBI
	 */
	_99("99", "(blank)–this data value is not currently used by the FBI");
	
	private PropertyDescriptionCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<PropertyDescriptionCode> asSet() {
		return EnumSet.allOf(PropertyDescriptionCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (PropertyDescriptionCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}
	
	public static final boolean isMotorVehicleCode(String code) {
		return Arrays.asList(_03.code, _05.code, 
				_24.code, _28.code, _37.code ).contains(code);
	}
	
	public static final boolean containsMotorVehicleCode(Collection<String> codes) {
		return codes.stream().anyMatch(code -> isMotorVehicleCode(code));
	}
	
	public static List<String> getIllogicalPropertyDescriptions(String offenseCode){
		offenseCode = StringUtils.trimToEmpty(offenseCode);
		
		if (!OffenseCode.codeSet().contains(offenseCode)){
			return new ArrayList<String>(PropertyDescriptionCode.codeSet()); 
		}
		
		switch (offenseCode) {
		case "220": case "240":
			return Arrays.asList(
					PropertyDescriptionCode._29.code,
					PropertyDescriptionCode._30.code,
					PropertyDescriptionCode._31.code, 
					PropertyDescriptionCode._32.code, 
					PropertyDescriptionCode._33.code, 
					PropertyDescriptionCode._34.code, 
					PropertyDescriptionCode._35.code 
					);
		case "23A": case "23B":
			return Arrays.asList(
					PropertyDescriptionCode._01.code,
					PropertyDescriptionCode._03.code,
					PropertyDescriptionCode._04.code, 
					PropertyDescriptionCode._05.code, 
					PropertyDescriptionCode._12.code, 
					PropertyDescriptionCode._15.code, 
					PropertyDescriptionCode._18.code, 
					PropertyDescriptionCode._24.code, 
					PropertyDescriptionCode._28.code,
					PropertyDescriptionCode._29.code, 
					PropertyDescriptionCode._30.code, 
					PropertyDescriptionCode._31.code, 
					PropertyDescriptionCode._32.code, 
					PropertyDescriptionCode._33.code, 
					PropertyDescriptionCode._34.code, 
					PropertyDescriptionCode._35.code, 
					PropertyDescriptionCode._37.code, 
					PropertyDescriptionCode._39.code, 
					PropertyDescriptionCode._66.code,
					PropertyDescriptionCode._78.code 
					);
		case "23C": 
			return Arrays.asList(
					PropertyDescriptionCode._01.code,
					PropertyDescriptionCode._03.code, 
					PropertyDescriptionCode._05.code,
					PropertyDescriptionCode._12.code,
					PropertyDescriptionCode._15.code,
					PropertyDescriptionCode._18.code,
					PropertyDescriptionCode._24.code,
					PropertyDescriptionCode._28.code,
					PropertyDescriptionCode._29.code,
					PropertyDescriptionCode._30.code,
					PropertyDescriptionCode._31.code,
					PropertyDescriptionCode._32.code,
					PropertyDescriptionCode._33.code,
					PropertyDescriptionCode._34.code,
					PropertyDescriptionCode._35.code,
					PropertyDescriptionCode._37.code,
					PropertyDescriptionCode._39.code
					);
		case "23D": case "23E": case "23F": case "23G": 
			return Arrays.asList(
					PropertyDescriptionCode._03.code, 
					PropertyDescriptionCode._05.code,
					PropertyDescriptionCode._24.code,
					PropertyDescriptionCode._28.code,
					PropertyDescriptionCode._29.code,
					PropertyDescriptionCode._30.code,
					PropertyDescriptionCode._31.code,
					PropertyDescriptionCode._32.code,
					PropertyDescriptionCode._33.code,
					PropertyDescriptionCode._34.code,
					PropertyDescriptionCode._35.code,
					PropertyDescriptionCode._37.code
					);
		case "23H": 
			return Arrays.asList(
					PropertyDescriptionCode._03.code, 
					PropertyDescriptionCode._05.code,
					PropertyDescriptionCode._24.code,
					PropertyDescriptionCode._28.code,
					PropertyDescriptionCode._37.code
					);
		default:
			return new ArrayList<String>();
		}
	}
	
}
