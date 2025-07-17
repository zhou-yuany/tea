// pages/salerecent/salerecent.js
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
    legend: {  //右上角提示样式
      data: ['本月', '上月'],
      width: 64,
      top: 0,
      right: 5,
      backgroundColor: 'white',
      z: 100
    },
    grid: { //X轴距离左侧留白
      containLabel: true
    },
    tooltip: {  //提示框
      show: true,  //显示
      trigger: 'axis'  //坐标轴触发
    },
    xAxis: {   //X轴
      type: 'category',  //类目轴
      boundaryGap: false,  // 坐标轴两边留白策略
      data: ['0', '1', '2', '3', '4', '5', '6', '7'],  //类目数据
      splitLine: {   //坐标轴在 grid 区域中的分隔线
        lineStyle: {   //分割线
          type: 'solid',
          color: '#F2F2F2'
        }
      },
      axisLine:{   //坐标轴轴线相关设置
        lineStyle:{
          color: '#303030'
        }
      },
      axisTick: {  //坐标轴刻度相关设置
        show: false
      }
    },
    yAxis: {   //  Y轴
      x: 'center',
      type: 'value',  //数值轴
      splitLine: {  //坐标轴在 grid 区域中的分隔线
        lineStyle: {   //分隔线
          type: 'solid',
          color: '#F2F2F2'
        }
      },
      min: 0,  //坐标轴刻度最小值
      axisLine:{   //坐标轴轴线相关设置
        lineStyle:{
          color: '#303030'
        }
      },
      axisTick: {  //坐标轴刻度相关设置
        show: false
      }
    },
    series: [{
      name: '本月',
      type: 'line',  //折线图
      smooth: true,   //是否平滑曲线显示
      data: [300, 200, 350, 400, 300, 400, 330, 400],
      color: '#3FD196'  //折线颜色
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
    }
  },

  //横屏提示
  goHorizontal:function(){
    wx.showModal({
      title: '提示',
      content: '请打开手机“自动旋转”功能即可横屏查看',
      showCancel: false,
      success: function(res) {
       if (res.confirm) {
        // console.log('用户点击确定')
       }
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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