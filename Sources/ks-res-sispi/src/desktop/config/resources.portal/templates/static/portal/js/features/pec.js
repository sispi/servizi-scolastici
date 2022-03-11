var App = Vue.component('App', { template: `
<div class="container">
    <h2>Contatti PEC</h2>
    <p class="lead text-justify">
        Di seguito trovi tutti i recapiti di posta elettronica certificata (PEC) per poter scrivere direttamente agli uffici competenti.
    </p>
    <p class="text-justify">
        In riferimento alle iniziative volte al contenimento della diffusione del Covid-19, fino al termine dell'emergenza, i recapiti di rete fissa non saranno presidiati.
    </p>
    <p class="text-justify">
        Si invitano pertanto gli utenti che avessero necessità di mettersi in contatto con gli uffici, ad inoltrare le loro richieste utilizzando i seguenti canali di posta elettronica.
    </p>
    <div class="row mt-3">
        <div class="col-md-6">
            <div class="card-wrapper">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Per problematiche legate alla registrazione al portale</h5>
                        <p class="card-text">Posta Elettronica: <a href="servizionline@comune.palermo.it">servizionline@comune.palermo.it</a></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card-wrapper">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Per problematiche legate alla registrazione al portale</h5>
                        <p class="card-text">Posta Elettronica Certificata : <a href="servizionline@cert.comune.palermo.it">servizionline@cert.comune.palermo.it</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row mt-3">
        <div class="col-md-6">
            <div class="card-wrapper">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Per problematiche legate alla materia tributi</h5>
                        <p class="card-text">Posta Elettronica: <a href="settoretributi@comune.palermo.it">settoretributi@comune.palermo.it</a></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card-wrapper">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Per problematiche legate alla materia tributi</h5>
                        <p class="card-text">Posta Elettronica Certificata : <a href="settoretributi@cert.comune.palermo.it">settoretributi@cert.comune.palermo.it</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
`
}); 
new Vue({ el: "#app" });
