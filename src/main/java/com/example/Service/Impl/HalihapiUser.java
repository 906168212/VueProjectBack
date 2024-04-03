package com.example.Service.Impl;

import com.example.Service.HalihapiUserDetails;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;


@Getter
public class HalihapiUser extends User implements HalihapiUserDetails {
    private final int userID;
    public HalihapiUser(UserDetails user,int userID) {
        super(user.getUsername(), user.getPassword(),
                user.isEnabled(), user.isAccountNonExpired(),
                user.isCredentialsNonExpired(), user.isAccountNonLocked(),
                user.getAuthorities());
        this.userID = userID;
    }

    // 无参构造函数
    public HalihapiUser() {
        super("", "", true, true, true, true, new ArrayList<>());
        this.userID = 0; // 或者根据需求设置默认值
    }
}
