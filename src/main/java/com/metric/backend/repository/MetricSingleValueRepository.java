package com.metric.backend.repository;

import com.metric.backend.model.MetricSingleValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricSingleValueRepository extends JpaRepository<MetricSingleValue, Long> {
    MetricSingleValue findByReportIdAndMetricType(Long reportId, String metricType);
}
