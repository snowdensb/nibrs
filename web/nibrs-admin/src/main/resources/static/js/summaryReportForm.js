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

 $(function(){
   $(".chosen-select").chosen();  
   $('[data-toggle="popover"]').popover(); 
   $('.incidentYear').mask('9999');
   
   $('#summaryReportRequestForm input').keypress(function (e) {
     if (e.which == 13) {
       $('#submit').click();
       return false;    
     }
   });
   
   refreshIncidentYearDropDown();
   refreshIncidentMonthDropDown();
   
   $('#portalContent').on('change', '#summaryReportRequestForm #ori', function(){
	   refreshIncidentYearDropDown();
   });
   
   $('#portalContent').on('change', '#summaryReportRequestForm #incidentYear', function(){
	   refreshIncidentMonthDropDown()
   });

   function refreshIncidentMonthDropDown(){
	   ori = $('#ori').val(); 
	   incidentYear = $('#incidentYear').val();
	   if (ori && incidentYear){
		   xhr = $.get( context +"months/" + incidentYear + "/" + ori, function(data) {
			   $('#incidentMonth').empty();
			   $('#incidentMonth').append('<option value="">Please select ...</option>');
			   data.forEach( function(item, index) {
				   $('#incidentMonth').append($('<option></option>').attr('value', item).text(item));
			   });
			   $('#incidentMonth').trigger("chosen:updated");
		   }).fail(ojbc.displayFailMessage);
	   }
   }
   function refreshIncidentYearDropDown(){
	   ori = $('#ori').val(); 
	   if (ori){
		   xhr = $.get( context +"years/" + ori, function(data) {
			   $('#incidentYear').empty();
			   $('#incidentYear').append('<option value="">Please select ...</option>');
			   data.forEach( function(item, index) {
                   $('#incidentYear').append($('<option></option>').attr('value', item).text(item));
               });
			   $('#incidentYear').trigger("chosen:updated");
	       }).fail(ojbc.displayFailMessage);
	   }
   }
   
   $('#submit').click( function(){
	 $("#loadingText").removeClass("d-none");
     var formData = $('#summaryReportRequestForm').serialize() + '&'+_csrf_param_name+'='+_csrf_token;
     //console.log("formData" + formData);
     
     var form = document.getElementById('summaryReportRequestForm'); 
     
     var isValidForm = form.checkValidity(); 
     form.classList.add('was-validated'); 
     if ( isValidForm === true){
    	 var reportTypes = $('#reportType').val();
    	 var xhr = [], i, j=0;
    	 var len=reportTypes.length;
    	 for( i=0; i < len; i++){
    		 var reportType = reportTypes[i];
    		 console.log("reportType: " + reportType);
    		 xhr[i] = new XMLHttpRequest();
    		 xhr[i].onreadystatechange = function() {
    			 console.log("i:" + i); 
    			 console.log("len -1 :" + (len -1)); 
		    	  if( this.readyState === 4){
		    		if (++j === len ){
		    			$("#loadingAjaxPane").hide();
		    		}
		    	  }
		    	  else{
					var loadingDiv =  $("#loadingAjaxPane");
					var portalContentDiv = $("#portalContent");
					
					loadingDiv.height(portalContentDiv.height());
					loadingDiv.width(portalContentDiv.width());
					
					$("#loadingAjaxPane").show();
		    	  }
		     };
		     xhr[i].open('POST', context + "summaryReports/"+reportType, true);
		     xhr[i].setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
		     xhr[i].responseType = 'blob';
	    	 console.log("xhr[i]: " + xhr[i]); 
		
		     xhr[i].onload = function(e) {
		         if (this.status === 200) {
		             var blob = this.response;
			    	 var fileName = "";
			    	 var disposition = this.getResponseHeader('Content-Disposition');
			         if (disposition && disposition.indexOf('attachment') !== -1) {
			             var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
			             var matches = filenameRegex.exec(disposition);
			             if (matches != null && matches[1]) fileName = matches[1].replace(/['"]/g, '');
		         	 }
		             if(window.navigator.msSaveOrOpenBlob) {
		                 window.navigator.msSaveBlob(blob, fileName);
		             }
		             else{
		                 var downloadLink = window.document.createElement('a');
		                 var contentTypeHeader = this.getResponseHeader("Content-Type");
		                 downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
		                 downloadLink.download = fileName;
		                 document.body.appendChild(downloadLink);
		                 downloadLink.click();
		                 document.body.removeChild(downloadLink);
		            }
		        }
		      }
		     xhr[i].send(formData);
    	 }
     }
   });
   
   $('#reset').click (function(){
     xhr = $.get(context + "summaryReports/searchForm/reset", function(data) {
    	 $('#portalContent').html(data);
    	 $('.chosen-select').chosen();
     }).fail(ojbc.displayFailMessage);
   });
   

 });
