// pages/performance/performance.js
const app = getApp()

import * as echarts from '../../ec-canvas/echarts';
let chart = null;

Page({

  /**
   * 页面的初始数据
   */
  data: {
    contentList: [["主营业务收入","主营业务利润","净利润"], ["708/106%","708/98%","10/80%"], ["708/106%",'708/98%',"10/80%"]],
    ecBar: {
      onInit: initChart
    },
    ecLine: {
      onInit: initChart_a
    },
    ecPie: {
      onInit: initChart_b
    }
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

// 柱状图
function initChart(canvas, width, height, dpr) {
  chart = echarts.init(canvas, null, {
    width: width,
    height: height,
    devicePixelRatio: dpr // new
  });
  canvas.setChart(chart);

  var option = {
    title: {
      text: '双柱形图',
      left: 'left',
      textStyle: {
        fontSize: 14
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {            // 坐标轴指示器，坐标轴触发有效
        type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
      },
      confine: true
    },
    legend: {
      data: ['图例1', '图例2']
    },
    grid: {
      left: 20,
      right: 20,
      bottom: 15,
      top: 40,
      containLabel: true
    },
    xAxis: [
      {
        type: 'value',
        axisLine: {
          lineStyle: {
            color: '#999'
          }
        },
        axisLabel: {
          color: '#666',
        }
      }
    ],
    yAxis: [
      {
        type: 'category',
        axisTick: { show: false },
        data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
        axisLine: {
          lineStyle: {
            color: '#999'
          }
        },
        axisLabel: {
          color: '#666',
          interval: 0
        }
      }
    ],
    series: [
      {
        name: '图例1',
        type: 'bar',
        data: [300, 270, 340, 344, 300, 320, 310, 270, 340, 344, 320, 310],
      },
      {
        name: '图例2',
        type: 'bar',
        stack: '总量',
        data: [120, 102, 141, 174, 190, 250, 220, 141, 174, 190, 174, 190],
      }
    ]
  };

  chart.setOption(option);
  return chart;
}

// 折线图
function initChart_a(canvas, width, height, dpr) {
  const charta = echarts.init(canvas, null, {
    width: width,
    height: height,
    devicePixelRatio: dpr // new
  });
  canvas.setChart(charta);

  var optiona = {
    title: {
      text: '折线图',
      left: 'left',
      textStyle: {
        fontSize: 14
      }
    },
    legend: {
      data: ['A', 'B', 'C']
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
      data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
      axisLabel: {
        interval: 0
      }
    },
    yAxis: {
      x: 'center',
      type: 'value',
      scale: true,
      splitLine: {
        show: false,
        lineStyle: {
          type: 'dashed'
        }
      },
      max:200,
      min: 0,
      splitNumber: 5,
      boundaryGap: [0.2, 0.2]
    },
    series: [{
      name: 'A',
      type: 'line',
      smooth: true,
      data: [100, 150, 120, 80, 300, 500, 70, 500, 600, 400, 100, 180]
    }, {
      name: 'B',
      type: 'line',
      smooth: true,
      data: [120, 500, 510, 305, 700, 300, 200, 80, 300, 500, 70, 500]
    }, {
      name: 'C',
      type: 'line',
      smooth: true,
      data: [100, 300, 310, 500, 400, 200, 100, 120, 500, 510, 305, 700]
    }]
  };

  charta.setOption(optiona);
  return charta;
}

// 饼图
function initChart_b(canvas, width, height, dpr) {
  const chartb = echarts.init(canvas, null, {
    width: width,
    height: height,
    devicePixelRatio: dpr // new
  });
  canvas.setChart(chartb);

  let myData = [
    {value: 50, name: '上海'},
    {value: 10, name: '广州'},
    {value: 70, name: '杭州'},
    {value: 30, name: '北京'},
    {value: 90, name: '武汉'}
  ]

  var optionb = {
    title: {
      text: '饼状图',
      left: 'left',
      textStyle: {
        fontSize: 14
      }
    },
    legend: {
      x: 'center',
      y: 'bottom',
      itemWidth: 10,  // 设置宽度
      itemHeight: 10, // 设置高度
      itemGap: 15 ,// 设置间距
      formatter: function (name) {
        let total = 0
        let tarValue
        for (let i = 0; i < myData.length; i++) {
            total += myData[i].value
            if (myData[i].name == name) {
            tarValue = myData[i].value
            }
        }
        let p = Math.round((tarValue / total) * 100) + '%'  
        return `${name}  ${p}`
      }
    },
    series: [{
      label: {
        normal: {
          fontSize: 14,
          show: false
        }
      },
      type: 'pie',
      center: ['50%', '40%'],
      radius: ['30%', '40%'],
      data: [{
        value: 55,
        name: '北京'
      }, {
        value: 20,
        name: '武汉'
      }, {
        value: 10,
        name: '杭州'
      }, {
        value: 20,
        name: '广州'
      }, {
        value: 38,
        name: '上海'
      }]
    }]
  };

  chartb.setOption(optionb);
  return chartb;
}