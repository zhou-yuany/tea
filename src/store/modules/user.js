import { login, logout, getInfo, getShopInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    name: '',
    roles: '',
    shopToken: '',
    shopId: '',
    paramlist: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    PARAM_LIST: (state, paramlist) => {
      state.paramlist = paramlist
    },
    // shop_token: (state, shopToken) => {
    //   state.shopToken = shopToken
    // },


  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        login(username, userInfo.password).then(response => {
          const data = response.data
          setToken(data.token)
          commit('SET_TOKEN', data.token)
          localStorage.setItem("token", data.token);
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    getInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token).then(response => {
          const data = response.data.adminInfo
            // commit('SET_ROLES', data.roles)
            commit('SET_ROLES', data.type)
            
            // commit('shop_id', data.shopId)
          // commit('SET_NAME', data.name)
          commit('SET_NAME', data.name)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 获取饿了么/美团店铺信息
    getShopInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        let access_token = localStorage.getItem('access_token');
        let token_type = localStorage.getItem('token_type');
        let expires_in = localStorage.getItem('expires_in');
        let refresh_token = localStorage.getItem('refresh_token');
        getShopInfo(localStorage.getItem('shopId'), access_token, token_type, expires_in, refresh_token).then(response => {
          // const data = response.data.adminInfo
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },



    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          removeToken()
          resolve()
          
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },

    // 配料不足
    batchWarnning({ commit }, params) {
      let paramlist = [];
      if(params != ''){
        paramlist = params.split(",");
      }else{
        paramlist = [];
      }   
      commit('PARAM_LIST', paramlist)
    },
  }
}

export default user
