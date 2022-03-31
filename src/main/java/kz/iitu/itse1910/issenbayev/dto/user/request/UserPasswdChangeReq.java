package kz.iitu.itse1910.issenbayev.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class UserPasswdChangeReq {
    private final Long id;
    private final String oldPassword;
    private final String newPassword;
}
