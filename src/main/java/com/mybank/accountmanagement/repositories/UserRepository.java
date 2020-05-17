package com.mybank.accountmanagement.repositories;

import com.mybank.accountmanagement.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    String findUsernameById(Long id);
}
