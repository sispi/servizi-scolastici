<div class="row ${query.type}"><!-- IMPORTANTE NON ELIMINARE CLASSE -->
    <template v-if="riferimento.COD_CLIENTI">
        <input type="hidden" class="form-control nameToBuildPayload" name="COD_CLIENTI" v-model="riferimento.COD_CLIENTI" disabled>
    </template>
    <input id="tipoAnagrafica" type="hidden" class="form-control" value="CLIENTI">
    <div class="form-group col-md-12">
        <label class="labelNew">{{messages["listaAnagrafiche.descrizione"] || "Descrizione"}}</label>
        <input type="text" class="form-control nameToBuildPayload" name="DES_CLIENTI" v-model="riferimento.DES_CLIENTI">
    </div>
    <div class="form-group col-md-6">
        <label class="labelNew">{{messages["listaAnagrafiche.metadato1"] || "Metadato1"}}</label>
        <input type="text" class="form-control nameToBuildPayload" name="METADATO_1" v-model="riferimento.METADATO_1">
    </div>
    <div class="form-group col-md-6">
        <label class="labelNew">{{messages["listaAnagrafiche.metadato2"] || "Metadato2"}}</label>
        <input type="text" class="form-control nameToBuildPayload" name="METADATO_2" v-model="riferimento.METADATO_2">
    </div>
</div>


