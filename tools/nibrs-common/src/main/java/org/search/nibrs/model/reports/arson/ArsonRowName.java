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
package org.search.nibrs.model.reports.arson;
public enum ArsonRowName{
	SINGLE_OCCUPANCY_RESIDENTIAL("A. Single Occupancy Residential:\n\t\t\tHouses, Townhouses, Duplexes, etc."),  
	OTHER_RESIDENTIAL("B. Other Residential:\n\t\t\tApartments, Tenements, Flats, Hotels, Motels, Inns,\n\t\t\tDormitories, Boarding Houses, etc."), 
	STORAGE("C. Storage:\n\t\t\tBarns, Garages, Warehouses, etc."), 
	INDUSTRIAL_MANUFACTURING ("D. Industrial/Manufacturing"), 
	OTHER_COMMERCIAL("E. Other Commercial:\n\t\t\tStores, Restaurants, Offices, etc."), 
	COMMUNITY_PUBLIC("F. Community/Public:\n\t\t\tChurches, Jails, Schools, Colleges, Hospitals, etc."), 
	ALL_OTHER_STRUCTURE("G. All Other Structure:\n\t\t\tOut Buildings, Monuments, Buildings Under\n\t\t\tConstruction, etc."), 
	TOTAL_STRUCTURE("TOTAL STRUCTURE"), 
	MOTOR_VEHICLES("H. Motor Vehicles:\n\t\t\tAutomobiles, Trucks, Buses, Motorcycles, etc.:\nt\t\t\tUCR Definition"), 
	OTHER_MOBILE_PROPERTY("I. Other Mobile Property:\n\t\t\tTrailers, Recreational Vehicles, Airplanes, Boats, etc."), 
	TOTAL_MOBILE("TOTAL MOBILE"), 
	TOTAL_OTHER("\t\t\t\tJ. TOTAL OTHER\n\t\t\t\t\t\t\tCrops, Timber, Fences, Signs, etc."), 
	GRAND_TOTAL("GRAND TOTAL"); 
	
	private String label;
	
	
	private ArsonRowName(String label){
		
		this.setLabel(label);
		
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}

