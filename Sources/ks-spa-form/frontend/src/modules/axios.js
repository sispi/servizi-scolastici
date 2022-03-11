import Vue from 'vue'
import axios from 'axios'
import nprogress from '@/modules/nprogress.js'

const instance = axios.create({
  baseURL: '/form-manager/api'
})

instance.interceptors.request.use(config => {
  nprogress.start()
  return config
})
instance.interceptors.response.use(
  response => {
    nprogress.done()
    return response
  },
  error => {
    nprogress.done()
    Vue.notify({
      type: 'error',
      title: error.response.data.message,
      text: error.response.status
    })
    return error
  }
)

export default instance
