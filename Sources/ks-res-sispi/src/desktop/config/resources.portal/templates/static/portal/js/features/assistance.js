var App = Vue.component('App', { template: `<div class="container">
    <h2>Supporto e Assistenza</h2>
    <p class="lead">
        Utilizza questo form per richiedere supporto o assistenza circa un determinato procedimento
    </p>
    <div class="card-wrapper">
        <div class="card">
            <div class="card-body">
                <div class="form-row">
                    <div class="form-group col-md-12">
                        <div class="bootstrap-select-wrapper">
                            <label>Procedimento</label>
                            <select title="Scegli una opzione" v-model="selectedMethod">
                                <option v-for="myMethod in myMethods" v-bind:value="myMethod.value">{{myMethod.methodName}}</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <input type="text" class="form-control" id="username" v-bind:value="username" disabled />
                        <label for="username">Nome</label>
                    </div>
                    <div class="form-group col-md-6">
                        <input type="email" class="form-control" id="email" v-bind:value="email" disabled />
                        <label for="email">Email</label>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-12">
                        <input type="text" class="form-control" id="object" v-model="object" />
                        <label for="object">Oggetto</label>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-12">
                        <label for="message">Messaggio</label>
                        <textarea id="message" rows="3" required placeholder="Messaggio" v-model="message"></textarea>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary mt-3" v-on:click="getFormValues">Invia Email</button>
            </div>
        </div>
    </div>
</div>
`, 
data() { 
    return {
        username: 'Davide Simboli',
        email: 'davide.simboli@aesys.tech',
        myMethods: [ 
            { 
                methodName: 'Procedimento 1',
                value: '1' 
            }, 
            { 
                methodName: 'Procedimento 2',
                value: '2' 
            }, 
            { 
                methodName: 'Procedimento 3',
                value: '3' 
            } 
        ],
        selectedMethod: null,
        object: null,
        message: null,
        output: null
    };
},
methods: {
    getFormValues: function() {
      this.output = {
          'selectedMethod': this.selectedMethod,
          'username': this.username,
          'email': this.email,
          'object': this.object,
          'message': this.message
      };
      console.log(jQuery.parseJSON(JSON.stringify(this.output)));
    }
  }
}); 
new Vue({ el: "#app" });
