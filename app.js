// app.js
const api = require("/utils/api.js")

App({
  onLaunch() {
    // 展示本地存储能力
    // const logs = wx.getStorageSync('logs') || []
    // logs.unshift(Date.now())
    // wx.setStorageSync('logs', logs)
    
  },
  globalData: {
    userInfo: null,
    user: {},
    openId: null,
    urlPrefix: api.urlPrefix
  }
})
