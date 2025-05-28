package com.metric.backend.repository;

import com.metric.backend.model.MetricTimeseries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricTimeseriesRepository extends JpaRepository<MetricTimeseries, Long> {
    List<MetricTimeseries> findByReportIdAndMetricType(Long reportId, String metricType);
}
