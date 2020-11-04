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
public enum ReturnARecordCardRowName{
	GRAND_TOTAL(""),
	VIOLENT_TOTAL("Total"),  
	MURDER_SUBTOTAL("Subtotal"), 
	MURDER_MURDER("Murder"), 
	RAPE_SUBTOTAL("Subtotal"), 
	RAPE_BY_FORCE("Rape"), 
	ATTEMPTS_TO_COMMIT_FORCIBLE_RAPE("Attempted Rape"), 
	ROBBERY_SUBTOTAL("Subtotal"), 
	FIREARM_ROBBERY("Robbery - Firarm"), 
	KNIFE_CUTTING_INSTRUMENT_ROBBERY("Robbery - Knife or Cutting Instrument"), 
	OTHER_DANGEROUS_WEAPON_ROBBERY("Robbery - Other Dangerous Weapon"), 
	STRONG_ARM_ROBBERY("Robbery - Hands, Fists, Feet"), 
	ASSAULT_SUBTOTAL("Subtotal"), 
	FIREARM_ASSAULT("Assault - Firearm"), 
	KNIFE_CUTTING_INSTRUMENT_ASSAULT("Assault -  Knife or Cutting Instrument"), 
	OTHER_DANGEROUS_WEAPON_ASSAULT("Assault -  Other Dangerous Weapon"), 
	HANDS_FISTS_FEET_AGGRAVATED_INJURY_ASSAULT("Assault - Hands, Fists, Feet"), 
	PROPERTY_TOTAL("Total"), 
	BURGLARY_SUBTOTAL("Subtotal"), 
	FORCIBLE_ENTRY_BURGLARY("Burglary - Forcible Entry"),
	UNLAWFUL_ENTRY_NO_FORCE_BURGLARY("Burglary - No Force"),
	ATTEMPTED_FORCIBLE_ENTRY_BURGLARY("Burglary - Attempted Forcible Entry"),
	/**
	 * Larceny-Theft Total (Except Motor Vehicle Theft) 
	 */
	LARCENY_THEFT_SUBTOTAL("Subtotal"),
	LARCENY_THEFT("LARCENCY - THEFT"),
	MOTOR_VEHICLE_THEFT_SUBTOTAL("Subtotal"), 
	AUTOS_THEFT("Auto Theft"), 
	TRUCKS_BUSES_THEFT("Truck and Bus Theft"), 
	OTHER_VEHICLES_THEFT("Other Vehicle Theft"),
	OTHER_ASSAULT_NOT_AGGRAVATED("Simple Assault");
	
	private String label;
	
	private ReturnARecordCardRowName(String label){
		this.setLabel(label);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}

