// pages/my/my.js
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
    pictureInfo: wx.getStorageSync('pictureInfo') ? JSON.parse(wx.getStorageSync('pictureInfo')) : '',
    skin: getApp().globalData.skin,
    theme: wx.getStorageSync('systemTheme'),
    config,
    flag: '',
    flagId: '',
    isShow: false,
    userInfo: {}
  },

  // 我的积分
  goIntegral: function () {
    wx.navigateTo({
      url: '../integral_my/integral_my',
    })
  },

  // 个人资料
  goPersonal: function () {
    wx.navigateTo({
      url: '../personal/personal',
    })
  },

  // 领券中心
  goCoupon: function () {
    wx.navigateTo({
      url: '../coupon/coupon',
    })
  },

  // 联系客服
  freeTell: function () {
    wx.makePhoneCall({
      phoneNumber: '15542639795',
    })
  },

  // 合伙人
  goPartner: function () {
    wx.navigateTo({
      url: '../partner/partner',
    })
  },

  // 经营状况
  goPerformance: function () {
    wx.navigateTo({
      url: '../performance/performance',
    })
  },

  // 流水
  goTurnover: function () {
    const {
      flag,
      flagId
    } = this.data;
    wx.navigateTo({
      url: '../turnover/turnover?flag=' + flag + '&flagId=' + flagId,
    })
  },

  //地址管理
  goAddress() {
    wx.navigateTo({
      url: '../addresslist/addresslist?tab=my',
    })
  },

  // 分账表 
  goLedger: function () {
    wx.navigateTo({
      url: '../ledger/ledger',
    })
  },

  // 合伙人权益
  goEquity: function () {
    wx.navigateTo({
      url: '../equity/equity',
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

  getInfo() {
    let that = this;
    wx.request({
      url: `${config}/users/getInfo`,
      method: 'GET',
      data: {
        openid: wx.getStorageSync('openid')
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let info = res.data.data.info;
        that.setData({
          userInfo: info
        })
      }
    });
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () { 
    let that = this;
    if (wx.getStorageSync('phone')) {

      if (wx.getStorageSync('flag')) {
        let f = wx.getStorageSync('flag') == 'agent' || wx.getStorageSync('flag') == 'shop';
        this.setData({
          flag: wx.getStorageSync('flag'),
          flagId: wx.getStorageSync('flagId'),
          isShow: f
        })
      }
      this.getInfo();
    } else {
      wx.navigateTo({
        url: '../login/login?from=index',
      })
    }

    if (wx.getStorageSync('selectColor')) {
      theme.setTheme(wx.getStorageSync('selectColor'));
      setTimeout(function () {
        that.setData({
          theme: wx.getStorageSync('systemTheme')
        })
      }, 300)
      that.getTemplateInfo(wx.getStorageSync('selectColor'))
    }

  },
  getTemplateInfo(selectColor) {
    let that = this;
    wx.request({
      url: `${config}/template/getTemplateInfo`,
      method: 'GET',
      data: {
        color: selectColor
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let pictureInfo = res.data.data.pictureInfo;
        that.setData({
          pictureInfo
        })
      }
    });
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