package com.roomfix.api.user.repository;

import java.util.List;


import com.roomfix.api.user.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>{
    List<User> findAll();
    User findByMail(String email);
}
