import { FormatDateAndTime } from "/static/utilities/date.utilities.js?no-cache";
import { DeletePortalInstance } from "/static/portal/js/services/instance.service.js?no-cache";
import { FindAllInstancesNotSended } from "/static/portal/js/services/myPractice.service.js?no-cache";
import { AssociateServiceAndProceeding } from "/static/utilities/proceeding.utilities.js?no-cache";
import { checkProceedingDates } from "/static/utilities/proceeding.utilities.js?no-cache";
export function InstancesNotSended() {
    var instancesNotSended = {
        template: `
        <div class="container step-container">
            <div v-if="!serverError">
                <div class="alert alert-info" role="alert" v-if="page.count === 0">
                    Per questa sezione non sono presenti istanze.
                </div>
                <div v-else>
                    <p>Di seguito trovi tutte le istanze create ma non ancora inviate</p>

                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th scope="col">Servizio</th>
                                    <th scope="col">Pratica</th>
                                    <th scope="col">Data di creazione</th>
                                    <th scope="col">Stato</th>
                                    <th scope="col" style="width: 220px;"></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="instance in page.data">
                                    <td>{{ instance.proceeding.service.name }}</td>
                                    <td>{{ instance.proceeding.title }}</td>
                                    <td>{{ formatDate(instance.creationDate) }}</td>
                                    <td>Salvato</td>
                                    <td style="text-align: right;">
                                        <a  v-if="canSendProceeding(instance)"
                                            v-bind:href="'/portal/features/update-instance-start?upid=' + instance.id" 
                                            class="btn btn-primary btn-xs"> <span class="glyphicon glyphicon-pencil"></span> Completa </a>
                                        <span v-else style="font-size: 0.9em; font-weight: 600; padding: 8px; margin-right: 4px;"> Scaduto </span>
                                        <button type="button" class="btn btn-danger btn-xs" @click="deleteInstance(instance.id, instance.proceeding.title)"><i class="fa fa-times" aria-hidden="true"></i> Elimina</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="mt-3"></div>
                    <nav class="pagination-wrapper justify-content-center" aria-label="Navigazione centrata" v-if="page.count > page.pageSize">
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
            <div v-else class="alert alert-danger" role="alert">
                Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
            </div>
        </div>
        `,
        data() {
            return {
                page: null,
                currentPage: 1,
                pages: null,
                serverError: false,
            };
        },
        methods: {
            formatDate: function (date) {
                return FormatDateAndTime(date);
            },
            deleteInstance: function (id, name) {
                confirm("Sei sicuro di voler eliminare l'istanza '" + name + "'?", function () {
                    const response = DeletePortalInstance(id);
                    location.reload();
                });
            },
            nextPage: function () {
                this.currentPage = this.currentPage + 1;
                const res = FindAllInstancesNotSended(this.currentPage, 10);
                this.page.data = AssociateServiceAndProceeding(res.data.data, this.proceedings);
            },
            previusPage: function () {
                this.currentPage = this.currentPage - 1;
                const res = FindAllInstancesNotSended(this.currentPage, 10);
                this.page.data = AssociateServiceAndProceeding(res.data.data, this.proceedings);
            },
            specifyPage: function (page) {
                this.currentPage = page;
                const res = FindAllInstancesNotSended(this.currentPage, 10);
                this.page.data = AssociateServiceAndProceeding(res.data.data, this.proceedings);
            },
            canSendProceeding: function(instance) {
                return checkProceedingDates(instance.proceeding) || instance.proceeding.sendIfExpired;
            }
        },
        created() {
            const instanceNotSendedResponse = FindAllInstancesNotSended(1, 10);
            if (instanceNotSendedResponse.status === "success") {
                this.page = instanceNotSendedResponse.data;
                this.page.data = AssociateServiceAndProceeding(this.page.data, this.proceedings);
                const count = this.page.count;
                const quotient = Math.floor(count / 10);
                const remainder = count % 10;
                var pageNumber = quotient;
                if (remainder > 0) {
                    pageNumber++;
                }
                this.pages = pageNumber;
            } else {
                this.serverError = true;
            }
        },
    };
    return instancesNotSended;
}
