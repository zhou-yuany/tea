// pages/addresslist/addresslist.js
const app = getApp()
const theme = require("../../utils/themeSystem")
const {
  config, mapKey
} = require('../../utils/config.js')
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
    config,
    pictureInfo: wx.getStorageSync('pictureInfo') ? JSON.parse(wx.getStorageSync('pictureInfo')) : '',
    theme:wx.getStorageSync('systemTheme'),
    skin: getApp().globalData.skin,
    from: '',
    hongse: 'hongse',
    id: 0,
    addressInfo: {},
    addressList: [],
    tab: '',
    windowHeight: 0,
    scrollHeight: 0,
    dizhik: 0,
    dizhihomek: 0
  },

  // 添加地址
  goAdd: function () {
    wx.navigateTo({
      url: '../add_address/add_address?tab=' + this.data.tab
    })
  },
  clearData(){
    wx.removeStorageSync('positioning');
    
    let tab = this.data.tab;
    let pages = getCurrentPages();
    let prevPage = pages[pages.length - 2];
    if (tab == "info" || tab == "goods") {
      prevPage.setData({
        tab: "info",
        flag: 1,
        address_info: {}
      })
      wx.navigateBack({
        delta: 1,
      })
    }
    if (tab !== "my" && tab !== "info" && tab !== "goods") {
      prevPage.setData({
        ids: wx.getStorageSync('cartId').length > 0 ? wx.getStorageSync('cartId').split(',') : '',
        tab: tab,
        flag: 1,
        address_info: {}
      })
      wx.navigateBack({
        delta: 1,
      })

    }
  },
  goSelect: function (e) {
    let tab = this.data.tab;
    wx.setStorageSync('positioning', JSON.stringify(e.currentTarget.dataset.info));
    // wx.setStorageSync('address_info', e.currentTarget.dataset.id)
    let pages = getCurrentPages();
    let prevPage = pages[pages.length - 2];
    if (tab == "info" || tab == "goods") {
      prevPage.setData({
        tab: "info",
        flag: 1,
      })
      wx.navigateBack({
        delta: 1,
      })
    }
    if (tab !== "my" && tab !== "info" && tab !== "goods") {
      prevPage.setData({
        ids: wx.getStorageSync('cartId').length > 0 ? wx.getStorageSync('cartId').split(',') : '',
        tab: tab,
        flag: 1,
      })
      wx.navigateBack({
        delta: 1,
      })

    }
    // if(this.data.id!=0)
    // {
    // wx.navigateBack({
    //   delta: 1,
    // })
    // }

  },

  //编辑地址
  goEdit: function (e) {
    wx.navigateTo({
      url: '../edit_address/edit_address?id=' + e.currentTarget.dataset.id + '&tab=' + this.data.tab
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;

    if (typeof (options.id) != "undefined") {
      that.setData({
        id: options.id
      })
    }
    if (options.tab) {
      that.setData({
        tab: options.tab
      })
    }
    if (options.from) {
      this.setData({
        from: options.from
      })
      if(options.from == 'takeaway'){
        this.onLocation();
      }
      
    }

    wx.getSystemInfo({
      success: function (res) {
        let windowHeight = (res.windowHeight * (750 / res.windowWidth));
        that.setData({
          windowHeight: windowHeight
        })
      }
    });

    setTimeout(function (options) {
      var dizhik = wx.createSelectorQuery();
      dizhik.select('.dizhik').boundingClientRect();
      dizhik.exec(function (rect) { if (rect[0] === null) return; that.setData({ dizhik: rect[0].height }) });
      var dizhihomek = wx.createSelectorQuery();
      dizhihomek.select('.dizhihomek').boundingClientRect();
      dizhihomek.exec(function (rect) { if (rect[0] === null) return; that.setData({ dizhihomek: rect[0].height }) });
    }, 500)
    setTimeout(function () {
      var dizhikHeight = that.data.dizhik;
      var dizhihomekHeight = that.data.dizhihomek;
      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - dizhikHeight - dizhihomekHeight - 80
          })
        }
      })
    }, 1000)
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
            // const latitude = res.latitude;
            // const longitude = res.longitude;
            qqmapsdk.reverseGeocoder({
              location: {
                latitude: res.latitude,
                longitude: res.longitude
              },
              success: function (res) {

                // that.setData({
                //     address: res.result.address
                // })
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
          isHighAccuracy: true, // 开启地图精准定位
          type: 'gcj02',
          success(res) {
            // const latitude = res.latitude;
            // const longitude = res.longitude;
            qqmapsdk.reverseGeocoder({
              location: {
                latitude: res.latitude,
                longitude: res.longitude
              },
              success: function (res) {
  
                // that.setData({
                //     address: res.result.address
                // })
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

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    let that = this;
    if(wx.getStorageSync('positioning')){
      this.setData({
        addressInfo: JSON.parse(wx.getStorageSync('positioning'))
      })
    }else{
      this.setData({
        addressInfo: {}
      })
    }
    if (wx.getStorageSync('defaultAddress')) {
      wx.removeStorageSync('defaultAddress')
    }
    wx.request({
      url: `${config}/address/getListByOpenid`,
      header: {
        "Content-Type": "application/json"
      },
      method: 'GET',
      data: {
        openid: wx.getStorageSync('openid'),
      },
      success: function (res) {
        let list = res.data.data.list;

        if (list.length > 0) {
          that.setData({
            addressList: list
          })
          let defaultAddress = list.filter(item => {
            return item.is_default == 1
          })
          if (defaultAddress.length > 0) {
            wx.setStorageSync('defaultAddress', defaultAddress[0])
          }
        } else {
          that.setData({
            addressList: []
          })
          if (wx.getStorageSync('address_info')) {
            wx.removeStorageSync('address_info')
          }
        }

      }
    });

    if (wx.getStorageSync('selectColor')) {
      theme.setTheme(wx.getStorageSync('selectColor'));
      setTimeout(function () {
        that.setData({
          theme: wx.getStorageSync('systemTheme')
        })
      }, 300)
      that.getTemplateInfo(wx.getStorageSync('selectColor'))
    }
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
        that.setData({
          pictureInfo
        })
      }
    });
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
    if (this.data.tab == "my") {
      wx.redirectTo({
        url: '../my/my',
      })
    }

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