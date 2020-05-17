package com.mybank.accountmanagement.repositories;

import com.mybank.accountmanagement.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long id);
    Optional<Account> findById(Long id);
    Optional<Account> findByName(String name);
}