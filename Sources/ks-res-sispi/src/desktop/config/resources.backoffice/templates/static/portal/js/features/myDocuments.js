import { Document } from '/static/portal/js/components/documents.component.js?no-cache';

Vue.component('App', { template: `<div class="container">
    <h2>I Tuoi Documenti</h2>
    <ul class="nav nav-tabs" id="myTab" role="tablist">
        <li class="nav-item"><a class="nav-link active" id="tab1-tab" data-toggle="tab" href="#tab1" role="tab" aria-controls="tab1" aria-selected="true">Documenti inviati</a></li>
        <li class="nav-item"><a class="nav-link" id="tab2-tab" data-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false">Documenti ricevuti</a></li>
    </ul>
    <div class="tab-content" id="myTabContent">
        <div class="tab-pane p-4 fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
            <my-documents-sent></my-documents-sent>
        </div>
        <div class="tab-pane p-4 fade" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
            <my-documents-received></my-documents-received>
        </div>
    </div>
</div>
`,
    data() {
        return {
            documents: [
                {
                    id: 1,
                    type: 'SENT',
                    name: 'Documenti Inviati'
                },
                {
                    id: 2,
                    type: 'RECEIVED',
                    name: 'Documenti Ricevuti'
                }
            ]
        };
    },
    methods: { },
    components: {
        'my-documents-sent': Document('SENT'),
        'my-documents-received': Document('RECEIVED')
    }
});

new Vue({ el: "#app" });
