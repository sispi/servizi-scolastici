import { FindAllTasksForUser } from '/static/portal/js/services/task.service.js?no-cache';
import { FormatDateAndTime } from '/static/utilities/date.utilities.js?no-cache';
Vue.component('App', { template: `
<div class="container">
  <h2>Lista attività</h2>
  <p>
    Di seguito trovi la lista delle attività che hai gestito 
    o che devi ancora gestire.
  </p>
  <div class="it-list-wrapper" v-if="tasks.count > 0">
    <ul class="it-list">
      <li v-for="task in tasks.data">
        <a v-bind:href="'/portal/features/task?id=' + task.id">
          <div class="it-right-zone"><span class="text">{{ task.name }} - {{ task.status }}</span><span class="metadata">{{ formatDate(task.startTs) }}</span>
          </div>
        </a>
      </li>
    </ul>
  </div>
  <div class="alert alert-info" role="alert" v-else>
    "<b>Info</b>" Non è presente nessuna attività.
  </div>
  <div class="mt-3"></div>
  <nav class="pagination-wrapper justify-content-center" aria-label="Navigazione centrata">
    <ul class="pagination">
      <li class="page-item" v-bind:class="{ disabled: currentPage === 1 }">
        <a class="page-link" @click="previusPage" style="cursor: pointer;">
        <i class="fa fa-chevron-left" aria-hidden="true"></i>
          <span class="sr-only">Pagina precedente</span>
        </a>
      </li>

      <li class="page-item" v-if="currentPage === 1">
        <a class="page-link" aria-current="page">1</a>
      </li>
      <li class="page-item" v-if="currentPage === 1 && pages > 1">
        <a class="page-link">2</a>
      </li>

      <li class="page-item" v-if="currentPage > 1">
        <a class="page-link">{{currentPage - 1}}</a>
      </li>
      <li class="page-item" v-if="currentPage > 1">
        <a class="page-link" aria-current="page">{{currentPage}}</a>
      </li>
      <li class="page-item" v-if="currentPage > 1 && pages > currentPage">
        <a class="page-link">{{currentPage + 1}}</a>
      </li>

      <li class="page-item" v-bind:class="{ disabled: currentPage === pages }">
        <a class="page-link" @click="nextPage" style="cursor: pointer;">
          <span class="sr-only">Pagina successiva</span>
          <i class="fa fa-chevron-right" aria-hidden="true"></i>
        </a>
      </li>
    </ul>
  </nav>
</div>
`,
data() {
  // const result = data("data-tasks");
  const result = FindAllTasksForUser(1, 10).data;
  const count = result.count;
  const quotient = Math.floor(count / 10);
  const remainder = count % 10;
  var pageNumber = quotient;
  if(remainder > 0){
    pageNumber++;
  }
    return {
        tasks: result,
        currentPage: 1,
        pages: pageNumber
    }
},
methods: {
  nextPage: function(){
    this.currentPage = this.currentPage + 1;
    this.tasks = FindAllTasksForUser(this.currentPage, 10).data;
  },
  previusPage: function(){
    this.currentPage = this.currentPage - 1;
    this.tasks = FindAllTasksForUser(this.currentPage, 10).data;
  },
  specifyPage: function(page){
    this.currentPage = page;
    this.tasks = FindAllTasksForUser(this.currentPage, 10).data;
  },
  formatDate: function(date){
    return FormatDateAndTime(date);
  }
}
}); 
new Vue({ el: "#app" });
