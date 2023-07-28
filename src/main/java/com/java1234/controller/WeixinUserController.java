package com.java1234.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.java1234.constant.JwtConstant;
import com.java1234.entity.R;
import com.java1234.entity.WxUserInfo;
import com.java1234.properties.WeixinProperties;
import com.java1234.service.IWxUserInfoService;
import com.java1234.util.DateUtil;
import com.java1234.util.HttpClientUtil;
import com.java1234.util.JwtUtils;
import com.java1234.util.StringUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/user")
@RestController
public class WeixinUserController {

    @Autowired
    private IWxUserInfoService wxUserInfoService;

    @Autowired
    private WeixinProperties weixinProperties;

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Value("${userImagesFilePath}")
    private String userImagesFilePath;

    /**
     * 测试
     * @return
     */
    @RequestMapping("/test")
    public R test(){
        List<WxUserInfo> list = wxUserInfoService.list();
        HashMap<String,Object> result=new HashMap<>();
        result.put("list",list);
        return R.ok(result);
    }

    /**
     * 微信用户登录
     * @param wxUserInfo
     * @return
     */
    @RequestMapping("/wxLogin")
    public R wxLogin(@RequestBody WxUserInfo wxUserInfo){
        String jscode2sessionUrl=weixinProperties.getJscode2sessionUrl()+"?appid="+weixinProperties.getAppid()+"&secret="+weixinProperties.getSecret()+"&js_code="+wxUserInfo.getCode()+"&grant_type=authorization_code";
        System.out.println(jscode2sessionUrl);
        String result = httpClientUtil.sendHttpGet(jscode2sessionUrl);
        System.out.println(result);
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.get("openid").toString();
        System.out.println(openid);
        // 插入用户到数据库 假如 用户不存在 我们添加用户  存在的话 更新用户信息
        WxUserInfo resultWxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("openid", openid));
        if(resultWxUserInfo==null){
            System.out.println("不存在，插入用户信息");
            wxUserInfo.setOpenid(openid);
            wxUserInfo.setRegisterDate(new Date());
            wxUserInfo.setLastLoginDate(new Date());
            wxUserInfoService.save(wxUserInfo);
        }else{
            System.out.println("存在，更新用户信息");
            resultWxUserInfo.setLastLoginDate(new Date());
            wxUserInfoService.updateById(resultWxUserInfo);
        }
        if(resultWxUserInfo!=null && resultWxUserInfo.getStatus().equals("1")){ // 被警用
            return R.error(400,"用户被封禁，具体请联系管理员");
        }else{
            // 利用Jwt生成token返回到前端
            String token = JwtUtils.createJWT(openid, wxUserInfo.getNickName(), JwtConstant.JWT_TTL);
            Map<String,Object> resultMap=new HashMap<>();
            resultMap.put("token",token);
            resultMap.put("openid",openid);
            return R.ok(resultMap);
        }
    }

    /**
     * 获取当前用户信息
     * @param token
     * @return
     */
    @RequestMapping("/getUserInfo")
    public R getUserInfo(@RequestHeader String token){
        System.out.println("token="+token);
        Claims claims = JwtUtils.validateJWT(token).getClaims();
        System.out.println("openid="+claims.getId());
        WxUserInfo currentUser = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("openid", claims.getId()));
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("currentUser",currentUser);
        return R.ok(resultMap);
    }

    /**
     * 更新用户昵称
     * @param wxUserInfo
     * @param token
     * @return
     */
    @RequestMapping("/updateNickName")
    public R updateNickName(@RequestBody WxUserInfo wxUserInfo, @RequestHeader String token){
        System.out.println(wxUserInfo);
        if(StringUtil.isNotEmpty(wxUserInfo.getNickName())){
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            wxUserInfoService.update(new UpdateWrapper<WxUserInfo>().eq("openid",claims.getId()).set("nick_name",wxUserInfo.getNickName()));
        }
        return R.ok();
    }

    /**
     * 上传用户头像图片
     * @param userImage
     * @param token
     * @return
     */
    @RequestMapping("/updateUserImage")
    public Map<String,Object> updateUserImage(MultipartFile userImage,@RequestHeader String token)throws  Exception{
        System.out.println("filename:"+userImage.getName());
        Map<String,Object> resultMap=new HashMap<>();
        if(!userImage.isEmpty()){
            String originalFilename = userImage.getOriginalFilename();
            String suffixName=originalFilename.substring(originalFilename.lastIndexOf(".")); // 获取文件名后缀 .png .jpg
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;
            FileUtils.copyInputStreamToFile(userImage.getInputStream(),new File(userImagesFilePath+newFileName));
            resultMap.put("userImageFileName",newFileName);
            resultMap.put("code",0);
            resultMap.put("msg","上传成功");
            // 更新到数据库
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            UpdateWrapper<WxUserInfo> updateWrapper=new UpdateWrapper<>();
            updateWrapper
                    .eq("openid",claims.getId())
                    .set("avatar_url",newFileName);
            wxUserInfoService.update(updateWrapper);
        }
        return resultMap;
    }
}
