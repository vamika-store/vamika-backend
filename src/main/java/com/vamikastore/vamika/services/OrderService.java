package com.vamikastore.vamika.services;

import com.stripe.exception.StripeException;
import com.vamikastore.vamika.auth.dto.OrderResponse;
import com.vamikastore.vamika.auth.entities.User;
import com.vamikastore.vamika.dto.OrderRequest;
import com.vamikastore.vamika.entities.*;
import com.vamikastore.vamika.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
public class OrderService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ProductService productService;

    @Autowired
    PaymentIntentService paymentIntentService;


    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, Principal principal) throws StripeException {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Address address = user.getAddressList().stream().filter(address1 -> address1.getId().equals(orderRequest.getAddressId())).findFirst().orElseThrow(() -> new RuntimeException("Address not found"));
        Order order = Order.builder()
                .user(user)
                .address(address)
                .totalAmount(orderRequest.getTotalAmount())
                .orderDate(orderRequest.getOrderDate())
                .discount(orderRequest.getDiscount())
                .expectedDeliveryDate(String.valueOf(orderRequest.getExpectedDeliveryDate()))
                .paymentMethod(orderRequest.getPaymentMethod())
                .orderStatus(OrderStatus.PENDING)
                .build();
        List<OrderItem> orderItems = orderRequest.getOrderItemRequests().stream().map(orderItemRequest -> {
            try {
                Product product = productService.fetchProductById(orderItemRequest.getProductId());
                OrderItem orderItem = OrderItem.builder()
                        .product(product)
                        .productVariantId(orderItemRequest.getProductVariantId())
                        .quantity(orderItemRequest.getQuantity())
                        .order(order)
                        .build();
                return orderItem;
            } catch (Exception e) {
                throw new RuntimeException("Product not found with ID: " + orderItemRequest.getProductId(), e);
            }

        }).toList();
        order.setOrderItems(orderItems);
        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(new Date());
        payment.setOrder(order);
        payment.setAmount(orderRequest.getTotalAmount());
        payment.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);

        OrderResponse orderResponse = OrderResponse.builder()
                .paymentMethod(orderRequest.getPaymentMethod())
                .id(savedOrder.getId())
                .build();
        if(Objects.equals(orderRequest.getPaymentMethod(), "CARD")) {
            orderResponse.setCredentials(paymentIntentService.createPaymentIntent(order));
        }
        return orderResponse;
    }
}
