// pages/address/address.js
// 获取应用实例
const app = getApp()
const {
  config, mapKey
} = require('../../utils/config')
var QQMapWX = require('../../libs/qqmap-wx-jssdk.js');
// 实例化API核心类
var qqmapsdk = new QQMapWX({
  key: mapKey// 必填，填自己在腾讯位置服务申请的key
});
Page({

  /**
   * 页面的初始数据
   */
  data: {
    addressList: [],
    speed: 0,
    accuracy: 16,
    city: '',
    keyword: '',
    markers: [],
    covers: [],
    latitude: 38.849563,
    longitude: 121.515856
  },
  inputKeyword(e) {
    this.setData({
      keyword: e.detail.value.trim()
    });

  },
  

  async onLocation() {
    let that = this;
    const { authSetting } = await wx.getSetting()

    const isAuth = !!authSetting['scope.userLocation']

    if (!isAuth) {
      const modalRes = await wx.modal({
        title: '授权提示',
        content: '需要需要您的地理位置信息，请确认授权'
      })

      if (!modalRes) return wx.toast({ title: '您拒绝了授权' })
      const { authSetting } = await wx.openSetting()

      if (!authSetting['scope.userLocation'])
        return wx.toast({ title: '授权失败！' })

      try {
        await wx.getLocation({
          isHighAccuracy: true,
          type: 'gcj02',
          success(res) {
            const latitude = res.latitude;
            const longitude = res.longitude;
            that.setData({
              latitude: res.latitude,
              longitude: res.longitude
            })
            qqmapsdk.reverseGeocoder({
              location: {
                latitude: res.latitude,
                longitude: res.longitude
              },
              success: function (res) {
                
                let markers = [];
                let obj = {};
                obj.id = 1;
                obj.latitude = latitude;
                obj.longitude = longitude;
                obj.name = res.result.address;
                markers[0] = obj;
                that.setData({
                  markers
                })
              },
              fail: function (error) {
                console.error(error);
              },
              complete: function (res) {

              }
            })


          },
        })
      } catch (err) {
        console.log(err)
      }
    } else {
      try {
        await wx.getLocation({
          isHighAccuracy: true,
          type: 'gcj02',
          success(res) {
            const latitude = res.latitude;
            const longitude = res.longitude;
            that.setData({
              latitude: res.latitude,
              longitude: res.longitude
            })
            qqmapsdk.reverseGeocoder({
              location: {
                latitude: res.latitude,
                longitude: res.longitude
              },
              success: function (res) {
                let markers = [];
                let obj = {};
                obj.id = 1;
                obj.latitude = latitude;
                obj.longitude = longitude;
                obj.name = res.result.address;
                markers[0] = obj;
                console.log(markers)
                that.setData({
                  markers
                })
              },
              fail: function (error) {
                console.error(error);
              },
              complete: function (res) {

              }
            })


          },
        })
      } catch (error) {
        wx.toast({ title: '您拒绝授权获取地址位置' })
      }
    }
  },
  getAddress(latitude, longitude){
    qqmapsdk.reverseGeocoder({
      location: {
        latitude: latitude,
        longitude: longitude
      },
      success: function (res) {
        let markers = [];
        markers.id = 1;
        markers.latitude = latitude;
        markers.longitude = longitude;
        markers.name = res.result.address;
        that.setData({
          markers
        })
      },
      fail: function (error) {
        console.error(error);
      },
      complete: function (res) {

      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    var that = this;
    if (options.latitude) {
      this.setData({
        latitude: options.latitude,
        longitude: options.longitude,
      })
      this.getAddress(options.latitude, options.longitude)
    } else {
      this.onLocation();
    }
  },

  //城市选择
  chooseCity: function () {
    wx.navigateTo({
      url: '../city/city',
    })
  },

  // 导航
  goNavigation: function (e) {
    let data = e.currentTarget.dataset;
    wx.getLocation({//获取当前经纬度
      type: 'gcj02', //返回可以用于wx.openLocation的经纬度，官方提示bug: iOS 6.3.30 type 参数不生效，只会返回 wgs84 类型的坐标信息
      success: function (res) {
        wx.openLocation({//?使用微信内置地图查看位置。
          latitude: Number(data.lat),//要去地点的纬度
          longitude: Number(data.lng),///要去地点的经度-地址
          scale: 18,
          name: data.name,//
          address: data.address,
          success() {
            console.log('地图打开成功');
          },
          fail(err) {
            console.error('地图打开失败', err);
          }
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady(e) {
    this.myCxt = wx.createMapContext('myMap')
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {


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