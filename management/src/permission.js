import router from './router'
import { constantRouterMap, admin } from './router'
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
    if (to.path === '/login') {
      next()
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {
      let roleInfo;
      store.dispatch('getInfo').then(res => { // 拉取用户信息
        localStorage.setItem("roles", res.data.adminInfo.type)
        localStorage.setItem("adminInfo", JSON.stringify(res.data.adminInfo))
      // localStorage.setItem("isAuth", isBefore);
        roleInfo = admin
      

      router.addRoutes(roleInfo)
      router.options.routes = constantRouterMap.concat(roleInfo)
      next()


        next()
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
