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
                    Dati anagrafici
                </a>
            </li>
            <li class="nav-item">
                <a @click="showBtnEdit()" class="nav-link" id="tab2-tab" data-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false">
                    Dati di contatto
                </a>
            </li>
            <li class="nav-item">
                <a @click="showBtnEdit()" class="nav-link" id="tab3-tab" data-toggle="tab" href="#tab3" role="tab" aria-controls="tab3" aria-selected="false">
                    Dati di fatturazione
                </a>
            </li>
            <li style="display: none" class="nav-item">
                <a @click="hideBtnEdit()" class="nav-link" id="tab4-tab" data-toggle="tab" href="#tab4" role="tab" aria-controls="tab4" aria-selected="false">
                    Dati ISEE
                </a>
            </li>
        </ul>
        <br />
        <form @submit.prevent>
            <div class="tab-content" id="myTabContent">
                <div class="tab-pane p-4 fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="FIRST_NAME" v-model="user.FIRST_NAME" :disabled="!modify || disabledMod" placeholder="Nome" />
                            <label for="FIRST_NAME">Nome</label>
                        </div>
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="LAST_NAME" v-model="user.LAST_NAME" :disabled="!modify || disabledMod" placeholder="Cognome" />
                            <label for="LAST_NAME">Cognome</label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="FISCAL_CODE" v-model="user.FISCAL_CODE" :disabled="!modify || disabledMod" placeholder="Codice fiscale" />
                            <label for="FISCAL_CODE">Codice fiscale</label>
                        </div>
                        <div class="it-datepicker-wrapper col-md-6">
                            <div class="form-group">
                                <input class="form-control it-date-datepicker" id="date1" type="date" v-model="birthdayDate" :disabled="!modify || disabledMod" />
                                <label for="date1">La tua data di nascita</label>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="BIRTH_PLACE" v-model="user.BIRTH_PLACE" :disabled="!modify || disabledMod" placeholder="Nazione di nascita" />
                            <label for="BIRTH_PLACE">Luogo di nascita</label>
                        </div>
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" id="BIRTH_COUNTY" v-model="user.BIRTH_COUNTY" :disabled="!modify || disabledMod" placeholder="Provincia di nascita" />
                            <label for="BIRTH_COUNTY">Provincia di nascita</label>
                        </div>
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" id="SEX" v-model="user.SEX" :disabled="!modify || disabledMod"/>
                            <label for="SEX">Sesso</label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-10">
                            <input type="text" class="form-control" id="ADDRESS_STREET" v-model="user.ADDRESS_STREET" :disabled="!modify || disabledMod" placeholder="Indirizzo di residenza" />
                            <label for="ADDRESS_STREET">Indirizzo di residenza</label>
                        </div>
                        <div class="form-group col-md-2">
                            <input type="text" class="form-control" id="ADDRESS_STREET" v-model="user.ADDRESS_CNUMBER" :disabled="!modify || disabledMod" placeholder="Numero" />
                            <label for="ADDRESS_STREET">Numero</label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="ADDRESS_MUNICIPALITY" v-model="user.ADDRESS_MUNICIPALITY" :disabled="!modify || disabledMod" placeholder="Comune di residenza" />
                            <label for="ADDRESS_MUNICIPALITY">Comune di residenza</label>
                        </div>
                        <div class="form-group col-md-2">
                            <input type="text" class="form-control" id="ADDRESS_POSTALCODE" v-model="user.ADDRESS_POSTALCODE" :disabled="!modify || disabledMod" placeholder="CAP" />
                            <label for="ADDRESS_POSTALCODE">CAP</label>
                        </div>
                        <div class="form-group col-md-4">
                            <input type="tel" class="form-control" id="TELEPHONE" v-model="user.TELEPHONE" :disabled="!modify || disabledMod" placeholder="Telefono" />
                            <label for="TELEPHONE">Telefono</label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="email" class="form-control" id="EMAIL_ADDRESS" v-model="user.EMAIL_ADDRESS" :disabled="!modify || disabledMod" required placeholder="Email" />
                            <label for="EMAIL_ADDRESS">Email</label>
                        </div>
                        <div class="form-group col-md-6">
                            <input type="email" class="form-control" id="DIGITAL_ADDRESS" v-model="user.DIGITAL_ADDRESS" :disabled="!modify || disabledMod" placeholder="PEC" />
                            <label for="DIGITAL_ADDRESS">PEC</label>
                        </div>
                    </div>
                </div>
                <div class="tab-pane p-4 fade" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="email" class="form-control" id="CONTATTO_EMAIL_ADDRESS" v-model="user.CONTATTO_EMAIL_ADDRESS" :disabled="!modify" placeholder="Email" />
                            <label for="CONTATTO_EMAIL_ADDRESS">Email</label>
                        </div>
                        <div class="form-group col-md-6">
                            <input type="email" class="form-control" id="CONTATTO_DIGITAL_ADDRESS" v-model="user.CONTATTO_DIGITAL_ADDRESS" :disabled="!modify" placeholder="PEC" />
                            <label for="CONTATTO_DIGITAL_ADDRESS">PEC</label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="tel" class="form-control" id="CONTATTO_TELEPHONE" v-model="user.CONTATTO_TELEPHONE" :disabled="!modify" placeholder="Telefono" />
                            <label for="CONTATTO_TELEPHONE">Telefono</label>
                        </div>
                        <div class="form-group col-md-6">
                            <input type="tel" class="form-control" id="CONTATTO_TELEPHONE_HOME" v-model="user.CONTATTO_TELEPHONE_HOME" :disabled="!modify" placeholder="Telefono Casa" />
                            <label for="CONTATTO_TELEPHONE_HOME">Telefono Casa</label>
                        </div>
                    </div>
                </div>
                <div class="tab-pane p-4 fade" id="tab3" role="tabpanel" aria-labelledby="tab3-tab">
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="FATTURAZIONE_NOME" v-model="user.FATTURAZIONE_NOME" :disabled="!modify" placeholder="Nome" />
                            <label for="FATTURAZIONE_NOME">Nome</label>
                        </div>
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="FATTURAZIONE_COGNOME" v-model="user.FATTURAZIONE_COGNOME" :disabled="!modify" placeholder="Cognome" />
                            <label for="FATTURAZIONE_COGNOME">Cognome</label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="FATTURAZIONE_CF" v-model="user.FATTURAZIONE_CF" :disabled="!modify" placeholder="Codice Fiscale" />
                            <label for="FATTURAZIONE_CF">Codice Fiscale</label>
                        </div>
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" id="FATTURAZIONE_RESIDENZA" v-model="user.FATTURAZIONE_RESIDENZA" :disabled="!modify" placeholder="Residenza" />
                            <label for="FATTURAZIONE_RESIDENZA">Residenza</label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="email" class="form-control" id="FATTURAZIONE_MAIL" v-model="user.FATTURAZIONE_MAIL" :disabled="!modify" placeholder="Email" />
                            <label for="FATTURAZIONE_MAIL">Email</label>
                        </div>
                    </div>
                </div>
                
                <div class="tab-pane p-4 fade" id="tab4" role="tabpanel" aria-labelledby="tab4-tab">
                    <span class="row col-md-12" style="border-bottom: 1px solid #06c;margin-bottom: 40px;color: #06c;">Dichiarante</span>
                    <div class="form-row">
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" v-model="isee.cognomeDichiatanteISEE" placeholder="Cognome" />
                            <label for="cognomeDichiatanteISEE">Cognome</label>
                        </div>
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" v-model="isee.nomeDichiatanteISEE" placeholder="Nome" />
                            <label for="nomeDichiatanteSEE">Nome</label>
                        </div>
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" v-model="isee.cfDichiatanteISEE" placeholder="Codice Fiscale" />
                            <label for="cfDichiatanteISEE">Codice Fiscale</label>
                        </div> 
                        <div class="form-group col-md-3" style="margin-top: -1px;border-bottom: 1px solid;">
                            <label for="relazioneUtenteISEE">Relazione con l'utente</label>
                            <select style="color:#17324d;font-weight: bold" class="form-control" v-model="isee.relazioneUtenteISEE" >
                              <option v-for="option in isee.optionsRelazioneUtente" v-bind:value="option.value">
                                {{ option.text }}
                              </option>
                            </select>
                        </div> 
                    </div>
                    <span class="row col-md-12" style="border-bottom: 1px solid #06c;margin-bottom: 40px;color: #06c;">Componenti del nucleo familiare del dichiarante che usufruiscono dell'ISEE</span>
        
                    <div v-for="(familiare, index) in isee.familiari">
                        <div class="form-row">
                            <div class="form-group col-md-2" style="margin-top: -1px;border-bottom: 1px solid;">
                                <label for="familiare.relazioneDichiaranteISEE">Relazione con il dichiarante</label>
                                <select style="color:#17324d;font-weight: bold" class="form-control" v-model="familiare.relazioneDichiaranteISEE" >
                                  <option v-for="option in familiare.optionsRelazioneDichiarante" v-bind:value="option.value">
                                    {{ option.text }}
                                  </option>
                                </select>
                            </div> 
                            <div class="form-group col-md-3">
                                <input type="text" class="form-control" v-model="familiare.cognomeFamiliareISEE" placeholder="Cognome" />
                                <label for="familiare.cognomeFamiliareISEE">Cognome</label>
                            </div>
                            <div class="form-group col-md-3">
                                <input type="text" class="form-control" v-model="familiare.nomeFamiliareISEE" placeholder="Nome" />
                                <label for="familiare.nomeFamiliareISEE">Nome</label>
                            </div>
                            <div class="form-group col-md-3">
                                <input type="text" class="form-control" v-model="familiare.cfFamiliareISEE" placeholder="Codice Fiscale" />
                                <label for="familiare.cfFamiliareISEE">Codice Fiscale</label>
                            </div>
                            <div class="form-group col-md-1">
                                <button @click="deleteFamiliare(index)" class="btn btn-danger btn-sm btn-block"><i style="color: #fff;" class="fas fa-times"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-11">
                        </div>
                        <div class="form-group col-md-1">
                            <button @click="addFamiliare" class="btn btn-secondary btn-sm btn-block"><i style="color: #fff;" class="fas fa-plus"></i>
                            </button>
                        </div>
                    </div>
                    <br>
        
                    <span class="row col-md-12" style="border-bottom: 1px solid #06c;margin-bottom: 40px;color: #06c;">ISEE</span>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <input type="text" class="form-control" v-model="isee.codiceISEE" placeholder="codice ISEE" />
                            <label for="codiceISEE">Codice ISEE</label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-3" style="margin-top: -1px;border-bottom: 1px solid;">
                            <label for="tipoISEE">Tipo ISEE</label>
                            <select style="color:#17324d;font-weight: bold" class="form-control" v-model="isee.tipoISEE" >
                              <option v-for="option in isee.optionsTipoISEE" v-bind:value="option.value">
                                {{ option.text }}
                              </option>
                            </select>
                        </div> 
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" v-model="isee.valoreISEE" placeholder="Valore ISEE" />
                            <label for="valoreISEE">Valore ISEE</label>
                        </div>
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" v-model="isee.dataRilascioISEE" placeholder="Rilasciato il"  />
                            <label for="dataRilascioISEE">Rilasciato il</label>
                        </div>
                        <div class="form-group col-md-3">
                            <input type="text" class="form-control" v-model="isee.dataScadenzaISEE" placeholder="Valido fino al" />
                            <label for="dataScadenzaISEE">Valido fino al</label>
                        </div>
                    </div>
                    <button @click="updateIsee()" type="button" class="btn btn-secondary btn-sm btn-block">Aggiorna ISEE o componenti del nucleo familiare</button>
                </div>
                
                </div>
            <button v-on:click="update" type="button" class="btn btn-primary btn-sm btn-block" v-if="modify">Salva</button>
        </form>
        <button id="btn-edit" type="button" v-on:click="switchModify" class="btn btn-secondary btn-sm btn-block" v-if="!modify">Modifica</button>
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
            birthdayDate: null,
            isee: null
        }
    },
    methods: {
        update: function() {
            this.errors = [];
            //if(this.checkForm()){
            const u = {
                FATTURAZIONE_NOME: this.user.FATTURAZIONE_NOME?this.user.FATTURAZIONE_NOME:"__NULL__",
                FATTURAZIONE_COGNOME: this.user.FATTURAZIONE_COGNOME?this.user.FATTURAZIONE_COGNOME:"__NULL__",
                FATTURAZIONE_CF: this.user.FATTURAZIONE_CF?this.user.FATTURAZIONE_CF:"__NULL__",
                FATTURAZIONE_RESIDENZA: this.user.FATTURAZIONE_RESIDENZA?this.user.FATTURAZIONE_RESIDENZA:"__NULL__",
                FATTURAZIONE_MAIL: this.user.FATTURAZIONE_MAIL?this.user.FATTURAZIONE_MAIL:"__NULL__",
                CONTATTO_EMAIL_ADDRESS: this.user.CONTATTO_EMAIL_ADDRESS?this.user.CONTATTO_EMAIL_ADDRESS:"__NULL__",
                CONTATTO_DIGITAL_ADDRESS: this.user.CONTATTO_DIGITAL_ADDRESS?this.user.CONTATTO_DIGITAL_ADDRESS:"__NULL__",
                CONTATTO_TELEPHONE: this.user.CONTATTO_TELEPHONE?this.user.CONTATTO_TELEPHONE:"__NULL__",
                CONTATTO_TELEPHONE_HOME: this.user.CONTATTO_TELEPHONE_HOME?this.user.CONTATTO_TELEPHONE_HOME:"__NULL__",
                USER_ID: this.user.USER_ID,
                id: this.user.id
            };
            this.modify = !this.modify;
            const response = UpdateUser(u);
            //}
        },
        switchModify: function(){
            this.modify = !this.modify;
        },
        checkForm: function(){
            if(this.user.TELEPHONE && this.user.EMAIL_ADDRESS){
                return true;
            }
            this.errors = [];
            if (!this.user.TELEPHONE) {
                this.errors.push('Telefono richiesto.');
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
        },
        updateIsee: function () {
            if(this.checkFormIsee()){
                /* RIMUOVO LE OPTIONS
                delete this.isee["optionsRelazioneUtente"];
                delete this.isee["optionsTipoISEE"];
                var i = null;
                for(i=0;i<this.isee.familiari.length;i++) {
                    delete this.isee.familiari[i]["optionsRelazioneDichiarante"];
                }
                */
                this.errors = [];//azzero eventuali errori visualizzati
                alert(this.isee);
                //window.location.href = "/portal/features/instance-start?pid="+this.user.USER_ID;
            }
        },
        deleteFamiliare: function (index) {
            delete this.isee.familiari.splice(index, 1);
        },
        addFamiliare: function () {
            var fam =   {
                "cognomeFamiliareISEE":"",
                "nomeFamiliareISEE":"",
                "cfFamiliareISEE":"",
                "relazioneDichiaranteISEE": '1',
                optionsRelazioneDichiarante: [
                    { text: 'Figlio', value: '1' },
                    { text: 'Coniuge', value: '2' }
                ]
            };
            this.isee.familiari.push( fam );
        },
        checkFormIsee: function(){
            var i = null;
            if(this.isee.familiari.length>0){
                for(i=0;i<this.isee.familiari.length;i++) {
                    if(this.isee.familiari[i].cognomeFamiliareISEE && this.isee.familiari[i].nomeFamiliareISEE && this.isee.familiari[i].cfFamiliareISEE){
                        var emptyFigli = true;
                    }else{
                        var emptyFigli = false;
                    }
                }
            }else{
                var emptyFigli = true;
            }
            if(this.isee.cognomeDichiatanteISEE && this.isee.nomeDichiatanteISEE && this.isee.cfDichiatanteISEE && this.isee.codiceISEE && this.isee.valoreISEE && this.isee.dataRilascioISEE && this.isee.dataScadenzaISEE && emptyFigli){
                return true;
            }
            this.errors = [];
            if (!this.isee.cognomeDichiatanteISEE) {
                this.errors.push('Cognome richiesto.');
            }
            if (!this.isee.nomeDichiatanteISEE) {
                this.errors.push('Nome richiesto.');
            }
            if (!this.isee.cfDichiatanteISEE) {
                this.errors.push('Codice Fiscale richiesto.');
            }
            if (!this.isee.codiceISEE) {
                this.errors.push('Codice ISEE richiesto.');
            }
            if (!this.isee.valoreISEE) {
                this.errors.push('Valore ISEE richiesto.');
            }
            if (!this.isee.dataRilascioISEE) {
                this.errors.push('Data rilascio ISEE richiesta.');
            }
            if (!this.isee.dataScadenzaISEE) {
                this.errors.push('Data scadenza ISEE richiesta.');
            }
            if (!emptyFigli) {
                this.errors.push('Campi Familiari richiesti.');
            }
            if(this.errors.length>0){//vado in alto nella pag per far vedere gli errori
                $("html, body").animate({ scrollTop: 0 }, "slow");
            }
        },
    },
    mounted: function () {
        this.hideBtnEdit();
    },
    created(){
        const response = FindUserByUsername(username);
        if(response.status === 'success'){
            this.user = response.data;
            this.birthdayDate = DateAdapterForInput(this.user.BIRTH_DATE);
            this.user.CONTATTO_EMAIL_ADDRESS = this.user.CONTATTO_EMAIL_ADDRESS?this.user.CONTATTO_EMAIL_ADDRESS:this.user.EMAIL_ADDRESS;
            this.user.CONTATTO_DIGITAL_ADDRESS = this.user.CONTATTO_DIGITAL_ADDRESS?this.user.CONTATTO_DIGITAL_ADDRESS:this.user.DIGITAL_ADDRESS;
            this.user.CONTATTO_TELEPHONE = this.user.CONTATTO_TELEPHONE?this.user.CONTATTO_TELEPHONE:this.user.TELEPHONE;
        } else {
            this.serverError = true;
        }

        //dati di test, da recuperare quando sarà pronta l'API getISEE
        this.isee = {
            "cognomeDichiatanteISEE":"Cucchiari",
            "nomeDichiatanteISEE":"Stefano",
            "cfDichiatanteISEE":"CCCFRD67D05H501O",
            "relazioneUtenteISEE": '1',
            optionsRelazioneUtente: [
                { text: 'Me stesso', value: '1' },
                { text: 'Coniuge', value: '2' }
            ],
            familiari: [
                {
                    "cognomeFamiliareISEE":"Cucchiari",
                    "nomeFamiliareISEE":"Francesco",
                    "cfFamiliareISEE":"CDRFRD67D05H587Y",
                    "relazioneDichiaranteISEE": '1',
                    optionsRelazioneDichiarante: [
                        { text: 'Figlio', value: '1' },
                        { text: 'Coniuge', value: '2' }
                    ]
                },
                {
                    "cognomeFamiliareISEE":"Cucchiari",
                    "nomeFamiliareISEE":"Stella",
                    "cfFamiliareISEE":"CDRSTL67D05H787R",
                    "relazioneDichiaranteISEE": '2',
                    optionsRelazioneDichiarante: [
                        { text: 'Figlio', value: '1' },
                        { text: 'Coniuge', value: '2' }
                    ]
                }
            ],
            "codiceISEE": "INPS-ISEE-2021-0658745874-00",
            "tipoISEE": '1',
            optionsTipoISEE: [
                { text: 'Ordinario', value: '1' },
                { text: 'Speciale', value: '2' }
            ],
            "valoreISEE": "16.187,98",
            "dataRilascioISEE": "15/03/2021",
            "dataScadenzaISEE": "31/12/2022"
        }
    }
});
new Vue({ el: "#app" });
