export function EuroFormatter(euro){
    var formatter = new Intl.NumberFormat('it-IT', {
        style: 'currency',
        currency: 'EUR'
    });
    var importoFormattato = formatter.format(euro);
    return importoFormattato;
}