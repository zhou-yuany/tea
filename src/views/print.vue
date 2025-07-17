<template>
  <div class="print_box">
    <!-- 顶部 -->
    <div class="print_head white_bg" ref="getheight">
      <img
        src="../../static/back.png"
        alt=""
        class="print_back"
        @click="goBack()"
      />
      <span>打印</span>
      <div class="tab-tit">
        <a @click="curId = 0" :class="{ cur: curId === 0 }">打印列表</a>
        <a @click="curId = 1" :class="{ cur: curId === 1 }">打印机状态</a>
      </div>
      <div class="print_searchk">
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
    <div class="tab-con">
      <div
        class="print_one white_bg"
        style="padding-top: 0"
        :style="{ height: height + 'px' }"
        v-show="curId === 0"
      >
        <div class="tab-tits">
          <a @click="curIds = 0" :class="{ curs: curIds === 0 }"
            >等待发送
            <div :class="{ print_xian: curIds === 0 }"></div
          ></a>
          <a @click="curIds = 1" :class="{ curs: curIds === 1 }"
            >故障订单
            <div :class="{ print_xian: curIds === 1 }"></div
          ></a>
          <a @click="curIds = 2" :class="{ curs: curIds === 2 }"
            >已发送
            <div :class="{ print_xian: curIds === 2 }"></div
          ></a>
        </div>
        <div class="tab-cons">
          <div v-show="curIds === 0">
            <el-table
              border
              :data="tableData"
              style="width: 96.5%; margin: auto"
              :header-cell-style="{ background: '#EFF6FD' }"
            >
              <el-table-column prop="brand" label="订单号">
                <template slot-scope="scope">
                  <span>1247858865</span>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="流水号">
                <template slot-scope="scope">
                  <span>000011</span>
                </template>
              </el-table-column>
              <el-table-column prop="specifications" label="单据类型">
                <template slot-scope="scope">
                  <span>单据类型</span>
                </template>
              </el-table-column>
              <el-table-column prop="inventory" label="打印机">
                <template slot-scope="scope">
                  <span>打印机一号</span>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              :page-size="limit"
              :total="total"
              style="padding: 20px 0 30px; text-align: center"
              layout="total, prev, pager, next, jumper"
              @current-change="getBatchList"
            >
            </el-pagination>
          </div>
          <div v-show="curIds === 1">鼓掌订单</div>
          <div v-show="curIds === 2">已发送</div>
        </div>
      </div>
      <div
        class="print_one white_bg"
        :style="{ height: height + 'px' }"
        v-show="curId === 1"
      >
        <el-table
          border
          :data="tableData_a"
          style="width: 96.5%; margin: auto"
          :header-cell-style="{ background: '#EFF6FD' }"
        >
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="120"
          ></el-table-column>
          <el-table-column prop="type" label="设备名称">
            <template slot-scope="scope">
              <span>打印机一号</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="设备状态">
            <template slot-scope="scope">
              <span>错误</span>
            </template>
          </el-table-column>
          <el-table-column prop="specifications" label="错误信息">
            <template slot-scope="scope">
              <span>错误信息内容</span>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getBatchList"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>
  
<script>
import Api from "@/api/api";
import { defineComponent } from "vue";
export default defineComponent({
  name: "ItmanTable2",
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      curId: 0,
      curIds: 0,
      height: 0,
      tableData: [
        {
          id: 1,
        },
        {
          id: 2,
        },
      ],
      tableData_a: [
        {
          id: 1,
        },
        {
          id: 2,
        },
      ],
    };
  },
  mounted() {
    let printheight = this.$refs.getheight.offsetHeight;
    let windowheight = window.innerHeight;
    this.height =
      windowheight - printheight - localStorage.getItem("touheight") - 57;
  },
  onLoad() {},
  created() {},
  methods: {
    // 返回
    goBack: function () {
      this.$router.go(-1);
    },

  },
});
</script>
    
<style scoped>
.print_box {
  background: #f2f3f4;
  padding-top: 18px;
  padding-bottom: 22px;
}
/* 顶部 */
.print_head {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
}
.print_back {
  width: 30px;
  height: 30px;
  display: block;
  margin-right: 38px;
  margin-left: 56px;
  cursor: pointer;
}
.print_head span {
  font-size: 24px;
  margin-right: 96px;
}
.tab-tit {
  font-size: 0;
  width: 334px;
  cursor: pointer;
}
.tab-tit a {
  display: inline-block;
  height: 55px;
  line-height: 55px;
  font-size: 24px;
  font-family: PingFangSCBold-Bold;
  text-align: center;
  color: #333;
  text-decoration: none;
  background: #efefef;
  padding: 0 23px;
  border-radius: 8px;
  margin-right: 13px;
}
.tab-tit .cur {
  color: #0256ff;
  background: #eff6fd;
}
.print_searchk {
  width: 300px;
  height: 40px;
  border: 1px solid #999999;
  display: flex;
  align-items: center;
  border-radius: 8px;
  margin-left: 226px;
}
.print_searchk img {
  width: 24px;
  height: 24px;
  display: block;
  margin: 0 8px;
}
.print_searchk input {
  width: 250px;
  height: 38px;
  font-size: 20px;
  text-align: left;
}
/* 内容 */
.tab-con {
  padding-top: 17px;
}
.print_one {
  width: 97%;
  margin: auto;
  border-radius: 8px;
  padding-top: 31px;
}
.tab-tits {
  font-size: 0;
  width: 100%;
  cursor: pointer;
  margin-left: -34px;
}
.tab-tits a {
  display: inline-block;
  width: 250px;
  height: 88px;
  line-height: 88px;
  font-size: 24px;
  text-align: center;
  color: #666666;
  text-decoration: none;
  position: relative;
}
.tab-tits a div {
  width: 48px;
  height: 4px;
  margin: auto;
  border-radius: 20px;
  position: absolute;
  bottom: 15px;
  left: 0;
  right: 0;
  z-index: 1;
}
.tab-tits .curs {
  color: #0256ff;
}
.tab-tits a div.print_xian {
  background: #0256ff;
}
</style>