package com.roomfix.api.device.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roomfix.api.building.Building;
import com.roomfix.api.failure.Failure;
import com.roomfix.api.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="devices_categories")
@Getter
@Setter
@NoArgsConstructor
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
