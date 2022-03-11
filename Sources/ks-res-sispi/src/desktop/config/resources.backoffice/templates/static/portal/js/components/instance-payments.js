import { FindAllPaymentByUserAndReference } from '/static/portal/js/services/payment.service.js?no-cache';
import { FormatDate } from '/static/utilities/date.utilities.js?no-cache';
import { DownloadReceipt } from '/static/portal/js/services/payment.service.js?no-cache';

export function InstancePayments() {
    var instancePayments = {
        template: `
<div class="container">
    <div class="table-responsive" v-if="!serverError">
        <table class="table" v-if="pagedPayments.count > 0">
            <thead>
                <tr>
                    <th scope="col">Causale</th>
                    <th scope="col">Totale</th>
                    <th scope="col">Data di processamento</th>
                    <th scope="col">Stato</th>
                    <th scope="col"></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="payment in payments">
                    <td>{{ payment.causal }}</td>
                    <td>{{ payment.totalAmount }}</td>
                    <td>{{ formatDate(payment.processingDate) }}</td>
                    <td>{{ payment.transactionResult }}</td>
                    <td>
                        <button @click="download(payment.uuid)" type="button" class="btn-alpha blu-accent" ><i class="fa fa-download "></i></button>
                    </td>
                </tr>
            </tbody>
        </table>
        <div v-else class="alert alert-info" role="alert">
            Non è presente nessun Pagamento.
        </div>
    </div>
    <div v-else class="alert alert-danger" role="alert">
        Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
    </div>
</div>
    `,
    props: {
        instance: Object
    },
    data() {
      return {
          payments: [],
          serverError: false,
          pagedPayments: null
      }
    },
    methods: {
        formatDate: function(date){
            return FormatDate(date);
        },
        download: function(uuid) {
            const response = DownloadReceipt("pdf", uuid);
            console.log(response);
            if(response.status === 'success'){
                window.open(response.data.url, '_blank');
            } else {
                alert("Errore durante il caricamento");
            }
        }
    },
    created(){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        var id = null;
        if(urlParams.has('id')){
            id = urlParams.get('id');
        }
        console.log(this.instance);
        const response = FindAllPaymentByUserAndReference(id, this.instance.processId);
        if(response.status === 'success'){
            this.pagedPayments = response.data;
            this.payments = this.pagedPayments.data;
        } else {
            this.serverError = true;
        }
    }
  }
  return instancePayments;
}
