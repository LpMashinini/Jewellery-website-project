package com.Backend.Jewellery.repository;

import com.Backend.Jewellery.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Orders> findByPaymentReference(String paymentReference);
}
