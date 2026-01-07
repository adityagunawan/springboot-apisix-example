package com.bcal.o2o.mpos.sample;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MposTransactionDto(
    Long id,
    String reference,
    BigDecimal amount,
    String status,
    LocalDate businessDate) {
}
