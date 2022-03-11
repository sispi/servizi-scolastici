var App = Vue.component('App', { template: `<div class="container">
  <div style="text-align:center; background: #fff; padding: 35px; border-radius: 5px; border: 1px solid #efefef;">
    <i style="color:#5AC64E; font-size:70px; margin-bottom:20px;" class="far fa-check-circle"></i>
    <h2>Pagamento Confermato</h2>
    <div>Le confermiamo che il pagamento e' andato a buon fine</div>
    <br>
    <button @click="goToPayments()" class="btn btn-success">Torna ai tuoi pagamenti</button>
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
