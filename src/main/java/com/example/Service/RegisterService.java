package com.example.Service;
import com.example.Entity.RestBeanNew;
import com.example.Entity.vo.request.EmailRegisterVO;


public interface RegisterService {
//    void setRegisterCompletionFuture(CompletableFuture<RestBeanNew<?>> registerCompletionFuture);
    RestBeanNew<String> registerAccount(EmailRegisterVO vo) throws Exception;
}
