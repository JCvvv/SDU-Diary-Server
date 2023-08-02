package com.java1234.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java1234.entity.R;
import com.java1234.entity.Vote;
import com.java1234.entity.VoteItem;
import com.java1234.service.IVoteItemService;
import com.java1234.service.IVoteService;
import com.java1234.util.DateUtil;
import com.java1234.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投票Controller控制器
 */
@RestController
@RequestMapping("/vote")
public class VoteController {

    @Value("${coverImagesFilePath}")
    private String coverImagesFilePath;

    @Autowired
    private IVoteService voteService;

    @Autowired
    private IVoteItemService voteItemService;

    /**
     * 上传封面图片
     * @param coverImage
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadCoverImage")
    public Map<String,Object> uploadCoverImage(MultipartFile coverImage)throws Exception{
        System.out.println("filename:"+coverImage.getName());
        Map<String,Object> resultMap=new HashMap<>();
        if(!coverImage.isEmpty()){
            // 获取文件名
            String originalFilename = coverImage.getOriginalFilename();
            String suffixName=originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;
            FileUtils.copyInputStreamToFile(coverImage.getInputStream(),new File(coverImagesFilePath+newFileName));
            resultMap.put("code",0);
            resultMap.put("msg","上传成功");
            resultMap.put("coverImageFileName",newFileName);
        }
        return resultMap;
    }


    @RequestMapping("/add")
    @Transactional
    public R add(@RequestBody Vote vote, @RequestHeader String token){
        Claims claims = JwtUtils.validateJWT(token).getClaims();
        vote.setOpenid(claims.getId());
        voteService.save(vote);
        List<VoteItem> voteItemList =  vote.getVoteItemList();
        for (VoteItem voteItem:voteItemList){
            voteItem.setVoteId(vote.getId());

            voteItemService.save(voteItem);
            System.out.println(voteItem);
        }
        return R.ok();
    }


    @RequestMapping("/listOfUser")
    public R listOfUser(@RequestHeader String token){
        Claims claims = JwtUtils.validateJWT(token).getClaims();
        List<Vote> voteList = voteService.list(new QueryWrapper<Vote>().eq("openid",claims.getId()).orderByDesc("vote_end_time"));
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("voteList",voteList);
        return R.ok(resultMap);
    }

}
