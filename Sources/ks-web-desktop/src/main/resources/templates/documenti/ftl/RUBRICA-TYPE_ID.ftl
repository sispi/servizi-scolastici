<div class="row ${query.type}"><!-- IMPORTANTE NON ELIMINARE CLASSE -->
    <template v-if="riferimento.COD_RUBRICA">
        <input type="hidden" class="form-control nameToBuildPayload" name="COD_RUBRICA" v-model="riferimento.COD_RUBRICA" disabled>
    </template>
    <input id="tipoAnagrafica" type="hidden" class="form-control" value="RUBRICA">
    <div class="form-group col-md-12">
        <label class="labelNew">{{messages["listaAnagrafiche.tipoRubrica"] || "Tipo rubrica"}}</label>
        <select id="tipoRubrica" class="form-control nameToBuildPayload" name="TIPO_RUBRICA" v-model="riferimento.TIPO_RUBRICA">
          <option v-for="option in options" v-bind:value="option.value">
            {{ option.text }}
          </option>
        </select>
    </div>
    <template v-if="riferimento.TIPO_RUBRICA">
        <div class="form-group col-md-6">
            <label v-if="riferimento.TIPO_RUBRICA=='Persona'" class="labelNew">{{messages["listaAnagrafiche.denominazione"] || "Denominazione"}}</label>
            <label v-if="riferimento.TIPO_RUBRICA=='PersonaGiuridica'" class="labelNew">{{messages["listaAnagrafiche.ragioneSociale"] || "Ragione Sociale"}}</label>
            <label v-if="riferimento.TIPO_RUBRICA=='Amministrazione'" class="labelNew">{{messages["listaAnagrafiche.amministrazione"] || "Amministrazione"}}</label>
            <input type="text" class="form-control nameToBuildPayload" name="DES_RUBRICA" v-model="riferimento.DES_RUBRICA">
        </div>
        <div v-if="riferimento.TIPO_RUBRICA=='Amministrazione'" class="form-group col-md-6">
            <label class="labelNew">{{messages["listaAnagrafiche.ufficio"] || "Ufficio"}}</label>
            <input type="text" class="form-control nameToBuildPayload" name="AOO_UFFICIO" v-model="riferimento.AOO_UFFICIO">
        </div>
        <div v-if="riferimento.TIPO_RUBRICA=='Persona'" class="form-group col-md-6">
            <label class="labelNew">{{messages["listaAnagrafiche.codiceFiscale"] || "Codice Fiscale"}}</label>
            <input type="text" class="form-control cfCheck nameToBuildPayload" name="CODICE_FISCALE" v-model="riferimento.CODICE_FISCALE">
        </div>
        <div v-if="riferimento.TIPO_RUBRICA=='PersonaGiuridica'" class="form-group col-md-6">
            <label class="labelNew">{{messages["listaAnagrafiche.partitaIva"] || "Partita Iva"}}</label>
            <input type="text" class="form-control pivaCheck nameToBuildPayload" name="PARTITA_IVA" v-model="riferimento.PARTITA_IVA">
        </div>
        <div class="form-group col-md-6">
            <label class="labelNew">{{messages["listaAnagrafiche.indirizzoPec"] || "Indirizzo Pec"}}</label>
            <input type="text" class="form-control emailCheck nameToBuildPayload" name="INDIRIZZO_PEC" v-model="riferimento.INDIRIZZO_PEC">
        </div>
        <div v-if="riferimento.TIPO_RUBRICA=='Persona'" class="form-group col-md-6">
            <label class="labelNew">{{messages["listaAnagrafiche.indirizzoPeo"] || "Indirizzo Peo"}}</label>
            <input type="text" class="form-control emailCheck nameToBuildPayload" name="INDIRIZZO_PEO" v-model="riferimento.INDIRIZZO_PEO">
        </div>
        <div class="form-group col-md-6">
            <label class="labelNew">{{messages["listaAnagrafiche.indirizzoPostale"] || "Indirizzo Postale"}}</label>
            <input type="text" class="form-control nameToBuildPayload" name="INDIRIZZO_POSTALE" v-model="riferimento.INDIRIZZO_POSTALE">
        </div>
        <div v-if="riferimento.TIPO_RUBRICA=='Persona'" class="form-group col-md-6">
            <label class="labelNew">{{messages["listaAnagrafiche.fax"] || "Fax"}}</label>
            <input type="text" class="form-control nameToBuildPayload" name="FAX" v-model="riferimento.FAX">
        </div>
    </template>
</div>
