import { CompleteTask, SaveTask } from '/static/portal/js/services/task.service.js';

var start = new Vue({ 
    el: "#start-form2",
    data() {
        const t = data("data-task");
        var m = t.input;
        m.userinfo = data("data-user");
        return {
            task: t,
            portalInstance: data("data-instance"),
            model: m
        };
    },
    methods: {
        onKsFormAction(model, event){
            if(event === 'submit'){
                const complete = {
                    "autoProgress": true,
                    "mergeInput": true,
                    "input": model,
                    "output": model,
                    "message": null
                };
                const response = CompleteTask(complete, this.task.id);
                if(response.status === "success"){
                    window.alert('La pratica è stata completata');
                    location.href = "/portal/features/myPractices";
                } else {
                    console.log(response);
                }
            }
            if(event === 'save'){
                const save = {
                    "mergeInput": true,
                    "input": model,
                    "message": null
                };
                const response = SaveTask(save, this.task.id);
                if(response.status === "success"){
                    window.alert('La pratica è stata salvata');
                    location.href = "/portal/features/myPractices";
                } else {
                    console.log(response);
                }
            }
        }
      }
});
