import { FindUserByUsername } from '/static/portal/js/services/user.service.js?no-cache';
var App = Vue.component('App', { template: `
<div class="container-fluid">
    <div class="container">
        <h2>Servizi Online</h2>
        <p class="lead">
            Attraverso il Portale dei Servizi online l'Amministrazione comunale di 
            Palermo offre un servizio più trasparente e più vicino alle istanze dei 
            suoi cittadini.
        </p>
        <p class="lead">
            L'attivazione di tale strumento contribuisce a migliorare la qualità 
            delle prestazioni garantite dalla pubblica amministrazione con l'obiettivo 
            esplicito di migliorare Palermo che, sempre più, è proiettata a diventare 
            una città intelligente ed inclusiva.
        </p>
        <p class="lead">
            La nostra città, in questo modo, utilizza le grandi potenzialità delle 
            nuove tecnologie per migliorare il rapporto tra istituzioni pubbliche 
            e territorio, in una logica di partecipazione e condivisione.
        </p>
        <p class="lead">
            Il Portale dei Servizi online è parte del sito web istituzionale del 
            Città di Palermo; entrambi gli strumenti sono in continua evoluzione e 
            aggiornamento per contribuire a rendere limpida e visibile tutta l'attività 
            amministrativa del Comune di Palermo ed avvicinarla sempre più ai cittadini.
        </p>
    </div>
    <div class="notification with-icon success dismissable" role="alert" aria-labelledby="not2dms-title" id="not2dms">
        <h5 id="not2dms-title"><i class="fa fa-user" aria-hidden="true"></i> Profilo utente</h5>
        <p>Clicca <a href="/portal/user/myProfile">qui</a> per completare il tuo profilo utente.</p>
        <button type="button" class="btn notification-close">
            <i class="fa fa-times" aria-hidden="true"></i>
            <span class="sr-only">Chiudi notifica: Profilo utente</span>
        </button>
    </div>
</div>
`,created: function () {
    const response = FindUserByUsername(username);
    if(response.status.status === 404 && username != 'guest'){
        $(document).ready(function() { 
            notificationShow('not2dms'); 
        });
    }
},
}); 
new Vue({ el: "#app" });
