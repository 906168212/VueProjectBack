package com.example.Controller;

import com.example.Entity.RestBeanNew;
import com.example.Entity.dto.ArticleInfo;
import com.example.Repo.AccountRepository;
import com.example.Repo.ArticleInfoRepository;
import com.example.Service.AccountService;
import com.example.Util.CommonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    ArticleInfoRepository articleInfoRepository;
    @GetMapping("/info")
    @Transactional
    public RestBeanNew<?> article_info() throws Exception {
//        return RestBeanNew.success(accountService.getAllAccount(2));
//        return RestBeanNew.success(accountService.getSingleAccount(2));
//        return RestBeanNew.success(accountService.getInfoAccount(2));
//        return RestBeanNew.success(accountService.getAccountArticle(2));
        return RestBeanNew.success(articleInfoRepository.findArticleInfosByRecommend(1));
    }
}
