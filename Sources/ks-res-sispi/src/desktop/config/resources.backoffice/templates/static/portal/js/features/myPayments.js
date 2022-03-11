import { FindAllPaymentByUser } from '/static/portal/js/services/payment.service.js?no-cache';
import { DownloadReceipt } from '/static/portal/js/services/payment.service.js?no-cache';
var App = Vue.component('App', { template: `<div class="container">
  <h2>I tuoi pagamenti</h2>
  <div v-if="!serverError">
  <div v-if="page.count > 0">
      <table class="table">
          <thead>
              <tr>
                  <th scope="col">Causale</th>
                  <th scope="col">Tipo di pagamento</th>
                  <th scope="col">Totale</th>
                  <th scope="col">#</th>
              </tr>
          </thead>
          <tbody>
              <tr v-for="payment in payments">
                  <th>{{ payment.causal }}</th>
                  <td>{{ payment.paymentType }}</td>
                  <td>€ {{ payment.totalAmount }}</td>
                  <td>
                    <button @click="download(payment.uuid)" type="button" class="btn-alpha blu-accent" ><i class="fa fa-download "></i></button>
                  </td>
              </tr>
          </tbody>
      </table>
  </div>
  <div v-else class="alert alert-info" role="alert">
    Non è stato effettuato nessun pagamento.
    </div>
  </div>
  <div v-else class="alert alert-danger" role="alert">
    Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
    </div>
</div>
`,
data() {
    const response = FindAllPaymentByUser();
    if(response.status === 'success'){
        const pagedList = response.data;
        const count = pagedList.count;
        const quotient = Math.floor(count / 10);
        const remainder = count % 10;
        var pageNumber = quotient;
        if(remainder > 0){
            pageNumber++;
        }
        return {
            serverError: false,
            payments: pagedList.data,
            currentPage: 1,
            pages: pageNumber,
            page: pagedList
        }
    } else {
        return {
            serverError: true
        }
    }
    
},
methods: {
    download: function(uuid) {
        const response = DownloadReceipt("pdf", uuid);
        console.log(response);
        if(response.status === 'success'){
            window.open(response.data.url, '_blank');
        } else {
            alert("Errore durante il caricamento");
        }
    }
}
}); 
new Vue({ el: "#app" });
