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
                        <label for="username">Il tuo nome</label>
                        <input @change="checkValidation('username',contactUs.name)" type="text" class="form-control is-invalid" id="username" v-model="contactUs.name" />
                        <div class="invalid-feedback">* Campo obbligatorio.</div>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="email">La tua email</label>
                        <input @change="checkValidation('email',contactUs.from)" type="email" class="form-control is-invalid" id="email" v-model="contactUs.from" />
                        <div class="invalid-feedback">* Campo obbligatorio.</div>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-12">
                        <label for="object">Oggetto</label>
                        <input @change="checkValidation('object',contactUs.subject)" type="text" class="form-control is-invalid" id="object" v-model="contactUs.subject" />
                        <div class="invalid-feedback">* Campo obbligatorio.</div>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-12">
                        <label for="message">Messaggio</label>
                        <textarea @change="checkValidation('message',contactUs.body)" id="message" rows="3" required placeholder="Messaggio" class="form-control is-invalid"  v-model="contactUs.body"></textarea>
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
        return {
            contactUs: {
                subject: null,
                body: null,
                from: null,
                name: null
            },
            errors: [],
            resError: false
        };
    },
    methods: {
        getFormValues: function() {
            if(this.validateForm()){
                const response = SendContactUsEmail(this.contactUs);
                if(response.status === 'success'){
                    this.showNotification();
                    this.contactUs = {
                        subject: null,
                        body: null,
                        from: null,
                        name: null
                    }
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
                this.contactUs.name
            ){
                return true;
            } else {
                this.errors = [];
                if (!this.contactUs.subject) {
                    this.errors.push('Oggetto');
                }
                if (!this.contactUs.body) {
                    this.errors.push('Messaggio');
                }
                if (!this.contactUs.from) {
                    this.errors.push('La tua email');
                }
                if (!this.contactUs.name) {
                    this.errors.push('Il tuo nome');
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
