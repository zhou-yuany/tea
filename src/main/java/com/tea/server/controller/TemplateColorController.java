package com.tea.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.entity.Template;
import com.tea.server.entity.TemplateColor;
import com.tea.server.entity.data.TemplateColorData;
import com.tea.server.service.TemplateColorService;
import com.tea.server.service.TemplateService;
import com.tea.server.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 模板配色 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-09-23
 */
@RestController
@RequestMapping("templateColor")
public class TemplateColorController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplateColorService templateColorService;

    @GetMapping("getInfo/{templateId}")
    public R getInfo(@PathVariable Long templateId) {
        Template templateInfo = templateService.getById(templateId);

        LambdaQueryWrapper<TemplateColor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TemplateColor :: getTemplateId, templateId).eq(TemplateColor :: getStatus, 1);
        TemplateColor info = templateColorService.getOne(wrapper);

        TemplateColorData templateColorData = new TemplateColorData();
        templateColorData.setColorName(templateInfo.getColorName());
        templateColorData.setName(templateInfo.getName());
        templateColorData.setTemplateId(templateId);
        if (null != info){
            BeanUtils.copyProperties(info, templateColorData);
        }

        return R.ok().data("info", templateColorData);
    }

    /**
     * 添加模板
     *
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody TemplateColorData obj) {
        TemplateColor templateColor = new TemplateColor();
        BeanUtils.copyProperties(obj, templateColor);
        templateColor.setCreateTime(LocalDateTime.now());
        templateColor.setUpdateTime(LocalDateTime.now());
        boolean save = templateColorService.save(templateColor);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }


    }

    /**
     * 修改模板
     *
     * @return
     */
    @PostMapping("updateData")
    public R updateData(@RequestBody TemplateColorData obj) {
        TemplateColor templateColor = new TemplateColor();
        BeanUtils.copyProperties(obj, templateColor);
        templateColor.setUpdateTime(LocalDateTime.now());
        boolean save = templateColorService.updateById(templateColor);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }


    }

}

