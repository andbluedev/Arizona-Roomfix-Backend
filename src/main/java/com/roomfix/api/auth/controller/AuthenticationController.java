package com.roomfix.api.auth.controller;

import com.roomfix.api.auth.dto.LoginRequestDto;
import com.roomfix.api.auth.service.AuthenticationService;
import com.roomfix.api.auth.dto.UserCreateDto;
import com.roomfix.api.common.exceptionhandling.exception.BadRequestException;
import com.roomfix.api.user.entity.User;
import com.roomfix.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/account")
public class AuthenticationController {
    private UserRepository userRepository;
    private AuthenticationService authUserService;

    @Autowired
    public AuthenticationController(UserRepository userRepository, AuthenticationService authUserService) {
        this.userRepository = userRepository;
        this.authUserService = authUserService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User register(@RequestBody UserCreateDto userCreateDto) {
        final User userExists = userRepository.findByMail(userCreateDto.getMail());
        final Pattern pattern = Pattern.compile("^(.+)@isep.fr$");
        final Matcher matcher = pattern.matcher(userCreateDto.getMail());
        if (userExists != null) {
            throw new BadRequestException("User with this email already exists.");
        }
        if (!matcher.matches()) {
            throw new BadRequestException("Must use an isep email address.");
        }

        User user = new User();
        user.setMail(userCreateDto.getMail());
        user.setPassword(authUserService.encodePassword(userCreateDto.getPassword()));

        return userRepository.save(user);
    }


    @RequestMapping(value = "/check-creds", method = RequestMethod.POST)
    public boolean checkCredentials(@RequestBody LoginRequestDto loginRequestDto) {
        return this.authUserService.credentialsValid(loginRequestDto.getUsername(), loginRequestDto.getPassword());
    }
}
