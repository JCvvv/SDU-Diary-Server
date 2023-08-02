package com.java1234.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 投票选项实体
 */
@TableName("t_vote_item")
@Data
public class VoteItem {

    private Integer id; // 编号

    private Integer voteId; // 投票ID

    private String name; // 投票选项名称


}
