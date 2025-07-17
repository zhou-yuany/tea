// pages/hobby/hobby.js
const app = getApp()
const {
  config
} = require('../../utils/config')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    theme:wx.getStorageSync('systemTheme'),
    config: config,
    hobbyList: [{
      title: '兴趣爱好',
      subtitle: '您平时的兴趣爱好主要包括哪些方面？',
      Data: [{
        isCheck:false,
        name: '读书'
      },{
        isCheck:false,
        name: '看电影'
      },{
        isCheck:false,
        name: '玩游戏'
      },{
        isCheck:false,
        name: '运动健身'
      },{
        isCheck:false,
        name: '旅行'
      },{
        isCheck:false,
        name: '其他（请说明）'
      }],
      other: ''
    },{
      title: '娱乐活动',
      subtitle: '您在业余时间最喜欢参与的娱乐活动是什么？',
      Data: [{
        isCheck:false,
        name: '看电视剧/电影'
      },{
        isCheck:false,
        name: '听音乐'
      },{
        isCheck:false,
        name: '逛街购物'
      },{
        isCheck:false,
        name: '参加体育运动'
      },{
        isCheck:false,
        name: '其他（请说明）'
      }],
      other: ''
    },{
      title: '阅读偏好',
      subtitle: '您平时喜欢阅读哪种类型的书籍或文章？',
      Data: [{
        isCheck:false,
        name: '小说'
      },{
        isCheck:false,
        name: '文学类'
      },{
        isCheck:false,
        name: '科技类'
      },{
        isCheck:false,
        name: '历史类'
      },{
        isCheck:false,
        name: '其他（请说明）'
      }],
      other: ''
    },{
      title: '运动偏好',
      subtitle: '您喜欢哪些类型的体育运动或健身活动？',
      Data: [{
        isCheck:false,
        name: '跑步'
      },{
        isCheck:false,
        name: '游泳'
      },{
        isCheck:false,
        name: '瑜伽'
      },{
        isCheck:false,
        name: '篮球'
      },{
        isCheck:false,
        name: '其他（请说明）'
      }],
      other: ''
    },{
      title: '旅行偏好',
      subtitle: '您喜欢去什么样的地方旅行？',
      Data: [{
        isCheck:false,
        name: '自然风光'
      },{
        isCheck:false,
        name: '历史文化'
      },{
        isCheck:false,
        name: '城市探索'
      },{
        isCheck:false,
        name: '海滩度假'
      },{
        isCheck:false,
        name: '其他（请说明）'
      }],
      other: ''
    },{
      title: '音乐偏好',
      subtitle: '您喜欢听哪种类型的音乐？',
      Data: [{
        isCheck:false,
        name: '流行音乐'
      },{
        isCheck:false,
        name: '古典音乐'
      },{
        isCheck:false,
        name: '摇滚音乐'
      },{
        isCheck:false,
        name: '电子音乐'
      },{
        isCheck:false,
        name: '其他（请说明）'
      }],
      other: ''
    },{
      title: '影视偏好',
      subtitle: '您最喜欢看哪种类型的电影或电视剧？',
      Data: [{
        isCheck:false,
        name: '动作片'
      },{
        isCheck:false,
        name: '喜剧片'
      },{
        isCheck:false,
        name: '科幻片'
      },{
        isCheck:false,
        name: '纪录片'
      },{
        isCheck:false,
        name: '其他（请说明）'
      }],
      other: ''
    },{
      title: '游戏偏好',
      subtitle: '您喜欢玩哪种类型的游戏？',
      Data: [{
        isCheck:false,
        name: '电子游戏（主机/PC/手机）'
      },{
        isCheck:false,
        name: '桌游'
      },{
        isCheck:false,
        name: '棋牌类'
      },{
        isCheck:false,
        name: '其他（请说明）'
      }],
      other: ''
    },{
      title: '美食偏好',
      subtitle: '除了奶茶外，您还喜欢吃哪些类型的食物？',
      Data: [{
        isCheck:false,
        name: '中餐'
      },{
        isCheck:false,
        name: '西餐'
      },{
        isCheck:false,
        name: '日料'
      },{
        isCheck:false,
        name: '韩餐'
      },{
        isCheck:false,
        name: '其他（请说明）'
      }],
      other: ''
    },{
      title: '社交活动',
      subtitle: '您通常喜欢和朋友一起进行哪些活动？',
      Data: [{
        isCheck:false,
        name: '聚聚'
      },{
        isCheck:false,
        name: '看电影'
      },{
        isCheck:false,
        name: '打游戏'
      },{
        isCheck:false,
        name: '户外活动'
      },{
        isCheck:false,
        name: '其他（请说明）'
      }],
      other: ''
    }]
  },

  // 提交
  goSubmit:function(){
    let hobbyList = this. data.hobbyList;
    let fillData = [];
    hobbyList.forEach((item, index) =>{
      let count = item.Data.filter(d => d.isCheck).length;
      let count2 = item.Data.filter(d => d.isCheck && d.name == '其他（请说明）').length;
      let arr = item.Data.filter(d => d.isCheck);
      let newArr = arr.map(d => d.name);
      if(count == 0){
        wx.showToast({
          title: '请选择'+item.title,
          icon: 'none',
          duration: 1000
        })
        return false;
      }
      if(count2 == 1 && item.other == ''){
        wx.showToast({
          title: '请说明'+item.title,
          icon: 'none',
          duration: 1000
        })
        return false;
      }
      let obj = {};
      
      if(count2 == 1){
        obj.other = item.other;
      }else{
        obj.other = ''
      }
      obj.title = item.title;
      obj.subtitle = item.subtitle;
      obj.select = newArr;
      obj.no = index+1;
      fillData.push(obj)

    });

    let info = {};
    info.openid = wx.getStorageSync('openid');
    info.hobby = fillData;
    wx.request({
      url: `${config}/users/saveHobby`,
      method: 'POST',
      data: info,
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        wx.showToast({
          title: '提交成功',
          icon: 'none',
          duration: 1200
        })
        setTimeout(function () {
          wx.setStorageSync('hobby', true)
          wx.navigateBack({
            delta: -1
          })
          
        }, 1200)
      }
    });

    
  },
  change(e){

    let index = e.currentTarget.dataset.index;
    let hobbyList = this.data.hobbyList;
    let values = e.detail.value;

    for (let i = 0, lenI = hobbyList[index].Data.length; i < lenI; ++i) {
      hobbyList[index].Data[i].isCheck = false
      for (let j = 0, lenJ = values.length; j < lenJ; ++j) {
        if (hobbyList[index].Data[i].name === values[j]) {
          hobbyList[index].Data[i].isCheck = true
          break
        }
      }
    }
    this.setData({
      hobbyList
    })
  },
  inputOther(e){
    let index = e.currentTarget.dataset.index;
    let hobbyList = this.data.hobbyList;
    hobbyList[index].other = e.detail.value;
    this.setData({
      hobbyList
    })
  },
  getInfo() {
    let that = this;
    wx.request({
      url: `${config}/users/getHobbyInfo`,
      method: 'GET',
      data: {
        openid: wx.getStorageSync('openid')
      },
      header: {
        'Content-Type': 'application/json'
      },
      success(res) {
        let list = res.data.data.list;
        if(list.length > 0){
          let hobbyList = that.data.hobbyList;
          list.forEach(item =>{
            let index = item.no;
            hobbyList[index].other = item.other;
            item.select.forEach(s =>{
              hobbyList[index].Data.forEach(d =>{
                if(d.name == s){
                  d.isCheck = true;
                }
              })
            });
            
          });
          that.setData({
            hobbyList
          })
        }
   
      }
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

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
    this.getInfo();
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