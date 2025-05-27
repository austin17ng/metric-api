package com.metric.backend.service;

import com.metric.backend.dto.ApplicationDto;
import com.metric.backend.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<ApplicationDto> getAllApplications() {
        return applicationRepository.findAll()
                .stream()
                .map(app -> new ApplicationDto(app.getPackageName(), app.getAppName()))
                .toList();
    }
}
