import router from './router'
import { constantRouterMap, admin } from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth'


// const whiteList = ['/Login'] // 不重定向白名单

router.beforeEach((to, from, next) => {
  NProgress.start()
  let query = {};
    window.location.search.slice(1).split("&").forEach(function (item) {
      let arr = item.split("=");
      query[arr[0]] = arr[1];
    });
  // if (getToken()) {
    
  //   next()
    
  // } else {
    if (query.equipmentNo != undefined) {

      // window.location.assign('https://tea.yinghai-tec.com/portrait');
      localStorage.setItem('equipmentNo', query.equipmentNo);
      // 登录获取信息
      store.dispatch('Login',query.equipmentNo).then(res => {
        
        let url = document.location.href;
        let reg = /[^\w](equipmentNo)=?([^&|^#]*)/g;
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

      })

    } else{
      next()
    }
  
    // if (whiteList.indexOf(to.path) !== -1) {

    //   next()
    // } else {
    //   next(`/Login?redirect=${to.path}`) // 否则全部重定向到登录页
    //   NProgress.done()
    // }
  // }
})

router.afterEach(() => {
  window.scrollTo(0, 0);
  NProgress.done() // 结束Progress
})
