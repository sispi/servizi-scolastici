export function Timeline() {
    var timeline = {
        template: `
<div class="container">
    <div class="alert alert-info" role="alert" v-if="!timelinePresent">
        Nessuna timeline trovata.
    </div>
    <div class="it-timeline-wrapper" v-else>
        <div class="row">
            <div class="col-12" v-for="step in steps">
                <!--COMPLETATO-->
                <div class="timeline-element" v-if="step.state === 2">
                    <div class="it-pin-wrapper it-evidence">
                        <div class="pin-icon text-white">
                            <i class="fa fa-check" aria-hidden="true"></i>
                        </div>
                        <div class="pin-text"><span>Completato</span></div>
                    </div>
                    <div class="card-wrapper">
                        <div class="card">
                            <div class="card-body">
                                <div class="category-top">
                                    <span class="category">Completato il</span>
                                    <span class="data">{{step.timestamp}}</span>
                                </div>
                                <h5 class="card-title">{{step.label}}</h5>
                            </div>
                        </div>
                    </div>
                </div>
                <!--IN ATTESA-->
                <div class="timeline-element" v-if="step.state === 1">
                    <div class="it-pin-wrapper it-now">
                        <div class="pin-icon text-white">
                            <i class="fa fa-ellipsis-h" aria-hidden="true"></i>
                        </div>
                        <div class="pin-text"><span>In attesa</span></div>
                    </div>
                    <div class="card-wrapper">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">{{step.label}}</h5>
                                <a style="text-decoration: none;" class="read-more col-md-11" v-bind:href="'/portal/features/task?id=' + taskToCompile.id" v-if="step.type === 'HT-cittadino' && taskToCompile != null">
                                    <button class="btn btn-primary col-md-12">Compila <span class="glyphicon glyphicon-arrow-right"></span></button>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <!--PROGRAMMATO-->
                <div class="timeline-element" v-if="step.state === 0">
                    <div class="it-pin-wrapper">
                        <div class="pin-icon">
                            <i class="fa fa-bullseye" aria-hidden="true"></i>
                        </div>
                        <div class="pin-text"><span>Programmato</span></div>
                    </div>
                    <div class="card-wrapper">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">{{step.label}}</h5>
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
        user: Object
      },
    data() {
      return {
        steps: [],
        taskToCompile: null,
        timelinePresent: true
      }
    },
    methods: {
    },
    created(){
        console.log('utente: ' + this.user);
        if(this.instance.variables.timeline.value != null){
            this.steps = this.instance.variables.timeline.value;
            const tasks = this.instance.tasks;
            for(var x = 0; x < tasks.length; x++){
                if(tasks[x].status === 'Ready' || tasks[x].status === 'InProgress'){
                    const potentialOwners = tasks[x].assignments.potentialOwners;
                    for(var y = 0; y < potentialOwners.length; y++){
                        if(potentialOwners[y] === this.user){
                            this.taskToCompile = tasks[x];
                            break;
                        }
                    }
                    if(this.taskToCompile != null){
                        break;
                    }
                }
            }
        } else {
            this.timelinePresent = false;
        }
    }
  }
  return timeline;
}
