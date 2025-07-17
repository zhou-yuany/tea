// index.js
// 获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    windowHeight: 0,
    scrollHeight: 0,
    headtop: 0,
    select: false,
    selecta: false,
    selectb: false,
    tihuoWay: '当日数据',
    tihuoWaya: '当日数据',
    tihuoWayb: '当日数据',
    hiddendata: true
  },

  // 下拉框
  bindShowMsg() {
    this.setData({
      select:!this.data.select
    })
  },
  mySelect(e) {
    var name = e.currentTarget.dataset.name
    this.setData({
      tihuoWay: name,
      select: false
    })
  },

  bindShowMsga() {
    this.setData({
      selecta:!this.data.selecta
    })
  },
  mySelecta(e) {
    var name = e.currentTarget.dataset.name
    this.setData({
      tihuoWaya: name,
      selecta: false
    })
  },

  bindShowMsgb() {
    this.setData({
      selectb:!this.data.selectb
    })
  },
  mySelectb(e) {
    var name = e.currentTarget.dataset.name
    this.setData({
      tihuoWayb: name,
      selectb: false
    })
  },

  //切换视图
  goList:function(){
    var that = this;
    wx.navigateTo({
      url: '../data/data'
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
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
      var headtop = wx.createSelectorQuery();
      headtop.select('.headtop').boundingClientRect();
      headtop.exec(function (rect) { if (rect[0] === null) return; that.setData({ headtop: rect[0].height }) });
    }, 500)
    setTimeout(function () {
      var headtopHeight = that.data.headtop;
      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - headtopHeight - 24
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