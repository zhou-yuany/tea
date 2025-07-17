import request from '@/utils/request'
export default{
    
    // 优惠券列表(条件查询分页)
    getList(page,limit,couponQuery){
        return request({
            // url: '/eduservice/teacher/pageTeacherCondition/'+current+'/'+limit,
            url: `/coupon/getCoupons/${page}/${limit}`,  // 飘号
            method: 'post',
            data: couponQuery
          })
    },



    // 添加优惠券
    addCoupon(coupon){
        return request({
            // url: '/eduservice/course/pageTeacherCondition/'+current+'/'+limit,
            url: `/coupon/addCoupon`,  // 飘号
            method: 'post',
            data:coupon
        })
    },

    // 修改优惠券
    updateCoupon(coupon){
        return request({
            url: `/coupon/updateCoupon`,
            method: 'post',
            data: coupon
        })
    },

    // 根据id查询
    getCouponInfo(id){
        return request({
            url: `/coupon/getCouponInfo/${id}`,
            method: 'get'
        })
    },

    // 根据所有店铺
    getShops(){
        return request({
            url: `/coupon/getShops`,
            method: 'get'
        })
    },


    // 删除
    deleteCopupon(id){
        return request({
            url: `/coupon/${id}`,
            method: 'delete'
        })
    }


}