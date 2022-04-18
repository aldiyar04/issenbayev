package kz.iitu.itse1910.issenbayev.controller;

import kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.UserFilterReq;
import kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.annotation.CompoundRequestParam;
import kz.iitu.itse1910.issenbayev.controller.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.controller.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.controller.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.controller.dto.user.response.UserPaginatedResp;
import kz.iitu.itse1910.issenbayev.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserPaginatedResp> getUsers(Pageable pageable,
                                                      @Valid @CompoundRequestParam UserFilterReq filterReq) {
        UserPaginatedResp resp = userService.getUsers(pageable,
                Optional.ofNullable(filterReq.getRole()),
                Optional.ofNullable(filterReq.getIsAssignedToProject()));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") long id) {
        UserDto user = userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserSignupReq signupReq) {
        UserDto createdUser = userService.register(signupReq);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") long id,
                                              @RequestBody UserUpdateReq updateReq) {
        UserDto updatedUser = userService.update(id, updateReq);
        return ResponseEntity.ok(updatedUser);
    }

    // TODO: uncomment when password hashing is implemented
//    @PatchMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public void changePassword(@PathVariable("id") long id,
//                               @RequestBody UserPasswdChangeReq passwdChangeReq) {
//        userService.changePassword(id, passwdChangeReq);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
