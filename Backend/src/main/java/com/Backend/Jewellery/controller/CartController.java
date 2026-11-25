package com.Backend.Jewellery.controller;

import com.Backend.Jewellery.dto.AddToCartRequest;
import com.Backend.Jewellery.dto.CartResponse;
import com.Backend.Jewellery.dto.UpdateQuantityRequest;
import com.Backend.Jewellery.jwtAuth.AppUserDetails;
import com.Backend.Jewellery.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private CartService cartService;

    @GetMapping
    public CartResponse getCart(@AuthenticationPrincipal AppUserDetails principal){
        return cartService.getCart(principal.getId());
    }

    @PostMapping("/items")
    public CartResponse addItem(@AuthenticationPrincipal AppUserDetails principal,
                                @Valid @RequestBody AddToCartRequest request){

        return cartService.addToCart(principal.getId(), request.getProductId(), request.getQuantity());
    }

    @PutMapping("/items/{productId}")
    public CartResponse updateQuantity(
            @AuthenticationPrincipal AppUserDetails principal,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateQuantityRequest request
            ){

        return cartService.updateQuantity(principal.getId(), productId, request.getQuntity());
    }

    @DeleteMapping("/items/{productid}")
    public CartResponse removeItem(
            @AuthenticationPrincipal AppUserDetails principal,
            @PathVariable Long productId
    ){
        return cartService.removeItem(principal.getId(), productId);
    }
}
