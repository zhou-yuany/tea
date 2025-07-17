// pages/login/login.js
// 获取应用实例
const app = getApp();
const theme = require("../../utils/themeSystem")
const {
    config
} = require('../../utils/config')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        config:config,
        pictureInfo: wx.getStorageSync('pictureInfo') ? JSON.parse(wx.getStorageSync('pictureInfo')) : '',
        skin: getApp().globalData.skin,
        theme:wx.getStorageSync('systemTheme'),
        hidden: true,
        phone: '',
        isShowphone: true,
        from: ''
    },

    showPhone: function () {
        this.setData({
            isShowphone: false
        })
    },
    getPhoneNumber() {

    },

    goCancel() {
        // wx.switchTab({
        //   url: '../food/food',
        // })
        let that = this;
        const from = that.data.from;
        wx.switchTab({
            url: '../' + from + '/' + from,
        })
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
                        wx.setStorageSync("openid", resObj.openid);
                        // that.saveInfo(resObj.openid);
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
    getPhoneNumber(e) {
        if (e.detail.errMsg == "getPhoneNumber:ok") {
            const from = this.data.from;
            wx.request({
                url: `${config}/users/getPhone`,
                method: 'GET',
                data: {
                    code: e.detail.code,
                    openid: wx.getStorageSync('openid'),
                    userId:  wx.getStorageSync('fuserId') ? wx.getStorageSync('fuserId') : 0
                },
                header: {
                    'Content-Type': 'application/json'
                },
                success(res) {
                    let response = res.data.data.phone_info;
                    wx.setStorageSync('info', JSON.stringify(res.data.data.info));
                    wx.setStorageSync('phone', response.phone_info.phoneNumber);
                    wx.setStorageSync('flag', res.data.data.flag)
                    wx.setStorageSync('flagId', res.data.data.flagId)
                    wx.switchTab({
                        url: '../' + from + '/' + from,
                    })
                }
            });
        }

    },

    submitPhone() {
        var myreg = /^(14[0-9]|13[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$$/;
        if (!myreg.test(this.data.phone)) {
            wx.showToast({
                title: '请输入正确的手机号',
                icon: 'none',
                duration: 1000
            })
            return false;
        }
        let that = this;
        const from = that.data.from;
        wx.request({
            url: `${config}/users/submitPhone`,
            header: {
                "Content-Type": "application/json"
            },
            method: 'GET',
            data: {
                openid: wx.getStorageSync('openid'),
                phone: that.data.phone,
            },
            success: function (res) {
                wx.setStorageSync('phone', that.data.phone)
                wx.setStorageSync('flag', res.data.data.flag)
                wx.setStorageSync('flagId', res.data.data.flagId)
                wx.switchTab({
                    url: '../' + from + '/' + from,
                })

            }
        });
    },

    returnHandler() {
        wx.navigateBack({
            delta: -1,
        })
    },

    getPhone: function (e) {
        this.setData({
            phone: e.detail.value
        })
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        if (options.from) {
            this.setData({
                from: options.from
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
        if (!wx.getStorageSync('openid')) {
            this.loginIn();
        } else if (wx.getStorageSync('phone') && wx.getStorageSync('phone') != null) {
            wx.switchTab({
                url: 'pages/food/food',
            })
        }
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