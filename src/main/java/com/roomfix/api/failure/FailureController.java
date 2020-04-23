package com.roomfix.api.failure;

import com.roomfix.api.common.exceptionhandling.exception.ResourceNotFoundException;
import com.roomfix.api.room.Room;
import com.roomfix.api.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/failures")
public class FailureController {

    private final FailureRepository failureRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public FailureController(FailureRepository failureRepository, RoomRepository roomRepository) {
        this.failureRepository = failureRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Failure> getAllFailure() {
        return this.failureRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Failure getFailureById(@PathVariable("id") long failureId) {
        return this.failureRepository.findById(failureId).orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Failure addFailure(@RequestBody Failure newFailure, @RequestParam("roomId") long roomId) {
        Room room = this.roomRepository.findById(roomId).orElseThrow(ResourceNotFoundException::new);
        newFailure.setRoom(room);
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
