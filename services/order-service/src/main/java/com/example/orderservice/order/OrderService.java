package com.example.orderservice.order;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public List<OrderDto> list() {
    return orderRepository.findAll().stream().map(OrderDto::fromEntity).toList();
  }

  public OrderDto get(long id) {
    return orderRepository.findById(id)
        .map(OrderDto::fromEntity)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
  }

  public OrderDto create(OrderRequest request) {
    OrderEntity entity = new OrderEntity();
    entity.setUserId(request.userId());
    entity.setTotal(request.total());
    entity.setOrderDate(request.date());
    entity.setStatus(request.status() != null ? request.status() : "NEW");
    return OrderDto.fromEntity(orderRepository.save(entity));
  }

  public OrderDto update(long id, OrderRequest request) {
    OrderEntity entity = orderRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    entity.setUserId(request.userId());
    entity.setTotal(request.total());
    entity.setOrderDate(request.date());
    entity.setStatus(request.status() != null ? request.status() : entity.getStatus());
    return OrderDto.fromEntity(orderRepository.save(entity));
  }

  public void delete(long id) {
    OrderEntity entity = orderRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    orderRepository.delete(entity);
  }
}
