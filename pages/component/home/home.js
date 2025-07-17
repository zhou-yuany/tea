// 获取应用实例
const app = getApp()
const util = require('../../../utils/util')
const api = require("../../../utils/api")
const context = require("../../../utils/context")
const validate = require("../../../utils/validate")
let animationShowHeight = 42;
let animationShowHeights = -42;
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    admin: Number,
    logo: String
  },

  /**
   * 组件的初始数据
   */
  data: {
    login: false,
    admin: context.getUser().type, //判断角色 1客户 2合伙人
    logo: undefined,
    defaultStartDate: util.formatDate(new Date()),
    urlPrefix: app.globalData.urlPrefix,
    start: wx.getStorageSync('start') || ['辽宁省', '大连市', '中山区'],
    dest: ['上海市', '上海市', '市辖区'],
    startTime: util.formatDate(new Date()), //托运日期
    cars: [],
    countsCar: 0, //数量
    needJc: 2, //上门提车 1接车 2不接车
    remarks: '',
    detail: '',
    lon: 0,
    lat: 0,
    directionCorrect: true,
    startAnimation: '',
    destAnimation: '',
    cartype: '请选择',
    s_move: true,
    shows: false, //分享
    activity: true, //是否有活动
    showrules: false //分销规则
      ,
    activityInfo: {},
    shareUrl: "",
    current: 0,
    autoplay: true,
    // 按钮是否可用
    buttonDisabled: false,
    // 上一次点击时间
    lastClickTime: 0
  },


  /**
   * 组件的方法列表
   */
  methods: {
    //监听轮播图的下标
    monitorCurrent: function (e) {
      let current = e.detail.current;
      this.setData({
        current: current
      })
    },

    cancelLogin(){
      this.setData({
        login: false
      })
    },
    //省市联动
    bindRegionChangeStart: function (e) {
      this.setData({
        start: e.detail.value
      })
    },
    bindRegionChangeDest: function (e) {
      this.setData({
        dest: e.detail.value
      })
    },

    //始发地和目的地换位
    goChange: function () {
      var startAnimation = wx.createAnimation({
        transformOrigin: "50% 50%",
        duration: 1000,
        timingFunction: "ease",
        delay: 0
      })


      var destAnimation = wx.createAnimation({
        transformOrigin: "50% 50%",
        duration: 1000,
        timingFunction: "ease",
        delay: 0
      })
      let directionCorrect = !this.data.directionCorrect
      if (this.data.directionCorrect) {
        startAnimation.translateY(animationShowHeight).step()
        destAnimation.translateY(animationShowHeights).step()
      } else {
        startAnimation.translateY(0).step()
        destAnimation.translateY(0).step()
      }

      this.setData({
        startAnimation: startAnimation.export(),
        destAnimation: destAnimation.export()
      })

      this.setData({
        directionCorrect: directionCorrect
      })
    },

    //日期选择
    bindDateChange: function (e) {
      this.setData({
        startTime: e.detail.value
      })
    },

    //选择托运车辆
    goChoosecar: function () {
      let this_ = this
      wx.navigateTo({
        url: '/pages/vehicle/vehicle',
        events: {
          setData: function (data) {
            let countsCar = 0;
            data.forEach(d => {
              countsCar += d.count
            })
            this_.setData({
              cars: data,
              countsCar: countsCar
            })
          }
        },
        success: function (res) {
          res.eventChannel.emit("getData", this_.data.cars)
        }
      })
    },

    //上门提车
    goCrv: function () {
      let start = this.data.directionCorrect ? this.data.start : this.data.dest
      let this_ = this;
      wx.navigateTo({
        url: `/pages/crv/crv?needJc=${this.data.needJc}&start=${start[0]+start[1]+start[2]}&detail=${this.data.detail}&lat=${this.data.lat}&lon=${this.data.lon}`,
        success: function (res) {
          // 通过eventChannel向被打开页面传送数据
          res.eventChannel.on("setData", function (data) {
            this_.setData(data)
          })
        }
      })
    },
    // 设置备注
    bindRemarks: function (e) {
      this.setData({
        remarks: e.detail.value
      })
    },
    //发起询价
    goEnquire: function () {

    const now = new Date().getTime();
    const { lastClickTime, buttonDisabled } = this.data;
    const CLICK_INTERVAL = 2000; // 设定点击间隔为2秒

    // 如果按钮已禁用或上次点击时间距离现在小于间隔，则忽略本次点击

    if (buttonDisabled || (now - lastClickTime < CLICK_INTERVAL)) {
      return;
    }
     // 更新按钮状态为禁用
     this.setData({
      lastClickTime: now
    });

    let that = this;
      if(wx.getStorageSync('customerId')){
        // 更新按钮状态为禁用
        this.setData({
          buttonDisabled: true
        });
        if (this.data.countsCar < 1) {
          wx.showToast({
            title: '请选择托运车辆',
            icon: 'error',
            duration: 1000
          })
          return;
        }
        if (!validate.submit(this.data, [
            // {required: true, key: 'countsCar', name: '托运车辆', type: 'number', min: 1}
            , {
              required: false,
              key: 'remarks',
              name: '备注',
              max: 50
            }
          ])) {
          return;
        }
        //空格或者全部是空格
        var regu = "^[ ]+$"
        var re = new RegExp(regu)
        if (re.test(this.data.remarks)) {
          wx.showToast({
            title: '请输入有效的备注',
            icon: 'none',
            duration: 2000
          })
          return
        }
        let start = this.data.directionCorrect ? this.data.start : this.data.dest
        let dest = !this.data.directionCorrect ? this.data.start : this.data.dest
        wx.setStorageSync('start', start)
        api.post("/order/insert", {
          startTime: this.data.startTime,
          startProv: start[0],
          startCity: start[1],
          statrtArea: start[2],
          detail: this.data.detail,
          needJc: this.data.needJc,
          destProv: dest[0],
          destCity: dest[1],
          destArea: dest[2],
          countsCar: this.data.countsCar,
          remarks: this.data.remarks,
          startLon: this.data.lon,
          startLat: this.data.lat,
          orderCars: this.data.cars
        }).then(orderId => {
          that.setData({
            buttonDisabled: false,
            // 可以选择重置上次点击时间，或者保持当前时间以便继续计算间隔
            // lastClickTime: 0 // 如果需要重置时间，可以取消注释这行
          });
          wx.showModal({
            title: '提示',
            content: '您已成功发起询价请耐心等待客服报价',
            success: function (res) {
              if (res.confirm) {
                wx.navigateTo({
                  url: '/pages/wait/wait?orderId=' + orderId
                })
              }
            }
          })
  
        })
      }else{
        this.initLogin()
      }
      
    },
    getPhoneNumber (e) {
      let this_ = this
      api.post(`/wechat/customer/login`, {
        encryptedData: e.detail.encryptedData
        , iv: e.detail.iv
        , code: app.globalData.openId
        // , nickName: this.data.userInfo.nickName
        // , avatarUrl: this.data.userInfo.avatarUrl
        // , gender: this.data.userInfo.gender
        , sharerId: this.data.sharerId
      }).then(res=>{
        context.setUser(res);
        context.setUserId(res.id)
        wx.setStorageSync('customerInfo', res);
        wx.setStorageSync('customer-phone', res.phone);
        wx.setStorageSync('customerId', res.id)
        app.globalData.user = res;
        this_.setData({login: false, admin: res.type, logo: res.logo})
      })
    },
    initLogin: function(){
      let this_ = this
      // 登录
      wx.login({
        success: res => {
          if(res.code){
            app.globalData.openId = res.code
            const userId = wx.getStorageSync('customerId')
            if(userId){
              // 跳转首页
              context.setUserId(userId)
              api.get(`/wechat/customer`).then(res=>{
                context.setUser(res);
                this_.setData({admin: res.type, logo: res.logo})
                if(res.phone != wx.getStorageSync('customer-phone')){
                  this_.setData({login: true})
                  wx.navigateTo({
                    url: '/pages/tips/tips',
                  })
                }
              })
            }else{
              this_.setData({login: true})
            }
          }
        }
      })
    },

    // 客服
    show_move: function () {
      let s_move = this.data.s_move;
      this.setData({
        s_move: !s_move
      })
      let this_ = this;
      setTimeout(() => {
        this_.setData({
          s_move: s_move
        })
      }, 3000)
    },

    //联系客服
    freeTell: function () {
      wx.makePhoneCall({
        phoneNumber: '18525469418',
      })
    },

    //保存图片
    saveImage: function (e) {
      wx.getImageInfo({
        src: this.data.shareUrl,
        success(res) {
          wx.saveImageToPhotosAlbum({
            filePath: res.path,
            success(res) {
              wx.showToast({
                title: '已保存到相册',
                icon: 'none',
                duration: 1000
              })
            },
            fail(res) {
              console.log(res);
            }
          })
        }
      })
    },

    goEnjoy: function () {
      let userId = wx.getStorageSync('customerId') ? context.getUserId() : -1;
      api.get(`/user/currentActivity`).then(res => {
        this.setData({
          shows: true,
          activityInfo: res,
          activity: Boolean(res),
          shareUrl: `${app.globalData.urlPrefix}/wechat/share/qrcode/${userId}?time=${new Date().getTime()}`
        });
      });
    
    },
    //关闭分享弹窗
    goClosed: function () {
      this.setData({
        shows: false
      })
    },

    //规则
    goRules: function () {
      this.setData({
        showrules: true
      })
    },
    goCloseds: function () {
      this.setData({
        showrules: false
      })
    }
  }
})