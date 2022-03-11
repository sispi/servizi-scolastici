import { SendContactUsEmail } from '/static/portal/js/services/mail.service.js?no-cache';

Vue.component('App', { template: `
<div class="container">
    <h2>Contattaci</h2>
    <p class="lead">
        Se hai delle richieste specifiche oppure non riesci a trovare risposta alle tue domande,
        usa questo form per contattarci.
    </p>
    <div class="alert alert-danger" role="alert" v-if="resError">
        Errore durante l'invio della email, riprovare più tardi.
    </div>
    
    <div class="alert alert-warning" role="alert" v-if="errors.length">
        Questi campi sono obbligatori:
        <ul>
            <li v-for="error in errors">{{ error }}</li>
        </ul>
    </div>

    <div class="card-wrapper">
        <div class="card">
            <div class="card-body">
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="username">Nome</label>
                        <input v-if="contactUs.firstName" @change="checkValidation('firstName',contactUs.firstName)" type="text" class="form-control is-valid" id="firstName" v-model="contactUs.firstName" /> 
                        <input v-else @change="checkValidation('firstName',contactUs.firstName)" type="text" class="form-control is-invalid" id="firstName" v-model="contactUs.firstName" />
                        <div v-if="!contactUs.firstName" class="invalid-feedback">* Campo obbligatorio.</div>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="username">Cognome</label>
                        <input v-if="contactUs.lastName" @change="checkValidation('lastName',contactUs.lastName)" type="text" class="form-control is-valid" id="lastName" v-model="contactUs.lastName" />
                        <input v-else @change="checkValidation('lastName',contactUs.lastName)" type="text" class="form-control is-invalid" id="lastName" v-model="contactUs.lastName" />
                        <div v-if="!contactUs.lastName" class="invalid-feedback">* Campo obbligatorio.</div>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-6">
                        <label for="email">Email</label>
                        <input v-if="contactUs.from" @change="checkValidation('from',contactUs.from)" type="email" class="form-control is-valid" id="from" v-model="contactUs.from" />
                        <input v-else @change="checkValidation('from',contactUs.from)" type="email" class="form-control is-invalid" id="from" v-model="contactUs.from" />
                        <div v-if="!contactUs.from" class="invalid-feedback">* Campo obbligatorio.</div>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="email">Telefono</label>
                        <input v-if="contactUs.telephone" @change="checkValidation('telephone',contactUs.telephone)" type="text" class="form-control is-valid" id="telephone" v-model="contactUs.telephone" />
                        <input v-else @change="checkValidation('telephone',contactUs.telephone)" type="text" class="form-control is-invalid" id="telephone" v-model="contactUs.telephone" />
                        <div v-if="!contactUs.telephone" class="invalid-feedback">* Campo obbligatorio.</div>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-12">
                        <label for="subject">Oggetto</label>
                        <input @change="checkValidation('subject',contactUs.subject)" type="text" class="form-control is-invalid" id="subject" v-model="contactUs.subject" />
                        <div class="invalid-feedback">* Campo obbligatorio.</div>
                    </div>
                </div>
                <!--
                <div class="row">
                    <div class="form-group col-md-12">
                        <label for="subject">Oggetto da menu a tendina</label>
                        <select class="form-control" v-model="selected">
                          <option v-for="option in options" v-bind:value="option.value">
                            {{ option.text }}
                          </option>
                        </select>
                    </div>
                </div>
                -->
                <div class="row">
                    <div class="form-group col-md-12">
                        <label for="message">Messaggio (<span>{{ remaincharactersText }}</span>)</label>
                        <textarea @keyup='remaincharCount()' @change="checkValidation('message',contactUs.body)" id="message" rows="3" required placeholder="Scrivi qui il tuo messaggio..." class="form-control is-invalid"  v-model="contactUs.body"></textarea>
                        <div class="invalid-feedback">* Campo obbligatorio.</div>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary mt-3" v-on:click="getFormValues">Invia Email</button>
            </div>
        </div>
    </div>
</div>
`,
    data() {
    var user = userInfo.claims;
        return {
            contactUs: {
                subject: null,
                body: null,
                from: user.email,
                telephone: user.TELEPHONE,
                firstName: user.FIRST_NAME,
                lastName: user.LAST_NAME,
            },
            maxcharacter: 1000,
            remaincharactersText: " Caratteri rimanenti 1000 ",
            selected: 'email1@gmail.com',
            options: [
                { text: 'Problemi nei pagamenti', value: 'email1@gmail.com' },
                { text: 'Problema inserimento dati', value: 'email2@gmail.com' },
                { text: 'Altro', value: 'email3@gmail.com' }
            ],
            errors: [],
            resError: false
        };
    },
    mounted: function () {
        var u = userInfo.claims;
        u.FIRST_NAME?$("#firstName").attr("disabled", true):$("#firstName").attr("disabled", false);
        u.LAST_NAME?$("#lastName").attr("disabled", true):$("#lastName").attr("disabled", false);
        u.email?$("#from").attr("disabled", true):$("#from").attr("disabled", false);
        u.TELEPHONE?$("#telephone").attr("disabled", true):$("#telephone").attr("disabled", false);
    },
    methods: {

        remaincharCount: function(){
            var message = this.contactUs.body;
            if(message.length > this.maxcharacter){
                this.remaincharactersText = " Limite massimo caratteri superato ";
            }else{
                var remainCharacters = this.maxcharacter - message.length;
                this.remaincharactersText = " Caratteri rimanenti " + remainCharacters + " ";
            }
        },
        getFormValues: function() {
            if(this.validateForm()){
                // calcolo name
                this.contactUs["name"] = this.contactUs["firstName"] + " " + this.contactUs["lastName"];
                //elimino i 2 campi uniti in 'name'
                delete this.contactUs["firstName"];
                delete this.contactUs["lastName"];
                // abilitare le 2 righe sottostanti quando il menù a tendina per subject è visibile
                //delete this.contactUs["subject"];
                //this.contactUs.subject = this.selected;
                const response = SendContactUsEmail(this.contactUs);
                if(response.status === 'success'){
                    this.showNotification();
                } else {
                    this.resError = true;
                }
            }
        },
        validateForm: function(){
            if(
                this.contactUs.subject &&
                this.contactUs.body &&
                this.contactUs.from &&
                this.contactUs.telephone &&
                this.contactUs.firstName &&
                this.contactUs.lastName &&
                this.contactUs.body.length<=1000
            ){
                return true;
            } else {
                this.errors = [];
                if (this.contactUs.body.length>1000) {
                    this.errors.push('Numero caratteri consentito per il messaggio superato');
                }
                if (!this.contactUs.subject) {
                    this.errors.push('Oggetto');
                }
                if (!this.contactUs.body) {
                    this.errors.push('Messaggio');
                }
                if (!this.contactUs.from) {
                    this.errors.push('Email');
                }
                if (!this.contactUs.telephone) {
                    this.errors.push('Telefono');
                }
                if (!this.contactUs.firstName) {
                    this.errors.push('Nome');
                }
                if (!this.contactUs.lastName) {
                    this.errors.push('Cognome');
                }
                return false;
            }
        },
        showNotification: function(){
            alert("Email inviata");
        },
        checkValidation: function (id, value) {
            if(value){
                $('#'+id).removeClass("is-invalid").addClass("is-valid");
            }else{
                $('#'+id).removeClass("is-valid").addClass("is-invalid");
            }
        },
    }
});
new Vue({ el: "#app" });
