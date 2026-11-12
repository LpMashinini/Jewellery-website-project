package com.Backend.Jewellery.repository;

import com.Backend.Jewellery.entity.Otp;
import com.Backend.Jewellery.entity.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByCodeAndUser_IdAndTypeAndVerifiedIsFalseAndExpiresAtAfter(
            String code, Long userId, OtpType type, LocalDateTime now
    );

    List<Otp> findByUser_IdAndVerifiedIsFalseAndExpiresAtBefore(
            Long userid, LocalDateTime now
    );
}
