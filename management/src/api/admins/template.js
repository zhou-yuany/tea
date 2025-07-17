import request from '@/utils/request'
export default{
    
    // 列表(条件查询分页)
    getList(page,limit,shopQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/template/getList/${page}/${limit}`,  // 飘号
            method: 'post',
            data: shopQuery
          })
    },



    // 添加
    insertData(obj){
        return request({
            // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
            url: `/template/insertData`,  // 飘号
            method: 'post',
            data:obj
        })
    },

    // 修改
    updateData(obj){
        return request({
            url: `/template/updateData`,
            method: 'post',
            data: obj
        })
    },

    // 根据id查询
    getInfo(id){
        return request({
            url: `/template/getInfo/${id}`,
            method: 'get'
        })
    },


    // 删除
    deleteData(id){
        return request({
            url: `/template/${id}`,
            method: 'delete'
        })
    }


}