<template>
  <div class="order_box">
    <!-- 顶部 -->
    <div class="order_head white_bg" ref="getorderheight">
      <img
        src="../../static/back.png"
        alt=""
        class="order_back"
        @click="goBack()"
      />
      <span>订单</span>
      <div class="order_searchk">
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
    <div class="order_container">
      <!-- 左部分 -->
      <div class="order_left white_bg" :style="{ height: height + 'px' }">
        <div id="tab">
          <div class="order_hang white_bg">
            <div class="tab-tit">
              <a @click="curId = 0" :class="{ cur: curId === 0 }"
                >全部
                <div :class="{ order_xian: curId === 0 }"></div
              ></a>
              <a @click="curId = 1" :class="{ cur: curId === 1 }"
                >待结账
                <div :class="{ order_xian: curId === 1 }"></div
              ></a>
              <a @click="curId = 2" :class="{ cur: curId === 2 }"
                >已结账
                <div :class="{ order_xian: curId === 2 }"></div
              ></a>
            </div>
          </div>
          <div class="tab-con">
            <ul
              class="order_ul"
              :style="{ height: height + 'px' }"
              v-show="curId === 0"
            >
              <li
                class="order_itemk white_bg"
                v-for="(item, index) in alllist"
                :key="index"
                :class="{ active: item.ischeck }"
                @click="onCheck(index, item.id)"
              >
                <img
                  :src="
                    item.payType == 1
                      ? '../../static/wexin.png'
                      : '../../static/pos.png'
                  "
                  alt=""
                  class="order_pic"
                />
                <div class="order_lp">
                  <div class="order_number">流水号{{ "000000" + item.id }}</div>
                  <div class="gray order_ordernum">
                    订单尾号：{{
                      item.orderNum
                        ? item.orderNum.substring(item.orderNum.length - 4)
                        : ""
                    }}
                  </div>
                </div>
                <div class="order_rp">
                  <div class="order_price">￥{{ item.totalPrice }}</div>
                  <div class="gray order_state">
                    <!-- {{ item.payStatus == 1 ? "待结账" : "已结账" }} -->
                    {{
                      item.orderStatus == 0
                        ? "待制作"
                        : item.orderStatus == 1
                        ? "制作中"
                        : "制作完成"
                    }}
                  </div>
                  <div class="gray order_time">{{ item.createTime }}</div>
                </div>
              </li>
            </ul>
            <ul
              class="order_ul"
              :style="{ height: height + 'px' }"
              v-show="curId === 1"
            >
              <li
                class="order_itemk white_bg"
                v-for="(item, index) in alllist"
                :key="index"
                :class="{ active: item.ischecka }"
                @click="onChecka(index, item.id)"
              >
                <img
                  :src="
                    item.payType == 1
                      ? '../../static/wexin.png'
                      : '../../static/pos.png'
                  "
                  alt=""
                  class="order_pic"
                />
                <div class="order_lp">
                  <div class="order_number">流水号{{ "000000" + item.id }}</div>
                  <div class="gray order_ordernum">
                    订单尾号：{{
                      item.orderNum
                        ? item.orderNum.substring(item.orderNum.length - 4)
                        : ""
                    }}
                  </div>
                </div>
                <div class="order_rp">
                  <div class="order_price">￥{{ item.totalPrice }}</div>
                  <div class="gray order_state">
                    <!-- {{ item.payStatus == 1 ? "待结账" : "已结账" }} -->
                    {{
                      item.orderStatus == 0
                        ? "待制作"
                        : item.orderStatus == 1
                        ? "制作中"
                        : "制作完成"
                    }}
                  </div>
                  <div class="gray order_time">{{ item.createTime }}</div>
                </div>
              </li>
            </ul>
            <ul
              class="order_ul"
              :style="{ height: height + 'px' }"
              v-show="curId === 2"
            >
              <li
                class="order_itemk white_bg"
                v-for="(item, index) in alllist"
                :key="index"
                :class="{ active: item.ischeckb }"
                @click="onCheckb(index, item.id)"
              >
                <img
                  v-if="item.payType == 1"
                  src="../../static/wexin.png"
                  alt=""
                  class="order_pic"
                />
                <img
                  v-else
                  src="../../static/pos.png"
                  alt=""
                  class="order_pic"
                />
                <div class="order_lp">
                  <div class="order_number">流水号{{ "000000" + item.id }}</div>
                  <div class="gray order_ordernum">
                    订单尾号：{{
                      item.orderNum
                        ? item.orderNum.substring(item.orderNum.length - 4)
                        : ""
                    }}
                  </div>
                </div>
                <div class="order_rp">
                  <div class="order_price">￥{{ item.totalPrice }}</div>
                  <div class="gray order_state">
                    <!-- {{ item.payStatus == 1 ? "待结账" : "已结账" }} -->
                    {{
                      item.orderStatus == 0
                        ? "待制作"
                        : item.orderStatus == 1
                        ? "制作中"
                        : "制作完成"
                    }}
                  </div>
                  <div class="gray order_time">{{ item.createTime }}</div>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <!-- 右部分 -->
      <div class="order_right white_bg" :style="{ height: height + 'px' }">
        <div class="order_header" ref="gettitleheight">订单详情</div>
        <div class="order_bottom" :style="{ height: bottomheight + 'px' }">
          <div class="order_bottom_l">
            <div class="order_sigle">
              <span>订单号：{{ ordersInfo.orderNum }}</span>
              <span class="blue order_zhuangtai">{{
                ordersInfo.payStatus == 1 ? "待支付" : "已支付"
              }}</span>
            </div>
            <div class="gray order_hui">
              营业日期：{{
                ordersInfo.createTime ? ordersInfo.createTime.substr(0, 10) : ""
              }}
            </div>
            <div class="gray order_hui">
              下单时间：{{ ordersInfo.createTime }}
            </div>
            <div v-show="ordersInfo.payStatus == 2" class="gray order_hui">
              结账时间：{{ ordersInfo.updateTime }}
            </div>
            <div class="order_mess">订单信息</div>
            <div class="order_sigle order_lineheight">
              <span class="gray">支付合计</span
              ><span>￥{{ ordersInfo.totalPrice }}</span>
            </div>
            <div class="order_sigle order_lineheight">
              <span class="gray">类型</span
              ><span>{{ ordersInfo.payType == 1 ? "小程序" : "收银POS" }}</span>
            </div>
            <div class="order_sigle order_lineheight">
              <span class="gray">来源</span
              ><span>{{ ordersInfo.payType == 1 ? "小程序" : "收银POS" }}</span>
            </div>
            <div class="order_sigle order_lineheight">
              <span class="gray">流水号</span
              ><span>{{ "000000" + ordersInfo.id }}</span>
            </div>
            <div class="order_sigle order_lineheight">
              <span class="gray">开单人</span
              ><span>{{
                ordersInfo.payType == 1 ? "茶饮小程序" : ordersInfo.adminName
              }}</span>
            </div>
            <div class="order_mess gray_bg">支付信息</div>
            <div class="order_sigle">
              <span class="ddje">订单金额</span
              ><span>￥{{ ordersInfo.totalPrice }}</span>
            </div>
            <div class="order_sigle order_lineheight">
              <span class="dark_gray ypyj">饮品原价</span
              ><span>￥{{ ordersInfo.totalPrice }}</span>
            </div>
          </div>
          <div class="order_bottom_r">
            <div
              class="order_mess gray_bg"
              style="margin-top: 0"
              ref="getmessheight"
            >
              饮品 信息
            </div>
            <div class="order_sigle" ref="getsigleheight">
              <!-- <span>饮品总数：{{ ordersInfo.totalCount }}</span> -->
              <span></span>
              <span class="hjhj">合计：￥{{ ordersInfo.totalPrice }}</span>
            </div>
            <div class="order_table dark_gray" ref="gettableheight">
              <div style="width: 60%">饮品名称</div>
              <div style="margin-left: auto; width: 20%; text-align: center">
                数量
              </div>
              <div style="width: 20%; text-align: center">小计</div>
            </div>
            <ul class="order_yinpin" :style="{ height: ypheight + 'px' }">
              <li
                v-for="(goods, goodsIndex) in ordersInfo.params"
                :key="goodsIndex"
              >
                <div style="width: 60%; display: flex; flex-direction: column">
                  <span class="shangpinmingcheng"
                    >{{ goods.name
                    }}{{
                      goods.specifications.indexOf("杯") > -1
                        ? "（" + goods.specifications.substring(0, 2) + "）"
                        : ""
                    }}</span
                  >
                  <span class="gray zuofass"
                    >做法：{{
                      goods.specifications.indexOf("杯") > -1
                        ? goods.specifications.substring(3)
                        : goods.specifications
                    }}</span
                  >
                </div>
                <div
                  class="dark_gray shuliangs"
                  style="margin-left: auto; width: 20%; text-align: center"
                >
                  x{{ goods.number }}
                </div>
                <div
                  class="dark_gray jjjq"
                  style="width: 20%; text-align: center"
                >
                  ￥{{ parseInt(goods.price) * goods.number }}
                </div>
              </li>
            </ul>
          </div>
        </div>
        <div class="order_di" ref="getdiheight">
          <button
            v-if="ordersInfo.orderStatus == 1 || ordersInfo.orderStatus == 0"
            type="button"
            class="white green_bg dayin"
            @click="startPrint()"
          >
            打印
          </button>
          <button
            v-if="ordersInfo.orderStatus == 0"
            type="button"
            class="white blue_bg"
            @click="payOrders(ordersInfo)"
          >
            确认订单
          </button>
          
          <button
            v-if="ordersInfo.orderStatus == 1"
            type="button"
            class="white blue_bg"
            @click="finishOrders(ordersInfo)"
          >
            确认完成
          </button>
        </div>
      </div>
    </div>
    <!-- 打印小票 -->
    <div class="mask receipt_kuang" v-show="false">
      <!-- 订单票 -->
      <div class="receipt_a white_bg" id="printOrders" style="zoom: 0.8;">
        <div
          style="
            width: 100%;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
          "
        >
          {{ ordersInfo.shopName }}
        </div>
        <div
          style="width: 100%; text-align: center; font-size: 20px; margin: 10px"
        >
          — 已在线支付 —
        </div>
        <div
          style="
            width: 100%;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 15px;
          "
        >
          取餐号：{{ ordersInfo.pickupCode }}
        </div>
        <div style="font-size: 14px; line-height: 18px">
          下单时间：{{
            ordersInfo.updateTime ? ordersInfo.updateTime.substring(5, 10) : ""
          }}{{
            ordersInfo.updateTime ? ordersInfo.updateTime.substring(10, 19) : ""
          }}
        </div>
        <div style="font-size: 14px; line-height: 18px; margin-bottom: 15px">
          订单编号：{{ ordersInfo.orderNum }}
        </div>
        <div
          style="
            font-size: 20px;
            line-height: 24px;
            border-top: 1px dashed #000;
            border-bottom: 1px dashed #000;
            margin-bottom: 15px;
          "
        >
          收货人：{{ ordersInfo.consignee }}（{{ordersInfo.sex == 1 ? '男士' : '女士'}}）{{ ordersInfo.receiverPhone }}
        </div>

        <div
          style="
            font-size: 20px;
            line-height: 24px;
            border-top: 1px dashed #000;
            border-bottom: 1px dashed #000;
            margin-bottom: 15px;
          "
        >
          收货地址：{{ ordersInfo.address }}
        </div>
        <div
          style="
            font-size: 20px;
            line-height: 24px;
            border-top: 1px dashed #000;
            border-bottom: 1px dashed #000;
            margin-bottom: 15px;
          "
        >
          备注：{{ ordersInfo.notes }}
        </div>
        <div
        v-if="ordersInfo.giver"
          style="
            font-size: 20px;
            line-height: 24px;
            border-top: 1px dashed #000;
            border-bottom: 1px dashed #000;
            margin-bottom: 15px;
          "
        >
          赠予者：{{ordersInfo.isAnonymous == 1 ? '匿名用户' : ordersInfo.giver}}
        </div>
        <div
          v-if="ordersInfo.giverNotes"
          style="
            font-size: 20px;
            line-height: 24px;
            border-top: 1px dashed #000;
            border-bottom: 1px dashed #000;
            margin-bottom: 15px;
          "
        >
          赠予者留言：{{ ordersInfo.giverNotes}}
        </div>
        <div
          class="goodsxq"
          v-for="(item, index) in ordersInfo.params"
          :key="index"
        >
          <span class="goodsname">{{ item.name }}</span>
          <span>{{
            item.specifications.indexOf("杯") > -1
              ? "（" + item.specifications.substring(0, 2) + "）"
              : ""
          }}</span>
          <span>{{
            item.specifications.indexOf("杯") > -1
              ? item.specifications.substring(3)
              : item.specifications
          }}</span>
          <span>x{{ item.number }}</span>
        </div>
        <div
          style="
            width: 100%;
            font-size: 22px;
            text-align: right;
            line-height: 50px;
            height: 50px;
            border-top: 1px dashed #000;
            border-bottom: 1px dashed #000;
          "
        >
          实付：￥{{ ordersInfo.totalPrice }}
        </div>
        <div style="width: 180px; height: 180px; margin: 20px auto">
          <img
            src="../assets/qrcode.jpg"
            alt=""
            style="width: 180px; height: 180px; display: block"
          />
        </div>
      </div>
      <!-- 贴纸 -->
      <div
        class="receipt_b white_bg"
        style="page-break-after: always;"
        v-for="(item, index) in arr"
        :key="index"
        :id="'printTips' + index"
      >
        <div>
          <div class="qucanh" style="margin-bottom: 5px;">
            <div style="width: 100%;font-size: 14px;">小程序</div>
            <div style="width: 100%;font-size: 14px;">自提</div>
            <div class="large" style="width: 100%;font-size: 20px;font-weight: 600;">取餐号：{{ ordersInfo.pickupCode }}</div>
          </div>
          <div class="goodsxqs" style="width: 100%;margin-bottom: 5px;">
            <div class="goodsname" style="font-size: 14px;font-weight: 600;">{{ item.name }}</div>
            <div style="font-size: 14px;">
              {{
                item.specifications.indexOf("杯") > -1
                  ? "（" + item.specifications.substring(0, 2) + "）"
                  : ""
              }}
            </div>
            <div style="font-size: 14px;">
              {{
                item.specifications.indexOf("杯") > -1
                  ? item.specifications.substring(3)
                  : item.specifications
              }}
            </div>
          </div>
          <div class="order_time">
            <span style="font-size: 14px;margin-right: 10px;">{{
              ordersInfo.updateTime
                ? ordersInfo.updateTime.substring(5, 10)
                : ""
            }}</span
            ><span style="font-size: 14px;margin-right: 10px;">{{
              ordersInfo.updateTime
                ? ordersInfo.updateTime.substring(10, 19)
                : ""
            }}</span>
          </div>
          <div class="order_shopname" style="font-size: 14px;">{{ ordersInfo.shopName }}</div>
          <div class="order_beizhu" style="font-size: 16px;">备注：{{ ordersInfo.notes }}</div>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
