import { FindAllTasksForTheInstitution } from '/static/portal/js/services/task.service.js?no-cache';
import { FormatDateAndTime } from '/static/utilities/date.utilities.js?no-cache';
export function TasksForTheInstitution() {
    var tasksForTheInstitution = {
        template: `
<div>
    <div class="mt-5"></div>
    <div class="row">
        <div class="col-md-12">
            <div class="row">
                <div class="col-md-6"><h4>Task da completare</h4></div>
                <div class="col-md-6"><a href="/bpm/tasks?status=Ready&status=InProgress&assignedAs=PotentialOwner" class="float-right">Vedi tutte <i class="fa fa-angle-right" aria-hidden="true"></i></a></div>
            </div>
            <div class="spinner-border text-secondary" role="status" v-if="isLoading"></div>
            <div class="table-responsive" v-if="!serverError">
                <div v-if="tasks.length > 0">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th scope="col">Nome</th>
                                <th scope="col">Soggetto</th>
                                <th scope="col">Stato</th>
                                <th scope="col">Data di inizio</th>
                                <th scope="col">Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="task in tasks">
                                <td><strong>{{ task.name }}</strong></td>
                                <td>{{ task.subject }}</td>
                                <td>{{ task.status }}</td>
                                <td>{{ formatDateAndTime(task.startTs) }}</td>
                                <td><a v-bind:href="'/bpm/instances/task?id=' + task.id" class="btn btn-primary"><i class="fa fa-eye" aria-hidden="true"></i> Vedi</a></td>
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
                tasks: [],
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
            const response = FindAllTasksForTheInstitution(1, 5, 'startTs:DESC');
            if(response.status === 'success'){
                this.tasks = response.data.data;
                this.serverError = false;
                this.isLoading = false;
            } else {
                this.serverError = true;
                this.isLoading = false;
            }
        }
    };
    return tasksForTheInstitution;
}
