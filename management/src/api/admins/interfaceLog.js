import request from '@/utils/request'
export default{
    
    // 列表(条件查询分页)
    getList(page,limit,interfaceLogQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/interfaceLog/getInterfaceLogList/${page}/${limit}`,  // 飘号
            method: 'post',
            data: interfaceLogQuery
          })
    },
      // 查询所有商户
  getAllShops() {
    return request({
      url: `/shop/getAllShops`,
      method: 'get',
    })
  },

}