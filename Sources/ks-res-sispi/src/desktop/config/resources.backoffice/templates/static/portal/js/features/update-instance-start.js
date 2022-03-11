import { UpdateInstance } from '/static/portal/js/services/instance.service.js';
import { CreateBpmInstance } from '/static/portal/js/services/instance.service.js';

var update = new Vue({
    el: "#start-form2",
    data() {
        var x =  {
            instance: data("data-instance"),
            configuration: data("data-configuration"),
        };
        x.model =  JSON.parse(x.instance.model);
        return x;
    },
    methods: {
        onKsFormAction: function(model, event){
            //Con l'evento submit si procede con l'avvio della pratica
            if(event === 'submit'){
                const bpmInstance = {
                    'operationId': null,
                    'processId': this.configuration.deployment.processId,
                    'input': model
                };
                
                const bpmResponse = CreateBpmInstance(bpmInstance);
                if(bpmResponse.status === "success"){
                    //istanza creata correttamente nel bpm
                    const portalInstance = {
                        'id': this.instance.id,
                        'proceedingId': this.instance.proceeding.id,
                        'creationDate': this.instance.creationDate,
                        'model': JSON.stringify(model),
                        'dispatchDate': new Date(),
                        'sent': true,
                        'bpmInstanceId': bpmResponse.data.id
                    };
                    const portalResponse = UpdateInstance(portalInstance);
                    if(portalResponse.status === "success"){
                        //istanza aggiornata correttamente nel portale
                        window.alert('La tua pratica è stata inviata');
                        location.href = "/portal/features/myPractices";
                    } else {
                        //errore durante l'aggiornamento dell'istanza nel portale
                        window.alert('Abbiamo riscontrato un errore');
                    }
                } else {
                    //errore durante la creazione dell'istanza nel bpm
                    window.alert('Abbiamo riscontrato un errore');
                }
            }
            //Con l'evento save si procede con il salvataggio dello stato della pratica
            if(event === 'save'){
                const portalInstance = {
                    'id': this.instance.id,
                    'proceedingId': this.instance.proceeding.id,
                    'creationDate': this.instance.creationDate,
                    'model': JSON.stringify(model),
                    'dispatchDate': null,
                    'sent': false,
                    'bpmInstanceId': null
                };
                const portalResponse = UpdateInstance(portalInstance);
                if(portalResponse.status === "success"){
                    //istanza aggiornata correttamente nel portale
                    window.alert('La tua pratica è stata aggiornata');
                    location.href = "/portal/features/myPractices";
                } else {
                    //errore durante l'aggiornamento dell'istanza nel portale
                    window.alert('Abbiamo riscontrato un errore');
                }
            }
        }
    }
});
