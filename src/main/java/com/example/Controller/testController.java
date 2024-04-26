package com.example.Controller;

import com.example.Entity.RestBeanNew;
import com.example.Entity.dto.Account;
import com.example.Entity.dto.ArticleInfo;
import com.example.Entity.dto.UserAllArticleDTO;
import com.example.Entity.dto.UserArticleDTO;
import com.example.Repo.AccountRepository;
import com.example.Util.CommonUtils;
import com.example.mapper.ArticleMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class testController {

    @Resource
    ArticleMapper mapper;
    @Resource
    AccountRepository accountRepository;

    @GetMapping("/initial")
    @Transactional
    public RestBeanNew<?> test() throws Exception {
        Account account = accountRepository.findAccountBySid(2);
        List<ArticleInfo> articleInfoList =account.getArticleInfoList();
        List<UserArticleDTO> userArticleDTOList = mapper.toUserArticleDTOList(articleInfoList);
        UserAllArticleDTO userAllArticleDTO = mapper.toUserAllArticleDTO(account,userArticleDTOList);
        return RestBeanNew.success(userAllArticleDTO);
    }
}
