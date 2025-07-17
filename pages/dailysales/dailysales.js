// pages/dailysales/dailysales.js
// 获取应用实例
const app = getApp()
var date = new Date();//调用系统时间函数
Page({

  /**
   * 页面的初始数据
   */
  data: {
    starttime: '2021-01-01',
    endtime: '2100-12-31',
    dates: "2021-08-08",
    saleData:[{
      "time":"2021-08-08",
      "allsale":"2,500",
      "num":"100",
      "price":"500"
    },{
      "time":"2021-08-08",
      "allsale":"2,500",
      "num":"100",
      "price":"500"
    },{
      "time":"2021-08-08",
      "allsale":"2,500",
      "num":"100",
      "price":"500"
    },{
      "time":"2021-08-08",
      "allsale":"2,500",
      "num":"100",
      "price":"500"
    },{
      "time":"2021-08-08",
      "allsale":"2,500",
      "num":"100",
      "price":"500"
    },{
      "time":"2021-08-08",
      "allsale":"2,500",
      "num":"100",
      "price":"500"
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
        // console.log('用户点击确定')
       }
      }
    })
  },

  bindDateChange: function (e) {
    console.log(e.detail.value)
   this.setData({
     dates: e.detail.value
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