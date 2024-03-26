package com.example.Entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EmailRegisterVO {
    //同时包含字母、数字，不包含中文、字符、空格
    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?!.*[\\u4e00-\\u9fa5])(?!.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]).*$")
    @Length(min = 10,max = 20)
    String username;
    @Pattern(regexp = "^(?![a-zA-Z]+$)(?!\\d+$)(?![!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]+$)(?!.*[\\u4e00-\\u9fff]).*$")
    @NotBlank
    @Length(min = 8,max = 16)
    String password;
    @Email(message = "邮箱格式错误")
    @NotBlank
    String mail;
    @Length(max=6,min = 6,message = "验证码必须为6位")
    @NotBlank
    String code;
}
