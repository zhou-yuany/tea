package com.tea.server.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.imageio.plugins.jpeg.JPEG;
import com.sun.javafx.iio.jpeg.JPEGImageLoaderFactory;
import com.tea.server.config.WeChatProperties;
import com.tea.server.entity.*;
import com.tea.server.entity.data.AuthJson;
import com.tea.server.entity.data.Wxacode;
import com.tea.server.entity.query.OrderQuery;
import com.tea.server.entity.query.ShopQuery;
import com.tea.server.service.*;
import com.tea.server.utils.CoreUtil;
import com.tea.server.utils.HttpRequestUtil;
import com.tea.server.utils.JwtUtils;
import com.tea.server.utils.R;
import eleme.openapi.sdk.api.entity.product.OCategory;
import eleme.openapi.sdk.api.service.ProductService;
import eleme.openapi.sdk.config.Config;
import eleme.openapi.sdk.oauth.OAuthClient;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.mock.web.MockMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 店铺表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2023-08-24
 */
@Slf4j
@RestController
@RequestMapping("shop")
public class ShopController {


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
    private ShopsService shopsService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private WeChatProperties wechatProperties;

    @Autowired
    private ShopToParamService shopToParamService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentAllService equipmentAllService;

    @Autowired
    private GoodsAllBatchService goodsAllBatchService;

    @Autowired
    private TemplateService templateService;


    @Value("${wechat.appid}")
    private String wxAppid;
    @Value("${wechat.secret}")
    private String wxSecret;


