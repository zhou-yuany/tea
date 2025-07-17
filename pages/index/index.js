// index.js
// 获取应用实例
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
    config,
    pictureInfo: wx.getStorageSync('pictureInfo') ? JSON.parse(wx.getStorageSync('pictureInfo')) : '',
    skin: getApp().globalData.skin,
    theme: wx.getStorageSync('systemTheme'),
    statusBarHeight: app.globalData.statusBarHeight,
    bannerList: [],
    indicatorDots: false,
    autoplay: true,
    interval: 5000,
    duration: 300,
    circular: true,
    controls: false,
    isAd: true,
  },

  goYh: function () {
    wx.navigateTo({
      url: '../coupon/coupon',
    })
    wx.setStorageSync('isAd', true)
    this.setData({
      isAd: true
    })
  },
  onClosed: function () {
    wx.setStorageSync('isAd', true)
    this.setData({
      isAd: true
    })
  },

  // 轮播图
  previewImage: function (e) {
    var current = e.target.dataset.src;
    wx.previewImage({
      current: current,
      urls: this.data.banner_list,
    })
  },

  videoPlay: function () {
    var videoplay = wx.createVideoContext('video');
    videoplay.play()
    this.setData({
      controls: true
    })
  },

  //点餐
  goFood: function () {
    wx.switchTab({
      url: '../food/food?from=store',
    })
  },

  //外卖
  goStore: function () {
    wx.navigateTo({
      url: '../store/store?from=takeaway',
    })
  },

  //外卖制作分成
  goMake: function () {
    wx.navigateTo({
      url: '../divideinto/divideinto',
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  loginIn() {
    let that = this;
    //用户按了允许授权的按钮
    wx.login({
      success(res) {
        wx.request({
          url: `${config}/users/auth`,
          method: 'GET',
          data: {
            code: res.code
          },
          header: {
            'Content-Type': 'application/json'
          },
          success(res) {
            let response = res.data.data.response;
            let token = res.data.data.token;
            let resObj = JSON.parse(response);
            that.saveInfo(resObj.openid);
          }
        });
        wx.setStorageSync("code", res.code)
      },
    })
  },

  saveInfo(openid) {
    wx.request({
      url: `${config}/users/login`,
      method: 'GET',
      data: { openid },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        wx.setStorageSync("openid", openid);
      }
    });
  },
  async getShopInfo() {

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
          isHighAccuracy: true, // 开启地图精准定位
          type: 'gcj02',
          success(res) {
            const latitude = res.latitude;
            const longitude = res.longitude;
            wx.request({
              url: `${config}/shop/getShopList`,
              method: 'GET',
              data: {},
              header: {
                'Content-Type': 'application/json'
              },
              success(res) {
                let list = res.data.data.list;
                list.forEach(item => {
                  let lat = item.lat;
                  let lng = item.lng;
                  item.distance = that.getDistances(lat, lng, latitude, longitude);

                });
                let newArr = list.sort((a, b) => a.distance - b.distance);
                wx.setStorageSync('selectColor', newArr[0].color);
                that.getTemplateInfo(newArr[0].color);
              }
            });

          },

        })
      } catch (err) {
        console.log(err)
      }
    } else {
      try {
        await wx.getLocation({
          isHighAccuracy: true, // 开启地图精准定位
          type: 'gcj02',
          success(res) {
            const latitude = res.latitude;
            const longitude = res.longitude;
            wx.request({
              url: `${config}/shop/getShopList`,
              method: 'GET',
              data: {},
              header: {
                'Content-Type': 'application/json'
              },
              success(res) {
                let list = res.data.data.list;
                list.forEach(item => {
                  let lat = item.lat;
                  let lng = item.lng;
                  item.distance = that.getDistances(lat, lng, latitude, longitude);

                });
                let newArr = list.sort((a, b) => a.distance - b.distance);
                wx.setStorageSync('selectColor', newArr[0].color);
                that.getTemplateInfo(newArr[0].color);
              }
            });

          },

        })
      } catch (error) {
        wx.toast({ title: '您拒绝授权获取地址位置' })
      }
    }



  },
  getDistances(lat1, lng1, lat2, lng2) {
    let EARTH_RADIUS = 6378.137; // 地球半径
    let radLat1 = lat1 * Math.PI / 180.0; //lat1 * Math.PI / 180.0=>弧度计算
    let radLat2 = lat2 * Math.PI / 180.0;
    let a = radLat1 - radLat2;
    let b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
    let s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
    s = s * EARTH_RADIUS;
    s = Math.round(s * 10000) / 10000; // 输出为公里
    return s
  },

  checkLocationAuth() {
    let that = this
    wx.getSetting({
      success: (res) => {
        let authSetting = res.authSetting
        if (authSetting['scope.userLocation']) {
          that.getShopInfo();
        } else if (authSetting['scope.userLocation'] === false) {
          wx.showModal({
            title: '您未开启地理位置授权',
            content: '请在系统设置中打开位置授权，以便我们为您提供更好的服务',
            success: (res) => {
              if (res.confirm) {
                wx.openSetting()
              }
            }
          })
        } else {
          wx.authorize({
            scope: 'scope.userLocation',
            success: () => {
              that.getShopInfo();
            },
            fail: () => {
              wx.showModal({
                title: '您未开启地理位置授权',
                content: '请在系统设置中打开位置授权，以便我们为您提供更好的服务',
                success: (res) => {
                  if (res.confirm) {
                    wx.openSetting()
                  }
                }
              })
            }
          })
        }
      }
    })
  },

  getTemplateInfo(selectColor) {
    let that = this;
    wx.request({
      url: `${config}/template/getTemplateInfo`,
      method: 'GET',
      data: {
        color: selectColor
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let pictureInfo = res.data.data.pictureInfo;
        let list = pictureInfo.banner.split(",");
        let newList = [];
        list.forEach((item, index) => {
          let obj = {};
          obj.url = item;
          obj.type = 0;
          newList.push(obj);
        });
        that.setData({
          bannerList: newList,
          pictureInfo
        })
      }
    });
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    let that = this;
    if (!wx.getStorageSync('openid')) {
      this.loginIn()
    } else {
      if (wx.getStorageSync('info')) {
        if (JSON.parse(wx.getStorageSync('info')).isNew == 1) {
          if (wx.getStorageSync('isAd')) {
            this.setData({
              isAd: wx.getStorageSync('isAd')
            })
          } else {
            wx.setStorageSync('isAd', false)
            this.setData({
              isAd: false
            })
          }


        }

        
      }
    }
    if (!wx.getStorageSync('selectColor')) {
      this.checkLocationAuth("list");
    }else{
      theme.setTheme(wx.getStorageSync('selectColor'));
      setTimeout(function () {
        that.setData({
          theme: wx.getStorageSync('systemTheme')
        })
    }, 300)
    that.getTemplateInfo(wx.getStorageSync('selectColor'))
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