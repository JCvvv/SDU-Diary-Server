package com.sdu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sdu.entity.Vote;
import org.springframework.stereotype.Repository;

/**
 * 投票Mapper接口
 */
@Repository
public interface VoteMapper extends BaseMapper<Vote> {
}
