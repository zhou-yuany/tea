// pages/old_phone/old_phone.js
// 获取应用实例
const app = getApp()
const context = require("../../utils/context")
const validate = require("../../utils/validate")
const api = require("../../utils/api")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    phone: '',//手机号
    code: '',//验证码
    iscode: null,//用于存放验证码接口里获取到的code
    hidden: false
  },

  getPhoneValue: function (e) {
    this.setData({
      phone: e.detail.value
    })
  },

  getCodeValue: function (e) {
    this.setData({
      code: e.detail.value
    })
  },

  //获取验证码
  getVerificationCode() {
    var _this = this
    _this.setData({
      disabled: true
    })
    api.get("/wechat/verification/code").then(res=>{
      if(res.id){
        _this.setData({id: res.id});
      }
    })
  },

  //提交表单信息
  save: function () {
    let this_ = this
    if(!validate.submit(this.data, [
      , {required: true, key: 'id', name: 'id', message: '请先发送验证码'}
      , {required: true, key: 'code', name: '验证码'}
    ])){
      return;
    }
    let id = this.data.id;
    let code = this.data.code;
    api.post("/wechat/verification/code", {id, code}).then(res=>{
      if(res.data){
        this_.setData({hidden: true})
      }
    })
  },

  goSure:function(){
    wx.navigateTo({
      url: "../newphone/newphone"
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({phone: context.getUser().phone})
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