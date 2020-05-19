package com.roomfix.api.failure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roomfix.api.device.category.DeviceCategory;
import com.roomfix.api.room.Room;
import com.roomfix.api.user.entity.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToMany
    @JoinTable(name = "upvote",
            joinColumns = @JoinColumn(name = "failure_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> upvoters;

    public void addUpvoter(User upvoter) {
        if (!this.upvoters.contains(upvoter)) this.upvoters.add(upvoter);
    }

    public void removeUpvoter(User upvoter) {
        this.upvoters.remove(upvoter);
    }
}
