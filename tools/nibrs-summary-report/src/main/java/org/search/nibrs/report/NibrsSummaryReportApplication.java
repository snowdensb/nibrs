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
package org.search.nibrs.report;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.reports.cargotheft.CargoTheftReport;
import org.search.nibrs.report.service.ArsonExcelExporter;
import org.search.nibrs.report.service.AsrExcelExporter;
import org.search.nibrs.report.service.CargoTheftReportExporter;
import org.search.nibrs.report.service.HumanTraffickingExporter;
import org.search.nibrs.report.service.ReturnAFormExporter;
import org.search.nibrs.report.service.StagingDataRestClient;
import org.search.nibrs.report.service.SupplementaryHomicideReportExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NibrsSummaryReportApplication implements CommandLineRunner{
	public static final Log log = LogFactory.getLog(NibrsSummaryReportApplication.class);
	@Autowired 
	public StagingDataRestClient restClient; 
	@Autowired 
	public ReturnAFormExporter excelExporter;
	@Autowired 
	public AsrExcelExporter asrExcelExporter;
	@Autowired 
	public ArsonExcelExporter arsonExcelExporter;
	@Autowired 
	public HumanTraffickingExporter humanTraffickingExporter;
	@Autowired 
	public SupplementaryHomicideReportExporter supplementaryHomicideReportExporter;
	@Autowired 
	public CargoTheftReportExporter cargoTheftReportExporter;

	public static ConfigurableApplicationContext context;
	
	public static void main(String[] args) {
		SpringApplication.run(NibrsSummaryReportApplication.class, args).close();
	}
	
    @Override
    public void run(String... args) throws Exception {

    	if (args.length < 3){
    		System.out.println("Please enter all the non optional arguments ORI, Year and Month");
    		System.out.println("Example: java -jar target/nibrs-summary-report-1.0.0.jar HI0020000 2017 3");
    		System.out.println("Additional optional arguments can be entered: ");
    		System.out.println("--app.stagingDataRestServiceBaseUrl default value is http://localhost:8080");
    		System.out.println("--app.returnAFormOutputPath default value is .");
    		System.exit(0);; 
    	}
    	
        for (String arg: args){
        	System.out.println("arg: " + arg);
        }
        
//        AsrReports asrAdult = restClient.getAsrReports(args[0], args[1], args[2]);
//        System.out.println("asrAdult: \n" + asrAdult);
//        asrExcelExporter.exportAsrJuvenileForm(asrAdult);
//        asrExcelExporter.exportAsrAdultForm(asrAdult);
        
//        ArsonReport arsonReport = restClient.getArsonReport(args[0], args[1], args[2]);
//        System.out.println("arsonReport: \n" + arsonReport);
//        arsonExcelExporter.exportArsonReport(arsonReport);

//        HumanTraffickingForm humanTraffickingForm = restClient.getHumanTraffickingForm(args[0], args[1], args[2]);
//        humanTraffickingExporter.exportHumanTraffickingReport(humanTraffickingForm);
        
//        SupplementaryHomicideReport supplementaryHomicideReport = restClient.getSupplementaryHomicideReport(args[0], args[1], args[2]);
//        supplementaryHomicideReportExporter.exportSupplementaryHomicideReport(supplementaryHomicideReport);
        
        CargoTheftReport cargoTheftReport = restClient.getCargoTheftReport(args[0], args[1], args[2], "0");
        cargoTheftReportExporter.exportCargoTheftReport(cargoTheftReport);;
        
//        ReturnAForm returnAForm = restClient.getReturnAForm(args[0], args[1], args[2]);
//        System.out.println("returnAForm: \n" + returnAForm);
//        excelExporter.exportReturnAForm(returnAForm);
//        excelExporter.exportReturnASupplement(returnAForm);

    }
}
