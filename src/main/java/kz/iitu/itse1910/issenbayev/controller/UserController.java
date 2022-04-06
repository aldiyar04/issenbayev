package kz.iitu.itse1910.issenbayev.controller;

import kz.iitu.itse1910.issenbayev.dto.user.response.UserPaginatedResp;
import kz.iitu.itse1910.issenbayev.feature.validation.CheckUserDtoRole;
import kz.iitu.itse1910.issenbayev.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserPaginatedResp> getUsers(Pageable pageable,
                                                  @RequestParam(required = false) @CheckUserDtoRole String role,
                                                  @RequestParam(required = false) Boolean isAssignedToProject) {
        UserPaginatedResp resp = userService.getUsers(pageable, role, isAssignedToProject);
        return ResponseEntity.ok().body(resp);
    }
}
