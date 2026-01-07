package com.bcal.o2o.cms.client;

import com.bcal.o2o.cms.sample.CmsItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
    name = "master-data-service",
    url = "${master-data-service.url}"
)
public interface MasterDataClient {

  @GetMapping("/internal/cms-items")
  List<CmsItemDto> getAllCmsItems();
}
