package kz.iitu.itse1910.issenbayev.kafka.eventdto;

import kz.iitu.itse1910.issenbayev.controller.dto.ticket.response.TicketDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class TicketResolvedDto {
    private String projectName;
    private String title;
    private String assigneeUsername;
    private String submitterUsername;
    private TicketDto.Type type;
    private TicketDto.Priority priority;
    private LocalDate targetResolutionDate;
    private LocalDateTime createdAt;

    public static class Field {
        public static final String TITLE = "title";
        public static final String PROJECT_NAME = "projectName";
        public static final String ASSIGNEE_USERNAME = "assigneeUsername";
        public static final String SUBMITTER_USERNAME = "submitterUsername";
        public static final String TYPE = "type";
        public static final String PRIORITY = "priority";
        public static final String TARGET_RES_DATE = "targetResolutionDate";
        public static final String CREATED_AT = "createdAt";
    }
}
