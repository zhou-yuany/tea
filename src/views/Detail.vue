<template>
  <div class="xq_box">
    <!-- 选项卡 -->
    <ul class="tabs darkwhite_bg" ref="gettabheight" v-show="orderNo == ''">
      <li
        class="tab"
        v-for="(item, index) in tabs"
        :key="index"
        :class="{ active: current == item.id }"
        @click="handle(item.id)"
      >
        <img :src="item.id == current ? item.src : item.path" alt="" />
        <p>{{ item.title }}</p>
      </li>
    </ul>
    <!-- 内容 -->
    <div class="con">
      <!-- 商品 竖屏 -->
      <div
        class="con_boxas"
        v-if="current == 0 && !ishiddens && !ismake && isScreen == true"
        :style="{ height: height + 'px' }"
      >
        <ul class="good_tab">
          <li
            @click="onChangea(index)"
            :class="{ actiona: curIndex == index }"
            v-for="(item, index) in goodList"
            :key="index"
          >
            {{ item.name }}
          </li>
        </ul>
        <div
          :class="{ content: true, actiona: curIndex == index }"
          v-for="(item, index) in goodList"
          :key="index"
        >
          <div
            v-for="(childrenItem, childrenIndex) in item.children"
            :key="childrenIndex"
            class="itemk"
            :class="{ currenta: childrenIndex == currenta }"
            @click="goLook(childrenIndex, childrenItem)"
          >
            {{ childrenItem.name }}
          </div>
        </div>
      </div>
      <!-- 商品 横屏 -->
      <div
        class="con_boxas"
        v-if="current == 0 && !ishiddens && !ismake && isScreen == false"
        :style="{ height: height + 'px' }"
      >
        <ul class="good_tab">
          <li
            @click="getNum(index)"
            :class="{ actiona: curIndex == index }"
            v-for="(item, index) in goodList"
            :key="index"
          >
            {{ item.name }}
          </li>
        </ul>
        <ul
          class="scroll-container"
          v-for="(item, index) in goodList"
          :key="index"
          v-show="index == curIndex"
        >
          <li
            v-for="(childrenItem, childrenIndex) in item.children"
            :key="childrenIndex"
            class="list-item"
            :class="{ currenta: childrenIndex == currenta }"
            @click="goLook(childrenIndex, childrenItem)"
          >
            {{ childrenItem.name }}
          </li>
        </ul>
        <img
          src="../../static/leftico.png"
          class="leftico"
          alt=""
          :style="{ top: heightico / 2 - 63 + 'px' }"
        />
        <img
          src="../../static/rightico.png"
          class="rightico"
          alt=""
          :style="{ top: heightico / 2 - 63 + 'px' }"
        />
      </div>
      <div
        class="con_boxb"
        v-if="ishiddens"
        :style="{ height: height + 'px' }"
        style="overflow: hidden"
      >
        <div class="backbtns">
          <p class="blue">{{ goodsName }}</p>
          <p style="margin-right: auto">选择做法</p>
          <button
            type="button"
            class="lightblue_bg white"
            @click="goBackgood()"
          >
            返回
          </button>
        </div>
        <div
          class="xqks"
          :style="{ height: xqheight + 'px' }"
          v-if="isScreen == true"
        >
          <div class="xqk_bt">规格</div>
          <ul class="lista">
            <li
              v-for="(item, index) in guige"
              :key="index"
              @click="currentsp = index"
              :class="{ xz: index == currentsp }"
            >
              <p>{{ item.name }}</p>
              <p>{{ item.size }}</p>
            </li>
            <div style="clear: both"></div>
          </ul>
          <div class="xqk_bt">
            温度（必选
            <p class="red">1</p>
            ）
          </div>
          <ul class="listb">
            <li
              v-for="(item, index) in temperature"
              :key="index"
              @click="currentspa = index"
              :class="{ xz: index == currentspa }"
            >
              {{ item.name }}
            </li>
            <div style="clear: both"></div>
          </ul>
          <div class="xqk_bt">
            甜度（必选
            <p class="red">1</p>
            ）
          </div>
          <ul class="listb">
            <li
              v-for="(item, index) in sweet"
              :key="index"
              @click="currentspb = index"
              :class="{ xz: index == currentspb }"
            >
              {{ item.name }}
            </li>
            <div style="clear: both"></div>
          </ul>
          <div class="annv">
            <button type="button" class="white lightblue_bg" @click="onMake()">
              确定制作
            </button>
          </div>
          <div style="width: 100%; height: 40px; margin: auto"></div>
        </div>
        <div class="xqks" :style="{ height: xqheight + 'px' }" v-else>
          <div style="display: flex">
            <div class="xqk_bt">规格</div>
            <ul class="lista">
              <li
                v-for="(item, index) in guige"
                :key="index"
                @click="currentsp = index"
                :class="{ xz: index == currentsp }"
              >
                <p>{{ item.name }}</p>
                <p>{{ item.size }}</p>
              </li>
              <div style="clear: both"></div>
            </ul>
          </div>
          <div style="display: flex">
            <div class="xqk_bt">
              温度（必选
              <p class="red">1</p>
              ）
            </div>
            <ul class="listb">
              <li
                v-for="(item, index) in temperature"
                :key="index"
                @click="currentspa = index"
                :class="{ xz: index == currentspa }"
              >
                {{ item.name }}
              </li>
              <div style="clear: both"></div>
            </ul>
          </div>
          <div style="display: flex">
            <div class="xqk_bt">
              甜度（必选
              <p class="red">1</p>
              ）
            </div>
            <ul class="listb">
              <li
                v-for="(item, index) in sweet"
                :key="index"
                @click="currentspb = index"
                :class="{ xz: index == currentspb }"
              >
                {{ item.name }}
              </li>
              <div style="clear: both"></div>
            </ul>
          </div>
          <div class="annv">
            <button type="button" class="white lightblue_bg" @click="onMake()">
              确定制作
            </button>
          </div>
          <!-- <div style="width: 100%;height: 40px;margin: auto;"></div> -->
        </div>
      </div>
      <div
        class="con_boxb"
        v-if="ismake"
        :style="{ height: height + 'px' }"
        style="overflow: hidden"
      >
        <div class="hangg">
          <div class="hangd">
            <p class="blue">{{ goodsName }}</p>
            <p class="gray">做法：{{ makeWay }}</p>
          </div>
          <button
            type="button"
            class="lightblue_bg white"
            @click="goBackgood()"
            v-if="!ismaking"
          >
            返回
          </button>
        </div>
        <div v-if="ismaking">
          <div class="pick"><img src="../../static/making.gif" alt="" /></div>
          <div class="blue tips">
            <p>制作中</p>
            <p>请耐心等待</p>
          </div>
        </div>
        <div v-else>
          <div class="pick"><img src="/mini/static/pic.png" alt="" /></div>
          <div class="blue tips">
            <p>制作完成</p>
            <p>请取走饮品</p>
          </div>
          <div class="aginbtn">
            <button type="button" class="white lightblue_bg" @click="onMake()">
              再来一杯
            </button>
          </div>
        </div>
      </div>
      <!-- 订单 竖屏-->
      <div
        class="con_boxa"
        v-if="current == 1 && !ishidden && isScreen == true"
        :style="{ height: height + 'px' }"
      >
        <ul class="good_tab">
          <li
            @click="onChangeb(index)"
            :class="{ actionb: curIndexb == index }"
            v-for="(item, index) in orderList"
            :key="index"
          >
            {{ item.name }}
          </li>
        </ul>
        <div
          :class="{ contentb: true, actionb: curIndexb == index1 }"
          v-for="(item, index1) in orderList"
          :key="index1"
        >
          <div
            v-for="(item, index) in item.allList"
            :key="index"
            class="itemks"
            @click="onOrder(index, item)"
            :class="item.orderStatus == 0 ? 'currentb' : ''"
          >
            <div class="bq darkblue_bg white" v-if="item.orderStatus < 2">
              待
            </div>
            <div class="bq green_bg white" v-if="item.orderStatus > 1">完</div>
            <div class="order_right">
              <div class="hanga">
                <div class="number">取餐号{{ item.pickupCode }}</div>
                <div class="hanga_r">
                  <p>￥{{ item.totalPrice }}</p>
                  <p class="blue" v-if="item.platformType == 2">饿了么</p>
                  <p class="orange" v-if="item.platformType == 3">美团</p>
                  <p v-if="item.platformType == 1">小程序</p>
                </div>
              </div>
              <div class="hangb gray">
                <p v-if="item.orderNum">
                  订单尾号：{{
                    item.orderNum.substring(
                      item.orderNum.length - 4,
                      item.orderNum.length
                    )
                  }}
                </p>
                <p>{{ item.createTime }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 订单 横屏-->
      <div
        class="con_boxas"
        v-if="current == 1 && !ishidden && isScreen == false"
        :style="{ height: height + 'px' }"
      >
        <ul class="good_tab">
          <li
            @click="onChangeb(index)"
            :class="{ actionb: curIndexb == index }"
            v-for="(item, index) in orderList"
            :key="index"
          >
            {{ item.name }}
          </li>
        </ul>
        <div
          :class="{ contentb: true, actionb: curIndexb == index1 }"
          v-for="(item, index1) in orderList"
          :key="index1"
        >
          <div
            v-for="(item, index) in item.allList"
            :key="index"
            class="itemks"
            @click="onOrder(index, item)"
            :class="item.orderStatus == 0 ? 'currentb' : ''"
          >
            <div class="bq darkblue_bg white" v-if="item.orderStatus < 2">
              待
            </div>
            <div class="bq green_bg white" v-if="item.orderStatus > 1">完</div>
            <div class="order_right">
              <div class="hanga">
                <div class="number">取餐号{{ item.pickupCode }}</div>
                <div class="hanga_r">
                  <p>￥{{ item.totalPrice }}</p>
                  <p class="blue" v-if="item.platformType == 2">饿了么</p>
                  <p class="orange" v-if="item.platformType == 3">美团</p>
                  <p v-if="item.platformType == 1">小程序</p>
                </div>
              </div>
              <div class="hangb gray">
                <p v-if="item.orderNum">
                  订单尾号：{{
                    item.orderNum.substring(
                      item.orderNum.length - 4,
                      item.orderNum.length
                    )
                  }}
                </p>
                <p>{{ item.createTime }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div
        class="con_boxb"
        v-if="ishidden"
        :style="{ height: height + 'px' }"
        style="overflow: hidden"
      >
        <div class="backbtn">
          <button type="button" class="white_bg" @click="goBackorder()">
            返回
          </button>
        </div>
        <div class="xqk" :style="{ height: xqheight + 'px' }">
          <div class="xq_title">饮品 信息</div>
          <div class="table_top dark_gray">
            <div class="diva">饮品名称</div>
            <div class="divb">数量</div>
            <div class="divc"></div>
          </div>
          <ul class="xqlist" :style="{ height: ypheight + 'px' }">
            <li v-for="(goods, index) in arrParams" :key="index">
              <div class="zuo">
                <div class="xqname">
                  {{ goods.name
                  }}{{
                    goods.specifications.indexOf("杯") > -1
                      ? "（" + goods.specifications.substring(0, 2) + "）"
                      : ""
                  }}
                </div>
                <div class="gray xqzf">
                  做法：{{
                    goods.specifications.indexOf("杯") > -1
                      ? goods.specifications.substring(3)
                      : goods.specifications
                  }}
                </div>
              </div>
              <div class="you dark_gray">x{{ goods.number }}</div>
              <div class="zuiyou">
                <button
                  type="button"
                  class="lightblue_bg white"
                  @click="startMake(goods, index)"
                >
                  {{ goods.makeStatus == 2 ? "重做" : "制作" }}
                </button>
              </div>
              <div v-if="goods.makeStatus == 2" class="zuiyou red">
                <span>已完成</span>
              </div>
            </li>
          </ul>
          <!-- 灰层制作中 -->
          <div class="mask" v-if="ismakings">
            <div class="pick" style="margin-top: 2rem">
              <img src="../../static/making.gif" alt="" />
            </div>
            <div class="white tips">
              <p>制作中</p>
              <p>请耐心等待</p>
            </div>
          </div>
        </div>
      </div>
      <!-- 流水 -->
      <div class="con_boxc" v-if="current == 2">
        <div class="searchk">
          <div class="searchk_l gray_bg">
            <img src="../../static/search.png" alt="" @click="goSure" />
            <input
              type="text"
              class="gray_bg"
              placeholder="请输入订单编号"
              v-model="accountQuery.keyword"
            />
          </div>
          <img
            src="../../static/list.png"
            alt=""
            class="listico"
            @click="showScreen()"
          />
        </div>
        <div class="turnover">
          <!-- 筛选 -->
          <div class="graycs" v-if="isscreen">
            <div class="white_bg screen">
              <div class="screen_bt">时间范围</div>
              <el-form :inline="true" class="demo-form-inline">
                <el-form-item label="" style="margin-bottom: 0">
                  <el-date-picker
                    v-model="accountQuery.begin"
                    type="datetime"
                    placeholder="请选择日期"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    default-time="00:00:00"
                    :append-to-body="false"
                  ></el-date-picker>
                </el-form-item>
                <el-form-item label="-" style="margin-bottom: 0">
                  <el-date-picker
                    v-model="accountQuery.end"
                    type="datetime"
                    placeholder="请选择日期"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    default-time="00:00:00"
                    :append-to-body="false"
                  ></el-date-picker>
                </el-form-item>
              </el-form>
              <div class="btnzu">
                <button type="button" class="white_bg" @click="goReset()">
                  重置
                </button>
                <button
                  type="button"
                  class="lightblue_bg white"
                  @click="goSure()"
                >
                  确认
                </button>
              </div>
            </div>
          </div>
          <div class="blue income">
            <span>总制作数量：{{ cupCount }}杯</span>
            <span>总收入：{{ banlance }}元</span>
          </div>
          <!-- 列表 -->
          <div class="tablek" :style="{ height: turnoverheight + 'px' }">
            <el-table
              border
              :data="tableData"
              style="width: 92%; margin: auto"
              :header-cell-style="{ background: '#EFF6FD' }"
            >
              <el-table-column prop="time" label="时间">
                <template slot-scope="scope">
                  <span>{{ scope.row.createTime }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="type" label="类型">
                <template slot-scope="scope">
                  <span class="blue">{{
                    scope.row.type == 1 ? "入账" : "出账"
                  }}</span>
                </template>
                <el-table-column prop="type" label="平台">
                  <template slot-scope="scope">
                    <span class="blue">{{
                      scope.row.payPlatform == 0
                        ? "茶饮小程序"
                        : scope.row.payPlatform == 1
                        ? "饿了么"
                        : scope.row.payPlatform == 2
                        ? "美团"
                        : "线下"
                    }}</span>
                  </template>
                </el-table-column>
              </el-table-column>

              <el-table-column prop="shopDistributionPrice" label="金额">
                <template slot-scope="scope">
                  <span>{{ scope.row.shopDistributionPrice }}</span>
                </template>
              </el-table-column>
              <el-table-column label="做法详情">
                <template slot-scope="scope">
                  <span class="blue" @click="goXqs(scope.row)">做法详情</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <!-- 做法详情 -->
        <div class="grayc" v-if="isStream">
          <div class="white_bg whiteks">
            <div class="tc_title">
              <p>做法详情</p>
              <img src="../../static/x.png" alt="" @click="goCloses()" />
            </div>
            <div class="jiequ">
              <div v-for="(item, index) in makeParams" :key="index">
                <div class="pfnames">{{ item.name }}</div>
                <div style="display: flex">
                  <div
                    :class="
                      item.params.length == cIndex + 1
                        ? 'pfbeis dark_gray'
                        : 'pfnamess dark_gray'
                    "
                    v-for="(param, cIndex) in item.params"
                    :key="cIndex"
                  >
                    {{ param }}
                  </div>
                </div>
              </div>
            </div>
            <div class="closebtn">
              <button type="button" class="white_bg" @click="goCloses()">
                关闭
              </button>
            </div>
          </div>
        </div>
      </div>
      <!-- 原材料 -->
      <div class="con_boxd" v-if="current == 3">
        <div class="searchk">
          <div class="searchk_l gray_bg">
            <img src="../../static/search.png" alt="" @click="getBatchList()" />
            <input
              type="text"
              class="gray_bg"
              placeholder="请输入要查询的内容"
              v-model="keyword"
            />
          </div>
          <div class="lightblue_bg white searchbtn" @click="getBatchList()">
            搜索
          </div>
        </div>
        <div class="zhanwei"></div>
        <div class="addbtn">
          <button type="button" class="white" @click="onAdd()">新建</button>
        </div>
        <div class="tablek" :style="{ height: materialheight + 'px' }">
          <el-table
            border
            :data="ytableData"
            style="width: 92%; margin: auto"
            :header-cell-style="{ background: '#EFF6FD' }"
          >
            <el-table-column
              prop="brand"
              label="序号"
              type="index"
              width="90"
            ></el-table-column>
            <el-table-column prop="batchName" label="配料"> </el-table-column>
            <el-table-column prop="outlet" label="出料口"> </el-table-column>
            <el-table-column prop="totalCount" label="剩余容量">
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <span class="blue editbtn" @click="onUpdate(scope.row)"
                  >编辑</span
                >
                <span class="red editbtn" @click="onDell(scope.row)">删除</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!-- 新建/修改表单 -->
        <div class="grayc" v-if="isAdd">
          <div class="white_bg whitek">
            <div class="tc_title">
              <p>原材料</p>
              <img src="/mini/static/x.png" alt="" @click="onGuanbi()" />
            </div>
            <div class="hangf">
              <div class="mings">配料</div>
              <el-select
                v-model="batchInfo.batchId"
                placeholder="请选择"
                style="width: 50%"
              >
                <el-option
                  :disabled="batchInfo.id > 0"
                  v-for="item in allBatchs"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </div>
            <div class="hangf">
              <div class="mings">出料口</div>
              <el-input
                type="text"
                v-model="batchInfo.outlet"
                placeholder="请输入"
                style="width: 50%"
              ></el-input>
            </div>
            <div class="hangf">
              <div class="mings">总数量</div>
              <el-input
                type="text"
                v-model="batchInfo.totalCount"
                placeholder="请输入"
                onkeyup="this.value=this.value.replace(/^(0+)|[^\d]+/g,'')"
                style="width: 50%"
              ></el-input>
            </div>
            <div class="closebtn">
              <button
                type="button"
                class="lightblue_bg white"
                @click="saveBatch()"
              >
                确定
              </button>
            </div>
          </div>
        </div>
      </div>
      <!-- 配方 -->
      <div class="con_boxe" v-if="current == 4">
        <div class="searchk">
          <div class="searchk_l gray_bg">
            <img src="/mini/static/search.png" alt="" @click="goSures()" />
            <input
              type="text"
              class="gray_bg"
              placeholder="请输入要查询的内容"
              v-model="goodsBatchQuery.keyword"
            />
          </div>
          <img
            src="/mini/static/list.png"
            alt=""
            class="listico"
            @click="showScreens()"
          />
        </div>
        <div style="position: relative">
          <!-- 筛选 -->
          <div class="graycs" v-if="isshowscreen">
            <div class="white_bg screen">
              <!-- <div class="hangc">
                <div class="ming">配方类型</div>
                <el-select
                  v-model="goodsBatchQuery"
                  placeholder="请选择"
                  style="width: 50%"
                >
                  <el-option
                    v-for="item in typeList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </div> -->
              <div class="hangc">
                <div class="ming">配方规格</div>
                <el-select
                  v-model="goodsBatchQuery.sizeId"
                  placeholder="请选择"
                  style="width: 50%"
                >
                  <el-option
                    v-for="item in cupSizes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </div>
              <div class="btnzu">
                <button type="button" class="white_bg" @click="goResets()">
                  重置
                </button>
                <button
                  type="button"
                  class="lightblue_bg white"
                  @click="goSures()"
                >
                  确认
                </button>
              </div>
            </div>
          </div>
          <div class="zhanwei"></div>
          <div class="tablek" :style="{ height: formulaheight + 'px' }">
            <el-table
              border
              :data="ptableData"
              style="width: 92%; margin: auto"
              :header-cell-style="{ background: '#EFF6FD' }"
            >
              <el-table-column
                label="序号"
                type="index"
                width="80"
              ></el-table-column>
              <el-table-column prop="goodsName" label="产品名称">
              </el-table-column>
              <el-table-column prop="paramName" label="规格"> </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <span class="blue" @click="goXq(scope.row)">详情</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
        <!-- 配方详情 -->
        <div class="grayc" v-if="isFormula">
          <div class="white_bg whitek">
            <div class="tc_title">
              <p>配方</p>
              <img src="/mini/static/x.png" alt="" @click="goClose()" />
            </div>
            <div class="pfname">{{ goodBatch.goodsName }}</div>
            <div class="pfbei">{{ goodBatch.sizeName }}</div>
            <el-table
              border
              :data="goodBatch.goodsToBatchList"
              style="width: 86%; margin: auto"
              :header-cell-style="{ background: '#DFDFDF' }"
            >
              <el-table-column label="商品配料">
                <template slot-scope="scope">
                  <span>{{ scope.row.batchName }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="number" label="配比数量">
                <template slot-scope="scope">
                  <span>{{ scope.row.useNumber }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="calculate" label="核算单位">
                <template slot-scope="scope">
                  <span>{{ scope.row.unit }}</span>
                </template>
              </el-table-column>
            </el-table>
            <div class="closebtn">
              <button type="button" class="white_bg" @click="goClose()">
                关闭
              </button>
            </div>
          </div>
        </div>
      </div>
      <!-- 物料消耗 -->
      <div class="con_boxf" v-if="current == 5">
        <div class="searchk">
          <div class="searchk_l gray_bg">
            <img src="/mini/static/search.png" alt="" @click="goSurea" />
            <input
              type="text"
              class="gray_bg"
              placeholder="请输入订单编号"
              v-model="batchUseQuery.keyword"
            />
          </div>
          <img
            src="/mini/static/list.png"
            alt=""
            class="listico"
            @click="showScreena()"
          />
        </div>
        <div class="turnover">
          <!-- 筛选 -->
          <div class="graycs" v-if="isscreena">
            <div class="white_bg screens">
              <div class="screen_bt">时间范围</div>
              <el-form
                :inline="true"
                class="demo-form-inline"
                style="display: flex; align-items: center"
              >
                <el-form-item label="" style="margin-bottom: 0">
                  <el-date-picker
                    v-model="batchUseQuery.begin"
                    type="datetime"
                    placeholder="请选择日期"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    default-time="00:00:00"
                    :append-to-body="false"
                  ></el-date-picker>
                </el-form-item>
                <el-form-item label="-" style="margin-bottom: 0">
                  <el-date-picker
                    v-model="batchUseQuery.end"
                    type="datetime"
                    placeholder="请选择日期"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    default-time="00:00:00"
                    :append-to-body="false"
                  ></el-date-picker>
                </el-form-item>
              </el-form>
              <div class="screen_bts">物料名称</div>
              <el-input
                v-model="batchUseQuery.name"
                placeholder="请输入物料名称"
                style="width: 50%"
                class="inputk"
              ></el-input>
              <div class="btnzu">
                <button type="button" class="white_bg" @click="goReseta()">
                  重置
                </button>
                <button
                  type="button"
                  class="lightblue_bg white"
                  @click="goSurea()"
                >
                  确认
                </button>
              </div>
            </div>
          </div>
          <div class="zhanwei"></div>
          <div
            class="addbtn"
            style="color: #409eff; font-size: 20px; height: 40px"
          >
            总消耗：{{ totalUse }}ml
          </div>
          <!-- 表格 -->
          <div class="tablek" :style="{ height: materialheight + 'px' }">
            <el-table
              border
              :data="batchUseList"
              style="width: 92%; margin: auto"
              :header-cell-style="{ background: '#EFF6FD' }"
            >
              <el-table-column
                prop="brand"
                label="序号"
                type="index"
                width="90"
              ></el-table-column>
              <el-table-column
                prop="batchName"
                label="物料名"
              ></el-table-column>
              <el-table-column
                prop="useCount"
                label="消耗(ml)"
                width="90"
              ></el-table-column>
              <el-table-column
                prop="createTime"
                label="时间"
                width="150"
                :formatter="dateFormat"
              ></el-table-column>
              <el-table-column prop="orderNum" label="订单号"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </div>

    <!-- 打印小票 -->
    <div class="mask receipt_kuang" v-show="false">
      <!-- 订单票 -->
      <div class="receipt_a white_bg" id="printOrders" style="zoom: 0.8">
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
          收货人：{{ ordersInfo.consignee }}（{{
            ordersInfo.sex == 1 ? "男士" : "女士"
          }}）{{ ordersInfo.receiverPhone }}
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
          赠予者：{{
            ordersInfo.isAnonymous == 1 ? "匿名用户" : ordersInfo.giver
          }}
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
          赠予者留言：{{ ordersInfo.giverNotes }}
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
      <!-- <div
        class="receipt_b white_bg"
        style="page-break-after: always;"
        id="printTips"
      >
        <div>
          <div class="qucanh" style="margin-bottom: 5px;">
            <div style="width: 100%;font-size: 14px;">小程序</div>
            <div style="width: 100%;font-size: 14px;">自提</div>
            <div class="large" style="width: 100%;font-size: 20px;font-weight: 600;">取餐号：{{ stickersInfo.pickupCode }}</div>
          </div>
          <div class="goodsxqs" style="width: 100%;margin-bottom: 5px;">
            <div class="goodsname" style="font-size: 14px;font-weight: 600;">{{ stickersInfo.name }}</div>
             <div style="font-size: 14px;" v-if="stickersInfo.specifications != null && stickersInfo.specifications != ''">
              {{
                stickersInfo.specifications.indexOf("杯") > -1
                  ? "（" + stickersInfo.specifications.substring(0, 2) + "）"
                  : ""
              }}
            </div>
            <div style="font-size: 14px;" v-if="stickersInfo.specifications != null && stickersInfo.specifications != ''">
              {{
                stickersInfo.specifications.indexOf("杯") > -1
                  ? stickersInfo.specifications.substring(3)
                  : stickersInfo.specifications
              }}
            </div>
          </div>
          <div class="order_time">
            <span style="font-size: 14px;margin-right: 10px;">{{
              ordersInfo.createTime
                ? ordersInfo.createTime.substring(5, 10)
                : ""
            }}</span
            ><span style="font-size: 14px;margin-right: 10px;">{{
              ordersInfo.createTime
                ? ordersInfo.createTime.substring(10, 19)
                : ""
            }}</span>
          </div>
          <div class="order_shopname" style="font-size: 14px;">{{ stickersInfo.shopName }}</div>
          <div v-if="stickersInfo.notes != null && stickersInfo.notes != ''" class="order_beizhu" style="font-size: 16px;">备注：{{ stickersInfo.notes }}</div>
        </div>
      </div> -->
    </div>
  </div>
</template>
  
<script>
import Api from "@/api/api";
import homeApi from "@/api/home/index";
import moment from "moment";
import { defineComponent } from "vue";
import { getLodop } from "@/assets/js/LodopFuncs";
import {
  createSocket,
  createSocket2,
  sendWSPush,
  closeWs,
  closeWs2,
} from "./websocket/websocket.js";
import store from "../store";
export default defineComponent({
  data() {
    return {
      current: 0,
      goodsName: "",
      makeWay: "",
      stickersInfo: {}, // 贴纸小票
      tabs: [
        {
          id: 0,
          title: "商品",
          path: "/mini/static/ico6.png",
          src: "/mini/static/ico6_on.png",
        },
        // {
        //   id: 1,
        //   title: "订单",
        //   path: "/static/ico7.png",
        //   src: "/static/ico7_on.png",
        // },
        // {
        //   id: 2,
        //   title: "流水",
        //   path: "/static/ico8.png",
        //   src: "/static/ico8_on.png",
        // },
        {
          id: 3,
          title: "原材料",
          path: "/mini/static/ico9.png",
          src: "/mini/static/ico9_on.png",
        },
        {
          id: 5,
          title: "物料消耗",
          path: "/mini/static/ico10.png",
          src: "/mini/static/ico10_on.png",
        },
        // {
        //   id: 4,
        //   title: "配方",
        //   path: "/static/ico10.png",
        //   src: "/static/ico10_on.png",
        // },
      ],
      banlance: 0,
      goodList: [],
      batchUseList: [],
      totalUse: 0,
      getsocketData: "",
      goodsInfo: {},
      curIndex: 0,
      currenta: -1,
      height: 0,
      heightico: 0,
      ishiddens: false,
      arrParams: [],
      ordersInfo: {},
      guige: [],
      currentsp: 0,
      temperature: [],
      currentspa: 0,
      formulaheight: 0,
      sweet: [],
      currentspb: 0,
      ismake: false,
      ismaking: false,
      ismakings: false,
      orderList: [],
      curIndexb: 0,
      currentb: 0,
      ishidden: false,
      xqheight: 0,
      ypheight: 0,
      turnoverheight: 0, //流水
      cupCount: 0,
      isStream: false,
      tableData: [],
      allBatchs: [],
      isscreen: false,
      accountQuery: {},
      makeParams: [],
      materialheight: 0,
      keyword: "",
      ytableData: [],
      ptableData: [],
      goodBatch: {
        id: "",
        goodsId: "",
        goodsToBatchList: [{ batchId: "", useNumber: "" }],
      },
      isshowscreen: false,
      selectedCityId: "",
      typeList: [
        {
          id: 1,
          name: "类型一",
        },
        {
          id: 2,
          name: "类型二",
        },
      ],
      cupSizes: [],
      goodsBatchQuery: {},
      isFormula: false,
      formulaData: [
        {
          id: 1,
          batching: "",
          number: "",
          calculate: "",
        },
        {
          id: 2,
          batching: "",
          number: "",
          calculate: "",
        },
      ],
      isAdd: false,
      batchInfo: {},
      materialName: "",
      batchUseQuery: {},
      isscreena: false,
      wtableData: [],
      screenHW: window.orientation, // 判断横竖屏
      isScreen: true, // 横竖屏
      orderNo: "",
    };
  },
  mounted() {
    this.screenHW = window.orientation; //保证刷新时获取状态准确

    // 监听窗口大小
    window.onresize = () => {
      return (() => {
        this.screenHW = window.orientation; // 把横竖屏的变化赋值
      })();
    };

    let tabheight = this.$refs.gettabheight.offsetHeight;
    let windowheight = window.innerHeight;

    this.heightico = windowheight;

    this.height = windowheight - localStorage.getItem("touheight") - 4;

    this.xqheight = windowheight - localStorage.getItem("touheight") - 74;

    this.ypheight = windowheight - localStorage.getItem("touheight") - 150;

    this.turnoverheight =
      windowheight - localStorage.getItem("touheight") - 176;

    this.materialheight =
      windowheight - localStorage.getItem("touheight") - 196;

    this.formulaheight = windowheight - localStorage.getItem("touheight") - 176;
  },
  watch: {
    screenHW: {
      immediate: true,
      handler(newval, old) {
        this.rotate();
      },
    },
  },
  created() {
    // if(this.divideAccounts != 2){
    //   this.tabs.shift();
    // }
    this.ismake = false;
    this.ismaking = false;
    this.ismakings = false;
    this.current = this.$route.query.current;
    this.orderNo =
      this.$route.query.orderNo != undefined ? this.$route.query.orderNo : "";
    if (this.current == 0) {
      this.getCateAndGoods();
      this.getParamList();
    } else if (this.current == 1) {
      if (this.orderNo != "") {
        this.ishidden = true;
        this.getOrdersInfoBy(this.orderNo);
      }
      this.getOrderList();
    } else if (this.current == 2) {
      this.ordersFlowList();
    } else if (this.current == 3) {
      this.getBatchList();
      createSocket();
      createSocket2();
      // createSocket('wss://tea.yinghai-tec.com/tea-management/platSocket/');
      this.getsocketData = (e) => {
        // 创建接收消息函数
        let data = e && e.detail.data;
        if (data) {
          // if (data != 'ping' && JSON.parse(data).message == '您有新订单啦') {
          //   // 已经建立了连接
          //   this.getOrderList();
          // }
          if (data != "ping" && JSON.parse(data).message.includes("不足")) {
            // this.getOrderList();
            this.getBatchList();
            // 发出配料不足警告
            // this.playVoice(JSON.parse(data).message+"，请及时添加")
            //   store.dispatch('batchWarnning', JSON.parse(data).message).then(() => {
            //     this.playVoice(JSON.parse(data).message+"，请及时添加")
            // })
          }
          // if(data != 'ping' && JSON.parse(data).message == '禁用'){
          //     localStorage.setItem("balance", -1);
          //   }
          // if(data != 'ping' && JSON.parse(data).message == '您有新订单啦'){
          // 已经建立了连接
          // this.getOrderList();
          // this.playVoice("您有新的订单，请及时处理")
          // }
        }
      };
      // 注册监听事件
      window.addEventListener("onmessageWS", this.getsocketData);
      // 注册监听事件
      window.addEventListener("onmessageWS2", this.getsocketData);
    } else if (this.current == 5) {
      this.getBatchUseList();
    } else {
      this.getCupSizes();
      this.getGoodsBatchings();
    }
  },
  destroyed() {
    closeWs();
    closeWs2();
  },
  methods: {
    dateFormat: function (row, column) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD");
    },
    // 判断横竖屏
    rotate() {
      if (this.screenHW == 180 || this.screenHW == 0) {
        // console.log('竖屏')
        this.isScreen = true;
      } else if (this.screenHW == 90 || this.screenHW == -90) {
        // console.log('横屏')
        this.isScreen = false;
      }
    },

    // 物料消耗列表
    getBatchUseList() {
      var adminId = localStorage.getItem("adminId");
      homeApi.batchUseList(adminId, this.batchUseQuery).then((res) => {
        if (res.code == 20000) {
          this.batchUseList = res.data.list;
          this.totalUse = res.data.totalUse;
        }
      });
    },
    // 语音播报
    playVoice(message) {
      const synth = window.speechSynthesis;
      const speech = new SpeechSynthesisUtterance();
      speech.text = message; // 文字内容
      speech.lang = "zh-CN"; // 使用的语言:中文
      speech.volume = 50; // 声音音量：1
      speech.rate = 1; // 语速：1
      speech.pitch = 1; // 音高：1
      synth.speak(speech); // 播放
    },
    selectBatch(e) {
      this.batchInfo.outlet = this.allBatchs[e].outlet;
    },
    getCupSizes() {
      homeApi.getCupSizes().then((res) => {
        if (res.code == 20000) {
          this.cupSizes = res.data.paramList;
        }
      });
    },
    // 全部商品
    getCateAndGoods() {
      var adminId = localStorage.getItem("adminId");
      homeApi.getCateAndGoods(adminId).then((res) => {
        if (res.code == 20000) {
          this.goodList = res.data.list;
        }
      });
    },
    getParamList() {
      var adminId = localStorage.getItem("adminId");
      homeApi.getStandards(adminId).then((res) => {
        if (res.code == 20000) {
          this.guige = res.data.guige;
          this.sweet = res.data.sweet;
          this.temperature = res.data.temperature;
        }
      });
    },
    getOrderList() {
      var adminId = localStorage.getItem("adminId");
      // var refreshToken = localStorage.getItem("refreshToken");
      homeApi.getOrderList(adminId).then((res) => {
        if (res.code == 20000) {
          this.orderList = res.data.orderList;
          // localStorage.setItem("refreshToken", res.data.refreshToken);
        }
      });
    },
    // 订单流水
    ordersFlowList() {
      var adminId = localStorage.getItem("adminId");
      homeApi.ordersFlowList(adminId, this.accountQuery).then((res) => {
        if (res.code == 20000) {
          this.tableData = res.data.list;
          this.banlance = res.data.banlance;
          let cupCount = 0;
          if (res.data.list.length > 0) {
            res.data.list.forEach((item) => {
              cupCount += item.number;
              this.cupCount = cupCount;
            });
          }
        }
      });
    },
    // 原材料 配料列表
    getBatchList() {
      var adminId = localStorage.getItem("adminId");
      homeApi.getBatchList(adminId, this.keyword).then((res) => {
        if (res.code == 20000) {
          this.ytableData = res.data.list;
          this.allBatchs = res.data.allBatchs;
        }
      });
    },
    // 原材料新建/编辑
    onAdd() {
      this.isAdd = true;
      this.batchInfo = {};
    },
    // 原材料新建/编辑
    onUpdate(data) {
      this.isAdd = true;
      homeApi.getBatchInfo(data.id).then((res) => {
        if (res.code == 20000) {
          this.batchInfo = res.data.info;
        }
      });
    },
    // 弹窗关闭
    onGuanbi() {
      this.isAdd = false;
    },
    // 添加
    addBatchInfo() {
      var shopId = localStorage.getItem("shopInfoId");
      this.batchInfo.shopId = shopId;
      homeApi
        .insertData8(this.batchInfo)
        .then((response) => {
          if (response.message == "该配料已添加") {
            this.$message.error(response.message);
          } else {
            this.isAdd = false;
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.getBatchList();
          }
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "新增失败",
          });
        });
    },
    // 修改信息
    updateBatchInfo() {
      homeApi
        .updateData8(this.batchInfo.id, this.batchInfo)
        .then((response) => {
          if (response.message == "该配料已添加") {
            this.$message.error(response.message);
          } else {
            this.isAdd = false;
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.getBatchList();
          }
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "修改失败",
          });
        });
    },
    // 删除
    onDell(data) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        homeApi.deleteBatch(data.id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getBatchList();
        });
      });
    },
    // 确定
    saveBatch() {
      if (!this.batchInfo.batchId) {
        this.$message({
          type: "warning",
          message: "请选择配料",
        });
        return;
      }
      if (!this.batchInfo.outlet) {
        this.$message({
          type: "warning",
          message: "请输入出料口",
        });
        return;
      }
      if (!this.batchInfo.totalCount) {
        this.$message({
          type: "warning",
          message: "请输入总数量",
        });
        return;
      }
      if (!this.batchInfo.id) {
        this.addBatchInfo();
      } else {
        // 修改
        this.updateBatchInfo();
      }
    },

    // 产品配方列表
    getGoodsBatchings() {
      var adminId = localStorage.getItem("adminId");
      homeApi.getGoodsBatchings(adminId, this.goodsBatchQuery).then((res) => {
        if (res.code == 20000) {
          this.ptableData = res.data.list;
        }
      });
    },
    // 选项卡
    handle(index) {
      this.current = index;
      this.ishiddens = false;
      this.ismake = false;
      this.ishidden = false;
      if (this.current == 0) {
        this.getCateAndGoods();
        this.getParamList();
      } else if (this.current == 1) {
        this.getOrderList();
        createSocket();
        createSocket2();
        // createSocket('wss://tea.yinghai-tec.com/tea-management/platSocket/');
        this.getsocketData = (e) => {
          // 创建接收消息函数
          let data = e && e.detail.data;
          if (data) {
            // if (data != 'ping' && JSON.parse(data).message == '您有新订单啦') {
            //   // 已经建立了连接
            //   this.getOrderList();
            // }

            if (data != "ping" && JSON.parse(data).message.includes("不足")) {
              // this.getOrderList();
              this.getBatchList();
              // 发出配料不足警告
              // this.playVoice(JSON.parse(data).message+"，请及时添加")
              //   store.dispatch('batchWarnning', JSON.parse(data).message).then(() => {
              //     this.playVoice(JSON.parse(data).message+"，请及时添加")
              // })
            }
            // if(data != 'ping' && JSON.parse(data).message == '禁用'){
            //     localStorage.setItem("balance", -1);
            //   }
            // if(data != 'ping' && JSON.parse(data).message == '您有新订单啦'){
            // 已经建立了连接
            // this.getOrderList();
            // this.playVoice("您有新的订单，请及时处理")
            // }
          }
        };
        // 注册监听事件
        window.addEventListener("onmessageWS", this.getsocketData);
        // 注册监听事件
        window.addEventListener("onmessageWS2", this.getsocketData);
      } else if (this.current == 2) {
        this.ordersFlowList();
      } else if (this.current == 3) {
        this.getBatchList();
      } else if (this.current == 5) {
        this.getBatchUseList();
      } else {
        this.getCupSizes();
        this.getGoodsBatchings();
      }
    },

    // 商品选项卡
    onChangea(index) {
      this.curIndex = index;
      this.currenta = -1;
    },

    getNum(index) {
      this.curIndex = index;
    },

    goLook(index, data) {
      this.currenta = index;
      this.goodsName = data.name;
      this.ishiddens = true;
      this.goodsInfo = data;
    },
    goBackgood() {
      this.ishiddens = false;
      this.ismake = false;
    },

    onMake() {
      this.makeWay =
        this.guige[this.currentsp].name +
        "," +
        this.sweet[this.currentspb].name +
        "," +
        this.temperature[this.currentspa].name;
      let params = [];
      params.push(this.guige[this.currentsp]);
      params.push(this.sweet[this.currentspb]);
      params.push(this.temperature[this.currentspa]);
      let goodsInfo = this.goodsInfo;
      let orderParams = {};
      orderParams.goodsId = goodsInfo.id;
      orderParams.goodsName = goodsInfo.name;
      orderParams.price = goodsInfo.price + this.guige[this.currentsp].addPrice;
      orderParams.selectCount = 1;
      orderParams.selectParams = params;
      let orderNo =
        Date.parse(new Date()) + "" + Math.floor(Math.random() * 10000);
      let adminId = localStorage.getItem("adminId");
      homeApi
        .startOrders(orderNo, 2, adminId, goodsInfo.id, orderParams)
        .then((res) => {
          if (res.code == 20000) {
            this.ishiddens = false;
            this.ismake = true;
            this.ismaking = true;
            let orders = res.data.orders;
            let info = {};
            info.shopName = orders.shopName;
            info.pickupCode = orders.pickupCode;
            info.createTime = orders.createTime;
            info.notes = orders.notes;
            info.from =
              orders.platformType == 1
                ? "小程序"
                : orders.platformType == 2
                ? "饿了么"
                : "美团";

            info.name = goodsInfo.name;
            info.specifications = orderParams.selectParams
              .map((iii) => iii.name)
              .join("，");
            this.stickersInfo = info;

            // let outletList = res.data.outletVoList;
            let ttt = parseInt(res.data.max) * 1000;
            setTimeout(() => {
              this.ismaking = false;
            }, ttt);
            // 暂时不打印
            // this.print1()
            // this.printOrders();

          }
        });
    },

    getOrdersInfoBy(orderNo) {
      let adminId = localStorage.getItem("adminId");
      homeApi.ordersInfoBy(orderNo, adminId).then((res) => {
        if (res.code == 20000) {
          this.ordersInfo = res.data.info;
          let params = res.data.info.params;
          params.forEach((item) => {
            item.pickupCode = res.data.info.pickupCode;
            item.shopName = res.data.info.shopName;
            item.createTime = res.data.info.createTime;
            item.notes = res.data.info.notes;
            item.from =
              res.data.info.platformType == 1
                ? "小程序"
                : res.data.info.platformType == 2
                ? "饿了么"
                : "美团";
          });
          this.arrParams = params;
        }
      });
    },
    getOrdersInfo(id) {
      let adminId = localStorage.getItem("adminId");
      homeApi.ordersInfo(id, adminId).then((res) => {
        if (res.code == 20000) {
          this.ordersInfo = res.data.info;
          let params = this.ordersInfo.params;
          params.forEach((item) => {
            item.pickupCode = res.data.info.pickupCode;
            item.shopName = res.data.info.shopName;
            item.createTime = res.data.info.createTime;
            item.notes = res.data.info.notes;
            item.from =
              res.data.info.platformType == 1
                ? "小程序"
                : res.data.info.platformType == 2
                ? "饿了么"
                : "美团";
          });
          this.arrParams = params;
        }
      });
    },
    getUrl(index, url) {
      homeApi.getUrl(url).then((res) => {
        if (res.code == 20000) {
          this.arrParams[index].makeStatus = 2;
        }
      });
    },
    print1() {
      let stickersInfo = this.stickersInfo;
      var LODOP = getLodop();
      // var totalPrint = 0 // 第几个
      // // 如果有预设的打印机名称，则设置，否则使用本机默认打印机
      // if (!this.isEmpty(this.print_info[0].打印机名称)) {

      // }
      LODOP.SET_PRINT_MODE("WINDOW_DEFPRINTER", "Xprinter-365b");
      // 循环打印

      // totalPrint++

      LODOP.SET_PRINT_PAGESIZE(1, 390, 300, "40mm"); // 设定纸张大小:纵向，宽为0，高为0，纸张类型为148.5mm，
      LODOP.ADD_PRINT_TEXT(5, 0, 248, 30, stickersInfo.shopName);
      LODOP.SET_PRINT_STYLEA(0, "FontSize", 8);

      //LODOP.ADD_PRINT_TEXT(5, 50, 228, 35, '自提') // 增加纯文本项:距离顶部31mm,距离左边312，宽度228，高度35
      //LODOP.SET_PRINT_STYLEA(0, 'FontSize', 10) // 设置第0个内容项字体为18号字体
      LODOP.ADD_PRINT_TEXT(
        20,
        0,
        657,
        25,
        "取餐号：" + stickersInfo.pickupCode
      );
      LODOP.SET_PRINT_STYLEA(0, "FontSize", 10);
      LODOP.ADD_PRINT_TEXT(20, 90, 657, 25, "(" + stickersInfo.from + ")");
      LODOP.SET_PRINT_STYLEA(0, "FontSize", 10);
      LODOP.ADD_PRINT_TEXT(36, 0, 646, 189, stickersInfo.name);
      LODOP.SET_PRINT_STYLEA(0, "FontSize", 8);
      // LODOP.ADD_PRINT_TEXT(52, 0, 346, 30, stickersInfo.specifications.indexOf("杯") > -1
      //       ? "（" + stickersInfo.specifications.substring(0, 2) + "）"
      //       : "")
      LODOP.ADD_PRINT_TEXT(52, 0, 346, 30, stickersInfo.specifications);
      LODOP.SET_PRINT_STYLEA(0, "FontSize", 8);
      // LODOP.ADD_PRINT_TEXT(52, 0, 248, 30, stickersInfo.specifications.indexOf("杯") > -1
      //       ? stickersInfo.specifications.substring(3)
      //       : stickersInfo.specifications)
      // LODOP.SET_PRINT_STYLEA(0, 'FontSize', 8)
      LODOP.ADD_PRINT_TEXT(
        67,
        0,
        248,
        30,
        stickersInfo.createTime ? stickersInfo.createTime.substring(5, 10) : ""
      );
      LODOP.SET_PRINT_STYLEA(0, "FontSize", 8);
      LODOP.ADD_PRINT_TEXT(
        67,
        30,
        248,
        30,
        stickersInfo.createTime ? stickersInfo.createTime.substring(10, 19) : ""
      );
      LODOP.SET_PRINT_STYLEA(0, "FontSize", 8);

      if (stickersInfo.notes != null && stickersInfo.notes != "") {
        LODOP.ADD_PRINT_TEXT(
          82,
          0,
          248,
          30,
          stickersInfo.notes ? "备注：" + stickersInfo.notes : ""
        );
        LODOP.SET_PRINT_STYLEA(0, "FontSize", 8);
      }

      LODOP.PRINT();
      // LODOP.PREVIEW() // 为防止真正打印，采用预览模式,预览模式只能预览一页，无法翻页，切会弹出‘有窗口已打开，先关闭它’提示
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
    startMake(data, index) {
      this.$confirm("确认制作此饮品吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        if (data.from == "饿了么" && localStorage.getItem("balance") <= 0) {
          this.$confirm(
            "帐户余额小于或等于0，请到商户端-订单列表中充值余额！",
            "提示",
            {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            }
          );
        } else {
          homeApi.makeParam(data.id, data).then((res) => {
            if (res.code == 20000) {
              this.ismakings = true;
              // 打印
              this.stickersInfo = data;

              let ttt = parseInt(res.data.max) * 1000;
              setTimeout(() => {
                this.ismakings = false;
                this.arrParams[index].makeStatus = 2;
              }, ttt);
              // 暂时不打印
              // this.print1();
              // this.printOrders();
            }
          });
        }
      });
    },

    //订单选项卡
    onChangeb(index) {
      this.curIndexb = index;
    },
    onOrder(index, data) {
      this.currentb = index;
      this.ishidden = true;
      this.getOrdersInfo(data.id);
    },
    goBackorder() {
      this.ishidden = false;
      if (this.orderNo != "") {
        this.$router.push({ path: "/Index" });
      } else {
        this.getOrderList();
      }
    },

    //流水筛选
    showScreen() {
      this.isscreen = true;
    },
    goReset() {
      // this.isscreen = false;
      this.accountQuery = {};
    },
    goSure() {
      if (this.accountQuery.begin && this.accountQuery.end) {
        var timestart = new Date(this.accountQuery.begin).getTime();
        var timeend = new Date(this.accountQuery.end).getTime();
        if (timestart > timeend) {
          this.$message({
            type: "warning",
            message: "开始时间不能大于结束时间！",
          });
          return false;
        }
      }
      this.isscreen = false;
      this.ordersFlowList();
    },
    goXqs(data) {
      this.isStream = true;
      homeApi.getMakeWay(data).then((res) => {
        if (res.code == 20000) {
          let makeParams = res.data.list;
          makeParams.forEach((item) => {
            item.params = item.specifications.split("，");
          });
          this.makeParams = makeParams;
        }
      });
    },
    goCloses() {
      this.isStream = false;
      this.makeParams = [];
    },

    // 配方筛选
    showScreens() {
      this.isshowscreen = true;
    },
    goResets() {
      // this.isshowscreen = false;
      this.goodsBatchQuery = {};
    },
    goSures() {
      this.getGoodsBatchings();
      this.isshowscreen = false;
    },
    goXq(data) {
      this.isFormula = true;
      var adminId = localStorage.getItem("adminId");
      homeApi
        .getGoodsBatchInfo(
          data.goodsId,
          data.sizeId,
          data.sugarId,
          data.temperatureId,
          adminId
        )
        .then((res) => {
          if (res.code == 20000) {
            this.goodBatch = res.data.info;
            this.goodBatch.goodsToBatchList.forEach((item) => {
              item.list = this.batchList;
            });
          }
        });
    },
    goClose() {
      this.isFormula = false;
    },

    //物料消耗
    showScreena() {
      this.isscreena = true;
    },
    goReseta() {
      this.batchUseQuery = {};
    },
    goSurea() {
      if (this.batchUseQuery.begin && this.batchUseQuery.end) {
        var timestarta = new Date(this.batchUseQuery.begin).getTime();
        var timeenda = new Date(this.batchUseQuery.end).getTime();
        if (timestarta > timeenda) {
          this.$message({
            type: "warning",
            message: "开始时间不能大于结束时间！",
          });
          return false;
        }
      }
      this.isscreena = false;
    },
  },
});
</script>
    
<style scoped>
@import url("../assets/detail.css");
</style>