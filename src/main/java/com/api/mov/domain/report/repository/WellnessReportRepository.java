package com.api.mov.domain.report.repository;

import com.api.mov.domain.report.entity.WellnessReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WellnessReportRepository extends JpaRepository<WellnessReport, Long> {
}
