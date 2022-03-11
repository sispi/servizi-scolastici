import { PaymentsJson } from '/static/portal/json/payments-json.js';
import { ProceedingFooter } from '/static/portal/js/components/proceeding-footer.js?no-cache';
import { UserJson } from '/static/portal/json/user-json.js';

export function Payments() {
    var payments = {
        template: `
    <div class="container step-container">
        <h4>Pagamenti richiesti</h4>
        <h5><strong>Pagamenti necessari</strong> per la pratica richiesta</h5>
        <div class="card-wrapper card-space" v-for="payment in payments">
            <div class="card card-bg card-big border-bottom-card">
                <div class="etichetta" v-if="residenceData()">
                    <button type="button" class="btn btn-primary btn-xs" 
                        v-if="payment.ricevutaBlob == null || payment.ricevutaBlob == 0 || payment.esitoTransazione == null || payment.esitoTransazione != 'OK'">
                        <i class="fa fa-credit-card"></i> Paga OnLine
                    </button>
                    <button type="button" class="btn btn-secondary btn-xs" 
                        v-if="payment.ricevutaBlob != null && payment.ricevutaBlob != 0 && payment.esitoTransazione != null && payment.esitoTransazione == 'OK'">
                        <i class="fa fa-download"></i> Scarica la ricevuta
                    </button>
                </div>
                <div class="card-body">
                    <div class="categoryicon-top" v-if="payment.ricevutaBlob == null || payment.ricevutaBlob == 0 || payment.esitoTransazione == null || payment.esitoTransazione != 'OK'">
                        <span><i class="fa fa-credit-card" aria-hidden="true"></i>
                        <span>€ {{payment.importoNetto}}</span></span>
                    </div>
                    <h5>{{ payment.causale }}</h5>
                    <small class="text-muted" v-if="payment.esitoTransazione == 'OK'">Pagato | <i>Data di pagamento: {{payment.dataElaborazione}}</i></small>
                    <small class="text-muted" v-if="payment.esitoTransazione != 'OK'">Non pagato</small>
                    <small class="text-muted" v-if="payment.ricevutaBlob == 0 && payment.esitoTransazione == 'OK'">Elaborazione della ricevuta in corso... Attendere qualche minuto</small>
                    <span class="lead text-danger" v-if="!residenceData()">
                        Non puoi ancora effettuare questo pagamento perchè i tuoi dati di residenza non sono aggiornati. <br>
                        Accedi alla pagina impostazioni per aggiornare i tuoi dati.
                    </span>
                </div>
            </div>
        </div>
        <div class="card-wrapper card-space" v-if="payments.length > 1">
            <div class="card card-bg card-big border-bottom-card">
                <div class="etichetta" v-if="residenceData()">
                    <button type="button" class="btn btn-primary btn-xs" 
                        v-if="residenceData()">
                        <i class="fa fa-credit-card"></i> Paga Totale
                    </button>
                </div>
                <div class="card-body">
                    <div class="categoryicon-top">
                        <span><i class="fa fa-credit-card" aria-hidden="true"></i>
                        <span>€ {{totalPayment()}}</span></span>
                    </div>
                    <h5>E' possibile effettuare un unico pagamento</h5>
                    <span class="lead text-danger" v-if="!residenceData()">
                        Non puoi ancora effettuare questo pagamento perchè i tuoi dati di residenza non sono aggiornati. <br>
                        Accedi alla pagina impostazioni per aggiornare i tuoi dati.
                    </span>
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
                payments: PaymentsJson(),
                user: UserJson()
            }
        },
        components: {
            'my-proceeding-footer': ProceedingFooter(),
        },
        methods: {
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
    return payments;
}
