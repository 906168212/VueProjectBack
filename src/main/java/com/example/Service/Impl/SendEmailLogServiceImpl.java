package com.example.Service.Impl;

import com.example.Entity.dto.SendEmailLog;
import com.example.Repo.SendEmailLogRepository;
import com.example.Service.SendEmailLogService;
import com.example.Util.MyException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SendEmailLogServiceImpl implements SendEmailLogService {

    @Resource
    SendEmailLogRepository sendEmailLogRepository;

    @Override
    public void updateStatus(String uuid,Integer status) {
        // 根据主键查询实体
        SendEmailLog existingLog = sendEmailLogRepository.findSendEmailLogByUuid(uuid);
        if (existingLog!=null){
            existingLog.setSendStatus(status);
            sendEmailLogRepository.save(existingLog);
        }else throw new MyException("数据库信息不存在！");
        // 如果记录不存在，可以选择抛出异常或进行其他处理
    }



    @Override
    public void storeBasicLog(String uuid,String email,String type,String exchange,String routingKey){
        SendEmailLog sendEmailLog = new SendEmailLog();
        sendEmailLog.setUuid(uuid);
        sendEmailLog.setEmail(email);
        sendEmailLog.setType(type);
        sendEmailLog.setExchange(exchange);
        sendEmailLog.setRoutingKey(routingKey);
        sendEmailLogRepository.save(sendEmailLog);
    }

    @Override
    public Integer getConsumeStatus(String uuid) {
        // 根据主键查询实体
        SendEmailLog existingLog = sendEmailLogRepository.findSendEmailLogByUuid(uuid);
        if (existingLog!=null){
            return existingLog.getSendStatus();
        }else throw new MyException("数据库信息不存在！");
    }

}
