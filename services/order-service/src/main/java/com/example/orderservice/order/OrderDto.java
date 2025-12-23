package com.example.orderservice.order;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderDto(Long id, Long userId, BigDecimal total, LocalDate date, String status) {
  public static OrderDto fromEntity(OrderEntity entity) {
    return new OrderDto(
        entity.getId(),
        entity.getUserId(),
        entity.getTotal(),
        entity.getOrderDate(),
        entity.getStatus());
  }
}
