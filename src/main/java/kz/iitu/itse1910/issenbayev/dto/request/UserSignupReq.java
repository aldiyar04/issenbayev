package kz.iitu.itse1910.issenbayev.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class UserSignupReq {
    private final String email;
    private final String username;
    private final String password;
}
