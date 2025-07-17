// pages/order_info/order_info.js
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
    skin: getApp().globalData.skin,
    theme:wx.getStorageSync('systemTheme'),
    pictureInfo: wx.getStorageSync('pictureInfo') ? JSON.parse(wx.getStorageSync('pictureInfo')) : '',
    config: config,
    selectMap: false,
    orderNo: '',
    selectCount: 0,
    totalPrice: 0,
    shopId: 1,
    distance: '',
    shopName: '',
    shopAddress: '',
    remark: '',
    phone: '',
    addressInfo: {},
    goodsList: [],
    isHidden: true,
    couponHave: false,
    couponId: null,
    couponValue: 0,
    isCoupon: 0,
    currentTab: 0,
    giver: '',
    giverNotes: '',
    isAnonymous: '',
    deliveryTime: '',
    deliveryDate: '',
    isChecked: false,
    hiddengift: false
  },
  checkboxChange() {
    this.setData({
      isChecked: !this.data.isChecked
    });

  },
  inputGiver(e) {
    this.setData({
      giver: e.detail.value
    })
  },
  inputGiverNotes(e) {
    this.setData({
      giverNotes: e.detail.value
    })
  },
  swichNav: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      if (e.target.dataset.current == 1) {
        let info = this.data.addressInfo;
        let minutesToAdd = info.distance > 4 ? 40 : info.distance > 3 ? 35 : 30;
        var date = new Date();
        var min = date.getMinutes();
        date.setMinutes(min + minutesToAdd);
        var y = date.getFullYear();
        var m = (date.getMonth() + 1) < 10 ? ("0" + (date.getMonth() + 1)) : (date.getMonth() + 1);
        var d = date.getDate() < 10 ? ("0" + date.getDate()) : date.getDate();
        var h = date.getHours() < 10 ? ('0' + date.getHours()) : date.getHours()
        var f = date.getMinutes() < 10 ? ('0' + date.getMinutes()) : date.getMinutes()

        var formatdate = y + '-' + m + '-' + d + ' ' + h + ":" + f;
        var formattime = h + ":" + f;
        this.setData({
          deliveryDate: formatdate,
          deliveryTime: formattime,
          addressInfo: info
        })
      }
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
  },

  goGift: function () {
    this.setData({
      hiddengift: !this.data.hiddengift
    })
  },

  inputRemark(e) {
    this.setData({
      remark: e.detail.value.trim()
    })
  },
  inputPhone(e) {

    this.setData({
      phone: e.detail.value.trim()
    })
  },

  // 地址
  goAddress: function () {
    this.setData({
      isHidden: false
    })
  },

  goAddresslist: function () {
    wx.navigateTo({
      url: '../addresslist/addresslist',
    })
  },

  //弹窗
  goClosed: function () {
    this.setData({
      isHidden: true
    })
  },
  goCancel: function () {
    this.setData({
      isHidden: true
    })
  },
  goSure: function () {
    this.setData({
      isHidden: true
    })
  },

  // 优惠券
  goDiscount: function () {
    if (this.data.couponHave && this.data.isCoupon == 0) {
      if (this.data.couponId) {
        wx.navigateTo({
          url: '../discount_x/discount_x?shopId=' + this.data.shopId + '&totalPrice=' + this.data.totalPrice + '&couponId=' + this.data.couponId,
        });
      } else {
        wx.navigateTo({
          url: '../discount_x/discount_x?shopId=' + this.data.shopId + '&totalPrice=' + this.data.totalPrice,
        });
      }

    }

  },

  // 支付
  goPay: function () {
    let that = this;
    // let phone = that.data.phone;
    // let myreg = /^(14[0-9]|13[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$$/;
    // if (!myreg.test(phone)) {
    //       wx.showToast({
    //         title: '请输入正确的手机号',
    //         icon: 'none',
    //         duration: 1000
    //       })
    //       return false;
    //     }

    let orderNo = that.data.orderNo == '' ? Date.parse(new Date()) + '' + Math.floor(Math.random() * 10000) : that.data.orderNo;
    that.savaOrders(orderNo);

  },

  savaOrders(orderNo) {
    let that = this;
    let shopId = this.data.shopId;
    let params = this.data.goodsList;
    if (this.data.hiddengift && this.data.currentTab == 1) {
      if (!this.data.isChecked && (this.data.giver == '' || this.data.giver.trim() == '')) {
        wx.showToast({
          title: '赠予者不可为空',
          icon: 'none',
          duration: 2000
        })
        return false;
      }
      params[0].giver = this.data.isChecked ? '匿名用户' : this.data.giver;
      params[0].isAnonymous = this.data.isChecked ? 1 : 2;
      params[0].giverNotes = this.data.giverNotes;
    }
    params[0].consignee = this.data.addressInfo.username;
    params[0].receiverPhone = this.data.addressInfo.phone;
    params[0].sex = this.data.addressInfo.sex;
    params[0].address = this.data.addressInfo.address + this.data.addressInfo.roomNumber;
    params[0].deliveryDate = this.data.deliveryDate;
    params[0].orderType = this.data.currentTab == 1 ? 1 : 2;
    params[0].shopId = this.data.shopId;
    params[0].equipmentType = wx.getStorageSync('equipmentType');
    params[0].remark = this.data.remark;
    params[0].phone = this.data.phone;
    params[0].openid = wx.getStorageSync('openid');
    params[0].totalPrice = this.data.totalPrice - this.data.couponValue;
    params[0].totalCount = this.data.selectCount;
    params[0].orderNo = orderNo;
    wx.request({
      url: `${config}/orders/insertOrders`,
      method: 'POST',
      data: JSON.stringify(params),
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        if (res.data.code == 20000) {
          let orderId = res.data.data.orders.id;
          let price = that.data.totalPrice - that.data.couponValue;
          // 以分为单位，所以×100
          price = price * 100;
          wx.request({
            url: `${config}/pay/payment/${shopId}/${orderId}`,
            method: 'POST',
            data: {
              openid: wx.getStorageSync("openid"),
              totalfee: price,
              couponId: that.data.couponId ? that.data.couponId : 0
            },
            header: {
              'Content-Type': "application/x-www-form-urlencoded"
            },
            success(res) {
              wx.requestPayment({
                timeStamp: res.data.data.paySign.timeStamp,
                nonceStr: res.data.data.paySign.nonceStr,
                package: res.data.data.paySign.package,
                signType: res.data.data.paySign.signType,
                paySign: res.data.data.paySign.paySign,
                appId: res.data.data.paySign.appid,
                success(ress) {
                  let couponId = that.data.couponId;;
                  that.updateOrders(orderId, couponId);
                  that.setData({
                    orderNo: ''
                  });
                  // wx.removeStorageSync('temporary');
                  wx.removeStorageSync('paramList');
                  wx.removeStorageSync('isAllChecked');
                  wx.removeStorageSync('totalPrice');
                  wx.removeStorageSync('selectCount');
                  

                  let pages = getCurrentPages();
                  let prevPage = pages[pages.length - 2];
                  prevPage.setData({
                    fromSelect: false
                  })
                  wx.requestSubscribeMessage({
                    tmplIds: ['KbLNNVRPPIQInCzH5apqCEof6m9Zkjvd3dlRe3OdD-g'],
                    success(res) {
                      if (wx.getStorageSync('equipmentType') == 1) {
                        wx.redirectTo({
                          url: '../payresult/payresult?orderId=' + orderId,
                        })
                      } else {
                        let pages = getCurrentPages();
                        let prevPage = pages[pages.length - 2];
                        prevPage.setData({
                          temporary: {},
                          fromSelect: false,
                          paramList: []
                        });
                        
                        wx.redirectTo({
                          url: '../order_xq/order_xq?orderId=' + orderId,
                        })
                      }

                    },
                    fail(err) {
                      wx.redirectTo({
                        url: '../payresult/payresult?orderId=' + orderId,
                      })
                    }
                  })
                },
                fail(ress) {
                  wx.redirectTo({
                    url: '../payresult/payresult?orderId=' + orderId + '&orderNo=' + orderNo + '&goodsList=' + JSON.stringify(that.data.goodsList) + '&totalPrice=' + that.data.totalPrice + '&selectCount=' + that.data.selectCount + '&shopId=' + that.data.shopId,
                  })

                }
              })


            }
          })

        }

      }
    });
  },

  updateOrders(id, couponId) {
    let that = this;
    wx.request({
      // url: `${config}/orders/updateOrders/${id}/${couponId}`,
      url: `${config}/orders/updateOrders/${id}`,
      method: 'POST',
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {

      }
    });
  },

  getShopInfo() {
    let that = this;
    wx.request({
      url: `${config}/shop/getShopInfo`,
      method: 'GET',
      data: {
        shopId: this.data.shopId,
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let info = res.data.data.info;
        that.setData({
          shopName: info.name,
          shopAddress: info.address,
          distance: wx.getStorageSync('distance') || 0
        })
        // if(that.data.selectMap){
        //   let lat = info.lat;
        // let lng = info.lng;
        // wx.getLocation({
        //     isHighAccuracy: true, // 开启地图精准定位
        //     type: 'gcj02',
        //     success(res) {
        //         const latitude = res.latitude;
        //         const longitude = res.longitude;
        //         let distance = that.getDistances(lat, lng, latitude, longitude);
        //         that.setData({
        //             distance: distance < 1 ? Number(distance * 1000).toFixed(2) + 'm' : distance.toFixed(2) + 'km'
        //         })
        //     },
        // })
        // }


      }
    });
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

  getCouponHave() {
    let that = this;
    wx.request({
      url: `${config}/coupon/getIsCoupons`,
      method: 'GET',
      data: {
        shopId: this.data.shopId,
        openid: wx.getStorageSync('openid')
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let list = res.data.data.list;

        let newList = list.filter(item => item.limits <= that.data.totalPrice);
        // let couponList = list.filter(item => item.type == 1 && item.limits <= that.data.totalPrice);
        that.setData({
          couponHave: newList.length > 0 ? true : false,
          // couponId: couponList.length > 0 ? couponList[0].id : null,
          // isCoupon: couponList.length > 0 ? couponList[0].parValue : 0
        })

      }
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 适配IphoneX
    let isIphoneX = app.globalData.isIphoneX;
    this.setData({
      isIphoneX: isIphoneX
    });
    if (options.params) {
      this.setData({
        goodsList: JSON.parse(options.params),
        totalPrice: options.totalPrice,
        selectCount: options.selectCount,
        shopId: options.shopId
      })
    }
    if (options.orderNo) {
      this.setData({
        orderNo: options.orderNo
      })
    } else {
      this.setData({
        orderNo: ''
      });
    }
    if (options.from) {
      this.setData({
        currentTab: options.from == 'takeaway' ? 1 : 0
      })
    }

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
    if (wx.getStorageSync('selectMap')) {
      this.setData({
        selectMap: wx.getStorageSync('selectMap')
      })
    }
    this.getShopInfo();
    this.getCouponHave();
    if (wx.getStorageSync('positioning')) {
      let info = JSON.parse(wx.getStorageSync('positioning'));
      let minutesToAdd = info.distance > 4 ? 40 : info.distance > 3 ? 35 : 30;
      var date = new Date();
      var min = date.getMinutes();
      date.setMinutes(min + minutesToAdd);
      var y = date.getFullYear();
      var m = (date.getMonth() + 1) < 10 ? ("0" + (date.getMonth() + 1)) : (date.getMonth() + 1);
      var d = date.getDate() < 10 ? ("0" + date.getDate()) : date.getDate();
      var h = date.getHours() < 10 ? ('0' + date.getHours()) : date.getHours()
      var f = date.getMinutes() < 10 ? ('0' + date.getMinutes()) : date.getMinutes()

      var formatdate = y + '-' + m + '-' + d + ' ' + h + ":" + f;
      var formattime = h + ":" + f;
      this.setData({
        deliveryDate: formatdate,
        deliveryTime: formattime,
        addressInfo: info
      })
    }

    if (wx.getStorageSync('selectColor')) {
      theme.setTheme(wx.getStorageSync('selectColor'));
      setTimeout(function () {
        that.setData({
          theme: wx.getStorageSync('systemTheme')
        })
      }, 300)
      that.getTemplateInfo(wx.getStorageSync('selectColor'));
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