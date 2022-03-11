import { FormatDateAndTime } from "/static/utilities/date.utilities.js?no-cache";
import { FindAllInstancesToCompileByInstitution } from "/static/portal/js/services/myPractice.service.js?no-cache";
import { AssociateServiceAndProceeding } from "/static/utilities/proceeding.utilities.js?no-cache";
export function InstancesInProgress() {
    var InstancesInProgress = {
        template: `
        <div class="container step-container">
            <div v-if="!serverError">
                <div class="alert alert-info" role="alert" v-if="page.count === 0">
                    Per questa sezione non sono presenti istanze.
                </div>
                <div v-else>
                    <p>Di seguito trovi le pratiche in lavorazione presso l'ente</p>

                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th scope="col">Servizio</th>
                                    <th scope="col">Pratica</th>
                                    <th scope="col">Data di creazione</th>
                                    <th scope="col">Stato</th>
                                    <th scope="col"></th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="instance in page.data">
                                    <td v-if="instance.proceeding != null">{{ instance.proceeding.service.name }}</td>
                                    <td v-else></td>
                                    <td v-if="instance.proceeding != null">{{ instance.proceeding.title }}</td>
                                    <td v-else>{{ instance.name }}</td>
                                    <td>{{ formatDate(instance.startTs) }}</td>
                                    <td>
                                        <span v-if="instance.status === 1">Attivo</span>
                                        <span v-else-if="instance.status === 2">Completato</span>
                                        <span v-else-if="instance.status === 3">Rigettata</span>
                                    </td>
                                    <td>
                                        <a v-bind:href="'/portal/features/proceedingInstanceManage?id=' + instance.id" class="btn btn-primary btn-xs"> <i class="fa fa-eye" aria-hidden="true"></i> Vedi </a>
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
        props: {
            proceedings: Object,
        },
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
            nextPage: function () {
                this.currentPage = this.currentPage + 1;
                const res = FindAllInstancesToCompileByInstitution(this.currentPage, 10);
                this.page.data = AssociateServiceAndProceeding(res.data.data, this.proceedings);
            },
            previusPage: function () {
                this.currentPage = this.currentPage - 1;
                const res = FindAllInstancesToCompileByInstitution(this.currentPage, 10);
                this.page.data = AssociateServiceAndProceeding(res.data.data, this.proceedings);
            },
            specifyPage: function (page) {
                this.currentPage = page;
                const res = FindAllInstancesToCompileByInstitution(this.currentPage, 10);
                this.page.data = AssociateServiceAndProceeding(res.data.data, this.proceedings);
            }
        },
        created() {
            const InstancesToCompileByInstitutionResponse = FindAllInstancesToCompileByInstitution(1, 10);
            if (InstancesToCompileByInstitutionResponse.status === "success") {
                this.page = InstancesToCompileByInstitutionResponse.data;
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
    return InstancesInProgress;
}
