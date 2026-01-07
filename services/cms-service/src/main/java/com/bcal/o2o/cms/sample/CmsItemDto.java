package com.bcal.o2o.cms.sample;

import java.time.LocalDate;

public record CmsItemDto(Long id, String title, String status, LocalDate publishDate) {
}
