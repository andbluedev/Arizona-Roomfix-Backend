package com.roomfix.api.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roomfix.api.building.Building;
import com.roomfix.api.failure.Failure;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="rooms")
@Getter
@Setter
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String number;

    @CreatedDate
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private Building building;

    @JsonIgnore
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Failure> failures;
}
