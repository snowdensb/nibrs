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
   
   $('#searchForm input').keypress(function (e) {
     if (e.which == 13) {
       $('#submit').click();
       return false;    
     }
   });
   
   $('#submit').click (function(){
     var formData = $('#searchForm').serialize();
     
     var form = document.getElementById('searchForm'); 
     
     var isValidForm = form.checkValidity(); 
     form.classList.add('was-validated'); 
     if ( isValidForm === true){
    	 var reportTypes = $('#reportType').val();
    	 for(var i=0, len=reportTypes.length; i < len; i++){
    		 var reportType = reportTypes[i];
		     var request = new XMLHttpRequest();
		     request.onreadystatechange = function() {
		    	  if(this.readyState === 4){
				    $("#loadingAjaxPane").hide();                
		    	  }
		    	  else{
					var loadingDiv =  $("#loadingAjaxPane");
					var portalContentDiv = $("#portalContent");
					
					loadingDiv.height(portalContentDiv.height());
					loadingDiv.width(portalContentDiv.width());
					
					$("#loadingAjaxPane").show();
		    	  }
		     };
		     request.open('POST', context + "summaryReports/"+reportType, true);
		     request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
		     request.responseType = 'blob';
		
		     request.onload = function(e) {
		         if (this.status === 200) {
		             var blob = this.response;
			    	 var fileName = ""
			    	 var disposition = request.getResponseHeader('Content-Disposition');
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
		                 var contentTypeHeader = request.getResponseHeader("Content-Type");
		                 downloadLink.href = window.URL.createObjectURL(new Blob([blob], { type: contentTypeHeader }));
		                 downloadLink.download = fileName;
		                 document.body.appendChild(downloadLink);
		                 downloadLink.click();
		                 document.body.removeChild(downloadLink);
		            }
		        }
		      }
		      request.send(formData);
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
