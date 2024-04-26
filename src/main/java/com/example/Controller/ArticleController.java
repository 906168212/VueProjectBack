package com.example.Controller;

import com.example.Entity.RestBeanNew;
import com.example.Entity.dto.*;
import com.example.Entity.vo.ArticleVO;
import com.example.Repo.AccountRepository;
import com.example.Repo.ArticleInfoRepository;
import com.example.Service.AccountService;
import com.example.Util.CommonUtils;
import com.example.mapper.ArticleMapper;
import com.example.mapper.MainMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/article")
public class ArticleController {

    @Resource
    CommonUtils utils;
    @Resource
    AccountService accountService;
    @Resource
    MainMapper mapper;
    @Resource
    ArticleInfoRepository articleInfoRepository;
    @Resource
    ArticleMapper articleMapper;
    @Resource
    AccountRepository accountRepository;

    @GetMapping("/all")
    @Transactional
    public RestBeanNew<?> getAllArticle() throws Exception {
        int uid = utils.getAuthenticationUserID();
        Account account = accountRepository.findAccountBySid(uid);
        List<ArticleInfo> articleInfoList = articleInfoRepository.findArticleInfosByAccount_SidAndStatusOrderByPubDateDesc(uid,1);
        List<UserArticleDTO> userArticleDTOList = articleMapper.toUserArticleDTOList(articleInfoList);
        UserAllArticleDTO userAllArticleDTOList = articleMapper.toUserAllArticleDTO(account,userArticleDTOList);
//        List<ArticleDTO> articleDTOList = mapper.toArticleDTOList(articleInfoList);
//        List<ArticleVO> articleVOList = mapper.toArticleVOList(articleDTOList);
        return RestBeanNew.success(userAllArticleDTOList);
    }

    @GetMapping("/recommend")
    @Transactional
    public RestBeanNew<?> recommendArticle(@RequestParam int pt){
        List<ArticleInfo> articleInfoList = articleInfoRepository.findRecommendArticleInfosByType(pt);
        List<ArticleDTO> articleDTOList = mapper.toArticleDTOList(articleInfoList);
        List<ArticleVO> articleVOList = mapper.toArticleVOList(articleDTOList);
        return RestBeanNew.success(articleVOList);
    }
    @GetMapping("/dynamic/region")
    @Transactional
    public RestBeanNew<?> getArticleRegion(@RequestParam int rid) throws Exception {
        List<ArticleInfo> articleInfoList = articleInfoRepository.findArticleInfosByCategory(rid);
        List<ArticleDTO> articleDTOList = mapper.toArticleDTOList(articleInfoList);
        List<ArticleVO> articleVOList =mapper.toArticleVOList(articleDTOList);
        return RestBeanNew.success(articleVOList);
    }

    @GetMapping("/ranking/region")
    @Transactional
    public RestBeanNew<?> rankRegion(@RequestParam int rid){
        List<ArticleInfo> articleInfoList = articleInfoRepository.findArticleInfosByCategoryOrderByVisitor(rid);
        List<ArticleDTO> articleDTOList = mapper.toArticleDTOList(articleInfoList);
        List<ArticleVO> articleVOList = mapper.toArticleVOList(articleDTOList);
        return  RestBeanNew.success(articleVOList);
    }

    @GetMapping("/update/region")
    @Transactional
    public RestBeanNew<?> updateRegion(@RequestParam int pt){
        List<ArticleInfo> articleInfoList = articleInfoRepository.findLatestArticleInfos(pt);
        List<ArticleDTO> articleDTOList = mapper.toArticleDTOList(articleInfoList);
        List<ArticleVO> articleVOList = mapper.toArticleVOList(articleDTOList);
        return RestBeanNew.success(articleVOList);
    }
}
