<template>
    <div class="form_box">
      <!-- 顶部 -->
      <div class="form_head white_bg">
        <img src="../../static/back.png" alt="" class="form_back" @click="goBack()">
        <span>报表</span>
        <div class="form_searchk">
          <img src="../../static//search.png" alt="" />
          <input
            type="text"
            placeholder="请输入内容"
            @input="searchGoods"
            v-model="keyword"
          />
        </div>
      </div>
      <!-- 内容 -->
      <div class="form_container">
        <!-- 总数据 -->
        <ul class="form_list">
          <li class="form_a">
            <div class="form_hanga">今日营业收入<span style="color: #3387B7;">环比昨日 10%↑</span></div>
            <div class="form_hangb"><span>￥</span>0.00</div>
            <div class="form_hangc">实际金额</div>
          </li>
          <li class="form_b">
            <div class="form_hanga">今日订单数量<span style="color: #E18109;">环比昨日 10%↑</span></div>
            <div class="form_hangb">10<span>   单</span></div>
            <div class="form_hangc">已支付订单数量</div>
          </li>
          <li class="form_c">
            <div class="form_hanga">今日订单数量<span style="color: #10673E;">环比昨日 10%↑</span></div>
            <div class="form_hangb">10<span>   单</span></div>
            <div class="form_hangc">已支付订单数量</div>
          </li>
          <li class="form_d">
            <div class="form_hanga">今日订单数量<span style="color: #79170A;">环比昨日 10%↑</span></div>
            <div class="form_hangb">10<span>   单</span></div>
            <div class="form_hangc">已支付订单数量</div>
          </li>
        </ul>
        <!-- 筛选栏 -->
        <el-form :inline="true" class="demo-form-inline">
          <el-form-item label="指定日期">
            <el-date-picker
              v-model="accountQuery.begin"
              type="datetime"
              placeholder="选择开始时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              default-time="00:00:00"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="至">
            <el-date-picker
              v-model="accountQuery.end"
              type="datetime"
              placeholder="选择截止时间"
              value-format="yyyy-MM-dd HH:mm:ss"
              default-time="00:00:00"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search">查询</el-button>
            <el-button type="default" icon="el-icon-refresh-left">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 图表 -->
        <div class="chartk">
          <div class="chartk_left">
            <div class="tab-tit">
              <a @click="curId = 0" :class="{ cur: curId === 0 }">近三日</a>
              <a @click="curId = 1" :class="{ cur: curId === 1 }">近七日</a>
              <a @click="curId = 2" :class="{ cur: curId === 2 }">近半月</a>
            </div>
            <div class="echart white_bg" id="mychart" :style="myChartStyle"></div>
          </div>
          <div class="chartk_right">
            <div class="tab-tit">
              <a @click="curIds = 0" :class="{ cur: curIds === 0 }">近三日</a>
              <a @click="curIds = 1" :class="{ cur: curIds === 1 }">近七日</a>
              <a @click="curIds = 2" :class="{ cur: curIds === 2 }">近半月</a>
            </div>
            <div class="echart white_bg" id="main" :style="myChartStyle"></div>
          </div>
        </div>
      </div>
    </div>
</template>
  
