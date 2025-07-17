// pages/personal/personal.js
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
    pictureInfo: wx.getStorageSync('pictureInfo') ? JSON.parse(wx.getStorageSync('pictureInfo')) : '',
    skin: getApp().globalData.skin,
    theme:wx.getStorageSync('systemTheme'),
    config: config,
    name: '',
    phone: '',
    avatar: '',
    sex: '请选择',
    date: '请选择',
    career: '请选择',
    objectArray: [
      {
        id: 1,
        name: '男'
      },
      {
        id: 2,
        name: '女'
      }
    ],
    objectIndex: 0,
    objectArraya: [{
      id: 1,
      name: '自由职业者'
    },{
      id: 2,
      name: '技术类职业'
    },{
      id: 3,
      name: '医疗健康类职业'
    },{
      id: 4,
      name: '商业管理类职业'
    },{
      id: 5,
      name: '艺术创作类职业'
    },{
      id: 6,
      name: '公共服务类职业'
    },{
      id: 7,
      name: '科学研究类职业'
    },{
      id: 8,
      name: '在校学生'
    },{
      id: 9,
      name: '无业待业'
    }],
    objectIndexa: 0,
    hobby: false,
    styleList: [{
      url: 'style1.png',
      name: '港漫风',
      type: "hongkong"
    },{
      url: 'style2.png',
      name: '国画风',
      type: "claborate"
    },{
      url: 'style3.png',
      name: '漫画风',
      type: "comic"
    },{
      url: 'style4.png',
      name: '动画3D',
      type: "animation3d"
    },{
      url: 'style5.png',
      name: '手绘风',
      type: "handdrawn"
    },{
      url: 'style6.png',
      name: '艺术特效风',
      type: "artstyle"
    },{
      url: 'style7.png',
      name: '3D特效风',
      type: "3d"
    },{
      url: 'style8.png',
      name: '日漫风',
      type: "anime"
    },{
      url: 'style9.png',
      name: '铅笔画',
      type: "sketch"
    }],
    hidestyle: true,
    curSelect: 'hongkong',
    currentTaba: 0
  },

  goSheng:function(){
    this.setData({
      hidestyle: false
    })
  },
  goCancel:function(){
    this.setData({
      hidestyle: true
    })
  },
  selectImg(e){
    if (this.data.currentTaba == e.currentTarget.dataset.currenta){
      return;
    }

    let curSelect = e.currentTarget.dataset.type;
    this.setData({
      curSelect: curSelect,
      currentTaba: e.currentTarget.dataset.currenta
    })
  },

  onChooseAvatar(e) {
    this.uploadAvatar(e.detail.avatarUrl)
  },
  // 生成头像
  generateAvatar() {
    var that = this;
    if(this.data.curSelect == ''){
      wx.showToast({
        title: '选择风格',
        icon: 'none',
        duration: 1500
      })
      return false;
    }
    that.setData({
      hidestyle: true
    })
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      camera: 'front',
      sizeType: ['original'],
      success(res) {
        let src = res.tempFiles[0].tempFilePath;
        wx.showLoading({
          title: '生成卡通头像中...'
        })
        wx.uploadFile({
          url: 'https://doc.yinghai-tec.com/api/column/avatar',
          method: 'POST',
          filePath: src,
          formData: {
            "type": that.data.curSelect,
            openid: wx.getStorageSync('openid')
          },
          header: {
            'content-type': 'multipart/form-data'
          },
          name: 'ref',
          success: function (res) {
            wx.hideLoading();
            if(JSON.parse(res.data).code == 400){
              let message = JSON.parse(res.data).data.data.Message;
              let ind = message.indexOf('-');
              var newM = JSON.parse(res.data).data.data.Message;
              if(ind > -1){
                newM = message.slice(0, ind);
              }
              wx.showModal({
                title: '提示',
                content: newM,
              })
            }else{
              let url = JSON.parse(res.data).data.Data.ImageURL;
            wx.downloadFile({
              url: url,
              success: function (res){
                that.uploadAvatar(res.tempFilePath)
              }
            })          
            }
          },
          fail: function (e) {
            wx.hideLoading();

            console.log('接口调用失败')
          }
        })
      }
    })
    
  },
  // 上传头像
  uploadAvatar(src) {
    var that = this;
    wx.showLoading({
      title: '上传中...'
    })
    wx.uploadFile({
      url: `${config}/users/uploadAvatar`,
      method: 'POST',
      filePath: src,
      header: {
        'content-type': 'multipart/form-data'
      },
      name: 'avatar',
      formData: {
        avatar: src
      },
      success: function (res) {
        wx.hideLoading();
        that.setData({
          avatar: JSON.parse(res.data).data.url,
        })

      },
      fail: function (e) {
        wx.hideLoading();
        console.log('接口调用失败')
      }
    })
  },
  getNickname(e){
    this.setData({
      name: e.detail.value
    })

  },
  inputName(e) {
    this.setData({
      name: e.detail.value.trim()
    })

  },
  //生日日期选择器：
  bindDateChange: function (e) {
    this.setData({
      date: e.detail.value
    })

  },

  // 性别
  bindPickerChange: function (e) {
    this.setData({
      objectIndex: e.detail.value,
      sex: this.data.objectArray[e.detail.value].name
    })

  },

  // 职业
  bindPickerChangea: function (e) {
    this.setData({
      objectIndexa: e.detail.value,
      career: this.data.objectArraya[e.detail.value].name
    })

  },

  // 爱好
  goHobby:function(){
    wx.navigateTo({
      url: '../hobby/hobby',
    })
  },

  saveInfo() {
    let that = this;
    const {avatar, name, date, career, sex, objectIndex} = this.data;
    if(avatar == ''){
      wx.showToast({
        title: '请上传头像',
        icon: 'none',
        duration: 1000
      })
      return false;
    }
    if(name == ''){
      wx.showToast({
        title: '请输入昵称',
        icon: 'none',
        duration: 1000
      })
      return false;
    }
    if(sex == '请选择'){
      wx.showToast({
        title: '请选择性别',
        icon: 'none',
        duration: 1000
      })
      return false;
    }
    if(date == '请选择'){
      wx.showToast({
        title: '请选择生日',
        icon: 'none',
        duration: 1000
      })
      return false;
    }
    let info = {};
    info.openid = wx.getStorageSync('openid');
    info.username = name;
    info.brithday = date;
    info.avatar = avatar;
    info.career = career;
    info.sex = sex == '男' ? 1 : 2;
    wx.request({
      url: `${config}/users/saveInfo`,
      method: 'POST',
      data: info,
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {

        wx.navigateTo({
          url: '../hobbyresult/hobbyresult',
        })
        // wx.showToast({
        //   title: '保存成功',
        //   icon: 'none',
        //   duration: 1200
        // })
        // setTimeout(function () {
        //   wx.switchTab({
        //     url: '../my/my',
        //   })
        // }, 1200)
      }
    });
  },

  getInfo() {
    let that = this;
    wx.request({
      url: `${config}/users/getInfo`,
      method: 'GET',
      data: {
        openid: wx.getStorageSync('openid')
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let info = res.data.data.info;
        that.setData({
          name: info.username ? info.username : '微信用户',
          phone: info.phone,
          sex: info.sex == 1 ? '男' : info.sex == 2 ? '女' : '请选择',
          objectIndex: info.sex ? info.sex : 0,
          avatar: info.avatar ? info.avatar : '',
          career: info.career ? info.career : '请选择',
          date: info.brithday ? info.brithday : '请选择',
          hobby: res.data.data.hobbyUser ? true : false
        })
      }
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getInfo();
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
    if(wx.getStorageSync('hobby')){
      this.setData({
        hobby: wx.getStorageSync('hobby')
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