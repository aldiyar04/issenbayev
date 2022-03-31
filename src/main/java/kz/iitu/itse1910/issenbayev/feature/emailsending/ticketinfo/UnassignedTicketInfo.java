package kz.iitu.itse1910.issenbayev.feature.emailsending.ticketinfo;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class UnassignedTicketInfo {
    private final String leadDevEmail;
    private final String projectName;
    private final String title;
    private final String type;
    private final String priority;
    private final LocalDate targetResolutionDate;
    private final LocalDateTime createdAt;
}
