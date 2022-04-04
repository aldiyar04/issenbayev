package kz.iitu.itse1910.issenbayev.dto.project.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ProjectResp {
    private final Long id;
    private final String name;
    private final String description;
    private final String leadDevUname;
    private final String createdOn;
    private final String updatedOn;

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_LEAD_DEV_UNAME = "lead-dev-username";
    public static final String FIELD_CREATED_ON = "created-on";
    public static final String FIELD_UPDATED_ON = "updated-on";
}
