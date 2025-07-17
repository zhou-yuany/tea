package com.tea.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.vo.CouponQuery;

/**
 * <p>
 * 优惠券表 服务类
 * </p>
 *
 * @author zyy
 * @since 2023-08-25
 */
public interface CouponService extends IService<Coupon> {

    // 优惠券列表 分页查询
    Page<Coupon> pageCoupons(Page<Coupon> couponPage, CouponQuery couponQuery);


}
