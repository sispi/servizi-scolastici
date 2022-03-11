var App = Vue.component('App', { template: `<div class="container">
  <div style="text-align:center; background: #fff; padding: 35px; border-radius: 5px; border: 1px solid #efefef;">
    <i style="color:#e61818; font-size:70px; margin-bottom:20px;" class="far fa-times-circle"></i>
    <h2>Pagamento non andato a buon fine</h2>
    <div>Siamo spiacenti il pagamento non e' andato a buon fine, si prega di riprovare piu' tardi</div>
    <br>
    <button @click="goToPayments()" class="btn btn-danger">Torna ai tuoi pagamenti</button>
  </div>
</div>
`,
    methods: {
        goToPayments: function () {
            window.location.href = "/portal/features/myPayments";
        }
    }
});
new Vue({ el: "#app" });
