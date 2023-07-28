package com.java1234.controller;

import com.java1234.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 投票Controller控制器
 */
@RestController
@RequestMapping("/vote")
public class VoteController {

    @Value("${coverImagesFilePath}")
    private String coverImagesFilePath;

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
}
