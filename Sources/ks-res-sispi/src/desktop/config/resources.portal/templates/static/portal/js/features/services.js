import { GetServicesTree } from '/static/portal/js/services/service.service.js?no-cache';
import { TreeTemplate } from '/static/portal/js/components/serviceTreeTemplate.component.js?no-cache';

Vue.component('App', { template: `
<div class="container-fluid">
<div class="container">
    <h2>Servizi per il cittadino</h2>
    <p class="lead">
        Benvenuto {{userInfo.fullname}}, puoi selezionare il servizio di tuo interesse dall'elenco sottostante
    </p>

    <div class="alert alert-danger" role="alert" v-if="status != 'success'">
      <b>Attenzione!</b> Si è verificato un errore durante il caricamento dei dati. Riprovare più tardi.
    </div>
    <div v-if="status == 'success'">
      <div class="alert alert-info" role="alert" v-if="treeDatas.length == 0">
        Siamo spiacenti, non è presente nessun servizio.
      </div>
      <div class="it-list-wrapper">
        <ul id="demo" v-for="treeData in treeDatas" style="list-style-type: none;" class="it-list">
          <tree-item class="item" :item="treeData" @make-folder="makeFolder" @add-item="addItem"></tree-item>
        </ul>
      </div>
    </div>
    
</div>
</div>
`,
data() {
  const res = GetServicesTree();
  if(res.status === 'success'){
    return {
      treeDatas: res.data,
      status: res.status
    }
  } else {
    return {
      treeDatas: null,
      status: res.status
    }
  }
},
methods: {
  makeFolder: function(item) {
    Vue.set(item, "children", []);
    this.addItem(item);
  },
  addItem: function(item) {
    item.children.push({
      name: "new stuff"
    });
  }
},
components: {
  'tree-item': TreeTemplate()
}
}); 
new Vue({ el: "#app" });
