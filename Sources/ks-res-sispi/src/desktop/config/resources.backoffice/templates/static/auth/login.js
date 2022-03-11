var App = Vue.component('App', { template: `<div class="container">
    <h2>Autenticazione Utente</h2>
    <p class="lead">
        Fai login per usufruire di tutti i servizi offerti dal portale
    </p>
    <ul class="nav nav-tabs" id="myTab" role="tablist">
        <li class="nav-item">
            <a class="nav-link active" id="tab1-tab" data-toggle="tab" href="#tab1" role="tab" aria-controls="tab1" aria-selected="true">
                Sistema di autenticazione del portale
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab2-tab" data-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false">
                Sistema di autenticazione digitale
            </a>
        </li>
    </ul>
    <div class="tab-content" id="myTabContent">
        <div class="tab-pane p-4 fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
            <form id="executeLogin" action="/auth/executeLogin" method="POST">
            <div class="form-group">
                <input type="text" class="form-control" name="username" id="username" v-model="username" />
                <input type="hidden" class="form-control" name="aoo" id="aoo" v-model="aoo" />
                <label for="username">Username</label>
                <div class="valid-feedback">Validato!</div>
            </div>
            <div class="form-group">
                <input type="password" class="form-control" name="password" id="password" v-model="password" />
                <label for="password">Password</label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" id="disabledFieldsetCheck" v-model="remember" />
                <label class="form-check-label" for="disabledFieldsetCheck">
                    Ricordami
                </label>
            </div>
            <button type="submit" class="btn btn-primary mt-3" v-on:click0="getFormValues">Accedi</button>
            </form>
            <p class="mt-3">
                <a class="underlineHover" href="#">Hai dimenticato la tua password?</a><br />
                Non hai ancora un acccount sul nostro portale? <a class="underlineHover" href="portal/auth/register">Registrati</a>
            </p>
        </div>
        <div class="tab-pane p-4 fade" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
            <button type="submit" class="btn btn-primary btn-lg mt-3">Accedi con SPID</button>
            <p class="mt-3">Non hai ancora le credenziali SPID? <a class="underlineHover" href="#">Richiedi SPID</a></p>
        </div>
    </div>
</div>
`, 
data() { 
    return {
        username: null,
        password: null,
        aoo: aoo,
        remember: false,
        output: null
    };
},
methods: {
    getFormValues: function() {
      this.output = {
          'username': this.username,
          'password': this.password,
          'aoo': this.aoo,
          'remember': this.remember
      };
      console.log(jQuery.parseJSON(JSON.stringify(this.output)));
    }
  }
}); 
var login = new Vue({ el: "#app" });
