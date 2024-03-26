package com.example.Controller;

import com.example.Entity.RestBeanNew;
import com.example.Entity.vo.request.EmailRegisterVO;
import com.example.Service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Validated
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthorizeController  {
    @Resource
    EmailService emailService;
    @Resource
    RegisterService registerService;
    @Resource
    AccountService accountService;

    @Value("${spring.mail.limitTime}")
    String limitTime;


    // 请求邮件验证码 注册
    @PostMapping("/askRegisterCode")
    public RestBeanNew<?> askVerifyCode(@RequestParam @Email String email,
                                           HttpServletRequest request) throws ExecutionException, InterruptedException {
        CompletableFuture<RestBeanNew<?>> verifyEmailCompletionFuture = new CompletableFuture<>(); // 创建一个新的 CompletableFuture 对象
        setEmailCompletionFuture(verifyEmailCompletionFuture);
        askEmailCode(email, request);
        // 等待 CompletableFuture 完成，并在完成后返回响应给前端
        return verifyEmailCompletionFuture.get(); // 等待 CompletableFuture 完成
    }

    //注册
    @PostMapping("/register")
    public RestBeanNew<?> Register(@RequestBody @Valid EmailRegisterVO vo) throws Exception{
        CompletableFuture<RestBeanNew<?>> registerCompletionFuture = new CompletableFuture<>(); // 创建一个新的 CompletableFuture 对象
        setEmailCompletionFuture(registerCompletionFuture);
        RestBeanNew<?> response = registerAccount(vo);
        if (response!=null) return response;
        else return registerCompletionFuture.get(); // 等待 CompletableFuture 完成

    }

    // 忘记密码 - 1 - 请求用户是否存在
    @PostMapping("/askAccount")
    public RestBeanNew<?> findAccount(@RequestParam String username) throws Exception {
        return confirmResetAccount(username);
    }
    private void askEmailCode(String email,HttpServletRequest request){
        emailService.EmailVerifyCode("register", email, request.getRemoteAddr());
    }
    private void setEmailCompletionFuture(CompletableFuture<RestBeanNew<?>> future){
        emailService.setEmailCompletionFuture(future);
    }

    private RestBeanNew<?> registerAccount(EmailRegisterVO vo) throws Exception {
        return registerService.registerAccount(vo);
    }
    private RestBeanNew<?> confirmResetAccount(String username) throws Exception {
        return accountService.confirmResetAccount(username);
    }

}
