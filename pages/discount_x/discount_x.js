// pages/discount_x/discount_x.js
// 获取应用实例
const app = getApp();
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
    discountList: [{
      dis_price: '10',
      price: '20',
      rules: '适用于全品类商品',
      content: 'VIP专属优惠券',
      endtime: '2022.12.31 23:59',
      checked: true
    },{
      dis_price: '10',
      price: '20',
      rules: '适用于全品类商品',
      content: 'VIP专属优惠券',
      endtime: '2022.12.31 23:59',
      checked: false
    }],
    selectValue: 0,
    couponId: null,
    couponList: [],
    shopId: null,
    totalPrice: 0,
    windowHeight: 0,
    scrollHeight: 0,
    optional: 0,
    footerk: 0
  },
  selectData(e){
    let couponList = this.data.couponList;
    if(e.detail.value == 1){
      let selectValue = 0;
      couponList.forEach(item => {
        item.checked = item.id == e.currentTarget.dataset.id ? true : false;
        if(item.id == e.currentTarget.dataset.id){
          selectValue = item.parValue
        };
      });
      this.setData({
        couponList: couponList,
        selectValue: selectValue,
        couponId: e.currentTarget.dataset.id
      });
    }
   
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    if(options.shopId && options.totalPrice){
      this.setData({
        shopId: options.shopId,
        totalPrice: options.totalPrice
      });
        wx.request({
          url: `${config}/coupon/getIsCoupons`,
          method: 'GET',
          data: {
            shopId: options.shopId,
            openid: wx.getStorageSync('openid')
          },
          header: {
            'Content-Type': 'application/json'
          },
          success(res) {
            let list = res.data.data.list;
            let newList = list.filter(item => item.limits <= options.totalPrice);
            newList.forEach(item => {
              
              if(options.couponId && item.id == options.couponId){
                item.checked = true;
              }else{
                item.checked = false;
              }      
              
            });
              that.setData({
                couponList: newList
              })
            
          }
        });
    }
    
    wx.getSystemInfo({
      success: function (res) {
        let windowHeight = (res.windowHeight * (750 / res.windowWidth));
        that.setData({
          windowHeight: windowHeight
        })
      }
    });

    setTimeout(function () {
      var footerk = wx.createSelectorQuery();
      footerk.select('.footerk').boundingClientRect();
      footerk.exec(function (rect) { if (rect[0] === null) return; that.setData({ footerk: rect[0].height }) });
    }, 500)
    setTimeout(function () {
      var footerkHeight = that.data.footerk;

      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - footerkHeight - 20
          })
        }
      })
    }, 1000)
  },

  goSure:function(){
    console.log(this.data.couponId)
    let pages = getCurrentPages();
    let prevPage = pages[pages.length - 2];
    prevPage.setData({
      couponId: this.data.couponId,
      couponValue: this.data.selectValue,
      isCoupon: 0
    });
    wx.navigateBack({
      delta: -1,
    })
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