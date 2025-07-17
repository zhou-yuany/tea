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
    path: '/equipment',
    component: Layout,
    redirect: '/equipment/table',
    name: '设备管理',
    meta: { title: '设备管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '设备管理',
        component: () => import('@/views/equipment/list'),
        meta: { title: '设备管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/brand',
    component: Layout,
    redirect: '/brand/table',
    name: '品牌管理',
    meta: { title: '品牌管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '品牌管理',
        component: () => import('@/views/brand/list'),
        meta: { title: '品牌管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/formula',
    component: Layout,
    redirect: '/formula/table',
    name: '商品管理',
    meta: { title: '商品管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '商品管理',
        component: () => import('@/views/formula/list'),
        meta: { title: '商品管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/shopList',
    component: Layout,
    redirect: '/shopList/table',
    name: '商户管理',
    meta: { title: '商户管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '商户管理',
        component: () => import('@/views/shop/shop'),
        meta: { title: '商户管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/account',
    component: Layout,
    redirect: '/account/table',
    name: '帐号管理',
    meta: { title: '帐号管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '帐号管理',
        component: () => import('@/views/account/account'),
        meta: { title: '帐号管理', icon: 'table', role: 2 }
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
  
  {
    path: '/agentList',
    component: Layout,
    redirect: '/agentList/table',
    name: '代理管理',
    meta: { title: '代理管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '代理管理',
        component: () => import('@/views/agent/agent'),
        meta: { title: '代理管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/takeawayList',
    component: Layout,
    redirect: '/takeawayList/table',
    name: '骑手管理',
    meta: { title: '骑手管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '骑手管理',
        component: () => import('@/views/takeaway/takeaway'),
        meta: { title: '骑手管理', icon: 'table', role: 2 }
      }
    ]
  },
  {
    path: '/flowList',
    component: Layout,
    redirect: '/flowList/table',
    name: '分账管理',
    meta: { title: '分账管理', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '分账管理',
        component: () => import('@/views/flow/flow'),
        meta: { title: '分账管理', icon: 'table', role: 2 }
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
    path: '/batchUse',
    component: Layout,
    redirect: '/batchUse/table',
    name: '物料消耗',
    meta: { title: '物料消耗', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '物料消耗',
        component: () => import('@/views/batchUse/list'),
        meta: { title: '物料消耗', icon: 'table', role: 2 }
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
  {
    path: '/template',
    component: Layout,
    redirect: '/template/table',
    name: '模板维护',
    meta: { title: '模板维护', icon: 'example', role: 2 },
    children: [
      {
        path: 'table',
        name: '模板维护',
        component: () => import('@/views/template/template'),
        meta: { title: '模板维护', icon: 'table', role: 2 }
      },
      {
        path: 'colorInfo/:id',
        name: 'templateInfo',
        hidden: true,
        component: () => import('@/views/template/colorInfo'),
        meta: { title: '详情', icon: 'info' }
      },
      {
        path: 'pictureInfo/:id',
        name: 'pictureInfo',
        hidden: true,
        component: () => import('@/views/template/pictureInfo'),
        meta: { title: '详情', icon: 'info' }
      },

    ]
  },
  // {
  //   path: '/partner',
  //   component: Layout,
  //   redirect: '/partner/table',
  //   name: '合伙人',
  //   meta: { title: '合伙人', icon: 'example', role: 2 },
  //   children: [
  //     {
  //       path: 'table',
  //       name: '合伙人',
  //       component: () => import('@/views/partner/list'),
  //       meta: { title: '合伙人', icon: 'table', role: 2 }
  //     }
  //   ]
  // },

]


let roles = localStorage.getItem("roles")

let roleInfo = []
if(roles == 2){
  roleInfo = admin
}
  

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap.concat(roleInfo)
})
