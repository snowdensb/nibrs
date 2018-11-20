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

/*
 * When recreate the ddl from SQL Power,  don't forget to add ` AUTO_INCREMENT` to all the primary IDs.
 */
Drop schema if exists search_nibrs_staging;

CREATE schema search_nibrs_staging;
use search_nibrs_staging;


CREATE TABLE Submission (
                SubmissionID INT AUTO_INCREMENT NOT NULL,
                IncidentIdentifier VARCHAR(50) NOT NULL,
                RequestFilePath VARCHAR(300) NOT NULL,
                ResponseFilePath VARCHAR(300),
                AcceptedIndicator BOOLEAN DEFAULT false NOT NULL,
                ResponseTimestamp timestamp,
                FaultCode VARCHAR(100),
                FaultDescription VARCHAR(500),
                SubmissionTimestamp TIMESTAMP NOT NULL,
                PRIMARY KEY (SubmissionID)
);


CREATE TABLE Violation (
                ViolationID INT AUTO_INCREMENT NOT NULL,
                SubmissionID INT NOT NULL,
                ViolationCode VARCHAR(3) NOT NULL,
                ViolationLevel VARCHAR(1) NOT NULL,
                ViolationTimestamp TIMESTAMP NOT NULL,
                PRIMARY KEY (ViolationID)
);


CREATE TABLE CargoTheftIndicatorType (
                CargoTheftIndicatorTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(7) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(7) NOT NULL,
                PRIMARY KEY (CargoTheftIndicatorTypeID)
);


CREATE TABLE AgencyType (
                AgencyTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(2) NOT NULL,
                StateDescription VARCHAR(80) NOT NULL,
                NIBRSCode VARCHAR(2) NOT NULL,
                NIBRSDescription VARCHAR(80) NOT NULL,
                PRIMARY KEY (AgencyTypeID)
);


CREATE TABLE DateType (
                DateTypeID INT NOT NULL AUTO_INCREMENT,
                CalendarDate DATE NOT NULL,
                Year INT NOT NULL,
                YearLabel CHAR(4) NOT NULL,
                CalendarQuarter INT NOT NULL,
                Month INT NOT NULL,
                MonthName VARCHAR(12) NOT NULL,
                FullMonth CHAR(7) NOT NULL,
                Day INT NOT NULL,
                DayOfWeek VARCHAR(9) NOT NULL,
                DayOfWeekSort INT NOT NULL,
                DateMMDDYYYY CHAR(10) NOT NULL,
                PRIMARY KEY (DateTypeID)
);


CREATE TABLE Agency (
                AgencyID INT NOT NULL AUTO_INCREMENT,
                AgencyORI CHAR(9) NOT NULL,
                AgencyName VARCHAR(60) NOT NULL,
                AgencyTypeID INT NOT NULL,
                StateCode CHAR(2) NOT NULL,
                StateName VARCHAR(20) NOT NULL,
                Population INT,
                PRIMARY KEY (AgencyID)
);


CREATE TABLE OfficerAssignmentTypeType (
                OfficerAssignmentTypeTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(100) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(100) NOT NULL,
                PRIMARY KEY (OfficerAssignmentTypeTypeID)
);


CREATE TABLE OfficerActivityCircumstanceType (
                OfficerActivityCircumstanceTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(2) NOT NULL,
                StateDescription VARCHAR(75) NOT NULL,
                NIBRSCode VARCHAR(2) NOT NULL,
                NIBRSDescription VARCHAR(75) NOT NULL,
                PRIMARY KEY (OfficerActivityCircumstanceTypeID)
);


CREATE TABLE TypeOfWeaponForceInvolvedType (
                TypeOfWeaponForceInvolvedTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(2) NOT NULL,
                StateDescription VARCHAR(30) NOT NULL,
                NIBRSCode VARCHAR(2) NOT NULL,
                NIBRSDescription VARCHAR(30) NOT NULL,
                PRIMARY KEY (TypeOfWeaponForceInvolvedTypeID)
);


CREATE TABLE SuspectedDrugTypeType (
                SuspectedDrugTypeTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(32) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(32) NOT NULL,
                PRIMARY KEY (SuspectedDrugTypeTypeID)
);


CREATE TABLE BiasMotivationType (
                BiasMotivationTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(2) NOT NULL,
                StateDescription VARCHAR(60) NOT NULL,
                NIBRSCode VARCHAR(2) NOT NULL,
                NIBRSDescription VARCHAR(60) NOT NULL,
                BiasMotivationCategory VARCHAR(30) NOT NULL,
                PRIMARY KEY (BiasMotivationTypeID)
);


CREATE TABLE MethodOfEntryType (
                MethodOfEntryTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(10) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(10) NOT NULL,
                PRIMARY KEY (MethodOfEntryTypeID)
);


CREATE TABLE LocationTypeType (
                LocationTypeTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(2) NOT NULL,
                StateDescription VARCHAR(45) NOT NULL,
                NIBRSCode VARCHAR(2) NOT NULL,
                NIBRSDescription VARCHAR(45) NOT NULL,
                PRIMARY KEY (LocationTypeTypeID)
);


