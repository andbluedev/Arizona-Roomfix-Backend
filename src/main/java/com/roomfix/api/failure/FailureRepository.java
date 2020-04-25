package com.roomfix.api.failure;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FailureRepository extends CrudRepository<Failure, Long> {
    List<Failure> findAll();
}

