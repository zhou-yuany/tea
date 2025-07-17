// pages/report/report.js
// 获取应用实例
const app = getApp()
const api = require("../../utils/api")
const context = require("../../utils/context")
let animationShowHeight = 44;
let animationShowHeights = -44;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    customItem: [],
    detailed: '辽宁省大连市中山区',
    customItems: [],
    detaileds: '上海市上海市市辖区',
    animation: '',
    animations: '',
    changa: true,
    changb: false,
    cartype: '请选择',
    pics:[],
    isShow: true,
    hidden: false,
    orderId: undefined
    , start: ''
    , dest: ''
    , countsCar: ''
    , totalPrice: ''
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
  bindRegionChanges: function (e) {
    var that = this;    
    that.setData({
      class: ''
    })
    this.setData({
      detaileds: e.detail.value[0] + " " + e.detail.value[1] + " " + e.detail.value[2],
      region: e.detail.value
    })
    this.setData({
      "AddSite.area": e.detail.value[0] + " " + e.detail.value[1] + " " + e.detail.value[2]
    })
  },

  //始发地和目的地换位
  goChange: function () {
    var animation = wx.createAnimation({
      transformOrigin: "50% 50%",
      duration: 1000,
      timingFunction: "ease",
      delay: 0
    })
    this.animation = animation
    animation.translateY(animationShowHeight).step()

    var animations = wx.createAnimation({
      transformOrigin: "50% 50%",
      duration: 1000,
      timingFunction: "ease",
      delay: 0
    })
    this.animations = animations
    animations.translateY(animationShowHeights).step()

    this.setData({
      animation: animation.export(),
      animations: animations.export()
    })

    this.setData({
      changa: false,
      changb: true
    })
  },
  goChanges: function () {
    var animation = wx.createAnimation({
      transformOrigin: "50% 50%",
      duration: 1000,
      timingFunction: "ease",
      delay: 0
    })
    this.animation = animation
    animation.translateY(0).step()

    var animations = wx.createAnimation({
      transformOrigin: "50% 50%",
      duration: 1000,
      timingFunction: "ease",
      delay: 0
    })
    this.animations = animations
    animations.translateY(0).step()

    this.setData({
      animation: animation.export(),
      animations: animations.export()
    })

    this.setData({
      changa: true,
      changb: false
    })
  },

  //选择托运车辆
  goChoosecar: function(){
    wx.navigateTo({
      url: '/pages/vehicle/vehicle'
    })
  },

  /**上传图片 */
  uploadImage:function(){
    let that=this;
    let pics = that.data.pics;
    wx.chooseImage({
      count:3 - pics.length,
      sizeType: ['original', 'compressed'], 
      sourceType: ['album', 'camera'], 
      success: function(res) {
        let imgSrc = res.tempFilePaths;
        imgSrc.forEach(img=>pics.push(img))
        // pics.push(imgSrc);
        if (pics.length >= 3){
          that.setData({
            isShow: false
          }) 
        }
        that.setData({
          pics: pics
        })
      },
    }) 
  },
  
  /**删除图片 */
  deleteImg:function(e){
    let that=this;
    let index = e.currentTarget.dataset.index;
    this.data.pics.splice(index, 1)
    that.setData({
      pics: this.data.pics,
      isShow: true
    })
  },

  /**提交 */
  goReport:function(){
    let pics = this.data.pics
    if(pics.length == 0){
      api.post("/accusation/insert", {
        orderId: this.data.orderId
      }).then((result) => {
        wx.showToast({
            title: '举报成功',
            icon: 'none',
            duration: 1000
        })
        setTimeout(()=>{
          wx.navigateBack({
            delta: 2
          })
        }, 1000)
      })
    }else{
      wx.showLoading({ title: '加载中...', duration: 6000})
      let promises = pics.map(pic=>{
        return new Promise((resolve, reject) => {
          wx.uploadFile({
            filePath: pic,
            name: 'image',
            url: api.urlPrefix+"/wechat/upload",
            header: {'user-id': context.getUserId()},
            success: res => {
                let statusCode = res.statusCode
                if(statusCode >=200 && statusCode < 400){
                    resolve(JSON.parse(res.data))
                }else{
                    reject(res)
                }
            },
            fail: res => {
                reject(res)
            },
          })
        })
      })
      Promise.all(promises).then((result) => {
        wx.hideLoading()
        api.post("/accusation/insert", {
          orderId: this.data.orderId
          , images: result
        }).then((result) => {
          wx.showToast({
              title: '举报成功',
              icon: 'none',
              duration: 3000
          })
          wx.navigateBack({
            delta: 1,
          })
        })
      }).catch((error) => {
        wx.hideLoading()
        wx.showToast({
            title: '网络异常，请检查网络状态',
            icon: 'none',
            duration: 3000
        })
      })
    }
  },

  goSure:function(){
    this.setData({
      hidden: false
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      orderId: options.orderId
      , start: options.start
      , dest: options.dest
      , countsCar: options.countsCar
      , totalPrice: options.totalPrice
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