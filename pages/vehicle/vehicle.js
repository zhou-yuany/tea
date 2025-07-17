// pages/vehicle/vehicle.js
// 获取应用实例
const app = getApp()
const api = require('../../utils/api')
const validate = require("../../utils/validate")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    cars: [],
    showAddBtn: true,

    typesKey: [1, 2, 3],
    types: ['二手车', '新车', '私家车'],
    colors: ['红色','绿色','白色','黄色','银色','灰色','蓝色','黑色','棕色'],
    type: '请选择品牌型号',
    models: {},
    series: {},
    details: {},
    detail: undefined
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

    // api.get("/car/type/select").then(res=>{
    //   let details = {};
    //   let detail = res[res.length-2];
    //   res.forEach(d=>{
    //     details[d.id] = d.name
    //   })
    //   this_.setData({details, detail})
    //   if(this.data.cars.length == 0){
    //     this.addOption()
    //   }
    // })
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

    let this_ = this
    const eventChannel = this.getOpenerEventChannel()
    eventChannel.on('getData', function(cars) {
      this_.setData({cars})
    })
  },
  goLatestPage: function() {
    const eventChannel = this.getOpenerEventChannel()
    for(var i = 0;i < this.data.cars.length; i++){
      var car = this.data.cars[i];
      if(!validate.submit(car, [
        , {required: true, key: 'brand', name: '车辆品牌'}
        , {required: true, key: 'size', name: '车辆款式'}
        // , {required: true, key: 'detail', name: '车辆具体型号'}
      ])){
        return;
      }
    }
    console.log(this.data.cars)
    eventChannel.emit('setData', this.data.cars);
    wx.navigateBack({
      delta: 1,
    })
  },
  //继续添加
  addOption: function (){
    // let detail = this.data.detail
    // if(!detail){
    //   return;
    // }
    let cars = this.data.cars;
    cars.push({
      type: 0,  // 车辆性质
      color: 0, // 车辆颜色
      count: 1, // 车辆数
      brand: '', // 车辆品牌
      size: '', // 车辆款式
      detail: '', // 车辆具体型号
    })
    this.setData({cars: cars});
    // 选项大于15个后移除添加按钮
    this.setData({showAddBtn: cars.length < 15});
  },
  //删除
  delOption: function (e){
    let cars = this.data.cars;
    cars.splice(e.target.dataset.index, 1);

    this.setData({cars: cars});
    // 选项大于15个后移除添加按钮
    this.setData({showAddBtn: cars.length < 15});
  },

  //车辆性质
  bindTypeChange: function(e) {
    let index = e.currentTarget.dataset.index
    this.setData({
      [`cars[${index}].type`]: e.detail.value
    })
  },
  //车辆颜色
  bindColorChange: function(e) {
    let index = e.currentTarget.dataset.index
    this.setData({
      [`cars[${index}].color`]: e.detail.value
    })
  },

  //品牌型号
  goModel:function(e){
    let this_ = this
    let index = e.currentTarget.dataset.index
    wx.navigateTo({
      url: '/pages/carmodel/carmodel',
      events: {
        setData: function(data){
          this_.setData({
            [`cars[${index}].brand`]: data.brand,
            [`cars[${index}].size`]: data.size,
            // [`cars[${index}].detail`]: data.detail
          })
          api.get("/car/model/select").then(res=>{
            let models = {};
            res.forEach(d=>{
              models[d.id] = d.name
            })
            this_.setData({models})
          })
        },

      }
    })
  },

  /* 加数 */
  addCount: function (e) {
    let index = e.currentTarget.dataset.index
    var count = this.data.cars[index].count;
    // 总数量-1  
    if (count < 10) {
      count++;
    }
    this.setData({
      [`cars[${index}].count`]: count
    })
  },
  /* 减数 */
  delCount: function (e) {
    let index = e.currentTarget.dataset.index
    var count = this.data.cars[index].count;
    // 总数量-1  
    if (count > 1) {
      count--;
    }
    this.setData({
      [`cars[${index}].count`]: count
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
    let this_ = this;
    api.get("/car/model/select").then(res=>{
      let models = {};
      res.forEach(d=>{
        models[d.id] = d.name
      })
      this_.setData({models})
    })
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