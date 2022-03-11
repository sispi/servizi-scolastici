/*

da modificare in solrconfig.xml :


...

 <updateRequestProcessorChain name="updateChain">

 <processor class="solr.LogUpdateProcessorFactory" >
 <int name="maxNumToLog">100</int>
 </processor>

 <processor class="solr.StatelessScriptUpdateProcessorFactory">
 <bool name="enabled">false</bool>
 <str name="script">fontetrigger.js</str>
 </processor>

 <processor class="solr.RemoveBlankFieldUpdateProcessorFactory" >
 <!--<lst name="exclude">
 <str name="fieldName">parent</str>
 </lst>-->
 </processor>

 ....

 */


function processAdd(cmd) {

    doc = cmd.solrDoc;  // org.apache.solr.common.SolrInputDocument

    id = doc.getFieldValue("id");

    CF_PERSONA = doc.getFieldValue("CF_PERSONA");
    CF_AZIENDA = doc.getFieldValue("CF_AZIENDA");
    ID_IMMOBILE = doc.getFieldValue("ID_IMMOBILE");

    if (CF_PERSONA != null || CF_AZIENDA != null || ID_IMMOBILE != null)
    {
        doc.setField("NEEDS_FONTE_UPDATE", true );
        doc.setField("NEEDS_RACCOGLITORE_UPDATE", true );

        logger.info("fontetrigger#processAdd: id=" + id );
    }
}

function processDelete(cmd) {
    // no-op
}

function processMergeIndexes(cmd) {
    // no-op
}

function processCommit(cmd) {
    // no-op
}

function processRollback(cmd) {
    // no-op
}

function finish() {
    // no-op
}
