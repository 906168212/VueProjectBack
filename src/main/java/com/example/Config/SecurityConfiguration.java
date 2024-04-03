package com.example.Config;

import com.example.Entity.dto.Account;
import com.example.Entity.RestBeanNew;
import com.example.Entity.vo.response.AuthorizeVO;
import com.example.Filter.JwtAuthenticationFilter;
import com.example.Repo.AccountRepository;
import com.example.Service.Impl.HalihapiUser;
import com.example.Util.JWTUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;

@Configuration
@Slf4j
public class SecurityConfiguration {

    @Resource
    JWTUtils utils;
    @Resource
    AccountRepository repository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security)throws Exception{
        return security
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(conf -> {
                    conf.requestMatchers("/api/auth/**","/api/resetPassword/**","/api/QRCode/**").permitAll();
                    conf.anyRequest().authenticated();
                })
                // 登录页面请求  默认放权
                .formLogin(conf -> {
                    //一般前后端分离后，为了统一规范接口，使用 /api/模块/功能 的形式命名接口
                    //前后端分离后，不需要指定登录页面的位置，只要接收登录请求的接口进行处理即可
                    conf.loginProcessingUrl("/api/auth/login");
                    //做处理成功和失败的处理服务
                    conf.successHandler(this::handleProcess);
                    conf.failureHandler(this::handleProcess);
                    conf.permitAll();   //放行
                })
                //配置退出登录
                .logout(conf ->{
                    conf.logoutUrl("/api/auth/logout");
                    conf.logoutSuccessHandler(this::onLogoutSuccess);
                })
                //配置跨域访问
                .cors(conf -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    //添加前端站点地址，添加浏览器信任
                    cors.addAllowedOrigin("http://localhost:5173");
                    cors.addAllowedOrigin("http://192.168.75.1:5173");
                    cors.addAllowedOrigin("http://192.168.20.1:5173");
                    cors.addAllowedOrigin("http://192.168.31.219:5173");
                    //虽然也可以像这样允许所有 cors.addAllowedOriginPattern("*"); 但不安全
                    cors.setAllowCredentials(false); //不允许跨域请求携带cookies
                    cors.addAllowedHeader("*");     //其他的也可以配置
                    cors.addAllowedMethod("*");
                    cors.addExposedHeader("*");
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**",cors);       //针对所有网址生效
                    conf.configurationSource(source);
                })
                //未登录或错误状态下的处理
                .exceptionHandling(conf ->{
                    //配置授权相关异常处理器
                    conf.accessDeniedHandler(this::handleProcess);
                    //配置验证相关异常的处理器
                    conf.authenticationEntryPoint(this::handleProcess);
                })
                //配置无状态，不让SS还是用Session验证
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //配置用于JWT验证的过滤器,在用户密码验证之前
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }




    //异常处理器
    private void handleProcess(HttpServletRequest request,          //请求和回应处理，以及其他额外的错误处理
                               HttpServletResponse response,
                               Object ExceptionOrAuthentication) throws IOException{
        response.setContentType("application/json;charset=utf-8");  //定义向前端发送的数据格式
        PrintWriter writer = response.getWriter();
        //AccessDeniedException 验证错误处理，即未登录，403处理
        if(ExceptionOrAuthentication instanceof AccessDeniedException exception){
            writer.write(RestBeanNew.failure(403,exception.getMessage()).asJSONString());
            //其余错误类型，包括用户登录错误以及验证错误，可以放在一起
        } else if (ExceptionOrAuthentication instanceof Exception exception) {
            writer.write(RestBeanNew.failure(401, exception.getMessage()).asJSONString());
            //登录成功的处理
        } else if (ExceptionOrAuthentication instanceof Authentication auth) {

            HalihapiUser user = (HalihapiUser) auth.getPrincipal();   // 获取用户登录后的信息 包括id,用户名、密码、角色
//            AuthorizeVO vo = new AuthorizeVO();
            String token = utils.CreateJWT(user);     // 创建token令牌
            int userId = user.getUserID();
//            String usernameOrEmail = user.getUsername();
//            Account account = repository.findAccountByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
            Account account = repository.findAccountBySid(userId);
            // 用我们自行封装的DataBase方法,还可以再优雅一点！
            AuthorizeVO vo = account.asViewObject(AuthorizeVO.class,v ->{
                v.setExpireTime(utils.expireTime.getTime());  //过期时间戳
                v.setToken(token);
            });
            //在登陆成功的时候需要返回我们的JWT令牌，下次客户端访问就会携带这个令牌了，令牌过期后需要重新登陆才行
            writer.write(RestBeanNew.success(vo).asJSONString());
        }
    }

    //退出登录处理器
    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication auth) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");  //获取头部中的Authorization
        String token = utils.convertToken(authorization);
        if (token != null){
            log.info("请求头合法，具有合规token");
            //将Token加入黑名单，退出登录操作不需要在进行验证
            if(utils.invalidateJWT(token)){
                //只有加入黑名单成功才能退出成功
                writer.write(RestBeanNew.success("退出登录成功").asJSONString());
            }else
                writer.write(RestBeanNew.failure(400,"退出登录失败").asJSONString());
        }else
            writer.write(RestBeanNew.failure(400,"退出登录失败").asJSONString());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }


}
