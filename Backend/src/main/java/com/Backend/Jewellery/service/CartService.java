package com.Backend.Jewellery.service;

import com.Backend.Jewellery.dto.CartResponse;
import com.Backend.Jewellery.entity.Cart;
import com.Backend.Jewellery.entity.CartItem;
import com.Backend.Jewellery.entity.Product;
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

    @Transactional
    public CartResponse addToCart(Long userId, Long productId, int quantity){

        requirePositive(quantity);

        Cart cart = getOrCreateCart(userId);
        Product product = findProduct(productId);

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);


        int newQuantity = (existing == null? 0 : existing.getQuantity() + quantity);
        checkStock(product,newQuantity);

        if (existing == null){
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(newQuantity);
            cart.getItems().add(item);

        }else {
            existing.setQuantity(newQuantity);
        }

        return CartResponse.from(cart);
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

    private Product findProduct(Long productId){
        return  productRepository.findById(productId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }

    private void requirePositive(int quantity){
        if (quantity < 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be at least 1");
        }
    }

    private void checkStock(Product product, int requested){
        if (requested > product.getStockQuantity()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Not enough stock available for " + product.getName()
            );
        }
    }


}
