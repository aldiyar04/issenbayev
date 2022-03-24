package kz.iitu.itse1910.issenbayev.helper.mapper;

import kz.iitu.itse1910.issenbayev.dto.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.response.UserResp;
import kz.iitu.itse1910.issenbayev.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResp toResponse(User user);
    User toEntity(UserSignupReq signupReq);
}
