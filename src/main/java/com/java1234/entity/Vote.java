package com.java1234.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 投票实体
 * @author java1234_小锋 （公众号：java1234）
 * @site www.java1234.vip
 * @company 南通小锋网络科技有限公司
 */
@TableName("t_vote")
@Data
public class Vote {

    private Integer id; // 编号

    private String title; // 标题

    private String explanation; // 投票说明

    private String coverImage; // 封面图片

    @JsonSerialize(using=CustomDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date voteEndTime; // 投票结束时间

    private String openid; // 投票发起人openid

    @TableField(select=false,exist = false)
    private List<VoteItem> voteItemList;

    @TableField(select=false,exist = false)
    private WxUserInfo wxUserInfo;

    private Integer type=1; // 1 文字投票  2 图片投票

}
