import Vue from 'vue'
import Router from 'vue-router'
// 页面
import Login from '@/views/Login'
import Home from '@/views/Home'
import Product from '@/views/Product'
import Primers from '@/views/Primers'
import Condiments from '@/views/Condiments'
import Pay from '@/views/Pay'


Vue.use(Router)


const routes = [
  // 路由配置信息
  {
    path: '/',
    redirect: '/Login'
  },
  {
    path: '/Login',
    name: '登陆',
    component: Login,
    meta: { title: '登陆'},
  },
  {
    path: '/Home',
    name: '首页',
    component: Home,
    meta: { title: '首页'},
  },
  {
    path: '/Product',
    name: '选产品',
    component: Product,
    meta: { title: '选产品' },
  },
  {
    path: '/Primers',
    name: '选底料',
    component: Primers,
    meta: { title: '选底料' },
  },
  {
    path: '/Condiments',
    name: '选小料',
    component: Condiments,
    meta: { title: '选小料' }
  },
  {
    path: '/Pay',
    name: '扫码支付',
    component: Pay,
    meta: { title: '扫码支付' }
  }
]

const router = new Router({
  // mode: 'history',
  routes
})

router.beforeEach((to, from, next) => {
  window.document.title = to.meta.title;
  // next()
  if(to.path == "/Login"){
    next()
  }else if( localStorage.getItem('username') != null){
      next()   
}else{
  next('/Login')
}

});


router.afterEach(() => {
  window.scrollTo(0, 0);
});


export default router