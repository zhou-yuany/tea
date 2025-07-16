import router from './router'
import { constantRouterMap, admin } from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth'


const whiteList = ['/Login'] // 不重定向白名单

router.beforeEach((to, from, next) => {
  NProgress.start()
  
  if (getToken()) {
    
    let query = {};
    window.location.search.slice(1).split("&").forEach(function (item) {
      let arr = item.split("=");
      query[arr[0]] = arr[1];
    });
    if (to.path === '/Login') {
      
      next()
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else if (query.access_token != undefined) {
      // window.location.assign('https://tea.yinghai-tec.com/portrait');
      localStorage.setItem('access_token', query.access_token);
      localStorage.setItem('token_type', query.token_type);
      localStorage.setItem('expires_in', query.expires_in);
      localStorage.setItem('refresh_token', query.refresh_token)
      // 拉取饿了么/美团店铺信息
      store.dispatch('getShopInfo').then(res => {
        localStorage.setItem('test', 'test')
        
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
        
        next()

      }).catch((err) => {
        store.dispatch('FedLogOut').then(() => {
          Message.error(err || 'Verification failed, please login again')
          
          next({ path: '/' })
        })
      })

    } else {
 
      store.dispatch('getInfo').then(res => { // 拉取用户信息
        localStorage.setItem("divideAccounts", res.data.divideAccounts);
        localStorage.setItem("username", res.data.adminInfo.username);
        localStorage.setItem("adminId", res.data.adminInfo.id);
        localStorage.setItem("shopId", res.data.shopId);

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
      next(`/Login?redirect=${to.path}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  window.scrollTo(0, 0);
  NProgress.done() // 结束Progress
})
