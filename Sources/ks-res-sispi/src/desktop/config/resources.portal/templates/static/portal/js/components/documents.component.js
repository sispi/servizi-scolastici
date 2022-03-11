import { FindDocumentByYear } from "/static/portal/js/services/document.service.js?no-cache";
import { FormatDateAndTime } from '/static/utilities/date.utilities.js?no-cache';

export function Document(direction) {
    var documents = {
        template: `<div class="container step-container">
    <h4 v-if="currentYear!='Tutti'">
        {{ direction == 'SENT' ? 'Documenti Inviati' : 'Documenti Ricevuti' }} nel {{currentYear}}
    </h4>
    <h4 v-else>
        {{ direction == 'SENT' ? 'Documenti Inviati' : 'Documenti Ricevuti' }}
    </h4>
    <select @change="yearChange()" class="form-control" v-model="currentYear">
        <option v-for="(anno, key) in anni" :value="anno">{{anno}}</option>
    </select>

    <div v-if="documents.data.data.length > 0">
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th scope="col">Nome</th>
                        <th style="text-align:center;" scope="col">Procedimento</th>
                        <th style="text-align:center;" scope="col">Tipo documento</th>
                        <th style="text-align:center;" scope="col">Data creazione</th>
                        <th style="text-align:center;" scope="col">Data Protocollo</th>
                        <th style="text-align:center;" scope="col">N. Protocollo</th>
                        <th style="text-align:center;" scope="col"></th>
                    </tr>
                </thead>
                <tbody v-if="direction == 'SENT'">
                    <tr v-for="(document,i) in displayedDocs" v-if="document.PORTAL_DIRECTION=='SENT'" :key="i">
                        <td>{{ document.DOCNAME | limitText20 }} ({{ document.DOCNUM }})</td>
                        <td style="text-align:center;" v-if="document.PORTAL_ID">
                            {{ document.PORTAL_DESCRIPTION }} ({{ document.PORTAL_ID }})
                        </td>
                        <td style="text-align:center;" v-else>
                            -
                        </td>
                        <td style="text-align:center;" v-if="document.PORTAL_MODULETYPE">
                            {{ document.PORTAL_MODULETYPE }}
                        </td>
                        <td style="text-align:center;" v-else>
                            -
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
                <tbody v-else>
                    <tr v-for="(document,i) in displayedDocs" v-if="document.PORTAL_DIRECTION=='RECEIVED'" :key="i">
                        <td>{{ document.DOCNAME | limitText20 }} ({{ document.DOCNUM }})</td>
                        <td style="text-align:center;" v-if="document.PORTAL_ID">
                            {{ document.PORTAL_DESCRIPTION }} ({{ document.PORTAL_ID }})
                        </td>
                        <td style="text-align:center;" v-else>
                            -
                        </td>
                        <td style="text-align:center;" v-if="document.PORTAL_MODULETYPE">
                            {{ document.PORTAL_MODULETYPE }}
                        </td>
                        <td style="text-align:center;" v-else>
                            -
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
    <div v-if="documents.data.data.length == 0">
        <p class="lead">
            <br>
            Non sono presenti documenti.
        </p>
    </div>
</div>
`,
        data() {
            return {
                direction: direction,
                documents: direction == 'SENT' ? FindDocumentByYear(new Date().getFullYear(),'SENT') : FindDocumentByYear(new Date().getFullYear(),'RECEIVED'),
                displayedDocs: [],
                currentYear: new Date().getFullYear(),
                anni:[],
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
            yearChange: function() {
                this.documents =  FindDocumentByYear(this.currentYear, direction);
                this.displayedDocs = [];
                this.documents.data.data.forEach((document) => {
                    this.displayedDocs.push(document);
                });
            },
            FormatDateAndTime: function(isoDate){
                return FormatDateAndTime(isoDate);
            },
        },
        created() {
            this.getDocs();

            var currentYear = new Date().getFullYear();
            this.anni.push(currentYear, currentYear-1, currentYear-2, "Tutti");
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
    return documents;
}
