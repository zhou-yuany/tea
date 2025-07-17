<template>
    <div class="index_box">
        <div class="itemk_index" @click="goTakeaway()"><img src="../../static/ico14.png" alt="" /></div>
        <div class="itemk_index" @click="goIndex()"><img src="../../static/ico15.png" alt="" /></div>
    </div>
</template>
  
<script>
    import Api from "@/api/api";
    import { defineComponent } from "vue";
    import homeApi from "@/api/home/index";
    // import  { DurationStay } from '../components/DurationStay'
    export default defineComponent({
        // mixins:[ DurationStay ],
        data() {
            return {
                
  
            };
        },
        created() {
            
        },
        methods: {
            goIndex() {
                this.$router.push({
                    path: "/Home",
                });
            },
            goTakeaway(){
                this.$router.push({
                    path: "/Pickupcode",
                });
            }
        },
        mounted() {},
    });
</script>
    
<style scoped>
    @import url("../assets/index.css");
</style>