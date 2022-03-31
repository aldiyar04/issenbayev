package kz.iitu.itse1910.issenbayev.feature.tickethousekeeping;

import kz.iitu.itse1910.issenbayev.entity.Ticket;
import kz.iitu.itse1910.issenbayev.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class TicketHousekeepingScheduler {
    private final TicketRepository ticketRepository;

    @Scheduled(fixedRateString = "${scheduling.ticket-housekeeping.is-overdue.fixed-rate}")
    @Transactional
    public void updateIsOverdueForAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        // Imagine that's not nonsense but some operation with heavy workload
        tickets.forEach(t -> t.setOverdue(t.getTargetResolutionDate().isBefore(LocalDate.now())));
    }
}
