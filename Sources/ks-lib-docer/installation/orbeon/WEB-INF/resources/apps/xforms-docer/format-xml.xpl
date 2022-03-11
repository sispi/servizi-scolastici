
<p:config xmlns:p="http://www.orbeon.com/oxf/pipeline"
          xmlns:oxf="http://www.orbeon.com/oxf/processors">

  <p:param type="input" name="instance" />
  <p:param type="output" name="data"/>

    <p:processor name="oxf:java">
        <!-- Define the class that implements the custom processor -->
        <p:input name="config">
            <config sourcepath="." class="CallXSLT"/>
        </p:input>
        <!-- Pass a data input that the custom processor will read -->
        <p:input name="myinput" href="#instance"/>
        <!-- Return out the data output returned by the custom processor -->
        <p:output name="myoutput" ref="data"/>
    </p:processor>

</p:config>