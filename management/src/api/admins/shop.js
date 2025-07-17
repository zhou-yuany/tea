import request from '@/utils/request'
export default{
    
    // 列表(条件查询分页)
    getList(page,limit,shopQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/shop/getList/${page}/${limit}`,  // 飘号
            method: 'post',
            data: shopQuery
          })
    },
    // 根据id查询
    getTemplateList(){
        return request({
            url: `/template/getTemplateList`,
            method: 'get'
        })
    },

    addPrice(goodBatch){
        return request({
            url: `/batchShopPrice/saveShopBatch`,
            method: 'post',
            data: goodBatch
        })
    },

    // 签约的配料
    getShopBatch(id){
        return request({
            url: `/batchShopPrice/getShopBatch/${id}`,
            method: 'get'
        })
    },
    // 所有设备
    getEquipmentList(){
        return request({
            url: `/equipmentAll/allList`,
            method: 'get'
        })
    },
    // 所有品牌
    getBrandList(){
        return request({
            url: `/brand/allList`,
            method: 'get'
        })
    },

    // 所有商品
    getGoodsList(){
        return request({
            url: `/goods/allList`,
            method: 'get'
        })
    },


    // 添加
    insertData(obj){
        return request({
            // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
            url: `/shop/insertData`,  // 飘号
            method: 'post',
            data:obj
        })
    },

    // 修改
    updateData(id,obj){
        return request({
            url: `/shop/updateData/${id}`,
            method: 'post',
            data: obj
        })
    },

    // 根据id查询
    getInfo(id){
        return request({
            url: `/shop/getShopInfo/${id}`,
            method: 'get'
        })
    },


    // 删除
    deleteData(id){
        return request({
            url: `/shop/${id}`,
            method: 'delete'
        })
    }


}