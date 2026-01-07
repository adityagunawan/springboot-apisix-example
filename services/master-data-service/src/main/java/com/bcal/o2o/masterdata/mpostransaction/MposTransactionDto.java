package com.bcal.o2o.masterdata.mpostransaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MposTransactionDto {

  private Long id;
  private String reference;
  private BigDecimal amount;
  private String status;
  private LocalDate businessDate;

  public MposTransactionDto() {
  }

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getBusinessDate() {
    return businessDate;
  }

  public void setBusinessDate(LocalDate businessDate) {
    this.businessDate = businessDate;
  }
}
