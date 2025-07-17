// pages/car_information/car_information.js
// 获取应用实例
const app = getApp()
const api = require("../../utils/api")
const context = require("../../utils/context")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix
    , a1: ''
    , a2: ''
    , a3: ''
    , a4: ''
    , carErrors: []
    , imageTypeName: {
      'DJ_ERROR': '代驾'
      , 'START_CJ_ERROR': '始发地城市经理'
      , 'ZZ_CJ_ERROR': '中转城市经理'
      , 'DEST_CJ_ERROR': '目的地城市经理'
      , 'CYR_ERROR': '承运人'
      , 'ZZ_CYR_ERROR': '中转承运人'
    }
  },

  //代驾放大图片
  previewImage: function(e){
    let image = e.currentTarget.dataset.image;
    let index = e.currentTarget.dataset.index;
    wx.previewImage({
      //当前显示下表
      current: `${this.data.urlPrefix}/wechat/download?path=${image}`,
      //数据源
      urls: this.data.carErrors[index].carImages.map(imagePath=>`${this.data.urlPrefix}/wechat/download?path=${imagePath}`)
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData(options)
    wx.setNavigationBarTitle({
      title: '车辆信息·'+options.index,
    })
    let this_ = this
    api.get(`/order/car/error/image/${options.orderId}/${options.carId}`).then(carErrors=>{
      this_.setData({carErrors: carErrors.filter(e=>e.userType != 'INFO')})
    });
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