<div class="row">
    <div class="col-md-8">
        <label class="labelNew">{{messages["searchDocument.nomeDocumento"] || "Nome documento"}}</label>
        <input class="form-control nameToBuildPayload" type="text" name="DOCNAME" v-model="paramSearch.DOCNAME">
    </div>
    <div class="col-md-4">
        <label class="labelNew">{{messages["searchDocument.docnum"] || "Docnum"}}</label>
        <input class="form-control nameToBuildPayload" type="text" name="DOCNUM" v-model="paramSearch.DOCNUM">
    </div>
</div>
<div class="space-1h"></div>
<div class="row">
    <div class="col-md-4">
        <label class="labelNew">{{messages["searchDocument.creatoDa"] || "Creato da"}}</label>
        <input class="form-control nameToBuildPayload" type="text" name="CREATOR" v-model="paramSearch.CREATOR">
    </div>
    <div class="col-md-4">
        <label class="labelNew">{{messages["searchDocument.creatoDal"] || "Creato dal"}}</label>
        <input class="form-control" type="datetime-local" v-model="paramSearch.creatoDal">
    </div>
    <div class="col-md-4">
        <label class="labelNew">{{messages["searchDocument.al"] || "al"}}</label>
        <input class="form-control" type="datetime-local" v-model="paramSearch.creatoAl">
    </div>
</div>
<div class="space-1h"></div>
<div class="row">
    <div class="col-md-4">
        <label class="labelNew">{{messages["searchDocument.modificatoDa"] || "Modificato da"}}</label>
        <input class="form-control nameToBuildPayload" type="text" name="MODIFIER" v-model="paramSearch.MODIFIER">
    </div>
    <div class="col-md-4">
        <label class="labelNew">{{messages["searchDocument.modificatoDal"] || "Modificato dal"}}</label>
        <input class="form-control" type="datetime-local" v-model="paramSearch.modificatoDal">
    </div>
    <div class="col-md-4">
        <label class="labelNew">{{messages["searchDocument.al"] || "al"}}</label>
        <input class="form-control" type="datetime-local" v-model="paramSearch.modificatoAl">
    </div>
</div>