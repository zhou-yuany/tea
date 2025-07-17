<template>
  <input v-model="defaultTimeVal" 
  :style="{'background-color':bgColor,'text-align':position,'cursor':'pointer','font-size':size}" 
  :class="'dr-date'+ids " 
  :placeholder="placeholder"  
  readonly="readonly"
  type="text" 
  @click="show"
  />
</template>


<style scoped>
input {
  border-radius: 30px;
  color: #999999;
  padding:0;
  border: none;
  font-size: 28px;
}
</style>


<script>
import "./2.1.1-jquery.min.js";
import "./time-picker.js";

export default {
  data() {
    return {
      defaultTimeVal:'',
      ids:''
    };
  },
  props: {
    height: {
      type: String,
      default: "30",
    },
    width: {
      type: String,
      default: "100",
    },
    dateType: {
      type: Number,
      default: 0,
    },
    defaultTime:{
      type:String,
      default:'',
    },
    bgColor:{
      type:String,
      default:'',
    },
    title:{
      type:String,
      default:'日期和时间',
    },
    placeholder:{
      type:String,
      default:'请选择',
    },
    position:{
      type:String,
      default:'center',
    },
    keys:{
      type:String | Number,
      default:()=>{
        return '' || 0
      },
    },
    startYear:{
      type:Number,
      default:10,
    },
    endYear:{
      type:Number,
      default:0,
    },
    dayArr:{
      type:Array,
      default:function(){
        return []
      }
    },
    size:{
      type:String,
      default:'16px',
    },
  },
  watch: {
    defaultTime:{
      handler:function(val){
        this.defaultTimeVal = val
      },
      immediate:true
    },
    keys:{
      handler:function(val){
        this.ids = val
      },
      immediate:true
    }
  },
  mounted() {
    this.show()
  },
  destroyed(){
    this.hide()
  },
  methods: {
    dataSet(t) {
      return t > 9 ? t : "0" + t;
    },
    initCss() {
      // height: this.px2rem(this.height+'px'
      $(`.dr-date${this.ids}`).css({ height: this.height+'px', width: this.width + "%" });
    },

    show() {
      let year =  new Date().getFullYear();
      let month = this.dataSet(new Date().getMonth() + 1);
      let date = this.dataSet(new Date().getDate());
      let hours = this.dataSet(new Date().getHours());
      let mins = this.dataSet(new Date().getMinutes());
      let seconds = this.dataSet(new Date().getSeconds());

      if (this.defaultTimeVal!='') { 
        let oV = this.defaultTimeVal
        let newVo = ''
        if (this.dateType == 0) { //年月日 时分秒
          newVo = oV
        } else if (this.dateType == 1) { //年月日 时分
          newVo = oV + ':' + seconds
        } else if (this.dateType == 2) { //年月日 时
          newVo = oV + ':'+ mins +':' + seconds
        } else if (this.dateType == 3) { //年月日
          newVo = oV + ' ' + hours + ':'+ mins + ':'+ seconds
        } else if (this.dateType == 4) { //年月
          newVo = oV + '-' + date + ' ' + hours + ':'+ mins + ':'+ seconds
        } else if (this.dateType == 5) { //年
          newVo = oV + '-' + month + '-' + date + ' ' + hours + ':'+ mins + ':'+ seconds
        } else if (this.dateType == 6) { //月
          newVo = year + '-' + oV + '-' +  date +' ' + hours + ':'+ mins + ':'+ seconds
        } else if (this.dateType == 7) { //日
          newVo = year + '-' + month + '-' +  oV +' 00:00:00'
        } else if (this.dateType == 8) { //时
          newVo = year + '-' + month + '-' +  date + ' '+ oV +':00:00'
        } else if (this.dateType == 9) { //分
          newVo = year + '-' + month + '-' +  date + ' '+ hours +':'+ oV+':00'
        } else if (this.dateType == 10) { //秒
          newVo = year + '-' + month + '-' +  date + ' '+ hours + mins + oV
        } else if (this.dateType == 11) { //时分秒
          newVo = year + '-' + month + '-' +  date + ' '+ oV
        } else if (this.dateType == 12) { //时分
          newVo = year + '-' + month + '-' +  date + ' '+ oV +':'+ seconds
        } else if (this.dateType == 13) { //月日时分
          let ovL = oV.replace("月","-")
          let ovL1 = ovL.replace("日","")
          newVo = year + '-' + ovL1 + ':' + seconds
          // newVo = year + '-' + oV + ':' + seconds
        }
        year = new Date(newVo).getFullYear();
        month = this.dataSet(new Date(newVo).getMonth() + 1);
        date = this.dataSet(new Date(newVo).getDate());
        hours = this.dataSet(new Date(newVo).getHours());
        mins = this.dataSet(new Date(newVo).getMinutes());
        seconds = this.dataSet(new Date(newVo).getSeconds());

        this.initCss();
      } else {
        this.initCss();
      }

      
      // 年月日 时分秒
      $.timePicker({
        ele: `.dr-date${this.ids}`,
        title: this.title,
        year: year,
        month: month,
        day: date,
        hour: hours,
        minute: mins,
        second: seconds,
        startYear: year-this.startYear,//当年年前多少年
        endYear: year + this.endYear,//当年年后多少年
        dateType: this.dateType, // 0年月日 时分秒 1年月日 时分 2年月日时 3年月日 4年月 5年 6月 7日 8时 9分 10秒 11时分秒 12时分
        dayArr:this.dayArr,
        afterThing: (d1, d2, d3, d4, d5, d6) => {
          if (this.dateType == 0) {
            this.$emit("update:defaultTime",d1 + "-" + d2 + "-" + d3 + " " + d4 + ":" + d5 + ":" + d6)
            this.$emit("getDateValue", d1 + "-" + d2 + "-" + d3 + " " + d4 + ":" + d5 + ":" + d6,this.keys);
          } else if (this.dateType == 1) {
            this.$emit("update:defaultTime",d1 + "-" + d2 + "-" + d3 + " " + d4 + ":" + d5)
            this.$emit("getDateValue",d1 + "-" + d2 + "-" + d3 + " " + d4 + ":" + d5,this.keys);
          } else if (this.dateType == 2) {
            this.$emit("update:defaultTime",d1 + "-" + d2 + "-" + d3 + " " + d4 + ":00")
            this.$emit("getDateValue",d1 + "-" + d2 + "-" + d3 + " " + d4 + ":00",this.keys);
          } else if (this.dateType == 3) {
            this.$emit("update:defaultTime",d1 + "-" + d2 + "-" + d3)
            this.$emit("getDateValue", d1 + "-" + d2 + "-" + d3,this.keys);
          } else if (this.dateType == 4) {
            this.$emit("update:defaultTime",d1 + "-" + d2)
            this.$emit("getDateValue", d1 + "-" + d2,this.keys);
          } else if (this.dateType == 5) {
            this.$emit("update:defaultTime",d1)
            this.$emit("getDateValue", d1,this.keys);
          } else if (this.dateType == 6) {
            this.$emit("update:defaultTime",d2)
            this.$emit("getDateValue", d2,this.keys);
          } else if (this.dateType == 7) {
            this.$emit("update:defaultTime",d3)
            this.$emit("getDateValue", d3,this.keys);
          } else if (this.dateType == 8) {
            this.$emit("update:defaultTime",d4)
            this.$emit("getDateValue", d4,this.keys);
          } else if (this.dateType == 9) {
            this.$emit("update:defaultTime",d5)
            this.$emit("getDateValue", d5,this.keys);
          } else if (this.dateType == 10) {
            this.$emit("update:defaultTime",d6)
            this.$emit("getDateValue", d6,this.keys);
          } else if (this.dateType == 11) {
            this.$emit("update:defaultTime",d4 + ":" + d5 + ":" + d6)
            this.$emit("getDateValue", d4 + ":" + d5 + ":" + d6,this.keys);
          } else if (this.dateType == 12) {
            this.$emit("update:defaultTime",d4 + ":" + d5)
            this.$emit("getDateValue", d4 + ":" + d5,this.keys);
          } else if (this.dateType == 13) {
            // this.$emit("update:defaultTime",d2 + "-" + d3 + " " + d4 + ":" + d5)
            // this.$emit("getDateValue", d2 + "-" + d3 + " " + d4 + ":" + d5 + ":" + d5,this.keys);
            this.$emit("update:defaultTime",d2 + "月" + d3 +  "日" +" " + d4 + ":" + d5)
            this.$emit("getDateValue", d2 + "月" + d3 +  "日" +" " + d4 + ":" + d5 + ":" + d5,this.keys);
          } 
        },
      });
    },
    
    hide(){
      $('.dr-contents').hide();
    },
  },
};
</script>
