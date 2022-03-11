import { ProceedingsInstanceJson } from '/static/portal/json/proceedings-instance-json.js';
import { FindUoByGroupId } from '/static/portal/js/services/docer.service.js?no-cache';

export function ProceedingDetail() {
    var proceedingDetail = {
        template: `
    <div class="container">
        <p v-if="proceeding.title">
            <strong>Denominazione procedimento</strong> <br>
            <span>{{ proceeding.title }}</span>
        </p>
        <p v-if="proceeding.body">
            <strong>Descrizione procedimento</strong> <br>
            <div id="editor">
        
            </div>
        </p>
        <p v-if="proceeding.accountableOffice">
            <strong>Ufficio responsabile di istruttoria</strong> <br>
            <span>{{ proceeding.accountableOffice }}</span>
        </p>
        <p v-if="proceeding.accountableStaff">
            <strong>Responsabile del Procedimento</strong> <br>
            <span>{{ proceeding.accountableStaff }}</span>
        </p>
        <p v-if="proceeding.operatorStaff">
            <strong>Operatore</strong> <br>
            <span>{{ proceeding.operatorStaff }}</span>
        </p>
        <p v-if="proceeding.applicantRequirement">
            <strong>Requisiti del soggetto che presenta domanda di avvio del procedimento</strong> <br>
            <span>{{ proceeding.applicantRequirement }}</span>
        </p>
        <p v-if="proceeding.costs">
            <strong>Costi</strong> <br>
            <span>{{ proceeding.costs }}</span>
        </p>
        <p v-if="proceeding.attachments">
            <strong>Documentazione da allegare alla domanda</strong> <br>
            <span>{{ proceeding.attachments }}</span>
        </p>
        <p v-if="proceeding.howToSubmit">
            <strong>Modalità di presentazione della domanda</strong> <br>
            <span>{{ proceeding.howToSubmit }}</span>
        </p>
        <p v-if="proceeding.timeNeeded">
            <strong>Tempi di conclusione del procedimento</strong> <br>
            <span>{{ proceeding.timeNeeded }}</span>
        </p>
        <p v-if="proceeding.uo">
            <strong>Informazioni di contatto</strong> <br>
            <span>{{ uo.GROUP_NAME }}: {{ uo.EMAIL_ADDRESS }}</span>
        </p>
    </div>
    `,
        props: {
            proceeding: Object,
        },
        data() {
            return {
                proceedingInstance: ProceedingsInstanceJson(),
                uo: null
            }
        },
        created(){
            if(this.proceeding.uo != null){
                const response = FindUoByGroupId(this.proceeding.uo);
                if(response.status === 'success'){
                    this.uo = response.data;
                } else {
                    this.serverError = true;
                }
            }
        }
    }
    return proceedingDetail;
}
