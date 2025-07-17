import request from '@/utils/request'
export default{
    
    // 成绩管理列表(条件查询分页)
    getListPage(page,limit,softWareQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/achievement/getScoreList/${page}/${limit}`,  // 飘号
            method: 'post',
            data: softWareQuery
          })
    },

    // 编辑成绩评语
    updateScore(id,softWarePo){
        return request({
            url: `/achievement/updateScore/${id}`,
            method: 'post',
            data: softWarePo
        })
    },

    // 根据id查询成绩
    getScoreInfo(id){
        return request({
            url: `/achievement/getScore/${id}`,
            method: 'get'
        })
    },

     // 所有软件
     getSoftwareList(){
        return request({
            url: `/achievement/getSoftwareList`,
            method: 'get'
        })
    },

     // 所有实训项目
     getTrainList(){
        return request({
            url: `/achievement/getTrainList`,
            method: 'get'
        })
    },


}