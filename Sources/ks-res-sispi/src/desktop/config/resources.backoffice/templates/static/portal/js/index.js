import { FindUserByUsername } from '/static/portal/js/services/user.service.js?no-cache';
var App = Vue.component('App', { template: `
<div class="container-fluid">
    <div class="container">
        <h2>Servizi Online</h2>
        <p class="lead">
            Lo spazio virtuale rivolto ai cittadini per avviare pratiche online, ricevere, fornire o consultare la documentazione di interesse e dialogare con gli uffici amministrativi.
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
