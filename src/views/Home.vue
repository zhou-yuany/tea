<template>
  <div class="home_box">
      <img
        v-if="divideAccounts == 2"
        src="../../static/ico1.png"
        alt=""
        @click="goXq(0)"
      />
      <img src="../../static/ico3.png" alt="" @click="goXq(2)" />
      <!-- <img src="/static/ico5.png" alt="" @click="goXq(4)"> -->
      <img src="../../static/ico12.png" alt="" @click="goXq(5)">
      <img src="../../static/ico11.png" alt="" @click="goSelect()" />
      

      <img src="../../static/ico2.png" alt="" @click="goXq(1)" />
      <img src="../../static/ico4.png" alt="" @click="goXq(3)" />

      <img src="../../static/ico13.png" alt="" @click="goSelect2()" />





    <!-- 点击清洗弹出下拉框 -->
    <div class="grayc" v-if="isShow">
          <div class="white_bg whitek">
            <div class="tc_title">
              <p>清洗</p>
              <img src="/static/x.png" alt="" @click="onGuanbi()" />
            </div>
            <div class="hangf">
              <div class="mings">设备编号</div>
              <el-select v-model="equipmentId" placeholder="请选择" style="width: 50%">
                <el-option v-for="(item, index) in machineList" :key="index" :label="item" :value="item"></el-option>
              </el-select>
            </div>
            <div class="closebtn">
              <button type="button" class="lightblue_bg white" @click="goClear()">确定</button>
            </div>
          </div>
        </div>

         <!-- 点击排空气弹出下拉框 -->
    <div class="grayc" v-if="isShow2">
          <div class="white_bg whitek">
            <div class="tc_title">
              <p>排出空气</p>
              <img src="/static/x.png" alt="" @click="onGuanbi2()" />
            </div>
            <div class="hangf">
              <div class="mings">设备编号</div>
              <el-select v-model="equipmentId2" placeholder="请选择" style="width: 50%">
                <el-option v-for="(item, index) in machineList" :key="index" :label="item" :value="item"></el-option>
              </el-select>
            </div>
            <div class="closebtn">
              <button type="button" class="lightblue_bg white" @click="goClear2()">确定</button>
            </div>
          </div>
        </div>
  </div>
</template>

<script>
import Api from "@/api/api";
import { defineComponent } from "vue";
import homeApi from "@/api/home/index";
export default defineComponent({
  data() {
    return {
      equipmentId: '',
      equipmentId2: '',
      currenta: -1,
      isShow: false,
      isShow2: false,
      machineList: []

    };
  },
  created() {
    this.divideAccounts = localStorage.getItem("divideAccounts");
    this.getMachineList();
  },
  methods: {
    // 查询设备
    getMachineList() {
      var adminId = localStorage.getItem("adminId");
      homeApi.getMachineList(adminId).then((res) => {
        if (res.code == 20000) {
          this.machineList = res.data.machineList;
          
        }
      });
    },
    goSelect() {
      this.isShow = true;
    },
    goSelect2() {
      this.isShow2 = true;
    },
    goXq(index) {
      this.$router.push({
        path: "/Detail",
        query: { current: index },
      });
    },
    onGuanbi() {
      this.isShow = false;

    },
    // 清洗
    goClear() {
      if(this.equipmentId == ''){
         this.$message({
            type: "warning",
            message: "请选择清洗的设备编号！",
          });
          return 
      }
      var adminId = localStorage.getItem("adminId");
      homeApi.clearMachine(adminId, this.equipmentId).then((res) => {
        if (res.code == 20000) {
          this.isShow = false;
          let ttt = 20 * 1000;
          setTimeout(() => {
            // 清洗完毕
            
          }, ttt);
        }
      });
    },
    onGuanbi2() {
      this.isShow2 = false;

    },
    // 排出空去
    goClear2() {
      if(this.equipmentId2 == ''){
         this.$message({
            type: "warning",
            message: "请选择排放空气的设备编号！",
          });
          return 
      }
      var adminId = localStorage.getItem("adminId");
      homeApi.dischargeAir(adminId, this.equipmentId2).then((res) => {
        if (res.code == 20000) {
          this.isShow = false;
          // let ttt = 10 * 1000;
          // setTimeout(() => {
          //   // 清洗完毕
            
          // }, ttt);
        }
      });
    },
  },
  mounted() {},
});
</script>
  
<style scoped>
@import url("../assets/home.css");
</style>