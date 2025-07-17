// pages/data/data.js
// 获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    windowHeight: 0,
    scrollHeight: 0,
    headtop: 0,
    dataList: [{
      pic: '../../images/ico0.png',
      name: '销售排名'
    },{
      pic: '../../images/ico3.png',
      name: '支付渠道'
    },{
      pic: '../../images/ico4.png',
      name: '在售库存'
    },{
      pic: '../../images/ico5.png',
      name: '数据漏斗'
    },{
      pic: '../../images/ico6.png',
      name: '销售近况'
    },{
      pic: '../../images/ico7.png',
      name: '日销数据'
    }]
  },

  //切换视图
  goView:function(){
    wx.navigateTo({
      url: '../view/view',
    })
  },

  // 列表跳转页面
  itemClick:function(e){
    let item = e.currentTarget.dataset.item;
  
    if(item == 0){
      wx.navigateTo({
        url: '../ranking/ranking',
      })
    }else if(item == 1){
      wx.navigateTo({
        url: '../paychannel/paychannel',
      })
    }else if(item == 2){
      wx.navigateTo({
        url: '../stock/stock',
      })
    }else if(item == 3){
      wx.navigateTo({
        url: '../datafunnel/datafunnel',
      })
    }else if(item == 4){
      wx.navigateTo({
        url: '../salerecent/salerecent',
      })
    }else if(item == 5){
      wx.navigateTo({
        url: '../dailysales/dailysales',
      })
    }
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
      var headtop = wx.createSelectorQuery();
      headtop.select('.headtop').boundingClientRect();
      headtop.exec(function (rect) { if (rect[0] === null) return; that.setData({ headtop: rect[0].height }) });
    }, 500)
    setTimeout(function () {
      var headtopHeight = that.data.headtop;
      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - headtopHeight
          })
        }
      })
    }, 1000)
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