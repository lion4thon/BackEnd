package com.api.mov.domain.pass.repository;

import com.api.mov.domain.pass.entity.PassItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassItemRepository extends JpaRepository<PassItem, Long> {
}
