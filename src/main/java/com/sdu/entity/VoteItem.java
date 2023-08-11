package com.sdu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 投票选项实体
 */
@TableName(value = "t_vote_item")
@Data
public class VoteItem {

    private Integer id; // 编号

    private Integer voteId; // 投票ID

    private String name; // 投票选项名称


}
