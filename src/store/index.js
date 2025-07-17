import Vue from 'vue'
import Vuex from 'vuex'
import _ from 'lodash'
import api from '@/api/api'
import localStorageUtil from '@/utils/LocalStorageUtil'
// import permission from './modules/permission'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    unionId: '',
    openid: '',
    accessToken: '',
    wechatUserInfo: {}
  },
  getters: {
    unionId: state => {
      return state.unionId || localStorageUtil.get('unionId')
    },
    openid: state => {
      return state.openid || localStorageUtil.get('openid')
    },
    accessToken: state => {
      return state.accessToken || localStorageUtil.get('accessToken')
    },
    wechatUserInfo: state => {
      return state.wechatUserInfo || localStorageUtil.get('wechatUserInfo')
    }
  },
  mutations: {
    saveWechatUserInfo: (state, wechatUser) => {
      state.wechatUserInfo = wechatUser
      // todo 保存到storage，设置一定日期，定期更新
      state.unionId = wechatUser.unionId
      state.openid = wechatUser.openid
      state.accessToken = wechatUser.accessToken
      localStorageUtil.set('unionId', wechatUser.unionId)
      localStorageUtil.set('openid', wechatUser.openid)
      localStorageUtil.set('accessToken', wechatUser.accessToken)
      // 保存userInfo，设置有效时间，默认30天
      localStorageUtil.set('wechatUserInfo', wechatUser, 30)
    }
  },
  actions: {
    // 静默授权获取用户信息
    async getUserInfo({ commit, getters }) {
      const openid = getters.openid
      const token = getters.accessToken
      if (!_.isEmpty(openid) && !_.isEmpty(token)) {
        // 存在openid和accessToken，已经授过权
        // 判断accessToken是否过期，过期刷新token，获取用户信息
        const res = await api.getUserInfo(openid, token)
        if (res.data.data.wechatUser) {
          // todo 判断res.data是否有误
          commit('saveWechatUserInfo', res.data.data.wechatUser)
        }
      }
    },
    // 初次授权获取用户信息
    async getAuthUserInfo({ commit }, { code, state }) {
      if (!_.isEmpty(code) && !_.isEmpty(state)) {
        const res = await api.getAuthUserInfo(code, state)
        if (res.data.data.wechatUser) {
          commit('saveWechatUserInfo', res.data.data.wechatUser)
        }
      }
    }
  }

})

export default store
