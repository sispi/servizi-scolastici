<!-- VIEW -->
<div class="card content-grey hiddenIfEdit">
    <div class="row col-lg-12 col-md-12 col-sm-12">
        <div class="col-lg-12 col-md-12 col-sm-12">
            <p class="title-proto"><i class="fas fa-receipt blk"></i> {{messages["viewProfile.fattura"] || "Fattura"}} <br></p>
            <div class="space-h"></div>
            <p class="desc-proto"><i class="fas fa-ellipsis-v grey"></i> {{messages["viewProfile.numFattura"] || "Numero fattura"}}: <strong>{{documento.NUMERO_FATT}}</strong></p>
            <hr>
            <p class="desc-proto"><i class="fas fa-ellipsis-v grey"></i> {{messages["viewProfile.importoFattura"] || "Importo fattura"}}: <strong>{{documento.IMPORTO_FATT}}</strong><br></p>
            <hr>
            <p class="desc-proto"><i class="fas fa-ellipsis-v grey"></i> {{messages["viewProfile.intestatario"] || "Intestatario"}}: <strong>{{documento.INTESTATARIO_FATT}}</strong><br></p>
            <hr>
            <p class="desc-proto"><i class="fas fa-ellipsis-v grey"></i> {{messages["viewProfile.pIva"] || "Partita IVA"}}: <strong>{{documento.PIVA_FATT}}</strong><br></p>
        </div>
    </div>
</div>


<!-- EDIT -->
<hr>
<div class="hiddenIfView">
    <div class="form-group">
        <div class="row">
            <div class="col-md-6">
                <span class="labelNew">{{messages["viewProfile.numFattura"] || "Numero fattura"}}</span>
                <input type="text" class="form-control nameToBuildPayload" name="NUMERO_FATT" v-model="documento.NUMERO_FATT" >
            </div>
            <div class="col-md-6">
                <span class="labelNew">{{messages["viewProfile.importoFattura"] || "Importo Fattura"}}</span>
                <input type="text" class="form-control nameToBuildPayload" name="IMPORTO_FATT" v-model="documento.IMPORTO_FATT" >
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="row">
            <div class="col-md-6">
                <span class="labelNew">{{messages["viewProfile.intestatarioFattura"] || "Intestatario fattura"}}</span>
                <input type="text" class="form-control nameToBuildPayload" name="INTESTATARIO_FATT" v-model="documento.INTESTATARIO_FATT" >
            </div>
            <div class="col-md-6">
                <span class="labelNew">{{messages["viewProfile.pIvaFattura"] || "Partita IVA fattura"}}</span>
                <input type="text" class="form-control nameToBuildPayload" name="PIVA_FATT" v-model="documento.PIVA_FATT" >
            </div>
        </div>
    </div>
</div>