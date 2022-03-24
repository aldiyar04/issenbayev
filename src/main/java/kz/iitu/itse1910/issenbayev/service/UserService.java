package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.response.UserResp;

public interface UserService {
    UserResp register(UserSignupReq signupReq);
    void changePassword(UserPasswdChangeReq passChangeReq);
    UserResp update(UserUpdateReq updateReq);
    void delete(Long id);
}
