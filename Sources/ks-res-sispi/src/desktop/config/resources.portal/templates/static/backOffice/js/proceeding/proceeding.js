import { DeleteProceeding } from '/static/portal/js/services/proceeding.service.js?no-cache';
import { FormatDateAndTime } from '/static/utilities/date.utilities.js?no-cache';
import { FindAllProceedingsPaged } from '/static/portal/js/services/proceeding.service.js?no-cache';

var App = Vue.component('App', { template: `
<div class="my-body-settings">
    <h2>
        <span class="title">Procedimenti</span>
    </h2>
    <div class="alert alert-success alert-dismissible fade show" role="alert" v-if="alertSuccess">
        {{successMessage}}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <div class="alert alert-danger alert-dismissible fade show" role="alert" v-if="alertError">
        {{errorMessage}}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <div v-if="!serverError">
        <div v-if="page.count > 0">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Titolo</th>
                            <th style="text-align:center" scope="col">Servizio</th>
                            <th style="text-align:center" scope="col">Inizio</th>
                            <th style="text-align:center" scope="col">Fine</th>
                            <th style="text-align:center" scope="col">Custom Template</th>
                            <th style="text-align:center" scope="col">Mostra Custom Template</th>
                            <th style="text-align:center" scope="col">Attivo</th>
                            <th style="text-align:center" scope="col">Online</th>
                            <th style="text-align:center" scope="col">Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="proceeding in proceedings">
                            <th scope="row">{{proceeding.id}}</th>
                            <td>{{proceeding.title}}</td>
                            <td style="text-align:center">{{proceeding.service.name}}</td>
                            <td style="text-align:center">{{FormatDateAndTime(proceeding.startDate)}}</td>
                            <td style="text-align:center">{{FormatDateAndTime(proceeding.endDate)}}</td>
                            <td style="text-align:center">{{proceeding.customTemplate}}</td>
                            <td style="text-align:center">
                                <span class="glyphicon glyphicon-ok-sign" v-bind:class="{ 'text-success': proceeding.showCustomTemplate, 'text-secondary': !proceeding.showCustomTemplate }"></span>
                            </td>
                            <td style="text-align:center">
                                <span class="glyphicon glyphicon-ok-sign" v-bind:class="{ 'text-success': proceeding.isActive, 'text-secondary': !proceeding.isActive }"></span>
                            </td>
                            <td style="text-align:center">
                                <span class="glyphicon glyphicon-ok-sign" v-bind:class="{ 'text-success': proceeding.isOnline, 'text-secondary': !proceeding.isOnline }"></span>
                            </td>
                            <td style="text-align:center">
                                <a style="min-width:90px;" v-bind:href="'/backOffice/proceeding/proceeding-update?id=' + proceeding.id" class="btn btn-secondary btn-sm">
                                    <span class="glyphicon glyphicon-pencil"></span>
                                    Modifica
                                </a>
                                <button style="min-width:90px;" type="button" class="btn btn-danger btn-sm" @click="deleteProceeding(proceeding.id, proceeding.title)">
                                    <span class="glyphicon glyphicon-trash"></span>
                                    Elimina
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="d-flex justify-content-center">
                <nav class="pagination-wrapper" aria-label="Navigazione centrata">
                    <ul class="pagination">
                        <li class="page-item" v-bind:class="{ disabled: currentPage === 1 }">
                            <a class="page-link" @click="previusPage" style="cursor: pointer;">
                                <i class="fa fa-chevron-left" aria-hidden="true"></i>
                                <span class="sr-only">Pagina precedente</span>
                            </a>
                        </li>

                        <li class="page-item" v-if="currentPage === 1">
                            <a class="page-link" aria-current="page">1</a>
                        </li>
                        <li class="page-item" v-if="currentPage === 1 && pages > 1">
                            <a class="page-link">2</a>
                        </li>

                        <li class="page-item" v-if="currentPage > 1">
                            <a class="page-link">{{currentPage - 1}}</a>
                        </li>
                        <li class="page-item" v-if="currentPage > 1">
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
            alertSuccess: false,
            successMessage: '',
            alertError: false,
            errorMessage: '',
            page: null,
            proceedings: null,
            serverError: false,
            currentPage: 1,
            pages: null
        }
    },
    methods: {
        FormatDateAndTime: function(isoDate){
            return FormatDateAndTime(isoDate);
        },
        deleteProceeding: function(id, name){
            confirm ("Sei sicuro di voler eliminare il procedimento '" + name + "'?", function() {
                const response = DeleteProceeding(id);
                if(response.status === 'success'){
                    location.reload();
                } else {
                    alert('Non è possibile eliminare questo Procedimento');
                }
            });
        },
        nextPage: function(){
            this.currentPage = this.currentPage + 1;
            const response = FindAllProceedingsPaged(this.currentPage, 10);
            const pagedList = response.data;
            this.proceedings = pagedList.data;
        },
        previusPage: function(){
            this.currentPage = this.currentPage - 1;
            const response = FindAllProceedingsPaged(this.currentPage, 10);
            const pagedList = response.data;
            this.proceedings = pagedList.data;
        }
    },
    created(){
        const response = FindAllProceedingsPaged(1, 10);
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        if(urlParams.has('success')){
            const success = urlParams.get('success');
            this.successMessage = success;
            this.alertSuccess = true;
        }
        if (urlParams.has('error')){
            const error = urlParams.get('error');
            this.errorMessage = error;
            this.alertError = true;
        }
        if(response.status === 'success'){
            const pagedList = response.data;
            this.page = pagedList;
            this.proceedings = pagedList.data;
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
