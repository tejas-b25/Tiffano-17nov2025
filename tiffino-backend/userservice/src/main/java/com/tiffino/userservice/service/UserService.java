package com.tiffino.userservice.service;

import com.tiffino.userservice.dto.LoginRequest;
import com.tiffino.userservice.dto.LoginResponse;
import com.tiffino.userservice.dto.UserRequest;
import com.tiffino.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);
    UserResponse getUser(Long id);
    UserResponse updateUser(Long id, UserRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserByEmail(String email);
    UserResponse getUserByPhoneNumber(String phoneNumber);
    LoginResponse login(LoginRequest request);
    String deleteUser(Long id);
    LoginResponse adminLogin(LoginRequest request);
}
