package com.example.Entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class resetPasswordStatusVO {
    @Email(message = "邮箱格式错误")
    String email;
    @NotBlank(message = "未请求账号")
    String reset_uuid;
}
