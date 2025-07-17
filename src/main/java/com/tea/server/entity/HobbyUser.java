package com.tea.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.tea.server.core.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户兴趣爱好填写表
 * </p>
 *
 * @author zyy
 * @since 2024-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="HobbyUser对象", description="用户兴趣爱好填写表")
public class HobbyUser extends BaseBean implements Serializable {


    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "兴趣爱好回答")
    private String ans1;

    @ApiModelProperty(value = "兴趣爱好其他说明")
    private String other1;

    @ApiModelProperty(value = "娱乐活动回答")
    private String ans2;

    @ApiModelProperty(value = "娱乐活动其他说明")
    private String other2;

    @ApiModelProperty(value = "阅读偏好回答")
    private String ans3;

    @ApiModelProperty(value = "阅读偏好其他说明")
    private String other3;

    @ApiModelProperty(value = "运动偏好回答")
    private String ans4;

    @ApiModelProperty(value = "运动偏好其他说明")
    private String other4;

    @ApiModelProperty(value = "旅行偏好回答")
    private String ans5;

    @ApiModelProperty(value = "旅行偏好其他说明")
    private String other5;

    @ApiModelProperty(value = "音乐偏好回答")
    private String ans6;

    @ApiModelProperty(value = "音乐偏好其他说明")
    private String other6;

    @ApiModelProperty(value = "影视偏好回答")
    private String ans7;

    @ApiModelProperty(value = "影视偏好其他说明")
    private String other7;

    @ApiModelProperty(value = "游戏偏好回答")
    private String ans8;

    @ApiModelProperty(value = "游戏偏好其他说明")
    private String other8;

    @ApiModelProperty(value = "美食偏好回答")
    private String ans9;

    @ApiModelProperty(value = "美食偏好其他说明")
    private String other9;

    @ApiModelProperty(value = "社交活动回答")
    private String ans10;

    @ApiModelProperty(value = "社交活动其他说明")
    private String other10;


}
