<template>
  <div class="callnum_box">
    <!-- 顶部 -->
    <div class="callnum_head white_bg" ref="getcallnumheight">
      <img
        src="../../static/back.png"
        alt=""
        class="callnum_back"
        @click="goBack()"
      />
      <span>叫号取餐</span>
      <div class="tab-tit">
        <a @click="curId = 0" :class="{ cur: curId === 0 }">叫号取餐（1）</a>
      </div>
      <div class="callnum_searchk">
        <img src="../../static//search.png" alt="" />
        <input
          type="text"
          placeholder="请输入内容"
          @input="searchOrders"
          v-model="keyword"
        />
      </div>
    </div>
    <!-- 内容 -->
    <div class="tab-con" :style="{ height: height + 'px' }">
      <div class="callnum_left">
        <ul v-show="curId === 0" :style="{ height: height + 'px' }">
          <li
            v-for="(item, index) in list"
            :key="index"
            class="white_bg callnum_itemk"
          >
            <div class="callnum_groupa">
              <span>{{ item.pickupCode }}</span>
              <span class="gray">已下单{{ item.orderTime }}</span>
            </div>
            <div style="width: 33%; display: flex">
              <div
                v-for="(good, goodIndex) in item.orderParams"
                :key="goodIndex"
                class="callnum_groupb gray"
              >
                <span>{{ good.name }}</span>
                <span>{{ good.specifications }}</span>
                <!-- <div class="callnum_groupc"><span>{{good.number}}</span>份</div> -->
              </div>
            </div>
            <div class="callnum_groupc">
              共 <span>{{ item.totalCount }}</span> 份
            </div>
            <button
              v-if="item.openid"
              type="button"
              class="white callnum_jhbtn"
              v-show="item.isCall == 1"
              @click="call(item)"
            >
              叫号<span>已叫号1次</span>
            </button>
            <button
              type="button"
              class="white green_bg callnum_yqbtn"
              @click="takeOrder(item.id)"
            >
              已取餐
            </button>
          </li>
        </ul>
      </div>
      <div class="callnum_right">
        <input
          type="number"
          name=""
          id=""
          v-model="pickupCode"
          placeholder="请输入取餐号"
        />
        <!-- 虚拟键盘 -->
        <div class="keyboard">
          <div class="keyboard_top">
            <div class="nums" @click="clickNum('1')">1</div>
            <div class="nums" @click="clickNum('2')">2</div>
            <div class="nums" @click="clickNum('3')">3</div>
            <div class="nums" @click="clickNum('4')">4</div>
            <div class="nums" @click="clickNum('5')">5</div>
            <div class="nums" @click="clickNum('6')">6</div>
            <div class="nums" @click="clickNum('7')">7</div>
            <div class="nums" @click="clickNum('8')">8</div>
            <div class="nums" @click="clickNum('9')">9</div>
            <div class="nums" @click="clickNum('00')">00</div>
            <div class="nums" @click="clickNum('0')">0</div>
            <div class="nums" @click="dellNum()"><img src="../../static/dell.png" alt="" /></div>
          </div>
          <div class="keyboard_bottom white" @click="callCode()">呼叫号码</div>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
import homeApi from "@/api/home/index";
import { defineComponent } from "vue";
export default defineComponent({
  data() {
    return {
      pickupCode: "",
      curId: 0,
      height: 0,
      page: 1,
      limit: 1000,
      total: 0,
      list: [],
      list1: [],
      keyword: "",
    };
  },
  created() {
    this.callOrders();
  },
  methods: {
    searchOrders() {
      let list1 = this.list1;
      if (this.keyword == "") {
        this.list = this.list1;
      } else {
        this.list = list1.filter((item) => {
          return String(item.pickupCode).indexOf(this.keyword) > -1;
        });
      }
    },
    clickNum(num) {
      this.pickupCode = this.pickupCode + "" + num;
    },
    dellNum(){
      this.pickupCode = this.pickupCode.slice(1)
    },
    takeOrder(id) {
      let that = this;
      homeApi.orderTake(id).then((res) => {
        if (res.data.code == 20000) {
          that.callOrders();
        }
      });
    },
    call(data) {
      let that = this;
      homeApi.callOrdersBy(data.id).then((res) => {
        if (res.data.code == 20000) {
          that.callOrders();
        }
      });
    },
    callCode() {
      let that = this;
      homeApi.callOrdersByCode(parseInt(this.pickupCode)).then((res) => {
        if (res.data.code == 20000) {
          if (res.data.message == "暂无此号！") {
            this.$message({
              type: "warning",
              message: "暂无此号！",
            });
          } else {
            that.callOrders();
          }
        }
      });
    },

    callOrders() {
      let shopId = localStorage.getItem("shopId");
      homeApi.callOrders(this.page, this.limit, shopId).then((res) => {
        if (res.data.code == 20000) {
          this.list = res.data.data.rows;
          this.list.forEach((item) => {
            let time =
              (new Date().getTime() - new Date(item.createTime).getTime()) /
              1000;
            item.orderTime =
              time < 60
                ? parseInt(time) + "秒"
                : time < 3600
                ? parseInt(time / 60) +
                  "分钟" +
                  parseInt(time - parseInt(time / 60) * 60) +
                  "秒"
                : parseInt(time / 3600) +
                  "小时" +
                  parseInt((time - parseInt(time / 3600) * 3600) / 60) +
                  "分钟" +
                  parseInt(
                    time -
                      3600 * parseInt(time / 3600) -
                      60 * parseInt((time - parseInt(time / 3600) * 3600) / 60)
                  ) +
                  "秒";
          });
          this.list1 = this.list;
          this.total = res.data.data.total;
        }
      });
    },

    // 返回
    goBack: function () {
      this.$router.go(-1);
    },
  },
  mounted() {
    let callnumheight = this.$refs.getcallnumheight.offsetHeight;
    let windowheight = window.innerHeight;
    this.height =
      windowheight - callnumheight - localStorage.getItem("touheight") - 18;
  },
});
</script>
    
