<template>
  <div class="formula_box">
    <!-- 顶部 -->
    <div class="formula_head white_bg" ref="getformulaheight">
      <img
        src="../../static/back.png"
        alt=""
        class="formula_back"
        @click="goBack()"
      />
      <span>配方</span>
      <div class="tab-tit">
        <a @click="changeCurId(0)" :class="{ cur: curId === 0 }">产品配方</a>
        <a @click="changeCurId(1)" :class="{ cur: curId === 1 }"
          >规格参数配方</a
        >
      </div>
      <div class="formula_searchk">
        <img src="../../static//search.png" alt="" />
        <input
          type="text"
          placeholder="请输入内容"
          @input="searchContent"
          v-model="keyword"
        />
      </div>
    </div>
    <!-- 内容 -->
    <div class="tab-con">
      <div
        class="formula_one white_bg"
        v-show="curId === 0"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <button type="button" class="white addbtn" @click="productAdd()">
            新建
          </button>
        </div>
        <el-table
          border
          :data="productList"
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
          <el-table-column prop="goodsName" label="产品名称"> </el-table-column>
          <el-table-column prop="batchName" label="规格"> </el-table-column>
          <el-table-column prop="numberUnit" label="容量"> </el-table-column>
          <el-table-column label="操作" width="300">
            <template slot-scope="scope">
              <el-button
                type="button"
                size="small"
                class="editbtn white"
                @click="editFormula(scope.row)"
                >编辑</el-button
              >
              <el-button
                type="button"
                size="small"
                class="dellbtn white"
                @click="dellFormula(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getGoodsBatchs"
        >
        </el-pagination>
        <!-- 新建配方 -->
        <div class="mask" v-show="isShow">
          <div class="formula_whitek">
            <div class="formula_shouh">
              {{ goodBatch.id != "" ? "修改配方" : "新建配方" }}
              <img src="../../static/closed.png" @click="onClose()" />
            </div>
            <el-form
              :model="goodBatch"
              ref="goodBatch"
              :rules="goodBatchRules"
              class="formula_form"
            >
              <el-form-item label="商品名称">
                <el-select
                  @visible-change="selectValue1($event)"
                  :disabled="goodBatch.id != ''"
                  v-model="goodBatch.goodsId"
                  placeholder="请选择"
                  @change="selectGoods(goodBatch.goodsId)"
                  class="selectk"
                >
                  <el-option
                    v-for="item in goodsList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="杯型选择">
                <el-select
                  :disabled="goodBatch.sizeId > 0"
                  v-model="goodBatch.sizeId"
                  placeholder="请选择"
                  class="selectk"
                >
                  <el-option
                    v-for="item in cupSizes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-form>
            <el-form ref="batchRules" :model="goodBatch">
              <el-table
                border
                :data="goodBatch.goodsToBatchList"
                class="neitable"
                :header-cell-style="{ background: '#C2C2C2', color: '#3D3D3D' }"
              >
                <el-table-column label="商品配料">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'goodsToBatchList.' + scope.$index + '.batchId'"
                      :rules="batchRules.batchId"
                    >
                      <el-select
                        v-model="scope.row.batchId"
                        placeholder="请选择"
                      >
                        <el-option
                          v-for="item in scope.row.list"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </template>
                </el-table-column>
                <el-table-column label="配比数量">
                  <template slot-scope="scope">
                    <el-form-item
                      :prop="'goodsToBatchList.' + scope.$index + '.useNumber'"
                      :rules="batchRules.useNumber"
                    >
                      <el-input
                        type="number"
                        v-model="scope.row.useNumber"
                        placeholder="请输入"
                      ></el-input>
                    </el-form-item>
                  </template>
                </el-table-column>
                <!-- <el-table-column prop="unit" label="核算单位">
                <template slot-scope="scope">
                  <el-select
                    v-model="selectUnit"
                    placeholder="请选择"
                    @change="changeUnit()"
                  >
                    <el-option
                      v-for="item in units"
                      :key="item.id"
                      :label="item.unit"
                      :value="item.unit"
                    ></el-option>
                  </el-select>
                </template>
              </el-table-column> -->
                <el-table-column
                  prop=""
                  label="操作"
                  align="center"
                  width="80"
                  v-if="goodBatch.goodsToBatchList.length > 1"
                >
                  <template slot-scope="scope">
                    <img
                      src="../../static/x-circle.png"
                      alt=""
                      style="cursor: pointer"
                      @click="deleteRows(scope)"
                    />
                  </template>
                </el-table-column>
              </el-table>
            </el-form>
            <button type="button" class="xinjian" @click="addRows()">
              +新增
            </button>
            <div class="annvzu">
              <button type="button" @click="onClose()" class="cancelbtn">
                取消
              </button>
              <button
                type="button"
                class="submitbtn"
                @click="saveGoodBatch('goodBatch', 'batchRules')"
              >
                提交
              </button>
            </div>
          </div>
        </div>
        <!-- 删除配方 -->
        <div class="mask" v-show="isHidden">
          <div class="formula_whiteks">
            <div class="formula_tip_title">删除确认</div>
            <div class="formula_tip_wen">确定要删除该配方吗？</div>
            <div class="formula_tip_btn">
              <button
                type="button"
                class="formula_tip_cancelbtn"
                @click="onCancel()"
              >
                取消
              </button>
              <button
                type="button"
                class="formula_tip_surebtn"
                @click="onSure()"
              >
                确定
              </button>
            </div>
          </div>
        </div>
      </div>
      <div
        class="formula_one white_bg"
        v-show="curId === 1"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <button type="button" class="white addbtn" @click="formulaAdd()">
            新建
          </button>
        </div>
        <el-table
          border
          :data="normsList"
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
          <el-table-column prop="name" label="名称"> </el-table-column>
          <el-table-column prop="heatName" label="温度"> </el-table-column>
          <el-table-column prop="heatUseNumber" label="用量"> </el-table-column>
          <el-table-column prop="sugarName" label="糖度"> </el-table-column>
          <el-table-column prop="sugarUseNumber" label="用量">
          </el-table-column>
          <el-table-column label="操作" width="300">
            <template slot-scope="scope">
              <el-button
                type="button"
                size="small"
                class="editbtn white"
                @click="formulaEdit(scope.row.id)"
                >编辑</el-button
              >
              <el-button
                type="button"
                size="small"
                class="dellbtn white"
                @click="dellFormulas(scope.row.id)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getParamBatchs"
        >
        </el-pagination>
        <!-- 新建规格参数配方 -->
        <div class="mask" v-show="isShows">
          <div class="formula_whitek">
            <div class="formula_shouh">
              {{
                paramBatch.id != "" ? "修改规格参数配方" : "新建规格参数配方"
              }}
              <img src="../../static/closed.png" @click="onCloseds()" />
            </div>
            <el-form
              :model="paramBatch"
              ref="paramBatch"
              :rules="paramBatchRules"
              class="formula_form zengao"
            >
              <el-form-item label="配方名称" prop="name">
                <el-input
                  type="text"
                  placeholder="请输入"
                  class="selectk"
                  v-model="paramBatch.name"
                ></el-input>
              </el-form-item>
              <el-form-item label="温度选择" prop="heatParamId">
                <el-select
                  v-model="paramBatch.heatParamId"
                  placeholder="请选择"
                  class="selectk"
                >
                  <el-option
                    v-for="item in heatList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="用量" class="inputk" prop="heatUseNumber">
                <el-input
                  type="number"
                  placeholder="请输入"
                  v-model="paramBatch.heatUseNumber"
                  class="selectk"
                ></el-input>
              </el-form-item>
              <el-form-item label="糖度选择" prop="sugarParamId">
                <el-select
                  v-model="paramBatch.sugarParamId"
                  placeholder="请选择"
                  class="selectk"
                >
                  <el-option
                    v-for="item in sugarList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="用量" class="inputk" prop="sugarUseNumber">
                <el-input
                  type="number"
                  placeholder="请输入"
                  v-model="paramBatch.sugarUseNumber"
                  class="selectk"
                ></el-input>
              </el-form-item>
              <el-form-item class="formula_tip_btns">
                <button
                  type="button"
                  class="formula_tip_cancelbtn"
                  @click="onCloseds()"
                >
                  取消
                </button>
                <button
                  type="button"
                  class="formula_tip_surebtn"
                  @click="saveOrUpdataParam('paramBatch')"
                >
                  确定
                </button>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <!-- 删除规格参数配方 -->
        <div class="mask" v-show="isHiddens">
          <div class="formula_whiteks">
            <div class="formula_tip_title">删除确认</div>
            <div class="formula_tip_wen">确定要删除该规格参数配方吗？</div>
            <div class="formula_tip_btn">
              <button
                type="button"
                class="formula_tip_cancelbtn"
                @click="onCancels()"
              >
                取消
              </button>
              <button
                type="button"
                class="formula_tip_surebtn"
                @click="onSures()"
              >
                确定
              </button>
            </div>
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
  name: "Index",
  data() {
    const validateSpace = (rule, value, callback) => {
      if (value.trim()) {
        callback();
      } else {
        callback(new Error("请输入有效的配方名称"));
      }
    };
    return {
      curId: 0,
      height: 0,
      page: 1,
      limit: 10,
      total: 0,
      paramBatchId: "",
      goodBatchId: "",
      keyword: "",
      batchList: [],
      goodsList: [],
      cupSizes: [],
      goodsId: "",
      goodBatch: {
        id: "",
        goodsId: "",
        goodsToBatchList: [{ batchId: "", useNumber: "" }],
      },
      paramBatch: {
        id: "",
        name: "",
        heatParamId: "",
        heatUseNumber: "",
        sugarParamId: "",
        sugarUseNumber: "",
      },
      heatList: [],
      sugarList: [],
      productList: [{}, {}],
      normsList: [{}, {}],
      isShow: false,
      isHidden: false,
      mixtureList: [{}, {}],
      isShows: false,
      isHiddens: false,
      goodBatchRules: {
        goodsId: [{ required: true, message: "请选择商品", trigger: "blur" }],
      },
      batchRules: {
        batchId: [{ required: true, message: "请选择配料", trigger: "blur" }],
        useNumber: [{ required: true, message: "请输入配比", trigger: "blur" }],
      },
      paramBatchRules: {
        name: [
          { required: true, message: "请输入配方名称", trigger: "blur" },
          { validator: validateSpace, trigger: "blur" },
        ],
        heatParamId: [
          { required: true, message: "请选择温度", trigger: "blur" },
        ],
        heatUseNumber: [
          { required: true, message: "请输入温度用量", trigger: "blur" },
        ],
        sugarParamId: [
          { required: true, message: "请选择糖度", trigger: "blur" },
        ],
        sugarUseNumber: [
          { required: true, message: "请输入糖度用量", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    if (this.curId == 0) {
      var shopId = localStorage.getItem("shopId");
      homeApi.allGoods(shopId).then((res) => {
        if (res.data.code == 20000) {
          this.goodsList = res.data.data.list;
          this.cupSizes = res.data.paramList;
        }
      });
      homeApi.batchUse(shopId).then((res) => {
        if (res.data.code == 20000) {
          this.batchList = res.data.data.batchList;
        }
      });
      this.getGoodsBatchs();
    } else {
      this.getParamBatchs();
    }
  },
  methods: {
    selectValue1(e) {
      if (e) {
        console.log(this.goodsList);
        this.goodsList = this.goodsList.filter((item) => item.addBatch == 2);
      } else {
        var shopId = localStorage.getItem("shopId");
        homeApi.allGoods(shopId).then((res) => {
          if (res.data.code == 20000) {
            this.goodsList = res.data.data.list;
          }
        });
      }
    },
    selectGoods(goodsId) {
      var shopId = localStorage.getItem("shopId");
      homeApi.batchUse(shopId).then((res) => {
        if (res.data.code == 20000) {
          this.batchList = res.data.data.batchList;
          this.goodsId = goodsId;
          this.goodBatch.goodsToBatchList = [{ batchId: "", useNumber: "" }];
          let goodsInfo = this.goodsList.filter((item) => item.id == goodsId);
          if (this.goodBatch.goodsToBatchList.length > 0) {
            this.goodBatch.goodsToBatchList.forEach((item, index) => {
              this.batchList = this.batchList.filter(
                (item) => item.machineNo == goodsInfo[0].machineNo
              );
              this.$set(
                this.goodBatch.goodsToBatchList[index],
                "list",
                this.batchList
              ); // 设置当前第几行的默认值
            });
          } else {
            this.batchList = this.batchList.filter(
              (item) => item.machineNo == goodsInfo[0].machineNo
            );
            this.$set(
              this.goodBatch.goodsToBatchList[0],
              "list",
              this.batchList
            ); // 设置当前第几行的默认值
          }
          this.$forceUpdate();
        }
      });
    },
    changeCurId(i) {
      this.curId = i;
      if (i == 0) {
        var shopId = localStorage.getItem("shopId");
        homeApi.allGoods(shopId).then((res) => {
          if (res.data.code == 20000) {
            this.goodsList = res.data.data.list;
          }
        });
        // homeApi.batchUse(shopId).then((res) => {
        //   if (res.data.code == 20000) {
        //     this.batchList = res.data.data.batchList;
        //   }
        // });
        this.getGoodsBatchs();
      } else {
        this.getParamBatchs();
      }
    },
    saveGoodBatch(goodBatch, batchRules) {
      var shopId = localStorage.getItem("shopId");
      this.$refs[goodBatch].validate((valid) => {
        if (valid) {
          this.$refs[batchRules].validate((validBatch) => {
            if (validBatch) {
              var shopId = localStorage.getItem("shopId");
              if (this.goodBatch.id != "") {
                this.goodBatch.shopId = shopId;
                homeApi.updateCostCards(this.goodBatch).then((res) => {
                  if (res.data.code == 20000) {
                    this.$message({
                      type: "success",
                      message: "修改成功",
                    });
                    this.isShow = false;

                    this.goodBatch = {
                      id: "",
                      goodsId: "",
                      goodsToBatchList: [{ batchId: "", useNumber: "" }],
                    };
                    //         homeApi.batchUse(shopId).then((res) => {
                    //   if (res.data.code == 20000) {
                    //     this.batchList = res.data.data.batchList;
                    //   }
                    // });
                    this.$refs[goodBatch].clearValidate();
                    this.$refs[validBatch].clearValidate();
                    this.getGoodsBatchs();
                  }
                });
              } else {
                this.goodBatch.shopId = shopId;
                homeApi.addCostCards(this.goodBatch).then((res) => {
                  if (res.data.code == 20000) {
                    this.$message({
                      type: "success",
                      message: "添加成功",
                    });
                    this.isShow = false;
                    this.getGoodsBatchs();

                    this.goodBatch = {
                      id: "",
                      goodsId: "",
                      goodsToBatchList: [{ batchId: "", useNumber: "" }],
                    };
                    //         homeApi.batchUse(shopId).then((res) => {
                    //   if (res.data.code == 20000) {
                    //     this.batchList = res.data.data.batchList;
                    //   }
                    // });
                    this.$refs[goodBatch].clearValidate();
                    this.$refs[validBatch].clearValidate();
                  }
                });
              }
            } else {
              return false;
            }
          });
        } else {
          return false;
        }
      });
    },
    saveOrUpdataParam(paramBatch) {
      this.$refs[paramBatch].validate((valid) => {
        if (valid) {
          if (this.paramBatch.id) {
            homeApi.updateParamBatch(this.paramBatch).then((res) => {
              if (res.data.code == 20000) {
                this.$message({
                  type: "success",
                  message: "修改成功",
                });
                this.isShows = false;
                this.getParamBatchs();
                this.paramBatch = {
                  id: "",
                  name: "",
                  heatParamId: "",
                  heatUseNumber: "",
                  sugarParamId: "",
                  sugarUseNumber: "",
                };
                this.$refs[paramBatch].clearValidate();
              }
            });
          } else {
            var shopId = localStorage.getItem("shopId");
            this.paramBatch.shopId = shopId;
            homeApi.addParamBatch(this.paramBatch).then((res) => {
              if (res.data.code == 20000) {
                this.$message({
                  type: "success",
                  message: "添加成功",
                });
                this.isShows = false;
                this.getParamBatchs();
                this.paramBatch = {
                  id: "",
                  name: "",
                  heatParamId: "",
                  heatUseNumber: "",
                  sugarParamId: "",
                  sugarUseNumber: "",
                };
                this.$refs[paramBatch].clearValidate();
              }
            });
          }
        } else {
          return false;
        }
      });
    },

    searchContent() {
      this.getParamBatchs();
    },
    // 产品配方列表
    getGoodsBatchs(page = 1) {
      this.page = page;
      var shopId = localStorage.getItem("shopId");
      homeApi
        .getGoodBatchList(this.page, this.limit, shopId, this.keyword)
        .then((res) => {
          if (res.data.code == 20000) {
            this.productList = res.data.data.rows;
            this.total = res.data.data.total;
          }
        });
    },
    // 规格配方列表
    getParamBatchs(page = 1) {
      this.page = page;
      var shopId = localStorage.getItem("shopId");
      homeApi
        .getParamBatchList(this.page, this.limit, shopId, this.keyword)
        .then((res) => {
          if (res.data.code == 20000) {
            this.normsList = res.data.data.rows;
            this.total = res.data.data.total;
          }
        });
    },
    // 返回
    goBack: function () {
      this.$router.go(-1);
    },

    //新建配方
    productAdd() {
      this.isShow = true;
      var shopId = localStorage.getItem("shopId");
      homeApi.allGoods(shopId).then((res) => {
        if (res.data.code == 20000) {
          this.goodsList = res.data.data.list;
        }
      });
      //   var shopId = localStorage.getItem("shopId");
      //   homeApi.allGoods(shopId).then((res) => {
      //     if (res.data.code == 20000) {
      //       this.goodsList = res.data.data.list;
      //     }
      //   });
      //   homeApi.batchUse(shopId).then((res) => {
      //     if (res.data.code == 20000) {
      //       this.batchList = res.data.data.batchList;
      //     }
      //   });
    },
    onClose() {
      this.isShow = false;
      this.goodBatch = {
        id: "",
        goodsId: "",
        goodsToBatchList: [{ batchId: "", useNumber: "" }],
      };
      //   homeApi.batchUse(shopId).then((res) => {
      //       if (res.data.code == 20000) {
      //         this.batchList = res.data.data.batchList;
      //       }
      //     });
    },

    //编辑配方
    editFormula(data) {
      var adminId = localStorage.getItem("adminId");

      homeApi.getGoodsBatchInfo(data.goodsId, data.sizeId, adminId).then((res) => {
        if (res.data.code == 20000) {
          console.log(this.goodsList);
          this.goodBatch = res.data.data.info;
          console.log(this.goodBatch);
          this.goodBatch.goodsToBatchList.forEach((item) => {
            item.list = this.batchList;
          });
          this.isShow = true;
        }
      });
    },

    //删除配方
    dellFormula(goodsId) {
      this.isHidden = true;
      this.goodBatchId = goodsId;
    },
    // 弹窗取消按钮
    onCancel() {
      this.isHidden = false;
    },
    // 弹窗确定按钮
    onSure() {
      var shopId = localStorage.getItem("shopId");
      homeApi.removeGoodBatch(this.goodBatchId, shopId).then((response) => {
        this.$message({
          type: "success",
          message: "删除成功!",
        });
        this.isHidden = false;
        this.getGoodsBatchs();
        this.goodBatchId = "";
      });
    },

    //添加配方行
    addRows() {
      let obj = {};
      obj.batchId = "";
      obj.useNumber = "";
      obj.list = this.batchList;
      this.goodBatch.goodsToBatchList.push(obj);
      //   const circle = this.mixtureList[0]; //取出数组中第一个对象
      //   if (circle) {
      //     const newObj = {};
      //     for (let key in circle) {
      //       //把第一个对象的属性都赋值给新对象newObj  然后每个属性的值都设置为空；
      //       newObj[key] = "";
      //     }
      //     this.mixtureList.splice(this.mixtureList.length - 1, 0, newObj);
      //   }
    },

    //删除配方行
    deleteRows(scope) {
      this.goodBatch.goodsToBatchList.splice(scope.$index, 1);
    },

    // 新建规格参数配方
    formulaAdd() {
      //   this.isShows = true;
      homeApi.getHeatAndSugarParams().then((res) => {
        if (res.data.code == 20000) {
          this.sugarList = res.data.data.sugarList;
          this.heatList = res.data.data.heatList;
          this.isShows = true;
        }
      });
    },
    formulaEdit(id) {
      homeApi.getParamBatchInfo(id).then((res) => {
        if (res.data.code == 20000) {
          this.paramBatch = res.data.data.info;
          homeApi.getHeatAndSugarParams().then((res) => {
            if (res.data.code == 20000) {
              this.sugarList = res.data.data.sugarList;
              this.heatList = res.data.data.heatList;
              this.isShows = true;
            }
          });
        }
      });
    },

    onCloseds() {
      this.isShows = false;
    },

    //删除规格参数配方
    dellFormulas(id) {
      this.isHiddens = true;
      this.paramBatchId = id;
    },
    // 删除规格参数弹窗取消按钮
    onCancels() {
      this.isHiddens = false;
    },
    // 删除规格参数弹窗确定按钮
    onSures() {
      homeApi.removeParamBatch(this.paramBatchId).then((response) => {
        this.$message({
          type: "success",
          message: "删除成功!",
        });
        this.isHiddens = false;
        this.getParamBatchs();
        this.paramBatchId = "";
      });
    },
  },

  mounted() {
    let inventoryheight = this.$refs.getformulaheight.offsetHeight;
    let windowheight = window.innerHeight;
    this.height =
      windowheight - inventoryheight - localStorage.getItem("touheight") - 57;
  },
});
</script>
  
<style scoped>
.formula_box {
  background: #f2f3f4;
  padding-top: 18px;
  padding-bottom: 22px;
}
/* 顶部 */
.formula_head {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
}
.formula_back {
  width: 30px;
  height: 30px;
  display: block;
  margin-right: 38px;
  margin-left: 56px;
  cursor: pointer;
}
.formula_head span {
  font-size: 24px;
  margin-right: 96px;
}
.tab-tit {
  font-size: 0;
  width: 487px;
  cursor: pointer;
}
.tab-tit a {
  display: inline-block;
  width: 190px;
  height: 55px;
  line-height: 55px;
  border-radius: 8px;
  background: #efefef;
  font-size: 24px;
  font-family: PingFangSCBold-Bold;
  text-align: center;
  color: #333;
  text-decoration: none;
  margin-right: 13px;
}
.tab-tit .cur {
  color: #0256ff;
  background: #eff6fd;
}
.formula_searchk {
  width: 300px;
  height: 40px;
  border: 1px solid #999999;
  display: flex;
  align-items: center;
  border-radius: 8px;
}
.formula_searchk img {
  width: 24px;
  height: 24px;
  display: block;
  margin: 0 8px;
}
.formula_searchk input {
  width: 250px;
  height: 38px;
  font-size: 20px;
  text-align: left;
}
/* 内容 */
.tab-con {
  padding-top: 17px;
}
.formula_one {
  width: 97%;
  margin: auto;
  border-radius: 8px;
  padding-top: 18px;
}
.addk {
  width: 96.5%;
  margin: auto;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 19px;
}
.addbtn {
  width: 100px;
  height: 44px;
  border-radius: 4px;
  background: #0256ff;
  font-size: 20px;
  font-family: PingFangSCBold-Bold;
  text-align: center;
}
.editbtn {
  width: 76px;
  height: 34px;
  border-radius: 4px;
  font-size: 18px;
  text-align: center;
  font-family: PingFangSCBold-Bold;
  background: #4ecb70;
  padding: 0;
  margin: 0 12px;
  line-height: 34px;
}
.dellbtn {
  width: 76px;
  height: 34px;
  border-radius: 4px;
  font-size: 18px;
  text-align: center;
  font-family: PingFangSCBold-Bold;
  background: #ea0000;
  padding: 0;
  margin: 0 12px;
  line-height: 34px;
}
/* 新建配方 */
.formula_whitek {
  width: 800px;
  height: auto;
  margin: auto;
  border-radius: 16px;
  position: absolute;
  top: 90px;
  left: 0;
  right: 0;
  background: #e9edf0;
}
.formula_shouh {
  width: 100%;
  height: 80px;
  position: relative;
  font-size: 32px;
  margin-left: 26px;
  line-height: 80px;
}
.formula_shouh img {
  width: 35px;
  height: 35px;
  display: block;
  position: absolute;
  top: 25px;
  right: 55px;
  z-index: 1;
  cursor: pointer;
}
.formula_form .el-form-item {
  display: flex;
  justify-content: center;
  align-items: center;
}
.zengao {
  padding-top: 20px;
}
.selectk {
  width: 400px;
}
.inputk {
  margin-left: 30px;
}
.neitable {
  width: 650px;
  margin-left: 61px;
}
.xinjian {
  width: 87px;
  height: 40px;
  border-radius: 4px;
  font-size: 18px;
  background: #0256ff;
  color: #ffffff;
  text-align: center;
  margin: 0;
  padding: 0;
  margin-left: 61px;
  margin-top: 22px;
}
.annvzu {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 58px;
  padding-bottom: 97px;
}
.annvzu button {
  width: 100px;
  height: 44px;
  border-radius: 4px;
  font-size: 20px;
  font-family: PingFangSCBold-Bold;
  margin: 0;
  padding: 0;
  text-align: center;
}
.cancelbtn {
  border: 1px solid #666666;
  background: #ffffff;
  color: #666666;
  margin-right: 112px !important;
}
.cancelbtn:hover {
  color: #666666;
}
.submitbtn {
  background: #0256ff;
  color: #ffffff;
}
button:hover {
  color: #ffffff;
}
/* 删除配方 */
.formula_whiteks {
  width: 630px;
  height: 338px;
  margin: auto;
  border-radius: 16px;
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  margin-top: -169px;
  background: #e9edf0;
}
.formula_tip_title {
  width: 100%;
  font-size: 32px;
  font-family: PingFangSCBold-Bold;
  text-align: center;
  padding-top: 27px;
  margin-bottom: 76px;
}
.formula_tip_wen {
  width: 100%;
  font-size: 28px;
  text-align: center;
}
.formula_tip_btn {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.formula_tip_btn button {
  width: 100px;
  height: 44px;
  border-radius: 4px;
  font-size: 20px;
  font-family: PingFangSCBold-Bold;
  text-align: center;
  margin: 0;
  padding: 0;
  margin-top: 75px;
}
.formula_tip_cancelbtn {
  border: 1px solid #666666;
  color: #666666;
  background: #ffffff;
  margin-right: 77px !important;
}
.formula_tip_cancelbtn:hover {
  color: #666666;
}
.formula_tip_surebtn {
  background: #0256ff;
  color: #ffffff;
}
.formula_tip_btns {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 77px;
  margin-top: 100px;
}
.formula_tip_btns button {
  width: 100px;
  height: 44px;
  border-radius: 4px;
  font-size: 20px;
  font-family: PingFangSCBold-Bold;
  text-align: center;
  margin: 0;
  padding: 0;
}
</style>