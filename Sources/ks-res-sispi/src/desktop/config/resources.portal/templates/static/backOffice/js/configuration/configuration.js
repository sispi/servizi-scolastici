import { FindAllChannelConfiguration } from '/static/portal/js/services/configuration.service.js?no-cache';
import { FindOneChannel } from '/static/portal/js/services/channel.service.js?no-cache';
import { DeleteChannelConfiguration } from '/static/portal/js/services/configuration.service.js?no-cache';
import { UpdateDefaultChannelConfiguration } from '/static/portal/js/services/configuration.service.js?no-cache';
var App = Vue.component('App', { template: `
<div class="my-body-settings">
    <h1>
        <span>{{ channel.name }}</span>
        <a v-bind:href="'/backOffice/configuration/configuration-create?channelId=' + channel.id" class="btn btn-primary float-right">
          <span class="glyphicon glyphicon-plus"></span> Crea una nuova Configurazione
        </a>
    </h1>
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
        <div class="table-responsive" v-if="configurations.length > 0">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Codice Valuta</th>
                        <th scope="col">Pagamento Abilitato</th>
                        <th scope="col">Lingua</th>
                        <th scope="col">Default</th>
                        <th scope="col">Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="configuration in configurations">
                        <th scope="row">{{ configuration.id }}</th>
                        <td>{{ configuration.currencyCode }}</td>
                        <td>
                            <span class="glyphicon glyphicon-ok-sign" v-bind:class="{ 'text-success': configuration.paymentEnabled, 'text-secondary': !configuration.paymentEnabled }"></span>
                        </td>
                        <td>{{ configuration.langId }}</td>
                        <td>
                            <span 
                            class="glyphicon glyphicon-ok-sign" 
                            v-bind:class="{ 'text-success': configuration.defaultConf, 'text-secondary': !configuration.defaultConf }" 
                            style="cursor: pointer;"
                            @click="switchDefault(configuration.id)"
                            ></span>
                        </td>
                        <td>
                            <a v-bind:href="'/backOffice/configuration/configuration-create?channelId=' + channel.id + '&configurationId=' + configuration.id" class="btn btn-primary btn-sm">
                                <span class="glyphicon glyphicon-pencil"></span>
                                Modifica
                            </a>
                            <button type="button" class="btn btn-danger btn-sm" @click="deleteConfiguration(configuration.id)">
                                <span class="glyphicon glyphicon-trash"></span>
                                Elimina
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div v-else class="alert alert-secondary" role="alert">
            Non è presente nessuna Configurazione per questo canale.
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
        configurations: null,
        serverError: false,
        channel: {
            id: null,
            name: null,
            provider: null
        }
    }
},
methods: {
    deleteConfiguration: function(id){
        confirm ("Sei sicuro di voler eliminare la configurazione '" + id + "'?", function() {
            const response = DeleteChannelConfiguration(id);
            if(response.status === 'success'){
                location.reload();
            } else {
                alert('Non è possibile eliminare questa Configurazione');
            }
        });
    },
    switchDefault: function(configurationId){
        const response = UpdateDefaultChannelConfiguration(configurationId, this.channel.id);
        location.reload();
    }
},
created(){
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    var id = null;
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
    if(urlParams.has('id')){
        id = urlParams.get('id');
        const channelResponse = FindOneChannel(id);
        if(channelResponse.status === 'success'){
            const c = channelResponse.data;
            this.channel = {
                id: id,
                name: c.name,
                provider: c.provider
            }
            const confResponse = FindAllChannelConfiguration(id);
            if(confResponse.status === 'success'){
                this.configurations = confResponse.data;
            } else {
                this.serverError = true;
            }
        } else {
            this.serverError = true;
        }
        
    } else {
        window.location.href = "/backOffice/channel/channel";
    }
}
}); 
new Vue({ el: "#app" });
