var App = Vue.component('App', { template: `<div class="container">
    <h2>Registrazione</h2>

    <ul class="nav nav-tabs" id="myTab" role="tablist">
        <li class="nav-item">
            <a class="nav-link active" id="tab1-tab" data-toggle="tab" href="#tab1" role="tab" aria-controls="tab1" aria-selected="true">
                I tuoi dati anagrafici
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab2-tab" data-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false">
                I tuoi dati di residenza
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab3-tab" data-toggle="tab" href="#tab3" role="tab" aria-controls="tab3" aria-selected="false">
                I tuoi recapiti
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab4-tab" data-toggle="tab" href="#tab4" role="tab" aria-controls="tab4" aria-selected="false">
                Le tue credenziali
            </a>
        </li>
    </ul>

    <div class="tab-content" id="myTabContent">
        <div class="tab-pane p-4 fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
            <div class="form-row">
                <div class="form-group col-md-6">
                    <input type="text" class="form-control" id="name" v-model="name" />
                    <label for="name">Il tuo nome</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
                <div class="form-group col-md-6">
                    <input type="text" class="form-control" id="surname" v-model="surname" />
                    <label for="surname">Il tuo cognome</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <input type="text" class="form-control" id="fiscalCode" v-model="fiscalCode" />
                    <label for="fiscalCode">Il tuo codice fiscale</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
                <div class="it-datepicker-wrapper col-md-6">
                    <div class="form-group">
                        <input class="form-control " id="date1" type="date" placeholder="gg/mm/aaaa" v-model="birthday" />
                        <label for="date1">La tua data di nascita</label>
                        <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <input type="text" class="form-control" id="birthdayCountry" v-model="birthdayCountry" />
                    <label for="birthdayCountry">Il tuo comune di nascita</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
                <div class="form-group col-md-6">
                    <div class="form-check form-check-inline">
                        <input name="sex" type="radio" id="man" value="man" v-model="sex" />
                        <label for="man">Uomo</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input name="sex" type="radio" id="woman" value="woman" v-model="sex" />
                        <label for="woman">Donna</label>
                    </div>
                </div>
            </div>
        </div>
        <div class="tab-pane p-4 fade" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
            <div class="form-row">
                <div class="form-group col-md-6">
                    <div class="bootstrap-select-wrapper">
                        <label>Nazione di residenza</label>
                        <select v-model="selectedNation">
                            <option v-for="nation in nations" v-bind:value="nation.value">{{nation.nationName}}</option>
                        </select>
                    </div>
                </div>
                <div class="form-group col-md-6">
                    <div class="bootstrap-select-wrapper">
                        <label>Provincia di residenza</label>
                        <select v-model="selectedProvince">
                            <option v-for="province in provinces" v-bind:value="province.value">{{province.provinceName}}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <input type="text" class="form-control" id="residenceCountry" v-model="residenceCountry" />
                    <label for="residenceCountry">Comune di residenza</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
                <div class="form-group col-md-6">
                    <input type="text" class="form-control" id="postalCode" v-model="postalCode" />
                    <label for="postalCode">CAP</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-12">
                    <input type="text" class="form-control" id="residenceAddress" v-model="residenceAddress" />
                    <label for="residenceAddress">Indirizzo di residenza</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
            </div>
        </div>
        <div class="tab-pane p-4 fade" id="tab3" role="tabpanel" aria-labelledby="tab3-tab">
            <div class="form-row">
                <div class="form-group col-md-6">
                    <input type="email" class="form-control" id="email" v-model="email" />
                    <label for="email">La tua email</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
                <div class="form-group col-md-6">
                    <input type="email" class="form-control" id="pec" v-model="pec" />
                    <label for="pec">La tua PEC</label>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <input type="tel" class="form-control" id="telephone" v-model="telephone" />
                    <label for="telephone">Il tuo telefono</label>
                </div>
            </div>
        </div>
        <div class="tab-pane p-4 fade" id="tab4" role="tabpanel" aria-labelledby="tab4-tab">
            <div class="form-row">
                <div class="form-group col-md-6">
                    <input type="text" class="form-control" id="login" v-model="login" />
                    <label for="login">La tua login</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
                <div class="form-group col-md-6">
                    <input type="text" class="form-control" id="traceCode" v-model="traceCode" placeholder="Trace code" />
                    <label for="traceCode">Trace code</label>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <input type="password" class="form-control" id="password" v-model="password" placeholder="Password" />
                    <label for="password">La tua password</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
                <div class="form-group col-md-6">
                    <input type="password" class="form-control" id="confirmPassword" v-model="confirmPassword" placeholder="Conferma password" />
                    <label for="confirmPassword">Conferma la tua password</label>
                    <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
                </div>
            </div>
            <p class="text-secondary">
                Lapassword deve essere composta da:
            </p>

            <ul>
                <li>Minimo 8 caratteri alfanumerici</li>
                <li>Almeno un carattere maiuscolo</li>
                <li>Almeno un carattere minuscolo</li>
            </ul>
            <button type="button" v-on:click="getFormValues" class="btn btn-primary btn-lg btn-block mt-3">Salva</button>
        </div>
    </div>
</div>
`, data() { 
    return { 
        name: null, 
        surname: null,
        fiscalCode: null,
        birthday: '09/11/2020',
        birthdayCountry: null,
        sex: null,
        nations: [ 
            { 
                nationName: 'Italia',
                value: 'Italia' 
            }, 
            { 
                nationName: 'Spagna',
                value: 'Spagna' 
            }, 
            { 
                nationName: 'Francia',
                value: 'Francia' 
            } 
        ],
        provinces: [ 
            { 
                provinceName: 'L\'Aquila',
                value: 'L\'Aquila' 
            }, 
            { 
                provinceName: 'Pescara',
                value: 'Pescara' 
            }, 
            { 
                provinceName: 'Teramo',
                value: 'Teramo' 
            }, 
            { 
                provinceName: 'Chieti',
                value: 'Chieti' 
            } 
        ],
        selectedNation: null,
        selectedProvince: null,
        residenceCountry: null,
        residenceAddress: null,
        postalCode: null,
        email: null,
        pec: null,
        telephone: null,
        login: null,
        traceCode: null,
        password: null,
        confirmPassword: null,
        output: null 
    }; 
}, 
methods: { 
    getFormValues: function() { 
        this.output = { 
            'name': this.name, 
            'surname': this.surname, 
            'fiscalCode': this.fiscalCode, 
            'birthday': this.birthday,
            'birthdayCountry': this.birthdayCountry,
            'sex' : this.sex,
            'selectedNation': this.selectedNation,
            'selectedProvince': this.selectedProvince,
            'residenceCountry': this.residenceCountry,
            'residenceAddress': this.residenceAddress,
            'postalCode': this.postalCode,
            'email': this.email,
            'pec': this.pec,
            'telephone': this.telephone,
            'login': this.login,
            'traceCode': this.traceCode,
            'password': this.password,
            'confirmPassword': this.confirmPassword
        }; 
        console.log(jQuery.parseJSON(JSON.stringify(this.output))); 
    } 
} }); 
new Vue({ el: "#app" });
