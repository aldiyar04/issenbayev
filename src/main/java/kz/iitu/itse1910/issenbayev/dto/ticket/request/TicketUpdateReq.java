package kz.iitu.itse1910.issenbayev.dto.ticket.request;

import kz.iitu.itse1910.issenbayev.dto.ticket.response.TicketDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TicketUpdateReq {
    private final String title;
    private final String description;
    private final String assignee;
    private final TicketDto.Type type;
    private final TicketDto.Status status;
    private final TicketDto.Priority priority;
    // TODO: add support for targetResolutionDate:
//    private final LocalDate targetResolutionDate;
}
