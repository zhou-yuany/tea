// pages/ranking/ranking.js
// 获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    listData:[{
      "code":"1",
      "text":"鱼香肉丝",
      "price":"1,000",
      "num":"1,000"
    },{
      "code":"2",
      "text":"麻婆豆腐",
      "price":"800",
      "num":"800"
    },{
      "code":"3",
      "text":"宫保鸡丁",
      "price":"600",
      "num":"600"
    },{
      "code":"3",
      "text":"清香小炒",
      "price":"600",
      "num":"600"
    },{
      "code":"3",
      "text":"红烧茄子",
      "price":"600",
      "num":"600"
    },{
      "code":"3",
      "text":"酱香鸡蛋",
      "price":"600",
      "num":"600"
    }]
  },

  //横屏提示
  goHorizontal:function(){
    wx.showModal({
      title: '提示',
      content: '请打开手机“自动旋转”功能即可横屏查看',
      showCancel: false,
      success: function(res) {
       if (res.confirm) {
         //console.log('用户点击确定')
       }
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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