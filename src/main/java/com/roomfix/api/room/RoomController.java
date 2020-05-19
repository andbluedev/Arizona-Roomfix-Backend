package com.roomfix.api.room;

import com.roomfix.api.building.Building;
import com.roomfix.api.building.BuildingRepository;
import com.roomfix.api.common.exceptionhandling.exception.BadRequestException;
import com.roomfix.api.common.exceptionhandling.exception.ResourceNotFoundException;
import com.roomfix.api.device.category.DeviceCategory;
import com.roomfix.api.device.category.DeviceCategoryRepository;
import com.roomfix.api.failure.Failure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;
    private final DeviceCategoryRepository deviceCategoryRepository;

    @Autowired
    public RoomController(RoomRepository roomRepository, BuildingRepository buildingRepository, DeviceCategoryRepository deviceCategoryRepository) {
        this.roomRepository = roomRepository;
        this.buildingRepository = buildingRepository;
        this.deviceCategoryRepository = deviceCategoryRepository;

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

    @PostMapping("/{id}/newdevice")
    @ResponseStatus(HttpStatus.CREATED)
    public Room addDeviceCategoryToRoom(@RequestParam("deviceCategoryId") long deviceCategoryId,@PathVariable("id") long roomId){
        Room room = this.roomRepository.findById(roomId).orElseThrow(ResourceNotFoundException::new);
        List<DeviceCategory> deviceCategoryList = room.getDevicesCategories();
        DeviceCategory deviceCategory = this.deviceCategoryRepository.findById(deviceCategoryId).orElseThrow(ResourceNotFoundException::new);
        if (!deviceCategoryList.contains(deviceCategory)){
            deviceCategoryList.add(deviceCategory);
            room.setDevicesCategories(deviceCategoryList);
            return this.roomRepository.save(room);

        } else throw new BadRequestException("Room already contains this device category");





    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Room deleteRoomById(@PathVariable("id") long roomId) {
        Room roomToDelete = this.roomRepository.findById(roomId).orElseThrow(ResourceNotFoundException::new);
        this.roomRepository.delete(roomToDelete);
        return roomToDelete;
    }

    @GetMapping("/{id}/device/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<DeviceCategory> getRoomDeviceCategories(@PathVariable("id") long roomId) {
        Room room = this.roomRepository.findById(roomId).orElseThrow(ResourceNotFoundException::new);
        return room.getDevicesCategories();
    }
}
