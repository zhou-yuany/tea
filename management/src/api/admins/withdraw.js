import request from '@/utils/request'
export default{
    
    // 分账列表 分页
    getList(page, limit, shopId, withdrawalQuery) {
    return request({
      url: `/withdrawal/getListShop/${page}/${limit}/${shopId}`,
      method: 'post',
      data: withdrawalQuery
    })
  },
}