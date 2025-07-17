package com.tea.server.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Coupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tea.server.entity.vo.CouponQuery;

/**
 * <p>
 * 优惠券表 Mapper 接口
 * </p>
 *
 * @author zyy
 * @since 2023-08-25
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    Page<Coupon> pageCoupons(Page<Coupon> couponPage, CouponQuery couponQuery);
}