CREATE TABLE ClearedExceptionallyType (
                ClearedExceptionallyTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(220) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(220) NOT NULL,
                PRIMARY KEY (ClearedExceptionallyTypeID)
);


CREATE TABLE DispositionOfArresteeUnder18Type (
                DispositionOfArresteeUnder18TypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(30) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(30) NOT NULL,
                PRIMARY KEY (DispositionOfArresteeUnder18TypeID)
);


CREATE TABLE ArresteeWasArmedWithType (
                ArresteeWasArmedWithTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(2) NOT NULL,
                StateDescription VARCHAR(30) NOT NULL,
                NIBRSCode VARCHAR(2) NOT NULL,
                NIBRSDescription VARCHAR(30) NOT NULL,
                PRIMARY KEY (ArresteeWasArmedWithTypeID)
);


CREATE TABLE MultipleArresteeSegmentsIndicatorType (
                MultipleArresteeSegmentsIndicatorTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(15) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(15) NOT NULL,
                PRIMARY KEY (MultipleArresteeSegmentsIndicatorTypeID)
);


CREATE TABLE TypeOfArrestType (
                TypeOfArrestTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(20) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(20) NOT NULL,
                PRIMARY KEY (TypeOfArrestTypeID)
);


CREATE TABLE AdditionalJustifiableHomicideCircumstancesType (
                AdditionalJustifiableHomicideCircumstancesTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(80) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(80) NOT NULL,
                PRIMARY KEY (AdditionalJustifiableHomicideCircumstancesTypeID)
);


CREATE TABLE TypeInjuryType (
                TypeInjuryTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(25) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(25) NOT NULL,
                PRIMARY KEY (TypeInjuryTypeID)
);


CREATE TABLE ResidentStatusOfPersonType (
                ResidentStatusOfPersonTypeID INT NOT NULL AUTO_INCREMENT,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(25) NOT NULL,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(25) NOT NULL,
                PRIMARY KEY (ResidentStatusOfPersonTypeID)
);


CREATE TABLE EthnicityOfPersonType (
                EthnicityOfPersonTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(25) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(25) NOT NULL,
                PRIMARY KEY (EthnicityOfPersonTypeID)
);


CREATE TABLE TypeOfVictimType (
                TypeOfVictimTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(75) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(75) NOT NULL,
                PRIMARY KEY (TypeOfVictimTypeID)
);


CREATE TABLE TypeDrugMeasurementType (
                TypeDrugMeasurementTypeID INT AUTO_INCREMENT NOT NULL,
                StateCode VARCHAR(2) NOT NULL,
                StateDescription VARCHAR(20) NOT NULL,
                NIBRSCode VARCHAR(2) NOT NULL,
                NIBRSDescription VARCHAR(20) NOT NULL,
                PRIMARY KEY (TypeDrugMeasurementTypeID)
);


CREATE TABLE PropertyDescriptionType (
                PropertyDescriptionTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(2) NOT NULL,
                StateDescription VARCHAR(70) NOT NULL,
                NIBRSCode VARCHAR(2) NOT NULL,
                NIBRSDescription VARCHAR(70) NOT NULL,
                PRIMARY KEY (PropertyDescriptionTypeID)
);


CREATE TABLE RaceOfPersonType (
                RaceOfPersonTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(42) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(42) NOT NULL,
                PRIMARY KEY (RaceOfPersonTypeID)
);


CREATE TABLE SexOfPersonType (
                SexOfPersonTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(35) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(35) NOT NULL,
                PRIMARY KEY (SexOfPersonTypeID)
);


CREATE TABLE TypeOfCriminalActivityType (
                TypeOfCriminalActivityTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(2) NOT NULL,
                StateDescription VARCHAR(80) NOT NULL,
                NIBRSCode VARCHAR(2) NOT NULL,
                NIBRSDescription VARCHAR(80) NOT NULL,
                PRIMARY KEY (TypeOfCriminalActivityTypeID)
);


CREATE TABLE OffenderSuspectedOfUsingType (
                OffenderSuspectedOfUsingTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(20) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(20) NOT NULL,
                PRIMARY KEY (OffenderSuspectedOfUsingTypeID)
);


CREATE TABLE SegmentActionTypeType (
                SegmentActionTypeTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(25) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(25) NOT NULL,
                PRIMARY KEY (SegmentActionTypeTypeID)
);


CREATE TABLE VictimOffenderRelationshipType (
                VictimOffenderRelationshipTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(2) NOT NULL,
                StateDescription VARCHAR(50) NOT NULL,
                NIBRSCode VARCHAR(2) NOT NULL,
                NIBRSDescription VARCHAR(50) NOT NULL,
                PRIMARY KEY (VictimOffenderRelationshipTypeID)
);


CREATE TABLE AggravatedAssaultHomicideCircumstancesType (
                AggravatedAssaultHomicideCircumstancesTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(3) NOT NULL,
                StateDescription VARCHAR(55) NOT NULL,
                NIBRSCode VARCHAR(3) NOT NULL,
                NIBRSDescription VARCHAR(55) NOT NULL,
                PRIMARY KEY (AggravatedAssaultHomicideCircumstancesTypeID)
);


