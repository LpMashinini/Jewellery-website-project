package com.Backend.Jewellery.service;

import com.Backend.Jewellery.dto.PayStackInitializeRequest;
import com.Backend.Jewellery.dto.PaystackInitializeResponse;
import com.Backend.Jewellery.entity.Order;
import com.Backend.Jewellery.entity.OrderStatus;
import com.Backend.Jewellery.entity.Payment;
import com.Backend.Jewellery.entity.PaymentStatus;
import com.Backend.Jewellery.repository.OrderRepository;
import com.Backend.Jewellery.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayStackService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;


    @Value("${paystack.secret-key}")
    private String secretKey;

    @Value("${paystack.base-url}")
    private String baseUrl;

    @Value("${paystack.callback-url}")
    private String callbackUrl;

    public PaystackInitializeResponse initializeOrderPayment(Long userId, Long orderId) {

        Order order = orderRepository.findWithUserById(orderId)
                .filter(o ->o.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));


        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order");
        }

        BigDecimal amount = order.getTotalAmount();

        long amountInCents = amount
                .movePointRight(2)
                .setScale(0, RoundingMode.UNNECESSARY)
                .longValueExact();

        String reference = "JWL-" + orderId + UUID.randomUUID().toString().replace("-", "");

        PayStackInitializeRequest request = new PayStackInitializeRequest();
        request.setEmail(order.getUser().getEmail());
        request.setAmount(String.valueOf(amountInCents));
        request.setReference(reference);
        request.setCallback_url(callbackUrl);


        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();


        PaystackInitializeResponse response = restClient
                .post()
                .uri("/transaction/initialize")
                .body(request)
                .retrieve()
                .body(PaystackInitializeResponse.class);


        if (response == null || response.getData() == null || response.getData().getReference() == null){
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"Unable to start payment");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setReference(response.getData().getReference());
        payment.setAmount(amount);
        payment.setCurrency("ZAR");
        payment.setStatus(PaymentStatus.PENDING);

        return response;
    }
}
