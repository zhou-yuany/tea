<template>
  <div class="app-container">
    <el-button type="primary" @click="clickReturn()">返回</el-button>
    <!-- 图片预览 -->
 <el-dialog title="图片预览" :visible.sync="previewVisible" width="50%">
    <img :src="previewPath" alt="" style="width:100%;height:100%" />
 </el-dialog>

    <el-form
      :model="pictureInfo"
      :rules="rules"
      ref="pictureInfo"
      label-width="120px"
    >
      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        基本信息
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>

      <div style="display: flex">
        <el-form-item
          label="模板名称："
          style="width: 78%"
          label-width="100px"
          prop="name"
        >
          <span>{{ pictureInfo.name }}</span>
        </el-form-item>

        <el-form-item
          label="模板颜色："
          style="width: 78%"
          label-width="100px"
          prop="indexColor"
        >
          <el-select
            :disabled="true"
            v-model="pictureInfo.colorName"
            placeholder="请选择"
          >
            <el-option
              v-for="typePro in colorNameList"
              :key="typePro.value"
              :label="typePro.label"
              :value="typePro.value"
            >
              <span style="display: flex; align-items: center">
                <span
                  :style="{ backgroundColor: typePro.value }"
                  style="
                    width: 15px;
                    height: 15px;
                    margin-right: 5px;
                    border-radius: 50%;
                  "
                ></span>
                {{ typePro.label }}
              </span>
            </el-option>
          </el-select>
        </el-form-item>
      </div>

      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        登录
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>
      <div style="display: flex">
        <el-form-item
          label="logo"
          prop="logo"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleLogoSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.logo"
              :src="'/tea-management/image/' + pictureInfo.logo"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内300*300px的图片
            </div>
          </el-upload>
        </el-form-item>
        
      </div>

      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        首页
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>

      <div style="display: flex">
        <el-form-item label="轮播图" label-width="100px" prop="pics">
          <div class="up-picture-box">
            <el-upload
              :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
              list-type="picture-card"
              :before-upload="handlePictureCardPreview"
              :on-preview="handlePreview"
              :on-success="handleTemplateSuccess"
              :on-remove="handleTemplateRemove"
              :file-list="bannerList"
            >
              <div class="el-upload__tip" slot="tip" style="font-weight: bold;color: red;">
                JPG或PNG格式！请上传大小不超过2M，大于750*750px图片
              </div>
            </el-upload>
          </div>
        </el-form-item>
      </div>
      <div style="display: flex">
        <el-form-item
          label="到店自取"
          prop="order1"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleDiancanSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.order1"
              :src="'/tea-management/image/' + pictureInfo.order1"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内150*150px的图片
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item
          label="外卖点单"
          prop="order2"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleDiancan2Success"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.order2"
              :src="'/tea-management/image/' + pictureInfo.order2"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内150*150px的图片
            </div>
          </el-upload>
        </el-form-item>
      </div>
       <div style="display: flex">
        <el-form-item
          label="广告图上"
          prop="ad"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleAdSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.ad"
              :src="'/tea-management/image/' + pictureInfo.ad"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内686*200px的图片
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item
          label="广告图下"
          prop="ad2"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleAd2Success"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.ad2"
              :src="'/tea-management/image/' + pictureInfo.ad2"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内686*200px的图片
            </div>
          </el-upload>
        </el-form-item>
      </div>
        <el-form-item
          label="优惠券广告"
          prop="adBg"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleAdBgSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.adBg"
              :src="'/tea-management/image/' + pictureInfo.adBg"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内635*663px的图片
            </div>
          </el-upload>
        </el-form-item>
        
      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        点餐
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>
      <div style="display: flex">
        <el-form-item
          label="外卖"
          prop="switch1"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleSwitchSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.switch1"
              :src="'/tea-management/image/' + pictureInfo.switch1"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内160*50px的图片
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item
          label="到店"
          prop="switch2"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleSwitch2Success"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.switch2"
              :src="'/tea-management/image/' + pictureInfo.switch2"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内的160*50px图片
            </div>
          </el-upload>
        </el-form-item>
      </div>
      <div style="display: flex">
        <el-form-item
          label="活动图"
          prop="activity"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleActivitySuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.activity"
              :src="'/tea-management/image/' + pictureInfo.activity"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内750*200px的图片
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item
          label="画报"
          prop="canvas"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleCanvasSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.canvas"
              :src="'/tea-management/image/' + pictureInfo.canvas"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内492*455px的图片
            </div>
          </el-upload>
        </el-form-item>
      </div>
      <el-form-item
          label="购物车"
          prop="cart"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleCartSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.cart"
              :src="'/tea-management/image/' + pictureInfo.cart"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内80*80px的图片
            </div>
          </el-upload>
        </el-form-item>


      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        支付结果
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>
      <div style="display: flex">
        <el-form-item
          label="成功"
          prop="success"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleSuccessSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.success"
              :src="'/tea-management/image/' + pictureInfo.success"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内136*136px的图片
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item
          label="失败"
          prop="warning"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleWarningSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.warning"
              :src="'/tea-management/image/' + pictureInfo.warning"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内136*136px的图片
            </div>
          </el-upload>
        </el-form-item>
      </div>

      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        我的/个人资料
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>
      <div style="display: flex">
        <el-form-item
          label="头像"
          prop="header"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleHeaderSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.header"
              :src="'/tea-management/image/' + pictureInfo.header"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内184*184px的图片
            </div>
          </el-upload>
        </el-form-item>
        
      </div>


      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        地址
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>
      <div style="display: flex">
        <el-form-item
          label="编辑"
          prop="edit"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleEditSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.edit"
              :src="'/tea-management/image/' + pictureInfo.edit"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内36*36px的图片
            </div>
          </el-upload>
        </el-form-item>
        
      </div>

      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        确认订单
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>
      <div style="display: flex">
        <el-form-item
          label="自提"
          prop="selfPickup"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleSelfPickupSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.selfPickup"
              :src="'/tea-management/image/' + pictureInfo.selfPickup"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内433*148px的图片
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item
          label="外卖"
          prop="takeaway"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleTakeawaySuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.takeaway"
              :src="'/tea-management/image/' + pictureInfo.takeaway"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内433*148px的图片
            </div>
          </el-upload>
        </el-form-item>
        
      </div>

      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        订单详情
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>
      <div style="display: flex">
        <el-form-item
          label="勾选"
          prop="checked"
          style="justify-content: flex-start"
        >
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/templatePicture/uploadPics/'+pictureInfo.colorName"
            :show-file-list="false"
            :on-success="handleCheckSuccess"
            :before-upload="beforeDiancanUpload"
          >
            <img
              v-if="pictureInfo.checked"
              :src="'/tea-management/image/' + pictureInfo.checked"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              请上传2M以内30*30px的图片
            </div>
          </el-upload>
        </el-form-item>
        
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeShop('pictureInfo')">取 消</el-button>
      <el-button type="primary" @click="saveOrUpdate('pictureInfo')"
        >确 定</el-button
      >
    </div>
  </div>
