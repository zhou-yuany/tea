<template>
  <div class="formula_box">
    <!-- 顶部 -->
    <div class="formula_head white_bg" ref="getformulaheight">
      <div class="tab-tit">
        <a @click="changeCurId(12)" :class="{ cur: curId === 12 }">设备管理</a>
        <a @click="changeCurId(0)" :class="{ cur: curId === 0 }">产品分类</a>
        <a @click="changeCurId(4)" :class="{ cur: curId === 4 }">产品管理</a>
        <!-- <a @click="changeCurId(6)" :class="{ cur: curId === 6 }">规格参数</a> -->
        <a @click="changeCurId(8)" :class="{ cur: curId === 8 }">配料管理</a>
        <a @click="changeCurId(10)" :class="{ cur: curId === 10 }">产品配方</a>
      </div>
    </div>
    <el-form :inline="true" class="formula_searchk" v-if="curId < 6">
      <el-form-item label="">
        <el-input v-model="keyword" placeholder="请输入内容"></el-input>
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
      <!-- 设备管理 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 12"
        :style="{ height: height + 'px' }"
      >
        <el-table border :data="equipmentShopList" style="width: 98%; margin: auto">
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80"
            align="center"
          ></el-table-column>

          <el-table-column
            prop="equipmentNo"
            label="设备编号"
            align="center"
          >
          </el-table-column>
          <el-table-column prop="name" label="设备别名" align="center">
          </el-table-column>

          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-edit"
                class="editbtn white"
                @click="formulaEdit12(scope.row.id)"
                >编辑</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :current-page="page"
          :page-size="limit"
          :total="total12"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getEquipmentShopList"
        >
        </el-pagination>
        <!-- 新建/修改设备别名 -->
        <el-dialog
          width="60%"
          :visible.sync="isShows12"
          title="修改设备别名"
          @close="onCloseds12('equipmentInfo')"
          center
        >
          <el-form
            :model="equipmentInfo"
            ref="equipmentInfo"
            :rules="equipmentInfoRules"
            class="formula_form zengao"
          >
          <el-form-item label="设备编号" prop="equipmentNo">
              <el-input
                :disabled="true"
                type="text"
                class="selectk"
                v-model="equipmentInfo.equipmentNo"
              ></el-input>
            </el-form-item>
            <el-form-item label="设备别名" prop="name">
              <el-input
                type="text"
                class="selectk"
                v-model="equipmentInfo.name"
              ></el-input>
            </el-form-item>
           
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds12('equipmentInfo')">取 消</el-button>
            <el-button type="primary" @click="saveParam12('equipmentInfo')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
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
        <el-table border :data="catePageList" style="width: 98%; margin: auto">
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
                @click="removeCate(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :current-page="page"
          :page-size="limit"
          :total="total0"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getcatePageList"
        >
        </el-pagination>
        <!-- 新建/修改产品分类 -->
        <el-dialog
          width="60%"
          :visible.sync="isShows0"
          :title="cateInfo.id > 0 ? '修改' : '新建'"
          @close="onCloseds0('cateInfo')"
          center
        >
          <el-form
            :model="cateInfo"
            ref="cateInfo"
            :rules="cateInfoRules"
            class="formula_form zengao"
          >
            <el-form-item label="分类实际名称" class="inputk" prop="cateAllId">
              <el-select
                v-model="cateInfo.cateAllId"
                placeholder="请选择"
                class="selectk"
              >
                <el-option
                  v-for="item in cateAllList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="分类名称" prop="name">
              <el-input
                type="text"
                placeholder="请输入"
                v-model="cateInfo.name"
                maxlength="12"
                class="selectk"
              ></el-input>
            </el-form-item>
            <el-form-item label="描述" prop="description">
              <el-input
                type="text"
                placeholder="请输入"
                v-model="cateInfo.description"
                maxlength="100"
                class="selectk"
              ></el-input>
            </el-form-item>
            <!-- <el-form-item label="设备占位" prop="machineNo">
              <el-select
                v-model="cateInfo.machineNo"
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
            <el-button @click="onCloseds0('cateInfo')">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdataParam0('cateInfo')"
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
              <el-button
                type="danger"
                size="mini"
                class="dellbtn white"
                icon="el-icon-delete"
                @click="removeGoodsInfo(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :current-page="page"
          :page-size="limit"
          :total="total4"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getGoodsList"
        >
        </el-pagination>
        <!-- 新建/修改产品名称 -->
        <el-dialog
          width="60%"
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
          >
            <el-form-item label="商品选择" prop="goodsId">
              <el-select
                :disabled="goodsDetails.id > 0"
                v-model="goodsDetails.goodsId"
                placeholder="请选择"
                class="selectk"
                @change="selectGoods"
              >
                <el-option
                  v-for="item in goodsList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <div v-if="goodsDetails.goodsId > 0">
              <el-form-item label="商品分类" prop="cateId">
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
              <el-form-item label="设备编号" prop="equipmentId">
                <el-select
                  v-model="goodsDetails.equipmentId"
                  placeholder="请选择"
                  class="selectk"
                >
                  <el-option
                    v-for="(item, index) in equipmentList"
                    :key="index"
                    :label="item.equipmentNo+ item.name != null ? '' : ('('+item.name+')')"
                    :value="item.equipmentNo"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="名称" prop="name">
                <el-input
                  type="text"
                  placeholder="请输入"
                  class="selectk"
                  v-model="goodsDetails.name"
                ></el-input>
              </el-form-item>
              <el-form-item label="封面" prop="url">
                <el-upload
                  accept=".png,.jpg,.jpeg"
                  class="avatar-uploader"
                  :action="'/tea-management/shopToGoods/upload'"
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
              <el-form-item label="介绍" class="inputk" prop="introduce">
                <el-input
                  type="text"
                  placeholder="请输入"
                  v-model="goodsDetails.introduce"
                  class="selectk"
                ></el-input>
              </el-form-item>
              <el-form-item label="小贴士" prop="tips">
                <el-input
                  type="text"
                  placeholder="请输入"
                  class="selectk"
                  v-model="goodsDetails.tips"
                ></el-input>
              </el-form-item>
              
              <el-form-item
                label="总数量"
                style="width: 78%"
                label-width="140px"
                prop="totalCount"
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
                label="销售数量"
                style="width: 78%"
                label-width="140px"
                prop="useCount"
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
                style="width: 78%"
                label-width="140px"
              >
                <el-switch
                  v-model="isGroundingFlag"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  @change="changeGround"
                >
                </el-switch>
              </el-form-item>

              <el-form-item label="详情图" label-width="100px" prop="detailsUrl">
          <div class="up-picture-box">
            <el-upload
              :action="'/tea-management/shopToGoods/uploadDetails'"
              list-type="picture-card"
              :on-preview="handlePictureCardPreview"
              :on-success="handleDetailsSuccess"
              :on-remove="handleDetailsRemove"
              :file-list="detailsList"
            >
              <div class="el-upload__tip" slot="tip" style="font-weight: bold">
                JPG或PNG格式！照片大小不超过2M
              </div>
            </el-upload>
          </div>
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

      <!-- 规格参数 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 6"
        :style="{ height: height + 'px' }"
      >
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
          <el-table-column label="加价" align="center">
            <template slot-scope="scope">
              {{
                scope.row.type == 1 || scope.row.type == 4
                  ? "￥" + scope.row.addPrice
                  : "不可加价"
              }}
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
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :current-page="page"
          :page-size="limit"
          :total="total6"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getParamList"
        >
        </el-pagination>
        <!-- 新建/修改规格参数 -->
        <el-dialog
          width="60%"
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
          >
            <el-form-item label="名称" prop="name">
              <el-input
                :disabled="true"
                type="text"
                class="selectk"
                v-model="paramInfo.name"
              ></el-input>
            </el-form-item>
            <el-form-item
              v-if="paramInfo.type == 1 || paramInfo.type == 4"
              label="加价（元）"
              style="width: 78%"
              label-width="140px"
              prop="addPrice"
            >
              <el-input
                type="number"
                v-model.trim="paramInfo.addPrice"
                placeholder=" 加价（元）"
              />
            </el-form-item>

            <el-form-item
              label="用量（整数）"
              style="width: 78%"
              label-width="140px"
              prop="useNumber"
            >
              <el-input
                type="number"
                v-model.trim="paramInfo.useNumber"
                placeholder=" 用量（整数）"
              />
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds6('paramInfo')">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdataParam6('paramInfo')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>

      <!-- 配料管理 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 8"
        :style="{ height: height + 'px' }"
      >
        <div class="addk">
          <el-button
            type="success"
            @click="formulaAdd8()"
            style="float: right; margin-bottom: 10px"
            >新建</el-button
          >
        </div>
        <el-table border :data="batchList" style="width: 98%; margin: auto">
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80"
            align="center"
          ></el-table-column>
          <el-table-column prop="batchName" label="配料名称" align="center">
          </el-table-column>
          <el-table-column
            prop="outlet"
            label="出料口"
            align="center"
            width="200"
          >
          </el-table-column>
          <el-table-column
            prop="totalCount"
            label="总数量"
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
                @click="formulaEdit8(scope.row.id)"
                >编辑</el-button
              >
              <el-button
                type="danger"
                size="mini"
                class="dellbtn white"
                icon="el-icon-delete"
                @click="removeBatchInfo(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :current-page="page"
          :page-size="limit"
          :total="total8"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getBatchList"
        >
        </el-pagination>
        <!-- 新建/修改配料管理 -->
        <el-dialog
          width="60%"
          :visible.sync="isShows8"
          :title="batchInfo.id > 0 ? '修改' : '新建'"
          @close="onCloseds8('batchInfo')"
          center
        >
          <el-form
            :model="batchInfo"
            ref="batchInfo"
            :rules="batchInfoRules"
            class="formula_form zengao"
          >
            <el-form-item label="配料" class="inputk" prop="batchId">
              <el-select
                v-model="batchInfo.batchId"
                placeholder="请选择"
                class="selectk"
                @change="selectBatch"
              >
                <el-option
                  :disabled="batchInfo.id > 0"
                  v-for="item in allBatchs"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="出料口" prop="outlet">
              <el-input
                type="text"
                placeholder="请输入"
                v-model="batchInfo.outlet"
                class="selectk"
              ></el-input>
            </el-form-item>
            <el-form-item label="总数量" prop="totalCount">
              <el-input
                type="totalCount"
                v-model="batchInfo.totalCount"
                onkeyup="this.value=this.value.replace(/^(0+)|[^\d]+/g,'')"
                placeholder=" 总数量"
              />
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds8('batchInfo')">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdataParam8('batchInfo')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>

      <!-- 产品配方 -->
      <div
        class="formula_one white_bg"
        v-show="curId === 10"
        :style="{ height: height + 'px' }"
      >
        <!-- <div class="addk">
          <el-button
            type="success"
            @click="productAdd()"
            style="float: right; margin-bottom: 10px"
            >新建</el-button
          >
        </div> -->
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
          :current-page="page"
          :page-size="limit"
          :total="total10"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getGoodsBatchs"
        >
        </el-pagination>
        <!-- 新建配方 -->
        <el-dialog
          width="80%"
          :visible.sync="isShows10"
          :title="goodBatch.id > 0 ? '修改配方' : '新建配方'"
          @close="onClose10('goodBatch', 'batchRules')"
          center
        >
          <el-form
            :model="goodBatch"
            ref="goodBatch"
            :rules="goodBatchRules"
            class="formula_form"
          >
            <el-form-item label="商品名称" prop="goodsId" label-width="100px" class="ttts">
              <el-select
                @visible-change="selectValue1($event)"
                :disabled="goodBatch.id > 0"
                v-model="goodBatch.goodsId"
                placeholder="请选择"
                @change="selectGoodsInfo(goodBatch.goodsId)"
                class="selectk"
              >
                <el-option
                  v-for="item in shopGoodsList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="商品规格" prop="sizeId" label-width="100px" class="ttts">
              <el-select
                :disabled="goodBatch.id > 0"
                v-model="goodBatch.sizeId"
                placeholder="请选择"
                class="selectk"
                @change="selectCupSize(goodBatch.sizeId)"
              >
                <el-option
                  v-for="item in cupSizes"
                  :key="item.id"
                  :label="item.name"
                  :value="item.paramId"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="糖度" prop="sugarId" label-width="100px" class="ttts">
              <el-select
                :disabled="goodBatch.id > 0"
                v-model="goodBatch.sugarId"
                placeholder="请选择"
                class="selectk"
                @change="selectSugar(goodBatch.sugarId)"
              >
                <el-option
                  v-for="item in sugars"
                  :key="item.id"
                  :label="item.name"
                  :value="item.paramId"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="温度" prop="temperatureId" label-width="100px" class="ttts">
              <el-select
                :disabled="goodBatch.id > 0"
                v-model="goodBatch.temperatureId"
                placeholder="请选择"
                class="selectk"
                @change="selectTemperatureId(goodBatch.temperatureId)"
              >
                <el-option
                  v-for="item in temperatures"
                  :key="item.id"
                  :label="item.name"
                  :value="item.paramId"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item
                v-if="isShowData && goodBatch.goodsId > 0 && goodBatch.sizeId > 0 && goodBatch.sugarId > 0 && goodBatch.temperatureId > 0"
                label="售价（元）" label-width="100px" class="ttts"
                prop="price"
              >
                <el-input
                  min="0"
                  type="number"
                  v-model="goodBatch.price"
                  placeholder=" 售价（元）" class="selectk"
                />
              </el-form-item>
              <el-form-item
              v-if="isShowData && goodBatch.goodsId > 0 && goodBatch.sizeId > 0 && goodBatch.sugarId > 0 && goodBatch.temperatureId > 0"
                label="成本（元）"
                label-width="100px"
                prop="cost" class="ttts"
              >
                <el-input
                  min="0"
                  type="number"
                  v-model="goodBatch.cost"
                  placeholder=" 成本（元）" class="selectk"
                />
              </el-form-item>
          </el-form>
          <!-- <el-form ref="batchRules" :model="goodBatch" v-if="goodBatch.goodsId > 0 && goodBatch.sizeId > 0">
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
                    <el-select v-model="scope.row.batchId" placeholder="请选择" :disabled="true">
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
              
            </el-table>
          </el-form> -->
          <!-- <el-button type="success" class="xinjian" @click="addRows()"
            >+新增</el-button
          > -->
          <!-- <div slot="footer" class="dialog-footer">
            <el-button @click="onClose10('goodBatch', 'batchRules')"
              >取 消</el-button
            >
            <el-button
              type="primary"
              @click="saveGoodBatch('goodBatch', 'batchRules')"
              >确 定</el-button
            >
          </div> -->
          <div slot="footer" class="dialog-footer">
            <el-button @click="onClose10('goodBatch')">取 消</el-button>
            <el-button :disabled="!isShowData" type="primary" @click="saveGoodBatch('goodBatch')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>
  
