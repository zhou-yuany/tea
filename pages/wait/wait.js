// pages/wait/wait.js
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
    , order: {}
    , remarks: ''
    , remarksFlag: true
    , linePrice: -1
    , djPrice: -1
    , models: {}
    , series: {}
    , details: {}
    //, userType: 2  // 临时变量
    , userType: context.getUser().type // 1客户,2为合伙人 
  },

  // 设置备注
  bindRemarks: function (e){
    this.setData({
      remarks: e.detail.value
    })
  
},
  //下单
  goInfo: function(){
    //空格或者全部是空格
    var regu = "^[ ]+$"
    var re = new RegExp(regu)
    if (re.test(this.data.remarks)) {
      wx.showToast({
        title: '请输入有效的备注',
        icon: 'none',
        duration: 2000
      })
      return
    }
    wx.navigateTo({
      url: '/pages/order_info/order_info?orderId='+this.data.order.id+'&remarks='+this.data.remarks,
    })
  },

  //举报
  goReport:function(){
    let order = this.data.order;
    let start = order.startProv+order.startCity+order.statrtArea;
    let dest = order.destProv+order.destCity+order.destArea;
    let countsCar = order.countsCar;
    let totalPrice = order.totalPrice;
    wx.navigateTo({
      url: `/pages/report/report?orderId=${order.id}&start=${start}&dest=${dest}&countsCar=${countsCar}&totalPrice=${totalPrice}`,
    })
  },

  // 编辑备注
  editRemarks(){
    this.setData({
      remarksFlag: false
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
      let remarks = order.remarks;
      this_.setData({order, linePrice, djPrice, remarks})
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