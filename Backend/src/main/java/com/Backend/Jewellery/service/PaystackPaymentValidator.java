package com.Backend.Jewellery.service;

import com.Backend.Jewellery.dto.PaystackVerifyData;
import com.Backend.Jewellery.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaystackPaymentValidator {

    public void validate (Payment payment, String reference, PaystackVerifyData transaction){

        validateReference(payment, reference, transaction);
        validateAmount(payment, transaction);
        validateCurrency(payment,transaction);
        validateStatus(transaction);

        if (transaction.getId() == null) {
            throw new IllegalStateException("Paystack transaction ID is missing");
        }
    }

    private void validateReference(Payment payment, String reference, PaystackVerifyData transaction){

        if (transaction.getReference() == null ||!payment.getReference().equals(reference) || !payment.getReference().equals(transaction.getReference())){
            throw new IllegalStateException("Payment reference mismatch");
        }
    }

    private void validateAmount(Payment payment, PaystackVerifyData transaction){
        if (transaction.getAmount() == null){
            throw new IllegalStateException("Payment amount is missing");
        }

        long expectedAmount = payment.getAmount().movePointRight(2).longValueExact();
        long received;

        try {
            received = Long.parseLong(transaction.getAmount());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid Paystack amount");
        }

        if (received != expectedAmount){
            throw new IllegalStateException("Payment amount mismatch. Expected: " + expectedAmount + ", received " + received);
        }
    }

    private void validateCurrency(Payment payment, PaystackVerifyData transaction){
        if (transaction.getCurrrency() == null || !payment.getCurrency().equalsIgnoreCase(transaction.getCurrrency())){
            throw new IllegalStateException("Payment currency mismatch");
        }
    }

    private void validateStatus(PaystackVerifyData transaction){

        if (!"success".equalsIgnoreCase(transaction.getStatus())){
            throw new IllegalStateException("Paystack transaction is not successful");
        }

    }


}
