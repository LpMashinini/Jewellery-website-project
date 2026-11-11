package com.Backend.Jewellery.repository;

import com.Backend.Jewellery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String number);
    boolean existByEmail(String email);
    Boolean existByPhoneNumber(String number);
}
