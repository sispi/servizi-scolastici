import { ProceedingFooter } from '/static/portal/js/components/proceeding-footer.js?no-cache';
import { AttachmentsJson } from '/static/portal/json/attachments-json.js';
import { PaymentsModuleJson } from '/static/portal/json/payments-module-json.js';
import { PaymentsJson } from '/static/portal/json/payments-json.js';
import { UserJson } from '/static/portal/json/user-json.js';
import { MandatoryModules } from '/static/portal/json/mandatory-modules-json.js';

export function Result() {
    var result = {
        template: `<div class="container step-container">
    <h4>Riepilogo</h4>
    <h5>Riepilogo delle informazioni inserite per la pratica richiesta.</h5>
    <div class="action-container">
        <span class="lead">Allegati</span>
        <div class="card-wrapper card-space" v-for="attachment in attachments">
            <div class="card card-bg card-big border-bottom-card">
                <div class="flag-icon"></div>
                <div class="etichetta">
                    <button type="button" class="btn btn-primary btn-xs"><i class="fa fa-eye"></i> Visualizza</button>
                </div>
                <div class="card-body">
                    <h5>Documento: {{ attachment.documentFile }}</h5>
                    <small class="text-muted" v-if="attachment.uploadDate != null">Caricato il {{attachment.uploadDate}}.</small>
                    <small class="text-muted" v-if="attachment.acquisitionDate != null">Acquisito il {{attachment.acquisitionDate}}.</small>
                    <small class="text-muted" v-if="attachment.acquisitionDate == null">Non ancora acquisito dall'ente.</small>
                </div>
            </div>
        </div>
        <div v-if="attachments.length == 0">
            <small class="text-muted">Non sono presenti allegati</small>
        </div>
    </div>

    <div class="action-container">
        <span class="lead">Moduli di pagamento</span>
        <div class="card-wrapper card-space" v-for="paymentModule in paymentsModule" v-if="paymentModule.agencyModule.moduleTypeId == 3 && paymentModule.proceedingsInstance.status.code != 'INTEGRATION'">
            <div class="card card-bg card-big border-bottom-card">
                <div class="card-header">
                    <h5>{{ paymentModule.agencyModule.name }}</h5>
                    <small class="text-muted" v-if="paymentModule.uploadDate != null">File caricato: <i>{{paymentModule.documentFile}}</i></small>
                </div>
                <div class="card-body">
                    <small class="text-muted" v-if="paymentModule.uploadDate != null">Caricato il {{paymentModule.uploadDate}}.</small>
                    <small class="text-muted" v-if="paymentModule.uploadDate == null">Non ancora caricato.</small>
                    <small class="text-muted" v-if="paymentModule.acquisitionDate != null">Acquisito il {{paymentModule.acquisitionDate}}.</small>
                    <small class="text-muted" v-if="paymentModule.acquisitionDate == null">Non ancora acquisito dall'ente.</small>
                    <div class="row">
                        <div class="col-md-12" v-if="paymentModule.uploadDate != null">
                            <button type="button" class="btn btn-primary" v-if="paymentModule.documentBlob != null && paymentModule.documentBlob != 0 && paymentModule.documentSize > 0">Scarica <i class="fa fa-download"></i></button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="documentNumber() == 0">
            <small class="text-muted">Al momento non ci sono documenti in questa sezione.</small>
        </div>
    </div>

    <div class="action-container">
        <span class="lead">Pagamenti</span>
        <div class="card-wrapper card-space" v-for="payment in payments">
            <div class="card card-bg card-big border-bottom-card">
                <div class="etichetta" v-if="residenceData()">
                    <button type="button" class="btn btn-primary btn-xs" v-if="payment.ricevutaBlob == null || payment.ricevutaBlob == 0 || payment.esitoTransazione == null || payment.esitoTransazione != 'OK'">
                        <i class="fa fa-credit-card"></i> Paga OnLine
                    </button>
                    <button type="button" class="btn btn-secondary btn-xs" v-if="payment.ricevutaBlob != null && payment.ricevutaBlob != 0 && payment.esitoTransazione != null && payment.esitoTransazione == 'OK'">
                        <i class="fa fa-download"></i> Scarica la ricevuta
                    </button>
                </div>
                <div class="card-body">
                    <div class="categoryicon-top" v-if="payment.ricevutaBlob == null || payment.ricevutaBlob == 0 || payment.esitoTransazione == null || payment.esitoTransazione != 'OK'">
                        <span><i class="fa fa-credit-card" aria-hidden="true"></i> <span>€ {{payment.importoNetto}}</span></span>
                    </div>
                    <h5>{{ payment.causale }}</h5>
                    <small class="text-muted" v-if="payment.esitoTransazione == 'OK'">Pagato | <i>Data di pagamento: {{payment.dataElaborazione}}</i></small>
                    <small class="text-muted" v-if="payment.esitoTransazione != 'OK'">Non pagato</small>
                    <small class="text-muted" v-if="payment.ricevutaBlob == 0 && payment.esitoTransazione == 'OK'">Elaborazione della ricevuta in corso... Attendere qualche minuto</small>
                    <span class="lead text-danger" v-if="!residenceData()">
                        Non puoi ancora effettuare questo pagamento perchè i tuoi dati di residenza non sono aggiornati. <br />
                        Accedi alla pagina impostazioni per aggiornare i tuoi dati.
                    </span>
                </div>
            </div>
        </div>
        <div class="card-wrapper card-space" v-if="payments.length > 1">
            <div class="card card-bg card-big border-bottom-card">
                <div class="etichetta" v-if="residenceData()">
                    <button type="button" class="btn btn-primary btn-xs" v-if="residenceData()"><i class="fa fa-credit-card"></i> Paga Totale</button>
                </div>
                <div class="card-body">
                    <div class="categoryicon-top">
                        <span><i class="fa fa-credit-card" aria-hidden="true"></i> <span>€ {{totalPayment()}}</span></span>
                    </div>
                    <h5>E' possibile effettuare un unico pagamento</h5>
                    <span class="lead text-danger" v-if="!residenceData()">
                        Non puoi ancora effettuare questo pagamento perchè i tuoi dati di residenza non sono aggiornati. <br />
                        Accedi alla pagina impostazioni per aggiornare i tuoi dati.
                    </span>
                </div>
            </div>
        </div>
    </div>

    <div class="action-container">
        <span class="lead">Moduli obbligatori</span>
        <div class="card-wrapper card-space" v-for="module in modules">
            <div class="card card-bg card-big border-bottom-card">
                <div class="card-header">
                    <h5>{{ module.agencyModule.name }}</h5>
                    <small class="text-muted" v-if="module.uploadDate != null">File caricato: <i>{{ module.documentFile }}</i></small>
                </div>
                <div class="card-body">
                    <small class="text-muted" v-if="module.uploadDate != null">Caricato il {{module.uploadDate}}.</small>
                    <small class="text-muted" v-if="module.uploadDate == null">Non ancora caricato.</small>
                    <small class="text-muted" v-if="module.acquisitionDate != null">Acquisito il {{module.acquisitionDate}}.</small>
                    <small class="text-muted" v-if="module.acquisitionDate == null">Non ancora acquisito dall'ente.</small>
                    <div class="row">
                        <div class="col-md-12" v-if="module.uploadDate != null">
                            <button type="button" class="btn btn-primary btn-sm"><i class="fa fa-eye" aria-hidden="true"></i> Visualizza</button>
                            <button type="button" class="btn btn-danger btn-sm"><i class="fa fa-trash" aria-hidden="true"></i> Elimina</button>
                        </div>
                        <div class="col-md-12" v-if="module.uploadDate == null">
                            <button type="button" class="btn btn-primary btn-sm"><i class="fa fa-keyboard" aria-hidden="true"></i> Compila Online</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <my-proceeding-footer></my-proceeding-footer>
</div>
`,
        props: ['stepId', 'description', 'proceedingsInstanceId'],
        data() {
            return {
                number: +this.stepId + 3,
                attachments: AttachmentsJson(),
                paymentsModule: PaymentsModuleJson(),
                documentCount: 0,
                payments: PaymentsJson(),
                user: UserJson(),
                modules: MandatoryModules(),
            }
        },
        components: {
            'my-proceeding-footer': ProceedingFooter(),
        },
        methods: {
            documentNumber: function(){
                this.paymentsModule.forEach(element => {
                    if (element.agencyModule.moduleTypeId == 3 && element.proceedingsInstance.status.code != 'INTEGRATION') {
                        this.documentCount++;
                    }
                });
                return this.documentCount;
            },
            residenceData: function(){
                if(this.user.residenceCountry != null && this.user.residenceMunicipality != null && this.user.residenceProvince != null && this.user.address != null){
                    return true;
                }
                return false;
            },
            totalPayment: function(){
                var total = 0;
                this.payments.forEach(payment => {
                    if(payment.esitoTransazione != null || payment.esitoTransazione != 'OK'){
                        total = total + payment.importoNetto;
                    }
                });
                return total.toFixed(2);
            }
        }
    }
    return result;
}
