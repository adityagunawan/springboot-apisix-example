package com.bcal.o2o.masterdata.cmsitem;

import java.time.LocalDate;

public class CmsItemDto {

  private Long id;
  private String title;
  private String status;
  private LocalDate publishDate;

  public CmsItemDto() {
  }

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(LocalDate publishDate) {
    this.publishDate = publishDate;
  }
}
