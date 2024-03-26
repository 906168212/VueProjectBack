package com.example.Service;

import com.example.Entity.RestBeanNew;
import com.example.Entity.dto.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface AccountService extends UserDetailsService {
    RestBeanNew<?> confirmResetAccount(String username) throws Exception;

}
