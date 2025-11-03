package com.api.mov.domain.facility.repository;

import com.api.mov.domain.facility.entity.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Page<Facility> findBySportId(Long sportId, Pageable pageable);
}
