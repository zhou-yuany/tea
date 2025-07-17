import request from '@/utils/request'

export default{
  // 获取公众号文章或视频
  getMpList(obj) {
    return request({
      url: `/wechat/getMpList`,
      method: 'post',
      data: obj
    })
  },
    weChatValidate(url ,token) {
        return request({
          url: `/wechat/jssdkConf`,
          method: 'get',
          params: {url, token}
        })
      },
      auth(authCallbackUrl, returnUrl) {
        return request({
          url: '/wechat/auth',
          method: 'get',
          params:{authCallbackUrl, returnUrl}
        })
      },
      getUserInfo(openid, token) {
        return request({
          url: '/wechat/token/userInfo',
          method: 'get',
          params:{openid, token}
        })
      },
      getAuthUserInfo(code) {
        return request({
          url: '/wechat/auth/userInfo',
          method: 'get',
          params:{code}
        })
      },
      creatMenu() {
        return request({
          url: '/wechat/creatMenu',
          method: 'get',
        })
      },
      getMenu(accessToken) {
        return request({
          url: '/wechat/getMenu',
          method: 'get',
          params:{accessToken}
        })
      }
}
// export function weChatValidate() {
//   return request({
//     url: '/wechat',
//     method: 'get',
//   })
// }

