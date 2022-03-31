package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.user.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserResp;

public interface UserService {
    UserResp register(UserSignupReq signupReq);
    void changePassword(UserPasswdChangeReq passChangeReq);
    UserResp update(UserUpdateReq updateReq);
    void delete(Long id);
}
