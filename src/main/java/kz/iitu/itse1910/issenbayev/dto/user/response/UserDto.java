package kz.iitu.itse1910.issenbayev.dto.user.response;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@Getter
@ToString
public class UserDto {
    public static final String FIELD_ID = "id";
    public static final String FIELD_ROLE = "role";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_CREATED_ON = "created-on";

    public static final String FILTER_IS_ASSIGNED_TO_PROJECT = "is-assigned-to-project";

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_LEAD_DEV = "lead-dev";
    public static final String ROLE_DEVELOPER = "developer";

    private final Long id;
    private final String role;
    private final String email;
    private final String username;
    private final LocalDate createdOn;
}
