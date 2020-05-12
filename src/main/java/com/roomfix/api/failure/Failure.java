package com.roomfix.api.failure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roomfix.api.device.category.DeviceCategory;
import com.roomfix.api.room.Room;
import com.roomfix.api.user.entity.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "failures")
@Data
public class Failure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private FailureState state = FailureState.UN_RESOLVED;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime endedAt;

    @JsonIgnore
    @ManyToOne
    private Room room;

    @ManyToOne
    private DeviceCategory deviceCategory;

    @ManyToOne
    private User user;
}
