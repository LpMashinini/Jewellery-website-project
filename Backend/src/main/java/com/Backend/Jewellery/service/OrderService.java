package com.Backend.Jewellery.service;

import com.Backend.Jewellery.entity.*;
import com.Backend.Jewellery.repository.CartRepository;
import com.Backend.Jewellery.repository.OrderRepository;
import com.Backend.Jewellery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Order checkout(Long userId){

        Cart cart = cartRepository.findByUserId(userId)
                .filter(c -> !c.getItems().isEmpty())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty"));


        List<CartItem> items = cart.getItems().stream()
                .sorted(Comparator.comparing(item -> item.getProduct().getId()))
                .toList();

        for (CartItem item : items) {

            int updated = productRepository.decrementStock(
                    item.getProduct().getId(), item.getQuantity()
            );

            if (updated == 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Insufficient stock for " + item.getProduct().getName()
                        );
            }
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {

            Product product = item.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            order.getItems().add(orderItem);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        cart.getItems().clear();

        return orderRepository.save(order);
    }
}
