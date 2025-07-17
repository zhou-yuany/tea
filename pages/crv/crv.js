// pages/crv/crv.js
// 获取应用实例
const app = getApp()
const context = require("../../utils/context")
const chooseLocation = requirePlugin('chooseLocation');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    isChecked: false,
    needJc: 2,
    detail: '',
    lat: '',
    lon: '',
    customItem: [],
    start: '辽宁省大连市中山区',
    mapCtx: {},
    markers: []
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    let this_ = this;
    this.mapCtx = wx.createMapContext('mapId')
    const location = chooseLocation.getLocation();
    if(location != null){
      this.setData({
        detail: location.name,
        lon: location.longitude,
        lat: location.latitude,
        markers: [{latitude: location.latitude, longitude: location.longitude}]
      })
    }
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    chooseLocation.setLocation(null);
  },
  goLocation: function(){
    const key = context.data.locationKey
    const referer = context.data.applicationName
    const location = JSON.stringify({
      latitude: this.data.lat,
      longitude: this.data.lon
    });
    wx.navigateTo({
      url: `plugin://chooseLocation/index?key=${key}&referer=${referer}&location=${location}`
    }); 
  },

  // 开关
  changeSwitch: function (e){
    this.setData({isChecked: e.detail.value})
  },

  //省市联动
  bindRegionChange: function (e) {
    var that = this;    
    that.setData({
      clas: ''
    })
    this.setData({
      detailed: e.detail.value[0] + " " + e.detail.value[1] + " " + e.detail.value[2],
      region: e.detail.value
    })
    this.setData({
      "AddSite.area": e.detail.value[0] + " " + e.detail.value[1] + " " + e.detail.value[2]
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let this_ = this
    let data = {
      needJc: options.needJc
      , isChecked: options.needJc == 1
      , start: options.start
      , detail: options.detail
    }
    if(options.lat != 0){
      data['lat'] = options.lat
      data['lon'] = options.lon
      data['markers'] = [{latitude: options.lat, longitude: options.lon}]
      this.setData(data)
    }else{
      let location = wx.getStorageSync('location')
      if(location){
        data['lat'] = location.lat
        data['lon'] = location.lon
        data['markers'] = [{latitude: location.lat, longitude: location.lon}]
        data['detail'] = location.detail
        this_.setData(data)
      }else{
        wx.getLocation({
          type: 'wgs84',
          success (res) {
            data['lat'] = res.latitude
            data['lon'] = res.longitude
            data['markers'] = [{latitude: res.latitude, longitude: res.longitude}]
            this_.setData(data)
          }
        })
      }
    }
  },
  goLatestPage: function() {
    if(this.data.detail == '' && this.data.isChecked){
      wx.showToast({
        title: '请填写详细地址',
        icon: 'error',
        duration: 1000
      })
      return;
    }
    let eventChannel = this.getOpenerEventChannel()
    let location = {
      detail: this.data.detail,
      lon: this.data.lon,
      lat: this.data.lat,
      needJc: this.data.isChecked ? 1 : 2
    }
    eventChannel.emit("setData", location)
    wx.setStorageSync('location', location)
    wx.navigateBack({
      delta: 1,
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },


  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

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