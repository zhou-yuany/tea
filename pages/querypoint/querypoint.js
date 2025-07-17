// pages/querypoint/querypoint.js
// 获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    windowHeight: 0,
    scrollHeight: 0,
    driverk: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    options['markers'] = [{latitude: options.lat, longitude: options.lon}]
    this.setData(options)
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        let windowHeight = (res.windowHeight * (750 / res.windowWidth));
        that.setData({
          windowHeight: windowHeight
        })
      }
    });
    setTimeout(function () {
      var driverk = wx.createSelectorQuery();
      driverk.select('.driverk').boundingClientRect();
      driverk.exec(function (rect) { if (rect[0] === null) return; that.setData({ driverk: rect[0].height }) });
    }, 500)

    setTimeout(function () {
      var driverkHeight = that.data.driverk;
      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - driverkHeight - 22
          })
        }
      })
    }, 1000)
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