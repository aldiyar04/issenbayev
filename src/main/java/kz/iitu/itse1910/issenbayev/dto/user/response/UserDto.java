package kz.iitu.itse1910.issenbayev.dto.user.response;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class UserDto {
    private final Long id;
    private final String role;
    private final String email;
    private final String username;
    private final LocalDate createdOn;

    public static class Field {
        public static final String ID = "id";
        public static final String ROLE = "role";
        public static final String EMAIL = "email";
        public static final String USERNAME = "username";
        public static final String CREATED_ON = "created-on";
    }

    public static class Filter {
        public static final String IS_ASSIGNED_TO_PROJECT = "is-assigned-to-project";
    }

    public static class Role {
        public static final String ADMIN = "admin";
        public static final String MANAGER = "manager";
        public static final String LEAD_DEV = "lead-dev";
        public static final String DEVELOPER = "developer";
    }
}
