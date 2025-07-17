package com.tea.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.Coupon;
import com.tea.server.entity.vo.CouponQuery;
import com.tea.server.mapper.CouponMapper;
import com.tea.server.service.CouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠券表 服务实现类
 * </p>
 *
 * @author zyy
 * @since 2023-08-25
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Override
    public Page<Coupon> pageCoupons(Page<Coupon> couponPage, CouponQuery couponQuery) {
        Page<Coupon> list = baseMapper.pageCoupons(couponPage, couponQuery);
        return list;
    }


}
