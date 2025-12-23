package com.example.orderservice.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderRequest(
    @NotNull @Positive Long userId,
    @NotNull @Positive BigDecimal total,
    @NotNull LocalDate date,
    String status
) {
}
