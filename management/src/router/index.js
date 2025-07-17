import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Dashboard',
    hidden: true,
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index')
    }]
  },
  { path: '*', redirect: '/dashboard', hidden: true }
]

export const admin = [
  {
    path: '/shopInfo',
    component: Layout,
    redirect: '/shopInfo/table',
    name: '基本信息',
    meta: { title: '基本信息', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '基本信息',
        component: () => import('@/views/shopInfo/shopInfo'),
        meta: { title: '基本信息', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/commodity',
    component: Layout,
    redirect: '/commodity/table',
    name: '商品管理',
    meta: { title: '商品管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '商品管理',
        component: () => import('@/views/commodity/list'),
        meta: { title: '商品管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/coupon',
    component: Layout,
    redirect: '/coupon/table',
    name: '优惠券',
    meta: { title: '优惠券', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '优惠券',
        component: () => import('@/views/coupon/list'),
        meta: { title: '优惠券', icon: 'table', role: 2 }
      }
    ]
  },
  // {
  //   path: '/formula',
  //   component: Layout,
  //   redirect: '/formula/table',
  //   name: '配方',
  //   meta: { title: '配方', icon: 'example', role: 2 },
  //   children: [
  //     {
  //       path: 'table',
  //       name: '配方',
  //       component: () => import('@/views/formula/list'),
  //       meta: { title: '配方', icon: 'table', role: 2 }
  //     }
  //   ]
  // },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/table',
    name: '订单管理',
    meta: { title: '订单管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '订单管理',
        component: () => import('@/views/order/list'),
        meta: { title: '订单管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/recharge',
    component: Layout,
    redirect: '/recharge/table',
    name: '账单管理',
    meta: { title: '账单管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '账单管理',
        component: () => import('@/views/recharge/list'),
        meta: { title: '账单管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/withdrawalList',
    component: Layout,
    redirect: '/withdrawalList/table',
    name: '提现管理',
    meta: { title: '提现管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '提现管理',
        component: () => import('@/views/withdrawal/withdrawal'),
        meta: { title: '提现管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/interfaceLog',
    component: Layout,
    redirect: '/interfaceLog/table',
    name: '日志管理',
    meta: { title: '日志管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '日志管理',
        component: () => import('@/views/interfaceLog/list'),
        meta: { title: '日志管理', icon: 'table', role: 2 }
      }
    ]
  },
]

export const Aivide = [
  {
    path: '/shopInfo',
    component: Layout,
    redirect: '/shopInfo/table',
    name: '基本信息',
    meta: { title: '基本信息', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '基本信息',
        component: () => import('@/views/shopInfo/shopInfo'),
        meta: { title: '基本信息', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/commodity',
    component: Layout,
    redirect: '/commodity/table',
    name: '商品管理',
    meta: { title: '商品管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '商品管理',
        component: () => import('@/views/commodity/list'),
        meta: { title: '商品管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/coupon',
    component: Layout,
    redirect: '/coupon/table',
    name: '优惠券',
    meta: { title: '优惠券', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '优惠券',
        component: () => import('@/views/coupon/list'),
        meta: { title: '优惠券', icon: 'table', role: 2 }
      }
    ]
  },
  // {
  //   path: '/formula',
  //   component: Layout,
  //   redirect: '/formula/table',
  //   name: '配方',
  //   meta: { title: '配方', icon: 'example', role: 2 },
  //   children: [
  //     {
  //       path: 'table',
  //       name: '配方',
  //       component: () => import('@/views/formula/list'),
  //       meta: { title: '配方', icon: 'table', role: 2 }
  //     }
  //   ]
  // },
  {
    path: '/order',
    component: Layout,
    redirect: '/order/table',
    name: '订单管理',
    meta: { title: '订单管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '订单管理',
        component: () => import('@/views/order/list'),
        meta: { title: '订单管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/recharge',
    component: Layout,
    redirect: '/recharge/table',
    name: '账单管理',
    meta: { title: '账单管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '账单管理',
        component: () => import('@/views/recharge/list'),
        meta: { title: '账单管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/interfaceLog',
    component: Layout,
    redirect: '/interfaceLog/table',
    name: '日志管理',
    meta: { title: '日志管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '日志管理',
        component: () => import('@/views/interfaceLog/list'),
        meta: { title: '日志管理', icon: 'table', role: 2 }
      }
    ]
  },
]

let roleInfo = Aivide;
if(JSON.parse(localStorage.getItem("adminInfo"))){
  let isAivideAccounts = JSON.parse(localStorage.getItem("adminInfo")).divideAccounts;
  if(isAivideAccounts == 1){
    roleInfo = admin
  }else{
    roleInfo = Aivide
  }
}


  

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap.concat(roleInfo)
})
