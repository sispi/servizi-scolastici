
<p:config xmlns:p="http://www.orbeon.com/oxf/pipeline"
	xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:oxf="http://www.orbeon.com/oxf/processors" xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:delegation="http://orbeon.org/oxf/xml/delegation" xmlns:ev="http://www.w3.org/2001/xml-events"
	xmlns:saxon="http://saxon.sf.net/">


	<p:param type="input" name="instance" />
	<p:param type="output" name="data" />

	<p:processor name="oxf:request">
		<p:input name="config">
			<config>
                          <include>/request</include>
                          <include>/request/headers</include>
			</config>
		</p:input>
		<p:output name="data" id="request-query" />
	</p:processor>

	<p:processor name="oxf:identity">
		<p:input name="data">
			<error>false</error>
		</p:input>
		<p:output name="data" id="error-instance" />
	</p:processor>



	<p:choose href="#request-query">
          <p:when test="//parameter[name='action']/value= tokenize('download;download-timbro;download-data',';') or contains(//query-string, 'output-type=pdf')">
            <p:processor name="oxf:url-generator">
              <p:input name="config" transform="oxf:xslt" href="#request-query">
                <config xsl:version="2.0">
                  <url><xsl:value-of select="concat('http://localhost:8080/xforms/XForms?', //query-string)" /></url>
                  <content-type>application/octet-stream</content-type>
                  <cache-control>
                    <use-local-cache>false</use-local-cache>
                  </cache-control>
                </config>
              </p:input>
              <p:output name="data" id="bin-data"/>
            </p:processor>
            <p:processor name="oxf:http-serializer">
              <p:input name="config" transform="oxf:xslt" href="#request-query" >
                <config xsl:version="2.0">
                  <content-type>application/octet-stream</content-type>
                  <force-content-type>true</force-content-type>
                  <header>
                    <name>Content-Disposition</name>
                    <value><xsl:value-of select="concat('attachment; filename=&quot;', //parameter[name='filename']/value, '&quot;')"/></value>
                    <!--<value>attachement; filename=test.bin</value>-->
                  </header>
                  <cache-control>
                    <use-local-cache>false</use-local-cache>
                  </cache-control>
                  <!--<header>
                      <name>Content-Length</name>
                      <value>100000000</value>
                      </header>-->
                </config>
              </p:input>
              <p:input name="data" href="#bin-data"/>
            </p:processor>
            <p:processor name="oxf:identity">
              <p:input name="data" href="#request-query"/>
              <p:output name="data" ref="data"/>
            </p:processor>
          </p:when>
          <p:otherwise>


            <p:processor name="oxf:xforms-submission">

              <p:input name="submission" transform="oxf:xslt" href="#request-query">
                <xforms:submission xsl:version="2.0" method="post" separator="&amp;" ref="/wrapped-instance/*[1]">
                  <xsl:attribute name="action">
                    <xsl:value-of select="concat('http://localhost:8080/xforms/XForms?', //query-string)" />
                  </xsl:attribute>
                  <xforms:header>
                    <xforms:name>username</xforms:name>
                    <xforms:value><xsl:value-of select="//headers/header[name = 'username']/value" /></xforms:value>
                  </xforms:header>

                  <xforms:setvalue ev:event="xforms-submit-error"
                                   ref="/wrapped-instance/error" value="event('response-body')/error/error-message" />
                </xforms:submission>
              </p:input>
              <p:input name="request"
                       href="aggregate('wrapped-instance',#instance, #error-instance)" />
              <p:output name="response" id="response" />
            </p:processor>
            
            <p:choose href="#response">
              <p:when test="/wrapped-instance/error!='false'">
                <p:processor name="oxf:http-serializer">
                  <p:input name="config">
                    <config>
                      <status-code>500</status-code>
                    </config>
                  </p:input>
                  
                  <p:input name="data" transform="oxf:xslt" href="#response">
                    <test xsl:version="2.0" xsi:type="xs:string">
                      <xsl:value-of select="//error" />
                    </test>
                  </p:input>
                  
                  <!--<p:output name="data" ref="data"/> -->
                </p:processor>
                
                <!--<p:processor name="oxf:null-serializer"> <p:input name="data" href="#response"/> 
                    </p:processor> -->
                <p:processor name="oxf:identity">
                  <p:input name="data" href="#response" />

                  <p:output name="data" ref="data" />
                </p:processor>
              </p:when>
              <p:otherwise>
                <p:processor name="oxf:identity">
                  <p:input name="data" href="#response" />
                  
                  <p:output name="data" ref="data" />
                </p:processor>
              </p:otherwise>
            </p:choose>
          </p:otherwise>
	</p:choose>
	
</p:config>
