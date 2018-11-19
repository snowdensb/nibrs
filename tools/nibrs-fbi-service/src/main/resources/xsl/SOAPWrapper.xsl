<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" version="1.0" encoding="UTF-8"/>
	<xsl:template match="/">
		<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ws="http://ws.nibrs.ucr.cjis.fbi.gov/">
			<soap:Header/>
			<soap:Body>
				<ws:SubmitNibrsNIEMDocument >
					<xmlDoc>
						<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
							<xsl:copy-of select="."/>
						<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
					</xmlDoc>
				</ws:SubmitNibrsNIEMDocument>
			</soap:Body>
		</soap:Envelope>
	</xsl:template>
</xsl:stylesheet>