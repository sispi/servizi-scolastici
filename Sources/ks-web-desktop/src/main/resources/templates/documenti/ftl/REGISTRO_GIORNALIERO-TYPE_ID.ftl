<div class="card content-grey">
    <div class="row col-lg-12 col-md-12 col-sm-12">
        <div class="col-lg-12 col-md-12 col-sm-12">
            <p class="title-proto"><i class="fas fa-calendar-alt blk"></i> {{messages["viewProfile.regGiornaliero"] || "Registro Giornaliero"}} <br></p>
            <div class="space-h"></div>
            <p class="desc-proto"><i class="fas fa-ellipsis-v grey"></i> {{messages["viewProfile.numIniziale"] || "Numero iniziale"}} : <strong>{{documento.NUMERO_INIZIALE}}</strong></p>
            <hr>
            <p class="desc-proto"><i class="fas fa-ellipsis-v grey"></i> {{messages["viewProfile.numFinale"] || "Numero finale"}} : <strong>{{documento.NUMERO_FINALE}}</strong><br></p>
            <hr>
            <p class="desc-proto"><i class="fas fa-ellipsis-v grey"></i> {{messages["viewProfile.inizioRegistrazioni"] || "Inizio registrazioni"}} : <strong>{{date(documento.DATA_INIZIO_REGISTRAZIONI)}}</strong><br></p>
            <hr>
            <p class="desc-proto"><i class="fas fa-ellipsis-v grey"></i> {{messages["viewProfile.fineRegistrazioni"] || "Fine registrazioni"}} : <strong>{{date(documento.DATA_FINE_REGISTRAZIONI)}}</strong><br></p>
        </div>
    </div>
</div>