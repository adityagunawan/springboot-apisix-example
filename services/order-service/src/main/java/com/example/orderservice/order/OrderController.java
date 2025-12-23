package com.example.orderservice.order;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public List<OrderDto> listOrders() {
    return orderService.list();
  }

  @GetMapping("/{id}")
  public OrderDto getOrder(@PathVariable long id) {
    return orderService.get(id);
  }

  @PostMapping
  public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest request) {
    OrderDto created = orderService.create(request);
    return ResponseEntity
        .created(URI.create("/orders/" + created.id()))
        .body(com.example.orderservice.common.StandardResponse.success(
            201, "order created", created));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateOrder(@PathVariable long id, @Valid @RequestBody OrderRequest request) {
    OrderDto updated = orderService.update(id, request);
    return ResponseEntity.ok(com.example.orderservice.common.StandardResponse.success(
        200, "order updated", updated));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrder(@PathVariable long id) {
    orderService.delete(id);
    return ResponseEntity.ok(com.example.orderservice.common.StandardResponse.success(
        200, "order deleted", null));
  }
}
