package com.Backend.Jewellery.repository;

import com.Backend.Jewellery.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Order> findByPaymentReference(String paymentReference);

    @EntityGraph(attributePaths = "user")
    Optional<Order> findWithUserById(Long id);

    @EntityGraph(attributePaths = {"user", "items", "items.product"})
    Optional<Order> findDetailedById(Long id);
}
