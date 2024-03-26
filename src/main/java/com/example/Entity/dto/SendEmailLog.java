package com.example.Entity.dto;

import com.example.Entity.BaseData;
import com.example.listener.SendEmailLogEntityListener;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "send_email_log")
@EntityListeners(SendEmailLogEntityListener.class)
public class SendEmailLog implements BaseData {
    @Id
    @Column(name = "uuid",nullable = false)
    String uuid;
    @Column(name = "email",nullable = false)
    String email;
    @Column(name = "type",nullable = false)
    String type;
    @Column(name ="exchange",nullable = false)
    String exchange;
    @Column(name = "routing_key",nullable = false)
    String routingKey;
    @Column(name = "send_status",nullable = false)
    int sendStatus;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    Date createTime;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    Date updateTime;


}
