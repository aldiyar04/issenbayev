package kz.iitu.itse1910.issenbayev.controller.stringtoenumconverter;

import kz.iitu.itse1910.issenbayev.controller.api.UserApi;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUserDtoRoleConverter implements Converter<String, UserDto.Role> {
    @Override
    public UserDto.Role convert(String source) {
        switch (source) {
            case UserApi.Role.DEVELOPER:
                return UserDto.Role.DEVELOPER;
            case UserApi.Role.LEAD_DEV:
                return UserDto.Role.LEAD_DEV;
            case UserApi.Role.MANAGER:
                return UserDto.Role.MANAGER;
            case UserApi.Role.ADMIN:
                return UserDto.Role.ADMIN;
            default:
                throw new IllegalArgumentException("Invalid user role: " + source);
//                // This exception is ignored by Spring, it simply returns 400 bad request without any message.
//                String exMsg = String.format("Role '%s' doesn't exist. Valid roles are: '%s', '%s', '%s', '%s'.", source,
//                        UserApi.Role.DEVELOPER, UserApi.Role.LEAD_DEV, UserApi.Role.MANAGER, UserApi.Role.ADMIN);
//                ApiExceptionDetailHolder exDetailHolder = ApiExceptionDetailHolder.builder()
//                        .message(exMsg)
//                        .build();
//                throw new ApiException(exDetailHolder);
        }
    }
}
