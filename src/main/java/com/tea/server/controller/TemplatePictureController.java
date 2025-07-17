package com.tea.server.controller;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tea.server.config.WeChatProperties;
import com.tea.server.entity.Template;
import com.tea.server.entity.TemplateColor;
import com.tea.server.entity.TemplatePicture;
import com.tea.server.entity.data.TemplateColorData;
import com.tea.server.entity.data.TemplatePictureData;
import com.tea.server.service.TemplateColorService;
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
import java.time.LocalDateTime;

/**
 * <p>
 * 模板配图 前端控制器
 * </p>
 *
 * @author zyy
 * @since 2024-09-23
 */
@RestController
@RequestMapping("templatePicture")
public class TemplatePictureController {
    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplatePictureService templatePictureService;

    @Autowired
    private WeChatProperties wechatProperties;

    /**
     * 上传模板图集
     *
     * @param file
     * @return
     */
    @PostMapping("uploadPics/{color}")
    public R uploadPics(@PathVariable String color, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path relativePath = Paths.get(color
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

    @GetMapping("getInfo/{templateId}")
    public R getInfo(@PathVariable Long templateId) {
        Template templateInfo = templateService.getById(templateId);

        LambdaQueryWrapper<TemplatePicture> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TemplatePicture :: getTemplateId, templateId).eq(TemplatePicture :: getStatus, 1);
        TemplatePicture info = templatePictureService.getOne(wrapper);

        TemplatePictureData templatePictureData = new TemplatePictureData();
        templatePictureData.setColorName(templateInfo.getColorName());
        templatePictureData.setName(templateInfo.getName());
        templatePictureData.setTemplateId(templateId);
        if (null != info){
            BeanUtils.copyProperties(info, templatePictureData);
        }

        return R.ok().data("info", templatePictureData);
    }

    /**
     * 添加模板
     *
     * @return
     */
    @PostMapping("insertData")
    public R insertData(@RequestBody TemplatePictureData obj) {
        TemplatePicture templatePicture = new TemplatePicture();
        BeanUtils.copyProperties(obj, templatePicture);
        templatePicture.setCreateTime(LocalDateTime.now());
        templatePicture.setUpdateTime(LocalDateTime.now());
        boolean save = templatePictureService.save(templatePicture);
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
    public R updateData(@RequestBody TemplatePictureData obj) {
        TemplatePictureData templatePictureData = new TemplatePictureData();
        BeanUtils.copyProperties(obj, templatePictureData);
        templatePictureData.setUpdateTime(LocalDateTime.now());
        boolean save = templatePictureService.updateById(templatePictureData);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }


    }

}

