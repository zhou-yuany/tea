// pages/evaluatepage/evaluatepage.js
// 获取应用实例
const app = getApp()
const api = require("../../utils/api")
const context = require("../../utils/context")
const validate = require("../../utils/validate")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    stars: 1,
    remark: '',
    hidden: false,
    orderId: undefined
  },

  img_bind: function(event) {
    var type = event.currentTarget.dataset.type;
    var index = event.currentTarget.dataset.index;
    if(type == 'stars_x'){
      this.setData({stars: index})
    }else{
      this.setData({stars: this.data.stars + index})
    }
  },

  //评价
  goPingjia: function(e){
    this.setData({remark: e.detail.value.remark})
    if(!validate.submit(this.data, [
      {required: true, key: 'remark', name: '评价内容', max: 200}
    ])){
      return;
    }
    let this_ = this
    api.post("/evaluation/insert", {
      orderId: this.data.orderId
      , grade: this.data.stars
      , remark: this.data.remark
    }).then(()=>{
      this_.setData({
        hidden: true
      })
    })
    
  },
  goSure:function(){
    let channel = this.getOpenerEventChannel();
    channel.emit('setEvaluation', {grade: this.data.stars, remark: this.data.remark});
    wx.navigateBack({
      delta: 1
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData(options);
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