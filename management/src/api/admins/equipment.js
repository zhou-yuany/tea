import request from '@/utils/request'
export default{
    
// 查询设备列表 分页
getEquipmentList(page, limit, type) {
    return request({
      url: `/equipmentAll/getEquipmentList/${page}/${limit}`,
      method: 'post',
      params: { type: type }
    })
  },
    // 添加设备
    insertData(obj) {
        return request({
          url: `/equipmentAll/insertData`,
          method: 'post',
          data: obj
        })
      },


}