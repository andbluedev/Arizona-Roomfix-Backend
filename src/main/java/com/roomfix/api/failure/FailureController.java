package com.roomfix.api.failure;

import com.roomfix.api.common.exceptionhandling.exception.BadRequestException;
import com.roomfix.api.common.exceptionhandling.exception.ResourceNotFoundException;
import com.roomfix.api.device.category.DeviceCategory;
import com.roomfix.api.device.category.DeviceCategoryRepository;
import com.roomfix.api.room.Room;
import com.roomfix.api.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/failures")
public class FailureController {

    private final FailureRepository failureRepository;
    private final RoomRepository roomRepository;
    private final DeviceCategoryRepository deviceCategoryRepository;

    @Autowired
    public FailureController(FailureRepository failureRepository, RoomRepository roomRepository, DeviceCategoryRepository deviceCategoryRepository) {
        this.failureRepository = failureRepository;
        this.roomRepository = roomRepository;
        this.deviceCategoryRepository = deviceCategoryRepository;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Failure> getAllFailure() {
        return this.failureRepository.findAll();
    }

    @GetMapping("/room{roomId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Failure> getFailuresByRoom(@PathVariable("roomId") long roomId) {

        Room room = this.roomRepository.findById(roomId).orElseThrow(ResourceNotFoundException::new);
        List<Failure> listFailures = room.getFailures();
        listFailures.removeIf(failure -> failure.getState() == FailureState.CLOSED || failure.getState() == FailureState.USELESS);

        return listFailures;
    }




    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Failure getFailureById(@PathVariable("id") long failureId) {
        return this.failureRepository.findById(failureId).orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Failure addFailure(@RequestBody Failure newFailure, @RequestParam("roomId") long roomId, @RequestParam("deviceCategoryId") long deviceCategoryId ) {
        if (roomId != 0) {
            Room room = this.roomRepository.findById(roomId).orElseThrow(ResourceNotFoundException::new);
            newFailure.setRoom(room);

        }
        if (deviceCategoryId != 0) {
            DeviceCategory deviceCategory = this.deviceCategoryRepository.findById(deviceCategoryId).orElseThrow(ResourceNotFoundException::new);
               if (newFailure.getRoom().getDevicesCategories().contains(deviceCategory)){   //check if the device category is compatible with the room
                newFailure.setDeviceCategory(deviceCategory);
           } else throw new BadRequestException();
        }


        if (newFailure.getTitle().isEmpty()){
            newFailure.setTitle("Untitled Failure #"+ newFailure.getId());
        }
         else if (newFailure.getTitle().length()>=101){
            newFailure.setTitle(newFailure.getTitle().substring(0,100));
        }



        return this.failureRepository.save(newFailure);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Failure deleteFailureById(@PathVariable("id") long failureId) {
        Failure failureToDelete = this.failureRepository.findById(failureId).orElseThrow(ResourceNotFoundException::new);
        this.failureRepository.delete(failureToDelete);
        return failureToDelete;
    }
}
