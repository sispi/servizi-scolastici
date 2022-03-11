import Vue from 'vue'
import VueRouter from 'vue-router'
import nprogress from '@/modules/nprogress.js'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/console'
  },
  {
    path: '/index.html',
    redirect: '/console'
  },
  {
    path: '/console',
    name: 'Console',
    component: () => import(/* webpackChunkName: "console" */ '@/views/Console.vue'),
    meta: { title: 'Console' }
  },
  {
    path: '/editor/:formId',
    name: 'Editor',
    props: true,
    component: () => import(/* webpackChunkName: "editor" */ '@/views/Editor.vue'),
    meta: { title: 'Form Editor' }
  },
  // {
  //   path: '/preview/:formId',
  //   name: 'Preview',
  //   props: true,
  //   component: () => import(/* webpackChunkName: "preview" */ '@/views/Preview.vue'),
  //   meta: { title: 'Form Preview' }
  // },
  {
    path: '*',
    component: () => import('@/views/Error404.vue')
  }
]

const DEFAULT_TITLE = 'Form Manager'
const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeResolve((to, from, next) => {
  nprogress.start()
  next()
})

router.afterEach((to, from) => {
  nprogress.done()
  Vue.nextTick(() => {
    document.title = to.meta.title || DEFAULT_TITLE
  })
})

export default router
