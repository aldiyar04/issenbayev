package kz.iitu.itse1910.issenbayev.controller.compoundrequestparam;

import kz.iitu.itse1910.issenbayev.controller.api.UserApi;
import kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.annotation.RequestParamName;
import kz.iitu.itse1910.issenbayev.controller.dto.user.response.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.AssertTrue;

@AllArgsConstructor
@Getter
@ToString
public class UserFilterReq {
    @RequestParamName(UserApi.Filter.ROLE)
    private final UserDto.Role role;

    @RequestParamName(UserApi.Filter.IS_ASSIGNED_TO_PROJECT)
    private final Boolean isAssignedToProject;

    @AssertTrue(message = UserApi.Filter.IS_ASSIGNED_TO_PROJECT + " can be used only if " + UserApi.Filter.ROLE
    + " is specified as '" + UserApi.Role.LEAD_DEV + "' or '" + UserApi.Role.DEVELOPER + "'")
    public boolean isValidCombinationOfFields() {
        return isAssignedToProject == null || (role != null && isRoleAppropriateWhenIsAssignedToProjectPresent(role));
    }

    private boolean isRoleAppropriateWhenIsAssignedToProjectPresent(UserDto.Role role) {
        return role.equals(UserDto.Role.LEAD_DEV) || role.equals(UserDto.Role.DEVELOPER);
    }
}
