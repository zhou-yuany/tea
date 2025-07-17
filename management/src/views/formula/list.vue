<template>
  <div class="formula_box">
    <!-- 顶部 -->
    <div class="formula_head white_bg" ref="getformulaheight">
      <div class="tab-tit">
        <a @click="changeCurId(0)" :class="{ cur: curId === 0 }">产品分类</a>
        <a @click="changeCurId(1)" :class="{ cur: curId === 1 }">配方管理</a>
        <a @click="changeCurId(4)" :class="{ cur: curId === 4 }">产品管理</a>
        <a @click="changeCurId(2)" :class="{ cur: curId === 2 }">产品配方</a>
        <!-- <a @click="changeCurId(5)" :class="{ cur: curId === 5 }">规格分类</a> -->
        <a @click="changeCurId(6)" :class="{ cur: curId === 6 }">规格参数</a>
        <!-- <a @click="changeCurId(3)" :class="{ cur: curId === 3 }"
          >规格参数配方</a
        > -->
      </div>
    </div>
    <el-form :inline="true" class="formula_searchk">
      <el-form-item label="">
        <el-input
          v-model="keyword"
          placeholder="请输入内容"
          @input="searchContent"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getList()" icon="el-icon-search"
          >搜索</el-button
        >
        <el-button type="default" @click="resetData()" icon="el-icon-my-clear"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 内容 -->
    <div class="tab-con">
      <!-- 产品分类 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 0"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <el-button
            type="success"
            @click="formulaAdd0()"
            style="float: right; margin-bottom: 10px"
            >新建</el-button
          >
        </div>
        <el-table border :data="cateAllList" style="width: 98%; margin: auto">
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="名称（小程序分类名称）"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="realName"
            label="实际名称"
            align="center"
            width="200"
          >
          </el-table-column>
          <!-- <el-table-column label="设备占位" align="center"
            ><template slot-scope="scope">
              <span>
                {{
                  scope.row.machineNo == "01"
                    ? "奶茶"
                    : scope.row.machineNo == "02"
                    ? "果茶"
                    : ""
                }}
              </span>
            </template>
          </el-table-column> -->
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-edit"
                class="editbtn white"
                @click="formulaEdit0(scope.row.id)"
                >编辑</el-button
              >
              <el-button
                type="danger"
                size="mini"
                class="dellbtn white"
                icon="el-icon-delete"
                @click="removeCateAll(scope.row.id)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total0"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getCateAllList"
        >
        </el-pagination>
        <!-- 新建/修改产品分类 -->
        <el-dialog
          :visible.sync="isShows0"
          :title="cateAllInfo.id > 0 ? '修改' : '新建'"
          @close="onCloseds0('cateAllInfo')"
          center
        >
          <el-form
            :model="cateAllInfo"
            ref="cateAllInfo"
            :rules="cateAllInfoRules"
            class="formula_form zengao"
          >
            <el-form-item label="名称（小程序分类名称）" prop="name">
              <el-input
                type="text"
                placeholder="请输入"
                class="selectk"
                maxlength="12"
                v-model="cateAllInfo.name"
              ></el-input>
            </el-form-item>
            <el-form-item label="实际名称" prop="realName">
              <el-input
                type="text"
                placeholder="请输入"
                v-model="cateAllInfo.realName"
                maxlength="12"
                class="selectk"
              ></el-input>
            </el-form-item>
            <!-- <el-form-item label="设备占位" prop="machineNo">
              <el-select
                v-model="cateAllInfo.machineNo"
                placeholder="请选择"
                class="selectk"
              >
                <el-option
                  v-for="item in machineNoList"
                  :key="item.id"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item> -->
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds0('cateAllInfo')">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdataParam0('cateAllInfo')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
      <!-- 配方管理 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 1"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <el-button
            type="success"
            @click="formulaAdd1()"
            style="float: right; margin-bottom: 10px"
            >新建</el-button
          >
        </div>
        <el-table
          border
          :data="goodsBatchList"
          style="width: 98%; margin: auto"
        >
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80"
            align="center"
          ></el-table-column>
          <el-table-column prop="name" label="名称" align="center">
          </el-table-column>
          <el-table-column prop="brand" label="品牌" align="center">
          </el-table-column>
          <el-table-column prop="outlet" label="出料口" align="center">
          </el-table-column>
          <el-table-column prop="price" label="收购价" align="center">
          </el-table-column>
          <!-- <el-table-column label="设备占位" align="center"
            ><template slot-scope="scope">
              <span>
                {{
                  scope.row.machineNo == "01"
                    ? "奶茶"
                    : scope.row.machineNo == "02"
                    ? "果茶"
                    : ""
                }}
              </span>
            </template>
          </el-table-column> -->
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-edit"
                class="editbtn white"
                @click="formulaEdit1(scope.row.id)"
                >编辑</el-button
              >
              <el-button
                type="danger"
                size="mini"
                class="dellbtn white"
                icon="el-icon-delete"
                @click="removeBatchInfo(scope.row.id)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total1"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getBatchList"
        >
        </el-pagination>
        <!-- 新建/修改配料 -->
        <el-dialog
          :visible.sync="isShows1"
          :title="batchInfo.id > 0 ? '修改' : '新建'"
          @close="onCloseds1('batchInfo')"
          center
        >
          <el-form
            :model="batchInfo"
            ref="batchInfo"
            :rules="batchInfoRules"
            class="formula_form zengao"
          >
          <el-form-item label="品牌" prop="brand">
              <el-select
              class="selectk"
                clearable
                filterable
                v-model="batchInfo.brand"
                placeholder="请选择"
              >
                <el-option
                  v-for="typePro in brands"
                  :key="typePro.id"
                  :label="typePro.name"
                  :value="typePro.name"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="配料名称" prop="name">
              <el-input
                type="text"
                placeholder="请输入"
                class="selectk"
                v-model="batchInfo.name"
              ></el-input>
            </el-form-item>

            
            <el-form-item label="出料口" prop="outlet">
              <el-input
                type="text"
                placeholder="请输入"
                class="selectk"
                v-model="batchInfo.outlet"
              ></el-input>
            </el-form-item>
            <el-form-item label="收购价（元）" prop="price">
              <el-input
                min="0"
                type="number"
                class="selectk"
                v-model.trim="batchInfo.price"
              />
            </el-form-item>
            <!-- <el-form-item label="设备占位" prop="machineNo">
              <el-select
                v-model="batchInfo.machineNo"
                placeholder="请选择"
                class="selectk"
              >
                <el-option
                  v-for="item in machineNoList"
                  :key="item.id"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item> -->
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds1('batchInfo')">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdataParam1('batchInfo')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
      <!-- 产品配方 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 2"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <el-button
            type="success"
            @click="productAdd()"
            style="float: right; margin-bottom: 10px"
            >新建</el-button
          >
        </div>
        <el-table border :data="productList" style="width: 98%; margin: auto">
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80"
            align="center"
          ></el-table-column>
          <el-table-column prop="goodsName" label="产品名称" align="center">
          </el-table-column>
          <el-table-column prop="paramName" label="规格" align="center">
          </el-table-column>
          <el-table-column prop="sugarName" label="糖度" align="center">
          </el-table-column>
          <el-table-column prop="temperatureName" label="温度" align="center">
          </el-table-column>
          <!-- <el-table-column
            prop="numberUnit"
            label="容量"
            align="center"
            width="260"
          >
          </el-table-column> -->
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                class="editbtn"
                icon="el-icon-edit"
                @click="editFormula(scope.row)"
                >编辑</el-button
              >
              <el-button
                type="danger"
                size="mini"
                class="dellbtn"
                icon="el-icon-delete"
                @click="dellFormula(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total2"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getGoodsBatchs"
        >
        </el-pagination>
        <!-- 新建配方 -->
        <el-dialog
          :visible.sync="isShow"
          :title="goodBatch.flag ? '修改配方' : '新建配方'"
          @close="onClose('goodBatch', 'batchRules')"
          center
        >
          <el-form
            :model="goodBatch"
            ref="goodBatch"
            :rules="goodBatchRules"
            class="formula_form"
          >
            <div style="display: flex; justify-content: center">
              <el-form-item label="商品名称" prop="goodsId">
                <el-select
                  @visible-change="selectValue1($event)"
                  :disabled="goodBatch.flag == 'info'"
                  v-model="goodBatch.goodsId"
                  placeholder="请选择"
                  @change="selectGoods(goodBatch.goodsId)"
                >
                  <el-option
                    v-for="item in goodsList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                prop="sizeId"
                label="商品杯型"
                style="margin-left: 10px"
              >
                <el-select
                  :disabled="goodBatch.flag == 'info'"
                  v-model="goodBatch.sizeId"
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in cupSizes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div style="display: flex; justify-content: center">
              <el-form-item prop="sugarId" label="商品糖度">
                <el-select
                  :disabled="goodBatch.flag == 'info'"
                  v-model="goodBatch.sugarId"
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in sugars"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="商品温度"
                prop="temperatureId"
                style="margin-left: 10px"
              >
                <el-select
                  :disabled="goodBatch.flag == 'info'"
                  v-model="goodBatch.temperatureId"
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in temperatures"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div style="display: flex; justify-content: center">
              <el-form-item
                label="售价（元）"
                prop="price"
                style="justify-content: flex-start"
              >
                <el-input
                  min="0"
                  type="number"
                  v-model.trim="goodBatch.price"
                  placeholder=" 售价（元）"
                />
              </el-form-item>
              <el-form-item
                label="成本（元）"
                prop="cost"
                style="justify-content: flex-start; margin-left: 10px"
              >
                <el-input
                  min="0"
                  type="number"
                  v-model.trim="goodBatch.cost"
                  placeholder=" 成本（元）"
                />
              </el-form-item>
            </div>
          </el-form>
          <div class="xinjiank">
            <el-button type="success" class="xinjian" @click="addRows()"
              >+新增配料</el-button
            >
          </div>
          <el-form ref="batchRules" :model="goodBatch">
            <el-table
              border
              :data="goodBatch.goodsAllBatchList"
              class="neitable"
              :header-cell-style="{ background: '#C2C2C2', color: '#3D3D3D' }"
            >
              <el-table-column label="商品配料">
                <template slot-scope="scope">
                  <el-form-item
                    :prop="'goodsAllBatchList.' + scope.$index + '.batchId'"
                    :rules="batchRules.batchId"
                  >
                    <el-select v-model="scope.row.batchId" placeholder="请选择">
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
                    :prop="'goodsAllBatchList.' + scope.$index + '.useNumber'"
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
                v-if="goodBatch.goodsAllBatchList.length > 1"
              >
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

          <div slot="footer" class="dialog-footer">
            <el-button @click="onClose('goodBatch', 'batchRules')"
              >取 消</el-button
            >
            <el-button
              type="primary"
              @click="saveGoodBatch('goodBatch', 'batchRules')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
      <!-- 参数规格配方 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 3"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <el-button
            type="success"
            @click="formulaAdd()"
            style="float: right; margin-bottom: 10px"
            >新建</el-button
          >
        </div>
        <el-table border :data="normsList" style="width: 98%; margin: auto">
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80"
            align="center"
          ></el-table-column>
          <el-table-column prop="name" label="名称" align="center">
          </el-table-column>
          <el-table-column label="类型" align="center" width="200">
            <template slot-scope="scope">
              <span>
                {{
                  scope.row.type == 1
                    ? "规格"
                    : scope.row.machineNo == 2
                    ? "糖度"
                    : scope.row.machineNo == 3
                    ? "温度"
                    : "小料"
                }}
              </span>
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="heatUseNumber"
            label="用量"
            align="center"
            width="200"
          >
          </el-table-column>
          <el-table-column
            prop="sugarName"
            label="糖度"
            align="center"
            width="200"
          >
          </el-table-column>
          <el-table-column
            prop="sugarUseNumber"
            label="用量"
            align="center"
            width="200"
          >
          </el-table-column> -->
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-edit"
                class="editbtn white"
                @click="formulaEdit3(scope.row.id)"
                >编辑</el-button
              >
              <el-button
                type="danger"
                size="mini"
                class="dellbtn white"
                icon="el-icon-delete"
                @click="dellFormulas3(scope.row.id)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total3"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getParamBatchs"
        >
        </el-pagination>
        <!-- 新建规格参数配方 -->
        <el-dialog
          :visible.sync="isShows"
          :title="paramBatch.id > 0 ? '修改规格参数配方' : '新建规格参数配方'"
          @close="onCloseds('paramBatch')"
          center
        >
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
            <!-- <el-form-item label="用量" class="inputk" prop="sugarUseNumber">
              <el-input
                type="number"
                placeholder="请输入"
                v-model="paramBatch.sugarUseNumber"
                class="selectk"
              ></el-input>
            </el-form-item> -->
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds('paramBatch')">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdataParam('paramBatch')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
      <!-- 产品管理 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 4"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <el-button
            type="success"
            @click="formulaAdd4()"
            style="float: right; margin-bottom: 10px"
            >新建</el-button
          >
        </div>
        <el-table
          border
          :data="goodsDetailsList"
          style="width: 98%; margin: auto"
        >
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80"
            align="center"
          ></el-table-column>
          <el-table-column prop="name" label="名称" align="center">
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            align="center"
            width="200"
          >
          </el-table-column>

          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-edit"
                class="editbtn white"
                @click="formulaEdit4(scope.row.id)"
                >编辑</el-button
              >
              <!-- <el-button
                type="danger"
                size="mini"
                class="dellbtn white"
                icon="el-icon-delete"
                @click="removeGoodsInfo(scope.row.id)"
                >删除</el-button
              > -->
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total4"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getGoodsList"
        >
        </el-pagination>
        <!-- 新建/修改产品名称 -->
        <el-dialog
          :visible.sync="isShows4"
          :title="goodsDetails.id > 0 ? '修改' : '新建'"
          @close="onCloseds4('goodsDetails')"
          center
        >
          <el-form
            :model="goodsDetails"
            ref="goodsDetails"
            :rules="goodsDetailsRules"
            class="formula_form zengao"
            style="width: 66%; margin: auto"
          >
            <el-form-item
              label="商品分类"
              prop="cateId"
              style="justify-content: flex-start"
            >
              <el-select
                v-model="goodsDetails.cateId"
                placeholder="请选择"
                class="selectk"
              >
                <el-option
                  v-for="item in cateList"
                  :key="item.id"
                  :label="item.name + '(' + item.realName + ')'"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item
              label="名称"
              prop="name"
              style="justify-content: flex-start"
            >
              <el-input
                type="text"
                placeholder="请输入"
                class="selectk"
                v-model="goodsDetails.name"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="封面"
              prop="url"
              style="justify-content: flex-start"
            >
              <el-upload
                accept=".png,.jpg,.jpeg"
                class="avatar-uploader"
                :action="'/tea-management/goods/upload'"
                :show-file-list="false"
                :on-success="handleCoverSuccess"
                :before-upload="beforeCoverUpload"
              >
                <img
                  v-if="goodsDetails.url"
                  :src="'/tea-management/image/' + goodsDetails.url"
                  class="avatar"
                  style="width: 30%; height: 30%"
                />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                <div
                  class="el-upload__tip"
                  slot="tip"
                  style="font-weight: bold; color: red"
                >
                  请上传5M以内，分辨率不小于450×450的正方形图片
                </div>
              </el-upload>
            </el-form-item>
            <el-form-item
              label="介绍"
              class="inputk"
              prop="introduce"
              style="justify-content: flex-start; margin-left: 0"
            >
              <el-input
                type="text"
                placeholder="请输入"
                v-model="goodsDetails.introduce"
                class="selectk"
              ></el-input>
            </el-form-item>
            <el-form-item
              label="小贴士"
              prop="tips"
              class="inputk"
              style="justify-content: flex-start; margin-left: 0"
            >
              <el-input
                type="text"
                placeholder="请输入"
                class="selectk"
                v-model="goodsDetails.tips"
              ></el-input>
            </el-form-item>
            <!-- <div style="display: flex">
              <el-form-item
                label="售价（元）"
                prop="price"
                style="justify-content: flex-start"
              >
                <el-input
                  min="0"
                  type="number"
                  v-model.trim="goodsDetails.price"
                  placeholder=" 售价（元）"
                />
              </el-form-item>
              <el-form-item
                label="成本（元）"
                prop="cost"
                style="justify-content: flex-start; margin-left: 10px"
              >
                <el-input
                  min="0"
                  type="number"
                  v-model.trim="goodsDetails.cost"
                  placeholder=" 成本（元）"
                />
              </el-form-item>
            </div> -->
            <div style="display: flex">
              <el-form-item
                label="总数量"
                prop="totalCount"
                style="justify-content: flex-start"
              >
                <el-input
                  min="0"
                  type="number"
                  v-model="goodsDetails.totalCount"
                  onkeyup="this.value=this.value.replace(/^(0+)|[^\d]+/g,'')"
                  placeholder=" 总数量"
                />
              </el-form-item>
              <el-form-item
                label="剩余数量"
                prop="haveCount"
                style="justify-content: flex-start; margin-left: 10px"
              >
                <el-input
                  min="0"
                  type="number"
                  v-model="goodsDetails.haveCount"
                  onkeyup="this.value=this.value.replace(/^(0+)|[^\d]+/g,'')"
                  placeholder=" 剩余数量"
                />
              </el-form-item>
            </div>
            <div style="display: flex">
              <el-form-item
                label="销售数量"
                prop="useCount"
                style="justify-content: flex-start"
              >
                <el-input
                  min="0"
                  type="number"
                  v-model="goodsDetails.useCount"
                  onkeyup="this.value=this.value.replace(/^(0+)|[^\d]+/g,'')"
                  placeholder=" 销售数量"
                />
              </el-form-item>
              <el-form-item
                label="是否上架"
                style="justify-content: flex-start; margin-left: 10px"
              >
                <el-switch
                  v-model="isGroundingFlag"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  @change="changeGround"
                >
                </el-switch>
              </el-form-item>
            </div>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds4('goodsDetails')">取 消</el-button>
            <el-button
              type="primary"
              @click="saveOrUpdataParam4('goodsDetails')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
      <!-- 规格分类 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 5"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <el-button
            type="success"
            @click="formulaAdd5()"
            style="float: right; margin-bottom: 10px"
            >新建</el-button
          >
        </div>
        <el-table border :data="paramCateList" style="width: 98%; margin: auto">
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80"
            align="center"
          ></el-table-column>
          <el-table-column prop="name" label="分类名称" align="center">
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            align="center"
            width="200"
          >
          </el-table-column>

          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-edit"
                class="editbtn white"
                @click="formulaEdit5(scope.row.id)"
                >编辑</el-button
              >
              <el-button
                type="danger"
                size="mini"
                class="dellbtn white"
                icon="el-icon-delete"
                @click="removeParamCateAll(scope.row.id)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total5"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getParamCateList"
        >
        </el-pagination>
        <!-- 新建/修改规格分类 -->
        <el-dialog
          :visible.sync="isShows5"
          :title="paramCateInfo.id > 0 ? '修改' : '新建'"
          @close="onCloseds5('paramCateInfo')"
          center
        >
          <el-form
            :model="paramCateInfo"
            ref="paramCateInfo"
            :rules="paramCateInfoRules"
            class="formula_form zengao"
          >
            <el-form-item label="分类名称" prop="name">
              <el-input
                type="text"
                placeholder="请输入"
                class="selectk"
                v-model="paramCateInfo.name"
              ></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds5('paramCateInfo')">取 消</el-button>
            <el-button
              type="primary"
              @click="saveOrUpdataParam5('paramCateInfo')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
      <!-- 规格参数 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 6"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <el-button
            type="success"
            @click="formulaAdd6()"
            style="float: right; margin-bottom: 10px"
            >新建</el-button
          >
        </div>
        <el-table border :data="paramList" style="width: 98%; margin: auto">
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80"
            align="center"
          ></el-table-column>

          <!-- <el-table-column
            prop="cateName"
            label="分类名称"
            align="center"
            width="200"
          >
          </el-table-column> -->
          <el-table-column prop="name" label="名称" align="center">
          </el-table-column>
          <el-table-column label="类型" align="center" width="200">
            <template slot-scope="scope">
              <span>
                {{
                  scope.row.type == 1
                    ? "规格"
                    : scope.row.type == 2
                    ? "糖度"
                    : scope.row.type == 3
                    ? "温度"
                    : "小料"
                }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-edit"
                class="editbtn white"
                @click="formulaEdit6(scope.row.id)"
                >编辑</el-button
              >
              <el-button
                type="danger"
                size="mini"
                class="dellbtn white"
                icon="el-icon-delete"
                @click="removeParam(scope.row.id)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total6"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getParamList"
        >
        </el-pagination>
        <!-- 新建/修改规格参数 -->
        <el-dialog
          :visible.sync="isShows6"
          :title="paramInfo.id > 0 ? '修改' : '新建'"
          @close="onCloseds6('paramInfo')"
          center
        >
          <el-form
            :model="paramInfo"
            ref="paramInfo"
            :rules="paramInfoRules"
            class="formula_form zengao"
            style="width: 66%; margin: auto"
          >
            <div style="display: flex">
              <el-form-item label="类型" prop="type">
                <el-select v-model="paramInfo.type" placeholder="请选择">
                  <el-option
                    v-for="item in typeList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <!-- <el-form-item
                label="规格分类"
                prop="paramCateId"
                style="margin-left: 10px"
              >
                <el-select v-model="paramInfo.paramCateId" placeholder="请选择">
                  <el-option
                    v-for="item in paramCates"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item> -->
            </div>
            <div style="display: flex">
              <el-form-item label="名称" prop="name">
                <el-input
                  type="text"
                  placeholder="请输入"
                  v-model="paramInfo.name"
                ></el-input>
              </el-form-item>
              <el-form-item
                v-if="paramInfo.paramCateId == 1 || paramInfo.paramCateId == 4"
                label="加价（元）"
                prop="addPrice"
                style="margin-left: 10px"
              >
                <el-input
                  type="number"
                  v-model.trim="paramInfo.addPrice"
                  placeholder=" 加价（元）"
                />
              </el-form-item>
            </div>
            <!-- <el-form-item
              label="用量（整数）"
              prop="useNumber"
              style="justify-content: flex-start"
            >
              <el-input
                type="number"
                v-model.trim="paramInfo.useNumber"
                placeholder=" 用量（整数）"
              />
            </el-form-item>
            <el-form-item
              label="是否可调整"
              style="justify-content: flex-start"
            >
              <el-switch
                v-model="isAdjust"
                active-color="#13ce66"
                inactive-color="#ff4949"
                @change="changeGround2"
              >
              </el-switch>
            </el-form-item>
            <el-form-item label="是否上架" style="justify-content: flex-start">
              <el-switch
                v-model="isRecommend"
                active-color="#13ce66"
                inactive-color="#ff4949"
                @change="changeGround3"
              >
              </el-switch>
            </el-form-item> -->
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds6('paramInfo')">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdataParam6('paramInfo')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>
  
<script>
import homeApi from "@/api/admins/formula";

export default {
  name: "Index",
  data() {
    const validateSpace = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的配方名称"));
      }
    };
    const validateSpace2 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的名称（小程序分类名称）"));
      }
    };
    const validateSpace3 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的实际名称"));
      }
    };
    const validateSpace4 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的配料名称"));
      }
    };
    const validateSpace5 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的品牌"));
      }
    };
    const validateSpace6 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的出料口"));
      }
    };
    const validateSpace7 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的商品名称"));
      }
    };
    const validateSpace8 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的商品介绍"));
      }
    };
    const validateSpace9 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的小贴士"));
      }
    };
    const validateSpace10 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的规格分类"));
      }
    };
    const validateSpace11 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的规格名称"));
      }
    };
    return {
      curId: 0,
      height: 0,
      page: 1,
      limit: 10,
      total0: 0,
      total1: 0,
      total2: 0,
      total3: 0,
      total4: 0,
      total5: 0,
      total6: 0,
      paramBatchId: "",
      goodBatchId: "",
      keyword: "",
      cateAllInfo: {},
      batchInfo: {},
      goodsDetails: {},
      goodsDetailsList: [],
      cateList: [],
      batchList: [],
      goodsList: [],
      cateAllList: [],
      goodsBatchList: [],
      paramCates: [],
      brands: [],
      isGroundingFlag: true,
      isAdjust: true,
      isRecommend: true,
      typeList: [
        {
          value: 1,
          label: "规格",
        },
        {
          value: 2,
          label: "糖度",
        },
        {
          value: 3,
          label: "温度",
        },
        {
          value: 4,
          label: "小料",
        },
      ],
      machineNoList: [
        {
          id: 0,
          value: "",
          label: "暂无",
        },
        {
          id: 1,
          value: "01",
          label: "奶茶",
        },
        {
          id: 2,
          value: "02",
          label: "果茶",
        },
      ],
      goodsId: "",
      goodBatch: {
        id: "",
        goodsId: "",
        goodsAllBatchList: [{ batchId: "", useNumber: "" }],
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
      paramList: [],
      paramCateList: [],
      cupSizes: [],
      sugars: [],
      batchList1: [],
      temperatures: [],
      isShows0: false,
      isShows1: false,
      isShows4: false,
      isShows5: false,
      isShows6: false,
      isShows: false,
      paramInfo: {},
      paramCateInfo: {},
      goodsDetailsRules: {
        name: [
          {
            required: true,
            message: "请输入商品名称",
            trigger: "blur",
          },
          { validator: validateSpace7, trigger: "blur" },
        ],
        introduce: [
          { required: true, message: "请输入商品介绍", trigger: "blur" },
          { validator: validateSpace8, trigger: "blur" },
        ],
        tips: [
          { required: true, message: "请输入小贴士", trigger: "blur" },
          { validator: validateSpace9, trigger: "blur" },
        ],
        url: [{ required: true, message: "请上传商品封面", trigger: "blur" }],
        cateId: [
          { required: true, message: "请选择商品分类", trigger: "blur" },
        ],
        price: [
          { required: true, message: "请输入售价", trigger: "input" },
          {
            pattern:
              /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
            message: "请输入正确金额格式,可保留两位小数",
          },
        ],
        cost: [
          { required: true, message: "请输入成本", trigger: "input" },
          {
            pattern:
              /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
            message: "请输入正确金额格式,可保留两位小数",
          },
        ],
        totalCount: [
          { required: true, message: "请输入总数量", trigger: "blur" },
        ],
        haveCount: [
          { required: true, message: "请输入剩余数量", trigger: "blur" },
        ],
        useCount: [
          { required: true, message: "请输入销售数量", trigger: "blur" },
        ],
        isGrounding: [
          { required: true, message: "请选择是否上架", trigger: "blur" },
        ],
      },
      batchInfoRules: {
        name: [
          {
            required: true,
            message: "请输入配料名称",
            trigger: "blur",
          },
          { validator: validateSpace4, trigger: "blur" },
        ],
        brand: [
          { required: true, message: "请输入品牌", trigger: "blur" },
          { validator: validateSpace5, trigger: "blur" },
        ],
        outlet: [
          { required: true, message: "请输入出料口", trigger: "blur" },
          { validator: validateSpace6, trigger: "blur" },
        ],
        price: [
          { required: true, message: "请输入收购价", trigger: "input" },
          {
            pattern:
              /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
            message: "请输入正确金额格式,可保留两位小数",
          },
        ],
      },
      cateAllInfoRules: {
        name: [
          {
            required: true,
            message: "请输入名称（小程序分类名称）",
            trigger: "blur",
          },
          { validator: validateSpace2, trigger: "blur" },
        ],
        realName: [
          { required: true, message: "请输入实际名称", trigger: "blur" },
          { validator: validateSpace3, trigger: "blur" },
        ],
      },
      goodBatchRules: {
        goodsId: [{ required: true, message: "请选择商品", trigger: "blur" }],
        sizeId: [{ required: true, message: "请选择杯型", trigger: "blur" }],
        sugarId: [{ required: true, message: "请选择糖度", trigger: "blur" }],
        temperatureId: [
          { required: true, message: "请选择温度", trigger: "blur" },
        ],
        price: [
          { required: true, message: "请输入售价", trigger: "input" },
          {
            pattern:
              /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
            message: "请输入正确金额格式,可保留两位小数",
          },
        ],
        cost: [
          { required: true, message: "请输入成本", trigger: "input" },
          {
            pattern:
              /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
            message: "请输入正确金额格式,可保留两位小数",
          },
        ],
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
      paramCateInfoRules: {
        name: [
          { required: true, message: "请输入规格分类", trigger: "blur" },
          { validator: validateSpace10, trigger: "blur" },
        ],
      },
      paramInfoRules: {
        name: [
          { required: true, message: "请输入规格名称", trigger: "blur" },
          { validator: validateSpace11, trigger: "blur" },
        ],
        addPrice: [
          { required: true, message: "请输入加价金额", trigger: "blur" },
          {
            pattern:
              /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
            message: "请输入正确金额格式,可保留两位小数",
          },
        ],
        useNumber: [
          { required: true, message: "请输入用量", trigger: "blur" },
          {
            pattern: /^(0|[1-9][0-9]*)$/,
            message: "请输入正确用量，只能输入正整数",
          },
        ],
        type: [{ required: true, message: "请选择类型", trigger: "blur" }],
        paramCateId: [
          { required: true, message: "请选择规格分类", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    
    if (this.curId == 0) {
      this.getCateAllList();
    } else if (this.curId == 1) {
      this.getBatchList();
      this.getAllBrand();
    } else if (this.curId == 2) {
      homeApi.allGoods().then((res) => {
        if (res.code == 20000) {
          this.goodsList = res.data.list;
        }
      });
      homeApi.getAllBatching().then((res) => {
        if (res.code == 20000) {
          this.batchList = res.data.batchList;
        }
      });
      this.getGoodsBatchs();
    } else if (this.curId == 3) {
      this.getParamBatchs();
    } else if (this.curId == 4) {
      this.getGoodsList();
      this.getAllCates();
    } else if (this.curId == 5) {
      this.getParamCateList();
    } else {
      this.getAllParamCates();
      this.getParamList();
    }
  },
  methods: {
    // 新建规格分类
    formulaAdd5() {
      this.isShows5 = true;
      this.paramCateInfo = {};
    },
    formulaEdit5(id) {
      this.isShows5 = true;
      homeApi.getParamCateInfo(id).then((res) => {
        if (res.code == 20000) {
          this.paramCateInfo = res.data.info;
        }
      });
    },
    // 添加
    addparamCateInfo() {
      homeApi
        .insertData5(this.paramCateInfo)
        .then((response) => {
          if (response.message == "该分类已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows5 = false;
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.getParamCateList();
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
    updateParamCate() {
      homeApi
        .updateData5(this.paramCateInfo.id, this.paramCateInfo)
        .then((response) => {
          if (response.message == "该分类已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows5 = false;
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.getParamCateList();
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
    removeParamCateAll(id) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        homeApi.deleteParamCate(id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getParamCateList();
        });
      });
    },

    saveOrUpdataParam5(paramCateInfo) {
      // 判断添加还是修改
      if (!this.paramCateInfo.id) {
        this.$refs[paramCateInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[paramCateInfo].clearValidate();
            this.addparamCateInfo();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[paramCateInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.updateParamCate();
          } else {
            return false;
          }
        });
      }
    },
    // 规格分类列表
    getParamCateList(page = 1) {
      this.page = page;
      homeApi
        .getParamCateList(this.page, this.limit, this.keyword)
        .then((res) => {
          if (res.code == 20000) {
            this.paramCateList = res.data.rows;
            this.total5 = res.data.total;
          }
        });
    },
    getAllParamCates() {
      homeApi.getAllParamCates().then((res) => {
        if (res.code == 20000) {
          this.paramCates = res.data.list;
        }
      });
    },
    // 查询所有品牌
    getAllBrand() {
      homeApi.getAllBrand().then((res) => {
        if (res.code == 20000) {
          this.brands = res.data.list;
        }
      });
    },
    // 新建规格参数
    formulaAdd6() {
      this.isShows6 = true;
      this.paramInfo = {};
    },
    formulaEdit6(id) {
      this.isShows6 = true;
      homeApi.getParamInfo(id).then((res) => {
        if (res.code == 20000) {
          this.paramInfo = res.data.info;
          this.isAdjust = this.paramInfo.isAdjust == 1 ? true : false;
          this.isRecommend = this.paramInfo.isRecommend == 1 ? true : false;
        }
      });
    },
    // 添加
    addParamInfo() {
      this.paramInfo.isAdjust = this.isAdjust ? 1 : 2;
      this.paramInfo.isRecommend = this.isRecommend ? 1 : 2;
      homeApi
        .insertData6(this.paramInfo)
        .then((response) => {
          if (response.message == "该规格已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows6 = false;
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.getParamList();
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
    updateParam() {
      this.paramInfo.isAdjust = this.isAdjust ? 1 : 2;
      this.paramInfo.isRecommend = this.isRecommend ? 1 : 2;
      homeApi
        .updateData6(this.paramInfo.id, this.paramInfo)
        .then((response) => {
          if (response.message == "该规格已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows6 = false;
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.getParamList();
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
    removeParam(id) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        homeApi.deleteParam(id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getParamList();
        });
      });
    },

    saveOrUpdataParam6(paramInfo) {
      // 判断添加还是修改
      if (!this.paramInfo.id) {
        this.$refs[paramInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[paramInfo].clearValidate();
            this.addParamInfo();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[paramInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.updateParam();
          } else {
            return false;
          }
        });
      }
    },
    // 规格列表
    getParamList(page = 1) {
      this.page = page;
      homeApi.getParamList(this.page, this.limit, this.keyword).then((res) => {
        if (res.code == 20000) {
          this.paramList = res.data.rows;
          this.total6 = res.data.total;
        }
      });
    },
    getList() {
      if (this.curId == 0) {
        this.getCateAllList();
      } else if (this.curId == 1) {
        this.getBatchList();
        this.getAllBrand();
      } else if (this.curId == 2) {
        this.getGoodsBatchs();
        homeApi.getAllBatching().then((res) => {
      if (res.code == 20000) {
        this.batchList = res.data.batchList;
      }
    });
      } else if (this.curId == 3) {
        this.getParamBatchs();
      } else if (this.curId == 4) {
        this.getGoodsList();
        this.getAllCates();
      } else if (this.curId == 5) {
        this.getParamCateList();
      } else {
        this.getAllParamCates();
        this.getParamList();
      }
    },
    changeGround(e) {
      this.isGroundingFlag = e;
    },
    changeGround2(e) {
      this.isAdjust = e;
    },
    changeGround3(e) {
      this.isRecommend = e;
    },
    handleCoverSuccess(res, file) {
      // this.carrierInfo.codeDrive=res.data.url
      this.$set(this.goodsDetails, "url", res.data.url);
    },
    beforeCoverUpload(file) {
      //添加文件类型的限制
      let types = ["image/jpeg", "image/jpg", "image/png"];
      const isImage = types.includes(file.type);
      const isLt5M = file.size / 1024 / 1024 <= 5;
      if (!isLt5M) {
        this.$message.error("上传图片大小不能超过 5MB!");
        return false;
      }
      if (!isImage) {
        this.$message.error("上传图片只能是 JPG、JPEG、PNG 格式!");
        return false;
      }
    },
    selectValue1(e) {
      if (e) {
 
        // this.productList.map(item => item.)
 
        // this.goodsList = this.goodsList.filter((item) => item.addBatch == 2);
      } else {
        homeApi.allGoods().then((res) => {
          if (res.code == 20000) {
            this.goodsList = res.data.list;
          }
        });
      }
    },
    selectGoods(goodsId) {
      homeApi.getAllBatching().then((res) => {
        if (res.code == 20000) {
          // this.batchList = res.data.batchList;
          this.goodsId = goodsId;
          this.goodBatch.goodsAllBatchList = [{ batchId: "", useNumber: "" }];
          let goodsInfo = this.goodsList.filter((item) => item.id == goodsId);
          if (this.goodBatch.goodsAllBatchList.length > 0) {
            this.goodBatch.goodsAllBatchList.forEach((item, index) => {
              // this.batchList1 = this.batchList.filter(
              //   (item) => item.machineNo == goodsInfo[0].machineNo
              // );
              this.$set(
                this.goodBatch.goodsAllBatchList[index],
                "list",
                this.batchList
              ); // 设置当前第几行的默认值
            });
          } else {
            // this.batchList1 = this.batchList.filter(
            //   (item) => item.machineNo == goodsInfo[0].machineNo
            // );
            this.$set(
              this.goodBatch.goodsAllBatchList[0],
              "list",
              this.batchList
            ); // 设置当前第几行的默认值
          }
          this.$forceUpdate();
        }
      });
    },
    changeCurId(i) {
      this.page = 1;
      this.curId = i;
      if (i == 0) {
        this.getCateAllList();
      } else if (i == 1) {
        this.getBatchList();
        this.getAllBrand();
      } else if (i == 2) {
        homeApi.allGoods().then((res) => {
          if (res.code == 20000) {
            this.goodsList = res.data.list;
            let list = res.data.paramList;
            if (list.length > 0) {
              this.cupSizes = list.filter((item) => item.type == 1);
              this.sugars = list.filter((item) => item.type == 2);
              this.temperatures = list.filter((item) => item.type == 3);
            }
          }
        });
        this.getGoodsBatchs();
        homeApi.getAllBatching().then((res) => {
      if (res.code == 20000) {
        this.batchList = res.data.batchList;
      }
    });
      } else if (i == 3) {
        this.getParamBatchs();
      } else if (i == 4) {
        this.getGoodsList();
        this.getAllCates();
      } else if (this.curId == 5) {
        this.getParamCateList();
      } else {
        this.getAllParamCates();
        this.getParamList();
      }
    },
    saveGoodBatch(goodBatch, batchRules) {
      this.$refs[goodBatch].validate((valid) => {
        if (valid) {
          this.$refs[batchRules].validate((validBatch) => {
            if (validBatch) {
              if (this.goodBatch.id != "") {
                homeApi.updateCostCards(this.goodBatch).then((res) => {
                  if (res.code == 20000) {
                    this.$message({
                      type: "success",
                      message: "修改成功",
                    });
                    this.isShow = false;

                    this.goodBatch = {
                      id: "",
                      goodsId: "",
                      goodsAllBatchList: [{ batchId: "", useNumber: "" }],
                    };

                    this.$refs[goodBatch].clearValidate();
                    this.$refs[validBatch].clearValidate();
                    this.getGoodsBatchs();
                  }
                });
              } else {
                homeApi.addCostCards(this.goodBatch).then((res) => {
                  if (res.code == 20000) {
                    this.$message({
                      type: "success",
                      message: "添加成功",
                    });
                    this.isShow = false;
                    this.getGoodsBatchs();

                    this.goodBatch = {
                      id: "",
                      goodsId: "",
                      goodsAllBatchList: [{ batchId: "", useNumber: "" }],
                    };

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
    // 添加
    addCateAll() {
      homeApi
        .insertData0(this.cateAllInfo)
        .then((response) => {
          if (response.message == "该分类已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows0 = false;
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.getCateAllList();
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
    updateCateAll() {
      homeApi
        .updateData0(this.cateAllInfo.id, this.cateAllInfo)
        .then((response) => {
          if (response.message == "该分类已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows0 = false;
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.getCateAllList();
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
    removeCateAll(id) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        homeApi.deleteCateAll(id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getCateAllList();
        });
      });
    },

    getAllCates() {
      homeApi.getAllCates().then((res) => {
        if (res.code == 20000) {
          this.cateList = res.data.list;
        }
      });
    },
    saveOrUpdataParam0(cateAllInfo) {
      // 判断添加还是修改
      if (!this.cateAllInfo.id) {
        this.$refs[cateAllInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[cateAllInfo].clearValidate();
            this.addCateAll();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[cateAllInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.updateCateAll();
          } else {
            return false;
          }
        });
      }
    },
    // 添加
    addBatchInfo() {
      homeApi
        .insertData1(this.batchInfo)
        .then((response) => {
          if (response.message == "该配料已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows1 = false;
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
        .updateData1(this.batchInfo.id, this.batchInfo)
        .then((response) => {
          if (response.message == "该配料已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows1 = false;
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
    removeBatchInfo(id) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        homeApi.deleteBatchInfo(id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getBatchList();
        });
      });
    },
    saveOrUpdataParam1(batchInfo) {
      // 判断添加还是修改
      if (!this.batchInfo.id) {
        this.$refs[batchInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[batchInfo].clearValidate();
            this.addBatchInfo();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[batchInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.updateBatchInfo();
          } else {
            return false;
          }
        });
      }
    },
    // 添加
    addGoodsInfo() {
      this.goodsDetails.isGrounding = this.isGroundingFlag ? 1 : 2;
      homeApi
        .insertData4(this.goodsDetails)
        .then((response) => {
          if (response.message == "该商品已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows4 = false;
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.getGoodsList();
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
    updateGoodsInfo() {
      this.goodsDetails.isGrounding = this.isGroundingFlag ? 1 : 2;
      homeApi
        .updateData4(this.goodsDetails.id, this.goodsDetails)
        .then((response) => {
          if (response.message == "该商品已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows4 = false;
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.getGoodsList();
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
    removeGoodsInfo(id) {
      this.$confirm("删除前是否确定没有商户经营该产品, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        homeApi.deleteGoods(id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getGoodsList();
        });
      });
    },
    saveOrUpdataParam4(goodsDetails) {
      // 判断添加还是修改
      if (!this.goodsDetails.id) {
        this.$refs[goodsDetails].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[goodsDetails].clearValidate();
            this.addGoodsInfo();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[goodsDetails].validate((valid) => {
          if (valid) {
            // 修改
            this.updateGoodsInfo();
          } else {
            return false;
          }
        });
      }
    },
    // 删除
    dellFormulas3(id) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        homeApi.removeParamBatch(id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getParamBatchs();
        });
      });
    },
    saveOrUpdataParam(paramBatch) {
      this.$refs[paramBatch].validate((valid) => {
        if (valid) {
          if (this.paramBatch.id) {
            homeApi.updateParamBatch(this.paramBatch).then((res) => {
              if (res.code == 20000) {
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
            homeApi.addParamBatch(this.paramBatch).then((res) => {
              if (res.code == 20000) {
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
    resetData() {
      this.keyword = "";
      if (this.curId == 0) {
        this.getCateAllList();
      } else if (this.curId == 1) {
        this.getBatchList();
      } else if (this.curId == 2) {
        this.getGoodsBatchs();
      } else if (this.curId == 3) {
        this.getParamBatchs();
      } else if (this.curId == 4) {
        this.getGoodsList();
        this.getAllCates();
      } else if (this.curId == 5) {
        this.getParamCateList();
      } else {
        this.getAllParamCates();
        this.getParamList();
      }
    },

    // 分类列表
    getCateAllList(page = 1) {
      this.page = page;
      homeApi
        .getCateAllList(this.page, this.limit, this.keyword)
        .then((res) => {
          if (res.code == 20000) {
            this.cateAllList = res.data.rows;
            this.total0 = res.data.total;
          }
        });
    },
    // 配料管理列表
    getBatchList(page = 1) {
      this.page = page;
      homeApi.getBatchList(this.page, this.limit, this.keyword).then((res) => {
        if (res.code == 20000) {
          this.goodsBatchList = res.data.rows;
          this.total1 = res.data.total;
        }
      });
    },
    // 产品分类列表
    getGoodsList(page = 1) {
      this.page = page;
      homeApi.getGoodsList(this.page, this.limit, this.keyword).then((res) => {
        if (res.code == 20000) {
          this.goodsDetailsList = res.data.rows;
          this.total4 = res.data.total;
        }
      });
    },
    // 产品配方列表
    getGoodsBatchs(page = 1) {
      this.page = page;
      homeApi
        .getGoodBatchList(this.page, this.limit, this.keyword)
        .then((res) => {
          if (res.code == 20000) {
            this.productList = res.data.rows;
            this.total2 = res.data.total;
          }
        });
    },
    // 规格配方列表
    getParamBatchs(page = 1) {
      this.page = page;
      homeApi
        .getParamBatchList(this.page, this.limit, this.keyword)
        .then((res) => {
          if (res.code == 20000) {
            this.normsList = res.data.rows;
            this.total3 = res.data.total;
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
      homeApi.allGoods().then((res) => {
        if (res.code == 20000) {
          this.goodsList = res.data.list;
        }
      });
    },
    onClose(goodBatch, batchRules) {
      this.isShow = false;
      var shopId = localStorage.getItem("shopId");
      this.goodBatch = {
        id: "",
        goodsId: "",
        goodsAllBatchList: [{ batchId: "", useNumber: "" }],
      };
    },

    //编辑配方
    editFormula(data) {
      homeApi
        .getGoodsBatchInfo(
          data.goodsId,
          data.sizeId,
          data.sugarId,
          data.temperatureId
        )
        .then((res) => {
          if (res.code == 20000) {
            this.goodBatch = res.data.info;
            this.goodBatch.goodsAllBatchList.forEach((item) => {
              item.list = this.batchList;
            });
            this.isShow = true;
          }
        });
    },

    //删除配方
    dellFormula(data) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        homeApi
          .removeGoodBatch(
            data.goodsId,
            data.sizeId,
            data.sugarId,
            data.temperatureId
          )
          .then((response) => {
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
      this.goodBatch.goodsAllBatchList.push(obj);
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
      this.goodBatch.goodsAllBatchList.splice(scope.$index, 1);
    },

    // 新建分类
    formulaAdd0() {
      this.isShows0 = true;
      this.cateAllInfo = {};
    },
    // 新建配料
    formulaAdd1() {
      this.isShows1 = true;
      this.batchInfo = {};
    },
    // 新建商品
    formulaAdd4() {
      this.isShows4 = true;
      this.goodsDetails = {};
    },
    // 新建规格参数配方
    formulaAdd() {
      //   this.isShows = true;
      homeApi.getHeatAndSugarParams().then((res) => {
        if (res.code == 20000) {
          this.sugarList = res.data.sugarList;
          this.heatList = res.data.heatList;
          this.isShows = true;
        }
      });
    },
    formulaEdit0(id) {
      this.isShows0 = true;
      homeApi.getCateAllInfo(id).then((res) => {
        if (res.code == 20000) {
          this.cateAllInfo = res.data.info;
        }
      });
    },
    formulaEdit1(id) {
      this.isShows1 = true;
      homeApi.getBatchInfo(id).then((res) => {
        if (res.code == 20000) {
          this.batchInfo = res.data.info;
        }
      });
    },
    formulaEdit4(id) {
      this.isShows4 = true;
      homeApi.getGoodInfo(id).then((res) => {
        if (res.code == 20000) {
          this.goodsDetails = res.data.info;
          this.isGroundingFlag =
            this.goodsDetails.isGrounding == 1 ? true : false;
        }
      });
    },
    formulaEdit3(id) {
      homeApi.getParamAllBatchInfo(id).then((res) => {
        if (res.code == 20000) {
          this.paramBatch = res.data.info;
          homeApi.getHeatAndSugarParams().then((res) => {
            if (res.code == 20000) {
              this.sugarList = res.data.sugarList;
              this.heatList = res.data.heatList;
              this.isShows = true;
            }
          });
        }
      });
    },
    onCloseds0(cateAllInfo) {
      this.$refs[cateAllInfo].clearValidate();
      this.isShows0 = false;
    },

    onCloseds1(batchInfo) {
      this.$refs[batchInfo].clearValidate();
      this.isShows1 = false;
    },

    onCloseds4(goodsDetails) {
      this.$refs[goodsDetails].clearValidate();
      this.isShows4 = false;
    },

    onCloseds(paramBatch) {
      this.$refs[paramBatch].clearValidate();
      this.isShows = false;
    },
    onCloseds5(paramCateInfo) {
      this.$refs[paramCateInfo].clearValidate();
      this.isShows5 = false;
    },
    onCloseds6(paramInfo) {
      this.$refs[paramInfo].clearValidate();
      this.isShows6 = false;
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
};
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
.formula_searchk {
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
  margin: auto;
}
.xinjiank {
  width: 650px;
  margin: auto;
  text-align: right;
}
.xinjian {
  margin: 22px 0;
}
button:hover {
  color: #ffffff;
}
</style>