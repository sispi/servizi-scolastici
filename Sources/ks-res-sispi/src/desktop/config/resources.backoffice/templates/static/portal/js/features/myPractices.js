import { InstancesNotSended } from '/static/portal/js/components/instances-not-sended.component.js?no-cache';
import { InstancesCompleted } from '/static/portal/js/components/instances-completed.component.js?no-cache';
import { InstancesToBeIntegrated } from '/static/portal/js/components/instances-to-be-integrated.component.js?no-cache';
import { InstancesInProgress } from '/static/portal/js/components/instances-in-progress.component.js?no-cache';

import { ProceedingsMap } from '/static/utilities/proceeding.utilities.js?no-cache';

var App = Vue.component('App', { template: `<div class="container">
    <h2>Benvenuto nel tuo sportello personale, {{username}}</h2>
    <p class="lead">
        In questa area personale e riservata potrai avviare le tue 
        pratiche online, revisionare quelle attualmente in corso e 
        scambiare messaggi con la Pubblica Amministrazione.
    </p>
    <a href="/public/services" class="btn btn-primary btn-lg btn-block">
        Avvia una nuova pratica
    </a>
    <hr />
    <h3>
        <span class="bd-content-title">Le tue pratiche</span>
    </h3>
    <ul class="nav nav-tabs" id="myTab" role="tablist">
        <li class="nav-item">
            <a class="nav-link active" id="tab1-tab" data-toggle="tab" href="#tab1" role="tab" aria-controls="tab1" aria-selected="true">
                Pratiche non inviate <span class="badge badge-danger" v-if="notifications.portal > 0">{{ notifications.portal }}</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab2-tab" data-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false">
                Pratiche da integrare <span class="badge badge-danger" v-if="notifications.bpm > 0">{{ notifications.bpm }}</span>
            </a>
        </li>
        <li class="nav-item"><a class="nav-link" id="tab4-tab" data-toggle="tab" href="#tab4" role="tab" aria-controls="tab4" aria-selected="false">Pratiche in lavorazione presso l'ente</a></li>
        <li class="nav-item"><a class="nav-link" id="tab3-tab" data-toggle="tab" href="#tab3" role="tab" aria-controls="tab3" aria-selected="false">Pratiche concluse</a></li>
    </ul>
    <div class="tab-content" id="myTabContent">
        <div class="tab-pane p-4 fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
            <instances-not-sended></instances-not-sended>
        </div>
        <div class="tab-pane p-4 fade" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
            <instances-to-be-integrated :proceedings="proceedings"></instances-to-be-integrated>
        </div>
        <div class="tab-pane p-4 fade" id="tab4" role="tabpanel" aria-labelledby="tab4-tab">
            <instances-in-progress :proceedings="proceedings"></instances-in-progress>
        </div>
        <div class="tab-pane p-4 fade" id="tab3" role="tabpanel" aria-labelledby="tab3-tab">
            <instances-completed :proceedings="proceedings"></instances-completed>
        </div>
    </div>
</div>
`,
data() {
    const user = username;
    return {
        username: user,
        completedPage: [],
        inProgressAtTheInstitution: [],
        notSendedPage: [],
        toBeIntegrated: [],
        proceedings: null,
        notifications: null
    }
},
components: {
    'instances-not-sended': InstancesNotSended(),
    'instances-completed': InstancesCompleted(),
    'instances-to-be-integrated': InstancesToBeIntegrated(),
    'instances-in-progress': InstancesInProgress()
},
created(){
    this.proceedings = ProceedingsMap();
    const notificationRes = notificationResponse;
    if(notificationRes.status === 'success'){
        this.notifications = notificationRes.data;
    }
}
}); 
new Vue({ el: "#app" });
