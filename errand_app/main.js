import apiConfig from '@/config.js'
Vue.prototype.$baseUrl = process.env.NODE_ENV === "development" ? apiConfig.dev.baseUrl : apiConfig.prod.baseUrl
import App from './App'
import request from '@/utils/request.js'
Vue.prototype.$request = request
// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
import '@/static/css/global.css'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif