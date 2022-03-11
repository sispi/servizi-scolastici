<!-- VIEW -->
<div class="card content-grey hiddenIfEdit">
    <div class="box-title">
        <div class="title-info">
            <p class="desc-title">{{messages["viewProfile.Da"] || "Da"}}</p>
            <p class="desc-info">{{documento.MAIL_FROM}}</p>
        </div>
        <div class="title-info">
            <p class="desc-title">{{messages["viewProfile.A"] || "A"}}</p>
            <p class="desc-info">{{documento.MAIL_TO}}</p>
        </div>
        <template v-if="documento.MAIL_CC && documento.TYPE_ID != ''">
            <div class="title-info">
                <p class="desc-title">{{messages["viewProfile.CC"] || "CC"}}</p>
                <p class="desc-info">{{documento.MAIL_CC}}</p>
            </div>
        </template>
        <div class="title-info">
            <p class="desc-title">{{messages["viewProfile.dataInvio"] || "Data invio"}}</p>
            <p class="desc-info">{{date(documento.MAIL_DATE)}}</p>
        </div>
        <template v-if="documento.MAIL_STATE">
        <div class="title-info">
            <p class="desc-title">{{messages["viewProfile.statoMail"] || "Stato Mail"}}</p>
            <template v-if="documento.MAIL_STATE == '1'">
                <p class="desc-info">{{messages["viewProfile.entrata"] || "ENTRATA"}} </p>
            </template>
            <template v-if="documento.MAIL_STATE == '2'">
                <p class="desc-info">{{messages["viewProfile.archiviata"] || "ARCHIVIATA"}} </p>
            </template>
            <template v-if="documento.MAIL_STATE == '3'">
                <p class="desc-info">{{messages["viewProfile.scartata"] || "SCARTATA"}} </p>
            </template>
            <template v-if="documento.MAIL_STATE == '4'">
                <p class="desc-info">{{messages["viewProfile.uscita"] || "IN USCITA"}} </p>
            </template>
            <template v-if="documento.MAIL_STATE == '5'">
                <p class="desc-info">{{messages["viewProfile.consegnata"] || "CONSEGNATA"}} </p>
            </template>
            <template v-if="documento.MAIL_STATE == '6'">
                <p class="desc-info">{{messages["viewProfile.accettata"] || "ACCETTATA"}} </p>
            </template>
            <template v-if="documento.MAIL_STATE == '7'">
                <p class="desc-info">{{messages["viewProfile.errore"] || "IN ERRORE"}} </p>
            </template>
            <template v-if="documento.MAIL_STATE == '8'">
                <p class="desc-info">{{messages["viewProfile.conferma"] || "CONFERMA"}} </p>
            </template>
        </div>
        </template>
    </div>
    <div class="col-lg-10 col-md-10 col-sm-12">
        <p class="title-mail"><strong> <i style="color: #000" class="fas fa-envelope-open-text"></i> {{documento.MAIL_SUBJECT}}</strong></p>
        <!-- filtro per sostituire \n con <br>-->
        <p class="body-mail" v-html="this.$options.filters.wrapText(documento.MAIL_BODY)"></p>
    </div>
</div>

<!-- EDIT -->
<hr>
<div class="hiddenIfView">
    <div class="form-group">
        <div class="row">
            <div class="col-md-6">
                <span class="labelNew">{{messages["viewProfile.mittente"] || "Mittente"}}</span>
                <input type="text" class="form-control nameToBuildPayload" name="MAIL_FROM" v-model="documento.MAIL_FROM" >
            </div>
            <div class="col-md-6">
                <span class="labelNew">{{messages["viewProfile.destinatari"] || "Destinatari"}}</span>
                <input type="text" class="form-control nameToBuildPayload" name="MAIL_TO" v-model="documento.MAIL_TO" >
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="row">
            <div class="col-md-12">
                <span class="labelNew">{{messages["viewProfile.oggetto"] || "Oggetto"}}</span>
                <input type="text" class="form-control nameToBuildPayload" name="MAIL_SUBJECT" v-model="documento.MAIL_SUBJECT" >
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="labelNew">{{messages["viewProfile.messaggio"] || "Messaggio"}}</label>
        <textarea class="form-control nameToBuildPayload" rows="4" name="MAIL_BODY" v-model="documento.MAIL_BODY"></textarea>
    </div>
</div>