package com.java1234.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java1234.entity.Vote;
import org.springframework.stereotype.Repository;

/**
 * 投票Mapper接口
 */
@Repository
public interface VoteMapper extends BaseMapper<Vote> {
}
