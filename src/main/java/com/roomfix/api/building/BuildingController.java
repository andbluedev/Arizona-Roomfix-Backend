package com.roomfix.api.building;

import com.roomfix.api.common.exceptionhandling.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buildings")
public class BuildingController {

    private final BuildingRepository buildingRepository;

    @Autowired
    public BuildingController(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Building> getAllBuilding() {
        return this.buildingRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Building getBuildingById(@PathVariable("id") long buildingId) {
        return this.buildingRepository.findById(buildingId).orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Building addBuilding(@RequestBody Building newBuilding) {
        return this.buildingRepository.save(newBuilding);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Building deleteBuildingById(@PathVariable("id") long buildingId) {
        Building buildingToDelete = this.buildingRepository.findById(buildingId).orElseThrow(ResourceNotFoundException::new);
        this.buildingRepository.delete(buildingToDelete);
        return buildingToDelete;
    }

}
