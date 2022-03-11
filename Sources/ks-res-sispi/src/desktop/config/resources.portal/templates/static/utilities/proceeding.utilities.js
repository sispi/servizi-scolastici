import { CountCompletedInstances } from '/static/portal/js/services/instance.service.js';
import { CountActiveInstances } from '/static/portal/js/services/instance.service.js';
import { FindAllProceedings } from '/static/portal/js/services/proceeding.service.js?no-cache';

export function canCreateInstance(proceeding) {
    if (!checkProceedingDates(proceeding)) {
        return "error.outOfTerms";
    }
    if (proceeding.multipleInstance) {
        return true;
    }
    const activeInstances = CountActiveInstances(proceeding.configurationId);
    if (activeInstances.status === "success") {
         if (activeInstances.data.count > 0) {
             return "error.activeInstance";
         }
    } else {
        //errore durante il conteggio delle istanze
        window.alert('Abbiamo riscontrato un errore');
        return "error.generic";
    }
    if (!proceeding.uniqueInstance) {
        return true;
    }
    const instances = CountCompletedInstances(proceeding.configurationId);
    if (instances.status === "success") {
         return instances.data.coun == 0 ? true : "error.uniqueInstance";
    } else {
        //errore durante il conteggio delle istanze
        window.alert('Abbiamo riscontrato un errore');
        return "error.generic";
    }
}

export function checkProceedingDates(proceeding) {
    return ((new Date(proceeding.startDate) <= new Date()) && (new Date(proceeding.endDate) >= new Date()));
}

export function ProceedingsMap() {
    const proceedingResponse = FindAllProceedings();
    var proceedings = {};
    if (proceedingResponse.status === "success") {
        proceedingResponse.data.data.forEach(proceeding => {
            proceedings[proceeding.processId] = proceeding;
        });
        return proceedings;
    } else {
        return proceedings;
    }
}

export function AssociateServiceAndProceeding(instances, proceedings){
    instances.forEach(instance => {
        if(proceedings){
            instance.proceeding = proceedings[instance.processId];
        }
    });
    return instances;
}
