// pages/turnover/turnover.js
const theme = require("../../utils/themeSystem")
const {
    config
  } = require('../../utils/config')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    theme:wx.getStorageSync('systemTheme'),
    page: 1,
    limit: 500000,
    totalPage: 0,
    contentList: [],
    selectData: [],
    show: false,
    index1: 0,
    sumPrice: 0
  },

  // 点击下拉显示框
  selectTap() {
    this.setData({
      show: !this.data.show
    });
  },

  // 点击下拉列表
  optionTap(e) {
    let Index = e.currentTarget.dataset.index; //获取点击的下拉列表的下标
    this.setData({
      index1: Index,
      show: !this.data.show
    });
    this.getList();
  },

  getList(){
    let that = this;
    const {page, limit, flag, flagId, selectData, index1} = this.data;
    let shopId = selectData.length > 0 ? selectData[index1].id : 0;
    wx.request({
      url: `${config}/ordersFlow/getList/${page}/${limit}`,
      header: {
        "Content-Type": "application/json"
      },
      method: 'GET',
      data: {
        flagId,
        type: flag == 'agent' ? 2 : 1,
        shopId
      },
      success: function (res) {
          let arr = res.data.data.rows;
        let sumPrice = flag == 'agent' ? res.data.data.sumPrice : res.data.data.sumPrice2;
        let reg = /^(.{4})(?:\d+)(.{4})$/;
        if(arr.length > 0){
            arr.forEach(item => {
                item.serialNum = item.serialNum.replace(reg, "$1 **** $2")
            });
        }
        that.setData({
            contentList: arr,
            totalPage: res.data.data.total ,
            sumPrice
        })
      }
    });
  },
  getShopList(){
    let that = this;
    const {flagId} = this.data;
    wx.request({
      url: `${config}/ordersFlow/getShopList/${flagId}`,
      header: {
        "Content-Type": "application/json"
      },
      method: 'GET',
      data: {},
      success: function (res) {
          let obj = {};
          let list = res.data.data.list
          obj.id = 0;
          obj.name = '全部'
          list.unshift(obj);
        that.setData({
            selectData: list,
        })
      }
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    if(options.flag && options.flagId){
        this.setData({
            flagId: options.flagId,
            flag: options.flag
        })
        this.getList();
        if(options.flag == 'agent'){
            this.getShopList()
        }
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