<style scoped>
.callnum_box {
  background: #f2f3f4;
  padding-top: 18px;
}
/* 顶部 */
.callnum_head {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
}
.callnum_back {
  width: 30px;
  height: 30px;
  display: block;
  margin-right: 38px;
  margin-left: 56px;
  cursor: pointer;
}
.callnum_head span {
  font-size: 24px;
  margin-right: 48px;
}
.tab-tit {
  font-size: 0;
  width: 185px;
  cursor: pointer;
}
.tab-tit a {
  display: inline-block;
  width: 185px;
  height: 55px;
  line-height: 55px;
  border-radius: 8px;
  background: #efefef;
  font-size: 24px;
  font-family: PingFangSCBold-Bold;
  text-align: center;
  color: #333;
  text-decoration: none;
}
.tab-tit .cur {
  color: #0256ff;
  background: #eff6fd;
}
.callnum_searchk {
  width: 300px;
  height: 40px;
  border: 1px solid #999999;
  display: flex;
  align-items: center;
  border-radius: 8px;
  margin-left: 359px;
}
.callnum_searchk img {
  width: 24px;
  height: 24px;
  display: block;
  margin: 0 8px;
}
.callnum_searchk input {
  width: 250px;
  height: 38px;
  font-size: 20px;
  text-align: left;
}
/* 内容 */
.tab-con {
  width: 100%;
  margin: auto;
  display: flex;
  justify-content: space-between;
}
/* 左侧 */
.callnum_left {
  width: 62%;
  border-right: 1px solid #d8d8d8;
}
.callnum_left ul {
  padding: 21px 24px 0;
  overflow: hidden;
  overflow-y: scroll;
}
.callnum_itemk {
  width: 100%;
  height: 140px;
  margin: auto;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
}
.callnum_groupa {
  width: 30%;
  display: flex;
  flex-direction: column;
  padding-left: 25px;
}
.callnum_groupa span {
  font-size: 34px;
  line-height: 45px;
}
.callnum_groupa span:last-child {
  font-size: 20px;
  line-height: 28px;
  margin-top: 5px;
}
.callnum_groupb {
  width: 47%;
  display: flex;
  flex-direction: column;
}
.callnum_groupb span {
  width: 100%;
  font-size: 20px;
  line-height: 28px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.callnum_groupc {
  font-size: 20px;
  font-weight: 400;
}
.callnum_groupc span {
  font-size: 28px;
  font-weight: bold;
}
.callnum_itemk button {
  width: 140px;
  height: 70px;
  font-size: 28px;
  text-align: center;
  line-height: 68px;
  font-family: PingFangSCBold-Bold;
  border-radius: 8px;
}
.callnum_jhbtn {
  margin-left: auto;
  background: #0256ff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  line-height: normal !important;
}
.callnum_jhbtn span {
  font-size: 18px;
  font-family: PingFangSCMedium-Medium;
}
.callnum_yqbtn {
  margin-right: 24px;
  margin-left: 18px;
}
/* 右侧 */
.callnum_right {
  width: 38%;
  padding: 21px 24px 0;
}
.callnum_right input {
  width: 100%;
  height: 152px;
  border-radius: 8px;
  text-align: center;
  font-size: 28px;
  font-family: PingFangSCBold-Bold;
  margin-bottom: 13px;
}
.keyboard {
  width: 100%;
}
.keyboard_top {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
}
.nums {
  width: 215px;
  height: 110px;
  background: #ffffff;
  border-radius: 8px;
  font-size: 28px;
  font-family: PingFangSCBold-Bold;
  cursor: pointer;
  margin-bottom: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.nums img {
  width: 45px;
  height: 28px;
  display: block;
}
.keyboard_bottom {
  width: 100%;
  height: 80px;
  background: #0256ff;
  border-radius: 8px;
  font-size: 28px;
  font-family: PingFangSCBold-Bold;
  text-align: center;
  line-height: 80px;
  cursor: pointer;
}
</style>