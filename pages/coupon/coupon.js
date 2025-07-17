// pages/coupon/coupon.js
// 获取应用实例
const app = getApp()
const theme = require("../../utils/themeSystem")
const {
  config
} = require('../../utils/config')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    theme:wx.getStorageSync('systemTheme'),
    currentIndex: 0,
    windowHeight: 0,  //计算高度
    scrollHeight: 0,
    tab: 0,
    couponList:[]
  },

  //swiper切换时会调用
  pagechange: function (e) {
    if ("touch" === e.detail.source) {
      let currentPageIndex = this.data.currentIndex
      currentPageIndex = (currentPageIndex + 1) % 2
      this.setData({
        currentIndex: currentPageIndex
      });
      this.getCouponList();
    }
  },
  titleClick: function (e) {
    this.setData({
      currentIndex: e.currentTarget.dataset.idx
    });
    this.getCouponList();
  },
  goIndex:function(){
    wx.switchTab({
      url: '../index/index',
    })
  },

  // 去使用
  lingqu:function(e){
    wx.switchTab({
      url: '../food/food',
    })
  },

  getCouponList(){
    let that = this;
    wx.request({
      url: `${config}/coupon/getCouponList`,
      method: 'GET',
      data: {
        type: this.data.currentIndex,
        openid: wx.getStorageSync('openid')
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let list = res.data.data.list;
        that.setData({
          couponList: list,
        })
      }
    });
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    var that = this;
    // 高度计算
    wx.getSystemInfo({
      success: function (res) {
        // console.log(res.windowHeight);// 获取可使用窗口高度
        let windowHeight = (res.windowHeight * (750 / res.windowWidth));
        //将高度乘以换算后的该设备的rpx与px的比例
        // console.log(windowHeight) //最后获得转化后得rpx单位的窗口高度
        that.setData({
          windowHeight: windowHeight
        })
      }
    });

    setTimeout(function () {
      var tab = wx.createSelectorQuery();
      tab.select('.title').boundingClientRect();
      tab.exec(function (rect) { if (rect[0] === null) return; that.setData({ tab: rect[0].height }) });
    }, 500)
    setTimeout(function () {
      var tabHeight = that.data.tab;
      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - tabHeight - 21
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
    this.getCouponList();
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