<template>
  <div class="inventory_box">
    <!-- 顶部 -->
    <div class="inventory_head white_bg" ref="getinventoryheight">
      <img
        src="../../static/back.png"
        alt=""
        class="inventory_back"
        @click="goBack()"
      />
      <span>库存</span>
      <div class="tab-tit">
        <a @click="curId = 0" :class="{ cur: curId === 0 }">当前库存</a>
        <a @click="curId = 1" :class="{ cur: curId === 1 }">商品成本卡</a>
        <a @click="curId = 2" :class="{ cur: curId === 2 }">出入库记录</a>
      </div>
      <div class="inventory_searchk">
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
        class="inventory_one white_bg"
        :style="{ height: height + 'px' }"
        v-show="curId === 0"
      >
        <el-table
          border
          :data="batchList"
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
          <el-table-column prop="brand" label="品牌">
            <template slot-scope="scope">
              <span>{{ scope.row.brandName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="名称">
            <template slot-scope="scope">
              <span>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="specifications" label="规格" width="328">
            <template slot-scope="scope">
              <span>{{ scope.row.unit == "g" ? "L" : "KG" }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="inventory" label="当前库存">
            <template slot-scope="scope">
              {{
                scope.row.unit == "g"
                  ? parseFloat(scope.row.totalCount / 1000)
                  : parseFloat(scope.row.totalCount / 1000)
              }}
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
      <div
        class="inventory_one white_bg"
        :style="{ height: height + 'px' }"
        v-show="curId === 1"
      >
        <el-table
          border
          :data="goodsCartList"
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
          <el-table-column prop="type" label="分类">
            <template slot-scope="scope">
              <span>{{ scope.row.realName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="商品名称">
            <template slot-scope="scope">
              <span>{{ scope.row.goodsName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="specifications" label="商品规格">
            <template slot-scope="scope">
              <span>{{
                scope.row.paramName
              }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="411">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                @click="handleSave(scope.row)"
                class="pfxqbtn white"
                >配方详情</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getCartList"
        >
        </el-pagination>
        <!-- 配方详情 -->
        <div class="mask" v-show="isShow">
          <div class="white_bg inventory_whitek">
            <div class="inventory_shouh">
              配方详情<img src="../../static/close.png" @click="onClose()" />
            </div>
            <div class="blue inventory_title">{{ curGoodsName }}</div>
            <el-table
              border
              :data="CartInfoList"
              style="width: 85%; margin: auto"
            >
              <el-table-column prop="goodsname" label="配比商品" align="left">
                <template slot-scope="scope">
                  <span>{{ scope.row.batchName }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="num" label="配比数量" align="left">
                <template slot-scope="scope">
                  <span>{{ scope.row.batchUseCount }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="unit" label="核算单位" align="left">
                <template slot-scope="scope">
                  <span>{{ scope.row.batchUnit }}</span>
                </template>
              </el-table-column>
            </el-table>
            <div style="width: 100%; text-align: center">
              <button
                type="button"
                class="inventory_btn white_bg"
                @click="onClose()"
              >
                关闭
              </button>
            </div>
          </div>
        </div>
      </div>
      <div
        class="inventory_one white_bg"
        :style="{ height: height + 'px' }"
        v-show="curId === 2"
      >
        <div class="inventory_shai">
          <el-select v-model="inventoryType" placeholder="请选择">
            <el-option
              v-for="item in inventoryTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <div>
            <button type="button" class="inventory_ruku white" @click="ruku()">
              入库
            </button>
            <button
              type="button"
              class="white_bg inventory_chuku gray"
              @click="chuku()"
            >
              出库
            </button>
          </div>
        </div>
        <el-table
          border
          :data="inventoryList"
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
          <el-table-column prop="type" label="类型">
            <template slot-scope="scope">
              <span>{{ scope.row.type == 1 ? "入库" : "出库" }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="brand" label="品牌">
            <template slot-scope="scope">
              <span>{{ scope.row.brand }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="名称">
            <template slot-scope="scope">
              <span>{{ scope.row.batchName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="specifications" label="规格">
            <template slot-scope="scope">
              <span>{{ scope.row.useNumber + scope.row.unit }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="number" label="数量">
            <template slot-scope="scope">
              <span>{{ scope.row.count }}</span>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getInventoryList"
        >
        </el-pagination>
        <!-- 出库弹窗 -->
        <div class="mask" v-show="chukuShow">
          <div class="white_bg inventory_whitek" style="height: auto">
            <div class="inventory_shouh">
              出库<img src="../../static/close.png" @click="onClosed()" />
            </div>
            <el-form class="inventory_form">
              <el-form-item label="品牌">
                <el-select
                  v-model="selectBrand"
                  placeholder="请选择"
                  @change="changeBrand()"
                  style="width: 93%"
                >
                  <el-option
                    v-for="item in brands"
                    :key="item.id"
                    :label="item.brand"
                    :value="item.brand"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="名称">
                <el-select
                  v-model="selectbatchId"
                  placeholder="请选择"
                  @change="changebatchId()"
                  style="width: 93%"
                >
                  <el-option
                    v-for="item in batchNames"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="规格">
                <el-select
                  v-model="selectbatchNumberId"
                  placeholder="请选择"
                  style="width: 93%"
                >
                  <el-option
                    v-for="item in batchNumbers"
                    :key="item.id"
                    :label="item.useNumber + item.unit"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="数量" style="width: 97%">
                <el-input
                  type="number"
                  placeholder=""
                  v-model="inventoryCount"
                ></el-input>
              </el-form-item>
              <el-form-item label="备注" style="width: 94%">
                <el-input
                  type="textarea"
                  placeholder=""
                  v-model="inventoryContent"
                  class="textareak"
                ></el-input>
              </el-form-item>
              <el-form-item style="text-align: center">
                <button
                  type="button"
                  @click="onClosed()"
                  class="white_bg cancelbtn"
                >
                  取消
                </button>
                <button
                  type="button"
                  @click="onSure()"
                  class="blue_bg white surebtn"
                >
                  确定
                </button>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <!-- 入库弹窗 -->
        <div class="mask" v-show="rukuShow">
          <div class="white_bg inventory_whitek" style="height: auto">
            <div class="inventory_shouh">
              入库<img src="../../static/close.png" @click="onCloseds()" />
            </div>
            <el-form class="inventory_form">
              <el-form-item label="品牌">
                <el-input placeholder="" v-model="batchForm.brand"></el-input>
              </el-form-item>
              <el-form-item label="名称">
                <el-input
                  placeholder=""
                  v-model="batchForm.batchName"
                ></el-input>
              </el-form-item>
              <el-form-item label="规格">
                <el-input
                  type="number"
                  placeholder=""
                  v-model="batchForm.useNumber"
                ></el-input>
              </el-form-item>
              <el-form-item label="">
                <el-radio v-model="batchForm.unit" label="g">克</el-radio>
                <el-radio v-model="batchForm.unit" label="ml">毫升</el-radio>
                <div></div>
              </el-form-item>
              <el-form-item label="数量">
                <el-input
                  type="number"
                  placeholder=""
                  v-model="batchForm.count"
                ></el-input>
              </el-form-item>
              <el-form-item label="备注" style="width: 97%">
                <el-input
                  type="textarea"
                  placeholder=""
                  v-model="batchForm.remarks"
                  class="textareak"
                ></el-input>
              </el-form-item>
              <el-form-item style="text-align: center">
                <button
                  type="button"
                  @click="onCloseds()"
                  class="white_bg cancelbtn"
                >
                  取消
                </button>
                <button
                  type="button"
                  @click="onSures()"
                  class="blue_bg white surebtn"
                >
                  确定
                </button>
              </el-form-item>
            </el-form>
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
  name: "ItmanTable2",
  data() {
    return {
      page: 1,
      limit: 10,
      total: 0,
      curId: 0,
      batchList: [],
      batchList1: [],
      goodsCartList: [],
      goodsCartList1: [],
      CartInfoList: [],
      inventoryList: [],
      inventoryList1: [],
      curGoodsName: "",
      type: 0,
      selectBrand: "",
      selectbatchId: "",
      selectbatchNumberId: "",
      inventoryCount: "",
      inventoryContent: '',
      keyword: "",
      batchForm: {
        type: 1,
        shopId: "",
        batchId: "",
        batchName: "",
        brand: "",
        count: "",
        useNumber: "",
        unit: "g",
        remarks: ''
      },
      inventoryType: 0,
      brands: [],
      batchNames: [],
      batchNumbers: [],
      inventoryTypes: [
        {
          value: 0,
          label: "全部",
        },
        {
          value: 1,
          label: "入库",
        },
        {
          value: 2,
          label: "出库",
        },
      ],
      isShow: false,
      formula: [{}, {}, {}],
      chukuShow: false,
      rukuShow: false,
      height: 0,
    };
  },
  watch: {
    curId(newValue, oldValue) {
      if (newValue == 0) {
        this.getBatchList();
      } else if (newValue == 1) {
        this.getCartList();
      } else {
        this.getInventoryList();
      }
    },
    inventoryType(newValue, oldValue) {
      if (newValue == oldValue) {
        return;
      } else {
        this.getInventoryList();
      }
    },
  },
  created() {
    this.getBatchList();
  },
  methods: {

    searchGoods() {
      if (this.curId == 0) {
        let batchList = this.batchList;
        if (this.keyword == "") {
          this.batchList = this.batchList1;
        } else {
          this.batchList = batchList.filter((item) => {
            return (
              item.brandName.indexOf(this.keyword) > -1 ||
              item.name.indexOf(this.keyword) > -1
            );
          });
        }
      }
      if (this.curId == 1) {
        if (this.keyword == "") {
          this.goodsCartList = this.goodsCartList1;
        } else {
          this.goodsCartList = this.goodsCartList.filter((item) => {
            return (
              item.goodsName.indexOf(this.keyword) > -1 ||
              item.paramName.indexOf(this.keyword) > -1 ||
              item.realName.indexOf(this.keyword) > -1
            );
          });
        }
      }
      if (this.curId == 2) {
        if (this.keyword == "") {
          this.inventoryList = this.inventoryList1;
        } else {
          this.inventoryList = this.inventoryList.filter((item) => {
            return (
              item.batchName.indexOf(this.keyword) > -1 ||
              item.brand.indexOf(this.keyword) > -1
            );
          });
        }
      }
    },
    getBatchList(page = 1) {
      this.page = page;
      let shopId = localStorage.getItem("shopId");
      homeApi.batchList(this.page, this.limit, shopId).then((res) => {
        if (res.data.code == 20000) {
          this.batchList = res.data.data.rows;
          this.batchList1 = res.data.data.rows;
          this.total = res.data.data.total;
        }
      });
    },
    getCartList(page = 1) {
      this.page = page;
      let shopId = localStorage.getItem("shopId");
      homeApi.goodsCartList(this.page, this.limit, shopId).then((res) => {
        if (res.data.code == 20000) {
          this.goodsCartList = res.data.data.rows;
          this.goodsCartList1 = res.data.data.rows;
          this.total = res.data.data.total;
        }
      });
    },

    getInventoryList(page = 1) {
      this.page = page;
      let shopId = localStorage.getItem("shopId");
      homeApi
        .inventoryList(this.page, this.limit, shopId, this.inventoryType)
        .then((res) => {
          if (res.data.code == 20000) {
            this.inventoryList = res.data.data.rows;
            this.inventoryList1 = res.data.data.rows;
            this.total = res.data.data.total;
          }
        });
    },

    // 返回
    goBack: function () {
      this.$router.go(-1);
    },

    // 配方详情
    handleSave(data) {
      this.isShow = true;
      this.curGoodsName = data.goodsName + "—" + data.useNumber + data.unit;
      homeApi.cartInfo(data.id, data.sizeId).then((res) => {
        if (res.data.code == 20000) {
          this.CartInfoList = res.data.data.list;
        }
      });
    },

    getBatchBrands() {
      let shopId = localStorage.getItem("shopId");
      homeApi.getBatchBrands(shopId).then((res) => {
        if (res.data.code == 20000) {
          this.brands = res.data.data.list;
        }
      });
    },

    getBatchNames() {
      let that = this;
      let shopId = localStorage.getItem("shopId");
      homeApi.getBatchNames(shopId).then((res) => {
        if (res.data.code == 20000) {
          that.batchNames =
            res.data.data.list.length > 0
              ? res.data.data.list.filter(
                  (item) => item.brand == that.selectBrand
                )
              : res.data.data.list;
        }
      });
    },

    getBatchNumbers() {
      let that = this;
      let shopId = localStorage.getItem("shopId");
      homeApi.getBatchNumbers(shopId).then((res) => {
        if (res.data.code == 20000) {
          this.batchNumbers =
            res.data.data.list.length > 0
              ? res.data.data.list.filter(
                  (item) => item.batchId == that.selectbatchId
                )
              : res.data.data.list;
          console.log(this.batchNumbers);
        }
      });
    },
    onClose() {
      this.isShow = false;
    },

    // 出库弹窗
    chuku() {
      this.getBatchBrands();
      this.chukuShow = true;
    },
    onClosed() {
      this.chukuShow = false;
      this.selectbatchId = "";
      this.selectbatchNumberId = "";
      this.selectBrand = "";
      this.inventoryCount = "";
      this.inventoryContent = "";
    },
    onSure() {
      let that = this;
      if (this.selectBrand == "") {
        this.$toast({
          message: "请选择品牌",
          position: "top",
        });
        return;
      }

      if (this.selectbatchId == "") {
        this.$toast({
          message: "请选择名称",
          position: "top",
        });
        return;
      }
      if (this.selectbatchNumberId == "") {
        this.$toast({
          message: "请选择规格",
          position: "top",
        });
        return;
      }
      if (this.inventoryCount == "") {
        this.$toast({
          message: "请输入数量",
          position: "top",
        });
        return;
      }
      homeApi
        .outInventory(this.selectbatchNumberId, this.inventoryCount, this.inventoryContent)
        .then((res) => {
          if (res.data.code == 20000) {
            that.chukuShow = false;
            that.selectbatchId = "";
            that.selectbatchNumberId = "";
            that.selectBrand = "";
            this.inventoryCount = "";
            if (that.inventoryType == 0) {
              that.getBatchList();
            }
            if (that.inventoryType == 1) {
              that.getCartList();
            }
            if (that.inventoryType == 2) {
              that.getInventoryList();
            }
          }
        });
    },

    // 入库弹窗
    ruku() {
      this.rukuShow = true;
    },
    onCloseds() {
      this.rukuShow = false;
      this.batchForm = {
        type: 1,
        shopId: "",
        batchId: "",
        batchName: "",
        brand: "",
        count: "",
        useNumber: "",
        unit: "g",
        remarks: ''
      };
    },
    changeBrand() {
      this.getBatchNames();
    },
    changebatchId() {
      this.getBatchNumbers();
    },
    onSures() {
      let that = this;
      if (this.batchForm.batchName == "") {
        this.$toast({
          message: "请输入配方名称",
          position: "top",
        });
        return;
      }

      if (this.batchForm.brand == "") {
        this.$toast({
          message: "请输入品牌",
          position: "top",
        });
        return;
      }
      if (this.batchForm.useNumber == "") {
        this.$toast({
          message: "请输入规格",
          position: "top",
        });
        return;
      }
      if (this.batchForm.count == "") {
        this.$toast({
          message: "请输入数量",
          position: "top",
        });
        return;
      }
      this.batchForm.shopId = localStorage.getItem("shopId");
      homeApi.insertInventory(this.batchForm).then((res) => {
        if (res.data.code == 20000) {
          that.rukuShow = false;
          that.batchForm = {
            type: 1,
            shopId: "",
            batchId: "",
            batchName: "",
            brand: "",
            count: "",
            useNumber: "",
            unit: "g",
            remarks: ''
          };
          if (that.inventoryType == 0) {
            that.getBatchList();
          }
          if (that.inventoryType == 1) {
            that.getCartList();
          }
          if (that.inventoryType == 2) {
            that.getInventoryList();
          }
        }
      });
    },
  },
  mounted() {
    let inventoryheight = this.$refs.getinventoryheight.offsetHeight;
    let windowheight = window.innerHeight;
    this.height =
      windowheight - inventoryheight - localStorage.getItem("touheight") - 57;
  },
});
</script>
    
<style scoped>
.inventory_box {
  background: #f2f3f4;
  padding-top: 18px;
  padding-bottom: 22px;
}
/* 顶部 */
.inventory_head {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
}
.inventory_back {
  width: 30px;
  height: 30px;
  display: block;
  margin-right: 38px;
  margin-left: 56px;
  cursor: pointer;
}
.inventory_head span {
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
  width: 140px;
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
.inventory_searchk {
  width: 300px;
  height: 40px;
  border: 1px solid #999999;
  display: flex;
  align-items: center;
  border-radius: 8px;
}
.inventory_searchk img {
  width: 24px;
  height: 24px;
  display: block;
  margin: 0 8px;
}
.inventory_searchk input {
  width: 250px;
  height: 38px;
  font-size: 20px;
  text-align: left;
}
/* 内容 */
.tab-con {
  padding-top: 17px;
}
.inventory_one {
  width: 97%;
  margin: auto;
  border-radius: 8px;
  padding-top: 31px;
}
.pfxqbtn {
  width: 100px;
  height: 40px;
  background: #0256ff;
  border-radius: 8px;
  font-size: 20px;
  text-align: center;
  line-height: 40px;
  margin: 0;
  padding: 0;
}
/* 配方详情 */
.inventory_whitek {
  width: 500px;
  height: 420px;
  border-radius: 15px;
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -200px;
  margin-left: -250px;
}
.inventory_shouh {
  width: 100%;
  height: 40px;
  position: relative;
  font-size: 16px;
  margin-left: 20px;
  line-height: 40px;
}
.inventory_shouh img {
  width: 14px;
  height: 14px;
  display: block;
  position: absolute;
  top: 10px;
  right: 30px;
  z-index: 1;
  cursor: pointer;
}
.inventory_title {
  width: 100%;
  text-align: center;
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 20px;
}
.inventory_btn {
  width: 100px;
  height: 30px;
  border: 1px solid #000;
  margin: auto;
  text-align: center;
  font-size: 12px;
  line-height: 28px;
  margin-top: 20px;
}
.textareak {
  width: 107%;
}
/* 出入库记录 */
.inventory_shai {
  width: 96.5%;
  height: 45px;
  margin: auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 22px;
}
.inventory_chuku {
  width: 100px;
  border: 1px solid #999999;
  height: 44px;
  font-size: 20px;
  font-family: PingFangSCBold-Bold;
  margin-left: 40px;
  border-radius: 4px;
  text-align: center;
}
.inventory_ruku {
  width: 100px;
  height: 44px;
  font-size: 20px;
  font-family: PingFangSCBold-Bold;
  border-radius: 4px;
  background: #0256ff;
  text-align: center;
}
.inventory_form {
  width: 90%;
  margin: auto;
}
.inventory_form .el-form-item {
  display: flex;
  justify-content: center;
  align-items: center;
}
.cancelbtn {
  width: 100px;
  border: 1px solid #000;
  margin: 0 5px;
}
.surebtn {
  width: 100px;
  border: 1px solid #0256ff;
  margin: 0 5px;
}
</style>