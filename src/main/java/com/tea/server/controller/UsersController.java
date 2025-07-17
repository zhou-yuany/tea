package com.tea.server.controller;


import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tea.server.config.WeChatProperties;
import com.tea.server.entity.*;
import com.tea.server.entity.data.AuthJson;
import com.tea.server.entity.data.HobbyData;
import com.tea.server.entity.data.HobbySelect;
import com.tea.server.entity.wx.AccessToken;
import com.tea.server.service.*;
import com.tea.server.utils.CoreUtil;
import com.tea.server.utils.HttpRequest;
import com.tea.server.utils.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2023-08-02
 */
@Slf4j
@RestController
@RequestMapping("users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private AgentsService agentsService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ReceiveCouponService receiveCouponService;

    @Autowired
    private WeChatProperties wechatProperties;

    @Autowired
    private HobbyUserService hobbyUserService;

    @Value("${wx.applet.appid}")
    private String appid;
    @Value("${wx.applet.secret}")
    private String secret;

    @GetMapping("auth")
    public R auth(@RequestParam("code") String code) {
        log.info("进入了auth方法...");
        log.info("code = {}", code);
        // String appid = "wxfab8a1a2bfdd3d94";
        // String secret = "1abd9ecdd28ec3b77d1b6e984efef679";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        RestTemplate restTemplate2 = new RestTemplate();
        String token = restTemplate2.getForObject(tokenUrl, String.class);
        return R.ok().data("response", response).data("token", token);
    }

    // 登陆 微信小程序登陆用户信息
    @GetMapping("login")
    public R login(@RequestParam String openid) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getStatus, 1).eq(Users::getOpenid, openid);
        List<Users> list = usersService.list(queryWrapper);
        if (list.size() > 0) {
            return R.ok();
        } else {
            Boolean flag = usersService.login(openid);
            if (flag) {
                return R.ok();
            } else {
                return R.error("获取用户登录信息失败");
            }
        }

    }

    @GetMapping("getPhone")
    public R getPhone(@RequestParam("code") String code, @RequestParam("openid") String openid, @RequestParam("userId") Long userId) {

        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        RestTemplate restTemplate2 = new RestTemplate();
        AccessToken forObject = restTemplate2.getForObject(tokenUrl, AccessToken.class);
        //使用获取到的token和接受到的code像微信获取手机号
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        System.out.println("token:" + forObject.getAccess_token());
        String url3 = ("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + forObject.getAccess_token());
        String sr3 = HttpRequest.sendPost(url3, jsonObject);
        System.out.println("str3:" + sr3);
        JSONObject phone_info = JSONObject.parseObject(sr3);


        String phone_info1 = phone_info.getString("phone_info");
        JSONObject jsonObject1 = JSONObject.parseObject(phone_info1);
        String phoneNumber = jsonObject1.getString("phoneNumber");
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getStatus, 1).eq(Users::getOpenid, openid);
        Users one = usersService.getOne(wrapper);
        Users users = new Users();

        if (null != one) {

            BeanUtils.copyProperties(one, users);
            users.setPhone(phoneNumber);
            usersService.updateById(users);

        } else {

            users.setOpenid(openid);
            users.setPhone(phoneNumber);
            users.setIsNew(1);
            users.setCreateTime(LocalDateTime.now());
            users.setUpdateTime(LocalDateTime.now());


            LambdaQueryWrapper<Coupon> couponWrapper = new LambdaQueryWrapper<>();
            couponWrapper.eq(Coupon::getStatus, 1).eq(Coupon::getType, 2).le(Coupon::getStartTime, LocalDateTime.now()).ge(Coupon::getEndTime, LocalDateTime.now()).orderByDesc(Coupon::getId);
            List<Coupon> list = couponService.list(couponWrapper);

            if (null != list && list.size() > 0) {
                Coupon coupon = list.get(0);

                // 向用户下发优惠券
                ReceiveCoupon couponUser = new ReceiveCoupon();
                couponUser.setCouponId(coupon.getId());
                couponUser.setShopId(coupon.getShopId());
                couponUser.setDays(coupon.getDays());
                couponUser.setOpenid(openid);
                couponUser.setName(coupon.getName());
                couponUser.setStartTime(coupon.getStartTime());
                couponUser.setEndTime(coupon.getEndTime());
                couponUser.setLimits(coupon.getLimits());
                couponUser.setMonths(coupon.getMonths());
                couponUser.setPlatType(coupon.getPlatType());
                couponUser.setType(coupon.getType());
                couponUser.setParValue(coupon.getParValue());
                couponUser.setCreateTime(LocalDateTime.now());
                couponUser.setUpdateTime(LocalDateTime.now());
                receiveCouponService.save(couponUser);

                if (null != userId && userId > 0) {

                    Users usersOne = usersService.getById(userId);

                    if (null != usersOne) {
                        users.setZfbopenid(usersOne.getOpenid());

                        ReceiveCoupon couponUser2 = new ReceiveCoupon();
                        couponUser2.setCouponId(coupon.getId());
                        couponUser2.setShopId(coupon.getShopId());
                        couponUser2.setDays(coupon.getDays());
                        couponUser2.setOpenid(usersOne.getOpenid());
                        couponUser2.setName("分销优惠券");
                        couponUser2.setStartTime(coupon.getStartTime());
                        couponUser2.setEndTime(coupon.getEndTime());
                        couponUser2.setLimits(coupon.getLimits());
                        couponUser2.setMonths(coupon.getMonths());
                        couponUser2.setPlatType(coupon.getPlatType());
                        couponUser2.setType(coupon.getType());
                        couponUser2.setParValue(coupon.getParValue());
                        couponUser2.setCreateTime(LocalDateTime.now());
                        couponUser2.setUpdateTime(LocalDateTime.now());
                        receiveCouponService.save(couponUser2);
                    }

                }
            }

            usersService.save(users);
        }

        String flag = "";
        Long flagId = 0L;

        // 绑定商户openid
        LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Shop::getStatus, 1).eq(Shop::getPhone, phoneNumber);
        Shop serviceOne = shopService.getOne(wrapper2);
        if (null != serviceOne) {
            if (serviceOne.getDivideAccounts() == 1) {
                flag = "shop";
                flagId = serviceOne.getId();
            }
            Shop shop = new Shop();
            BeanUtils.copyProperties(serviceOne, shop);
            shop.setOpenid(openid);
            shopService.updateById(shop);
        }

        // 绑定代理openid
        LambdaQueryWrapper<Agents> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(Agents::getStatus, 1).eq(Agents::getPhone, phoneNumber);
        Agents agents = agentsService.getOne(wrapper3);

        if (null != agents) {
            if (agents.getDivideAccounts() == 1) {
                flag = "agent";
                flagId = agents.getId();
            }
            Agents agents1 = new Agents();
            BeanUtils.copyProperties(agents, agents1);
            agents1.setOpenid(openid);
            agentsService.updateById(agents1);
        }

        return R.ok().data("phone_info", phone_info).data("flag", flag).data("flagId", flagId).data("info", users);
    }


    @GetMapping("submitPhone")
    public R submitPhone(@RequestParam("openid") String openid, @RequestParam String phone) {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getStatus, 1).eq(Users::getOpenid, openid);
        Users one = usersService.getOne(wrapper);
        Users users = new Users();
        BeanUtils.copyProperties(one, users);
        users.setPhone(phone);
        usersService.updateById(users);

        String flag = "";
        Long flagId = 0L;

        // 绑定商户openid
        LambdaQueryWrapper<Shop> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Shop::getStatus, 1).eq(Shop::getPhone, phone);
        Shop serviceOne = shopService.getOne(wrapper2);
        if (null != serviceOne) {
            if (serviceOne.getDivideAccounts() == 1) {
                flag = "shop";
                flagId = serviceOne.getId();
            }
            Shop shop = new Shop();
            BeanUtils.copyProperties(serviceOne, shop);
            shop.setOpenid(openid);
            shopService.updateById(shop);
        }

        // 绑定代理openid
        LambdaQueryWrapper<Agents> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(Agents::getStatus, 1).eq(Agents::getPhone, phone);
        Agents agents = agentsService.getOne(wrapper3);

        if (null != agents) {
            if (agents.getDivideAccounts() == 1) {
                flag = "agent";
                flagId = agents.getId();
            }
            Agents agents1 = new Agents();
            BeanUtils.copyProperties(agents, agents1);
            agents1.setOpenid(openid);
            agentsService.updateById(agents1);
        }
        return R.ok().data("flag", flag).data("flagId", flagId);
    }

    @GetMapping("getHobbyInfo")
    public R getHobbyInfo(@RequestParam("openid") String openid) {
        LambdaQueryWrapper<HobbyUser> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(HobbyUser::getStatus, 1).eq(HobbyUser::getOpenid, openid);
        HobbyUser hobbyUser = hobbyUserService.getOne(wrapper2);

        List<HobbySelect> list = new ArrayList<>();
        if (null != hobbyUser){


        String ans1 = hobbyUser.getAns1();
        String other1 = hobbyUser.getOther1();
        List<String> strings1 = Stream.of(ans1.split(","))
                .collect(Collectors.toList());
        HobbySelect hobbySelect = new HobbySelect();
        hobbySelect.setNo(0);
        hobbySelect.setOther(other1);
        hobbySelect.setSelect(strings1);
        list.add(hobbySelect);

        String ans2 = hobbyUser.getAns2();
        String other2 = hobbyUser.getOther2();
        List<String> strings2 = Stream.of(ans2.split(","))
                .collect(Collectors.toList());
        HobbySelect hobbySelect2 = new HobbySelect();
        hobbySelect2.setNo(1);
        hobbySelect2.setOther(other2);
        hobbySelect2.setSelect(strings2);
        list.add(hobbySelect2);

        String ans3 = hobbyUser.getAns3();
        String other3 = hobbyUser.getOther3();
        List<String> strings3 = Stream.of(ans3.split(","))
                .collect(Collectors.toList());
        HobbySelect hobbySelect3 = new HobbySelect();
        hobbySelect3.setNo(2);
        hobbySelect3.setOther(other3);
        hobbySelect3.setSelect(strings3);
        list.add(hobbySelect3);

        String ans4 = hobbyUser.getAns4();
        String other4 = hobbyUser.getOther4();
        List<String> strings4 = Stream.of(ans4.split(","))
                .collect(Collectors.toList());
        HobbySelect hobbySelect4 = new HobbySelect();
        hobbySelect4.setNo(3);
        hobbySelect4.setOther(other4);
        hobbySelect4.setSelect(strings4);
        list.add(hobbySelect4);

        String ans5 = hobbyUser.getAns5();
        String other5 = hobbyUser.getOther5();
        List<String> strings5 = Stream.of(ans5.split(","))
                .collect(Collectors.toList());
        HobbySelect hobbySelect5 = new HobbySelect();
        hobbySelect5.setNo(4);
        hobbySelect5.setOther(other5);
        hobbySelect5.setSelect(strings5);
        list.add(hobbySelect5);

        String ans6 = hobbyUser.getAns6();
        String other6 = hobbyUser.getOther6();
        List<String> strings6 = Stream.of(ans6.split(","))
                .collect(Collectors.toList());
        HobbySelect hobbySelect6 = new HobbySelect();
        hobbySelect6.setNo(5);
        hobbySelect6.setOther(other6);
        hobbySelect6.setSelect(strings6);
        list.add(hobbySelect6);

        String ans7 = hobbyUser.getAns7();
        String other7 = hobbyUser.getOther7();
        List<String> strings7 = Stream.of(ans7.split(","))
                .collect(Collectors.toList());
        HobbySelect hobbySelect7 = new HobbySelect();
        hobbySelect7.setNo(6);
        hobbySelect7.setOther(other7);
        hobbySelect7.setSelect(strings7);
        list.add(hobbySelect7);

        String ans8 = hobbyUser.getAns8();
        String other8 = hobbyUser.getOther8();
        List<String> strings8 = Stream.of(ans8.split(","))
                .collect(Collectors.toList());
        HobbySelect hobbySelect8 = new HobbySelect();
        hobbySelect8.setNo(7);
        hobbySelect8.setOther(other8);
        hobbySelect8.setSelect(strings8);
        list.add(hobbySelect8);

        String ans9 = hobbyUser.getAns9();
        String other9 = hobbyUser.getOther9();
        List<String> strings9 = Stream.of(ans9.split(","))
                .collect(Collectors.toList());
        HobbySelect hobbySelect9 = new HobbySelect();
        hobbySelect9.setNo(8);
        hobbySelect9.setOther(other9);
        hobbySelect9.setSelect(strings9);
        list.add(hobbySelect9);

        String ans10 = hobbyUser.getAns10();
        String other10 = hobbyUser.getOther10();
        List<String> strings10 = Stream.of(ans10.split(","))
                .collect(Collectors.toList());
        HobbySelect hobbySelect10 = new HobbySelect();
        hobbySelect10.setNo(9);
        hobbySelect10.setOther(other10);
        hobbySelect10.setSelect(strings10);
        list.add(hobbySelect10);
        }

        return R.ok().data("list", list);
    }

    @GetMapping("getInfo")
    public R getInfo(@RequestParam("openid") String openid) {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getStatus, 1).eq(Users::getOpenid, openid);
        Users one = usersService.getOne(wrapper);

        LambdaQueryWrapper<HobbyUser> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(HobbyUser::getStatus, 1).eq(HobbyUser::getOpenid, openid);
        HobbyUser hobbyUser = hobbyUserService.getOne(wrapper2);

        return R.ok().data("info", one).data("hobbyUser", hobbyUser);
    }

    @PostMapping("saveInfo")
    public R saveInfo(@RequestBody Users obj) {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getStatus, 1).eq(Users::getOpenid, obj.getOpenid());
        Users one = usersService.getOne(wrapper);
        Users users = new Users();
        BeanUtils.copyProperties(one, users);
        users.setUsername(obj.getUsername());
        users.setBrithday(obj.getBrithday());
        users.setCareer(obj.getCareer());
        users.setSex(obj.getSex());
        users.setAvatar(obj.getAvatar());
        usersService.updateById(users);

        LambdaQueryWrapper<HobbyUser> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(HobbyUser::getStatus, 1).eq(HobbyUser::getOpenid, obj.getOpenid());
        HobbyUser hobbyUser = hobbyUserService.getOne(wrapper3);


        LambdaQueryWrapper<ReceiveCoupon> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(ReceiveCoupon::getStatus, 1).eq(ReceiveCoupon::getOpenid, obj.getOpenid()).eq(ReceiveCoupon::getType, 3);
        List<ReceiveCoupon> list = receiveCouponService.list(wrapper2);
        if (list.size() == 0 && null != obj.getCareer() && !obj.getCareer().equals("") && null != hobbyUser) {
            LambdaQueryWrapper<Coupon> couponWrapper = new LambdaQueryWrapper<>();
            couponWrapper.eq(Coupon::getStatus, 1).eq(Coupon::getType, 3).le(Coupon::getStartTime, LocalDateTime.now()).ge(Coupon::getEndTime, LocalDateTime.now()).orderByDesc(Coupon::getId);
            List<Coupon> couponList = couponService.list(couponWrapper);
            if (null != couponList && couponList.size() > 0) {
                couponList.forEach(coupon -> {
                    for (int i = 0; i < coupon.getCount(); i++) {
                        ReceiveCoupon receiveCoupon = new ReceiveCoupon();
                        receiveCoupon.setCouponId(coupon.getId());
                        receiveCoupon.setShopId(coupon.getShopId());
                        receiveCoupon.setDays(coupon.getDays());
                        receiveCoupon.setOpenid(obj.getOpenid());
                        receiveCoupon.setName("完善信息优惠券");
                        receiveCoupon.setStartTime(coupon.getStartTime());
                        receiveCoupon.setEndTime(coupon.getEndTime());
                        receiveCoupon.setLimits(coupon.getLimits());
                        receiveCoupon.setMonths(coupon.getMonths());
                        receiveCoupon.setPlatType(coupon.getPlatType());
                        receiveCoupon.setType(coupon.getType());
                        receiveCoupon.setParValue(coupon.getParValue());
                        receiveCoupon.setCreateTime(LocalDateTime.now());
                        receiveCoupon.setUpdateTime(LocalDateTime.now());
                        receiveCouponService.save(receiveCoupon);
                    }
                });
            }


        }

        return R.ok();
    }

    @PostMapping("saveHobby")
    public R saveHobby(@RequestBody HobbyData obj) {
        LambdaQueryWrapper<HobbyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HobbyUser::getStatus, 1).eq(HobbyUser::getOpenid, obj.getOpenid());
        HobbyUser one = hobbyUserService.getOne(wrapper);
        HobbyUser hobbyUser = new HobbyUser();
        List<HobbySelect> hobby = obj.getHobby();


        if (null != one) {
            BeanUtils.copyProperties(one, hobbyUser);
            hobbyUser.setUpdateTime(LocalDateTime.now());
            if (hobby.size() > 0) {
                hobby.stream().forEach(item -> {
                    switch (item.getNo()) {
                        case 1:
                            hobbyUser.setOther1(item.getOther());
                            hobbyUser.setAns1(String.join(",", item.getSelect()));
                            break;
                        case 2:
                            hobbyUser.setOther2(item.getOther());
                            hobbyUser.setAns2(String.join(",", item.getSelect()));
                            break;
                        case 3:
                            hobbyUser.setOther3(item.getOther());
                            hobbyUser.setAns3(String.join(",", item.getSelect()));
                            break;
                        case 4:
                            hobbyUser.setOther4(item.getOther());
                            hobbyUser.setAns4(String.join(",", item.getSelect()));
                            break;
                        case 5:
                            hobbyUser.setOther5(item.getOther());
                            hobbyUser.setAns5(String.join(",", item.getSelect()));
                            break;
                        case 6:
                            hobbyUser.setOther6(item.getOther());
                            hobbyUser.setAns6(String.join(",", item.getSelect()));
                            break;
                        case 7:
                            hobbyUser.setOther7(item.getOther());
                            hobbyUser.setAns7(String.join(",", item.getSelect()));
                            break;
                        case 8:
                            hobbyUser.setOther8(item.getOther());
                            hobbyUser.setAns8(String.join(",", item.getSelect()));
                            break;
                        case 9:
                            hobbyUser.setOther9(item.getOther());
                            hobbyUser.setAns9(String.join(",", item.getSelect()));
                            break;
                        case 10:
                            hobbyUser.setOther10(item.getOther());
                            hobbyUser.setAns10(String.join(",", item.getSelect()));
                            break;
                        default:
                            System.out.println("未知等级");
                    }

                });
            }

            hobbyUserService.updateById(hobbyUser);
        } else {
            hobbyUser.setOpenid(obj.getOpenid());
            hobbyUser.setCreateTime(LocalDateTime.now());
            hobbyUser.setUpdateTime(LocalDateTime.now());
            if (hobby.size() > 0) {
                hobby.stream().forEach(item -> {
                    switch (item.getNo()) {
                        case 1:
                            hobbyUser.setOther1(item.getOther());
                            hobbyUser.setAns1(String.join(",", item.getSelect()));
                            break;
                        case 2:
                            hobbyUser.setOther2(item.getOther());
                            hobbyUser.setAns2(String.join(",", item.getSelect()));
                            break;
                        case 3:
                            hobbyUser.setOther3(item.getOther());
                            hobbyUser.setAns3(String.join(",", item.getSelect()));
                            break;
                        case 4:
                            hobbyUser.setOther4(item.getOther());
                            hobbyUser.setAns4(String.join(",", item.getSelect()));
                            break;
                        case 5:
                            hobbyUser.setOther5(item.getOther());
                            hobbyUser.setAns5(String.join(",", item.getSelect()));
                            break;
                        case 6:
                            hobbyUser.setOther6(item.getOther());
                            hobbyUser.setAns6(String.join(",", item.getSelect()));
                            break;
                        case 7:
                            hobbyUser.setOther7(item.getOther());
                            hobbyUser.setAns7(String.join(",", item.getSelect()));
                            break;
                        case 8:
                            hobbyUser.setOther8(item.getOther());
                            hobbyUser.setAns8(String.join(",", item.getSelect()));
                            break;
                        case 9:
                            hobbyUser.setOther9(item.getOther());
                            hobbyUser.setAns9(String.join(",", item.getSelect()));
                            break;
                        case 10:
                            hobbyUser.setOther10(item.getOther());
                            hobbyUser.setAns10(String.join(",", item.getSelect()));
                            break;
                        default:
                            System.out.println("未知等级");
                    }

                });
            }

            hobbyUserService.save(hobbyUser);
        }


        return R.ok();
    }


    public static MultipartFile convertToMultipartFile(byte[] fileContent, String fileName, String contentType) {
        return new MockMultipartFile(fileName, fileName, contentType, fileContent);
    }

    // 分销
    @GetMapping("distribution")
    public R distribution(@RequestParam("openid") String openid, @RequestParam Long shopId) throws JsonProcessingException {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getStatus, 1).eq(Users::getOpenid, openid);
        Users one = usersService.getOne(wrapper);
        // 获取分销 二维码
        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        RestTemplate restTemplate = new RestTemplate();
        String token = restTemplate.getForObject(tokenUrl, String.class);
        log.info("token:" + token);
        ObjectMapper mapper = new ObjectMapper();
        AuthJson authToken = mapper.readValue(token, AuthJson.class);
        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Wxacode wxacode = new Wxacode();
        // wxacode.setPath("pages/food/food?shopId="+shop.getId());
        // 要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。
        // wxacode.setEnv_version("develop");
        // wxacode.setWidth("280");
        // Gson gson = new Gson();
        // String json = gson.toJson(wxacode);
        JSONObject json = new JSONObject();

        json.put("path", "pages/food/food?userId=" + one.getId() + "&shopId=" + shopId);
        json.put("env_version", "release");
        json.put("width", 280);
        log.info("json:" + json.toString());
        // String json = "{\"name\":\"John\",\"age\":30}";
        HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
        String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token=" + authToken.getAccess_token();

        byte[] bytes = restTemplate2.postForObject(url, entity, byte[].class);
        // 使用字节数组创建一个ByteArrayInputStream对象
        // 转换为MultipartFile
        MultipartFile multipartFile = convertToMultipartFile(bytes, "code.png", "text/plain");

        String fileName = multipartFile.getOriginalFilename();
        Path relativePath = Paths.get("userCode"
                , DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())
                , CoreUtil.shortUUID() + fileName.substring(fileName.indexOf('.')));
        File outFile = Paths.get(wechatProperties.getUploadPath(), relativePath.toString()).toFile();
        if (!FileUtil.exist(outFile)) {
            outFile.getParentFile().mkdirs();
        }
        try {
            multipartFile.transferTo(outFile);
            return R.ok().data("url", relativePath.toString().replace('\\', '/'));
        } catch (IOException e) {
            e.printStackTrace();
            return R.error();
        }
    }

    @ApiOperation("上传头像")
    @PostMapping("uploadAvatar")
    public R uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path relativePath = Paths.get("avatar"
                , DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())
                , CoreUtil.shortUUID() + fileName.substring(fileName.indexOf('.')));
        File outFile = Paths.get(wechatProperties.getUploadPath(), relativePath.toString()).toFile();
        if (!FileUtil.exist(outFile)) {
            outFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok().data("url", relativePath.toString().replace('\\', '/'));
    }
}