    /**
     * 根据收银员id获取店铺详情
     */
    @GetMapping("getShopInfo1/{adminId}")
    public R getShopInfo1(@PathVariable Long adminId) {
        // 茶饮小程序
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, 1).in(Shop::getAdminId, adminId);
        Shop one = shopsService.getOne(queryWrapper);
        return R.ok().data("info", one);
    }


    @ApiOperation(value = "登录")
    @PostMapping("login")
    public R loginService(@RequestBody Shop shop) {
        // 获取前端传来的手机号和密码
        String username = shop.getUsername();
        String password = shop.getPassword();
        // Integer type = shop.getType();


        // 判断用户名和密码是否为空
        if (!StringUtils.hasText(username)) {
            return R.error().message("帐号不能为空！");
        }
        // 判断密码是否为空
        if (!StringUtils.hasText(password)) {
            return R.error().message("密码不能为空！");
        }


        // 根据用户名查询数据库
        QueryWrapper<Shop> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Shop info = shopsService.getOne(wrapper);

        // 判断用户是否存在
        if (info == null) {
            return R.error().message("账号不存在！");
        }
        // 判断密码是否正确
        if (!MD5.create().digestHex(password).equals(info.getPassword())) {
            return R.error().message("密码错误！");
        }
        if (info != null && info.getIsUse() == 2) {
            return R.error().message("账号已被禁用！");
        }


        // 返回token字符串
        String jwtToken = JwtUtils.getJwtToken(info.getId(), info.getUsername());



        return R.ok().data("token", jwtToken);
    }


    @ApiOperation(value = "使用token获取用户id，根据用户id获取用户信息")
    @GetMapping("info")
    public R getMemberInfo(@RequestHeader("X-Token") String token, HttpServletRequest request) {
        // 使用jwt工具类的方法，根据request获取头部信息，返回用户id
        Integer memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        // 根据用户id到数据库查询并返回用户信息
        Shop shop = shopsService.getById(memberIdByJwtToken);
        if (shop.getType().contains("1")) {
            String redirect_uri = "https://tea.yinghai-tec.com/tea-management/ele/callback";
            String scope = "all";
            String state = shop.getName() + shop.getId();
            String authUrl = client.getAuthUrl(redirect_uri, scope, state);
            return R.ok().data("adminInfo", shop).data("authUrl", authUrl);
        } else if (shop.getType().contains("2")) {
            String url = "https://openapi.meituan.com/oauth/authorize";
            String client_id = "";
            String response_type = "code";
            String param = "client_id="+client_id+"&response_type="+response_type;
            HttpRequestUtil.sendGet(url, param);
            return R.ok().data("adminInfo", shop);
        } else {
            return R.ok().data("adminInfo", shop);
        }

    }


    @ApiOperation(value = "退出")
    @PostMapping("logout")
    public R logOut(HttpSession session) {

        session.invalidate();
        return R.ok();
    }

    // @ApiOperation(value = "设置Token信息")
    // private static void setTokenInfo(Token token) {
    //     if (null != token && token.isSuccess()) {
    //         tokenMap.put("access_token", token.getAccessToken());
    //         tokenMap.put("token_type", token.getTokenType());
    //         tokenMap.put("expires_in", String.valueOf(token.getExpires()));
    //         tokenMap.put("refresh_token", token.getRefreshToken());
    //         PropertiesUtils.setProps(tokenMap);
    //     }
    // }

    @ApiOperation(value = "查询所有商户")
    @GetMapping("getAllShops")
    public R getAllShops() {
        List<Shop> list = shopsService.list(null);
        return R.ok().data("list", list);

    }


    @ApiOperation(value = "根据id获取商户详情")
    @GetMapping("getShopInfo/{id}")
    public R getShopInfo(@PathVariable Long id) {
        Shop shop = shopsService.getById(id);
        if (null != shop.getBrandId()) {
            Brand byId = brandService.getById(shop.getBrandId());
            shop.setBrandName(byId.getName());
            String shopGoodRange = shop.getGoodRange();
            if (null != shopGoodRange && !shopGoodRange.equals("")) {
                List<Long> goodRangeList = Arrays.asList(shopGoodRange.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
                shop.setGoodRangeList(goodRangeList);
                if (goodRangeList.size() > 0) {
                    List<String> list = new ArrayList<>();
                    goodRangeList.stream().forEach(item -> {
                        Goods goods = goodsService.getById(item);
                        if (null != goods) {
                            list.add(goods.getName());
                        }

                    });
                    if (list.size() > 0) {
                        String platformName = list.stream().map(String::valueOf).collect(Collectors.joining(","));
                        shop.setPlatformName(platformName);
                    }

                }
            }
            String type = shop.getType();
            if (null != type && !type.equals("")) {
                List<Integer> platformType = Arrays.asList(type.split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
                shop.setPlatformType(platformType);
                if (platformType.size() > 0) {
                    List<String> list = new ArrayList<>();
                    platformType.stream().forEach(item -> {
                        list.add(item == 0 ? "自助店" : item == 1 ? "饿了么外卖店" : "美团外卖店");
                    });
                    if (list.size() > 0) {
                        String goodsName = list.stream().map(String::valueOf).collect(Collectors.joining(","));
                        shop.setGoodsName(goodsName);
                    }

                }

            }
            String equipmentId = shop.getEquipmentId();
            if (null != equipmentId && !equipmentId.equals("")) {
                List<String> equipmentIds = Arrays.asList(equipmentId.split(",")).stream().collect(Collectors.toList());
                LambdaQueryWrapper<Equipment> equipmentsWrapper = new LambdaQueryWrapper<>();
                equipmentsWrapper.eq(Equipment::getStatus, 1).eq(Equipment::getShopId, id).last("limit 1");
                Equipment one = equipmentService.getOne(equipmentsWrapper);
                shop.setEquipmentType(one.getType());
                shop.setEquipmentIdList(equipmentIds);
            }
            String equipmentCode = shop.getEquipmentCode();
            if (null != equipmentCode && !equipmentCode.equals("")) {
                List<String> equipmentCodes = Arrays.asList(equipmentCode.split(",")).stream().collect(Collectors.toList());
                shop.setEquipmentCodeList(equipmentCodes);
            }

        }

        return R.ok().data("info", shop);

    }

    /**
     * 上传商户logo
     *
     * @param file
     * @return
     */
    @PostMapping("upload")
    public R upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path relativePath = Paths.get("shop"
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

    /**
     * 商户列表 分页
     */
    @PostMapping("getList/{page}/{limit}")
    public R getList(@PathVariable long page, @PathVariable long limit, @RequestBody ShopQuery shopQuery) {
        // 创建page对象
        Page<Shop> shopPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Shop::getStatus, 1);
        String name = shopQuery.getName();
        String phone = shopQuery.getPhone();
        Long brandId = shopQuery.getBrandId();
        if (null != name && !name.equals("")) {
            wrapper.like(Shop::getName, name);
        }
        if (null != phone && !phone.equals("")) {
            wrapper.like(Shop::getPhone, phone);
        }
        if (null != brandId && brandId > 0) {
            wrapper.eq(Shop::getBrandId, brandId);
        }
        wrapper.orderByDesc(Shop::getId);

        shopsService.page(shopPage, wrapper);

        List<Shop> records = shopPage.getRecords();
        if (records.size() > 0) {
            records.stream().forEach(item -> {
                if (null != item.getBrandId()) {
                    Brand brand = brandService.getById(item.getBrandId());
                    item.setBrandName(brand.getName());
                }

            });
        }
        long total = shopPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    public static MultipartFile convertToMultipartFile(byte[] fileContent, String fileName, String contentType) {
        return new MockMultipartFile(fileName, fileName, contentType, fileContent);
    }

    /**
     * 添加
     *
     * @return
     */
    @SneakyThrows
    @PostMapping("insertData")
    public R insertData(@RequestBody Shop obj) {
        Shop shop = new Shop();
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getStatus, 1).eq(Shop::getName, obj.getName()).eq(Shop::getPhone, obj.getPhone());
        List<Shop> list = shopsService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该商铺已添加");
        } else {
            LambdaQueryWrapper<Shop> wrapper4 = new LambdaQueryWrapper<>();
            wrapper4.eq(Shop::getStatus, 1).eq(Shop::getPhone, obj.getPhone());
            List<Shop> list2 = shopsService.list(wrapper4);
            if (null != list2 && list2.size() > 0) {
                return R.ok().message("该手机号已被使用");
            } else {

                BeanUtils.copyProperties(obj, shop);
                List<Long> goodRangeList = obj.getGoodRangeList();

                // 增加商户配料
                // LambdaQueryWrapper<GoodsAllBatch> allBatchWrapper = new LambdaQueryWrapper<>();
                // allBatchWrapper.in(GoodsAllBatch :: getGoodsId, goodRangeList).eq(GoodsAllBatch :: getStatus, 1);
                // List<GoodsAllBatch> goodsAllBatches = goodsAllBatchService.list(allBatchWrapper);
                // List<Long> batchList = goodsAllBatches.stream().map(iii -> iii.getBatchId()) // 将List转换为Stream
                //         .distinct() // 去除重复元素
                //         .collect(Collectors.toList());
                //
                // BatchUse batchUse = new BatchUse();
                // batchUse.setBatchId();
                // batchUse.setTotalCount(0);
                // batchUse.setShopId();
                // batchUse.setOutlet();
                // batchUse.setCreateTime(LocalDateTime.now());
                // batchUse.setUpdateTime(LocalDateTime.now());
                if (null != obj.getTemplateId()){
                    Template template = templateService.getById(obj.getTemplateId());
                    shop.setColor(template.getColorName());
                }

                String goodRange = goodRangeList.stream().map(String::valueOf).collect(Collectors.joining(","));
                shop.setGoodRange(goodRange);
                if(null != obj.getEquipmentIdList() && obj.getEquipmentIdList().size() > 0){
                    List<String> equipmentIdList = obj.getEquipmentIdList();
                    String equipmentId = equipmentIdList.stream().collect(Collectors.joining(","));
                    shop.setEquipmentId(equipmentId);
                }

                List<Integer> platformType = obj.getPlatformType();
                String type = null;
                if (platformType.size() > 1) {
                    type = platformType.stream().map(String::valueOf).collect(Collectors.joining(","));
                } else {
                    type = String.valueOf(platformType.get(0));
                }
                shop.setRedirectUri("https://tea.yinghai-tec.com/tea-management/ele/callback");
                shop.setType(type);
                shop.setPassword(MD5.create().digestHex(obj.getPassword()));
                shop.setCreateTime(LocalDateTime.now());
                shop.setUpdateTime(LocalDateTime.now());
                boolean save = shopsService.save(shop);

                List<String> equipmentCodeList = new ArrayList<>();

                if (save) {
                    // 添加设备
                    if (null != obj.getEquipmentIdList() && obj.getEquipmentIdList().size() > 0) {

                        obj.getEquipmentIdList().stream().forEach(item -> {
                            LambdaQueryWrapper<EquipmentAll> equipmentAllWrapper = new LambdaQueryWrapper<>();
                            equipmentAllWrapper.eq(EquipmentAll::getStatus, 1).eq(EquipmentAll::getEquipmentNo, item);
                            EquipmentAll one = equipmentAllService.getOne(equipmentAllWrapper);
                            Equipment equipment = new Equipment();
                            equipment.setType(one.getType());
                            equipment.setEquipmentNo(item);
                            equipment.setShopId(shop.getId());
                            equipment.setCreateTime(LocalDateTime.now());
                            equipment.setUpdateTime(LocalDateTime.now());
                            equipmentService.save(equipment);

                            EquipmentAll equipmentAll = new EquipmentAll();
                            BeanUtils.copyProperties(one, equipmentAll);
                            equipmentAll.setIsBinding(1);
                            equipmentAllService.updateById(equipmentAll);

                            // 根据设备型号获取微信小程序商户自己的二维码
                            String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxAppid + "&secret=" + wxSecret;
                            RestTemplate restTemplate = new RestTemplate();
                            String token = restTemplate.getForObject(tokenUrl, String.class);
                            log.info("token:" + token);
                            ObjectMapper mapper = new ObjectMapper();
                            AuthJson authToken = null;
                            try {
                                authToken = mapper.readValue(token, AuthJson.class);
                                RestTemplate restTemplate2 = new RestTemplate();
                                HttpHeaders headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                JSONObject json = new JSONObject();
                                json.put("path", "pages/food/food?equipmentId=" + equipment.getId()); // 商户设备表id
                                json.put("env_version", "release");
                                json.put("width", 280);
                                log.info("json:" + json.toString());
                                HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
                                String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token=" + authToken.getAccess_token();

                                byte[] bytes = restTemplate2.postForObject(url, entity, byte[].class);
                                // 使用字节数组创建一个ByteArrayInputStream对象
                                // 转换为MultipartFile
                                MultipartFile multipartFile = convertToMultipartFile(bytes, "code.png", "text/plain");

                                String fileName = multipartFile.getOriginalFilename();
                                Path relativePath = Paths.get("equipmentCode"
                                        , DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())
                                        , CoreUtil.shortUUID() + fileName.substring(fileName.indexOf('.')));
                                File outFile = Paths.get(wechatProperties.getUploadPath(), relativePath.toString()).toFile();
                                if (!FileUtil.exist(outFile)) {
                                    outFile.getParentFile().mkdirs();
                                }
                                try {
                                    multipartFile.transferTo(outFile);
                                    equipmentCodeList.add(relativePath.toString().replace('\\', '/'));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    // // 获取微信小程序商户自己的二维码
                    // String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxAppid +"&secret="+ wxSecret;
                    // RestTemplate restTemplate = new RestTemplate();
                    // String token = restTemplate.getForObject(tokenUrl, String.class);
                    //
                    // ObjectMapper mapper = new ObjectMapper();
                    // AuthJson authToken = mapper.readValue(token, AuthJson.class);
                    // log.info("token:"+authToken.getAccess_token());
                    // RestTemplate restTemplate2 = new RestTemplate();
                    // HttpHeaders headers = new HttpHeaders();
                    // headers.setContentType(MediaType.APPLICATION_JSON);
                    // // Wxacode wxacode = new Wxacode();
                    // // wxacode.setPath("pages/food/food?shopId="+shop.getId());
                    // // 要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。
                    // // wxacode.setEnv_version("trial");
                    // // wxacode.setWidth("280");
                    // // Gson gson = new Gson();
                    // // String json = gson.toJson(wxacode);
                    // JSONObject json = new JSONObject();
                    // json.put("path", "pages/food/food?shopId="+shop.getId());
                    // json.put("env_version", "release");
                    // json.put("width", 280);
                    // log.info("json:"+json.toString());
                    //
                    // // String json = "{\"name\":\"John\",\"age\":30}";
                    // HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
                    // String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token="+authToken.getAccess_token();
                    //
                    // byte[] bytes = restTemplate2.postForObject(url, entity, byte[].class);
                    // // 使用字节数组创建一个ByteArrayInputStream对象
                    // // 转换为MultipartFile
                    // MultipartFile multipartFile = convertToMultipartFile(bytes, "code.png", "text/plain");
                    //
                    // String fileName = multipartFile.getOriginalFilename();
                    // Path relativePath = Paths.get("shopCode"
                    //         , DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())
                    //         , CoreUtil.shortUUID() + fileName.substring(fileName.indexOf('.')));
                    // File outFile = Paths.get(wechatProperties.getUploadPath(), relativePath.toString()).toFile();
                    // if (!FileUtil.exist(outFile)) {
                    //     outFile.getParentFile().mkdirs();
                    // }
                    // try {
                    //     multipartFile.transferTo(outFile);
                    // } catch (IOException e) {
                    //     e.printStackTrace();
                    // }

                    Shop shop1 = shopsService.getById(shop.getId());
                    Shop shop2 = new Shop();
                    BeanUtils.copyProperties(shop1, shop2);
                    String paddedNumber = String.format("%06d", shop.getId());
                    String equipmentCode = equipmentCodeList.stream().map(String::valueOf).collect(Collectors.joining(","));
                    shop2.setEquipmentCode(equipmentCode);
                    // shop2.setCodeUrl(relativePath.toString().replace('\\', '/'));
                    shop2.setNumber(paddedNumber);
                    shopsService.updateById(shop2);

                    // 添加到商铺规格中
                    List<Param> params = paramService.list(null);
                    if (null != params && params.size() > 0) {
                        params.stream().forEach(item -> {
                            ShopToParam shopToParam = new ShopToParam();
                            shopToParam.setShopId(shop.getId());
                            shopToParam.setParamId(item.getId());
                            shopToParam.setTotalCount(1000);
                            shopToParam.setUseCount(0);
                            shopToParam.setName(item.getName());
                            shopToParam.setType(item.getType());
                            shopToParam.setAddPrice(item.getAddPrice());
                            shopToParam.setUseNumber(item.getUseNumber());
                            shopToParam.setCreateTime(LocalDateTime.now());
                            shopToParam.setUpdateTime(LocalDateTime.now());
                            shopToParamService.save(shopToParam);
                        });
                    }

                    return R.ok();
                } else {
                    return R.error();
                }
            }

        }

    }

    /**
     * 商户修改营业时间修改
     *
     * @return
     */
    @SneakyThrows
    @PostMapping("updateTime/{id}")
    public R updateTime(@PathVariable long id, @RequestBody Shop obj) {
        Shop shop = shopsService.getById(id);
        Shop shop1 = new Shop();
        BeanUtils.copyProperties(shop, shop1);
        shop1.setOnStartTime(obj.getOnStartTime());
        shop1.setOnEndTime(obj.getOnEndTime());

        shopsService.updateById(shop1);
        return R.ok();


    }

    /**
     * 修改
     *
     * @return
     */
    @SneakyThrows
    @PostMapping("updateData/{id}")
    public R updateData(@PathVariable long id, @RequestBody Shop obj) {
        Shop shop = shopsService.getById(id);
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getStatus, 1).eq(Shop::getName, obj.getName()).eq(Shop::getPhone, obj.getPhone()).ne(Shop::getId, id);
        List<Shop> list = shopsService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.ok().message("该商铺已添加");
        } else {
            LambdaQueryWrapper<Shop> wrapper4 = new LambdaQueryWrapper<>();
            wrapper4.eq(Shop::getStatus, 1).eq(Shop::getPhone, obj.getPhone()).ne(Shop::getId, id);
            List<Shop> list2 = shopsService.list(wrapper4);
            if (null != list2 && list2.size() > 0) {
                return R.ok().message("该手机号已被使用");
            } else {
                if (null != obj.getEquipmentIdList() && obj.getEquipmentIdList().size() > 0){
                    List<String> equipmentIdList = obj.getEquipmentIdList();
                    String equipmentId = equipmentIdList.stream().map(String::valueOf).collect(Collectors.joining(","));

                    if (!equipmentId.equals(shop.getEquipmentId())){
                        List<String> equipmentIds = new ArrayList<>();


                        List<String> equipmentCodeList = new ArrayList<>();

                        List<String> equipmentCodes = Arrays.asList(shop.getEquipmentCode().split(","));
                        // 查询设备
                        LambdaQueryWrapper<Equipment> equipmentWrappers = new LambdaQueryWrapper<>();
                        equipmentWrappers.eq(Equipment::getStatus, 1).eq(Equipment::getShopId, id);
                        List<Equipment> list1 = equipmentService.list(equipmentWrappers);
                        // equipmentService.remove(equipmentWrappers);



                        List<String> equipmentNos = list1.stream().map(e -> e.getEquipmentNo()).collect(Collectors.toList());

                        // 查找相同的值
                        Set<String> sameValues = new HashSet<>(equipmentNos);
                        sameValues.retainAll(equipmentIdList);
                        // 查找不同的值
                        Set<String> diffValues1 = new HashSet<>(equipmentNos); // 旧
                        Set<String> diffValues2 = new HashSet<>(equipmentIdList); // 新
                        diffValues1.removeAll(sameValues);
                        diffValues2.removeAll(sameValues);


                        if (diffValues1.size() > 0){ // 旧
                            List<String> oldValues = new ArrayList<>(diffValues1);
                            for (int i = 0; i < diffValues1.size(); i++) {
                                LambdaQueryWrapper<EquipmentAll> wrapper6 = new LambdaQueryWrapper<>();
                                wrapper6.eq(EquipmentAll::getStatus, 1).in(EquipmentAll::getEquipmentNo, oldValues.get(i));
                                EquipmentAll one3 = equipmentAllService.getOne(wrapper6);
                                EquipmentAll equipmentAll = new EquipmentAll();
                                BeanUtils.copyProperties(one3, equipmentAll);
                                equipmentAll.setIsBinding(2);
                                equipmentAllService.updateById(equipmentAll);

                                LambdaQueryWrapper<Equipment> equipmentWrappers2 = new LambdaQueryWrapper<>();
                                equipmentWrappers2.eq(Equipment::getStatus, 1).eq(Equipment::getShopId, id).eq(Equipment :: getEquipmentNo, oldValues.get(i));
                                equipmentService.remove(equipmentWrappers2);
                                equipmentCodeList.remove(equipmentCodes.get(i));
                            }
                        }
                        if (diffValues2.size() > 0) { // 新
                            List<String> newValues = new ArrayList<>(diffValues2);
                            for (int i = 0; i < newValues.size(); i++){
                                LambdaQueryWrapper<EquipmentAll> equipmentAllWrapper = new LambdaQueryWrapper<>();
                                equipmentAllWrapper.eq(EquipmentAll::getStatus, 1).eq(EquipmentAll::getEquipmentNo, newValues.get(i));
                                EquipmentAll one = equipmentAllService.getOne(equipmentAllWrapper);
                                EquipmentAll equipmentAll = new EquipmentAll();
                                BeanUtils.copyProperties(one, equipmentAll);
                                equipmentAll.setIsBinding(1);
                                equipmentAllService.updateById(equipmentAll);

                                Equipment equipment = new Equipment();
                                equipment.setEquipmentNo(newValues.get(i));
                                equipment.setType(one.getType());
                                equipment.setShopId(shop.getId());
                                equipment.setCreateTime(LocalDateTime.now());
                                equipment.setUpdateTime(LocalDateTime.now());
                                equipmentService.save(equipment);

                                // 根据设备型号获取微信小程序商户自己的二维码
                                String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxAppid + "&secret=" + wxSecret;
                                RestTemplate restTemplate = new RestTemplate();
                                String token = restTemplate.getForObject(tokenUrl, String.class);
                                log.info("token:" + token);
                                ObjectMapper mapper = new ObjectMapper();
                                AuthJson authToken = null;
                                try {
                                    authToken = mapper.readValue(token, AuthJson.class);
                                    RestTemplate restTemplate2 = new RestTemplate();
                                    HttpHeaders headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                    JSONObject json = new JSONObject();
                                    json.put("path", "pages/food/food?equipmentId=" + equipment.getId()); // 商户设备表id
                                    // 要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。
                                    json.put("env_version", "release");
                                    json.put("width", 280);
                                    log.info("json:" + json.toString());
                                    HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
                                    String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token=" + authToken.getAccess_token();

                                    byte[] bytes = restTemplate2.postForObject(url, entity, byte[].class);
                                    // 使用字节数组创建一个ByteArrayInputStream对象
                                    // 转换为MultipartFile
                                    MultipartFile multipartFile = convertToMultipartFile(bytes, "code.png", "text/plain");

                                    String fileName = multipartFile.getOriginalFilename();
                                    Path relativePath = Paths.get("equipmentCode"
                                            , DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())
                                            , CoreUtil.shortUUID() + fileName.substring(fileName.indexOf('.')));
                                    File outFile = Paths.get(wechatProperties.getUploadPath(), relativePath.toString()).toFile();
                                    if (!FileUtil.exist(outFile)) {
                                        outFile.getParentFile().mkdirs();
                                    }
                                    try {
                                        multipartFile.transferTo(outFile);
                                        equipmentCodeList.add(relativePath.toString().replace('\\', '/'));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        shop.setEquipmentId(equipmentId);
                        String equipmentCode = equipmentCodeList.stream().map(String::valueOf).collect(Collectors.joining(","));
                        shop.setEquipmentCode(equipmentCode);
                    }
                }





                // // 获取微信小程序商户自己的二维码
                // String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxAppid +"&secret="+ wxSecret;
                // RestTemplate restTemplate = new RestTemplate();
                // String token = restTemplate.getForObject(tokenUrl, String.class);
                // log.info("token:"+token);
                // ObjectMapper mapper = new ObjectMapper();
                // AuthJson authToken = mapper.readValue(token, AuthJson.class);
                // RestTemplate restTemplate2 = new RestTemplate();
                // HttpHeaders headers = new HttpHeaders();
                // headers.setContentType(MediaType.APPLICATION_JSON);
                // // Wxacode wxacode = new Wxacode();
                // // wxacode.setPath("pages/food/food?shopId="+shop.getId());
                // // 要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。
                // // wxacode.setEnv_version("develop");
                // // wxacode.setWidth("280");
                // // Gson gson = new Gson();
                // // String json = gson.toJson(wxacode);
                // JSONObject json = new JSONObject();
                // json.put("path", "pages/food/food?shopId="+shop.getId());
                // json.put("env_version", "release");
                // json.put("width", 280);
                // log.info("json:"+json.toString());
                // // String json = "{\"name\":\"John\",\"age\":30}";
                // HttpEntity<String> entity = new HttpEntity<String>(json.toString(), headers);
                // String url = "https://api.weixin.qq.com/wxa/getwxacode?access_token="+authToken.getAccess_token();
                //
                // byte[] bytes = restTemplate2.postForObject(url, entity, byte[].class);
                // // 使用字节数组创建一个ByteArrayInputStream对象
                // // 转换为MultipartFile
                // MultipartFile multipartFile = convertToMultipartFile(bytes, "code.png", "text/plain");
                //
                // String fileName = multipartFile.getOriginalFilename();
                // Path relativePath = Paths.get("shopCode"
                //         , DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())
                //         , CoreUtil.shortUUID() + fileName.substring(fileName.indexOf('.')));
                // File outFile = Paths.get(wechatProperties.getUploadPath(), relativePath.toString()).toFile();
                // if (!FileUtil.exist(outFile)) {
                //     outFile.getParentFile().mkdirs();
                // }
                // try {
                //     multipartFile.transferTo(outFile);
                // } catch (IOException e) {
                //     e.printStackTrace();
                // }
                List<Long> goodRangeList = obj.getGoodRangeList();
                String goodRange = goodRangeList.stream().map(String::valueOf).collect(Collectors.joining(","));
                shop.setGoodRange(goodRange);
                List<Integer> platformType = obj.getPlatformType();
                String type = platformType.stream().map(String::valueOf).collect(Collectors.joining(","));
                shop.setType(type);

                if (null != obj.getTemplateId()){
                    Template template = templateService.getById(obj.getTemplateId());
                    shop.setTemplateId(obj.getTemplateId());
                    shop.setColor(template.getColorName());
                }

                // shop.setCodeUrl(relativePath.toString().replace('\\', '/'));
                shop.setIsUseDevice(obj.getIsUseDevice());
                shop.setName(obj.getName());
                shop.setLogo(obj.getLogo());
                shop.setUsername(obj.getUsername());
                shop.setProvince(obj.getProvince());
                shop.setCity(obj.getCity());
                shop.setIsUse(obj.getIsUse());
                shop.setDivideAccounts(obj.getDivideAccounts());
                shop.setProportion(obj.getProportion());
                shop.setArea(obj.getArea());
                shop.setPhone(obj.getPhone());
                shop.setAgreement(obj.getAgreement());
                shop.setBrandId(obj.getBrandId());
                shop.setEleShopId(obj.getEleShopId());
                shop.setRedirectUri(obj.getRedirectUri());
                shop.setSector(obj.getSector());
                shop.setTurnoverM(obj.getTurnoverM());
                shop.setLat(obj.getLat());
                shop.setLng(obj.getLng());
                shop.setUpdateTime(LocalDateTime.now());
                boolean save = shopsService.updateById(shop);
                if (save) {
                    return R.ok();
                } else {
                    return R.error();
                }
            }

        }

    }


    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        boolean b = shopsService.removeById(id);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @PostMapping("uploadAgreement")
    public R uploadAgreement(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path relativePath = Paths.get("agreement"
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

