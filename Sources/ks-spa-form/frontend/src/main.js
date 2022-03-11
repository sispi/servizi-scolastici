import Vue from 'vue'
import App from '@/App.vue'
import router from '@/modules/router'
import axios from '@/modules/axios.js'
import nprogress from '@/modules/nprogress.js'
import 'nprogress/nprogress.css/'
import '@/modules/jquery.js'
import { BootstrapVue, BootstrapVueIcons } from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faSave, faEye, faCopy, faHistory, faCog, faPlus, faMinus, faArrowDown, faArrowUp, faPencilAlt, faEdit, faArrowsAlt, faTimes, faShareAlt, faPlusCircle, faFilter, faAsterisk, faThumbtack } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import CKEditor from 'ckeditor4-vue'
import moment from 'moment'
import Notifications from 'vue-notification'
import '@/assets/global.css'

Vue.config.productionTip = false
Vue.prototype.$axios = axios
Vue.prototype.$nprogress = nprogress
Vue.use(BootstrapVue)
Vue.use(BootstrapVueIcons)
Vue.use(Notifications)
Vue.use(CKEditor)

library.add(faSave)
library.add(faEye)
library.add(faCopy)
library.add(faHistory)
library.add(faCog)
library.add(faPlus)
library.add(faPlusCircle)
library.add(faMinus)
library.add(faArrowDown)
library.add(faArrowUp)
library.add(faPencilAlt)
library.add(faEdit)
library.add(faArrowsAlt)
library.add(faTimes)
library.add(faShareAlt)
library.add(faFilter)
library.add(faAsterisk)
library.add(faThumbtack)

Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.filter('formatDate', function (value) {
  if (value) {
    return moment(String(value)).format('DD/MM/YYYY HH:mm')
  }
})

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
