package com.example.listener;

import com.example.Entity.dto.SendEmailLog;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Date;

public class SendEmailLogEntityListener {
    @PrePersist
    protected void  onCreate(SendEmailLog sendEmailLog) {
        sendEmailLog.setCreateTime(new Date());
        sendEmailLog.setUpdateTime(new Date());
    }
    @PreUpdate
    protected void onUpdate(SendEmailLog sendEmailLog) {
        sendEmailLog.setUpdateTime(new Date());
    }
}
