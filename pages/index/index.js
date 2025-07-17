// index.js
// 获取应用实例
const app = getApp()
const context = require("../../utils/context.js")
const api = require("../../utils/api.js")
const util = require("../../utils/util.js")

Page({
  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    admin: context.getUser().type, //判断角色 1客户 2合伙人
    logo: undefined,
    currentTab: 0,
    login: false,
    loginStep: true,
    userInfo: {},
    showAd: false,
    activity: {},
    sharerId: undefined
  },

  refresh: function(){
    // if(this.data.currentTab == 1){
      this.selectComponent('#orderComponent').refreshOrder();
    // }
    if(this.data.currentTab == 2){
      this.selectComponent('#myComponent').componentInnerFunction();
    }
  },

  // 自定义导航切换
  swichNav: function (e) {
    let that = this;
    if(wx.getStorageSync('customerId')){
      if (this.data.currentTab === e.target.dataset.current) {
        return false;
      } else {
        if(e.target.dataset.current == 1){
          this.selectComponent('#orderComponent').componentInnerFunction();
        }else if(e.target.dataset.current == 2){
          this.selectComponent('#myComponent').componentInnerFunction();
        }else if(e.target.dataset.current == 3){
          this.selectComponent('#accountComponent').componentInnerFunction();
        }
        that.setData({
          currentTab: e.target.dataset.current
        })
      }
    }else{
      this.initLogin()
    }
    
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var sharerId = options.sharerId;
    if(sharerId){
      this.setData({sharerId})
      delete options.sharerId
    }
    // if(Object.keys(options).length == 0){
    //   this.initLogin()
    // }
    this.setData(options)
    let current = options.currentTab
    if(current == 1){
      this.selectComponent('#orderComponent').componentInnerFunction();
    }
    // 显示广告
    let latest = wx.getStorageSync('latest-activity')
    let now = util.now()
    if(!latest){
      this.showActivity();
    }else if(now > latest){
      this.showActivity();
    }
    wx.setStorageSync('latest-activity', now)

  },
  showActivity: function(){
    api.get(`/user/currentActivity`).then(res=>{
      if(res){
        this.setData({showAd: true, activity: res});
      }
    });
  },
  initLogin: function(){
    let this_ = this
    // 登录
    wx.login({
      success: res => {
        if(res.code){
          app.globalData.openId = res.code
          const userId = wx.getStorageSync('customerId')
          if(userId){
            // 跳转首页
            context.setUserId(userId)
            api.get(`/wechat/customer`).then(res=>{
              context.setUser(res);
              this_.setData({admin: res.type, logo: res.logo})
              if(res.phone != wx.getStorageSync('customer-phone')){
                this_.setData({login: true})
                wx.navigateTo({
                  url: '/pages/tips/tips',
                })
              }
            })
          }else{
            this_.setData({login: true})
          }
        }
      }
    })
  },
  getUserInfo (e) {
    this.setData({
      userInfo: e.detail.userInfo,
      loginStep: false
    })
  },
  getPhoneNumber (e) {
    let this_ = this
    api.post(`/wechat/customer/login`, {
      encryptedData: e.detail.encryptedData
      , iv: e.detail.iv
      , code: app.globalData.openId
      // , nickName: this.data.userInfo.nickName
      // , avatarUrl: this.data.userInfo.avatarUrl
      // , gender: this.data.userInfo.gender
      , sharerId: this.data.sharerId
    }).then(res=>{
      context.setUser(res);
      context.setUserId(res.id)
      wx.setStorageSync('customerInfo', res);
      wx.setStorageSync('customer-phone', res.phone);
      wx.setStorageSync('customerId', res.id)
      app.globalData.user = res;
      this_.setData({login: false, admin: res.type, logo: res.logo})
    })
  },

  //关闭广告弹窗
  goClosed:function(){
    this.setData({
      showAd: false
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    // wx.showShareMenu({
    //   withShareTicket: true,
    //   menus: ['shareAppMessage', 'shareTimeline']
    // })
    context.setUser(wx.getStorageSync('customerInfo'));
    context.setUserId(wx.getStorageSync('customerId'));
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  }
})