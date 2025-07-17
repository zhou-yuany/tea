// pages/write/write.js
// 获取应用实例
const app = getApp()
const contextUtil = require("../../utils/context.js")
const api = require("../../utils/api.js")
var context = null;// 使用 wx.createContext 获取绘图上下文 context
var isButtonDown = false;//是否在绘制中
var arrx = [];//动作横坐标
var arry = [];//动作纵坐标
var arrz = [];//总做状态，标识按下到抬起的一个组合
var canvasw = 0;//画布宽度
var canvash = 0;//画布高度

Page({

  /**
   * 页面的初始数据
   */
  data: {
    
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.startCanvas();
  },

  startCanvas: function () {//画布初始化执行
    var that = this;
    //获取系统信息
    wx.getSystemInfo({
      success: function (res) {
        canvasw = res.windowWidth;
        canvash = res.windowHeight;
        that.setData({ canvasw: canvasw });
        that.setData({ canvash: canvash });
      }
    }); 
    this.initCanvas();
    this.cleardraw(); 
  },

  //初始化函数
  initCanvas: function () {
    context = wx.createCanvasContext('canvas');
    context.beginPath()
    context.fillStyle = 'rgba(255, 255, 255, 0)';
    context.setStrokeStyle('#000000');
    context.setLineWidth(4);
    context.setLineCap('round');
    context.setLineJoin('round');
  },

  canvasStart: function (event) {
    isButtonDown = true;
    arrz.push(0);
    arrx.push(event.changedTouches[0].x);
    arry.push(event.changedTouches[0].y);
  },

  canvasMove: function (event) {
    if (isButtonDown) {
      arrz.push(1);
      arrx.push(event.changedTouches[0].x);
      arry.push(event.changedTouches[0].y);
    }
    for (var i = 0; i < arrx.length; i++) {
      if (arrz[i] == 0) {
        context.moveTo(arrx[i], arry[i])
      } else {
        context.lineTo(arrx[i], arry[i])
      }
    }
    context.clearRect(0, 0, canvasw, canvash);
    context.setStrokeStyle('#000000');
    context.setLineWidth(4);
    context.setLineCap('round');
    context.setLineJoin('round');
    context.stroke();
    context.draw(false);
  },

  canvasEnd: function (event) {
    isButtonDown = false;
  },

  //清除画布
  cleardraw: function () {
    arrx = [];
    arry = [];
    arrz = [];
    context.clearRect(0, 0, canvasw, canvash);
    context.draw(true);
  },

  uploadImg(){
    var that = this
    const eventChannel = this.getOpenerEventChannel()
    var pages = getCurrentPages()
    var orderInfoPage = pages[pages.length-3]
    //生成图片
    wx.canvasToTempFilePath({
      canvasId: 'canvas',
      //设置输出图片的宽高
      // destWidth:150, 
      // destHeight:150,
      // fileType:'jpg',
      quality:1.0,
      success: function (res) {
        wx.uploadFile({
          filePath: res.tempFilePath,
          name: 'image',
          url: api.urlPrefix+"/order/upload/contract/sign",
          header: {'user-id': contextUtil.getUserId()},
          success: res => {
            let statusCode = res.statusCode
            if(statusCode >=200 && statusCode < 400){
              eventChannel.emit('setContractSign', JSON.parse(res.data).contractSign);
              wx.navigateBack({
                delta: 2
              })
              // 自动下单
              orderInfoPage.goOrder();
            }
          }
        })
      },
      fail: function () {
        wx.showModal({
          title: '提示',
          content: 'canvas生成图片失败。微信当前版本不支持，请更新到最新版本！',
          showCancel: false
        });
      },
      complete: function () {}
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