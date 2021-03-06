package com.roomfix.api.device.category;

import com.roomfix.api.common.exceptionhandling.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices/categories")
public class DeviceCategoryController {

    private final DeviceCategoryRepository deviceCategoryRepository;

    @Autowired
    public DeviceCategoryController(DeviceCategoryRepository deviceCategoryRepository) {
        this.deviceCategoryRepository = deviceCategoryRepository;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<DeviceCategory> getAllDeviceCategory() {
        return this.deviceCategoryRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeviceCategory getDeviceCategoryById(@PathVariable("id") long deviceCategoryId) {
        return this.deviceCategoryRepository.findById(deviceCategoryId).orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceCategory addDeviceCategory(@RequestBody DeviceCategory newDeviceCategory) {
        return this.deviceCategoryRepository.save(newDeviceCategory);
    }

    @PutMapping("/{id}/update")
    @ResponseStatus(HttpStatus.OK)
    public DeviceCategory changeDeviceCategoryName(@RequestParam("newName") String newName, @PathVariable("id") long deviceCategoryId){
        DeviceCategory deviceCategoryToUpdate = this.deviceCategoryRepository.findById(deviceCategoryId).orElseThrow(ResourceNotFoundException::new);
        deviceCategoryToUpdate.setName(newName);
        return this.deviceCategoryRepository.save(deviceCategoryToUpdate);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeviceCategory deleteDeviceCategoryById(@PathVariable("id") long deviceCategoryId) {
        DeviceCategory deviceCategoryToDelete = this.deviceCategoryRepository.findById(deviceCategoryId).orElseThrow(ResourceNotFoundException::new);
        this.deviceCategoryRepository.delete(deviceCategoryToDelete);
        return deviceCategoryToDelete;
    }

}
