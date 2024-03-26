package com.example.Service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailMessageService {
    SimpleMailMessage createRegisterMessage(Integer code, String email);
    SimpleMailMessage createResetPasswordMessage(Integer code,String email);
    SimpleMailMessage createRegisterSuccessMessage(String email);
}
