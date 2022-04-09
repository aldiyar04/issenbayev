package kz.iitu.itse1910.issenbayev.dto.project.response;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class ProjectDto {
    private final Long id;
    private final String name;
    private final String description;
    private final Long leadDevId;
    private final String leadDevUname;
    private final String createdOn;
    private final String updatedOn;

    public static class Field {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String LEAD_DEV_UNAME = "lead-dev-username";
        public static final String CREATED_ON = "created-on";
        public static final String UPDATED_ON = "updated-on";
    }
}
