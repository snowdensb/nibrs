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
 * Enum for NIBRS location type codes.
 *
 */
public enum LocationTypeCode {
	
	_01("01","Air/Bus/Train Terminal"),
	_02("02","Bank/Savings and Loan"),
	_03("03","Bar/Nightclub"),
	_04("04","Church/Synagogue/Temple/Mosque"),
	_05("05","Commercial/Office Building"),
	_06("06","Construction Site"),
	_07("07","Convenience Store"),
	_08("08","Department/Discount Store"),
	_09("09","Drug Store/Doctor's Office/Hospital"),
	_10("10","Field/Woods"),
	_11("11","Government/Public Building"),
	_12("12","Grocery/Supermarket"),
	_13("13","Highway/Road/Alley/Street/Sidewalk"),
	_14("14","Hotel/Motel/Etc."),
	_15("15","Jail/Prison/Penitentiary/Corrections Facility"),
	_16("16","Lake/Waterway/Beach"),
	_17("17","Liquor Store"),
	_18("18","Parking/Drop Lot/Garage"),
	/**
	 * Rental Storage Facility
	 */
	_19("19","Rental Storage Facility"),
	/**
	 * Residence
	 */
	_20("20","Residence/Home"),
	_21("21","Restaurant"),
	_22("22","School/College"),
	_23("23","Service/Gas Station"),
	_24("24","Specialty Store"),
	_25("25","Other/Unknown"),
	_37("37","Abandoned/Condemned Structure"),
	_38("38","Amusement Park"),
	_39("39","Arena/Stadium/Fairgrounds/Coliseum"),
	_40("40","ATM Separate from Bank"),
	_41("41","Auto Dealership New/Used"),
	_42("42","Camp/Campground"),
	_44("44","Daycare Facility"),
	_45("45","Dock/Wharf/Freight/Modal Terminal"),
	_46("46","Farm Facility"),
	_47("47","Gambling Facility/Casino/Race Track"),
	_48("48","Industrial Site"),
	_49("49","Military Installation"),
	_50("50","Park/Playground"),
	_51("51","Rest Area"),
	_52("52","School-College/University"),
	_53("53","School-Elementary/Secondary"),
	_54("54","Shelter-Mission/Homeless"),
	_55("55","Shopping Mall"),
	_56("56","Tribal Lands"),
	_57("57","Community Center"),
	_58("58","Cyberspace")
	;
	
	public String code;
	public String description;

	private LocationTypeCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public static final Set<LocationTypeCode> asSet() {
		return EnumSet.allOf(LocationTypeCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (LocationTypeCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}
