var App = Vue.component('App', { template: `
<div class="container">
    <h2>Modifica Password</h2>
    <p class="lead">
        Modifica la tua password
    </p>

    <div class="form-row">
        <div class="form-group col-md-6">
            <input type="password" class="form-control" id="password" v-model="password" />
            <label for="password">Nuova password</label>
            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
        </div>
        <div class="form-group col-md-6">
            <input type="password" class="form-control" id="confirmPassword" v-model="confirmPassword" />
            <label for="confirmPassword">Conferma nuova password</label>
            <small id="formGroupExampleInputWithHelpDescription" class="form-text text-muted">* Campo obbligatorio</small>
        </div>
    </div>

    <p class="text-secondary">
        Lapassword deve essere composta da:
        <ul>
            <li>Minimo 8 caratteri alfanumerici</li>
            <li>Almeno un carattere maiuscolo</li>
            <li>Almeno un carattere minuscolo</li>
        </ul>
    </p>
    <button type="button" v-on:click="getFormValues" class="btn btn-primary">Salva</button>
</div>
`, 
data() { 
    return {
        password: null,
        confirmPassword: null,
        output: null
    };
},
methods: {
    getFormValues: function() {
      this.output = {
          'password': this.password,
          'confirmPassword': this.confirmPassword
      };
      console.log(jQuery.parseJSON(JSON.stringify(this.output)));
    }
  }
}); 
new Vue({ el: "#app" });
