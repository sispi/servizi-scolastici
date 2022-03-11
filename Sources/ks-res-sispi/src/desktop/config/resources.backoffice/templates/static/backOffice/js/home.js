import { HomeCards } from '/static/backOffice/js/components/home-cards.component.js?no-cache';
import { ActivitiesInCharge } from '/static/backOffice/js/components/activities-in-charge.component.js?no-cache';
import { InstancesCharts } from '/static/backOffice/js/components/instances-charts.component.js?no-cache';
import { PaymentsResume } from '/static/backOffice/js/components/payments-resume.component.js?no-cache';
import { PaymentsChart } from '/static/backOffice/js/components/payments-chart.component.js?no-cache';
import { TasksForTheInstitution } from '/static/backOffice/js/components/tasks.component.js?no-cache';

var App = Vue.component('App', { template: `
<div class="my-body-settings">
    <template v-if="userInfo.username=='admin'">
        <home-cards></home-cards>
    </template>
    <institution-tasks></institution-tasks>
    <activities-in-charge></activities-in-charge>
    <instances-charts></instances-charts>
    <template v-if="userInfo.username=='admin'">
        <payments-resume></payments-resume>
        <payments-chart></payments-chart>
    </template>
</div>
`,
    data(){
        return {
            columnChart: null
        }
    },
    components: {
        'home-cards': HomeCards(),
        'activities-in-charge': ActivitiesInCharge(),
        'instances-charts': InstancesCharts(),
        'payments-resume': PaymentsResume(),
        'payments-chart': PaymentsChart(),
        'institution-tasks': TasksForTheInstitution()
    }
});
new Vue({ el: "#app" });
