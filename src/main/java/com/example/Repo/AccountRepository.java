package com.example.Repo;

import com.example.Entity.dto.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account,Integer> {
    Account findAccountBySid(int sid);
    Account findAccountByUsernameOrEmail(String username, String email);
    Account findAccountByEmail(String email);
    Account findAccountByUsername(String username);
    long count();
}
