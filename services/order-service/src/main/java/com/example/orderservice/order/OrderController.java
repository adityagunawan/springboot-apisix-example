package com.example.orderservice.order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private static final List<Map<String, Object>> ORDERS = List.of(
      Map.of("id", 101, "userId", 1, "total", 49.99, "date", LocalDate.now().minusDays(1).toString()),
      Map.of("id", 102, "userId", 2, "total", 24.50, "date", LocalDate.now().toString())
  );

  @GetMapping
  public List<Map<String, Object>> listOrders() {
    return ORDERS;
  }

  @GetMapping("/{id}")
  public Map<String, Object> getOrder(@PathVariable int id) {
    Optional<Map<String, Object>> order =
        ORDERS.stream().filter(o -> o.get("id").equals(id)).findFirst();
    return order.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
  }
}
