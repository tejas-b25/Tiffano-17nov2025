package com.tiffino.userservice.repository;

import com.tiffino.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    // Optionally, to check by phone number as well
    Optional<User> findByPhoneNumber(String phoneNumber);

    Page<User> findAllByIsActiveTrue(Pageable pageable);
    //Optional<User> findByEmail(String email);
   // Optional<User> findByPhoneNumber(String phoneNumber);

}
