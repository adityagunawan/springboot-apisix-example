package com.bcal.o2o.masterdata.cmsitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsItemRepository extends JpaRepository<CmsItemEntity, Long> {
}
