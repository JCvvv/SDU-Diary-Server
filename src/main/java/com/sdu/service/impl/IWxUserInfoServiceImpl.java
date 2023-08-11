package com.sdu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdu.entity.WxUserInfo;
import com.sdu.mapper.WxUserInfoMapper;
import com.sdu.service.IWxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信用户Service实现类
 */
@Service("wxUserInfoService")
public class IWxUserInfoServiceImpl extends ServiceImpl<WxUserInfoMapper, WxUserInfo> implements IWxUserInfoService {

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;
}
