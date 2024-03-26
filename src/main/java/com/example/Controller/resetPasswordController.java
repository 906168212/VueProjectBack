package com.example.Controller;

import com.example.Entity.RestBeanNew;
import com.example.Entity.vo.request.EmailVerifyVO;
import com.example.Entity.vo.request.resetPasswordStatusVO;
import com.example.Entity.vo.request.resetPasswordVO;
import com.example.Service.EmailService;
import com.example.Service.ResetPasswordService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Validated
@RestController
@RequestMapping("/api/resetPassword")
@Slf4j
public class resetPasswordController {
    @Resource
    ResetPasswordService resetPasswordService;
    @Resource
    EmailService emailService;

    // 忘记密码 - 2 - 发送验证码
    @PostMapping("/askResetPasswordEmailCode")
    public RestBeanNew<?> askResetPasswordEmailCode(@RequestBody @Valid resetPasswordStatusVO vo, HttpServletRequest request) throws Exception {
        RestBeanNew<?> response = sendEmailRequirementResponse(vo);
        if (response!=null) return response;
        CompletableFuture<RestBeanNew<?>> verifyEmailCompletionFuture = new CompletableFuture<>(); // 创建一个新的 CompletableFuture 对象
        emailService.setEmailCompletionFuture(verifyEmailCompletionFuture);
        emailService.EmailVerifyCode("resetPassword", vo.getEmail(), request.getRemoteAddr());
        return verifyEmailCompletionFuture.get(); // 等待 CompletableFuture 完成
    }

    // 忘记密码 - 3 - 邮箱验证
    @PostMapping("/resetIdentify")
    public RestBeanNew<?> resetIdentify(@RequestBody @Valid EmailVerifyVO vo) throws Exception {
        return verifyIdentifyResponse(vo);
    }

    // 忘记密码 - 4 - 重置密码
    @PostMapping("/resetPassword")
    public RestBeanNew<?> finalResetPassword(@RequestBody @Valid resetPasswordVO vo) {
        return resetPasswordResponse(vo);
    }

    private RestBeanNew<?> sendEmailRequirementResponse(resetPasswordStatusVO vo) throws Exception{
        return resetPasswordService.sendEmailRequirement(vo);
    }
    private RestBeanNew<?> resetPasswordResponse(resetPasswordVO vo){
        return resetPasswordService.resetPassword(vo);
    }
    private RestBeanNew<?> verifyIdentifyResponse(EmailVerifyVO vo) throws Exception {
        return resetPasswordService.verifyIdentify(vo);
    }
}
