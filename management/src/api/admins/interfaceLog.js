import request from '@/utils/request'
export default{
    
    // 列表(条件查询分页)
    getList(page,limit,shopId,interfaceLogQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/interfaceLog/getShopInterfaceLogList/${page}/${limit}/${shopId}`,  // 飘号
            method: 'post',
            data: interfaceLogQuery
          })
    },

}