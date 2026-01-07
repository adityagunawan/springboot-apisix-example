package com.bcal.o2o.masterdata.controllers;

import com.bcal.o2o.masterdata.dto.MposTransactionDto;
import com.bcal.o2o.masterdata.entities.repositories.MposTransactionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/mpos-transactions")
public class InternalMposTransactionController {

  private final MposTransactionRepository repository;

  public InternalMposTransactionController(MposTransactionRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<MposTransactionDto> getAllTransactions() {
    return repository.findAll()
      .stream()
      .map(MposTransactionDto::fromEntity)
      .collect(Collectors.toList());
  }
}
