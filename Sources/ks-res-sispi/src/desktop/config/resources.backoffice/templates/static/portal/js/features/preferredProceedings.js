import { DeleteFavoriteProceeding } from '/static/portal/js/services/favoriteProceeding.service.js?no-cache';
import { FindAllFavoriteProceedings } from '/static/portal/js/services/favoriteProceeding.service.js?no-cache';
Vue.component('App', { template: `
<div class="container">
    <h2>Procedimenti preferiti</h2>
    <div class="alert alert-danger" role="alert" v-if="status != 'success'"><b>Attenzione!</b> Si è verificato un errore durante il caricamento dei dati. Riprovare più tardi.</div>
    <div v-if="status == 'success'">
        <div class="alert alert-info" role="alert" v-if="preferredProceedings.length === 0">
            Non hai nessun procedimento preferito.
        </div>
        <div class="table-responsive" v-else>
            <table class="table">
                <thead>
                    <tr>
                        <th scope="col">Servizio</th>
                        <th scope="col">Procedimento</th>
                        <th scope="col">Avviabile</th>
                        <th scope="col"></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="preferredProceeding in preferredProceedings">
                        <td>{{ preferredProceeding.proceeding.service.name }}</td>
                        <td>{{ preferredProceeding.proceeding.title }}</td>
                        <td v-if="preferredProceeding.proceeding.isActive"><i style="color:#008758" class="fa fa-thumbs-up" aria-hidden="true" title="Procedimento avviabile"></i></td>
                        <td v-else><i style="color:#d9364f" class="fa fa-hand-paper" aria-hidden="true" title="Procedimento non avviabile"></i></td>
                        <td>
                            <a v-bind:href="'/public/proceedingDetail?id=' + preferredProceeding.proceeding.id" class="btn btn-primary btn-xs"> <i class="fa fa-eye" aria-hidden="true"></i> Vedi </a>
                            <button type="button" class="btn btn-danger btn-xs" v-on:click="removePreferredProceeding(preferredProceeding.id, preferredProceeding.proceeding.title)"><i class="fa fa-times" aria-hidden="true"></i> Rimuovi</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
`,
    data() {
        const res = FindAllFavoriteProceedings();
        if(res.status === 'success'){
            return {
                preferredProceedings: res.data,
                status: res.status
            }
        } else {
            return {
                preferredProceedings: null,
                status: res.status
            }
        }/*
    return {
        preferredProceedings: data("data-favoriteProceeding")
    }*/
    },
    methods: {
        removePreferredProceeding(id, name){
            confirm ("Sei sicuro di voler il procedimento '" + name + "' dai preferiti?", function() {
                const response = DeleteFavoriteProceeding(id);
                if(response.status === 'success'){
                    location.reload();
                } else {
                    alert('Non è possibile rimuovere questo elemento');
                }
            });
        }
    }
});
new Vue({ el: "#app" });