import homeApi from "@/api/home/index";
import { defineComponent } from "vue";
import { createSocket, sendWSPush, closeWs } from "./websocket/websocket.js";
import { getLodop } from '@/assets/js/LodopFuncs'

export default defineComponent({
  data() {
    return {
      height: 0,
      bottomheight: 0,
      ypheight: 0,
      curId: 0,
      page: 1,
      limit: 1000,
      total: 0,
      getsocketData: "",
      keyword: "",
      arr: [],
      ordersInfo: {
        orderNum: "",
        payStatus: 1,
        updateTime: "",
        createTime: "",
        totalPrice: "",
        payType: "",
        adminName: "",
        id: "",
        totalCount: "",
        params: [],
      },
      alllist: [],
      // dailist:[{
      //   pic: '../../static/login_bg.png',
      //   number: '0001111222333566',
      //   ordernum: '00001',
      //   price: '18.00',
      //   state: '已结账',
      //   time: '2023-07-28  14:14:53',
      //   ischecka: false
      // },{
      //   pic: '../../static/login_bg.png',
      //   number: '0001111222333566',
      //   ordernum: '00001',
      //   price: '18.00',
      //   state: '已结账',
      //   time: '2023-07-28  14:14:53',
      //   ischecka: false
      // }],
      // yilist:[{
      //   pic: '../../static/login_bg.png',
      //   number: '0001111222333566',
      //   ordernum: '00001',
      //   price: '18.00',
      //   state: '已结账',
      //   time: '2023-07-28  14:14:53',
      //   ischeckb: false
      // },{
      //   pic: '../../static/login_bg.png',
      //   number: '0001111222333566',
      //   ordernum: '00001',
      //   price: '18.00',
      //   state: '已结账',
      //   time: '2023-07-28  14:14:53',
      //   ischeckb: false
      // }]
    };
  },
  watch: {
    curId(newValue, oldValue) {
      this.getOrdersList();
    },
  },
  created() {
    this.getOrdersList();

    createSocket();
    this.getsocketData = (e) => {
      // 创建接收消息函数
      let data = e && e.detail.data;
      if (data) {
        if (data.indexOf("addOrder")) {
          // 已经建立了连接
          this.getOrdersList();
        }
      }
    };
    // 注册监听事件
    window.addEventListener("onmessageWS", this.getsocketData);
  },
  mounted() {
    let orderheight = this.$refs.getorderheight.offsetHeight;
    let windowheight = window.innerHeight;
    this.height =
      windowheight - orderheight - localStorage.getItem("touheight") - 57;

    let titleheight = this.$refs.gettitleheight.offsetHeight;
    let diheight = this.$refs.getdiheight.offsetHeight;
    this.bottomheight = this.height - titleheight - diheight;

    let messheight = this.$refs.getmessheight.offsetHeight;
    let sigleheight = this.$refs.getsigleheight.offsetHeight;
    let tableheight = this.$refs.gettableheight.offsetHeight;
    this.ypheight =
      this.bottomheight - messheight - sigleheight - tableheight - 81;
  },
  methods: {
    startPrint(){
      // this.prints();
      this.print1()
      this.printOrders();
    },
    print1(){
      if (this.arr.length > 0) {
        var LODOP = getLodop()
        // var totalPrint = 0 // 第几个
        // // 如果有预设的打印机名称，则设置，否则使用本机默认打印机
        // if (!this.isEmpty(this.print_info[0].打印机名称)) {
          
        // }
        LODOP.SET_PRINT_MODE('WINDOW_DEFPRINTER', "Xprinter-365b")
        // 循环打印
        for (var i = 0; i < this.arr.length; i++) {
          console.log(this.arr[i])
          // totalPrint++
  
            LODOP.SET_PRINT_PAGESIZE(1, 390, 300, '40mm') // 设定纸张大小:纵向，宽为0，高为0，纸张类型为148.5mm，
            LODOP.ADD_PRINT_TEXT(5, 0, 248, 30, this.ordersInfo.shopName)
            LODOP.SET_PRINT_STYLEA(0, 'FontSize', 8)
            //LODOP.ADD_PRINT_TEXT(5, 0, 157, 25, '小程序')
            //LODOP.SET_PRINT_STYLEA(0, 'FontSize', 10)
            //LODOP.ADD_PRINT_TEXT(5, 50, 228, 35, '自提') // 增加纯文本项:距离顶部31mm,距离左边312，宽度228，高度35
            //LODOP.SET_PRINT_STYLEA(0, 'FontSize', 10) // 设置第0个内容项字体为18号字体
            LODOP.ADD_PRINT_TEXT(20, 0, 657, 25, "取餐号："+this.ordersInfo.pickupCode)
            LODOP.SET_PRINT_STYLEA(0, 'FontSize', 10)
            LODOP.ADD_PRINT_TEXT(36, 0, 646, 189, this.arr[i].name)
            LODOP.SET_PRINT_STYLEA(0, 'FontSize', 8)
            LODOP.ADD_PRINT_TEXT(52, 0, 346, 30, this.arr[i].specifications.indexOf("杯") > -1
                  ? "（" + this.arr[i].specifications.substring(0, 2) + "）"
                  : "")
            LODOP.SET_PRINT_STYLEA(0, 'FontSize', 8)
            LODOP.ADD_PRINT_TEXT(52, 0, 248, 30, this.arr[i].specifications.indexOf("杯") > -1
                  ? this.arr[i].specifications.substring(3)
                  : this.arr[i].specifications)
            LODOP.SET_PRINT_STYLEA(0, 'FontSize', 8)
            LODOP.ADD_PRINT_TEXT(67, 0, 248, 30, this.ordersInfo.updateTime
                ? this.ordersInfo.updateTime.substring(5, 10)
                : "")
            LODOP.SET_PRINT_STYLEA(0, 'FontSize', 8)
            LODOP.ADD_PRINT_TEXT(67, 30, 248, 30, this.ordersInfo.updateTime
                ? this.ordersInfo.updateTime.substring(10, 19)
                : "")
            LODOP.SET_PRINT_STYLEA(0, 'FontSize', 8)
            
            // LODOP.ADD_PRINT_TEXT(79, 0, 248, 30, this.ordersInfo.shopName)
            // LODOP.SET_PRINT_STYLEA(0, 'FontSize', 10)
            LODOP.ADD_PRINT_TEXT(82, 0, 248, 30, this.ordersInfo.notes ? "备注："+this.ordersInfo.notes : '')
            LODOP.SET_PRINT_STYLEA(0, 'FontSize', 8)
            // if (totalPrint == this.arr.length) { 
              // console.log(this.arr[i].name)
              LODOP.PRINT()
              // LODOP.PREVIEW() // 为防止真正打印，采用预览模式,预览模式只能预览一页，无法翻页，切会弹出‘有窗口已打开，先关闭它’提示
            
          // } 
        }
      }
    },
    prints() {
      // const promiseAll = [];
      // const img = [];
      const newWin = window.open("");
      for (let i = 0; i < this.arr.length; i++) {
        const imageToPrint = document.getElementById("printTips" + i);

        imageToPrint.onload = function () {
          imageToPrint.style.margin = 0;

          // return false;
        };
        newWin.document.write(imageToPrint.outerHTML);
        console.log(newWin)
        // promiseAll[i] = new Promise((resolve, reject) => {

        //     img[i] = new Image();
        //     img[i].src = this.ordersInfo.params[i];

        //     img[i].onload = function () {
        //         // 第i张加载完成
        //         resolve(img[i]);
        //     };
        //     img[i].onerror = function () {
        //         reject(img[i]);
        //     };
        //   console.log('img', img)
        // });
      }
      
      // const styleSheet = `<style>
      // </style>`; 
      //           newWin.document.head.innerHTML = styleSheet; //给打印的内容加上样式
      newWin.document.close();
      newWin.focus();
      newWin.print();
      newWin.close();
      // 判断全部图片是否加载成功
      // Promise.all(promiseAll).then(img => {
      //     newWin.print();
      //     newWin.close();
      // }).catch(err => {
      //     newWin.close();

      //     // this.$Message.error('图片加载失败');
      // });
    },
    printOrders() {
      const newWin = window.open("");
      // const img = {};
      const print = document.getElementById("printOrders");
      //   img = new Image();
      //     img.src = this.ordersInfo.params[i];
      //     img[i].onload = function () {
      //         // 第i张加载完成
      //         resolve(img[i]);
      //     };
      //     img[i].onerror = function () {
      //         reject(img[i]);
      //     };
      //   console.log('img', img)
      // });
      print.onload = function () {
        return false;
      };
      newWin.document.write(print.outerHTML);
      newWin.document.close();
      newWin.focus();
      newWin.print();
      newWin.close();
    },
    startPrint1() {
      // 拿到打印页面dom节点
      let printNode = this.$refs.printTips;
      if (!printNode) return;
      // 页面文档创建一个空的内框架，用于挂载打印节点，并设置一定的样式
      const newIframe = document.createElement("iframe");
      newIframe.setAttribute(
        "style",
        "width:0px;height:0px;position:absolute;left:-9999px;top:-9999px;"
      );
      newIframe.setAttribute("align", "center");
      document.body.appendChild(newIframe);
      // 将打印页面设置为内框架内容
      let doc = null;
      doc = newIframe.contentWindow.document;
      doc.write(`
    <style type="text/css">
      @media print {
        @page {
          size: auto;
          margin: 1mm; /* 影响打印小票的边缘margin */
          table { page-break-after: auto; }
          tr    { page-break-inside: avoid; page-break-after: auto; }
          td    { page-break-inside: avoid; page-break-after: auto; }
          thead { display: table-header-group; }
          tfoot { display: table-footer-group; }
        }
      }
      /* 打印页面样式 */
      .print-preview {  color: rgba(51, 51, 51, 1); font-family: Microsoft YaHei, '黑体', STSong, '宋体',Consolas, Monaco, Droid, Sans, Mono, Source, Code, Pro, Menlo, Lucida, Sans, Type, Writer, Ubuntu, Mono; }
      .title {  font-size: 21px; height: 20px; line-height: 20px; font-weight: bold; }
      .divide_line { border-bottom: 1px dashed rgba(51, 51, 51, 1); }
      /* 打印页面样式，一个class一个class写，可以.classA .classB {}, 但不要嵌套，不要.classA { .classB {} } */
    </style>
    <div style="width:100%;height:auto;width:320px;margin:0px auto;"align="center">
      ${printNode.innerHTML}
    </div>`);
      doc.close();
      // 浏览器打印页面打开渲染
      setTimeout(() => {
        newIframe.contentWindow.focus();
        newIframe.contentWindow.print();
        document.body.removeChild(newIframe); // 移除打印内框架，下次打印下次再挂载
      }, 10);
    },
    cancelPrint1() {
      this.showTips = false;
    },
    searchOrders() {
      let alllist = this.alllist;
      if (alllist.length > 0) {
        if (this.keyword != "") {
          this.alllist = alllist.filter(
            (item) => item.orderNum.indexOf(this.keyword) > -1
          );
        } else {
          this.getOrdersList();
        }
      }
    },
    getOrdersList() {
      let shopId = localStorage.getItem("shopId");
      homeApi
        .ordersList(this.page, this.limit, shopId, this.curId)
        .then((res) => {
          if (res.data.code == 20000) {
            this.alllist = res.data.data.rows;
            this.total = res.data.data.total;
          }
        });
    },

    getOrdersInfo(id) {
      let adminId = localStorage.getItem("adminId");
      homeApi.ordersInfo(id, adminId).then((res) => {
        if (res.data.code == 20000) {
          this.ordersInfo = res.data.data.info;
          let params = this.ordersInfo.params;
          let arr = [];
          let obj = {};
          params.forEach((item) => {
            if (item.number > 1) {
              let num = item.number;
              for (let index = 0; index < num; index++) {
                obj = item;
                arr.push(obj);
              }
            } else {
              obj = item;
              arr.push(obj);
            }
          });
          this.arr = arr;
        }
      });
    },
    payOrders(data) {
      let that = this;
      homeApi.makeOrders(data.id).then((res) => {
        if (res.data.code == 20000) {
          that.getOrdersInfo(data.id);
          that.getOrdersList();

          // that.$toast({
          //   message: "状态修改为制作中",
          //   position: "top",
          // });
          // 传递给机器
          let orderObj = {};
          orderObj.orderId = data.id;
          orderObj.goodList = [];
          let params = data.params;

          params.forEach((item) => {
            if (item.number > 1) {
              let num = item.number;
              for (let index = 0; index < num; index++) {
                let obj = {};
                obj.spId = item.goodsId;
                obj.goodId = item.id;
                obj.machineNo = item.machineNo;
                let arr = [];
              arr = item.specifications.split("，");
              obj.td = arr[0].indexOf('正常')  > -1 ? 1 : arr[0].indexOf('七分')  > -1 ? 2 : 3;
              obj.wd = arr[1].indexOf('正常')  > -1 ? 1 : arr[1].indexOf('少')  > -1 ? 2 : arr[1].indexOf('常温')  > -1 ? 3 : 4;
                let goodInfo = "";
                if (item.outletList.length > 0) {
                  let outletList = item.outletList;
                  for (let i = 0; i < outletList.length; i++) {
                    goodInfo =
                      goodInfo +
                      outletList[i].useNumber +
                      "-" +
                      outletList[i].outlet +
                      ",";
                  }
                }
                goodInfo =
                  goodInfo.length > 0
                    ? goodInfo.slice(0, goodInfo.length - 1)
                    : goodInfo;
                obj.goodInfo = goodInfo;
                orderObj.goodList.push(obj);
              }
            } else {
              let obj = {};
              obj.spId = item.goodsId
              obj.goodId = item.id;
              obj.machineNo = item.machineNo;
              let arr = [];
              arr = item.specifications.split("，");
              obj.td = arr[0].indexOf('正常')  > -1 ? 1 : arr[0].indexOf('七分')  > -1 ? 2 : 3;
              obj.wd = arr[1].indexOf('正常')  > -1 ? 1 : arr[1].indexOf('少')  > -1 ? 2 : arr[1].indexOf('常温')  > -1 ? 3 : 4;
              let goodInfo = "";
              if (item.outletList.length > 0) {
                let outletList = item.outletList;
                for (let i = 0; i < outletList.length; i++) {
                  goodInfo =
                    goodInfo +
                    outletList[i].useNumber +
                    "-" +
                    outletList[i].outlet +
                    ",";
                }
              }
              goodInfo =
                goodInfo.length > 0
                  ? goodInfo.slice(0, goodInfo.length - 1)
                  : goodInfo;
              obj.goodInfo = goodInfo;
              orderObj.goodList.push(obj);
            }
          });
          sendWSPush("handleOrder:" + JSON.stringify(orderObj));
          //  let msg = {"event":'open_screen',"to_user":this.userId}
          //
        }
      });
    },

    finishOrders(data) {
      homeApi.finishOrders(data.id).then((res) => {
        if (res.data.code == 20000) {
          this.getOrdersInfo(data.id);
          this.getOrdersList();
        }
      });
    },
    // 返回
    goBack: function () {
      this.$router.go(-1);
    },

    //选择订单
    onCheck(index, id) {
      this.alllist.forEach((item, i) => {
        item.ischeck = i === index;
      });
      this.getOrdersInfo(id);
      this.$forceUpdate();
    },
    onChecka(index, id) {
      this.alllist.forEach((item, i) => {
        item.ischecka = i === index;
      });
      this.getOrdersInfo(id);
      this.$forceUpdate();
    },
    onCheckb(index, id) {
      this.alllist.forEach((item, i) => {
        item.ischeckb = i === index;
      });
      this.getOrdersInfo(id);
      this.$forceUpdate();
    },
  },
});
</script>
    
