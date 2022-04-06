package kz.iitu.itse1910.issenbayev.dto.ticket.request;

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
    private final String type;
    private final String status;
    private final String priority;
}
