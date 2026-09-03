package com.jssv.globalinvoice.repository;

import com.jssv.globalinvoice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findByEmailContainingIgnoreCase(Pageable pageable, String email);
    Optional<User> findByEmail(String email);
}
