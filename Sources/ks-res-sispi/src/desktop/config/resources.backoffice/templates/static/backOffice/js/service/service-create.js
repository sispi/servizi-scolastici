import { CreateService } from '/static/portal/js/services/service.service.js?no-cache';
import { FindAllMediaPaged } from '/static/portal/js/services/media.service.js?no-cache';
var App = Vue.component('App', {
    template: `
<div class="container">
    <h2>Crea un nuovo Servizio</h2>
    <div class="alert alert-danger alert-dismissible fade show" role="alert" id="alertError" v-if="resError">
        Errore durante la creazione del servizio.
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <div class="alert alert-warning alert-dismissible fade show" role="alert" v-if="errors.length">
        Questi campi sono obbligatori:
        <ul>
            <li v-for="error in errors">{{ error }}</li>
        </ul>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <div class="form-group">
        <label for="parent">Servizio Padre</label>
        <input type="text" class="form-control" id="parent" placeholder="Servizio padre" v-model="serviceName" readonly>
    </div>
    <div class="form-group">
        <label for="name">Nome</label>
        <input @change="checkValidation('name',name)" type="text" class="form-control is-invalid" id="name" placeholder="Nome" v-model="name">
        <div class="invalid-feedback">* Campo obbligatorio.</div>
    </div>
    <div class="form-group">
        <label for="code">Codice</label>
        <input @change="checkValidation('code',code)" type="text" class="form-control is-invalid" id="code" placeholder="Codice" v-model="code">
        <div class="invalid-feedback">* Campo obbligatorio.</div>
    </div>
    <!--<div class="form-group">
        <label>Logo</label>
        <div class="custom-file">
            <input type="file" class="custom-file-input" id="logo" @change="setLogoName">
            <label class="custom-file-label" for="logo">{{logoName}}</label>
        </div>
    </div>-->

    <div class="form-group">
        <label for="media">Logo</label>
        <select id="media" class="form-control" v-model="media">
            <option v-bind:value="null"></option>
            <option v-for="media in medias" v-bind:value="media">{{media.name}}</option>
        </select>
    </div>

    <div class="form-check">
        <input @change="showHideCustomInput()" type="checkbox" class="form-check-input" id="externalService" v-model="externalService">
        <label class="form-check-label" for="externalService">Servizio Esterno</label>
    </div>
    <div class="form-group">
        <input @change="checkValidation('link',link)" type="text" class="form-control is-invalid" id="link" placeholder="Link" v-model="link">
        <div id="labelLink" class="invalid-feedback">* Campo obbligatorio.</div>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" id="valid" v-model="valid">
        <label class="form-check-label" for="valid">Attivo</label>
    </div>
    <br>
    <button type="submit" class="btn btn-primary" v-on:click="createService">Salva</button>
    <button type="button" class="btn btn-secondary" v-on:click="goBack">Annulla</button>
</div>
`,
    data(){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        var serviceId = null;
        var serviceName = null;
        if(urlParams.has('serviceId')){
            serviceId = urlParams.get('serviceId');
            if(urlParams.has('serviceName')){
                serviceName = urlParams.get('serviceName');
            }
        }
        return {
            serviceName: serviceName,
            parentId: serviceId,
            name: null,
            code: null,
            link: null,
            externalService: false,
            valid: false,
            service: null,
            logo: null,
            logoName: 'Scegli file',
            parents: data("data-serviceList"),
            errors: [],
            resError: false,
            medias: [],
            media: null
        }
    },
    created(){
        const response = FindAllMediaPaged();
        if(response.status === 'success'){
            const pagedList = response.data;
            this.medias = pagedList.data;
        }
    },
    mounted: function () {
        if(this.name){
            $('#name').removeClass("is-invalid").addClass("is-valid");
        }
        if(this.code){
            $('#code').removeClass("is-invalid").addClass("is-valid");
        }
        if(this.externalService==true){
            $('#link').show();
            $('#labelLink').hide();
            $('#link').removeClass("is-invalid").addClass("is-valid");
        }else{
            $('#link').hide();
            $('#labelLink').hide();
        }
    },
    methods: {
        createService: function(){
            const parent = this.parentId;
            const name = this.name;
            const code = this.code;
            const link = this.link;
            const extService = this.externalService;
            const valid = this.valid;
            if (this.name && this.code && ((this.externalService==true && this.link)||(this.externalService==false) ) ) {
                //var file = document.getElementById("logo").files[0];
                var file = this.media;
                if(file){
                    const b64 = 'data:' + file.fileType + ';base64,' + file.file;
                    this.service = {
                        'parentId': parent,
                        'name': name,
                        'code': code,
                        'link': link,
                        'externalService': extService,
                        'valid': valid,
                        'logo': b64
                    }
                    const res = CreateService(this.service);
                    if(res.status === 'success'){
                        const message = 'Il servizio ' + name + ' è stato creato.'
                        location.href = "/backOffice/service/service?success=" + message;
                    } else {
                        const message = 'Errore durante la creazione del servizio.'
                        location.href = "/backOffice/service/service?error=" + message;
                    }

                    /*var reader = new FileReader();
                    var image = null;
                    reader.readAsDataURL(file);
                    reader.onloadend = function () {
                        var type = document.getElementById("logo").files[0].type;
                        image = {
                            'name': document.getElementById("logo").files[0].name,
                            'file': reader.result.toString().split(",")[1],
                            'fileType': type,
                            'mimeType': type.toString().split("/")[1],
                            'size': document.getElementById("logo").files[0].size
                        }
                        const b64 = 'data:' + image.fileType + ';base64,' + image.file;
                        this.service = {
                            'parentId': parent,
                            'name': name,
                            'code': code,
                            'link': link,
                            'externalService': extService,
                            'valid': valid,
                            'logo': b64
                        }
                        const res = CreateService(this.service);
                        if(res.status === 'success'){
                            const message = 'Il servizio ' + name + ' è stato creato.'
                            location.href = "/backOffice/service/service?success=" + message;
                        } else {
                            const message = 'Errore durante la creazione del servizio.'
                            location.href = "/backOffice/service/service?error=" + message;
                        }
                    }*/
                } else {
                    this.service = {
                        'parentId': parent,
                        'name': name,
                        'code': code,
                        'link': link,
                        'externalService': extService,
                        'valid': valid,
                        'logo': null
                    }
                    const res = CreateService(this.service);
                    if(res.status === 'success'){
                        const message = 'Il servizio ' + name + ' è stato creato.'
                        location.href = "/backOffice/service/service?success=" + message;
                    } else {
                        this.resError = true;
                    }
                }
            } else {
                this.errors = [];
                if (!this.name) {
                    this.errors.push('Nome');
                }
                if (!this.code) {
                    this.errors.push('Codice');
                }
                if (!this.link && this.externalService==true) {
                    this.errors.push('Link');
                }
            }
        },
        setLogoName: function(){
            this.logoName = document.getElementById("logo").files[0].name;
        },
        goBack: function(){
            window.history.back();
        },
        checkValidation: function (id, value) {
            if(value){
                $('#'+id).removeClass("is-invalid").addClass("is-valid");
            }else{
                $('#'+id).removeClass("is-valid").addClass("is-invalid");
            }
        },
        showHideCustomInput: function () {
            if(this.externalService){
                $('#link').show();
                $('#labelLink').show();
            }else{
                $('#link').hide();
                $('#labelLink').hide();
                this.link="";
                $('#link').removeClass("is-valid").addClass("is-invalid");
            }
        },
    }
});
new Vue({ el: "#app" });
