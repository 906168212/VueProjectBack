package com.example.Entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EmailVerifyVO {
    @Email(message = "邮箱格式错误")
    String email;
    @Length(max=6,min = 6,message = "验证码必须为6位")
    String emailCode;
    @NotBlank(message = "未请求账号")
    String reset_uuid;
}
