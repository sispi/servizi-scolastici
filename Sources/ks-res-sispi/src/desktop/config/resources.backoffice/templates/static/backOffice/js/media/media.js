import { FindAllMediaPaged } from '/static/portal/js/services/media.service.js?no-cache';
import { DeleteMedia } from '/static/portal/js/services/media.service.js?no-cache';

var App = Vue.component('App', { template: `
<div class="my-body-settings">
    <h2>
        <span class="title">Media</span>
        <a href="/backOffice/media/media-create" class="btn btn-primary btn-sm float-right" v-if="!serverError">
          <span class="glyphicon glyphicon-plus"></span> Crea un nuovo Media
        </a>
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
        <div v-if="page.count > 0">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Nome</th>
                            <th style="text-align:center" scope="col">Descrizione</th>
                            <th style="text-align:center" scope="col">Tipo del file</th>
                            <th style="text-align:center" scope="col">Estensione del file</th>
                            <th style="text-align:center" scope="col">Grandezza</th>
                            <th style="text-align:center" scope="col">Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="media in medias" v-if="media.myKey != 'logo'">
                            <th scope="row">{{media.id}}</th>
                            <td>{{media.name}}</td>
                            <td style="text-align:center">{{media.description}}</td>
                            <td style="text-align:center">{{media.fileType}}</td>
                            <td style="text-align:center">{{media.mimeType}}</td>
                            <td style="text-align:center">{{formattNumber(media.size / 1024)}} Kb</td>
                            <td style="text-align:center">
                                <a style="min-width:90px;" class="btn btn-secondary btn-sm" v-bind:href="'/backOffice/media/media-create?id=' + media.id">
                                    <span class="glyphicon glyphicon-pencil"></span>
                                    Modifica
                                </a>
                                <a class="btn btn-info btn-sm" v-bind:href="'/backOffice/media/media-detail?id=' + media.id">
                                    <span class="glyphicon glyphicon-eye-open"></span>
                                    Vedi
                                </a>
                                <button style="min-width:90px;" type="button" class="btn btn-danger btn-sm" @click="deleteM(media.id, media.name)">
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
            Non è presente nessun Media.
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
            page: null,
            medias: null,
            serverError: false,
            currentPage: 1,
            pages: null
        }
    },
    methods: {
        deleteM: function(id, name){
            confirm ("Sei sicuro di voler eliminare il media '" + name + "'?", function() {
                const response = DeleteMedia(id);
                if(response.status === 'success'){
                    location.reload();
                } else {
                    alert('Non è possibile eliminare questo Media');
                }
            });
        },
        formattNumber: function(number){
            return number.toFixed(2);
        }
    },
    created(){
        const response = FindAllMediaPaged();
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
            const pagedList = response.data;
            this.page = pagedList;
            this.medias = pagedList.data;
            this.serverError = false;

            const count = pagedList.count;
            const quotient = Math.floor(count / 10);
            const remainder = count % 10;
            var pageNumber = quotient;
            if(remainder > 0){
                pageNumber++;
            }
            this.pages = pageNumber;
        } else {
            this.serverError = true;
        }
    }
});
new Vue({ el: "#app" });
