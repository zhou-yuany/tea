// pages/city/city.js
// 获取应用实例
const app = getApp()

var $ = require('../../libs/conf.js');
var city = require('../../libs/city.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    citySelected: '大连市',
    city: '大连市',
    cityData: {},
    hotCityData: [],
    _py: ["hot", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "W", "X", "Y", "Z"],
    inputVal: '',
    searchList: [],
    hidden: true,
    showPy: '★'
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    var that = this;

    this.setData({
      latitude: wx.getStorageSync("latitude"),
      longitude: wx.getStorageSync("longitude"),
      sname: "我的位置",
      saddress: '上海市浦东新区'
    });

    this.setData({
      cityData: city.all,
      hotCityData: city.hot
    });
  },

  //搜索关键字
  keyword: function(keyword) {
    var that = this;
    $.map.getInputtips({
      keywords: keyword,
      location: that.data.longitude + "," + that.data.latitude,
      success: function(data) {
        if (data && data.tips) {
          var searchList = data.tips.filter((item) => {
            return typeof item.location != 'undefined';
          })
          that.setData({
            searchList: searchList
          });
        }
      }
    });
  },

  //选择城市
  selectCity: function(e) {
    var dataset = e.currentTarget.dataset;
    console.log(dataset)
    this.setData({
      citySelected: dataset.fullname,
      location: {
        latitude: dataset.lat,
        longitude: dataset.lng
      }
    });
    wx.navigateTo({
      url: '/pages/store/store?latitude='+dataset.lat+'&longitude='+dataset.lng+'&city='+dataset.fullname,
    })
  },

  //获取文字信息
  getPy: function(e) {
    this.setData({
      hidden: false,
      showPy: e.target.id,
    })
  },

  setPy: function(e) {
    this.setData({
      hidden: true,
      scrollTopId: this.data.showPy
    })
  },

  //滑动选择城市
  tMove: function(e) {
    var y = e.touches[0].clientY,
        offsettop = e.currentTarget.offsetTop;
    //判断选择区域,只有在选择区才会生效
    if (y > offsettop) {
      var num = parseInt((y - offsettop) / 12);
      this.setData({
        showPy: this.data._py[num]
      })
    };
  },

  //触发全部开始选择
  tStart: function() {
    this.setData({
      hidden: false
    })
  },

  //触发结束选择
  tEnd: function() {
    this.setData({
      hidden: true,
      scrollTopId: this.data.showPy
    })
  },

  //输入
  input: function(e) {
    if (e.detail.value == '') {
      this.setData({
        inputVal: e.detail.value
      });
    } else {
      this.setData({
        inputVal: e.detail.value
      });
      this.keyword(this.data.citySelected + e.detail.value)
    }
  },

  //清除输入框
  clear: function() {
    this.setData({
      inputVal: ''
    })
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