<script>
import Api from "@/api/api";
import { defineComponent } from "vue";
import * as echarts from "echarts";
export default defineComponent({
    data() {
      return {
        accountQuery: {},
        curId: 0,
        curIds: 0,
        xData: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"], //横坐标
        yData: [23, 24, 18, 25, 27, 28, 25], //人数数据
        taskDate: [10, 11, 9, 17, 14, 13, 14],
        myChartStyle: { float: "left", width: "100%", height: "400px" } //图表样式
      };
    },
    mounted() {
      this.initEcharts();
      this.initEchart();
    },
    created() {
      
      
    },
    methods: {
      // 返回
      goBack:function(){
        this.$router.go(-1)
      },
      
      initEcharts() {
        // 多列柱状图
        const mulColumnZZTData = {
          xAxis: {
            data: this.xData
          },
          // 图例
          legend: {
            data: ["人数", "任务数"],
            top: "0%"
          },
          yAxis: {},
          series: [
            {
              type: "bar", //形状为柱状图
              data: this.yData,
              name: "人数", // legend属性
              label: {
                // 柱状图上方文本标签，默认展示数值信息
                show: true,
                position: "top"
              }
            },
            {
              type: "bar", //形状为柱状图
              data: this.taskDate,
              name: "任务数", // legend属性
              label: {
                // 柱状图上方文本标签，默认展示数值信息
                show: true,
                position: "top"
              }
            }
          ]
        };
        const myChart = echarts.init(document.getElementById("mychart"));
        myChart.setOption(mulColumnZZTData);
        //随着屏幕大小调节图表
        window.addEventListener("resize", () => {
          myChart.resize();
        });
      },

      initEchart() {
        const option = {
          title: {
            text: ''
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['收入', '支出', '利润']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '收入',
              type: 'line',
              stack: 'Total',
              data: [220, 182, 191, 234, 290, 330, 310]
            },
            {
              name: '支出',
              type: 'line',
              stack: 'Total',
              data: [120, 132, 101, 134, 90, 230, 210]
            },
            {
              name: '利润',
              type: 'line',
              stack: 'Total',
              data: [150, 232, 201, 154, 190, 330, 410]
            }
          ]
        };
        const myCharts = echarts.init(document.getElementById("main"));
        myCharts.setOption(option);
        //随着屏幕大小调节图表
        window.addEventListener("resize", () => {
          myCharts.resize();
        });
      }
    },
});
</script>
    
<style scoped>
.form_box{
  background: #F2F3F4;
  padding-top: 18px;
  padding-bottom: 29px;
}
/* 顶部 */
.form_head{
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
}
.form_back{
  width: 30px;
  height: 30px;
  display: block;
  margin-right: 38px;
  margin-left: 56px;
  cursor: pointer;
}
.form_head span{
  font-size: 24px;
  margin-right: 48px;
}
.form_searchk {
  width: 300px;
  height: 40px;
  border: 1px solid #999999;
  display: flex;
  align-items: center;
  border-radius: 8px;
  margin-left: 640px;
}
.form_searchk img {
  width: 24px;
  height: 24px;
  display: block;
  margin: 0 8px;
}
.form_searchk input {
  width: 250px;
  height: 38px;
  font-size: 20px;
  text-align: left;
}
/* 内容 */
.form_container{
  padding-top: 21px;
}
.form_list{
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 45px;
}
.form_list li{
  width: 420px;
  height: 200px;
  border-radius: 8px;
  margin: 0 22.5px;
  padding: 27px 18px 0 27px;
  line-height: 28px;
}
.form_a{
  border: 1px solid #08A0F7;
  background: #E1F2FC;
  color: #08A0F7;
}
.form_b{
  border: 1px solid #FBA63C;
  background: #FFF8EF;
  color: #FBA63C;
}
.form_c{
  border: 1px solid #34A770;
  background: #DCF3EB;
  color: #34A770;
}
.form_d{
  border: 1px solid #F85640;
  background: #FFF8F8;
  color: #F85640;
}
.form_hanga{
  font-size: 24px;
  font-family: PingFangSCBold-Bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 31px;
}
.form_hanga span{
  font-size: 20px;
  font-family: PingFangSCMedium-Medium;
}
.form_hangb{
  font-size: 40px;
  font-family: PingFangSCBold-Bold;
  margin-bottom: 31px;
}
.form_hangb span{
  font-size: 28px;
}
.form_hangc{
  font-family: PingFangSCBold-Bold;
  font-size: 24px;
}
/* 筛选栏 */
.demo-form-inline{
  width: 94%;
  margin: auto;
}
/* 图表 */
.chartk{
  display: flex;
  justify-content: center;
}
.chartk_left{
  width: 53%;
  margin-right: 76px;
}
.tab-tit{
  font-size: 0;
  width: 100%;
  cursor: pointer;
  margin-bottom: 24px;
}
.tab-tit a{
  display: inline-block;
  width: 120px;
  height: 40px;
  line-height: 38px;
  border-radius: 50px;
  background: rgba(150, 150, 150, 0);
  border: 1px solid #3F74F9;
  font-size: 20px;
  text-align: center;
  color: #3F74F9;
  text-decoration: none;
  margin-right: 27px;
}
.tab-tit .cur{
  color: #ffffff;
  background: #3F74F9;
}
.chartk_right{
  width: 36%;
}
</style>