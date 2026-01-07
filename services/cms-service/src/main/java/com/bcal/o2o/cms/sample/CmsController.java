package com.bcal.o2o.cms.sample;

import com.bcal.o2o.cms.client.MasterDataClient;
import feign.FeignException;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms")
public class CmsController {

  private static final Logger log = LoggerFactory.getLogger(CmsController.class);
  private final MasterDataClient masterDataClient;

  public CmsController(MasterDataClient masterDataClient) {
    this.masterDataClient = masterDataClient;
  }

  @GetMapping("/items")
  public ResponseEntity<?> listItems() {
    try {
      List<CmsItemDto> items = masterDataClient.getAllCmsItems();
      return ResponseEntity.ok(items);
    } catch (FeignException.ServiceUnavailable e) {
      log.error("Master data service unavailable", e);
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
          .body(Collections.singletonMap("error", "Master data service is currently unavailable"));
    } catch (FeignException e) {
      log.error("Error calling master data service: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Collections.singletonMap("error", "Failed to fetch CMS items"));
    }
  }
}
