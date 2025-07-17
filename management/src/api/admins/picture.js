import request from '@/utils/request'
export default{
    


    // 添加
    insertData(obj){
        return request({
            // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
            url: `/templatePicture/insertData`,  // 飘号
            method: 'post',
            data:obj
        })
    },

    // 修改
    updateData(obj){
        return request({
            url: `/templatePicture/updateData`,
            method: 'post',
            data: obj
        })
    },

    // 根据id查询
    getInfo(id){
        return request({
            url: `/templatePicture/getInfo/${id}`,
            method: 'get'
        })
    },



}