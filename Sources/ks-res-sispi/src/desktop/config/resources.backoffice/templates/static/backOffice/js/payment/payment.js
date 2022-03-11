import { FindAllPaymentsPaged } from '/static/portal/js/services/payment.service.js?no-cache';
import { EuroFormatter } from '/static/utilities/currency.utilities.js?no-cache';

var App = Vue.component('App', { template: `
<div class="my-body-settings">
    <h2>
        <span class="title">Pagamenti</span>
    </h2>
    <div v-if="!serverError">
        <div v-if="page.count > 0">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th scope="col">Casuale</th>
                            <th scope="col">Importo</th>
                            <th scope="col">Eseguito da</th>
                            <th scope="col">Status</th>
                            <!--<th scope="col">Azioni</th>-->
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="payment in payments">
                            <td><strong>{{ payment.causal }}</strong></td>
                            <td>{{ euroFormatter(payment.totalAmount) }}</td>
                            <td>{{ payment.customer.firstName }} {{ payment.customer.lastName }}</td>
                            <td>{{ payment.transactionResult }}</td>
                            <!--<td>
                                <a href="#" class="btn btn-primary btn-sm"><i class="fa fa-eye" aria-hidden="true"></i> Vedi</a>
                            </td>-->
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="mt-3"></div>
            <div class="d-flex justify-content-center">
                <nav class="pagination-wrapper" aria-label="Navigazione centrata">
                    <ul class="pagination">
                        <li class="page-item" v-bind:class="{ disabled: currentPage === 1 }">
                            <a class="page-link" @click="previusPage" style="cursor: pointer;">
                                <i class="fa fa-chevron-left" aria-hidden="true"></i>
                                <span class="sr-only">Pagina precedente</span>
                            </a>
                        </li>

                        <li class="page-item active" v-if="currentPage === 1">
                            <a class="page-link" aria-current="page">1</a>
                        </li>
                        <li class="page-item" v-if="currentPage === 1 && pages > 1">
                            <a class="page-link">2</a>
                        </li>

                        <li class="page-item" v-if="currentPage > 1">
                            <a class="page-link">{{currentPage - 1}}</a>
                        </li>
                        <li class="page-item active" v-if="currentPage > 1">
                            <a class="page-link" aria-current="page">{{currentPage}}</a>
                        </li>
                        <li class="page-item" v-if="currentPage > 1 && pages > currentPage">
                            <a class="page-link">{{currentPage + 1}}</a>
                        </li>

                        <li class="page-item" v-bind:class="{ disabled: currentPage === pages }">
                            <a class="page-link" @click="nextPage" style="cursor: pointer;">
                                <span class="sr-only">Pagina successiva</span>
                                <i class="fa fa-chevron-right" aria-hidden="true"></i>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
        <div v-else class="alert alert-secondary" role="alert">
            Non è presente nessun Procedimento.
        </div>
    </div>
    <div v-else class="alert alert-danger" role="alert">
        Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
    </div>
</div>
`,
    data(){
        return {
            page: null,
            payments: null,
            serverError: false,
            currentPage: 1,
            pages: null
        }
    },
    methods: {
        euroFormatter: function (euro){
            return EuroFormatter(euro);
        },
        nextPage: function(){
            this.currentPage = this.currentPage + 1;
            const response = FindAllPaymentsPaged(this.currentPage, 10, 'id:DESC');
            const pagedList = response.data;
            this.payments = pagedList.data;
        },
        previusPage: function(){
            this.currentPage = this.currentPage - 1;
            const response = FindAllPaymentsPaged(this.currentPage, 10, 'id:DESC');
            const pagedList = response.data;
            this.payments = pagedList.data;
        }
    },
    created(){
        const response = FindAllPaymentsPaged(1, 10, 'id:DESC');
        if(response.status === 'success'){
            const pagedList = response.data;
            this.page = pagedList;
            this.payments = pagedList.data;
            this.serverError = false;

            const count = pagedList.count;
            const quotient = Math.floor(count / 10);
            const remainder = count % 10;
            var pageNumber = quotient;
            if(remainder > 0){
                pageNumber++;
            }
            this.pages = pageNumber;
        } else {
            this.serverError = true;
        }
    }
});
new Vue({ el: "#app" });

