package kz.iitu.itse1910.issenbayev.controller.dto.user.response;


import com.fasterxml.jackson.annotation.JsonValue;
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
    private final Role role;
    private final String email;
    private final String username;
    private final LocalDate createdOn;

    public static class Field {
        public static final String ID = "id";
        public static final String ROLE = "role";
        public static final String EMAIL = "email";
        public static final String USERNAME = "username";
        public static final String CREATED_ON = "createdOn";
    }

    public enum Role {
        ADMIN("Admin"), MANAGER("Manager"), LEAD_DEV("Lead Dev"), DEVELOPER("Developer");

        private final String role;

        Role(String role) {
            this.role = role;
        }

        @Override
        @JsonValue
        public String toString() {
            return role;
        }
    }
}
