/*
 * Copyright 2016 Research Triangle Institute
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
package org.search.nibrs.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.ParsedObject;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.TypeOfVictimCode;

final class VictimRuleViolationExemplarFactory {

	private static final VictimRuleViolationExemplarFactory INSTANCE = new VictimRuleViolationExemplarFactory();

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(VictimRuleViolationExemplarFactory.class);

	private Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> groupATweakerMap;

	private VictimRuleViolationExemplarFactory() {
		groupATweakerMap = new HashMap<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>>();
		populateGroupAExemplarMap();
	}

	/**
	 * Get an instance of the factory.
	 * 
	 * @return the instance
	 */
	public static final VictimRuleViolationExemplarFactory getInstance() {
		return INSTANCE;
	}

	Map<Integer, Function<GroupAIncidentReport, List<GroupAIncidentReport>>> getGroupATweakerMap() {
		return groupATweakerMap;
	}

	private void populateGroupAExemplarMap() {
		
		groupATweakerMap.put(80, incident -> {
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.removeVictims();
			VictimSegment v1 = new VictimSegment();
			v1.setTypeOfVictim(TypeOfVictimCode.S.code);
			v1.setUcrOffenseCodeConnection(0, OffenseCode._720.code);
			copy.addVictim(v1);
			VictimSegment v2 = new VictimSegment();
			v2.setTypeOfVictim(TypeOfVictimCode.S.code);
			v2.setUcrOffenseCodeConnection(0, OffenseCode._40A.code);
			copy.addVictim(v2);
			
			copy.removeOffenses();
			OffenseSegment os1 = new OffenseSegment();
			os1.setUcrOffenseCode(OffenseCode._720.code);
			copy.addOffense(os1);

			incidents.add(copy);
			
			copy = new GroupAIncidentReport(copy);
			OffenseSegment os2 = new OffenseSegment();
			os2.setUcrOffenseCode(OffenseCode._40A.code);
			incidents.add(copy);
			
			return incidents;

		});
		
		groupATweakerMap.put(70, incident -> {
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setOffenderNumberRelated(0, new ParsedObject<>(2));
			incidents.add(copy);
			
			return incidents;

		});
		
		groupATweakerMap.put(401, incident -> {
			
			// The referenced data element in a Group A Incident AbstractReport
			// Segment 4 is mandatory & must be present.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimSequenceNumber(ParsedObject.getMissingParsedObject());
			incidents.add(copy);

			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, null);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfVictim(null);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimSequenceNumber(new ParsedObject<>(0));
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			VictimSegment victim = copy.getVictims().get(0);
			victim.setUcrOffenseCodeConnection(0, "100");
			victim.setTypeOfInjury(0, null);
			incidents.add(copy);
									
			return incidents;
			
		});

		groupATweakerMap.put(404, incident -> {
			
			//The referenced data element in a Group A Incident Report 
			//must be populated with a valid data value and cannot be blank.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			VictimSegment victim = copy.getVictims().get(0);
			victim.setSex(null);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			victim = copy.getVictims().get(0);
			victim.setRace(null);
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			victim = copy.getVictims().get(0);
			victim.setEthnicity("invalid");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			victim = copy.getVictims().get(0);
			victim.setResidentStatus("invalid");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfVictim("Z");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfVictim(" ");
			incidents.add(copy);
			
			
			copy = new GroupAIncidentReport(incident);
			victim = copy.getVictims().get(0);
			victim.setTypeOfInjury(0, "invalid");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			victim = copy.getVictims().get(0);
			victim.setVictimOffenderRelationship(0, "invalid");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			victim = copy.getVictims().get(0);
			victim.setOffenderNumberRelated(0, new ParsedObject<>(2));
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "999");
			incidents.add(copy);
			
			return incidents;
			
		});
		
		
		groupATweakerMap.put(406, incident -> {
			
			//(Victim Connected to UCR Offense Code) The referenced data element in 
			//error is one that contains multiple data values. When more than one code is 
			//entered, none can be duplicate codes.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenseSegment offense = new OffenseSegment();
			offense.setUcrOffenseCode("120");
			offense.setTypeOfCriminalActivity(1, "J");
			offense.setOffenseAttemptedCompleted("C");
			offense.setTypeOfWeaponForceInvolved(1, "99");
			offense.setOffendersSuspectedOfUsing(1, "N");
			offense.setBiasMotivation(1, "15");
			offense.setLocationType("15");
			offense.setNumberOfPremisesEntered(ParsedObject.getMissingParsedObject());
			offense.setAutomaticWeaponIndicator(0, " ");
			copy.addOffense(offense);
			copy.getVictims().get(0).setUcrOffenseCodeConnection(1, "13A");
			
			//(Aggravated Assault/Homicide Circumstances The referenced data element 
			//in error is one that contains multiple data values. When more than one 
			//code is entered, none can be duplicate codes.
			
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "02");
			copy2.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(1, "02");
			
			//(Type Injury) The referenced data element in error is one that 
			//contains multiple data values. When more than one code is entered, none can be duplicate codes.
			
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getVictims().get(0).setTypeOfInjury(0, "B");
			copy3.getVictims().get(0).setTypeOfInjury(1, "B");
			
			//(Offender Number to be Related) The referenced data element in error 
			//is one that contains multiple data values. When more than one code 
			//is entered, none can be duplicate codes.
			
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getVictims().get(0).setOffenderNumberRelated(1, new ParsedObject<>(1));
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
					
			return incidents;
				
			
		});
		
		groupATweakerMap.put(407, incident -> {
			
			// (Type Injury) Can have multiple data values and was entered with 
			// multiple values. However, the entry shown between the brackets in [value] 
			// above cannot be entered with any other data value.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfInjury(1, "B");
			incidents.add(copy);
			
			return incidents;
				
		});
		
		groupATweakerMap.put(410, incident -> {
			//(Age of Victim) was entered as an age-range. Accordingly, the first age 
			//component must be less than the second age.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAge(NIBRSAge.getAge(30, 25));
			incidents.add(copy);
			return incidents;
		});
				
		groupATweakerMap.put(419, incident -> {
			//Data Element 31 (Aggravated Assault/Homicide Circumstances) can only be entered 
			//when one or more of the offenses in Data Element 24 (Victim Connected to UCR Offense Code) are:
			//09A=Murder and Non-negligent Manslaughter
			//09B=Negligent Manslaughter
			//09C=Justifiable Homicide
			//13A=Aggravated Assault
			//Data Element 33 (Type Injury) can only be entered when one or more of the offenses in Data Element 24 (Victim Connected to UCR Offense Code) are:
			//100=Kidnapping/Abduction
			//11A=Rape
			//11B=Sodomy
			//11C=Sexual Assault With An Object
			//11D=Fondling
			//120=Robbery
			//13A=Aggravated Assault
			//13B=Simple Assault
			//210=Extortion/Blackmail
			//64A=Human Trafficking, Commercial Sex Acts
			//64B=Human Trafficking, Involuntary Servitude
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("200");
			VictimSegment victimSegment = copy.getVictims().get(0);
			victimSegment.setUcrOffenseCodeConnection(0, "200");
			victimSegment.setAggravatedAssaultHomicideCircumstances(0, "01");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("200");
			victimSegment = copy.getVictims().get(0);
			victimSegment.setUcrOffenseCodeConnection(0, "200");
			victimSegment.setTypeOfInjury(0, "M");
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(449, incident -> {
			//This is a warning message. Data Element 26 (Age of Victim) cannot be less than 18 years old when 
			//Data Element 35 (Relationship of Victim to Offender) contains a relationship of SE = Spouse.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAge(NIBRSAge.getAge(17, null));
			incidents.add(copy);
				
			return incidents;
				
		});	
		
		groupATweakerMap.put(450, incident -> {
			//(Age of Victim) contains a relationship of SE=Spouse. When this is so, the
			//age of the victim cannot be less than 10 years.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAge(NIBRSAge.getAge(9, null));
			incidents.add(copy);
				
			return incidents;
				
		});
		
		groupATweakerMap.put(451, incident -> {
			//When a Group A Incident Report is submitted, the individual segments
			//comprising the incident cannot contain duplicates. In this case,
			//two victim segments were submitted having the same entry in Data Element 23 (Victim Sequence Number).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			VictimSegment victim2 = new VictimSegment ();
			victim2.setTypeOfVictim("I");
			victim2.setTypeOfInjury(0, "M");
			victim2.setVictimSequenceNumber(new ParsedObject<>(1));
			victim2.setAge(NIBRSAge.getAge(20, 22));
			victim2.setEthnicity("N");
			victim2.setResidentStatus("R");
			victim2.setSex("F");
			victim2.setRace("B");
			victim2.setOffenderNumberRelated(0, new ParsedObject<>(1));
			victim2.setVictimOffenderRelationship(0, "AQ");
			victim2.setUcrOffenseCodeConnection(0, "13A");
			
			copy.addVictim(victim2);
			incidents.add(copy);
				
			return incidents;
				
		});
		
		
		
		groupATweakerMap.put(453, incident -> {
			//(Age of Victim) The Data Element associated with this error must be 
			//present when Data Element 25 (Type of Victim) is I=Individual.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAge(null);
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getVictims().get(0).setSex(null);			
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getVictims().get(0).setRace(null);	
			
			
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			
			return incidents;
		
		});		
		
		groupATweakerMap.put(454, incident -> {
			//(Type of Officer Activity/Circumstance), Data Element 25B (Officer Assignment Type), 
			//Data Element 26 (Age of Victim), Data Element 27 (Sex of Victim), and 
			//Data Element 28 (Race of Victim) must be entered when 
			//Data Element 25 (Type of Victim) is L=Law Enforcement Officer.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			//Type of Officer Activity/Circumstance is null
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfOfficerActivityCircumstance(null);
			copy.getVictims().get(0).setOfficerAssignmentType("K");
			copy.getVictims().get(0).setTypeOfVictim("L");
			//Officer Assignment Type is null
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getVictims().get(0).setTypeOfOfficerActivityCircumstance("01");
			copy2.getVictims().get(0).setOfficerAssignmentType(null);
			copy2.getVictims().get(0).setTypeOfVictim("L");
			//Age is null
			GroupAIncidentReport copy3 = new GroupAIncidentReport(incident);
			copy3.getVictims().get(0).setTypeOfOfficerActivityCircumstance("01");
			copy3.getVictims().get(0).setOfficerAssignmentType("K");
			copy3.getVictims().get(0).setAge(null);
			copy3.getVictims().get(0).setTypeOfVictim("L");
			//Sex is null
			GroupAIncidentReport copy4 = new GroupAIncidentReport(incident);
			copy4.getVictims().get(0).setTypeOfOfficerActivityCircumstance("01");
			copy4.getVictims().get(0).setOfficerAssignmentType("K");
			copy4.getVictims().get(0).setSex(null);
			copy4.getVictims().get(0).setTypeOfVictim("L");
			//Race is null
			GroupAIncidentReport copy5 = new GroupAIncidentReport(incident);
			copy5.getVictims().get(0).setTypeOfOfficerActivityCircumstance("01");
			copy5.getVictims().get(0).setOfficerAssignmentType("K");
			copy5.getVictims().get(0).setRace(null);
			copy5.getVictims().get(0).setTypeOfVictim("L");
				
					
			incidents.add(copy);
			incidents.add(copy2);
			incidents.add(copy3);
			incidents.add(copy4);
			incidents.add(copy5);
			
			return incidents;
			
			
		});
		
		groupATweakerMap.put(455, incident -> {
			//Aggravated Assault Homicide Circumstances contains: 20=Criminal Killed by Private Citizen
			//Or 21=Criminal Killed by Police Officer, but Data Element 32 (Additional Justifiable Homicide Circumstances) was not entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "20");
			copy.getVictims().get(0).setAdditionalJustifiableHomicideCircumstances(null);
						
			incidents.add(copy);
			
			return incidents;
			
		});
		
		
		
		groupATweakerMap.put(456, incident -> {
			//(Aggravated Assault/Homicide Circumstances) was entered with two entries, 
			//but was rejected for one of the following reasons:
			//1) Value 10=Unknown Circumstances is mutually exclusive with any other value. 
			//2) More than one category (i.e., Aggravated Assault, Negligent Manslaughter, etc.) was entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "01");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(1, "10");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "01");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(1, "20");
			incidents.add(copy);

			return incidents;
			
		});
		
		groupATweakerMap.put(457, incident -> {
			//Aggravated Assault Homicide Circumstances was entered, but Data Element 31 
			//Aggravated Assault/Homicide Circumstances) does not reflect a justifiable homicide circumstance.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "34");
			copy.getVictims().get(0).setAdditionalJustifiableHomicideCircumstances("C");
						
			incidents.add(copy);
					
			return incidents;
			
		});
		
		
		groupATweakerMap.put(458, incident -> {
			//The Data Element associated with this error cannot be entered 
			//when Data Element 25 (Type of Victim) is not I=Individual or 
			//L=Law Enforcement Officer when Data Element 24 (Victim Connected to 
			//UCR Offense Code) contains a Crime Against Person.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("200");
			copy.getVictims().get(0).setTypeOfVictim("B");
			copy.getVictims().get(0).setTypeOfInjury(0, "B");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("200");
			copy.getVictims().get(0).setTypeOfVictim("B");
			copy.getVictims().get(0).setSex("M");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("200");
			copy.getVictims().get(0).setTypeOfVictim("B");
			copy.getVictims().get(0).setRace("B");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("200");
			copy.getVictims().get(0).setTypeOfVictim("B");
			copy.getVictims().get(0).setAge(NIBRSAge.getNewbornAge());
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(459, incident -> {
			
			//The Data Element associated with this error cannot be entered 
			//when Data Element 25 (Type of Victim) is not I=Individual or 
			//L=Law Enforcement Officer when Data Element 24 (Victim Connected to 
			//UCR Offense Code) contains a Crime Against Person.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("220");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "220");
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(460, incident -> {
			//Corresponding Data Element 35 (Relationship of Victim to Offenders) 
			//data must be entered when Data Element 34 (Offender Numbers To Be Related) 
			//is entered with a value greater than 00.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, null);
			
			incidents.add(copy);
			
			return incidents;
			
		});
		
		
		groupATweakerMap.put(461, incident -> {
			//(Type of Victim) cannot have a value of S=Society/Public when the 
			//offense is 220=Burglary/Breaking and Entering.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("220");
			copy.getVictims().get(0).setTypeOfVictim("S");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "220");
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(462, incident -> {
			//(Aggravated Assault/Homicide Circumstances) An Offense Segment (Level 2) 
			//was submitted for 13A=Aggravated Assault. Accordingly, Data Element 31 
			//(Aggravated Assault/Homicide Circumstances) can only have codes of 01 through 06 and 08 through 10. 
			//All other codes, including 07=Mercy Killing, are not valid because they do not relate to an aggravated assault
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "30");
			
			incidents.add(copy);
					
			return incidents;
			
		});
		
		groupATweakerMap.put(463, incident -> {
			//(Aggravated Assault/Homicide Circumstances) When a Justifiable Homicide 
			//is reported, Data Element 31 (Aggravated Assault/Homicide Circumstances) 
			//can only have codes of 20=Criminal Killed by Private Citizen or 
			//21=Criminal Killed by Police Officer. In this case, a code other than the two mentioned was entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "30");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "09C");
			incidents.add(copy);
			
			return incidents;
			
		});
		
		
		groupATweakerMap.put(464, incident -> {
			//UCR Code contains a Crime Against Person, but Data Element 25 
			//(Type of Victim) is not I=Individual or L=Law Enforcement Officer when Data Element 24 
			//(Victim Connected to UCR Offense Code) contains a Crime Against Person.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfVictim("B");
			
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(465, incident -> {
			//UCR Code contains a Crime Against Society, but Data Element 25 
			//(Type of Victim) is not S=Society.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("720");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "720");
			copy.getVictims().get(0).setTypeOfVictim("B");
			
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(466, incident -> {
			//Each UCR Offense Code entered into Data Element 24 (Victim Connected to UCR Offense Codes)
			//must have the Offense Segment for the value. In this case, 
			//the victim was connected to offenses that were not submitted as Offense Segments. 
			//A victim cannot be connected to an offense when the offense itself is not present.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "720");
						
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(467, incident -> {
			//UCR code contains a Crime Against Property, but Data Element 25 
			//(Type of Victim) is S=Society. This is not an allowable code for Crime Against Property offenses.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("200");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "200");
			copy.getVictims().get(0).setTypeOfVictim("S");
			
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(468, incident -> {
			//Relationship of Victim to Offender) cannot be entered when Data Element 34 
			//(Offender Number to be Related) is zero. Zero means that the number of 
			//offenders is unknown; therefore, the relationship cannot be entered.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setOffenderNumberRelated(0, new ParsedObject<>(0));
			
					
			incidents.add(copy);
						
			return incidents;
			
		});
		
		groupATweakerMap.put(469, incident -> {
			//Data Element 26 (Age of Victim) should be under 18 when Data Element 24
			//(Victim Connected to UCR Offense Code) is 36B=Statutory Rape.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("36B");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "36B");
			copy.getVictims().get(0).setSex("U");
			GroupAIncidentReport copy2 = new GroupAIncidentReport(incident);
			copy2.getOffenses().get(0).setUcrOffenseCode("11A");
			copy2.getVictims().get(0).setUcrOffenseCodeConnection(0, "11A");
			copy2.getVictims().get(0).setSex("U");
			
			incidents.add(copy);
			incidents.add(copy2);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(470, incident -> {
			//When �VO� Relationship is present, must have two or more victims and offenders
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "VO");
			
			incidents.add(copy);
						
			return incidents;	
			
		});
		
		groupATweakerMap.put(471, incident -> {
			
			//(Offender Number to be Related) has relationships of VO=Victim Was Offender 
			//that point to multiple offenders, which is an impossible situation. 
			//A single victim cannot be two offenders.
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenderSegment os = new OffenderSegment();
			os.setOffenderSequenceNumber(new ParsedObject<>(2));
			copy.addOffender(os);
			copy.getVictims().get(0).setOffenderNumberRelated(0, new ParsedObject<>(1));
			copy.getVictims().get(0).setOffenderNumberRelated(1, new ParsedObject<>(2));
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "VO");
			copy.getVictims().get(0).setVictimOffenderRelationship(1, "VO");
			incidents.add(copy);
			
			return incidents;
			
		});
		
		groupATweakerMap.put(472, incident -> {
			//(Relationship of Victim to Offender) has a relationship to the offender
			//that is not logical. In this case, the offender was entered with unknown 
			//values for age, sex, and race. Under these circumstances, the relationship 
			//must be entered as RU=Relationship Unknown.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenders().get(0).setAge(NIBRSAge.getUnknownAge());
			copy.getOffenders().get(0).setSex("U");
			copy.getOffenders().get(0).setRace("U");
			incidents.add(copy);
						
			return incidents;
			
		});
		
		groupATweakerMap.put(474, incident -> {
			//(Victim Segment) Cannot be submitted multiple times with VO=Victim 
			//Was Offender in Data Element 35 (Relationship of Victim to Offender)
			//when Data Element 34 (Offender Number to be Related) contains the same data value 
			//indicating the same offender).
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "VO");
			VictimSegment victim2 = new VictimSegment();
			victim2.setVictimOffenderRelationship(0, "VO");
			victim2.setOffenderNumberRelated(0, new ParsedObject<>(1));
			copy.addVictim(victim2);
			incidents.add(copy);
			
			return incidents;
			
		});
			
			
		
		groupATweakerMap.put(475, incident -> {
			//(Offender Number to be Related) A victim can only have one
			//spousal relationship. In this instance, the victim has a relationship of 
			//SE=Spouse to two or more offenders.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			OffenderSegment os = new OffenderSegment();
			os.setOffenderSequenceNumber(new ParsedObject<>(2));
			copy.addOffender(os);
			copy.getVictims().get(0).setOffenderNumberRelated(0, new ParsedObject<>(1));
			copy.getVictims().get(0).setOffenderNumberRelated(1, new ParsedObject<>(2));
			copy.getVictims().get(0).setVictimOffenderRelationship(0, "SE");
			copy.getVictims().get(0).setVictimOffenderRelationship(1, "SE");
			incidents.add(copy);
			
			return incidents;
			
		});
		
		
		groupATweakerMap.put(476, incident -> {
			//An offender can only have one spousal relationship. In this instance,
			//two or more victims have a relationship of SE=Spouse to the same offender.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			VictimSegment victim2 = new VictimSegment();
			victim2.setVictimOffenderRelationship(0, "SE");
			victim2.setOffenderNumberRelated(0, new ParsedObject<>(1));
			copy.addVictim(victim2);
			incidents.add(copy);
			
			return incidents;
			
		});
		groupATweakerMap.put(477, incident -> {
			//(Aggravated Assault/Homicide Circumstances) A victim segment was 
			//submitted with Data Element 24 (Victim Connected to UCR Offense Code) 
			//having an offense that does not have a permitted code for 
			//Data Element 31 (Aggravated Assault/Homicide Circumstances). 
			//Only those circumstances listed in Volume 1, section VI, are valid for the particular offense.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("09C");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "01");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "09C");
			incidents.add(copy);
						
			return incidents;
			
		});
		
		
		groupATweakerMap.put(478, incident -> {
		//Data Element 24 (Victim Connected to UCR Offense Code) 
		//Mutually Exclusive offenses are ones that cannot occur to the same 
		//victim by UCR definitions. A Lesser Included offense is one that 
		//is an element of another offense and should not be reported as having 
		//happened to the victim along with the other offense. 
		//Lesser Included and Mutually Exclusive offenses are defined as follows:
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13A");
			OffenseSegment os = new OffenseSegment();
			os.setUcrOffenseCode("09A");
			copy.addOffense(os);
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "01");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "13A");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(1, "09A");
			incidents.add(copy);
						
			return incidents;
			
		});
		
		groupATweakerMap.put(479, incident -> {
			//A Simple Assault (13B) was committed against a victim, but the 
			//victim had major injuries/trauma entered for Data Element 33 (Type Injury). 
			//Either the offense should have been classified as an Aggravated Assault (13A) 
			//or the victims injury should not have been entered as major.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13B");
			copy.getVictims().get(0).setTypeOfInjury(0, "M");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "13B");
			incidents.add(copy);
						
			return incidents;
			
		});
		
		groupATweakerMap.put(480, incident -> {
			//Data Element 31 (Aggravated Assault/Homicide Circumstances) has 
			//08=Other Felony Involved but the incident has only one offense. 
			//For this code to be used, there must be an Other Felony. 
			//Either multiple entries for Data Element 6 (UCR Offense Code)
			//should have been submitted, or multiple individual victims should
			//have been submitted for the incident report.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("13A");
			copy.getVictims().get(0).setTypeOfInjury(0, "O");
			copy.getVictims().get(0).setAggravatedAssaultHomicideCircumstances(0, "08");
			incidents.add(copy);
						
			return incidents;
			
		});
		groupATweakerMap.put(481, incident -> {
			//Data Element 26 (Age of Victim) should be under 18 when Data Element 24
			//(Victim Connected to UCR Offense Code) is 36B=Statutory Rape.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("36B");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "36B");
			copy.getVictims().get(0).setAge(NIBRSAge.getAge(19, null));
			
			incidents.add(copy);
			
			return incidents;
					
		});
		
		groupATweakerMap.put(482, incident -> {
			//(Type of Victim) cannot be L=Law Enforcement Officer unless Data Element 24 
			//(Victim Connected to UCR Offense Code) is one of the following:
			//      09A=Murder & Non-negligent Manslaughter
			//		13A=Aggravated Assault
			//		13B=Simple Assault
			//		13C=Intimidation
			
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getOffenses().get(0).setUcrOffenseCode("200");
			copy.getVictims().get(0).setUcrOffenseCodeConnection(0, "200");
			copy.getVictims().get(0).setTypeOfVictim("L");
			
			incidents.add(copy);
			
			return incidents;
					
		});
	
		groupATweakerMap.put(483, incident -> {
			//(Type of Officer Activity/Circumstance) Data Element 25B (Officer Assignment Type), 
			//Data Element 25C (OfficerORI Other Jurisdiction), Data Element 26 (Age of Victim), 
			//Data Element 27 (Sex of Victim), Data Element 28 (Race of Victim), 
			//Data Element 29 (Ethnicity of Victim), Data Element 30 (Resident Status of Victim), and 
			//Data Element 34 (Offender Number to be Related) can only be entered when 
			//Data Element 25 (Type of Victim) is I=Individual or L=Law Enforcement Officer.
			List<GroupAIncidentReport> incidents = new ArrayList<GroupAIncidentReport>();
			GroupAIncidentReport copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfVictim("B");
			copy.getVictims().get(0).setTypeOfOfficerActivityCircumstance("01");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfVictim("B");
			copy.getVictims().get(0).setOfficerAssignmentType("G");
			incidents.add(copy);
			
			copy = new GroupAIncidentReport(incident);
			copy.getVictims().get(0).setTypeOfVictim("B");
			copy.getVictims().get(0).setOfficerOtherJurisdictionORI("321456789");
			incidents.add(copy);
			
			return incidents;
					
		});
		
	}
}
