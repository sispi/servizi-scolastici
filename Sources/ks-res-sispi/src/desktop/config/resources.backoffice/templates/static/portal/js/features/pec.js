var App = Vue.component('App', { template: `<div class="container">
  <h2>Contatti PEC</h2>
  <p class="lead text-justify">
      Di seguito trovi tutti i recapiti di posta elettronica certificata (PEC) per poter scrivere direttamente agli uffici competenti.
  </p>
  <p class="text-justify">
      Vuoi presentare istanze o comunicazioni al comune di SPOLTORE direttamente da casa tua? Puoi scrivere ad uno degli indirizzi di Posta Elettronica Certificata (PEC). Se lo fai dalla tua casella di PEC hai la certezza che la tua
      comunicazione arrivi al destinatario: automaticamente ottieni una ricevuta di ritorno telematica con pieno valore legale.
  </p>
  <p class="text-justify">
      Seleziona, di seguito, lo sportello a cui sei interessato ed invia una email PEC.
  </p>
  <div class="row mt-3">
      <div class="col-md-4">
          <div class="card-wrapper">
              <div class="card">
                  <div class="card-body">
                      <h5 class="card-title">Ufficio 1</h5>
                      <p class="card-text">
                          Responsabile: Mario Rossi<br />
                          Indirizzo PEC: <a href="mailto:abc@example.com">abc@example.com</a>
                      </p>
                  </div>
              </div>
          </div>
      </div>
      <div class="col-md-4">
          <div class="card-wrapper">
              <div class="card">
                  <div class="card-body">
                      <h5 class="card-title">Ufficio 2</h5>
                      <p class="card-text">
                          Responsabile: Mario Rossi<br />
                          Indirizzo PEC: <a href="mailto:abc@example.com">abc@example.com</a>
                      </p>
                  </div>
              </div>
          </div>
      </div>
      <div class="col-md-4">
          <div class="card-wrapper">
              <div class="card">
                  <div class="card-body">
                      <h5 class="card-title">Ufficio 3</h5>
                      <p class="card-text">
                          Responsabile: Mario Rossi<br />
                          Indirizzo PEC: <a href="mailto:abc@example.com">abc@example.com</a>
                      </p>
                  </div>
              </div>
          </div>
      </div>
  </div>
</div>
`
}); 
new Vue({ el: "#app" });
