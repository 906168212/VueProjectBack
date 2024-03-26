package com.example.Entity.vo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class resetPasswordVO {
    @Pattern(regexp = "^(?![a-zA-Z]+$)(?!\\d+$)(?![!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]+$)(?!.*[\\u4e00-\\u9fff]).*$",message = "密码格式错误")
    @NotBlank(message = "密码不得为空")
    @Length(min = 8,max = 16)
    String new_password;
    @NotBlank(message = "未请求账号")
    String reset_uuid;
    @NotBlank(message = "请先进行身份验证")
    String identify_uuid;
}
