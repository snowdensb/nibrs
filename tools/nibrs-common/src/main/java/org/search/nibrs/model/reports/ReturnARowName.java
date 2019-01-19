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
package org.search.nibrs.model.reports;
public enum ReturnARowName{
	MURDER_NONNEGLIGENT_HOMICIDE("1. CRIMINAL HOMICIDE \n\t\t\t\ta. Murder/Nonnegligent Homicide", "11"),  
	MANSLAUGHTER_BY_NEGLIGENCE("\t\t\t\tb. Manslaughter by Negligence","12"), 
	FORCIBLE_RAPE_TOTAL("2. FORCIBLE RAPE TOTAL", "20"), 
	RAPE_BY_FORCE("\t\t\t\ta. Rape by Force", "21"), 
	ATTEMPTS_TO_COMMIT_FORCIBLE_RAPE("\t\t\t\tb. Attempts to commit Forcible Rape", "22"), 
	ROBBERY_TOTAL("3. ROBBERY TOTAL", "30"), 
	FIREARM_ROBBERY("\t\t\t\ta. Firearm", "31"), 
	KNIFE_CUTTING_INSTRUMENT_ROBBERY("\t\t\t\tb. Knife and Cutting Instrument", "32"), 
	OTHER_DANGEROUS_WEAPON_ROBBERY("\t\t\t\tc. Other Dangerous Weapon", "33"), 
	STRONG_ARM_ROBBERY("\t\t\t\td. Hands, Fists, Feet, etc. - Aggravated Injury", "34"), 
	ASSAULT_TOTAL("4. ASSAULT TOTAL", "40" ), 
	FIREARM_ASSAULT("\t\t\t\ta. Firearm", "41"), 
	KNIFE_CUTTING_INSTRUMENT_ASSAULT("\t\t\t\tb. Knife and Cutting Instrument", "42"), 
	OTHER_DANGEROUS_WEAPON_ASSAULT("\t\t\t\tc. Other Dangerous Weapon", "43"), 
	HANDS_FISTS_FEET_AGGRAVATED_INJURY_ASSAULT("\t\t\t\td. Hands, Fists, Feet, etc. - Aggravated Injury", "44"), 
	OTHER_ASSAULT_NOT_AGGRAVATED("\t\t\t\td. Other Assaults - Simple, Not Aggravated", "44"), 
	BURGLARY_TOTAL("5. BURGLARY TOTAL", "50"), 
	FORCIBLE_ENTRY_BURGLARY("\t\t\t\ta. Forcible Entry", "51"),
	UNLAWFUL_ENTRY_NO_FORCE_BURGLARY("\t\t\t\tb. Unlawful Entry - No Force", "52"),
	ATTEMPTED_FORCIBLE_ENTRY_BURGLARY("\t\t\t\tc. Attempted Forcible Entry", "53"),
	/**
	 * Larceny-Theft Total (Except Motor Vehicle Theft) 
	 */
	LARCENY_THEFT_TOTAL("6. LARCENCY - THEFT TOTAL \n\t\t\t\t(Except Motor Vehicle Theft)", "60"),
	MOTOR_VEHICLE_THEFT_TOTAL("7. MOTOR VEHICLE THEFT TOTAL", "70"), 
	AUTOS_THEFT("\t\t\t\ta. Autos", "71"), 
	TRUCKS_BUSES_THEFT("\t\t\t\tb.Trucks and Buses", "72"), 
	OTHER_VEHICLES_THEFT("\t\t\t\tc. Other Vehicles", "73"), 
	GRAND_TOTAL("\t\t\t\t\t\t\t\tGRAND TOTAL", "77");
	
	private String label;
	
	private String dataEntry;
	
	private ReturnARowName(String label, String dataEntry){
		
		this.setLabel(label);
		
		this.setDataEntry(dataEntry);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDataEntry() {
		return dataEntry;
	}

	public void setDataEntry(String dataEntry) {
		this.dataEntry = dataEntry;
	}
}

