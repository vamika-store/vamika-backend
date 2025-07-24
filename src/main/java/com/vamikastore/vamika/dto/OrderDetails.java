package com.vamikastore.vamika.dto;

import com.vamikastore.vamika.entities.Address;
import com.vamikastore.vamika.entities.OrderItem;
import com.vamikastore.vamika.entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetails {

    private UUID id;
    private Date orderDate;
    private Address address;
    private Double totalAmount;
    private OrderStatus orderStatus;
    private String shippingTrackingNumber;
    private Date expectedDeliveryDate;
    private List<OrderItemDetails> orderItemList;
}
