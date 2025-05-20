package com.metric.backend.service;

import com.metric.backend.dto.FullReportRequest;
import com.metric.backend.model.*;
import com.metric.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private final ApplicationRepository applicationRepository;
    private final DeviceRepository deviceRepository;
    private final ReportRepository reportRepository;
    private final MetricTimeseriesRepository metricTimeseriesRepository;
    private final MetricSingleValueRepository metricSingleValueRepository;

    public ReportService(ApplicationRepository applicationRepository, DeviceRepository deviceRepository, ReportRepository reportRepository, MetricTimeseriesRepository metricTimeseriesRepository, MetricSingleValueRepository metricSingleValueRepository) {
        this.applicationRepository = applicationRepository;
        this.deviceRepository = deviceRepository;
        this.reportRepository = reportRepository;
        this.metricTimeseriesRepository = metricTimeseriesRepository;
        this.metricSingleValueRepository = metricSingleValueRepository;
    }

    @Transactional
    public Report createOrUpdateFullReport(FullReportRequest dto) {
        // 1. Upsert Application by packageName
        Application application = applicationRepository.findById(dto.getPackageName())
                .orElseGet(Application::new);
        application.setPackageName(dto.getPackageName());
        application.setAppName(dto.getAppName());
        applicationRepository.save(application);

        // 2. Upsert Device by deviceId
        Device device = deviceRepository.findByDeviceId(dto.getDeviceId())
                .orElseGet(Device::new);
        device.setDeviceId(dto.getDeviceId());
        device.setDeviceName(dto.getDeviceName());
        device.setPlatform(dto.getPlatform());
        device = deviceRepository.save(device);

        // 3. Create Report (new each time)
        final Report savedReport = new Report();
        savedReport.setPackageName(dto.getPackageName());
        savedReport.setAppVersion(dto.getAppVersion());
        savedReport.setReportedAt(dto.getReportedAt());
        savedReport.setDevice(device);
        reportRepository.save(savedReport);

        // 4. Save metric timeseries
        if (dto.getMetricsTimeSeries() != null) {
            List<MetricTimeseries> tsMetrics = dto.getMetricsTimeSeries().stream()
                    .map(m -> {
                        MetricTimeseries metric = new MetricTimeseries();
                        metric.setReport(savedReport);
                        metric.setMetricType(m.getMetricType());
                        metric.setValue(m.getValue());
                        metric.setTimestamp(m.getTimestamp());
                        return metric;
                    })
                    .toList();
            metricTimeseriesRepository.saveAll(tsMetrics);
        }

        // 5. Save metric single value
        if (dto.getMetricsSingleValue() != null) {
            List<MetricSingleValue> singleMetrics = dto.getMetricsSingleValue().stream()
                    .map(m -> {
                        MetricSingleValue metric = new MetricSingleValue();
                        metric.setReport(savedReport);
                        metric.setMetricType(m.getMetricType());
                        metric.setValue(m.getValue());
                        return metric;
                    })
                    .toList();
            metricSingleValueRepository.saveAll(singleMetrics);
        }

        return savedReport;
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }
}
