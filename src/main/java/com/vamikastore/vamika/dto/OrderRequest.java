package com.vamikastore.vamika.dto;

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
public class OrderRequest {

    private UUID id;
    private Date orderDate;
    private UUID AddressId;
    private List<OrderItemRequest> orderItemRequests;
    private Double totalAmount;
    private Double discount;
    private String paymentMethod;
    private Date expectedDeliveryDate;

}
