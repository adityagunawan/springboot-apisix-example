package com.bcal.o2o.mpos.sample;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mpos")
public class MposController {

  private final MposTransactionRepository transactionRepository;

  public MposController(MposTransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  @GetMapping("/transactions")
  public List<MposTransactionDto> listTransactions() {
    return transactionRepository.findAll().stream().map(MposTransactionDto::fromEntity).toList();
  }
}
