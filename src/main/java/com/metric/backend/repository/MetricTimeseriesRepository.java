package com.metric.backend.repository;

import com.metric.backend.model.MetricTimeseries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricTimeseriesRepository extends JpaRepository<MetricTimeseries, Long> {}
