package com.example.Controller;

import com.example.Entity.RestBeanNew;
import com.example.Entity.dto.Account;
import com.example.Entity.dto.AccountDetails;
import com.example.Entity.dto.LevelInfo;
import com.example.Entity.dto.VipInfo;
import com.example.Entity.vo.response.UserLevelInfoVO;
import com.example.Entity.vo.response.VipInfoVO;
import com.example.Repo.AccountRepository;
import com.example.Util.CommonUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    AccountRepository accountRepository;
    @Resource
    CommonUtils utils;

    @GetMapping("/nav")
    public RestBeanNew<?> user_nav(){
        String usernameOrEmail = utils.getAuthenticationUserNameOrEmail();
        if(!usernameOrEmail.isEmpty()){
            Account account = accountRepository.findAccountByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
            AccountDetails accountDetails = account.getAccountDetails();
            LevelInfo levelInfo = account.getLevelInfo();
            VipInfo vipInfo = account.getVipInfo();
            Map<String,Object> wallet = Map.of("point_coin",accountDetails.getPointCoin(),"future_coin",accountDetails.getFuture_coin());
            UserLevelInfoVO levelInfoVO = levelInfo.asViewObject(UserLevelInfoVO.class);
            VipInfoVO vipInfoVO = vipInfo.asViewObject(VipInfoVO.class);
            Map<String,Object> userMap = Map.of("username",account.getUsername(),
                    "wallet",wallet,"user_number",accountRepository.count(),"user_level",levelInfoVO,"vip",vipInfoVO);
            return RestBeanNew.success(userMap,"请求成功");
        }else return RestBeanNew.failure(401,"请求失败");
    }

    @GetMapping("/stat")
    public RestBeanNew<?> user_stat(){
        String usernameOrEmail = utils.getAuthenticationUserNameOrEmail();
        if(!usernameOrEmail.isEmpty()){
            Account account = accountRepository.findAccountByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
            AccountDetails accountDetails = account.getAccountDetails();
            Map<String,Object> userMap = Map.of("motion_num",accountDetails.getMotionNum(),"concerned_num",accountDetails.getConcernedNum(),"fan_num",accountDetails.getFanNum());
            return RestBeanNew.success(userMap,"请求成功");
        }else return RestBeanNew.failure(401,"请求失败");
    }


}
