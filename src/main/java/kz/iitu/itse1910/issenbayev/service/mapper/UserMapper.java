package kz.iitu.itse1910.issenbayev.service.mapper;

import kz.iitu.itse1910.issenbayev.controller.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.controller.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User entity);
    User signupReqToEntity(UserSignupReq signupReq);

    UserDto.Role toDtoRole(User.Role entityRole);
    User.Role toEntityRole(UserDto.Role dtoRole);
}
