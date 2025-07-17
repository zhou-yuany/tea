// pages/order_xq/order_xq.js
const app = getApp()
const theme = require("../../utils/themeSystem")
const {
  config
} = require('../../utils/config')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    theme:wx.getStorageSync('systemTheme'),
    config: config,
    info: {},
    id: '',
    equipmentNo: '',
    showBei: false,
    showMake: false,
    showStart: false
  },

  sure:function(){
    this.setData({
      showBei: false,
      showMake:true
    })
  },

  onMake:function(){
    this.setData({
      showMake: false
    })
  },
  makeOrders(e){
    let id = e.currentTarget.dataset.id;
    let equipmentNo = e.currentTarget.dataset.equipmentno;
    this.setData({
        id,
        equipmentNo,
        showBei: true,
        showStart: true
    })
},

startMake(){
    let that = this;
    let id = this.data.id;
    this.setData({
        showStart: false
    })
    wx.request({
        url: `${config}/orders/makeParam/${id}`,
        method: 'GET',
        data: {},
        header: {
            'Content-Type': 'application/json'
        },
        success(res) {
          if(res.data.code == 20000){
            setTimeout(function () {
              that.getInfo(that.data.orderId);
              that.setData({
                  showBei: false
              })
            }, parseInt(res.data.data.max)*1000)
          }else{
            console.log(res)
            wx.showModal({
              title: '材料不足',
              content: res.data.message,
              complete: (res) => {
                if (res.cancel) {
                  that.setData({
                    showBei: false
                })
                }
            
                if (res.confirm) {
                  that.setData({
                    showBei: false
                })
                }
              }
            })
          }
            
        },
    });
},

  onClick: function (e) {
    var that = this;
    wx.setClipboardData({
      //准备复制的数据
      data: that.data.info.orderNum,
      success: function (res) {
        wx.showToast({
          title: '复制成功',
          icon: 'none'
        });
      }
    });
  },

  //导航
  goDaohang(){
      const latitude = 29.557284
      const longitude = 106.577153
      //打开内部地图（默认腾讯地图）
      wx.openLocation({
            latitude,
            longitude,
            scale: 18
      })
  
  },

  //电话
  goTell:function(){
    wx.makePhoneCall({
      phoneNumber: this.data.info.shopPhone,
    })
  },
  getFoodInfo(orderId) {
    let that = this;
    wx.request({
        url: `${config}/orders/getOrdersInfo`,
        method: 'GET',
        data: {
            orderId: orderId
        },
        header: {
            'Content-Type': 'application/json'
        },
        success(res) {
            wx.setStorageSync('orderInfo', JSON.stringify(res.data.data.info))
            wx.switchTab({
                url: '/pages/food/food',
            })

        }
    });
},
  getInfo(orderId){
    let that = this;
    wx.request({
      url: `${config}/orders/getOrdersInfo`,
      method: 'GET',
      data: {
        orderId: orderId
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        that.setData({
          info: res.data.data.info
        })
        
      }
    });
  },

  anotherOrder(){
    // wx.setStorageSync('orderInfo', JSON.stringify(this.data.info));
    // wx.removeStorageSync('orderInfo');
    // wx.switchTab({
    //   url: '../food/food',
    // })
    this.getFoodInfo(this.data.info.id)
        wx.removeStorageSync('temporary');
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    if(options.orderId){
      this.setData({
        orderId: options.orderId
      });
      this.getInfo(options.orderId);
    }

    // 适配IphoneX
    let isIphoneX = app.globalData.isIphoneX;
    this.setData({
      isIphoneX: isIphoneX
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    let that = this;
    if (wx.getStorageSync('selectColor')) {
      theme.setTheme(wx.getStorageSync('selectColor'));
      setTimeout(function () {
        that.setData({
          theme: wx.getStorageSync('systemTheme')
        })
      }, 300)
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})