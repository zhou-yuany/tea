package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.Template;
import com.tea.server.entity.TemplateColor;
import com.tea.server.entity.TemplatePicture;
import com.tea.server.service.TemplateColorService;
import com.tea.server.service.TemplatePictureService;
import com.tea.server.service.TemplateService;
import com.tea.server.utils.R;
import org.junit.AfterClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 模板名称表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2024-09-23
 */
@RestController
@RequestMapping("template")
public class TemplateController {
    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplatePictureService templatePictureService;

    @Autowired
    private TemplateColorService templateColorService;



    @GetMapping("getTemplateColor")
    public R getTemplateColor(@RequestParam String color) {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Template :: getStatus, 1).eq(Template :: getColorName, color);
        Template info = templateService.getOne(wrapper);
        return R.ok().data("info", info);
    }

    @GetMapping("getTemplateInfo")
    public R getTemplateInfo(@RequestParam String color) {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Template :: getStatus, 1).eq(Template :: getColorName, color);
        Template info = templateService.getOne(wrapper);


        LambdaQueryWrapper<TemplatePicture> pictureWrapper = new LambdaQueryWrapper<>();
        pictureWrapper.eq(TemplatePicture :: getStatus, 1).eq(TemplatePicture :: getTemplateId, info.getId());
        TemplatePicture pictureInfo = templatePictureService.getOne(pictureWrapper);

        return R.ok().data("info", info).data("pictureInfo", pictureInfo);
    }

}

