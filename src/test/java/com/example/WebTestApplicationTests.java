package com.example;

import com.example.Entity.dto.*;
import com.example.Repo.AccountRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@SpringBootTest
class WebTestApplicationTests {

    @Resource
    AccountRepository accountRepository;

    @Test
    @Transactional
    @Rollback(false)
    void contextLoads() {
        Account account = accountRepository.findAccountBySid(2);
//        List<ArticleInfo> articleInfoList = account.getArticleInfoList();
//        for(int a = 1;a<=30;a++){
//            ArticleInfo articleInfo = new ArticleInfo();
//            articleInfo.setAccount(account);
//            articleInfo.setStatus(1);
//            articleInfo.setTitle("测试用标题_"+a);
//            articleInfo.setDesc("这是一段测试用的文章描述_"+a);
//            articleInfo.setCategory(0);
//            articleInfo.setPicWebp("https://picsum.photos/672/378.webp?"+Math.random());
//            articleInfo.setPubDate(System.currentTimeMillis());
//            articleInfo.setRecommend(new Random().nextInt(2));
//
//            ArticleStat articleStat = new ArticleStat();
//            articleStat.setLike(new Random().nextInt(8999)+1000);
//            articleStat.setReview(new Random().nextInt(8999)+1000);
//            articleStat.setCollect(new Random().nextInt(8999)+1000);
//            articleStat.setVisitor(new Random().nextInt(18999)+1000);
//            articleInfo.setArticleStat(articleStat);
//            articleInfoList.add(articleInfo);
//        }
//        account.setArticleInfoList(articleInfoList);
        Wallet wallet = new Wallet();
        wallet.setFutureCoin(100);
        wallet.setPointCoin(300);
        account.setWallet(wallet);
        accountRepository.save(account);
    }

}
