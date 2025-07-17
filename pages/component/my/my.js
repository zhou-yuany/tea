
// 获取应用实例
const app = getApp()
const context = require("../../../utils/context")
const api = require("../../../utils/api")
Component({
  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    user: {},
    shows: false, //分享
    activity: true,  //是否有活动
    showrules: false  //分销规则
    ,activityInfo: {}
    ,shareUrl: ""
  },

  /**
   * 组件的方法列表
   */
  methods: {
    componentInnerFunction: function(){
      this.setData({user: context.getUser()})
    },
    saveImage: function(e){
      wx.getImageInfo({
        src: this.data.shareUrl,
        success (res) {
          wx.saveImageToPhotosAlbum({
            filePath: res.path,
            success(res) { 
              wx.showToast({
                  title: '已保存到相册',
                  icon: 'none',
                  duration: 1000
              })
            },
            fail(res){
              console.log(res);
            }
          })
        }
      })
    },
    logout: function(){
      wx.clearStorageSync();
      wx.redirectTo({
        url: '/pages/index/index'
      })
    },

    goPhone:function(){
      wx.navigateTo({
        url: '/pages/old_phone/old_phone',
      })
    },

    goEnjoy:function(){
      api.get(`/user/currentActivity`).then(res=>{
        this.setData({shows: true, activityInfo: res, activity: Boolean(res), shareUrl: `${app.globalData.urlPrefix}/wechat/share/qrcode/${context.getUserId()}?time=${new Date().getTime()}`});
      });
    },
    //关闭分享弹窗
    goClosed: function(){
      this.setData({
        shows : false
      })
    },

    //规则
    goRules:function(){
      this.setData({
        showrules: true
      })
    },
    goCloseds:function(){
      this.setData({
        showrules: false
      })
    }
  }
})