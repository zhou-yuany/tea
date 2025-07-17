package com.tea.server.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.entity.*;
import com.tea.server.entity.data.AdminData;
import com.tea.server.entity.query.ShopQuery;
import com.tea.server.service.AdminsService;
import com.tea.server.service.EquipmentService;
import com.tea.server.service.ShopsService;
import com.tea.server.utils.CoreUtil;
import com.tea.server.utils.JwtUtils;
import com.tea.server.utils.R;
import eleme.openapi.sdk.config.Config;
import eleme.openapi.sdk.oauth.OAuthClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@RestController
@RequestMapping("admins")
@CrossOrigin
public class AdminsController {
    // 设置是否沙箱环境
    private static final boolean isSandbox = true;
    // 设置APPKEY
    private static final String key = "HTsjSlMBbm";
    // 设置APPSECRET
    private static final String secret = "bb5041114807043c51a59b208609ac11d97c9136";
    // 初始化OAuthClient
    private static OAuthClient client = null;
    private static Map<String, String> tokenMap = new HashMap<String, String>();
    private static Config config = null;

    static {
        // 初始化全局配置工具
        config = new Config(isSandbox, key, secret);
        client = new OAuthClient(config);
    }

    @Autowired
    private AdminsService adminsService;

    @Autowired
    private ShopsService shopsService;

    @Autowired
    private EquipmentService equipmentService;

//     // 茶饮、竖屏登录
//     @PostMapping("login1")
//     public R loginPlatform(@RequestBody Admins user, HttpServletRequest request) {
//         // 获取前端传来的用户名和密码
//         String username = user.getUsername();
//         String password = user.getPassword();
//         String encodePassword = MD5.create().digestHex(password, StandardCharsets.UTF_8);
// //        Users users = mapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getAccountNumber, username)
// //                .eq(Users::getPassword, encodePassword));
//         Admins users = adminsService.getOne(new LambdaQueryWrapper<Admins>().eq(Admins::getUsername, username).eq(Admins::getStatus, 1).eq(Admins :: getType, 1));
//         if (users == null) {
//             return R.error().message("用户不存在!");
//         }
//         if (!users.getPassword().equals(encodePassword)) {
//             return R.error().message("密码错误!");
//         }
//
//
//         String token = CoreUtil.getUUID();
//         CoreUtil.getCache().put(token, users);
//         request.getSession().setAttribute("admin",users );
//
//         LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
//         wrapper.eq(Shop :: getStatus, 1).eq(Shop :: getAdminId, users.getId());
//         Shop one = shopsService.getOne(wrapper);
//         if (one.getType().contains("1")){
//             String redirect_uri = "https://tea.yinghai-tec.com/tea-management/ele/callback";
//             String scope = "all";
//             String state = one.getName() + one.getId();
//             String authUrl = client.getAuthUrl(redirect_uri, scope, state);
//             return R.ok().data("info",users).data("token", token).data("divideAccounts", one.getDivideAccounts()).data("authUrl", authUrl);
//         }else if(one.getType().contains("2")){
//             return R.ok().data("info",users);
//         }else {
//             return R.ok().data("info",users).data("token", token).data("divideAccounts", one.getDivideAccounts());
//         }
//
//     }

