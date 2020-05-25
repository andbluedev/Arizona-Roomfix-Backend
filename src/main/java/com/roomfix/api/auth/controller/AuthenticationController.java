package com.roomfix.api.auth.controller;

import com.roomfix.api.auth.component.JwtUtil;
import com.roomfix.api.auth.dto.LoginRequestDto;
import com.roomfix.api.auth.dto.LoginResponseDto;
import com.roomfix.api.auth.dto.UserCreateDto;
import com.roomfix.api.common.exceptionhandling.exception.BadRequestException;
import com.roomfix.api.common.exceptionhandling.exception.InternalServerErrorException;
import com.roomfix.api.common.exceptionhandling.exception.UnauthorizedException;
import com.roomfix.api.user.entity.User;
import com.roomfix.api.user.entity.UserRole;
import lombok.var;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import com.roomfix.api.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/account")
public class AuthenticationController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authManager;
    private JwtUtil jwtTokenUtil;
    private ModelMapper modelMapper;
    private JavaMailSender emailSender;
    private TemplateEngine templateEngine;

    @Autowired
    public AuthenticationController(UserRepository userRepository, AuthenticationManager authUserService,
                                    PasswordEncoder passwordEncoder, ModelMapper modelMapper, JwtUtil jwtTokenUtil, JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.userRepository = userRepository;
        this.authManager = authUserService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    @PostMapping("/register")
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

        Map<String, Object> variables = new HashMap<>();
        variables.put("mail", user.getMail());
        final String templateFileName = "registrationEmailTemplate";
        String mailTemplate = this.templateEngine.process(templateFileName, new Context(Locale.getDefault(), variables));

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(user.getMail());
            helper.setSubject("Compte RoomFix créé");
            helper.setText(mailTemplate, true);
            this.emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        this.modelMapper.map(userRepository.save(user), response);
        return response;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            this.authenticate(loginRequestDto.getMail(), loginRequestDto.getPassword());

            final User user = userRepository.findByMail(loginRequestDto.getMail());
            final String token = jwtTokenUtil.generateToken(user);

            LoginResponseDto response = new LoginResponseDto();
            this.modelMapper.map(user, response);
            response.setToken(token);

            return response;
        } catch (DisabledException e1) {
            throw new UnauthorizedException("Account disabled");
        } catch (BadCredentialsException e2) {
            throw new UnauthorizedException("Invalid credentials");
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GetMapping("/me")
    public LoginResponseDto checkCredentials() {
        try {
            // gets the email from the JWT token in the Authorization Header
            var contextUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            final User user = userRepository.findByMail(contextUsername.toString());

            LoginResponseDto response = new LoginResponseDto();
            this.modelMapper.map(user, response);
            return response;

        } catch (Exception e) {
            throw new UnauthorizedException("Invalid Token");
        }
    }

    private void authenticate(String email, String password) throws Exception {
        this.authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)).isAuthenticated();
    }

}
