export const DurationStay = {
    data(){
      return {
        currentTime:"",
        DurationOfStay:1*60*1000,   //自定义的无操作时长5分钟
        intervalTime:0
      }
    },
    mounted(){
      this.currentTime = new Date().getTime();
      this.checkouttime();
      document.addEventListener("keydown",this.resetStartTime);
      document.addEventListener('touchstart',this.resetStartTime);
    },
  
    beforeDestroy(){
     //离开页面要销毁事件，否则会影响跳转的下个页面
      document.removeEventListener("keydown",this.resetStartTime);
      document.removeEventListener("touchstart",this.resetStartTime);
      if(this.intervalTime){
        clearInterval(this.intervalTime);
      }
    },
    methods:{
      resetStartTime(){
        this.currentTime = new Date().getTime();
      },
  
      checkouttime(){
        this.intervalTime= setInterval(()=>{
          let nowtime = new Date().getTime();
          if(nowtime - this.currentTime > this.DurationOfStay){
          	//特别注意：在这里要用自己独有的弹窗，不要跟项目的其他弹窗一直，否则会影响页面中别的弹窗弹起的bug
            this.$message({
                title: "温馨提示",
                message: "页面停留太长，点击“刷新一下”获取最新价格和库存。",
                type: 'success',
                confirmButtonText: "刷新一下",
                closeOnClickModal:false,
            }).then(()=>{
            	//这里可以自由发挥，根据需求去做
                this.init();
            });
            return false;
          }
        },30000)
      }
    }
  }