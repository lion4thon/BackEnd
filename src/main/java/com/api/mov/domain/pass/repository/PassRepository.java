package com.api.mov.domain.pass.repository;

import com.api.mov.domain.pass.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassRepository extends JpaRepository<Pass,Long> {
}
