import { FormatDateAndTime } from '/static/utilities/date.utilities.js?no-cache';
import { LongDateFormat } from '/static/utilities/date.utilities.js?no-cache';
import { TimeFormat } from '/static/utilities/date.utilities.js?no-cache';
import { FindAllInstanceTasks } from '/static/portal/js/services/instance.service.js?no-cache';

export function AllActivities() {
    var allActivities = {
        template: `
<div class="container">
<!--
    <div class="row">
        <div class="col-md-4"><span class="glyphicon glyphicon-calendar"></span><strong> Pratica creata: </strong><em>{{ formatDate(instance.startTs) }}</em></div>
        <div class="col-md-4"><span class="glyphicon glyphicon-calendar"></span><strong> Ultima attività: </strong><em>{{ formatDate(instance.lastActivityTs) }}</em></div>
        <div class="col-md-4" v-if="instance.endTs"><span class="glyphicon glyphicon-calendar"></span><strong> Pratica conclusa: </strong><em>{{ formatDate(instance.endTs) }}</em></div>
    </div>
    -->

    <div class="it-timeline-wrapper">
        <div class="row">
            <div class="col-12">
                <div class="timeline-element">
                    <div class="it-pin-wrapper it-evidence">
                        <div class="pin-icon text-white">
                            <i class="fa fa-check" aria-hidden="true"></i>
                        </div>
                        <div class="pin-text"><span>{{ getLongDateFormat(instance.startTs) }}</span></div>
                    </div>
                    <div class="card-wrapper">
                        <div class="card">
                            <div class="card-body">
                                <div class="category-top">
                                    <span class="category">Completato</span>
                                    <span class="data">{{ formatDate(instance.startTs) }}</span>
                                </div>
                                <h5 class="card-title">Pratica creata</h5>
                                <p class="card-text">La tua pratica è stata correttamente creata</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-12" v-for="task in tasks">
                <div class="timeline-element" v-if="task.status === 'Completed'">
                    <div class="it-pin-wrapper it-evidence">
                        <div class="pin-icon text-white">
                            <i class="fa fa-check" aria-hidden="true"></i>
                        </div>
                        <div class="pin-text"><span>{{ getLongDateFormat(task.startTs) }}</span></div>
                    </div>
                    <div class="card-wrapper">
                        <div class="card">
                            <div class="card-body">
                                <div class="category-top">
                                    <span class="category">Completato</span>
                                    <span class="data">{{ formatDate(task.endTs) }}</span>
                                </div>
                                <h5 class="card-title">{{task.name}}</h5>
                                <p class="card-text">{{task.description}}</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="timeline-element" v-if="task.status === 'Ready'">
                    <div class="it-pin-wrapper it-now">
                        <div class="pin-icon text-white">
                            <i class="fa fa-ellipsis-h" aria-hidden="true"></i>
                        </div>
                        <div class="pin-text"><span>{{ getLongDateFormat(task.startTs) }}</span></div>
                    </div>
                    <div class="card-wrapper">
                        <div class="card">
                            <div class="card-body">
                                <div class="category-top">
                                    <span class="category">Pronto per la compilazione</span>
                                </div>
                                <h5 class="card-title">{{task.name}}</h5>
                                <p class="card-text">{{task.description}}</p>
                                <a class="read-more" v-bind:href="'/portal/features/task?id=' + task.id">
                                    <span class="text">Compila <span class="glyphicon glyphicon-arrow-right"></span></span>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="timeline-element" v-if="task.status === 'InProgress'">
                    <div class="it-pin-wrapper it-now">
                        <div class="pin-icon text-white">
                            <i class="fa fa-ellipsis-h" aria-hidden="true"></i>
                        </div>
                        <div class="pin-text"><span>{{ getLongDateFormat(task.startTs) }}</span></div>
                    </div>
                    <div class="card-wrapper">
                        <div class="card">
                            <div class="card-body">
                                <div class="category-top">
                                    <span class="category">In lavorazione</span>
                                </div>
                                <h5 class="card-title">{{task.name}}</h5>
                                <p class="card-text">{{task.description}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-12" v-if="instance.endTs">
                <div class="timeline-element">
                    <div class="it-pin-wrapper it-evidence">
                        <div class="pin-icon text-white">
                            <i class="fa fa-check" aria-hidden="true"></i>
                        </div>
                        <div class="pin-text"><span>{{ getLongDateFormat(instance.endTs) }}</span></div>
                    </div>
                    <div class="card-wrapper">
                        <div class="card">
                            <div class="card-body">
                                <div class="category-top">
                                    <span class="category">Completato</span>
                                    <span class="data">{{ formatDate(instance.endTs) }}</span>
                                </div>
                                <h5 class="card-title">Pratica conclusa</h5>
                                <p class="card-text">La tua pratica è stata conclusa</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
          </div>
        </div>
    </div>
</div>

    `,
    props: {
      instance: Object,
    },
    data() {
      return {
        tasks: []
      }
    },
    methods: {
      formatDate: function(date){
          return FormatDateAndTime(date);
      },
      getLongDateFormat: function(date){
          return LongDateFormat(date);
      },
      getTimeFromDate: function(date){
          return TimeFormat(date);
      }
    },
    created(){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        var id = null;
        if(urlParams.has('id')){
            id = urlParams.get('id');
        }
        const response = FindAllInstanceTasks(id, false);
        if(response.status === 'success'){
            const pagedTasks = response.data;
            this.tasks = pagedTasks.data;
        }
    }
  }
  return allActivities;
}
