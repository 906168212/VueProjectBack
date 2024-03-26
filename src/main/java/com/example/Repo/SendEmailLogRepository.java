package com.example.Repo;

import com.example.Entity.dto.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SendEmailLogRepository extends JpaRepository<SendEmailLog,Integer> {
    SendEmailLog findSendEmailLogByUuid(String uuid);
}
