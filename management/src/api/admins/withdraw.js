import request from '@/utils/request'
export default{
    
    // 分账列表 分页
    getList(page, limit, withdrawalQuery) {
    return request({
      url: `/withdrawal/getList/${page}/${limit}`,
      method: 'post',
      data: withdrawalQuery
    })
  },
  toWithdrawal(id){
    return request({
      url: `/withdrawal/toWithdrawal/${id}`,
      method: 'post',
    })
  },
  unWithdrawalShop(id){
    return request({
      url: `/withdrawal/unWithdrawalShop/${id}`,
      method: 'post',
    })
  },
}