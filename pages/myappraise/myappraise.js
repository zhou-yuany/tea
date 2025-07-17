// pages/myappraise/myappraise.js
// 获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    windowHeight: 0,
    scrollHeight: 0,
    scorek: 0,
    tabList: [{
      tabitem: '全部',
      num: '366'
    },{
      tabitem: '最新'
    },{
      tabitem: '好评',
      num: '355'
    },{
      tabitem: '差评',
      num: '1'
    }],
    currentTab: 0,
    commont:[{
      virtual_avatar: '../../images/header.png',
      virtual_user: '匿名用户',
      addtime: '2021-06-08',
      content: '服务很好很准时服务很好很准时服务很好很准时服务很好很准时服务很好很准时服务很好很准时服务很好很准时服务很好很准时服务很好很准时',
      pic_list: ['../../images/food.jpg','../../images/food.jpg','../../images/food.jpg'],
      reply_content: '亲爱的客户，非常抱歉影响您的用餐，感谢您的提醒和反馈，您反馈的问题我们会在下次服务中改进，期待您再次光临！',
      ordernum: '123456789001',
      huifu: false
    },{
      virtual_avatar: '../../images/header.png',
      virtual_user: '匿名用户',
      addtime: '2021-06-08',
      content: '服务很好很准时服务很好很准时服务很好很准时',
      pic_list: ['../../images/food.jpg','../../images/food.jpg','../../images/food.jpg'],
      reply_content: '亲爱的客户，非常抱歉影响您的用餐，感谢您的提醒和反馈，您反馈的问题我们会在下次服务中改进，期待您再次光临！',
      ordernum: '123456789001',
      huifu: true
    },{
      virtual_avatar: '../../images/header.png',
      virtual_user: '匿名用户',
      addtime: '2021-06-08',
      content: '服务很好很准时服务很好很准时服务很好很准时',
      pic_list: ['../../images/food.jpg'],
      reply_content: '亲爱的客户，非常抱歉影响您的用餐，感谢您的提醒和反馈，您反馈的问题我们会在下次服务中改进，期待您再次光临！',
      ordernum: '123456789001',
      huifu: true
    }]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
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
      var scorek = wx.createSelectorQuery();
      scorek.select('.scorek').boundingClientRect();
      scorek.exec(function (rect) { if (rect[0] === null) return; that.setData({ scorek: rect[0].height }) });
    }, 500)
    setTimeout(function () {
      var scorekHeight = that.data.scorek;
      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - scorekHeight
          })
        }
      })
    }, 1000)
  },

  //评价tab
  clickAll: function(e){
    if (this.data.currentTab == e.currentTarget.dataset.current){
      return;
    }
    this.setData({
      currentTab: e.currentTarget.dataset.current
    })
  },

  //商家回复
  goAsk: function(){
    wx.navigateTo({
      url: '../appraise/appraise',
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