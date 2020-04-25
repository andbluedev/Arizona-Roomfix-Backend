package com.roomfix.api.device.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roomfix.api.failure.Failure;
import com.roomfix.api.room.Room;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "devices_categories")
@Data
public class DeviceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "devicesCategories")
    private List<Room> rooms;

    @JsonIgnore
    @OneToMany(mappedBy = "deviceCategory", fetch = FetchType.LAZY)
    private List<Failure> failures;
}
