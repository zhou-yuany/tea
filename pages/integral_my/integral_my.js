// pages/integral_my/integral_my.js
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    theme:wx.getStorageSync('systemTheme'),
    windowHeight: 0,
    scrollHeight: 0,
    head: 0,
    title: 0,
    integralList:[{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    },{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    },{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    },{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    },{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    },{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    },{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    },{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    },{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    },{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    },{
      name: '消费送积分',
      time: '2021-05-13 20:00:03',
      fen: '36'
    }]
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
      var head = wx.createSelectorQuery();
      head.select('.hanga').boundingClientRect();
      head.exec(function (rect) { if (rect[0] === null) return; that.setData({ head: rect[0].height }) });
      var title = wx.createSelectorQuery();
      title.select('.title').boundingClientRect();
      title.exec(function (rect) { if (rect[0] === null) return; that.setData({ title: rect[0].height }) });
    }, 500)
    setTimeout(function () {
      var headHeight = that.data.head;
      var titleHeight = that.data.title;
      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - headHeight - titleHeight - 30
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