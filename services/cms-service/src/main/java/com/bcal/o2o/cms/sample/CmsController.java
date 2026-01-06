package com.bcal.o2o.cms.sample;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms")
public class CmsController {

  private final CmsItemRepository cmsItemRepository;

  public CmsController(CmsItemRepository cmsItemRepository) {
    this.cmsItemRepository = cmsItemRepository;
  }

  @GetMapping("/items")
  public List<CmsItemDto> listItems() {
    return cmsItemRepository.findAll().stream().map(CmsItemDto::fromEntity).toList();
  }
}