    @ApiOperation(value = "后台登录")
    @PostMapping("login1")
    public R login1(@RequestBody Admins admins) {
        // 获取前端传来的用户名和密码
        String username = admins.getUsername();
        String password = admins.getPassword();


        // 判断用户名和密码是否为空
        if (!StringUtils.hasText(username)) {
            return R.error().message("账号不能为空！");
        }
        // 判断密码是否为空
        if (!StringUtils.hasText(password)) {
            return R.error().message("密码不能为空！");
        }

        // 根据用户名查询数据库
        QueryWrapper<Admins> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("type", 1);
        Admins admin = adminsService.getOne(wrapper);

        // 判断用户是否存在
        if (admin == null) {
            return R.error().message("账号不存在！");
        }
        // 判断密码是否正确
        if (!MD5.create().digestHex(password).equals(admin.getPassword())) {
            return R.error().message("密码错误！");
        }


        // 返回token字符串
        String jwtToken = JwtUtils.getJwtToken(admin.getId(), admin.getUsername());

        return R.ok().data("token", jwtToken);
    }
    @ApiOperation(value = "竖屏登录")
    @PostMapping("portraitLogin")
    public R portraitLogin(@RequestBody Admins admins) {
        // 获取前端传来的用户名和密码
        String username = admins.getUsername();
        String password = admins.getPassword();


        // 判断用户名和密码是否为空
        if (!StringUtils.hasText(username)) {
            return R.error().message("账号不能为空！");
        }
        // 判断密码是否为空
        if (!StringUtils.hasText(password)) {
            return R.error().message("密码不能为空！");
        }

        // 根据用户名查询数据库
        QueryWrapper<Admins> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("type", 1);
        Admins admin = adminsService.getOne(wrapper);

        // 判断用户是否存在
        if (admin == null) {
            return R.error().message("账号不存在！");
        }
        // 判断密码是否正确
        if (!MD5.create().digestHex(password).equals(admin.getPassword())) {
            return R.error().message("密码错误！");
        }

        LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Shop :: getStatus, 1).eq(Shop :: getAdminId, admin.getId()).eq(Shop :: getIsUse, 1);
        Shop one = shopsService.getOne(wrapper2);
        if (null != one){
            // 返回token字符串
            String jwtToken = JwtUtils.getJwtToken(admin.getId(), admin.getPhone());
            LambdaQueryWrapper<Equipment> equipmentWrapper = new LambdaQueryWrapper<>();
            equipmentWrapper.eq(Equipment :: getStatus, 1).eq(Equipment :: getShopId, one.getId());
            List<Equipment> list = equipmentService.list(equipmentWrapper);
            Integer type = list.get(0).getType();
            if (type == 1){
                return R.ok().data("token", jwtToken).data("info", admin).data("divideAccounts",one.getDivideAccounts()).data("shopId", one.getEleShopId()).data("shopInfoId", one.getId()).data("balance", one.getBalance());
            }else {
                return R.error().message("该店铺为自助店，不可登录！");
            }



        }else {
            return R.error().message("该账号已被禁用！");
        }



    }

    // @ApiOperation(value = "迷你屏登录")
    // @PostMapping("miniLogin")
    // public R miniLogin(@RequestBody Admins admins) {
    //     // 获取前端传来的用户名和密码
    //     String username = admins.getUsername();
    //     String password = admins.getPassword();
    //
    //
    //     // 判断用户名和密码是否为空
    //     if (!StringUtils.hasText(username)) {
    //         return R.error().message("账号不能为空！");
    //     }
    //     // 判断密码是否为空
    //     if (!StringUtils.hasText(password)) {
    //         return R.error().message("密码不能为空！");
    //     }
    //
    //     // 根据用户名查询数据库
    //     QueryWrapper<Admins> wrapper = new QueryWrapper<>();
    //     wrapper.eq("username", username).eq("type", 1);
    //     Admins admin = adminsService.getOne(wrapper);
    //
    //     // 判断用户是否存在
    //     if (admin == null) {
    //         return R.error().message("账号不存在！");
    //     }
    //     // 判断密码是否正确
    //     if (!MD5.create().digestHex(password).equals(admin.getPassword())) {
    //         return R.error().message("密码错误！");
    //     }
    //
    //     LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
    //     wrapper2.eq(Shop :: getStatus, 1).eq(Shop :: getAdminId, admin.getId()).eq(Shop :: getIsUse, 2);
    //     Shop one = shopsService.getOne(wrapper2);
    //     if (null != one){
    //         // 返回token字符串
    //         String jwtToken = JwtUtils.getJwtToken(admin.getId(), admin.getPhone());
    //         LambdaQueryWrapper<Equipment> equipmentWrapper = new LambdaQueryWrapper<>();
    //         equipmentWrapper.eq(Equipment :: getStatus, 1).eq(Equipment :: getShopId, one.getId());
    //         List<Equipment> list = equipmentService.list(equipmentWrapper);
    //         Integer type = list.get(0).getType();
    //         if (type == 2){
    //             return R.ok().data("token", jwtToken).data("info", admin).data("divideAccounts",one.getDivideAccounts()).data("shopId", one.getEleShopId()).data("shopInfoId", one.getId()).data("balance", one.getBalance());
    //         }else {
    //             return R.error().message("该店铺为外卖店，不可登录！");
    //         }
    //
    //
    //
    //     }else {
    //         return R.error().message("该账号已被禁用！");
    //     }
    //
    //
    //
    // }
    @ApiOperation(value = "迷你屏登录")
    @PostMapping("miniLogin")
    public R miniLogin(@RequestBody Equipment equipment) {
        // 获取前端传来的用户名和密码
        String equipmentNo = equipment.getEquipmentNo();


        // 判断用户名和密码是否为空
        // if (!StringUtils.hasText(username)) {
        //     return R.error().message("账号不能为空！");
        // }
        // 判断密码是否为空
        // if (!StringUtils.hasText(password)) {
        //     return R.error().message("密码不能为空！");
        // }

        // 根据用户名查询数据库
        // QueryWrapper<Admins> wrapper = new QueryWrapper<>();
        // wrapper.eq("username", username).eq("type", 1);
        // Admins admin = adminsService.getOne(wrapper);
        //
        // // 判断用户是否存在
        // if (admin == null) {
        //     return R.error().message("账号不存在！");
        // }
        // // 判断密码是否正确
        // if (!MD5.create().digestHex(password).equals(admin.getPassword())) {
        //     return R.error().message("密码错误！");
        // }
        LambdaQueryWrapper<Equipment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Equipment :: getStatus, 1).eq(Equipment :: getEquipmentNo, equipmentNo).eq(Equipment :: getIsUse, 2);
        Equipment equipmentServiceOne = equipmentService.getOne(wrapper);
        // LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
        // wrapper2.eq(Shop :: getStatus, 1).eq(Shop :: getAdminId, admin.getId()).eq(Shop :: getIsUse, 1);
        // Shop one = shopsService.getOne(wrapper2);
        Shop shop = shopsService.getById(equipmentServiceOne.getShopId());
        if (null != shop ){
            if (shop.getIsUse() == 1){
                Admins admin = adminsService.getById(shop.getAdminId());
                // 返回token字符串
                String jwtToken = JwtUtils.getJwtToken(shop.getId(), shop.getPhone());
                // LambdaQueryWrapper<Equipment> equipmentWrapper = new LambdaQueryWrapper<>();
                // equipmentWrapper.eq(Equipment :: getStatus, 1).eq(Equipment :: getShopId, one.getId());
                // List<Equipment> list = equipmentService.list(equipmentWrapper);
                // Integer type = list.get(0).getType();
                Integer type = equipmentServiceOne.getType();
                if (type == 2){
                    return R.ok().data("token", jwtToken).data("info", admin).data("divideAccounts",shop.getDivideAccounts()).data("shopId", shop.getEleShopId()).data("shopInfoId", shop.getId()).data("balance", shop.getBalance());
                }else {
                    return R.error().message("该店铺为外卖店，不可登录！");
                }
            }else {
                return R.error().message("该账号已被禁用！");
            }


        }else {
            return R.error().message("该设备编号无效！");

        }



    }

    @ApiOperation(value = "总后台登录")
    @PostMapping("login")
    public R loginService(@RequestBody Admins admins) {
        // 获取前端传来的用户名和密码
        String username = admins.getUsername();
        String password = admins.getPassword();


        // 判断用户名和密码是否为空
        if (!StringUtils.hasText(username)) {
            return R.error().message("账号不能为空！");
        }
        // 判断密码是否为空
        if (!StringUtils.hasText(password)) {
            return R.error().message("密码不能为空！");
        }

        // 根据用户名查询数据库
        QueryWrapper<Admins> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("type", 2);
        Admins admin = adminsService.getOne(wrapper);

        // 判断用户是否存在
        if (admin == null) {
            return R.error().message("账号不存在！");
        }
        // 判断密码是否正确
        if (!MD5.create().digestHex(password).equals(admin.getPassword())) {
            return R.error().message("密码错误！");
        }


        // 返回token字符串
        String jwtToken = JwtUtils.getJwtToken(admin.getId(), admin.getUsername());

        return R.ok().data("token", jwtToken);
    }

    @ApiOperation(value = "使用token获取用户id，根据用户id获取用户信息")
    @GetMapping("info")
    public R getMemberInfo(@RequestHeader("X-Token") String token, HttpServletRequest request) {
        // 使用jwt工具类的方法，根据request获取头部信息，返回用户id
        Integer memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        // 根据用户id到数据库查询并返回用户信息
        Admins member = adminsService.getById(memberIdByJwtToken);

        if (member.getType() == 1){
            LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Shop :: getStatus, 1).eq(Shop :: getAdminId, member.getId());
            Shop one = shopsService.getOne(wrapper);
            if (one.getType().contains("1")) {
                String redirect_uri = "https://tea.yinghai-tec.com/tea-management/ele/callback";
                String scope = "all";
                String state = one.getName() + one.getId();
                String authUrl = client.getAuthUrl(redirect_uri, scope, state);
                return R.ok().data("adminInfo", member).data("authUrl", authUrl).data("shopId", one.getEleShopId()).data("divideAccounts", one.getDivideAccounts()).data("balance", one.getBalance());
            }else if(one.getType().contains("2")){
                return R.ok();
            }else {
                return R.ok().data("adminInfo", member).data("shopId", one.getEleShopId()).data("divideAccounts", one.getDivideAccounts()).data("balance", one.getBalance());
            }
        }else {
            return R.ok().data("adminInfo", member);
        }



    }

    @ApiOperation(value = "退出")
    @PostMapping("logout")
    public R logOut(HttpSession session) {

        session.invalidate();
        return R.ok();
    }


    @ApiOperation(value = "根据id获取帐号详情")
    @GetMapping("getAdminInfo/{id}")
    public R getAdminInfo(@PathVariable Long id) {
        Admins admins = adminsService.getById(id);
        AdminData adminData = new AdminData();
        BeanUtils.copyProperties(admins, adminData);
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop :: getStatus, 1).eq(Shop :: getAdminId, id);
        Shop one = shopsService.getOne(queryWrapper);
        adminData.setShopId(one.getId());
        adminData.setShopName(one.getName());

        return R.ok().data("info", adminData);

    }


    /**
     * 帐号列表 分页
     */
    @PostMapping("getList/{page}/{limit}")
    public R getList(@PathVariable long page, @PathVariable long limit, @RequestBody Admins adminQuery) {
        // 创建page对象
        Page<Admins> adminPage = new Page<>(page, limit);
        // 构建条件
        Page<Admins> pageList = adminsService.getList(adminPage, adminQuery);

        long total = pageList.getTotal();
        List<Admins> records = pageList.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 添加
     *
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody AdminData obj) {
        Admins admins = new Admins();
        BeanUtils.copyProperties(obj, admins);
        admins.setType(1);
        admins.setPassword(MD5.create().digestHex(obj.getPassword()));
        admins.setCreateTime(LocalDateTime.now());
        admins.setUpdateTime(LocalDateTime.now());
        boolean save = adminsService.save(admins);
        if (save) {
            Long shopId = obj.getShopId();
            Shop shop = shopsService.getById(shopId);
            Shop shop1 = new Shop();
            BeanUtils.copyProperties(shop, shop1);
            shop1.setAdminId(admins.getId());
            shopsService.updateById(shop1);
            return R.ok();
        } else {
            return R.error();
        }


    }

    /**
     * 修改
     *
     * @return
     */
    @PostMapping("updateData/{id}")
    public R updateData(@PathVariable long id, @RequestBody AdminData obj) {
        Admins admins = adminsService.getById(id);
        admins.setName(obj.getName());
        admins.setPhone(obj.getPhone());
        admins.setUsername(obj.getUsername());
        if (!admins.getPassword().equals(MD5.create().digestHex(obj.getPassword()))) {
            admins.setPassword(MD5.create().digestHex(obj.getPassword()));
        }

        admins.setUpdateTime(LocalDateTime.now());
        boolean save = adminsService.updateById(admins);
        if (save) {
            Long shopId = obj.getShopId();
            Shop shop = shopsService.getById(shopId);
            Shop shop1 = new Shop();
            BeanUtils.copyProperties(shop, shop1);
            shop1.setAdminId(admins.getId());
            shopsService.updateById(shop1);
            return R.ok();
        } else {
            return R.error();
        }


    }


    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        boolean b = adminsService.removeById(id);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }

}

