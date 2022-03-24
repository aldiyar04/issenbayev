package kz.iitu.itse1910.issenbayev.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserUpdateReq {
    private final Long id;
    private final String newRole;
    private final String newEmail;
    private final String newUsername;
}
