import { MandatoryModules } from '/static/portal/json/mandatory-modules-json.js';
import { ProceedingFooter } from '/static/portal/js/components/proceeding-footer.js?no-cache';

export function MandatoryModule() {
    var mandatoryModule = {
        template: `
    <div class="container step-container">
        <h4>Moduli obbligatori</h4>
        <h5>Compilazione dei <strong>moduli obbligatori</strong> per la pratica richiesta</h5>
        <div class="card-wrapper card-space" v-for="module in modules">
            <div class="card card-bg card-big border-bottom-card">
                <div class="card-header">
                    <h5>{{ module.agencyModule.name }}</h5>
                    <small class="text-muted" v-if="module.uploadDate != null">File caricato: <i>{{ module.documentFile }}</i></small>
                </div>
                <div class="card-body">
                    <small class="text-muted" v-if="module.uploadDate != null">Caricato il {{module.uploadDate}}.</small>
                    <small class="text-muted" v-if="module.uploadDate == null">Non ancora caricato.</small>
                    <small class="text-muted" v-if="module.acquisitionDate != null">Acquisito il {{module.acquisitionDate}}.</small>
                    <small class="text-muted" v-if="module.acquisitionDate == null">Non ancora acquisito dall'ente.</small>
                    <div class="row">
                        <div class="col-md-12" v-if="module.uploadDate != null">
                            <button type="button" class="btn btn-primary btn-sm"><i class="fa fa-eye" aria-hidden="true"></i> Visualizza</button>
                            <button type="button" class="btn btn-danger btn-sm"><i class="fa fa-trash" aria-hidden="true"></i> Elimina</button>
                        </div>
                        <div class="col-md-12" v-if="module.uploadDate == null">
                            <button type="button" class="btn btn-primary btn-sm"><i class="fa fa-keyboard" aria-hidden="true"></i> Compila Online</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <my-proceeding-footer></my-proceeding-footer>
    </div>
    `,
        props: ['stepId', 'description', 'proceedingsInstanceId'],
        data() {
            return {
                number: +this.stepId + 3,
                modules: MandatoryModules()
            }
        },
        components: {
            'my-proceeding-footer': ProceedingFooter(),
        }
    }
    return mandatoryModule;
}
