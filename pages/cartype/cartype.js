// pages/cartype/cartype.js
// 获取应用实例
const app = getApp()
const api = require('../../utils/api')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    typeList: [],
    allList: [],
    search: ''
  },
  search: function(e){
    let search = e.detail.value;
    this.setData({search});
    this.filterList();
  },
  clearSearch: function(){
    this.setData({search: ''});
    this.filterList();
  },
  filterList: function(){
    let search = this.data.search;
    this.setData({typeList: this.data.allList.filter(d=>{
      if(search){
        return d.name.indexOf(search) >= 0;
      }
      return true;
    })})
  },

  goBack:function(e){
    let brand = e.currentTarget.dataset.brand
    let size = e.currentTarget.dataset.size
    let detail = e.currentTarget.dataset.detail
    const eventChannel = this.getOpenerEventChannel()
    eventChannel.emit('setData', {brand, size, detail});
    wx.navigateBack({
      delta: 3,
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let this_ = this;
    this_.setData(options)
    api.get("/car/type/select").then(res=>{
      this_.setData({allList: res.filter(data=>data.seriesId == options.seriesId).filter(data=>data.id != -1)})
      this_.filterList();
    })
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