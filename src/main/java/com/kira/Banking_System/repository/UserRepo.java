package com.kira.Banking_System.repository;

import com.kira.Banking_System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);
}