CREATE TABLE TypePropertyLossEtcType (
                TypePropertyLossEtcTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(1) NOT NULL,
                StateDescription VARCHAR(85) NOT NULL,
                NIBRSCode VARCHAR(1) NOT NULL,
                NIBRSDescription VARCHAR(85) NOT NULL,
                PRIMARY KEY (TypePropertyLossEtcTypeID)
);


CREATE TABLE UCROffenseCodeType (
                UCROffenseCodeTypeID INT NOT NULL AUTO_INCREMENT,
                StateCode VARCHAR(3) NOT NULL,
                StateDescription VARCHAR(70) NOT NULL,
                NIBRSCode VARCHAR(3) NOT NULL,
                NIBRSDescription VARCHAR(70) NOT NULL,
                OffenseCategory1 VARCHAR(70) NOT NULL,
                OffenseCategory2 VARCHAR(70) NOT NULL,
                OffenseCategory3 VARCHAR(70) NOT NULL,
                OffenseCategory4 VARCHAR(70) NOT NULL,
                PRIMARY KEY (UCROffenseCodeTypeID)
);


CREATE TABLE ArrestReportSegment (
                ArrestReportSegmentID INT NOT NULL AUTO_INCREMENT,
                SegmentActionTypeTypeID INT NOT NULL,
                MonthOfTape VARCHAR(2),
                YearOfTape VARCHAR(4),
                CityIndicator VARCHAR(4),
                AgencyID INT NOT NULL,
                ORI VARCHAR(9),
                ArrestTransactionNumber VARCHAR(12),
                ArresteeSequenceNumber INT NOT NULL,
                ArrestDate DATE,
                ArrestDateID INT NOT NULL,
                TypeOfArrestTypeID INT NOT NULL,
                AgeOfArresteeMin INT,
                AgeOfArresteeMax INT,
                NonNumericAge VARCHAR(2),
                SexOfPersonTypeID INT NOT NULL,
                RaceOfPersonTypeID INT NOT NULL,
                EthnicityOfPersonTypeID INT NOT NULL,
                ResidentStatusOfPersonTypeID INT NOT NULL,
                DispositionOfArresteeUnder18TypeID INT NOT NULL,
                UCROffenseCodeTypeID INT NOT NULL,
                PRIMARY KEY (ArrestReportSegmentID)
);


CREATE TABLE ArrestReportSegmentWasArmedWith (
                ArrestReportSegmentWasArmedWithID INT NOT NULL AUTO_INCREMENT,
                ArrestReportSegmentID INT NOT NULL,
                ArresteeWasArmedWithTypeID INT NOT NULL,
                AutomaticWeaponIndicator VARCHAR(1),
                PRIMARY KEY (ArrestReportSegmentWasArmedWithID)
);


CREATE TABLE AdministrativeSegment (
                AdministrativeSegmentID INT NOT NULL AUTO_INCREMENT,
                SegmentActionTypeTypeID INT NOT NULL,
                MonthOfTape VARCHAR(2),
                YearOfTape VARCHAR(4),
                CityIndicator VARCHAR(4),
                ORI VARCHAR(9),
                AgencyID INT NOT NULL,
                IncidentNumber VARCHAR(12),
                IncidentDate DATE,
                IncidentDateID INT NOT NULL,
                ReportDateIndicator VARCHAR(1),
                IncidentHour VARCHAR(2),
                ClearedExceptionallyTypeID INT NOT NULL,
                ExceptionalClearanceDate DATE,
                ExceptionalClearanceDateID INT NOT NULL,
                CargoTheftIndicatorTypeID INT NOT NULL,
                PRIMARY KEY (AdministrativeSegmentID)
);


CREATE TABLE ArresteeSegment (
                ArresteeSegmentID INT NOT NULL AUTO_INCREMENT,
                SegmentActionTypeTypeID INT NOT NULL,
                AdministrativeSegmentID INT NOT NULL,
                ArresteeSequenceNumber INT NOT NULL,
                ArrestTransactionNumber VARCHAR(12),
                ArrestDate DATE,
                ArrestDateID INT NOT NULL,
                TypeOfArrestTypeID INT NOT NULL,
                MultipleArresteeSegmentsIndicatorTypeID INT NOT NULL,
                AgeOfArresteeMin INT,
                AgeOfArresteeMax INT,
                NonNumericAge VARCHAR(2),
                SexOfPersonTypeID INT NOT NULL,
                RaceOfPersonTypeID INT NOT NULL,
                EthnicityOfPersonTypeID INT NOT NULL,
                ResidentStatusOfPersonTypeID INT NOT NULL,
                DispositionOfArresteeUnder18TypeID INT NOT NULL,
                UCROffenseCodeTypeID INT NOT NULL,
                PRIMARY KEY (ArresteeSegmentID)
);


