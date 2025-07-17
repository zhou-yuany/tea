<template>
  <div class="food_box">
    <!-- 顶部 -->
    <div class="food_head white_bg" ref="getheight">
      <img
        src="../../static/back.png"
        alt=""
        class="food_back"
        @click="goBack()"
      />
      <span>饮品点单</span>
      <button type="button" class="food_order white" @click="goOrder()">
        订单
      </button>
      <button type="button" class="food_jh white" @click="goCallnum()">
        叫号
      </button>
      <div class="food_searchk">
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
    <div class="food_container" :style="{ height: height + 'px' }">
      <!-- 左部分 -->
      <div class="food_left white_bg">
        <div class="food_caozuo white_bg">
          <div>批量操作</div>
          <div @click="clearAll()">清空</div>
        </div>
        <ul class="food_lb" :style="{ height: xpheight + 'px' }">
          <li v-for="(item, index) in orderList" :key="index">
            <div class="food_l">
              <div class="food_goodsname">
                {{ item.goodsName }} {{item.size ? '（' + item.size + '）' : item.size}}<span
                  style="margin-left: auto"
                  >x{{ item.selectCount }}</span
                ><span>￥{{ item.size && item.size.indexOf('大杯') > -1 ? (item.price + 2) * item.selectCount : item.price * item.selectCount }}</span>
              </div>
              <div class="red food_tip">为选完 必选做法</div>
            </div>
            <div class="food_ykuang">
              <div class="food_numk">
                <div class="food_jian" @click="reduceCount(item, index)"><img src="../../static/minus.png" alt=""></div>
                <input
                  type="number"
                  placeholder=""
                  @input="changeCount(item, index)"
                  v-model="item.selectCount" disabled
                />
                <div class="food_jia" @click="addCount(item, index)"><img src="../../static/plus.png" alt=""></div>
              </div>
              <button
                type="button"
                class="food_dell red white_bg"
                @click="removeData(index, item)"
              >
                删除
              </button>
            </div>
          </li>
          <!-- <li>
            <div class="food_l gray_bg">
              <div class="food_goodsname">
                杨枝甘露（大杯）<span style="margin-left: auto">x1</span
                ><span>￥18.00</span>
              </div>
              <div class="red food_tip">为选完 必选做法</div>
            </div>
            <div class="food_ykuang">
              <div class="food_numk">
                <div class="food_jian">-</div>
                <input type="text" placeholder="" value="1" />
                <div class="food_jia">+</div>
              </div>
              <button type="button" class="food_dell orange">删除</button>
            </div>
          </li> -->
        </ul>
        <div class="food_dikuang white_bg" ref="getdiheight">
          <div class="food_jgzu">
            <span class="food_nums">共{{ totalCount }}份</span>
            <span class="food_price">￥{{ totalPrice }}</span>
          </div>
          <div class="food_btnzu">
            <!-- 客户展示暂时隐藏 -->
            <!-- <button type="button" v-if="orderList.length > 0" class="white" @click="payScanCode()">扫码支付</button> -->
            <button type="button" v-if="orderList.length > 0" class="white" @click="payFinish()">完成支付</button>
          </div>
        </div>
        <!-- 扫码支付层 -->
        <div class="saomac" v-show="codeShow" @click="goGuanbi()"><img :src="orderCode" alt=""></div>
      </div>
      <!-- 中间部分 -->
      <div
        v-if="params.length > 0"
        class="food_middle white_bg"
        :style="{ height: height + 'px' }"
      >
        <span v-for="(item, index) in params" :key="index">
          <div v-if="item.name.indexOf('度') > -1">
            {{ item.name }}（必选<span class="red">1</span>）
          </div>
          <div v-else>{{ item.name }}</div>
          <ul>
            <li
              v-for="(param, paramIndex) in item.paramList"
              :key="paramIndex"
              :class="{ on: param.selected }"
              @click="handleItemClick(index, paramIndex, item.id, param)"
            >
              <span>{{ param.name }}</span>
              <span v-if="item.name.indexOf('规格') > -1">￥{{ param.name.indexOf('大杯') > -1 ? item.price + 2 : item.price }}</span>
            </li>
          </ul>
          <!-- <div>温度（必选<span class="red">1</span>）</div>
        <ul>
          <li
            v-for="(item, index) in wendu"
            :key="index"
            :class="{ on: item.selecteda }"
            @click="handleItemClicka(index)"
          >
            {{ item.name }}
          </li>
        </ul> -->

          <!-- <div>甜度（必选<span class="red">1</span>）</div>
        <ul>
          <li
            v-for="(item, index) in tiandu"
            :key="index"
            :class="{ on: item.selectedb }"
            @click="handleItemClickb(index)"
          >
            {{ item.name }}
          </li>
        </ul> -->
        </span>
      </div>
      <!-- 右部分 -->
      <div class="food_right">
        <div id="tab" :style="{ height: height + 'px' }">
          <div class="tab-con white_bg">
            <ul v-show="curId === 0" :style="{ height: tabheight + 'px' }">
              <li
                class="food_itemk white_bg"
                v-for="(item, index) in goodslist"
                :key="index"
                :class="{ active: item.checked && item.isGrounding == 1 }"
                @click="onCheck(index, item)"
              >
                <div class="food_name">{{ item.name }}</div>
                <div class="gray food_jiage">
                  <!-- <span>￥{{ item.lowprice }}-{{ item.highprice }}</span
                  > -->
                  <span>￥{{ item.price }}</span>
                  <el-button v-if="item.isGrounding == 2" size="samll" type="primary" @click="clickGrounding(item.id)" class="sjbtn">上架</el-button>
                </div>
                <div v-show="isShowcheck && item.checked && item.isGrounding == 1">
                  <el-checkbox
                  :key="item.selectChecked"
                    :checked="item.selectChecked"
                    @click.stop.native="clickCheck($event, index)"
                  ></el-checkbox>
                  <span>是否沽清</span>
                </div>
                <div class="white red_bg food_num" v-show="item.checked && item.isGrounding == 1">
                  {{ item.selectNumber }}
                </div>
              </li>
            </ul>
            <!-- 沽清管理 -->
            <div class="food_qg white_bg" ref="getqgheight">
              <button type="button" class="white_bg" @click="toggle()">沽清管理</button>
              <div v-show="isShow">
                <el-checkbox @change="selectAll" v-model="checkAllValue">全选</el-checkbox>
                <button type="button" class="white_bg" @click="clearSelect()">沽清</button>
                <button type="button" class="white_bg" @click="finish()">完成</button>
              </div>
            </div>
            <!-- <ul v-show="curId===1">
                            <li class="food_itemk white_bg" v-for="(item, index) in goodslist" :key="index" :class="{'active':item.checked}" @click="onCheck(index)">
                                <div class="food_name">{{ item.name }}</div>
                                <div class="gray food_jiage"><span>￥{{item.lowprice}}-{{ item.highprice }}</span><el-checkbox :checked="item.checked "></el-checkbox></div>
                            </li>
                        </ul>
                        <ul v-show="curId===2">
                            <li class="food_itemk white_bg" v-for="(item, index) in goodslist" :key="index" :class="{'active':item.checked}" @click="onCheck(index)">
                                <div class="food_name">{{ item.name }}</div>
                                <div class="gray food_jiage"><span>￥{{item.lowprice}}-{{ item.highprice }}</span><el-checkbox :checked="item.checked "></el-checkbox></div>
                            </li>
                        </ul> -->
          </div>
          <div class="tab-tit">
            <a @click="curId = 0" :class="{ cur: curId === 0 }">全部</a>
            <!-- <a  @click="curId=1" :class="{'cur':curId===1}">奶茶</a>
                        <a @click="curId=2" :class="{'cur':curId===2}">果茶</a> -->
          </div>
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
      orderNo: '',
      keyword: "",
      orderId: '',
      codeShow: false,
      orderCode: '',
      params: [],
      orderList: [],
      goodsInfo: {},
      totalPrice: 0,
      totalCount: 0,
      bei: [
        {
          name: "大杯",
          price: "18.00",
          selected: false,
        },
        {
          name: "中杯",
          price: "16.00",
          selected: false,
        },
      ],
      wendu: [
        {
          name: "常温",
          selecteda: false,
        },
        {
          name: "热",
          selecteda: false,
        },
        {
          name: "去冰",
          selecteda: false,
        },
        {
          name: "少冰",
          selecteda: false,
        },
        {
          name: "正常冰",
          selecteda: false,
        },
        {
          name: "加冰",
          selecteda: false,
        },
        {
          name: "温热",
          selecteda: false,
        },
      ],
      tiandu: [
        {
          name: "全糖",
          selectedb: false,
        },
        {
          name: "7分糖",
          selectedb: false,
        },
        {
          name: "5分糖",
          selectedb: false,
        },
        {
          name: "3分糖",
          selectedb: false,
        },
        {
          name: "不额外加糖",
          selectedb: false,
        },
      ],
      curId: 0,
      goodslist: [
        {
          name: "霸气葡萄",
          checked: false,
          lowprice: "16",
          highprice: "18",
        },
        {
          name: "杨枝甘露",
          checked: false,
          lowprice: "16",
          highprice: "18",
        },
      ],
      checkAllValue: false,
      height: 0,
      xpheight: 0,
      tabheight: 0,
      isShow: false,
      isShowcheck: false,
      isShownum: false,
      timer: null
    };
  },
  created() {
    this.getGoods();
    
  },
  mounted() {
    let foodheight= this.$refs.getheight.offsetHeight;
    let windowheight = window.innerHeight;
    this.height = windowheight - foodheight - localStorage.getItem('touheight') - 57;

    let fooddiheight = this.$refs.getdiheight.offsetHeight;
    this.xpheight = this.height - fooddiheight;

    let goodqgheight = this.$refs.getqgheight.offsetHeight;
    this.tabheight = this.height - goodqgheight;
  },
  beforeDestroy () {
    clearInterval(this.timer);
      this.timer = null;
  },

  methods: {
    payFinish(){
      let that = this;
        // let orderNo = this.orderNo == '' ? Date.parse(new Date()) + '' + Math.floor(Math.random() * 10000) : this.orderNo;
        let orderNo = Date.parse(new Date()) + '' + Math.floor(Math.random() * 10000);
        let adminId = localStorage.getItem('adminId');
        this.orderList.forEach(item =>{
          if(item.size == '大杯'){
            item.price = item.price + 2;
          }
        });
        homeApi.saveOrders(orderNo, 2, adminId, this.orderList).then((res) => {
        if (res.data.code == 20000) {
          that.orderId = res.data.data.orders.id;
          that.getOrdersBy(res.data.data.orders.id)
          this.goodslist.forEach(item =>{
            item.selectNumber = 1;
            item.checked = false;
        });
          // that.getcodeQr(res.data.data.orders.id);
        }
      });
    },
    payScanCode(){
      let that = this;
        // let orderNo = this.orderNo == '' ? Date.parse(new Date()) + '' + Math.floor(Math.random() * 10000) : this.orderNo;
        let orderNo = Date.parse(new Date()) + '' + Math.floor(Math.random() * 10000);
        let shopId = localStorage.getItem('shopId');
        let adminId = localStorage.getItem('adminId');
        this.orderList.forEach(item =>{
          if(item.size == '大杯'){
            item.price = item.price + 2;
          }
        });
        homeApi.saveOrders(orderNo, 2, shopId, adminId, this.orderList).then((res) => {
        if (res.data.code == 20000) {
          that.codeShow = true;
          that.orderId = res.data.data.orders.id;
          that.getcodeQr(res.data.data.orders.id);

            // this.orderList = [];
            // this.getGoods();
        //   let list = res.data.data.orders;
        //   list.forEach((item) => {
        //     item.checked = false;
        //     item.selectChecked = false;
        //     item.selectNumber = 1;
        //   });
        //   this.goodslist = list;
        }
      });
    },
    goGuanbi(){
      this.codeShow = false;
    },
    getcodeQr(id){
      let that = this;
      homeApi.payOrders(id).then((res) => { 
        console.log('图片，',res)
        const href = window.URL.createObjectURL(res.data);//转成url格式
        that.orderCode = href; //赋值
        that.codeShow = true;
        // 实现轮询
        that.timer = window.setInterval(() => {
        
          setTimeout(this.getOrdersBy(id), 0);
      }, 500);
      });
    },

    getOrdersBy(id){
      let that = this;
        homeApi.getOrdersInfoBy(id).then((res) => { 
        if(res.data.data.payStatus == 2){
          clearInterval(that.timer);
          that.timer = null;
          that.orderList = [];
          that.codeShow = false;
          that.orderCode = "";
          that.orderId = '';
          that.totalCount = 0;
          that.totalPrice = 0;

        }
        
      });
     
      
    },
    clickGrounding(id){
      homeApi.groundingGoods(id).then((res) => {
        if (res.data.code == 20000) {
          this.getGoods();
        }
      });
    },

    clearAll(){
        this.orderList = [];
        this.goodslist.forEach(item =>{
            item.selectNumber = 1;
            item.checked = false;
        });
    },
    selectAll(e){
      if(e){
        this.goodslist.forEach(item => {
                item.selectChecked = true;
                item.checked = true;
        });
      }else{
        this.goodslist.forEach(item => {
                item.selectChecked = false;
                item.checked = false;
        });
      }
      
    },
    clearSelect(){
      let goodsObj = {};
        let goodIds = [];
        this.goodslist.forEach(item => {
              if(item.selectChecked){
        
                item.checked = false;
                goodIds.push(item.id);
                item.selectChecked = false;
                this.orderList = this.orderList.filter(order => order.goodsId != item.id);
              }
        });
        goodsObj.ids = goodIds;
        homeApi.clearGoods(goodsObj).then((res) => {
        if (res.data.code == 20000) {
          this.getGoods();
        }
      });
    },
    clickCheck(e, index){
        if(e.target.localName == 'input'){
            this.goodslist[index].selectChecked = !this.goodslist[index].selectChecked;
        }
    },
    searchGoods() {
        let goodslist = this.goodslist;
        if(this.keyword != ''){
            this.goodslist = goodslist.filter(item => item.name.indexOf(this.keyword) > -1);
        }else{
            this.getGoods();
        }
        
    },
    changeCount(orders, index) {
      let count = 0;
      this.orderList.forEach((orderObj) => {
        count =
          orderObj.goodsId == orders.goodsId
            ? count + parseInt(orderObj.selectCount)
            : count;
      });
      let goodsIndex = this.goodslist.findIndex(
        (item) => item.id == orders.goodsId
      );
      this.goodslist[goodsIndex].selectNumber = count;
      let totalPrice = 0;
      let totalCount = 0;
      this.orderList.forEach((orderObj) => {
        console.log(orderObj)
        totalPrice =
          (orderObj.size && orderObj.size.indexOf('大杯') > -1) ? totalPrice + ((orderObj.price + 2) * parseInt(orderObj.selectCount)) : totalPrice + orderObj.price * parseInt(orderObj.selectCount);
        totalCount = totalCount + parseInt(orderObj.selectCount);
      });
      this.totalCount = totalCount;
      this.totalPrice = totalPrice;
    },
    removeData(index, goods) {
      this.orderList.splice(index, 1);
      let goodsIndex = this.goodslist.findIndex(
        (item) => item.id == goods.goodsId
      );
      let goodsNumber = this.goodslist[goodsIndex].selectNumber;
      if (goodsNumber > goods.selectCount) {
        this.goodslist[goodsIndex].selectNumber =
          goodsNumber - goods.selectCount;
      } else {
        this.goodslist[goodsIndex].selectNumber = 1;
        this.goodslist[goodsIndex].checked = false;
      }
      let totalPrice = 0;
      let totalCount = 0;
      this.orderList.forEach((orderObj) => {
        totalPrice =
          (orderObj.size && orderObj.size.indexOf('大杯') > -1) ? totalPrice + ((orderObj.price + 2) * parseInt(orderObj.selectCount)) : totalPrice + orderObj.price * parseInt(orderObj.selectCount);
        totalCount = totalCount + parseInt(orderObj.selectCount);
      });
      this.totalCount = totalCount;
      this.totalPrice = totalPrice;
    },
    reduceCount(orders, index) {
      this.orderList[index].selectCount =
        parseInt(orders.selectCount) - 1 > 1
          ? parseInt(orders.selectCount) - 1
          : 1;
      let count = 0;
      this.orderList.forEach((orderObj) => {
        count =
          orderObj.goodsId == orders.goodsId
            ? count + parseInt(orderObj.selectCount)
            : count;
      });
      let goodsIndex = this.goodslist.findIndex(
        (item) => item.id == orders.goodsId
      );
      this.goodslist[goodsIndex].selectNumber = count;
      let totalPrice = 0;
      let totalCount = 0;
      this.orderList.forEach((orderObj) => {
        totalPrice =
          (orderObj.size && orderObj.size.indexOf('大杯') > -1) ? totalPrice + ((orderObj.price + 2) * parseInt(orderObj.selectCount)) : totalPrice + orderObj.price * parseInt(orderObj.selectCount);
        totalCount = totalCount + parseInt(orderObj.selectCount);
      });
      this.totalCount = totalCount;
      this.totalPrice = totalPrice;
      // this.$forceUpdate()
    },
    addCount(orders, index) {
      this.orderList[index].selectCount = parseInt(orders.selectCount) + 1;
      let count = 0;
      this.orderList.forEach((orderObj) => {
        count =
          orderObj.goodsId == orders.goodsId
            ? count + parseInt(orderObj.selectCount)
            : count;
      });
      let goodsIndex = this.goodslist.findIndex(
        (item) => item.id == orders.goodsId
      );
      this.goodslist[goodsIndex].selectNumber = count;
      let totalPrice = 0;
      let totalCount = 0;
      this.orderList.forEach((orderObj) => {
        totalPrice =
          (orderObj.size && orderObj.size.indexOf('大杯') > -1) ? totalPrice + ((orderObj.price + 2) * parseInt(orderObj.selectCount)) : totalPrice + orderObj.price * parseInt(orderObj.selectCount);
        totalCount = totalCount + parseInt(orderObj.selectCount);
      });
      this.totalCount = totalCount;
      this.totalPrice = totalPrice;
    },
    // 全部商品
    getGoods() {
      var shopId = localStorage.getItem("shopId");
      homeApi.goodsAllByShopId(shopId).then((res) => {
        if (res.data.code == 20000) {
          let list = res.data.data.list;
          list.forEach((item) => {
            item.checked = false;
            item.selectChecked = false;
            item.selectNumber = 1;
          });
          this.goodslist = list;
        }
      });
    },

    // 根据商品显示规格属性
    getGoodsParams(goodsId) {

      homeApi.goodsParams(goodsId).then((res) => {
        if (res.data.code == 20000) {
          let goodsInfo = res.data.data.info;
          let obj = {};
          obj.goodsName = goodsInfo.name;
          obj.goodsId = goodsId;
          obj.price = goodsInfo.price;
          obj.selectCount = 1;

          this.params = goodsInfo.paramCateList;
          this.params.forEach(param =>{
            param.price = goodsInfo.price;
          });
          if (this.params.length > 0) {
            let selectParams = [];

            this.params.forEach((item) => {
              if (item.name.indexOf("规格") > -1) {
                obj.size = item.paramList[0].name;
              }
              item.paramList.forEach((param, i) => {
                param.selected = i == 0 ? true : false;
              });

              selectParams.push(item.paramList[0]);
            });
            obj.selectParams = selectParams;
          }

          this.orderList.push(obj);
          let totalPrice = 0;
          let totalCount = 0;
          this.orderList.forEach((orderObj) => {
            totalPrice =
              (orderObj.size && orderObj.size.indexOf('大杯') > -1) ? totalPrice + ((orderObj.price + 2) * parseInt(orderObj.selectCount)) : totalPrice + orderObj.price * parseInt(orderObj.selectCount);
            totalCount = totalCount + parseInt(orderObj.selectCount);
          });
          this.totalCount = totalCount;
          this.totalPrice = totalPrice;
        }
      });
    },

    // 返回
    goBack: function () {
      this.$router.go(-1);
    },

    // 订单
    goOrder: function () {
      this.$router.push({
        path: "/order/",
      });
    },

    // 叫号取餐
    goCallnum: function () {
      this.$router.push({
        path: "/callnum/",
      });
    },

    // 杯规格
    handleItemClick(paramCateIndex, paramIndex, paramCateId, param) {
      this.params[paramCateIndex].paramList.forEach((param) => {
        param.selected = false;
      });

      this.params[paramCateIndex].paramList[paramIndex].selected = true;
      this.$forceUpdate();
      param.selected = true;
      let index = this.orderList[
        this.orderList.length - 1
      ].selectParams.findIndex((item) => item.paramCateId == paramCateId);

      this.orderList[this.orderList.length - 1].selectParams[index] = param;

      if (param.name.indexOf("杯") > -1) {
        this.orderList[this.orderList.length - 1].size = param.name;
      }
      let totalPrice = 0;
      this.orderList.forEach((orderObj) => {
        totalPrice =
          (orderObj.size && orderObj.size.indexOf('大杯') > -1) ? totalPrice + ((orderObj.price + 2) * parseInt(orderObj.selectCount)) : totalPrice + orderObj.price * parseInt(orderObj.selectCount);
      });
      this.totalPrice = totalPrice;
      //   this.bei.forEach((item, i) => {
      //     item.selected = i === index;
      //   });
    },

    // 温度
    handleItemClicka(index) {
      this.wendu.forEach((item, i) => {
        item.selecteda = i === index;
      });
    },

    // 甜度
    handleItemClickb(index) {
      this.tiandu.forEach((item, i) => {
        item.selectedb = i === index;
      });
    },

    //选择商品
    onCheck(index, item) {
      //   this.goodslist.forEach((item, i) => {
      //     item.checked = i === index;

      //   });
      this.goodslist[index].checked = true;
      if(item.isGrounding == 1){
        this.getGoodsParams(this.goodslist[index].id);
      }
      
    },

    // 清沽管理
    toggle() {
      (this.isShow = true), (this.isShowcheck = true), (this.isShownum = false) ;
      console.log(this.checkAllValue)
    },

    // 清沽完成
    finish() {
      (this.isShow = false),
        (this.isShowcheck = false),
        (this.isShownum = true),(this.checkAllValue = false);
        this.goodslist.forEach(item => item.checked = false)
    },
  },
});
</script>
    
