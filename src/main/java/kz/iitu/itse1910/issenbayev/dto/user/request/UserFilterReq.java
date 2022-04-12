package kz.iitu.itse1910.issenbayev.dto.user.request;

import kz.iitu.itse1910.issenbayev.controller.api.UserApi;
import kz.iitu.itse1910.issenbayev.dto.RequestParamName;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import lombok.Data;

@Data
public class UserFilterReq {
    @RequestParamName(UserApi.Filter.ROLE)
    private UserDto.Role role;

    @RequestParamName(UserApi.Filter.IS_ASSIGNED_TO_PROJECT)
    private Boolean isAssignedToProject;
}
