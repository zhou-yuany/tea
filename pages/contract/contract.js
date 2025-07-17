// pages/contract/contract.js
// 获取应用实例
const app = getApp()
const api = require("../../utils/api")
const context = require("../../utils/context")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    colors: ['红色','绿色','白色','黄色','银色','灰色','蓝色','黑色','棕色'], 
    hidden: false,
    contractSign: undefined,
    contract: ''
  },

  goSuccess:function(){
    this.setData({
      hidden:true
    })
  },
  goSure:function(){
    this.setData({
      hidden:false
    })
  },
  contractSign:function(){
    // wx.navigateTo({
    //   url: '/pages/canvas/canvas',
    // })
    const eventChannel = this.getOpenerEventChannel()
    wx.navigateTo({
      url: '../write/write',
      events: {
        setContractSign: function(contractSign){
          eventChannel.emit('setContractSign', contractSign);
        }
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let this_ = this;
    const eventChannel = this.getOpenerEventChannel()
    eventChannel.on('cars', function(cars) {
      this_.setData({cars})
    })
    eventChannel.on('mateData', function(mateData) {
      this_.setData(mateData)
    })
    
    this.setData(options)
    api.get("/order/contract").then(res=>{
      this_.setData({contract: res.content});
    });
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