<style scoped>
.food_box {
  width: 100%;
  height: 100%;
  margin: auto;
  overflow: hidden;
  background: #F2F3F4;
  padding-bottom: 22px;
}
/* 顶部 */
.food_head {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  margin-top: 17px;
  margin-bottom: 18px;
}
.food_back {
  width: 30px;
  height: 30px;
  display: block;
  margin-right: 38px;
  margin-left: 56px;
  cursor: pointer;
}
.food_head span {
  font-size: 24px;
  margin-right: 163px;
}
.food_order {
  margin: 0;
  padding: 0;
  width: 85px;
  height: 40px;
  text-align: center;
  line-height: 40px;
  background: #0256FF;
  font-size: 20px;
  border-radius: 8px;
  font-family: PingFangSCBold-Bold;
}
.food_jh {
  margin: 0;
  padding: 0;
  width: 85px;
  height: 40px;
  text-align: center;
  line-height: 40px;
  background: #0256FF;
  font-size: 20px;
  border-radius: 8px;
  font-family: PingFangSCBold-Bold;
  margin-left: 34px;
  margin-right: 225px;
}
.food_searchk {
  width: 300px;
  height: 40px;
  border: 1px solid #999999;
  display: flex;
  align-items: center;
  border-radius: 8px;
}
.food_searchk img {
  width: 24px;
  height: 24px;
  display: block;
  margin: 0 8px;
}
.food_searchk input {
  width: 250px;
  height: 38px;
  font-size: 20px;
  text-align: left;
}
/* 内容 */
.food_container {
  width: 100%;
  display: flex;
}
/* 左部分 */
.food_left {
  width: 32%;
  border-radius: 8px;
  position: relative;
  overflow: hidden;
  overflow-y: scroll;
  margin-left: 1.4%;
}
.food_caozuo {
  width: 78%;
  height: 65px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: absolute;
  top: 0;
  right: 110px;
}
.food_caozuo div {
  font-size: 20px;
  cursor: pointer;
}
.food_lb {
  overflow: hidden;
  overflow-y: scroll;
}
.food_lb li {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-top: 20px;
  margin-right: 12px;
}
.food_l {
  width: 79%;
  background: #EFF6FD;
  padding: 14px 9px 15px 22px;
}
.food_goodsname {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 28px;
  margin-bottom: 7px;
}
.food_goodsname span {
  font-size: 24px;
  margin-left: 15px;
}
.food_tip {
  font-size: 18px;
}
.food_ykuang {
  width: 85px;
  display: flex;
  flex-direction: column;
  margin-left: 16px;
}
.food_numk {
  width: 85px;
  height: 118px;
  border: 1px solid #BFBFBF;
}
.food_jian {
  width: 100%;
  height: 40px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
}
.food_jian img{
  width: 24px;
  height: 24px;
  display: block;
}
.food_numk input {
  width: 100%;
  height: 38px;
  text-align: center;
  padding: 0;
  margin: 0;
  font-size: 20px;
  border-top: 1px solid #BFBFBF;
  border-bottom: 1px solid #BFBFBF;
}
.food_jia {
  width: 100%;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}
