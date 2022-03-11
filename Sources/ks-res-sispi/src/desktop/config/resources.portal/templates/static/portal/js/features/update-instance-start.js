import { UpdateInstance } from '/static/portal/js/services/instance.service.js';

var update = new Vue({
    el: "#start-form2",
    data() {
        var x =  {
            instance: data("data-instance"),
            configuration: data("data-configuration"),
            saving: false
        };
        x.model =  JSON.parse(x.instance.model);
        x.model.userinfo = data("data-user");
        return x;
    },
    methods: {
        onKsFormAction: function(model, event){
            if (event=='back') {
                this.$refs.form.previous();
                return;
            }
            //Con l'evento submit si procede con l'avvio della pratica
            if(event === 'submit'  && !this.saving) {
                this.saving = true;

                const portalInstance = {
                    'id': this.instance.id,
                    'model': JSON.stringify(model),
                    'send': true,
                    'input': model
                    // 'proceedingId': this.instance.proceeding.id,
                    // 'creationDate': this.instance.creationDate,
                    //'operationId': null,
                    //'processId': this.configuration.deployment.processId,
                };

                const portalResponse = UpdateInstance(portalInstance);
                if(portalResponse.status === "success"){
                    //istanza aggiornata correttamente nel portale
                    window.alert('La tua pratica è stata inviata');
                    setTimeout(() => 
                        {
                            if(portalResponse.data.bpmInstanceId != null) {
                                location.href = "/portal/features/proceedingInstanceManage?id=" + portalResponse.data.bpmInstanceId;
                            } else {
                                location.href = "/portal/features/myPractices";
                            }
                        }, 
                        1500);
                } else {
                    //errore durante l'aggiornamento dell'istanza nel portale
                    console.log('error messages', portalResponse.status.responseJSON.details.messages);
                    this.saving = false;
                    if(portalResponse.status.responseJSON.details.messages[0] === 'Expired') {
                        window.alert('Non puoi inviare la pratica perché è SCADUTA.');
                    } else {
                        window.alert('Abbiamo riscontrato un errore');
                    }
                }
            }
            //Con l'evento save si procede con il salvataggio dello stato della pratica
            if(event === 'save' && !this.saving){
                this.saving = true;
                const portalInstance = {
                    'id': this.instance.id,
                    // 'proceedingId': this.instance.proceeding.id,
                    // 'creationDate': this.instance.creationDate,
                    'model': JSON.stringify(model),
                    // 'dispatchDate': null,
                    'send': false,
                    // 'bpmInstanceId': null
                };
                const portalResponse = UpdateInstance(portalInstance);
                if(portalResponse.status === "success"){
                    //istanza aggiornata correttamente nel portale
                    window.alert('La tua pratica è stata aggiornata');
                    setTimeout(() => 
                        {
                            location.href = "/portal/features/myPractices"
                        }, 
                        1500);
                } else {
                    //errore durante l'aggiornamento dell'istanza nel portale
                    this.saving = false;
                    window.alert('Abbiamo riscontrato un errore');
                }
            }
        }
    }
});
