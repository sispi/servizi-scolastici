import { FindAllPaymentByUserAndReference, DownloadReceipt } from '/static/portal/js/services/payment.service.js?no-cache';
import { FormatDate } from '/static/utilities/date.utilities.js?no-cache';
import { EuroFormatter } from '/static/utilities/currency.utilities.js?no-cache';

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
                    <td>{{ euroFormatter(payment.totalAmount) }}</td>
                    <td>{{ formatDate(payment.processingDate) }}</td>
                    <td>{{ payment.transactionResult }}</td>
                    <td>
                        <button @click="download(payment.uuid)" type="button" class="btn-alpha blu-accent" v-if="payment.receipt"><i class="fa fa-download "></i></button>
                    </td>
                </tr>
            </tbody>
        </table>
        <div v-else class="alert alert-info" role="alert">
            Non sono presenti pagamenti.
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
            if(response.status === 'success'){
                window.open(response.data.url, '_blank');
            } else {
                alert("Errore durante il caricamento");
            }
        },
        euroFormatter: function (euro){
            return EuroFormatter(euro);
        }
    },
    created(){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        var id = null;
        if(urlParams.has('id')){
            id = urlParams.get('id');
        }
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
