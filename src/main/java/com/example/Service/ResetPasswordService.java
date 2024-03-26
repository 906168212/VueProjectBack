package com.example.Service;

import com.example.Entity.RestBeanNew;
import com.example.Entity.vo.request.EmailVerifyVO;
import com.example.Entity.vo.request.resetPasswordStatusVO;
import com.example.Entity.vo.request.resetPasswordVO;

public interface ResetPasswordService {
    RestBeanNew<?> sendEmailRequirement(resetPasswordStatusVO vo) throws Exception;
    RestBeanNew<?> verifyIdentify(EmailVerifyVO vo) throws Exception;
    String getRealEmail(String reset_id);
    String updateResetAccountInfo(String reset_uuid) throws Exception;
    RestBeanNew<?> resetPassword(resetPasswordVO vo);
}
