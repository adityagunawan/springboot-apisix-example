package com.bcal.o2o.masterdata.dto;

import com.bcal.o2o.masterdata.entities.MposTransactionEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MposTransactionDto {

  private Long id;
  private String reference;
  private BigDecimal amount;
  private String status;
  private LocalDate businessDate;

  public MposTransactionDto(Long id, String reference, BigDecimal amount, String status, LocalDate businessDate) {
    this.id = id;
    this.reference = reference;
    this.amount = amount;
    this.status = status;
    this.businessDate = businessDate;
  }

  public static MposTransactionDto fromEntity(MposTransactionEntity entity) {
    return new MposTransactionDto(
      entity.getId(),
      entity.getReference(),
      entity.getAmount(),
      entity.getStatus(),
      entity.getBusinessDate()
    );
  }
}
