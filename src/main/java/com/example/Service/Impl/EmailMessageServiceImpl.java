package com.example.Service.Impl;

import com.example.Service.EmailMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailMessageServiceImpl implements EmailMessageService {
    @Value("${spring.mail.username}")
    String username;
    @Override
    public SimpleMailMessage createRegisterMessage(Integer code, String email) {
        return createMessage("【希望园】注册验证",
                """
                欢迎注册希望园，在这里，分享属于你自己的秘密吧！
                \n您的注册验证码为：""" + code + """
                \n有效时间为5分钟，请妥善保存！
                \n
                \n
                \n
                \n如非本人操作，请忽视，并赶紧查看您的账号安全！
                """,email);
    }
    @Override
    public SimpleMailMessage createResetPasswordMessage(Integer code,String email) {
        return createMessage("【希望园】重置密码身份验证",
                """
                欢迎来到希望园，在这里，分享属于你自己的秘密吧！
                \n您的账号正在进行密码重置操作！
                \n您的密码重置验证码为：""" + code + """
                \n有效时间为5分钟，请妥善保存！
                \n
                \n
                \n
                \n如非本人操作，请忽视，并赶紧查看您的账号安全！
                """,email);
    }
    @Override
    public SimpleMailMessage createRegisterSuccessMessage(String email) {
        return createMessage("【希望园】注册成功",
                """
                欢迎来到希望园，在这里，分享属于你自己的秘密吧！
                \n您的账号已成功完成注册！
                \n请妥善保存您的密码。
                \n
                \n
                \n
                \n注册即表示您已同意希望园服务协议以及隐私保护指引，对于您在我站造成的任何损失，本站概不负责！
                \n如非本人操作，请忽视，并赶紧查看您的账号安全！
                """,email);
    }

    private SimpleMailMessage createMessage(String title, String content, String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }
}
