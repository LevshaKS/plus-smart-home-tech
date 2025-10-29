package ru.yandex.practicum.analyzer.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Table (name="scenarios")
@AllArgsConstructor
@NoArgsConstructor
public class Scenarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="hub_id")
    private String hubId;

    private String name;

}
