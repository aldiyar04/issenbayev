package kz.iitu.itse1910.issenbayev.kafka.producer;

import kz.iitu.itse1910.issenbayev.kafka.KafkaConfig;
import kz.iitu.itse1910.issenbayev.kafka.eventdto.ProjectAssigneesUpdatedDto;
import kz.iitu.itse1910.issenbayev.kafka.eventdto.TicketResolvedDto;
import kz.iitu.itse1910.issenbayev.repository.ProjectRepository;
import kz.iitu.itse1910.issenbayev.repository.TicketRepository;
import kz.iitu.itse1910.issenbayev.repository.entity.Project;
import kz.iitu.itse1910.issenbayev.repository.entity.Ticket;
import kz.iitu.itse1910.issenbayev.repository.entity.User;
import kz.iitu.itse1910.issenbayev.service.mapper.TicketMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class KafkaProducer implements CommandLineRunner {
    private final ProjectRepository projectRepository;
    private final KafkaTemplate<String, ProjectAssigneesUpdatedDto> projectAssigneesUpdatedKafkaTemplate;

    private final TicketRepository ticketRepository;
    private final KafkaTemplate<String, TicketResolvedDto> ticketResolvedKafkaTemplate;

    @Override
    public void run(String... args) {
        produceProjectsAssigneesUpdated();
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Thread interrupted while sleeping");
        }
    }

    private void produceProjectsAssigneesUpdated() {
        List<Project> projects = projectRepository.findAllWithAssignees();
        for (Project project : projects) {
            for (User assignee : project.getAssignees()) {
                boolean gotAssigned = true;
                ProjectAssigneesUpdatedDto projectAssigneesUpdatedDto = ProjectAssigneesUpdatedDto.builder()
                        .projectName(project.getName())
                        .assigneeUsername(assignee.getUsername())
                        .wasAssignedOrUnassigned(gotAssigned)
                        .build();
                projectAssigneesUpdatedKafkaTemplate.send(KafkaConfig.TopicNames.PROJECTS_ASSIGNEES_UPDATES,
                        projectAssigneesUpdatedDto);
            }
        }
    }

//    private void produceTicketsResolved() {
//        List<Ticket> tickets = ticketRepository.findAll();
//        resolveTickets(tickets);
//        for (Ticket ticket : tickets) {
//            TicketResolvedDto ticketResolvedDto = TicketMapper.INSTANCE.entityToResolvedTicketDto(ticket);
//            ticketResolvedKafkaTemplate.send(KafkaConfig.TopicNames.RESOLVED_TICKETS, ticketResolvedDto);
//        }
//    }
//
//    private void resolveTickets(List<Ticket> tickets) {
//        for (Ticket ticket : tickets) {
//            ticket.setStatus(Ticket.Status.RESOLVED);
//        }
//        ticketRepository.saveAll(tickets);
//    }
}
