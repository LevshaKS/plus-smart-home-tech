package ru.yandex.practicum.analyzer.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sensors")
public class Sensors {

    @Id
    private String id;

    @Column(name = "hub_id")
    private String hubId;
}
