package com.example.enrollingservice.serviceImpl;

import com.example.enrollingservice.dto.UserDetailsDto;
import com.example.enrollingservice.exception.InvalidTokenException;
import com.example.enrollingservice.service.AuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Service
public class AuthServiceImpl implements AuthService {


    private final RestTemplate restTemplate;


    public AuthServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDetailsDto getUserDetailsFromAuthService(String serviceUrl, String token) {
        HttpHeaders headers = new HttpHeaders();
        if (!token.startsWith("Bearer ")) {
            token = "Bearer " + token;
        }
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try{
            ResponseEntity<UserDetailsDto> response = restTemplate.exchange(
                    serviceUrl,
                    HttpMethod.GET,
                    entity,
                    UserDetailsDto.class
            );
            return response.getBody();

        }catch (HttpClientErrorException ex){
            throw new InvalidTokenException("token is not valid");
        }

    }

    @Override
    public Boolean isAdmin(String serviceUrl, String token) {

        UserDetailsDto userDetailsDto = this.getUserDetailsFromAuthService(serviceUrl,token);
        return Objects.equals(userDetailsDto.getRole(), "ROLE_ADMIN");
    }

    @Override
    public Boolean isTeacher(String serviceUrl, String token) {
        UserDetailsDto userDetailsDto = this.getUserDetailsFromAuthService(serviceUrl,token);
        return Objects.equals(userDetailsDto.getRole(), "ROLE_TEACHER");
    }

    @Override
    public Boolean isStudent(UserDetailsDto userDetailsDto) {
        return Objects.equals(userDetailsDto.getRole(), "ROLE_STUDENT");
    }
}
