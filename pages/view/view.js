// pages/view/view.js
import * as echarts from '../../ec-canvas/echarts.min'
// 获取应用实例
const app = getApp()

function initChart(canvas, width, height, dpr) {
  const chart = echarts.init(canvas, null, {
    width: width,
    height: height,
    devicePixelRatio: dpr 
  });
  canvas.setChart(chart);

  var option = {
    legend: {
      data: ['本月', '上月'],
      width: 64,
      top: 0,
      right: 5,
      backgroundColor: 'white',
      z: 100
    },
    grid: {
      containLabel: true
    },
    tooltip: {
      show: true,
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['0', '1', '2', '3', '4', '5', '6', '7'],
      splitLine: {
        lineStyle: {
          type: 'solid',
          color: '#F2F2F2'
        }
      },
      axisLine:{
        lineStyle:{
          color: '#303030'
        }
      },
      axisTick: {
        show: false
      }
    },
    yAxis: {
      x: 'center',
      type: 'value',
      splitLine: {
        lineStyle: {
          type: 'solid',
          color: '#F2F2F2'
        }
      },
      min: 0,
      axisLine:{
        lineStyle:{
          color: '#303030'
        }
      },
      axisTick: {
        show: false
      }
    },
    series: [{
      name: '本月',
      type: 'line',
      smooth: true,
      data: [300, 200, 350, 400, 300, 400, 330, 400],
      color: '#3FD196'
    }, {
      name: '上月',
      type: 'line',
      smooth: true,
      data: [50, 100, 80, 200, 180, 400, 510, 600],
      color: '#F8A20F'
    }]
  };

  chart.setOption(option);
  return chart;
}

Page({

  /**
   * 页面的初始数据
   */
  data: {
    ec: {
      onInit: initChart
    },
    allselect: false,
    tradingArea: '请选择商业圈',
    listData:[{
      "code":"1",
      "text":"鱼香肉丝",
      "price":"1,000",
      "num":"1,000"
    },{
      "code":"2",
      "text":"麻婆豆腐",
      "price":"800",
      "num":"800"
    },{
      "code":"3",
      "text":"宫保鸡丁",
      "price":"600",
      "num":"600"
    }],
    stockData:[{
      "code":"1",
      "text":"鱼香肉丝",
      "num":"1,000"
    },{
      "code":"2",
      "text":"麻婆豆腐",
      "num":"800"
    },{
      "code":"3",
      "text":"宫保鸡丁",
      "num":"600"
    }],
    saleData:[{
      "time":"2021-08-08",
      "allsale":"2,500",
      "num":"100"
    },{
      "time":"2021-08-08",
      "allsale":"2,500",
      "num":"100"
    },{
      "time":"2021-08-08",
      "allsale":"2,500",
      "num":"100"
    }]
  },

  //返回列表
  goData:function(){
    wx.navigateBack({
      delta: -1,
    })
  },

  // 商圈下拉选
  bindShowAll(){
    this.setData({
      allselect:!this.data.allselect
    })
  },
  allSelect(e){
    var name = e.currentTarget.dataset.name
    this.setData({
      tradingArea: name,
      allselect: false
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },

  // 获取滚动条当前位置
  onPageScroll: function (e) {
    if (e.scrollTop > 100) {
      this.setData({
        floorstatus: true
      });
    } else {
      this.setData({
        floorstatus: false
      });
    }
  },

  //回到顶部
  goTop: function (e) {
    if (wx.pageScrollTo) {
      wx.pageScrollTo({
        scrollTop: 0
      })
    } else {
      wx.showModal({
        title: '提示',
        content: '当前微信版本过低，无法使用该功能，请升级到最新微信版本后重试。'
      })
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