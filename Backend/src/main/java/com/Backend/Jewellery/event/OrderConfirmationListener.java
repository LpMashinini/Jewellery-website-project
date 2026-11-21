package com.Backend.Jewellery.event;

import com.Backend.Jewellery.entity.Order;
import com.Backend.Jewellery.repository.OrderRepository;
import com.Backend.Jewellery.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConfirmationListener {

    private final OrderRepository orderRepository;
    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase =TransactionPhase.AFTER_COMMIT) // ensure the email is send when the payment is successful
    public void onOrderPaid(OrderPaidEvent event){

        Order order = orderRepository.findDetailedById(event.orderId()).orElse(null);

        if (order == null) {
            log.error("Order {} not found when sending confirmation", event.orderId());
        }

        List<OrderConfirmation.Line> lines = order.getItems().stream()
                .map(item -> new OrderConfirmation.Line(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtPurchase()
                )).toList();
    }

}
