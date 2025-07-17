
// 获取应用实例
const app = getApp()
const context = require("../../../utils/context")
const api = require("../../../utils/api")

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
    orderList: [{
      ordernum: 'A123456001',
      time: '2021-08-08',
      startaddress: '辽宁省大连市中山区',
      endaddress: '上海市市辖区黄浦路提车点',
      profit: '200',
      number: '2'
    },{
      ordernum: 'A123456001',
      time: '2021-08-08',
      startaddress: '辽宁省大连市中山区',
      endaddress: '上海市市辖区黄浦路提车点',
      profit: '200',
      number: '2'
    },{
      ordernum: 'A123456001',
      time: '2021-08-08',
      startaddress: '辽宁省大连市中山区',
      endaddress: '上海市市辖区黄浦路提车点',
      profit: '200',
      number: '2'
    },{
      ordernum: 'A123456001',
      time: '2021-08-08',
      startaddress: '辽宁省大连市中山区',
      endaddress: '上海市市辖区黄浦路提车点',
      profit: '200',
      number: '2'
    },{
      ordernum: 'A123456001',
      time: '2021-08-08',
      startaddress: '辽宁省大连市中山区',
      endaddress: '上海市市辖区黄浦路提车点',
      profit: '200',
      number: '2'
    }],
    user: {},
    windowHeight: 0,
    scrollHeight: 0,
    gather: 0, 
    triggered: false, 
    init: false, 
    sumPrice: 0, 
    orderAlreadyPayment: [], 
    getData: function(call, offset) {
      let requestData = {pageSize:4}
      if(offset){ requestData['offset'] = offset; }
      api.get(`/order/customer/selectPage/ALREADY_PAYMENT`, requestData, true).then(call)
    },
    showrules: false
  },

  ready: function(){
  },
  /**
   * 组件的方法列表
   */
  methods: {

    //规则
    goRules:function(){
      this.setData({
        showrules: true
      })
    },
    goCloseds:function(){
      this.setData({
        showrules: false
      })
    },
    
    refreshOrder: function(){
      let this_ = this
      this.data.getData(data=>{
        data['triggered'] = false
        this_.setData(data);
      }, this.data.orderType)

    },

    bindDownLoad: function(){
      let this_ = this
      let lastData = this.data.orderAlreadyPayment;
      if(lastData.length <= 0){
        return;
      }
      this.data.getData(list=>{
        list.forEach(d=>lastData.push(d))
        this_.setData({orderAlreadyPayment: lastData});
      }, lastData.length)
    },

    componentInnerFunction: function() {
      if(this.data.init){
        return;
      }
      this.setData({user: context.getUser()})
      this.setData({init: true})
      let this_ = this
      api.get("/order/customer/sum/price", {}, true).then(res=>this_.setData({sumPrice: res.sum}))
      this.data.getData(list=>this_.setData({orderAlreadyPayment: list, triggered: false}))
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
        var gather = wx.createSelectorQuery();
        gather.select('.gather').boundingClientRect();
        gather.exec(function (rect) { if (rect[0] === null) return; that.setData({ gather: rect[0].height }) });
      }, 500)

      setTimeout(function () {
        var gatherHeight = that.data.gather;
        wx.getSystemInfo({
          success: function (res) {
            let scrollHeight = res.windowHeight;
            that.setData({
              scrollHeight: scrollHeight - gatherHeight - 106
            })
          }
        })
      }, 1000)
    },
  }
})