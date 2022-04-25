package kz.iitu.itse1910.issenbayev.controller;

import kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.UserFilterReq;
import kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.annotation.CompoundRequestParam;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserPaginatedResp;
import kz.iitu.itse1910.issenbayev.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserPaginatedResp getUsers(Pageable pageable,
                                                      @Valid @CompoundRequestParam UserFilterReq filterReq) {
        return userService.getUsers(pageable,
                Optional.ofNullable(filterReq.getRole()),
                Optional.ofNullable(filterReq.getIsAssignedToProject()));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable("id") long id) {
        return userService.getById(id);
    }

    @PostMapping
    public UserDto registerUser(@Valid @RequestBody UserSignupReq signupReq) {
        return userService.register(signupReq);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") long id,
                                              @RequestBody UserUpdateReq updateReq) {
        return userService.update(id, updateReq);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@PathVariable("id") long id,
                               @RequestBody UserPasswdChangeReq passwdChangeReq) {
        userService.changePassword(id, passwdChangeReq);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
    }
}
