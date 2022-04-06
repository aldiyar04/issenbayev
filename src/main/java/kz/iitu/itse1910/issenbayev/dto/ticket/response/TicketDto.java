package kz.iitu.itse1910.issenbayev.dto.ticket.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class TicketDto {
    private final Long id;
    private final String title;
    private final String description;
    private final Long projectId;
    private final String projectName;
    private final Long assigneeId;
    private final String assigneeUsername;
    private final Long submitterId;
    private final String submitterUsername;
    private final String type;
    private final String status;
    private final String priority;
    private final LocalDate targetResolutionDate;
    private final LocalDate actualResolutionDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static class Field {
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String PROJECT_ID = "projectId";
        public static final String PROJECT_NAME = "projectName";
        public static final String ASSIGNEE_ID = "assigneeId";
        public static final String ASSIGNEE_USERNAME = "assigneeUsername";
        public static final String SUBMITTER_ID = "submitterId";
        public static final String SUBMITTER_USERNAME = "submitterUsername";
        public static final String TYPE = "type";
        public static final String STATUS = "status";
        public static final String PRIORITY = "priority";
        public static final String TARGET_RES_DATE = "targetResDate";
        public static final String ACTUAL_RES_DATE = "actualResDate";
        public static final String CREATED_AT = "createdAt";
        public static final String UPDATED_AT = "updatedAt";
    }
}
