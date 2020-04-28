package com.roomfix.api.auth.controller;

import com.roomfix.api.auth.dto.LoginRequestDto;
import com.roomfix.api.auth.dto.LoginResponseDto;
import com.roomfix.api.auth.dto.UserCreateDto;
import com.roomfix.api.common.exceptionhandling.exception.BadRequestException;
import com.roomfix.api.user.entity.User;
import com.roomfix.api.user.entity.UserRole;
import com.roomfix.api.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/account")
public class AuthenticationController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authManager;
    private ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(UserRepository userRepository, AuthenticationManager authUserService,  PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.authManager = authUserService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public LoginResponseDto register(@RequestBody UserCreateDto userCreateDto) {
        // checking Mail format
        final Pattern pattern = Pattern.compile("^(.+)@isep.fr$");
        final Matcher matcher = pattern.matcher(userCreateDto.getMail());
        if (!matcher.matches()) {
            throw new BadRequestException("Must use an isep email address.");
        }

        // checking if user exists
        final User userExists = userRepository.findByMail(userCreateDto.getMail());
        if (userExists != null) {
            throw new BadRequestException("User with this email already exists.");
        }

        User user = new User();
        LoginResponseDto response = new LoginResponseDto();

        user.setMail(userCreateDto.getMail());
        user.setRole(UserRole.STUDENT);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));

        modelMapper.map(userRepository.save(user), response);
        return response;
    }

    @RequestMapping(value = "/check-credentials", method = RequestMethod.POST)
    public ResponseEntity<?> checkCredentials(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            this.authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(),
                    loginRequestDto.getPassword())).isAuthenticated();
            return ResponseEntity.ok("All good!");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Account and/or Credentials.");
        }
    }

}
