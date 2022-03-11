import { GetServicesTree } from '/static/portal/js/services/service.service.js?no-cache';
import { TreeTemplate } from '/static/backOffice/js/components/serviceTreeTemplate.component.js?no-cache';
var App = Vue.component('App', { template: `
<div class="my-body-settings">
    <h2>
        <span class="title">Servizi</span>
        <a href="/backOffice/service/service-create" class="btn btn-primary btn-sm float-right" v-if="!serverError">
          <span class="glyphicon glyphicon-plus"></span> Crea un nuovo Servizio
        </a>
    </h2>
    <div v-if="!serverError">
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
        <ul id="demo" v-for="treeData in treeDatas" style="list-style-type: none;" class="it-list">
            <tree-item class="item" :item="treeData" @make-folder="makeFolder" @add-item="addItem"></tree-item>
        </ul>
    </div>
    <div v-else class="alert alert-danger" role="alert">
        Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
    </div>
</div>

`,
    data(){
        var paramSuccess = false;
        var paramError = false;
        var succMessage = '';
        var errMessage = '';
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        if(urlParams.has('success')){
            const success = urlParams.get('success');
            succMessage = success;
            paramSuccess = true;
        }
        if (urlParams.has('error')){
            const error = urlParams.get('error');
            errMessage = error;
            paramError = true;
        }
        const res = GetServicesTree();
        if(res.status === 'success'){
            return {
                treeDatas: res.data,
                status: res.status,
                alertSuccess: paramSuccess,
                successMessage: succMessage,
                alertError: paramError,
                errorMessage: errMessage,
                serverError: false
            }
        } else {
            return {
                treeDatas: null,
                status: res.status,
                alertSuccess: paramSuccess,
                successMessage: succMessage,
                alertError: paramError,
                errorMessage: errMessage,
                serverError: true
            }
        }
    },
    methods: {
        makeFolder: function(item) {
            Vue.set(item, "children", []);
            this.addItem(item);
        },
        addItem: function(item) {
            item.children.push({
                name: "new stuff"
            });
        }
    },
    components: {
        'tree-item': TreeTemplate()
    }
});
new Vue({ el: "#app" });
