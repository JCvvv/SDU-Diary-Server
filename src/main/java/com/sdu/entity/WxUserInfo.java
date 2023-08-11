package com.sdu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信用户信息实体
 */
@TableName(value = "t_wxuserinfo")
@Data
public class WxUserInfo implements Serializable {

    private Integer id; // 用户编号

    private String openid; // 用户唯一标识

    private String nickName="微信用户"; // 用户昵称

    private String avatarUrl="default.png"; // 用户头像图片的 URL

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date registerDate; // 注册日期

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    private Date lastLoginDate; // 最后登录日期

    private String status="0"; // 用户状态 状态 0 可用 1 封禁

    @TableField(select = false,exist = false)
    private String code; // 微信用户code 前端传来的


}
