package kz.iitu.itse1910.issenbayev.dto.user.response;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@Getter
@ToString
public class UserResp {
    private final Long id;
    private final String role;
    private final String email;
    private final String username;
    private final LocalDate createdOn;

    public static final String FIELD_ID = "id";
    public static final String FIELD_ROLE = "role";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_CREATED_ON = "created-on";
}
