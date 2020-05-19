package com.roomfix.api.room;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long>{
    List<Room> findAll();
    List<Room> findAllByBuildingId(long buildingId);
}
