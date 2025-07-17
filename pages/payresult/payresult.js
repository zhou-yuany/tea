// pages/payresult/payresult.js
const app = getApp()
const theme = require("../../utils/themeSystem")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    pictureInfo: wx.getStorageSync('pictureInfo') ? JSON.parse(wx.getStorageSync('pictureInfo')) : '',
    skin: getApp().globalData.skin,
    theme:wx.getStorageSync('systemTheme'),
    orderId: '',
    orderNo: '',
    selectCount: 0,
    totalPrice: 0,
    shopId: '',
    goodsList:[],
  },

  goIndex:function(){
    wx.switchTab({
      url: '../food/food',
    })
  },

  goXq:function(){
    if(this.data.orderNo != ''){
      wx.navigateTo({
        url: '../order_info/order_info?orderId=' + this.data.orderId + '&orderNo=' + this.data.orderNo + '&params=' + JSON.stringify(this.data.goodsList) + '&totalPrice=' + this.data.totalPrice + '&selectCount=' + this.data.selectCount + '&shopId=' + this.data.shopId,
      })
    }else{
      wx.navigateTo({
        url: '../order_xq/order_xq?orderId=' + this.data.orderId,
      })
    }
    
  },



  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    if(options.orderId){
      this.setData({
        orderId: options.orderId
      })
    }
    if(options.orderNo){
      this.setData({
        orderNo: options.orderNo,
        goodsList: JSON.parse(options.goodsList),
        totalPrice: options.totalPrice,
        selectCount: options.selectCount,
        shopId: options.shopId
      })
    }
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