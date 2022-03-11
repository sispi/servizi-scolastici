import { FindUserByUsername } from '/static/portal/js/services/user.service.js?no-cache';
import { UpdateUser } from '/static/portal/js/services/user.service.js?no-cache';
import { DateAdapterForInput } from '/static/utilities/date.utilities.js?no-cache';
Vue.component('App', { template: `
<div class="container">
    <h2>Il Mio Profilo</h2>

    <div class="alert alert-warning alert-dismissible fade show" role="alert" v-if="errors.length">
        Questi campi sono obbligatori:
        <ul>
            <li v-for="error in errors">{{ error }}</li>
        </ul>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>

    <div v-if="!serverError">
        <ul class="nav nav-tabs" id="myTab" role="tablist">
            <li class="nav-item">
                <a @click="hideBtnEdit()" class="nav-link active" id="tab1-tab" data-toggle="tab" href="#tab1" role="tab" aria-controls="tab1" aria-selected="true">
                    I tuoi dati anagrafici
                </a>
            </li>
            <li class="nav-item">
                <a @click="showBtnEdit()" class="nav-link" id="tab2-tab" data-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false">
                    I tuoi dati di residenza
                </a>
            </li>
            <li class="nav-item">
                <a @click="showBtnEdit()" class="nav-link" id="tab3-tab" data-toggle="tab" href="#tab3" role="tab" aria-controls="tab3" aria-selected="false">
                    I tuoi recapiti
                </a>
            </li>
        </ul>
        <br />
        <form @submit="update">
            <div class="tab-content" id="myTabContent">
                <div class="tab-pane p-4 fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="FIRST_NAME" v-model="user.FIRST_NAME" :disabled="!modify || disabledMod" required placeholder="Nome" />
                            <label for="FIRST_NAME">Nome</label>
                            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                        </div>
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="LAST_NAME" v-model="user.LAST_NAME" :disabled="!modify || disabledMod" required placeholder="Cognome" />
                            <label for="LAST_NAME">Cognome</label>
                            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="FISCAL_CODE" v-model="user.FISCAL_CODE" :disabled="!modify || disabledMod" required placeholder="Codice fiscale" />
                            <label for="FISCAL_CODE">Codice fiscale</label>
                            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                        </div>
                        <div class="it-datepicker-wrapper col-md-6">
                            <div class="form-group">
                                <input class="form-control it-date-datepicker" id="date1" type="date" v-model="birthdayDate" :disabled="!modify || disabledMod" required />
                                <label for="date1">La tua data di nascita</label>
                                <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="BIRTH_PLACE" v-model="user.BIRTH_PLACE" :disabled="!modify || disabledMod" required placeholder="Nazione di nascita" />
                            <label for="BIRTH_PLACE">Luogo di nascita</label>
                            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                        </div>
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" id="BIRTH_COUNTY" v-model="user.BIRTH_COUNTY" :disabled="!modify || disabledMod" required placeholder="Provincia di nascita" />
                            <label for="BIRTH_COUNTY">Provincia di nascita</label>
                            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                        </div>
                        <div class="form-group col-md-3">
                            <div class="form-check form-check-inline">
                                <input name="SEX" type="radio" id="man" value="M" v-model="user.SEX" :disabled="!modify || disabledMod" required />
                                <label for="man">Uomo</label>
                                <input name="SEX" type="radio" id="woman" value="W" v-model="user.SEX" :disabled="!modify || disabledMod" />
                                <label for="woman">Donna</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="tab-pane p-4 fade" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
                    <div class="form-row">
                        <div class="form-group col-md-12">
                            <input type="text" class="form-control" id="ADDRESS_STREET" v-model="user.ADDRESS_STREET" :disabled="!modify" required placeholder="Indirizzo di residenza" />
                            <label for="ADDRESS_STREET">Indirizzo di residenza</label>
                            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="ADDRESS_MUNICIPALITY" v-model="user.ADDRESS_MUNICIPALITY" :disabled="!modify" required placeholder="Comune di residenza" />
                            <label for="ADDRESS_MUNICIPALITY">Comune di residenza</label>
                            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                        </div>
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" id="ADDRESS_PROVINCE" v-model="user.ADDRESS_PROVINCE" :disabled="!modify" required placeholder="Provincia di residenza" />
                            <label for="ADDRESS_PROVINCE">Provincia di residenza</label>
                            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                        </div>
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" id="ADDRESS_POSTALCODE" v-model="user.ADDRESS_POSTALCODE" :disabled="!modify" required placeholder="CAP" />
                            <label for="ADDRESS_POSTALCODE">CAP</label>
                            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                        </div>
                    </div>
                </div>
                <div class="tab-pane p-4 fade" id="tab3" role="tabpanel" aria-labelledby="tab3-tab">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="email" class="form-control" id="EMAIL_ADDRESS" v-model="user.EMAIL_ADDRESS" :disabled="!modify" required placeholder="Email" />
                            <label for="EMAIL_ADDRESS">Email</label>
                            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                        </div>
                        <div class="form-group col-md-6">
                            <input type="email" class="form-control" id="DIGITAL_ADDRESS" v-model="user.DIGITAL_ADDRESS" :disabled="!modify" placeholder="PEC" />
                            <label for="DIGITAL_ADDRESS">PEC</label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="tel" class="form-control" id="TELEPHONE" v-model="user.TELEPHONE" :disabled="!modify" placeholder="Telefono" />
                            <label for="TELEPHONE">Telefono</label>
                        </div>
                    </div>
                </div>
            </div>
            <button type="submit" class="btn btn-primary btn-sm btn-block" v-if="modify">Salva</button>
        </form>
        <button style="display: none;" id="btn-edit" type="button" v-on:click="switchModify" class="btn btn-secondary btn-sm btn-block" v-if="!modify">Modifica</button>
    </div>
    <div v-else class="alert alert-danger" role="alert">
        Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
    </div>
</div>
`, data() {
        return {
            user: null,
            disabledMod: true,
            errors: [],
            notFound: false,
            serverError: false,
            modify: false,
            birthdayDate: null
        }
    },
    methods: {
        update: function() {
            if(this.checkForm()){
                const u = {
                    ADDRESS_STREET: this.user.ADDRESS_STREET,
                    ADDRESS_MUNICIPALITY: this.user.ADDRESS_MUNICIPALITY,
                    ADDRESS_PROVINCE: this.user.ADDRESS_PROVINCE,
                    ADDRESS_POSTALCODE: this.user.ADDRESS_POSTALCODE,
                    EMAIL_ADDRESS: this.user.EMAIL_ADDRESS,
                    DIGITAL_ADDRESS: this.user.DIGITAL_ADDRESS,
                    TELEPHONE: this.user.TELEPHONE,
                    USER_ID: this.user.USER_ID,
                    id: this.user.id
                };
                this.modify = !this.modify;
                const response = UpdateUser(u);
            }
        },
        switchModify: function(){
            this.modify = !this.modify;
        },
        checkForm: function(){
            if(this.user.FIRST_NAME && this.user.LAST_NAME && this.user.FISCAL_CODE && this.user.BIRTH_DATE &&
                this.user.SEX && this.user.ADDRESS_PROVINCE && this.user.BIRTH_COUNTY && this.user.BIRTH_PLACE &&
                this.user.ADDRESS_MUNICIPALITY && this.user.ADDRESS_POSTALCODE && this.user.ADDRESS_STREET &&
                this.user.EMAIL_ADDRESS){
                return true;
            }
            this.errors = [];
            if (!this.user.FIRST_NAME) {
                this.errors.push('Nome richiesto.');
            }
            if (!this.user.LAST_NAME) {
                this.errors.push('Cognome richiesto.');
            }
            if (!this.user.FISCAL_CODE) {
                this.errors.push('Codice fiscale richiesto.');
            }
            if (!this.user.BIRTH_DATE) {
                this.errors.push('Data di nascita richiesta.');
            }
            if (!this.user.SEX) {
                this.errors.push('Sesso richiesto.');
            }
            if (!this.user.ADDRESS_PROVINCE) {
                this.errors.push('Provincia di residenza richiesta.');
            }
            if (!this.user.BIRTH_COUNTY) {
                this.errors.push('Provincia di nascita richiesta.');
            }
            if (!this.user.BIRTH_PLACE) {
                this.errors.push('Luogo di nascita richiesta.');
            }
            if (!this.user.ADDRESS_MUNICIPALITY) {
                this.errors.push('Comune di residenza richiesta.');
            }
            if (!this.user.ADDRESS_POSTALCODE) {
                this.errors.push('CAP richiesto.');
            }
            if (!this.user.ADDRESS_STREET) {
                this.errors.push('Indirizzo di residenza richiesto.');
            }
            if (!this.user.EMAIL_ADDRESS) {
                this.errors.push('Email richiesta.');
            }
        },
        hideBtnEdit: function() {
            $('#btn-edit').hide();
        },
        showBtnEdit: function() {
            $('#btn-edit').show();
        }
    },
    created(){
        const response = FindUserByUsername(username);
        if(response.status === 'success'){
            this.user = response.data;
            this.birthdayDate = DateAdapterForInput(this.user.BIRTH_DATE);
        } else {
            this.serverError = true;
        }
    }
});
new Vue({ el: "#app" });
