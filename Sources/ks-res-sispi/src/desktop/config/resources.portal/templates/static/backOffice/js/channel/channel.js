import { FindAllChannelPaged } from '/static/portal/js/services/channel.service.js?no-cache';
import { DeleteChannel } from '/static/portal/js/services/channel.service.js?no-cache';
var App = Vue.component('App', { template: `
<div class="my-body-settings">
    <h2>
        <span class="title">Canali di Pagamento</span>
        <a href="/backOffice/channel/channel-create" class="btn btn-primary btn-sm float-right"> <span class="glyphicon glyphicon-plus"></span> Crea un nuovo Canale </a>
    </h2>
    <div class="alert alert-success alert-dismissible fade show" role="alert" v-if="alertSuccess">
        {{successMessage}}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <div class="alert alert-danger alert-dismissible fade show" role="alert" v-if="alertError">
        {{errorMessage}}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>

    <div v-if="!serverError">
        <div v-if="channels.length > 0">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                            <th scope="col">#</th>
                            <th style="text-align:center" scope="col">Nome</th>
                            <th style="text-align:center" scope="col">Provider</th>
                            <th style="text-align:center" scope="col">Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="channel in channels">
                            <th scope="row">{{channel.id}}</th>
                            <td style="text-align:center">{{channel.name}}</td>
                            <td style="text-align:center">{{channel.provider.name}}</td>
                            <td style="text-align:center">
                                <a style="min-width:90px;" v-bind:href="'/backOffice/channel/channel-create?id=' + channel.id" class="btn btn-primary btn-sm">
                                    <span class="glyphicon glyphicon-pencil"></span>
                                    Modifica
                                </a>
                                <a v-bind:href="'/backOffice/configuration/configuration?id=' + channel.id" class="btn btn-success btn-sm">
                                    <span class="glyphicon glyphicon-cog"></span>
                                    Configura
                                </a>
                                <button style="min-width:90px;" type="button" class="btn btn-danger btn-sm" @click="deleteChannel(channel.id, channel.name)">
                                    <span class="glyphicon glyphicon-trash"></span>
                                    Elimina
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div v-else class="alert alert-secondary" role="alert">
            Non è presente nessun Canale di Pagamento.
        </div>
    </div>
    <div v-else class="alert alert-danger" role="alert">
        Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
    </div>
</div>
`,
data(){
    return {
        alertSuccess: false,
        successMessage: '',
        alertError: false,
        errorMessage: '',
        channels: null,
        serverError: false
    }
},
methods: {
    deleteChannel: function(id, name){
        confirm ("Sei sicuro di voler eliminare il canale '" + name + "'?", function() {
            const response = DeleteChannel(id);
            if(response.status === 'success'){
                location.reload();
            } else {
                alert('Non è possibile eliminare questo Canale');
            }
        });
    }
},
created(){
    const response = FindAllChannelPaged(1, 10);
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    if(urlParams.has('success')){
        const success = urlParams.get('success');
        this.successMessage = success;
        this.alertSuccess = true;
    } 
    if (urlParams.has('error')){
        const error = urlParams.get('error');
        this.errorMessage = error;
        this.alertError = true;
    }
    if(response.status === 'success'){
        this.channels = response.data;
        this.serverError = false;
    } else {
        this.serverError = true;
    }
}
}); 
new Vue({ el: "#app" });
