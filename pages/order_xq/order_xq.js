// pages/order_xq/order_xq.js
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
    , orderStatus: {
      10: '待报价'
      , 20: '已报价'
      , 30: '已下单'
      , 40: '待提车'
      , 45: '已提车'
      , 50: '待中转'
      , 60: '已中转'
      , 70: '运输中'
      , 80: '已到达'
      , 90: '已支付'
    }
    , userType: undefined
    , colors: ['红色','绿色','白色','黄色','银色','灰色','蓝色','黑色','棕色']
    , order: {}
    , linePrice: -1
    , djPrice: -1
    , models: {}
    , series: {}
    , details: {}
  },

  // 车辆信息
  goCarxq:function(e){
    let carId = e.currentTarget.dataset.id;
    let index = e.currentTarget.dataset.index;
    let car = this.data.order.orderCarAll.filter(c=>c.id == carId)[0]
    if(!car){
      return;
    }
    let a1 = this.data.models[car.brand] + this.data.series[car.size]
    let a2 = this.data.details[car.detail] || ''
    let a3 = car.carNumber
    let a4 = this.data.colors[car.color]
    wx.navigateTo({
      url: `/pages/car_information/car_information?index=${index}&a1=${a1}&a2=${a2}&a3=${a3}&a4=${a4}&carId=${carId}&orderId=${car.orderId}`
    })
  },

  //查看位置
  goQuery:function(){
    let lon = this.data.order.lon
    let lat = this.data.order.lat
    if(lon && lat){
      let name = this.data.order.cyr.name;
      let phone = this.data.order.cyr.phone;
      wx.navigateTo({
        url: `../querypoint/querypoint?lon=${lon}&lat=${lat}&name=${name}&phone=${phone}`,
      })
    }else{
      wx.showToast({
          title: '未发车, 暂无位置',
          icon: 'none',
          duration: 3000
      })
    }
  },

  //评价承运人
  goPingjia: function(){
    let this_ = this
    wx.navigateTo({
      url: '../evaluatepage/evaluatepage?orderId='+this.data.order.id,
      events: {
        setEvaluation: function(data) {
          this_.setData({
            'order.evaluation': data
          })
        }
      }
    })
  },

  //查看合同
  goLook:function(){
    let this_ = this
    wx.navigateTo({
      url: '../contract/contract?contractSign='+this.data.order.contractSign,
      success: function(res) {
        res.eventChannel.emit('cars', this_.data.order.orderCarAll)
        res.eventChannel.emit('mateData', {models: this_.data.models, series: this_.data.series
          , details: this_.data.details})
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let this_ = this
    this.setData({userType: context.getUser().type})

    api.get("/car/type/select").then(res=>{
      let details = {};
      let detail = {};
      res.forEach(d=>{
        details[d.id] = d.name
        detail = d
      })
      this_.setData({details, detail})
    })
    api.get("/car/series/select").then(res=>{
      let series = {};
      res.forEach(d=>{
        series[d.id] = d.name
      })
      this_.setData({series})
    })
    api.get("/car/model/select").then(res=>{
      let models = {};
      res.forEach(d=>{
        models[d.id] = d.name
      })
      this_.setData({models})
    })

    api.get(`/order/select/${options.orderId}`).then(order=>{
      // 计算干线价格
      let linePrice = -1
      if(order.linePrice && order.orderState > 10){
        linePrice = order.linePrice / order.countsCar
      }
      // 计算提车价格
      let djPrice = -1
      if(order.djPrice && order.orderState > 10){
        djPrice = order.djPrice / order.countsCar
      }
      this_.setData({order, linePrice, djPrice})
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