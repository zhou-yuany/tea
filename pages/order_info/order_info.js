// pages/order_info/order_info.js
// 获取应用实例
const app = getApp()
const api = require("../../utils/api")
const context = require("../../utils/context")
const validate = require("../../utils/validate")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    isChecked: false,
    hidden: false,
    orderId: undefined,
    remarks: '',
    index: 0,
    loading: false,
    orderStatus: {
      10: '待报价',
      20: '已报价',
      30: '已下单',
      40: '待提车',
      45: '已提车',
      50: '待中转',
      60: '已中转',
      70: '运输中',
      80: '已到达',
      90: '已支付'
    },
    order: {},
    models: {},
    series: {},
    details: {},
    addPrice: undefined,
    receiverName: '',
    receiverPhone: ''
      // , receiverCard: ''
      ,
    contractSign: '',
    cars: []
      //, userType: 2  // 临时变量
      ,
    userType: context.getUser().type // 1客户,2为合伙人 
      ,
    partnerType: context.getUser().partnerType // 合伙人类型 0：个人， 1：企业
  },
  bindReceiverName: function (e) {
    this.setData({
      receiverName: e.detail.value
    })
    wx.setStorageSync('receiverName', e.detail.value)
  },
  bindReceiverPhone: function (e) {
    this.setData({
      receiverPhone: e.detail.value
    })
    wx.setStorageSync('receiverPhone', e.detail.value)
  },
  // 设置备注
  bindRemarks: function (e) {
    this.setData({
      remarks: e.detail.value
    })
    wx.setStorageSync('remarks', e.detail.value)
  },
  // bindReceiverCard: function(e){ this.setData({receiverCard: e.detail.value}) },
  bindAddPrice: function (e) {
    this.setData({
      addPrice: e.detail.value
    })
    wx.setStorageSync('addPrice', e.detail.value)
  },

  // 开关
  changeSwitch: function (e) {
    this.setData({
      isChecked: e.detail.value
    })
  },
  bindCarNumber: function (e) {
    let index = e.currentTarget.dataset.index
    this.setData({
      [`cars[${index}].carNumber`]: e.detail.value,
      index: index
    })
    wx.setStorageSync(`cars[${index}].carNumber`, e.detail.value)
  },
  // 确认下单
  goOrder: function () {
    if (this.data.loading) {
      return;
    }
    // if (!validate.submit(this.data, [
    //     // {required: true, key: 'countsCar', name: '托运车辆', type: 'number', min: 1}
    //     , {
    //       required: true,
    //       key: 'receiverName',
    //       name: '收货人',
    //       max: 5
    //     }, {
    //       required: true,
    //       key: 'receiverPhone',
    //       name: '联系电话',
    //       eq: 11
    //     }
    //     // , {required: false, key: 'receiverCard', name: '身份证号码', min: 15, max: 18}
    //   ])) {
        
    //   return;
    // }
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
    // for (let index in this.data.cars) {
    //   let car = this.data.cars[index];
    //   car.carNumber = car.carNumber ? car.carNumber.trim() : ''
    //   if (!validate.submit(car, [{
    //       required: true,
    //       key: 'carNumber',
    //       name: '车架号',
    //       eq: 6
    //     }])) {
    //     return;
    //   }
    // }
    
    let data = {
      id: this.data.orderId,
      receiverName: this.data.receiverName,
      receiverPhone: this.data.receiverPhone,
      remarks: this.data.remarks
        // , receiverCard: this.data.receiverCard
        ,
      orderCarAll: this.data.cars
    }
    if (this.data.isChecked) {
      if (!validate.submit(this.data, [{
          required: true,
          key: 'addPrice',
          name: '加价金额',
          type: 'number'
        }])) {
        return;
      }
      data['addPrice'] = this.data.addPrice
      data['totalPrice'] = Number(this.data.order.totalPrice) + (Number(this.data.addPrice) * this.data.cars.length)
    }

    let this_ = this
    let index = this_.data.index;
    // 签署合同
    if (!this.data.contractSign && this.data.userType <= 2) {
      // 车架号查重 二期取消车架号查重
      // api.post("/order/checkRepeat", data).then((result) => {
      //   if (result.length > 0) {
      //     wx.showToast({
      //       title: `车架号：${result}已下单!`,
      //       icon: 'none'
      //     })
      //   } else {
          wx.navigateTo({
            url: '/pages/contract/contract',
            events: {
              setContractSign: function (contractSign) {
                this_.setData({
                  contractSign
                })
              }
            },
            success: function (res) {
              res.eventChannel.emit('cars', this_.data.order.orderCarAll.map((c, i) => {
                c.carNumber = this_.data.cars[i].carNumber
                return c
              }))
              res.eventChannel.emit('mateData', {
                models: this_.data.models,
                series: this_.data.series,
                details: this_.data.details
              })
            }
          // })
        // }
      })

      return;

    }
    data['contractSign'] = this.data.contractSign;
    this.setData({
      loading: true
    })
    if (this.data.contractSign !== '' && this.data.contractSign !== null) {
      api.post("/order/submit", data).then((result) => {
        // if(this_.data.userType == 2){
        // 合伙人点击后弹窗
        this_.setData({
          hidden: true,
          loading: false
        })
        if (wx.getStorageSync(`cars[${index}].carNumber`)) {
          wx.removeStorageSync(`cars[${index}].carNumber`)
        }
        if (wx.getStorageSync('receiverName')) {
          wx.removeStorageSync('receiverName')
        }
        if (wx.getStorageSync('receiverPhone')) {
          wx.removeStorageSync('receiverPhone')
        }
        if (wx.getStorageSync('remarks')) {
          wx.removeStorageSync('remarks')
        }
        if (wx.getStorageSync('addPrice')) {
          wx.removeStorageSync('addPrice')
        }
        // }
      }).catch(function onRejected(error) {
        this_.setData({
          loading: false
        })
      })
    }

  },
  goSure: function () {
    let indexPage = getCurrentPages()[0];
    indexPage.refresh();
    wx.navigateBack({
      delta: this.data.showType == 'order' ? 1 : 2
    })
  },
  cancelLogin(){
    this.setData({
      login: false
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    options['userType'] = context.getUser().type
    this.setData(options)
    let this_ = this
    api.get("/car/type/select").then(res => {
      let details = {};
      res.forEach(d => {
        details[d.id] = d.name
      })
      this_.setData({
        details
      })
    })
    api.get("/car/series/select").then(res => {
      let series = {};
      res.forEach(d => {
        series[d.id] = d.name
      })
      this_.setData({
        series
      })
    })
    api.get("/car/model/select").then(res => {
      let models = {};
      res.forEach(d => {
        models[d.id] = d.name
      })
      this_.setData({
        models
      })
    })

    api.get(`/order/select/${this.data.orderId}`).then(order => {
      this_.setData({
        order
      })
      this_.setData({
        cars: order.orderCarAll.map(car => {
          return {
            id: car.id,
            carNumber: ''
          }
        })
      })

      if (this.data.order.remarks !== '' && this.data.order.remarks !== null && !options.remarks) {
        this.setData({
          remarks: this.data.order.remarks
        })
      } else {
        this.setData({
          remarks: options.remarks
        })
      }
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
    let index = this.data.index;
    if (wx.getStorageSync(`cars[${index}].carNumber`)) {
      this.setData({
        [`cars[${index}].carNumber`]: wx.getStorageSync(`cars[${index}].carNumber`)
      })
    }
    if (wx.getStorageSync('receiverName')) {
      this.setData({
        receiverName: wx.getStorageSync('receiverName')
      })
    }
    if (wx.getStorageSync('receiverPhone')) {
      this.setData({
        receiverPhone: wx.getStorageSync('receiverPhone')
      })
    }
    if (wx.getStorageSync('remarks')) {
      this.setData({
        remarks: wx.getStorageSync('remarks')
      })
    }
    if (wx.getStorageSync('addPrice')) {
      this.setData({
        addPrice: wx.getStorageSync('addPrice')
      })
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