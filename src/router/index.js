import Vue from 'vue'
import Router from 'vue-router'
import header from '../components/header'
// 页面
import Login from '@/views/Login'
import Home from '@/views/Home'
import Food from '@/views/food'
import Order from '@/views/order'
import Callnum from '@/views/callnum'
import Form from '@/views/form'
import Print from '@/views/print'
import Inventory from '@/views/inventory'
import Formula from '@/views/formula'


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
    meta: { title: '首页', alive: true},
  },
  {
    path: '/food',
    name: '饮品点单',
    component: Food,
    meta: { title: '饮品点单', alive: true},
  },
  {
    path: '/order',
    name: '订单',
    component: Order,
    meta: { title: '订单', alive: true},
  },
  {
    path: '/callnum',
    name: '叫号取餐',
    component: Callnum,
    meta: { title: '叫号取餐', alive: true},
  },
  {
    path: '/form',
    name: '报表',
    component: Form,
    meta: { title: '报表', alive: true},
  },
  {
    path: '/print',
    name: '打印',
    component: Print,
    meta: { title: '打印', alive: true},
  },
  {
    path: '/inventory',
    name: '库存',
    component: Inventory,
    meta: { title: '库存', alive: true},
  },
  {
    path: '/formula',
    name: '配方',
    component: Formula,
    meta: { title: '配方', alive: true},
  },

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