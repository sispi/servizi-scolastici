import { FindOneMedia } from '/static/portal/js/services/media.service.js?no-cache';
var App = Vue.component('App', {
    template: `
<div class="container">
    <div v-if="!uriError">
        <div v-if="!serverError">
            <h2>{{media.name}}</h2>
            <h5>{{media.description}}</h5>
            <dl>
                <dt>Tipo del file</dt>
                <dd>{{media.fileType}}</dd>
                <dt>Grandezza del file</dt>
                <dd>{{formattNumber(media.size / 1024)}} Kb</dd>
                <dt>Immagine</dt>
                <dd class="table-responsive">
                    <img :src="'data:' + media.fileType + ';base64,' + media.file" alt="immagine" style="max-height: 400px"/>
                </dd>
            </dl>
            <button type="button" class="btn btn-primary btn-sm" @click="returnBack()">
                <span class="glyphicon glyphicon-arrow-left"></span> Indietro
            </button>
        </div>
        <div v-else class="alert alert-danger" role="alert">
            Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
        </div>
    </div>
    <div v-else class="alert alert-danger" role="alert">
        Media non trovato
    </div>
</div>
`,
data(){
    return {
        media: null,
        serverError: false,
        uriError: false
    }
},
methods: {
    formattNumber: function(number){
        return number.toFixed(2);
    },
    returnBack: function(){
        window.history.back();
    }
},
created(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    if(urlParams.has('id')){
        const response = FindOneMedia(urlParams.get('id'));
        if(response.status === 'success'){
            this.media = response.data;
        } else {
            this.serverError = true;
        }
    } else {
        this.uriError = true;
    }
}
});
new Vue({ el: "#app" });
