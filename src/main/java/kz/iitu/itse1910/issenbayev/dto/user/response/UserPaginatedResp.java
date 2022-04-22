package kz.iitu.itse1910.issenbayev.dto.user.response;

import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.feature.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class UserPaginatedResp {
    private final List<UserDto> userDtos;
    private final int totalPages;

    public static UserPaginatedResp fromUserPage(Page<User> userPage) {
        List<UserDto> userDtos = userPage.getContent().stream()
                .map(UserMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
        return new UserPaginatedResp(userDtos, userPage.getTotalPages());
    }
}
