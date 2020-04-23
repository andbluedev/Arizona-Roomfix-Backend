package com.roomfix.api.failure;

import com.roomfix.api.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="failures")
@Getter
@Setter
@NoArgsConstructor
public class Failure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private FailureState state = FailureState.UN_RESOLVED;

    @CreatedDate
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime endedAt;

    @ManyToOne
    private Room room;
}
