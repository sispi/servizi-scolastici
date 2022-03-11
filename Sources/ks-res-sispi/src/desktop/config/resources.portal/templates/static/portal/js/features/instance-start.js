import { CreateInstance } from '/static/portal/js/services/instance.service.js';
import { canCreateInstance } from '/static/utilities/proceeding.utilities.js?no-cache';

var start = new Vue({ 
    el: "#start-form2",
    data() {
        const m = {
            userinfo: data("data-user")
        }
        var x = {
            proceeding: data("data-proceeding"),
            configuration: data("data-configuration"),
            model: m,
            saving: false
        }
        return x;
    },
    methods: {
        canCreateInstance: function() {
            canCreateInstance(this.proceeding);
        },
        onKsFormAction(model, event){//onKsFormAction(event, model)
            //if (this.canCreateInstance()){
                //Con l'evento submit si procede con l'avvio della pratica
                if(this.saving) {
                    window.alert('Sto salvando la tua pratica. Attendi...');
                }
                if (event=='back') {
                    this.$refs.form.previous();
                    return;
                }
                if(event === 'submit' && !this.saving) {
                    this.saving = true;

                    const portalInstance = {
                        'proceedingId': this.proceeding.id,
                        'model': JSON.stringify(model),
                        // 'operationId': null,
                        // 'processId': this.configuration.deployment.processId,
                        // 'input': model
                    };

                    const portalResponse = CreateInstance(portalInstance);
                    if(portalResponse.status === "success") {
                        //istanza salvata correttamente nel portale
                        window.alert('La tua pratica è stata inviata');
                        location.href = "/portal/features/myPractices";
                    } else {
                        //errore durante l'aggiornamento dell'istanza nel portale
                        this.saving = false;
                        window.alert('Abbiamo riscontrato un errore');
                    }
                }
                //Con l'evento save si procede con il salvataggio dello stato della pratica
                if(event === 'save' && !this.saving) {
                    this.saving = true;
                    const draftPortalInstance = {
                        'proceedingId': this.proceeding.id,
                        'model': JSON.stringify(model),
                        // 'creationDate': new Date(),
                        // 'dispatchDate': null,
                        // 'sent': false,
                        // 'bpmInstanceId': null
                    };
                    const portalResponse = CreateInstance(draftPortalInstance);
                    if(portalResponse.status === "success"){
                        //istanza aggiornata correttamente nel portale
                        window.alert('La tua pratica è stata salvata');
                        location.href = "/portal/features/myPractices";
                    } else {
                        //errore durante l'aggiornamento dell'istanza nel portale
                        this.saving = false;
                        window.alert('Abbiamo riscontrato un errore');
                    }
                }
            //}
        }
    }
});
