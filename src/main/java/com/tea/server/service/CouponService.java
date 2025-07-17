package com.tea.server.service;

import com.tea.server.entity.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tea.server.entity.ReceiveCoupon;

import java.util.List;

/**
 * <p>
 * 优惠券表 服务类
 * </p>
 *
 * @author testjava
 * @since 2023-08-25
 */
public interface CouponService extends IService<Coupon> {

    // 查询店铺下的优惠券 以及用户是否领取过优惠券
    List<ReceiveCoupon> getCouponByOpenid(Long shopId, String openid);
}
