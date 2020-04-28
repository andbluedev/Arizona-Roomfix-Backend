package com.roomfix.api.auth.service;

import com.roomfix.api.user.entity.User;
import com.roomfix.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByMail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return user;
    }

    public boolean credentialsValid(String email, String password) {
        try {
            this.authenticate(email, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }




    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public String encodePassword(String passwordToEncode) {
        return  passwordEncoder.encode(passwordToEncode);
    }

}
