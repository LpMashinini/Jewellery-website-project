package com.Backend.Jewellery.service;

import com.Backend.Jewellery.dto.CartResponse;
import com.Backend.Jewellery.entity.Cart;
import com.Backend.Jewellery.repository.CartRepository;
import com.Backend.Jewellery.repository.ProductRepository;
import com.Backend.Jewellery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId){

        return cartRepository.findByUserId(userId)
                .map(CartResponse::from)
                .orElse(CartResponse.empty());
    }

    private Cart getOrCreateCart(Long userId){

        return cartRepository.findByUserId(userId)
                .orElseGet( () -> {

                    Cart cart = new Cart();
                    cart.setUser(userRepository.getReferenceById(userId));
                    return cartRepository.save(cart);
                });
    }

    private Cart findCart(Long userId){
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "cart not found"));
    }


}
