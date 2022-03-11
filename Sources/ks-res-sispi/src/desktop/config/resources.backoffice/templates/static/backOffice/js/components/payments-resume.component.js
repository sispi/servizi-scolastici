import { FindAllPaymentsPaged } from '/static/portal/js/services/payment.service.js?no-cache';
import { EuroFormatter } from '/static/utilities/currency.utilities.js?no-cache';
export function PaymentsResume() {
    var paymentResume = {
        template: `
<div>
    <div class="mt-5"></div>
    <div class="row">
        <div class="col-md-12">
            <div class="row">
                <div class="col-md-6"><h4>Pagamenti</h4></div>
                <div class="col-md-6"><a href="/backOffice/payment/payment" class="float-right">Vedi tutti <i class="fa fa-angle-right" aria-hidden="true"></i></a></div>
            </div>
            <div class="spinner-border text-secondary" role="status" v-if="isLoading"></div>
            <div class="table-responsive" v-if="!serverError">
                <div v-if="payments.length > 0">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th scope="col">Causale</th>
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
                                <!--<td><button type="button" class="btn btn-primary">Gestisci</button></td>-->
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div v-else class="alert alert-secondary" role="alert">
                    Non è presente nessun Pagamento.
                </div>
            </div>
            <div v-else class="alert alert-danger" role="alert">
                Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
            </div>
        </div>
    </div>
</div>
        `,
        data() {
            return {
                payments: [],
                serverError: false,
                isLoading: true
            };
        },
        methods: {
            euroFormatter: function (euro){
                return EuroFormatter(euro);
            }
        },
        created() {
            const response = FindAllPaymentsPaged(1, 5, 'id:DESC');
            if(response.status === 'success'){
                this.payments = response.data.data;
                this.serverError = false;
                this.isLoading = false;
            } else {
                this.serverError = true;
                this.isLoading = false;
            }
        }
    };
    return paymentResume;
}
