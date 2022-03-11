var App = Vue.component('App', { template: `
<div class="container">
    <div class="jumbotron">
    <h2 class="display-4">{{serviceName}}</h2>
    <p class="lead">Questo servizio non ha ne sottoservizi ne procedimenti associati.</p>
    <hr class="my-4">
    <p>Cosa vuoi creare sotto il servizio <strong>{{serviceName}}</strong>?</p>
    <p class="lead">
        <a class="btn btn-primary btn-lg" v-bind:href="'/backOffice/service/service-create?serviceId=' + serviceId + '&serviceName=' + serviceName" role="button">
            Un Sottoservizio
        </a>
        <a class="btn btn-primary btn-lg" v-bind:href="'/backOffice/proceeding/proceeding-create?serviceId=' + serviceId + '&serviceName=' + serviceName" role="button">
            Un Procedimento
        </a>
        <button type="button" class="btn btn-secondary btn-lg" v-on:click="goBack">Annulla</button>
    </p>
    </div>
</div>
`,
data(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    var serviceId = null;
    var serviceName = null;
    if(urlParams.has('serviceId')){
        serviceId = urlParams.get('serviceId');
        if(urlParams.has('serviceName')){
            serviceName = urlParams.get('serviceName');
        }
    }
    return {
        serviceName: serviceName,
        serviceId: serviceId,
    }
},
methods: {
    goBack: function(){
        window.history.back();
    }
}
}); 
new Vue({ el: "#app" });
