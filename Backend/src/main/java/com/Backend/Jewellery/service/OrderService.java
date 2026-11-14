package com.Backend.Jewellery.service;

import com.Backend.Jewellery.entity.CartItem;
import com.Backend.Jewellery.entity.Orders;
import com.Backend.Jewellery.repository.CartRepository;
import com.Backend.Jewellery.repository.OrderRepository;
import com.Backend.Jewellery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public Orders checkout(Long userId){
        return null;
    }
}