CREATE TABLE ArresteeSegmentWasArmedWith (
                ArresteeSegmentWasArmedWithID INT NOT NULL AUTO_INCREMENT,
                ArresteeSegmentID INT NOT NULL,
                ArresteeWasArmedWithTypeID INT NOT NULL,
                AutomaticWeaponIndicator VARCHAR(1),
                PRIMARY KEY (ArresteeSegmentWasArmedWithID)
);


CREATE TABLE OffenderSegment (
                OffenderSegmentID INT NOT NULL AUTO_INCREMENT,
                SegmentActionTypeTypeID INT NOT NULL,
                AdministrativeSegmentID INT NOT NULL,
                OffenderSequenceNumber INT,
                AgeOfOffenderMin INT,
                AgeOfOffenderMax INT,
                NonNumericAge VARCHAR(2),
                SexOfPersonTypeID INT NOT NULL,
                RaceOfPersonTypeID INT NOT NULL,
                EthnicityOfPersonTypeID INT NOT NULL,
                PRIMARY KEY (OffenderSegmentID)
);


CREATE TABLE VictimSegment (
                VictimSegmentID INT NOT NULL AUTO_INCREMENT,
                SegmentActionTypeTypeID INT NOT NULL,
                AdministrativeSegmentID INT NOT NULL,
                VictimSequenceNumber INT,
                TypeOfVictimTypeID INT NOT NULL,
                OfficerActivityCircumstanceTypeID INT NOT NULL,
                OfficerAssignmentTypeTypeID INT NOT NULL,
                OfficerOtherJurisdictionORI VARCHAR(9),
                AgeOfVictimMin INT,
                AgeOfVictimMax INT,
                AgeNeonateIndicator INT NOT NULL,
                AgeFirstWeekIndicator INT NOT NULL,
                AgeFirstYearIndicator INT NOT NULL,
                NonNumericAge VARCHAR(2),
                SexOfPersonTypeID INT NOT NULL,
                RaceOfPersonTypeID INT NOT NULL,
                EthnicityOfPersonTypeID INT NOT NULL,
                ResidentStatusOfPersonTypeID INT NOT NULL,
                AdditionalJustifiableHomicideCircumstancesTypeID INT NOT NULL,
                PRIMARY KEY (VictimSegmentID)
);


CREATE TABLE TypeInjury (
                TypeInjuryID INT NOT NULL AUTO_INCREMENT,
                VictimSegmentID INT NOT NULL,
                TypeInjuryTypeID INT NOT NULL,
                PRIMARY KEY (TypeInjuryID)
);


CREATE TABLE AggravatedAssaultHomicideCircumstances (
                AggravatedAssaultHomicideCircumstancesID INT NOT NULL AUTO_INCREMENT,
                VictimSegmentID INT NOT NULL,
                AggravatedAssaultHomicideCircumstancesTypeID INT NOT NULL,
                PRIMARY KEY (AggravatedAssaultHomicideCircumstancesID)
);


CREATE TABLE VictimOffenderAssociation (
                VictimOffenderAssociationID INT NOT NULL AUTO_INCREMENT,
                VictimSegmentID INT NOT NULL,
                OffenderSegmentID INT NOT NULL,
                VictimOffenderRelationshipTypeID INT NOT NULL,
                PRIMARY KEY (VictimOffenderAssociationID)
);


CREATE TABLE PropertySegment (
                PropertySegmentID INT NOT NULL AUTO_INCREMENT,
                SegmentActionTypeTypeID INT NOT NULL,
                AdministrativeSegmentID INT NOT NULL,
                TypePropertyLossEtcTypeID INT NOT NULL,
                NumberOfStolenMotorVehicles INT,
                NumberOfRecoveredMotorVehicles INT,
                PRIMARY KEY (PropertySegmentID)
);


CREATE TABLE PropertyType (
                PropertyTypeID INT NOT NULL AUTO_INCREMENT,
                PropertySegmentID INT NOT NULL,
                PropertyDescriptionTypeID INT NOT NULL,
                ValueOfProperty NUMERIC,
                RecoveredDate DATE,
                RecoveredDateID INT NOT NULL,
                PRIMARY KEY (PropertyTypeID)
);


CREATE TABLE SuspectedDrugType (
                SuspectedDrugTypeID INT NOT NULL AUTO_INCREMENT,
                PropertySegmentID INT NOT NULL,
                SuspectedDrugTypeTypeID INT NOT NULL,
                TypeDrugMeasurementTypeID INT NOT NULL,
                EstimatedDrugQuantity DECIMAL(10,3),
                PRIMARY KEY (SuspectedDrugTypeID)
);


CREATE TABLE OffenseSegment (
                OffenseSegmentID INT NOT NULL AUTO_INCREMENT,
                SegmentActionTypeTypeID INT NOT NULL,
                AdministrativeSegmentID INT NOT NULL,
                UCROffenseCodeTypeID INT NOT NULL,
                OffenseAttemptedCompleted VARCHAR(1),
                LocationTypeTypeID INT NOT NULL,
                NumberOfPremisesEntered INT,
                MethodOfEntryTypeID INT NOT NULL,
                PRIMARY KEY (OffenseSegmentID)
);