<style scoped>
.order_box {
  width: 100%;
  height: 100%;
  margin: auto;
  overflow: hidden;
  background: #f2f3f4;
  padding-bottom: 22px;
}
/* 顶部 */
.order_head {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  margin-top: 18px;
  margin-bottom: 17px;
}
.order_back {
  width: 30px;
  height: 30px;
  display: block;
  margin-right: 38px;
  margin-left: 56px;
  cursor: pointer;
}
.order_head span {
  font-size: 24px;
  margin-right: 640px;
}
.order_searchk {
  width: 300px;
  height: 40px;
  border: 1px solid #999999;
  display: flex;
  align-items: center;
  border-radius: 8px;
}
.order_searchk img {
  width: 24px;
  height: 24px;
  display: block;
  margin: 0 8px;
}
.order_searchk input {
  width: 250px;
  height: 38px;
  font-size: 20px;
  text-align: left;
}
/* 内容 */
.order_container {
  width: 100%;
  display: flex;
  justify-content: center;
}
.order_left {
  width: 35%;
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 11px;
}
#tab {
  width: 100%;
  margin: 0 auto;
}
.order_hang {
  width: 100%;
  height: 75px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: absolute;
  top: 0;
}
.tab-tit {
  font-size: 0;
  width: 100%;
}
.tab-tit a {
  display: inline-block;
  height: 75px;
  line-height: 75px;
  font-size: 24px;
  width: 33.3%;
  text-align: center;
  color: #666666;
  text-decoration: none;
  cursor: pointer;
  font-family: PingFangSCMedium-Medium;
  position: relative;
}
.tab-tit .cur {
  font-family: PingFangSCBold-Bold;
  color: #0256ff;
}
.tab-tit a div {
  width: 48px;
  height: 4px;
  margin: auto;
  border-radius: 20px;
  background: #ffffff;
  position: absolute;
  bottom: 15px;
  left: 0;
  right: 0;
}
.tab-tit a div.order_xian {
  background: #0256ff;
}
.order_ul {
  width: 100%;
  margin: auto;
  overflow: hidden;
  overflow-y: scroll;
  padding-top: 80px;
  padding-bottom: 5px;
}
.order_itemk {
  width: 632px;
  border: 2px solid #bfbfbf;
  height: 120px;
  margin: auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  cursor: pointer;
  border-radius: 8px;
  background: #ffffff;
}
.active {
  border: 2px solid #0256ff;
  background: rgba(224, 234, 255, 0.4);
}
.order_pic {
  width: 45px;
  height: 45px;
  display: block;
  border-radius: 50%;
  margin: 0 20px;
}
.order_lp {
  margin-right: auto;
}
.order_number {
  font-size: 28px;
  line-height: 39px;
  margin-bottom: 7px;
}
.order_ordernum {
  font-size: 20px;
}
.order_rp {
  margin: 0 20px;
  text-align: right;
  line-height: 28px;
}
.order_price {
  font-size: 20px;
}
.order_state {
  font-size: 20px;
}
.order_time {
  font-size: 20px;
}
/* 右部分 */
.order_right {
  width: 62%;
  border-radius: 8px;
}
.order_header {
  width: 100%;
  height: 66px;
  font-size: 28px;
  border-bottom: 1px solid #d8d8d8;
  line-height: 67px;
  padding-left: 23px;
}
.order_bottom {
  width: 100%;
  margin: auto;
  display: flex;
  border-bottom: 1px solid #d8d8d8;
}
.order_bottom_l {
  width: 50%;
  border-right: 1px solid #d8d8d8;
  padding-top: 18px;
}
.order_sigle {
  font-size: 22px;
  line-height: 31px;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.ddje {
  font-size: 26px;
}
.ypyj {
  font-size: 24px;
}
.hjhj {
  font-size: 24px;
}
.shangpinmingcheng {
  font-size: 22px;
  font-family: PingFangSCBold-Bold;
  margin-bottom: 9px;
}
.zuofass {
  margin-left: 16.5px;
  font-size: 18px;
  font-family: PingFangSCBold-Bold;
}
.order_zhuangtai {
  font-size: 22px;
  font-family: PingFangSCBold-Bold;
}
.shuliangs {
  font-size: 22px;
}
.jjjq {
  font-size: 22px;
}
.order_hui {
  font-size: 20px;
  padding: 0 20px;
  line-height: 28px;
}
.order_mess {
  margin: 25px 0;
  height: 60px;
  line-height: 60px;
  font-size: 26px;
  padding-left: 22px;
  background: rgba(184, 218, 245, 0.23);
  font-family: PingFangSCBold-Bold;
}
.order_lineheight {
  margin: 10px 0;
}
.order_bottom_r {
  width: 50%;
}
.order_table {
  width: 545px;
  height: 30px;
  margin: 19px auto;
  background: rgba(184, 218, 245, 0.23);
  line-height: 30px;
  font-size: 20px;
  padding-left: 9px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.order_yinpin {
  width: 545px;
  margin: auto;
  overflow: hidden;
  overflow-y: scroll;
}
.order_yinpin li {
  padding-left: 9px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.order_di {
  width: 100%;
  height: 87px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding-right: 20px;
}
.order_di button {
  width: 175px;
  height: 57px;
  border-radius: 8px;
  background: #0256ff;
  font-size: 24px;
  text-align: center;
  line-height: 57px;
  margin: 0;
  padding: 0;
}
.order_di button.dayin{
  margin-right: 16px;
}
/* 打印小票 */

.receipt_kuang {
  width: 100%;
  margin: auto;
}
/* 订单票 */
.receipt_a {
  width: 250px;
  height: auto;
  padding: 10px;
  float: left;
  margin: 15px;
}
.goodsxq {
  width: 100%;
  margin-bottom: 15px;
}
.goodsxq div {
  font-size: 14px;
}
.goodsxq div.goodsname {
  font-size: 20px;
  font-weight: 600;
}
/* 贴纸 */
.receipt_b {
  width: 280px;
  height: auto;
  padding: 10px;
  border-radius: 10px;
  float: left;
  margin: 15px;
}
</style>