Vue.component('App', { template: `
<div class="container-fluid">
<div class="container">
    <h2>Info & FAQ</h2>
    <div class="row">
        <div class="col-md-6">
            <h3>Informazioni sull'utilizzo del portale</h3>
            <p class="text-secondary text-justify">
                Il Portale del Cittadino rende disponibili gli sportelli virtuali dedicati a cittadini e imrpese per consentire l'accesso rapido e unificato ad un ampio numero di procedimenti amministrativi on-line messi a disposizione dai
                vari uffici.
            </p>
            <h4 class="font-weight-light">Portale del Cittadino</h4>
            <p class="text-secondary text-justify">
                In basso trovi le istruzioni su come interagire con il Portale. Se ha domande specifiche alle quali non trovi risposta puoi contattarci utilizzando
                <a class="underlineHover" href="#">questa form</a>.
            </p>
            <p class="text-secondary text-justify">
                <strong>Interagire e dialogare con gli uffici</strong><br />
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla quam velit, vulputate eu pharetra nec, mattis ac neque. Duis vulputate commodo lectus, ac blandit elit tincidunt id. Sed rhoncus, tortor sed eleifend tristique,
                tortor mauris molestie elit, et lacinia ipsum quam nec dui. Quisque nec mauris sit amet elit iaculis pretium sit amet quis magna. Aenean velit odio, elementum in tempus ut, vehicula eu diam. Pellentesque rhoncus aliquam
                mattis. Ut vulputate eros sed felis sodales nec vulputate justo hendrerit. Vivamus varius pretium ligula, a aliquam odio euismod sit amet. Quisque laoreet sem sit amet orci ullamcorper at ultricies metus viverra.
                Pellentesque arcu mauris, malesuada quis ornare accumsan, blandit sed diam.
            </p>
            <p class="text-secondary text-justify">
                <strong>Sportelli usabili e accessibili</strong><br />
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla quam velit, vulputate eu pharetra nec, mattis ac neque. Duis vulputate commodo lectus, ac blandit elit tincidunt id. Sed rhoncus, tortor sed eleifend tristique,
                tortor mauris molestie elit, et lacinia ipsum quam nec dui. Quisque nec mauris sit amet elit iaculis pretium sit amet quis magna. Aenean velit odio, elementum in tempus ut, vehicula eu diam. Pellentesque rhoncus aliquam
                mattis. Ut vulputate eros sed felis sodales nec vulputate justo hendrerit. Vivamus varius pretium ligula, a aliquam odio euismod sit amet. Quisque laoreet sem sit amet orci ullamcorper at ultricies metus viverra.
                Pellentesque arcu mauris, malesuada quis ornare accumsan, blandit sed diam.
            </p>
        </div>
        <div class="col-md-6">
            <h3>FAQ</h3>
            <div id="accordionDiv1" class="collapse-div" role="tablist">
                <div class="collapse-header" id="headingA1">
                    <button data-toggle="collapse" data-target="#accordion1" aria-expanded="true" aria-controls="accordion1">
                        Cerco informazioni su un procedimento amministrativo comunale. Cosa devo fare?
                    </button>
                </div>
                <div id="accordion1" class="collapse show" role="tabpanel" aria-labelledby="headingA1" data-parent="#accordionDiv1">
                    <div class="collapse-body">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla quam velit, vulputate eu pharetra nec, mattis ac neque. Duis vulputate commodo lectus, ac blandit elit tincidunt id. Sed rhoncus, tortor sed eleifend tristique,
                        tortor mauris molestie elit, et lacinia ipsum quam nec dui. Quisque nec mauris sit amet elit iaculis pretium sit amet quis magna. Aenean velit odio, elementum in tempus ut, vehicula eu diam. Pellentesque rhoncus aliquam mattis.
                        Ut vulputate eros sed felis sodales nec vulputate justo hendrerit. Vivamus varius pretium ligula, a aliquam odio euismod sit amet. Quisque laoreet sem sit amet orci ullamcorper at ultricies metus viverra. Pellentesque arcu
                        mauris, malesuada quis ornare accumsan, blandit sed diam.
                    </div>
                </div>
                <div class="collapse-header" id="headingA2">
                    <button data-toggle="collapse" data-target="#accordion2" aria-expanded="false" aria-controls="accordion2">
                        Di cosa si occupa il nuovo SUAP previsto dal DPR 160/2010?
                    </button>
                </div>
                <div id="accordion2" class="collapse" role="tabpanel" aria-labelledby="headingA2" data-parent="#accordionDiv1">
                    <div class="collapse-body">
                        Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them accusamus labore sustainable VHS.
                    </div>
                </div>
                <div class="collapse-header" id="headingA3">
                    <button data-toggle="collapse" data-target="#accordion3" aria-expanded="false" aria-controls="accordion3">
                        Come posso attivare la carta regionale dei servizi (CRS)?
                    </button>
                </div>
                <div id="accordion3" class="collapse" role="tabpanel" aria-labelledby="headingA3" data-parent="#accordionDiv1">
                    <div class="collapse-body">
                        Food truck quinoa nesciunt laborum eiusmod. Brunch 3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred
                        nesciunt sapiente ea proident.
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
`
}); 
new Vue({ el: "#app" });
