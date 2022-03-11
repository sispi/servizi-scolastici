import { FindAllInstances } from '/static/portal/js/services/instance.service.js?no-cache';
import { FormatDateAndTime } from '/static/utilities/date.utilities.js?no-cache';
export function ActivitiesInCharge() {
    var activitiesInCharge = {
        template: `
<div>
    <div class="mt-5"></div>
    <div class="row">
        <div class="col-md-12">
            <div class="row">
                <div class="col-md-6"><h4>Istanze</h4></div>
                <div class="col-md-6"><a href="/bpm/instances" class="float-right">Vedi tutte <i class="fa fa-angle-right" aria-hidden="true"></i></a></div>
            </div>
            <div class="spinner-border text-secondary" role="status" v-if="isLoading"></div>
            <div class="table-responsive" v-if="!serverError">
                <div v-if="instances.length > 0">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th scope="col">Istanza</th>
                                <th scope="col">Data</th>
                                <th scope="col">Avviato da</th>
                                <th scope="col">Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="instance in instances">
                                <td><strong>{{ instance.businessName }}</strong></td>
                                <td>{{ formatDateAndTime(instance.startTs) }}</td>
                                <td>{{ instance.variables.userinfo?.value.FIRST_NAME }} {{ instance.variables.userinfo?.value.LAST_NAME }}</td>
                                <td><a v-bind:href="'/bpm/instances/details?id=' + instance.id" class="btn btn-primary"><i class="fa fa-eye" aria-hidden="true"></i> Vedi</a></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div v-else class="alert alert-danger" role="alert">
                Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
            </div>
        </div>
    </div>
</div>
        `,
        data() {
            return {
                instances: [],
                serverError: false,
                isLoading: true
            };
        },
        methods: {
            formatDateAndTime: function(date){
                return FormatDateAndTime(date);
            }
        },
        created() {
            const response = FindAllInstances(1, 5, 'startTs:DESC', 'variables(@userinfo,value)', true, false);
            if(response.status === 'success'){
                this.instances = response.data.data;
                this.serverError = false;
                this.isLoading = false;
            } else {
                this.serverError = true;
                this.isLoading = false;
            }
        }
    };
    return activitiesInCharge;
}
