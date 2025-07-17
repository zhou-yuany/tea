import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []
//循环每一个路由
  routes.forEach(route => {
    const tmp = { ...route }
    //判断是否有权限
    if (hasPermission(roles, tmp)) {
      //判断是否有下限
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    //把过滤出来有权限的路由议添加到不需要权限的路由去
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  async generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      //存的是有权限的路由，是一个数组
      let accessedRoutes

      //这里可以写自己的拦截逻辑
      if (roles.includes(0)) {
        // admin角色的话左右路由都可以看
        // accessedRoutes = asyncRoutes || []
        //通过所属的角色去过滤路由，生成新的路由表
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      } else if(roles.includes(1)){
        //通过所属的角色去过滤路由，生成新的路由表
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      }else if(roles.includes(2)) {
        
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      }
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

