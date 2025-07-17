// pages/orderlist/orderlist.js
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
    theme: wx.getStorageSync('systemTheme'),
    config: config,
    openid: '',
    windowHeight: 0,
    scrollHeight: 0,
    tab: 0,
    currentTab: 0,
    nowList: [],
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
      var tab = wx.createSelectorQuery();
      tab.select('.swiper-tab').boundingClientRect();
      tab.exec(function (rect) {
        if (rect[0] === null) return;
        that.setData({
          tab: rect[0].height
        })
      });
    }, 500)
    setTimeout(function () {
      var tabHeight = that.data.tab;
      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - tabHeight - 26
          })
        }
      })
    }, 1000)
  },

  //tab切换逻辑
  swichNav: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      });
      this.getList(parseInt(e.target.dataset.current))
    }
  },

  bindChange: function (e) {
    var that = this;
    that.setData({
      currentTab: e.detail.current
    });
    this.getList(parseInt(e.detail.current))
  },

  // 订单详情
  goXq(e) {
    wx.navigateTo({
      url: '../order_xq/order_xq?orderId=' + e.currentTarget.dataset.id,
    })
  },
  getInfo(orderId) {
    let that = this;
    wx.request({
      url: `${config}/orders/getOrdersInfo`,
      method: 'GET',
      data: {
        orderId: orderId
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        wx.setStorageSync('takeType', res.data.data.info.orderType);
        wx.setStorageSync('orderInfo', JSON.stringify(res.data.data.info))
        wx.switchTab({
          url: '../food/food',
        })

      }
    });
  },
  // 再来一单
  goAgain(e) {
    this.getInfo(e.currentTarget.dataset.id)
    wx.removeStorageSync('temporary');
    // wx.navigateTo({
    //   url: '../order_info/order_info',
    // })
  },

  getList(currentTab) {
    let that = this;
    let openid = this.data.openid;
    wx.request({
      url: `${config}/orders/getOrdersList`,
      method: 'GET',
      data: {
        openid: openid,
        type: parseInt(currentTab)
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        that.setData({
          nowList: res.data.data.list
        })

      }
    });
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
    if (wx.getStorageSync('phone')) {
      this.setData({
        openid: wx.getStorageSync('openid')
      });
      this.getList(this.data.currentTab)
    } else {
      wx.navigateTo({
        url: '../login/login?from=food',
      })
    }

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