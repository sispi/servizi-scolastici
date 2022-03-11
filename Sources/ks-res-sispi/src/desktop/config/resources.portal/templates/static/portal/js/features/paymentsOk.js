import { getPaymentByUuId } from "/static/portal/js/services/payment.service.js?no-cache";
import { EuroFormatter } from '/static/utilities/currency.utilities.js?no-cache';
var App = Vue.component("App", {
    template: `
<div class="container">
  <div style="text-align: center; background: #f9f9f9; padding: 35px; border-radius: 5px; border: 1px solid #efefef;">
    <i style="color: #5ac64e; font-size: 70px; margin-bottom: 20px;" class="far fa-check-circle"></i>
    <h2>Pagamento Confermato</h2>
    <div>
      Le confermiamo che il pagamento è andato a buon fine
      <div v-if="!serverError" class="mt-2">
        <span>Codice richiesta:</span> <span style="font-weight:bold;font-size: 23px">{{ payment.requestCode }}</span> <br />
        <span>Importo pagato:</span> <span style="font-weight:bold;font-size: 23px">{{ euroFormatter(payment.totalAmount) }}</span>
        <div class="mt-2"></div>
        <a v-bind:href="'/portal/features/proceedingInstanceManage?id=' + payment.referenceInstanceId" class="btn btn-success">Torna alla pratica</a>
      </div>
    </div>
  </div>
</div>
`,
  data() {
    return {
      payment: null,
      serverError: false,
    };
  },
  created() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    var uuid = null;
    if (urlParams.has("paymentId")) {
      uuid = urlParams.get("paymentId");
      const response = getPaymentByUuId(uuid);
      if (response.status === "success") {
        this.payment = response.data;
      } else {
        this.serverError = true;
      }
    }
  },
  methods: {
    euroFormatter: function (euro){
      return EuroFormatter(euro);
    }
  }
});
new Vue({ el: "#app" });
