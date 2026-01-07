package com.bcal.o2o.masterdata.controllers;

import com.bcal.o2o.masterdata.dto.CmsItemDto;
import com.bcal.o2o.masterdata.entities.repositories.CmsItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/cms-items")
public class InternalCmsItemController {

  private final CmsItemRepository repository;

  public InternalCmsItemController(CmsItemRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<CmsItemDto> getAllItems() {
    return repository.findAll()
      .stream()
      .map(CmsItemDto::fromEntity)
      .collect(Collectors.toList());
  }
}
