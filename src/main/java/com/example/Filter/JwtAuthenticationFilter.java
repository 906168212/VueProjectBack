package com.example.Filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Service.HalihapiUserDetails;
import com.example.Util.JWTUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    @Resource
    JWTUtils utils;

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //获取请求头
        String authorization = request.getHeader("Authorization");
        if(authorization != null){
            //获取解析过的JWT
            String token = utils.convertToken(authorization);      //获取token
            DecodedJWT jwt = utils.resolveJWT(token);
            if (jwt != null){
                HalihapiUserDetails user = utils.toUserDetails(jwt);
                //使用UsernamePasswordAuthenticationToken作为实体，填写相关用户信息进去
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //然后直接把配置好的Authentication塞给SecurityContext表示已经完成验证
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //为了方便可以丢一些用户名之类的东西

            }
        }

        //最后放行，继续下一个过滤器
        //可能各位小伙伴会好奇，要是没验证成功不是应该拦截吗？这个其实没有关系的
        //因为如果没有验证失败上面是不会给SecurityContext设置Authentication的，后面直接就被拦截掉了
        //而且有可能用户发起的是用户名密码登录请求，这种情况也要放行的，不然怎么登录，所以说直接放行就好
        filterChain.doFilter(request, response);
    }

}
