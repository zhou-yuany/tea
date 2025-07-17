// pages/add_address/add_address.js
const app = getApp()
const theme = require("../../utils/themeSystem")
const {
  config
} = require('../../utils/config.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    theme:wx.getStorageSync('systemTheme'),
    id: '',
    username: '',
    sex: 1,
    phone: '',
    roomNumber: '',
    address: '',
    latitude: '',
    longitude: '',
    isChecked: false,
    region: ['请选择省', '市', '区'],
    customItem: '请选择',
    area: '',
    is_default: false,
    tab: '',
    items: [{
      name: '男士',
      value: 1,
      checked: "true"
    }, {
      name: '女士',
      value: 2,
    }],
    tipList: ['家', '公司', '学校', '朋友', '其他'],
    currentTab: 0
  },
  inputName(e) {
    this.setData({
      username: e.detail.value
    })
  },
  inputPhone(e) {
    this.setData({
      phone: e.detail.value
    })
  },
  inputRoomNumber(e) {
    this.setData({
      roomNumber: e.detail.value
    })
  },
  radioChange: function (e) {
    this.setData({
      sex: e.detail.value
    })
  },

  typeChange(e) {
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      this.setData({
        currentTab: e.target.dataset.current
      })
    }
  },

  // 地址
  onAddress: function () {
    let that = this;
    wx.chooseLocation({
      success: (res) => {
        that.setData({
          latitude: res.latitude,
          longitude: res.longitude,
          address: res.name
        })
      }
    })
  },

  //省市区选择器：
  bindRegionChange: function (e) {
    this.setData({
      region: e.detail.value,
      area: e.detail.value[0] + ',' + e.detail.value[1] + ',' + e.detail.value[2]
    })
  },

  changeSwitch: function () {
    if (this.data.is_default) {
      this.setData({
        is_default: false,
      })
    } else {
      this.setData({
        is_default: true,
      })
    }
  },

  formSubmit: function (e) {

    let obj = e.detail.value
    let that = this;
    let label = that.data.tipList[that.data.currentTab];

    if (obj.username == '' || obj.username.trim() == '') {
      wx.showToast({
        title: '收货人不可为空',
        icon: 'none',
        duration: 2000
      })
      return false;
    }

    let myreg = /^(1\d{10})$/;
    if (!(myreg.test(obj.phone))) {
      wx.showToast({
        title: '手机号格式不对',
        icon: 'none',
        duration: 2000
      })
      return false;
    }
    if (that.data.address == '') {
      wx.showToast({
        title: '请选择地址',
        icon: 'none',
        duration: 2000
      })
      return false;
    }
    if (obj.roomNumber == '' || obj.roomNumber.trim() == '') {
      wx.showToast({
        title: '门牌号不可为空',
        icon: 'none',
        duration: 2000
      })
      return false;
    }
    wx.request({
      url: `${config}/address/updateAddress`,
      header: {
        "Content-Type": "application/json"
      },
      method: 'POST',
      data: {
        openid: wx.getStorageSync('openid'),
        id: that.data.id,
        username: obj.username,
        phone: obj.phone,
        address: that.data.address,
        latitude: that.data.latitude,
        longitude: that.data.longitude,
        roomNumber: obj.roomNumber,
        sex: that.data.sex,
        label: label
      },
      success: function (res) {
        const pages = getCurrentPages()
        const prevPage = pages[pages.length - 2]
        prevPage.setData({
          tab: that.data.tab
        })
        wx.navigateBack({
          delta: 1
        })
      }
    });

  },
  getInfo(id) {
    let that = this;
    wx.request({
      url: `${config}/address/getInfo/${id}`,
      header: {
        "Content-Type": "application/json"
      },
      method: 'GET',
      data: {
        openid: wx.getStorageSync('openid'),

      },
      success: function (res) {
        let obj = res.data.data.info;
        let currentTab = that.data.tipList.findIndex(item => item == obj.label);
        let items = that.data.items;
        if(obj.sex == 2){
          items[0].checked = false;
          items[1].checked = true;
        }else{
          items[1].checked = false;
          items[0].checked = true;
        }
        that.setData({
          items,
          currentTab: currentTab,
          username: obj.username,
          phone: obj.phone,
          address: obj.address,
          latitude: obj.latitude,
          longitude: obj.longitude,
          roomNumber: obj.roomNumber,
          sex: obj.sex,
          label: obj.label
        })
      }
    });
  },
  delete: function (e) {
    let that = this
    wx.showModal({
        title: '删除',
        content: '确认删除该地址吗？！',
        success(res) {
            if (res.confirm) {
                wx.request({
                    url: `${config}/address/deleteData/${that.data.id}`,
                    header: {
                        "Content-Type": "application/json"
                    },
                    method: 'POST',
                    data: {},
                    success: function (res) {
                        wx.hideLoading();
                            wx.showToast({
                                title: '删除成功！',
                                icon: 'success',
                                duration: 1500,
                                success: function () {
                                  if(wx.getStorageSync('positioning')){
                                    let id =  JSON.parse(wx.getStorageSync('positioning')).id;
                                    if(that.data.id == id){
                                      wx.removeStorageSync('positioning')
                                    }
                                  }
                                    
                                    setTimeout(function () {
                                        wx.navigateBack({
                                            delta: 1
                                        })
                                    }, 1500) //延迟时间

                                }
                            })
                    }
                });
            } else if (res.cancel) {
                console.log('用户点击取消')
            }
        }
    })


},
changeSwitch: function () {
    if (this.data.is_default) {
        this.setData({
            is_default: false
        })
    } else {
        this.setData({
            is_default: true,
        })
    }
},
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (options.tab) {
      this.setData({
        tab: options.tab
      })
    }
    if (options.id) {
      this.setData({
        id: options.id
      })
      this.getInfo(options.id);
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