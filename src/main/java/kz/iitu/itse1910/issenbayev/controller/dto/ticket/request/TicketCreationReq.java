package kz.iitu.itse1910.issenbayev.controller.dto.ticket.request;

import kz.iitu.itse1910.issenbayev.controller.dto.ticket.response.TicketDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TicketCreationReq {
    private final String title;
    private final String description;
    private final Long submitterId;
    private final TicketDto.Type type;
    private final TicketDto.Status status;
    private final TicketDto.Priority priority;
}
