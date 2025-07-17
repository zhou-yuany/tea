import Vue from 'vue'
import App from './App'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI);

import router from './router'

// import 'amfe-flexible';
import 'lib-flexible/flexible.js';

import '../src/assets/main.css'  //引用公用css

import 'swiper/swiper.css'  //积分中心swiper

// import { Tab,Tabs,Toast,row,col } from 'vant'; //积分兑换样式 消息提示
// Vue.use(Tab)
// Vue.use(Tabs)
// Vue.use(Toast)
// Vue.use(row)
// Vue.use(col)

import vant from 'vant';
import 'vant/lib/index.css';
Vue.use(vant)

// 打印
import Print from 'vue-print-nb'

Vue.use(Print);
Vue.prototype.$print = Print;

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
