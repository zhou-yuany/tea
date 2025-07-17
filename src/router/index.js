import Vue from 'vue'
import Router from 'vue-router'
// 页面
import header from '../components/header'
import Login from '@/views/Login'
import Home from '@/views/Home'
import Detail from '@/views/Detail'
import Pickupcode from '@/views/Pickupcode'
import Index from '@/views/Index'
import { getToken } from '@/utils/auth'
import store from '@/store'


Vue.use(Router)


const routes = [
   // 路由配置信息
   {
    path: '/',
    redirect: '/Index'
  },
  
  // {
  //   path: '/Login',
  //   name: '登陆',
  //   component: Login,
  //   meta: { title: '登陆'},
  // },
  {
    path: '/Home',
    name: '首页',
    component: Home,
    meta: { title: '首页',alive:true},
  },
  {
    path: '/Detail',
    name: '详情',
    component: Detail,
    meta: { title: '详情',alive:true }
  },
  {
    path: '/Pickupcode',
    name: '取餐码',
    component: Pickupcode,
    meta: { title: '取餐码',alive:true }
  },
  {
    path: '/Index',
    name: '主页',
    component: Index,
    meta: { title: '主页',alive:true }
  }
]

const router = new Router({
  // mode: 'history',
  routes
})

// router.beforeEach((to, from, next) => {
//   window.document.title = to.meta.title;
//   next()
  
//   if(to.path == "/Login"){
//     next()
//   }else if( localStorage.getItem('username') != null){
//       next()   
// }else{
//   next('/Login')
// }

// let query = {};
//     window.location.search.slice(1).split("&").forEach(function (item) {
//       let arr = item.split("=");
//       query[arr[0]] = arr[1];
//     });
//     if (to.path === '/Login') {
//       next()
      
//     } else if (query.access_token != undefined) {

//       localStorage.setItem('access_token', query.access_token);
//       localStorage.setItem('token_type', query.token_type);
//       localStorage.setItem('expires_in', query.expires_in);
//       localStorage.setItem('refresh_token', query.refresh_token)
//       // 拉取饿了么/美团店铺信息
//       store.dispatch('getShopInfo').then(res => {
//         localStorage.setItem("shopInfo", res.data.shopInfo)
//         let url = document.location.href;
//         let reg = /[^\w](access_token|token_type|expires_in|refresh_token)=?([^&|^#]*)/g;
//         url = url.replace(reg, "");
//         reg = /&&/g;
//         url = url.replace(reg, "");
//         reg = /&#/g;
//         url = url.replace(reg, "#");
//         reg = /\?#/g;
//         url = url.replace(reg, "#");
//         // url = url.replaceAll(document.domain,"");
//         // url = url.replaceAll("http://","");
//         // url = url.replaceAll("https://","");
//         reg = /\?#/g;
//         url = url.replace(reg, "#");
//         window.history.replaceState(null, null, url);
//         next()

//       }).catch((err) => {
//         store.dispatch('FedLogOut').then(() => {
//           Message.error(err || 'Verification failed, please login again')
//           next({ path: '/' })
//         })
//       })

//     } else {

//       store.dispatch('getInfo').then(res => { // 拉取用户信息
//         console.log()
//         localStorage.setItem("divideAccounts", res.data.divideAccounts);
//         localStorage.setItem("username", res.data.adminInfo.username);
//         localStorage.setItem("adminId", res.data.adminInfo.id);
//         localStorage.setItem("shopId", res.data.shopId);

//         if (!res.data.authUrl) {
//           next()
//         } else if (localStorage.getItem("access_token")) {
//           next()
//         } else {
//           window.location.assign(res.data.authUrl);

//         }


//       }).catch((err) => {
//         store.dispatch('FedLogOut').then(() => {
//           Message.error(err || 'Verification failed, please login again')
//           next({ path: '/' })
//         })
//       })


//     }

// });



router.afterEach(() => {
  window.scrollTo(0, 0);
});


export default router