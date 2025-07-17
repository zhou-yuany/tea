import request from '@/utils/request'
export default{
    
    // 列表(条件查询分页)
    getList(page,limit,agentsQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/agents/getList/${page}/${limit}`,  // 飘号
            method: 'post',
            data: agentsQuery
          })
    },


    // 所有商户
    getShopList(){
        return request({
            url: `/agents/allList`,
            method: 'get'
        })
    },


    // 添加
    insertData(obj){
        return request({
            // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
            url: `/agents/insertData`,  // 飘号
            method: 'post',
            data:obj
        })
    },

    // 修改
    updateData(id,obj){
        return request({
            url: `/agents/updateData/${id}`,
            method: 'post',
            data: obj
        })
    },

    // 根据id查询
    getInfo(id){
        return request({
            url: `/agents/getAgentsInfo/${id}`,
            method: 'get'
        })
    },


    // 删除
    deleteData(id){
        return request({
            url: `/agents/${id}`,
            method: 'delete'
        })
    }


}