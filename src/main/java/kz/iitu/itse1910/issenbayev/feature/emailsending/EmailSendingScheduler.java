package kz.iitu.itse1910.issenbayev.feature.emailsending;

import kz.iitu.itse1910.issenbayev.feature.emailsending.ticketinfo.OverdueTicketInfo;
import kz.iitu.itse1910.issenbayev.feature.emailsending.ticketinfo.UnassignedTicketInfo;
import kz.iitu.itse1910.issenbayev.repository.TicketRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EmailSendingScheduler {
    private static final String CRON = "${scheduling.email-sending.ticket-reports.cron}";
    private final EmailSender emailSender;
    private final TicketRepository ticketRepository;

    public EmailSendingScheduler(EmailSender emailSender, TicketRepository ticketRepository) {
        this.emailSender = emailSender;
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(cron = CRON)
    public void sendUnassignedTicketsEmailsToLeadDevs() {
        List<UnassignedTicketInfo> ticketInfos = toUnassignedTicketInfos(ticketRepository.findUnassignedTicketInfos());
        for (String projectName: unassignedTicketCountsForProjects(ticketInfos).keySet()) {
            List<UnassignedTicketInfo> ticketsOfCurrentProject = ticketInfos.stream()
                    .filter(t -> projectName.equals(t.getProjectName()))
                    .collect(Collectors.toList());

            String leadDevEmail = ticketsOfCurrentProject.get(0).getLeadDevEmail();
            String subject = "Unassigned Tickets of Project " + projectName;
            String text = assembleUnassignedTicketsEmailText(ticketsOfCurrentProject);
            emailSender.sendSimpleMessage(leadDevEmail, subject, text);
        }
    }

    private List<UnassignedTicketInfo> toUnassignedTicketInfos(List<Object[]> unassignedTickets) {
        List<UnassignedTicketInfo> result = new ArrayList<>();
        unassignedTickets.forEach(ticketObjArr -> {
            UnassignedTicketInfo ticketInfo = UnassignedTicketInfo.builder()
                    .leadDevEmail((String) ticketObjArr[0])
                    .projectName((String) ticketObjArr[1])
                    .title((String) ticketObjArr[2])
                    .type((String) ticketObjArr[3])
                    .priority((String) ticketObjArr[4])
                    .targetResolutionDate((LocalDate) ticketObjArr[5])
                    .createdAt((LocalDateTime) ticketObjArr[6])
                    .build();
            result.add(ticketInfo);
        });
        return result;
    }

    private Map<String, Integer> unassignedTicketCountsForProjects(List<UnassignedTicketInfo> unassignedTickets) {
        Map<String, Integer> ticketCountsForProjects = new LinkedHashMap<>();
        unassignedTickets.forEach(ticket -> {
            int prevCount = ticketCountsForProjects.getOrDefault(ticket.getProjectName(), 0);
            ticketCountsForProjects.put(ticket.getProjectName(), prevCount + 1);
        });
        return ticketCountsForProjects;
    }

    private String assembleUnassignedTicketsEmailText(List<UnassignedTicketInfo> unassignedTickets) {
        StringBuilder result = new StringBuilder();
        result.append(unassignedTickets.size()).append(" tickets unassigned").append("\n");
        unassignedTickets.forEach(ticket -> {
            result.append("\n");
            result.append("Title: ").append(ticket.getTitle()).append("\n");
            result.append("Type: ").append(ticket.getType()).append("\n");
            result.append("Priority: ").append(ticket.getPriority()).append("\n");
            result.append("Target resolution date: ").append(ticket.getTargetResolutionDate()).append("\n");
            result.append("Created at: ").append(ticket.getCreatedAt()).append("\n");
        });
        return result.toString();
    }

    @Scheduled(cron = CRON)
    public void sendOverdueTicketsEmailsToLeadDevs() {
        List<OverdueTicketInfo> tickets = toOverdueTicketInfos(ticketRepository.findOverdueTicketInfos());
        for (String projectName: overdueTicketCountsForProjects(tickets).keySet()) {
            List<OverdueTicketInfo> ticketsOfCurrentProject = tickets.stream()
                    .filter(t -> projectName.equals(t.getProjectName()))
                    .collect(Collectors.toList());

            String leadDevEmail = ticketsOfCurrentProject.get(0).getLeadDevEmail();
            String subject = "Overdue Tickets of Project " + projectName;
            String text = assembleOverdueTicketsEmailText(ticketsOfCurrentProject);
            emailSender.sendSimpleMessage(leadDevEmail, subject, text);
        }
    }

    private List<OverdueTicketInfo> toOverdueTicketInfos(List<Object[]> overdueTicketInfos) {
        List<OverdueTicketInfo> result = new ArrayList<>();
        overdueTicketInfos.forEach(ticketObjArr -> {
            OverdueTicketInfo overdueTicketInfo = OverdueTicketInfo.builder()
                    .leadDevEmail((String) ticketObjArr[0])
                    .projectName((String) ticketObjArr[1])
                    .title((String) ticketObjArr[2])
                    .type((String) ticketObjArr[3])
                    .status((String) ticketObjArr[4])
                    .priority((String) ticketObjArr[5])
                    .targetResolutionDate((LocalDate) ticketObjArr[6])
                    .build();
            result.add(overdueTicketInfo);
        });
        return result;
    }

    private Map<String, Integer> overdueTicketCountsForProjects(List<OverdueTicketInfo> overdueTickets) {
        Map<String, Integer> ticketCountsForProjects = new LinkedHashMap<>();
        overdueTickets.forEach(ticket -> {
            int prevCount = ticketCountsForProjects.getOrDefault(ticket.getProjectName(), 0);
            ticketCountsForProjects.put(ticket.getProjectName(), prevCount + 1);
        });
        return ticketCountsForProjects;
    }

    private String assembleOverdueTicketsEmailText(List<OverdueTicketInfo> overdueTickets) {
        StringBuilder result = new StringBuilder();
        result.append(overdueTickets.size()).append(" tickets overdue").append("\n");
        overdueTickets.forEach(ticket -> {
            result.append("\n");
            result.append("Title: ").append(ticket.getTitle()).append("\n");
            result.append("Type: ").append(ticket.getType()).append("\n");
            result.append("Status: ").append(ticket.getStatus()).append("\n");
            result.append("Priority: ").append(ticket.getPriority()).append("\n");
            result.append("Target resolution date: ").append(ticket.getTargetResolutionDate()).append("\n");
        });
        return result.toString();
    }
}
