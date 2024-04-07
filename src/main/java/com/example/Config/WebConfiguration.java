package com.example.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;

@Configuration
public class WebConfiguration {
    //密码加密
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 就是注入一个Bean，类型为MappingJackson2HttpMessageConverter，
    // 获取到ObjectMapper通过mapper.registerModule(hibernate6Module);
    // 注册Module还可以定义时间日期的序列化格式。
    // 注意如果要让未加载的时候完全不输出，那么在Entity的类级别注解要使用Empty，
    // 例如：@JsonInclude(JsonInclude.Include.NON_EMPTY)，
    // 不然当数据为null的时候会输出null。
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = converter.getObjectMapper();
        Hibernate6Module hibernate6Module = new Hibernate6Module();
        objectMapper.registerModule(hibernate6Module);
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return converter;
    }
}
