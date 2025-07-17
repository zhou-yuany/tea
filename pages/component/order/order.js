// 获取应用实例
const app = getApp()
const api = require("../../../utils/api")
const context = require("../../../utils/context")

Component({
  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    orderType: 'ALL', // ALL:全部,WAIT_OFFER:询价中,TRANSFER_ING:运输中,ALREADY_PAYMENT:已完成
    swiperCurrent: {
      'ALL': 0,
      'WAIT_OFFER': 1,
      'TRANSFER_ING': 2,
      'ALREADY_PAYMENT': 3
    },
    windowHeight: 0,
    scrollHeight: 0,
    tab: 0,
    currentTab: 0,
    orderList: [{
        type: 0,
        ordernum: 'A123456001',
        startaddress: '辽宁省大连市中山区',
        endaddress: '上海市上海市辖区',
        number: '2',
        time: '2021-08-08'
      },
      {
        type: 1,
        ordernum: 'A123456001',
        startaddress: '辽宁省大连市中山区',
        endaddress: '上海市上海市辖区',
        number: '2',
        time: '2021-08-08'
      },
      {
        type: 2,
        ordernum: 'A123456001',
        startaddress: '辽宁省大连市中山区',
        endaddress: '上海市上海市辖区',
        number: '2',
        time: '2021-08-08'
      },
      {
        type: 3,
        ordernum: 'A123456001',
        startaddress: '辽宁省大连市中山区',
        endaddress: '上海市上海市辖区',
        number: '2',
        time: '2021-08-08'
      },
      {
        type: 4,
        ordernum: 'A123456001',
        startaddress: '辽宁省大连市中山区',
        endaddress: '上海市上海市辖区',
        number: '2',
        time: '2021-08-08'
      }
    ],
    orderAll: [],
    orderWaitOffer: [],
    orderTransferIng: [],
    orderAlreadyPayment: [],
    triggered: false,
    init: false,
    userType: undefined,
    orderDataKey: {
      'ALL': 'orderAll',
      'WAIT_OFFER': 'orderWaitOffer',
      'TRANSFER_ING': 'orderTransferIng',
      'ALREADY_PAYMENT': 'orderAlreadyPayment'
    },
    getData: function (call, orderType, offset) {
      let this_ = this
      let requestData = {
        pageSize: 4
      }
      if (offset) {
        requestData['offset'] = offset;
      }
      api.get(`/order/customer/selectPage/${orderType}`, requestData, true).then(list => {
        let data = {}
        data[this_.orderDataKey[orderType]] = list
        call(data)
      })
    }
  },

  ready: function () {},
  // 查询列表


  /**
   * 组件的方法列表
   */
  methods: {

    // 取消订单
    cancelOrders(e) {
      let id = e.currentTarget.dataset.id;
      let this_ = this
      wx.showModal({
        title: '提示',
        content: '确认取消此订单吗',
        success (res) {
          if (res.confirm) {
            api.post(`/order/cancel/${id}`,).then(res => {
              if(res == 1){
                wx.showToast({
                  title: '已取消',
                  icon: 'success',
                  duration: 2000,
            success: function () {
              setTimeout(function () {
                Object.keys(this_.data.orderDataKey).forEach(orderType => {
                  this_.data.getData(data => {
                    data['triggered'] = false
                    this_.setData(data);
                  }, orderType)
                })
              }, 2000) //延迟时间
            }
                })
              }
            })
          } else if (res.cancel) {
            console.log('用户点击取消')
          }
        }
      })
      
    },

    refreshOrder: function () {
      let this_ = this
      Object.keys(this.data.orderDataKey).forEach(orderType => {
        this_.data.getData(data => {
          data['triggered'] = false
          this_.setData(data);
        }, orderType)
      })
    },

    bindDownLoad: function () {
      let this_ = this
      let dataKey = this.data.orderDataKey[this.data.orderType];
      let lastData = this.data[dataKey];
      if (lastData.length <= 0) {
        return;
      }
      this.data.getData(data => {
        data[dataKey].forEach(d => lastData.push(d))
        data[dataKey] = lastData
        this_.setData(data);
      }, this.data.orderType, lastData.length)
    },

    componentInnerFunction: function () {
      if (this.data.init) {
        return;
      }
      this.setData({
        init: true,
        userType: context.getUser().type
      })
      let this_ = this
      this.data.getData(data => this_.setData(data), 'ALL')
      this.data.getData(data => this_.setData(data), 'WAIT_OFFER')
      this.data.getData(data => this_.setData(data), 'TRANSFER_ING')
      this.data.getData(data => this_.setData(data), 'ALREADY_PAYMENT')

      var that = this;
      wx.getSystemInfo({
        success: function (res) {
          let windowHeight = (res.windowHeight * (750 / res.windowWidth));
          that.setData({
            windowHeight: windowHeight
          })
        }
      });
      setTimeout(function () {
        var tab = wx.createSelectorQuery();
        tab.select('.swiper-tab').boundingClientRect();
        tab.exec(function (rect) {
          if (rect[0] === null) return;
          that.setData({
            tab: rect[0].height
          })
        });
      }, 500)

      setTimeout(function () {
        var tabHeight = that.data.tab;
        wx.getSystemInfo({
          success: function (res) {
            let scrollHeight = res.windowHeight;
            that.setData({
              scrollHeight: scrollHeight - tabHeight - 75 - 46
            })
          }
        })
      }, 1000)
    },

    //  tab切换逻辑
    swichNav: function (e) {
      var that = this;
      if (this.data.orderType === e.target.dataset.current) {
        return false;
      } else {
        that.setData({
          orderType: e.target.dataset.current
        })
      }
    },
    bindChange: function (e) {
      this.setData({
        orderType: Object.keys(this.data.swiperCurrent)[e.detail.current]
      });
    },

    //跳转详情页
    goXq: function (e) {
      let id = e.currentTarget.dataset.id;
      let order = this.data.orderAll.filter(o => o.id == id)[0] ||
        this.data.orderWaitOffer.filter(o => o.id == id)[0] ||
        this.data.orderTransferIng.filter(o => o.id == id)[0] ||
        this.data.orderAlreadyPayment.filter(o => o.id == id)[0];
      if (!order) {
        return;
      }
      if (order.orderState <= 20) {
        wx.navigateTo({
          url: '../wait/wait?orderId=' + id,
        })
      } else {
        wx.navigateTo({
          url: '../order_xq/order_xq?orderId=' + id,
        })
      }
    },

    //立即下单
    goPay: function (e) {
      let id = e.currentTarget.dataset.id;
      // let order = this.data.orderAll.filter(o=>o.id == id)[0]
      // if(!order){
      //   return;
      // }
      wx.navigateTo({
        url: '../order_info/order_info?orderId=' + id,
      })
    },

    //查看位置
    goQuery: function (e) {
      let index = e.currentTarget.dataset.index;
      let order = this.data[this.data.orderDataKey[this.data.orderType]][index];
      let lon = order.lon
      let lat = order.lat
      let name = order.cyr.name;
      let phone = order.cyr.phone;
      wx.navigateTo({
        url: `../querypoint/querypoint?lon=${lon}&lat=${lat}&name=${name}&phone=${phone}`,
      })
    },

    //评价承运人
    goPingjia: function (e) {
      let orderId = e.currentTarget.dataset.id;
      let list = this.data[this.data.orderDataKey[this.data.orderType]];
      let order = list.filter(o => o.id == orderId)[0]
      if (!order) {
        return;
      }
      wx.navigateTo({
        url: '/pages/evaluatepage/evaluatepage?orderId=' + orderId,
        events: {
          setEvaluation: function (data) {
            this_.data.orderAll.filter(o => o.id == orderId).forEach((o, index) => {
              this_.setData({
                ['orderAll[' + index + '].evaluation']: data
              })
            })
            this_.data.orderAlreadyPayment.filter(o => o.id == orderId).forEach((o, index) => {
              this_.setData({
                ['orderAlreadyPayment[' + index + '].evaluation']: data
              })
            })
          }
        }
      })
    }
  }
})