CREATE TABLE BiasMotivation (
                BiasMotivationID INT AUTO_INCREMENT NOT NULL AUTO_INCREMENT,
                OffenseSegmentID INT NOT NULL,
                BiasMotivationTypeID INT NOT NULL,
                PRIMARY KEY (BiasMotivationID)
);


CREATE TABLE VictimOffenseAssociation (
                VictimOffenseAssociationID INT NOT NULL AUTO_INCREMENT,
                VictimSegmentID INT NOT NULL,
                OffenseSegmentID INT NOT NULL,
                PRIMARY KEY (VictimOffenseAssociationID)
);


CREATE TABLE TypeOfWeaponForceInvolved (
                TypeOfWeaponForceInvolvedID INT NOT NULL AUTO_INCREMENT,
                AutomaticWeaponIndicator VARCHAR(1) NOT NULL,
                OffenseSegmentID INT NOT NULL,
                TypeOfWeaponForceInvolvedTypeID INT NOT NULL,
                PRIMARY KEY (TypeOfWeaponForceInvolvedID)
);


CREATE TABLE TypeCriminalActivity (
                TypeCriminalActivityID INT NOT NULL AUTO_INCREMENT,
                OffenseSegmentID INT NOT NULL,
                TypeOfCriminalActivityTypeID INT NOT NULL,
                PRIMARY KEY (TypeCriminalActivityID)
);


CREATE TABLE OffenderSuspectedOfUsing (
                OffenderSuspectedOfUsingID INT NOT NULL AUTO_INCREMENT,
                OffenseSegmentID INT NOT NULL,
                OffenderSuspectedOfUsingTypeID INT NOT NULL,
                PRIMARY KEY (OffenderSuspectedOfUsingID)
);


CREATE TABLE ZeroReportingSegment (
                ZeroReportingSegmentID INT NOT NULL AUTO_INCREMENT,
                AdministrativeSegmentID INT NOT NULL,
                RecordDescriptionWord VARCHAR(4),
                SegmentLevel VARCHAR(1),
                MonthOfTape VARCHAR(2),
                YearOfTape VARCHAR(4),
                CityIndicator VARCHAR(4),
                IncidentDate DATE,
                IncidentDateID INT NOT NULL,
                ReportDateIndicator VARCHAR(1),
                IncidentHour VARCHAR(4),
                CleardExceptionally VARCHAR(1),
                ExceptionalClearanceDate DATE,
                PRIMARY KEY (ZeroReportingSegmentID)
);


CREATE TABLE LEOKASegment (
                LEOKASegmentID INT NOT NULL AUTO_INCREMENT,
                AdministrativeSegmentID INT NOT NULL,
                RecordDescriptionWord VARCHAR(4),
                SegmentLevel VARCHAR(1),
                MonthOfTape VARCHAR(2),
                YearOfTape VARCHAR(4),
                CityIndicator VARCHAR(4),
                Filler VARCHAR(12),
                LEOKAData VARCHAR(600),
                PRIMARY KEY (LEOKASegmentID)
);


