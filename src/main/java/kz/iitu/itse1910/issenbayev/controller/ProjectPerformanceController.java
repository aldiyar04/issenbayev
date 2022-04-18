package kz.iitu.itse1910.issenbayev.controller;

import kz.iitu.itse1910.issenbayev.kafka.response.ProjectPerformanceResponse;
import kz.iitu.itse1910.issenbayev.service.ProjectPerformanceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProjectPerformanceController {
    private final ProjectPerformanceService service;

    @GetMapping("/project-performances")
    public List<ProjectPerformanceResponse> getProjectsPerformances() {
        return service.getProjectsPerformances();
    }
}
