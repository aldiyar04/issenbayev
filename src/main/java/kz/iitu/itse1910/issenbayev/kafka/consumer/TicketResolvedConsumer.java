package kz.iitu.itse1910.issenbayev.kafka.consumer;

import kz.iitu.itse1910.issenbayev.controller.dto.ticket.response.TicketDto;
import kz.iitu.itse1910.issenbayev.kafka.KafkaConfig;
import kz.iitu.itse1910.issenbayev.kafka.eventdto.TicketResolvedDto;
import kz.iitu.itse1910.issenbayev.repository.ProjectPerformanceRepository;
import kz.iitu.itse1910.issenbayev.repository.entity.ProjectPerformance;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TicketResolvedConsumer {
    private final ProjectPerformanceRepository projectPerformanceRepository;

    @KafkaListener(topics = KafkaConfig.TopicNames.RESOLVED_TICKETS, groupId = "group-0")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateProjectTicketsPerformance(@Payload TicketResolvedDto ticketResolvedDto) {
        ProjectPerformance projectPerformance = projectPerformanceRepository
                .findByName(ticketResolvedDto.getProjectName())
                .orElseThrow(() -> new IllegalStateException("Project performance must be created before " +
                        " a ticket of it gets resolved"));
//        String projectName = ticketResolvedDto.getProjectName();
//        Optional<ProjectPerformance> projectPerformanceOptional = projectPerformanceRepository
//                .findByName(projectName);
//        if (projectPerformanceOptional.isEmpty()) {
//            ProjectPerformance projectPerformance = buildNewProjectPerformance(projectName);
//            projectPerformanceRepository.save(projectPerformance);
//            return;
//        }
//        ProjectPerformance projectPerformance = projectPerformanceOptional.get();
        long oldTicketsPerformances = projectPerformance.getTicketsPerformance();
        long newTicketsPerformances = oldTicketsPerformances + getTicketPerformance(ticketResolvedDto);
        projectPerformance.setTicketsPerformance(newTicketsPerformances);
        projectPerformanceRepository.save(projectPerformance);
    }

//    private ProjectPerformance buildNewProjectPerformance(String projectName) {
//        return ProjectPerformance.builder()
//                .name(projectName)
//                .meanNumAssignees(0L)
//                .meanNumAssigneesUpdatedAt(LocalDateTime.now())
//                .numAssignees(0)
//                .ticketsPerformance(0L)
//                .build();
//    }

    private int getTicketPerformance(TicketResolvedDto ticketResolvedDto) {
        int ticketResolutionSpeed = getTicketResolutionSpeed(ticketResolvedDto);
        int ticketPriorityCoeff = ticketPriorityToCoefficient(ticketResolvedDto.getPriority());
        return ticketResolutionSpeed * ticketPriorityCoeff;
    }

    private int getTicketResolutionSpeed(TicketResolvedDto ticketResolvedDto) {
        int actualDays = Period.between(ticketResolvedDto.getCreatedAt().toLocalDate(),
                LocalDate.now()).getDays();
        int targetDays = Period.between(ticketResolvedDto.getCreatedAt().toLocalDate(),
                ticketResolvedDto.getTargetResolutionDate()).getDays();
        int speedInverse = actualDays / targetDays;
        return 1 / (1 + speedInverse);
    }

    private int ticketPriorityToCoefficient(TicketDto.Priority priority) {
        switch (priority) {
            case NONE:
                return 0;
            case LOW:
                return 1;
            case MEDIUM:
                return 2;
            case HIGH:
                return 3;
            case CRITICAL:
                return 4;
            default:
                throw new IllegalArgumentException("Not all possible priority enum values considered in the switch statement.");
        }

    }
}
