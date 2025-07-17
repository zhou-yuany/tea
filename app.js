// app.js
const theme = require("./utils/themeSystem")
const {
  config
} = require('./utils/config')
App({
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })

    //获取设备顶部窗口的高度（不同设备窗口高度不一样，根据这个来设置自定义导航栏的高度）
    wx.getSystemInfo({
      success:res=>{
        this.globalData.height = res.statusBarHeight;
        this.globalData.windowHeight = res.windowHeight;
        this.globalData.windowWidth = res.windowWidth;
      }
    })
  },
  globalData: {
    isIphoneX: false,
    userInfo: null,
    statusBarHeight:wx.getSystemInfoSync()['statusBarHeight'],
    height: 0,
    skin: {
      file: config+"/image"
    },
    pictureInfo: {},

  },
  getTemplateInfo(){
    let that = this;
    wx.request({
      url: `${config}/template/getTemplateInfo`,
      method: 'GET',
      data: {
        color: wx.getStorageSync('selectColor')
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let pictureInfo = res.data.data.pictureInfo;
        wx.setStorageSync('pictureInfo', JSON.stringify(pictureInfo))
        that.globalData.pictureInfo = pictureInfo;
      }
    });
  },
  onShow:function(){
    let  that = this;
    wx.getSystemInfo({
      success:  res=>{
        // console.log('手机信息res'+res.model)
        let modelmes = res.model;
        if (modelmes.search('iPhone X') != -1) {
          that.globalData.isIphoneX = true
        }
      }
    })
    if(wx.getStorageSync('selectColor')){
      theme.setTheme(wx.getStorageSync('selectColor'));
      that.getTemplateInfo(wx.getStorageSync('selectColor'));
    }
     //这里我只是示范，setTheme方法里面具体的值可以根据后端接口返回
  },
  onHide() {
      
},
})
