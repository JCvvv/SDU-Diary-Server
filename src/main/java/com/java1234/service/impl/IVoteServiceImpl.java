package com.java1234.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java1234.entity.Vote;
import com.java1234.mapper.VoteMapper;
import com.java1234.service.IVoteService;
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
