package com.example;

import com.example.Entity.dto.Account;
import com.example.Entity.dto.AccountDetails;
import com.example.Entity.dto.LevelInfo;
import com.example.Entity.dto.VipInfo;
import com.example.Repo.AccountRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class WebTestApplicationTests {

    @Resource
    AccountRepository accountRepository;

    @Test
    void contextLoads() {
        Account account = accountRepository.findAccountBySid(1);
        VipInfo vipInfo = new VipInfo();
        vipInfo.setStatus(1);
        vipInfo.setType(3);
        vipInfo.setOpeningTime(System.currentTimeMillis());
        vipInfo.setExpireTime(Long.MAX_VALUE); // 永久

        vipInfo.setAccount(account);
        account.setVipInfo(vipInfo);
        // 设置关联的 Account 对象
//        accountDetails.setAccount(account);
        // 关联 accountDetails 到 account
//        account.setAccountDetails(newAccountDetails);
        accountRepository.save(account);
//
//
//        accountDetails.setConcernedNum(0);
//        accountDetails.setFanNum(0);
//        accountDetails.setMotionNum(0);
//        accountDetails.setPointCoin(10.0F);
//        accountDetails.setFuture_coin(0.0F);
//        account.setAccountDetails(accountDetails); // 将 accountDetails 关联到 account
//        accountRepository.save(account); // 保存 account
    }

}
