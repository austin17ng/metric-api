package com.metric.backend.service;

import com.metric.backend.dto.MetricPointDto;
import com.metric.backend.dto.ReportDto;
import com.metric.backend.dto.ReportRequestDto;
import com.metric.backend.model.*;
import com.metric.backend.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
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

    public List<MetricPointDto> getMetricPoints(Long reportId, String metricType) {
        List<MetricTimeseries> metrics = metricTimeseriesRepository.findByReportIdAndMetricType(reportId, metricType);

        return metrics.stream()
                .map(m -> new MetricPointDto(m.getTimestamp(), m.getValue()))
                .sorted(Comparator.comparing(MetricPointDto::getTimestamp))
                .collect(Collectors.toList());
    }

    public MetricPointDto getLatestMetricPoint(Long reportId, String metricType) {
        MetricSingleValue obj = metricSingleValueRepository.findByReportIdAndMetricType(reportId, metricType);
        if (obj == null) {
            throw new NoSuchElementException("No metric found for reportId=" + reportId + " and metricType=" + metricType);
        }
        return new MetricPointDto(null, obj.getValue());
    }

    public List<ReportDto> getReportsByPackageName(String packageName) {
        return reportRepository.findByApplication_PackageName(packageName)
                .stream()
                .map(report -> new ReportDto(report.getId(), report.getAppVersion(), report.getReportedAt()))
                .toList();
    }
}