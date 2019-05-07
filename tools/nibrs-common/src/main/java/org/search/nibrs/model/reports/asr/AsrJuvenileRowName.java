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
package org.search.nibrs.model.reports.asr;
public enum AsrJuvenileRowName{
	MURDER_NONNEGLIGENT_MANSLAUGHTER("Murder and Nonnegligent Manslaughter                       01a"),  
	MANSLAUGHTER_BY_NEGLIGENCE("Manslaughter by Negligence                                            01b"),  
	RAPE("Rape                                                                                         02"), 
	ROBBERY("ROBBERY                                                                                 03"), 
	AGGRAVATED_ASSAULT("Aggravated Assault (Return A - 4a-d)                                04"), 
	BURGLARY("Burglary - Breaking or Entering                                          05"), 
	LARCENY_THEFT("Larceny - Theft (Except Motor Vehicle Theft)                  06"),
	MOTOR_VEHICLE_THEFT("Motor Vehicle Theft                                                             07"), 
	OTHER_ASSAULTS("Other Assaults(Return A - 4e)                                             08"), 
	ARSON("Arson                                                                                       09"), 
	FORGERY_AND_COUNTERFEITING("Forgery and Counterfeiting                                                 10"), 
	FRAUD("Fraud                                                                                       11"),
	EMBEZZLEMENT("Embezzlement                                                                       12"),
	STOLEN_PROPERTY_BUYING_RECEIVING_POSSESSING("Stolen Property; Buying Receiving, Possessing                13"),
	VANDALISM("Vandalism                                                                               14"),
	WEAPONS_CARRYING_POSSESSING_ETC("Weapons; Carrying,\nPossessing, etc.                                                                      15"),
	PROSTITUTION_AND_COMMERCIALIZED_VICE("Prostitution and\nCommercialized Vice\t\t\t\t\t\t\t\t\t\t\t\t\t       Total                               16"),
	PROSTITUTION("Prostitution                                                                              a"),
	ASSISTING_PROMOTING_PROSTITUTION("Assisting or Promoting Prostitution                                     b"),
	PURCHASING_PROSTITUTION("Purchasing Prostitution                                                          c"),
	SEX_OFFENSES("Sex Offenses (Except Forcible \nRape and Prostitution)                                                         17"),
	DRUG_ABUSE_VIOLATIONS_GRAND_TOTAL("Drug Abuse Violations\n\t\t\t\t\t\t\t\t\t\t\t\t\t                           Grand Total                                    18"),
	DRUG_SALE_MANUFACTURING_SUBTOTAL("(1) Sale/Manufacturing\n\t\t\t\t\t\t\t\t\t\t\t                           Subtotal                                          180"),
	DRUG_SALE_MANUFACTURING_OPIUM_COCAINE_DERIVATIVES("Opium or Cocaine and Their Derivatives\n(Morphine, Heroin, Codeine)                                                a"),
	DRUG_SALE_MANUFACTURING_MARIJUANA("Marijuana                                                                                 b"),
	DRUG_SALE_MANUFACTURING_SYNTHETIC_NARCOTICS("Synthetic Narcotics - Manufactured\n Narcotics Which Can Cause True Drug\n Addiction Demerol,                                                                c"),
	DRUG_SALE_MANUFACTURING_OTHER("Other - Dangerous Nonnarcotic Drugs\n(Barbiturates, Benzedrine),                                                   d"),
	DRUG_POSSESSION_SUBTOTAL("(2) Possession\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSubtotal                                        185"),
	DRUG_POSSESSION_OPIUM_COCAINE_DERIVATIVES("Opium or Cocaine and Their Derivatives\n(Morphine, Heroin, Codeine)                                                e"),
	DRUG_POSSESSION_MARIJUANA("Marijuana                                                                                  f"),
	DRUG_POSSESSION_SYNTHETIC_NARCOTICS("Synthetic Narcotics - Manufactured\n Narcotics Which Can Cause True Drug\n Addiction Demerol,                                                                g"),
	DRUG_POSSESSION_OTHER("Other - Dangerous Nonnarcotic Drugs\n(Barbiturates, Benzedrine)                                                    h"),
	GAMBLING_TOTAL("Gambling\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t                            Total                                          19"),
	GAMBLING_BOOKMAKING("Bookmaking (Horse and Sport Book)                                   a"),
	GAMBLING_NUMBERS_LOTTERY("Numbers and Lottery                                                              b"),
	GAMBLING_ALL_OTHER("All Other Gambling                                                                  c"),
	OFFENSES_AGAINST_FAMILY_AND_CHILDREN("Offenses Against the Family and Children                        20"),
	DRIVING_UNDER_THE_INFLUENCE("Driving Under the Influence                                                21"),
	LIQUOR_LAWS("Liquor Laws                                                                            22"),
	DRUNKENNESS("DRUNKENNESS                                                                      23"),
	DISORDERLY_CONDUCT("Disorderly Conduct                                                               24"),
	VAGRANCY("Vagrancy                                                                                 25"),
	ALL_OTHER_OFFENSES("All Other Offenses (Except Traffic)                                     26"),
	SUSPICION("Suspicion                                                                                 27"),
	CURFEW_AND_LOITERING_LAW_VIOLATIONS("Curfew and Loitering Law Violations                                 28"),
	RUNAWAYS("Runaways                                                                                29"),
	HUMAN_TRAFFICKING_COMMERCIAL_SEX_ACTS("Human Trafficking/Commercial Sex Acts                          30"),
	HUMAN_TRAFFICKING_INVOLUNTARY_SERVITUDE("Human Trafficking/Involuntary Servitude                         31"),
	TOTAL("\t\t\t\t\t\t\t\t\tTotal"),
	;
	
	private String label;
	
	private AsrJuvenileRowName(String label){
		this.setLabel(label);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}

