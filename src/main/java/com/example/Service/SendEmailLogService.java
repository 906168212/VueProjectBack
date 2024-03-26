package com.example.Service;

import java.util.Date;

public interface SendEmailLogService {
    void updateStatus(String uuid,Integer status);
    void storeBasicLog(String uuid,String email,String type,String exchange,String routingKey);
    //判断投递状态
    Integer getConsumeStatus(String uuid);
}
