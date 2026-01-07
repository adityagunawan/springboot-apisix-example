package com.bcal.o2o.masterdata.dto;

import com.bcal.o2o.masterdata.entities.CmsItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class CmsItemDto {

  private Long id;
  private String title;
  private String status;
  private LocalDate publishDate;

  public CmsItemDto(Long id, String title, String status, LocalDate publishDate) {
    this.id = id;
    this.title = title;
    this.status = status;
    this.publishDate = publishDate;
  }

  public static CmsItemDto fromEntity(CmsItemEntity entity) {
    return new CmsItemDto(
      entity.getId(),
      entity.getTitle(),
      entity.getStatus(),
      entity.getPublishDate()
    );
  }
}
