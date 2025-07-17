import router from './router'
import { constantRouterMap, admin,Aivide  } from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth'
import Layout from "./views/layout/Layout";
import Vue from 'vue'

const whiteList = ['/login'] // 不重定向白名单

router.beforeEach((to, from, next) => {
  NProgress.start()

  if (getToken()) {
    let query = {};
    window.location.search.slice(1).split("&").forEach(function (item) {
      let arr = item.split("=");
      query[arr[0]] = arr[1];
    });
    if (to.path === '/login') {

      next()
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    }else if(query.code != undefined){
      localStorage.setItem('mtCode', query.code);
      localStorage.setItem('mtState', query.state);
      // 获取美团access_token
      store.dispatch('getMtAccess').then(res => {

        localStorage.setItem("getMtAccess", res.data.shopInfo)
        let url = document.location.href;
        let reg = /[^\w](access_token|token_type|expires_in|refresh_token)=?([^&|^#]*)/g;
        url = url.replace(reg, "");
        reg = /&&/g;
        url = url.replace(reg, "");
        reg = /&#/g;
        url = url.replace(reg, "#");
        reg = /\?#/g;
        url = url.replace(reg, "#");
        // url = url.replaceAll(document.domain,"");
        // url = url.replaceAll("http://","");
        // url = url.replaceAll("https://","");
        reg = /\?#/g;
        url = url.replace(reg, "#");
        window.history.replaceState(null, null, url);
        next()
      }).catch((err) => {
        store.dispatch('FedLogOut').then(() => {
          Message.error(err || 'Verification failed, please login again')
          next({ path: '/' })
        })
      })
      
    }
     else if (query.access_token != undefined) {

      localStorage.setItem('access_token', query.access_token);
      localStorage.setItem('token_type', query.token_type);
      localStorage.setItem('expires_in', query.expires_in);
      localStorage.setItem('refresh_token', query.refresh_token)
      // 拉取饿了么店铺信息
      store.dispatch('getShopInfo').then(res => {

        localStorage.setItem("shopInfo", res.data.shopInfo)
        let url = document.location.href;
        let reg = /[^\w](access_token|token_type|expires_in|refresh_token)=?([^&|^#]*)/g;
        url = url.replace(reg, "");
        reg = /&&/g;
        url = url.replace(reg, "");
        reg = /&#/g;
        url = url.replace(reg, "#");
        reg = /\?#/g;
        url = url.replace(reg, "#");
        // url = url.replaceAll(document.domain,"");
        // url = url.replaceAll("http://","");
        // url = url.replaceAll("https://","");
        reg = /\?#/g;
        url = url.replace(reg, "#");
        window.history.replaceState(null, null, url);

        let roleInfo;
        let isAivideAccounts = JSON.parse(localStorage.getItem("adminInfo")).divideAccounts;
        if (isAivideAccounts == 1) {
          roleInfo = admin
        } else {
          roleInfo = Aivide
        }
        router.addRoutes(roleInfo)
        router.options.routes = constantRouterMap.concat(roleInfo)
        next()
      }).catch((err) => {
        store.dispatch('FedLogOut').then(() => {
          Message.error(err || 'Verification failed, please login again')
          next({ path: '/' })
        })
      })

    } else {

      let roleInfo;
      store.dispatch('getInfo').then(res => { // 拉取用户信息
        localStorage.setItem("roles", res.data.adminInfo.type)
        localStorage.setItem("adminInfo", JSON.stringify(res.data.adminInfo))
        // localStorage.setItem("isAuth", isBefore);
        if (res.data.adminInfo.divideAccounts == 1) {
          roleInfo = admin
        } else {
          roleInfo = Aivide
        }


        router.addRoutes(roleInfo)
        router.options.routes = constantRouterMap.concat(roleInfo)
        if (!res.data.authUrl) {

          next()
        } else if (localStorage.getItem("access_token")) {
          next()
        } else {
          window.location.assign(res.data.authUrl);

        }


      }).catch((err) => {
        store.dispatch('FedLogOut').then(() => {
          Message.error(err || 'Verification failed, please login again')
          next({ path: '/' })
        })
      })


    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {

      next()
    } else {

      next(`/login?redirect=${to.path}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
