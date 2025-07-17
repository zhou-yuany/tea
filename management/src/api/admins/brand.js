import request from '@/utils/request'
export default {

  // 品牌列表 分页
  getBrandList(page, limit, brandQuery) {
    return request({
      url: `/brand/getBrandList/${page}/${limit}`,
      method: 'post',
      data: brandQuery
    })
  },
  // 添加品牌
  insertData(obj) {
    return request({
      url: `/brand/insertData`,
      method: 'post',
      data: obj
    })
  },
  // 修改品牌
  updateData(obj) {
    return request({
      url: `/brand/updateData`,
      method: 'post',
      data: obj
    })
  },
  getInfo(id) {
    return request({
      url: `/brand/getInfo/${id}`,
      method: 'get',
    })
  },


}