.food_jia img{
  width: 24px;
  height: 24px;
  display: block;
}
.food_dell {
  width: 85px;
  height: 40px;
  border: 1px solid #BFBFBF;
  font-size: 24px;
  text-align: center;
  line-height: 38px;
  margin-top: 27px;
}
.food_dikuang{
  width: 100%;
  height: 146px;
  border-top: 1px solid #D8D8D8;
  position: absolute;
  bottom: 0;
}
.food_jgzu{
  padding-top: 17px;
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
}
.food_price {
  font-size: 28px;
  margin-left: 8px;
  margin-right: 110px;
}
.food_nums {
  font-size: 24px;
}
.food_btnzu button {
  width: 146px;
  height: 68px;
  background: #0256FF;
  font-size: 24px;
  text-align: center;
  line-height: 68px;
  margin-bottom: 15px;
  margin-left: 20px;
}
/* 中间部分 */
.food_middle {
  width: 19%;
  border-radius: 8px;
  margin-left: 1.25%;
}
.food_middle ul {
  display: flex;
  flex-wrap: wrap;
  padding: 0 33px;
}
.food_middle ul li {
  width: 140px;
  height: 70px;
  border: 1px solid #BFBFBF;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-radius: 8px;
  margin: 11px 18px 11px 0;
}
.food_middle ul li:nth-child(2n){
  margin-right: 0;
}
.food_middle ul li span{
  font-size: 24px;
  margin-left: 10px;
}
.food_middle ul li span:last-child{
  font-size: 22px;
}
.food_middle ul li.on {
  border: 1px solid #0256FF;
}
.food_middle div {
  width: 100%;
  margin-left: 30px;
  font-size: 24px;
  padding-top: 22px;
  padding-bottom: 11px;
}
/* 右部分 */
.food_right {
  width: 44.375%;
  height: 100%;
  position: relative;
  margin-left: 1.25%;
}
#tab {
  width: 100%;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  overflow: hidden;
  overflow-y: scroll;
  margin-bottom: 40px;
}
.tab-tit {
  font-size: 0;
  width: 85px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.tab-tit a {
  height: 50px;
  line-height: 50px;
  border-radius: 8px;
  font-size: 20px;
  font-family: PingFangSCBold-Bold;
  width: 100%;
  text-align: center;
  background: #ccc;
  text-decoration: none;
  cursor: pointer;
  margin-bottom: 10px;
}
.tab-tit .cur {
  background: #0256ff;
  color: #fff;
}
.tab-con {
  width: 88.6%;
  border: 1px dashed #ddd;
  border-radius: 8px;
  position: relative;
}
.tab-con ul {
  display: flex;
  flex-wrap: wrap;
  padding: 23px 15px 0;
  overflow: hidden;
  overflow-y: scroll;
}
.food_itemk {
  width: 224px;
  height: 182px;
  border-radius: 8px;
  padding: 22px 0 15px 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border: 1px solid #BFBFBF;
  margin: 0 8px 17px;
  cursor: pointer;
  position: relative;
}
.food_name {
  width: 100%;
  font-size: 28px;
}
.food_jiage {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.food_jiage span {
  font-size: 24px;
}
.sjbtn{
  margin-right: 20px;
}
.active {
  border: 1px solid #0256FF;
}
.food_num {
  width: 20px;
  height: 20px;
  font-size: 10px;
  text-align: center;
  line-height: 20px;
  border-radius: 50%;
  position: absolute;
  top: 0;
  right: 0;
}
/* 清沽管理 */
.food_qg {
  width: 100%;
  height: 60px;
  position: absolute;
  bottom: 0;
  left: 0;
  z-index: 1;
  display: flex;
  align-items: center;
}
.food_qg div{
  margin-left: 30px;
}
.food_qg button{
  width: 110px;
  height: 40px;
  border-radius: 8px;
  border: 1px solid #BFBFBF;
  font-size: 20px;
  font-family: PingFangSCBold-Bold;
  text-align: center;
  line-height: 36px;
  margin-left: 21px;
}
/* 扫码支付层 */
.saomac{
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  margin: auto;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 99;
  cursor: pointer;
}
.saomac img{
  width: 200px;
  height: 200px;
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -100px;
  margin-left: -100px;
}
</style>