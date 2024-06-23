package com.example.enrollingservice.service;


import com.example.enrollingservice.dto.UserDetailsDto;

public interface AuthService {

    public UserDetailsDto getUserDetailsFromAuthService(String serviceUrl, String token);

    public Boolean isAdmin(String serviceUrl,String token);
    //public Boolean isTeacher(String serviceUrl,String token);

    public Boolean isTeacher(String serviceUrl,String token);

    public Boolean isStudent(UserDetailsDto userDetailsDto);

}
