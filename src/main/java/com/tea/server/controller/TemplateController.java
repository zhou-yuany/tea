package com.tea.server.controller;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tea.server.config.WeChatProperties;
import com.tea.server.entity.Brand;
import com.tea.server.entity.Template;
import com.tea.server.entity.TemplatePicture;
import com.tea.server.entity.data.TemplatePictureData;
import com.tea.server.entity.query.BrandQuery;
import com.tea.server.service.TemplatePictureService;
import com.tea.server.service.TemplateService;
import com.tea.server.utils.CoreUtil;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

/**
 * <p>
 * 模板名称表 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-09-20
 */
@RestController
@RequestMapping("template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplatePictureService templatePictureService;

    @Autowired
    private WeChatProperties wechatProperties;

    @GetMapping("getTemplateList")
    public R getTemplateList() {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Template :: getStatus, 1);
        List<Template> list = templateService.list(wrapper);

        List<TemplatePictureData> allList = new ArrayList<>();

        if(list.size() > 0){
            list.forEach(item -> {
                TemplatePictureData templatePictureData = new TemplatePictureData();

                LambdaQueryWrapper<TemplatePicture> pictureWrapper = new LambdaQueryWrapper<>();
                pictureWrapper.eq(TemplatePicture :: getStatus, 1).eq(TemplatePicture :: getTemplateId, item.getId());
                TemplatePicture templatePicture = templatePictureService.getOne(pictureWrapper);

                if (null != templatePicture){
                    templatePictureData.setTemplateId(item.getId());
                    templatePictureData.setName(item.getName());
                    templatePictureData.setColorName(item.getColorName());
                    allList.add(templatePictureData);
                }

            });
        }
        return R.ok().data("list", allList);
    }

    /**
     * 上传模板图集
     *
     * @param file
     * @return
     */
    @PostMapping("uploadPics")
    public R uploadPics(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path relativePath = Paths.get("template"
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
     * 列表 分页
     */
    @PostMapping("getList/{page}/{limit}")
    public R getList(@PathVariable long page, @PathVariable long limit, @RequestBody Template templateQuery) {
        // 创建page对象
        Page<Template> brandPage = new Page<>(page, limit);

        // 构建条件
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Template::getStatus, 1);

        String name = templateQuery.getName();
        String colorName = templateQuery.getColorName();

        if (null != name && !name.equals("")) {
            wrapper.like(Template::getName, name);
        }
        if (null != colorName && !colorName.equals("")) {
            wrapper.like(Template::getColorName, colorName);
        }
        wrapper.orderByDesc(Template::getId);

        templateService.page(brandPage, wrapper);

        List<Template> records = brandPage.getRecords();
        long total = brandPage.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 添加模板
     *
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody Template obj) {
        // 查询设备是否已经添加过
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Template::getStatus, 1).eq(Template::getName, obj.getName()).eq(Template::getColorName, obj.getColorName());
        List<Template> list = templateService.list(wrapper);
        if (null != list && list.size() > 0) {
            return R.error().message("该模板颜色已添加过！");
        } else {
            Template template = new Template();
            BeanUtils.copyProperties(obj, template);
            template.setCreateTime(LocalDateTime.now());
            template.setUpdateTime(LocalDateTime.now());
            boolean save = templateService.save(template);
            if (save) {
                return R.ok();
            } else {
                return R.error();
            }
        }

    }

    /**
     * 修改模板
     *
     * @return
     */
    @PostMapping("updateData")
    public R updateData(@RequestBody Template obj) {
        Template template = new Template();
        BeanUtils.copyProperties(obj, template);
        template.setUpdateTime(LocalDateTime.now());
        boolean save = templateService.updateById(template);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }


    }

    @GetMapping("getInfo/{id}")
    public R getInfo(@PathVariable Long id) {
        Template info = templateService.getById(id);
        return R.ok().data("info", info);
    }



    @DeleteMapping("{id}")
    public R remove(@PathVariable("id") Long id) {
        boolean b = templateService.removeById(id);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }


}

