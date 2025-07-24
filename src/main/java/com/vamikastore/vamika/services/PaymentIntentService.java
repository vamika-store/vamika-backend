package com.vamikastore.vamika.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.vamikastore.vamika.auth.entities.User;
import com.vamikastore.vamika.entities.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentIntentService {

    public Map<String, String> createPaymentIntent(Order order) throws StripeException {
        User user = order.getUser();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("orderId", order.getId().toString());
        PaymentIntentCreateParams paymentIntentCreateParams = PaymentIntentCreateParams.builder()
                .setAmount((long) (order.getTotalAmount() * 100 * 80))// USD cents, assuming 1 USD = 80 INR
                .setCurrency("inr") // Currency code for Indian Rupee
                .putAllMetadata(metadata)
                .setDescription("Test Payment Project -1")
                .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentCreateParams);
        Map<String, String> map = new HashMap<>();
        map.put("clientSecret", paymentIntent.getClientSecret());
        return map;
    }
}
