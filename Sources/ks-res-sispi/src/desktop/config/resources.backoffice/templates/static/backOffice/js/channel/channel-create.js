import { FindAllProviders } from '/static/portal/js/services/provider.service.js?no-cache';
import { CreateChannel } from '/static/portal/js/services/channel.service.js?no-cache';
import { FindOneChannel } from '/static/portal/js/services/channel.service.js?no-cache';
import { UpdateChannel } from '/static/portal/js/services/channel.service.js?no-cache';
var App = Vue.component('App', {
    template: `
<div class="container">
    <h2>Crea un nuovo Canale</h2>
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
            <label for="id">Id</label>
            <input type="number" class="form-control" id="id" placeholder="Id" required v-model="channelId" disabled>
        </div>
        <div class="form-group">
            <label for="name">Nome del Canale</label>
            <input type="text" class="form-control" id="name" aria-describedby="nameRequired" placeholder="Nome" required v-model="channel.name">
            <div class="invalid-feedback">
                Questo campo è obbligatorio.
            </div>
        </div>
        <div class="form-group">
            <label for="provider">Provider</label>
            <select id="provider" class="form-control" aria-describedby="providerRequired" required v-model="channel.providerId">
                <option v-bind:value="null"></option>
                <option v-for="provider in providers" v-bind:value="provider.id">{{ provider.name }}</option>
            </select>
            <div class="invalid-feedback">
                Questo campo è obbligatorio.
            </div>
        </div>
        <button type="submit" class="btn btn-primary" @click="createChannel">Salva</button>
        <button type="button" class="btn btn-secondary" v-on:click="goBack">Annulla</button>
</div>
`,
data(){
    const response = FindAllProviders();
    if(response.status === 'success'){
        return {
            providers: response.data,
            channel: {
                name: null,
                providerId: null
            },
            channelId: null,
            resError: false,
            errors: [],
            isUpdate: false
        }
    }
},
methods: {
    createChannel: function(){
        if(this.validateForm()){
            if(this.isUpdate){
                const response = UpdateChannel(this.channel, this.channelId);
                if(response.status === 'success'){
                    const message = "Il Canale '" + this.channel.name + "' è stato modificato.";
                    window.location.href = "/backOffice/channel/channel?success=" + message;
                } else {
                    this.resError = true;
                }
            } else {
                const response = CreateChannel(this.channel);
                if(response.status === 'success'){
                    const message = "Il Canale '" + this.channel.name + "' è stato creato.";
                    window.location.href = "/backOffice/channel/channel?success=" + message;
                } else {
                    this.resError = true;
                }
            }
        }
    },
    goBack: function(){
        window.history.back();
    },
    validateForm: function(){
        if(this.channel.name && this.channel.providerId){
            return true;
        } else {
            this.errors = [];
            if (!this.channel.name) {
                this.errors.push('Nome');
            }
            if (!this.channel.providerId) {
                this.errors.push('Provider');
            }
            return false;
        }
    },
    findChannel: function(id){
        const response = FindOneChannel(id);
        if(response.status === 'success'){
            const c = response.data;
            this.channel = {
                name: c.name,
                providerId: c.provider.id
            };
        }
    }
},
created() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    var id = null;
    if(urlParams.has('id')){
        id = urlParams.get('id');
    }
    this.channelId = id;
    if(this.channelId != null){
        this.isUpdate = true;
        this.findChannel(this.channelId);
    }
}
});
new Vue({ el: "#app" });
