import { CountInstances, CountInstanceDocuments } from '/static/portal/js/services/instance.service.js?no-cache';
import { CountPayments, PaymentsAmount } from '/static/portal/js/services/payment.service.js?no-cache';
import { EuroFormatter } from '/static/utilities/currency.utilities.js?no-cache';

export function HomeCards() {
    var homeCards = {
        template: `
<div>
    <div class="mt-5"></div>
    <div class="row">
        <div class="col-md-4 d-flex justify-content-start mb-3">
            <div class="card" style="width: 100%;">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-10">
                            <h5 class="card-title">TOTALE ISTANZE</h5>
                            <div class="spinner-border text-secondary" role="status" v-if="instances.isLoading"></div>
                            <h6 class="card-subtitle mb-2 text-muted" v-if="!instances.error">{{ instances.number }}</h6>
                            <h6 class="card-subtitle mb-2 text-muted" v-else>Errore durante il caricamento</h6>
                        </div>
                        <div class="col-md-2">
                            <span class="glyphicon glyphicon-tag align-middle icon-settings-custom"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4 d-flex justify-content-center mb-3">
            <div class="card" style="width: 100%;">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-10">
                            <h5 class="card-title">TOTALE DOCUMENTI</h5>
                            <div class="spinner-border text-secondary" role="status" v-if="documents.isLoading"></div>
                            <h6 class="card-subtitle mb-2 text-muted" v-if="!documents.error">{{ documents.number }}</h6>
                            <h6 class="card-subtitle mb-2 text-muted" v-else>Errore durante il caricamento</h6>
                        </div>
                        <div class="col-md-2">
                            <span class="glyphicon glyphicon-file align-middle icon-settings-custom"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4 d-flex justify-content-end mb-3">
            <div class="card" style="width: 100%;">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-10">
                            <h5 class="card-title">TOTALE PAGAMENTI</h5>
                            <div class="spinner-border text-secondary" role="status" v-if="payments.isLoading && paymentsAmount.isLoading"></div>
                            <h6 class="card-subtitle mb-2 text-muted" v-if="!payments.error && !paymentsAmount.error">{{ payments.number }} per {{ euroFormatter(paymentsAmount.amount) }}</h6>
                            <h6 class="card-subtitle mb-2 text-muted" v-else>Errore durante il caricamento</h6>
                        </div>
                        <div class="col-md-2">
                            <span class="glyphicon glyphicon-euro align-middle icon-settings-custom"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
        `,
        data() {
            return {
                instances: {
                    number: null,
                    error: false,
                    isLoading: true
                },
                documents: {
                    number: null,
                    error: false,
                    isLoading: true
                },
                payments: {
                    number: null,
                    error: false,
                    isLoading: true
                },
                paymentsAmount: {
                    amount: null,
                    error: false,
                    isLoading: true
                }
            };
        },
        methods: {
            euroFormatter: function (euro){
                return EuroFormatter(euro);
            }
        },
        created() {
            const countInstancesResponse = CountInstances();
            if(countInstancesResponse.status === 'success'){
                this.instances = {
                    number: countInstancesResponse.data,
                    error: false,
                    isLoading: false
                };
            } else {
                this.instances = {
                    number: null,
                    error: true,
                    isLoading: false
                };
            }
            const countInstanceDocumentResponse = CountInstanceDocuments();
            if(countInstanceDocumentResponse.status === 'success'){
                this.documents = {
                    number: countInstanceDocumentResponse.data,
                    error: false,
                    isLoading: false
                };
            } else {
                this.documents = {
                    number: null,
                    error: true,
                    isLoading: false
                };
            }
            const countPaymentsResponse = CountPayments();
            if(countPaymentsResponse.status === 'success'){
                this.payments = {
                    number: countPaymentsResponse.data,
                    error: false,
                    isLoading: false
                };
            } else {
                this.payments = {
                    number: null,
                    error: true,
                    isLoading: false
                };
            }
            const paymentsAmountResponse = PaymentsAmount();
            if(paymentsAmountResponse.status === 'success'){
                this.paymentsAmount = {
                    amount: paymentsAmountResponse.data,
                    error: false,
                    isLoading: false
                };
            } else {
                this.paymentsAmount = {
                    amount: null,
                    error: true,
                    isLoading: false
                };
            }
        }
    };
    return homeCards;
}
