package com.tea.server.controller;


import com.tea.server.entity.Address;
import com.tea.server.entity.ReceiveCoupon;
import com.tea.server.service.AddressService;
import com.tea.server.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 地址管理表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2024-09-04
 */
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;


    @ApiOperation("查询地址")
    @GetMapping("getListByOpenid")
    public R getListByOpenid(@RequestParam("openid") String openid) {
        List<Address> list = addressService.getListByOpenid(openid);
        return R.ok().data("list", list);
    }

    @ApiOperation("添加地址")
    @PostMapping("addAddress")
    public R addAddress(@RequestBody Address address){
        Address info = addressService.addAddress(address);
        if (null != info){
            return R.ok().data("info", info);
        }else {
            return R.error();
        }
    }

    @ApiOperation("地址详情")
    @GetMapping("getInfo/{id}")
    public R getInfo(@PathVariable Long id){
        Address info = addressService.getById(id);
        return R.ok().data("info", info);
    }

    @ApiOperation("修改地址")
    @PostMapping("updateAddress")
    public R updateAddress(@RequestBody Address address){
        Integer i = addressService.updateAddress(address);
        if (i > 0){
            return R.ok();
        }else {
            return R.error();
        }
    }
    @ApiOperation("地址详情")
    @PostMapping("deleteData/{id}")
    public R deleteData(@PathVariable Long id){
        boolean b = addressService.removeById(id);
        return R.ok();
    }

}

