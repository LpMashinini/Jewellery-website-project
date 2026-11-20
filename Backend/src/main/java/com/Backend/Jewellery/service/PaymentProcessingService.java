package com.Backend.Jewellery.service;

import com.Backend.Jewellery.dto.PaystackVerifyData;
import com.Backend.Jewellery.entity.Order;
import com.Backend.Jewellery.entity.OrderStatus;
import com.Backend.Jewellery.entity.Payment;
import com.Backend.Jewellery.entity.PaymentStatus;
import com.Backend.Jewellery.event.OrderPaidEvent;
import com.Backend.Jewellery.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentProcessingService {

    private final PaymentRepository paymentRepository;
    private final PaystackPaymentValidator validator;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public void processVerifiedPayment(String reference, PaystackVerifyData transaction){

        Payment payment = paymentRepository.findByReferenceForUpdate(reference)
                .orElseThrow( () -> new IllegalArgumentException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.SUCCESS){
            return; // already processed payment, do nothing
        }


        validator.validate(payment, reference, transaction);

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setReference(transaction.getReference());
        payment.setPaidAt(LocalDateTime.now());

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.PAID);

        eventPublisher.publishEvent(new OrderPaidEvent(order.getId()));

    }
}
