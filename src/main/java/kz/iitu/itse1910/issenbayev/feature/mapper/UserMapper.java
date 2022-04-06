package kz.iitu.itse1910.issenbayev.feature.mapper;

import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    @Mapping(target = "password", source = "password")
    User toEntity(UserSignupReq signupReq);
}
