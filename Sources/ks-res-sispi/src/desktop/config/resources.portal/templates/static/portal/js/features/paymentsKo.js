import { getPaymentByUuId } from "/static/portal/js/services/payment.service.js?no-cache";
var App = Vue.component('App', { template: `
<div class="container">
  <div style="text-align:center; background: #fff; padding: 35px; border-radius: 5px; border: 1px solid #efefef;">
    <i style="color:#e61818; font-size:70px; margin-bottom:20px;" class="far fa-times-circle"></i>
    <h2>Pagamento non andato a buon fine</h2>
    <div>
      Siamo spiacenti il pagamento non e' andato a buon fine, si prega di riprovare piu' tardi
      <div class="mt-2"></div>
      <a v-bind:href="'/portal/features/proceedingInstanceManage?id=' + payment.referenceInstanceId" class="btn btn-danger" v-if="!serverError">
        Torna alla pratica
      </a>
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
  }
});
new Vue({ el: "#app" });
