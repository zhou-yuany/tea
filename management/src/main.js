import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import router from './router'
import store from './store'

import '@/icons' // icon
import '@/permission' // permission control

import moment from 'moment'

// import Element from 'element-ui'
// import './styles/variables.scss'
// 修改el-dialog 默认点击遮照不关闭
ElementUI.Dialog.props.closeOnClickModal.default=false;

import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css'
Vue.use(Viewer)
Viewer.setDefaults({
  Options: { 'inline': true, 'button': true, 'navbar': true, 'title': true, 'toolbar': true, 'tooltip': true, 'movable': true, 'zoomable': true, 'rotatable': true, 'scalable': true, 'transition': true, 'fullscreen': true, 'keyboard': true, 'url': 'data-source' }
})



//在main.js中
// 注意引入 Blob.js 和 Export2Excel.js 的路径
import Blob from '@/utils/Blob.js'    
import Export2Excel from '@/utils/Export2Excel.js'


//全局过滤器
Vue.filter('dateFormat', (input, formatString = "YYYY-MM-DD") => {
  return moment(input).format(formatString)
})

Vue.use(ElementUI, { locale })

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
