import request from '@/utils/request'
export default{

    // 修改
    updateTime(id,obj){
        return request({
            url: `/shop/updateTime/${id}`,
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


}