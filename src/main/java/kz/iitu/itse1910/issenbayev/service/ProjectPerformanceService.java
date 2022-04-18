package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.kafka.response.ProjectPerformanceResponse;
import kz.iitu.itse1910.issenbayev.repository.ProjectPerformanceRepository;
import kz.iitu.itse1910.issenbayev.repository.entity.ProjectPerformance;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectPerformanceService {
    private final ProjectPerformanceRepository repository;

    public List<ProjectPerformanceResponse> getProjectsPerformances() {
        List<ProjectPerformance> projectPerformances = repository.findAll();
        return projectPerformances.stream()
                .filter(projectPerformance -> projectPerformance.getTicketsPerformance() != null)
                .map(projectPerformance -> {
                    long ticketsPerformance = projectPerformance.getTicketsPerformance();
                    long meanNumAssignees = projectPerformance.getMeanNumAssignees();
                    int performance = (int) (ticketsPerformance / meanNumAssignees);
                    return ProjectPerformanceResponse.builder()
                            .name(projectPerformance.getName())
                            .performance(performance)
                            .build();
                })
                .sorted((o1, o2) -> o2.getPerformance() - o1.getPerformance())
                .collect(Collectors.toList());
    }
}
