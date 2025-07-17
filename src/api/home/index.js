import request from '@/utils/request'

export default {
  // 用户详情
  getUsersInfo(openid) {
    return request({
      url: `/users/getUsersInfo/${openid}`,
      method: 'get',
    })
  },
  // 删除病例图
  removeImg(openid) {
    return request({
      url: `/users/removeCaseImg/${openid}`,
      method: 'get',
    })
  },
  // 多图上传
  uploadCase(file, openid){
    return request({
        url: `/users/uploadCase/${openid}`,
        method: 'post',
        data: file,
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
},
  // 上传病例图
  // saveImages(obj) {
  //   return request({
  //     url: `/users/saveImages`,
  //     method: 'post',
  //     data: obj
  //   })
  // },
  // 用户详情
  getUsersInfo(openid) {
    return request({
      url: `/users/getUsersInfo/${openid}`,
      method: 'get',
    })
  },
   // 用户详情 完整
   getUserInfo(openid) {
    return request({
      url: `/users/getInfo/${openid}`,
      method: 'get',
    })
  },
  // 发送短信
  sendCode(phone,openid) {
    return request({
      url: `/sms/sendSms/${phone}/${openid}`,
      method: 'post',
    })
  },

  // 保存电话
  savePhone(phone,openid) {
    return request({
      url: `/users/savePhone/${phone}/${openid}`,
      method: 'post',
    })
  },
  // 是否上传病例
  getCaseImg(openid) {
    return request({
      url: `/casePicture/getCaseImgs/${openid}`,
      method: 'get',
    })
  },
  // 删除图片
  removeCases(id){
    return request({
        url: `/casePicture/${id}`,
        method: 'delete'
    })
},
  // 病例图片
  getCases(openid) {
    return request({
      url: `/users/getCases/${openid}`,
      method: 'get',
    })
  },
  // 规则
  getRuleInfo() {
    return request({
      url: `/users/getRule`,
      method: 'get',
    })
  },
  // 轮播图
  getCharts() {
    return request({
      url: '/chart/getCharts',
      method: 'get',
    })
  },
  // 热门文章或视频
  getHotList() {
    return request({
      url: '/library/getHotList',
      method: 'get',
    })
  },
  // 专属文章或视频
  getExclusiveList(openid) {
    return request({
      url: `/library/getExclusiveList/${openid}`,
      method: 'get',
    })
  },
  // 援助申请
  getHelpUrl() {
    return request({
      url: '/url/getHelp',
      method: 'get',
    })
  },
  // 查询文章
  getArticleInfo(id, openid) {
    return request({
      url: `/library/getArticleInfo/${id}/${openid}`,
      method: 'get',
    })
  },
  // 查询视频
  getVideoInfo(id, openid) {
    return request({
      url: `/library/getVideoInfo/${id}/${openid}`,
      method: 'get',
    })
  },
  // 查询礼品活动详情
  getActivityInfo(id) {
    return request({
      url: `/activity/getInfo/${id}`,
      method: 'get',
    })
  },
  // 获取分类
  getCates() {
    return request({
      url: '/category/getList',
      method: 'get',
    })
  },
  // 根据分类id查询文章或视频
  getListByCateId(cateId) {
    return request({
      url: `/library/getListByCateId/${cateId}`,
      method: 'get',
    })
  },
  // 礼品活动列表
  getShops() {
    return request({
      url: '/activity/getShops',
      method: 'get',
    })
  },
  // 获取地址信息
  getAddress(openid) {
    return request({
      url: `/address/getAddress/${openid}`,
      method: 'get',
    })
  },
  // 添加地址信息
  insertAddress(openid,info) {
    return request({
      url: `/address/insertAddress/${openid}`,
      method: 'post',
      data: info
    })
  },
  // 修改地址信息
  editAddress(info) {
    return request({
      url: `/address/updateAddress`,
      method: 'post',
      data: info
    })
  },
  // 兑换商品
  insertSendInfo(form) {
    return request({
      url: `/send/insertSendInfo`,
      method: 'post',
      data: form
    })
  },
  // 查询已选择地址信息
  selectAddress(id) {
    return request({
      url: `/address/selectAddress/${id}`,
      method: 'get',
    })
  },
  // 兑换商品列表 全部
  getShopList(openid) {
    return request({
      url: `/send/getShopList/${openid}`,
      method: 'get',
    })
  },
  // 查询兑换商品详情
  getSendInfo(id) {
    return request({
      url: `/send/getSendInfo/${id}`,
      method: 'get',
    })
  },
  // 签到
  signIn(openid) {
    return request({
      url: `/users/signIn/${openid}`,
      method: 'post',
    })
  },
  // 药房列表
  getPharmacys() {
    return request({
      url: '/pharmacy/getList',
      method: 'get',
    })
  },
  // 关键字搜索药房
  getListByKeyWord(form){
    return request({
      url: `/pharmacy/getListByKeyWord`,
      method: 'post',
      data: form
    })
  },
   // 加入历史记录
   insertSearch(openid,form){
    return request({
      url: `/search/insertData/${openid}`,
      method: 'post',
      data: form
    })
  },
  // 搜索记录
  getSearchs(openid) {
    return request({
      url: `/search/getList/${openid}`,
      method: 'get',
    })
  },
  // 删除药房搜索记录
  deleteSearchs(openid){
    return request({
        url: `/search/${openid}`,
        method: 'delete'
    })
},
// 关键字搜索暖冰护理
getLibrarysByKeyWord(form){
  return request({
    url: `/library/getListByKeyWord`,
    method: 'post',
    data: form
  })
},
 // 加入暖冰护理历史记录
 insertRecords(openid,form){
  return request({
    url: `/record/insertData/${openid}`,
    method: 'post',
    data: form
  })
},
// 暖冰护理搜索记录
getRecords(openid) {
  return request({
    url: `/record/getList/${openid}`,
    method: 'get',
  })
},
// 删除暖冰护理搜索记录
deleteRecords(openid){
  return request({
      url: `/record/${openid}`,
      method: 'delete'
  })
},
  // 完善个人档案
  improveInfo(form){
    return request({
      url: `/users/perfectInfo`,
      method: 'post',
      data: form
    })
  },
  // 病例多图上传
  photoUpdate(fileList){
    return request({
        url: `/users/uploadMoreCase`,
        method: 'post',
        data: fileList,
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
},
// 积分详情列表
getPoints(openid) {
  return request({
    url: `/points/getList/${openid}`,
    method: 'get',
  })
},
  auth(authCallbackUrl, returnUrl) {
    return request({
      url: '/wechat/auth',
      method: 'get',
      params: { authCallbackUrl, returnUrl }
    })
  },

}

