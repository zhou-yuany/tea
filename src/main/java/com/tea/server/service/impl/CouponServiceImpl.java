package com.tea.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.Coupon;
import com.tea.server.entity.ReceiveCoupon;
import com.tea.server.mapper.CouponMapper;
import com.tea.server.mapper.ReceiveCouponMapper;
import com.tea.server.service.CouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2023-08-25
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {
    @Resource
    private ReceiveCouponMapper receiveCouponMapper;

    @Override
    public List<ReceiveCoupon> getCouponByOpenid(Long shopId, String openid) {

        List<ReceiveCoupon> list = new ArrayList<>();
                LambdaQueryWrapper<ReceiveCoupon> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ReceiveCoupon :: getStatus, 1).eq(ReceiveCoupon :: getOpenid, openid);
                List<ReceiveCoupon> receiveCoupons = receiveCouponMapper.selectList(wrapper);
                if (null != receiveCoupons && receiveCoupons.size() > 0){
                    receiveCoupons.stream().forEach(item -> {
                        if (item.getPlatType() == 1){
                            list.add(item);
                        }else{
                            if (item.getShopId() == shopId){
                                list.add(item);
                            }
                        }
                    });
                }

        return list;
    }
}
