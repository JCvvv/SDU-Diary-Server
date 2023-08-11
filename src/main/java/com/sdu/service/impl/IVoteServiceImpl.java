package com.sdu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdu.entity.Vote;
import com.sdu.mapper.VoteMapper;
import com.sdu.service.IVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 投票Service实现类
 */
@Service("voteService")
public class IVoteServiceImpl extends ServiceImpl<VoteMapper, Vote> implements IVoteService {

    @Autowired
    private VoteMapper voteMapper;
}
