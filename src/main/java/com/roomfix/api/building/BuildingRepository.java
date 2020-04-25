package com.roomfix.api.building;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface BuildingRepository extends CrudRepository<Building, Long>{
    List<Building> findAll();
}
