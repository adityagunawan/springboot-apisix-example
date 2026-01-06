package com.bcal.o2o.cms.sample;

import java.time.LocalDate;

public record CmsItemDto(Long id, String title, String status, LocalDate publishDate) {
  public static CmsItemDto fromEntity(CmsItemEntity entity) {
    return new CmsItemDto(entity.getId(), entity.getTitle(), entity.getStatus(), entity.getPublishDate());
  }
}
