// pages/food/food.js
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
    config: config,
    pictureInfo: wx.getStorageSync('pictureInfo') ? JSON.parse(wx.getStorageSync('pictureInfo')) : '',
    skin: getApp().globalData.skin,
    theme: wx.getStorageSync('systemTheme'),
    selectMap: true,
    fromSelect: false,
    windowHeight: 0,
    scrollHeight: 0,
    scrollHeights: 0,
    head: 0,
    banner: 0,
    shopId: 0,
    curNum: 1,
    distance: '',
    shopName: '',
    selectCount: 0,
    totalPrice: 0,
    isRefresh: false,
    currentTab: 0,
    addressInfo: {},
    tabList: [],
    goodsList: [],
    paramList: [],
    temporary: {},
    list: [],
    isAllChecked: false, //全选
    minusStatus: true,
    coupon: 0,
    checkbox: 0,
    hiddenshopcark: false,
    temParamList: [],
    shopkhiddden: true,
    couponList: [],
    couponShow: true,
    maskHidden: false,
    hiddenstore: false
  },

  goChange: function () {
    this.setData({
      hiddenstore: !this.data.hiddenstore
    })
    if (this.data.hiddenstore) {

      wx.navigateTo({
        url: '../store/store?from=takeaway',
      })
    } else {
      this.getShopInfo();
    }

  },

  getCouponList() {
    let that = this;
    wx.request({
      url: `${config}/coupon/getCouponByOpenid`,
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
        that.setData({
          couponList: list,
        })
      }
    });
  },

  lingqu(e) {
    let couponId = e.currentTarget.dataset.id;
    let that = this;
    wx.request({
      url: `${config}/coupon/receiveCouponByOpenid`,
      method: 'POST',
      data: {
        couponId: couponId,
        openid: wx.getStorageSync('openid')
      },
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      success(res) {
        that.getCouponList();
      }
    });
  },

  gocouponList: function () {
    this.setData({
      couponShow: false
    });
  },
  goClose: function () {
    this.setData({
      couponShow: true
    })
  },


  showShop: function () {
    this.setData({
      shopkhiddden: !this.data.shopkhiddden
    })
  },

  //选择门店
  goStore: function () {
    if (this.data.selectMap) {
      let hiddenstore = this.data.hiddenstore;
      let from = hiddenstore ? 'takeaway' : 'store';
      wx.navigateTo({
        url: '../store/store?from=' + from,
      })
    }

  },
  // goStore(){
  //     console.log('vvvvvvvvvv')
  //     let that = this;
  //     wx.getSetting({
  //         success: (res) => {
  //           let authSetting = res.authSetting
  //           if (authSetting['scope.userLocation']) {
  //             // 已授权
  //             wx.getLocation({
  //                 isHighAccuracy: true, // 开启地图精准定位
  //                 type: 'gcj02',
  //                 success(res) {
  //                     const latitude = res.latitude;
  //                     const longitude = res.longitude;
  //                     wx.request({
  //                         url: `${config}/shop/getShopList`,
  //                         method: 'GET',
  //                         data: {},
  //                         header: {
  //                             'Content-Type': 'application/json'
  //                         },
  //                         success(res) {
  //                             let list = res.data.data.list;
  //                             list.forEach(item => {
  //                                 let lat = item.lat;
  //                                 let lng = item.lng;
  //                                 item.distance = that.getDistances(lat, lng, latitude, longitude);
  //                             });
  //                             let newArr = list.sort((a, b) => a.distance - b.distance);
  //                             that.setData({
  //                                 shopId: newArr[0].id,
  //                                 shopName: newArr[0].name,
  //                                 distance: newArr[0].distance < 1 ? Number(newArr[0].distance * 1000).toFixed(2) + 'm' : newArr[0].distance.toFixed(2) + 'km'
  //                             })

  //                             that.getCates();

  //                             that.getCouponList();
  //                         }
  //                     });

  //                 },
  //             })
  //           } else {
  //             wx.showModal({
  //               title: '开启地理位置授权',
  //               content: '确认开通地理位置开始点餐吗？',
  //               success: res => {
  //                 if (res.confirm) {
  //                   wx.openSetting()
  //                 }
  //               }
  //             })
  //           }
  //         }
  //       })


  // },
  // 内容
  tabNav(e) {
    let currentTab = e.currentTarget.dataset.index;
    let goodsList = this.data.tabList[currentTab].goodsList;
    this.setData({
      currentTab,
      goodsList
    })
    // this.getGoods();
  },
  handleSwiper(e) {
    let {
      current,
      source
    } = e.detail
    if (source === 'autoplay' || source === 'touch') {
      const currentTab = current;
      let goodsList = this.data.tabList[currentTab].goodsList;
      this.setData({
        currentTab,
        goodsList
      })
      // this.getGoods();
    }
  },

  // 产品详情
  goXq: function (e) {
    if (wx.getStorageSync('phone')) {
      let flag = this.isInTime(this.data.startTime, this.data.endTime)
      if (flag) {
        let cateId = this.data.tabList[`${this.data.currentTab}`].id;
        if (this.data.paramList.length > 0) {
          let curParam = this.data.paramList.reverse().find(item => item.goodsId == e.currentTarget.dataset.id);

          if (curParam != undefined) {
            wx.navigateTo({
              url: '../productxq/productxq?cateId=' + cateId + '&goodsId=' + e.currentTarget.dataset.id + '&curSelect=' + curParam.curSelect + '&shopId=' + this.data.shopId,
            })
          } else {
            wx.navigateTo({
              url: '../productxq/productxq?cateId=' + cateId + '&goodsId=' + e.currentTarget.dataset.id + '&shopId=' + this.data.shopId,
            })
          }

        } else {
          wx.navigateTo({
            url: '../productxq/productxq?cateId=' + cateId + '&goodsId=' + e.currentTarget.dataset.id + '&shopId=' + this.data.shopId,
          })
        }
      } else {
        this.setData({
          isBusiness: 2
        })
        wx.showToast({
          title: '该店铺已休息，明天再来吧~',
        })
      }


    } else {
      wx.navigateTo({
        url: '../login/login?from=food',
      })
    }



  },

  // 单选全选
  checkboxChange(e) {
    let index = e.currentTarget.dataset.index;
    let paramList = this.data.paramList;

    this.setData({
      ['paramList[' + index + '].checked']: e.detail.value[0] ? true : false
    })
    let checkedCount = 0;
    let selectCount = 0;
    let totalPrice = 0;

    paramList.forEach(item => {
      checkedCount = item.checked ? checkedCount + 1 : checkedCount;
      selectCount = item.checked ? parseInt(item.goodsCount) + selectCount : selectCount;
      totalPrice = item.checked ? totalPrice + item.price * parseInt(item.goodsCount) : totalPrice;
    })

    this.setData({
      isAllChecked: paramList.length == checkedCount ? true : false,
      selectCount,
      totalPrice
    })

  },

  // 全选单选
  allChecked() {
    let paramList = this.data.paramList;
    paramList.forEach(item => {
      item.checked = !item.checked;
    })
    this.setData({
      paramList: paramList
    })
    let selectCount = 0;
    let totalPrice = 0;
    this.data.paramList.forEach(item => {
      selectCount = item.checked ? parseInt(item.goodsCount) + selectCount : selectCount;
      totalPrice = item.checked ? totalPrice + item.price * parseInt(item.goodsCount) : totalPrice;
    })
    this.setData({
      totalPrice: totalPrice,
      selectCount: selectCount
    })

  },

  //数量增加
  addNum(e) {
    let index = e.currentTarget.dataset.index;
    let paramList = this.data.paramList;
    let goodsCount = paramList[index].goodsCount;
    let selectCount = 0;
    let totalPrice = 0;

    this.setData({
      ['paramList[' + index + '].goodsCount']: parseInt(goodsCount) + 1,
      minusStatus: false,
    })
    this.data.paramList.forEach(item => {
      selectCount = item.checked ? parseInt(item.goodsCount) + selectCount : selectCount;
      totalPrice = item.checked ? totalPrice + item.price * parseInt(item.goodsCount) : totalPrice;
    })
    this.setData({
      totalPrice: totalPrice.toFixed(2),
      selectCount: selectCount
    })
  },

  //数量减少
  minusNum(e) {
    let index = e.currentTarget.dataset.index;
    let paramList = this.data.paramList;
    let goodsCount = parseInt(paramList[index].goodsCount);
    let selectCount = 0;
    let totalPrice = 0;
    if (goodsCount > 1) {
      goodsCount--;
    }
    var minusStatus = goodsCount <= 1 ? true : false;
    this.setData({
      ['paramList[' + index + '].goodsCount']: goodsCount,
      minusStatus: minusStatus,
    })
    this.data.paramList.forEach(item => {
      selectCount = item.checked ? parseInt(item.goodsCount) + selectCount : selectCount;
      totalPrice = item.checked ? totalPrice + item.price * parseInt(item.goodsCount) : totalPrice;
    })
    this.setData({
      totalPrice: totalPrice.toFixed(2),
      selectCount: selectCount
    })
  },

  inputGoodsCount(e) {
    let index = e.currentTarget.dataset.index;
    let selectCount = 0;
    let totalPrice = 0;

    this.setData({
      ['paramList[' + index + '].goodsCount']: e.detail.value,
    })
    this.data.paramList.forEach(item => {
      selectCount = item.checked ? parseInt(item.goodsCount) + selectCount : selectCount;
      totalPrice = item.checked ? totalPrice + item.price * parseInt(item.goodsCount) : totalPrice;
    })
    this.setData({
      totalPrice: totalPrice.toFixed(2),
      selectCount: selectCount,
    })
  },

  confirmGoodsCount(e) {
    let index = e.currentTarget.dataset.index;
    this.setData({
      ['paramList[' + index + '].goodsCount']: e.detail.value < 1 ? 1 : e.detail.value
    })
  },

  // 结算
  goSettlement: function () {
    let params = JSON.stringify(this.data.paramList);
    let from = this.data.hiddenstore ? 'takeaway' : 'store';
    let selectCount = this.data.selectCount;
    let totalPrice = this.data.totalPrice;
    let shopId = this.data.shopId;
    wx.navigateTo({
      url: '../order_info/order_info?params=' + params + '&selectCount=' + selectCount + '&totalPrice=' + totalPrice + '&shopId=' + shopId + '&from=' + from,
    })
  },

  //已选购商品删除
  goTrash: function () {

    let paramList = this.data.paramList;
    let arr = [];
    for (let i = paramList.length - 1; i >= 0; i--) {
      if (paramList[i].checked) {
        paramList.splice(i, 1);
      }

    }
    arr = arr.concat(paramList);
    let selectCount = 0;
    let totalPrice = 0;
    if (arr.length > 0) {
      arr.forEach(param => {
        param.checked = true;
        selectCount += parseInt(param.goodsCount);
        totalPrice += (parseInt(param.goodsCount) * param.price);
      })
    }
    this.setData({
      paramList: arr,
      selectCount: selectCount,
      totalPrice: totalPrice.toFixed(2),
      hiddenshop: arr.length > 0 ? false : true,
      shopkhiddden: arr.length > 0 ? false : true,
      temporary: {},
      isAllChecked: true
    });
    wx.removeStorageSync('paramList');
    wx.removeStorageSync('isAllChecked');
    wx.removeStorageSync('totalPrice');
    wx.removeStorageSync('selectCount');
  },

  // getCates() {
  //     let that = this;
  //     wx.request({
  //         url: `${config}/shopToGoods/getCates`,
  //         method: 'GET',
  //         data: {
  //             shopId: this.data.shopId,
  //         },
  //         header: {
  //             'Content-Type': 'application/json'
  //         },
  //         success(res) {
  //             that.setData({
  //                 tabList: res.data.data.list
  //             });
  //             that.getGoods();
  //         }
  //     });
  // },

  // getGoods() {
  //     let that = this;
  //     let cateId = this.data.tabList[`${this.data.currentTab}`].id;
  //     wx.request({
  //         url: `${config}/shopToGoods/getGoods`,
  //         method: 'GET',
  //         data: {
  //             shopId: this.data.shopId,
  //             cateId: cateId
  //         },
  //         header: {
  //             'Content-Type': 'application/json'
  //         },
  //         success(res) {
  //             that.setData({
  //                 goodsList: res.data.data.list
  //             });
  //         }
  //     });
  // },

  //   getShopInfo() {
  //     let that = this;
  //     wx.request({
  //       url: `${config}/shop/getShopInfo`,
  //       method: 'GET',
  //       data: {
  //         shopId: this.data.shopId,
  //       },
  //       header: {
  //         'Content-Type': 'application/json'
  //       },
  //       success(res) {
  //         let info = res.data.data.info;
  //         that.setData({
  //           shopName: info.name,
  //         })
  //         let lat = info.lat;
  //         let lng = info.lng;
  //         wx.getLocation({
  //           isHighAccuracy: true, // 开启地图精准定位
  //           type: 'gcj02',
  //           success(res) {
  //             const latitude = res.latitude;
  //             const longitude = res.longitude;
  //             let distance = that.getDistances(lat, lng, latitude, longitude);
  //             that.setData({
  //               distance: distance < 1 ? Number(distance * 1000).toFixed(2) + 'm' : distance.toFixed(2) + 'km'
  //             })
  //           },
  //         })

  //       }
  //     });
  //   },
  getCateGoodsByCode(equipmentId) {

    let that = this;
    wx.request({
      url: `${config}/shop/getGoodsByCode/${equipmentId}`,
      method: 'GET',
      data: {
        openid: wx.getStorageSync('openid')
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let info = res.data.data.shop;
        let list = res.data.data.list;
        let goodsList = list[that.data.currentTab].goodsList;
        let flag = that.isInTime(info.onStartTime, info.onEndTime)
        that.setData({
          shopId: info.id,
          shopName: info.name,
          tabList: list,
          goodsList: goodsList,
          startTime: info.onStartTime,
          endTime: info.onEndTime,
          isBusiness: flag ? 1 : 2,
        })
        wx.setStorageSync('selectColor', info.color);
        wx.setStorageSync('equipmentType', res.data.data.equipmentType);
        that.getCouponList();

        theme.setTheme(info.color);
        setTimeout(function () {
          that.setData({
            theme: wx.getStorageSync('systemTheme')
          })
        }, 300)
        that.getTemplateInfo(info.color);
        // let lat = info.lat;
        // let lng = info.lng;
        // wx.getLocation({
        //     isHighAccuracy: true, // 开启地图精准定位
        //     type: 'gcj02',
        //     success(res) {
        //         const latitude = res.latitude;
        //         const longitude = res.longitude;
        //         let distance = that.getDistances(lat, lng, latitude, longitude);
        //         that.setData({
        //             distance: distance < 1 ? Number(distance * 1000).toFixed(2) + 'm' : distance.toFixed(2) + 'km',
        //             tabList: list,
        //             goodsList: goodsList
        //         })
        //         that.getCouponList();
        //     },
        // })

      }
    });
  },
  getCateGoodsById(id) {
    let that = this;
    wx.request({
      url: `${config}/shop/getGoodsById/${id}`,
      method: 'GET',
      data: {
        openid: wx.getStorageSync('openid')
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let list = res.data.data.list;
        let goodsList = list[that.data.currentTab].goodsList;
        wx.setStorageSync('equipmentType', res.data.data.shop.equipmentType);
        that.setData({
          tabList: list,
          goodsList: goodsList
        })
        that.getCouponList();

      }
    });
  },
  isInTime(start, end) {
    var date = new Date();
    var hour = date.getHours();
    var minutes = date.getMinutes();
    var current = hour * 60 + minutes;
    var start = start.split(":");
    start = start[0] * 60 + +start[1];
    var end = end.split(":");
    end = end[0] * 60 + +end[1];
    return current >= start && current <= end;
  },
  async getAddressInfo() {
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
              url: `${config}/address/getListByOpenid`,
              method: 'GET',
              data: {
                openid: wx.getStorageSync('openid'),
              },
              header: {
                'Content-Type': 'application/json'
              },
              success(res) {
                let list = res.data.data.list;
                list.forEach(item => {
                  item.distance = that.getDistances(item.latitude, item.longitude, latitude, longitude);
                  // item.distance = distance < 1 ? Number(distance * 1000).toFixed(2) + 'm' : distance.toFixed(2) + 'km';
                });

                let newArr = list.sort((a, b) => a.distance - b.distance);
                let obj = newArr[0];
                obj.distance = obj.distance < 1 ? Number(obj.distance * 1000).toFixed(2) + 'm' : obj.distance.toFixed(2) + 'km';
                that.setData({
                  addressInfo: obj
                })

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
              url: `${config}/address/getListByOpenid`,
              method: 'GET',
              data: {
                openid: wx.getStorageSync('openid'),
              },
              header: {
                'Content-Type': 'application/json'
              },
              success(res) {
                let list = res.data.data.list;
                list.forEach(item => {
                  item.distance = that.getDistances(item.latitude, item.longitude, latitude, longitude);
                });

                let newArr = list.sort((a, b) => a.distance - b.distance);
                let obj = newArr[0];
                obj.distance = obj.distance < 1 ? Number(obj.distance * 1000).toFixed(2) + 'm' : obj.distance.toFixed(2) + 'km';
                that.setData({
                  addressInfo: obj
                })

              }
            });

          },

        })
      } catch (error) {
        wx.toast({ title: '您拒绝授权获取地址位置' })
      }
    }



  },
  checkLocationAuth(flag, id) {
    let that = this
    wx.getSetting({
      success: (res) => {
        let authSetting = res.authSetting
        if (authSetting['scope.userLocation']) {
          if (flag == 'list') {
            that.getShopInfo();
          } else {
            that.getShopId(id)
          }
          // that.getAddressInfo();
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
              // that.getAddressInfo();
              if (flag == 'list') {
                that.getShopInfo();
              } else {
                that.getShopId(id)
              }
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
                let flag = that.isInTime(newArr[0].onStartTime, newArr[0].onEndTime);
                let obj = newArr[0];
                let distance = obj.distance < 1 ? Number(obj.distance * 1000).toFixed(2) + 'm' : obj.distance.toFixed(2) + 'km';
                that.setData({
                  startTime: newArr[0].onStartTime,
                  endTime: newArr[0].onEndTime,
                  isBusiness: flag ? 1 : 2,
                  shopId: newArr[0].id,
                  shopName: newArr[0].name,
                  distance: distance
                })
                wx.setStorageSync('distance', newArr[0].distance);
                wx.setStorageSync('selectColor', newArr[0].color);
                wx.setStorageSync('shopId', newArr[0].id);
                that.getCateGoodsById(newArr[0].id);
                theme.setTheme(newArr[0].color);
                setTimeout(function () {
                  that.setData({
                    theme: wx.getStorageSync('systemTheme')
                  })
                }, 300)
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
                let flag = that.isInTime(newArr[0].onStartTime, newArr[0].onEndTime);
                let obj = newArr[0];
                let distance = obj.distance < 1 ? Number(obj.distance * 1000).toFixed(2) + 'm' : obj.distance.toFixed(2) + 'km';
                that.setData({
                  startTime: newArr[0].onStartTime,
                  endTime: newArr[0].onEndTime,
                  isBusiness: flag ? 1 : 2,
                  shopId: newArr[0].id,
                  shopName: newArr[0].name,
                  distance: distance
                })
                wx.setStorageSync('distance', newArr[0].distance);
                wx.setStorageSync('selectColor', newArr[0].color);
                wx.setStorageSync('shopId', newArr[0].id);
                that.getCateGoodsById(newArr[0].id);
                theme.setTheme(newArr[0].color);
                setTimeout(function () {
                  that.setData({
                    theme: wx.getStorageSync('systemTheme')
                  })
                }, 300)

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

  async getShopId(id) {

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
              url: `${config}/shop/getGoodsById/${id}`,
              method: 'GET',
              data: {
                openid: wx.getStorageSync('openid')
              },
              header: {
                'Content-Type': 'application/json'
              },
              success(res) {
                let shopInfo = res.data.data.shop;
                let list = res.data.data.list;
                let goodsList = [];
                if (list.length > 0) {
                  goodsList = list[that.data.currentTab].goodsList;
                }
                let distance = that.getDistances(shopInfo.lat, shopInfo.lng, latitude, longitude);
                distance = distance < 1 ? Number(distance * 1000).toFixed(2) + 'm' : distance.toFixed(2) + 'km';
                wx.setStorageSync('selectColor', shopInfo.color);
                wx.setStorageSync('equipmentType', shopInfo.equipmentType);
                let flag = that.isInTime(shopInfo.onStartTime, shopInfo.onEndTime)
                that.setData({
                  tabList: list,
                  goodsList: goodsList,
                  distance: distance,
                  startTime: shopInfo.onStartTime,
                  endTime: shopInfo.onEndTime,
                  isBusiness: flag ? 1 : 2,
                  shopId: id,
                  shopName: shopInfo.name,
                })
                wx.setStorageSync('distance', distance)
                that.getCouponList();
                theme.setTheme(shopInfo.color);
                setTimeout(function () {
                  that.setData({
                    theme: wx.getStorageSync('systemTheme')
                  })
                }, 300)
                that.getTemplateInfo(shopInfo.color);
              }
            });

          }
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
              url: `${config}/shop/getGoodsById/${id}`,
              method: 'GET',
              data: {
                openid: wx.getStorageSync('openid')
              },
              header: {
                'Content-Type': 'application/json'
              },
              success(res) {
                let shopInfo = res.data.data.shop;
                let list = res.data.data.list;
                let goodsList = [];
                if (list.length > 0) {
                  goodsList = list[that.data.currentTab].goodsList;
                }
                let distance = that.getDistances(shopInfo.lat, shopInfo.lng, latitude, longitude);
                distance = distance < 1 ? Number(distance * 1000).toFixed(2) + 'm' : distance.toFixed(2) + 'km';
                wx.setStorageSync('equipmentType', shopInfo.equipmentType);
                let flag = that.isInTime(shopInfo.onStartTime, shopInfo.onEndTime)
                that.setData({
                  tabList: list,
                  goodsList: goodsList,
                  distance: distance,
                  startTime: shopInfo.onStartTime,
                  endTime: shopInfo.onEndTime,
                  isBusiness: flag ? 1 : 2,
                  shopId: id,
                  shopName: shopInfo.name,
                })
                wx.setStorageSync('selectColor', shopInfo.color);
                wx.setStorageSync('distance', distance);
                that.getCouponList();
                theme.setTheme(shopInfo.color);
                setTimeout(function () {
                  that.setData({
                    theme: wx.getStorageSync('systemTheme')
                  })
                }, 300)
                that.getTemplateInfo(shopInfo.color);

              }
            });

          }
        })
      } catch (error) {
        wx.toast({ title: '您拒绝授权获取地址位置' })
      }
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
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    var that = this;
    // if(wx.getStorageSync('equipmentType')){
    //     wx.removeStorageSync('equipmentType');
    // }

    if (options.userId && options.shopId) {

      this.checkLocationAuth("info", options.shopId);
      this.setData({
        selectMap: true
      })
      wx.setStorageSync('shopId', options.shopId)
      wx.setStorageSync('selectMap', true)
      wx.setStorageSync('fuserId', options.userId)
    } else if (options.equipmentId) {
      wx.setStorageSync('equipmentId', options.equipmentId)
      this.setData({
        selectMap: false
      })
      this.getCateGoodsByCode(wx.getStorageSync('equipmentId'))
    } else {
      wx.removeStorageSync('shopId');
      wx.removeStorageSync('equipmentId');
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
      var head = wx.createSelectorQuery();
      var banner = wx.createSelectorQuery();
      // var coupon = wx.createSelectorQuery();
      head.select('.header').boundingClientRect();
      head.exec(function (rect) {
        if (rect[0] === null) return;
        that.setData({
          head: rect[0].height
        })
      });
      banner.select('.tab_title').boundingClientRect();
      banner.exec(function (rect) {
        if (rect[0] === null) return;
        that.setData({
          banner: rect[0].height
        })
      });
      // coupon.select('.couponk').boundingClientRect();
      // coupon.exec(function (rect) {
      //   if (rect[0] === null) return;
      //   that.setData({
      //     coupon: rect[0].height
      //   })
      // });
    }, 300)
    setTimeout(function () {
      var headHeight = that.data.head;
      var bannerHeight = that.data.banner;
      // var couponHeight = that.data.coupon;
      wx.getSystemInfo({
        success: function (res) {
          let scrollHeight = res.windowHeight;
          that.setData({
            scrollHeight: scrollHeight - headHeight - bannerHeight - 16.5,
            scrollHeights: scrollHeight - headHeight - bannerHeight - 61.5,
          })
        }
      })
    }, 1000)
  },

  //将canvas转换为图片保存到本地，然后将图片路径传给image图片的src
  createNewImg(url) {
    var that = this;
    var context = wx.createCanvasContext('mycanvas');
    context.setFillStyle("#ffffff")
    context.fillRect(0, 0, 300, 390)
    var path = app.globalData.skin.file + "/" + that.data.pictureInfo.canvas;
    wx.downloadFile({
      url: path,
      success(res) {
        if (res.statusCode === 200) {
          context.drawImage(res.tempFilePath, 27, 68.5, 246, 227.5);
          //标题
          context.setFontSize(14);
          context.setFillStyle('#333333');
          context.setTextAlign('left');
          context.fillText("微信好友", 27, 24);
          context.stroke();
          context.setFontSize(12);
          context.setFillStyle('#666666');
          context.setTextAlign('left');
          context.fillText("送你“新人专享福利”8元优惠券", 27, 48);
          context.stroke();
          //绘制验证码背景
          // context.drawImage(path3, 170, 275, 75, 75);
          context.drawImage(url, 27, 316.5, 50, 50);
          //内容
          context.setFontSize(12);
          context.setFillStyle('#666666');
          context.setTextAlign('left');
          context.fillText("盈海优品", 87, 334);
          context.stroke();
          context.setFontSize(12);
          context.setFillStyle('#666666');
          context.setTextAlign('left');
          context.fillText("长按识别小程序码帮好友助力", 87, 355.5);
          context.stroke();

          context.draw();
          //将生成好的图片保存到本地，需要延迟一会，绘制期间耗时
          setTimeout(function () {
            wx.canvasToTempFilePath({
              canvasId: 'mycanvas',
              success: function (res) {
                wx.hideToast()
                that.setData({
                  maskHidden: true
                });
                var tempFilePath = res.tempFilePath;
                that.setData({
                  imagePath: tempFilePath,
                  canvasHidden: true
                });
              },
              fail: function (res) {
                wx.hideToast()

              }
            });
          }, 200);
        }
      }
    })

  },
  //点击保存到相册
  baocun: function () {
    var that = this
    wx.saveImageToPhotosAlbum({
      filePath: that.data.imagePath,
      success(res) {
        wx.showModal({
          content: '图片已保存到相册，赶紧晒一下吧~',
          showCancel: false,
          confirmText: '好的',
          confirmColor: '#333',
          success: function (res) {
            if (res.confirm) {
              console.log('用户点击确定');
              /* 该隐藏的隐藏 */
              that.setData({
                maskHidden: false
              })
            }
          }, fail: function (res) {

          }
        })
      }
    })
  },
  goGb: function () {
    this.setData({
      maskHidden: false
    })
  },
  //点击生成
  formSubmit: function (e) {
    var that = this;
    if (!wx.getStorageSync('phone')) {
      wx.navigateTo({
        url: '../login/login?from=food',
      })
    } else {
      this.setData({
        maskHidden: false
      });
      wx.showToast({
        title: '生成中...',
        icon: 'loading',
      });
      wx.request({
        url: `${config}/users/distribution`,
        method: 'GET',
        data: {
          openid: wx.getStorageSync('openid'),
          shopId: this.data.shopId
        },
        header: {
          'Content-Type': 'application/json'
        },
        success(res) {
          let url = config + '/image/' + res.data.data.url;
          wx.downloadFile({
            url: url,
            success(res) {
              if (res.statusCode === 200) {
                that.createNewImg(res.tempFilePath);
              }
            }
          })

        }
      });
    }

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
    let that = this;
    wx.request({
      url: `${config}/users/login`,
      method: 'GET',
      data: {
        openid
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        wx.setStorageSync("openid", openid);
      }
    });
  },
  showData() {
    // wx.showLoading({
    //     title: '获取中......',
    // })
    this.setData({
      couponShow: true
    })
    let that = this;

    if (wx.getStorageSync('orderInfo')) {
      let info = JSON.parse(wx.getStorageSync('orderInfo'));
      wx.removeStorageSync('orderInfo')
      this.getShopId(info.shopId);

      let arr = [];
      info.params.forEach(item => {
        let obj = {};
        obj.checked = true;
        obj.curSelect = item.specifications;
        obj.goodsCount = item.number;
        obj.goodsId = item.goodsId;
        obj.price = item.price;
        obj.url = item.url;
        obj.name = item.name;
        obj.paramIds = item.paramIds;
        arr.push(obj)
      });
      this.setData({
        isAllChecked: true,
        paramList: arr,
        shopName: info.shopName,
        selectCount: info.totalCount,
        totalPrice: info.totalPrice
      })


    } else if (this.data.fromSelect) {
      let temporary = this.data.temporary;
      let paramList = this.data.paramList;

      let aaa = paramList.find(item => item.goodsId == temporary.goodsId && item.curSelect == temporary.curSelect);
      let bbb = paramList.find(item => item.goodsCount == temporary.goodsCount);
      if (aaa == undefined) {
        let params = paramList.concat(temporary).filter(item => item.goodsId != null);
        this.setData({
          paramList: params
        })
      } else {
        if (bbb == undefined) {
          let curIndex = paramList.findIndex(item => item.goodsId == temporary.goodsId && item.curSelect == temporary.curSelect);
          this.setData({
            ['paramList[' + curIndex + '].goodsCount']: temporary.goodsCount,

          })
        }
      }
      let selectCount = 0;
      let totalPrice = 0;
      let checkedCount = 0;
      this.data.paramList.forEach(item => {
        checkedCount = item.checked ? checkedCount + 1 : checkedCount;
        selectCount += parseInt(item.goodsCount);
        totalPrice += item.price * parseInt(item.goodsCount);
      })
      this.setData({
        isAllChecked: this.data.paramList.length == checkedCount ? true : false,
        totalPrice: totalPrice.toFixed(2),
        selectCount: selectCount,
      });

    } else if (wx.getStorageSync('paramList') && wx.getStorageSync('isAllChecked') && wx.getStorageSync('totalPrice') && wx.getStorageSync('selectCount')) {
      this.setData({
        paramList: wx.getStorageSync('paramList'),
        isAllChecked: wx.getStorageSync('isAllChecked'),
        totalPrice: wx.getStorageSync('totalPrice'),
        selectCount: wx.getStorageSync('selectCount')
      })
    } else {
      this.setData({
        paramList: [],
        isAllChecked: false,
        totalPrice: 0,
        selectCount: 0
      })
    }


    if (this.data.paramList.length > 1) {
      wx.setStorageSync('paramList', this.data.paramList);
      wx.setStorageSync('isAllChecked', this.data.isAllChecked);
      wx.setStorageSync('totalPrice', this.data.totalPrice);
      wx.setStorageSync('selectCount', this.data.selectCount);
      this.setData({
        paramList: this.data.paramList.reverse()
      });

    }
    // wx.hideLoading()
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
    if (wx.getStorageSync('positioning')) {
      this.setData({
        addressInfo: JSON.parse(wx.getStorageSync('positioning'))
      })
    } else {
      this.setData({
        addressInfo: {}
      })
    }

    if (wx.getStorageSync('shopId')) {
      this.checkLocationAuth("info", wx.getStorageSync('shopId'));
      this.setData({
        selectMap: true
      })
      wx.setStorageSync('selectMap', true)
    } else if (!wx.getStorageSync('equipmentId') && !this.data.shopId) {
      this.checkLocationAuth("list");
      this.setData({
        selectMap: true
      })
      wx.setStorageSync('selectMap', true)
    }
    if (wx.getStorageSync('takeType')) {
      this.setData({
        hiddenstore: wx.getStorageSync('takeType') == 1 ? true : false
      })
      wx.removeStorageSync('takeType')
    }
    this.showData();

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
    return {
      title: "这个是我分享出来的东西",
      success: function (res) {
        console.log(res, "转发成功")
      },
      fail: function (res) {
        console.log(res, "转发失败")
      }
    }
  }
})