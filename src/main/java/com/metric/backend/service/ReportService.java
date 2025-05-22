package com.metric.backend.service;

import com.metric.backend.dto.ReportRequestDto;
import com.metric.backend.model.*;
import com.metric.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Long saveReport(ReportRequestDto dto) {
        // 1. Upsert Application by packageName
        Application application = applicationRepository.findById(dto.getPackageName())
                .orElseGet(() -> {
                    Application app = new Application();
                    app.setPackageName(dto.getPackageName());
                    app.setAppName(dto.getAppName());
                    return applicationRepository.save(app);
                });

        // 2. Upsert Device by deviceId
        Device device = deviceRepository.findByDeviceId(dto.getDeviceId())
                .orElseGet(() -> {
                    Device dev = new Device();
                    dev.setDeviceId(dto.getDeviceId());
                    dev.setDeviceName(dto.getDeviceName());
                    dev.setPlatform(dto.getPlatform());
                    return deviceRepository.save(dev);
                });

        // 3. Create Report (new each time)
        Report report = new Report();
        report.setApplication(application);
        report.setDevice(device);
        report.setAppVersion(dto.getAppVersion());
        report.setReportedAt(dto.getReportedAt());

        // Step 4: Add metric single values
        List<MetricSingleValue> singleValues = dto.getMetricsSingleValue().stream()
                .map(metric -> {
                    MetricSingleValue m = new MetricSingleValue();
                    m.setMetricType(metric.getMetricType());
                    m.setValue(metric.getValue());
                    m.setReport(report); // set back-reference
                    return m;
                }).collect(Collectors.toList());

        report.setMetricSingleValues(singleValues);

        // Step 5: Add metric time series
        List<MetricTimeseries> timeseries = dto.getMetricsTimeSeries().stream()
                .map(metric -> {
                    MetricTimeseries m = new MetricTimeseries();
                    m.setMetricType(metric.getMetricType());
                    m.setValue(metric.getValue());
                    m.setTimestamp(metric.getTimestamp());
                    m.setReport(report); // set back-reference
                    return m;
                }).collect(Collectors.toList());

        report.setMetricTimeseries(timeseries);

        // Step 6: Save Report
        Report savedReport = reportRepository.save(report);
        return savedReport.getId();
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    public boolean deleteReportById(Long id) {
        return reportRepository.findById(id).map(r -> {
            reportRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    public boolean deleteByDeviceId(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId).map(device -> {
            deviceRepository.delete(device);
            return true;
        }).orElse(false);
    }
}
