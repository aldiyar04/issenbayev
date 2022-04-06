package kz.iitu.itse1910.issenbayev.controller;

import kz.iitu.itse1910.issenbayev.dto.user.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserPaginatedResp;
import kz.iitu.itse1910.issenbayev.feature.validation.CheckUserDtoRole;
import kz.iitu.itse1910.issenbayev.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserPaginatedResp> getUsers(Pageable pageable,
                                                  @RequestParam(required = false) @CheckUserDtoRole String role,
                                                  @RequestParam(required = false, value = "is-assigned-to-project")
                                                                  Boolean isAssignedToProject) {
        UserPaginatedResp resp = userService.getUsers(pageable, role, isAssignedToProject);
        return ResponseEntity.ok().body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") long id) {
        UserDto user = userService.getById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody UserSignupReq signupReq) {
        UserDto createdUser = userService.register(signupReq);
        return ResponseEntity.ok().body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") long id,
                                              @RequestBody UserUpdateReq updateReq) {
        UserDto updatedUser = userService.update(id, updateReq);
        return ResponseEntity.ok().body(updatedUser);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@PathVariable("id") long id,
                               @RequestBody UserPasswdChangeReq passwdChangeReq) {
        userService.changePassword(id, passwdChangeReq);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
