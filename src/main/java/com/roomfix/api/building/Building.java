package com.roomfix.api.building;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roomfix.api.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="buildings")
@Getter
@Setter
@NoArgsConstructor
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @CreatedDate
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY)
    private List<Room> rooms;
}
