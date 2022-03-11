import { FormatDate } from '/static/utilities/date.utilities.js?no-cache';

Vue.component('App', { template: `<div class="container">
    <h2>{{ instance.proceedings }}</h2>
    <div class="row">
        <div class="col-md-4">
            <p>
                <i class="fa fa-briefcase"></i>
                <strong>Stato: </strong> 
                Completato
            </p>
        </div>
        <div class="col-md-4">
            <p>
                <i class="fa fa-dot-circle"></i>
                <strong>Incaricato: </strong> {{ instance.accountableStaff }}
            </p>
        </div>
    </div>
    <ul class="nav nav-tabs" id="myTab" role="tablist">
        <li class="nav-item">
            <a class="nav-link active" id="tab1-tab" data-toggle="tab" href="#tab1" role="tab" aria-controls="tab1" aria-selected="true">
                Attività
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab2-tab" data-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false">
                Dettaglio
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab3-tab" data-toggle="tab" href="#tab3" role="tab" aria-controls="tab3" aria-selected="false">
                Pagamenti
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab3-tab" data-toggle="tab" href="#tab4" role="tab" aria-controls="tab4" aria-selected="false">
                Documenti
            </a>
        </li>
    </ul>
    <div class="tab-content" id="myTabContent">
        <div class="tab-pane p-4 fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
            <div class="col-md-12">
                <span style="font-size: 1.2rem;font-weight: bold;padding-left: 25px;" class="col-md-6 border-bottom float-left text-left"><span title="in carico Utente" class="glyphicon glyphicon-user"></span> Cittadino</span>
                <span style="font-size: 1.2rem;font-weight: bold;padding-right: 40px;" class="col-md-6 border-bottom float-right text-right"><span title="in carico Utente" class="glyphicon glyphicon-home"></span> Ente</span>
            </div>
            <br>
                <div v-for="activity in instance.activities" :key="activity.step">
                  <div class="container">
                    <!--no path-->
                    <div v-if="previous(activity).type == activity.type" class="row timeline"></div>
                    <!--path da sinistra a destra-->
                    <div v-else-if="previous(activity).type == 'HT-cittadino'" class="row timeline">
                        <div class="col-2"><div class="corner top-right"></div></div>
                        <div class="col-8"><hr/></div>
                        <div class="col-2"><div class="corner left-bottom"></div></div>
                    </div>
                    <!--path da destra a sinistra-->
                    <div v-else class="row timeline">
                        <div class="col-2"><div class="corner right-bottom"></div></div>
                        <div class="col-8"><hr/></div>
                        <div class="col-2"><div class="corner top-left"></div></div>
                    </div>

                    <!-- CITTADINO -->
                    <template v-if="activity.type == 'HT-cittadino'">
                      <div class="row align-items-center how-it-works d-flex cittadinoContent">
                        <div class="col-2 text-center bottom d-inline-flex justify-content-center align-items-center">
                          <div style="background-color: #008758" class="circle font-weight-bold">{{activity.step}}</div>
                        </div>
                        <div style="margin-top: 30px;margin-bottom: 30px;" class="col-6">
                          <strong>{{activity.label}}</strong> <br>
                          <span v-if="activity.timestamp"> completato il {{formatDate(activity.timestamp)}} </span>
                        </div>
                      </div>
                    </template>
                    <!-- ENTE -->
                    <template v-else>
                        <div class="row align-items-center justify-content-end how-it-works d-flex enteContent">
                          <div style="margin-top: 30px;margin-bottom: 30px;" class="col-6 text-right">
                            <strong>{{activity.label}}</strong>
                            <br> <span v-if="activity.timestamp"> completato il {{formatDate(activity.timestamp)}}</span>
                          </div>
                          <div class="col-2 text-center full d-inline-flex justify-content-center align-items-center">
                            <div style="background-color: #008758" class="circle font-weight-bold">{{activity.step}}</div>
                          </div>
                        </div>
                    </template>
                  </div>
                </div>
        </div>
        <div class="tab-pane p-4 fade" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
            <div class="container">
                <p v-if="instance.proceedings">
                    <strong>Denominazione procedimento</strong> <br>
                    <span>{{ instance.proceedings }}</span>
                </p>
                <p v-if="instance.accountableStaff">
                    <strong>Responsabile del Procedimento</strong> <br>
                    <span>{{ instance.accountableStaff }}</span>
                </p>
            </div>
        </div>
        <div class="tab-pane p-4 fade" id="tab3" role="tabpanel" aria-labelledby="tab3-tab">
            <div class="container">
                <div class="table-responsive">
                    <table class="table" v-if="instance.payments.length > 0">
                        <thead>
                            <tr>
                                <th scope="col">Causale</th>
                                <th scope="col">Totale</th>
                                <th scope="col">Data di processamento</th>
                                <th scope="col">Stato</th>
                                <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="payment in instance.payments" :key="payment.id">
                                <td>{{ payment.reason }}</td>
                                <td>{{ formatCurrency(payment.amount) }}</td>
                                <td>{{ formatDate(payment.processingTs) }}</td>
                                <td>{{ payment.status }}</td>
                                <td>
                                    <button @click="downloadReceipt(instance.id, payment.id)" type="button" class="btn-alpha blu-accent"><i class="fa fa-download "></i></button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div v-else class="alert alert-info" role="alert">
                        Non sono presenti pagamenti.
                    </div>
                </div>
            </div>
        </div>
        <div class="tab-pane p-4 fade" id="tab4" role="tabpanel" aria-labelledby="tab4-tab">
            <div class="container step-container">
                <div v-if="instance.documents.length > 0">
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th scope="col">Nome</th>
                                    <th style="text-align:center;" scope="col">Direzione</th>
                                    <th style="text-align:center;" scope="col">Tipo documento</th>
                                    <th style="text-align:center;" scope="col">Data creazione</th>
                                    <th style="text-align:center;" scope="col">Data Protocollo</th>
                                    <th style="text-align:center;" scope="col">N. Protocollo</th>
                                    <th style="text-align:center;" scope="col"></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="document in instance.documents" :key="document.id">
                                    <td>{{ document.name }}</td>
                                    <td style="text-align:center;" v-if="document.direction == 'SENT'">
                                        Inviato
                                    </td>
                                    <td style="text-align:center;" v-else>
                                        Ricevuto
                                    </td>
                                    <td style="text-align:center;" v-if="document.type">
                                        {{ document.type }}
                                    </td>
                                    <td style="text-align:center;" v-else>
                                        -
                                    </td>    
                                    <td style="text-align:center;" v-if="document.createTs">
                                        {{ formatDate(document.createTs) }}
                                    </td>
                                    <td style="text-align:center;" v-else>
                                        -
                                    </td>
                                    <td style="text-align:center;" v-if="document.protocolTs">
                                        {{ formatDate(document.protocolTs) }}
                                    </td>
                                    <td style="text-align:center;" v-else>
                                        -
                                    </td>
                                    <td style="text-align:center;" v-if="document.protocolNumber">
                                        {{ document.protocolNumber }}
                                    </td>
                                    <td style="text-align:center;" v-else>
                                        -
                                    </td>
                                    <td style="text-align:center;">
                                        <button @click="downloadDocument(instance.id, document.id)" type="button" class="btn-alpha blu-accent" ><i class="fa fa-download "></i></button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div v-else class="alert alert-info" role="alert">
                    Non sono presenti documenti.
                </div>
            </div>
        </div>
    </div>
</div>
`,
    data() {
        return {
            instance: null,
            user: null
        }
    },
    created() {
        this.user = username;
        this.instance = data("data-legacy-instance");
    },
    methods: {
        previous: function (activity) {
            if (activity.step > 1) {
                return this.instance.activities[activity.step - 2]
            } else {
                return activity
            }
        },        
        downloadReceipt: function(id, paymentId) {
            window.location.href = "/portale/v1/legacy-instances/"+id+"/payments/"+paymentId+"/receipt";
        },
        downloadDocument: function(id, documentId) {
            window.location.href = "/portale/v1/legacy-instances/"+id+"/documents/"+documentId;
        },
        formatCurrency: function (value){
            return new Intl
                .NumberFormat('it-IT', { style: 'currency', currency: 'EUR' })
                .format(value);
        },
        formatDate: function(isoDate){
            return FormatDate(isoDate);
        }       
    }
});
new Vue({ el: "#app" });
