import { Logo } from '/static/backOffice/js/components/logo.component.js?no-cache';
var App = Vue.component('App', { template: `
<div class="container-fluid">
    <div class="container">
        <h2><span class="title">Impostazioni</span></h2>
        <my-logo></my-logo>
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
        return {
            alertSuccess: paramSuccess,
            successMessage: succMessage,
            alertError: paramError,
            errorMessage: errMessage
        }
    },
    components: {
        'my-logo': Logo()
    }
});
new Vue({ el: "#app" });
