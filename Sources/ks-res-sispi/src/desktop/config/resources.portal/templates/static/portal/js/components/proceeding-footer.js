export function ProceedingFooter() {
    var proceedingFooter = {
        template: `
    <div>
        <span class="size">La validazione formale avviene solo nell'ultima sezione "Invio pratica".</span>
        <div class="mt-3">
            <div class="row">
                <div class="col-md-6">
                    <button type="button" class="btn btn-danger btn-block">Annulla</button>
                </div>
                <div class="col-md-6">
                    <button type="button" class="btn btn-primary btn-block">Salva la Pratica</button>
                </div>
            </div>
        </div>
    </div>
    `
    }
    return proceedingFooter;
}