package com.roomfix.api.user.controller;

import java.util.List;

import com.roomfix.api.common.exceptionhandling.exception.ResourceNotFoundException;
import com.roomfix.api.user.entity.User;
import com.roomfix.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUser() {
        List<User> users = this.userRepository.findAll();
        return users;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable("id") long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        return user;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User deleteUserById(@PathVariable("id") long userId) {
        User userToDelete = this.userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        this.userRepository.delete(userToDelete);
        return userToDelete;
    }
}
