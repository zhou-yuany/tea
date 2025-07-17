package com.tea.server.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.controller.converter.CustomImageModifyHandler;
import com.tea.server.entity.*;
import com.tea.server.entity.excel.FlowData;
import com.tea.server.entity.query.AccountQuery;
import com.tea.server.service.*;
import com.tea.server.utils.R;
import com.tea.server.utils.excel.ExcelCellWidthStyleStrategy;
import com.tea.server.utils.excel.StyleUtils;
import eleme.openapi.sdk.api.service.ShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 订单流水表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-04-15
 */
@RestController
@RequestMapping("ordersFlow")
public class OrdersFlowController {

    @Autowired
    private OrdersFlowService ordersFlowService;

    @Autowired
    private OrderParamService orderParamService;

    @Autowired
    private ShopsService shopService;

    @Autowired
    private WithdrawalRecordService withdrawalRecordService;

    @Autowired
    private InterfaceLogService interfaceLogService;

    /**
     * 商户竖屏分账流水列表
     */
    @PostMapping("getList/{adminId}")
    public R getList(@PathVariable Long adminId, @RequestBody AccountQuery accountQuery){

        // 构建条件
        LambdaQueryWrapper<OrdersFlow> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(OrdersFlow :: getStatus, 1).eq(OrdersFlow :: getAdminId, adminId).eq(OrdersFlow :: getPayStatus, 1);

        String keyword = accountQuery.getKeyword();
        String begin = accountQuery.getBegin();
        String end = accountQuery.getEnd();
        Integer type = accountQuery.getType();
        if (null != keyword && !keyword.equals("")){
            wrapper.like(OrdersFlow :: getSerialNum, keyword);
        }
        if (null != type && type > 0){
            wrapper.eq(OrdersFlow :: getType, type);
        }
        if (null != begin && !begin.equals("")){
            wrapper.ge(OrdersFlow :: getCreateTime, begin);
        }
        if (null != end && !end.equals("")){
            wrapper.le(OrdersFlow :: getCreateTime, end);
        }

        wrapper.orderByDesc(OrdersFlow :: getId);
        List<OrdersFlow> list = ordersFlowService.list(wrapper);

        LambdaQueryWrapper<OrdersFlow> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(OrdersFlow :: getStatus, 1).eq(OrdersFlow :: getType, 1).eq(OrdersFlow :: getAdminId, adminId).eq(OrdersFlow :: getPayStatus, 1);
        List<OrdersFlow> addList = ordersFlowService.list(wrapper2);

        LambdaQueryWrapper<OrdersFlow> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(OrdersFlow :: getStatus, 1).eq(OrdersFlow :: getType, 2).eq(OrdersFlow :: getAdminId, adminId).eq(OrdersFlow :: getPayStatus, 1);
        List<OrdersFlow> subList = ordersFlowService.list(wrapper3);


        BigDecimal sumPrice = Optional.ofNullable(addList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(x-> x.getShopDistributionPrice() != null) // 避免空指针
                .map(OrdersFlow::getShopDistributionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal subPrice = Optional.ofNullable(subList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(x-> x.getShopDistributionPrice() != null) // 避免空指针
                .map(OrdersFlow::getShopDistributionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal banlance = sumPrice.subtract(subPrice);

        return R.ok().data("list", list).data("banlance", banlance);
    }

    /**
     * 根据订单流水查询商品做法
     */
    @PostMapping("getMakeWay")
    public R getMakeWay(@RequestBody OrdersFlow ordersFlow){
        Long orderId = ordersFlow.getOrderId();
        LambdaQueryWrapper<OrderParam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderParam::getStatus, 1).eq(OrderParam::getOrderId, orderId);
        List<OrderParam> list = orderParamService.list(wrapper);
        return R.ok().data("list", list);

    }


    /**
     * 总后台分账流水列表
     */
    @PostMapping("getListAdmin/{page}/{limit}")
    public R getList(@PathVariable Long page, @PathVariable Long limit, @RequestBody AccountQuery accountQuery){

        // 创建page对象
        Page<OrdersFlow> flowPage = new Page<>(page, limit);
        // 构建条件
        LambdaQueryWrapper<OrdersFlow> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(OrdersFlow :: getStatus, 1).eq(OrdersFlow :: getPayStatus, 1);

        String keyword = accountQuery.getKeyword();
        String begin = accountQuery.getBegin();
        String end = accountQuery.getEnd();
        Integer type = accountQuery.getType();
        if (null != keyword && !keyword.equals("")){
            wrapper.like(OrdersFlow :: getSerialNum, keyword);
        }
        if (null != type && type > 0){
            wrapper.eq(OrdersFlow :: getType, type);
        }
        if (null != begin && !begin.equals("")){
            wrapper.ge(OrdersFlow :: getCreateTime, begin);
        }
        if (null != end && !end.equals("")){
            wrapper.le(OrdersFlow :: getCreateTime, end);
        }

        wrapper.orderByDesc(OrdersFlow :: getId);
        ordersFlowService.page(flowPage, wrapper);
        long total = flowPage.getTotal();
        List<OrdersFlow> records = flowPage.getRecords();


        LambdaQueryWrapper<OrdersFlow> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(OrdersFlow :: getStatus, 1).eq(OrdersFlow :: getType, 1);
        List<OrdersFlow> addList = ordersFlowService.list(wrapper2);

        LambdaQueryWrapper<OrdersFlow> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(OrdersFlow :: getStatus, 1).eq(OrdersFlow :: getType, 2);
        List<OrdersFlow> subList = ordersFlowService.list(wrapper3);


        BigDecimal sumPrice = Optional.ofNullable(addList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(x-> x.getPlatDistributionPrice() != null) // 避免空指针
                .map(OrdersFlow::getPlatDistributionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal subPrice = Optional.ofNullable(subList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(x-> x.getPlatDistributionPrice() != null) // 避免空指针
                .map(OrdersFlow::getPlatDistributionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPrice = sumPrice.subtract(subPrice);

        return R.ok().data("total", total).data("rows", records).data("totalPrice", totalPrice);
    }


    /**
     * 商户分账流水列表
     */
    @PostMapping("getListShop/{page}/{limit}/{shopId}")
    public R getList(@PathVariable Long page, @PathVariable Long limit, @PathVariable Long shopId, @RequestBody AccountQuery accountQuery){

        // 创建page对象
        Page<OrdersFlow> flowPage = new Page<>(page, limit);
        // 构建条件
        LambdaQueryWrapper<OrdersFlow> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(OrdersFlow :: getStatus, 1).eq(OrdersFlow :: getShopId, shopId).eq(OrdersFlow :: getPayStatus, 1);

        String keyword = accountQuery.getKeyword();
        String begin = accountQuery.getBegin();
        String end = accountQuery.getEnd();
        Integer type = accountQuery.getType();
        if (null != keyword && !keyword.equals("")){
            wrapper.like(OrdersFlow :: getSerialNum, keyword);
        }
        if (null != type && type > 0){
            wrapper.eq(OrdersFlow :: getType, type);
        }
        if (null != begin && !begin.equals("")){
            wrapper.ge(OrdersFlow :: getCreateTime, begin);
        }
        if (null != end && !end.equals("")){
            wrapper.le(OrdersFlow :: getCreateTime, end);
        }

        wrapper.orderByDesc(OrdersFlow :: getId);
        ordersFlowService.page(flowPage, wrapper);
        long total = flowPage.getTotal();
        List<OrdersFlow> records = flowPage.getRecords();


        LambdaQueryWrapper<OrdersFlow> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(OrdersFlow :: getStatus, 1).eq(OrdersFlow :: getType, 1).eq(OrdersFlow :: getShopId, shopId);
        List<OrdersFlow> addList = ordersFlowService.list(wrapper2);

        LambdaQueryWrapper<OrdersFlow> wrapper3 = new LambdaQueryWrapper<>();
        wrapper3.eq(OrdersFlow :: getStatus, 1).eq(OrdersFlow :: getType, 2).eq(OrdersFlow :: getShopId, shopId);
        List<OrdersFlow> subList = ordersFlowService.list(wrapper3);


        BigDecimal sumPrice = Optional.ofNullable(addList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(x-> x.getShopDistributionPrice() != null) // 避免空指针
                .map(OrdersFlow::getShopDistributionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal subPrice = Optional.ofNullable(subList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(x-> x.getShopDistributionPrice() != null) // 避免空指针
                .map(OrdersFlow::getShopDistributionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPrice = sumPrice.subtract(subPrice);

        Shop shop = shopService.getById(shopId);
        BigDecimal balance = shop.getBalance();

        return R.ok().data("total", total).data("rows", records).data("totalPrice", totalPrice).data("balance", balance);
    }

    /**
     * 商户提现
     */
    @PostMapping("toWithdrawal/{shopId}")
    public R toWithdrawal(@PathVariable Long shopId, @RequestParam BigDecimal price){
        WithdrawalRecord withdrawalRecord = new WithdrawalRecord();
        withdrawalRecord.setCreateTime(LocalDateTime.now());
        withdrawalRecord.setCreateTime(LocalDateTime.now());
        withdrawalRecord.setShopId(shopId);
        withdrawalRecord.setPrice(price);
        withdrawalRecordService.save(withdrawalRecord);


        // 商户扣除金额
        Shop shop = shopService.getById(shopId);
        Shop shop1 = new Shop();
        BeanUtils.copyProperties(shop, shop1);
        BigDecimal zero = BigDecimal.ZERO;

        BigDecimal priceP = shop.getBalance().subtract(price);
        // 比较 bd 和 0
        int comparisonResult = price.compareTo(zero);
        shop1.setBalance(comparisonResult > 0 ? priceP : zero);
        shopService.updateById(shop1);

        BigDecimal balance = shop.getBalance().subtract(withdrawalRecord.getPrice());

        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setTitle("商户申请提现");
        interfaceLog.setMethodName("startOrders");
        String content = "商户"+shop.getName()+"申请提现，"+"提现金额为："+price+"元";
        interfaceLog.setContent(content);
        interfaceLog.setShopId(shopId);
        interfaceLog.setTypeStatus(0);
        interfaceLog.setCreateTime(LocalDateTime.now());
        interfaceLog.setUpdateTime(LocalDateTime.now());
        interfaceLogService.save(interfaceLog);

        return R.ok().data("balance", balance);
    }

    /**
     * 商户账单导出
     * @param response
     * @param shopId
     * @throws ParseException
     * @throws UnsupportedEncodingException
     */
    @GetMapping("download/{shopId}")
    public void download (HttpServletResponse response,@PathVariable Long shopId, @RequestParam(required = false) Integer type, @RequestParam(required = false) String begin, @RequestParam(required = false) String end) throws ParseException, UnsupportedEncodingException {

        List<FlowData> list = ordersFlowService.excelFlows(shopId, type, begin, end);

        String outFileName = "账单.xlsx";
        String ENCODING="UTF-8";
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 设置单元格样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(StyleUtils.getHeadStyle(), StyleUtils.getContentStyle());
        // 列宽策略设置
        ExcelCellWidthStyleStrategy widthStyleStrategy = new ExcelCellWidthStyleStrategy();
        ExcelWriter writer = EasyExcel.write(out)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .registerWriteHandler(widthStyleStrategy)
                .registerWriteHandler(new CustomImageModifyHandler())
                .build();
        WriteSheet sheet = EasyExcel.writerSheet(0, "账单").head(FlowData.class).build();
        writer.write(list, sheet);

        //通知浏览器以附件的形式下载处理，设置返回头要注意文件名有中文
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + outFileName + ";filename*=utf-8''"
                + URLEncoder.encode(outFileName, ENCODING));
        writer.finish();
        response.setCharacterEncoding("utf-8");
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