<script>
import homeApi from "@/api/admins/commodity";
import { createSocket } from "../websocket/websocket.js";

export default {
  name: "Index",
  data() {
    const validateSpace2 = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的分类名称"));
      }
    };
    const validateSpace3 = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的描述"));
      }
    };

    const validateSpace7 = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的商品名称"));
      }
    };
    const validateSpace8 = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的商品介绍"));
      }
    };
    const validateSpace9 = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的小贴士"));
      }
    };
    const validateSpace11 = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的规格名称"));
      }
    };
    const validateSpace12 = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的出料口"));
      }
    };
    const validateSpace13 = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的设备别名"));
      }
    };
    return {
      curId: 12,
      height: 0,
      page: 1,
      limit: 10,
      total0: 0,
      total4: 0,
      total6: 0,
      total8: 0,
      total10: 0,
      total12: 0,
      keyword: "",
      isShowData: false,
      cateInfo: {},
      batchInfo: {},
      goodsDetails: {},
      equipmentInfo: {},
      goodBatch: {
        id: "",
        goodsId: "",
        goodsToBatchList: [{ batchId: "", useNumber: "" }],
      },
      goodsDetailsList: [],
      detailsList: [],
      cateList: [],
      equipmentShopList: [],
      equipmentList: [],
      goodsList: [],
      shopGoodsList: [],
      catePageList: [],
      cateAllList: [],
      batchList: [],
      cupSizes: [],
      sugars: [],
      temperatures: [],
      isGroundingFlag: true,
      paramList: [],
      allBatchs: [],
      paramInfo: {},
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
      productList: [{}, {}],
      isShow: false,
      isShows0: false,
      isShows4: false,
      isShows6: false,
      isShows8: false,
      isShows10: false,
      isShows12: false,
      goodsDetailsRules: {
        goodsId: [{ required: true, message: "请选择商品", trigger: "blur" }],
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
        equipmentId: [
          { required: true, message: "请选择设备编号", trigger: "blur" },
        ],
        price: [
          { required: true, message: "请输入售价", trigger: "blur" },
          {
            pattern:
              /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
            message: "请输入正确金额格式,可保留两位小数",
          },
        ],
        cost: [
          { required: true, message: "请输入成本", trigger: "blur" },
          {
            pattern:
              /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
            message: "请输入正确金额格式,可保留两位小数",
          },
        ],
        totalCount: [
          { required: true, message: "请输入总数量", trigger: "blur" },
        ],
        useCount: [
          { required: false, message: "请输入销售数量", trigger: "blur" },
        ],
        isGrounding: [
          { required: true, message: "请选择是否上架", trigger: "blur" },
        ],
      },
      cateInfoRules: {
        name: [
          {
            required: true,
            message: "请输入分类名称",
            trigger: "blur",
          },
          { validator: validateSpace2, trigger: "blur" },
        ],
        cateAllId: [
          { required: true, message: "请选择实际名称", trigger: "blur" },
        ],
        description: [
          {
            required: true,
            message: "请输入描述",
            trigger: "blur",
          },
          { validator: validateSpace3, trigger: "blur" },
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
      },
      batchInfoRules: {
        outlet: [
          {
            required: true,
            message: "请输入出料口",
            trigger: "blur",
          },
          { validator: validateSpace12, trigger: "blur" },
        ],
        batchId: [
          { required: true, message: "请选择实际名称", trigger: "blur" },
        ],
        totalCount: [
          { required: true, message: "请输入总数量", trigger: "blur" },
          {
            pattern: /^(0|[1-9][0-9]*)$/,
            message: "请输入正确数量，只能输入正整数",
          },
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
          { required: true, message: "请输入售价", trigger: "blur" },
          {
            pattern:
              /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
            message: "请输入正确金额格式,可保留两位小数",
          },
        ],
        cost: [
          { required: true, message: "请输入成本", trigger: "blur" },
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
      equipmentInfoRules: {
        name: [
          { required: true, message: "请输入设备别名", trigger: "blur" },
          { validator: validateSpace13, trigger: "blur" },
        ],
      },
    };
  },
  created() {
    if (this.curId == 0) {
      this.getAllCates();
      this.getcatePageList();
    } else if (this.curId == 4) {
      this.getGoodsList();
      this.getCates();
    } else if (this.curId == 6) {
      this.getParamList();
    } else if (this.curId == 8) {
      this.getBatchList();
      createSocket();
    this.getsocketData = (e) => {
      // 创建接收消息函数
      let data = e && e.detail.data;
      if (data) {
        if (data != "ping" && JSON.parse(data).message == ("下单")) {
          this.getBatchList();
        }
      }
    };
    // 注册监听事件
    window.addEventListener("onmessageWS", this.getsocketData);
    } else if(this.curId == 10){
      // this.getShopGoodsList();
      this.getShopGoodsList();
      this.getShopParams();
      this.getGoodsBatchs();
    }else{
      this.getEquipmentShopList();
    }
  },
  methods: {
     // 上传协议成功调用方法
    handleDetailsSuccess(res, file) {
      this.goodsDetails.detailsUrl =
        this.goodsDetails.detailsUrl == undefined
          ? res.data.url
          : this.goodsDetails.detailsUrl + "," + res.data.url;
      this.detailsList =
        this.goodsDetails.detailsUrl == undefined
          ? []
          : this.goodsDetails.detailsUrl.split(",");
    },
    handleDetailsRemove(res) {
      let rmUrl = res.response
        ? res.response.data.url
        : res.url.replace("/tea-management/image/", "");
      this.goodsDetails.detailsUrl = this.goodsDetails.detailsUrl
        .replace(rmUrl, "")
        .replace(",,", ",");
    },

    // 上传照片之前调用的方法
    handlePictureCardPreview(file) {
      const isJPG = file.type === "image/jpeg";
      // const isGIF = file.type === 'image/gif';
      const isPNG = file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
      }
      // return (isJPG || isBMP || isGIF || isPNG) && isLt2M;
      return (isJPG || isPNG) && isLt2M;
    },
    getBtachAllList() {
      homeApi.getAllBatching().then((res) => {
        if (res.code == 20000) {
          this.batchList = res.data.batchList;
        }
      });
    },
    selectValue1(e) {
      if (e) {
      } else {
        var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
        homeApi.getAllGoods(shopId).then((res) => {
          if (res.code == 20000) {
            this.goodsList = res.data.list;
          }
        });
      }
    },
    selectGoodsInfo(goodsId) {
      if (
        this.goodBatch.sizeId > 0 &&
        this.goodBatch.sugarId > 0 &&
        this.goodBatch.temperatureId > 0
      ) {
        var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
        homeApi
          .getAllGoodsBatchInfo(
            goodsId,
            shopId,
            this.goodBatch.sizeId,
            this.goodBatch.sugarId,
            this.goodBatch.temperatureId
          )
          .then((res) => {
            if (res.code == 20000) {
              if (res.message == "请到产品管理页添加此产品！") {
                this.$message({
                  type: "warning",
                  message: "请到产品管理页添加此产品！",
                });
                this.isShowData = false;
              } else if(res.message == "总平台未添加该配方，请联系总平台添加"){
                this.$message({
                  type: "warning",
                  message: "总平台未添加该配方，请联系总平台添加",
                });
                this.isShowData = false;
              }else{
                this.isShowData = true;
                let list = res.data.info.goodsToBatchList;
                list.forEach((item) => {
                  item.list = this.batchList;
                });
                // this.goodBatch.price = res.data.info.price;
                // this.goodBatch.cost = res.data.info.cost;
                this.goodBatch.goodsToBatchList = list;
                this.$set(this.goodBatch, "price", res.data.info.price);
                this.$set(this.goodBatch, "cost", res.data.info.cost);
              }
            }
          });
        //   homeApi.getAllBatching().then((res) => {
        //   if (res.code == 20000) {
        //     this.batchList = res.data.batchList;
        //     this.goodsId = goodsId;
        //     this.goodBatch.goodsToBatchList = [{ batchId: "", useNumber: "" }];
        //     let goodsInfo = this.goodsList.filter((item) => item.id == goodsId);
        //     if (this.goodBatch.goodsToBatchList.length > 0) {
        //       this.goodBatch.goodsToBatchList.forEach((item, index) => {
        //         this.batchList = this.batchList.filter(
        //           (item) => item.machineNo == goodsInfo[0].machineNo
        //         );
        //         this.$set(
        //           this.goodBatch.goodsToBatchList[index],
        //           "list",
        //           this.batchList
        //         ); // 设置当前第几行的默认值
        //       });
        //     } else {
        //       this.batchList = this.batchList.filter(
        //         (item) => item.machineNo == goodsInfo[0].machineNo
        //       );
        //       this.$set(
        //         this.goodBatch.goodsToBatchList[0],
        //         "list",
        //         this.batchList
        //       ); // 设置当前第几行的默认值
        //     }
        //     this.$forceUpdate();
        //   }
        // });
      }
    },
    selectCupSize(sizeId) {
      if (
        this.goodBatch.goodsId > 0 &&
        this.goodBatch.sugarId > 0 &&
        this.goodBatch.temperatureId > 0
      ) {
        var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
        homeApi
          .getAllGoodsBatchInfo(
            this.goodBatch.goodsId,
            shopId,
            sizeId,
            this.goodBatch.sugarId,
            this.goodBatch.temperatureId
          )
          .then((res) => {
            if (res.code == 20000) {
              if (res.message == "请到产品管理页添加此产品！") {
                this.$message({
                  type: "warning",
                  message: "请到产品管理页添加此产品！",
                });
                this.isShowData = false;
              } else if(res.message == "总平台未添加该配方，请联系总平台添加"){
                this.$message({
                  type: "warning",
                  message: "总平台未添加该配方，请联系总平台添加",
                });
                this.isShowData = false;
              }else{
                this.isShowData = true;
                let list = res.data.info.goodsToBatchList;
                list.forEach((item) => {
                  item.list = this.batchList;
                });
                // this.goodBatch.price = res.data.info.price;
                // this.goodBatch.cost = res.data.info.cost;
                this.goodBatch.goodsToBatchList = list;
                this.$set(this.goodBatch, "price", res.data.info.price);
                this.$set(this.goodBatch, "cost", res.data.info.cost);
              }
            }
          });

        //   homeApi.getAllBatching().then((res) => {
        //   if (res.code == 20000) {
        //     this.batchList = res.data.batchList;
        //     this.goodsId = goodsId;
        //     this.goodBatch.goodsToBatchList = [{ batchId: "", useNumber: "" }];
        //     let goodsInfo = this.goodsList.filter((item) => item.id == goodsId);
        //     if (this.goodBatch.goodsToBatchList.length > 0) {
        //       this.goodBatch.goodsToBatchList.forEach((item, index) => {
        //         this.batchList = this.batchList.filter(
        //           (item) => item.machineNo == goodsInfo[0].machineNo
        //         );
        //         this.$set(
        //           this.goodBatch.goodsToBatchList[index],
        //           "list",
        //           this.batchList
        //         ); // 设置当前第几行的默认值
        //       });
        //     } else {
        //       this.batchList = this.batchList.filter(
        //         (item) => item.machineNo == goodsInfo[0].machineNo
        //       );
        //       this.$set(
        //         this.goodBatch.goodsToBatchList[0],
        //         "list",
        //         this.batchList
        //       ); // 设置当前第几行的默认值
        //     }
        //     this.$forceUpdate();
        //   }
        // });
      }
    },
    selectSugar(sugarId) {
      if (
        this.goodBatch.goodsId > 0 &&
        this.goodBatch.sizeId > 0 &&
        this.goodBatch.temperatureId > 0
      ) {
        var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
        homeApi
          .getAllGoodsBatchInfo(
            this.goodBatch.goodsId,
            shopId,
            this.goodBatch.sizeId,
            sugarId,
            this.goodBatch.temperatureId
          )
          .then((res) => {
            if (res.code == 20000) {
              if (res.message == "请到产品管理页添加此产品！") {
                this.$message({
                  type: "warning",
                  message: "请到产品管理页添加此产品！",
                });
                this.isShowData = false;
              } else if(res.message == "总平台未添加该配方，请联系总平台添加"){
                this.$message({
                  type: "warning",
                  message: "总平台未添加该配方，请联系总平台添加",
                });
                this.isShowData = false;
              }else{
                this.isShowData = true;
                let list = res.data.info.goodsToBatchList;
                list.forEach((item) => {
                  item.list = this.batchList;
                });
                // this.goodBatch.price = res.data.info.price;
                // this.goodBatch.cost = res.data.info.cost;
                this.goodBatch.goodsToBatchList = list;
                this.$set(this.goodBatch, "price", res.data.info.price);
                this.$set(this.goodBatch, "cost", res.data.info.cost);
              }
            }
          });
      }
    },
    selectTemperatureId(temperatureId) {
      if (
        this.goodBatch.goodsId > 0 &&
        this.goodBatch.sizeId > 0 &&
        this.goodBatch.sugarId > 0
      ) {
        var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
        homeApi
          .getAllGoodsBatchInfo(
            this.goodBatch.goodsId,
            shopId,
            this.goodBatch.sizeId,
            this.goodBatch.sugarId,
            temperatureId
          )
          .then((res) => {
            if (res.code == 20000) {
              if (res.message == "请到产品管理页添加此产品！") {
                this.$message({
                  type: "warning",
                  message: "请到产品管理页添加此产品！",
                });
                this.isShowData = false;
              } else if(res.message == "总平台未添加该配方，请联系总平台添加"){
                this.$message({
                  type: "warning",
                  message: "总平台未添加该配方，请联系总平台添加",
                });
                this.isShowData = false;
              }else{
                this.isShowData = true;
                let list = res.data.info.goodsToBatchList;
                list.forEach((item) => {
                  item.list = this.batchList;
                });
                // this.goodBatch.price = res.data.info.price;
                // this.goodBatch.cost = res.data.info.cost;
                this.goodBatch.goodsToBatchList = list;
                this.$set(this.goodBatch, "price", res.data.info.price);
                this.$set(this.goodBatch, "cost", res.data.info.cost);
              }
            }
          });
      }
    },

    //编辑配方
    editFormula(data) {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      homeApi
        .getGoodsBatchInfo(
          data.goodsId,
          data.sizeId,
          data.sugarId,
          data.temperatureId,
          shopId
        )
        .then((res) => {
          if (res.code == 20000) {
            this.goodBatch = res.data.info;
            this.goodBatch.goodsToBatchList.forEach((item) => {
              item.list = this.batchList;
            });
            this.isShowData = true;
            this.isShows10 = true;
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
        var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
        homeApi
          .removeGoodBatch(
            data.goodsId,
            shopId,
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
    // saveGoodBatch(goodBatch, batchRules) {
    //   var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
    //   this.$refs[goodBatch].validate((valid) => {
    //     if (valid) {
    //       this.$refs[batchRules].validate((validBatch) => {
    //         if (validBatch) {
    //           if (this.goodBatch.id != "") {
    //             this.goodBatch.shopId = shopId;
    //             homeApi.updateCostCards(this.goodBatch).then((res) => {
    //               if (res.code == 20000) {
    //                 this.$message({
    //                   type: "success",
    //                   message: "修改成功",
    //                 });
    //                 this.isShows10 = false;

    //                 this.goodBatch = {
    //                   id: "",
    //                   goodsId: "",
    //                   goodsToBatchList: [{ batchId: "", useNumber: "" }],
    //                 };

    //                 this.$refs[goodBatch].clearValidate();
    //                 this.$refs[validBatch].clearValidate();
    //                 this.getGoodsBatchs();
    //               }
    //             });
    //           } else {
    //             this.goodBatch.shopId = shopId;
    //             homeApi.addCostCards(this.goodBatch).then((res) => {
    //               if (res.code == 20000) {
    //                 this.$message({
    //                   type: "success",
    //                   message: "添加成功",
    //                 });
    //                 this.isShows10 = false;
    //                 this.getGoodsBatchs();

    //                 this.goodBatch = {
    //                   id: "",
    //                   goodsId: "",
    //                   goodsToBatchList: [{ batchId: "", useNumber: "" }],
    //                 };

    //                 this.$refs[goodBatch].clearValidate();
    //                 this.$refs[validBatch].clearValidate();
    //               }
    //             });
    //           }
    //         } else {
    //           return false;
    //         }
    //       });
    //     } else {
    //       return false;
    //     }
    //   });
    // },
    saveGoodBatch(goodBatch) {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      this.$refs[goodBatch].validate((valid) => {
        if (valid) {
          if (this.goodBatch.id != "") {
            this.goodBatch.shopId = shopId;
            homeApi.updateCostCards(this.goodBatch).then((res) => {
              if (res.code == 20000) {
                this.$message({
                  type: "success",
                  message: "修改成功",
                });
                this.isShows10 = false;

                this.goodBatch = {
                  id: "",
                  goodsId: "",
                  goodsToBatchList: [{ batchId: "", useNumber: "" }],
                };

                this.$refs[goodBatch].clearValidate();

                this.getGoodsBatchs();
              }
            });
          } else {
            this.goodBatch.shopId = shopId;
            homeApi.addCostCards(this.goodBatch).then((res) => {
              if (res.code == 20000) {
                this.$message({
                  type: "success",
                  message: "添加成功",
                });
                this.isShows10 = false;
                this.getGoodsBatchs();

                this.goodBatch = {
                  id: "",
                  goodsId: "",
                  goodsToBatchList: [{ batchId: "", useNumber: "" }],
                };

                this.$refs[goodBatch].clearValidate();
              }
            });
          }
        } else {
          return false;
        }
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
    // onClose10(goodBatch, batchRules) {
    //   this.isShows10 = false;
    //   this.goodBatch = {
    //     id: "",
    //     goodsId: "",
    //     goodsToBatchList: [{ batchId: "", useNumber: "" }],
    //   };
    // },
    onClose10(goodBatch) {
      this.isShows10 = false;
      this.goodBatch = {
        id: "",
        goodsId: "",
        goodsToBatchList: [{ batchId: "", useNumber: "" }],
      };
    },
    // 产品配方列表
    getGoodsBatchs(page = 1) {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      this.page = page;

      homeApi
        .getGoodBatchList(this.page, this.limit, shopId, this.keyword)
        .then((res) => {
          if (res.code == 20000) {
            this.productList = res.data.rows;
            this.total10 = res.data.total;
          }
        });
    },
    //新建配方
    productAdd() {
      this.isShows10 = true;
    },
    selectBatch(e) {
      this.batchInfo.outlet = this.allBatchs[e].outlet;
    },
    selectGoods(e) {
      homeApi.getGoodsById(e).then((res) => {
        if (res.code == 20000) {
          this.goodsDetails = res.data.info;
          this.goodsDetails.goodsId = e;
          this.goodsDetails.id = null;
          this.goodsDetails.cateId = null;
        }
      });
    },
    resetData() {
      this.keyword = "";
      if (this.curId == 0) {
        this.getcatePageList();
      } else if (this.curId == 4) {
        this.getGoodsList();
      } else if (this.curId == 6) {
        this.getParamList();
      } else if (this.curId == 8) {
        this.getBatchList();
      } else if(this.curId == 10){
        this.getBtachAllList();
        this.getAllGoods();
        this.getShopGoodsList();
        this.getShopParams();
        this.getGoodsBatchs();
      }else{
      this.getEquipmentShopList();
    }
    },
    getList() {
      if (this.curId == 0) {
        this.getcatePageList();
      } else if (this.curId == 4) {
        this.getGoodsList();
      } else if (this.curId == 6) {
        this.getParamList();
      } else if (this.curId == 8) {
        this.getBatchList();
      } else if(this.curId == 10){
        this.getBtachAllList();
        this.getAllGoods();
        this.getShopGoodsList();
        this.getShopParams();
        this.getGoodsBatchs();
      }else{
      this.getEquipmentShopList();
    }
    },
    changeGround(e) {
      this.isGroundingFlag = e;
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

    changeCurId(i) {
      this.page = 1;
      this.curId = i;
      if (i == 0) {
        this.getAllCates();
        this.getcatePageList();
      } else if (i == 4) {
        this.getAllGoods();
        this.getGoodsList();
        this.getCates();
      } else if (i == 6) {
        this.getParamList();
      } else if (i == 8) {
        this.getBatchList();
      } else if(i == 10){
        this.getBtachAllList();
        this.getAllGoods();
        this.getShopGoodsList();
        this.getShopParams();
        this.getGoodsBatchs();
      }else{
      this.getEquipmentShopList();
    }
    },
    getShopParams() {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      homeApi.getShopParams(shopId).then((res) => {
        if (res.code == 20000) {
          let list = res.data.paramList;
          if (list.length > 0) {
            this.cupSizes = list.filter((item) => item.type == 1);
            this.sugars = list.filter((item) => item.type == 2);
            this.temperatures = list.filter((item) => item.type == 3);
          }
        }
      });
    },
    // 添加
    addCate(type) {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      this.cateInfo.shopId = shopId;
      let access_token = localStorage.getItem("access_token");
      let token_type = localStorage.getItem("token_type");
      let expires_in = localStorage.getItem("expires_in");
      let refresh_token = localStorage.getItem("refresh_token");
      this.cateInfo.accessToken = access_token;
      this.cateInfo.tokenType = token_type;
      this.cateInfo.expiresIn = expires_in;
      this.cateInfo.refreshToken = refresh_token;
      if (type == 0) {
        homeApi
          .insertData0(this.cateInfo)
          .then((response) => {
            this.isShows0 = false;
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.getcatePageList();
          })
          .catch((response) => {
            this.$message({
              type: "error",
              message: "新增失败",
            });
          });
      } else if (type == 1) {
        homeApi
          .createCategory(this.cateInfo)
          .then((response) => {
            this.isShows0 = false;
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.getcatePageList();
          })
          .catch((response) => {
            this.$message({
              type: "error",
              message: "新增失败",
            });
          });
      } else {
      }
    },

    // 修改信息
    updateCate(type) {
      if (type == 0) {
        homeApi
          .updateData0(this.cateInfo.id, this.cateInfo)
          .then((response) => {
            this.isShows0 = false;
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.getcatePageList();
          })
          .catch((response) => {
            this.$message({
              type: "error",
              message: "修改失败",
            });
          });
      } else if (type == 1) {
        var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
        let access_token = localStorage.getItem("access_token");
        let token_type = localStorage.getItem("token_type");
        let expires_in = localStorage.getItem("expires_in");
        let refresh_token = localStorage.getItem("refresh_token");
        this.cateInfo.shopId = shopId;
        this.cateInfo.accessToken = access_token;
        this.cateInfo.tokenType = token_type;
        this.cateInfo.expiresIn = expires_in;
        this.cateInfo.refreshToken = refresh_token;
        homeApi
          .updateCategory(this.cateInfo)
          .then((response) => {
            this.isShows0 = false;
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.getcatePageList();
          })
          .catch((response) => {
            this.$message({
              type: "error",
              message: "修改失败",
            });
          });
      } else {
      }
    },

    // 删除
    removeCate(data) {
      
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let type = JSON.parse(localStorage.getItem("adminInfo")).type;
  
        if (type.includes(1)) {
          let access_token = localStorage.getItem("access_token");
          let token_type = localStorage.getItem("token_type");
          let expires_in = localStorage.getItem("expires_in");
          let refresh_token = localStorage.getItem("refresh_token");
          homeApi
            .deleteEleCate(
              data.eleId,
              access_token,
              token_type,
              expires_in,
              refresh_token
            )
            .then((response) => {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              // 回到列表页面
              this.getcatePageList();
            });
          
        } else if (type.includes(0)) {
          homeApi.deleteCate(data.id).then((response) => {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            // 回到列表页面
            this.getcatePageList();
          });
        } else {
        }
      });
    },
    getShopGoodsList() {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      homeApi.getShopGoodsList(shopId).then((res) => {
        if (res.code == 20000) {
          this.shopGoodsList = res.data.list;
        }
      });
    },
    getAllGoods() {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      homeApi.getAllGoods(shopId).then((res) => {
        if (res.code == 20000) {
          this.goodsList = res.data.list;
        }
      });
    },

    getAllCates() {
      homeApi.getAllCates().then((res) => {
        if (res.code == 20000) {
          this.cateAllList = res.data.list;
        }
      });
    },
    getCates() {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      homeApi.getCates(shopId).then((res) => {
        if (res.code == 20000) {
          this.cateList = res.data.list;
          this.equipmentList = res.data.equipmentList;
        }
      });
    },
    saveOrUpdataParam0(cateInfo) {
      let type = JSON.parse(localStorage.getItem("adminInfo")).type;
      // 判断添加还是修改
      if (!this.cateInfo.id) {
        this.$refs[cateInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[cateInfo].clearValidate();
            this.addCate(type);
          } else {
            return false;
          }
        });
      } else {
        this.$refs[cateInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.updateCate(type);
          } else {
            return false;
          }
        });
      }
    },

    // 添加
    addGoodsInfo(type) {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      this.goodsDetails.shopId = shopId;
      this.goodsDetails.isGrounding = this.isGroundingFlag ? 1 : 2;

      let access_token = localStorage.getItem("access_token");
      let token_type = localStorage.getItem("token_type");
      let expires_in = localStorage.getItem("expires_in");
      let refresh_token = localStorage.getItem("refresh_token");
      this.goodsDetails.accessToken = access_token;
      this.goodsDetails.tokenType = token_type;
      this.goodsDetails.expiresIn = expires_in;
      this.goodsDetails.refreshToken = refresh_token;
      this.goodsDetails.imageHash = refresh_token;

      if (type == 0) {
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
      } else if (type == 1) {

        homeApi
          .createGoods(this.goodsDetails)
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
              message:
                "为了提高用户体验，请上传5M以内，分辨率不小于450×450的正方形图片。",
            });
          });
      } else {
      }
    },
    // 修改信息
    updateGoodsInfo(type) {
      this.goodsDetails.isGrounding = this.isGroundingFlag ? 1 : 2;
      let access_token = localStorage.getItem("access_token");
      let token_type = localStorage.getItem("token_type");
      let expires_in = localStorage.getItem("expires_in");
      let refresh_token = localStorage.getItem("refresh_token");
      this.goodsDetails.accessToken = access_token;
      this.goodsDetails.tokenType = token_type;
      this.goodsDetails.expiresIn = expires_in;
      this.goodsDetails.refreshToken = refresh_token;
      if (type == "0") {
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
      } else if (type.includes("1")) {
        homeApi
          .updateGoods(this.goodsDetails)
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
              message:
                "为了提高用户体验，请上传5M以内，分辨率不小于450×450的正方形图片。",
            });
          });
      } else {
      }
    },
    // 删除
    removeGoodsInfo(data) {
      let access_token = localStorage.getItem("access_token");
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        if (!access_token) {
          homeApi.deleteGoods(data.id).then((response) => {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            // 回到列表页面
            this.getGoodsList();
          });
        } else if (access_token && access_token != null) {
          let access_token = localStorage.getItem("access_token");
          let token_type = localStorage.getItem("token_type");
          let expires_in = localStorage.getItem("expires_in");
          let refresh_token = localStorage.getItem("refresh_token");
          homeApi
            .deleteEleGoods(
              data.id,
              access_token,
              token_type,
              expires_in,
              refresh_token
            )
            .then((response) => {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              // 回到列表页面
              this.getGoodsList();
            });
        } else {
        }
      });
    },
    saveOrUpdataParam4(goodsDetails) {
      let type = JSON.parse(localStorage.getItem("adminInfo")).type;
      // 判断添加还是修改
      if (!this.goodsDetails.id) {
        this.$refs[goodsDetails].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[goodsDetails].clearValidate();
            this.addGoodsInfo(type);
          } else {
            return false;
          }
        });
      } else {
        this.$refs[goodsDetails].validate((valid) => {
          if (valid) {
            // 修改
            this.updateGoodsInfo(type);
          } else {
            return false;
          }
        });
      }
    },

    // 添加
    addBatchInfo() {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      this.batchInfo.shopId = shopId;
      homeApi
        .insertData8(this.batchInfo)
        .then((response) => {
          if (response.message == "该配料已添加") {
            this.$message.error(response.message);
          } else {
            this.isShows8 = false;
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
            this.isShows8 = false;
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
    removeBatchInfo(data) {
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
    formulaEdit8(id) {
      this.isShows8 = true;
      homeApi.getBatchInfo(id).then((res) => {
        if (res.code == 20000) {
          this.batchInfo = res.data.info;
        }
      });
    },
    saveOrUpdataParam8(batchInfo) {
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
    // 配料管理列表
    getBatchList(page = 1) {
      this.page = page;
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      homeApi.getBatchList(this.page, this.limit, shopId).then((res) => {
        if (res.code == 20000) {
          this.batchList = res.data.rows;
          this.allBatchs = res.data.allBatchs;
          this.total8 = res.data.total;
        }
      });
    },

    // 设备管理列表 
    getEquipmentShopList(page = 1){
      this.page = page;
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      homeApi.getEquipmentShopList(this.page, this.limit, shopId,this.keyword).then((res) => {
        if (res.code == 20000) {
          this.equipmentShopList = res.data.rows;
          this.total12 = res.data.total;
        }
      });
    },

    // 分类列表
    getcatePageList(page = 1) {
      this.page = page;
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      homeApi
        .getCateList(this.page, this.limit, shopId, this.keyword)
        .then((res) => {
          if (res.code == 20000) {
            this.catePageList = res.data.rows;
            this.total0 = res.data.total;
          }
        });
    },
    // 产品分类列表
    getGoodsList(page = 1) {
      this.page = page;
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      homeApi
        .getGoodsList(this.page, this.limit, shopId, this.keyword)
        .then((res) => {
          if (res.code == 20000) {
            this.goodsDetailsList = res.data.rows;
            this.total4 = res.data.total;
          }
        });
    },
    formulaEdit6(id) {
      this.isShows6 = true;
      homeApi.getParamInfo(id).then((res) => {
        if (res.code == 20000) {
          this.paramInfo = res.data.info;
        }
      });
    },
    updateEquipment() {
      homeApi
        .updateData12(this.equipmentInfo.id, this.equipmentInfo)
        .then((response) => {
          this.isShows12 = false;
          this.$message({
            type: "success",
            message: "修改成功",
          });
          this.getEquipmentShopList();
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "修改失败",
          });
        });
    },
    // 修改信息
    updateParam() {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      this.paramInfo.shopId = shopId;
      homeApi
        .updateData6(this.paramInfo.id, this.paramInfo)
        .then((response) => {
          this.isShows6 = false;
          this.$message({
            type: "success",
            message: "修改成功",
          });
          this.getParamList();
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "修改失败",
          });
        });
    },

    saveOrUpdataParam6(paramInfo) {
      this.$refs[paramInfo].validate((valid) => {
        if (valid) {
          // 修改
          this.updateParam();
        } else {
          return false;
        }
      });
    },
    saveParam12(equipmentInfo) {
      this.$refs[equipmentInfo].validate((valid) => {
        if (valid) {
          // 修改
          this.updateEquipment();
        } else {
          return false;
        }
      });
    },
    // 规格列表
    getParamList(page = 1) {
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      this.page = page;
      homeApi
        .getParamList(this.page, this.limit, shopId, this.keyword)
        .then((res) => {
          if (res.code == 20000) {
            this.paramList = res.data.rows;
            this.total6 = res.data.total;
          }
        });
    },
    // 返回
    goBack: function () {
      this.$router.go(-1);
    },

    // 新建分类
    formulaAdd0() {
      this.isShows0 = true;
      this.cateInfo = {};
    },
    // 新建商品
    formulaAdd4() {
      this.isShows4 = true;
      this.goodsDetails = {};
    },
    formulaAdd8() {
      this.isShows8 = true;
      this.batchInfo = {};
    },
    formulaEdit0(id) {
      this.isShows0 = true;
      homeApi.getCateInfo(id).then((res) => {
        if (res.code == 20000) {
          this.cateInfo = res.data.info;
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
    formulaEdit12(id) {
      this.isShows12 = true;
      homeApi.getEquipmentInfo(id).then((res) => {
        if (res.code == 20000) {
          this.equipmentInfo = res.data.info;
        }
      });
    },

    onCloseds0(cateInfo) {
      this.$refs[cateInfo].clearValidate();
      this.isShows0 = false;
    },

    onCloseds4(goodsDetails) {
      this.$refs[goodsDetails].clearValidate();
      this.isShows4 = false;
    },
    onCloseds6(paramInfo) {
      this.$refs[paramInfo].clearValidate();
      this.isShows6 = false;
    },
    onCloseds8(batchInfo) {
      this.$refs[batchInfo].clearValidate();
      this.isShows8 = false;
    },
    onCloseds12(equipmentInfo) {
      this.$refs[equipmentInfo].clearValidate();
      this.isShows12 = false;
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