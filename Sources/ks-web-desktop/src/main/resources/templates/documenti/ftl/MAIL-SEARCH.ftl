<hr>
<div class="${query.type}"><!-- IMPORTANTE NON ELIMINARE CLASSE -->
    <div class="card-body">
        <div class="row">
            <div class="col-md-5">
                <label class="labelNew">{{messages["searchDocument.oggettoMail"] || "Oggetto mail"}}</label>
                <input class="form-control nameToBuildPayload" type="text" name="MAIL_SUBJECT" v-model="paramSearch.MAIL_SUBJECT">
            </div>
            <div class="col-md-7">
                <label class="labelNew">{{messages["searchDocument.testoMail"] || "Testo mail"}}</label>
                <input class="form-control nameToBuildPayload" type="text" name="MAIL_BODY" v-model="paramSearch.MAIL_BODY">
            </div>
        </div>
        <div class="space-1h"></div>
        <div class="row">
            <div class="col-md-5">
                <label class="labelNew">{{messages["searchDocument.mittenteMail"] || "Mittente mail"}}</label>
                <input class="form-control nameToBuildPayload" type="text" name="MAIL_FROM" v-model="paramSearch.MAIL_FROM">
            </div>
            <div class="col-md-5">
                <label class="labelNew">{{messages["searchDocument.destinatariMail"] || "Destinatari mail"}}</label>
                <input class="form-control nameToBuildPayload" type="text" name="MAIL_TO" v-model="paramSearch.MAIL_TO">
            </div>
            <div class="col-md-2">
                <label class="labelNew">{{messages["searchDocument.dataMail"] || "Data mail"}}</label>
                <input class="form-control nameToBuildPayload" type="date" name="MAIL_DATE" v-model="paramSearch.MAIL_DATE">
            </div>
        </div>
        <div class="space-1h"></div>
    </div>
</div>