import { CreateMedia } from '/static/portal/js/services/media.service.js?no-cache';
import { FindOneMedia } from '/static/portal/js/services/media.service.js?no-cache';
import { UpdateMedia } from '/static/portal/js/services/media.service.js?no-cache';
var App = Vue.component('App', {
    template: `
<div class="container">
    <h2 v-if="!isUpdate">Crea un nuovo Media</h2>
    <h2 v-else>Aggiorna un Media</h2>
    <div v-if="!serverError">
        <div class="alert alert-danger alert-dismissible fade show" role="alert" id="alertError" v-if="resError">
            Errore durante la creazione del media.
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
            <label for="name">Nome</label>
            <input @change="checkValidation('name',name)" type="text" class="form-control is-invalid" id="name" placeholder="Nome" v-model="name">
            <div class="invalid-feedback">* Campo obbligatorio.</div
        </div>
        <br>
        <div class="form-group">
            <label for="description">Descrizione</label>
            <input type="text" class="form-control" id="description" placeholder="Descrizione" v-model="description">
        </div>
        <div class="form-group">
            <label>File</label>
            <div class="custom-file" v-if="!isUpdate">
                <input type="file" accept="image/*" class="custom-file-input" id="file" @change="setFileName">
                <label class="custom-file-label" for="file">{{fileName}}</label>
            </div>

            <div v-else>
                <div class="row" v-if="!removed">
                    <div class="col-md-8">
                        <img :src="'data:' + oldMedia.fileType + ';base64,' + oldMedia.file" alt="immagine" style="max-height: 48px; max-width: 48px;"/>
                    </div>
                    <div class="col-md-4 text-right">
                        <button type="button" class="btn btn-danger" v-on:click="removeMedia"><span class="glyphicon glyphicon-trash"></span> Rimuovi</button>
                    </div>
                </div>
                <div class="custom-file" v-else>
                    <input type="file" accept="image/*" class="custom-file-input" id="file" @change="setFileName">
                    <label class="custom-file-label" for="file">{{fileName}}</label>
                </div>
            </div>
            
        </div>
        <button type="submit" class="btn btn-primary" v-on:click="createService">
            <span class="glyphicon glyphicon-floppy-disk"></span> Salva
        </button>
        <button type="button" class="btn btn-secondary" v-on:click="goBack">
            <span class="glyphicon glyphicon-remove"></span> Annulla
        </button>
    </div>
    <div v-else class="alert alert-danger" role="alert">
        Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
    </div>
    
</div>
`,
    data(){
        return {
            fileName: 'Scegli file',
            errors: [],
            resError: false,
            name: null,
            description: null,
            serverError: false,
            isUpdate: false,
            removed: false,
            oldMedia: null,
            isLogo: false
        }
    },
    methods: {
        removeMedia: function(){
            this.removed = true;
        },
        createService: function(){
            if (this.name) {
                const isLogo = this.isLogo;
                var myKey = null;
                if(isLogo){
                    myKey = 'logo';
                }
                if(this.isUpdate){
                    this.update(myKey);
                } else {
                    var file = document.getElementById("file").files[0];
                    if(file){
                        const name = this.name;
                        const description = this.description;
                        var reader = new FileReader();
                        var image = null;
                        reader.readAsDataURL(file);
                        reader.onloadend = function () {
                            var type = document.getElementById("file").files[0].type;
                            const fileImage = type.toString().split("/")[0];
                            if(fileImage === 'image'){
                                image = {
                                    'name': document.getElementById("file").files[0].name,
                                    'file': reader.result.toString().split(",")[1],
                                    'fileType': type,
                                    'mimeType': type.toString().split("/")[1],
                                    'size': document.getElementById("file").files[0].size
                                }
                                //const b64 = 'data:' + image.fileType + ';base64,' + image.file;
                                var media = {
                                    'name': name,
                                    'description': description,
                                    'file': image.file,
                                    'fileType': image.fileType,
                                    'mimeType': image.mimeType,
                                    'size': image.size,
                                    'myKey': myKey
                                };
                                const res = CreateMedia(media);
                                if(res.status === 'success'){
                                    const message = 'Il media ' + media.name + ' è stato creato.';
                                    location.href = "/backOffice/media/media?success=" + message;
                                } else {
                                    const message = 'Errore durante la creazione del media.';
                                    location.href = "/backOffice/media/media?error=" + message;
                                }
                            } else {
                                alert('Questo formato del file non è supportato');
                            }
                        }
                    } else {
                        this.errors = [];
                        this.errors.push('File');
                    }
                }
            } else {
                this.errors = [];
                if (!this.name) {
                    this.errors.push('Nome');
                }
            }
        },
        setFileName: function(){
            this.fileName = document.getElementById("file").files[0].name;
        },
        update: function(myKey){
            const oldMedia = this.oldMedia;
            const name = this.name;
            const description = this.description;
            if(this.removed){
                var file = document.getElementById("file").files[0];
                if(file){
                    var reader = new FileReader();
                    var image = null;
                    reader.readAsDataURL(file);
                    reader.onloadend = function () {
                        var type = document.getElementById("file").files[0].type;
                        const fileImage = type.toString().split("/")[0];
                        if(fileImage === 'image'){
                            image = {
                                'name': document.getElementById("file").files[0].name,
                                'file': reader.result.toString().split(",")[1],
                                'fileType': type,
                                'mimeType': type.toString().split("/")[1],
                                'size': document.getElementById("file").files[0].size
                            }
                            //const b64 = 'data:' + image.fileType + ';base64,' + image.file;
                            var media = {
                                'id': oldMedia.id,
                                'name': name,
                                'description': description,
                                'file': image.file,
                                'fileType': image.fileType,
                                'mimeType': image.mimeType,
                                'size': image.size,
                                'myKey': myKey
                            };
                            var res = UpdateMedia(media);
                            if(res.status === 'success'){
                                const message = 'Il media ' + media.name + ' è stato creato.';
                                location.href = "/backOffice/media/media?success=" + message;
                            } else {
                                const message = 'Errore durante la creazione del media.';
                                location.href = "/backOffice/media/media?error=" + message;
                            }
                        } else {
                            alert('Questo formato del file non è supportato');
                        }
                    }
                } else {
                    this.errors = [];
                    this.errors.push('File');
                }
            } else {
                const media = {
                    'id': oldMedia.id,
                    'name': name,
                    'description': description,
                    'file': oldMedia.file,
                    'fileType': oldMedia.fileType,
                    'mimeType': oldMedia.mimeType,
                    'size': oldMedia.size,
                    'myKey': myKey
                };
                const res = UpdateMedia(media);
                if(res.status === 'success'){
                    const message = 'Il media ' + media.name + ' è stato modificato.';
                    location.href = "/backOffice/media/media?success=" + message;
                } else {
                    const message = 'Errore durante la modifica del media.';
                    location.href = "/backOffice/media/media?error=" + message;
                }
            }
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
    },
    mounted: function () {
        if(this.name){
            $('#name').removeClass("is-invalid").addClass("is-valid");
        }
    },
    created(){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        if(urlParams.has('logo')){
            const isLogo = urlParams.get('logo');
            if(isLogo === 'true'){
                console.log('logo');
                this.isLogo = true;
            }
        }
        if(urlParams.has('id')){
            const response = FindOneMedia(urlParams.get('id'));
            if(response.status === 'success'){
                this.isUpdate = true;
                this.oldMedia = response.data;
                this.name = this.oldMedia.name;
                this.description = this.oldMedia.description;
            } else {
                this.serverError = true;
            }
        }
    }
});
new Vue({ el: "#app" });
