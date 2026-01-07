package com.bcal.o2o.mpos.client;

import com.bcal.o2o.mpos.sample.MposTransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
    name = "master-data-service",
    url = "${master-data-service.url}"
)
public interface MasterDataClient {

  @GetMapping("/internal/mpos-transactions")
  List<MposTransactionDto> getAllMposTransactions();
}
