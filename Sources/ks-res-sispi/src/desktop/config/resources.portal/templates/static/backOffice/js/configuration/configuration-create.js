import { CreateChannelConfiguration } from '/static/portal/js/services/channel.service.js?no-cache';
import { FindOneChannelConfiguration } from '/static/portal/js/services/configuration.service.js?no-cache';
import { UpdateChannelConfiguration } from '/static/portal/js/services/configuration.service.js?no-cache';
var App = Vue.component('App', {
    template: `
<div class="container">
    <h2>Crea una nuova Configurazione</h2>
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
            <input type="number" class="form-control" id="id" placeholder="Id" required v-model="configurationId" disabled>
        </div>
        <div class="form-group">
            <label for="secretKey">Chiave segreta</label>
            <input type="text" class="form-control" id="secretKey" placeholder="Chiave segreta" required v-model="configuration.secretKey">
            <div class="invalid-feedback">
                Questo campo è obbligatorio.
            </div>
        </div>
        <div class="form-group">
            <label for="secretUser">Utente segreto</label>
            <input type="text" class="form-control" id="secretUser" placeholder="Utente segreto" required v-model="configuration.secretUser">
            <div class="invalid-feedback">
                Questo campo è obbligatorio.
            </div>
        </div>
        <div class="form-group">
            <label for="servicePassword">Password di servizio</label>
            <input type="password" class="form-control" id="servicePassword" placeholder="Password di servizio" required v-model="configuration.servicePassword">
            <div class="invalid-feedback">
                Questo campo è obbligatorio.
            </div>
        </div>
        <div class="form-check">
            <input type="checkbox" class="form-check-input" id="showPassword" @click="showPassword()">
            <label class="form-check-label" for="showPassword">Mostra password</label>
        </div>
        <div class="form-group">
            <label for="terminalId">Terminale</label>
            <input type="text" class="form-control" id="terminalId" placeholder="Terminale" required v-model="configuration.terminalId">
            <div class="invalid-feedback">
                Questo campo è obbligatorio.
            </div>
        </div>
        <div class="form-group">
            <label for="currencyCode">Codice valuta</label>
            <input type="text" class="form-control" id="currencyCode" placeholder="Codice valuta" required v-model="configuration.currencyCode">
            <div class="invalid-feedback">
                Questo campo è obbligatorio.
            </div>
        </div>
        <div class="form-group">
            <label for="langId">Lingua</label>
            <select id="langId" class="form-control" required v-model="configuration.langId">
                <option value="null"></option>
                <option value="it">Italiano</option>
                <option value="en">Inglese</option>
            </select>
            <div class="invalid-feedback">
                Questo campo è obbligatorio.
            </div>
        </div>
        <div class="form-group">
            <label for="cashMode">Modalità cash</label>
            <input type="text" class="form-control" id="cashMode" placeholder="Modalità cash" required v-model="configuration.cashMode">
            <div class="invalid-feedback">
                Questo campo è obbligatorio.
            </div>
        </div>
        <div class="form-group">
            <div class="custom-control custom-switch">
                <input type="checkbox" class="custom-control-input" id="paymentEnabled" v-model="configuration.paymentEnabled">
                <label class="custom-control-label" for="paymentEnabled">Pagamento abilitato</label>
            </div>
        </div>
        
        <button type="submit" class="btn btn-primary" @click="createConfiguration">Salva</button>
        <button type="button" class="btn btn-secondary" v-on:click="goBack">Annulla</button>
</div>
`,
data(){
    return {
        configuration: {
            paymentEnabled: false,
            secretKey: null,
            secretUser: null,
            servicePassword: null,
            terminalId: null,
            currencyCode: null,
            langId: null,
            cashMode: null,
            defaultConf: false
        },
        channelId: null,
        configurationId: null,
        resError: false,
        errors: [],
        isUpdate: false
    }
},
methods: {
    createConfiguration: function(){
        if(this.validateForm()){
            if(this.isUpdate){
                const response = UpdateChannelConfiguration(this.configuration, this.configurationId);
                if(response.status === 'success'){
                    const message = "La Configurazione '" + this.configuration.terminalId + "' è stata modificata.";
                    window.location.href = "/backOffice/configuration/configuration?id=" + this.channelId + "&success=" + message;
                } else {
                    this.resError = true;
                }
            } else {
                const response = CreateChannelConfiguration(this.configuration, this.channelId);
                if(response.status === 'success'){
                    const message = "Una nuova Configurazione è stata creata per questo canale.";
                    window.location.href = "/backOffice/configuration/configuration?id=" + this.channelId + "&success=" + message;
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
        if(
            this.configuration.secretKey &&
            this.configuration.secretUser &&
            this.configuration.servicePassword &&
            this.configuration.terminalId && 
            this.configuration.currencyCode &&
            this.configuration.langId &&
            this.configuration.cashMode
            ){
            return true;
        } else {
            this.errors = [];
            if (!this.configuration.secretKey) {
                this.errors.push('Chiave segreta');
            }
            if (!this.configuration.secretUser) {
                this.errors.push('Utente segreto');
            }
            if (!this.configuration.servicePassword){
                this.errors.push('Password di servizio');
            }
            if (!this.configuration.terminalId){
                this.errors.push('Terminale');
            }
            if (!this.configuration.currencyCode){
                this.errors.push('Codice valuta');
            }
            if (!this.configuration.langId){
                this.errors.push('Lingua');
            }
            if (!this.configuration.cashMode){
                this.errors.push('Modalità cash');
            }
            return false;
        }
    },
    showPassword: function(){
        var x = document.getElementById("servicePassword");
        if (x.type === "password") {
            x.type = "text";
        } else {
            x.type = "password";
        }
    },
    findConfiguration: function(id){
        const response = FindOneChannelConfiguration(id);
        if(response.status === 'success'){
            const c = response.data;
            this.configuration = {
                paymentEnabled: c.paymentEnabled,
                secretKey: c.secretKey,
                secretUser: c.secretUser,
                servicePassword: c.servicePassword,
                terminalId: c.terminalId,
                currencyCode: c.currencyCode,
                langId: c.langId,
                cashMode: c.cashMode,
                channelId: c.channel.id,
                defaultConf: c.defaultConf
            };
        }
    }
},
created() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    var channelId = null;
    var confId = null;
    if(urlParams.has('channelId')){
        channelId = urlParams.get('channelId');
        if(urlParams.has('configurationId')){
            confId = urlParams.get('configurationId');
        }
    }
    this.channelId = channelId;
    this.configurationId = confId;
    if(this.configurationId != null){
        this.isUpdate = true;
        this.findConfiguration(this.configurationId);
    }
}
});
new Vue({ el: "#app" });
