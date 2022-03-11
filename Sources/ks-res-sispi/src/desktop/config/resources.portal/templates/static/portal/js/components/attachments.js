import { AttachmentsJson } from '/static/portal/json/attachments-json.js';
import { ProceedingFooter } from '/static/portal/js/components/proceeding-footer.js';

export function Attachments(){
    var attachments = {
        template: `<div class="container step-container">
    <h4>Allegati aggiuntivi</h4>
    <h5>In questa sezione si possono caricare eventuali <strong>allegati aggiuntivi</strong> richiesti dalla pratica</h5>
    <div class="row">
        <div class="col-md-6 mt-3">
            <input type="file" id="newAttachment" name="newAttachment" v-on:change="previewFiles" />
        </div>
        <div class="col-md-6 mt-3">
            <button type="button" class="btn btn-primary btn-xs btn-block" v-on:click="addAttachment" :disabled="newAttachment == null"><i class="fa fa-folder-open"></i> Aggiungi allegato aggiuntivo</button>
        </div>
    </div>
    <hr />
    <span class="lead">Allegati già caricati</span>
    <div class="card-wrapper card-space" v-for="attachment in attachments">
        <div class="card card-bg card-big border-bottom-card">
            <div class="flag-icon"></div>
            <div class="etichetta">
                <button v-on:click="removeAttachment(attachment.id)" type="button" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> Elimina</button>
            </div>
            <div class="card-body">
                <h5>Documento: {{ attachment.documentFile }}</h5>
                <small class="text-muted" v-if="attachment.uploadDate != null">Caricato il {{attachment.uploadDate}}.</small>
                <small class="text-muted" v-if="attachment.acquisitionDate != null">Acquisito il {{attachment.acquisitionDate}}.</small>
                <small class="text-muted" v-if="attachment.acquisitionDate == null">Non ancora acquisito dall'ente.</small>
            </div>
        </div>
    </div>
    <my-proceeding-footer></my-proceeding-footer>
</div>
`,
        data() {
            return {
                number: +this.stepId + 3,
                attachments: AttachmentsJson(),
                output: null,
                newAttachment: null
            }
        },
        components: {
            'my-proceeding-footer': ProceedingFooter(),
        },
        methods: {
            addAttachment: function(){
                var attachment = jQuery.parseJSON(JSON.stringify(this.newAttachment));
                this.attachments.push(attachment);
                this.newAttachment = null;
            },
            previewFiles(event) {
                var id = 0;
                this.attachments.forEach(element => {
                    id = element.id + 1;
                });
                var obj = event.target.files[0];
                this.newAttachment = {
                    "id": id,
                    "documentFile": obj.name,
                    "uploadDate": new Date(),
                    "acquisitionDate": null,
                    "mimeType": obj.type,
                    "documentSize": obj.size
                };
            },
            removeAttachment(id){
                this.attachments.forEach(element => {
                    if(element.id === id){
                        const index = this.attachments.indexOf(element);
                        this.attachments.splice(index, 1);
                    }
                });
            }
        }
    }
    return attachments;
}