</template>
<script>
import templateApi from "@/api/admins/picture";
export default {
  data() {
    const validateName = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的模板名称"));
      }
    };

    return {
      list: null, // 查询后接口返回集合
      prePage: "",
      errorImg: "../../static/errorImg.png",
      picList: [],
      pictureInfo: {},
      listLoading: false,
      dialogShop: false,
      previewPath: "",
      previewVisible: false,
      templateId: "",
      bannerList: [],
      divideList: [
        {
          value: 1,
          label: "是",
        },
        {
          value: 2,
          label: "否",
        },
      ],
      colorNameList: [
        {
          value: "red",
          label: "红",
        },
        {
          value: "orange",
          label: "橙",
        },
        {
          value: "yellow",
          label: "黄",
        },
        {
          value: "green",
          label: "绿",
        },
        {
          value: "cyan",
          label: "青",
        },
        {
          value: "blue",
          label: "蓝",
        },
        {
          value: "purple",
          label: "紫",
        },
      ],
      rules: {
        logo: [
          { required: true, message: "请上传logo", trigger: "blur" },
        ],
        order1: [
          { required: true, message: "请上传到店自取图片", trigger: "blur" },
        ],
        order2: [
          { required: true, message: "请上传外卖点单图片", trigger: "blur" },
        ],
        ad: [
          { required: true, message: "请上传广告图上", trigger: "blur" },
        ],
        ad2: [
          { required: true, message: "请上传广告图下", trigger: "blur" },
        ],
        adBg: [
          { required: true, message: "请上传优惠券广告图片", trigger: "blur" },
        ],
        switch1: [
          { required: true, message: "请上传外卖图片", trigger: "blur" },
        ],
        switch2: [
          { required: true, message: "请上传到店图片", trigger: "blur" },
        ],
        activity: [
          { required: true, message: "请上传活动图", trigger: "blur" },
        ],
        canvas: [
          { required: true, message: "请上传画报图片", trigger: "blur" },
        ],
        cart: [
          { required: true, message: "请上传购物车图片", trigger: "blur" },
        ],
        success: [
          { required: true, message: "请上传成功图片", trigger: "blur" },
        ],
        warning: [
          { required: true, message: "请上传失败图片", trigger: "blur" },
        ],
        header: [
          { required: true, message: "请上传头像图片", trigger: "blur" },
        ],
        edit: [
          { required: true, message: "请上传编辑图片", trigger: "blur" },
        ],
        selfPickup: [
          { required: true, message: "请上传自提图片", trigger: "blur" },
        ],
        takeaway: [
          { required: true, message: "请上传外卖图片", trigger: "blur" },
        ],
        checked: [
          { required: true, message: "请上传勾选图片", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    if (this.$route.params.id && this.$route.params.id > 0) {
      this.templateId = this.$route.params.id;
      this.getInfo(this.$route.params.id);
    }
    if (this.$route.query.page) {
      this.prePage = this.$route.query.page;
    }
  },
  methods: {
    clickReturn() {
      this.$router.push({
        path: "/template/table",
        query: { page: this.prePage },
      });
    },

    // 上传轮播图成功调用方法
    handleTemplateSuccess(res, file) {
      this.pictureInfo.banner =
        this.pictureInfo.banner == undefined
          ? res.data.url
          : this.pictureInfo.banner + "," + res.data.url;
    },
    handleTemplateRemove(res, fileList) {
      if (fileList.length > 0) {
        this.pictureInfo.banner = fileList
          .map((item) =>
            item.response
              ? item.response.data.url
              : item.url.replace("/tea-management/image/", "")
          )
          .join(",");
      } else {
        this.pictureInfo.banner = null;
      }
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
      return (isJPG || isPNG) && isLt2M;
    },
    handlePreview(file){
      this.previewPath = file.url
      this.previewVisible = true
    },

    handleDiancanSuccess(res, file) {
      this.$set(this.pictureInfo, "order1", res.data.url);
    },
    handleDiancan2Success(res, file) {
      this.$set(this.pictureInfo, "order2", res.data.url);
    },
    handleAdSuccess(res, file) {
      this.$set(this.pictureInfo, "ad", res.data.url);
    },
    handleAd2Success(res, file) {
      this.$set(this.pictureInfo, "ad2", res.data.url);
    },
    handleNavigationSuccess(res, file) {
      this.$set(this.pictureInfo, "navigation", res.data.url);
    },
    handleAdBgSuccess(res, file) {
      this.$set(this.pictureInfo, "adBg", res.data.url);
    },
    handleSwitchSuccess(res, file) {
      this.$set(this.pictureInfo, "switch1", res.data.url);
    },
    handleSwitch2Success(res, file) {
      this.$set(this.pictureInfo, "switch2", res.data.url);
    },
    handleActivitySuccess(res, file) {
      this.$set(this.pictureInfo, "activity", res.data.url);
    },
    handleCanvasSuccess(res, file) {
      this.$set(this.pictureInfo, "canvas", res.data.url);
    },
    handleHeaderSuccess(res, file) {
      this.$set(this.pictureInfo, "header", res.data.url);
    },
    handleSuccessSuccess(res, file) {
      this.$set(this.pictureInfo, "success", res.data.url);
    },
    handleWarningSuccess(res, file) {
      this.$set(this.pictureInfo, "warning", res.data.url);
    },
    handleLogoSuccess(res, file) {
      this.$set(this.pictureInfo, "logo", res.data.url);
    },
    handleEditSuccess(res, file) {
      this.$set(this.pictureInfo, "edit", res.data.url);
    },
    handleCartSuccess(res, file) {
      this.$set(this.pictureInfo, "cart", res.data.url);
    },
    handleCheckSuccess(res, file) {
      this.$set(this.pictureInfo, "checked", res.data.url);
    },
    handleSelfPickupSuccess(res, file) {
      this.$set(this.pictureInfo, "selfPickup", res.data.url);
    },
    handleSelfPickupSuccess(res, file) {
      this.$set(this.pictureInfo, "selfPickup", res.data.url);
    },
    handleTakeawaySuccess(res, file) {
      this.$set(this.pictureInfo, "takeaway", res.data.url);
    },
    beforeDiancanUpload(file) {
      //添加文件类型的限制
      let types = ["image/jpeg", "image/jpg", "image/png"];
      const isImage = types.includes(file.type);
      const isLt2M = file.size / 1024 / 1024 <= 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
        return false;
      }
      if (!isImage) {
        this.$message.error("上传图片只能是 JPG、JPEG、PNG 格式!");
        return false;
      }
    },
    // 添加
    addData() {
      if(this.pictureInfo.banner == ''){
        this.$message({
            type: "warnning",
            message: "请上传轮播图",
          });
      }

      templateApi
        .insertData(this.pictureInfo)
        .then((response) => {
          this.$message({
            type: "success",
            message: "新增成功",
          });
          this.$router.push({
            path: "/template/table",
            query: { page: this.prePage },
          });
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "新增失败",
          });
        });
    },

    getInfo(templateId) {
      templateApi.getInfo(templateId).then((response) => {
        this.pictureInfo = response.data.info;
        if (response.data.info.banner != null) {
          let bannerList =
            response.data.info.banner.split(",");
          let newList = [];
          bannerList.forEach((item, index) => {
            let obj = {};
            obj.url = "/tea-management/image/" + item;
            obj.name = index;
            newList.push(obj);
          });
          this.bannerList = newList;
        }
      });
    },

    // 修改信息
    updateData() {
      if(this.pictureInfo.banner == ''){
        this.$message({
            type: "warnning",
            message: "请上传轮播图",
          });
      }

      templateApi
        .updateData(this.pictureInfo)
        .then((response) => {
          this.$message({
            type: "success",
            message: "修改成功",
          });
          this.$router.push({
            path: "/template/table",
            query: { page: this.prePage },
          });
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "修改失败",
          });
        });
    },

    closeShop(pictureInfo) {
      this.$refs[pictureInfo].clearValidate();
      this.$router.push({
        path: "/template/table",
        query: { page: this.prePage },
      });
    },

    // 保存并下一步
    saveOrUpdate(pictureInfo) {
      // 判断添加还是修改
      if (!this.pictureInfo.id) {
        this.$refs[pictureInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[pictureInfo].clearValidate();
            this.addData();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[pictureInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.updateData();
            this.$refs[pictureInfo].clearValidate();
          } else {
            return false;
          }
        });
      }
    },
  },
};
</script>
<style>
.el-icon-my-clear:before {
  content: "替";
  font-size: 16px;
  visibility: hidden;
}
.el-upload {
  text-align: left;
}
</style>