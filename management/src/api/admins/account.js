import request from '@/utils/request'
export default{
    
    // 列表(条件查询分页)
    getList(page,limit,adminQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/admins/getList/${page}/${limit}`,  // 飘号
            method: 'post',
            data: adminQuery
          })
    },

    // 所有商户
    getshopsList(){
        return request({
            url: `/shop/getAllShops`,
            method: 'get'
        })
    },


    // 添加
    insertData(obj){
        return request({
            // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
            url: `/admins/insertData`,  // 飘号
            method: 'post',
            data:obj
        })
    },

    // 修改
    updateData(id,obj){
        return request({
            url: `/admins/updateData/${id}`,
            method: 'post',
            data: obj
        })
    },

    // 根据id查询
    getInfo(id){
        return request({
            url: `/admins/getAdminInfo/${id}`,
            method: 'get'
        })
    },


    // 删除
    deleteData(id){
        return request({
            url: `/admins/${id}`,
            method: 'delete'
        })
    }


}