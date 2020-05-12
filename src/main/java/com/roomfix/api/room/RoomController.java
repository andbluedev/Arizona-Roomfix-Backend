package com.roomfix.api.room;

import com.roomfix.api.building.Building;
import com.roomfix.api.building.BuildingRepository;
import com.roomfix.api.common.exceptionhandling.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;

    @Autowired
    public RoomController(RoomRepository roomRepository, BuildingRepository buildingRepository) {
        this.roomRepository = roomRepository;
        this.buildingRepository = buildingRepository;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Room> getAllRoom() {
        return this.roomRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Room getRoomById(@PathVariable("id") long roomId) {
        return this.roomRepository.findById(roomId).orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/{building_id}/with-failures")
    @ResponseStatus(HttpStatus.OK)
    public List<Room> getRoomsOfBuildingWithFailures(@PathVariable("building_id") long buildingId) {
        Building building = this.buildingRepository.findById(buildingId).orElseThrow(ResourceNotFoundException::new);
        return building.getRooms();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Room addRoom(@RequestBody Room newRoom, @RequestParam("buildingId") long buildingId) {
        Building building = this.buildingRepository.findById(buildingId).orElseThrow(ResourceNotFoundException::new);
        newRoom.setBuilding(building);
        return this.roomRepository.save(newRoom);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Room deleteRoomById(@PathVariable("id") long roomId) {
        Room roomToDelete = this.roomRepository.findById(roomId).orElseThrow(ResourceNotFoundException::new);
        this.roomRepository.delete(roomToDelete);
        return roomToDelete;
    }





}
