import request from '@/utils/request'
export default{
    
    // 列表(条件查询分页)
    getList(page,limit,accountQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/ordersFlow/getListAdmin/${page}/${limit}`,  // 飘号
            method: 'post',
            data: accountQuery
          })
    },

}