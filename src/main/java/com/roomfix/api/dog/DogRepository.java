package com.roomfix.api.dog;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

public interface DogRepository extends CrudRepository<Dog, Long>{
    List<Dog> findAll();
}

