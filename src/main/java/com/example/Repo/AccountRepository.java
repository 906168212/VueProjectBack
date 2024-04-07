package com.example.Repo;

import com.example.Entity.dto.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account,Integer> {

    Account findAccountBySid(int sid);
    Account findAccountByUsernameOrEmail(String username, String email);
    Account findAccountByEmail(String email);
    Account findAccountByUsername(String username);
    long count();

    @EntityGraph(value = "account.all")
    @Query("SELECT a from Account a where a.sid = :sid")
    Account findAccountBySidWithAll(int sid);

    @EntityGraph(value = "account.info")
    @Query("SELECT a from Account a where a.sid = :sid")
    Account findAccountBySidWithInfo(int sid);
    @EntityGraph(value = "account.article")
    @Query("SELECT a from Account a where a.sid = :sid")
    Account findAccountBySidWithArticle(int sid);
}
