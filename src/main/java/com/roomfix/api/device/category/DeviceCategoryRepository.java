package com.roomfix.api.device.category;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceCategoryRepository extends CrudRepository<DeviceCategory, Long> {
    List<DeviceCategory> findAll();
}
