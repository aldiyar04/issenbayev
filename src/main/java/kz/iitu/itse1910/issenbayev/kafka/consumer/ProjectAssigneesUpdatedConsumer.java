package kz.iitu.itse1910.issenbayev.kafka.consumer;

import kz.iitu.itse1910.issenbayev.kafka.KafkaConfig;
import kz.iitu.itse1910.issenbayev.kafka.eventdto.ProjectAssigneesUpdatedDto;
import kz.iitu.itse1910.issenbayev.repository.ProjectPerformanceRepository;
import kz.iitu.itse1910.issenbayev.repository.entity.ProjectPerformance;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ProjectAssigneesUpdatedConsumer {
    private final ProjectPerformanceRepository repository;

    @KafkaListener(topics = KafkaConfig.TopicNames.PROJECTS_ASSIGNEES_UPDATES, groupId = "group-0")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateProjectPerformanceNumAssignees(@Payload ProjectAssigneesUpdatedDto projectAssigneesUpdatedDto) {
        String projectName = projectAssigneesUpdatedDto.getProjectName();
        Optional<ProjectPerformance> projectPerformanceOptional = repository.findByName(projectName);
        if (projectPerformanceOptional.isEmpty()) {
            ProjectPerformance projectPerformance = buildNewProjectPerformance(projectName);
            repository.save(projectPerformance);
            return;
        }
        ProjectPerformance projectPerformance = projectPerformanceOptional.get();
        setNumAssignees(projectPerformance, projectAssigneesUpdatedDto.getWasAssignedOrUnassigned());
        setMeanNumAssignees(projectPerformance);
        repository.save(projectPerformance);
    }

    private ProjectPerformance buildNewProjectPerformance(String projectName) {
        return ProjectPerformance.builder()
                .name(projectName)
                .meanNumAssignees(1L)
                .meanNumAssigneesUpdatedAt(LocalDateTime.now())
                .numAssignees(1)
                .ticketsPerformance(0L)
                .build();
    }

    private void setNumAssignees(ProjectPerformance projectPerformance, boolean wasAssigned) {
        int newNumAssignees = wasAssigned ?
                projectPerformance.getNumAssignees() + 1 :
                projectPerformance.getNumAssignees() - 1;
        projectPerformance.setNumAssignees(newNumAssignees);
    }

    private void setMeanNumAssignees(ProjectPerformance projectPerformance) {
        long oldTimeWeightedSumOfNumsAssignees = getOldTimeWeightedSumOfNumsAssignees(projectPerformance);
        long newTimeWeightedSumOfNumsAssignees = calcNewTimeWeightedSumOfNumsAssignees(
                oldTimeWeightedSumOfNumsAssignees, projectPerformance);
        long projectLifetimeMinutes = Duration.between(projectPerformance.getCreatedAt(), LocalDateTime.now())
                .toMinutes();
        if (projectLifetimeMinutes == 0) {
            return;
        }
        long newMeanNumAssignees = newTimeWeightedSumOfNumsAssignees / projectLifetimeMinutes;

        projectPerformance.setMeanNumAssignees(newMeanNumAssignees);
        projectPerformance.setMeanNumAssigneesUpdatedAt(LocalDateTime.now());
    }

    private long getOldTimeWeightedSumOfNumsAssignees(ProjectPerformance projectPerformance) {
        long oldMeanNumAssignees = projectPerformance.getMeanNumAssignees();
        long oldProjectLifetimeMinutes = Duration.between(projectPerformance.getCreatedAt(),
                projectPerformance.getMeanNumAssigneesUpdatedAt()).toMinutes();
        return oldProjectLifetimeMinutes * oldMeanNumAssignees;
    }

    private long calcNewTimeWeightedSumOfNumsAssignees(long oldTimeWeightedSumOfNumsAssignees,
                                                       ProjectPerformance projectPerformance) {
        long numAssignees = projectPerformance.getNumAssignees();
        long timeWeightForNumAssignees = Duration.between(projectPerformance.getMeanNumAssigneesUpdatedAt(),
                LocalDateTime.now()).toMinutes();
        long timeWeightedNumAssignees = numAssignees * timeWeightForNumAssignees;
        return oldTimeWeightedSumOfNumsAssignees + timeWeightedNumAssignees;
    }

    @Scheduled(fixedDelay = 60_000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateMeanNumAssigneesForAllProjects() {
        List<ProjectPerformance> projectPerformances = repository.findAll();
        projectPerformances.forEach(this::setMeanNumAssignees);
        repository.saveAll(projectPerformances);
    }
}
