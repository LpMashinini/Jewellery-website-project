package com.Backend.Jewellery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.AbstractDocument;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_payment_reference",
                columnNames = "reference"
        )
        }
)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "reference", nullable = false, length = 100)
    private String reference;

    @Column(name = "paystack_transaction_id")
    private Long transactionId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "ZAR";

    @Column(name = "payment_status", nullable = false)
    private PaymentStatus status;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "paidAt", nullable = false)
    private LocalDateTime paidAt;
}
