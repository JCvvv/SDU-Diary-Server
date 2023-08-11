package com.sdu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdu.entity.VoteItem;
import com.sdu.mapper.VoteItemMapper;
import com.sdu.service.IVoteItemService;
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
