package com.java1234.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java1234.entity.VoteItem;
import com.java1234.mapper.VoteItemMapper;
import com.java1234.service.IVoteItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 投票选项Service实现类
 */
@Service("voteItemService")
public class IVoteItemServiceImpl extends ServiceImpl<VoteItemMapper,VoteItem> implements IVoteItemService {

    @Autowired
    private VoteItemMapper voteItemMapper;

}
