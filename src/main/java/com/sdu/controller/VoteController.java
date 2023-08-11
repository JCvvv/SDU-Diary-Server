package com.sdu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdu.entity.R;
import com.sdu.entity.Vote;
import com.sdu.entity.VoteItem;
import com.sdu.entity.WxUserInfo;
import com.sdu.service.IVoteItemService;
import com.sdu.service.IVoteService;
import com.sdu.service.IWxUserInfoService;
import com.sdu.util.DateUtil;
import com.sdu.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

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

    @Autowired
    private IWxUserInfoService wxUserInfoService;

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
        }
        return R.ok();
    }

    @RequestMapping("/update/{id}")
    @Transactional
    public R update(@PathVariable(value = "id") Integer id,@RequestBody Vote vote, @RequestHeader String token){
        Claims claims = JwtUtils.validateJWT(token).getClaims();
        vote.setOpenid(claims.getId());
        voteService.update(vote,new QueryWrapper<Vote>().eq("id",id));
        voteItemService.remove(new QueryWrapper<VoteItem>().eq("vote_id",id));
        List<VoteItem> voteItemList =  vote.getVoteItemList();
        for (VoteItem voteItem:voteItemList){
            voteItem.setVoteId(id);
            voteItemService.save(voteItem);
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


    @RequestMapping("/listOfAllUser")
    public R listOfAllUser(@RequestHeader String token){
        Claims claims = JwtUtils.validateJWT(token).getClaims();

        List<Vote> voteList = voteService.list(new QueryWrapper<Vote>().ne("openid",claims.getId()).orderByDesc("vote_end_time"));

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("voteList",voteList);
        return R.ok(resultMap);


//        Vote vote = voteService.getById(id);
//        WxUserInfo wxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("openid",vote.getOpenid()));
//        vote.setWxUserInfo(wxUserInfo);
//        List<VoteItem> voteItemList = voteItemService.list(new QueryWrapper<VoteItem>().eq("vote_id",id));
//        vote.setVoteItemList(voteItemList);
//        Map<String,Object> resultMap = new HashMap<>();
//        resultMap.put("vote",vote);
    }

    @RequestMapping("/listOfToday")
    public R listOfToday(@RequestHeader String token) throws Exception {


        Claims claims = JwtUtils.validateJWT(token).getClaims();
        List<Vote> voteList = voteService.list(new QueryWrapper<Vote>().eq("openid",claims.getId()).like("vote_end_time",DateUtil.getTodayDate()).orderByDesc("vote_end_time"));
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("voteList",voteList);

        return R.ok(resultMap);
    }

    @RequestMapping("/selectOfUser/{title}")
    public R selectOfUser(@PathVariable(value = "title") String title,@RequestHeader String token) throws Exception {

        Claims claims = JwtUtils.validateJWT(token).getClaims();
        List<Vote> voteList = voteService.list(new QueryWrapper<Vote>().eq("openid",claims.getId()).like("title",title).orderByDesc("vote_end_time"));
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("voteList",voteList);
        return R.ok(resultMap);
    }

    @RequestMapping("/selectSmileOfUser")
    public R selectSmileOfUser(@RequestHeader String token){
        Claims claims = JwtUtils.validateJWT(token).getClaims();
        List<Vote> voteList = voteService.list(new QueryWrapper<Vote>().eq("openid",claims.getId()).in("type",0,2,4).orderByDesc("vote_end_time"));
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("voteList",voteList);
        return R.ok(resultMap);
    }

    @RequestMapping("/{id}")
    public R findById(@PathVariable(value = "id") Integer id){
        Vote vote = voteService.getById(id);
        WxUserInfo wxUserInfo = wxUserInfoService.getOne(new QueryWrapper<WxUserInfo>().eq("openid",vote.getOpenid()));
        vote.setWxUserInfo(wxUserInfo);
        List<VoteItem> voteItemList = voteItemService.list(new QueryWrapper<VoteItem>().eq("vote_id",id));
        vote.setVoteItemList(voteItemList);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("vote",vote);

        return R.ok(resultMap);
    }

    @DeleteMapping("/delete/{id}")
    public R deleteVote(@PathVariable(value = "id") Integer id){
            voteService.removeById(id);
            voteItemService.remove(new QueryWrapper<VoteItem>().eq("vote_id",id));
        return R.ok("删除成功");
    }

}
