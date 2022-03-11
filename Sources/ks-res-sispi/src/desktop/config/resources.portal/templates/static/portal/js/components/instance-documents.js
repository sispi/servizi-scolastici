import { FindDocumentByProceed } from "/static/portal/js/services/document.service.js?no-cache";
import { FormatDateAndTime } from '/static/utilities/date.utilities.js?no-cache';

export function InstanceDocuments() {
    var InstanceDocuments = {
        template: `<div class="container step-container">
    <div v-if="documents.data.data.length > 0">
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
                    <tr v-for="(document,i) in displayedDocs" :key="i">
                        <td>{{ document.DOCNAME | limitText20 }} ({{ document.DOCNUM }})</td>
                        <td style="text-align:center;" v-if="document.PORTAL_DIRECTION=='SENT'">
                            Inviato
                        </td>
                        <td style="text-align:center;" v-else>
                            Ricevuto
                        </td>
                        <td style="text-align:center;">
                            {{ document.PORTAL_MODULETYPE }}
                        </td>
                        <td style="text-align:center;" v-if="document.CREATED">
                            {{FormatDateAndTime(document.CREATED)}}
                        </td>
                        <td style="text-align:center;" v-else>
                            -
                        </td>
                        <td style="text-align:center;" v-if="document.DATA_PG">
                            {{FormatDateAndTime(document.DATA_PG)}}
                        </td>
                        <td style="text-align:center;" v-else>
                            -
                        </td>
                        <td style="text-align:center;" v-if="document.NUM_PG">
                            {{ document.NUM_PG }}
                        </td>
                        <td style="text-align:center;" v-else>
                            -
                        </td>
                        <td style="text-align:center;">
                            <button @click="downloadFileFromList(document.DOCNUM)" type="button" class="btn-alpha blu-accent" ><i class="fa fa-download "></i></button>
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
`,
        props: {
            proceeding: Object,
            bpmInstance: Object
        },
        data() {
            return {
                documents: FindDocumentByProceed(this.bpmInstance.id),
                displayedDocs: [],
            };
        },
        methods: {
            getDocs() {
                this.documents.data.data.forEach((document) => {
                    this.displayedDocs.push(document);
                });
            },
            downloadFileFromList: function(docnum) {
                window.location.href = "/docer/v1/documenti/"+docnum+"/file";
            },
            FormatDateAndTime: function(isoDate){
                return FormatDateAndTime(isoDate);
            },
        },
        created() {
            this.getDocs();
        },
        filters: {
            moment: function (date) {
                return moment(date).format('DD-MM-YYYY HH:mm');
            },
            limitText20: function(text) {
                if(text.length > 20)
                    text = text.substring(0,20) + '...';
                return text
            },
        }
    };
    return InstanceDocuments;
}
