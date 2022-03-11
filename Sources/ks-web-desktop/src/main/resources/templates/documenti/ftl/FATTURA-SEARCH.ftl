<hr>
<div class="${query.type}"><!-- IMPORTANTE NON ELIMINARE CLASSE -->
    <div class="card-body">
        <div class="row">
            <div class="col-md-3">
                <label class="labelNew">{{messages["searchDocument.numeroFattura"] || "Numero fattura"}}</label>
                <input class="form-control nameToBuildPayload" type="text" name="NUMERO_FATT" v-model="paramSearch.NUMERO_FATT">
            </div>
            <div class="col-md-3">
                <label class="labelNew">{{messages["searchDocument.importoFattura"] || "Importo fattura"}}</label>
                <input class="form-control nameToBuildPayload" type="text" name="IMPORTO_FATT" v-model="paramSearch.IMPORTO_FATT">
            </div>
            <div class="col-md-3">
                <label class="labelNew">{{messages["searchDocument.intestatarioFattura"] || "Intestatario fattura"}}</label>
                <input class="form-control nameToBuildPayload" type="text" name="INTESTATARIO_FATT" v-model="paramSearch.INTESTATARIO_FATT">
            </div>
            <div class="col-md-3">
                <label class="labelNew">{{messages["searchDocument.partitaIva"] || "Partita IVA"}}</label>
                <input class="form-control nameToBuildPayload" type="text" name="PIVA_FATT" v-model="paramSearch.PIVA_FATT">
            </div>
        </div>
        <div class="space-1h"></div>
    </div>
</div>