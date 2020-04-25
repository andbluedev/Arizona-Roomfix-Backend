package com.roomfix.api.room;

import com.roomfix.api.building.Building;
import com.roomfix.api.device.category.DeviceCategory;
import com.roomfix.api.failure.Failure;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String number;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private Building building;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Failure> failures;

    @ManyToMany
    @JoinTable(name = "room_device_category",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "device_category_id"))
    private List<DeviceCategory> devicesCategories;
}
