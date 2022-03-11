import { CreateInstance } from '/static/portal/js/services/instance.service.js';
import { CreateBpmInstance } from '/static/portal/js/services/instance.service.js';
import { canCreateInstance } from '/static/utilities/proceeding.utilities.js?no-cache';

var start = new Vue({ 
    el: "#start-form2",
    data() {
        return {
            proceeding: data("data-proceeding"),
            configuration: data("data-configuration")
        };
    },
    methods: {
        canCreateInstance: function() {
            canCreateInstance(this.proceeding);
        },
        onKsFormAction(model, event){//onKsFormAction(event, model)
            //if (this.canCreateInstance()){
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
                            'proceedingId': this.proceeding.id,
                            'creationDate': new Date(),
                            'model': JSON.stringify(model),
                            'dispatchDate': new Date(),
                            'sent': true,
                            'bpmInstanceId': bpmResponse.data.id
                        };
                        const portalResponse = CreateInstance(portalInstance);
                        if(portalResponse.status === "success"){
                            //istanza salvata correttamente nel portale
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
                        'proceedingId': this.proceeding.id,
                        'creationDate': new Date(),
                        'model': JSON.stringify(model),
                        'dispatchDate': null,
                        'sent': false,
                        'bpmInstanceId': null
                    };
                    const portalResponse = CreateInstance(portalInstance);
                    if(portalResponse.status === "success"){
                        //istanza aggiornata correttamente nel portale
                        window.alert('La tua pratica è stata salvata');
                        location.href = "/portal/features/myPractices";
                    } else {
                        //errore durante l'aggiornamento dell'istanza nel portale
                        window.alert('Abbiamo riscontrato un errore');
                    }
                }
            //}
        }
    }
});
