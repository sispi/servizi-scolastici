export function Timeline() {
    var timeline = {
        template: `
<div class="container">
    <template v-if="!timelinePresent">
        <div class="alert alert-info" role="alert" >
            Nessuna timeline trovata.
        </div>
    </template>
    <template v-else>
    
    <template v-for="step in steps">
            <div class="col-12 taskToDo" v-if="step.state === 1">
                Il procedimento e' fermo allo step {{step.step}} - <strong>{{step.label}}</strong>
                <a class="btn-taskToDo" v-bind:href="'/portal/features/task?id=' + taskToCompile.id" v-if="step.type === 'HT-cittadino' && taskToCompile != null">
                    Vai al {{step.label}} <span class="glyphicon glyphicon-arrow-right"></span>
                </a>
            </div>
        </template>
    <div class="col-md-12">
        <span style="font-size: 1.2rem;font-weight: bold;padding-left: 25px;" class="col-md-6 border-bottom float-left text-left"><span title="in carico Utente" class="glyphicon glyphicon-user"></span> Cittadino</span>
        <span style="font-size: 1.2rem;font-weight: bold;padding-right: 40px;" class="col-md-6 border-bottom float-right text-right"><span title="in carico Utente" class="glyphicon glyphicon-home"></span> Ente</span>
    </div>
    <br>
        <div v-for="step in steps">
         <!--aggiunto style alla classe container per bug primo elemento timeline su firefox-->
          <div class="container" style="display: inline-block;">
            <!--no path-->
            <div v-if="step.previousType==step.type" class="row timeline"></div>
            <!--path da sinistra a destra-->
            <div v-else-if="step.previousType=='HT-cittadino'" class="row timeline">
                <div class="col-2"><div class="corner top-right"></div></div>
                <div class="col-8"><hr/></div>
                <div class="col-2"><div class="corner left-bottom"></div></div>
            </div>
            <!--path da destra a sinistra-->
            <div v-else class="row timeline">
                <div class="col-2"><div class="corner right-bottom"></div></div>
                <div class="col-8"><hr/></div>
                <div class="col-2"><div class="corner top-left"></div></div>
            </div>
            
            <!-- CITTADINO -->
            <template v-if="step.type=='HT-cittadino'">
              <div class="row align-items-center how-it-works d-flex cittadinoContent">
                <div class="col-2 text-center bottom d-inline-flex justify-content-center align-items-center">
                  <!--COMPLETATO-->
                  <div v-if="step.state === 2" style="background-color: #008758" class="circle font-weight-bold">{{step.step}}</div>
                  <!--IN ATTESA-->
                  <div v-if="step.state === 1" style="background-color: #CC0038" class="circle font-weight-bold">{{step.step}}</div>
                  <!--PROGRAMMATO-->
                  <div v-if="step.state === 0" class="circle font-weight-bold">{{step.step}}</div>
                </div>
                <div style="margin-top: 30px;margin-bottom: 30px;" class="col-6">
                  <strong>{{step.label}}</strong> <br>
                  <span v-if="step.timestamp"> completato il {{step.timestamp}} </span>
                    <a style="text-decoration: none;" class="read-more" v-bind:href="'/portal/features/task?id=' + taskToCompile.id" v-if="step.state === 1 && taskToCompile != null">
                        <button class="btn btn-primary col-md-8">Vai al {{step.label}} <span class="glyphicon glyphicon-arrow-right"></span></button>
                    </a>
                </div>
              </div>
            </template>
            <!-- ENTE -->
            <template v-else>
                <div class="row align-items-center justify-content-end how-it-works d-flex enteContent">
                  <div style="margin-top: 30px;margin-bottom: 30px;" class="col-6 text-right">
                    <strong>{{step.label}}</strong>
                    <br> <span v-if="step.timestamp"> completato il {{step.timestamp}}</span>
                  </div>
                  <div class="col-2 text-center full d-inline-flex justify-content-center align-items-center">
                    <!--COMPLETATO-->
                    <div v-if="step.state === 2" style="background-color: #008758" class="circle font-weight-bold">{{step.step}}</div>
                    <!--IN ATTESA-->
                    <div v-if="step.state === 1" style="background-color: #CC0038" class="circle font-weight-bold">{{step.step}}</div>
                    <!--PROGRAMMATO-->
                    <div v-if="step.state === 0" class="circle font-weight-bold">{{step.step}}</div>
                  </div>
                </div>
            </template>
          </div>
        </div>
    
        <!-- vecchia timeline
        <div class="it-timeline-wrapper">
            <div class="row">
                <div class="col-12" v-for="step in steps">
                    COMPLETATO
                    <div class="timeline-element" v-if="step.state === 2">
                        <div class="it-pin-wrapper it-evidence">
                            <div class="pin-icon text-white">
                                <i class="fa fa-check" aria-hidden="true"></i>
                            </div>
                            <div class="pin-text"><span>Step {{step.step}}</span></div>
                        </div>
                        <div class="card-wrapper">
                            <div class="card">
                                <div class="card-body">
                                    <h7 class="card-title">
                                        <template v-if="step.type=='HT-cittadino'">
                                            <span title="in carico Utente" class="glyphicon glyphicon-user"></span>
                                        </template>
                                        <template v-else>
                                            <span title="in carico Ente" class="glyphicon glyphicon-home"></span>
                                        </template>
                                        <strong>{{step.label}}</strong> <br> completato il {{step.timestamp}}
                                    </h7>
                                </div>
                            </div>
                        </div>
                    </div>
                    IN ATTESA
                    <div class="timeline-element" v-if="step.state === 1">
                        <div class="it-pin-wrapper it-now">
                            <div class="pin-icon text-white">
                                <i class="fa fa-ellipsis-h" aria-hidden="true"></i>
                            </div>
                            <div class="pin-text"><span>Step {{step.step}}</span></div>
                        </div>
                        <div class="card-wrapper">
                            <div class="card">
                                <div class="card-body">
                                    <h7 class="card-title">
                                        <template v-if="step.type=='HT-cittadino'">
                                            <span title="in carico Utente" class="glyphicon glyphicon-user"></span>
                                        </template>
                                        <template v-else>
                                            <span title="in carico Ente" class="glyphicon glyphicon-home"></span>
                                        </template>
                                        <strong>{{step.label}}</strong>
                                    </h7><hr><br><br>
                                    <a style="text-decoration: none;" class="read-more col-md-11" v-bind:href="'/portal/features/task?id=' + taskToCompile.id" v-if="step.type === 'HT-cittadino' && taskToCompile != null">
                                        <button class="btn btn-primary col-md-12">Vai al {{step.label}} <span class="glyphicon glyphicon-arrow-right"></span></button>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    PROGRAMMATO
                    <div class="timeline-element" v-if="step.state === 0">
                        <div class="it-pin-wrapper">
                            <div class="pin-icon">
                                <i class="fa fa-ellipsis-h" aria-hidden="true"></i>
                            </div>
                            <div class="pin-text"><span>Step {{step.step}}</span></div>
                        </div>
                        <div class="card-wrapper">
                            <div class="card">
                                <div class="card-body">
                                    <h7 class="card-title">
                                        <template v-if="step.type=='HT-cittadino'">
                                            <span title="in carico Utente" class="glyphicon glyphicon-user"></span>
                                        </template>
                                        <template v-else>
                                            <span title="in carico Ente" class="glyphicon glyphicon-home"></span>
                                        </template>
                                        <strong>{{step.label}}</strong>
                                    </h7>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        -->
       
    </template>
</div>
    `,
        props: {
            instance: Object,
            user: Object
        },
        data() {
            return {
                steps: [],
                /*steps: [
                    {label: "Compilazione modulo di pagamento",state: 2,step: 1,timestamp: "08/09/2021 08:13:31",type: "HT-cittadino"},
                    {label: "Pagamento su PagoPA",state: 1,step: 2,type: "HT-cittadino"},
                    {label: "Verifica dati inseriti",state: 0,step: 3,type: ""},
                    {label: "Caricamento documentazione",state: 0,step: 4,timestamp: "",type: "HT-cittadino"},
                    {label: "Conferma Ente per fine procedura",state: 0,step: 5,timestamp: "",type: ""}
                ],*/
                taskToCompile: null,
                timelinePresent: true,
                typeLastStep: null
            }
        },
        methods: {
        },
        mounted: function () {
            if(this.typeLastStep == "HT-cittadino"){
                $( ".cittadinoContent:last" ).removeClass( "how-it-works" ).addClass( "how-it-works-last" );
            }else{
                $( ".enteContent:last" ).removeClass( "how-it-works" ).addClass( "how-it-works-last" );
            }
        },
        created(){
            if(this.instance.variables.timeline.value != null){
                this.steps = this.instance.variables.timeline.value;
                //aggiungo il type dello step precedente con la chiave previousType

                for(var y = 0; y < this.steps.length; y++){
                    if(y==this.steps.length-1){
                        this.steps[y].lastStep="true";
                        if(this.steps[y].type=='HT-cittadino'){
                            this.typeLastStep = "HT-cittadino";
                        }else{
                            this.typeLastStep = "";
                        }
                    }else{
                        this.steps[y].lastStep="false";
                    }
                    if(y>0){
                        this.steps[y].previousType=this.steps[y-1].type;
                    }else{
                        this.steps[y].previousType=this.steps[y].type;
                    }
                }

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
