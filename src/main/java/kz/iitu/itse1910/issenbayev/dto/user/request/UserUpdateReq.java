package kz.iitu.itse1910.issenbayev.dto.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class UserUpdateReq {
    private final String role;
    private final String email;
    private final String username;
}
