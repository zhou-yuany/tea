// pages/divideinto/divideinto.js
// 获取应用实例
const app = getApp()
const theme = require("../../utils/themeSystem")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    theme:wx.getStorageSync('systemTheme'),
    date: '全部',
    intoList: [{}],
    windowHeight: 0,
    scrollHeight: 0,
    screenk: 0,
    intok: 0
  },

  bindDatechange:function(e){
    this.setData({
      date: e.detail.value
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        let windowHeight = (res.windowHeight * (750 / res.windowWidth));
        that.setData({
          windowHeight: windowHeight
        })
      }
    });

    setTimeout(function (options) {
      var intok = wx.createSelectorQuery();
      intok.select('.intok').boundingClientRect();
      intok.exec(function (rect) { if (rect[0] === null) return; that.setData({ intok: rect[0].height }) });
      var screenk = wx.createSelectorQuery();
      screenk.select('.screenk').boundingClientRect();
      screenk.exec(function (rect) { if (rect[0] === null) return; that.setData({ screenk: rect[0].height }) });
    }, 500)
    setTimeout(function () {
      var intokHeight = that.data.intok;
      var screenkHeight = that.data.screenk;
      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - intokHeight - screenkHeight - 17
          })
        }
      })
    }, 1000)
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    let that = this;
    if (wx.getStorageSync('selectColor')) {
      theme.setTheme(wx.getStorageSync('selectColor'));
      setTimeout(function () {
        that.setData({
          theme: wx.getStorageSync('systemTheme')
        })
      }, 300)
      
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})