ALTER TABLE Violation ADD CONSTRAINT submission_violation_fk
FOREIGN KEY (SubmissionID)
REFERENCES Submission (SubmissionID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AdministrativeSegment ADD CONSTRAINT cargotheftindicatortype_administrativesegment_fk
FOREIGN KEY (CargoTheftIndicatorTypeID)
REFERENCES CargoTheftIndicatorType (CargoTheftIndicatorTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE Agency ADD CONSTRAINT agencytype_agency_fk
FOREIGN KEY (AgencyTypeID)
REFERENCES AgencyType (AgencyTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AdministrativeSegment ADD CONSTRAINT date_administrativesegment_fk
FOREIGN KEY (IncidentDateID)
REFERENCES DateType (DateTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PropertyType ADD CONSTRAINT date_propertytype_fk
FOREIGN KEY (RecoveredDateID)
REFERENCES DateType (DateTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT date_arresteesegment_fk
FOREIGN KEY (ArrestDateID)
REFERENCES DateType (DateTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegment ADD CONSTRAINT date_arrestreportsegment_fk
FOREIGN KEY (ArrestDateID)
REFERENCES DateType (DateTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ZeroReportingSegment ADD CONSTRAINT date_zeroreportingsegment_fk
FOREIGN KEY (IncidentDateID)
REFERENCES DateType (DateTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AdministrativeSegment ADD CONSTRAINT datetype_administrativesegment_fk
FOREIGN KEY (ExceptionalClearanceDateID)
REFERENCES DateType (DateTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AdministrativeSegment ADD CONSTRAINT agency_administrativesegment_fk
FOREIGN KEY (AgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegment ADD CONSTRAINT agency_arrestreportsegment_fk
FOREIGN KEY (AgencyID)
REFERENCES Agency (AgencyID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimSegment ADD CONSTRAINT officerassignmenttypetype_victimsegment_fk
FOREIGN KEY (OfficerAssignmentTypeTypeID)
REFERENCES OfficerAssignmentTypeType (OfficerAssignmentTypeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimSegment ADD CONSTRAINT typeofofficeractivitycircumstancetype_victimsegment_fk
FOREIGN KEY (OfficerActivityCircumstanceTypeID)
REFERENCES OfficerActivityCircumstanceType (OfficerActivityCircumstanceTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE TypeOfWeaponForceInvolved ADD CONSTRAINT typeofweaponforceinvolvedtype_typeofweaponforceinvolved_fk
FOREIGN KEY (TypeOfWeaponForceInvolvedTypeID)
REFERENCES TypeOfWeaponForceInvolvedType (TypeOfWeaponForceInvolvedTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SuspectedDrugType ADD CONSTRAINT suspecteddrugtypetype_suspecteddrugtype_fk
FOREIGN KEY (SuspectedDrugTypeTypeID)
REFERENCES SuspectedDrugTypeType (SuspectedDrugTypeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BiasMotivation ADD CONSTRAINT biasmotivationtype_biasmotivation_fk
FOREIGN KEY (BiasMotivationTypeID)
REFERENCES BiasMotivationType (BiasMotivationTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenseSegment ADD CONSTRAINT methodofentrytype_offensesegment_fk
FOREIGN KEY (MethodOfEntryTypeID)
REFERENCES MethodOfEntryType (MethodOfEntryTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenseSegment ADD CONSTRAINT locationtypetype_offensesegment_fk
FOREIGN KEY (LocationTypeTypeID)
REFERENCES LocationTypeType (LocationTypeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AdministrativeSegment ADD CONSTRAINT clearedexceptionallytype_adminseg_fk
FOREIGN KEY (ClearedExceptionallyTypeID)
REFERENCES ClearedExceptionallyType (ClearedExceptionallyTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT dispositionofarresteeunder18lookup_arresteesegment_fk
FOREIGN KEY (DispositionOfArresteeUnder18TypeID)
REFERENCES DispositionOfArresteeUnder18Type (DispositionOfArresteeUnder18TypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegment ADD CONSTRAINT dispositionofarresteeunder18lookup_arrestreportsegment_fk
FOREIGN KEY (DispositionOfArresteeUnder18TypeID)
REFERENCES DispositionOfArresteeUnder18Type (DispositionOfArresteeUnder18TypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegmentWasArmedWith ADD CONSTRAINT arresteearmedwithtypelookup_arresteearmedwith_fk
FOREIGN KEY (ArresteeWasArmedWithTypeID)
REFERENCES ArresteeWasArmedWithType (ArresteeWasArmedWithTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegmentWasArmedWith ADD CONSTRAINT arresteewasarmedwithtype_arrestreportsegmentwasarmedwith_1_fk
FOREIGN KEY (ArresteeWasArmedWithTypeID)
REFERENCES ArresteeWasArmedWithType (ArresteeWasArmedWithTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT multiplearresteesegmentsindicatorlookup_arresteesegment_fk
FOREIGN KEY (MultipleArresteeSegmentsIndicatorTypeID)
REFERENCES MultipleArresteeSegmentsIndicatorType (MultipleArresteeSegmentsIndicatorTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT typeofarrestlookup_arresteesegment_fk
FOREIGN KEY (TypeOfArrestTypeID)
REFERENCES TypeOfArrestType (TypeOfArrestTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegment ADD CONSTRAINT typeofarrestlookup_arrestreportsegment_fk
FOREIGN KEY (TypeOfArrestTypeID)
REFERENCES TypeOfArrestType (TypeOfArrestTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimSegment ADD CONSTRAINT additionaljustifiablehomicidecircumstancestype_victimsegment_fk
FOREIGN KEY (AdditionalJustifiableHomicideCircumstancesTypeID)
REFERENCES AdditionalJustifiableHomicideCircumstancesType (AdditionalJustifiableHomicideCircumstancesTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE TypeInjury ADD CONSTRAINT typeofinjurylookup_injurytable_fk
FOREIGN KEY (TypeInjuryTypeID)
REFERENCES TypeInjuryType (TypeInjuryTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimSegment ADD CONSTRAINT residentstatusofpersontype_victimsegment_fk
FOREIGN KEY (ResidentStatusOfPersonTypeID)
REFERENCES ResidentStatusOfPersonType (ResidentStatusOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT residentstatusofpersontype_arresteesegment_fk
FOREIGN KEY (ResidentStatusOfPersonTypeID)
REFERENCES ResidentStatusOfPersonType (ResidentStatusOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegment ADD CONSTRAINT residentstatusofpersontype_arrestreportsegment_fk
FOREIGN KEY (ResidentStatusOfPersonTypeID)
REFERENCES ResidentStatusOfPersonType (ResidentStatusOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimSegment ADD CONSTRAINT ethnicityofpersontype_victimsegment_fk
FOREIGN KEY (EthnicityOfPersonTypeID)
REFERENCES EthnicityOfPersonType (EthnicityOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT ethnicityofpersontype_arresteesegment_fk
FOREIGN KEY (EthnicityOfPersonTypeID)
REFERENCES EthnicityOfPersonType (EthnicityOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegment ADD CONSTRAINT ethnicityofpersontype_arrestreportsegment_fk
FOREIGN KEY (EthnicityOfPersonTypeID)
REFERENCES EthnicityOfPersonType (EthnicityOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenderSegment ADD CONSTRAINT ethnicityofpersontype_offendersegment_fk
FOREIGN KEY (EthnicityOfPersonTypeID)
REFERENCES EthnicityOfPersonType (EthnicityOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimSegment ADD CONSTRAINT typeofvictimtype_victimsegment_fk
FOREIGN KEY (TypeOfVictimTypeID)
REFERENCES TypeOfVictimType (TypeOfVictimTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SuspectedDrugType ADD CONSTRAINT typedrugmeasurementtype_suspecteddrugtype_fk
FOREIGN KEY (TypeDrugMeasurementTypeID)
REFERENCES TypeDrugMeasurementType (TypeDrugMeasurementTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PropertyType ADD CONSTRAINT propertydescriptiontype_propertytype_fk
FOREIGN KEY (PropertyDescriptionTypeID)
REFERENCES PropertyDescriptionType (PropertyDescriptionTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimSegment ADD CONSTRAINT raceofpersontype_victimsegment_fk
FOREIGN KEY (RaceOfPersonTypeID)
REFERENCES RaceOfPersonType (RaceOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenderSegment ADD CONSTRAINT raceofpersontype_offender_fk
FOREIGN KEY (RaceOfPersonTypeID)
REFERENCES RaceOfPersonType (RaceOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT raceofpersontype_arresteesegment_fk
FOREIGN KEY (RaceOfPersonTypeID)
REFERENCES RaceOfPersonType (RaceOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegment ADD CONSTRAINT raceofpersontype_arrestreportsegment_fk
FOREIGN KEY (RaceOfPersonTypeID)
REFERENCES RaceOfPersonType (RaceOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenderSegment ADD CONSTRAINT sexcodelookup_offender_fk
FOREIGN KEY (SexOfPersonTypeID)
REFERENCES SexOfPersonType (SexOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegment ADD CONSTRAINT sexcodelookup_arrestreportsegment_fk
FOREIGN KEY (SexOfPersonTypeID)
REFERENCES SexOfPersonType (SexOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimSegment ADD CONSTRAINT sexofpersontype_victimsegment_fk
FOREIGN KEY (SexOfPersonTypeID)
REFERENCES SexOfPersonType (SexOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT sexofpersontype_arresteesegment_fk
FOREIGN KEY (SexOfPersonTypeID)
REFERENCES SexOfPersonType (SexOfPersonTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE TypeCriminalActivity ADD CONSTRAINT typeofcriminalactivitytype_typecriminalactivity_fk
FOREIGN KEY (TypeOfCriminalActivityTypeID)
REFERENCES TypeOfCriminalActivityType (TypeOfCriminalActivityTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenderSuspectedOfUsing ADD CONSTRAINT offendersuspectedofusingtype_offendersuspectedofusing_fk
FOREIGN KEY (OffenderSuspectedOfUsingTypeID)
REFERENCES OffenderSuspectedOfUsingType (OffenderSuspectedOfUsingTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AdministrativeSegment ADD CONSTRAINT segmentactiontype_adminseg_fk
FOREIGN KEY (SegmentActionTypeTypeID)
REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenseSegment ADD CONSTRAINT segmentactiontype_offensesegment_fk
FOREIGN KEY (SegmentActionTypeTypeID)
REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PropertySegment ADD CONSTRAINT segmentactiontype_propertysegment_fk
FOREIGN KEY (SegmentActionTypeTypeID)
REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimSegment ADD CONSTRAINT segmentactiontype_victimsegment_fk
FOREIGN KEY (SegmentActionTypeTypeID)
REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenderSegment ADD CONSTRAINT segmentactiontype_offender_fk
FOREIGN KEY (SegmentActionTypeTypeID)
REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT segmentactiontype_arresteesegment_fk
FOREIGN KEY (SegmentActionTypeTypeID)
REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegment ADD CONSTRAINT segmentactiontype_arrestreportsegment_fk
FOREIGN KEY (SegmentActionTypeTypeID)
REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimOffenderAssociation ADD CONSTRAINT victimoffenderrelationshiptype_victimassociation_fk
FOREIGN KEY (VictimOffenderRelationshipTypeID)
REFERENCES VictimOffenderRelationshipType (VictimOffenderRelationshipTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AggravatedAssaultHomicideCircumstances ADD CONSTRAINT aahomicidecircumstancestype_aahomicidecircumstances_fk
FOREIGN KEY (AggravatedAssaultHomicideCircumstancesTypeID)
REFERENCES AggravatedAssaultHomicideCircumstancesType (AggravatedAssaultHomicideCircumstancesTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PropertySegment ADD CONSTRAINT typepropertylossetctype_propertysegment_fk
FOREIGN KEY (TypePropertyLossEtcTypeID)
REFERENCES TypePropertyLossEtcType (TypePropertyLossEtcTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenseSegment ADD CONSTRAINT ucroffensecodetype_offensesegment_fk
FOREIGN KEY (UCROffenseCodeTypeID)
REFERENCES UCROffenseCodeType (UCROffenseCodeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT ucroffensecodetype_arresteesegment_fk
FOREIGN KEY (UCROffenseCodeTypeID)
REFERENCES UCROffenseCodeType (UCROffenseCodeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegment ADD CONSTRAINT ucroffensecodetype_arrestreportsegment_fk
FOREIGN KEY (UCROffenseCodeTypeID)
REFERENCES UCROffenseCodeType (UCROffenseCodeTypeID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArrestReportSegmentWasArmedWith ADD CONSTRAINT arrestreportsegment_arrestreportsegmentwasarmedwith_1_fk
FOREIGN KEY (ArrestReportSegmentID)
REFERENCES ArrestReportSegment (ArrestReportSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE LEOKASegment ADD CONSTRAINT adminseg_leokasegment_fk
FOREIGN KEY (AdministrativeSegmentID)
REFERENCES AdministrativeSegment (AdministrativeSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ZeroReportingSegment ADD CONSTRAINT adminseg_zeroreportingsegment_fk
FOREIGN KEY (AdministrativeSegmentID)
REFERENCES AdministrativeSegment (AdministrativeSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenseSegment ADD CONSTRAINT administrativesegment_offensesegment_fk
FOREIGN KEY (AdministrativeSegmentID)
REFERENCES AdministrativeSegment (AdministrativeSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PropertySegment ADD CONSTRAINT administrativesegment_propertysegment_fk
FOREIGN KEY (AdministrativeSegmentID)
REFERENCES AdministrativeSegment (AdministrativeSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimSegment ADD CONSTRAINT administrativesegment_victimsegment_fk
FOREIGN KEY (AdministrativeSegmentID)
REFERENCES AdministrativeSegment (AdministrativeSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenderSegment ADD CONSTRAINT administrativesegment_offender_fk
FOREIGN KEY (AdministrativeSegmentID)
REFERENCES AdministrativeSegment (AdministrativeSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegment ADD CONSTRAINT administrativesegment_arresteesegment_fk
FOREIGN KEY (AdministrativeSegmentID)
REFERENCES AdministrativeSegment (AdministrativeSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE ArresteeSegmentWasArmedWith ADD CONSTRAINT arresteesegment_arresteewasarmedwith_fk
FOREIGN KEY (ArresteeSegmentID)
REFERENCES ArresteeSegment (ArresteeSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimOffenderAssociation ADD CONSTRAINT offendersegment_segmentassociation_fk
FOREIGN KEY (OffenderSegmentID)
REFERENCES OffenderSegment (OffenderSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimOffenderAssociation ADD CONSTRAINT victimsegment_ucroffensecodeassociation_fk
FOREIGN KEY (VictimSegmentID)
REFERENCES VictimSegment (VictimSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE AggravatedAssaultHomicideCircumstances ADD CONSTRAINT victimsegment_aggravatedassaulthomicidecircumstances_fk
FOREIGN KEY (VictimSegmentID)
REFERENCES VictimSegment (VictimSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE TypeInjury ADD CONSTRAINT victimsegment_injurytable_fk
FOREIGN KEY (VictimSegmentID)
REFERENCES VictimSegment (VictimSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimOffenseAssociation ADD CONSTRAINT victimsegment_victimoffenseassociation_fk
FOREIGN KEY (VictimSegmentID)
REFERENCES VictimSegment (VictimSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE SuspectedDrugType ADD CONSTRAINT propertysegment_suspecteddrugtype_fk
FOREIGN KEY (PropertySegmentID)
REFERENCES PropertySegment (PropertySegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE PropertyType ADD CONSTRAINT propertysegment_propertytype_fk
FOREIGN KEY (PropertySegmentID)
REFERENCES PropertySegment (PropertySegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE OffenderSuspectedOfUsing ADD CONSTRAINT offensesegment_offendersuspectedofusing_fk
FOREIGN KEY (OffenseSegmentID)
REFERENCES OffenseSegment (OffenseSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE TypeCriminalActivity ADD CONSTRAINT offensesegment_typecriminalactivity_fk
FOREIGN KEY (OffenseSegmentID)
REFERENCES OffenseSegment (OffenseSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE TypeOfWeaponForceInvolved ADD CONSTRAINT offensesegment_typeofweaponforceinvolved_fk
FOREIGN KEY (OffenseSegmentID)
REFERENCES OffenseSegment (OffenseSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE VictimOffenseAssociation ADD CONSTRAINT offensesegment_victimoffenseassociation_fk
FOREIGN KEY (OffenseSegmentID)
REFERENCES OffenseSegment (OffenseSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE BiasMotivation ADD CONSTRAINT offensesegment_biasmotivation_fk
FOREIGN KEY (OffenseSegmentID)
REFERENCES OffenseSegment (OffenseSegmentID)
ON DELETE NO ACTION
ON UPDATE NO ACTION;