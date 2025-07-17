<template>
  <div class="formula_box">
    <!-- 顶部 -->
    <div class="formula_head white_bg" ref="getformulaheight">
      <div class="tab-tit">
        <a @click="changeCurId(0)" :class="{ cur: curId === 0 }">产品配方</a>
        <a @click="changeCurId(1)" :class="{ cur: curId === 1 }">规格参数配方</a>
      </div>
    </div>
    <el-form :inline="true" class="formula_searchk">
      <el-form-item label="">
        <el-input v-model="keyword" placeholder="请输入内容" @input="searchContent"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getParamBatchs()" icon="el-icon-search">搜索</el-button>
        <el-button type="default" @click="resetData()" icon="el-icon-my-clear">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 内容 -->
    <div class="tab-con">
      <div
        class="formula_one white_bg"
        v-show="curId === 0"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <el-button type="success" @click="productAdd()" style="float: right; margin-bottom: 10px">新建</el-button>
        </div>
        <el-table
          border
          :data="productList"
          style="width: 98%; margin: auto"
        >
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80" align="center"
          ></el-table-column>
          <el-table-column prop="goodsName" label="产品名称" align="center"> </el-table-column>
          <el-table-column prop="batchName" label="规格" align="center"> </el-table-column>
          <el-table-column prop="numberUnit" label="容量" align="center" width="260"> </el-table-column>
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                class="editbtn"
                icon="el-icon-edit"
                @click="editFormula(scope.row.goodsId)"
                >编辑</el-button
              >
              <el-button
                type="danger"
                size="mini"
                class="dellbtn"
                icon="el-icon-delete"
                @click="dellFormula(scope.row.goodsId)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
        :current-page="page"
         :page-size="limit"
          :total="total"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getGoodsBatchs">
        </el-pagination>
        <!-- 新建配方 -->
        <el-dialog width="40%" :visible.sync="isShow" :title="goodBatch.id > 0 ? '修改配方' : '新建配方'" @close="onClose('goodBatch', 'batchRules')" center>
          <el-form :model="goodBatch" ref="goodBatch" :rules="goodBatchRules" class="formula_form">
            <el-form-item label="商品名称">
              <el-select @visible-change="selectValue1($event)" :disabled="goodBatch.id != ''" v-model="goodBatch.goodsId" placeholder="请选择" @change="selectGoods(goodBatch.goodsId)" class="selectk">
                <el-option v-for="item in goodsList" :key="item.id" :label="item.name" :value="item.id"></el-option>
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
              <el-table-column prop="" label="操作" align="center" width="80" v-if="goodBatch.goodsToBatchList.length > 1">
                <template slot-scope="scope">
                  <img
                    src="../../../static/x-circle.png"
                    alt=""
                    style="cursor: pointer"
                    @click="deleteRows(scope)"
                  />
                </template>
              </el-table-column>
            </el-table>
          </el-form>
          <el-button type="success" class="xinjian" @click="addRows()">+新增</el-button>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onClose('goodBatch', 'batchRules')">取 消</el-button>
            <el-button type="primary" @click="saveGoodBatch('goodBatch', 'batchRules')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
      <div
        class="formula_one white_bg"
        v-show="curId === 1"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <el-button type="success" @click="formulaAdd()" style="float: right; margin-bottom: 10px">新建</el-button>
        </div>
        <el-table
          border
          :data="normsList"
          style="width: 98%; margin: auto"
        >
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80" align="center"
          ></el-table-column>
          <el-table-column prop="name" label="名称" align="center"> </el-table-column>
          <el-table-column prop="heatName" label="温度" align="center" width="200"> </el-table-column>
          <el-table-column prop="heatUseNumber" label="用量" align="center" width="200"> </el-table-column>
          <el-table-column prop="sugarName" label="糖度" align="center" width="200"> </el-table-column>
          <el-table-column prop="sugarUseNumber" label="用量" align="center" width="200">
          </el-table-column>
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-edit"
                class="editbtn white"
                @click="formulaEdit(scope.row.id)"
                >编辑</el-button
              >
              <el-button
                type="danger"
                size="mini"
                class="dellbtn white"
                icon="el-icon-delete"
                @click="dellFormulas(scope.row.id)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
        :current-page="page"
          :page-size="limit"
          :total="total"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getParamBatchs">
        </el-pagination>
        <!-- 新建规格参数配方 -->
        <el-dialog width="40%" :visible.sync="isShows" :title="paramBatch.id > 0 ? '修改规格参数配方' : '新建规格参数配方'" @close="onCloseds('paramBatch')" center>
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
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds('paramBatch')">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdataParam('paramBatch')">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>
  
<script>
import homeApi from "@/api/admins/formula";

export default ({
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
      mixtureList: [{}, {}],
      isShows: false,
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
    resetData(){
      this.getParamBatchs();
      this.keyword = "";
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
    onClose(goodBatch, batchRules) {
      this.isShow = false;
      var shopId = localStorage.getItem("shopId");
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
    editFormula(goodsId) {
      var shopId = localStorage.getItem("shopId");

      homeApi.getGoodsBatchInfo(goodsId, shopId).then((res) => {
        if (res.data.code == 20000) {

          this.goodBatch = res.data.data.info;
 
          this.goodBatch.goodsToBatchList.forEach((item) => {
            item.list = this.batchList;
          });
          this.isShow = true;
        }
      });
    },

    //删除配方
    dellFormula(goodsId) {
      this.goodBatchId = goodsId;
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        var shopId = localStorage.getItem("shopId");
        homeApi.removeGoodBatch(this.goodBatchId, shopId).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          this.getGoodsBatchs();
          this.goodBatchId = "";
        });
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

    onCloseds(paramBatch) {
      this.isShows = false;
    },

    //删除规格参数配方
    dellFormulas(id) {
      this.paramBatchId = id;
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        homeApi.removeParamBatch(this.paramBatchId).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          this.getParamBatchs();
          this.paramBatchId = "";
        });
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
/* 顶部 */
.formula_head {
  width: 95%;
  height: 50px;
  margin: auto;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #ddd;
  margin-bottom: 20px;
}
.tab-tit {
  font-size: 0;
  cursor: pointer;
}
.tab-tit a {
  display: inline-block;
  width: 108px;
  height: 50px;
  line-height: 50px;
  font-size: 14px;
  text-align: center;
  color: #333;
  text-decoration: none;
  margin-right: 20px;
}
.tab-tit .cur {
  color: #0256ff;
  border-bottom: 2px solid rgb(64, 158, 255);
}
.formula_searchk{
  width: 95%;
  margin: auto;
}
/* 内容 */
.formula_one {
  width: 97%;
  margin: auto;
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
/* 新建配方 */
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
  margin-left: 61px;
  margin-top: 22px;
}
button:hover {
  color: #ffffff;
}
</style>